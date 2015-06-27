package KYUI;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import twaver.TWaverUtil;
import bb.gui.ClientConst;
import bb.gui.ClientContext;
import bb.gui.company.app.AppUtil;
import bb.gui.conf.ConfigurationBuilder;
import free.FreeMainUILogo;
import free.FreeMenu;
import free.FreeMenuBar;
import free.FreeMenuItem;
import free.FreeRootMenu;
import free.FreeSeparator;
import free.FreeUIConditionVisibleFilter;
import free.FreeUtil;
import free.OperateCode;
import free.OperateCodeManager;

public class KYUIUtil {


	public static FreeMenuBar loadMenuBar(String xml, ActionListener action,FreeMenuBar menuBar) {
		//FreeMenuBar menuBar = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(KYMainUI.class.getResource(xml).openStream());
			Element root = doc.getDocumentElement();
			NodeList rootMenus = root.getChildNodes();
			if (rootMenus != null) {
				//menuBar = new FreeShrinkMenuBar();
				for (int i = 0; i < rootMenus.getLength(); i++) {
					org.w3c.dom.Node menu = rootMenus.item(i);
					if (menu.getNodeType() == 1) {
						if (menu.getNodeName().equalsIgnoreCase("menu")) {
							org.w3c.dom.Node attribute = menu.getAttributes()
									.getNamedItem("text");
							String module = "";
							if (attribute != null) {
								module = attribute.getNodeValue();
								module = module.substring(
										module.indexOf(".") + 1).replace(
										"Menu_", "");
							}
							String text = FreeUtil.getStringAttribute(menu, "text");
							text = text.replaceAll("Box", "").trim();
							String id = FreeUtil.getStringAttribute(menu, "id");
							FreeRootMenu rootMenu = new FreeRootMenu();
							rootMenu.setText(text);
							rootMenu.setModule(module);
							menuBar.setMenuItemByID(id, rootMenu);
							rootMenu.putClientProperty("id", id);
							menuBar.add(rootMenu);
							processMenuItem(menuBar, menu, rootMenu, action);
						}
						if (menu.getNodeName().equalsIgnoreCase("logo")) {
							menuBar.add(Box.createGlue());
							FreeMainUILogo label = new FreeMainUILogo();
							JPanel logoPane = new JPanel(new BorderLayout());
							logoPane.setOpaque(false);
							logoPane.add(label, "East");
							menuBar.setLogoComponent(logoPane);
							menuBar.setLbLogo(label);
						}
					}
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		menuBar.adjustMainMenu();
		AppUtil.loadJars();
		AppUtil.loadAppMenu(menuBar);
		return menuBar;
	}

	
	
	private static void processMenuItem(FreeMenuBar menuBar,
			org.w3c.dom.Node menu, JMenuItem parentMenu, ActionListener action) {
		NodeList children = menu.getChildNodes();
		if (children != null) {
			for (int j = 0; j < children.getLength(); j++) {
				org.w3c.dom.Node itemNode = children.item(j);
				if (itemNode.getNodeType() != 1)
					continue;
				boolean isMenuItem = itemNode.getNodeName().equalsIgnoreCase(
						"menuitem");
				boolean isMenu = itemNode.getNodeName()
						.equalsIgnoreCase("menu");
				boolean isSeparator = itemNode.getNodeName().equalsIgnoreCase(
						"separator");
				String condition = FreeUtil.getStringAttribute(itemNode,
						FreeUIConditionVisibleFilter.CONDITION);
				boolean visible = FreeUIConditionVisibleFilter.getInstance()
						.isVisible(condition);
				if (!visible)
					continue;
				if (isSeparator) {
					FreeSeparator separator = new FreeSeparator();
					parentMenu.add(separator);
					continue;
				}
				if (!isMenuItem && !isMenu)
					continue;
				String text = FreeUtil.getStringAttribute(itemNode, "text");
				String tooltip = FreeUtil.getStringAttribute(itemNode, "tooltip");
				Icon icon = getIconAttribute(itemNode, "icon");
				String command = FreeUtil.getStringAttribute(itemNode, "action");
				String id = FreeUtil.getStringAttribute(itemNode, "id");
				OperateCode code = (OperateCode) OperateCodeManager.operateCodeMap
						.get(command);
				visible = code != null ? code.isVisible() : true;
				if (!visible)
					continue;
				condition = FreeUtil.getStringAttribute(itemNode, "condition");
				visible = FreeUIConditionVisibleFilter.getInstance().isVisible(
						condition);
				if (!visible)
					continue;
				JMenuItem menuItem = null;
				if (isMenuItem) {
					text = text != null && !text.isEmpty() ? text
							: FreeUtil.getDescriptionFromActionCode(command);
					tooltip = tooltip != null && !tooltip.isEmpty() ? tooltip
							: FreeUtil.getDescriptionFromActionCode(command);
				}
				if (isMenu) {
					menuItem = new FreeMenu();
				} else {
					menuItem = new FreeMenuItem();
					menuItem.addActionListener(action);
				}
				menuItem.setText(text);
				if (!isMenu)
					menuItem.setToolTipText(tooltip);
				menuItem.setActionCommand(command);
				menuItem.setIcon(icon);
				menuBar.setMenuItemByID(id, menuItem);
				parentMenu.add(menuItem);
				if (isMenu)
					processMenuItem(menuBar, itemNode, menuItem, action);
			}

		}
	}
	
	
	private static Icon getIconAttribute(org.w3c.dom.Node node, String name) {
		String iconURL = FreeUtil.getStringAttribute(node, name);
		if (iconURL != null && !iconURL.isEmpty())
			return TWaverUtil.getIcon(iconURL);
		else
			return null;
	}


	public static Properties readProperties(String Filename)
	{
		Properties prop =new Properties();
		File file = new File(Filename);
		if (file.exists())
			try {
				FileInputStream is = new FileInputStream(file);
				prop.load(is);
				is.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		return prop;
	}

	public static void writeProperties(Properties prop,String Filename)
	{
		File file = new File(Filename);
		if (file.exists())
			try {
				FileOutputStream out = new FileOutputStream(file);
				prop.store(out, "");
				
				out.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
	}

}
