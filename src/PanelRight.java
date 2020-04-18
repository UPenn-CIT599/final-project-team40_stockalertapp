import java.awt.BorderLayout;
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
        chart = new ChartGUI(this.adjustDateForChart(60)); // default to 5 years back
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

        alertWindow.addAlert("Welcome to alert Catcher");
        alertWindow.addAlert("Second Alert");
    }

    /**
     * Changes the target stock used for the chart and table views
     * 
     * @param s
     */
    public void changeTargetStock(Stock s) {
        targetStock = s;
        chart.changeStock(adjustDateForChart(60));
        table.setStock(targetStock.getTicker());
    }

    /**
     * Transforms complete data of Stock history to window specified by the
     * monthsBack parameter
     * 
     * @param monthsBack
     * @return
     */
    public TreeMap<LocalDate, OHLCV> adjustDateForChart(int monthsBack) {
        TreeMap<LocalDate, OHLCV> shortData = new TreeMap<>();
        TreeMap<LocalDate, OHLCV> tgtData = targetStock.getdataHistory();
        if (monthsBack > 0) {
            LocalDate startDate = tgtData.lastKey().minusMonths(monthsBack);
            for (Map.Entry entry : tgtData.entrySet()) {
                LocalDate k = (LocalDate) entry.getKey();
                OHLCV v = (OHLCV) entry.getValue();
                if (k.isAfter(startDate)) {
                    shortData.put(k, v);
                }
            }
        } else {
            return tgtData;
        }
        return shortData;
    }
    
    /**
     * Add an alert label to the Alert Window panel;
     * @param msg
     */
    public void addAlert(String msg) {
        alertWindow.addAlert(msg);
    }

    // ============================= Action Listeners =============================
    
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
            TreeMap<LocalDate, OHLCV> shortData = adjustDateForChart(monthsBack);
            chart.changeStock(shortData);
        }
    }
}
