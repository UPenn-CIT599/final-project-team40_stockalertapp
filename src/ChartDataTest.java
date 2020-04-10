
import java.time.DayOfWeek;
import java.time.LocalDate;

import org.junit.jupiter.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChartDataTest {
    ChartData chart;
    RandomPriceGenerator gen;
 
    
    @Test 
    void testSetValues() {
        gen = new RandomPriceGenerator("test");
        chart = new ChartData(gen, 300, 300, 30, 5);
        assert(chart.maxVal > chart.minVal);
    }
    
    @Test
    void testYAxisPlotLimits() {
        gen = new RandomPriceGenerator("test");
        chart = new ChartData(gen, 300, 300, 30, 5);
        assertEquals(chart.maxVal * chart.slopeY + chart.interceptY, 5);
        assertEquals(Math.round(chart.minVal * chart.slopeY + chart.interceptY), 300);
    }
    
    @Test
    void testXAxisPlotLimits() {
        gen = new RandomPriceGenerator("test");
        chart = new ChartData(gen, 300, 300, 30, 5);
        double firstX = chart.getPlotPoints().firstEntry().getValue()[0];
        double lastX = chart.getPlotPoints().lastEntry().getValue()[0];
        assertEquals(firstX, 30 + chart.deltaX);
        assertEquals(lastX, 330);
    }
    
    @Test
    void testPlotPoints() {
        gen = new RandomPriceGenerator("test");
        chart = new ChartData(gen, 300, 300, 30, 5);
        assertEquals(chart.getPlotPoints().size(), gen.getHistoricalPrices().size());
    }
    
    @Test
    void testChangeStock() {
        gen = new RandomPriceGenerator("test");
        RandomPriceGenerator gen2 = new RandomPriceGenerator("test2");
        chart = new ChartData(gen, 300, 300, 30, 5);
        chart.changeStock(gen2);
        assertEquals(gen2.getMax() * chart.slopeY + chart.interceptY, 5);
    }
    
    // 10 40 150 500 2000 over
    @Test
    void testXAxisTicksSplits() {
        gen = new RandomPriceGenerator("test");
        gen.setTimeLength(9);
        chart = new ChartData(gen, 300, 300, 30, 5);
        assertEquals(chart.getXAxisTicks().size(), 9); // one tick per day
        
        gen.setTimeLength(30);
        long numTicks = gen.getHistoricalPrices().keySet().stream().filter(x -> x.getDayOfMonth()%2 == 0).count();
        chart.changeStock(gen);
        assertEquals(chart.getXAxisTicks().size(), numTicks); // one tick every even numbered day
        
        gen.setTimeLength(100);
        chart.changeStock(gen);
        numTicks = gen.getHistoricalPrices().keySet().stream().filter(x -> x.getDayOfWeek() == DayOfWeek.MONDAY).count();
        assertEquals(chart.getXAxisTicks().size(), numTicks);
        
        gen.setTimeLength(250);
        chart.changeStock(gen);
        numTicks = gen.getHistoricalPrices().keySet().stream().map(LocalDate::getMonth).distinct().count();
        assertEquals(chart.getXAxisTicks().size(), numTicks);
        
        gen.setTimeLength(750);
        chart.changeStock(gen);
        numTicks = 9;
        assertEquals(chart.getXAxisTicks().size(), numTicks);
        
        gen.setTimeLength(2500);
        chart.changeStock(gen);
        numTicks = 7;
        assertEquals(chart.getXAxisTicks().size(), numTicks);
    }
    
    

}
