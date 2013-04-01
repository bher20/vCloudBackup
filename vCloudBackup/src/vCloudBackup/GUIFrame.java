/**
 * @author Brad Herring 
 * Main GUI for the application.
 */
package vCloudBackup;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import javax.swing.DefaultCellEditor;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;

import operations.FileOps;

import org.xml.sax.SAXException;

import util.Constants;
import util.Settings;
import util.Settings.VCloudServer;
import vCloudBackupLib.Backup;
import vCloudBackupLib.Server;
import vCloudBackupLib.Server.Environment;
import javax.swing.JSeparator;
import javax.swing.JButton;
import java.awt.BorderLayout;


/**
 * @author Brad Herring
 *	The main class for the application
 */
public class GUIFrame extends javax.swing.JFrame 
{
    private Backup backup;
    private static Settings settings;
   
    
    /**
     * Creates new form GUIFrame
     */
    public GUIFrame() {
    	addWindowListener(new WindowAdapter() {
    		@Override
    		public void windowClosing(WindowEvent arg0) 
    		{
    			Exit();
    		}
    	});
        initComponents();
        
        
        this.setTitle(Constants.APPLICATION_TITLE);
    }
    
    
    private void initComponents() 
    {	    	
        jScrollPane1 = new javax.swing.JScrollPane();
        serverTable = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        fileMenuLogin = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        serverTable.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"Backup", "Environment", "Server"
        	}
        ) {
        	Class[] columnTypes = new Class[] {
        		Boolean.class, Object.class, Object.class
        	};
        	public Class getColumnClass(int columnIndex) {
        		return columnTypes[columnIndex];
        	}
        	boolean[] columnEditables = new boolean[] {
        		true, false, false
        	};
        	public boolean isCellEditable(int row, int column) {
        		return columnEditables[column];
        	}
        });
        serverTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        serverTable.getColumnModel().getColumn(0).setMinWidth(60);
        serverTable.getColumnModel().getColumn(0).setMaxWidth(60);
        serverTable.getColumnModel().getColumn(1).setResizable(false);
        serverTable.getColumnModel().getColumn(2).setResizable(false);
        serverTable.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(serverTable);
        

        fileMenu.setText(Constants.Menu.MAIN_MENU_FILE);

        fileMenuLogin.setText(Constants.Menu.MAIN_MENU_FILE_LOGIN);
        fileMenuLogin.setToolTipText(Constants.Menu.MAIN_MENU_FILE_LOGIN_TOOLTIP);
        fileMenuLogin.setActionCommand("loginMenuItem");
        fileMenuLogin.setInheritsPopupMenu(true);
        fileMenuLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(fileMenuLogin);

        jMenuBar1.add(fileMenu);
        
        JMenuItem fileMenuExit = new JMenuItem(Constants.Menu.MAIN_MENU_FILE_EXIT);
        fileMenuExit.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent arg0) 
        	{        		
        		Exit();
        		System.exit(0);
        	}
        });
        
        mnExportVms = new JMenu("Export VMs");
        fileMenu.add(mnExportVms);
        
        mntmSelectServers = new JMenuItem("Selected Servers");
        mntmSelectServers.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) 
        	{
        		ExportServers(GetSelectedServers());
        	}
        });
        mnExportVms.add(mntmSelectServers);
        
        separator_2 = new JSeparator();
        mnExportVms.add(separator_2);
        
        mntmAllVcloudServers = new JMenuItem("All vCloud Servers");
        mntmAllVcloudServers.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent arg0) 
        	{
        		ExportServers(true);
        	}
        });
        mnExportVms.add(mntmAllVcloudServers);
        
        mntmSingleDatacenterVcloud = new JMenuItem("Single Datacenter vCloud Servers");
        mntmSingleDatacenterVcloud.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) 
        	{
        		ExportServers(false);
        	}
        });
        mnExportVms.add(mntmSingleDatacenterVcloud);
        
        JSeparator separator = new JSeparator();
        fileMenu.add(separator);
        fileMenu.add(fileMenuExit);

        setJMenuBar(jMenuBar1);
        
        viewMenu = new JMenu(Constants.Menu.MAIN_MENU_VIEW);
        jMenuBar1.add(viewMenu);
        
        viewMenuSelectProductionServers = new JMenuItem("Select Production Servers");
        viewMenu.add(viewMenuSelectProductionServers);
        
        viewMenuSelectStagingServers = new JMenuItem("Select Staging Servers");
        viewMenuSelectStagingServers.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent arg0) 
        	{
        		SelectAll(Server.Environment.STAGING);
        	}
        });
        viewMenu.add(viewMenuSelectStagingServers);
        
        viewMenuSelectDevelopmentServers = new JMenuItem("Select Development Servers");
        viewMenuSelectDevelopmentServers.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent arg0) 
        	{
        		SelectAll(Server.Environment.DEVELOPMENT);
        	}
        });
        viewMenu.add(viewMenuSelectDevelopmentServers);
        
        mntmSelectAllServers = new JMenuItem("Select All Servers");
        mntmSelectAllServers.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent arg0) 
        	{
        		SelectAll();
        	}
        });
        
        JSeparator separator_1 = new JSeparator();
        viewMenu.add(separator_1);
        viewMenu.add(mntmSelectAllServers);
        
        mntmSelectNoServers = new JMenuItem("Deselect All Servers");
        mntmSelectNoServers.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent arg0) 
        	{
        		DeselectAll();
        	}
        });
        viewMenu.add(mntmSelectNoServers);
        viewMenuSelectProductionServers.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent arg0) 
        	{
        		SelectAll(Server.Environment.PRODUCTION);
        	}
        });
        getContentPane().setLayout(new BorderLayout(0, 0));
        getContentPane().add(jScrollPane1);
        
        backupButton = new JButton("Backup");
        backupButton.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent arg0) 
        	{
        		ArrayList<Server> selectedServers = GetSelectedServers();
        		
        		
        		if (selectedServers.size() > 0)
        		{
	        		Calendar calendar = Calendar.getInstance();        		
	        		SimpleDateFormat  dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	        		
	        		
	    			DescriptionDialog desc = new DescriptionDialog(dateFormat.format(calendar.getTime()) + 
	    					Constants.Message.CATALOG_BACKUP_DEFUALT_DESCRIPTION);
	        		desc.setVisible(true);
	    			
	        		if (desc.getCancelled())
	        			return;
	        		
	        		for (Server server : selectedServers)
	        		{
	        			backup.BackupServer(server, desc.getDescription());
	        		}     
        		}
        		
        		else
        		{
        			JOptionPane.showMessageDialog(null, Constants.Message.NO_SERVERS_SELECTED_TITLE, 
        					Constants.Message.NO_SERVERS_SELECTED_MSG, JOptionPane.WARNING_MESSAGE);
        		}
        	}
        });
        getContentPane().add(backupButton, BorderLayout.SOUTH);

        pack();
    }

    
    /**
     * Log into a vCenter Server API
     * @param evt
     */
    private void loginMenuItemActionPerformed(java.awt.event.ActionEvent evt) 
    {
    	ProgressMonitor progressMonitor = new ProgressMonitor(this,
                "Logging in to vCloud API",
                "", 0, 100);
    	
    	LoginDialog loginD = new LoginDialog(this, true, settings, Constants.Debug.USERNAME, Constants.Debug.PASSWORD);
        loginD.setVisible(true);
        
        if (loginD.getCancelled())
        	return;
        
        String username = loginD.getUsername();
        String password = loginD.getPassword();
        Settings.VCloudServer vCloudServer = loginD.getVCloudServer();
        
        try
		{
        	
        	//pd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        	//pd.setVisible(true);
        	
			backup = new Backup(username, password, settings.getOrganization(), vCloudServer.getUrl(), vCloudServer.getName(), settings.getCatalogName());
		} 
        
        catch (SAXException | IOException | ParserConfigurationException e)
		{
        	//pd.setVisible(false);
        	
        	ShowBadSettingsFileError(false);
		}
        
        if (backup.getLoggedIn())
        {
        	backup.Logout();
        }
        
        if(backup.Login())
        {
        	AddServers(backup.getServers());
        }
        
        else
        {
        	JOptionPane.showMessageDialog(null, Constants.Message.LOGIN_FAILED_MSG, 
        			Constants.Message.LOGIN_FAILED_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUIFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUIFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUIFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUIFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() 
            {
                new GUIFrame().setVisible(true);
                
                settings = new Settings();
                try
				{
					settings.ReadSettings();
				} catch (SAXException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem fileMenuLogin;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable serverTable;
    private JMenuItem viewMenuSelectProductionServers;
    private JMenuItem viewMenuSelectStagingServers;
    private JMenuItem viewMenuSelectDevelopmentServers;
    private JMenu viewMenu;
    private JMenuItem mntmSelectAllServers;
    private JMenuItem mntmSelectNoServers;
    private JButton backupButton;
    private JMenu mnExportVms;
    private JMenuItem mntmAllVcloudServers;
    private JMenuItem mntmSingleDatacenterVcloud;
    private JMenuItem mntmSelectServers;
    private JSeparator separator_2;



    /**
     * Add an ArrayList of servers to the serverTable in the serverTable.
     * @param servers The ArrayList of servers to be added to the serverTable.
     */
    private void AddServers(ArrayList<Server> servers)
    {       
        DefaultTableModel mod = (DefaultTableModel) serverTable.getModel();
        
        try
        {
    		Server serv = new Server();
    		Collections.sort(servers, serv.new ServerNameComparator());
    		Collections.sort(servers, serv.new ServerEnvironmentComparator());
        	
            for (Server server : servers)
            {
                Object [] row = {
                	null, 
                	server.getEnvironment(),
                	server
                };
                
                mod.addRow(row);
            }

            serverTable.getColumn(serverTable.getColumnName(Constants.Table.BACKUP_COLUMN)).setCellEditor(new DefaultCellEditor(new javax.swing.JCheckBox())); 
            
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Check the checkbox next to the servers that have an environment 
     * 	matching the passed in one in the serverTable.
     * @param environment The servers that match the passed in environment.
     */
    private void SelectAll(Server.Environment environment)
    {
        DefaultTableModel mod = (DefaultTableModel) serverTable.getModel();
        
        int rowCount = mod.getRowCount();
        
        if (rowCount > 0)
        {
	        for (int i = 0; i < rowCount; i++)
	        {
	        	Server.Environment currentRowEnv = (Environment) mod.getValueAt(i, Constants.Table.ENVIRONMENT_COLUMN);
	        	
	        	if (currentRowEnv == environment)
	        		mod.setValueAt(true, i, Constants.Table.BACKUP_COLUMN);
	        	else
	        		mod.setValueAt(false, i, Constants.Table.BACKUP_COLUMN);
	        }
        }
    }
    
    
    /**
     * Select all of the servers in the serverTable.
     */
    private void SelectAll()
    {
        DefaultTableModel mod = (DefaultTableModel) serverTable.getModel();
        
        int rowCount = mod.getRowCount();
        
        if (rowCount > 0)
        {
	        for (int i = 0; i < rowCount; i++)
	        {	        	
	        	mod.setValueAt(true, i, Constants.Table.BACKUP_COLUMN);
	        }
        }
    }
    
    
    /**
     * Deselect all of the servers in the serverTable.
     */
    private void DeselectAll()
    {
        DefaultTableModel mod = (DefaultTableModel) serverTable.getModel();
        
        int rowCount = mod.getRowCount();
        
        if (rowCount > 0)
        {
	        for (int i = 0; i < rowCount; i++)
	        {	        	
	        	mod.setValueAt(false, i, Constants.Table.BACKUP_COLUMN);
	        }
        }
    }
    
    
    /**
     * Get all of the servers that have been selected in the serverTable.
     * @return An ArrayList containing the servers selected in the serverTable.
     */
    private ArrayList<Server> GetSelectedServers()
    {
    	ArrayList<Server> servers = new ArrayList<Server>();
    	
        DefaultTableModel mod = (DefaultTableModel) serverTable.getModel();
        
        int rowCount = mod.getRowCount();
        
        if (rowCount > 0)
        {
	        for (int i = 0; i < rowCount; i++)
	        {	        	
	        	Object obj = mod.getValueAt(i, Constants.Table.BACKUP_COLUMN);
	        	
	        	if (obj != null && (boolean)obj)
	        	{
	        		servers.add((Server)mod.getValueAt(i, Constants.Table.SERVER_OBJECT_COLUMN));
	        	}
	        }
        }
        
        
        return servers;
    }
    
    
    /**
     * Export servers, either all of the servers in every
     * 	vCloud or in a specific vCloud.
     * @param allServers Export all servers in every vCloud.
     * @return True if the servers were exported successfully,
     * 	false otherwise.
     */
    private boolean ExportServers(boolean allServers)
    {
    	Backup backupExport;
    	File fileToSave;
		
    	
    	
    	fileToSave = GetFileToSave();
		
    	if (fileToSave == null)
    		return false;
		
    	
    	
		
    	ArrayList<Server>  servers = GetServersToExport(allServers);
		

		try
		{
			backupExport = new Backup();
			backupExport.ServerDetailExcel(servers, fileToSave.getAbsolutePath());
			
			JOptionPane.showMessageDialog(this, Constants.File.SAVE_EXCEL_DONE_MSG + "\n" + fileToSave.getAbsolutePath(), 
					Constants.File.SAVE_EXCEL_DONE_TITLE, JOptionPane.INFORMATION_MESSAGE);
		} 
		
		catch (SAXException | IOException | ParserConfigurationException e1)
		{
			ShowBadSettingsFileError(true);
		}
		
		
		return true;
    }
    
    
    /**
     * Export the passed in servers.
     * @param servers The servers to be exported.
     * @return True if the servers were exported successfully,
     * 	false otherwise.
     */
    private boolean ExportServers(ArrayList<Server> servers)
    {
    	Backup backupExport;
    	File fileToSave;
    	
    	
		fileToSave = GetFileToSave();
		
    	if (fileToSave == null)
    		return false;
		
		
		
		try
		{
			backupExport = new Backup();
			backupExport.ServerDetailExcel(servers, fileToSave.getAbsolutePath());
			
			JOptionPane.showMessageDialog(this, Constants.File.SAVE_EXCEL_DONE_MSG + fileToSave.getAbsolutePath(), 
					Constants.File.SAVE_EXCEL_DONE_TITLE, JOptionPane.INFORMATION_MESSAGE);
		} 
		catch (SAXException | IOException | ParserConfigurationException e)
		{
        	ShowBadSettingsFileError(true);
		}
		
		return true;
    }
    
    
    /**
     * Get the path to the file that will contain the server information,.
     * @return The File object representing the file to be saved.
     */
    private File GetFileToSave()
    {
    	File fileToSave;
    	JFileChooser fileChooser = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter(Constants.File.EXCEL_DESCRIPTION, 
				Constants.File.EXCEL_EXTENTION);
		
		fileChooser.setDialogTitle(Constants.File.SAVE_EXCEL_FILE);   
		fileChooser.addChoosableFileFilter(filter);
		
		int userSelection = fileChooser.showSaveDialog(null);
		

		if (userSelection == JFileChooser.APPROVE_OPTION) 
		{
		    File tempFileToSave = fileChooser.getSelectedFile();
		    
		    String extention = FileOps.GetExtention(tempFileToSave.getName());
		    
		    
		    
		    if (!extention.equals(Constants.File.EXCEL_EXTENTION))
		    {
		    	fileToSave = new File(tempFileToSave.getAbsolutePath() + "." + Constants.File.EXCEL_EXTENTION);
		    }
		    
		    else
		    {
		    	fileToSave = tempFileToSave;
		    }
		    
		    
		    if (fileToSave.exists())
		    {
		    	int result = JOptionPane.showConfirmDialog(this, Constants.File.SAVE_FILE_EXISTS_MSG, Constants.File.SAVE_FILE_EXISTS_TITLE, JOptionPane.YES_NO_OPTION);
		    	
		    	if (result == JOptionPane.NO_OPTION)
		    		return null;
		    }
		}
		
		else 
			return null; 
		
		return fileToSave;
    }
    
    
    /**
     * Get the servers that will be exported.
     * @param allServers Set to true, to export every server in all vCloud servers, false
     * 	to only export servers from one vCloud.
     * @return An ArrayList of servers to export.
     */
    private ArrayList<Server> GetServersToExport(boolean allServers)
    {
    	LoginDialog ld;
		ArrayList<VCloudServer> vCloudServers = new ArrayList<VCloudServer>();
		ArrayList<Server> servers = new ArrayList<Server>();
		

		
		
		
		
		if (allServers)
		{
			ld = new LoginDialog(null, true, settings, Constants.Debug.USERNAME, Constants.Debug.PASSWORD, false);
			ld.setVisible(true);
			
			vCloudServers = settings.getvCloudServers();
		}
		
		else 
		{
			ld = new LoginDialog(null, true, settings, Constants.Debug.USERNAME, Constants.Debug.PASSWORD);
			ld.setVisible(true);
			
			vCloudServers.add(ld.getVCloudServer());
		}
		
		
		
		
		if (ld.getCancelled())
		{
			return null;        			
		}
		
		
		
		
		
		for (VCloudServer vCloudServer : vCloudServers)
		{     			
			Backup getAllBackup = null;
			
			try
			{
				getAllBackup = new Backup (ld.getUsername(), ld.getPassword(), settings.getOrganization(), 
						vCloudServer.getUrl(), vCloudServer.getName(), settings.getCatalogName());
			} 
			
			catch (SAXException | IOException | ParserConfigurationException e)
			{
	        	ShowBadSettingsFileError(true);
			}
			
			
			if (!getAllBackup.Login())
			{
				JOptionPane.showMessageDialog(null, Constants.Message.LOGIN_FAILED_MSG, 
	        			Constants.Message.LOGIN_FAILED_TITLE, JOptionPane.ERROR_MESSAGE);
				
				return null;
			}

    		servers.addAll(getAllBackup.getServers());
    		
    		getAllBackup.Logout();
		}
		
		
		return servers;
    }
    
    
    /**
     * Show an error message indicating that the settings file for 
     * 	either the main app or the backuplib is malformed.
     * @param backupLib Whether or not to indicate that the backuplib 
     * 	is the one that is throwing the error.
     */
    private void ShowBadSettingsFileError(boolean backupLib)
    {
    	if (backupLib)
    		JOptionPane.showMessageDialog(null, Constants.Message.SETTINGS_FILE_MALFORMED_LIB_MSG, 
    				Constants.Message.SETTINGS_FILE_MALFORMED_TITLE, JOptionPane.ERROR_MESSAGE);
    
    	else
    		JOptionPane.showMessageDialog(null, Constants.Message.SETTINGS_FILE_MALFORMED_MAIN_MSG, 
    				Constants.Message.SETTINGS_FILE_MALFORMED_TITLE, JOptionPane.ERROR_MESSAGE);    		
    }
    
    
    /**
     * Execute tasks that should be done when the application
     * 	exits.
     */
    private void Exit()
    {
    	if (backup != null && backup.getLoggedIn())
		{
			backup.Logout();
		}
    }
}
