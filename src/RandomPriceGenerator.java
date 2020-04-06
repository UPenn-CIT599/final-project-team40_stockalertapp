import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

/***
 * RANDOMPRICEGENERATOR CLASS:
 * This class gets the random prices for testing purposes.
 *
 */

public class RandomPriceGenerator {
    private String ticker;
    private List<LocalDate> dateRange;
    private TreeMap<LocalDate, Double> historicalPrices;
    private Random generator;
    private LocalDate startDate;
    private LocalDate endDate;
    
    /**
     * RANDOMPRICEGENERATOR METHOD:
     * This method generates a random price for testing purposes.
     * @param ticker
     */
    public RandomPriceGenerator(String ticker) {
        this.ticker = ticker;
        generator = new Random();
         
        endDate = LocalDate.now();
        startDate = endDate.minusDays(255);
        dateRange = startDate.datesUntil(endDate).collect(Collectors.toList());
        historicalPrices = new TreeMap<>();
        createHistoricalPrices();
        
    }
    
    /**
     * CREATEHISTORICALPRICES METHOD:
     * This method create historical prices for testing purposes.
     */
    private void createHistoricalPrices() {
        Double curPrice = generator.nextDouble() * 100;
        for(LocalDate day : dateRange) {
            curPrice = curPrice * (1 + generator.nextGaussian()/100);
            historicalPrices.put(day, curPrice);
        }
    }
    
    /***
     * GETHISTORICALPRICES METHOD:
     * This method returns the historical prices.
     * @return historical prices
     */
    public TreeMap<LocalDate, Double> getHistoricalPrices() {
        return historicalPrices;
    }
    
    /**
     * GETMAX METHOD:
     * This method returns the Max value.
     * @return max value
     */
    public Double getMax() {
        return historicalPrices.values().stream().max(Double::compareTo).get();
    }
    
    /**
     * GETMIN METHOD:
     * This method returns the Min value.
     * @return min value
     */
    public Double getMin() {
        return historicalPrices.values().stream().min(Double::compareTo).get();
    }
    
    /**
     * MAIN METHOD
     * @param args
     */
    public static void main(String[] args) {
        RandomPriceGenerator gen = new RandomPriceGenerator("spy");
        System.out.println(gen.getHistoricalPrices());
    }
    
    /**
     * SETTIMELENGTH METHOD:
     * This method sets the start date, date range, and runs the createHistoricalPrices method.
     * @param x
     */
    public void setTimeLength(int x) {
        startDate = endDate.minusDays(x);
        dateRange = startDate.datesUntil(endDate).collect(Collectors.toList());
        historicalPrices = new TreeMap<>();
        createHistoricalPrices();
    }
} 
