package gui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import gui.drawers.EntityDrawer;
import interfaces.ITracker;
import systems.Simulator;
import systems.WorldSimulation;
import systems.trackers.*;

public class DataPanel extends JPanel {
	private Simulator simulator;
	private WorldSimulation worldSim;

	public DataPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	public DataPanel(WorldSimulation simulation) {
		this();
		this.worldSim = simulation;
		this.simulator = new Simulator(List.of(worldSim), 20);
	}

	private void setupDefaultSimulation() {
		int height = getPreferredSize().height;

		worldSim = WorldSimulation.defaultSimulation(height, height);
		worldSim.addTracker(new PopulationTracker("Population"));

		simulator = new Simulator(List.of(worldSim), 30);
	}

	public void setup(int areaWidth, int areaHeight) {
		setPreferredSize(new Dimension(areaWidth, areaHeight));
		if (worldSim == null) {
			setupDefaultSimulation();
		}

		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		WorldPanel wp = new WorldPanel(areaHeight, areaHeight);
		wp.setBorder(BorderFactory.createLineBorder(Color.gray));
		wp.setWorld(worldSim.getWorld());

		ChartPanelArea chartPanelArea = new ChartPanelArea(worldSim, areaHeight, areaHeight);
		addAllChartsForTrackers(chartPanelArea);

		JPanel buttonPanel = setupAllButtons(chartPanelArea);

		row1.add(wp);
		row1.add(buttonPanel);
		row1.add(chartPanelArea);
		add(row1);

		Label slowColorText = new Label("Slowest:");
		Label fastColorText = new Label("Fastest:");
		JPanel slowColor = new JPanel();
		slowColor.setPreferredSize(new Dimension(10, 10));
		slowColor.setBackground(EntityDrawer.LOW_SPEED);
		JPanel fastColor = new JPanel();
		fastColor.setPreferredSize(new Dimension(10, 10));
		fastColor.setBackground(EntityDrawer.HIGH_SPEED);
		row2.add(slowColorText);
		row2.add(slowColor);
		row2.add(Box.createRigidArea(new Dimension(20, 0)));
		row2.add(fastColorText);
		row2.add(fastColor);

		add(row2);
	}

	private JPanel setupAllButtons(ChartPanelArea chartPanelArea) {
		JPanel buttonPanel = new JPanel();
//		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setPreferredSize(new Dimension(200, getPreferredSize().height));
		buttonPanel.setBorder(BorderFactory.createLineBorder(Color.gray));

		int padding = 5;
		final JButton startSimButton = new JButton("Start");
		final JButton pauseSimButton = new JButton("Pause");
		final JButton resetSimButton = new JButton("Reset");
		pauseSimButton.setEnabled(false);

		startSimButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (simulator.hasStarted()) {
					simulator.resumeSimulation();
				} else {
					simulator.startSimulation();
				}
				pauseSimButton.setEnabled(true);
				startSimButton.setEnabled(false);
			}
		});

		pauseSimButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulator.pauseSimulation();
				pauseSimButton.setEnabled(false);
				startSimButton.setEnabled(true);
			}
		});

		resetSimButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulator.resetSimulation();
				startSimButton.setEnabled(true);
				pauseSimButton.setEnabled(false);
			}
		});

		JComboBox<TrackerChartConfiguration> currentlyVisibleChartSelector = new JComboBox<TrackerChartConfiguration>();
		for (TrackerChartConfiguration type : TrackerChartConfiguration.values()) {
			if (type.trackerType() != null) {
				currentlyVisibleChartSelector.addItem(type);
			}
		}

		currentlyVisibleChartSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<TrackerChartConfiguration> box = (JComboBox<TrackerChartConfiguration>) e.getSource();
				TrackerChartConfiguration selectedItem = (TrackerChartConfiguration) box.getSelectedItem();
				chartPanelArea.viewChart(selectedItem.toString());
			}
		});

		addButtonToButtonPanel(buttonPanel, startSimButton, padding);
		addButtonToButtonPanel(buttonPanel, pauseSimButton, padding);
		addButtonToButtonPanel(buttonPanel, resetSimButton, padding);
		buttonPanel.add(new Label("Current Chart:", Label.LEFT));
		addButtonToButtonPanel(buttonPanel, currentlyVisibleChartSelector, padding);

		return buttonPanel;
	}

	private void addAllChartsForTrackers(ChartPanelArea panel) {
		for (TrackerChartConfiguration config : TrackerChartConfiguration.values()) {
			panel.addChartFor(config);
		}
	}

	private void addButtonToButtonPanel(JPanel panel, Component button, int vgap) {
		Dimension buttonSize = new Dimension(120, 30);
		button.setMinimumSize(buttonSize);
		button.setPreferredSize(buttonSize);
		button.setMaximumSize(buttonSize);
		panel.add(button);
		panel.add(Box.createRigidArea(new Dimension(0, vgap)));
	}
}
