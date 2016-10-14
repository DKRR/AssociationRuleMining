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

    public List<String[]> readGeneDataSet(String path) throws FileNotFoundException {
        List<String[]> transItemsList = new ArrayList<String[]>();
        try {
            if (path != null) {
                File source = new File(path);
                BufferedReader reader = new BufferedReader(new FileReader(source));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] lineSplit = line.split(",");
                    String[] transItems = new String[lineSplit.length - 1];
                    for (int i = 1; i < lineSplit.length; i++) {
                        transItems[i - 1] = "G" + i + "_" + lineSplit[i];
                    }
                    transItemsList.add(transItems);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);

        }
        return transItemsList;
    }

    public Map<String, Integer> frequentItemSetGenerator(double threshold, List<String[]> transItemsList) {
        Map<String, Integer> itemCount = new TreeMap<String, Integer>();
        for (int i = 0; i < transItemsList.size(); i++) {
            String[] transItems = transItemsList.get(i);
            for (String transItem : transItems) {
                if (!itemCount.containsKey(transItem)) {
                    itemCount.put(transItem, 1);
                } else {
                    itemCount.put(transItem, itemCount.get(transItem) + 1);
                }
            }
        }
        Map<String[], Integer> candidateItems = new TreeMap<String[], Integer>(new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                Arrays.sort(o1);
                Arrays.sort(o2);
                return o1[o1.length - 1].compareTo(o2[o2.length - 1]);
            }
        });
        for (Entry<String, Integer> entry : itemCount.entrySet()) {
            candidateItems.put(new String[]{entry.getKey()}, entry.getValue());
        }
        int count = 1;
        System.out.println("Support is set to be " + threshold * 100 + "%");
        while (candidateItems.size() > 0) {
            System.out.println("Running length-" + count + " frequent itemset");
            Map<String[], Integer> frequentItemSets = null;
            if (count == 1) {
                frequentItemSets = findFrequentItemSets(threshold, candidateItems, transItemsList.size());
            } else {
                frequentItemSets = candidateItems;
            }
            System.out.println("number of length-" + count + " frequent itemset: " + frequentItemSets.size());
            candidateItems = generateNextCandidateItemSetFromPrevSet(frequentItemSets, transItemsList, threshold);
            count++;

        }
        return itemCount;
    }


    public Map<String[], Integer> generateNextCandidateItemSetFromPrevSet(Map<String[], Integer> prevSet, List<String[]> transItemsList, double threshold) {
        List<String[]> prevItems = new ArrayList<String[]>(prevSet.keySet());
        List<String[]> combinations = new ArrayList<String[]>();
        Map<String[], Integer> nextCandidateItemSet = new TreeMap<String[], Integer>(new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                Arrays.sort(o1);
                Arrays.sort(o2);
                return o1[o1.length - 1].compareTo(o2[o2.length - 1]);
            }
        });
        for (int i = 0; i < prevItems.size(); i++) {
            String[] set1 = prevItems.get(i);
            Arrays.sort(set1);
            for (int j = i + 1; j < prevItems.size(); j++) {
                String[] set2 = prevItems.get(j);
                for (int k = 0; k < set2.length; k++) {
                    if (Arrays.binarySearch(set1, set2[k]) < 0) {
                        String[] newItemSet = new String[prevItems.get(i).length + 1];
                        for (int l = 0; l < set1.length; l++) {
                            newItemSet[l] = set1[l];
                        }
                        newItemSet[newItemSet.length - 1] = set2[k];
                        int count = getCandidateItemSetCount(newItemSet, transItemsList);
                        double support = (double) count / transItemsList.size();
                        if (support >= threshold) {
                            nextCandidateItemSet.put(newItemSet, count);
                        }
                    }
                }
            }
        }
        return nextCandidateItemSet;

    }

    public int getCandidateItemSetCount(String[] candidateItemset, List<String[]> transItemsList) {
        int count = 0;
        for (String[] transItemSet : transItemsList) {
            Arrays.sort(transItemSet);
            boolean found = false;
            for (String transItem : candidateItemset) {
                if (Arrays.binarySearch(transItemSet, transItem) < 0) {
                    found = false;
                    break;
                } else {
                    found = true;
                }
            }
            if (found) count++;
        }
        return count;

    }


    public Map<String[], Integer> findFrequentItemSets(double threshold, Map<String[], Integer> candidateItemSets, int transactionSize) {
        Iterator<Entry<String[], Integer>> it = candidateItemSets.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String[], Integer> candidateItemSet = it.next();
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
