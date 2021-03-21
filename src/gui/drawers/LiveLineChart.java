package gui.drawers;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import interfaces.IDataListener;
import interfaces.ITracker;

public class LiveLineChart implements IDataListener {
	private List<String> seriesKeys;
	private List<String> unregisteredSeries;
	private List<Color> defaultLineColors;

	private int timeTracker;
	private int dataReceivedCalls;
	private XYSeriesCollection dataset;
	private JFreeChart chart;

	private String chartName;
	private String xAxisName;
	private String yAxisName;
	
	private HashMap<String, Color> trackerColors;

	public LiveLineChart(String chartName, String xAxisName, String yAxisName, List<ITracker> dataTrackers) {
		seriesKeys = new ArrayList<String>();
		unregisteredSeries = new ArrayList<String>();
		this.chartName = chartName;
		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;

		for (ITracker tracker : dataTrackers) {
			if (seriesKeys.contains(tracker.getTrackerName())) {
				System.err.println("Cannot register tracker with duplicate name!");
				continue;
			}
			tracker.registerDataListener(this);
			seriesKeys.add(tracker.getTrackerName());
		}

		initDefaultLineColors();		
	}

	public ChartPanel getChart() {
		initChart();
		return new ChartPanel(chart);
	}
	
	public void setLineColor(String trackerName, Color color) {
		if (trackerColors == null) {
			trackerColors = new HashMap<String, Color>();
		}
		trackerColors.put(trackerName, color);
	}

	private void initDefaultLineColors() {
		defaultLineColors = new ArrayList<Color>();
		defaultLineColors.add(Color.CYAN);
		defaultLineColors.add(Color.GREEN);
		defaultLineColors.add(Color.ORANGE);
		defaultLineColors.add(Color.RED);
		defaultLineColors.add(Color.BLUE);
	}

	private void initChart() {
		dataset = new XYSeriesCollection();
		for (String seriesName : seriesKeys) {
			dataset.addSeries(new XYSeries(seriesName, false, false));
		}

		chart = ChartFactory.createXYLineChart("LiveLineChart: " + chartName, xAxisName, yAxisName, dataset);

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		for (int i = 0; i < seriesKeys.size(); ++i) {
			Color lineColor;
			if (trackerColors != null && trackerColors.containsKey(seriesKeys.get(i))) {
				lineColor = trackerColors.get(seriesKeys.get(i));
			} else {
				lineColor = defaultLineColors.get(i % defaultLineColors.size());
			}
			
			renderer.setSeriesPaint(i, lineColor);
			renderer.setSeriesStroke(i, new BasicStroke(2.0f));
			renderer.setSeriesShapesVisible(i, false);
		}

		XYPlot plot = chart.getXYPlot();
		plot.setRenderer(renderer);
		plot.setBackgroundPaint(Color.white);

		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);

		plot.setDomainGridlinesVisible(false);

		chart.getLegend().visible = true;
		chart.setTitle(new TextTitle(chartName, new Font("Serif", java.awt.Font.BOLD, 18)));

		timeTracker = 0;
		dataReceivedCalls = -1;
	}
	
	public void resetChart() {	
		timeTracker = 0;
		dataReceivedCalls = -1;
		dataset.removeAllSeries();
		for (String seriesName : seriesKeys) {
			dataset.addSeries(new XYSeries(seriesName, false, false));
		}
	}

	@Override
	public void dataReceived(double value, String key) {
		if (!seriesKeys.contains(key)) {
			if (!unregisteredSeries.contains(key)) {
				System.err
						.println("Tried adding value for key \"" + key + "\" but no key by that name was registered!");
				unregisteredSeries.add(key);
			}
			return;
		}

		if (dataReceivedCalls == seriesKeys.size() - 1) {
			dataReceivedCalls = 0;
			timeTracker++;
		} else {
			dataReceivedCalls++;
		}
//		System.out.println("Data received for " + key);

		dataset.getSeries(key).add(timeTracker, value);
	}
}
