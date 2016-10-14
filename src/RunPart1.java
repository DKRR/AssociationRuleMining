import java.io.FileNotFoundException;

/**
 * Created by VenkataRamesh on 10/13/2016.
 */
public class RunPart1 {

    public static void main(String[] args) throws FileNotFoundException {

        FrequentItemSetFinder finder = new FrequentItemSetFinder();

        System.out.println(finder.readGeneDataSet("gene_expression.csv"));

    }
}
