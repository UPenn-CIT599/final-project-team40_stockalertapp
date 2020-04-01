import java.io.BufferedReader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.MalformedURLException;
import java.net.URL;


import javax.net.ssl.HttpsURLConnection;

public class DataPull  {

	public DataPull () {
		// TODO Auto-generated constructor stub
	}
	
	public static String getJSON(String url) {
        HttpsURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();

            con.connect();


            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }
	
	public static void getcsv(String symbol) {
		///api key =JRVCT84VUG4TM97S
		//add json to project build path with link below
		//https://www.wikihow.com/Add-JARs-to-Project-Build-Paths-in-Eclipse-(Java)
		         try {
		        	
					String url=("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+symbol+"&outputsize=full&apikey=JRVCT84VUG4TM97S&datatype=csv");
					
					InputStream input = new URL(url).openStream();

					FileWriter fw= new FileWriter ((symbol+".csv"), true);
					PrintWriter pw= new PrintWriter (fw);
					
					 try (	InputStreamReader reader = new InputStreamReader(input, "UTF-8");

			                 BufferedReader br = new BufferedReader(reader)) {

			                 String line;

			             while ((line = br.readLine()) != null) {
			                 System.out.println(line);
			                 pw.println (line);
							 pw.flush();  
			             }
			         }

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

	public static void main(String[] args) {
		// tree map stock class
		//add error catching
		getcsv("SPY");
		      
		        
	

}
}