import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
 
/**
 * CHARTDATA CLASS:
 * 
 * The ChartData class hold data pertaining the the Stock object, namely
 * historical data, that is used when plotting the Stock details. Additionally,
 * it calculates the scaling and axis labeling for the ChartGUI object.
 * 
 * The values stored in yAxisTicks and xAxisticks are intended to represent the
 * canvas plot points of the ChartGUI such that no extra adjustments are needed
 * to plot the point. This is why a xOffset and yBuffer value are required in
 * the constructor. Ultimately, the ChartGUI object should only be concerned
 * with visual representation while scaling and calculations are done by this
 * class.
 * 
 * @author robertstanton
 *
 */
public class ChartData {

	public double slopeY; 
	public double interceptY; 
	public double deltaX;
	private int width;
	private int height;
	private int xOffset;
	private int yBuffer;
	public double minVal; 
	public double maxVal; 
	private TreeMap<LocalDate, OHLCV> historicalBars;
	private TreeMap<LocalDate, Double[]> plotPoints; // {x, y}
	private TreeMap<LocalDate, Double> xAxisTicks;
	private TreeMap<Double, Double> yAxisTicks;

	/**
	 * CHARTDATA METHOD:
	 * Constructs relevant values for the x and y axis plotting from the inputed
	 * values. The width and height represent the displayable space of the chart.
	 * While xOffset and yBuffer are used to adjust those numbers to match the
	 * actual plot points of the ChartGUI creating this object.
	 * 
	 * @param s
	 * @param width   ChartGUI plotting width. Should not include axis labeling
	 *                space.
	 * @param height  ChartGUI plotting height. Should not include axis labeling
	 *                space.
	 * @param xOffset ChartGUI x axis spacing to allow for labeling of the Y AXIS.
	 *                Intended to move the 'charting area' along x axis to allow for
	 *                Y AXIS labels.
	 * @param yBuffer ChartGUI y axis spacing to allow for labeling of the X AXIS.
	 *                Intended to move the 'charting area' up along the y axis to
	 *                allow for X AXIS labels.
	 */
	public ChartData(TreeMap<LocalDate, OHLCV> bars, int width, int height, int xOffset, int yBuffer) {

	    historicalBars = bars;
		this.width = width;
		this.height = height;
		this.xOffset = xOffset;
		this.yBuffer = yBuffer;
		setValues();
	}

	/**
	 * SETTVALUES METHOD:
	 * Helper method for setting a variety of required values in a single line. Used
	 * in Constructor and changeStock method.
	 */
	public void setValues() {
		setMinMaxValues();
		setSlopeIntercept(width, height);
		setPlotPoints();
		setYAxisTicks();
		setXAxisTicks();
	}

	/**
	 * CHANGESTOCK METHOD:
	 * Changes values of the ChartData for different Stocks depending on view
	 * preference of user. This way only one Chart is needed.
	 * 
	 * @param s
	 */
	public void changeStock(TreeMap<LocalDate, OHLCV> bars) { // TODO: change to Stock when stock is done
		
	    historicalBars = bars;
		setValues();
	}

	/**
	 * SETMINMAXVALUES METHOD:
	 * Sets the minimum and maximum values of the Stocks pricing data
	 */
	private void setMinMaxValues() {
		minVal = historicalBars.values().stream().map(x -> x.low).min(Double::compareTo).get();
		maxVal = historicalBars.values().parallelStream().map(x -> x.high).max(Double::compareTo).get(); 
	}

	/**
	 * SETSLOPEINTERCEPT METHOD:
	 * Sets values for converting Stock prices to plot points
	 * 
	 * @param width
	 * @param height
	 */
	private void setSlopeIntercept(int width, int height) {
		slopeY = maxVal < 0 ? 0 : (-(height - yBuffer)) / (maxVal - minVal);
		interceptY = minVal < 0 ? 0 : (-(slopeY * maxVal) + yBuffer);
		deltaX = (double) (width) / (historicalBars.size()); 
	}

	/**
	 * ConvertPriceToPlot METHOD:
	 * Converts a given price to a Y Axis plot point. Formula : slopeY * price +
	 * interceptY.
	 * 
	 * @param price
	 * @return
	 */
	public double convertPriceToPlot(double price) {
		return Math.round(slopeY * price + interceptY);
	}
	
	public double convertIndexToPlot(int idx) {
	    return idx * deltaX + xOffset;
	}
	
	/**
	 * returns coordinates to plot for a given x y value pairing. x is the number element you are trying to plot and y is the value at that index.
	 * @param x
	 * @param y
	 * @return
	 */
	public double[] convertToPlot(double x, double y) {
		double xPlot = x * deltaX + xOffset;
		double yPlot = convertPriceToPlot(y);
		return new double[] { x, y };
	}

	/**
	 * SETPLOTPOINTS METHOD:
	 * Converts Stock price to plot point and stores in plotPoints data structure.
	 * Each Entry is keyed by the LocalDate and valued as a Double[x, y]
	 * coordinates.
	 */
	private void setPlotPoints() {
		plotPoints = new TreeMap<>();
		int x = 1;
		for (Map.Entry entry : historicalBars.entrySet()) { 
		    OHLCV bar = (OHLCV) entry.getValue();
			double curVal = convertPriceToPlot(bar.close);
			plotPoints.put((LocalDate) entry.getKey(), new Double[] { (deltaX * x + xOffset), curVal });
			x++;
		}
	}

	/**
	 * SETXAXISTICKS METHOD:
	 * Set number of x axis ticks, the plot point to paint them and their associated
	 * labels.
	 */
	private void setXAxisTicks() {
		xAxisTicks = new TreeMap<>();
		int dataLength = plotPoints.size();
		int x = 0;
		if (dataLength < 10) {
			// daily ticks
			for (Map.Entry dateEntry : plotPoints.entrySet()) {
				// cast objects to respective types
				LocalDate key = (LocalDate) dateEntry.getKey();
				double curTick = Math.round(x * deltaX) + xOffset;
				xAxisTicks.put(key, curTick);
				x++;
			}
		} else {
		    if(dataLength % 10 == 0) {
		        int delta = dataLength / 10;
		        x = delta;
		        ArrayList<LocalDate> keys = plotPoints.keySet().stream().collect(Collectors.toCollection(ArrayList::new));
		        while(x <= dataLength - delta) {
		            double curTick = Math.round(x * deltaX) + xOffset;
		            xAxisTicks.put(keys.get(x), curTick);
		            x += delta;
		        }
		    } else if(dataLength % 8 == 0) {
		        int delta = dataLength / 8;
                x = delta;
                ArrayList<LocalDate> keys = plotPoints.keySet().stream().collect(Collectors.toCollection(ArrayList::new));
                while(x <= dataLength - delta) {
                    double curTick = Math.round(x * deltaX) + xOffset;
                    xAxisTicks.put(keys.get(x), curTick);
                    x += delta;
                }
		    } else {
		        int delta = dataLength / 6;
                x = delta;
                ArrayList<LocalDate> keys = plotPoints.keySet().stream().collect(Collectors.toCollection(ArrayList::new));
                while(x <= dataLength - delta) {
                    double curTick = Math.round(x * deltaX) + xOffset;
                    xAxisTicks.put(keys.get(x), curTick);
                    x += delta;
                }
		    }
		}
	}

	/**
	 * SETYAXISTICKS METHOD:
	 * Splits Y axis into 6 regions and calculates their plot points from the range
	 * of Prices as well as assigning a label. All values rounded to first decimal
	 * place.
	 */
	private void setYAxisTicks() {
		yAxisTicks = new TreeMap<>();
		double range = maxVal - minVal;
		double diff = range / 6;
		double curVal = maxVal;
		while (curVal > minVal) {
			curVal = Math.round((curVal - diff) * 10.0) / 10.0;
			double valToPlot = this.convertPriceToPlot(curVal);
			yAxisTicks.put(curVal, valToPlot);
		}
	}

	/**
	 * GETXAXISTICKS METHOD:
	 * Returns a TreeMap for plotting x axis labels. The key is the label and the
	 * value is the plot point.
	 * 
	 * @return
	 */
	public TreeMap<LocalDate, Double> getXAxisTicks() {
		return xAxisTicks;
	}

	/**
	 * GETYAXISTICKS METHOD:
	 * Returns a TreeMap for plotting y axis labels. The key is the label and the
	 * value is the plot point.
	 * 
	 * @return
	 */
	public TreeMap<Double, Double> getYAxisTicks() {
		return yAxisTicks;
	}

	/**
	 * GETPLOTPOINTS METHOD:
	 * Returns a TreeMap for plotting price points on the Chart. The key is the date
	 * and the value is an array representing x, y coordinates to plot.
	 * 
	 * @return
	 */
	public TreeMap<LocalDate, Double[]> getPlotPoints() {
		return plotPoints;
	}
	
	/**
	 * GETMAX METHOD:
	 * returns max price of the data set
	 */
	public double getMax() {
	    return maxVal;
	}
	
	/**
	 * Get closing values as ArrayList<Double>.
	 */
	public ArrayList<Double> getClosingPrices() {
	    return historicalBars.values().stream().map(x -> x.close).collect(Collectors.toCollection(ArrayList::new));
	}
}
