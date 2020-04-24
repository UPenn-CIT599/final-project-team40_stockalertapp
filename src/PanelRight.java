import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * This is the panel containing the charting and alert elements of the
 * application. The table at the top displays the ticker and adjusts date
 * ranges. The Alert window displays any alerts that were triggered.
 * 
 * @author robertstanton
 *
 */
public class PanelRight extends JPanel {

    private Stock targetStock;
    private ChartGUI chart;
    private TableGUI table;
    private AlertWindow alertWindow;

    /**
     * constructs the right panel contents to include a table which adjusts date
     * ranges, a chart of historical pricing, and an alert window.
     * 
     * @param s
     */
    public PanelRight(Stock s) {
        targetStock = s;
        chart = new ChartGUI(targetStock.getdataHistory()); // default to 5 years back
        table = new TableGUI(targetStock.getTicker());
        alertWindow = new AlertWindow();

        JScrollPane scroller = new JScrollPane(alertWindow);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // add action listeners
        // set table buttons to adjust time frame of chart
        for (JButton but : table.getButtons()) {
            but.addActionListener(new DateAdjustAction());
        }
        
        for(JButton alrt : table.getAlerts()) {
            alrt.addActionListener(new ToggleIndicators());
        }

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 10;
        gbc.weightx = 1;
        gbc.gridy = 0;
        gbc.gridx = 0;
        add(table, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.weighty = 60;
        gbc.gridy = 1;
        add(chart, gbc);

        gbc.weighty = 30;
        gbc.gridy = 2;
        add(scroller, gbc);
        
        // set alerts
        setAlerts();
        
    }
    
    public void setAlerts() {
        // sma 209.04 (100d) ema 207.7732 rsi 62.5398 macd 4.7365 obv 4.96e9
        double emaSmaDiv = targetStock.getEma() / targetStock.getSma() - 1;
        
        // check EMA vs SMA divergence
        if(emaSmaDiv < -.02) {
            alertWindow.addAlert("<html><line><bold><font color=blue>EMA SMA DIV ALERT : </font></bold><font color=red>" + emaSmaDiv +"</font></line><html>");
        }
        if(emaSmaDiv > .02) {
            alertWindow.addAlert("<html><line><font color=blue>EMA SMA DIV ALERT : </font><font color=green>" + emaSmaDiv +"</font></line><html>");
        }
        
        // check RSI
        double rsiVal = targetStock.getRsi();
        if(rsiVal >= 70) {
            alertWindow.addAlert("<html><line><font color=blue>RSI BREACHED</font><font color=green> : " + rsiVal + "</font></line></html>");
        }
        if(rsiVal <= 30) {
            alertWindow.addAlert("<html><line><font color=blue>RSI BREACHED</font><font color=red> : " + rsiVal + "</font></line></html>");
        }
        
        // check MACD
        double macd = targetStock.getMacd();
        if(macd < .5 && macd > .5) {
            String alertMsg = "<html><line><bold color=blue>MACD RECENT COLLAPSE</bold>";
            alertMsg += macd > 0 ? "<text color=green> : " + macd + "</text></line></html>" : "<font color=red> : " + macd + "</font></line></html>";
            alertWindow.addAlert(alertMsg);
        }
    }

    /**
     * Changes the target stock used for the chart and table views
     * 
     * @param s
     */
    public void changeTargetStock(Stock s) {
        targetStock = s;
        chart.changeStock(targetStock.getdataHistory());
        table.setStock(targetStock.getTicker());
        alertWindow.clearAlerts();
        
        alertWindow.addAlert(s.getTicker() + " : data loaded");
        setAlerts();
    }

    /**
     * load a stocks alert in the alert window.
     * 
     */
    
    public void addAlertsToWindow() {
        if(!targetStock.getCalculatedAlerts().isEmpty()) {
            for(Map.Entry entry : targetStock.getCalculatedAlerts().entrySet()) {
                String name = (String) entry.getKey();
                Boolean didTrigger = (Boolean) entry.getValue();
                if(didTrigger) {
                    alertWindow.addAlert("<html><div color=orange><bold color=black>" + targetStock.getTicker() + "</bold> : " + name + "</div></html>");
                }
            }
        }
    }
    
    
    /**
     * Add an alert label to the Alert Window panel;
     * @param msg
     */
    public void addAlert(String msg) {
        alertWindow.addAlert(msg);
    }
    
    /**
     * Clear the Alert Window.
     */
    public void clearAlerts() {
        alertWindow.clearAlerts();
    }

    // ============================= Action Listeners =============================
    
    /**
     * Toggles on or off a given indicator.
     * @author robertstanton
     *
     */
    private class ToggleIndicators implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            if(actionCommand.equals("50d")) {
                chart.toggleSMA50();
                ((AlertButton) e.getSource()).toggleOnOff();
            } else if(actionCommand.equals("200d")) {
                chart.toggleSMA200();
                ((AlertButton) e.getSource()).toggleOnOff();
            } else if(actionCommand.equals("100d")) {
                chart.toggleSMA100();
                ((AlertButton) e.getSource()).toggleOnOff();
            }
        }
    }
    
    /**
     * Changes the time frame displayed in the Chart to 3 months, 6 months, 1 year,
     * or all Data.
     * 
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
            dateAdjustMap.put("1Y", 12);
            dateAdjustMap.put("5Y", 60);
            dateAdjustMap.put("ALL", 0);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String timeAdjust = e.getActionCommand();
            int monthsBack = dateAdjustMap.get(timeAdjust);

            chart.changeDateRange(monthsBack);
            
            table.setFocus((JButton) e.getSource());
        }
    }
}
