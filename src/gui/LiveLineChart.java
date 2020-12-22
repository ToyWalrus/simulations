package gui;

import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Millisecond;

import interfaces.IDataListener;
import interfaces.ITracker;

public class LiveLineChart implements IDataListener {
	private List<String> seriesKeys;
	private List<String> unregisteredSeries;
	private List<Color> lineColors;

	private int timeTracker;
	private int dataReceivedCalls;
	private DynamicTimeSeriesCollection dataset;
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
		dataset = new DynamicTimeSeriesCollection(numSeries, expectedNumDataPoints);
		dataset.setTimeBase(new Millisecond(new Date(System.currentTimeMillis())));

		int counter = 0;
		for (String seriesName : seriesKeys) {
			dataset.addSeries(new float[expectedNumDataPoints], counter++, seriesName);
		}

		chart = ChartFactory.createTimeSeriesChart("Population Over Time", "Time", "Population", dataset);

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		for (int i = 0; i < numSeries; ++i) {
			renderer.setSeriesPaint(i, lineColors.get(i % lineColors.size()));
			renderer.setSeriesStroke(i, new BasicStroke(2.0f));
		}
		
		XYPlot plot = chart.getXYPlot();
		plot.setRenderer(renderer);
		plot.setBackgroundPaint(Color.white);

		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);

		plot.setDomainGridlinesVisible(false);

		chart.getLegend().visible = true;
		chart.setTitle(new TextTitle("Population", new Font("Serif", java.awt.Font.BOLD, 18)));
		
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
			dataReceivedCalls = 0;
			timeTracker++;
			System.out.println();
			System.out.println("On to next index: " + timeTracker);
		} else {		
			dataReceivedCalls++;
		}
		
		System.out.println("Data received for " + key + ": " + value);

		int seriesIdx = seriesKeys.indexOf(key);
		dataset.addValue(seriesIdx, timeTracker, (float) value);

	}
}
