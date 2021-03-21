package gui.panels;

import java.awt.Dimension;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;

import gui.drawers.LiveLineChart;
import interfaces.ITracker;
import systems.WorldSimulation;

public class ChartPanelArea extends JPanel {
	private WorldSimulation worldSim;
	private Map<String, JPanel> allCharts;
	
	public ChartPanelArea(WorldSimulation worldSim, int areaWidth, int areaHeight) {
		setPreferredSize(new Dimension(areaWidth, areaHeight));
		this.worldSim = worldSim;
		this.allCharts = new TreeMap<>();
	}
	
	public void addChartFor(String label, Class<? extends ITracker> type) {
		if (type == null) return;
		if (allCharts.containsKey(label)) {
			System.out.println("Chart panel already has chart for " + label);
			return;
		}		
		
		JPanel chart = setupChartForTracker(label, type, allCharts.isEmpty());
		if (chart == null) {
			System.out.println("The given world sim doesn't have a tracker for type " + type.getName());
			return;
		}
		
		allCharts.put(label, chart);
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
	
	
	private JPanel setupChartForTracker(String label, Class<? extends ITracker> type, boolean visible) {
		List<ITracker> trackers = worldSim.getTrackersOfType(type);		
		if (trackers.isEmpty()) {
			return null;
		}
		
		LiveLineChart lineChart = new LiveLineChart("Data Visualization", "Time", label, trackers);
		JPanel chartPanel = lineChart.getChart();
		chartPanel.setPreferredSize(getPreferredSize());
		chartPanel.setVisible(visible);
		add(chartPanel);
		
		return chartPanel;
	}
}
