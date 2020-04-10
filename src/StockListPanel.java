import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class StockListPanel extends JPanel {
    
    private ArrayList<Stock> portfolio;
    private ArrayList<StockDetailButton> buttons;
    private GridBagConstraints gbConst;
    private JButton toggleVisible;
    private String[] toggleSymbols;
    
    public StockListPanel(ArrayList<Stock> s) {
        portfolio = s;
        buttons = new ArrayList<>();
        setBackground(Color.DARK_GRAY);
        setOpaque(true);
        
        setLayout(new GridBagLayout());
        toggleVisible = new JButton();
        Font buttonFont = toggleVisible.getFont();
        toggleVisible.setFont(new Font(buttonFont.getFontName(), buttonFont.getStyle(), (int) (buttonFont.getSize() * 1.1)));
        toggleVisible.setBackground(Color.DARK_GRAY);
        toggleVisible.setForeground(Color.WHITE);
        toggleVisible.setOpaque(true);
        toggleVisible.setBorderPainted(false);
        
        gbConst = new GridBagConstraints();
        gbConst.gridy = GridBagConstraints.RELATIVE;
        gbConst.gridx = 1;
        gbConst.fill = GridBagConstraints.BOTH;
        add(toggleVisible, gbConst);
        for(Stock stock : portfolio) {
            StockDetailButton button = new StockDetailButton(stock);
            buttons.add(button);
            add(button, gbConst);
        }
    }
    
    public ArrayList<StockDetailButton> getButtons() {
        return buttons;
    }
    
    public static void main(String[] args) {
        StubController controller = new StubController();
        
        controller.deserializeData(new File("portfolio.ser"));
        StockListPanel stockList = new StockListPanel(controller.stocks);
        JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        content.setLayout(new FlowLayout());
        content.add(stockList);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
