import java.awt.Color;

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
    
    /**
     * Constructs the JButton with a Stock object attribute as well as setting the background and foreground colors.
     * @param s
     */
    public StockDetailButton(Stock s) {
        stock = s;
        this.setText(stock.getTicker());
        this.setBackground(backGroundColor);
        this.setForeground(foreGroundColor);
        this.setBorderPainted(false);
        this.setOpaque(true);
        
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
