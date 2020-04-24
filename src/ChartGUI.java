import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * CHARTGUI CLASS:
 * 
 * Creates a Chart based on data held in a ChartData object drawn on the panel
 * to be placed in a frame. Besides setting the CharData object this class is
 * primarily responsible for rendering the image on the panel. Details can be
 * changed without destroying and constructing a new object by using the
 * changeStock() method. All data is derived from a Stock object.
 * 
 * @author robertstanton
 *
 */

public class ChartGUI extends JPanel {

    private final int buffer = 5; // for edge spacing
    private int maxLabelSize = 40;
    private ChartData chartData;
    private Dimension chartD;
    private int xOffset; // for x axis label
    private int yOffset; // for y axis label

    private int netWidth;
    private int netHeight;
    private BufferedImage image;
    private Graphics2D g2d;
    private TreeMap<LocalDate, OHLCV> stockData;

    private Color background = Color.WHITE;
    private Color foreground = Color.BLACK;
    private Color priceColor = Color.BLUE;
    private Color gridColor = Color.LIGHT_GRAY;
    private int ENDCAPS = BasicStroke.CAP_BUTT;
    private int LINEJOIN = BasicStroke.JOIN_BEVEL;
    private float[] dash = { 3f };
    private BasicStroke dashStroke = new BasicStroke(1, ENDCAPS, LINEJOIN, 3f, dash, 3f);
    private Color[] colorPallette = new Color[] { Color.RED, Color.ORANGE, Color.MAGENTA, Color.YELLOW, Color.GREEN };

    private boolean sma50;
    private boolean sma200;
    private boolean sma100;
    private boolean obv;

    // private Graphics2D gdd;

    /**
     * CHARTGUI METHOD: This method constructs a ChartGUI object with a default size
     * of width 800, height 400;
     * 
     * @param s
     */
    public ChartGUI(TreeMap<LocalDate, OHLCV> s) {
        this(s, new Dimension(1000, 800));
    }

    /**
     * CHARTGUI METHOD: This method constructs a ChartGUI object with the given
     * Stock and the given Dimension.
     * 
     * @param s
     * @param d
     */
    public ChartGUI(TreeMap<LocalDate, OHLCV> s, Dimension d) {
        stockData = s;
        chartD = d;
        image = new BufferedImage((int) chartD.getWidth(), (int) chartD.getHeight(), BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();

        if (chartD.getWidth() < 100 || chartD.getHeight() < 100) {
            chartD = new Dimension(800, 400);

        }
        setPreferredSize(chartD);
        setXOffset();
        setYOffset();
        
        // chartData = new ChartData(stockData, netWidth, netHeight, xOffset, buffer);
        sma50 = false;
        sma100 = false;
        sma200 = false;
        obv = false;

        // createChartImage();
        changeDateRange(60); // set default date range to 5 years
    }

    /**
     * SETYOFFSET METHOD: This method sets spacing for the x axis labeling.
     * 
     */

    public void setYOffset() {
        int minYOffset = g2d.getFontMetrics().getHeight();
        yOffset = (int) (chartD.getHeight() * 0.10);
        if (yOffset > minYOffset) {
            yOffset = minYOffset + 10;
        }
        netHeight = (int) (chartD.getHeight() - yOffset - buffer);
    }

    /**
     * SETXOFFSET METHOD: This method sets spacing for the y axis labeling.
     */

    public void setXOffset() {
        int minXOffset = 10;
        if (minXOffset > maxLabelSize) {
            xOffset = minXOffset;
        } else {
            xOffset = Math.min(maxLabelSize, (int) (0.10 * chartD.getWidth()));
        }
        netWidth = (int) chartD.getWidth() - xOffset - buffer;
    }

    /**
     * GETSIZEDFONT METHOD: This method adjusts the font size for higher priced
     * securities to allow the pricing to fit neatly in the space.
     * 
     * @return the new font
     */
    public Font getSizedFont() {
        double idealFontRatio = 1.0;
        int numXAxisTicks = chartData.getXAxisTicks().size();
        FontMetrics metric = g2d.getFontMetrics();
        int stringWidth = metric.stringWidth(chartData.getXAxisTicks().firstKey().toString());
        if (stringWidth * (numXAxisTicks + 1) >= netWidth) {
            int idealStringWidth = (int) (netWidth / (numXAxisTicks + 1) * .95);
            idealFontRatio = (double) idealStringWidth / stringWidth;
        }

        String maxVal = String.format("%.2f", chartData.getMax());
        int maxWidth = metric.stringWidth(maxVal);
        if (maxWidth >= 40) {
            double ratio = ((double) 40 / maxWidth);
            idealFontRatio = ratio < idealFontRatio ? ratio : idealFontRatio;
        }

        Font curFont = g2d.getFont();
        return new Font(curFont.getFontName(), curFont.getStyle(), (int) (curFont.getSize() * idealFontRatio));
    }

    /**
     * CHANGESTOCK METHOD: This method changes all details to allow for plotting
     * different stocks without the need for new ChartData and ChartGUI objects.
     * 
     * @param s
     */
    public void changeStock(TreeMap<LocalDate, OHLCV> s) {
        stockData = s;
        // chartData = new ChartData(stockData, netWidth, netHeight, xOffset, buffer);
        // createChartImage();
        changeDateRange(60); // sets default date range to 5 years
        repaint();
    }
    
    
    public void changeDateRange(int monthsBack) {
        TreeMap<LocalDate, OHLCV> shortData = new TreeMap<>();
        LocalDate refDate = stockData.lastKey().minusMonths(monthsBack);
        if(monthsBack == 0) {
            refDate = stockData.firstKey();
        }
        for(LocalDate d : stockData.keySet()) {
            if(d.isAfter(refDate)) {
                shortData.put(d, stockData.get(d));
            }
        }
        
        chartData = new ChartData(shortData, netWidth, netHeight, xOffset, buffer);
        createChartImage();
        repaint();
    }

    // ====================================================================================
    // Begin Draw Methods
    // ====================================================================================

    /**
     * PAINTCOMPONENT METHOD: Implementation of the Graphics Interface. Paints the
     * Chart on the panel.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image img = (Image) image;
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    /**
     * Create image of chart to Display standard price chart.
     * 
     * @return
     */
    public void createChartImage() {
        image = new BufferedImage((int) chartD.getWidth(), (int) chartD.getHeight(), BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D) image.getGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, (int) chartD.getWidth(), (int) chartD.getHeight());
        drawYAxis(g2d);
        drawXAxis(g2d);
        drawBorder(g2d);
        drawPlotPoints(g2d);
        
        LocalDate startDate = chartData.getPlotPoints().firstKey();
        
        if (sma50) {
            g2d.setColor(new Color(134, 206, 136));
            drawSMA(g2d, 50, startDate);
        }

        if (sma100) {
            g2d.setColor(new Color(255, 153, 132));
            drawSMA(g2d, 100, startDate);
        }

        if (sma200) {
            g2d.setColor(Color.ORANGE);
            drawSMA(g2d, 200, startDate);
        }
    }

    /**
     * DRAWXAXIS METHOD: Draws the x axis labels, ticks, and grid lines.
     * 
     * @param g
     */
    public void drawXAxis(Graphics2D g) {

        for (Map.Entry entry : chartData.getXAxisTicks().entrySet()) {
            String label = entry.getKey().toString();
            Double val = (Double) entry.getValue();
            int stringLen = g.getFontMetrics().stringWidth(label);
            if (val > xOffset && val < chartD.getWidth() - buffer) {
                g.setColor(foreground);
                g.drawString(entry.getKey().toString(), val.intValue() - stringLen / 2,
                        (int) (chartD.getHeight() - yOffset / 2));
                drawVGridLine(g, val);
            }
        }
    }

    /**
     * DRAWYAXIS METHOD: Draws the y axis labels, ticks, and gridlines.
     * 
     * @param g
     */
    public void drawYAxis(Graphics2D g) {
        // from 0 to yOffset
        Font sizedFont = getSizedFont();
        g.setFont(sizedFont);

        for (Map.Entry entry : chartData.getYAxisTicks().entrySet()) {
            Double val = (Double) entry.getValue();
            if (val > buffer && val < (chartD.getHeight() - yOffset * 2)) {
                g.setColor(foreground);
                g.drawString(entry.getKey().toString(), 2, val.intValue());
                drawHGridLine(g, val);
            }
        }
    }

    /**
     * DRAWBORDER METHOD: Draws the border for the Charting area.
     * 
     * @param g
     */
    public void drawBorder(Graphics2D g) {
        BasicStroke stroke = new BasicStroke(1);
        g.setColor(foreground);
        g.setStroke(stroke);
        g.drawLine(xOffset, (int) (chartD.getHeight() - yOffset), (int) (chartD.getWidth() - buffer),
                (int) (chartD.getHeight() - yOffset));
        g.drawLine(xOffset, buffer, xOffset, (int) (chartD.getHeight() - yOffset));
    }

    /**
     * DRAWHGRIDLINE METHOD: Draws a horizontal dashed gridline.
     * 
     * @param g
     * @param yVal
     */
    public void drawHGridLine(Graphics2D g, double yVal) {
        g2d.setColor(gridColor);
        // BasicStroke stroke = new BasicStroke(1, ENDCAPS, LINEJOIN, 5f, dash, 5f);
        g2d.setStroke(dashStroke);
        Line2D.Double lineSeg = new Line2D.Double((double) xOffset, yVal, chartD.getWidth() - buffer, yVal);
        g2d.draw(lineSeg);
    }

    /**
     * DRAWVGRIDLINE METHOD: Draws a vertical dashed gridline.
     * 
     * @param g
     * @param xVal
     */
    public void drawVGridLine(Graphics2D g, double xVal) {
        g2d.setColor(gridColor);
        BasicStroke stroke = new BasicStroke(1, ENDCAPS, LINEJOIN, 5f, dash, 5f);
        g2d.setStroke(stroke);
        Line2D.Double lineSeg = new Line2D.Double(xVal, buffer, xVal, chartD.getHeight() - yOffset);
        g2d.draw(lineSeg);
    }

    /**
     * DRAWPLOTPOINTS METHOD: Plots the stock price based on the ChartData
     * plotPoints (x, y) coordinates.
     * 
     * @param g
     */
    public void drawPlotPoints(Graphics2D g) {
        BasicStroke stroke = new BasicStroke(1);
        g.setStroke(stroke);
        g.setColor(priceColor);
        Double[] xy = chartData.getPlotPoints().firstEntry().getValue();
        double prevX = xy[0];
        double prevY = xy[1];
        for (Double[] coord : chartData.getPlotPoints().values()) {
            double x = coord[0];
            double y = coord[1];
            Line2D.Double lineSeg = new Line2D.Double(prevX, prevY, x, y);
            g.draw(lineSeg);
            prevX = x;
            prevY = y;
        }
    }
    
    /**
     * Computes the simple moving average based on entire dataset.
     * @param window
     * @return
     */
    private TreeMap<LocalDate, Double> calculateSMA(Integer window) {
        TreeMap<LocalDate, Double> smaVals = new TreeMap<>();
        
        int startIndex = 0;
        int endIndex = window - 1;
        LocalDate[] dateArray = stockData.keySet().toArray(new LocalDate[] {});
        
        if(window < dateArray.length) {
            double sumWindow = 0;
            for(int i = 0; i < window; i++) {
                sumWindow += stockData.get(dateArray[i]).close;
            }
            smaVals.put(dateArray[window - 1], sumWindow / window);
            while(endIndex < dateArray.length - 1) {
                startIndex++;
                endIndex++;
                double removeVal = stockData.get(dateArray[startIndex - 1]).close;
                double addVal = stockData.get(dateArray[endIndex]).close;
                sumWindow = sumWindow - removeVal + addVal;
                smaVals.put(dateArray[endIndex], sumWindow / window);
            }
        } else {
            double sumWindow = 0;
            for(LocalDate d : stockData.keySet()) {
                sumWindow += stockData.get(d).close;
            }
            smaVals.put(stockData.lastKey(), sumWindow / stockData.size());
        }
        return smaVals;
    }

    /**
     * Plot simple moving average based on window.
     * 
     * @param window
     */
    public void drawSMA(Graphics2D g, Integer window, LocalDate startDate) {
        Graphics2D gdd = g;
        BasicStroke stroke = new BasicStroke(1);
        gdd.setStroke(stroke);
        
        TreeMap<LocalDate, Double> smaPrices = calculateSMA(window);
        LocalDate[] datesInRange = chartData.getPlotPoints().keySet().toArray(new LocalDate[] {});
        
        double x;
        double y;
        double prevX = 0;
        double prevY = 0;
        
        for(int i = 0; i < datesInRange.length; i++) {
            
            if(smaPrices.containsKey(datesInRange[i])) {
                
                x = chartData.convertIndexToPlot(i);
                y = chartData.convertPriceToPlot(smaPrices.get(datesInRange[i]));
                
                if(y < netHeight && y > buffer) {
                    
                    if(prevX == 0 && prevY == 0) {
                        prevX = x;
                        prevY = y;
                    }
                    
                    Line2D.Double lineSeg = new Line2D.Double(prevX, prevY, x, y);
                    gdd.draw(lineSeg); 
                    
                    prevX = x;
                    prevY = y;
                }
            }
        }
    }

    /**
     * toggle sma 50 on or off.
     */
    public void toggleSMA50() {
        sma50 = !sma50;
        createChartImage();
        repaint();
    }

    /**
     * toggle sma 200 on or off.
     */
    public void toggleSMA200() {
        sma200 = !sma200;
        createChartImage();
        repaint();
    }

    /**
     * toggle sma 100 on or off.
     */
    public void toggleSMA100() {
        sma100 = !sma100;
        createChartImage();
        repaint();
    }
}
