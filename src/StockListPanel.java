import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StockListPanel extends JPanel {
    
    private ArrayList<Stock> portfolio;
    private ArrayList<StockDetailButton> buttons;
    private GridBagConstraints gbConst;
    private JButton addStock;
    private GridBagConstraints showAddStock;
    private GridBagLayout gbLayout;
    
    private String[] toggleSymbols;
    private MouseEventActions mouseControl;
    private JTextField newStockInput;
    
    public StockListPanel(ArrayList<Stock> s) {
        portfolio = s;
        buttons = new ArrayList<>();
        mouseControl = new MouseEventActions();
        setBackground(Color.DARK_GRAY);
        setOpaque(true);
        
        gbLayout = new GridBagLayout();
        setLayout(gbLayout);
        addStock = new JButton("+");
        Font buttonFont = addStock.getFont();
        addStock.setFont(new Font(buttonFont.getFontName(), buttonFont.getStyle(), (int) (buttonFont.getSize() * 1.5)));
        addStock.setBackground(Color.DARK_GRAY);
        addStock.setForeground(Color.WHITE);
        addStock.setOpaque(true);
        addStock.setBorderPainted(false);
        addStock.addMouseListener(mouseControl);
        
        newStockInput = new JTextField("enter ticker");
        newStockInput.setForeground(Color.LIGHT_GRAY);
        newStockInput.addMouseListener(mouseControl);
        newStockInput.setVisible(false);
        
        showAddStock = new GridBagConstraints();
        showAddStock.fill = GridBagConstraints.BOTH;
        showAddStock.gridy = 2;
        showAddStock.gridx = 1;
        
        
        gbConst = new GridBagConstraints();
        gbConst.gridy = GridBagConstraints.RELATIVE;
        gbConst.gridx = 1;
        gbConst.fill = GridBagConstraints.BOTH;
        add(addStock, gbConst);
        add(newStockInput, gbConst);
        
        for(Stock stock : portfolio) {
            StockDetailButton button = new StockDetailButton(stock);
            button.addMouseListener(mouseControl);
            buttons.add(button);
            add(button, gbConst);
        }
    }
    
    public ArrayList<StockDetailButton> getButtons() {
        return buttons;
    }
    
    public void addStock(Stock s) {
        portfolio.add(s);
        StockDetailButton newButton = new StockDetailButton(s);
        buttons.add(newButton);
        add(newButton, gbConst);
    }
    
    public void setAddStockSlideAction(ActionListener action) {
        addStock.addActionListener(action);
    }
    
    public void setAddStockTextAction(ActionListener action) {
        newStockInput.addActionListener(action);
    }
    public void setNewStockInputVisible() {
        if(!newStockInput.isVisible()) {
            newStockInput.setVisible(true);
            addStock.setText("-");
            revalidate();
        } else {
            newStockInput.setVisible(false);
            addStock.setText("+");
            revalidate();
        }
    }
    
    private class MouseEventActions implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            Object source = e.getSource();
            if(source instanceof JTextField) {
                ((JTextField) source).setText("");
                ((JTextField) source).setForeground(Color.BLACK);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getSource() instanceof JButton) {
                ((JButton) e.getSource()).setForeground(new Color(222, 180, 132, 100));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.getSource() instanceof JButton) {
                ((JButton) e.getSource()).setForeground(Color.WHITE);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(e.getSource() instanceof JButton) {
                ((JButton)e.getSource()).setBackground(Color.GRAY);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(e.getSource() instanceof JButton) {
                ((JButton) e.getSource()).setBackground(Color.DARK_GRAY); 
            }
        }
    }
}
