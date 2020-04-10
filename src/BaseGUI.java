import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;

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
	private JButton changeStock;
	private TableGUI table;
	private ArrayList<Stock> stocks;
	private Stock tgtStock;
	 
	public BaseGUI(ArrayList<Stock> s) {
	    this("StockAlertApp", s);
	}

	public BaseGUI(String title, ArrayList<Stock> s) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		stocks = s;
		// set layout for frame
		setLayout(new BorderLayout());

		// get content pane for frame
		Container content = getContentPane();

		// create components

		tgtStock = stocks.get(0);
		chart = new ChartGUI(tgtStock.getDataHistory());
		changeStock = new JButton("new Stock");
		table = new TableGUI(tgtStock.getTicker());

		// add components to content pane
		content.add(table, BorderLayout.NORTH);
		content.add(chart, BorderLayout.CENTER);
		content.add(changeStock, BorderLayout.SOUTH);

		// add action listener
		changeStock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			    int stocksSize = stocks.size();
			    int tgtStockIndex = stocks.indexOf(tgtStock);
			    System.out.println(tgtStock.getTicker() + " " + tgtStockIndex);
				tgtStock = stocks.get((tgtStockIndex + 1) % stocksSize);
				

				chart.changeStock(tgtStock.getDataHistory());
				table.setStock(tgtStock.getTicker());
			}

		});

		pack();
		setVisible(true);
	}
}
