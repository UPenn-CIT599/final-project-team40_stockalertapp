
/**
 * for Use in passing a private class to call back to a parent window.
 * @author robertstanton
 *
 */
public interface StockCallBack {
    
    public void addStock(String ticker);
    
    public void changeStock(Stock s);

    public void removeStock(Stock s);
}
