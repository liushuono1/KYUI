package Client4CLass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;







import KYUI.KYMainUI;
import KYUI.KYRefreshable;
import bb.common.EmployeeCardVO;
import bb.gui.ServerActionException;
import bb.gui.base.ClientUI;
import bb.gui.server.HRServerActionManager;

public class RollCallUI extends ClientUI implements KYRefreshable{
	
	static RollCallUI instence = null;
	
	
	static String  SQLIP = KYMainUI.SQLIP,
		   classType=KYMainUI.department;
	static int classMax=Integer.parseInt(KYMainUI.KYProperties.getProperty("classMax"));
	static boolean AfterCareTime=false;		
	static //String audiofile ="189.wav";
	java.sql.Time checkTime = new java.sql.Time(System.currentTimeMillis());
	{
		checkTime.setHours(0);
		checkTime.setMinutes(0);
		checkTime.setSeconds(0);
	}
	
	public JPanel northPanel;
	Collection<EmployeeCardVO> Student_present;
	Collection<EmployeeCardVO> Teachers;
	public int actualTotal;
	public int shouldTotal;

	public Hashtable<String, JButton> name_button;
	public Hashtable<String, JButton> id_button;
	List<JButton> buttonArray;// = new ArrayList<JButton>();
	public List<String> id_list;
	public List<String> loginList;
	public List<String> nameList;
	public int row,column;
	public JTextField field;
	public JPanel centerPanel;
	public JPanel buttonPanel;
	public JPanel labelPanel;
	public Hashtable<String, String> id_name;
	public Hashtable<String, String> id_status;
	public Hashtable<String, String> id_comment;
	public JPanel southPanel;
	public Font F1,F2;
	public JButton quitButton ;
	public boolean clickStart = false;
	public boolean timesup = true;
	public List<String> checkedList;
	public static List<String> morningCheckedList;
	public static List<String> afternoonChecedList;
	public static List<String> teacherCheckedList;
	public boolean stopChecking;
	public JLabel label;
	public List<String> timeList;
	
	public Hashtable<String, String> id_time ;
	public String time;
	public JTextArea output;
	
	
    

	
	RollCallUI()
	{	
		instence = this;
		this.Student_present=getStudentPresent();
		this.Teachers=this.getTeachers();
		this.shouldTotal=Student_present.size();
		name_button = new Hashtable<String, JButton>();
		id_list =new LinkedList<String>();
		id_name = new Hashtable<String, String>();
		id_button = new Hashtable<String, JButton>();
		id_status = new Hashtable<String, String>();
		id_comment= new Hashtable<String, String>();
		id_time = new Hashtable<String, String>();
		buttonArray = new ArrayList<JButton>();
		F1 = new Font("SimHei",0,40);
		F2 = new Font("SimHei",0,30);
		//JPanel northPanel = new JPanel();
		southPanel = new JPanel();
		//northPanel.setLayout(new GridLayout(1,3));
	
		centerPanel = new JPanel();
		buttonPanel = new JPanel();
		labelPanel = new JPanel();
	
	
		checkedList = new LinkedList<String>();
		morningCheckedList = new LinkedList<String>();
		afternoonChecedList = new LinkedList<String>();
		teacherCheckedList = new LinkedList<String>();
		setRowColumn(this.Student_present.size());
	
		
		//System.out.println(KYMainUI.KYProperties);
		
		initSwing();
		
		refresh();
		
		
	}
	

	
	
	
	public static Collection<EmployeeCardVO> getStudentPresent() {
		// TODO Auto-generated method stub
		Collection<EmployeeCardVO> temp=null,ret=null;
		if(!AfterCareTime){
			try {
				temp= HRServerActionManager.getInstance().findEmployeeCardsByDepartment(RollCallUI.classType, false, 0, RollCallUI.classMax);
				ret= HRServerActionManager.getInstance().findEmployeeCardsByDepartment(RollCallUI.classType, false, 0, RollCallUI.classMax);
				ret.removeAll(temp);
			} catch (ServerActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for(EmployeeCardVO empvo:temp)
			{
				
				System.out.println(empvo.getId());
				if(empvo.getSecurityLevel().equals("LEVEL 5"))
				{
					ret.add(empvo);
				}
			}
		}else
		{
			try {
				temp= HRServerActionManager.getInstance().findEmployeeCardsByDepartment("", false, 0, RollCallUI.classMax*10);
				ret= HRServerActionManager.getInstance().findEmployeeCardsByDepartment("", false, 0, RollCallUI.classMax*10);
				ret.removeAll(temp);
			} catch (ServerActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<String> ID_list = new LinkedList<String>();
			try
			{
				Connection conn = KYClassUI.bds.getConnection();
				PreparedStatement pstmt=null;
			
				pstmt = conn.prepareStatement("SELECT * from emp_logginRecord where L_Time > ? and id not in (select distinct(id) from emp_logginRecord where L_status='2');");
				
			    pstmt.setTime(1, checkTime);
			
			    ResultSet rs = pstmt.executeQuery();
			    while(rs.next())
			    {	
			    	ID_list.add(rs.getString("id"));
		    	
			    }

			rs.close();
			pstmt.close();
			conn.close();
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
			for(EmployeeCardVO empvo:temp)
			{
				if(empvo.getSecurityLevel().equals("LEVEL 5") && ID_list.contains(empvo.getId()))
				{
					System.out.println(empvo.getCompanyAddressBookName());
					ret.add(empvo);
				}
			}
		}
		return ret;
	}
	
	
	private Collection<EmployeeCardVO> getTeachers() {
		// TODO Auto-generated method stub
		Collection<EmployeeCardVO> temp=null,ret=null;
		try {
			EmployeeCardVO user =HRServerActionManager.getInstance().getEmployeeSelf();
			temp= HRServerActionManager.getInstance().findEmployeeCardsByDepartment(user.getDepartment(), false, 0, RollCallUI.classMax);
			ret= HRServerActionManager.getInstance().findEmployeeCardsByDepartment(user.getDepartment(), false, 0, RollCallUI.classMax);
			ret.removeAll(temp);
		} catch (ServerActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(EmployeeCardVO empvo:temp)
		{
			if(empvo.getJobId().equals(KYMainUI.department) && empvo.getSecurityLevel().equals("LEVEL 3"))
			{
				ret.add(empvo);
			}
		}
		return ret;
		
	}
	
	
	public void setOutput()
	{
		//JTextArea output = new JTextArea("");	?
		output.setFont(F1);
		if(shouldTotal == actualTotal)
		{
			output.setText("应到"+String.valueOf(shouldTotal)+"人, 实到"+String.valueOf(actualTotal)+"人");
		}
		else
		{
			output.setText("应到"+String.valueOf(shouldTotal)+"人, 实到"+String.valueOf(actualTotal)+"人");
		}
	}
	
	

	@Override
	public String getTitle()
	{
		return classType+"学生名单";
		
	}
	
	private void setRowColumn(int max) {
		if(max==0)
		{
			this.column=0;
			this.row=0;
			return;
		}
		
		if(max<20)
		{
			this.column=5;
			this.row=max/5 +1;
			return;
		}
		for(int i=5;i<10;i++)
		{
			if(i*i>max)
			{
				this.row=i;
				this.column=i;
				return;
			}
		}
		JOptionPane.showMessageDialog(null, "班级人数超过了设定上限100人，程序退出！！");
		System.exit(0);
		
	}

   public void AfterCare()
   {
	   AfterCareTime=true;
	   this.refreshALL();
   }

	private void initSwing()
	{
		if(row*column==0)
		{
			
			this.add(BorderLayout.CENTER, centerPanel);
			this.output = new JTextArea();
			output.setBackground(Color.PINK);
			southPanel.add(output, 0);
			this.add(BorderLayout.SOUTH, southPanel);
			return;
		}
	
		
		buttonArray.clear();
		id_list.clear();
		id_button.clear();
		id_status.clear();
		id_time.clear();
		buttonPanel.removeAll();
		
		buttonPanel.setLayout(new GridLayout(row, column));
		for(int i = 0; i < row*this.column; i++)
		{
			JButton btn = new JButton();
			btn.setBackground(Color.LIGHT_GRAY);
			btn.setFont(new Font("SimHei",0,40));
			//btn.setText(nameList.get(i));
			btn.setForeground(Color.WHITE);
			btn.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					
					JButton btn = (JButton)arg0.getSource();
					//System.out.println(id_list.get(buttonArray.indexOf(btn)));
					
					ButtonPressed(btn,id_status.get(id_list.get(buttonArray.indexOf(btn))));
					
				
				}


			});
			buttonArray.add(btn);
			buttonPanel.add(buttonArray.get(i));
		}
		int btncount=0;
		for(EmployeeCardVO empvo:this.Student_present)
		{
			buttonArray.get(btncount).setText(empvo.getCompanyAddressBookName());
			id_list.add(empvo.getId());
			id_button.put(empvo.getId(), buttonArray.get(btncount++));
			id_status.put(empvo.getId(), "");
			id_time.put(empvo.getId(), "");
		}
	
		centerPanel.setLayout(new BorderLayout());

		centerPanel.add(BorderLayout.CENTER, buttonPanel);//add the Buttpn panel
		
		double width = buttonPanel.getSize().width;
		double height = buttonPanel.getSize().height;
		JLabel label_one =new JLabel("缺勤追踪:");
		labelPanel.add(label_one);
		//yesterday();

		
		Dimension d = new Dimension();
		d.setSize(width/3, height);
		labelPanel.setMaximumSize(new Dimension(200,600));
		labelPanel.setSize(d);
		//centerPanel.add(BorderLayout.EAST, labelPanel);//add the Label pabel
		
		
		this.add(BorderLayout.CENTER, centerPanel);
		this.output = new JTextArea();
		output.setBackground(Color.PINK);
		southPanel.add(output, 0);
		this.add(BorderLayout.SOUTH, southPanel);
		
		
		
		//this.finish();
	}
	
	public Connection connect() throws SQLException 
	{
		Connection conn = null;
		
		conn=KYClassUI.bds.getConnection();
        if(!conn.isClosed()) 
        	System.out.println("Succeeded connecting to the Database!"+conn);
        System.out.println("Active("+KYClassUI.bds.getNumActive()+") Idel("+KYClassUI.bds.getNumIdle()+")");
        return conn;
	}
	
	public void refreshALL()
	{
		this.Student_present=getStudentPresent();
		setRowColumn(this.Student_present.size());
		initSwing();
		refresh();
		
	}
	
	@Override
	public void refresh()
	{
		getStatusDatebase();
		setButtonStatus();
		setOutput();
	}
	
	
	private void getStatusDatebase() {
		// TODO Auto-generated method stub
		PreparedStatement pstmt;
		ResultSet rs;
		pstmt = null;
		
		try {
			Connection conn = connect();
		
			if(!AfterCareTime)
			{
				pstmt = conn.prepareStatement("SELECT * from emp_logginRecord join emp_id on emp_id.id=emp_logginRecord.id where emp_id.department=? and L_Time > ?;");
			    pstmt.setString(1, classType);
			    pstmt.setTime(2, checkTime);
			}else
			{
				pstmt = conn.prepareStatement("SELECT * from emp_logginRecord;");

			}
		    rs = pstmt.executeQuery();
		    while(rs.next())
		    {	
		    	String ID=rs.getString("id");
		    	id_status.put(ID, rs.getString("L_status"));
		    	id_time.put(ID, rs.getTime("L_time").toString());
		    	//System.out.println(ID+  "  "+  rs.getString("L_status")+"  "+rs.getTime("L_time").toString() );
		    }

			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	public void setButtonStatus()
	{
		this.actualTotal=0;
		for(String id: id_status.keySet())
		{
			String status = id_status.get(id);
			if(status != "")
			{
			char s = status.charAt(0);
   	 		Color c = null;
   	 		switch(s)
   	 		{
	 			case '0': 
	 			{
	 				c = Color.GREEN;
	 				actualTotal++;
	 				break;
	 			
	 			}
	 			case '1': 
	 			{
	 				c = Color.BLUE;
	 				actualTotal++;
	 				break;
	 			}
	 			case '2': 
	 				c = Color.LIGHT_GRAY;
	 				break;
	 			case '3':
	 			{
	 				c = Color.ORANGE;
	 				actualTotal++;
	 				break;
	 				}
	 			case '4':
	 			{
	 				c = Color.ORANGE;
	 				actualTotal++;
	 				break;
	 				}
	 			default:
	 				break;
	 		}
   	 	
   	 		JButton nameBtn =id_button.get(id);
   	 		System.out.println(id+"   "+ nameBtn);
   	 		if(nameBtn!=null)
   	 		{
   	 			String name = nameBtn.getText();
   	 			nameBtn.setBackground(c);//change color
   	 			time = id_time.get(id);//get the corresponding time
   	 			nameBtn.setText("<html><body>"+name+"<br>"+time+"<body></html>");
   	 		}
   	 		//System.out.println(name + "   "+ status);
			}
		}
	 		
	}
	public void checkAttendence() throws SQLException
	{
	
		for(int i=0; i<loginList.size(); i++)
        {
       	 	String login_id = loginList.get(i);
       	 	String login_name = id_name.get(login_id);
       	 	if(id_list.contains(login_id))// && !morningCheckedList.contains(login_id))
       	 	{
       	 		String status = id_status.get(login_id);
       	 		char s = status.charAt(0);
       	 		Color c = null;
       	 		switch(s)
       	 		{
       	 			case '0': 
       	 				c = Color.GREEN;
       	 				if(!morningCheckedList.contains(login_id))
       	 				{
       	 					morningCheckedList.add(login_id);
       	 				}
       	 				break;
       	 			case '1': 
       	 				if(!afternoonChecedList.contains(login_id))
       	 				{
       	 					afternoonChecedList.add(login_id);
       	 				}
       	 				c = Color.BLUE;
       	 				break;
       	 			case '2': 
       	 				c = Color.LIGHT_GRAY;
       	 				if(!teacherCheckedList.contains(login_id))
       	 				{
       	 					teacherCheckedList.add(login_id);
       	 				}
       	 				break;
       	 			case '3':
       	 				c = Color.ORANGE;
       	 				break;
       	 			case '4':
       	 				c = Color.ORANGE;
       	 				break;
       	 			default:
       	 				break;
       	 		}
       	 		name_button.get(login_name).setBackground(c);//change color
       	 		List<String> idStatus = new LinkedList<String>();
				idStatus.add(login_id);//add id
				idStatus.add(status);//add status
				time = this.id_time.get(login_id);//get the corresponding time
				name_button.get(login_name).setText("<html><body>"+login_name+"<br>"+time+"<body></html>");
       	 	}
        }
	}
	
	
	public void ButtonPressed(JButton btn, String status)
	{
		if(!AfterCareTime)
		{
			if(status.equals("1"))
			{
				this.namePressed(btn);
			}
			else 
			{
				ButtonMenu(btn);
			}
		}
		else
		{
			this.namePressed(btn);
		}
		
		
	}
	
	public void namePressed(JButton btn)
	{
		java.sql.Time time = new java.sql.Time(System.currentTimeMillis());
		java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
		btn.setBackground(Color.LIGHT_GRAY);
		EmployeeCardVO[] Students = this.Student_present.toArray(new EmployeeCardVO[1]);
		String login_name=Students[buttonArray.indexOf(btn)].getCompanyAddressBookName();
		String login_id=Students[buttonArray.indexOf(btn)].getId();
		String classType =Students[buttonArray.indexOf(btn)].getDepartment();
		//System.out.println(login_name +"  "+login_id  );
		
		btn.setText("<html><body>"+login_name+"<br>"+time+"<body></html>");
		
		this.id_status.put(login_id, "2");
		try 
		{
			Connection conn = connect();
			PreparedStatement pstmt =null;
			
	        pstmt = conn.prepareStatement("insert into emp_logginRecord (id,L_name, L_time, L_date, L_status,department) values (?,?,?,?,?,?)");
	        pstmt.setString(1, login_id);
	        pstmt.setString(2, login_name);
	        pstmt.setTime(3, time);
	        pstmt.setDate(4, date);

	        pstmt.setString(6, classType);
	       	pstmt.setString(5, "2");
	        pstmt.execute();
	        pstmt = conn.prepareStatement("insert into emp_logginRecordall (id,L_name, L_time, L_date, L_status,department) values (?,?,?,?,?,?)");
	        pstmt.setString(1, login_id);
	        pstmt.setString(2, login_name);
	        pstmt.setTime(3, time);
	        pstmt.setDate(4, date);
	        pstmt.setString(6, classType);
	       
	       	pstmt.setString(5, "2");
			pstmt.execute();
			pstmt.close();
			
			
			pstmt.close();
			conn.close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void ButtonMenu(JButton btn)
	{
		EmployeeCardVO[] Students = this.Student_present.toArray(new EmployeeCardVO[1]);
		String login_name=Students[buttonArray.indexOf(btn)].getCompanyAddressBookName();
		String login_id=Students[buttonArray.indexOf(btn)].getId();
		Point localtion =MouseInfo.getPointerInfo().getLocation();

		
		if(allCheck.getInstence()!=null)
		{
			allCheck.close();
		}
		allCheck checkFrame = new allCheck(Students[buttonArray.indexOf(btn)],Teachers);
		checkFrame.setLocation(localtion);
		//checkFrame.f.setAlwaysOnTop(true);

	}
	

	public static RollCallUI getinstence()
	{
		if(instence == null)
		{
			 new RollCallUI();
		} 
		
		return instence;
		
	}

	@Override
	public void refresh(String[] msg) {
		// TODO Auto-generated method stub
		
	}
	
	

}
