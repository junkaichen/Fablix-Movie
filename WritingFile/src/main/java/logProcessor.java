import java.io.*;
import java.util.Scanner;


public class logProcessor {
    public static void main(String[] args)
    {
        double queryTime = 0;
        int queries = 0;
        double queryWithEmptyTime = 0;
        int allQueries = 0;
        double servletTime = 0;
        double servletWithEmptyTime = 0;
        try{
            // /tmp/timeInfo
            File f = new File("/tmp/timeInfo");
            Scanner reader = new Scanner(f);

            while(reader.hasNextLine())
            {
                String line = reader.nextLine();
                if(!line.isEmpty())
                {
                    String[] vals = line.split(",");
                    long val1 = Long.parseLong(vals[0]);
                    long val2 = Long.parseLong(vals[1]);
                    allQueries++;
                    queryWithEmptyTime += (double)(val2/1000000);
                    servletWithEmptyTime += (double)(val1/1000000);
                    // this just takes out all values of empty results not being measured
                    if((double)(val2/1000000) > 150)
                    {
                        queries++;
                        queryTime += (double)(val2/1000000);
                        servletTime += (double)(val1/1000000);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Avg query time = " + (double)(queryTime/queries)
                + "ms");
        System.out.println("Avg query time with empty results for unknown = "
                + (double)(queryWithEmptyTime/allQueries) + "ms");
        System.out.println("Avg servlet time = " + (double)(servletTime/queries)
                + "ms");
        System.out.println("Avg servlet time with empty results for unknown = "
                + (double)(servletWithEmptyTime/allQueries) + "ms");
        System.out.println("Avg JDBC time = "
                + ((double)(servletTime/queries) - (double)(queryTime/queries))  + "ms");
        System.out.println("Avg JDBC time with empty results for unknown = "
                + ((double)(servletWithEmptyTime/allQueries)-(double)(queryWithEmptyTime/allQueries)) + "ms");

    }

}
