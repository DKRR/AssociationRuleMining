import java.util.*;

/**
 * Created by Nitish on 10/13/2016.
 */
public class AssociationRuleFinder {

    public Map<List<String>, Integer> combinationMap;
    public Map<List<String>, Integer> combinationMapReplica;
    //public Map<List<String>, List<String>> tempRuleMap;
    //public Map<List<String>, List<String>> finalRuleMap;
    //public Map<List<String>, List<String>> sanitzedTempRuleMapReplica;
    public List<List<String>> RuleList;

    double confidenceval = 0.7;

    public AssociationRuleFinder() throws Exception{

        FrequentItemSetFinder finder = new FrequentItemSetFinder();
        //this.combinationMap = finder.getCombinationMap();

        FrequentItemSetFinder freqItemFinder = new FrequentItemSetFinder();
        List<List<String>> transItemList = freqItemFinder.readGeneDataSet("gene_expression.csv");
        //freqItemFinder.frequentItemSetGenerator(0.3, transItemList);

        this.combinationMap = finder.frequentItemSetGenerator(0.7, transItemList);


        //this.sanitzedTempRuleMapReplica = new HashMap<List<String>, List<String>>();
        //this.combinationMapReplica = finder.getCombinationMap();
        //this.combinationMapReplica = new HashMap<List<String>, Integer>();
        this.RuleList = new ArrayList<List<String>>();

    }

//    public void getAllPossibleAssociationRules(Map<List<String>, Integer> CombinationMap, double Confidence) {
    public void getAllPossibleAssociationRules() {

        Iterator<Map.Entry<List<String>, Integer>> iterator = combinationMap.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry<List<String>, Integer> entry = iterator.next();
            List<String> key = entry.getKey();
            Integer keysup = entry.getValue();

            List<List<String>> generatedsubsetlist = generateSubset(key);
            List<List<String>> generatedsubsetlistcopy = new ArrayList<List<String>>(generatedsubsetlist);
            //List<List<String>> generatedsubsetlistcopy = generateSubset(key);

            Iterator<List<String>> listit = generatedsubsetlist.iterator();

            while (listit.hasNext())
            {
                List<String> itemlist = listit.next();
                /*String ps = "";

                for(String s : itemlist)
                {
                    ps = ps + s + " ";
                }

                System.out.println("subset " + ps);*/

                if(itemlist.size()!=0 && itemlist.size()!=key.size())
                {
                    Iterator<List<String>> listitcopy = generatedsubsetlistcopy.iterator();

                    while(listitcopy.hasNext())
                    {
                        List<String> itemlistcopy = listitcopy.next();
                        if(itemlistcopy.size()!=0 && itemlistcopy.size()!=key.size())
                        {
                            if(checkRuleValid(itemlist,itemlistcopy))
                            {
                                itemlist.add("->");
                                itemlist.addAll(itemlistcopy);

                                if(!RuleList.contains(itemlist))
                                {
                                    RuleList.add(itemlist);
                                }
                            }
                            if(checkRuleValid(itemlistcopy,itemlist))
                            {
                                itemlistcopy.add("->");
                                itemlistcopy.addAll(itemlist);

                                if(!RuleList.contains(itemlistcopy))
                                {
                                    RuleList.add(itemlistcopy);
                                }
                            }
                        }
                    }


                }
            }



            /*Iterator<Map.Entry<List<String>, Integer>> replicaIterator = combinationMapReplica.entrySet().iterator();

//            System.out.printf("Key : %s and Value: %s %n", entry.getKey(), entry.getValue());

            while (replicaIterator.hasNext()) {

                Map.Entry<List<String>, Integer> replicaEntry = replicaIterator.next();
                List<String> replicaKey = replicaEntry.getKey();
                Integer replicaKeysup = replicaEntry.getValue();

                tempRuleMap.put(key, replicaKey);
                //System.out.println(key + "--" + replicaKey);

            }*/

        }
        /*Iterator<Map.Entry<List<String>,List<String>>> temprulemapprintIterator = tempRuleMap.entrySet().iterator();

        System.out.println("The set of temporary rule map association rules generated are: ");

        while (temprulemapprintIterator.hasNext())
        {
            Map.Entry<List<String>,List<String>> printEntrySet = temprulemapprintIterator.next();
            List<String> printlhs = printEntrySet.getKey();
            List<String> printrhs = printEntrySet.getValue();

            String lhsString = "";
            String rhsString = "";

            for(String s : printlhs)
            {
                lhsString += s + "\t";
            }
            for(String s : printrhs)
            {
                rhsString += s + "\t";
            }
            System.out.println(lhsString + "->  " + rhsString);
        }*/

        //santizedTempRuleMap();

        //finalRuleGenerator();

        /*Iterator<Map.Entry<List<String>,List<String>>> printIterator = finalRuleMap.entrySet().iterator();

        System.out.println("The set of association rules generated are: ");

        while (printIterator.hasNext())
        {
            Map.Entry<List<String>,List<String>> printEntrySet = printIterator.next();
            List<String> printlhs = printEntrySet.getKey();
            List<String> printrhs = printEntrySet.getValue();

            String lhsString = "";
            String rhsString = "";

            for(String s : printlhs)
            {
                lhsString += s + "\t";
            }
            for(String s : printrhs)
            {
                rhsString += s + "\t";
            }
            System.out.println(lhsString + "->  " + rhsString);
        }*/

        System.out.println(RuleList);
    }

    /*public void santizedTempRuleMap() {

        Iterator<Map.Entry<List<String>, List<String>>> templistiterator = tempRuleMap.entrySet().iterator();
        //Set<Map.Entry<List<String>,List<String>>> entrySet = tempRuleMap.entrySet();

        while (templistiterator.hasNext())
        {
            int flag =0;
            Map.Entry<List<String>,List<String>> templistentry = templistiterator.next();
            List<String> lhs = templistentry.getKey();
            List<String> rhs = templistentry.getValue();

            for(String lhsitem : lhs)
            {
                if(rhs.contains(lhsitem))
                {
                    flag = 1;
                    break;
                }
            }

            if(flag == 0)
            {
                sanitzedTempRuleMapReplica.put(lhs,rhs);
                sanitzedTempRuleMapReplica.put(rhs,lhs);
            }

        }

    }*/

    public double findconfidence(List<String> lhsstring,List<String> rhsstring)
    {
        int lhssup = combinationMap.get(lhsstring);
        int rhssup = combinationMap.get(rhsstring);

        List<String> tempunionlist = new ArrayList<String>(lhsstring);
        tempunionlist.addAll(rhsstring);

        Collections.sort(tempunionlist);

        int unionsup = combinationMap.get(tempunionlist);

        double confval = unionsup/lhssup;

        return confval;

    }

    /*public void finalRuleGenerator()
    {
        Iterator<Map.Entry<List<String>,List<String>>> finalRuleIterator = sanitzedTempRuleMapReplica.entrySet().iterator();

        while(finalRuleIterator.hasNext())
        {
            Map.Entry<List<String>,List<String>> finalEntrySet = finalRuleIterator.next();

            List<String> finallhs = finalEntrySet.getKey();
            List<String> finalrhs = finalEntrySet.getValue();

            double finalconf = findconfidence(finallhs,finalrhs);

            if(finalconf > confidenceval)
            {
                finalRuleMap.put(finallhs,finalrhs);
            }
        }
    }*/

    public List<List<String>> generateSubset(List<String> powerset)
    {
        List<List<String>> subsets = new ArrayList<List<String>>();
        if(powerset.isEmpty())
        {
            subsets.add(new ArrayList<String>());
            return subsets;
        }

        String head = powerset.get(0);
        List<String> rest = new ArrayList<String>(powerset.subList(1,powerset.size()));
        for(List<String> sublist : generateSubset(rest))
        {
            List<String> newlist = new ArrayList<String>();
            newlist.add(head);
            newlist.addAll(sublist);
            subsets.add(newlist);
            subsets.add(sublist);
        }

        return subsets;

    }

    public boolean checkRuleValid(List<String> checklhs,List<String> checkrhs)
    {
        int flag =0;

        for(String lhsitem : checklhs)
        {
            if(checkrhs.contains(lhsitem))
            {
                flag = 1;
                break;
            }
        }

        if(flag == 0)
        {
            double confvalue = findconfidence(checklhs,checkrhs);
            if(confvalue > confidenceval)
            {
                return true;
            }
        }

        return false;
    }

    public Map<List<String>,Integer> testDataMap()
    {
        Map<List<String>,Integer> testMap = new HashMap<List<String>, Integer>();

        List<String> testList1 = new ArrayList<String>();
        List<String> testList2 = new ArrayList<String>();
        List<String> testList3 = new ArrayList<String>();
        List<String> testList4 = new ArrayList<String>();
        List<String> testList5 = new ArrayList<String>();
        List<String> testList6 = new ArrayList<String>();
        List<String> testList7 = new ArrayList<String>();
        List<String> testList8 = new ArrayList<String>();
        List<String> testList9 = new ArrayList<String>();
        List<String> testList10 = new ArrayList<String>();
        List<String> testList11 = new ArrayList<String>();
        List<String> testList12 = new ArrayList<String>();
        List<String> testList13 = new ArrayList<String>();

        testList1.add("1");
        testList2.add("2");
        testList3.add("3");
        testList4.add("4");
        testList5.add("5");

        testList6.add("1");
        testList6.add("2");

        testList7.add("1");
        testList7.add("3");

        testList8.add("1");
        testList8.add("5");

        testList9.add("2");
        testList9.add("3");

        testList10.add("2");
        testList10.add("4");

        testList11.add("2");
        testList11.add("5");

        testList12.add("1");
        testList12.add("2");
        testList12.add("3");

        testList13.add("1");
        testList13.add("2");
        testList13.add("5");


        testMap.put(testList1, 6);
        testMap.put(testList2, 7);
        testMap.put(testList3, 6);
        testMap.put(testList4, 2);
        testMap.put(testList5, 2);

        testMap.put(testList6,4);
        testMap.put(testList7,4);
        testMap.put(testList8,2);
        testMap.put(testList9,4);
        testMap.put(testList10,2);
        testMap.put(testList11,2);

        testMap.put(testList12,2);
        testMap.put(testList13,2);

        return testMap;
    }

}
