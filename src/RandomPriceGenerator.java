import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class RandomPriceGenerator {
    private String ticker;
    private List<LocalDate> dateRange;
    private TreeMap<LocalDate, Double> historicalPrices;
    private Random generator;
    private LocalDate startDate;
    private LocalDate endDate;
    
    public RandomPriceGenerator(String ticker) {
        this.ticker = ticker;
        generator = new Random();
         
        endDate = LocalDate.now();
        startDate = endDate.minusDays(255);
        dateRange = startDate.datesUntil(endDate).collect(Collectors.toList());
        historicalPrices = new TreeMap<>();
        createHistoricalPrices();
        
    }
    
    private void createHistoricalPrices() {
        Double curPrice = generator.nextDouble() * 100;
        for(LocalDate day : dateRange) {
            curPrice = curPrice * (1 + generator.nextGaussian()/100);
            historicalPrices.put(day, curPrice);
        }
    }
    
    public TreeMap<LocalDate, Double> getHistoricalPrices() {
        return historicalPrices;
    }
    
    public Double getMax() {
        return historicalPrices.values().stream().max(Double::compareTo).get();
    }
    
    public Double getMin() {
        return historicalPrices.values().stream().min(Double::compareTo).get();
    }
    
    public static void main(String[] args) {
        RandomPriceGenerator gen = new RandomPriceGenerator("spy");
        System.out.println(gen.getHistoricalPrices());
    }
    
    public void setTimeLength(int x) {
        startDate = endDate.minusDays(x);
        dateRange = startDate.datesUntil(endDate).collect(Collectors.toList());
        historicalPrices = new TreeMap<>();
        createHistoricalPrices();
    }
} 
