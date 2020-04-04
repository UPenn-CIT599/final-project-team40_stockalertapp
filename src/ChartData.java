import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
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

	public double slopeY; // change to private post testing
	public double interceptY; // change to private post testing
	public double deltaX;
	private int width;
	private int height;
	private int xOffset;
	private int yBuffer;
	public double minVal; // change to private post testing
	public double maxVal; // change to private post testing
	private Stock stock;
	private TreeMap<LocalDate, Double[]> plotPoints; // {x, y}
	private TreeMap<LocalDate, Double> xAxisTicks;
	private TreeMap<Double, Double> yAxisTicks;

	private RandomPriceGenerator gen; // remove when Stock is done

	/**
	 * Constructs relavent values for the x and y axis plotting from the inputed
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
	public ChartData(RandomPriceGenerator g, int width, int height, int xOffset, int yBuffer) {
		// stock = s; REINSTATE

		gen = g; // TODO: REMOVE WHEN STOCK CLASS COMPLETE

		this.width = width;
		this.height = height;
		this.xOffset = xOffset;
		this.yBuffer = yBuffer;
		setValues();
	}

	/**
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
	 * Changes values of the ChartData for different Stocks depending on view
	 * preference of user. This way only one Chart is needed.
	 * 
	 * @param s
	 */
	public void changeStock(RandomPriceGenerator g) { // TODO: change to Stock when stock is done
		// stock = s;
		gen = g;
		setValues();
	}

	/**
	 * Sets the minimum and maximum values of the Stocks pricing data
	 */
	private void setMinMaxValues() {
		minVal = gen.getMin(); // TODO: switch to Stock
		maxVal = gen.getMax(); // TODO: switch to Stock
	}

	/**
	 * Sets values for converting Stock prices to plot points
	 * 
	 * @param width
	 * @param height
	 */
	private void setSlopeIntercept(int width, int height) {
		slopeY = maxVal < 0 ? 0 : (-(height - yBuffer)) / (maxVal - minVal);
		interceptY = minVal < 0 ? 0 : (-(slopeY * maxVal) + yBuffer);
		deltaX = (double) (width) / (gen.getHistoricalPrices().size()); // TODO: switch to Stock
	}

	/**
	 * Converts a given price to a Y Axis plot point. Formula : slopeY * price +
	 * interceptY.
	 * 
	 * @param price
	 * @return
	 */
	private double convertPriceToPlot(double price) {
		return Math.round(slopeY * price + interceptY);
	}

	public double[] convertToPlot(double x, double y) {
		double xPlot = x * deltaX;
		double yPlot = convertPriceToPlot(y);
		return new double[] { x, y };
	}

	/**
	 * Converts Stock price to plot point and stores in plotPoints data structure.
	 * Each Entry is keyed by the LocalDate and valued as a Double[x, y]
	 * coordinates.
	 */
	private void setPlotPoints() {
		plotPoints = new TreeMap<>();
		int x = 1;
		for (Map.Entry entry : gen.getHistoricalPrices().entrySet()) { // TODO: switch to Stock
			double curVal = convertPriceToPlot((double) entry.getValue());
			plotPoints.put((LocalDate) entry.getKey(), new Double[] { (deltaX * x + xOffset), curVal });
			x++;
		}
	}

	/**
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
		} else if (dataLength < 40) {
			// semi daily ticks
			for (Map.Entry dateEntry : plotPoints.entrySet()) {
				// cast objects to respective types
				LocalDate key = (LocalDate) dateEntry.getKey();
				double curTick = Math.round(x * deltaX) + xOffset;
				if (key.getDayOfMonth() % 2 == 0) {
					xAxisTicks.put(key, curTick);
				}
				x++;
			}
		} else if (dataLength < 150) {
			// weekly ticks
			for (Map.Entry dateEntry : plotPoints.entrySet()) {
				// cast objects to respective types
				LocalDate key = (LocalDate) dateEntry.getKey();
				double curTick = Math.round(x * deltaX) + xOffset;
				if (key.getDayOfWeek() == DayOfWeek.MONDAY) {
					xAxisTicks.put(key, curTick);
				}
				x++;
			}
		} else if (dataLength < 500) {
			// monthly ticks
			Map<Month, List<LocalDate>> monthCollection = plotPoints.entrySet().stream().map((entry) -> entry.getKey())
					.collect(Collectors.groupingBy(LocalDate::getMonth));

			for (LocalDate key : plotPoints.keySet()) {
				if (key == monthCollection.get(key.getMonth()).get(0)) {
					double curTick = Math.round(x * deltaX) + xOffset;
					xAxisTicks.put(key, curTick);
				}
				x++;
			}

		} else if (dataLength < 2000) {
			// quarterly ticks
			Month currentMonth = plotPoints.firstKey().getMonth();
			for (Map.Entry dateEntry : plotPoints.entrySet()) {
				// cast objects to respective types
				LocalDate key = (LocalDate) dateEntry.getKey();
				double curTick = Math.round(x * deltaX) + xOffset;

				if (key.getMonthValue() % 3 == 1 && !key.getMonth().equals(currentMonth)) {
					xAxisTicks.put(key, curTick);
					currentMonth = key.getMonth();
				}
				x++;
			}

		} else {
			// yearly ticks
			int currentYear = plotPoints.firstKey().getYear();
			for (Map.Entry dateEntry : plotPoints.entrySet()) {
				// cast objects to respective types
				LocalDate key = (LocalDate) dateEntry.getKey();
				double curTick = Math.round(x * deltaX) + xOffset;

				if (key.getMonthValue() % 3 == 1 && key.getYear() != currentYear) {
					xAxisTicks.put(key, curTick);
					currentYear = key.getYear();
				}
				x++;
			}
		}
	}

	/**
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
	 * Returns a TreeMap for plotting x axis labels. The key is the label and the
	 * value is the plot point.
	 * 
	 * @return
	 */
	public TreeMap<LocalDate, Double> getXAxisTicks() {
		return xAxisTicks;
	}

	/**
	 * Returns a TreeMap for plotting y axis labels. The key is the label and the
	 * value is the plot point.
	 * 
	 * @return
	 */
	public TreeMap<Double, Double> getYAxisTicks() {
		return yAxisTicks;
	}

	/**
	 * Returns a TreeMap for plotting price points on the Chart. The key is the date
	 * and the value is an array representing x, y coordinates to plot.
	 * 
	 * @return
	 */
	public TreeMap<LocalDate, Double[]> getPlotPoints() {
		return plotPoints;
	}
}
