import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ChartData {
    
    private double slopeY;
    private double interceptY;
    private double deltaX;
    private int width;
    private int height;
    private int xOffset;
    private int yBuffer;
    private double minVal; // done
    private double maxVal; // done
    private Stock stock;  // done
    private TreeMap<LocalDate, Double[]> plotPoints; // {x, y}
    private TreeMap<LocalDate, Double> xAxisTicks;
    private TreeMap<Double, Double> yAxisTicks;
    
    
    public ChartData(Stock s, int width, int height, int xOffset, int yBuffer) {
        stock = s;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yBuffer = yBuffer;
        plotPoints = new TreeMap<>();
        xAxisTicks = new TreeMap<>();
        yAxisTicks = new TreeMap<>();
        setValues();
    }
    
    public void setValues() {
        setMinMaxValues();
        setSlopeIntercept(width, height);
        setPlotPoints();
        setYAxisTicks();
        setXAxisTicks();
    }
    
    public void changeStock(Stock s) {
        stock = s;
        setValues();
    }
    
    private void setMinMaxValues() {
        minVal = stock.getHistoricalPrices().values().stream().min(Double::compareTo).get();
        maxVal = stock.getHistoricalPrices().values().stream().max(Double::compareTo).get();
    }
    
    private void setSlopeIntercept(int width, int height) {
        slopeY = maxVal < 0 ? 0 : (-height) / (maxVal - minVal);
        interceptY = minVal < 0 ? 0 : ( -(slopeY * maxVal) + yBuffer);
        deltaX = (double)(width) / (stock.getHistoricalPrices().size()-1);
    }
    
    private double convertPriceToPlot(double price) {
        return Math.round(slopeY * price + interceptY);
    }
    
    private void setPlotPoints() {
        int x = 1;
        for(Map.Entry entry : stock.getHistoricalPrices().entrySet()) {
            double curVal = convertPriceToPlot((double)entry.getValue());
            plotPoints.put((LocalDate)entry.getKey(), new Double[] {(deltaX * x + xOffset), curVal});
            x++;
        }
    }
    
    private void setXAxisTicks() {
        int dataLength = plotPoints.size();
        int x = 0;
        if(dataLength < 10) {
            // daily ticks
            for(Map.Entry dateEntry : plotPoints.entrySet()) {
                // cast objects to respective types
                LocalDate key = (LocalDate)dateEntry.getKey();
                double curTick = Math.round(x * deltaX) + xOffset;
                xAxisTicks.put(key, curTick);
                x++;
            }
        }else if(dataLength < 40) {
            // semi daily ticks
            for(Map.Entry dateEntry : plotPoints.entrySet()) {
                // cast objects to respective types
                LocalDate key = (LocalDate)dateEntry.getKey();
                double curTick = Math.round(x * deltaX) + xOffset;
                if(key.getDayOfMonth() % 2 == 0) {
                    xAxisTicks.put(key, curTick);
                }
                x++;
            }
        }else if(dataLength < 150) {
            // weekly ticks
            for(Map.Entry dateEntry : plotPoints.entrySet()) {
                // cast objects to respective types
                LocalDate key = (LocalDate)dateEntry.getKey();
                double curTick = Math.round(x * deltaX) + xOffset;
                if(key.getDayOfWeek() == DayOfWeek.MONDAY) {
                    xAxisTicks.put(key, curTick);
                }
                x++;
            }
        }else if(dataLength < 500) {
            //monthly ticks
            Map<Month, List<LocalDate>> monthCollection = plotPoints.entrySet().stream()
                                                             .map((entry) -> entry.getKey())
                                                             .collect(Collectors.groupingBy(LocalDate::getMonth));
            
            for(LocalDate key : plotPoints.keySet()) {
                if(key == monthCollection.get(key.getMonth()).get(0)) {
                    double curTick = Math.round(x * deltaX) + xOffset;
                    xAxisTicks.put(key, curTick);
                }
                x++;
            }
            
                
        }else if (dataLength < 2000){
            // quarterly ticks
            Month currentMonth = plotPoints.firstKey().getMonth();
            for(Map.Entry dateEntry : plotPoints.entrySet()) {
                // cast objects to respective types
                LocalDate key = (LocalDate)dateEntry.getKey();
                double curTick = Math.round(x * deltaX) + xOffset;
                
                if(key.getMonthValue() % 3 == 1 && !key.getMonth().equals(currentMonth)) {
                    xAxisTicks.put(key, curTick);
                    currentMonth = key.getMonth();
                }
                x++;
            }
            
        }else {
            // yearly ticks
            int currentYear = plotPoints.firstKey().getYear();
            for(Map.Entry dateEntry : plotPoints.entrySet()) {
                // cast objects to respective types
                LocalDate key = (LocalDate)dateEntry.getKey();
                double curTick = Math.round(x * deltaX) + xOffset;
                
                if(key.getMonthValue() % 3 == 1 && key.getYear() != currentYear) {
                    xAxisTicks.put(key, curTick);
                    currentYear = key.getYear();
                }
                x++;
            }
        }
    }
    
    private void setYAxisTicks() {
        double range = maxVal - minVal;
        double diff = range / 6;
        double curVal = maxVal;
        while(curVal > minVal) {
            curVal = Math.round((curVal - diff) * 10.0) / 10.0;
            double valToPlot = this.convertPriceToPlot(curVal);
            yAxisTicks.put(curVal, valToPlot);
        }
    }
    
    public TreeMap<LocalDate, Double> getXAxisTicks() {
        return xAxisTicks;
    }
    
    public TreeMap<Double, Double> getYAxisTicks() {
        return yAxisTicks;
    }
     
    public TreeMap<LocalDate, Double[]> getPlotPoints() {
        return plotPoints;
    }

}

