package KYUI;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.commons.dbcp2.BasicDataSource;

import twaver.Element;
import free.AppMainUI;
import free.FreeMenuBar;
import free.FreeRootMenu;
import free.LoginUI;
import KYUI.NoticeCenter.NoticeControl;
import bb.common.CompanyVO;
import bb.common.EmployeeCardVO;
import bb.gui.AbstractMainUI;
import bb.gui.ClientConst;
import bb.gui.ClientContext;
import bb.gui.ClientUtil;
import bb.gui.Main;
import bb.gui.ServerActionException;
import bb.gui.base.BB2Node;
import bb.gui.message.notice.NoticeCenterUI;
import bb.gui.server.CompanySettingServerActionManager;
import bb.gui.server.HRServerActionManager;

public class KYMainUI extends AppMainUI implements MissionControlledObj,KYRefreshable {
	public List<MissionControlAction> missionControlAction;
	public static String SQLIP="",department="",clientIP="",DBStr="";
	public static EmployeeCardVO logonUser;
	public static Properties KYProperties=new Properties();
	private static SpeakEngin speakEnging =null;
	static ScrollInfoPane si=null;
	private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private static final int INITIAL = 15;//初始化50个连接
    private static final int MAX_ACTIVE = 50;//最大值500个连接
    private static final int MAX_IDLE = 10;//最大空闲10
    private static final long MAX_WAIT = 5 * 1000;//超过500个访问，等待的时间
    public static BasicDataSource bds;
    static int clientLevel= 0;
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
    
	
	
	private KYUITray tray;
	Container MainView;
	EastPaneHolder eastpanelHolder;
	SimpleDateFormat dateformat=new SimpleDateFormat("yyyy年MM月dd日  E ");   
	//JPanel eastpanel;
	
	
	public void setClientLevel(int level)
	{
		clientLevel=level;
	}
	
	public static int clientLevel()
	{
		return clientLevel;
	}
	
	public ScrollInfoPane getScrollInfoPane()
	{
		return si;
			
	}
	
	public  void setScrollInfoPane(ScrollInfoPane Si)
	{
		si=Si;
			
	}
	public static SpeakEngin getSpeakEngin()
	{
		if(speakEnging==null || !speakEnging.getRunningState())
		{
			speakEnging = new SpeakEngin();
			(new Thread(speakEnging)).start();
		}
		
		return speakEnging;
			
	}
	
	
	public static SpeakEngin initSpeakEngin(String initWords)
	{
		if(speakEnging==null || !speakEnging.getRunningState())
		{
			speakEnging = new SpeakEngin(initWords);
			(new Thread(speakEnging)).start();
		}
		else
		{
			speakEnging.say(initWords);
		}
		
		return speakEnging;
			
	}
	
	
	public static void FinalizeSpeakEngin()
	{
		if(speakEnging!=null || speakEnging.getRunningState())
		{
			speakEnging.finallize();
		}
		
			
	}
	
	
	public KYMainUI()
	{   
		getLoginInfo();
		setConnectingUrl();
		readKYProperties();
		MainView=this.getContentPane();
		this.missionControlAction=MissionControlledObj.missionControlAction;

		if(!isDisplayTree() && isDisplayNoticePane())
		{
			NoticeCenterUI noticeCenterUI = new NoticeCenterUI();
			JPanel NoticePanel= new JPanel();
			NoticePanel.setLayout(new BorderLayout());
			noticeCenterUI.getTreePane().setPreferredSize(new Dimension(200,600));
			
			//noticeCenterUI.getTreePane().getTree().getDataBox().addElement(this.createStudioSearchNode());
			Element ele = noticeCenterUI.getTreePane().getTree().getDataBox().getElementByName("通知用户组管理");
			noticeCenterUI.getTreePane().getTree().getDataBox().removeElement(ele);
			System.out.println("size = "+noticeCenterUI.getTreePane().getTree().getDataBox().size());
			
			NoticePanel.add(noticeCenterUI.getTreePane(),BorderLayout.CENTER);
			MainView.add(NoticePanel,BorderLayout.WEST);
		}
		JPanel contentPane = (JPanel) this.getRootPane().getContentPane();
		
		contentPane.remove(this.getStatusBar());
		contentPane.add(new KYstatusBar(), BorderLayout.SOUTH);
		if(isDisplayTray())
		{
			tray= new KYUITray(this);
		}
		getKYMenubar();
		
		if(isDisplayEastPane()){		

			setEastPane(getEastPane());
		  }
		
		
		setupNoticeControl();
		
		
		
	}
	
	
	
	public void closeLoginUI()
	{
		if(LoginUI.getInstance().isVisible())  //隐藏登陆窗口 
			LoginUI.getInstance().setVisible(false);
	}
	
	public void MCstart(String[] parms)
	{
		
	}
	
	public void MCfinish(String[] parms)
	{
		
	}
	
	
	private void setupNoticeControl() {
		// TODO Auto-generated method stub
		
		MissionControlAction action = new MissionControlAction(){
			String Name=null;
			@Override
			public void setname(String name) {
				// TODO Auto-generated method stub
				this.Name=name;
				
			}

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return Name;
			}

			@Override
			public void StratAction(String[] parms) {
				// TODO Auto-generated method stub
				MCstart(parms);
				
			}

			@Override
			public void FinishAction(String[] parms) {
				// TODO Auto-generated method stub
				MCfinish(parms);
				
			}

			@Override
			public void TimeRepeatAction(String[] parms) {
				// TODO Auto-generated method stub
				DealTimeRepeat(parms);
				
			}

			@Override
			public void RepeatAction(String[] msg) {
				// TODO Auto-generated method stub
				DealMsg(msg);
			}
			
		};
		action.setname("NoticeAction");
		this.addAction(action);
		NoticeControl MsgMC = new NoticeControl(this);
		
		
		Thread t = new Thread(MsgMC);
		t.start();
		
	}
	public void DealTimeRepeat(String[] parms) {
	}
	
	
	public void DealMsg(String[] msg)
	{
		System.out.println("here\n"+msg+"\nend");
		 if(msg[0].equals("ADDMSG"))
	        {
	        	if(msg.length>1)
	        		pushMsg(msg);
	        }else if(msg[0].equals("ADDNOTICE"))
	        {
	        	if(msg.length>1)
	        		pushNotice(msg);
	        }else if(msg[0].equals("ADDONLYMSG"))
	        {
	        	//if(msg.length>1)
	        		pushOnlyMsg(msg);
	        }else if(msg[0].equals("POPUPMSG"))
	        {
	        	if(msg.length>1)
	        		PopupMsg(msg);
	        }else if(msg[0].equals("VOICEMSG"))
	        {
	        	if(msg.length>1)
	        		VoiceMsg(msg);
	        }else if(msg[0].equals("REFRESH"))
	        {
	        	
					refresh();
					refresh(msg);
				
	        }
	}
	
	public void VoiceMsg(String[] msg)
	{
		getSpeakEngin().say(msg[2]);
	}
	
	public void PopupMsg(String[] msg)
	{
		if(msg.length>2)
		  pushNotification(msg[1], msg[2]);
		else
		  pushNotification("", msg[1]);
		
	}
	
	public void pushMsg(String[] msg)
	{
		System.out.println("MSG"+msg[2]);
		si.pushMsg(msg[2]);
		PopupMsg(msg);
	}
	
	
	public void pushNotice(String[] msg)
	{
		System.out.println("NOTSG"+msg[2]);
		si.pushNotice(msg[2]);
	}
	
	public void pushOnlyMsg(String[] msg)
	{
		System.out.println("OLMSG"+msg[2]);
		si.pushOnlyMsg(msg[2]);
		PopupMsg(msg);
	}
	
	
	public void pushNotification(String Title,String msg)
	{
		tray.showTrayMsgDing(Title, msg);
	}
	
	public JPanel getEastPane()
	{
		JPanel pan = new JPanel();
		JButton sampleB = new JButton("Override JPanel getEastPane()");
		sampleB.setPreferredSize(new Dimension(300,100));
		pan.add(sampleB);
		//MainView.remove(eastpanel);
		//this.eastpanel=pan;
		//MainView.add(eastpanel, BorderLayout.EAST);
		
		return pan;
		
	}
	

	
	private void getLoginInfo()
	{
		SQLIP=getInstance().loginIP();
		
		if(ClientContext.getLoginPrincipal().contains("test"))
		{
			DBStr="test";
		}else
		{
			DBStr="default";
		}
		
		try
        {
		
          EmployeeCardVO empVO = HRServerActionManager.getInstance().getEmployeeSelf();
          if (empVO != null)
          {
        	
            logonUser = empVO;
            department= empVO.getJobId();
          }
        }
        catch (ServerActionException e)
        {
          ClientUtil.showException(e);
        }
		InetAddress addr=null;
        	try {
				 addr = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	if (addr != null)
        	{
        		clientIP=addr.getHostAddress();
        	}
			
	}
	
	

	private void setConnectingUrl()
	{
		bds.setUrl("jdbc:mysql://"+SQLIP+":3307/bb2_"+ DBStr);//数据库的路径，
	}
	
	private BB2Node createStudioSearchNode() {
		BB2Node node = new BB2Node();
		node.setName("Studio");
		node.putClientProperty("homeClass",
				studio.StudioSearchHomePage.class.getName());
		node.putClientProperty("nameKey", "Studio");
		node.setIcon("/bb/gui/images/submodule.png");
		return node;
	}

	private void setEastPane(JPanel eastpanel)
	{
		if(eastpanelHolder == null)
		{
			eastpanelHolder= new EastPaneHolder("");
			eastpanelHolder.ADD(eastpanel,"Center");
			MainView.add(eastpanelHolder, BorderLayout.EAST);
			System.out.println("Here");
		}else
		{
			eastpanelHolder.REMOVEALL();
			eastpanelHolder.ADD(eastpanel,"Center");
		}
		
		
			
	
	}
	private void readKYProperties()
	{
		File file = new File((new StringBuilder()).append(ClientConst.getUserHomeDir())
				.append(File.separatorChar).append("KY.properties")
				.toString());
		if (file.exists())
			try {
				FileInputStream is = new FileInputStream(file);
				KYProperties.load(is);
				is.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
	}
	public void initMissionControlAction()
	{
		
	}
	
	@Override
	public boolean isDisplayTree() {
		return true;
	}
	
	public boolean isDisplayTray() {
		return true;
	}
	
	public boolean isDisplayEastPane()
	{
		return true;
	}
	public boolean isDisplayNoticePane()
	{
		return true;
	}
	
	@Override
	public String getMainTreeXML()
	  {
	    return "/bb/gui/conf/MainTreeKY.xml";
	  }
	
	public FreeMenuBar getKYMenubar()
	{
		ActionListener defaultAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String action = e.getActionCommand();
				command(action);
			}

			
		};
		FreeMenuBar menubar = getFreeMenuBar();
		KYUIUtil.loadMenuBar(getMenuBarXML(), defaultAction,menubar);
		
        menubar.add(new FreeRootMenu("学生资源"));
        menubar.add(new FreeRootMenu("人力资源"));
        menubar.add(new FreeRootMenu("设置 "));
        menubar.add(new FreeRootMenu("在线帮助"));
        
        return menubar;
           
	}
   	
    @Override
	public String getTitle() {
    	String returnTxt="";
    	try {
			CompanyVO vo = CompanySettingServerActionManager.getInstance()
					.getCompanySettings();
			returnTxt += vo.getCompanyName();
		} catch (ServerActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	returnTxt += ("["+dateformat.format((new Date()))+"]");
    	
        return returnTxt;
    }
	
	
	@Override
	public String getMenuBarXML() {
		return "/KYUI/menubar.xml";
	}
	
	public  String loginIP()
	{
		
		return ClientContext.getServer();
		
	}
	
	
    public static KYMainUI getInstance()
    {
    	
    	return (KYMainUI) AbstractMainUI.getInstance();
    }
	
	public static void main(String[] args) {

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
    	Main.launchBizBox(KYMainUI.class.getName());
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

	
	@Override
	public List<MissionControlAction> getAction() {
		// TODO Auto-generated method stub
		return missionControlAction;
	}

	@Override
	public void addAction(MissionControlAction action) {
		if(missionControlAction.size()==0)
			missionControlAction.add(action);
		else
		{
			//missionControlAction.remove(0);
			missionControlAction.add(action);
		}
	}


	public void restore() {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void refresh(){
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void refresh(String[] msg) {
		// TODO Auto-generated method stub
		
	}
	



}
