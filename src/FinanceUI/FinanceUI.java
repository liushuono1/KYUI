package FinanceUI;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import org.apache.commons.dbcp2.BasicDataSource;
import bb.gui.ClientConst;
import bb.gui.CommonUI;
import bb.gui.Main;
import bb.gui.base.ClientUI;
import bb.gui.i18n.BB2Translator;
import bb.gui.main.MainUIActionManager;
import free.FreeMenuBar;
import free.FreeRootMenu;
import free.LoginUI;
import KYUI.KYMainUI;

public class FinanceUI extends KYMainUI{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2441925083422838873L;
	private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private static final int INITIAL = 15;//初始化50个连接
    private static final int MAX_ACTIVE = 50;//最大值500个连接
    private static final int MAX_IDLE = 10;//最大空闲10
    private static final long MAX_WAIT = 5 * 1000;//超过500个访问，等待的时间
    static BasicDataSource bds;
    static{
        if(bds == null){
            bds = new BasicDataSource();
        }
        bds.setDriverClassName(DRIVER_NAME);
        bds.setInitialSize(INITIAL);
        bds.setMaxTotal(MAX_ACTIVE);
        bds.setMaxIdle(MAX_IDLE);
        bds.setMaxWaitMillis(MAX_WAIT);
        bds.setTestWhileIdle(true);
        bds.setTimeBetweenEvictionRunsMillis(1000*60*5);
        bds.setMinEvictableIdleTimeMillis(1000*50*15);
        bds.setMaxConnLifetimeMillis(1000*60*29);
        bds.setRemoveAbandonedOnBorrow(true);
        bds.setRemoveAbandonedTimeout(180);
        bds.setValidationQuery("select 1;");

        bds.setUsername("root");//数据库用户
        bds.setPassword("root");//数据库密码
    }
    
	public FinanceUI()
	{
		setConnectingUrl();
		this.setExtendedState(JFrame.NORMAL); 
		setTitle(getTitle()+KYMainUI.department);
	}
	
	private void setConnectingUrl()
	{
		bds.setUrl("jdbc:mysql://"+KYMainUI.SQLIP+":3307/bb2_"+KYMainUI.DBStr);//数据库的路径，
	}
	public boolean isDisplayTree() {
		return true;
	}
	
	
	public FreeMenuBar getKYMenubar()
	{
		FreeMenuBar menubar = getFreeMenuBar();
		menubar.add(createMenu("测试"));
        menubar.add(createExitMenu("退出"));
        return menubar;
	}
	
	private FreeRootMenu createMenu(String text) {
	    FreeRootMenu menu = new FreeRootMenu(text);
	    JMenuItem item = new JMenuItem("添加");
	    item.addActionListener(new ActionListener() {
	 
	        @Override
	        public void actionPerformed(ActionEvent e) {
	           ClientUI ui = new Homepage();  
	            showTab(ui);
	        }
	    });
	    JMenuItem item1 = new JMenuItem("统计");
	    item1.addActionListener(new ActionListener() {
	   	 
	        @Override
	        public void actionPerformed(ActionEvent e) {
	           ClientUI ui = new StatisticHomepage();  
	           showTab(ui);
	        }
	    });
	    menu.add(item);
	    menu.add(item1);
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
	
	public static void main(String[] args)
	{
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
    	Main.launchBizBox(FinanceUI.class.getName());
    	
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
    		LoginUI login;
    		login=LoginUI.getInstance();
    		login.getPasswordField().setText(result.getProperty("password")); 
    		login.reLogin();
    	}	
	}

}

