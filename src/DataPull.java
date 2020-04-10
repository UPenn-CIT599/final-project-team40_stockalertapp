import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;


/**
 * A class to pull stock historical data
 * @author kravetsj
 *
 */
public class DataPull  {

	public DataPull () {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Returns a csv of stock data with data, open, high, low, close, volume
	 * @param ticker
	 * @throws InterruptedException 
	 */
	public static void getcsv(String symbol) throws InterruptedException {
		///api key =JRVCT84VUG4TM97S
		         try {
		        	
		        	 try
		             { 	 
		        		 System.gc(); 
		                 Files.deleteIfExists(Paths.get((symbol+".csv"))); 
		                 //System.out.println("Deleted old "+symbol+".csv file"); 
		             } 
		             catch(NoSuchFileException e) 
		             { 
		                 System.out.println("No such file/directory exists"); 
		             }
		        	 
		        	System.out.println("waiting 13 seconds for " +symbol+" data due to api rate limit")  ; 
		        	TimeUnit.SECONDS.sleep(13);
					String url="https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" +(symbol)+"&outputsize=full&apikey=JRVCT84VUG4TM97S&datatype=csv";
					
					InputStream input = new URL(url).openStream();

					FileWriter fw= new FileWriter ((symbol+".csv"), true);
					PrintWriter pw= new PrintWriter (fw);
					
					 try (	InputStreamReader reader = new InputStreamReader(input, "UTF-8");

			                 BufferedReader br = new BufferedReader(reader)) {

			                 String line;

			             while ((line = br.readLine()) != null) {
			                 //System.out.println(line);
			                 pw.println (line);
							 pw.flush();  
			             }
			             br.close();
			             reader.close();
			         }
					 

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	/**Returns the current price of a stock ticker
	 * 
	 * @param symbol
	 * @throws InterruptedException 
	 */
	public static double getcurrentquote(String symbol) throws InterruptedException {
		

		
		
		System.out.println("waiting 13 seconds for "+symbol+" data due to api rate limit")  ;
		TimeUnit.SECONDS.sleep(13);
		String url= "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" +symbol+"&apikey=JRVCT84VUG4TM97S";
		
		InputStream input = null;
		
		
		try {
			input = new URL(url).openStream();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try (	InputStreamReader reader = new InputStreamReader(input, "UTF-8");

	                BufferedReader br = new BufferedReader(reader)) 
		{
	                String line;
	                
	                String x= " ";
	            while ((line = br.readLine()) != null) {
	                x=x+line;
	            }
	            String[] lineComponents = x.split(":");
	            String[] priceComponents = lineComponents[6].split(",");
	            String str=priceComponents[0].replace("\"","");
	            double currentprice = Double.parseDouble(str);
	            br.close();
	            reader.close();
	            return currentprice;
	            
	        }
		
		
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0.0;
		}
	}

}
