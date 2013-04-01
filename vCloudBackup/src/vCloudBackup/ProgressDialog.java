package vCloudBackup;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

public class ProgressDialog extends JDialog
{
	private final JPanel contentPanel = new JPanel();
	
	private JProgressBar statusBar;
	JLabel statusLabel;
	
	
	/**
	 * Create the dialog.
	 */
	public ProgressDialog(boolean indeterminate)
	{
		setUndecorated(true);
		setResizable(false);
		//setModal(true);
		setAlwaysOnTop(true);
		setBounds(100, 100, 450, 97);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 450, 101);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		statusLabel = new JLabel("Status");
		statusLabel.setBounds(10, 11, 420, 14);
		contentPanel.add(statusLabel);
		
		statusBar = new JProgressBar();
		statusBar.setIndeterminate(indeterminate);
		statusBar.setBounds(10, 44, 430, 46);
		contentPanel.add(statusBar);
	}
	
	
	public void UpdateProgressBarValue(int newValue)
	{
		statusBar.setValue(newValue);
	}
	
	
	public int GetProgressBarValue()
	{
		return statusBar.getValue();
	}
	
	
	public void UpdateStatusLabel(String newValue)
	{
		statusLabel.setText(newValue);
	}
}
