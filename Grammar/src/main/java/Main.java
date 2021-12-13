import grammar.Grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        String filePath = "g1.txt";
        Grammar grammar = new Grammar(filePath, false);
        try{
            List<String> inputSeq = new ArrayList<>(List.of("(","int",")","+","int"));
            Stack<String> startStack = new Stack<>();
            startStack.push("$");
            startStack.push("s");
            List<Integer> res = grammar.parseSequence(inputSeq, startStack);
            System.out.println(res);
        } catch(Exception ex){
            System.out.println("Invalid seq");
        }

    }
}
