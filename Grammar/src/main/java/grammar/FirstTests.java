package grammar;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

public class FirstTests {


    private String getFirstTableValue(HashMap<String, List<List<String>>> firstTable, String nonTerminal, int iteration, int valueIndex){
        return firstTable.get(nonTerminal).get(iteration).get(valueIndex);
    }

    @Test
    public void firstTableIteration2_g1txtFile_correctOutput() {
        String filePath = "g1test.txt";
        Grammar testGrammar = new Grammar(filePath,null,"g1test.out", false);

        HashMap<String, List<List<String>>> firstTable = testGrammar.firstTable;

        int iterationNumber = firstTable.get(firstTable.keySet().stream().toList().get(0)).size() - 1;

        for(String key : firstTable.keySet()){
            switch (key){
                case "s":
                case "a":
                    assert getFirstTableValue(firstTable, key, iterationNumber, 0).equals("(");
                    assert getFirstTableValue(firstTable, key, iterationNumber, 1).equals("int");
                    break;
                case "b":
                    assert getFirstTableValue(firstTable, key, iterationNumber, 0).equals("+");
                    assert getFirstTableValue(firstTable, key, iterationNumber, 1).equals("eps");
                    break;
                case "c":
                    assert getFirstTableValue(firstTable, key, iterationNumber, 0).equals("*");
                    assert getFirstTableValue(firstTable, key, iterationNumber, 1).equals("eps");
                    break;
                default:
                    assert false;
            }
        }
    }

    @Test
    public void firstTableIteration1_g1txtFile_correctOutput() {
        String filePath = "g1test.txt";
        Grammar testGrammar = new Grammar(filePath,null,"g1test.out", false);

        HashMap<String, List<List<String>>> firstTable = testGrammar.firstTable;


        int iterationNumber = firstTable.get(firstTable.keySet().stream().toList().get(0)).size() - 2;

        for(String key : firstTable.keySet()){
            switch (key){
                case "s":
                case "a":
                    assert getFirstTableValue(firstTable, key, iterationNumber, 0).equals("(");
                    assert getFirstTableValue(firstTable, key, iterationNumber, 1).equals("int");
                    break;
                case "b":
                    assert getFirstTableValue(firstTable, key, iterationNumber, 0).equals("+");
                    assert getFirstTableValue(firstTable, key, iterationNumber, 1).equals("eps");
                    break;
                case "c":
                    assert getFirstTableValue(firstTable, key, iterationNumber, 0).equals("*");
                    assert getFirstTableValue(firstTable, key, iterationNumber, 1).equals("eps");
                    break;
                default:
                    assert false;
            }
        }
    }

    @Test
    public void firstTableIteration0_g1txtFile_correctOutput() {
        String filePath = "g1test.txt";
        Grammar testGrammar = new Grammar(filePath,null,"g1test.out", false);

        HashMap<String, List<List<String>>> firstTable = testGrammar.firstTable;

        int iterationNumber = firstTable.get(firstTable.keySet().stream().toList().get(0)).size() - 3;

        for(String key : firstTable.keySet()){
            switch (key){
                case "s":
                    assert firstTable.get(key).get(iterationNumber).size() == 0;
                    break;
                case "a":
                    assert getFirstTableValue(firstTable, key, iterationNumber, 0).equals("(");
                    assert getFirstTableValue(firstTable, key, iterationNumber, 1).equals("int");
                    break;
                case "b":
                    assert getFirstTableValue(firstTable, key, iterationNumber, 0).equals("+");
                    assert getFirstTableValue(firstTable, key, iterationNumber, 1).equals("eps");
                    break;
                case "c":
                    assert getFirstTableValue(firstTable, key, iterationNumber, 0).equals("*");
                    assert getFirstTableValue(firstTable, key, iterationNumber, 1).equals("eps");
                    break;
                default:
                    assert false;
            }
        }
    }
}
