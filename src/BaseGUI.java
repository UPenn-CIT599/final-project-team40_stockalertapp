import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * BASEGUI CLASS:
 * 
 * This class is the entry point for StockAlert Applications graphical user
 * interface which will be the default for all non-headless environments.
 *
 * @author robertstanton
 *
 */
public class BaseGUI extends JFrame {
	
	private ChartGUI chart;
	private StockDetailButton changeStock;
	private TableGUI table;
	private StockListPanel stockList;
	private ArrayList<Stock> stocks;
	private Stock tgtStock;
	 
	public BaseGUI(ArrayList<Stock> s) {
	    this("StockAlertApp", s);
	}

	public BaseGUI(String title, ArrayList<Stock> s) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		stocks = s;
		

		// get content pane for frame
		Container content = getContentPane();
		content.setLayout(new BorderLayout());
		
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		leftPanel.setLayout(new BorderLayout());
		
		
		// create components

		tgtStock = stocks.get(0);
		chart = new ChartGUI(tgtStock.getDataHistory());
		// changeStock = new StockDetailButton(tgtStock);
		table = new TableGUI(tgtStock.getTicker());
		stockList = new StockListPanel(stocks);
		

		// add components to content pane
		rightPanel.add(table, BorderLayout.NORTH);
		rightPanel.add(chart, BorderLayout.CENTER);
		
		leftPanel.add(stockList, BorderLayout.NORTH);
		leftPanel.setBackground(Color.DARK_GRAY);
		leftPanel.setOpaque(true);
		
		content.add(leftPanel, BorderLayout.WEST);
		content.add(rightPanel, BorderLayout.CENTER);

		// add action listener
		for(StockDetailButton button : stockList.getButtons())
    		button.addActionListener(new ActionListener() {
    
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				StockDetailButton b = (StockDetailButton) e.getSource();
    				Stock tgtStock = b.getStock();
    
    				chart.changeStock(tgtStock.getDataHistory());
    				table.setStock(tgtStock.getTicker());
    			}
    
    		});

		pack();
		setVisible(true);
	}
	
	private class ChartAction implements ActionListener {
	    BaseGUI gui;
	    
	    public ChartAction(BaseGUI gui) {
	        this.gui = gui;
	    }
        @Override
        public void actionPerformed(ActionEvent e) {
            
        }
	    
	}
}
