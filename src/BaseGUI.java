import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	
	// panel left components
	private StockListPanel stockList; // remove
	 
	public BaseGUI(ArrayList<Stock> s) {
	    this("StockAlertApp", s);
	}

	public BaseGUI(String title, ArrayList<Stock> s) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		stocks = s;
		tgtStock = stocks.get(0);

		// get content pane for frame
		content = getContentPane();
		content.setLayout(new BorderLayout());
		
		// create components
        chart = new ChartGUI(tgtStock.getDatahistory());
        table = new TableGUI(tgtStock.getTicker());
        
        stockList = new StockListPanel(stocks);
        stockList.setStockChangeAction(new ChangeStockAction());
        
		// =====================================================================================
        
		// left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(stockList, BorderLayout.NORTH);
        leftPanel.setBackground(Color.DARK_GRAY);
        JScrollPane scroller = new JScrollPane(leftPanel);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
        // right panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(table, BorderLayout.NORTH);
        rightPanel.add(chart, BorderLayout.CENTER);
		
        // =====================================================================================
		

		// add components to content pane
		content.add(scroller, BorderLayout.WEST);
		content.add(rightPanel, BorderLayout.CENTER);
		

		// add action listeners
		// set table buttons to adjust time frame of chart
		for(JButton but : table.getButtons()) {
		    but.addActionListener(new DateAdjustAction());
		}
		
		// pack components and set visible
		pack();
		setVisible(true);
	} // end primary constructor
	
	public void notifyStockChange(Stock newStock) {
	    tgtStock = newStock;
	    chart.changeStock(tgtStock.getDatahistory());
	    table.setStock(tgtStock.getTicker());
	}
	
	
	// ==============================================================================================
	//                 Private ActionListener classes to act on components
	// ==============================================================================================
	
	/**
	 * Changes tgtStock and triggers notification to observers
	 */
	public class ChangeStockAction implements ActionListener {
	    
	    @Override
	    public void actionPerformed(ActionEvent e) {
	        if(e.getActionCommand().equals("stockDetailButton")) {
	            StockDetailButton button = (StockDetailButton) e.getSource();
	            notifyStockChange(button.getStock());
	        }
	    }
	}
	
	/** 
	 * Remove stock and update observers
	 */
	public class RemoveStockAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("removeItem")) {
                // leftPanel.removeStock();
            }
        }
	}
	
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
            chart.changeStock(tgtStock.getDatahistory());
            table.setStock(tgtStock.getTicker());
        }
	    
	}
	
	/**
	 * Changes the time frame displayed in the Chart to 3 months, 6 months, 1 year, or all Data.
	 * @author robertstanton
	 *
	 */
	private class DateAdjustAction implements ActionListener {
	    HashMap<String, Integer> dateAdjustMap;
	    TreeMap<LocalDate, OHLCV> shortData;
	    
	    public DateAdjustAction() {
	        shortData = new TreeMap<>();
	        dateAdjustMap = new HashMap<>();
	        dateAdjustMap.put("3M", 3);
	        dateAdjustMap.put("6M", 6);
	        dateAdjustMap.put("1Y",  12);
	        dateAdjustMap.put("5Y", 60);
	        dateAdjustMap.put("ALL", 0);
	    }
	    
        @Override
        public void actionPerformed(ActionEvent e) {
            String timeAdjust = e.getActionCommand();
            int monthsBack = dateAdjustMap.get(timeAdjust);
            TreeMap<LocalDate, OHLCV> tgtData = tgtStock.getDatahistory();
            if(monthsBack > 0) {
                LocalDate startDate = tgtData.lastKey().minusMonths(monthsBack);
                for(Map.Entry entry : tgtData.entrySet()) {
                    
                    LocalDate k = (LocalDate) entry.getKey();
                    OHLCV v = (OHLCV) entry.getValue();
                    
                    if(k.isAfter(startDate)) {
                        shortData.put(k, v);
                    }
                }
            } else {
                shortData.putAll(tgtData);
            }
            chart.changeStock(shortData);
        }
	    
	}
}
