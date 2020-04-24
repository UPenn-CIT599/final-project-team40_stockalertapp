import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/***
 * CONSOLEUI CLASS:
 * 
 * This class prompts the user entry of the stock ticker and desired financial
 * indicator.
 * 
 * @author robertstanton
 *
 */

public class ConsoleUI {

	private Scanner scan;
	private ArrayList<Stock> stocks;
	private File tickerList;

	/**
	 * CONSOLEUI METHOD:
	 * 
	 * This method prompts the user for stock ticker and desired financial indicator.
	 */
	public ConsoleUI() {
		scan = new Scanner(System.in);
		stocks = new ArrayList<>();
		tickerList = new File("portfolio3.ser");
	}
	
	/**
	 * Set Program start Message
	 */
	public void init() {
	    System.out.println("\t\t Welcome to the Stock Alert Application ");
	    System.out.println("\t<------------------------------------------------------>");
	    System.out.println("");
	    
	    selectionMenu();
	}
	
	/**
	 * Prompt for selection to execute action.
	 * 
	 */
	public void selectionMenu() {
	    System.out.println("1. Add Stock");
	    System.out.println("2. Remove Stock");
	    System.out.println("3. Load List of Stocks (must be serialized object)");
	    System.out.println("4. Check for Alerts");
	    System.out.println("5. Quit");
	    System.out.println("");
	    System.out.print("Please enter the number for your selection : ");
	    
	    processResponse();
	}
	
	/**
	 * Process user Responses and handle bad input.
	 */
	public void processResponse() {
	    boolean invalid = true;
	    String ticker = "";
	    while(invalid) {
	        if(scan.hasNextInt()) {
	            Integer res = scan.nextInt();
	            switch(res) {
	            case 1: 
	                
	                System.out.print("Please enter a stock ticker to add : ");
	                while(ticker.isBlank()) {
	                    ticker = scan.nextLine();
	                }
	                System.out.println("");
	                System.out.println("adding stock " + ticker);
                    addStock(ticker);
	                break;
	                
	            case 2:
	                
	                System.out.print("Please enter stock to remove : ");
	                while(ticker.isEmpty()) {
	                    ticker = scan.nextLine();
	                }
	                System.out.println("");
                    removeStock(ticker);
	                break;
	            
	            case 3:
	                System.out.println("");
	                System.out.println("loading stocks ... ");
	                loadStocks();
	                break;
	                
	            case 4:
	                
	                checkAlerts();
	                break;
	                
	            case 5:
	                
	                quit();
	                break;
	                
	            default:
	                System.out.println("");
	                System.out.println("Please select an option from the menu");
	                System.out.println("");
	                selectionMenu();
	            }
	            invalid = false;
	        }else {
	            String res = scan.nextLine();
	            System.out.println("Please enter a number between 1 and 5  : ");
	        }
	    }
	}
	
	/**
	 * Adds a new stock object to the stock list.
	 * @param ticker
	 */
	public void addStock(String ticker) {
	    System.out.println("");
	    System.out.println("fetching data ... ");
	    try {
            Stock stock = new Stock(ticker);
            stocks.add(stock);
            System.out.println(stock.getTicker() + " has been added to your list.");
            System.out.println("");
            
        } catch (FileNotFoundException | InterruptedException e) {
            if(e instanceof FileNotFoundException) {
                System.out.println("Unable to find " + ticker);
            }
            if(e instanceof InterruptedException) {
                System.out.println("Data collection stopped ... ");
            }
        } catch (Exception end) {
            System.out.println("unable to find " + ticker);
        }
	    selectionMenu();
	}
	
	/**
	 * check for alert triggers in stock list
	 */
	public void checkAlerts() {
	    System.out.println("");
	    if(stocks.size() > 0) {
	        System.out.println("Checking for alerts ... ");
	        System.out.println("");
	        for(Stock stock : stocks) {
	            String ticker = stock.getTicker();
	            System.out.println("");
	            System.out.println(" + start " + ticker + " alerts");
	            //check ema sma divergence
	            double emaSmaDiv = stock.getEma() / stock.getSma() - 1;
	            if(emaSmaDiv > .02) {
	                System.out.println("\t\t" + ticker + " : EMA leading SMA | " + emaSmaDiv );
	            }
	            if(emaSmaDiv < -.02) {
                    System.out.println("\t\t" + ticker + " : EMA trailing SMA | " + emaSmaDiv );
                }
	            
	            //check RSI
	            double rsiVal = stock.getRsi();
	            if(rsiVal >= 70) {
	                System.out.println("\t\t" + ticker + " : RSI overbought | " + rsiVal);
	            }
	            if(rsiVal <= 30) {
	                System.out.println("\t\t" + ticker + " : RSI oversold | " + rsiVal);
	            }
	            
	            //check MACD
	            double macd = stock.getMacd();
	            if(macd < .5 && macd > -.5) {
	                System.out.println("\t\t" + ticker + " : MACD recent cross | " + macd);
	            }
	            System.out.println(" - end " + ticker + "alerts");
	            System.out.println("");
	        }
	            
	    } else {
	        System.out.println("No stocks are loaded in your stock list");
	        System.out.println("");
	    }
	    selectionMenu();
	}
	
	/**
	 * Removes an inputed ticker from the stocks list.
	 * @param ticker
	 */
	public void removeStock(String ticker) {
	    System.out.println("");
	    if(stocks.size() > 0) {
	        for(int i = 0; i < stocks.size(); i++) {
	            if(stocks.get(i).getTicker().equals(ticker)) {
	                stocks.remove(stocks.get(i));
	                System.out.println(ticker + " has been removed.");
	            }
	        }
	    }
	    System.out.println("");
	    selectionMenu();
	}
	
	/**
	 * Loads a list of stocks and creates respective Stock object for each. 
	 */
	public void loadStocks() {
	    System.out.println("");
	    if(tickerList.exists()) {
	        FileInputStream f;
	        ArrayList<Object> listOfTickers;
            try {
                f = new FileInputStream(tickerList);
                ObjectInputStream o = new ObjectInputStream(f);
                Object source = o.readObject();
                if(source instanceof ArrayList<?>) {
                    listOfTickers = (ArrayList<Object>) source;
                    for(Object tick : listOfTickers) {
                        if(tick instanceof Stock) {
                            
                            stocks.add((Stock) tick);
                            System.out.println(((Stock) tick).getTicker() + " added to stock list.");
                        }
                    }
                }
                
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Unable to load file " + tickerList);
            }
	    } else {
	        System.out.println(tickerList.getAbsolutePath() + " does not exist.");
	    }
	    System.out.println("");
	    selectionMenu();
	}
	
	/**
	 * exits the UI and saves the list to tickerList.ser.
	 */
	public void quit() {
	    System.out.println("");
	    System.out.println("Thank you for using the Stock Alert Catcher");
	    saveList();
	    scan.close();
	}
	
	/**
	 * Saves a serialized ArrayList to tickerList.ser.  
	 */
	public void saveList() {
	    System.out.println("");
	    FileOutputStream f;
        try {
            f = new FileOutputStream(tickerList);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(stocks);
            o.close();
            f.close();
            System.out.println("file saved as : " + tickerList.getName());
        } catch (IOException e) {
            System.out.println("Unable to save File");
        }
	}
	
	public static void main(String[] args) {
	    String testTicker = "";
	    System.out.println(testTicker.length() + " " + testTicker.isBlank() + " "+ testTicker.isEmpty());
	    
	    ConsoleUI app = new ConsoleUI();
	    app.init();
	}
}
