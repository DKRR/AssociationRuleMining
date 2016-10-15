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

    public static Map<List<String>, Integer> allFrequenItemSets = new TreeMap<List<String>, Integer>();
    public double[] supportValues = new double[]{0.3, 0.4, 0.5, 0.6, 0.7};

    //read source file and generate itemset list
    public List<List<String>> readGeneDataSet(String path) throws FileNotFoundException {
        List<List<String>> transItemsList = new ArrayList<List<String>>();
        try {
            if (path != null) {
                File source = new File(path);
                BufferedReader reader = new BufferedReader(new FileReader(source));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] lineSplit = line.split(",");
                    List<String> transItems = new ArrayList<String>();
                    for (int i = 1; i < lineSplit.length; i++) {
                        transItems.add("G" + i + "_" + lineSplit[i]);
                    }
                    transItemsList.add(transItems);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);

        }
        return transItemsList;
    }

    public Map<List<String>, Integer> frequentItemSetGenerator(double threshold, List<List<String>> transItemsList) {
        Map<String, Integer> itemCount = new TreeMap<String, Integer>();
        for (int i = 0; i < transItemsList.size(); i++) {
            List<String> transItems = transItemsList.get(i);
            for (String transItem : transItems) {
                if (!itemCount.containsKey(transItem)) {
                    itemCount.put(transItem, 1);
                } else {
                    itemCount.put(transItem, itemCount.get(transItem) + 1);
                }
            }
        }
        Map<List<String>, Integer> candidateItems = new TreeMap<List<String>, Integer>(new Comparator<List<String>>() {
            @Override
            public int compare(List<String> o1, List<String> o2) {
                Collections.sort(o1);
                Collections.sort(o2);
                return o1.get(0).compareTo(o2.get(0));
            }
        });
        for (Entry<String, Integer> entry : itemCount.entrySet()) {
            List<String> itemSet = new ArrayList<String>();
            itemSet.add(entry.getKey());
            candidateItems.put(itemSet, entry.getValue());
        }
        int count = 1;
        System.out.println("Support is set to be " + threshold * 100 + "%");
        while (candidateItems.size() > 0) {
            System.out.println("Running length-" + count + " frequent itemset");
            Map<List<String>, Integer> frequentItemSets = null;
            if (count == 1) {
                frequentItemSets = findFrequentItemSets(threshold, candidateItems, transItemsList.size());
            } else {
                frequentItemSets = candidateItems;
            }
            allFrequenItemSets.putAll(frequentItemSets);
            System.out.println("number of length-" + count + " frequent itemset: " + frequentItemSets.size());
            candidateItems = generateNextCandidateItemSetFromPrevSet(frequentItemSets, transItemsList, threshold);
            count++;

        }
        return allFrequenItemSets;
    }


    public Map<List<String>, Integer> generateNextCandidateItemSetFromPrevSet(Map<List<String>, Integer> prevSet, List<List<String>> transItemsList, double threshold) {
        List<List<String>> prevItems = new ArrayList<List<String>>(prevSet.keySet());
        List<List<String>> combinations = new ArrayList<List<String>>();
        Map<List<String>, Integer> nextCandidateItemSet = new HashMap<List<String>, Integer>();
        for (int i = 0; i < prevItems.size(); i++) {
            List<String> set1 = prevItems.get(i);
            for (int j = i + 1; j < prevItems.size(); j++) {
                List<String> set2 = prevItems.get(j);
                int k = 0;
                while (k < set2.size()) {
                    if (!set1.contains(set2.get(k))) {
                        break;
                    }
                    k++;
                }
                if (k == set2.size() - 1) {
                    List<String> newItemSet = new ArrayList<String>();
                    newItemSet.addAll(set1.subList(0, set1.size() - 1));
                    if (!newItemSet.contains(set1.get(set1.size() - 1))) {
                        newItemSet.add(set1.get(set1.size() - 1));
                    }
                    newItemSet.add(set2.get(k));
                    int count = getCandidateItemSetCount(newItemSet, transItemsList);
                    double support = (double) count / transItemsList.size();
                    if (support >= threshold) {
                        nextCandidateItemSet.put(newItemSet, count);
                    }
                }
            }
        }

        return nextCandidateItemSet;

    }

    public int getCandidateItemSetCount(List<String> candidateItemset, List<List<String>> transItemsList) {
        int count = 0;
        Collections.sort(candidateItemset);
        for (List<String> transItemSet : transItemsList) {
            if (transItemSet.containsAll(candidateItemset)) {
                count++;
            }
        }
        return count;

    }


    public Map<List<String>, Integer> findFrequentItemSets(double threshold, Map<List<String>, Integer> candidateItemSets, int transactionSize) {
        Iterator<Entry<List<String>, Integer>> it = candidateItemSets.entrySet().iterator();
        while (it.hasNext()) {
            Entry<List<String>, Integer> candidateItemSet = it.next();
            double support = (double) candidateItemSet.getValue() / transactionSize;
            if (support < threshold) {
                it.remove();
            }
        }
        return candidateItemSets;
    }


    public class GeneCompartor implements Comparator<Entry<String, List<String>>> {

        @Override
        public int compare(Entry<String, List<String>> o1, Entry<String, List<String>> o2) {
            return o1.getKey().compareTo(o2.getKey());
        }
    }


}
