import java.time.LocalDate;
import java.util.TreeMap;


/**
 * A class to store a stock's ticker, price, and historical data
 * @author kravetsj
 *
 */
public class Stock {

private static final TreeMap<LocalDate, Double> TreeMap = null;
private String ticker;
private String csv;
private Object currentprice;
private String quote;
private TreeMap datahistory;

	/**
	 * Stock object with stock's ticker, price, and historical data
	 * @param ticker
	 */
	
	public Stock(String ticker) {
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
	 */
	private TreeMap<LocalDate, Double> getTreemap(String csv, String Ticker) {
		// TODO Auto-generated method stub
		DataPull t= new DataPull();
		t.getcsv(csv);
		
		
		
		return TreeMap;
	}





	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	

}
