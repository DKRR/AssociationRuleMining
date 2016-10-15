import java.util.*;

/**
 * Created by Nitish on 10/13/2016.
 */
public class AssociationRuleFinder {

    public Map<List<String>, Integer> combinationMap;
    public List<List<String>> RuleList;
    public List<List<String>> BodyList;
    public List<List<String>> HeadList;

    double confidenceval = 0.6;

    public AssociationRuleFinder() throws Exception {

        FrequentItemSetFinder finder = new FrequentItemSetFinder();

        FrequentItemSetFinder freqItemFinder = new FrequentItemSetFinder();
        List<List<String>> transItemList = freqItemFinder.readGeneDataSet("gene_expression.csv");
        this.combinationMap = finder.frequentItemSetGenerator(0.5, transItemList);
        //this.combinationMap = testDataMap();
        this.RuleList = new ArrayList<List<String>>();
        this.BodyList = new ArrayList<List<String>>();
        this.HeadList = new ArrayList<List<String>>();

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

            while (listit.hasNext()) {
                List<String> itemlist = listit.next();
                /*String ps = "";

                for(String s : itemlist)
                {
                    ps = ps + s + " ";
                }

                System.out.println("subset " + ps);*/

                if (itemlist.size() != 0 && itemlist.size() != key.size()) {
                    Iterator<List<String>> listitcopy = generatedsubsetlistcopy.iterator();

                    while (listitcopy.hasNext()) {
                        List<String> itemlistcopy = listitcopy.next();
                        if(itemlistcopy.size()!=0 && itemlistcopy.size()!=key.size())
                        {
                            if(checkRuleValid(itemlist,itemlistcopy))
                            {

                                List<String> rule = new ArrayList<String>();
                                rule.addAll(itemlist);
                                rule.add("->");
                                rule.addAll(itemlistcopy);

                                //itemlist.add("->");
                                //itemlist.addAll(itemlistcopy);

                                if(!RuleList.contains(rule))
                                {
                                    RuleList.add(rule);
                                    BodyList.add(itemlist);
                                    HeadList.add(itemlistcopy);
                                }
                            }


                            // check if answer is not matching after implementation


                            /*if(checkRuleValid(itemlistcopy,itemlist))
                            {
>>>>>>> Stashed changes
                                itemlistcopy.add("->");
                                itemlistcopy.addAll(itemlist);

                                if (!RuleList.contains(itemlistcopy)) {
                                    RuleList.add(itemlistcopy);
                                }
                            }*/
                        }
                    }


                }
            }
        }
        System.out.println(RuleList);

        List<String> qstring1 = new ArrayList<String>();
        qstring1.add("G6_UP");
        samplequerytemplate1("RULE", "ANY", qstring1);

        List<String> qstring2 = new ArrayList<String>();
        qstring2.add("G1_UP");
        samplequerytemplate1("RULE", "1", qstring2);

        List<String> qstring3 = new ArrayList<String>();
        qstring3.add("G1_UP");
        qstring3.add("G10_Down");
        samplequerytemplate1("RULE", "1", qstring3);

        List<String> qstring4 = new ArrayList<String>();
        qstring4.add("G6_UP");
        samplequerytemplate1("BODY", "ANY", qstring4);

        List<String> qstring5 = new ArrayList<String>();
        qstring5.add("G72_UP");
        samplequerytemplate1("BODY", "NONE", qstring5);

        List<String> qstring6 = new ArrayList<String>();
        qstring6.add("G1_UP");
        qstring6.add("G10_Down");
        samplequerytemplate1("BODY", "1", qstring6);

        List<String> qstring7 = new ArrayList<String>();
        qstring7.add("G6_UP");
        samplequerytemplate1("HEAD", "ANY", qstring7);

        List<String> qstring8 = new ArrayList<String>();
        qstring8.add("G1_UP");
        qstring8.add("G6_UP");
        samplequerytemplate1("HEAD", "NONE", qstring8);

        List<String> qstring9 = new ArrayList<String>();
        qstring9.add("G6_UP");
        qstring9.add("G8_UP");
        samplequerytemplate1("HEAD", "1", qstring9);

        List<String> qstring10 = new ArrayList<String>();
        qstring10.add("G1_UP");
        qstring10.add("G6_UP");
        qstring10.add("G72_UP");
        samplequerytemplate1("RULE","1",qstring10);

        List<String> qstring11 = new ArrayList<String>();
        qstring11.add("G1_UP");
        qstring11.add("G6_UP");
        qstring11.add("G72_UP");
        samplequerytemplate1("RULE","ANY",qstring11);

        samplequerytemplate2("RULE", 3);
        samplequerytemplate2("BODY",2);
        samplequerytemplate2("HEAD",2);

        samplequerytemplate3_1();
        samplequerytemplate3_2();
        samplequerytemplate3_3();
        samplequerytemplate3_4();

    }


    public double findconfidence(List<String> lhsstring, List<String> rhsstring) {
        int lhssup = combinationMap.get(lhsstring);
        int rhssup = combinationMap.get(rhsstring);

        List<String> tempunionlist = new ArrayList<String>(lhsstring);
        tempunionlist.addAll(rhsstring);

        Collections.sort(tempunionlist);

        int unionsup = combinationMap.get(tempunionlist);

        double confval = (double)unionsup / lhssup;

        return confval;

    }


    public List<List<String>> generateSubset(List<String> powerset) {
        List<List<String>> subsets = new ArrayList<List<String>>();
        if (powerset.isEmpty()) {
            subsets.add(new ArrayList<String>());
            return subsets;
        }

        String head = powerset.get(0);
        List<String> rest = new ArrayList<String>(powerset.subList(1, powerset.size()));
        for (List<String> sublist : generateSubset(rest)) {
            List<String> newlist = new ArrayList<String>();
            newlist.add(head);
            newlist.addAll(sublist);
            subsets.add(newlist);
            subsets.add(sublist);
        }

        return subsets;

    }

    public boolean checkRuleValid(List<String> checklhs, List<String> checkrhs) {
        int flag = 0;

        for (String lhsitem : checklhs) {
            if (checkrhs.contains(lhsitem)) {
                flag = 1;
                break;
            }
        }

        if (flag == 0) {
            double confvalue = findconfidence(checklhs, checkrhs);
            if (confvalue >= confidenceval) {
                return true;
            }
        }

        return false;
    }

    public Map<List<String>, Integer> testDataMap() {
        Map<List<String>, Integer> testMap = new HashMap<List<String>, Integer>();

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

        testMap.put(testList6, 4);
        testMap.put(testList7, 4);
        testMap.put(testList8, 2);
        testMap.put(testList9, 4);
        testMap.put(testList10, 2);
        testMap.put(testList11, 2);

        testMap.put(testList12, 2);
        testMap.put(testList13, 2);

        return testMap;
    }


    public void samplequery1()
    {
        int count = 0;

        for(int i =0; i < RuleList.size(); i++)
        {
            if(RuleList.get(i).contains("G1_UP")||RuleList.get(i).contains("G10_Down"))
            {
                count = count +1;
                continue;
            }
        }

        System.out.println("Number of rules for query 1 = " + count);

    }

    public void samplequery2()
    {
        int count = 0;

        for(int i =0; i < BodyList.size(); i++)
        {
            if(BodyList.get(i).contains("G6_UP"))
            {
                count = count +1;
                continue;
            }
        }

        System.out.println("Number of rules for query 1 = " + count);

    }

    public void samplequerytemplate1(String part1, String clause, List<String> genes)
    {
        int count = 0;

        if(part1.equals("RULE"))
        {
            if(clause.equals("ANY"))
            {
                for(int i =0; i < RuleList.size(); i++)
                {
                    for(int j =0; j<genes.size();j++)
                    {
                        if(RuleList.get(i).contains(genes.get(j)))
                        {
                            count = count + 1;
                            break;
                        }
                    }
                }
            }
            else if(clause.equals("NONE"))
            {
                for(int i =0; i < RuleList.size(); i++)
                {
                    int flag = 0;
                    for(int j =0; j<genes.size();j++)
                    {
                        if(RuleList.get(i).contains(genes.get(j)))
                        {
                            flag = 1;
                            break;
                        }
                    }
                    if(flag == 0)
                    {
                        count = count + 1;
                    }
                }
            }
            else if(clause.equals("1"))
            {
                for(int i =0; i < RuleList.size(); i++)
                {
                    for(int j =0; j<genes.size();j++)
                    {
                        if(RuleList.get(i).contains(genes.get(j)))
                        {
                            count = count + 1;
                            break;
                        }
                    }
                }
            }
        }
        else if(part1.equals("BODY"))
        {
            if(clause.equals("ANY"))
            {
                for(int i =0; i < BodyList.size(); i++)
                {
                    for(int j =0; j<genes.size();j++)
                    {
                        if(BodyList.get(i).contains(genes.get(j)))
                        {
                            count = count + 1;
                            break;
                        }
                    }
                }
            }
            else if(clause.equals("NONE"))
            {
                for(int i =0; i < BodyList.size(); i++)
                {
                    int flag = 0;
                    for(int j =0; j<genes.size();j++)
                    {
                        if(BodyList.get(i).contains(genes.get(j)))
                        {
                            flag = 1;
                            break;
                        }
                    }
                    if(flag == 0)
                    {
                        count = count + 1;
                    }
                }
            }
            else if(clause.equals("1"))
            {
                for(int i =0; i < BodyList.size(); i++)
                {
                    for(int j =0; j<genes.size();j++)
                    {
                        if(BodyList.get(i).contains(genes.get(j)))
                        {
                            count = count + 1;
                            break;
                        }
                    }
                }
            }

        }
        else if(part1.equals("HEAD"))
        {
            if(clause.equals("ANY"))
            {
                for(int i =0; i < HeadList.size(); i++)
                {
                    for(int j =0; j<genes.size();j++)
                    {
                        if(HeadList.get(i).contains(genes.get(j)))
                        {
                            count = count + 1;
                            break;
                        }
                    }
                }
            }
            else if(clause.equals("NONE"))
            {
                for(int i =0; i < HeadList.size(); i++)
                {
                    int flag = 0;
                    for(int j =0; j<genes.size();j++)
                    {
                        if(HeadList.get(i).contains(genes.get(j)))
                        {
                            flag = 1;
                            break;
                        }
                    }
                    if(flag == 0)
                    {
                        count = count + 1;
                    }
                }
            }
            else if(clause.equals("1"))
            {
                for(int i =0; i < HeadList.size(); i++)
                {
                    for(int j =0; j<genes.size();j++)
                    {
                        if(HeadList.get(i).contains(genes.get(j)))
                        {
                            count = count + 1;
                            break;
                        }
                    }
                }
            }
        }

        System.out.println("The number of rules that satisfy the querytemplate1 are " + count);

    }

    public void samplequerytemplate2(String part1, int counter)
    {
        int count = 0;
        int checker = 0;

        if(part1.equals("RULE"))
        {
            for(int i =0; i < RuleList.size(); i++)
            {
                checker = RuleList.get(i).size();
                if(checker-1 >= counter)
                {
                    count = count + 1;
                    continue;
                }
            }
        }
        else if(part1.equals("BODY"))
        {
            for(int i =0; i < BodyList.size(); i++)
            {
                checker = BodyList.get(i).size();
                if(checker >= counter)
                {
                    count = count + 1;
                }
            }
        }
        else if(part1.equals("HEAD"))
        {
            for(int i =0; i < HeadList.size(); i++)
            {
                checker = HeadList.get(i).size();
                if(checker >= counter)
                {
                    count = count + 1;
                }
            }
        }

        System.out.println("The number of rules that satisfy the query2template are " + count);
    }

    public void samplequerytemplate3_1()
    {
        int count = 0;
        int flag1 = 0;
        //int flag2 = 0;

        for(int i =0; i<BodyList.size();i++)
        {
            if(BodyList.get(i).contains("G1_UP") && HeadList.get(i).contains("G59_UP"))
            {
                count = count + 1;
                break;
            }
        }

        System.out.println("The number of rules that satisfy the query3template are " + count);
    }

    public void samplequerytemplate3_2()
    {
        int count = 0;
        int flag1 = 0;
        //int flag2 = 0;

        for(int i =0; i<BodyList.size();i++)
        {
            if(BodyList.get(i).contains("G1_UP") || HeadList.get(i).contains("G6_UP"))
            {
                count = count + 1;
                break;
            }
        }

        System.out.println("The number of rules that satisfy the query3template are " + count);
    }

    //need to handle 2 of g6 up part.
    public void samplequerytemplate3_3()
    {
        int count = 0;

        for(int i =0; i<BodyList.size();i++)
        {
            if(BodyList.get(i).contains("G1_UP"))
            {
                count = count + 1;
                break;
            }
        }

        System.out.println("The number of rules that satisfy the query3template are " + count);
    }

    public void samplequerytemplate3_4()
    {
        int count = 0;

        for(int i =0; i<BodyList.size();i++)
        {
            if(HeadList.get(i).contains("G1_UP") && (!BodyList.get(i).contains("AML")) && (!BodyList.get(i).contains("ALL")) && (!BodyList.get(i).contains("Breast Cancer")) && (!BodyList.get(i).contains("Colon Cancer")))
            {
                count = count + 1;
                break;
            }
        }

        System.out.println("The number of rules that satisfy the query3template are " + count);
    }
}
