import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Entry point for StockAlert Applications graphical user interface which will be the default for all non-headless environments.
 * @author robertstanton
 *
 */
public class BaseGUI extends JFrame {
    private ChartGUI chart;
    private JButton changeStock;
    private TableGUI table;
    
    public BaseGUI() {
        this("StockAlertApp");
    }
    
    
    public BaseGUI(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // set layout for frame
        setLayout(new BorderLayout());
        
        // get content pane for frame
        Container content = getContentPane();
        
        // create components
<<<<<<< HEAD
        Stock stock = new Stock("SPY");
=======
        StockRandom stock = new StockRandom();
>>>>>>> guiMod
        chart = new ChartGUI(stock);
        changeStock = new JButton("new Stock");
        
        // add components to content pane
        content.add(chart, BorderLayout.NORTH);
        content.add(changeStock, BorderLayout.SOUTH);
        
        // add action listener
        changeStock.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
<<<<<<< HEAD
                Stock s = new Stock("AAPL");
=======
                StockRandom s = new StockRandom();
>>>>>>> guiMod
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
