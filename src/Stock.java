import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Stock {
    
    private String ticker;
    private TreeMap<LocalDate, Double> historicalPrices;
    
    public Stock() {
        this("test");
    }
    
    public Stock(String tick) {
        historicalPrices = new TreeMap<>();
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        // LocalDate endDate = LocalDate.of(2020, 1, 30);
        LocalDate endDate = startDate.plusDays((long)2400);
        List<LocalDate> dateRange = startDate.datesUntil(endDate).collect(Collectors.toList());
        setHistoricalPrices(dateRange);
        ticker = tick;
    }
    
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

