import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
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
    private ArrayList<JButton> buttons;
    private JLabel stockLabel;
    private GridBagConstraints gridCont;
    private String ticker;
    
    public TableGUI(String ticker) {
        this.ticker = ticker;
        buttons = new ArrayList<>();
        gridCont = new GridBagConstraints();
        
        setLayout(new GridBagLayout());
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        this.setBackground(Color.DARK_GRAY);
        
        stockLabel = new JLabel(ticker.toUpperCase(), JLabel.CENTER);
        stockLabel.setForeground(Color.WHITE);
        stockLabel.setBackground(Color.DARK_GRAY);
        stockLabel.setOpaque(true);
        stockLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        
        for(String alertName : new String[] { "3M", "6M", "1Y", "5Y", "ALL"}) {
            
            JButton dateAdjust = new JButton(alertName);
            dateAdjust.setForeground(new Color(230, 145, 0));
            dateAdjust.setBackground(Color.DARK_GRAY);
            dateAdjust.setOpaque(true);
            dateAdjust.setHorizontalTextPosition(JLabel.CENTER);
            dateAdjust.setActionCommand(alertName);
            buttons.add(dateAdjust);
        }
        createRow(1);
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
        for(JButton item : buttons) {
            gridCont.weightx = 1.0;
            if(x == buttons.size()) {
                gridCont.anchor = GridBagConstraints.FIRST_LINE_END;
            }
            add(item, gridCont);
            x++;
        }
    }
    
    public void setStock(String ticker) {
        this.ticker = ticker;
        stockLabel.setText(ticker.toUpperCase());
    }
    
    public String getStock() {
        return ticker;
    }
    
    public ArrayList<JButton> getButtons() {
        return buttons;
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
