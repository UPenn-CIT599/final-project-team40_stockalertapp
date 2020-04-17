import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

/**
 * A class to store a stock's ticker, price, and historical data
 * 
 * @author Joseph Kravets
 *
 */
public class Stock implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -1995220666246658729L;
    private String ticker;
    private String csv;
    private double quote;
    private TreeMap dataHistory;

    /**
     * Stock object with stock's ticker, price, and historical data
     * 
     * @param ticker
     * @throws FileNotFoundException
     * @throws InterruptedException
     */

    public Stock(String ticker) throws FileNotFoundException, InterruptedException {

	DataPull data = new DataPull();

	this.ticker = ticker;
	this.csv = ticker + ".csv";

	try {
	    this.quote = data.getCurrentQuote(ticker);
	    this.dataHistory = getTreeMap(csv, ticker);

	} catch (java.time.format.DateTimeParseException e) {
	    System.out.print("waiting 30 seconds due to api rate limit");
	    TimeUnit.SECONDS.sleep(30);
	    this.dataHistory = getTreeMap(csv, ticker);
	    this.quote = data.getCurrentQuote(ticker);
	}
    }

    /**
     * Tree map to store data from stock csv
     * 
     * @param ticker
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    private TreeMap<LocalDate, OHLCV> getTreeMap(String csv, String ticker)
	    throws FileNotFoundException, InterruptedException {

	System.gc();

	DataPull dataPull = new DataPull();
	dataPull.getCsv(ticker);
	File data = new File(csv);
	Scanner fileReader = new Scanner(data);
	fileReader.nextLine();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");

	TreeMap dataHistory = new TreeMap<>();

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

		OHLCV ohlcv = new OHLCV(open, high, low, close, volume);
		dataHistory.put(localDate, ohlcv);

	    }
	}
	fileReader.close();

	try {
	    System.gc();
	    Files.deleteIfExists(Paths.get((csv)));

	} catch (NoSuchFileException e) {
	    System.out.println("No such file/directory exists");
	} catch (IOException e) {

	    e.printStackTrace();
	}

	return dataHistory;
    }

    /**
     * Getters and setters
     * 
     * @return
     */

    public String getTicker() {
	return ticker;
    }

    public void setTicker(String ticker) {
	this.ticker = ticker;
    }

    public String getCsv() {
	return csv;
    }

    public void setCsv(String csv) {
	this.csv = csv;
    }

    public double getQuote() {
	return quote;
    }

    public void setQuote(double quote) {
	this.quote = quote;
    }

    public TreeMap getdataHistory() {
	return dataHistory;
    }

    public void setdataHistory(TreeMap dataHistory) {
	this.dataHistory = dataHistory;
    }

    /*
     * main for testing
     * 
     */
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
	// TODO Auto-generated method stub

	Stock t;

	t = new Stock("SLB");

	// get all entries
	Set<Map.Entry<LocalDate, OHLCV>> entries = t.dataHistory.entrySet();

	// using for loop
	for (Map.Entry<LocalDate, OHLCV> entry : entries) {
	    System.out.println(entry.getKey() + " open " + entry.getValue().open + " high " + entry.getValue().high
		    + " low " + entry.getValue().low + " close " + entry.getValue().close + " volume "
		    + entry.getValue().volume);
	}

	System.out.println(t.quote);

	t = new Stock("UPRO");

	// get all entries
	Set<Map.Entry<LocalDate, OHLCV>> entries1 = t.dataHistory.entrySet();

	// using for loop
	for (Map.Entry<LocalDate, OHLCV> entry : entries1) {
	    System.out.println(entry.getKey() + " open " + entry.getValue().open + " high " + entry.getValue().high
		    + " low " + entry.getValue().low + " close " + entry.getValue().close + " volume "
		    + entry.getValue().volume);
	}

	System.out.println(t.quote);

    }

}
