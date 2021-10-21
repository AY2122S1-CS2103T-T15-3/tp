package seedu.academydirectory.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * A ui for the chart displayed.
 */
public class ChartDisplay extends UiPart<Region> {

    private static final String FXML = "ChartDisplay.fxml";

    @FXML
    private StackPane chartPane;

    public ChartDisplay() {
        super(FXML);
    }

    private BoxAndWhiskerCategoryDataset createDataset() {
        String[] entities = {"RA1", "Midterm", "Final"};

        DefaultBoxAndWhiskerCategoryDataset dataset = new
                DefaultBoxAndWhiskerCategoryDataset();

        for (int i = 0; i < entities.length; i += 1) {
            List<Double> values = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                values.add(10.0 + Math.random() * 10.0);
                values.add(13.0 + Math.random() * 4.0);
            }
            dataset.add(values, "Assessments", entities[i]);
        }
        return dataset;
    }

    private JFreeChart createChart(
            final BoxAndWhiskerCategoryDataset dataset) {

        BoxAndWhiskerRenderer boxRenderer = new BoxAndWhiskerRenderer();

        boxRenderer.setFillBox(true);
        boxRenderer.setSeriesFillPaint(0, Color.BLUE);

        boxRenderer.setSeriesPaint(1, Color.RED);
        boxRenderer.setSeriesPaint(100, Color.BLUE);
        boxRenderer.setMaximumBarWidth(0.1);
        boxRenderer.setMedianVisible(true);
        boxRenderer.setMeanVisible(false);


        DefaultCategoryDataset catData = new DefaultCategoryDataset();
        CategoryAxis xAxis = new CategoryAxis("Type");
        NumberAxis yAxis = new NumberAxis("Value");
        yAxis.setAutoRangeIncludesZero(false);
        CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, boxRenderer);
        JFreeChart chart = new JFreeChart("Test", JFreeChart.DEFAULT_TITLE_FONT, plot, true);

        StandardChartTheme theme = (StandardChartTheme) StandardChartTheme.createDarknessTheme();
        theme.apply(chart);

        boxRenderer.setSeriesPaint(0, new Color(0, 255, 0, 150));

        return chart;
    }

    public void drawChart() {
        this.chartPane.getChildren().add(new ChartViewer(createChart(createDataset())));
    }
}
