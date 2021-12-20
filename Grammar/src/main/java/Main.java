import grammar.Grammar;

public class Main {
    public static void main(String[] args) {
        String filePath = "g3.txt";
        String outPath = "g3.out";
        String seqPath = "g3seq.txt";
        new Grammar(filePath,seqPath,outPath, true);
    }
}
