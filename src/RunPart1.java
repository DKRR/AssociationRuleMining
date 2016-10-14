import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by VenkataRamesh on 10/13/2016.
 */
public class RunPart1 {

    public static void main(String[] args) throws FileNotFoundException {

        FrequentItemSetFinder freqItemFinder = new FrequentItemSetFinder();
        List<String[]> transItemList = freqItemFinder.readGeneDataSet("gene_expression.csv");
        freqItemFinder.frequentItemSetGenerator(0.2, transItemList);
    }
}
