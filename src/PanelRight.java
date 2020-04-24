import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
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
    
    /**
     * display existing alerts in panel.
     */
    public void setAlerts() {
        String alertMessageContainer = "<div text-align=center><font color=black font-size=Large>  Active Alerts  </font></div>";
        
        if(!targetStock.getStoredAlerts().isEmpty()) {
            Map<String, HashMap<String, Double>> storedAlerts = targetStock.getStoredAlerts();
            
            for(Map.Entry entry : storedAlerts.entrySet()) {
                String alertName = entry.getKey().toString();
                String alertCriteria = entry.getValue().toString();
                alertMessageContainer += "<div><font color=blue>" + alertName + "</font><font color=black> : " + alertCriteria + "</font></div>";
            }
            alertWindow.addAlert("<html>" + alertMessageContainer + "</html>");
            
            targetStock.calculateAlerts();
            for(Map.Entry entry : targetStock.getCalculatedAlerts().entrySet()) {
                String indicatorName = (String) entry.getKey();
                Boolean isTrue = (Boolean) entry.getValue();
                if(isTrue) {
                    alertWindow.addAlert("<html><line><font color=red font-size=large>" + indicatorName + "</font><font color=black> has been triggered</font></line></html>");
                }
            }
            
        } else {
            alertWindow.addAlert("<html><line><font color=orange font-size=large>" + targetStock.getTicker() + "</font><font color=black> : right click ticker to add alerts</font></line> </html>");
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
