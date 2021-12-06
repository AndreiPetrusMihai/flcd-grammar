//package grammar;
//
//import org.junit.jupiter.api.Test;
//
//import java.util.HashMap;
//import java.util.List;
//
//public class FollowTests {
//
//    private String getFollowTableValue(HashMap<String, List<List<String>>> followTable, String nonTerminal, int iteration, int valueIndex){
//        return followTable.get(nonTerminal).get(iteration).get(valueIndex);
//    }
//
//    @Test
//    public void followTableIteration5_g1txtFile_correctOutput() {
//        String filePath = "g1test.txt";
//        Grammar testGrammar = new Grammar(filePath, false);
//
//        HashMap<String, List<List<String>>> followTable = testGrammar.followTable;
//
//        int iterationNumber = followTable.get(followTable.keySet().stream().toList().get(0)).size() - 1;
//
//        for(String key : followTable.keySet()){
//            switch (key){
//                case "s":
//                case "b":
//                    assert getFollowTableValue(followTable, key, iterationNumber, 0).equals("eps");
//                    assert getFollowTableValue(followTable, key, iterationNumber, 1).equals(")");
//                    break;
//                case "a":
//                case "c":
//                    assert getFollowTableValue(followTable, key, iterationNumber, 0).equals("+");
//                    assert getFollowTableValue(followTable, key, iterationNumber, 1).equals("eps");
//                    assert getFollowTableValue(followTable, key, iterationNumber, 2).equals(")");
//                    break;
//                default:
//                    assert false;
//            }
//        }
//    }
//
//    @Test
//    public void followTableIteration1_g1txtFile_correctOutput() {
//        String filePath = "g1test.txt";
//        Grammar testGrammar = new Grammar(filePath, false);
//
//        HashMap<String, List<List<String>>> followTable = testGrammar.followTable;
//
//
//        int iterationNumber = followTable.get(followTable.keySet().stream().toList().get(0)).size() - 2;
//
//        for(String key : followTable.keySet()){
//            switch (key){
//                case "s":
//                case "a":
//                    assert getFollowTableValue(followTable, key, iterationNumber, 0).equals("(");
//                    assert getFollowTableValue(followTable, key, iterationNumber, 1).equals("int");
//                    break;
//                case "b":
//                    assert getFollowTableValue(followTable, key, iterationNumber, 0).equals("+");
//                    assert getFollowTableValue(followTable, key, iterationNumber, 1).equals("eps");
//                    break;
//                case "c":
//                    assert getFollowTableValue(followTable, key, iterationNumber, 0).equals("*");
//                    assert getFollowTableValue(followTable, key, iterationNumber, 1).equals("eps");
//                    break;
//                default:
//                    assert false;
//            }
//        }
//    }
//
//    @Test
//    public void followTableIteration0_g1txtFile_correctOutput() {
//        String filePath = "g1test.txt";
//        Grammar testGrammar = new Grammar(filePath, false);
//
//        HashMap<String, List<List<String>>> followTable = testGrammar.followTable;
//
//        int iterationNumber = followTable.get(followTable.keySet().stream().toList().get(0)).size() - 3;
//
//        for(String key : followTable.keySet()){
//            switch (key){
//                case "s":
//                    assert followTable.get(key).get(iterationNumber).size() == 0;
//                    break;
//                case "a":
//                    assert getFollowTableValue(followTable, key, iterationNumber, 0).equals("(");
//                    assert getFollowTableValue(followTable, key, iterationNumber, 1).equals("int");
//                    break;
//                case "b":
//                    assert getFollowTableValue(followTable, key, iterationNumber, 0).equals("+");
//                    assert getFollowTableValue(followTable, key, iterationNumber, 1).equals("eps");
//                    break;
//                case "c":
//                    assert getFollowTableValue(followTable, key, iterationNumber, 0).equals("*");
//                    assert getFollowTableValue(followTable, key, iterationNumber, 1).equals("eps");
//                    break;
//                default:
//                    assert false;
//            }
//        }
//    }
//}
