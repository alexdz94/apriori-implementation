import java.util.ArrayList;

public class FrequentItemsets {

     /*
    * a frequent itemset is caraterized by a list of items
    * and the size of this list is the order
    * 1-frequentItemsets , 2-frequentItemsets ..... etc
    * */

    ArrayList<String> itemset = new ArrayList<>();
    int order ;
    public FrequentItemsets(ArrayList<String> itemset , int order)
    {
        this.itemset.addAll(itemset);
        this.order = order;
    }
}
