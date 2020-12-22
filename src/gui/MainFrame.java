package gui;

import javax.swing.*;

public class MainFrame extends JFrame {
	private JLabel averageLabel;
	
	public MainFrame() {
		super("Simulation");
		initFrame();
	}

	public MainFrame(String title) {
		super(title);
		initFrame();
	}
	
	private void initFrame() {
		averageLabel = new JLabel("Average:");
		
		setSize(400, 500);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(averageLabel);
		
		add(panel);
	}
	
	public void setLabelText(String txt) {
		averageLabel.setText(txt);
	}
	
}
