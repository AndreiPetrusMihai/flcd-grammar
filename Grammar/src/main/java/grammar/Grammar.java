package grammar;

import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Grammar {

    List<String> terminals;
    List<String> nonTerminals;

    HashMap<String, List<String>> productions = new HashMap<>();

    String filePath;

    public Grammar(String filePath) {
        this.filePath = filePath;
        init();
        start();
    }

    private void printOptions() {
        System.out.println("0.Exit");
        System.out.println("1.Set of terminals");
        System.out.println("2.Set of non terminals");
        System.out.println("3.Set of productions");
        System.out.println("4.Productions for a non-terminal");
    }

    private void printProductions(){
        for(String nonTerminal : productions.keySet()){
            List<String> particularProductions = productions.get(nonTerminal);
            System.out.println("Non-Terminal: " + nonTerminal);
            if(particularProductions != null){
                for(String production : particularProductions){
                    System.out.println(production);
                }
            }
        }
    }

    private void askForAndPrintProductionsForTerminal(Scanner scanner){
        System.out.println("Provide the non terminal we should check for: ");
        String userProduction = scanner.nextLine();

        List<String> particularProductions = productions.get(userProduction);

        if(particularProductions != null){
            for(String production : particularProductions){
                System.out.println(production);
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
                default -> System.out.println("Option doesn't match a command.");
            }
            System.out.println();
            if (shouldStop) break;
        }
    }

    private void init() {
        java.util.Scanner scanner;

        File file = new File(filePath);
        try {
            scanner = new java.util.Scanner(file);

            String nonTerminalsLine = scanner.nextLine();
            this.nonTerminals = Arrays.stream(nonTerminalsLine.split(",")).toList();

            String terminalsLine = scanner.nextLine();
            this.terminals = Arrays.stream(terminalsLine.split(",")).toList();

            while (scanner.hasNextLine()) {
                String production = scanner.nextLine();

                String[] productionElements = production.split(":");

                //Here we turn into a list what is before the ":", resulting in a list of not-terminals/terminals
                String productionNonTerminals = productionElements[0];
                String[] productionNonTerminalsList = productionNonTerminals.split("\\|");

                //Here we turn into a list what is after the ":", resulting in a list of productions
                String productionResults = productionElements[1];
                List<String> productionResultsList = List.of(productionResults.split("\\|"));

                //We load the productions like this in case we have an ambiguous grammar.
                //If the nonTerminal var starts with a lowercase letter or productionNonTerminalsList
                //has a length other than one, we know for sure that we don't have a context-free grammar
                for(String nonTerminal : productionNonTerminalsList){
                    List<String> existingProductions = productions.get(nonTerminal);

                    if(existingProductions!=null){
                        //Merging the lists needs to be done like this because of the immutability.
                        List<String> newProductionsList = new ArrayList<>();
                        newProductionsList.addAll(existingProductions);
                        newProductionsList.addAll(productionResultsList);
                        productions.put(nonTerminal, newProductionsList);
                    } else {
                        productions.put(nonTerminal, productionResultsList);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
