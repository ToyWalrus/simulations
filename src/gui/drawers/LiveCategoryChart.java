package gui.drawers;

import interfaces.IDataListener;
import interfaces.ILiveChart;
import interfaces.ITracker;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;

public class LiveCategoryChart implements IDataListener, ILiveChart {
	private List<String> categoryKeys;
	private List<String> unregisteredCategories;
	private List<Color> defaultCategoryColors;
	private HashMap<String, Color> categoryColors;
	private String chartName;

	private JFreeChart chart;
	private DefaultCategoryDataset dataset;
	final private String yAxisLabel = "Count";
	final private String xAxisLabel = "Category";

	public LiveCategoryChart(String chartName, List<String> categoryNames, List<ITracker> dataTrackers) {
		categoryKeys = new ArrayList<>();
		unregisteredCategories = new ArrayList<>();
		this.chartName = chartName;

		for (ITracker tracker : dataTrackers) {
			tracker.registerDataListener(this);
		}
		
		for (String category : categoryNames) {
			if (categoryKeys.contains(category)) {
				System.err.println("Cannot register tracker with duplicate name!");
				continue;
			}
			categoryKeys.add(category);
		}

		initDefaultCategoryColors();
	}

	public ChartPanel getChart() {
		initChart();
		ValueAxis rangeAxis = chart.getCategoryPlot().getRangeAxis();
		rangeAxis.setRange(new Range(0, 10), false, false);
		return new ChartPanel(chart);
	}

	private void initChart() {
		dataset = new DefaultCategoryDataset();
		resetChart();

		chart = ChartFactory.createBarChart("LiveCategoryChart: " + chartName, xAxisLabel, yAxisLabel, dataset);

		BarRenderer renderer = new BarRenderer();
		for (int i = 0; i < categoryKeys.size(); ++i) {
			Color barColor;
			if (categoryColors != null && categoryColors.containsKey(categoryKeys.get(i))) {
				barColor = categoryColors.get(categoryKeys.get(i));
			} else {
				barColor = defaultCategoryColors.get(i % defaultCategoryColors.size());
			}

			renderer.setSeriesPaint(i, barColor);
		}

		CategoryPlot plot = chart.getCategoryPlot();
		plot.setRenderer(renderer);
		plot.setBackgroundPaint(Color.white);

		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);

		plot.setDomainGridlinesVisible(false);

		chart.getLegend().visible = true;
		chart.setTitle(new TextTitle(chartName, new Font("Serif", java.awt.Font.BOLD, 18)));
	}

	private void initDefaultCategoryColors() {
		defaultCategoryColors = new ArrayList<Color>();
		defaultCategoryColors.add(Color.ORANGE);
		defaultCategoryColors.add(Color.RED);
		defaultCategoryColors.add(Color.YELLOW);
		defaultCategoryColors.add(Color.BLUE);
		defaultCategoryColors.add(Color.GREEN);
		defaultCategoryColors.add(Color.CYAN);
	}

	public void setCategoryColor(String categoryName, Color color) {
		if (categoryColors == null) {
			categoryColors = new HashMap<String, Color>();
		}
		categoryColors.put(categoryName, color);
	}

	public void resetChart() {
		dataset.clear();
		for (String categoryName : categoryKeys) {
			dataset.addValue(0, categoryName, yAxisLabel);
		}
	}

	@Override
	public void dataReceived(double value, String category) {
		if (!categoryKeys.contains(category)) {
			if (!unregisteredCategories.contains(category)) {
				System.err.println("Tried adding value for category \"" + category
						+ "\" but no category by that name was registered!");
				unregisteredCategories.add(category);
			}
			return;
		}

		double currentVal = dataset.getValue(category, yAxisLabel).doubleValue();
		currentVal += value;
		dataset.setValue(currentVal, category, yAxisLabel);
	}

}
