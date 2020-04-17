import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
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
	
	// panel left components
	private StockListPanel stockList; // remove
	
	/**
	 * Constructs Application with default title of StockAlertApp.
	 * @param s
	 */
	public BaseGUI(ArrayList<Stock> s) {
	    this("StockAlertApp", s);
	}
	
	/**
	 * Constructs Applicatino with a title and the set of stocks.
	 * @param title
	 * @param s
	 */
	public BaseGUI(String title, ArrayList<Stock> s) {
		super(title);
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
	 * Alert observing components of a change in the target stock
	 * 
	 * @param newStock
	 */
	public void notifyStockChange(Stock newStock) {
	    tgtStock = newStock;
	    rightPanel.changeTargetStock(tgtStock);
	}
	
	public void addNewStock(String ticker) {
	    rightPanel.addAlert("<html><bold>" + ticker + "</bold> : fetching data now ... </html>");
	    try {
            Stock newStock = new Stock(ticker);
            stockList.addStock(newStock);
            stocks.add(newStock);
            notifyStockChange(newStock);
            
        } catch (FileNotFoundException | InterruptedException e) {
            rightPanel.addAlert("<html><bold>" + ticker + "</bold> : failed to retrieve ticker</html>");
            e.printStackTrace();
        }
	}
	
	// =====================  CALLBACK CLASS =============================
    
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
	
	
	// ==============================================================================================
	//                 Private ActionListener classes to act on components
	// ==============================================================================================

	
	/**
	 * Changes the Focus Stock for the RightPanel components (Chart and Table).
	 * @author robertstanton
	 *
	 */
	private class SwitchStockAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            StockDetailButton b = (StockDetailButton) e.getSource();
            tgtStock = b.getStock();
            chart.changeStock(tgtStock.getdataHistory());
            table.setStock(tgtStock.getTicker());
        }
	}
}
