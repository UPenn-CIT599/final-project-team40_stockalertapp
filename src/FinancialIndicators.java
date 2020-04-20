import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/***
 * FINANCIALINDICATORS CLASS:
 * 
 * This class calculates the financial indicators for the selected stock.
 * 
 * @author Tiffany Choi
 *
 */
public class FinancialIndicators {

    /** 
     * Returns an array of Daily aboslute differences.  
     * @param data
     * @return
     */
    private ArrayList<Double> calculateDailyDiff(List<Double> data) {
        ArrayList<Double> retArray = new ArrayList<>();
        for(int i = 0; i < data.size()-2; i++) {
            retArray.add(data.get(i+1) - data.get(i));
        }
        return retArray;
    }
    
    /**
     * Returns daily returns.
     * @param data
     * @return
     */
    private ArrayList<Double> calculateDailyRet(List<Double> data) {
        ArrayList<Double> retArray = new ArrayList<>();
        for(int i = 0; i < data.size()-2; i++) {
            retArray.add(data.get(i+1) / data.get(i) - 1);
        }
        return retArray;
    }
    
	/**
	 * CALCULATESMA METHOD:
	 * This method calculates the Simple Moving Average (SMA).
	 */
	
	public ArrayList<Double> calculateSMA(List<Double> data, int window) {
	    ArrayList<Double> retArray = new ArrayList<>();
	    int startIndex = 0;
	    int endIndex = window - 1;
	    double sumWindow = 0;
	    for(int i = 0; i < window; i++) {
	        sumWindow += data.get(i);
	    }
	    retArray.add(sumWindow / window);
	    double removeVal = 0;
	    double addVal = 0;
	    while(endIndex < data.size() - 1) {
	        startIndex++;
            endIndex++;
            removeVal = data.get(startIndex - 1);
            addVal = data.get(endIndex);
	        sumWindow = sumWindow - removeVal + addVal;
	        retArray.add(sumWindow / window);
	    }
	    return retArray;
	}

	/**
	 * CALCULATERSI METHOD:
	 * This method calculates the Relative Strength Index (RSI).
	 */
	
	public ArrayList<Double> calculateRSI(List<Double> data, int window) {
		ArrayList<Double> retArray = new ArrayList<>();
		ArrayList<Double> dailyChg = calculateDailyDiff(data);
		int startIndex = 0;
		int endIndex = window - 1;
		double sumUp = 0;
		double sumDown = 0;
		double removeVal = 0;
		double addVal = 0;
		sumUp = dailyChg.stream().limit(window).filter(x -> x>0).mapToDouble(x -> x).sum();
		sumDown = dailyChg.stream().limit(window).filter(x -> x<0).mapToDouble(x -> x).sum();
		retArray.add(100 - 100/(sumUp/(-sumDown)));
		while(endIndex < dailyChg.size() - 2) {
		    startIndex++;
		    endIndex++;
		    removeVal = dailyChg.get(startIndex - 1);
		    addVal = dailyChg.get(endIndex);
		    
		    sumUp -= removeVal > 0 ? removeVal : 0;
		    sumUp += addVal > 0 ? addVal : 0;
		    
		    sumDown -= removeVal < 0 ? removeVal : 0;
		    sumDown += addVal < 0 ? addVal : 0;
		    
		    retArray.add(100 - 100/(sumUp/(-sumDown)));
		}
		return retArray;
	}

	/**
	 * CALCULATEMACD METHOD:
	 * This method calculates the Moving Average Convergence Divergence (MACD). Altered to Use SMA.
	 */
	
	public ArrayList<Double> calculateMACD(List<Double> data) {
		ArrayList<Double> longMA = calculateSMA(data, 26);
		ArrayList<Double> shortMA = calculateSMA(data, 13);
		ArrayList<Double> retArray = new ArrayList<>();
		for(int i = 0; i < (longMA.size() - shortMA.size()); i++) {
		    shortMA.remove(i);
		}
		
		for(int i = 0; i < longMA.size(); i++) {
		    retArray.add(shortMA.get(i) - longMA.get(i));
		}
		return retArray;
	}
	
	/**
	 * CALCULATEOBV METHOD:
	 * This method calculates the On-Balance Volume (OBV).
	 */

	public void calculateOBV() {
		;
	}
	
	public static void main(String[] args) {
	    Random generator = new Random();
        ArrayList<Double> dat = new ArrayList<>();
        for(double i = 1; i <= 10; i++) {
            dat.add(i);
        }
        
        FinancialIndicators fin = new FinancialIndicators();
        fin.calculateSMA(dat, 3).stream().forEach(System.out::println);
        
        ArrayList<Double> rets = new ArrayList<>();
        double price = 100;
        for(int i = 0 ; i< 100; i++) {
            price = price + .015*generator.nextGaussian();
            rets.add(price);
        }
        fin.calculateRSI(rets, 13).stream().forEach(System.out::println);
        fin.calculateMACD(rets).stream().forEach(System.out::println);
        
    }
}
