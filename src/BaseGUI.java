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

	public BaseGUI() {
	    
		this("StockAlertApp", );
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


		chart = new ChartGUI(stocks.get(0));
		changeStock = new JButton("new Stock");

		// add components to content pane
		content.add(chart, BorderLayout.NORTH);
		content.add(changeStock, BorderLayout.SOUTH);

		// add action listener
		changeStock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Stock s = stocks.get(1);

				chart.changeStock(s);
			}

		});

		pack();
		setVisible(true);

	}

	public static void main(String[] args) {
		BaseGUI gui = new BaseGUI();
	}

}
