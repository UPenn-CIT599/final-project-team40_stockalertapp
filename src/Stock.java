import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;


/**
 * A class to store a stock's ticker, price, and historical data
 * @author kravetsj
 *
 */
public class Stock {


private String ticker;
private String csv;
private double quote;
private TreeMap datahistory;

	/**
	 * Stock object with stock's ticker, price, and historical data
	 * @param ticker
	 * @throws FileNotFoundException 
	 */
	
	public Stock(String ticker) throws FileNotFoundException {
		// TODO Auto-generated constructor stub
		
		DataPull t= new DataPull();
		 
		this.ticker= ticker;
		this.csv= ticker+".csv";
		this.quote= t.getcurrentquote(ticker);
		this.datahistory=getTreemap(csv, ticker);
	}

	
	
	
	/**
	 * Tree map to store data from stock csv
	 * @param ticker
	 * @throws FileNotFoundException 
	 */
	private TreeMap<LocalDate, OHLCV> getTreemap(String csv, String Ticker) throws FileNotFoundException {
		// TODO Auto-generated method stub
		DataPull t= new DataPull();
		t.getcsv(ticker);
		File data= new File(csv);
		Scanner fileReader = new Scanner(data);
		fileReader.nextLine(); 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
		
		TreeMap datahistory =new TreeMap<>();
       
		
		int iteration = 0;
        while (fileReader.hasNextLine()) {
        	
        	
			String line = fileReader.nextLine();
			String[] lineComponents = line.split(",");
			
			if (!lineComponents[0].equals("timestamp")) {
				
			LocalDate localDate = LocalDate.parse(lineComponents[0], formatter);
			double open = Double.parseDouble(lineComponents[1]);
			double high = Double.parseDouble(lineComponents[2]);
			double low = Double.parseDouble(lineComponents[3]);
			double close = Double.parseDouble(lineComponents[4]);
			double volume = Double.parseDouble(lineComponents[5]);
			
			OHLCV ohlcv= new OHLCV( open,  high, low, close, volume);
			datahistory.put(localDate, ohlcv);

			}
			
			
		}
		return datahistory;
	}



	
	

}
