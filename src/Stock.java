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
 * @author kravetsj
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
    private TreeMap datahistory; 

	/**
	 * Stock object with stock's ticker, price, and historical data
	 * @param ticker
	 * @throws FileNotFoundException 
	 * @throws InterruptedException 
	 */
	
	public Stock(String ticker) throws FileNotFoundException, InterruptedException{
		// TODO Auto-generated constructor stub
		
		DataPull t= new DataPull();
		 
		this.ticker= ticker;
		this.csv= ticker+".csv";
		//this.quote= t.getcurrentquote(ticker);
		
		try {
		this.quote= t.getcurrentquote(ticker);
		this.datahistory=getTreemap(csv, ticker);
		
		}
		catch(java.time.format.DateTimeParseException e){
			System.out.print("waiting 30 seconds due to api rate limit")  ;
			TimeUnit.SECONDS.sleep(30);
			this.datahistory=getTreemap(csv, ticker);
			this.quote= t.getcurrentquote(ticker);
		}
	}

	
	
	
	/**
	 * Tree map to store data from stock csv
	 * @param ticker
	 * @throws FileNotFoundException 
	 * @throws InterruptedException 
	 */
	private TreeMap<LocalDate, OHLCV> getTreemap(String csv, String Ticker) throws FileNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		
		System.gc(); 
		
		DataPull t= new DataPull();
		t.getcsv(ticker);
		File data= new File(csv);
		Scanner fileReader = new Scanner(data);
		fileReader.nextLine(); 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
		
		TreeMap datahistory =new TreeMap<>();
       
		
		
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
        fileReader.close();
        
        
        try
        { 	 
   		 System.gc(); 
            Files.deleteIfExists(Paths.get((csv))); 
            //System.out.println("Deleted old "+csv+ "file"); 
        } 
        catch(NoSuchFileException e) 
        { 
            System.out.println("No such file/directory exists"); 
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	 

        
		return datahistory;
	}
	
	public TreeMap<LocalDate, OHLCV> getDataHistory() {
	    return datahistory;
	}
	
	public String getTicker() {
	    return ticker;
	}

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		
		Stock t;
		
		t = new Stock("SLB");
		
		
		
		
		//get all entries
        Set<Map.Entry<LocalDate, OHLCV>> entries = t.datahistory.entrySet();
        
        //using for loop
        for(Map.Entry<LocalDate, OHLCV> entry : entries){
            System.out.println( entry.getKey() + " open " + entry.getValue().open +  " high " +entry.getValue().high+ " low " + entry.getValue().low +  " close " + entry.getValue().close+ " volume " + entry.getValue().volume);
        }
        
        System.out.print(t.quote)  ;
        
        
        t = new Stock("UPRO");
		
		
		
		
		//get all entries
        Set<Map.Entry<LocalDate, OHLCV>> entries1 = t.datahistory.entrySet();
        
        //using for loop
        for(Map.Entry<LocalDate, OHLCV> entry : entries1){
            System.out.println( entry.getKey() + " open " + entry.getValue().open +  " high " +entry.getValue().high+ " low " + entry.getValue().low +  " close " + entry.getValue().close+ " volume " + entry.getValue().volume);
        }
        
        System.out.print(t.quote)  ;
		
	}
}
