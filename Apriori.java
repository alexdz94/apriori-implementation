import java.util.*;
public class Apriori {
    public double minSup; // minimum support threshold
    public ArrayList<ArrayList<String>> dataset ; // our dataset representation as list of lists ({{1,2},{1,4,5}}..)
    public ArrayList<String> items ;  // ﻿The domain of items (distinct values).
    public LinkedHashMap<String,Integer> itemsCount ; // a list of itemsets of size 1 with support
    public ArrayList<FrequentItemsets> frequent; // a list of frequent itemset.
    // constructor who run all processes
    public Apriori(ArrayList<String> items ,ArrayList<ArrayList<String>> dataset , double sup)
    {
        this.dataset = dataset;
        this.items = items;
        this.minSup = sup;
        this.frequent = new ArrayList<>();
        this.itemsCount = new LinkedHashMap<>();
        System.out.println("Dataset size : "+this.dataset.size()+" | Min support : "+this.minSup+"\n");
        ArrayList<ArrayList<String>> candidates;
        ArrayList<ArrayList<String>> oneFreq = getCandidates(1);
        for(ArrayList<String> oneItemset : oneFreq)
        {
            this.frequent.add(new FrequentItemsets(oneItemset,1));
        }

        System.out.print("\n1-Frequent Itemsets : ");
        ArrayList<FrequentItemsets> itemsets = getFrequentItemsetsOrder(1);
        for(FrequentItemsets itemset : itemsets)
        {
            System.out.print(itemset.itemset+" ");
        }
        System.out.println();
        for(int k = 2 ; getFrequentItemsetsOrder(k-1).size() != 0 ; k++)
        {
            candidates = getCandidates(k);

            for(ArrayList<String> cand : candidates)
            {
                int size = cand.size();
                double counter = 0 ;
                double itemsCounter = 0 ;
                for(ArrayList<String> transaction : this.dataset)
                {
                    for(String item : cand)
                    {
                        if(transaction.contains(item)) itemsCounter++;
                    }
                    if (itemsCounter == size) counter++;
                    itemsCounter = 0 ;
                }
                if(counter/this.dataset.size() >= this.minSup)
                {
                    ArrayList<String> newFrequent = new ArrayList<>();
                    for(String item : cand)
                    {
                        newFrequent.add(item);
                    }
                    FrequentItemsets newFrequentItemset = new FrequentItemsets(newFrequent,k);
                    this.frequent.add(newFrequentItemset);
                }
            }
            System.out.print("\n"+k+"-Frequent Itemsets : ");
            ArrayList<FrequentItemsets> itemsets_ = getFrequentItemsetsOrder(k);
            if(itemsets_.size()==0) System.out.print(" There's no "+k+" frequent itemsets.\n");
            else for(FrequentItemsets itemset : itemsets_) System.out.print(itemset.itemset+" ");
            System.out.println();
        }
        System.out.print("\nGenerated Frequent Itemsets : "+this.frequent.size()+"\n");
        System.out.println("####### Finish ######\n");
    }
    // method who get all k-candidates from k-1 frequent itemsets.
    public ArrayList<ArrayList<String>> getCandidates(int k)
    {
        ArrayList<ArrayList<String>>  candidates = new ArrayList<>();
        if(k==1)
        {
            for(ArrayList<String> transaction : this.dataset)
            {
                for(String item : transaction)
                {
                    if(this.itemsCount.containsKey(item)) this.itemsCount.replace(item,this.itemsCount.get(item)+1);
                    else this.itemsCount.put(item,1);
                }
            }

            this.items = new ArrayList<>();

            Object[] keySet = this.itemsCount.keySet().toArray();

            System.out.println("Frequence of each item : "+this.itemsCount);

            for(int i = 0 ; i < keySet.length ; i++ )
            {

                if((double)this.itemsCount.get(keySet[i])/this.dataset.size() >= this.minSup)
                {
                    itemsCount.put(String.valueOf(keySet[i]),this.itemsCount.get(keySet[i]));
                    ArrayList<String> candid_freq_item = new ArrayList<>();
                    candid_freq_item.add(String.valueOf(keySet[i]));
                    candidates.add(candid_freq_item);
                    this.items.add(String.valueOf(keySet[i]));
                }
            }

        }else{

            if(k==2)
            {
                for(int i = 0 ; i < this.items.size() ; i++)
                {
                    String one = this.items.get(i);
                    for(int j = i+1 ; j < this.items.size() ; j++)
                    {
                        String two = this.items.get(j);
                        ArrayList<String> temp = new ArrayList<>();
                        temp.add(one);
                        temp.add(two);
                        if (!hasInfrequentSubitemset(getFrequentItemsetsOrder(k-1),temp))
                        {
                             candidates.add(temp);
                        }
                    }
                }

            }else{
                ArrayList<ArrayList<String>> joinList = joinItemsets(k);
                for(ArrayList<String> temp : joinList)
                {
                    // a priori knowledge here !
                    // if a subset of a k-candidate itemset not exists in k-1 frequent itemsets
                    // we don't put it in the candidates list.
                    if (!hasInfrequentSubitemset(getFrequentItemsetsOrder(k-1),temp))
                    {
                        candidates.add(temp);
                    }
                }
            }
        }
        return candidates;
    }
    // method who returns true/false if any subset of itemset isn't frequent in k-1 lastFreq (k-1 frequent itemsets).
    public boolean hasInfrequentSubitemset(ArrayList<FrequentItemsets> lastFreq , ArrayList<String> itemset)
    {
        int itemsetSize = itemset.size();
        for(String item : itemset)
        {
            int counter = 0;
            for (int i = 0 ; i < lastFreq.size() ; i++)
            {
                for(int j=0 ; j < lastFreq.get(i).itemset.size() ; j++)
                {
                    if(item.matches(lastFreq.get(i).itemset.get(j)))
                    {
                        counter++;
                    }
                }
            }
            if( counter == 0 ) return true;
        }
        return false;
    }
    // to get all frequent itemsets of size (int order)
    public ArrayList<FrequentItemsets> getFrequentItemsetsOrder(int order)
    {
        ArrayList<FrequentItemsets> inOrder = new ArrayList<>();
        for(FrequentItemsets itemset : this.frequent)
        {
            if(order==itemset.order) inOrder.add(itemset);
        }
        return inOrder;
    }
    // this method sort the itemset with a COMPARATOR object to give the possibility of join itemsets based on the order.
    public ArrayList<ArrayList<String>> joinItemsets(int k)
    {
        ArrayList<FrequentItemsets> freqToJoin = getFrequentItemsetsOrder(k-1);
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>(freqToJoin.size());
        for (FrequentItemsets itemset : freqToJoin) {
            ArrayList<String> l = new ArrayList<>(itemset.itemset);
            Collections.sort(l,ITEM_COMPARATOR);
            list.add(l);
        }
        int joinSize = list.size();
        ArrayList<ArrayList<String>> joinResult = new ArrayList<>(joinSize);
        for (int i = 0; i < joinSize; ++i) {
            for (int j = i + 1; j < joinSize; ++j) {
                ArrayList<String> candidate = tryMergeItemsets(list.get(i), list.get(j));
                if (candidate != null) {
                    joinResult.add(candidate);
                }
            }
        }
        return joinResult;
    }
    /* comparator from stackoverflow.com */
    private static final Comparator ITEM_COMPARATOR = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            return ((Comparable) o1).compareTo(o2);
        }
    };
    // this method returns the merge of itemset1 and itemset2 if the itemsets can be merged, it returns null otherwise.
    public ArrayList<String> tryMergeItemsets(ArrayList<String> itemset1 , ArrayList<String> itemset2)
    {
        int length = itemset1.size();
        for (int i = 0; i < length - 1; ++i) {
            if (!itemset1.get(i).equals(itemset2.get(i))) {
                return null;
            }
        }
        if (itemset1.get(length - 1).equals(itemset2.get(length - 1))) {
            return null;
        }
        ArrayList<String> merge = new ArrayList<>(length + 1);

        for (int i = 0; i < length - 1; ++i) {
            merge.add(itemset1.get(i));
        }
        merge.add(itemset1.get(length - 1));
        merge.add(itemset2.get(length - 1));
        return merge;
    }
}