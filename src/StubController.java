import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class StubController {
    public ArrayList<Stock> stocks;
    
    public StubController() {
        stocks = new ArrayList<>();
    }
    
    public void serializeData(File f) { 
        
        try {
            FileOutputStream file = new FileOutputStream(f);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(stocks);
            file.close();
            out.close();
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
    }
    
    public void deserializeData(File f) {
        try {
            FileInputStream file = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(file);
            stocks = (ArrayList<Stock>) in.readObject();
            file.close();
            in.close();
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        StubController app = new StubController();
        File tgtFile = new File("portfolio.ser");
        if(tgtFile.exists()) {
            app.deserializeData(tgtFile);
        }else {
            String[] stockNames = new String[] {"SPY", "AAPL", "GOOGL", "MSFT", "TSLA"};
            for(String sname : stockNames) {
                try {
                    Stock s = new Stock(sname);
                    app.stocks.add(s);
                } catch (FileNotFoundException | InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    // Serialize whatever you have
                    app.serializeData(tgtFile);
                    
                }
            }
        }
        
        if(app.stocks.size() > 0) {
            BaseGUI gui = new BaseGUI(app.stocks);
        }
    }
}
