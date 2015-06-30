package Client4CLass;



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import kyHRUI.Student.RegHome;

import org.apache.commons.dbcp2.BasicDataSource;

import bb.gui.AbstractMainUI;
import bb.gui.ClientConst;
import bb.gui.CommonUI;
import bb.gui.Main;
import bb.gui.base.ClientUI;
import bb.gui.i18n.BB2Translator;
import bb.gui.main.ClientChecker;
import bb.gui.main.MainUIActionManager;
import bb.gui.server.CompanySettingServerActionManager;
import bb.gui.swing.DetachUI;
import free.FreeMenuBar;
import free.FreeRootMenu;
import free.LoginUI;
import KYUI.AskForLeaveUI;
import KYUI.EastPanel;
import KYUI.KYMainUI;
import KYUI.MissionControlAction;
import KYUI.RestoreWindow;
import KYUI.ScrollInfoPane;


public class KYClassUI extends KYMainUI{
	
    static BasicDataSource bds = KYMainUI.bds;
    
   // RestoreWindow restoreWin = new RestoreWindow();

    public static String audioFile="189.wav";

	
	public KYClassUI()
	{
		//System.out.println(KYMainUI.SQLIP);
		this.setClientLevel(0);
		this.setExtendedState(JFrame.NORMAL); 
		setTitle(getTitle()+KYMainUI.department);
		this.removeWindowListener(this.getWindowListeners()[0]);
		
		replaceWindowActionListener();
		
		//setEastPane();
		
		this.showTab(RollCallUI.getinstence());
		
		//setupMissonControl();
		
		System.err.println(LoginUI.getInstance().isVisible());
	}
	
	
	final public JPanel getEastPane()
	{
		if(getScrollInfoPane()==null)
		{
			setScrollInfoPane(new ScrollInfoPane(2,"��Ԫ�����׶�԰",""));
			
		}
		return new eastPanel();
	}
	
	
	public void MCstart(String[] prams)
	{
		RegClient();
	}
	private void setupMissonControl() {
		// TODO Auto-generated method stub
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
				HandelNotice(parms);
				
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
				refresh();  //���Ϊ ��ˢ�½ӿ�ͳһ����
			}};
		
		
		
		action.setname("Class action1");
		
		System.out.println(action.getName());
		
		this.addAction(action);
		
		ClsMissionControl MC = new ClsMissionControl(this);
		
		Thread t = new Thread(MC);
		t.start();
		
	}


	public void refresh()
	{
		RollCallUI.getinstence().refresh();
		EastPanel.getInstance().refresh();
	}
	
	public boolean isDisplayTree() {
		return false;
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
	
	public void HandelNotice(String[] noticebody)
	{
		
	}
	
	public void replaceWindowActionListener()
	{
		
		addWindowListener(new java.awt.event.WindowListener() {

			public void windowClosing(java.awt.event.WindowEvent e) {
				WindowIconfied();
			}

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
		RestoreWindow.getInstance().setMainUI(KYClassUI.getInstance());
		RestoreWindow.getInstance().setScrollInfoPane(getScrollInfoPane());
		DetachUI s = RestoreWindow.getDetachRestoreWindow();
		getScrollInfoPane().setLines(1);
		getScrollInfoPane().setPreferredSize(new Dimension(EastPanel.getInstance().getSize().width,25));
		s.setVisible(true);
		
		setExtendedState(JFrame.ICONIFIED);
		setVisible(false);
	}
	
	public void restore() {
		// TODO Auto-generated method stub
		getScrollInfoPane().setLines(2);
		getScrollInfoPane().setPreferredSize(new Dimension(eastPanel.getInstance().getSize().width,100));
		EastPanel.getInstance().getContentPane().add(BorderLayout.NORTH,getScrollInfoPane());
	}
	public FreeMenuBar getKYMenubar()
	{
 
		FreeMenuBar menubar = getFreeMenuBar();
		menubar.add(createMenu("�༶����"));
		
        menubar.add(createFuncItem("����"));
        menubar.add(createRefreshItem("ˢ��"));
        
        menubar.add(createExitMenu("�˳�"));

        
        return menubar;
           
	}
	private FreeRootMenu createRefreshItem(String text) {
		FreeRootMenu menu = new FreeRootMenu(text);
		JMenuItem item = new JMenuItem(text);
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
		JMenuItem item ;
		String showReg=KYMainUI.KYProperties.getProperty("showReg");
		
		System.out.println(showReg);
		if(showReg==null || showReg.equals("1"))
		{
			item = new JMenuItem("����ע��");
		    item.addActionListener(new ActionListener() {
		 
		        @Override
		        public void actionPerformed(ActionEvent e) {
		        	 
		        	//  ��ʱ�Խ�ֹ
		        	//ClientUI ui = RegHome.getInstance();
			        // showTab(ui);
		        }
		    });
		    menu.add(item);
		}
		
		item = new JMenuItem("ֵ��");
	    item.addActionListener(new ActionListener() {
	 
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	 
	        	if(Integer.parseInt((new java.sql.Time(System.currentTimeMillis()).toString().replace(":", "")))<170500)
	        	{
	        		JOptionPane.showMessageDialog(null, "δ��ֵ��ʱ�䣡����");
	        		
	        	}else
	        	{
	        		RollCallUI.getinstence().AfterCare();
	        	}
	        }
	    });
	    menu.add(item);
		
		 item = new JMenuItem("�������");
	    item.addActionListener(new ActionListener() {
	 
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	 ClientUI ui = AskForLeaveUI.getInstance();
		            showTab(ui);
	        }
	    });
	    menu.add(item);
	    return menu;
	}
	
	private FreeRootMenu createMenu(String text) {
	    FreeRootMenu menu = new FreeRootMenu(text);
	    JMenuItem item = new JMenuItem("������");
	    item.addActionListener(new ActionListener() {
	 
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            ClientUI ui = RollCallUI.getinstence();
	            showTab(ui);
	        }
	    });
	    menu.add(item);
	    return menu;
	}
	
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
	    JMenuItem item = new JMenuItem("�˳�ϵͳ");
	    item.addActionListener(new ActionListener() {
	 
	        @Override
	       
	        public void actionPerformed(ActionEvent e) {
	        	 closing();      
	            
	        }
	    });
	    menu.add(item);
	    return menu;
	}
	public void refresh(String msg[])
	{
		RollCallUI.getinstence().refresh();
		EastPanel.getInstance().refresh();
		pushNotification(msg[0] ,msg[1]);
	}
	
	public static void main(String[] args)
	{
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
    	Main.launchBizBox(KYClassUI.class.getName());
    	
    	//new KYUI.KYUITray(null);
    	//autoLogon=false;
    	LoginUI login=LoginUI.getInstance();
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

