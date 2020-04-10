import java.io.FileNotFoundException;
import java.util.ArrayList;

public class StubController {
    
    public static void main(String[] args) {
        String[] stockNames = new String[] {"SPY", "AAPL", "GOOGL"};
        ArrayList<Stock> stocks = new ArrayList<>();
        for(String sname : stockNames) {
            try {
                Stock s = new Stock(sname);
                stocks.add(s);
            } catch (FileNotFoundException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                for(Stock s : stocks) {
                    System.out.println(s.getDataHistory().toString());
                }
            }
        }
        if(stocks.size() > 0) {
            BaseGUI gui = new BaseGUI(stocks);
            
        }
    }

}
