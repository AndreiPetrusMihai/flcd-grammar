package grammar;

import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Grammar {

    List<String> terminals;
    List<String> nonTerminals;
    List<String> operators = new ArrayList<>(List.of("/", "*", "+", "-", "<", ">", "<=", "==", "!=", ">=", "="));
    List<String> separators = new ArrayList<>(List.of("(", ")", "{", "}", ";", "###"));
    List<String> reservedWords = new ArrayList<>(List.of("array", "if", "else", "while", "for", "read", "write", "int", "char", "string", "float"));


    String EPS = "eps";

    LinkedHashMap<String, List<Pair<Integer,List<String>>>> productions = new LinkedHashMap<>();

    LinkedHashMap<String, List<List<String>>> firstTable = new LinkedHashMap<>();
    LinkedHashMap<String, List<String>> lastFirstIteration = new LinkedHashMap<>();

    LinkedHashMap<String,List<List<String>>> followTable = new LinkedHashMap<>();
    LinkedHashMap<String,List<String>> lastFollowIteration = new LinkedHashMap<>();
    LinkedHashMap<String, LinkedHashMap<String, List<Pair<Integer,List<String>>>>> ll1Table = new LinkedHashMap<>();


    String filePath;

    public Grammar(String filePath, boolean interactiveMode) {
        this.filePath = filePath;
        init();
        generateFIRST();
        generateFollow();
        generateLL1();
        if (interactiveMode)
            start();
    }

    private void printOptions() {
        System.out.println("0.Exit");
        System.out.println("1.Set of terminals");
        System.out.println("2.Set of non terminals");
        System.out.println("3.Set of productions");
        System.out.println("4.Productions for a non-terminal");
        System.out.println("5.First iteration for non-terminals");
        System.out.println("6.Print LL1 table");

    }

    private void printProductions() {
        for (String nonTerminal : productions.keySet()) {
            List<Pair<Integer,List<String>>> particularProductions = productions.get(nonTerminal);
            System.out.println("Non-Terminal: " + nonTerminal);
            if (particularProductions != null) {
                for (Pair<Integer,List<String>> production : particularProductions) {
                    System.out.println(production.getValue());
                }
            }
        }
    }

    private void askForIterationAndPrintFirstContent(Scanner scanner) {
        int maxIteration = firstTable.get(nonTerminals.get(0)).size();
        System.out.println("Provide the iteration we should get. It should be smaller than " + maxIteration + ":");
        int iteration = Integer.parseInt(scanner.nextLine());

        if (maxIteration <= iteration) {
            System.out.println("Iteration is too big");
            return;
        }
        printFIRSTIteration(iteration);
    }

    private void askForAndPrintProductionsForTerminal(Scanner scanner) {
        System.out.println("Provide the non terminal we should check for: ");
        String userProduction = scanner.nextLine();

        List<Pair<Integer,List<String>>> particularProductions = productions.get(userProduction);

        if (particularProductions != null) {
            for (Pair<Integer,List<String>> production : particularProductions) {
                System.out.println(production.getValue().toString());
            }
        }
    }

    private void unwind(ArrayList<Integer> _productions){
        for(var p : this.productions.keySet())
            for(var x : this.productions.get(p)){
                for(var prodNumber : _productions)
                    if(Objects.equals(x.getKey(), prodNumber)){
                        var data = x.getValue();
                        for (int i = 0; i < data.size(); i++) {
                            if(this.nonTerminals.contains(data.get(i))){
                               x.getValue().set(i, x.getValue().get(prodNumber));
                        }

                        }
                    }
            }
    }



    private void start() {
        Scanner scanner = new Scanner(System.in);
        boolean shouldStop = false;
        while (true) {
            printOptions();
            int option;
            try {
                String userInput = scanner.nextLine();
                option = Integer.parseInt(userInput);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input");
                continue;
            }
            switch (option) {
                case 0 -> shouldStop = true;
                case 1 -> System.out.println(this.terminals.toString());
                case 2 -> System.out.println(this.nonTerminals.toString());
                case 3 -> printProductions();
                case 4 -> askForAndPrintProductionsForTerminal(scanner);
                case 5 -> askForIterationAndPrintFirstContent(scanner);
                case 6 -> printLL1Table();

                default -> System.out.println("Option doesn't match a command.");
            }
            System.out.println();
            if (shouldStop) break;
        }
    }

    private void printLL1Table(){
        for(String k1: ll1Table.keySet()){
            for(String k2: ll1Table.get(k1).keySet()){
                List<Pair<Integer,List<String>>> tableValues = ll1Table.get(k1).get(k2);
                if(tableValues.size() > 0){
                    System.out.println(k1 + " " + k2 + ":");
                    for(Pair<Integer,List<String>> entry : tableValues){
                        System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                    }
                }
            }
        }
    }

    private void unpackAlphabet() {
        //This will allow us to add a...z,A...Z,0...9 to the terminals
        List<String> unpackedTerminals = new ArrayList();
        for (String terminal : terminals) {
            if (terminal.equals("a...z")) {
                for (int c = 97; c <= 122; c++) {
                    char ch = (char) c;
                    unpackedTerminals.add(String.valueOf(ch));
                }
                continue;
            }
            if (terminal.equals("A...Z")) {
                for (int c = 65; c <= 90; c++) {
                    char ch = (char) c;
                    unpackedTerminals.add(String.valueOf(ch));
                }
                continue;
            }
            if (terminal.equals("0...9")) {
                for (int n = 0; n <= 9; n++) {
                    unpackedTerminals.add(String.valueOf(n));
                }
                continue;
            }
            if (terminal.equals("operators")) {
                unpackedTerminals.addAll(operators);
                continue;
            }
            if (terminal.equals("separators")) {
                unpackedTerminals.addAll(separators);
                continue;
            }
            if (terminal.equals("reservedWords")) {
                unpackedTerminals.addAll(reservedWords);
                continue;
            }
            unpackedTerminals.add(terminal);
        }
        terminals = unpackedTerminals;
    }

    private void init() {
        java.util.Scanner scanner;

        File file = new File(filePath);
        try {
            scanner = new java.util.Scanner(file);

            String terminalsLine = scanner.nextLine();
            this.terminals = Arrays.stream(terminalsLine.split(",")).toList();

            unpackAlphabet();

            String nonTerminalsLine = scanner.nextLine();
            this.nonTerminals = Arrays.stream(nonTerminalsLine.split(",")).toList();
            Integer productionIndex = 0;
            while (scanner.hasNextLine()) {
                String production = scanner.nextLine();

                String[] productionElements = production.split(":");

                //Here we turn into a list what is before the ":", resulting in a list of not-terminals/terminals
                String productionNonTerminals = productionElements[0].trim();
                List<String> productionNonTerminalsList = Arrays.stream(productionNonTerminals.split("\\|")).map(String::trim).toList();

                //Here we turn into a list what is after the ":", resulting in a list of productions
                String productionResults = productionElements[1];
                List<String> productionResultsList = Arrays.stream(productionResults.split("\\|")).map(String::trim).toList();

                //We load the productions like this in case we have an ambiguous grammar.
                //If the nonTerminal var starts with a lowercase letter or productionNonTerminalsList
                //has a length other than one, we know for sure that we don't have a context-free grammar
                for (String nonTerminal : productionNonTerminalsList) {
                    productionIndex++;
                    List<Pair<Integer,List<String>>> existingProductions = productions.get(nonTerminal);
                    //Here we discompose every production results in order to know the members
                    //Ex a S b | b b A will result in the following: [[a,S,b],[b,b,A]]
                    Integer finalProductionIndex = productionIndex;
                    List<Pair<Integer,List<String>>> processedProduction = productionResultsList.stream().map((prodRes) -> new Pair<>(finalProductionIndex,Arrays.stream(prodRes.split(" ")).toList().stream().map(String::trim).toList())).toList();
                    if (existingProductions != null) {
                        //Merging the lists needs to be done like this because of the immutability.
                        List<Pair<Integer,List<String>>> newProductionsList = new ArrayList<>();
                        newProductionsList.addAll(existingProductions);

                        newProductionsList.addAll(processedProduction);
                        productions.put(nonTerminal, newProductionsList);
                    } else {
                        productions.put(nonTerminal, processedProduction);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initFirstTable() {
        firstTable = new LinkedHashMap<>();
        for (String nonTerminal : nonTerminals) {
            firstTable.put(nonTerminal, new ArrayList<>());
        }
    }

    private void initFollowTable(){
        followTable = new LinkedHashMap<>();
        for(String nonTerminal : nonTerminals)
            followTable.put(nonTerminal,new ArrayList<>());
    }

    private void initNewFirstIteration() {
        for (String nonTerminal : nonTerminals) {
            List<List<String>> currentList = firstTable.get(nonTerminal);
            currentList.add(new ArrayList<>());
        }
    }

    private void saveLastFIRSTIteration() {
        int iterationCount = firstTable.get(nonTerminals.get(0)).size();
        for (String key : firstTable.keySet()) {
            lastFirstIteration.put(key, new ArrayList<>(firstTable.get(key).get(iterationCount - 1)));
        }
    }

    private void generateFollow() {
        try {
            initFollowTable();
            File file = new File(filePath);
            Scanner reader = new Scanner(file);
            reader.nextLine();
            reader.nextLine();
            while(reader.hasNextLine()){
                var currentLine = reader.nextLine();
                var rightData = currentLine.split(":")[1];
                List<String> productionResultsList = Arrays.stream(rightData.split("\\|")).map(String::trim).toList();
                for(int i = 0 ; i < productionResultsList.size(); i++)
                {
                    var data = productionResultsList.get(i).split(" ");
                    for (int j = 0; j < data.length; j++) {
                        if(data[j].charAt(0) == '\''){
                            var trimmed = data[j].substring(1,data[j].length()-1);
//                            System.out.println(trimmed);
                                if(!this.terminals.contains(trimmed))
                                    if(data.length>1 && j+1<data.length)
                                        if(this.terminals.contains(data[j+1]))
                                            this.followTable.get(data[j]).get(0).add(data[j+1]);
                                        else
                                            this.followTable.put(data[j],firstTable.get(data[j]));
                        }
                        else{
                            if(!this.terminals.contains(data[j]))
                                if(data.length>1 && j+1<data.length)
                                    if(this.terminals.contains(data[j+1]))
                                        this.followTable.get(data[j]).get(0).add(data[j+1]);
                                    else
                                        this.followTable.put(data[j],firstTable.get(data[j]));
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        for(var x : this.nonTerminals)
            System.out.println(x + "->" +followTable.get(x));
    }

    private void LL1Parsing(String productionString){

    }
    private void generateFIRST() {
        initFirstTable();
        boolean differencesFound = true;
        int currentIteration = 0;

        while (differencesFound) {
            differencesFound = false;
            initNewFirstIteration();
            for (String nonTerminal : productions.keySet()) {

                List<String> newFirstList = firstTable.get(nonTerminal).get(currentIteration);

                //We initialize it with the last iteration values.
                if (currentIteration > 0) {
                    newFirstList.addAll(new ArrayList<>(firstTable.get(nonTerminal)
                            .get(currentIteration - 1)));
                }

                for (Pair<Integer,List<String>> production : productions.get(nonTerminal)) {

                    String firstProductionElement = production.getValue().get(0);

                    if (firstProductionElement.charAt(0) == '\'') {
                        //This means the production starts with a non-terminal.
                        String strippedTerminal = firstProductionElement.substring(1, firstProductionElement.length() - 1);
                        //Remove the ''
                        if (!newFirstList.contains(strippedTerminal)) {
                            //If it doesn't contain it, we don't add it.
                            newFirstList.add(strippedTerminal);
                            differencesFound = true;
                        }
                    } else {
                        if (firstProductionElement.equals(EPS)) {
                            if (!newFirstList.contains(EPS)) {
                                //If it contains it, we don't add it.
                                newFirstList.add(EPS);
                                differencesFound = true;
                            }
                        } else {
                            //This means the production starts with a terminal.
                            if (currentIteration > 0) {
                                int productionElementIndex = 0;
                                boolean keepGoing = true;
                                String productionElement;
                                while(productionElementIndex < production.getValue().size() && keepGoing){
                                    productionElement = production.getValue().get(productionElementIndex);

                                    keepGoing = false;
                                    for (String firstValue : firstTable.get(productionElement).get(currentIteration - 1)) {
                                        if (!newFirstList.contains(firstValue)) {
                                            newFirstList.add(firstValue);
                                            differencesFound = true;
                                        }
                                        if(firstValue.equals("eps")){
                                            keepGoing = true;
                                            productionElementIndex++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            currentIteration++;
        }

        saveLastFIRSTIteration();
        generateMockFollow();
    }

    private void generateMockFollow(){
        for(String nonTerminal : nonTerminals){
            ArrayList<String> values = new ArrayList<>();
            switch (nonTerminal) {
                case "s", "b" -> {
                    values.add("eps");
                    values.add(")");
                }
                case "a", "c" -> {
                    values.add("+");
                    values.add("eps");
                    values.add(")");
                }
            }
            lastFollowIteration.put(nonTerminal, values);
        }
    }

    private void printFIRSTIteration(int iterationToPrint) {
        for (String nonTerminal : nonTerminals) {
            System.out.print(nonTerminal + ": {");
            for (String firstValue : firstTable.get(nonTerminal).get(iterationToPrint)) {
                System.out.print(" " + firstValue + " ");
            }
            System.out.println("}");
        }
    }

    private void initLL1Table() {
        ll1Table = new LinkedHashMap<>();
        for (String nonTerminal : nonTerminals) {
            LinkedHashMap<String, List<Pair<Integer,List<String>>>> terminalMap = new LinkedHashMap<>();
            for (String terminal : terminals) {
                terminalMap.put(terminal, new ArrayList<>());
            }
            terminalMap.put("$", new ArrayList<>());
            ll1Table.put(nonTerminal, terminalMap);
        }
        for (String terminal : terminals) {
            LinkedHashMap<String, List<Pair<Integer,List<String>>>> terminalMap = new LinkedHashMap<>();
            for (String subTerminal : terminals) {
                terminalMap.put(subTerminal, new ArrayList<>());
            }
            terminalMap.put("$", new ArrayList<>());
            ll1Table.put(terminal, terminalMap);
        }
        LinkedHashMap<String, List<Pair<Integer,List<String>>>> terminalMap = new LinkedHashMap<>();
        terminalMap.put("$", new ArrayList<>());
        ll1Table.put("$", terminalMap);
    }

    private void generateLL1() {
        initLL1Table();
//        System.out.println(ll1Table.keySet().toString());

        for (String key : productions.keySet()) {
            for (Pair<Integer,List<String>> production : productions.get(key)) {
                String firstElement = production.getValue().get(0);
                if (firstElement.charAt(0) == '\'') {
                    //This means it's starting with a terminal. We also strip them of ''.
                    ll1Table.get(key).get(firstElement.substring(1,firstElement.length()-1)).add(production);
                } else if (firstElement.equals("eps")) {
                    //This means it's resulting in epsilon
                    for(String value : lastFollowIteration.get(key)){
                        //If the value is epsilon, we add it to the dollar column
                        if(value.equals("eps")){
                            ll1Table.get(key).get("$").add(production);
                        } else {
                            ll1Table.get(key).get(value).add(production);
                        }
                    }
                } else {
                    //This means it's starting with a non-terminal
                    for (String value : lastFirstIteration.get(firstElement)) {
                        ll1Table.get(key).get(value).add(production);
                    }
                }
            }
        }
    }


    public void parseSequence(List<String> sequence){

    }
}