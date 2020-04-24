import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
	private AlertWindow alertWindowTemp;
	
	// panel left components
	private StockListPanel stockList;
	
	// panel menu components
	private JPanel menu;
	private JButton menuButton;
	private JLabel menuLabel;
	private JPopupMenu menuPopup;
	private JMenuItem saveItem;
	private JMenuItem loadItem;
	private JFileChooser fileChooser;
	private JScrollPane scroller;
	private AlertPanel alertSetter;
	
	/**
	 * Constructs an entry program to allow a user to add stocks from nothing.
	 */
	public BaseGUI() {
        super("StockAlertApp");
        this.setPreferredSize(new Dimension(800, 800));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        alertSetter = new AlertPanel(this);
        stocks = new ArrayList<>();
        content = this.getContentPane();
        fileChooser = new JFileChooser(System.getProperty("user.dir"));
        
        stockList = new StockListPanel(stocks);
        stockList.setNewStockAddAction(new AddStockCallBack());
        
     // left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(stockList, BorderLayout.NORTH);
        leftPanel.setBackground(Color.DARK_GRAY);
        scroller = new JScrollPane(leftPanel);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
      // right panel temporary
        alertWindowTemp = new AlertWindow();
        alertWindowTemp.setDefaultAlert();
        
        content.add(scroller, BorderLayout.WEST);
        content.add(alertWindowTemp, BorderLayout.CENTER);
        setMenuBar();
        
        this.pack();
        this.setVisible(true);
    }
	
	/**
	 * Constructs Application with default title of StockAlertApp.
	 * @param s
	 */
	public BaseGUI(ArrayList<Stock> s) {
	    this("StockAlertApp", s);
	}
	
	/**
	 * Constructs Application with a title and the set of stocks.
	 * @param title
	 * @param s
	 */
	public BaseGUI(String title, ArrayList<Stock> s) {
		super(title);
		//this.setPreferredSize(new Dimension(1000, 800));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		alertSetter = new AlertPanel(this);
		fileChooser = new JFileChooser(System.getProperty("user.dir"));
		
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
        scroller = new JScrollPane(leftPanel);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
        // right panel
        rightPanel = new PanelRight(tgtStock);
        
        // =====================================================================================
		

		// add components to content pane
		content.add(scroller, BorderLayout.WEST);
		content.add(rightPanel, BorderLayout.CENTER);
		setMenuBar();
		
		// pack components and set visible
		pack();
		setVisible(true);
	}
	
	/**
	 * Constructs right panel components and adds them to content pane.
	 */
	public void setRightPanel() {
	    rightPanel = new PanelRight(stocks.get(0));
	    content.remove(alertWindowTemp);
	    content.add(rightPanel, BorderLayout.CENTER);
	    revalidate();
	}
	
	/**
	 * Constructs the menu bar at top of application.
	 * 
	 */
	public void setMenuBar() {
	    menu = new JPanel();
	    menu.setLayout(new BorderLayout());
	    
	    menuLabel = new JLabel("Stock Alert");
	    menuLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
	    menuLabel.setHorizontalAlignment(JLabel.CENTER);
	    menuLabel.setBackground(Color.DARK_GRAY);
	    menuLabel.setForeground(Color.WHITE);
	    menuLabel.setOpaque(true);
	    
	    menuButton = new JButton("\u2261");
	    menuButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 85));
	    menuButton.setVerticalTextPosition(JButton.TOP);
	    menuButton.setBorderPainted(false);
	    menuButton.setForeground(Color.WHITE);
	    menuButton.setBackground(Color.DARK_GRAY);
	    menuButton.setOpaque(true);
	    menuButton.addMouseListener(new MenuButtonActions());
	    
	    menuPopup = new JPopupMenu();
	    MenuItemAction menuActions = new MenuItemAction();
        saveItem = new JMenuItem("Save Stock List");
        saveItem.setActionCommand("saveItem");
        saveItem.addActionListener(new MenuItemAction());
        
        loadItem = new JMenuItem("Load Stock List");
        loadItem.setActionCommand("loadItem");
        loadItem.addActionListener(new MenuItemAction());
        
        menuPopup.add(loadItem);
        menuPopup.add(saveItem);
	    
	    menu.add(menuButton, BorderLayout.WEST);
	    menu.add(menuLabel, BorderLayout.CENTER);
	    add(menu, BorderLayout.NORTH);
	}
	
	public void clearStockList() {
	    ArrayList<Stock> stockRemove = new ArrayList<>();
	    stockRemove.addAll(stocks);
	    for(int i = 0; i < stockRemove.size(); i++) {
	        stockList.removeStock(stockRemove.get(i));
	    }
	}
	
	/**
	 * sets an array list of stocks.
	 * @param s
	 */
	public void setStocks(ArrayList<Stock> s) {
	    if(stocks.size() < 1) {
	        stocks = s;
	        setRightPanel();
	        for(Stock stock : stocks) {
	            stockList.addStock(stock);
	        }
	    } else {
	        clearStockList();
	        stocks = s;
	        setRightPanel();
	        for(Stock stock : stocks) {
	            stockList.addStock(stock);
	        }
	    }
	    
	    revalidate();
	}
	
	/**
	 * Saves a list of Stock Tickers.
	 * 
	 */
	public void saveStockList(File savedTickers) {
	    
	    FileOutputStream file;
        try {
            file = new FileOutputStream(savedTickers);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(stocks);
            out.close();
            rightPanel.addAlert("<html><p><bold color=green>Stock list has saved</bold> : "+ savedTickers.getName() +"</p></html> ");
        } catch (FileNotFoundException e) {
            rightPanel.addAlert("<html><p><bold color=red> Failed to save file </bold> : FileNotFoundException thrown </p></html> ");
        } catch (IOException e) {
            rightPanel.addAlert("<html><p><bold color=red> Failed to save file </bold> : IOException thrown </p></html> ");
        }
        revalidate();
        System.out.println("done saveStockList");
	}
	
	/**
	 * Alert observing components of a change in the target stock
	 * 
	 * @param newStock
	 */
	public void notifyStockChange(Stock newStock) {
	    tgtStock = newStock;
	    rightPanel.changeTargetStock(tgtStock);
	    content.repaint();
	}
	
	/**
	 * Constructs a new Stock object notifies observers and adds to stocks arraylist and stockList panel.
	 * @param ticker
	 */
	public void addNewStock(String ticker) {
	    if(stocks.size() > 0) {
	        rightPanel.clearAlerts();
	        rightPanel.addAlert("<html><line color=orange><b>" + ticker + "</b> : api call will take a minute</line></html>");
	        content.repaint();
	    } else {
	        alertWindowTemp.clearAlerts();
	        alertWindowTemp.addAlert("<html><line color=orange><bold color=black>" + ticker + "</bold> : api call will take a minute</line></html>");
	        content.repaint();
	    }
	    
	    // beginning async call to new Stock
	    new Thread(new Runnable() {
	        @Override
            public void run() {
            
        	    try {
                    Stock newStock = new Stock(ticker);
                    stockList.addStock(newStock);
                    stocks.add(newStock);
                    if(stocks.size() == 1) {
                        setRightPanel();
                    }
                    notifyStockChange(newStock);
                    stockList.resetTickerInputField();
                    
                } catch (FileNotFoundException | InterruptedException e) {
                    rightPanel.addAlert("<html><line color=orange><bold font-size=large color=blue>" + ticker + "</bold> : failed to retrieve ticker</line></html>");
                    e.printStackTrace();
                } catch(Exception end) {
                    end.printStackTrace();
                    if(stocks.size() > 0) {
                        rightPanel.addAlert(ticker + " : unable to find requested ticker");
                    } else {
                        alertWindowTemp.addAlert(ticker + " : unable to find requested ticker");
                    }
                }
	        }
            
	    }).start();
	}
	
	/**
	 * Bulk adds a list of tickers.
	 * @param s
	 */
	public void bulkAddStocks(ArrayList<String> s) {
	    for(String ticker : s) {
	        addNewStock(ticker);
	    }
	}
	
	/**
	 * Returns the stock list.
	 * @return
	 */
	public ArrayList<Stock> getStocks() {
	    return stocks;
	}
	
	// =====================  CALLBACK CLASS =============================
    
	/**
	 * Class for implementing call backs from the StockListPanel.
	 * 
	 * @author robertstanton
	 *
	 */
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
            if(stocks.size() < 1) {
                remove(rightPanel);
                content.revalidate();
                add(alertWindowTemp);
                alertWindowTemp.clearAlerts();
                alertWindowTemp.setDefaultAlert();
                content.revalidate();
                content.repaint();
            }
        }
        
        @Override
        public void setNewAlert(Stock s) {
            alertSetter.createGUI(s);
        }
    }
    
    // ==================================================================
    //                         Action Listener
    // ==================================================================
    
    /**
     * Action listener for the save or load a stock list action.
     * 
     */
    private class MenuItemAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand() == "saveItem") {
                int returnChoice = fileChooser.showSaveDialog((JComponent) e.getSource());
                if(returnChoice == JFileChooser.APPROVE_OPTION) {
                    File f = fileChooser.getSelectedFile();
                    saveStockList(f);
                }
            }
            
            if(e.getActionCommand() == "loadItem") {
                int returnChoice = fileChooser.showOpenDialog((JComponent) e.getSource());
                
                if(returnChoice == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if(selectedFile.exists()) {
                        try {
                            FileInputStream file = new FileInputStream(selectedFile);
                            ObjectInputStream in = new ObjectInputStream(file);
                            ArrayList<Stock> newStocks = (ArrayList<Stock>) in.readObject();
                            if(newStocks.size() > 0) {
                                setStocks(newStocks);
                            } else {
                                if(stocks.size() > 0) {
                                    rightPanel.addAlert("<html><p>No data found : " + selectedFile + "</p> </html>");
                                } else {
                                    alertWindowTemp.addAlert("<html><p>No data found : " + selectedFile + "</p> </html>");
                                }
                            }
                            
                            file.close();
                            in.close();
                            
                        } catch (FileNotFoundException fnf) {
                            // TODO Auto-generated catch block
                            fnf.printStackTrace();
                        } catch(IOException ioe) {
                            ioe.printStackTrace();
                        } catch (ClassNotFoundException cnf) {
                            // TODO Auto-generated catch block
                            cnf.printStackTrace();
                        }
                        
                    } else {
                        if(stocks.size() > 0) {
                            rightPanel.addAlert("<html><p>Unable to find : " + selectedFile + "</p> </html>");
                        } else {
                            alertWindowTemp.addAlert("<html><p>Unable to find : " + selectedFile + "</p> </html>");
                        }
                    }
                }
            }
        }
    }
    
    // ==================================================================
    //                          Mouse Listener
    // ==================================================================
    
    /**
     * Formats the menuButton for actions
     * 
     */
    private class MenuButtonActions implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            Object source = e.getSource();
            if(source instanceof JButton) {
                menuPopup.show((JButton)source, ((JButton) source).getX(), ((JButton) source).getY());
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            Object source = e.getSource();
            if(source instanceof JButton) {
                ((JButton) source).setBackground(Color.GRAY);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Object source = e.getSource();
            if(source instanceof JButton) {
                ((JButton) source).setBackground(Color.DARK_GRAY);
                ((JButton) source).setForeground(Color.WHITE);
            }
        }
        
    }
}
