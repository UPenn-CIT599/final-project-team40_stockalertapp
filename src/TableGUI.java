import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

public class TableGUI extends JPanel{
    ArrayList<JLabel> alerts;
    JLabel tickerHeader;
    JLabel stockLabel;
    GridBagConstraints gridCont;
    String ticker;
    
    public TableGUI(String ticker) {
        this.ticker = ticker;
        setBackgroundColor(Color.GREEN);
        setOpaque(true);
        setLayout(new GridBagLayout());
        gridCont = new GridBagConstraints();
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        tickerHeader = new JLabel("Ticker", JLabel.CENTER);
        tickerHeader.setForeground(Color.WHITE);
        tickerHeader.setBackground(Color.DARK_GRAY);
        tickerHeader.setOpaque(true);
        
        stockLabel = new JLabel(ticker.toUpperCase(), JLabel.CENTER);
        stockLabel.setForeground(Color.WHITE);
        stockLabel.setBackground(Color.DARK_GRAY);
        stockLabel.setOpaque(true);
        
        alerts = new ArrayList<>();
        for(String alertName : new String[] { "MACD", "OBV", "SMA", "ExpMA"}) {
            JLabel alert = new JLabel(alertName);
            alert.setForeground(Color.WHITE);
            alert.setBackground(Color.DARK_GRAY);
            alert.setOpaque(true);
            alert.setHorizontalTextPosition(JLabel.CENTER);
            alerts.add(alert);
        }
        
        createHeader();
        createRow(1);
        
    }
    
    private void setBackgroundColor(Color black) {
        // TODO Auto-generated method stub
        
    }

    public void createHeader() {
        gridCont.fill = GridBagConstraints.BOTH;
        gridCont.weightx = 4.0;
        gridCont.weighty = 1.0;
        //gridCont.ipady = 10;
        //gridCont.ipadx = 10;
        gridCont.gridy = 0;
        gridCont.anchor = GridBagConstraints.FIRST_LINE_START;
        add(tickerHeader, gridCont);
        
        int x = 1;
        for(JLabel item : alerts) {
            gridCont.gridx = GridBagConstraints.RELATIVE;
            gridCont.anchor = GridBagConstraints.BASELINE;
            gridCont.weightx = 1.0;
            if(x == alerts.size()) {
                gridCont.anchor = GridBagConstraints.FIRST_LINE_END;
            }
            add(item, gridCont);
            x++;
        }
        
    }
    
    public void createRow(int num) {
        gridCont.weighty = 3.0;
        gridCont.anchor = GridBagConstraints.LINE_START;
        gridCont.gridy = num;
        gridCont.weightx = 4.0;
        gridCont.fill = GridBagConstraints.BOTH;
        add(stockLabel, gridCont);
        
        gridCont.gridx = GridBagConstraints.RELATIVE;
        int x = 1;
        for(JLabel item : alerts) {
            gridCont.weightx = 1.0;
            if(x == alerts.size()) {
                gridCont.anchor = GridBagConstraints.FIRST_LINE_END;
            }
            JRadioButton button = new JRadioButton();
            button.setForeground(Color.WHITE);
            button.setBackground(Color.DARK_GRAY);
            button.setOpaque(true);
            add(button, gridCont);
            x++;
        }
    }
    
    public void setStock(String ticker) {
        this.ticker = ticker;
        stockLabel.setText(ticker.toUpperCase());
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container cont = frame.getContentPane();
        cont.setLayout(new BorderLayout());
        TableGUI table = new TableGUI("SPY");
        table.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        
        JButton changeStock = new JButton("changeStock");
        
        cont.add(table, BorderLayout.NORTH);
        cont.add(changeStock, BorderLayout.SOUTH);
        
        changeStock.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                table.setStock("AAPL");
            }
            
        });
        
        
        frame.pack();
        frame.setVisible(true);
        
    }
}
