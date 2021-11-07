package seedu.academydirectory.ui.creator;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import seedu.academydirectory.model.AdditionalInfo;


public class GraphCreator extends Creator {

    private static final String FXML = "creator/GraphCreator.fxml";

    private final LinkedHashMap<String, List<Integer>> studentAssessmentResults;

    @FXML
    private StackPane placeHolder;

    /**
     * Constructor of Graph Creator
     * @param additionalInfo info to be passed in
     */
    @SuppressWarnings("unchecked")
    public GraphCreator(AdditionalInfo<?> additionalInfo) {
        super(additionalInfo, FXML);
        LinkedHashMap<String, List<Integer>> rawResults = (LinkedHashMap<String, List<Integer>>) additionalInfo.get();
        this.studentAssessmentResults = this.cleanAssessmentResults(rawResults);

        BoxAndWhiskerCategoryDataset dataset = this.createDataset();
        ChartViewer chartViewer = new ChartViewer(createChart(dataset));

        placeHolder.getChildren().add(chartViewer);
    }

    private List<Integer> filterNegative(List<Integer> list) {
        return list.stream().filter(num -> num >= 0).collect(Collectors.toList());
    }

    private LinkedHashMap<String, List<Integer>> cleanAssessmentResults(
            LinkedHashMap<String, List<Integer>> assessmentResults) {
        return assessmentResults
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entrySet -> filterNegative(entrySet.getValue()), (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                },
                LinkedHashMap::new));
    }

    private BoxAndWhiskerCategoryDataset createDataset() {

        DefaultBoxAndWhiskerCategoryDataset dataset = new
                DefaultBoxAndWhiskerCategoryDataset();

        this.studentAssessmentResults.entrySet()
                .stream()
                .forEachOrdered(entrySet -> dataset.add(entrySet.getValue(), "Assessments", entrySet.getKey()));

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

        CategoryAxis xAxis = new CategoryAxis("Assessments");
        NumberAxis yAxis = new NumberAxis("Grade");
        yAxis.setAutoRangeIncludesZero(false);
        CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, boxRenderer);
        JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);

        StandardChartTheme theme = (StandardChartTheme) StandardChartTheme.createDarknessTheme();
        theme.apply(chart);

        boxRenderer.setSeriesPaint(0, new Color(0, 255, 0, 150));

        return chart;
    }

    /**
     * Create the view
     * @return new View
     */
    @Override
    public Node create() {
        return this.getRoot();
    }
}
