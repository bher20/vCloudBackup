package vCloudBackup;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

import util.Constants;

public class DescriptionDialog extends JDialog
{
	private String description;
	private boolean cancelled;
	private boolean useForAll;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField descriptionBox;
	private JCheckBox chckbxUseDescriptionFor;
	
	public String getDescription()
	{
		return description;
	}
	
	public boolean getCancelled()
	{
		return cancelled;
	}
	
	public boolean getUseForAll()
	{
		return useForAll;
	}

	
	/**
	 * Create the dialog.
	 */
	public DescriptionDialog()
	{
		InitilizeComponents();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		cancelled = false;
	}

	
	/**
	 * Create the dialog.
	 */
	public DescriptionDialog(String defaultDescription)
	{
		InitilizeComponents();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		descriptionBox.setText(defaultDescription);
		
		cancelled = false;	
		
        setAlwaysOnTop(true);
        setModal(true);
	}
	
	
	private void InitilizeComponents()
	{
		setBounds(100, 100, 512, 168);
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHgap(5);
		getContentPane().setLayout(borderLayout);
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblDescription = new JLabel("Description:");
		contentPanel.add(lblDescription);
		
		descriptionBox = new JTextField();
		contentPanel.add(descriptionBox);
		descriptionBox.setColumns(40);
		
		chckbxUseDescriptionFor = new JCheckBox(Constants.Message.SET_DESCRIPTION_FOR_ALL);
		chckbxUseDescriptionFor.setSelected(true);
		contentPanel.add(chckbxUseDescriptionFor);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				description = descriptionBox.getText();
				useForAll = chckbxUseDescriptionFor.isSelected();
				
				setVisible(false);
			}
		});
			
			
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				cancelled = true;
				
				setVisible(false);
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
	}
}
