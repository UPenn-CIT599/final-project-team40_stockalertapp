import java.io.BufferedReader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * DATAPULL CLASS:
 * 
 * This class to pull stock historical data from an external source.
 * 
 * @author kravetsj
 *
 */
public class DataPull {

	public DataPull() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * GETJSON METHOD:
	 * This method returns a JSON string from a URL
	 * 
	 * @param ticker
	 */
	
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

	/**
	 * GETTCSV METHOD:
	 * This method returns a CSV of stock data with data, open, high, low, close, volume.
	 * 
	 * @param ticker
	 */
	public static void getcsv(String symbol) {
		/// api key =JRVCT84VUG4TM97S
		// add json to project build path with link below
		// https://www.wikihow.com/Add-JARs-to-Project-Build-Paths-in-Eclipse-(Java)
		try {

			String url = ("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + symbol
					+ "&outputsize=full&apikey=JRVCT84VUG4TM97S&datatype=csv");

			InputStream input = new URL(url).openStream();

			FileWriter fw = new FileWriter((symbol + ".csv"), true);
			PrintWriter pw = new PrintWriter(fw);

			try (InputStreamReader reader = new InputStreamReader(input, "UTF-8");

					BufferedReader br = new BufferedReader(reader)) {

				String line;

				while ((line = br.readLine()) != null) {
					System.out.println(line);
					pw.println(line);
					pw.flush();
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * GETCURRENTQUOTE METHOD:
	 * 
	 * This method returns a quote like this:
	 * 
	 * "Global Quote": { "01. symbol": "IBM", "02. open": "112.0000", "03. high":
	 * "113.8100", "04. low": "110.1700", "05. price": "110.9300", "06. volume":
	 * "6305552", "07. latest trading day": "2020-03-31", "08. previous close":
	 * "112.9300", "09. change": "-2.0000", "10. change percent": "-1.7710%" }
	 * 
	 * @param symbol
	 */
	public static String getcurrentquote(String symbol) {

		// String
		// url=("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+symbol+"&outputsize=full&apikey=JRVCT84VUG4TM97S&datatype=csv");

		String quote = getJSON("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol
				+ "&apikey=JRVCT84VUG4TM97S");
		return quote;
	}

	
	public static void main(String[] args) {

		getcsv("SPY");

		System.out.print(getJSON(getcurrentquote("SPY")));

	}
}