package ManageUI;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;


import javax.swing.JOptionPane;
import javax.swing.JPanel;
import kyHRUI.Student.RegHome;
import kyHRUI.Student.SearchHome;

import org.apache.commons.dbcp2.BasicDataSource;
import twaver.tree.TTree;
import bb.gui.AbstractMainUI;
import bb.gui.BBConfiguration;
import bb.gui.ClientConst;
import bb.gui.CommonUI;
import bb.gui.Main;
import bb.gui.base.ClientUI;
import bb.gui.base.CompanyComboBox;
import bb.gui.base.LanguageComboBox;
import bb.gui.base.ServerComboBox;
import bb.gui.i18n.BB2Translator;
import bb.gui.main.ClientChecker;
import bb.gui.main.MainUIActionManager;
import bb.gui.message.notice.NoticeTreeNode;
import bb.gui.swing.DetachUI;
import free.FreeMenuBar;
import free.FreeRootMenu;
import free.FreeTextField;
import free.FreeUtil;
import free.LoginUI;
import AuthModule.CardAuth;
import AuthModule.CardUtils;
import Client4CLass.eastPanel;
import KYUI.AskForLeaveUI;
import KYUI.EastPanel;
import KYUI.KYMainUI;
import KYUI.MissionControl;
import KYUI.MissionControlAction;
import KYUI.RestoreWindow;
import KYUI.ScrollInfoPane;
import Simple.CardsLib;
import Simple.HR.SimpleNoFeesCount;


public class KYManageUI extends KYMainUI{

	
    static BasicDataSource bds = KYMainUI.bds;
    
    
   // RestoreWindow restoreWin = new RestoreWindow();

    public static String audioFile="189.wav";

	
	public KYManageUI()
	{
		KYMainUI.getInstance();
		//System.out.println(KYMainUI.SQLIP);
		this.setClientLevel(1);
		this.setExtendedState(Frame.NORMAL); 
		setTitle(getTitle()+KYMainUI.department);
		this.removeWindowListener(this.getWindowListeners()[0]);
		
		replaceWindowActionListener();
		CardAuth.setPooledConnection(bds);
		
		
		
		System.err.println("notice name："+((NoticeTreeNode)getNoticeCenterUI().getTreePane().getTTree().getDataBox().getAllElements().get(2)).getName());
		setupMissonControl();
		
		System.out.println(" resource===="+bb.gui.conf.MainTreeHelper.class.getResource("/ManageUI/ManageTree.xml"));
		
		showTab(new bb.gui.company.homepage.CompanyHomepageUI());
		
		replace_node_name();
		System.err.println("kyui--->"+ AbstractMainUI.isInstanced());
		
		closeLoginUI();

	}
	

	private void replace_node_name()
	{
		
		//System.out.println(bb.gui.BBConfiguration.getActionCodeFile());
		
		TTree tree = getMainTreePane().getMainTree();
		//tree.getDataBox().addElement(createStudioSearchNode());
		
		for(twaver.Element ele : (List<twaver.Element>)tree.getDataBox().getAllElements())
		{
			//System.out.println(ele.getName());
			ele.setName(ele.getName().replace("MainTreeHelper.", ""));
		}
	}
	
	
	private void setupMissonControl() {
		MissionControlAction action = new MissionControlAction(){
			String Name= "";

			
			@Override
			public void StratAction(String[] parms) {
				RegClient();
				
			}

			@Override
			public void FinishAction(String[] parms) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void TimeRepeatAction(String[] parms) {
				// TODO Auto-generated method stub
				RegClient();
				
			}


			@Override
			public void setname(String name) {
				// TODO Auto-generated method stub
				Name=name;
			}


			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return Name;
			}


			@Override
			public void RepeatAction(String[] parms) {
				// TODO Auto-generated method stub
				//RollCallUI.getinstence().refreashRollcall();
			}};
		
		
		
		action.setname("1st action");
		
		System.out.println(action.getName());
		
		this.addAction(action);
		
		System.out.println(this.getAction().get(0).getName());
		MissionControl MC = new MissionControl(this)
		{
			//-------complete MC  code here...
		};
		
		Thread t = new Thread(MC);
		t.start();
		
	}

	@Override
	public boolean isDisplayTree() {
		return true;
	}
	
	
	@Override
	public String getMainTreeXML() {
		return "/ManageUI/ManageTree.xml";
	}
	
	@Override
	final public JPanel getEastPane()
	{
		if(getScrollInfoPane()==null)
		{
			setScrollInfoPane(new ScrollInfoPane(2,"测试信息","测试通知"));
			
		}
		return new SimpleMeastPanel();
	}
	
	public void RegClient()
	{
		
		
		System.out.println("Run Reg~~~~~~~~~~~~");
		PreparedStatement pstmt=null;
		Connection conn;
		try {
			conn =bds.getConnection();
			
			
		    pstmt = conn.prepareStatement("UPDATE `ky_clientregiest` SET `OP_ID` = ?, `ClientIP` = ?,`Reg_Time` = ?,`Reg_Date` = ? WHERE ClientID=?;");
		    pstmt.setString(1, KYMainUI.logonUser.getUserName());
		    pstmt.setString(2, KYMainUI.clientIP);
		    pstmt.setTime(3, new java.sql.Time(System.currentTimeMillis()));
		    pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
		    pstmt.setString(5,KYMainUI.department);
		    int r=pstmt.executeUpdate();
		    System.out.println("Run Reg Update~~~~~~~~~~~~");
		    if(r==0)
		    {
		    	pstmt = conn.prepareStatement("INSERT INTO `ky_clientregiest` (`ClientID`,`OP_ID`,`ClientIP`,`Reg_Time`,`Reg_Date`) VALUES (?,?,?,?,?);");
				pstmt.setString(1,KYMainUI.department);
			    pstmt.setString(2, KYMainUI.logonUser.getUserName());
			    pstmt.setString(3, KYMainUI.clientIP);
			    pstmt.setTime(4, new java.sql.Time(System.currentTimeMillis()));
			    pstmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
			    System.out.println("Run Reg Insert~~~~~~~~~~~~");
			    pstmt.execute();
		    }
		    pstmt.close();
		    conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void RegClient(String role)
	{
		
		
		System.out.println("Run Reg~~~~~~~~~~~~");
		PreparedStatement pstmt=null;
		Connection conn;
		try {
			conn =bds.getConnection();
			
			
		    pstmt = conn.prepareStatement("UPDATE `ky_clientregiest` SET `OP_ID` = ?, `ClientIP` = ?,`Reg_Time` = ?,`Reg_Date` = ? WHERE ClientID=?;");
		    pstmt.setString(1, KYMainUI.logonUser.getUserName());
		    pstmt.setString(2, KYMainUI.clientIP);
		    pstmt.setTime(3, new java.sql.Time(System.currentTimeMillis()));
		    pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
		    pstmt.setString(5,role);
		    int r=pstmt.executeUpdate();
		    System.out.println("Run Reg Update~~~~~~~~~~~~");
		    if(r==0)
		    {
		    	pstmt = conn.prepareStatement("INSERT INTO `ky_clientregiest` (`ClientID`,`OP_ID`,`ClientIP`,`Reg_Time`,`Reg_Date`) VALUES (?,?,?,?,?);");
				pstmt.setString(1,role);
			    pstmt.setString(2, KYMainUI.logonUser.getUserName());
			    pstmt.setString(3, KYMainUI.clientIP);
			    pstmt.setTime(4, new java.sql.Time(System.currentTimeMillis()));
			    pstmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
			    System.out.println("Run Reg Insert~~~~~~~~~~~~");
			    pstmt.execute();
		    }
		    pstmt.close();
		    conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void replaceWindowActionListener()
	{
		
		addWindowListener(new java.awt.event.WindowListener() {

			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				WindowIconfied();
			}

			@Override
			public void windowClosed(java.awt.event.WindowEvent e) {
				/*//AbstractMainUI() = false;
				if (AbstractMainUI.getInstance() != null) {
					if (e.getSource() == AbstractMainUI.getInstance())
						MainUIActionManager.exitClient();
					else
						MainUIActionManager.closeMainUI();
					//AbstractMainUI.getInstance() = null;
				}*/
			}

			@Override
			public void windowOpened(java.awt.event.WindowEvent e) {
				ClientChecker.checkBB2(AbstractMainUI.getInstance());
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				WindowIconfied();
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

		});
	}
	
	private void WindowIconfied()
	{
		RestoreWindow.getInstance().setTitle(KYMainUI.department);
		RestoreWindow.getInstance().setMainUI(KYMainUI.getInstance());
		RestoreWindow.getInstance().setScrollInfoPane(getScrollInfoPane());
		DetachUI s = RestoreWindow.getDetachRestoreWindow();
		if(getScrollInfoPane()!=null){
			getScrollInfoPane().setLines(1);
			getScrollInfoPane().setPreferredSize(new Dimension(EastPanel.getInstance().getSize().width,25));
		}
		s.setVisible(true);
		setExtendedState(Frame.ICONIFIED);
		setVisible(false);
	}
	
	@Override
	public void restore() {
		// TODO Auto-generated method stub
		if(getScrollInfoPane()!=null){
			getScrollInfoPane().setLines(2);
			getScrollInfoPane().setPreferredSize(new Dimension(EastPanel.getInstance().getSize().width,100));
			EastPanel.getInstance().add(BorderLayout.NORTH,getScrollInfoPane());
		}
	}
	
	
	@Override
	public FreeMenuBar getKYMenubar()
	{
 
		FreeMenuBar menubar = getFreeMenuBar();
	
		
        menubar.add(createFuncItem("管理"));
        menubar.add(createRefreshItem("功能"));
        
        menubar.add(createExitMenu("退出"));

        
        return menubar;
           
	}
	private FreeRootMenu createRefreshItem(String text) {
		FreeRootMenu menu = new FreeRootMenu(text);
		
		JMenuItem item = new JMenuItem("请假");
	    item.addActionListener(new ActionListener() {
	 
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            ClientUI ui = new AskForLeaveUI();
	        	showTab(ui);
	        }
	    });
	    menu.add(item);
		
		
		item = new JMenuItem("刷新");
	    item.addActionListener(new ActionListener() {
	 
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            refresh();
	        }
	    });
	    menu.add(item);
	    return menu;
	}
	
	private FreeRootMenu createFuncItem(String text) {
		FreeRootMenu menu = new FreeRootMenu(text);
		JMenuItem item = new JMenuItem("添加新生");
	    item.addActionListener(new ActionListener() {
	 

	        @Override
	        public void actionPerformed(ActionEvent e) {

	        	 ClientUI ui = new RegHome();
		            showTab(ui);
	        }
	    });
	    menu.add(item);
	    
	    item = new JMenuItem("查找学生");
	    item.addActionListener(new ActionListener() {
	 

	        @Override
	        public void actionPerformed(ActionEvent e) {

		    	
	        	 ClientUI ui = new SearchHome();
		            showTab(ui);
	        }
	    });
	    
	    menu.add(new free.FreeSeparator());
	    menu.add(item);
	    
	    item = new JMenuItem("查看未缴费名单");
	    item.addActionListener(new ActionListener() {
	 

	        @Override
	        public void actionPerformed(ActionEvent e) {

		    	
	        	 JFrame nofees = new SimpleNoFeesCount();
	        	 
		         nofees.setVisible(true);
	        }
	    });
	    menu.add(item);
	    
	    item = new JMenuItem("查找电梯卡");
	    item.addActionListener(new ActionListener() {
	 

	        @Override
	        public void actionPerformed(ActionEvent e) {

		    	
	        	CardsLib cl=CardsLib.getInstance();
	        	
	        	JOptionPane.showMessageDialog(null,cl.getInfo(CardUtils.GetCardIDn().substring(0, 8)));
	        }
	    });
	    menu.add(item);
	    return menu;
	}
	
	
	
	@Override
	public void closing() {
		if (KYMainUI.getInstance().isCloseAble()) {
			int answer = CommonUI.showWarningConfirm(this,
					BB2Translator.getString("MainUI.CloseBizBox"), 0);
			System.out.println(answer);
			if (answer == 0) {
				//instanced = false;
				dispose();
				finalizingClosing();
			}
		}
	}
	
	public void finalizingClosing()
	{
		 try {
				bds.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
         
		MainUIActionManager.exitClient();
			
	}
	private FreeRootMenu createExitMenu(String text) {
	    FreeRootMenu menu = new FreeRootMenu(text);
	    JMenuItem item = new JMenuItem("退出系统");
	    item.addActionListener(new ActionListener() {
	 
	        @Override
	       
	        public void actionPerformed(ActionEvent e) {
	        	 closing();      
	            
	        }
	    });
	    menu.add(item);
	    return menu;
	}
	@Override
	public void refresh()
	{
		EastPanel.getInstance().refresh();
	//	eastPanel.getInstance().refreshAll();
	}
	
	
	private static JButton createTransparentButton(final LoginUI login) {
		JButton btn = new JButton();
		btn.setMargin(null);
		btn.setOpaque(false);
	
		btn.setContentAreaFilled(false);
		btn.setFocusPainted(false);
		btn.setRequestFocusEnabled(false);
		btn.setBorder(null);
		
		String IMAGE_URL_PREFIX = BBConfiguration.getImageURLPrefix();
    	Locale locale = (new LanguageComboBox(true)).getSelectedLocale();
		btn.setIcon(FreeUtil.getImageIcon("login_button", "png", locale,
				IMAGE_URL_PREFIX));
		btn.setRolloverIcon(FreeUtil.getImageIcon("login_button_rover",
				"png", locale, IMAGE_URL_PREFIX));
		btn.setPressedIcon(FreeUtil.getImageIcon("login_button_pressed",
				"png", locale, IMAGE_URL_PREFIX));
		btn.setDisabledIcon(FreeUtil.getImageIcon("login_button_disable",
				"png", locale, IMAGE_URL_PREFIX));
		btn.setBorder(null);
		
		ActionListener[] old = null;
		
		for(Component comp : login.getContentPane().getComponents())
    	{
			
			System.out.println(comp.getClass().getName());
    		if(comp instanceof JPanel){
    			for(Component compi:((JPanel) comp).getComponents())
    			{
    	
    				if(compi instanceof ServerComboBox )
    				{
    					
    				}
    				
    				if(compi instanceof CompanyComboBox )
    				{
    					
    				}
    				
    				if(compi instanceof FreeTextField )
    				{
    					
    				}
    				 				
    			}
    			
    		}
    		
    		
    		
    		
    		
    		if(comp instanceof JButton )
			{
    			old=((JButton)comp).getActionListeners();
				System.out.println("--->"+((JButton)comp).getActionListeners().length);
			}
    	}
		
		
		
		final ActionListener act=old[0];
		
		ActionListener loginactionListener =new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				boolean valid= CardAuth.Logon_Auth(login,getRequiredLevel());
				System.out.println("-----------.>"+valid);
				if(valid)
					act.actionPerformed(e);
				else
					JOptionPane.showMessageDialog(null, "请检查卡和权限");
			}

			
		};
		btn.addActionListener(loginactionListener);
		
		
		
		login.getPasswordField().addActionListener(loginactionListener);
		//System.out.println(login.getPasswordField().getActionListeners().length);
		//ActionListener actionListener =login.getPasswordField().getActionListeners()[0];
		
		//actionListener.actionPerformed(null);
    	login.getContentPane().add(btn, "South");
    	login.getRootPane().setDefaultButton(btn);
    	login.setDefaultButton();
    	
    	
		return btn;
	}
	
	private static int getRequiredLevel() {
		// TODO Auto-generated method stub
		return clientLevel();
	}
	
	public static void main(String[] args)
	{
		//Properties p =bb.gui.BBConfiguration.loadProperties("/KYUI/conf/Client.properties");
		bb.gui.BBConfiguration.initConfiguration("/KYUI/conf/Client.properties");
		boolean autoLogon=false;
    	if(args.length!=0)
    	{
    		for(int i=0;i<args.length;i++)
    		{
    			if(args[i].contains("auto"))
    			{
    				 autoLogon = true;
    			}
    		}
    	}
    	
    	final LoginUI login=LoginUI.getInstance();

    	JButton btn = createTransparentButton(login);

    	
    	
    	
    	
    	Main.launchBizBox(KYManageUI.class.getName());
    	
    	//new KYUI.KYUITray(null);
    	//autoLogon=false;

    	if(autoLogon)
    	{
    		Properties result = new Properties();
    		File file = new File(ClientConst.getLoginMessPropertyFile());
    		if (file.exists())
    			try {
    				FileInputStream is = new FileInputStream(file);
    				result.load(is);
    				is.close();
    			} catch (IOException ex) {
    				ex.printStackTrace();
    			}
    		
    		login.getPasswordField().setText(result.getProperty("password")); 
    		login.reLogin();
    		
    	}	
	}

}




