package kyHRUI.Student;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import KYUI.KYMainUI;


public class rollCall extends JFrame implements Runnable{
	public JPanel wholePanel;
	//public JFrame frame; 
	public JPanel northPanel;
	public static int actualTotal;
	public int shouldTotal;
	public static Hashtable<String, JButton> name_button = new Hashtable<String, JButton>();;
	List<JButton> buttonArray = new ArrayList<JButton>();
	public static List<String> id_list;
	public static List<String> loginList;
	public List<String> nameList;
	public int row,column;
	//public String classType;
	public JTextField field;
	public JPanel centerPanel;
	public JPanel buttonPanel;
	public JPanel labelPanel;
	public static Hashtable<String, String> id_name;
	public static Hashtable<String, String> id_status;
	public JPanel southPanel;
	public Font x;
	public JButton quitButton ;
	public boolean clickStart = false;
	public boolean timesup = true;
	public List<String> checkedList;
	public static List<String> morningCheckedList;
	public static List<String> afternoonChecedList;
	public static List<String> teacherCheckedList;
	public boolean stopChecking;
	public JLabel label;
	public static List<String> timeList;
	String SQLIP = "192.168.1.100";
	public static Hashtable<String, String> id_time ;
	public static String time;
	public JTextArea output;
	public Font f ;
	JScrollPane ScrollBtnPanel; 
	public JButton refreshBtn;
	
	public rollCall() throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{

	
		id_name = new Hashtable<String, String>();
		id_status = new Hashtable<String, String>();
		id_time = new Hashtable<String, String>();
	
		x = new Font("SimHei",0,30);
		f = new Font("SimHei",0,20);
		this.row = 30;     // 以后改为自适应尺寸
		this.column = 6;

		southPanel = new JPanel();

      	centerPanel = new JPanel();
		buttonPanel = new JPanel();
		labelPanel = new JPanel();
		
		ScrollBtnPanel = new JScrollPane(buttonPanel);
		ScrollBtnPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		ScrollBtnPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		checkedList = new LinkedList<String>();
		morningCheckedList = new LinkedList<String>();
		afternoonChecedList = new LinkedList<String>();
		teacherCheckedList = new LinkedList<String>();
		//setRowColumn();//1
		setFrame();//2
		readData();
		
	}
	
	
	public void finish()
	{
		setOutput();//5
		//this.frame.setVisible(true);
	}
	
	public void setOutput()
	{
		//JTextArea output = new JTextArea("");	?
		output.setFont(x);
		if(shouldTotal == actualTotal)
		{
			output.setText("应到"+String.valueOf(shouldTotal)+"人, 实到"+String.valueOf(actualTotal)+"人");
		}
		else
		{
			output.setText("应到"+String.valueOf(shouldTotal)+"人, 实到"+String.valueOf(actualTotal)+"人");
		}
	}
	
	public void setFrame() throws ClassNotFoundException, SQLException
	{
		PreparedStatement pstmt;
		ResultSet rs;
        Connection conn=null;
		try {
			conn= connect();
			pstmt = null;
			pstmt = conn.prepareStatement("select * from emp_id where security_level = 'level 5'");
			/*
			if(classType.equals("大班") || classType.equals("中班"))
				pstmt = conn.prepareStatement("SELECT * from emp_id where department = '大班' OR department = '中班';");
			else
				pstmt = conn.prepareStatement("SELECT * from emp_id where department = "+"'"+this.classType+"';");
			 */
         	rs = pstmt.executeQuery();
            int count = 0;
            id_list = new LinkedList<String>();// the total number of students
            nameList = new LinkedList<String>();
            //statusList = new LinkedList<String>();
            while(rs.next())
            {
            	count++;
            	String one_id = rs.getString("id");
            	String one_name = rs.getString("name_company_address_book");
            	id_list.add(one_id);
            	nameList.add(one_name);
            	id_status.put(one_id, "2");
            	id_name.put(one_id, one_name);
            }
            this.shouldTotal = count;//correct
            rs.close();
            pstmt.close();
            conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
		buttonPanel.setLayout(new GridLayout(row, column));
		
		for(int i = 0; i < row * column; i++)
		{
			JButton btn = new JButton();
			btn.setBackground(Color.LIGHT_GRAY);
			buttonArray.add(btn);
			buttonPanel.add(buttonArray.get(i));
			//System.out.println(nameList);
			if(i<nameList.size())
			{
				buttonArray.get(i).setFont(new Font("SimHei",0,20));
				buttonArray.get(i).setText(nameList.get(i));
				buttonArray.get(i).setForeground(Color.WHITE);
				buttonArray.get(i).addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
						JButton btn = (JButton)arg0.getSource();
						 namePressed(btn);
					
					}
				});
			}
		}
		centerPanel.setLayout(new BorderLayout());

		centerPanel.add(BorderLayout.CENTER, ScrollBtnPanel);//add the Buttpn panel
		
		
		wholePanel = new JPanel();
		wholePanel.setLayout(new BorderLayout());
		//frame.getContentPane().add(BorderLayout.CENTER, centerPanel);
		this.wholePanel.add(BorderLayout.CENTER, centerPanel);
		this.output = new JTextArea();
		//output.setBackground(Color.red);
		southPanel.add(output,0);
		JButton btn = new JButton("刷新");
		btn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					readData();
					checkAttendence();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		btn.setFont(x);
		southPanel.add(btn,1);
		//this.frame.getContentPane().add(BorderLayout.SOUTH, southPanel);
		this.wholePanel.add(BorderLayout.SOUTH, southPanel);
		
		//frame.setVisible(true);
		
	}


	private void namePressed(JButton btn) {

		
		String login_name=id_name.get(rollCall.id_list.get(buttonArray.indexOf(btn)));
		final String login_id=id_list.get(buttonArray.indexOf(btn));
		
		Point localtion =MouseInfo.getPointerInfo().getLocation();
		System.out.println("sssss"+localtion.toString());
		final JFrame btnFrame = new JFrame("");
		
		btnFrame.setSize(270, 200);
		btnFrame.setAlwaysOnTop(true);
		btnFrame.setResizable(false);
		btnFrame.setFocusable(false);
		btnFrame.setLayout(new FlowLayout());
		
		btnFrame.setLocation(localtion);
		btnFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		btnFrame.add(new JLabel("身高(cm)"));
		final JTextField Hight = new JTextField();
		Hight.setPreferredSize(new Dimension(170,30));
		btnFrame.add(Hight);
		btnFrame.add(new JLabel("体重(kg)"));
		final JTextField Weight = new JTextField();
		Weight.setPreferredSize(new Dimension(170,30));
		btnFrame.add(Weight);
		
		btnFrame.add(new JLabel("体温(摄氏度)"));
		final JTextField Tempe = new JTextField();
		Tempe.setPreferredSize(new Dimension(170,30));
		btnFrame.add(Tempe);
		//btnFrame.setLayout(new BorderLayout());
		
		btnFrame.add(BorderLayout.SOUTH,new JLabel("记录时间  ："+(new java.sql.Date(System.currentTimeMillis()).toString())));
		
		JButton OK_btn = new JButton("提交");
		OK_btn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Connection conn;
				try {
					conn = connect();

				PreparedStatement pstmt ;
				ResultSet rs;
				pstmt = null;
				java.sql.Date DATE = new java.sql.Date(System.currentTimeMillis());
				java.sql.Time TIME = new java.sql.Time(System.currentTimeMillis());
				pstmt = conn.prepareStatement("INSERT INTO `bb2_test`.`emp_healthcheckdata` (`id`, `H_date`, `H_time`, `H_type`, `H_detail`, `H_collectorid`, `H_comment`)"+
				" VALUES(?,?,?,?,?,'K2014000','');");
				pstmt.setString(1,login_id );
				pstmt.setDate(2,DATE );
				pstmt.setTime(3, TIME);
				pstmt.setString(4,"001" );
				pstmt.setString(5, Hight.getText());
		        pstmt.execute();
		       
				pstmt = conn.prepareStatement("INSERT INTO `bb2_test`.`emp_healthcheckdata` (`id`, `H_date`, `H_time`, `H_type`, `H_detail`, `H_collectorid`, `H_comment`)"+
				" VALUES(?,?,?,?,?,'K2014000','');");
				pstmt.setString(1,login_id );
				pstmt.setDate(2,DATE );
				pstmt.setTime(3, TIME);
				pstmt.setString(4,"002" );
				pstmt.setString(5, Weight.getText());
		        pstmt.execute();
		        
				pstmt = conn.prepareStatement("INSERT INTO `bb2_test`.`emp_healthcheckdata` (`id`, `H_date`, `H_time`, `H_type`, `H_detail`, `H_collectorid`, `H_comment`)"+
				" VALUES(?,?,?,?,?,'K2014000','');");
				pstmt.setString(1,login_id );
				pstmt.setDate(2,DATE );
				pstmt.setTime(3, TIME);
				pstmt.setString(4,"003" );
				pstmt.setString(5, Tempe.getText());
		        pstmt.execute();
		        
		        pstmt.close();
		        conn.close();
		        } catch ( SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				btnFrame.dispose();
				
				
			}
			
		});
		
		btnFrame.add(OK_btn);
		btnFrame.setVisible(true);

}
	
	
	public void readData()
	{
		Connection conn;
		//PreparedStatement pstmt;
		PreparedStatement pstmt1;
		//ResultSet rs;
		ResultSet rs1;
		try {
			conn= connect();
			pstmt1 = null;
	         pstmt1 = conn.prepareStatement("SELECT * from emp_logginRecord;");
	         rs1 = pstmt1.executeQuery();

	         loginList = new LinkedList<String>();
	         timeList = new LinkedList<String>(); 
	         //rs1.next();
	         while(rs1.next())
	         {
	        	 String id = rs1.getString("id");
	        	 String status = rs1.getString("L_status");
	        	 String time = rs1.getTime("L_time").toString();
	        	 loginList.add(id);
	        	 timeList.add(time);
	        	 id_status.put(id, status);        
	        	 id_time.put(id, time);//id_and_staus <--> the correspongding login time
	         }
	         rs1.close();
	         pstmt1.close();
	         conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int currActual = 0;
         List<String> checked = new LinkedList<String>(); 
         for(int i=0;i<loginList.size();i++)
         {
        	 String id = loginList.get(i);
        	 if(id_list.contains(id) && !checked.contains(id))
        	 {
        		 checked.add(id);
        		 currActual++;
        	 }
         }
         System.err.println("currActual  = "+currActual);
         rollCall.actualTotal = currActual;
         this.setOutput();
	}
	
	public void yesterday() throws ClassNotFoundException, SQLException
	{
		Connection conn = this.connect();	
		PreparedStatement pstmt, pstmt_original;
		ResultSet rs;
		pstmt = null;
		pstmt_original = null;
		String beforeDate = getBeforeDate();
		pstmt = conn.prepareStatement("SELECT * from emp_logginRecordall where L_date='"+beforeDate+"';");
        rs = pstmt.executeQuery();
        Hashtable<List<String>, String> nameDate_str_before = new Hashtable<List<String>, String>();
        while(rs.next())
        {
        	String name = rs.getString("L_name");
        	String date = rs.getString("L_date");
        	List<String> one_record = new LinkedList<String>();
        	one_record.add(name);
        	one_record.add(date);
        	nameDate_str_before.put(one_record, "0");
        }
        //System.out.println( "----------------------"+ nameDate_str_before.size());
        pstmt_original = conn.prepareStatement("SELECT * from emp_id where security_level='level 5';");
        ResultSet rs_original = pstmt_original.executeQuery();
        List<String> list_original = new LinkedList<String>();
        while(rs_original.next())
        {
        	String name = rs_original.getString("name_company_address_book");
        	if(!list_original.contains(name)){
            	list_original.add(name);	
        	}
        }
        Enumeration eumeration_before = nameDate_str_before.keys();
        List<String> list_before = new LinkedList<String>();
        while(eumeration_before.hasMoreElements())
        {
        	List<String> one = (List<String>) eumeration_before.nextElement();
        	String name = one.get(0);
        	if(!list_before.contains(name))
        	{
        	 	list_before.add(name);
        	}
        }
        labelPanel.setLayout(new GridLayout(30,1));
        for(int i=0;i<list_original.size();i++)
        {
        	String a_name = list_original.get(i);
        	if(!list_before.contains(a_name))
        	{
        		JLabel a_label = new JLabel(a_name);
        		a_label.setFont(f);
                labelPanel.add(a_label);
        	}
        }
        rs.close();
        pstmt.close();
        conn.close();
	}
	
	public String getBeforeDate()
	{
		Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);   
        String  yestedayDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	    return yestedayDate;
	}
		
	public void checkAttendence() throws SQLException
	{
		name_button = new Hashtable<String, JButton>();
		for(int i = 0;i<id_list.size();i++)
		{
			name_button.put(nameList.get(i), buttonArray.get(i));
		}
		for(int i=0; i<loginList.size(); i++)
        {
       	 	String login_id = loginList.get(i);
       	 	String login_name = id_name.get(login_id);
       	 	if(id_list.contains(login_id))// && !morningCheckedList.contains(login_id))
       	 	{
       	 		String status = id_status.get(login_id);
       	 		char s = status.charAt(0);
       	 		//System.out.println("---here---"+s);
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
				time = rollCall.id_time.get(login_id);//get the corresponding time
				name_button.get(login_name).setText("<html><body>"+login_name+"<br>"+time+"<body></html>");
       	 	}
        }
	}
	
	public void readDatabase() 
	{
		PreparedStatement pstmt;
		ResultSet rs;
		pstmt = null;
		Connection conn=null;
		try {
			conn = connect();
		
		    pstmt = conn.prepareStatement("SELECT * from emp_logginRecord;");
		    rs = pstmt.executeQuery();
		    loginList = new LinkedList<String>();
		    while(rs.next())
		    {
		    	String ID=rs.getString("id");
		    	if(!loginList.contains(ID))
		    		loginList.add(ID);
		    	id_name.put(ID, rs.getString("L_name"));
		    	id_status.put(ID, rs.getString("L_status"));
		    	id_time.put(ID, rs.getTime("L_time").toString());
		    }
		    
		    rs.close();
		    pstmt.close();
		    conn.close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
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
				String time = id_time.get(login_id);//get the corresponding time
				name_button.get(login_name).setText("<html><body>"+login_name+"<br>"+time+"<body></html>");
       	 	}
        }
	}

	/*
	public void morningNoonCheck()
	{
		JFrame checkFrame = new JFrame("晨检午检");
		frame.setSize(300, 200);
		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		frame.setFocusable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.setVisible(true);
	}*/
	
	public Connection connect() throws  SQLException
	{
		Connection conn = KYMainUI.bds.getConnection();
        if(!conn.isClosed()) 
        	System.out.println("Succeeded connecting to the Database!");
        return conn;
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		rollCall test = new rollCall();
		test.run();
	}
	@Override
	public void run() {
		try {
			readData();//3
			checkAttendence();//4
			KeepCheckingDatabase keepChecking = new KeepCheckingDatabase(this);
		//} catch (ClassNotFoundException | SQLException | IOException| InterruptedException e) {
		} catch (ClassNotFoundException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
