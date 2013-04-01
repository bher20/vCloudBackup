/**
 * 
 */
package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Brad Herring
 * Holds the settings for the application, read in from an xml file.
 */
public class Settings
{
	/**
	 * The default path to the settings.xml file.
	 */
	public static final String DEFAULT_XML_PATH = "settings.xml";
	
	/**
	 * The name of the organization tag in the settings.xml file.
	 */
	private static final String ORGANIZATION = "organization";
	/**
	 * The name of the system tag in the settings.xml file.
	 */
	private static final String SYSTEM = "system";
	/**
	 * The name of the vCloudServers tag in the settings.xml file.
	 */
	private static final String V_CLOUD_SERVERS = "vCloudServers";
	/**
	 * The name of the vCloudServer tag in the settings.xml file.
	 */
	private static final String V_CLOUD_SERVER = "server";
	/**
	 * The name of the vCloudServer tag in the settings.xml file.
	 */
	private static final String CATALOG = "catalog";
	
	/**
	 * The path to the settings file.
	 */
	private String settingsFilePath;
	
	/**
	 * The name of the organization.
	 */
	private String organization;
	/**
	 * The name of the private catalog
	 */
	private String catalogName;
	/**
	 * An ArrayList of VCloudServers.
	 */
	private ArrayList<VCloudServer> vCloudServers;

	
	
	/**
	 * Set the path to the settings file.
	 * @param settingsFilePath The path to the settings file.
	 */
	private void setSettingsFilePath(String settingsFilePath)
	{
		if (SettingsFileExists(settingsFilePath))
			this.settingsFilePath = settingsFilePath;
		
		else
			this.settingsFilePath = DEFAULT_XML_PATH;
	}

	/**
	 * Get the organization name.
	 * @return The name of the organization.
	 */
	public String getOrganization()
	{
		return organization;
	}

	/**
	 * Get the private catalog name.
	 * @return The name of the organization.
	 */
	public String getCatalogName()
	{
		return catalogName;
	}

	/**
	 * Get the ArrayList of VCloudServers read from the settings file.
	 * @return An ArrayList of VCloudServers.
	 */
	public ArrayList<VCloudServer> getvCloudServers()
	{
		return vCloudServers;
	}



















	/**
	 * Default constructor
	 */
	public Settings()
	{
		setSettingsFilePath(DEFAULT_XML_PATH);
		
		vCloudServers = new ArrayList<VCloudServer>();
	}
	
	
	/**
	 * Main constructor
	 * @param pathToXML The path to the settings file.
	 */
	public Settings(String pathToXML)
	{
		setSettingsFilePath(pathToXML);
		
		vCloudServers = new ArrayList<VCloudServer>();
	}
	
	
	/**
	 * Check if the settings file exists.
	 * @param pathToXML The path to the settings file.
	 * @return True, if the file exists, false otherwise.
	 */
	private boolean SettingsFileExists(String pathToXML)
	{
		File f = new File(pathToXML);
		
		
		if(f.exists()) 
		 return true;
		
		else
			return false;
	}
	
	
	/**
	 * Read in the settings file.
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public void ReadSettings() throws SAXException, IOException, ParserConfigurationException
	{
		NodeList nList;
		
		File fXmlFile = new File(this.settingsFilePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		
		
		
		
		
		//System Settings
		nList = doc.getElementsByTagName(SYSTEM);
 
		for (int i = 0; i < nList.getLength(); i++) 
		{
			Node nNode = nList.item(i);
		   
			if (nNode.getNodeType() == Node.ELEMENT_NODE) 
			{
				Element eElement = (Element) nNode;
				
				this.organization = GetTagValue(ORGANIZATION, eElement);
				this.catalogName = GetTagValue(CATALOG, eElement);
			}
		}
		
		
		//vCloudServers Settings
		nList = doc.getElementsByTagName(V_CLOUD_SERVER);
 	
		//Process each server
		for (int i = 0; i < nList.getLength(); i++) 
		{			
			Node nNode = nList.item(i);
			
			
			
			  
			if (nNode.getNodeType() == Node.ELEMENT_NODE) 
			{
				Element eElement = (Element) nNode;
				String name = eElement.getAttribute(VCloudServer.NAME);
				int id = Integer.parseInt(eElement.getAttribute(VCloudServer.ID));
				String url = GetTagValue(VCloudServer.URL, eElement);
				
				
				vCloudServers.add(new VCloudServer(name, id, url));
			}
		}
	}
	
	
	/**
	 * Get the passed in tags value.
	 * @param sTag The tag to get value for.
	 * @param eElement The element to read the tags value from.
	 * @return The value of the tag.
	 */
	private static String GetTagValue(String sTag, Element eElement) 
	{
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	 
	    Node nValue = (Node) nlList.item(0);
	 
		return nValue.getNodeValue();
	}	
	
	
	/**
	 * @author Brad Herring
	 * Represents a VCloudServer.
	 */
	public class VCloudServer
	{
		/**
		 * The name of the name tag in the settings.xml file.
		 */
		public static final String NAME = "name";
		/**
		 * The name of the id tag in the settings.xml file.
		 */
		public static final String ID = "id";
		/**
		 * The name of the url tag in the settings.xml file.
		 */
		public static final String URL = "url";
		
		
		/**
		 * The name of this VCloudServer.
		 */
		private String name;
		/**
		 * The id of this VCloudServer.
		 */
		private int id;
		/**
		 * The url of this VCloudServer.
		 */
		private String url;
		
		
		/**
		 * Get the name of this VCloudServer.
		 * @return The name of this VCloudServer.
		 */
		public String getName()
		{
			return name;
		}
		
		/**
		 * Get the id of this VCloudServer.
		 * @return The id of this VCloudServer.
		 */
		public int getId()
		{
			return id;
		}
		
		/**
		 * Get the url to the API of this VCloudServer.
		 * @return The url of the API of this VCloudServer.
		 */
		public String getUrl()
		{
			return url;
		}







		/**
		 * Main constructor
		 * @param name The name of this VCloudServer.
		 * @param id The id of this VCloudServer.
		 * @param url The url to the API of this VCloudServer.
		 */
		public VCloudServer(String name, int id, String url)
		{
			this.name = name;
			this.id = id;
			this.url = url;
		}
		
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override public String toString() 
		{
			return this.name;
		}
	}
}