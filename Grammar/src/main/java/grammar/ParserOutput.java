package grammar;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserOutput {
    private List<Integer> productionsString = new ArrayList<>();
    private List<String> derivationsString = new ArrayList<>();
    private String outputPath;

    public ParserOutput(String outputPath) {
        this.outputPath = outputPath;
    }

    public void setProductionsString(List<Integer> productionsString) {
        this.productionsString = productionsString;
    }

    public List<Integer> getProductionsString() {
        return productionsString;
    }

    public void printProductionsString() {
        System.out.println(productionsString);
    }

    public void saveProductionsStringToFile() {
        try {
            BufferedWriter productionsWriter = new BufferedWriter(new FileWriter(outputPath));
            productionsWriter.write("Productions string:\n");
            productionsWriter.flush();

            for (Integer production : productionsString) {
                productionsWriter.write(production.toString());
            }
            productionsWriter.flush();
            productionsWriter.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
