import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * Created by VenkataRamesh on 10/13/2016.
 */
public class RunPart1 {

    public static void main(String[] args) throws FileNotFoundException {

        FrequentItemSetFinder freqItemFinder = new FrequentItemSetFinder();
        List<List<String>> transItemList = freqItemFinder.readGeneDataSet("gene_expression.csv");
        System.out.println("Enter support count in percent value: ");
        Scanner scanner = new Scanner(System.in);
        Integer percent = null;
        try {
            percent = scanner.nextInt();
            if (percent < 1 || percent > 100) {
                System.out.println("Invalid Input, enter percent value between 1 and 100");
                return;
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid Input");
            return;
        }
        double support = (double) percent / 100;
        freqItemFinder.frequentItemSetGenerator(support, transItemList);
    }
}
