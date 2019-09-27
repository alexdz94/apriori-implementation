import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class lunch {

    public static void main(String []args)
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Give a minimum support for your Frequent Patterns. (ex : 0,5) : ");
        double minSup = input.nextDouble();  // scanner for reading the support count
        JFileChooser fileChooser = new JFileChooser(); // to choose a dataset file
        fileChooser.showOpenDialog(new JFrame());
        File inputFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
        ArrayList<String> items = new ArrayList<String>();
        ArrayList<ArrayList<String>> dataset = new ArrayList<ArrayList<String>>();  // out dataset list of lists
        // here we read the file and extract from each line a list of items , and we put them in a list μ.
        try{
            Scanner fileReader = new Scanner(inputFile);
            System.out.println("Reading dataset ... ");
            while (fileReader.hasNext())
            {
                String line = fileReader.nextLine();
                String[] line_array = line.split(",");
                ArrayList<String> tmp = new ArrayList<String>();
                for(String str : line_array)
                {
                    tmp.add(str);
                    if(!items.contains(str)) items.add(str);
                }
                dataset.add(tmp);
            }
            System.out.println("Dataset readed.");
            System.out.println("Starting timer ...");
            long start = System.nanoTime(); // we start a chrono
            Apriori apriori = new Apriori(items,dataset,minSup);
            // we lunch the algorithm with the dataset and the minimum support entered by the user
            long finish = System.nanoTime();
            // we finish the chrono
            long timeElapsed = finish - start; // execution time
            long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); // used memory
            System.out.println("Execution time : "+timeElapsed/1000000000F+" secondes.");
            System.out.println("Used memory : "+usedMem/(1024*1024)+" MB");
        }catch (Exception e)
        {
            System.out.println("Error : " + e.getMessage() +" ! ");
        }
    }
}
