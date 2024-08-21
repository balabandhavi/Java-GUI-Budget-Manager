import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;

public class PieChart extends JPanel {
    public PieChart(String[] sections, double[] values, Color[] colors) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (int i = 0; i < sections.length; i++) {
            dataset.setValue(sections[i], values[i]);
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Income vs Expense",
                dataset,
                true,
                true,
                false
        );

        // Get the plot and set the section colors
        PiePlot plot = (PiePlot) chart.getPlot();
        for (int i = 0; i < sections.length; i++) {
            plot.setSectionPaint(sections[i], colors[i]);
        }

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
    }
}
