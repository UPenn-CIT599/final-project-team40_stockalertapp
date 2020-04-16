import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelRight extends JPanel {
    
    private Stock targetStock;
    private ChartGUI chart;
    private TableGUI table;
    
    public PanelRight(Stock s) {
        targetStock = s;
        chart = new ChartGUI(targetStock.getdataHistory());
        table = new TableGUI(targetStock.getTicker());
        
        // add action listeners
        // set table buttons to adjust time frame of chart
        for(JButton but : table.getButtons()) {
            but.addActionListener(new DateAdjustAction());
        }
        
        setLayout(new BorderLayout());
        add(chart, BorderLayout.CENTER);
        add(table, BorderLayout.NORTH);
    }
    
    public void changeTargetStock(Stock s) {
        targetStock = s;
        chart.changeStock(targetStock.getdataHistory());
        table.setStock(targetStock.getTicker());
    }
    
    // ==================================  Action Listeners  ======================================
    /**
     * Changes the time frame displayed in the Chart to 3 months, 6 months, 1 year, or all Data.
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
            dateAdjustMap.put("1Y",  12);
            dateAdjustMap.put("5Y", 60);
            dateAdjustMap.put("ALL", 0);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String timeAdjust = e.getActionCommand();
            int monthsBack = dateAdjustMap.get(timeAdjust);
            TreeMap<LocalDate, OHLCV> tgtData = targetStock.getdataHistory();
            if(monthsBack > 0) {
                LocalDate startDate = tgtData.lastKey().minusMonths(monthsBack);
                for(Map.Entry entry : tgtData.entrySet()) {
                    
                    LocalDate k = (LocalDate) entry.getKey();
                    OHLCV v = (OHLCV) entry.getValue();
                    
                    if(k.isAfter(startDate)) {
                        shortData.put(k, v);
                    }
                }
            } else {
                shortData.putAll(tgtData);
            }
            chart.changeStock(shortData);
        }
    }
}
