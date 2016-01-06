package KYUI;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import Client4CLass.CalendarPop;
import bb.gui.base.ClientUI;

public class AskForLeaveUI extends ClientUI{
	
	public static AskForLeaveUI instance = null;
	
	public JTextField fromField;
	public JTextField toField;
	public JFrame f;
	private String passDate="";
	public CheckboxGroup cbg_teacher;
	public CheckboxGroup cbg_type;
	public JTextField reasonField;
	public CheckboxGroup time_cbg;
	public Hashtable<String, String> name_id;
	public CheckboxGroup cbg_from;
	public CheckboxGroup cbg_to;
	public JTextArea showArea;
	public String className = KYMainUI.department;
	public JButton fromBtn;
	public JButton toBtn;
	public String timeType;
	public JScrollPane jsp;
	public int counter;
	public Font font1;
	public String noon = "今日中午";
	public boolean currentOrNext = true;
	public int noonTimes = 4;
	
	public AskForLeaveUI()
	{
		instance=this;
		setTitle("请假申请");
		font1 = new Font("标楷体",Font.BOLD,15);
		fromField = new JTextField(10);
		toField = new JTextField(10);
		
		
		instance.add(BorderLayout.NORTH, getApplyPanel());
		
		instance.add(BorderLayout.CENTER, getShowPanel());

	}
	
	public JPanel getShowPanel()
	{
		JPanel showPanel = new JPanel();
		showPanel.setLayout(new BorderLayout());
		showArea = new JTextArea();
		showArea.setEditable(false);
		jsp = new JScrollPane(showArea);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		showAllRecords();
		
		JPanel refreshPanel = new JPanel();
		JButton refreshBtn = new JButton("更新");
		refreshBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				refresh();
			}
			
		});
		refreshPanel.add(refreshBtn);
		showPanel.add(jsp);
		showPanel.add(BorderLayout.SOUTH, refreshPanel);
		return showPanel;
	}
	
	public void refresh()
	{
		String output = "";
		String title = "";
		title += "代码\t工号\t姓名\t班级\t日期\t时间\t类别\t\t请假时间\t\t批准与否\n";
		output += title;
		try {
			Connection conn = connect();
			PreparedStatement p = null;
			ResultSet r = null;
			p = conn.prepareStatement("select * from emp_askForLeave where app_department='"+this.className+"' order by app_date desc, app_time desc;");
			r = p.executeQuery();
			while(r.next())
			{
				String one_record = "";
				String code = r.getString("code"); one_record += code+"\t";
				String id = r.getString("id"); one_record += id+"\t";
				String name = r.getString("app_name"); one_record += name+"\t";
				String department = r.getString("app_department"); one_record += department+"\t";
				Date date = r.getDate("app_date"); one_record += date+"\t";
				Time time = r.getTime("app_time"); one_record += time+"\t";
				String type = r.getString("app_type"); one_record += type+"\t";
				boolean currentNoon = r.getBoolean("app_currentNoon");
				Date fromDate = r.getDate("app_from");
				if(currentNoon)
				{
					one_record += "\t"+fromDate+"中午";
				}
				else
				{
					//one_record += "\n";
					String from_code = r.getString("from_code");
					
					Date toDate = r.getDate("app_to");
					String to_code = r.getString("to_code");
					one_record += fromDate+"("+from_code+")--"+toDate+"("+to_code+")";
				}
				//boolean allow = r.getBoolean("allow");
				int allow = r.getInt("allow");
				if(allow == 0)
				{
					one_record += "\t\n";
				}
				else if(allow == 2)
				{
					//one_record += "\t\n";
					one_record += "\t不批准\n";
				}
				else if(allow == 1)
				{
					one_record += "\t批准\n";	
				}
				output += one_record;
			}
			r.close();
			p.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showArea.setText(output);
		repaint();
	}
	
	public JPanel getApplyPanel()
	{
		List<String> teachers = getTeachers(className);
		JPanel panel = new JPanel();
	
		panel.setLayout(new BorderLayout());
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(1,2));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(3,1));
		leftPanel.setBorder(BorderFactory.createTitledBorder("请假事项"));
		//------------------teacher panel------------------------------------------------
		JPanel teacherPanel = new JPanel();
		teacherPanel.setLayout(new GridLayout(1,3));
		JLabel label = new JLabel("请假人:");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		teacherPanel.add(label);
		cbg_teacher = new CheckboxGroup(); //定义组 
		for(int i=0;i<teachers.size();i++)
		{
			String a_name = teachers.get(i);
			Checkbox one_item;
			if(i == 0)
			{
				one_item = new Checkbox(a_name, cbg_teacher, true);
			}
			else
			{
				one_item = new Checkbox(a_name, cbg_teacher, false);
			}
			teacherPanel.add(one_item);
		}  
		//operator = cbg_teacher.getSelectedCheckbox().toString().split("label=")[1].split(",")[0];
		
		//--------------------------type panel---------------------------------------------
		JPanel typePanel = new JPanel();
		typePanel.setLayout(new GridLayout(1,4));
		JLabel typeLabel = new JLabel("请假类型:");
		typeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		cbg_type = new CheckboxGroup(); //定义组 
		Checkbox type1 = new Checkbox("事假", cbg_type, true);
		Checkbox type2 = new Checkbox("病假", cbg_type, false);
		typePanel.add(typeLabel);
		typePanel.add(type1);
		typePanel.add(type2);
		
		//-----------------------------reason Panel--------------------------------------
		JPanel reasonPanel = new JPanel();
		reasonPanel.setLayout(new GridLayout(1,2));
		JLabel reasonLabel = new JLabel("请假原因:");
		reasonField = new JTextField(25);
		reasonPanel.add(reasonLabel);
		reasonPanel.add(reasonField);
	
		//-----------------------------time Panel--------------------------------------
		JPanel timePanel = new JPanel();
		timePanel.setBorder(BorderFactory.createTitledBorder("请假时间"));
		timePanel.setLayout(new GridLayout(4,2));
		time_cbg = new CheckboxGroup(); //定义组 
		Checkbox cb1 = new Checkbox(noon, time_cbg, true);
		Checkbox cb2 = new Checkbox("明日中午", time_cbg, false);
		Checkbox cb3 = new Checkbox("选择时间段", time_cbg, false);
		cb1.setName("currentNoon");
		cb2.setName("nextNoon");
		cb3.setName("DateBox");
		ItemListener it= new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(((Checkbox)e.getSource()).getName().equals("DateBox"))
				{
					
					if(reasonField.getText().equals("")){
						JOptionPane.showMessageDialog(null, "请填写请假原因！");
					}
					else
					/*
					while(reasonField.getText().equals(""))
					{
						
						JOptionPane.showMessageDialog(null, "请填写请假原因！");
					}*/
					{
						fromField.setEditable(true);
						toField.setEditable(true);
						fromBtn.setEnabled(true);
						toBtn.setEnabled(true);
					}
				}else
				{
					fromField.setEditable(false);
					toField.setEditable(false);
					fromBtn.setEnabled(false);
					toBtn.setEnabled(false);
				}
				if(((Checkbox)e.getSource()).getName().equals("currentNoon"))
				{
					currentOrNext = true;
				}
				else if(((Checkbox)e.getSource()).getName().equals("nextNoon"))
				{
					currentOrNext = false;
				}
				else
				{
					currentOrNext = true;
				}
			}
		};
		cb1.addItemListener(it);
		cb2.addItemListener(it);
		cb3.addItemListener(it);
		if(cb1.getState()==true)//noon    
		{
			//System.err.println("!!!!!!!!!!!!!!!!!!今日中午!!!!!!!!!!!!!!");
		}
		else if(cb2.getState()==true)
		{
			
		}
		else if(cb3.getState() == true)//time period 
		{
			this.fromBtn.setEnabled(true);
			this.toBtn.setEnabled(true);
		}
		
		timePanel.add(cb1);timePanel.add(new JLabel());
		timePanel.add(cb2);timePanel.add(new JLabel());
		timePanel.add(cb3);timePanel.add(new JLabel());
		
		JPanel fromPanel = new JPanel();//fromPanel.setVisible(false);
		fromPanel.setLayout(new GridLayout(2,2));
		fromBtn = new JButton("起始");fromBtn.setEnabled(false);

		cbg_from = new CheckboxGroup();
		final Checkbox from1 = new Checkbox("上午", cbg_from, false);
		from1.setVisible(false);
		final Checkbox from2 = new Checkbox("下午", cbg_from, false);
		from2.setVisible(false);
		fromBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				newFrame(fromField);
				//if(!passDate.equals(""))
				{
					from1.setVisible(true);
					from2.setVisible(true);
				}
			}
		});
		fromField.setName("BEGIN");
		fromField.setEditable(false);
		
		fromPanel.add(fromBtn);
		fromPanel.add(fromField);
		fromPanel.add(from1);
		fromPanel.add(from2);
		
		JPanel toPanel = new JPanel();
		toPanel.setLayout(new GridLayout(2,2));
		
		toBtn = new JButton("结束");toBtn.setEnabled(false);

		this.cbg_to = new CheckboxGroup(); //定义组 
		final Checkbox toG1 = new Checkbox("上午", cbg_to, false);
		final Checkbox toG2 = new Checkbox("下午", cbg_to, false);
		toG1.setVisible(false);
		toG2.setVisible(false);
		toBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				newFrame(toField);
				//if(!passDate.equals(""))
				{
					toG1.setVisible(true);
					toG2.setVisible(true);
				}
			}
		});
		toField.setName("END");
		toField.setEditable(false);
		
		toPanel.add(toBtn);
		toPanel.add(toField);
		toPanel.add(toG1);
		toPanel.add(toG2);
		
		timePanel.add(fromPanel);
		timePanel.add(toPanel);
		
		
		leftPanel.add(teacherPanel);
		leftPanel.add(typePanel);
		leftPanel.add(reasonPanel);
		
		timeType = time_cbg.getSelectedCheckbox().toString().split("label=")[1].split(",")[0];
		infoPanel.add(leftPanel);
		infoPanel.add(timePanel);
		
		//-----------------------------------cancel panel-------------------------------------------------
		JPanel cancelPanel = new JPanel();
		cancelPanel.setBorder(BorderFactory.createTitledBorder("删除申请项"));
		cancelPanel.setLayout(new GridLayout(2,4));
		JLabel hintLabel = new JLabel("输入需删除项的代码");
		final JTextField cancelCodeField = new JTextField(3);
		
		JButton deleBtn = new JButton("删除");
		cancelPanel.add(hintLabel);
		cancelPanel.add(new JLabel(""));
		cancelPanel.add(new JLabel(""));
		cancelPanel.add(new JLabel(""));
		cancelPanel.add(cancelCodeField);
		cancelPanel.add(deleBtn);
		cancelPanel.add(new JLabel(""));
		cancelPanel.add(new JLabel(""));
		//-----------------------------------submit panel-------------------------------------------------
		JPanel submitPanel = new JPanel();
		submitPanel.setLayout(new FlowLayout());
		deleBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cancelHandler(cancelCodeField.getText());
				refresh();
				cancelCodeField.setText("");
			}
		});

		final JButton submitBtn = new JButton("提交");
		submitBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				submitHandler();
				//if()
				{
					//clearApplicationPanel();
					refresh();
				}
			}
		});
		JButton quitBtn = new JButton("退出");
		quitBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				instance.dispose();
			}
			
		});
		submitPanel.add(submitBtn);
		//submitPanel.add(quitBtn);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(1,2));
		
		southPanel.add(cancelPanel);
		southPanel.add(submitPanel);
		
		panel.add(infoPanel);
		panel.add(BorderLayout.SOUTH, southPanel);
		return panel;
	}
	
	public void cancelHandler(String deleCode)
	{
		if(deleCode.equals("")){
			JOptionPane.showMessageDialog(null, "请输入您需要删除的已提交的申请项的代码");
		}
		else
		{
			boolean allow = true;
			try {
				Connection conn = connect();
				PreparedStatement p = null;
				p = conn.prepareStatement("select * from emp_askForLeave where app_department=? and code =? ;");
				p.setString(1, this.className);
				p.setString(2, deleCode);
				ResultSet rs=p.executeQuery();
				if(rs.next())
				{
					allow = rs.getBoolean("allow");
				}else
				{
					JOptionPane.showMessageDialog(null, "没有你所选择的条目");
				}
				
				
				
				if(!allow)
				{
				p = conn.prepareStatement("delete from emp_askForLeave where app_department=? and code =? ;");
				p.setString(1, this.className);
				p.setString(2, deleCode);
				
				p.execute();
				p.close();
				conn.close();
				}
				else
				{
					JOptionPane.showMessageDialog(null, "你不能删除已经批准的条目！");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void clearApplicationPanel()
	{
		this.reasonField.setText("");
		fromBtn.setEnabled(false);
		fromField.setEnabled(false);
		toBtn.setEnabled(false);
		toField.setEnabled(false);
	}
	
	public void submitHandler()
	{
		String name = cbg_teacher.getSelectedCheckbox().toString().split("label=")[1].split(",")[0];//!!!!name
		String type = cbg_type.getSelectedCheckbox().toString().split("label=")[1].split(",")[0];//!!!!type
		String reason = reasonField.getText();//!!!!reason
		String timeType = time_cbg.getSelectedCheckbox().getName();
		if(reason.equals(""))
		{
			JOptionPane.showMessageDialog(null, "请填写请假原因！");
		}
		else{
			try {
				Connection conn = connect();
				PreparedStatement p;
				ResultSet r = null;
				p = conn.prepareStatement("select * from emp_askForLeave;");
				r = p.executeQuery();
				counter = 0;
				if(!r.next())
					counter=0;
				else
				{
					r.last();
					counter=Integer.parseInt(r.getString("code"));
				}
				p = conn.prepareStatement("insert into emp_askForLeave values (?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
				
				String code = String.valueOf(counter+1);
				
				String id = name_id.get(name);
				java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
				java.sql.Time time = new java.sql.Time(System.currentTimeMillis());
				
				p.setString(1, code);
				p.setString(2, id);
				p.setString(3, name);
				p.setString(4, className);
				p.setDate(5, date);
				p.setTime(6, time);
				p.setString(7, type);
				p.setString(8, reason);
				if(timeType.equals("currentNoon") || timeType.equals("nextNoon"))
				{
					Date currentDate = new Date(System.currentTimeMillis());
					if(!currentOrNext)
					{
						currentDate=new java.sql.Date(System.currentTimeMillis()+1000*60*60*24);
					}
					p.setBoolean(9, true);
					p.setDate(10, currentDate);
					p.setString(11, "");
					p.setDate(12, null);
					p.setString(13, "");
				
					int validCount = validApplication(id);
					if(validCount <noonTimes)
					{
						validCount++;
						p.setInt(14, 1);//accept the application
						KYUITray.getInstance().showTrayMsg("申请批准", name+"的申请已经获得系统自动批准。你的免审批还余 "+(noonTimes-validCount)+" 次");
					}
					else
					{
						KYUITray.getInstance().showTrayMsg("等待批准", name+"，你本月中午请假免审批次数已经超过 "+String.valueOf(noonTimes)+" 次!,请等待 批准");
						JOptionPane.showMessageDialog(null, "你本月中午请假免批准次数已经超过"+String.valueOf(noonTimes)+"次!");
						p.setInt(14, 0);//not accept the application
					}
					p.execute();
					clearApplicationPanel();
				}
				else
				{
					p.setBoolean(9, false);
					String fromDateStr = fromField.getText();//from date
					String toDateStr = toField.getText();//to date
					if(fromDateStr.equals("") && toDateStr.equals(""))
					{
						JOptionPane.showMessageDialog(null, "请选择起始和结束日期");
					}
					else if(fromDateStr.equals(""))
					{
						JOptionPane.showMessageDialog(null, "请选择起始日期");
					}
					else if(toDateStr.equals(""))
					{
						JOptionPane.showMessageDialog(null, "请选择结束日期");
					}
					else
					{
						if(Date.valueOf(fromDateStr).after(Date.valueOf(toDateStr)))
						{
							//?????????
							this.fromBtn.enable(true);
							this.toBtn.enable(true);
							JOptionPane.showMessageDialog(null, "你选择的结束日期先于起始日期, 请修改");
						}
						else
						{
							if(cbg_from.getSelectedCheckbox()==null && cbg_to.getSelectedCheckbox() == null )
							{
								JOptionPane.showMessageDialog(null,"请选择开始和结束的具体时间");
							}
							else if(cbg_from.getSelectedCheckbox()==null)
							{
								JOptionPane.showMessageDialog(null,"请选择开始具体时间");
							}
							else if(cbg_to.getSelectedCheckbox()==null)
							{
								JOptionPane.showMessageDialog(null,"请选择结束的具体时间");
							}
							else
							{
								String fromHalfCode = cbg_from.getSelectedCheckbox().toString().split("label=")[1].split(",")[0];
								String toHalfCode = cbg_to.getSelectedCheckbox().toString().split("label=")[1].split(",")[0];
								
								p.setDate(10, Date.valueOf(fromDateStr));
								p.setString(11, fromHalfCode);
								p.setDate(12, java.sql.Date.valueOf(toDateStr));
								p.setString(13, toHalfCode);
								
								p.setInt(14, 0);
								p.execute();
								clearApplicationPanel();
							}
						}
					}
				}
				p.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int validApplication(String id)
	{
		int counter = 0;
		boolean flag = false;
		try {
			Date currentDate = new Date(System.currentTimeMillis());
			int year = currentDate.getYear()+1900;
			int month = currentDate.getMonth()+1;
			String currentDateStr = String.valueOf(year)+"-";
			if(month<10)
			{
				currentDateStr += "0"+String.valueOf(month);
			}
			else
			{
				currentDateStr += ""+String.valueOf(month);
			}
			Connection conn = connect();
			PreparedStatement p = null;
			ResultSet r = null;
			p = conn.prepareStatement("select * from emp_askForLeave where id = '"+id+"' and app_date like '"+currentDateStr+"%' and app_currentNoon='1';");
			r = p.executeQuery();
			//System.out.println(p);
			while(r.next())
			{
				counter += 1;
			}
			if(counter < noonTimes)
			{
				flag = true;
			}
			r.close();
			p.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return counter;
	}
	
	public void showAllRecords()
	{
		this.showArea.setFont(font1);
		String output = "";
		String title = "";
		title += "代码\t工号\t姓名\t班级\t日期\t时间\t类别\t\t请假时间\t\t批准与否\n";
		output += title;
		try {
			Connection conn = connect();
			PreparedStatement p = null;
			ResultSet r = null;
			p = conn.prepareStatement("select * from emp_askForLeave where app_department='"+this.className+"' order by app_date desc, app_time desc;");
			r = p.executeQuery();
			//int count = 0;
			while(r.next())
			{
				String one_record = "";
				String code = r.getString("code"); one_record += code+"\t";
				String id = r.getString("id"); one_record += id+"\t";
				String name = r.getString("app_name"); one_record += name+"\t";
				String department = r.getString("app_department"); one_record += department+"\t";
				Date date = r.getDate("app_date"); one_record += date+"\t";
				Time time = r.getTime("app_time"); one_record += time+"\t";
				String type = r.getString("app_type"); one_record += type+"\t";
				boolean currentNoon = r.getBoolean("app_currentNoon");
				Date fromDate = r.getDate("app_from");
				if(fromDate == null)
				{
					fromDate = new java.sql.Date(System.currentTimeMillis());
						
				}
				if(currentNoon)
				{
					java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
					//String nextDate = nextDate(currentDate.toString());
					
					one_record += "\t"+fromDate.toString()+"中午";
				}
				else
				{
					//Date fromDate = r.getDate("app_from");
					String from_code = r.getString("from_code");
					
					Date toDate = r.getDate("app_to");
					String to_code = r.getString("to_code");
					one_record += fromDate+"("+from_code+")--"+toDate+"("+to_code+")";
				}
				boolean allow = r.getBoolean("allow");
				if(!allow)
				{
					one_record += "\t\n";
				}
				else
				{
					one_record += "\t批准\n";
				}
				output += one_record;
			}
			r.close();
			p.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.showArea.setText(output);
	}
	
	public String nextDate(String currentDate)
	{
		String[] split_str = currentDate.split("-");
		String current_year = split_str[0];
		String current_month = split_str[1];
		String current_day = split_str[2];
		String next_year = String.valueOf(Integer.parseInt(current_year)+1);
		String next_month;
		if(Integer.valueOf(current_month) <= 11)
		{
			next_month = String.valueOf(Integer.parseInt(current_month)+1);
		}
		else
		{
			next_month = "01";
		}
		String result = next_year+"-"+next_month;
		return result;
	}
	
	public void newFrame(JTextField settedField)
	{
		JPanel menu = null; 
		f = new JFrame("日历");
			String className = "中班";//classField.getText();
			CalendarPop pop = new CalendarPop(className,this,settedField);
			menu = pop.mainPanel; 
			System.out.println("component = "+menu.countComponents());

			
			f.add(menu);
			int a10 = Toolkit.getDefaultToolkit().getScreenSize().width; // 取得屏幕长度
			int b10 = Toolkit.getDefaultToolkit().getScreenSize().height; // 取得屏幕宽度
			f.setLocation((a10 - 600) / 2, (b10 - 500) / 2); // 设定位置（屏幕中心）
			f.setSize(300, 280); // 设定大小
			f.setVisible(true); // 设定不能缩放
			f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
	}
	
	public List<String> getTeachers(String className)
	{
		List<String> teacher_list = new LinkedList<String>();
		//List<String> id_list = new LinkedList<String>();
		name_id = new Hashtable<String, String>();
		try {
			Connection conn = connect();
			PreparedStatement p = null;
			ResultSet r = null;

			
			
			p = conn.prepareStatement("select * from emp_id where job_id = '"+className+"' and security_level = 'level 3';");
			r = p.executeQuery();
			while(r.next())
			{
				String a_id = r.getString("id");
				String a_name = r.getString("name_company_address_book");
				teacher_list.add(a_name);
				name_id.put(a_name, a_id);
			}
			r.close();
			p.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return teacher_list;
	}
	
	public Connection connect() throws SQLException 
	{

        	return KYMainUI.bds.getConnection();
	}
	
	public void getDatefromCalender(String Date,JTextField settedText)
	{
		this.passDate = Date;
		System.err.println("passDate = "+passDate);
		settedText.setText(passDate);
		f.dispose();
	}

	public static AskForLeaveUI getInstance()
	{
		
		if(instance == null)
		{
			new AskForLeaveUI();
		}
		return instance;
	}
	
	

}
