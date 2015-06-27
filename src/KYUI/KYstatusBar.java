package KYUI;

import bb.common.EmployeeCardVO;
import bb.common.VersionInformation;
import bb.gui.*;
import bb.gui.base.GeneralUtil;
import bb.gui.company.license.LicenseUtil;
import bb.gui.main.*;
import bb.gui.server.*;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.border.Border;

import free.FreeStatusLabel;
import free.FreeStatusMessageLabel;
import free.FreeStatusSeparator;
import free.FreeStatusTimeLabel;
import free.FreeToolbarButton;
import free.FreeUtil;
import free.OperateCodeManager;
import twaver.TWaverUtil;

// Referenced classes of package free:
//            FreeStatusMessageLabel, FreeStatusTimeLabel, FreeToolbarButton, FreeStatusLabel, 
//            FreeStatusSeparator, FreeUtil, OperateCodeManager

public class KYstatusBar extends MainUIStatusBar {

	public KYstatusBar() {
		productName = "2BizBox";
		lbStatusMessage = new FreeStatusMessageLabel();
		lbStatusTime = new FreeStatusTimeLabel();
		lbServer = createStatusLabel("localhost", "/free/images/server.png");
		btnUser = new FreeToolbarButton("admin",
				TWaverUtil.getIcon("/free/images/user.png"));
		lbVersion = createStatusLabel("", null);
		backgroundImageURL = FreeUtil.getImageURL("statusbar_background.png");
		backgroundLeftImage = FreeUtil
				.getImage("statusbar_background_left.png");
		backgroundRightImage = FreeUtil
				.getImage("statusbar_background_right.png");
		backgroundImageIcon = TWaverUtil.getImageIcon(backgroundImageURL);
		paint = FreeUtil.createTexturePaint(backgroundImageURL);
		leftPane = new JPanel(new BorderLayout());
		rightPane = new JPanel(new FlowLayout(3, 0, 0));
		border = BorderFactory.createEmptyBorder(2, 10, 0, 0);
		enableUserNameAction = true;
		init();
		initStatusBar();
		MainTaskManager.getInstance().setPingTaskDisplay(lbStatusTime);
	}

	private void initStatusBar() {
		getLeftPane().add(lbStatusMessage, "Center");
		String companyName = CompanySettingServerActionManager.getInstance()
				.getCompanyName();
		boolean vipVerified = true;
		try {
			String vipVerifyCompanyName = LicenseUtil
					.getLicenseProperty("v_name");
			if (vipVerifyCompanyName != null
					&& !vipVerifyCompanyName.trim().isEmpty()) {
				vipVerifyCompanyName = vipVerifyCompanyName.trim();
				if (companyName.contains(vipVerifyCompanyName))
					vipVerified = true;
			}
		} catch (Exception ex) {
			ClientUtil.showException(this, ex);
		}
		if (!vipVerified) {
			getRightPane().add(createStatusLabel(companyName, null));
			if (ClientContext.getLocale().equals(ClientConst.CHINESE_LOCALE)) {
				String unregString = GeneralUtil
						.getString("FreeStatusBar.UnregisterUser");
				unregString = (new StringBuilder())
						.append("<html><font color=red><b>")
						.append(unregString).append("</b></font>").toString();
				FreeToolbarButton btnRegister = new FreeToolbarButton(
						unregString, null);
				btnRegister.setToolTipText(GeneralUtil
						.getString("FreeStatusBar.UnregisterUserTooltip"));
				btnRegister.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						ClientUtil.openHttpUrl(KYstatusBar.this,
								"http://www.2bizbox.cn/v");
					}

				});
				getRightPane().add(btnRegister);
			}
		} else {
			getRightPane().add(createStatusLabel(companyName, "/free/images/vip_1.png"));
		}
		addSeparator();
		getRightPane().add(lbServer);
		addSeparator();
		getRightPane().add(btnUser);
		addSeparator();
		getRightPane().add(lbStatusTime);
		addSeparator();
		if (!checkVersion()) {
			lbVersion.setForeground(Color.RED);
			lbVersion.setToolTipText(GeneralUtil.getString(
					"FreeStatusBar.VersionTooltip", new String[] { cVersion,
							sVersion }));
			lbVersion
					.setFont(new Font(lbVersion.getFont().getFontName(), 1, 13));
		}
		getRightPane().add(lbVersion);
		addSeparator();
		FreeToolbarButton btnPoweredby = new FreeToolbarButton(
				(new StringBuilder()).append("Powered by ").append(productName)
						.toString(), null);
		getRightPane().add(btnPoweredby);
		btnPoweredby.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				OperateCodeManager.about();
			}

	
		});
		addSeparator();
		FreeToolbarButton btnWebsite = null;
		final String website = ClientUtil.get2BizBoxWebsite(ClientContext
				.getLocale());
		btnWebsite = new FreeToolbarButton(website, null);
		getRightPane().add(btnWebsite);
		btnWebsite.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ClientUtil.openHttpUrl(KYstatusBar.this,
						(new StringBuilder()).append("http://").append(website)
								.toString());
			}

			
		});
		btnWebsite.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(12));
			}

			public void mouseExited(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(0));
			}

			
		});
		lbServer.setText(ClientContext.getServer());
		btnUser.setText((new StringBuilder())
				.append(ClientContext.getUserName()).append(getSystemName())
				.toString());
		btnUser.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				showUserDetail();
			}

			
		});
	}

	private FreeStatusLabel createStatusLabel(String text, String imageURL) {
		if (imageURL != null)
			return new FreeStatusLabel(text, TWaverUtil.getIcon(imageURL));
		else
			return new FreeStatusLabel(text);
	}

	private void init() {
		setLayout(new BorderLayout());
		add(leftPane, "Center");
		add(rightPane, "East");
		setBorder(border);
		leftPane.setOpaque(false);
		rightPane.setOpaque(false);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(paint);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.drawImage(backgroundLeftImage, 0, 0, null);
		g2d.drawImage(backgroundRightImage,
				getWidth() - backgroundRightImage.getWidth(null), 0, null);
	}

	public JPanel getLeftPane() {
		return leftPane;
	}

	public JPanel getRightPane() {
		return rightPane;
	}

	public void addSeparator() {
		rightPane.add(new FreeStatusSeparator());
	}

	public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width,
				backgroundImageIcon.getIconHeight());
	}

	public void showMessage(String message) {
		lbStatusMessage.setText(message);
	}

	public void showTooltipMessage(String message) {
		lbStatusMessage.setToolTipText(message);
	}

	public JLabel getVersionLabel() {
		return lbVersion;
	}

	public MemoryBarChart getMemoryBar() {
		return null;
	}

	public void setServer(String server) {
		lbServer.setText(server);
	}

	public void setUser(String username) {
		btnUser.setText(username);
	}

	public void setVersion(String version) {
		lbVersion.setText(version);
	}

	private boolean checkVersion() {
		String serverInfo = null;
		try {
			serverInfo = FacadeWrapper.getInstance().getVersionInfo();
		} catch (ServerActionException e) {
			CommonUI.showException(this, e);
		}
		StringTokenizer token = new StringTokenizer(serverInfo, "@");
		sVersion = token.nextToken();
		String guiInfo = VersionInformation.getVersionInfo();
		token = new StringTokenizer(guiInfo, "@");
		cVersion = token.nextToken();
		//return sVersion.equalsIgnoreCase(cVersion);
		return true;
	}

	public boolean isEnableUserNameAction() {
		return enableUserNameAction;
	}

	public void setEnableUserNameAction(boolean enableUserNameAction) {
		this.enableUserNameAction = enableUserNameAction;
	}

	private void showUserDetail() {
		if (isEnableUserNameAction()) {
			String className = (new StringBuilder())
					.append(AbstractMainUI.getInstance().getGUIMainPackage())
					.append(".security.user.UserActionManager").toString();
			try {
				Class clazz = Class.forName(className);
				Method m = clazz.getMethod("showUserBySelf", new Class[] { java.awt.Component.class });
				m.invoke(clazz, new Object[] { this });
			} catch (Exception e1) {
				ClientUtil.showException(e1);
			}
		}
	}

	private String getSystemName() {
		try {
			EmployeeCardVO empVO = HRServerActionManager.getInstance()
					.getEmployeeSelf();
			if (empVO != null) {
				String systemName = empVO.getUserName();
				if (systemName != null && !"".equals(systemName))
					return (new StringBuilder()).append("[").append(systemName)
							.append("]").toString();
			}
		} catch (ServerActionException e) {
			ClientUtil.showException(e);
		}
		return "";
	}

	private String productName;
	private String sVersion;
	private String cVersion;
	private FreeStatusMessageLabel lbStatusMessage;
	private FreeStatusTimeLabel lbStatusTime;
	private FreeStatusLabel lbServer;
	private FreeToolbarButton btnUser;
	private FreeStatusLabel lbVersion;
	private String backgroundImageURL;
	private Image backgroundLeftImage;
	private Image backgroundRightImage;
	private ImageIcon backgroundImageIcon;
	private TexturePaint paint;
	private JPanel leftPane;
	private JPanel rightPane;
	private Border border;
	private boolean enableUserNameAction;

}