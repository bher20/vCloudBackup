/**
 * 
 */
package util;

/**
 * @author Brad Herring
 *	Static Objects.
 */
public class Constants
{
	public static final String APPLICATION_TITLE = "CGI Federal - vCloud Backup Tool";
	
	
	/**
	 * @author Brad Herring
	 *	Constants to be used in message boxes.
	 */
	public class File
	{
	    public static final String EXCEL_DESCRIPTION = "Excel file";
	    public static final String EXCEL_EXTENTION = "xls";
	    
	    public static final String SAVE_FILE_EXISTS_MSG = "Are you sure you want to overwrite?";
	    public static final String SAVE_FILE_EXISTS_TITLE = "Overwrite File";
		public static final String SAVE_EXCEL_FILE = "Specify file to export list";
		public static final String SAVE_EXCEL_DONE_MSG = "The Excel spreadsheet has been created at the following location: ";
		public static final String SAVE_EXCEL_DONE_TITLE = "Excel Spreadsheet Created";
	}
	
	
	/**
	 * @author Brad Herring
	 *	Constants to be used in message boxes.
	 */
	public class Message
	{
	    public static final String LOGIN_FAILED_MSG = "Either your Active Directory account is locked or the username and/or password is invalid.";
	    public static final String LOGIN_FAILED_TITLE = "Invalid Login";

	    public static final String CATALOG_BACKUP_DEFUALT_DESCRIPTION = " - Patching Backup.";
	    public static final String CATALOG_BACKUP_TITLE = "Description of Backup";
	    
	    public static final String SET_DESCRIPTION_FOR_ALL = "Use Description for All"; 
	    
	    public static final String NO_SERVERS_SELECTED_MSG = "No Servers Selected";
	    public static final String NO_SERVERS_SELECTED_TITLE = "You haven't selected any servers. Try selected one or more servers then click backup.";
	
	    public static final String SETTINGS_FILE_MALFORMED_MAIN_MSG = "The settings xml file is not valid. Please verify the settings in the file.";
	    public static final String SETTINGS_FILE_MALFORMED_LIB_MSG = "The backup library settings xml file is not valid. Please verify the settings in the file.";
	    public static final String SETTINGS_FILE_MALFORMED_TITLE = "The settings file has malformed data.";
	}
	
	
	/**
	 * @author Brad Herring
	 *	Constants to be used in menus.
	 */
	public class Menu
	{
	   	public static final String MAIN_MENU_FILE = "File";
	   	public static final String MAIN_MENU_FILE_LOGIN = "Login";
	   	public static final String MAIN_MENU_FILE_LOGIN_TOOLTIP = "Login to vCloud";
	   	public static final String MAIN_MENU_FILE_EXIT = "Exit";
	   	
	   	public static final String MAIN_MENU_VIEW = "View";
	}
	
	
	/**
	 * @author Brad Herring
	 *	Constants to be used for retrieving data in tables.
	 */
	public class Table
	{
	    public static final int	BACKUP_COLUMN = 0;
	    public static final int	ENVIRONMENT_COLUMN = 1;
	   	public static final int	SERVER_OBJECT_COLUMN = 2;
	}
	
	
	/**
	 * @author Brad Herring
	 *	Constants to be used in debugging.
	 */
	public class Debug
	{
	   	public static final String USERNAME = "bherrin6";
	   	public static final String PASSWORD = "Bher202580945g!";
	}
}
