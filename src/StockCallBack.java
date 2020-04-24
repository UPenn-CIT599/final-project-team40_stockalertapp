
/**
 * for Use in passing a private class to call back to a parent window.
 * @author robertstanton
 *
 */
public interface StockCallBack {
    
    /**
     * Add a stock .
     * @param ticker
     */
    public void addStock(String ticker);
    
    /**
     * Change the stock.
     * @param s
     */
    public void changeStock(Stock s);
    
    /**
     * Removes the stock.
     * @param s
     */
    public void removeStock(Stock s);
    
    /**
     * sets a new alert on the given stock.
     * @param s
     */
    public void setNewAlert(Stock s);
}
