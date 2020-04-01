import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ChartGUI extends JPanel {
    private final int buffer = 5;  // for edge spacing
    private int maxLabelSize = 40;
    private ChartData chartData;
    private Dimension chartD;
    private int xOffset; // for x axis label
    private int yOffset; // for y axis label
    
    private int netWidth;
    private int netHeight;
    private BufferedImage image;
    private Graphics2D g2d;
    private Stock stock;
    
    private Color background = Color.WHITE;
    private Color foreground = Color.BLACK;
    private Color priceColor = Color.BLUE;
    private Color gridColor = Color.LIGHT_GRAY;
    private int ENDCAPS = BasicStroke.CAP_BUTT;
    private int LINEJOIN = BasicStroke.JOIN_BEVEL;
    private float[] dash = {3f};
    private BasicStroke dashStroke = new BasicStroke(1, ENDCAPS, LINEJOIN, 3f, dash, 3f);
    private Color[] colorPallette = new Color[] {Color.RED, Color.ORANGE, Color.MAGENTA, Color.YELLOW, Color.GREEN};
    
    public ChartGUI(Stock s) {
        this(s, new Dimension(800, 400));
    }
    
    public ChartGUI(Stock s, Dimension d) {
        stock = s;
        chartD = d;
        image = new BufferedImage((int)chartD.getWidth(), (int)chartD.getHeight(), BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        if(chartD.getWidth() < 100 || chartD.getHeight() < 100) {
            chartD = new Dimension(800, 400);
            
        }
        setPreferredSize(chartD);
        setXOffset();
        setYOffset();
        
        chartData = new ChartData(s, netWidth, netHeight, xOffset, buffer);
    }
    
    public void setYOffset() {
        int minYOffset = g2d.getFontMetrics().getHeight();
        yOffset = (int)(chartD.getHeight() * 0.10);
        if(yOffset > minYOffset) {
            yOffset = minYOffset + 10;
        }
        netHeight = (int)(chartD.getHeight() - yOffset - buffer);
    }
    
    public void setXOffset() {
        int minXOffset = 10;
        if(minXOffset > maxLabelSize) {
            xOffset = minXOffset;
        } else {
            xOffset = Math.min(maxLabelSize, (int)(0.10 * chartD.getWidth()));
        }
        netWidth = (int)chartD.getWidth() - xOffset - buffer;
    }
    
    public Font getSizedFont() {
        int maxWidth = 0;
        for(Map.Entry entry : chartData.getYAxisTicks().entrySet()) {
            int stringWidth = g2d.getFontMetrics().stringWidth(entry.getKey().toString());
            maxWidth = Math.max(stringWidth, maxWidth);
        }
        Font newFont = g2d.getFont();
        if(maxWidth > xOffset) {
            Font curFont = newFont;
            newFont = new Font(curFont.getFontName(), curFont.getStyle(), (int)(curFont.getSize() * (xOffset/maxWidth) * 0.90));
        }
        return newFont;
    }
    
    public void reScale(Dimension newDim) {
        if(newDim.getWidth() > 400 || newDim.getHeight() > 100) {
            chartD = newDim;
            setPreferredSize(chartD);
            setXOffset();
            setYOffset();
            chartData = new ChartData(stock, netWidth, netHeight, xOffset, buffer);
            repaint();
        }
    }
    
    public void changeStock(Stock s) {
        stock = s;
        chartData = new ChartData(stock, netWidth, netHeight, xOffset, buffer);
        repaint();
    }
    
    // Begin Draw Methods
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        reScale(this.getSize());
        
        g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, (int)chartD.getWidth(), (int)chartD.getHeight());
        drawYAxis(g2d);
        drawXAxis(g2d);
        drawBorder(g2d);
        drawPlotPoints(g2d);
    }
    
    public void drawXAxis(Graphics2D g) {
        
        for(Map.Entry entry : chartData.getXAxisTicks().entrySet()) {
            String label = entry.getKey().toString();
            Double val = (Double)entry.getValue();
            int stringLen = g.getFontMetrics().stringWidth(label);
            if(val > xOffset && val < chartD.getWidth() - buffer) {
                g.setColor(foreground);
                g.drawString(entry.getKey().toString(), val.intValue() - stringLen/2, (int)(chartD.getHeight() - yOffset/2));
                drawVGridLine(g, val);
            }
            
        }
    }
    
    public void drawYAxis(Graphics2D g) {
        // from 0 to yOffset
        Font sizedFont = getSizedFont();
        g.setFont(sizedFont);
        
        for(Map.Entry entry : chartData.getYAxisTicks().entrySet()) {
            Double val = (Double)entry.getValue();
            if(val > buffer && val < (chartD.getHeight() - yOffset*2)) {
                g.setColor(foreground);
                g.drawString(entry.getKey().toString(), 2, val.intValue());
                drawHGridLine(g, val);
            }
            
        }
    }
    
    public void drawBorder(Graphics2D g) {
        BasicStroke stroke = new BasicStroke(1);
        g.setColor(foreground);
        g.setStroke(stroke);
        g.drawLine(xOffset, (int)(chartD.getHeight() - yOffset), (int)(chartD.getWidth() - buffer), (int)(chartD.getHeight() - yOffset));
        g.drawLine(xOffset, buffer, xOffset, (int)(chartD.getHeight() - yOffset));
    }
    
    public void drawHGridLine(Graphics2D g, double yVal) {
        g2d.setColor(gridColor);
        // BasicStroke stroke = new BasicStroke(1, ENDCAPS, LINEJOIN, 5f, dash, 5f);
        g2d.setStroke(dashStroke);
        Line2D.Double lineSeg = new Line2D.Double((double)xOffset, yVal,chartD.getWidth() - buffer, yVal);
        g2d.draw(lineSeg);
    }
    
    public void drawVGridLine(Graphics2D g, double xVal) {
        g2d.setColor(gridColor);
        BasicStroke stroke = new BasicStroke(1, ENDCAPS, LINEJOIN, 5f, dash, 5f);
        g2d.setStroke(stroke);
        Line2D.Double lineSeg = new Line2D.Double(xVal, buffer, xVal, chartD.getHeight() - yOffset);
        g2d.draw(lineSeg);
    }
    
    public void drawPlotPoints(Graphics2D g) {
        BasicStroke stroke = new BasicStroke(1);
        g.setStroke(stroke);
        g.setColor(priceColor);
        Double[] xy = chartData.getPlotPoints().firstEntry().getValue();
        double prevX = xy[0];
        double prevY = xy[1];
        for(Double[] coord : chartData.getPlotPoints().values()) {
            double x = coord[0];
            double y = coord[1];
            Line2D.Double lineSeg = new Line2D.Double(prevX, prevY, x, y);
            g.draw(lineSeg);
            prevX = x;
            prevY = y;
        }
    }
}
