import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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
 * @author Joseph Kravets; Tiffany Choi
 *
 */
public class Stock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1995220666246658729L;
	private String ticker;
	private String csv;
	private double quote;
	private double sma;
	private double ema;
	private double rsi;
	private double macd;
	private double obv;
	private TreeMap dataHistory;
	private String indicator;
	private String above_below;
	private double savedalert;
	private HashMap<String, HashMap<String, Double>> storedAlerts;
	private HashMap<String, Boolean> calculatedAlerts;

	/**
	 * Stock object with stock's ticker, price, and historical data
	 * 
	 * @param ticker
	 * @throws FileNotFoundException
	 * @throws InterruptedException
	 */

	public Stock(String ticker) throws FileNotFoundException, InterruptedException {

		DataPull data = new DataPull();
		storedAlerts = new HashMap<>();
		calculatedAlerts = new HashMap<>();

		this.ticker = ticker;
		this.csv = ticker + ".csv";

		try {
			this.quote = data.getCurrentQuote(ticker);
			this.sma = data.getIndicator("SMA", ticker);
			this.ema = data.getIndicator("EMA", ticker);
			this.rsi = data.getIndicator("RSI", ticker);
			this.macd = data.getIndicator("MACD", ticker);
			this.obv = data.getIndicator("OBV", ticker);
			this.dataHistory = getTreeMap(csv, ticker);

		} catch (java.time.format.DateTimeParseException e) {
			System.out.print("waiting 30 seconds due to api rate limit");
			TimeUnit.SECONDS.sleep(30);
			this.dataHistory = getTreeMap(csv, ticker);
			this.quote = data.getCurrentQuote(ticker);
			this.sma = data.getIndicator("SMA", ticker);
			this.ema = data.getIndicator("EMA", ticker);
			this.rsi = data.getIndicator("RSI", ticker);
			this.macd = data.getIndicator("MACD", ticker);
			this.obv = data.getIndicator("OBV", ticker);
		}
	}

	/**
	 * Overloaded the Stock class to save Alerts
	 * 
	 * @param ticker
	 * @param indicator
	 * @param above_below
	 * @param savedalert
	 * @throws InterruptedException
	 */

	public Stock(String ticker, String indicator, String above_below, double savedalert)
			throws FileNotFoundException, InterruptedException {

		DataPull data = new DataPull();
		storedAlerts = new HashMap<>();
        calculatedAlerts = new HashMap<>();

		this.ticker = ticker;
		this.csv = ticker + ".csv";
		this.indicator = indicator;
		this.above_below = above_below;
		this.savedalert = savedalert;

		try {
			this.quote = data.getCurrentQuote(ticker);
			this.sma = data.getIndicator("SMA", ticker);
			this.ema = data.getIndicator("EMA", ticker);
			this.rsi = data.getIndicator("RSI", ticker);
			this.macd = data.getIndicator("MACD", ticker);
			this.obv = data.getIndicator("OBV", ticker);
			this.dataHistory = getTreeMap(csv, ticker);

		} catch (java.time.format.DateTimeParseException e) {
			System.out.print("waiting 30 seconds due to api rate limit");
			TimeUnit.SECONDS.sleep(30);
			this.dataHistory = getTreeMap(csv, ticker);
			this.quote = data.getCurrentQuote(ticker);
			this.sma = data.getIndicator("SMA", ticker);
			this.ema = data.getIndicator("EMA", ticker);
			this.rsi = data.getIndicator("RSI", ticker);
			this.macd = data.getIndicator("MACD", ticker);
			this.obv = data.getIndicator("OBV", ticker);
		}
	}

	/**
	 * Add Alerts to the stored alerts
	 * 
	 * @param ticker
	 * @param indicator
	 * @param above_below
	 * @param savedalert
	 */
	public void addAlert(String ticker, String indicator, String above_below, double savedalert) {

		HashMap<String, Double> temp = new HashMap<String, Double>();

		try {
			temp.put(above_below, savedalert);
			storedAlerts.put(ticker + "_" + indicator, temp);
		} catch (Exception e) {
			System.out.println("Alert cannot be found.");
		}
	}

	/**
	 * Remove Alerts from the stored alerts
	 * 
	 * @param ticker
	 * @param indicator
	 * @param above_below
	 * @param savedalert
	 */

	private void removeAlert(String ticker, String indicator, String above_below, double savedalert) {

		HashMap<String, Double> temp = new HashMap<String, Double>();

		try {
			temp.put(above_below, savedalert);
			storedAlerts.remove(ticker + "_" + indicator, temp);
		} catch (Exception e) {
			System.out.println("Alert cannot be found.");
		}

	}
	
	/**
	 * Overloaded calculateAlerts method for this stock;
	 */
	private void calculateAlerts() {
	    calculateAlerts(this.storedAlerts, this.ticker);
	}

	/***
	 * This method calculates the alerts and stores the booleans in a HashMap
	 * 
	 * @param storedAlerts
	 * @param ticker
	 */

	private void calculateAlerts(HashMap storedAlerts, String ticker) {

		Alerts alert = new Alerts();
		DataPull datapull = new DataPull();

		try {

			double sma = datapull.getIndicator("SMA", ticker);
			double ema = datapull.getIndicator("EMA", ticker);
			double rsi = datapull.getIndicator("RSI", ticker);
			double macd = datapull.getIndicator("MACD", ticker);
			double obv = datapull.getIndicator("OBV", ticker);

			if (storedAlerts.containsKey(ticker + "SMA")) {
				
				calculatedAlerts.put(ticker + "_SMA" + "_below",
						alert.belowPriceAlert(sma, (double) ((Map) storedAlerts.get(ticker + "SMA")).get("below")));
				calculatedAlerts.put(ticker + "_SMA" + "_above",
						alert.abovePriceAlert(sma, (double) ((Map) storedAlerts.get(ticker + "SMA")).get("above")));
			}

			if (storedAlerts.containsKey(ticker + "EMA")) {

				calculatedAlerts.put(ticker + "_EMA" + "_below",
						alert.belowPriceAlert(ema, (double) ((Map) storedAlerts.get(ticker + "EMA")).get("below")));
				calculatedAlerts.put(ticker + "_EMA" + "_above",
						alert.abovePriceAlert(ema, (double) ((Map) storedAlerts.get(ticker + "EMA")).get("above")));

			}

			if (storedAlerts.containsKey(ticker + "RSI")) {

				calculatedAlerts.put(ticker + "_RSI" + "_below",
						alert.belowPriceAlert(rsi, (double) ((Map) storedAlerts.get(ticker + "RSI")).get("below")));
				calculatedAlerts.put(ticker + "_RSI" + "_above",
						alert.abovePriceAlert(rsi, (double) ((Map) storedAlerts.get(ticker + "RSI")).get("above")));

			}

			if (storedAlerts.containsKey(ticker + "MACD")) {

				calculatedAlerts.put(ticker + "_MACD" + "_below",
						alert.belowPriceAlert(macd, (double) ((Map) storedAlerts.get(ticker + "MACD")).get("below")));
				calculatedAlerts.put(ticker + "_MACD" + "_above",
						alert.abovePriceAlert(macd, (double) ((Map) storedAlerts.get(ticker + "MACD")).get("above")));

			}

			if (storedAlerts.containsKey(ticker + "OBV")) {

				calculatedAlerts.put(ticker + "_OBV" + "_below",
						alert.belowPriceAlert(obv, (double) ((Map) storedAlerts.get(ticker + "OBV")).get("below")));
				calculatedAlerts.put(ticker + "_OBV" + "_above",
						alert.abovePriceAlert(obv, (double) ((Map) storedAlerts.get(ticker + "OBV")).get("above")));

			}

		} catch (InterruptedException e) {
			System.out.println("Cannot calculate the alert.");
			e.printStackTrace();
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

	public double getSma() {
		return sma;
	}

	public void setSma(double sma) {
		this.sma = sma;
	}

	public double getEma() {
		return ema;
	}

	public void setEma(double ema) {
		this.ema = ema;
	}

	public double getRsi() {
		return rsi;
	}

	public void setRsi(double rsi) {
		this.rsi = rsi;
	}

	public double getMacd() {
		return macd;
	}

	public void setMacd(double macd) {
		this.macd = macd;
	}

	public double getObv() {
		return obv;
	}

	public void setObv(double obv) {
		this.obv = obv;
	}

	public HashMap<String, Boolean> getCalculatedAlerts() {

		return calculatedAlerts;
	}
	
	public HashMap<String, HashMap<String, Double>> getStoredAlerts() {
	    return storedAlerts;
	}


}
