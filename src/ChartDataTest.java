
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

class ChartDataTest {
     
    private final RandomPriceGenerator gen = new RandomPriceGenerator("SPY");
    private final ChartData chart = new ChartData(gen.getHistoricalBars(), 300, 300, 30, 5);
 
     
    @Test 
    void testSetValues() {
        assert(chart.maxVal > chart.minVal);
    }
    
    @Test
    void testYAxisPlotLimits() {
        assertEquals(chart.maxVal * chart.slopeY + chart.interceptY, 5);
        assertEquals(Math.round(chart.minVal * chart.slopeY + chart.interceptY), 300);
    }
    
    @Test
    void testXAxisPlotLimits() {
        double firstX = chart.getPlotPoints().firstEntry().getValue()[0];
        double lastX = chart.getPlotPoints().lastEntry().getValue()[0];
        assertEquals(firstX, 30 + chart.deltaX);
        assertEquals(lastX, 330);
    }
    
    @Test
    void testPlotPoints() {
        assertEquals(chart.getPlotPoints().size(), gen.getHistoricalPrices().size());
    }
    
    @Test
    void testChangeStock() {
        RandomPriceGenerator gen2 = new RandomPriceGenerator("test2");
        TreeMap<LocalDate, OHLCV> testBars = gen2.getHistoricalBars();
        chart.changeStock(testBars);
        double maxVal = testBars.values().stream().map(entry -> entry.high).max(Double::compareTo).get();
        assertEquals(chart.convertPriceToPlot(maxVal), 5);
    }
    
    // 10 40 150 500 2000 over
    @Test
    void testXAxisTicksSplits() {
        gen.setTimeLength(9);
        chart.changeStock(gen.getHistoricalBars());
        assertEquals(chart.getXAxisTicks().size(), 9); // one tick per day
        
        gen.setTimeLength(30);
        long numTicks = gen.getHistoricalPrices().keySet().stream().filter(x -> x.getDayOfMonth()%2 == 0).count();
        chart.changeStock(gen.getHistoricalBars());
        assertEquals(chart.getXAxisTicks().size(), numTicks); // one tick every even numbered day
        
        gen.setTimeLength(100);
        chart.changeStock(gen.getHistoricalBars());
        numTicks = gen.getHistoricalPrices().keySet().stream().filter(x -> x.getDayOfWeek() == DayOfWeek.MONDAY).count();
        assertEquals(chart.getXAxisTicks().size(), numTicks);
        
        gen.setTimeLength(250);
        chart.changeStock(gen.getHistoricalBars());
        numTicks = gen.getHistoricalPrices().keySet().stream().map(LocalDate::getMonth).distinct().count();
        assertEquals(chart.getXAxisTicks().size(), numTicks);
        
        gen.setTimeLength(750);
        chart.changeStock(gen.getHistoricalBars());
        numTicks = 9;
        assertEquals(chart.getXAxisTicks().size(), numTicks);
        
        gen.setTimeLength(2500);
        chart.changeStock(gen.getHistoricalBars());
        numTicks = 7;
        assertEquals(chart.getXAxisTicks().size(), numTicks);
    }
    
    

}
