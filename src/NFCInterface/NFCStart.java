package NFCInterface;


import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.apache.commons.dbcp2.BasicDataSource;

import KYUI.KYMainUI;
import KYUI.MissionControlAction;
import KYUI.SpeakEngin;
import bb.common.EmployeeCardVO;
import bb.gui.AbstractMainUI;
import bb.gui.ClientConst;
import bb.gui.CommonUI;
import bb.gui.Main;
import bb.gui.i18n.BB2Translator;
import bb.gui.main.MainUIActionManager;
import bb.gui.server.HRServerActionManager;
import bb.gui.swing.DetachUI;
import free.FreeMenuBar;
import free.FreeRootMenu;
import free.LoginUI;

public class NFCStart extends KYMainUI{
	
	private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private static final int INITIAL = 50;//初始化50个连接
    private static final int MAX_ACTIVE = 500;//最大值500个连接
    private static final int MAX_IDLE = 10;//最大空闲10
    private static final long MAX_WAIT = 5 * 1000;//超过500个访问，等待的时间
    ShowWindow SW;
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
    
    static SpeakEngin speakEngin ;
    static NFCMissionControl NMC=null;

	public NFCStart()
	{
		setConnectingUrl();
		initSpeakEngin();
		SW=ShowWindow.getinstence(this);
		DetachUI showUI= new DetachUI(SW);
		this.setExtendedState(Frame.ICONIFIED);
		setFullView(showUI);
		showUI.setVisible(true);
		
	//	showTab(ShowWindow.getinstence(this));
		
		setupMissionControl();
		getScrollInfoPane().startshow();  //开始字幕滚动 
		speakEngin.say("初始化完成");
		
		
		closeLoginUI();
		
		
	}
	
	
	
	private void setupMissionControl() {
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
				removeolddata();
				RegClient();
				
			}

			@Override
			public void FinishAction(String[] parms) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void TimeRepeatAction(String[] parms) {
				// TODO Auto-generated method stub
				//keepAlive();
				
			}

			@Override
			public void RepeatAction(String[] parms) {
				// TODO Auto-generated method stub
				processCardID(parms[0]);
				
			}
			
		};
		action.setname("NFCControl");
		this.addAction(action);
		NFCMissionControl NfcMC = new NFCMissionControl(this);
		NFCStart.NMC=NfcMC;
		NMC.SwitchTimeLimite("ON");
		Thread t = new Thread(NfcMC);
		t.start();
		
		NFCMissionControl2 NfcMC2 = new NFCMissionControl2(this);
		
		NfcMC2.SwitchTimeLimite("ON");
	
		
	}
	
	
	

	@Override
	public void DealMsg(String[] msg)
	{
		System.out.println("here\n"+"\n"+msg[1]+"\nend");
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
			
        }else if(msg[0].equals("CHANGETIMELIMIT"))
	        {
	        	stwichTimeLimite();
	        }
	}
	
	public void stwichTimeLimite()
	{
		if(NMC.isTimeLimite())
			   NMC.SwitchTimeLimite("OFF");
			else
			{
				 NMC.SwitchTimeLimite("ON");
			}
		System.out.println("TIME LIMIT state is:" +NMC.isTimeLimite());
	}
	
	

	

	@Override
	public void closing() {
		if (this.isCloseAble()) {
			int answer = CommonUI.showWarningConfirm(this,
					BB2Translator.getString("MainUI.CloseBizBox"), 0);
			if (answer == 0) {
				KYMainUI.FinalizeSpeakEngin();
				dispose();
			}
		}
	}


	public void resetall()
	{   
		NMC.reInitReader();
		
	}
	public void initSpeakEngin()
	{
		speakEngin= KYMainUI.initSpeakEngin("开元国际幼儿园欢迎你");
	}
	
	private void setFullView(JFrame frame)
	{
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		frame.setSize(w, h);
		frame.setLocation(0, 0);
		frame.setAlwaysOnTop(true);
		frame.setTitle("");
		frame.setType(Window.Type.UTILITY);
	}
	
	@Override
	public boolean isDisplayEastPane()
	{
		return false;
	}
	
	@Override
	public boolean isDisplayNoticePane()
	{
		return false;
	}
	
	
	@Override
	public boolean isDisplayTree() {
		return false;
	} 
	
	
	@Override
	public FreeMenuBar getKYMenubar()
	{
		
	JLabel s =new JLabel();
		FreeMenuBar menubar = getFreeMenuBar();
		 menubar.add( new FreeRootMenu("                              "));
        menubar.add(createExitMenu("退出"));
        menubar.add(createExtendMenu("功能"));
        return menubar;
	}
	
	
	private FreeRootMenu createExtendMenu(String text) {
	    FreeRootMenu menu = new FreeRootMenu(text);
	    JMenuItem item = new JMenuItem("最大化");
	    item.addActionListener(new ActionListener() {
	 
	        @Override
	       
	        public void actionPerformed(ActionEvent e) {
	        	ShowWindow ui=null;
	        	try{
	        		ui=ShowWindow.getinstence();
	        	}catch(Exception ex)
	        	{
	        		ui=ShowWindow.getinstence(KYMainUI.getInstance());
	        	}
	        	
	    		DetachUI showUI= new DetachUI(ui);
	    		KYMainUI.getInstance().setExtendedState(Frame.ICONIFIED);
	    		setFullView(showUI);
	    		showUI.setVisible(true);
	        }
	    });
	    menu.add(item);
	    JMenuItem item1 = new JMenuItem("重置");
	    item.addActionListener(new ActionListener() {
	 
	        @Override
	       
	        public void actionPerformed(ActionEvent e) {
	        	resetall();
	        }
	    });
	    //menu.add(item1);
	    return menu;
	}
	
	
	private FreeRootMenu createExitMenu(String text) {
	    FreeRootMenu menu = new FreeRootMenu(text);
	    JMenuItem item = new JMenuItem("退出系统");
	    item.addActionListener(new ActionListener() {
	 
	        @Override
	       
	        public void actionPerformed(ActionEvent e) {
	        	 closing();
	        	 try {
					bds.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            if (AbstractMainUI.getInstance() != null) 
	            {
					if (e.getSource() == AbstractMainUI.getInstance())
						MainUIActionManager.exitClient();
					else
						MainUIActionManager.closeMainUI();
	            }
	            System.exit(0);
	            
	        }
	    });
	    menu.add(item);
	    return menu;
	}

	private void setConnectingUrl()
	{
		bds.setUrl("jdbc:mysql://"+KYMainUI.SQLIP+":3307/bb2_"+KYMainUI.DBStr);//数据库的路径，
		try { 
        	Connection conn = bds.getConnection();
        	System.out.println(conn);
        	
        	if(!conn.isClosed()) 
 
        	{
        		System.out.println("Connection to "+SQLIP+ "is sucessful" );
        	}
        	conn.close();
		}
        	
		catch(Exception e)
          {
           	 e.printStackTrace();
           	 System.out.println("Connection to "+SQLIP+ "failed" );
           	 SQLIP=JOptionPane.showInputDialog("Please enter Sever IP address");
           	 setConnectingUrl();
          }
          
	}
	

	public void keepAlive()
	{
		Connection conn = null;
			       try {
			    	   conn=bds.getConnection();
			    	   System.out.println("["+new java.sql.Time(System.currentTimeMillis())+"] : Keep Alive conn "+conn);
			    	   PreparedStatement pp=  conn.prepareStatement("select 1;");
			    	    pp.execute();
			    	   conn.close();
			       }catch(Exception e)
			       {
			    	   
			    	   System.out.println("["+new java.sql.Time(System.currentTimeMillis())+"] : Connection reseted!");
			    	   e.printStackTrace();
			    	  
			       }

			
	}
	
	
	
	public void removeolddata()
	{
		

        try { 
         Connection conn =bds.getConnection();

         if(conn.isClosed()) 
         {        
        	 System.out.println("Succeeded connecting to the Database!");
         }
         
         java.sql.Date crdt = new java.sql.Date(System.currentTimeMillis());
         
         System.out.println("------+" + crdt);
         //crdt.setDate(crdt.getDate()-3);
         System.out.println(crdt);
         
         PreparedStatement pstmt = null;
         pstmt = conn.prepareStatement("select * from emp_logginrecord where L_date<>?;"); 
         pstmt.setDate(1, crdt);
         ResultSet rs = pstmt.executeQuery();
         if(!rs.next())
         {
        	 System.out.println("Sucesscful removed Old data.");
         }
         else
         {
        	 pstmt = null;
             pstmt = conn.prepareStatement("delete from emp_logginrecord where L_Date<>?;"); 
             pstmt.setDate(1, crdt);
             pstmt.execute();
         }
       
         rs.close();
         pstmt.close();
         conn.close();
        }
         catch(Exception e)
         {
        	 e.printStackTrace();        	 
         }
         
	}
	
	
	private void processCardID(String cardID)
	{
		
		if(cardID.trim().equals(""))
		{
			try {
				ShowWindow.getinstence().showBlank();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(cardID.contains("CARDERROR"))
		{
			try {
				ShowWindow.getinstence().showCardErr();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(cardID.contains("TIMELIMIT"))
		{
			try {
				ShowWindow.getinstence().showTimeLimitMsg();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(cardID.contains("WAITNEXT"))
		{
			try {
				ShowWindow.getinstence().showBlank();;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else
		{
			EmployeeCardVO Emp = searchEmpVObyCard(cardID);
			System.out.println("EMPEMP---------------"+Emp);
			if(Emp==null)
			{
				//Card not reg Exception
				try {
					ShowWindow.getinstence().showCardNoReg();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(!ManageID(Emp))
			{
				
				System.out.println(Emp);
				try {
					ShowWindow.getinstence().showEmp(Emp);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
				String status=CallDBback(Emp);
				System.out.println("Call back "+ Emp.getId()+ "finished");
				VoiceOutput(Emp,status);
				//TextOutput(Emp,status);
				notifyClsClient(Emp);
				
			}else
			{
				//管理ID处理分支
			}
		}
	}
	
	
	private void notifyClsClient(final EmployeeCardVO Emp)
	{
		Thread t= new Thread(){
		int SERVERPORT=990;
		String ClientIP= getClientIP(Emp);
		
		@Override
		public void run(){
			if(ClientIP.contains("0.0.0.0"))
				return;
			System.out.println("this ip---------------->"+ClientIP);
			 try {
	             // 建立连接套接字。
	             Socket s = new Socket(KYMainUI.SQLIP,SERVERPORT);
	             System.out.println("socket = " + s);
	             
	             // 新建网络连接的输入流。
	             BufferedReader in = new BufferedReader(new InputStreamReader(s
	                     .getInputStream()));
	             
	             // 新建网络连接的自动刷新的输出流。
	             PrintWriter out = new PrintWriter(new BufferedWriter(
	                     new OutputStreamWriter(s.getOutputStream())),true);
	             
	             // 先使用System.in构造InputStreamReader，再构造BufferedReader。
	             BufferedReader stdin = new BufferedReader(
	                     new InputStreamReader(System.in));
	             
	             System.out.println("Enter a string， Enter BYE to exit! ");
	             
	             
	                 // 读取从控制台输入的字符串，并向网络连接输出，即向服务器端发送数据。
	             		
	             	out.println(ClientIP);
	             	String str = in.readLine();
	                out.println("REFRESH");
	                str = in.readLine();
	                out.println(Emp.getCompanyAddressBookName());
	                str = in.readLine();
	                out.println("EOT");
	            
	             s.close();
	         } catch (IOException e) {
	             System.err.println("IOException" + e.getMessage());
	         }
		 }
		};
		t.start();
	}
	
	private String getClientIP(EmployeeCardVO Emp)
	{
		String ClientIP="0.0.0.0";
		java.sql.Date date=new java.sql.Date(System.currentTimeMillis());
		java.sql.Time time=new java.sql.Time(System.currentTimeMillis());
		try 
		{
			
			Connection conn=bds.getConnection();
			PreparedStatement pstmt =null;
	        ResultSet rs=null;
	        
	        pstmt=conn.prepareStatement("SELECT * FROM ky_clientregiest WHERE ClientID=?;");
	       	pstmt.setString(1, Emp.getDepartment());
	       	rs=  pstmt.executeQuery();
	       	while(rs.next())
	       	{
	       		 ClientIP= rs.getString("ClientIP");
	       		 date=rs.getDate("Reg_Date");
	       		 time=rs.getTime("Reg_Time");
	       	}
	       	rs.close();
	       	pstmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(date+"  "+time);
		
		java.sql.Date Nowdate=new java.sql.Date(System.currentTimeMillis());
		java.sql.Time Nowtime=new java.sql.Time(System.currentTimeMillis());
		System.out.println(Nowdate+"  "+Nowtime+"   "+(time.getTime()-Nowtime.getTime()));
	//	if((-time.getTime()+Nowtime.getTime())<=1000*60*60)
		{
			System.out.println("Client "+Emp.getDepartment()+ClientIP+" found.");
			return ClientIP;
		}
		
	//	System.out.println("Client for "+Emp.getDepartment()+" not found.");
	//	return "0.0.0.0";
		
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
	
	
	private String CallDBback(EmployeeCardVO emp)
	{
        java.sql.Time tt =new java.sql.Time(System.currentTimeMillis());
        java.sql.Date dd = new java.sql.Date(System.currentTimeMillis());
        int timeint=Integer.parseInt(tt.toString().replace(":", "")); 
        
        String statusDtr ="";
        
        Connection conn;
		try {
			conn = bds.getConnection();
			
	        PreparedStatement pstmt =null;
	        ResultSet rs=null;
	        if(emp.getSecurityLevel().contains("LEVEL 5")) //孩子的
	        {
	        	if(timeint<80001)
	        		statusDtr= "0";         
	        	else if(timeint>80000 && timeint < 163000 )
	        		statusDtr= "3";
	        	else if(timeint>153000 && timeint < 174000 )
	        		statusDtr= "1";
	        	else if(timeint > 173000 )
	        		statusDtr= "4";
	        	else
	        		statusDtr= "-1";
	        
	        
	        
	        	pstmt=conn.prepareStatement("select * from emp_logginRecord where id=?;");
	        	pstmt.setString(1, emp.getId());
	        	//pstmt.setString(2, statusDtr);
	        	rs=  pstmt.executeQuery();
	        	boolean updateStatus=true;
	        	if(rs.next())
	        	{
	        		rs.last();
	        		if(rs.getString("L_status").contains(statusDtr))
	        		{
	        			if(timeint-Integer.parseInt(rs.getString("L_time").replace(":", ""))<3000)
	        					{
	        						updateStatus=false;
	        					}
	        		}
	        			
	        	}
	            
	        	if(updateStatus )
	        	{
	        
	       		 pstmt = conn.prepareStatement("insert into emp_logginRecord (id,L_name, L_time, L_date, L_status,department) values (?,?,?,?,?,?)");
	       		 pstmt.setString(1, emp.getId());
	       		 pstmt.setString(2, emp.getCompanyAddressBookName());
	       		 pstmt.setTime(3, tt);
	       		 pstmt.setDate(4, dd);
	       		 pstmt.setString(6, emp.getDepartment());
	       		 pstmt.setString(5, statusDtr);   
	       		 System.out.println(pstmt);
	        
	        
	       		 pstmt.execute();
	        
	       		 pstmt = conn.prepareStatement("insert into emp_logginRecordall (id,L_name, L_time, L_date, L_status,department) values (?,?,?,?,?,?)");
	       		 pstmt.setString(1, emp.getId());
	       		 pstmt.setString(2, emp.getCompanyAddressBookName());
	       		 pstmt.setTime(3, tt);
	       		 pstmt.setDate(4, dd);
	       		 pstmt.setString(6, emp.getDepartment());
	       		 pstmt.setString(5, statusDtr);  
	        
	        
	       		 pstmt.execute();
	        	}
	        }
	        else//老师的
	        {
	       	 	pstmt=conn.prepareStatement("select * from emp_logginRecord where id=?;");
	       	 	pstmt.setString(1, emp.getId());
	       	 	rs=  pstmt.executeQuery();
	       	 	rs.last();
	       	 	int rr= rs.getRow();
	       	 	if(rr!=0)
	       	 		statusDtr = String.valueOf(Integer.parseInt(rs.getString("L_status"))+1);
	       	 	else
	       	 		statusDtr="0";
	       	 	pstmt = conn.prepareStatement("insert into emp_logginRecord (id,L_name, L_time, L_date, L_status,department) values (?,?,?,?,?,?)");
	       	 	pstmt.setString(1, emp.getId());
	       	 	pstmt.setString(2, emp.getCompanyAddressBookName());
	       	 	pstmt.setTime(3, tt);
	       	 	pstmt.setDate(4, dd);
	       	 	pstmt.setString(6, emp.getDepartment());
	   		 	pstmt.setString(5, statusDtr);   
	    
	   		 	pstmt.execute();
	    
	   		 	
	   		 	pstmt = conn.prepareStatement("insert into emp_logginRecordall (id,L_name, L_time, L_date, L_status,department) values (?,?,?,?,?,?)");
	    	 	pstmt.setString(1, emp.getId());
	    	 	pstmt.setString(2, emp.getCompanyAddressBookName());
	    	 	pstmt.setTime(3, tt);
	    	 	pstmt.setDate(4, dd);
	    	 	pstmt.setString(6, emp.getDepartment());
			 	pstmt.setString(5, statusDtr);  
	    
			 	pstmt.execute();

	        }
	        
		 	rs.close();
		 	pstmt.close();
		 	conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return statusDtr;
	}
	
	
	
	private void TextOutput(EmployeeCardVO emp,String status) {
		if(emp.getSecurityLevel().contains("LEVEL 5") || emp.getSecurityLevel().contains("LEVEL 4"))
  		{
  		
  			System.out.println(""+emp.getCompanyAddressBookName()+"小朋友");
  		}
  		
  		else
  		{
  			if(Integer.parseInt(status)%2==1)
  				System.out.println("Miss "+emp.getCompanyAddressBookName().charAt(0)+",再见");
  			else
  				System.out.println("欢迎你， Miss "+emp.getCompanyAddressBookName().charAt(0));
  		}
	}
	private void VoiceOutput(EmployeeCardVO emp,String status) {
		// TODO Auto-generated method stub
  		if(emp.getSecurityLevel().contains("LEVEL 5") || emp.getSecurityLevel().contains("LEVEL 4"))
  		{
  			NFCStart.speakEngin.say(emp.getCompanyAddressBookName()+"小朋友");
  		
  		}
  		
  		else
  		{
  			if(Integer.parseInt(status)%2==1)
  				NFCStart.speakEngin.say("Miss "+emp.getCompanyAddressBookName().charAt(0)+",再见");
  			else
  				NFCStart.speakEngin.say("欢迎你， Miss "+emp.getCompanyAddressBookName().charAt(0));
  		}
  		
	}

	private EmployeeCardVO searchEmpVObyCard(String cardID)
	{
		Hashtable<String,String> ret = new Hashtable<String,String> ();
		System.out.println("Connecting");
		String id="",department="",name="",manager="",Holder_id="",level="",position="";
        try { 
      
        	Connection conn =bds.getConnection();
        	System.out.println("Connection Get。");
        	PreparedStatement pstmt = null;
        	pstmt = conn.prepareStatement("select id_NFC,emp_id.id, Holder_id,department,name_company_address_book,security_level,manager,position from emp_nfcid right join emp_id on emp_nfcid.id=emp_id.id where id_NFC=?;"); 
        	pstmt.setString(1, cardID);  	  
       
        	System.out.println(pstmt);
        	ResultSet rs = pstmt.executeQuery();
        	if(rs.next())
        	{
        		id = rs.getString("id");
        		department = rs.getString("department");
        		name= rs.getString("name_company_address_book");
        		level=rs.getString("security_level");
        		manager=rs.getString("manager");
        		position=rs.getString("position");
        		try{
        			Holder_id=rs.getString("Holder_id");
        		}catch(Exception e){}
        		
        		System.out.println("------"+id);
        	       	 
        	}
        	else
        	{
        		System.out.println("The Card is not Registered ");
        		//ShowWindow.getinstence().showCardNoReg();
            	rs.close();
            	pstmt.close();
            	conn.close();
        		return null;
        	} 
        	
        	rs.close();
        	pstmt.close();
        	conn.close();
           
            Collection<EmployeeCardVO> empVOs = HRServerActionManager.getInstance().findEmployeeCardsByEmployeeId(id, false, 0, 100);
              if (empVOs != null)
              {
            	  System.out.println(empVOs.size());
            	  for(EmployeeCardVO empVO:empVOs)
            	  {
                	System.out.println(empVO.getCompanyAddressBookName());
                	return empVO;
            	  }
              }else
              {
            	  
            	  System.out.println("[ERROR] NULL EMPSet found.");
            	  EmployeeCardVO empVO= new EmployeeCardVO();
            	  empVO.setId(id);
            	  empVO.setCompanyAddressBookName(name);
            	  empVO.setDepartment(department);
            	  empVO.setSecurityLevel(level);
            	  empVO.setManager(manager);
            	  empVO.setPosition(position);
            	  return empVO;
              }
              
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
            
        return null;
		
	}
	
	
	private boolean ManageID(EmployeeCardVO cardID)
	{
		return false;
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
    	Main.launchBizBox(NFCStart.class.getName());
    	
	
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
