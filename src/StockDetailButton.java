import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Button wrapper class to store Stock object to help pass information to other components in display.
 * @author robertstanton
 *
 */
public class StockDetailButton extends JButton{
    private Stock stock;
    private Color backGroundColor = Color.DARK_GRAY;
    private Color foreGroundColor = Color.WHITE;
    private Color hoverColor = Color.GRAY;
    private Font tickerFont;
    
    /**
     * Constructs the JButton with a Stock object attribute as well as setting the background and foreground colors.
     * @param s
     */
    public StockDetailButton(Stock s) {
        stock = s;
        tickerFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
        
        this.setFont(tickerFont);
        this.setText(stock.getTicker());
        this.setBackground(backGroundColor);
        this.setForeground(foreGroundColor);
        this.setBorderPainted(false);
        this.setOpaque(true);
        this.setActionCommand("stockDetailButton");
    }
    
    /**
     * Changes the Stock object associated with this button;
     * @param s
     */
    public void changeStock(Stock s) {
        stock = s;
        setText(stock.getTicker());
    }
    
    /**
     * Returns the associated Stock object.
     * @return
     */
    public Stock getStock() {
        return stock;
    }
}
