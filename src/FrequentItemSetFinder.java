import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by VenkataRamesh on 10/13/2016.
 */
public class FrequentItemSetFinder {

    public double[] supportValues = new double[]{0.3, 0.4, 0.5, 0.6, 0.7};

    public Map<String, List<String>> readGeneDataSet(String path) throws FileNotFoundException {
        Map<String, List<String>> generatedDataSet = new TreeMap<String, List<String>>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        try {
            if (path != null) {
                File source = new File(path);
                BufferedReader reader = new BufferedReader(new FileReader(source));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] lineSplit = line.split(",");
                    List<String> items = new ArrayList<String>();
                    for (int i = 1; i < lineSplit.length; i++) {
                        items.add("G" + i + "_" + lineSplit[i]);
                    }
                    generatedDataSet.put(lineSplit[0], items);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);

        }
        return generatedDataSet;
    }

    public class GeneDataCompartor implements Comparator<Entry<String, List<String>>> {

        @Override
        public int compare(Entry<String, List<String>> o1, Entry<String, List<String>> o2) {
            return o1.getKey().compareTo(o2.getKey());
        }
    }


}
