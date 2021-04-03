package gui.panels;

import java.awt.Dimension;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;

import gui.drawers.LiveCategoryChart;
import gui.drawers.LiveLineChart;
import interfaces.ITracker;
import systems.WorldSimulation;

public class ChartPanelArea extends JPanel {
	private WorldSimulation worldSim;
	private Map<String, JPanel> allCharts;
	final private String chartName = "Data Visualization"; 

	public ChartPanelArea(WorldSimulation worldSim, int areaWidth, int areaHeight) {
		setPreferredSize(new Dimension(areaWidth, areaHeight));
		this.worldSim = worldSim;
		this.allCharts = new TreeMap<>();
	}

	public void addChartFor(TrackerChartConfiguration config) {
		Class<? extends ITracker> type = config.trackerType();
		if (type == null) {
			return;
		}
		
		String label = config.toString();

		if (allCharts.containsKey(label)) {
			System.out.println("Chart panel already has chart for " + label);
			return;
		}

		List<ITracker> trackers = worldSim.getTrackersOfType(type);
		if (trackers.isEmpty()) {
			System.out.println("The given world sim doesn't have a tracker for type " + type.getName());
			return;
		}

		JPanel chart = null;
		ChartType chartType = config.getChartType();

		if (chartType == ChartType.Line) {
			LiveLineChart llc = new LiveLineChart(chartName, "Time", label, trackers);
			boolean initiallyVisible = allCharts.isEmpty();
			if (config.shouldAutoRange()) {
				chart = setChartSettings(llc.getChart(), initiallyVisible);
			} else {
				chart = setChartSettings(llc.getChart(config.getChartMinY(), config.getChartMaxY()), initiallyVisible);
			}
		} else if (chartType == ChartType.Bar) {
			LiveCategoryChart lcc = new LiveCategoryChart(label, config.getCategories(), trackers);
			boolean initiallyVisible = allCharts.isEmpty();
			chart = setChartSettings(lcc.getChart(), initiallyVisible);
		}

		if (chart != null) {
			allCharts.put(label, chart);
		}
	}

	public void viewChart(String label) {
		if (!allCharts.containsKey(label)) {
			System.out.println("Chart panel doesn't have a chart for " + label);
			return;
		}

		for (Map.Entry<String, JPanel> entry : allCharts.entrySet()) {
			if (entry.getKey() == label) {
				entry.getValue().setVisible(true);
			} else {
				entry.getValue().setVisible(false);
			}
		}
	}

	private JPanel setChartSettings(JPanel chartPanel, boolean visible) {
		chartPanel.setPreferredSize(getPreferredSize());
		chartPanel.setVisible(visible);
		add(chartPanel);

		return chartPanel;
	}
}
