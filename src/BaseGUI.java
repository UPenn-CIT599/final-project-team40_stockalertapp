import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * BASEGUI CLASS:
 * 
 * This class is the entry point for StockAlert Applications graphical user
 * interface which will be the default for all non-server environments.
 *
 * @author robertstanton
 *
 */
public class BaseGUI extends JFrame {
	
    private ArrayList<Stock> stocks;
    private Stock tgtStock;
    private Container content;
    
    
    
    // panel right components
	private ChartGUI chart;
	private TableGUI table;
	private PanelRight rightPanel;
	private AlertWindow alertWindowTemp;
	
	// panel left components
	private StockListPanel stockList;
	
	/**
	 * Constructs an entry program to allow a user to add stocks from nothing.
	 */
	public BaseGUI() {
        super("StockAlertApp");
        this.setPreferredSize(new Dimension(800, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        stocks = new ArrayList<>();
        content = this.getContentPane();
        
        stockList = new StockListPanel(stocks);
        stockList.setNewStockAddAction(new AddStockCallBack());
        
     // left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(stockList, BorderLayout.NORTH);
        leftPanel.setBackground(Color.DARK_GRAY);
        JScrollPane scroller = new JScrollPane(leftPanel);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
      // right panel temporary
        alertWindowTemp = new AlertWindow();
        alertWindowTemp.addAlert("<html><bold font-size=large text-align=center>Welcome to Stock Alerts</bold></html>");
        alertWindowTemp.addAlert("<html><p font-size=medium text-align=center>To start hit the <bold>+</bold> and type in a ticker and hit enter.</p></html>");
        
        content.add(scroller, BorderLayout.WEST);
        content.add(alertWindowTemp, BorderLayout.CENTER);
        
        this.pack();
        this.setVisible(true);
    }
	
	/**
	 * Constructs Application with default title of StockAlertApp.
	 * @param s
	 */
	public BaseGUI(ArrayList<Stock> s) {
	    this("StockAlertApp", s);
	}
	
	/**
	 * Constructs Application with a title and the set of stocks.
	 * @param title
	 * @param s
	 */
	public BaseGUI(String title, ArrayList<Stock> s) {
		super(title);
		this.setPreferredSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		stocks = s;
		tgtStock = stocks.get(0);

		// get content pane for frame
		content = getContentPane();
		content.setLayout(new BorderLayout());
		
		// =============================== Create Components ==================================
		
        // left panel components
        stockList = new StockListPanel(stocks);
        stockList.setNewStockAddAction(new AddStockCallBack());
        
		// left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(stockList, BorderLayout.NORTH);
        leftPanel.setBackground(Color.DARK_GRAY);
        JScrollPane scroller = new JScrollPane(leftPanel);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
        // right panel
        rightPanel = new PanelRight(tgtStock);
        
        // =====================================================================================
		

		// add components to content pane
		content.add(scroller, BorderLayout.WEST);
		content.add(rightPanel, BorderLayout.CENTER);
		
		// pack components and set visible
		pack();
		setVisible(true);
	}
	
	/**
	 * Constructs right panel components and adds them to content pane.
	 */
	public void setRightPanel() {
	    rightPanel = new PanelRight(stocks.get(0));
	    content.remove(alertWindowTemp);
	    content.add(rightPanel, BorderLayout.CENTER);
	    revalidate();
	}
	
	/**
	 * Alert observing components of a change in the target stock
	 * 
	 * @param newStock
	 */
	public void notifyStockChange(Stock newStock) {
	    tgtStock = newStock;
	    rightPanel.changeTargetStock(tgtStock);
	}
	
	/**
	 * Constructs a new Stock object notifies observers and adds to stocks arraylist and stockList panel.
	 * @param ticker
	 */
	public void addNewStock(String ticker) {
	    if(stocks.size() > 0) {
	        rightPanel.addAlert("<html><bold>" + ticker + "</bold> : fetching data now ... </html>");
	    } else {
	        alertWindowTemp.addAlert("<html><bold>" + ticker + "</bold> : fetching data now ... </html>");
	    }
	    
	    
	    // beginning async call to new Stock
	    new Thread(new Runnable() {
	        @Override
            public void run() {
            
        	    try {
                    Stock newStock = new Stock(ticker);
                    stockList.addStock(newStock);
                    stocks.add(newStock);
                    if(stocks.size() == 1) {
                        setRightPanel();
                    }
                    notifyStockChange(newStock);
                    rightPanel.addAlert("<html><bold font-size=large color=blue>" + ticker + "</bold> : data has been loaded!</html>");
                    stockList.resetTickerInputField();
                    
                } catch (FileNotFoundException | InterruptedException e) {
                    rightPanel.addAlert("<html><bold font-size=large color=blue>" + ticker + "</bold> : failed to retrieve ticker</html>");
                    e.printStackTrace();
                }
	        }
            
	    }).start();
	}
	
	/**
	 * Returns the stock list.
	 * @return
	 */
	public ArrayList<Stock> getStocks() {
	    return stocks;
	}
	
	// =====================  CALLBACK CLASS =============================
    
	/**
	 * Class for implementing call backs from the StockListPanel.
	 * 
	 * @author robertstanton
	 *
	 */
    private class AddStockCallBack implements StockCallBack {
        
        @Override
        public void addStock(String ticker) {
            addNewStock(ticker);
        }
        
        @Override
        public void changeStock(Stock s) {
            notifyStockChange(s);
        }
        
        @Override
        public void removeStock(Stock s) {
            stocks.remove(s);
        }
    }
}
