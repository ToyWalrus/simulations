package gui;

import java.awt.*;
import java.sql.Date;
import java.util.List;

import javax.swing.JPanel;

import java.util.ArrayList;

import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import interfaces.IDataListener;
import interfaces.ITracker;

public class LiveLineChart extends JPanel implements IDataListener {
	private List<String> seriesKeys;
	private List<String> unregisteredSeries;
	private List<Color> lineColors;

	private int timeTracker;
	private int dataReceivedCalls;
	private XYSeriesCollection dataset;
	private JFreeChart chart;
	private boolean recording;

	public LiveLineChart(List<ITracker> dataTrackers, int expectedNumDataPoints) {		
		seriesKeys = new ArrayList<String>();
		unregisteredSeries = new ArrayList<String>();
		recording = false;

		int numDataTrackers = 0;
		for (ITracker tracker : dataTrackers) {
			if (seriesKeys.contains(tracker.getTrackerName())) {
				System.err.println("Cannot register tracker with duplicate name!");
				continue;
			}
			tracker.registerDataListener(this);
			seriesKeys.add(tracker.getTrackerName());
			numDataTrackers++;
		}

		initLineColors();
		initChart(numDataTrackers, expectedNumDataPoints);
	}
	
	public void startRecording() {
		recording = true;
	}
	
	public void stopRecording() {
		recording = false;
	}

	public ChartPanel getChart() {
		return new ChartPanel(chart);
	}

	private void initLineColors() {
		lineColors = new ArrayList<Color>();
		lineColors.add(Color.CYAN);
		lineColors.add(Color.GREEN);
		lineColors.add(Color.ORANGE);
		lineColors.add(Color.RED);
		lineColors.add(Color.BLUE);
	}

	private void initChart(int numSeries, int expectedNumDataPoints) {
		dataset = new XYSeriesCollection();
		for (String seriesName : seriesKeys) {			
			dataset.addSeries(new XYSeries(seriesName, false, false));
		}

		chart = ChartFactory.createXYLineChart("Population Over Time", "Generations", "Population", dataset);

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		for (int i = 0; i < numSeries; ++i) {
			renderer.setSeriesPaint(i, lineColors.get(i % lineColors.size()));
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
		chart.setTitle(new TextTitle("Population Over Time", new Font("Serif", java.awt.Font.BOLD, 18)));
		
		timeTracker = 0;
		dataReceivedCalls = -1;
	}

	@Override
	public void dataReceived(double value, String key) {
		if (!recording) return;
		
		if (!seriesKeys.contains(key)) {
			if (!unregisteredSeries.contains(key)) {
				System.err
						.println("Tried adding value for key \"" + key + "\" but no key by that name was registered!");
				unregisteredSeries.add(key);
			}
			return;
		}
		
		if (dataReceivedCalls == seriesKeys.size() - 1) {
			repaint();
			dataReceivedCalls = 0;
			timeTracker++;
		} else {		
			dataReceivedCalls++;
		}
		
		dataset.getSeries(key).add(timeTracker, value);
	}
}
