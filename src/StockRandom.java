<<<<<<< HEAD:src/Stock.java
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
=======
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Helper class for generating random stochastic time series of prices.  Only used for testing purposes.
 * @author robertstanton
 *
 */
public class StockRandom {
    
    private String ticker;
    private TreeMap<LocalDate, Double> historicalPrices;
    
    public StockRandom() {
        this("test");
    }
    
    public StockRandom(String tick) {
        historicalPrices = new TreeMap<>();
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        // LocalDate endDate = LocalDate.of(2020, 1, 30);
        LocalDate endDate = startDate.plusDays((long)2400);
        List<LocalDate> dateRange = startDate.datesUntil(endDate).collect(Collectors.toList());
        setHistoricalPrices(dateRange);
        ticker = tick;
    }
    
    /**
     * creates stochastic time series of prices with normally distributed standard deviation.
     * @param dateRange
     */
    public void setHistoricalPrices(List<LocalDate> dateRange) {
        Random generator = new Random();
        Double curValue = generator.nextDouble() * 100;
        for(LocalDate d : dateRange) {
            curValue = curValue * ( 1 + (generator.nextGaussian() / 100));
            historicalPrices.put(d, curValue);
        }
    }
    
    public TreeMap<LocalDate, Double> getHistoricalPrices() {
         return historicalPrices;
    }
    
    public String getName() {
        return ticker;
    }
}

>>>>>>> guiMod:src/StockRandom.java
