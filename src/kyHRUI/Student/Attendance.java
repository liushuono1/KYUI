package kyHRUI.Student;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import KYUI.KYMainUI;
import bb.gui.hr.EmployeeActionManager;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class Attendance extends JFrame{
	public JPanel northPanel;
	public JPanel southPanel;
	public Connection conn;
	public  List<String> id_list;
	public List<String> nameList;
	public String classType;
	public Hashtable<String, String> id_name;
	public List<String> loginList;
	JComboBox jb;
	public JTextField studentField;	
	public boolean clickStart = false;
	public double actualTotal;
	public double shouldTotal;
	public double classDayAttdRate;
	public List<String> id_already;//the IDs that have been swiped
	public JTextArea area;
	public String[] allClassTypes;
	public Font font;
	public Font font1;
	public Hashtable<List<String>, String> idDate_str;
	public Hashtable<List<String>, String> stuIdDate_str;
	public String currentDate;
	public List<String> one_record;
	String current_year;
	String current_month;
	String current_day;
	public String studentName;
	public boolean studetExist = true;
	public double todayTotal;
	public JPanel whole;
	
	public Attendance() throws ClassNotFoundException, SQLException, IOException
	{
		
		//checkDifference();
		whole = new JPanel();
		font = new Font("标楷体",Font.BOLD,35);
		font1 = new Font("标楷体",Font.BOLD,25);
		//frame = new JFrame("出勤统计");
		int a10 = Toolkit.getDefaultToolkit().getScreenSize().width; // 取得屏幕长度
		int b10 = Toolkit.getDefaultToolkit().getScreenSize().height; // 取得屏幕宽度
		/*
		frame.setLocation((a10 - 600) / 2, (b10 - 500) / 2); // 设定位置（屏幕中心）
		frame.setSize(800, 600); // 设定大小
		frame.setResizable(true); // 设定不能缩放
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		*/
		readConfigFile();//get all different class names
		
		JPanel classPanel = new JPanel();
		classPanel.setLayout(new GridLayout(2,4));
		JLabel label = new JLabel("班级:",JLabel.CENTER);
		label.setFont(font1);
		//classField = new JTextField();
		//classField.setFont(font1);
		
		jb = new JComboBox();
		//jb.addItem("");
		jb.setFont(font1);
		for(int i=0;i<allClassTypes.length;i++)
		{
			String a_className = allClassTypes[i]; 
			jb.addItem(a_className);
		}
		
		studentField = new JTextField();
		studentField.setFont(font1);
		JButton classDayButton = new JButton("日出勤率");//班级日出勤
		JButton classMonthButton = new JButton("月出勤率");//班级月出勤
		JButton absentButton = new JButton("今日缺勤");
		absentButton.setFont(font1);
		absentButton.setBackground(Color.CYAN);
		absentButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					clearInfoArea();
					absenceStudents();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		classDayButton.setFont(font1);
		classMonthButton.setFont(font1);
		
		classDayButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				clearInfoArea();
				if(clickStart == false)
				{
					try {
						search(0);
						searchLoginAll();
						calculateClassDayAttendence();//4
					}catch (SQLException e1) {
						e1.printStackTrace();
					} catch (ClassNotFoundException e2) {
					}
				}
			}
		});
		
		classMonthButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				clearInfoArea();
				classType = jb.getSelectedItem().toString();
				java.sql.Date data = new java.sql.Date(System.currentTimeMillis());
				
				Hashtable<String, Double> rate =Simple.HR.SimpleAttendenceRate.getAttendenceRate(data.toString().split("-")[0]+"-"+data.toString().split("-")[1], KYMainUI.bds);
				
				area.setFont(new Font("SimHei",0,30));
				area.setForeground(Color.BLACK);
				area.setText(classType+" 月出勤率: "+( new BigDecimal(rate.get(classType)*100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
				
			}
		});
		
		classPanel.add(label);//1
		classPanel.add(jb);//2
		classPanel.add(classDayButton);//3
		classPanel.add(classMonthButton);//4
		
		JLabel studentLabel = new JLabel("学生: ", JLabel.CENTER);
		studentLabel.setFont(font1);
		JButton studentMonth = new JButton("月出勤率");
		studentMonth.setFont(font1);
		studentMonth.addActionListener(new  ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				clearInfoArea();
				try {
					searchStudentLoginAll();
					calculateStudentMonth();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		classPanel.add(studentLabel);//5
		classPanel.add(studentField);//6
		classPanel.add(studentMonth);//7
		classPanel.add(absentButton);
	
		area = new JTextArea();
		area.setEditable(false);
		area.setLineWrap(true);
		
		area.setFont( new Font("标楷体",Font.BOLD,25));
		area.setBackground(Color.LIGHT_GRAY);
		area.setForeground(Color.BLACK);
		JScrollPane jsp = new JScrollPane(area);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(1, 2));
		JButton outputButton = new JButton("输出本月总出勤");
		//JButton quitButton = new JButton("退出");
		JButton totalButton = new JButton("今日出勤");
		outputButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "xls");
					chooser.setFileFilter(filter);
					int option = chooser.showOpenDialog(null);
					if(option == JFileChooser.APPROVE_OPTION)
					{
						String path = chooser.getSelectedFile().getAbsolutePath();
						outputExcel(path);
					}
				} catch (WriteException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		totalButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					todayTotal();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		/*
		quitButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
                 frame.dispose();
			}
		});*/
		totalButton.setFont(font1);
		//quitButton.setFont(font1);
		southPanel.add(totalButton);
		southPanel.add(outputButton);
		//southPanel.add(quitButton);
		outputButton.setFont(font1);
		whole.setLayout(new BorderLayout());
		whole.add(BorderLayout.NORTH, classPanel);
		whole.add(BorderLayout.CENTER, jsp);
		whole.add(BorderLayout.SOUTH, southPanel);
		//frame.setVisible(true);
		
		//wholePanel.add(BorderLayout.NORTH, classPanel);
	}

	public Connection connect() throws ClassNotFoundException, SQLException
	{
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://192.168.1.100:3307/bb2_test";
		String user = "root"; 
		String password = "root";
        Class.forName(driver);
        conn = DriverManager.getConnection(url, user, password);
        if(!conn.isClosed()) 
        	System.out.println("Succeeded connecting to the Database!");
        return conn;
	}

	public int search(int status) throws SQLException, ClassNotFoundException
	{
		this.connect();
		PreparedStatement pstmt = null;
		ResultSet rs;
        classType = jb.getSelectedItem().toString();

        pstmt = conn.prepareStatement("SELECT * from emp_id where department = "+"'"+classType+"' and security_level='level 5';");
        rs = pstmt.executeQuery();
        id_list = new LinkedList<String>();
        if(rs.next())//not empty
        {
        	 int count = 1;
        	 id_list.add(rs.getString("id"));
             nameList = new LinkedList<String>();
             id_name = new Hashtable<String, String>();
             while(rs.next())
             {
            	 count++;
            	 String one_id = rs.getString("id");//get one id
            	 String one_name = rs.getString("name_company_address_book");//get one name
            	 id_list.add(one_id);
            	 nameList.add(one_name);
            	 id_name.put(one_id, one_name);
             }
             shouldTotal = count;//correct
             this.readLogin();
        
        }
        else{
        	JOptionPane.showMessageDialog(null, "您输入的不是合法的 班级名称, 请重新输入正确的班级名称!");
        	 return 0;
        }
        return 0;
	}
	
	public void searchLoginAll() throws SQLException, ClassNotFoundException
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		currentDate = df.format(new Date());
		splitCurrentDate();
				
		this.connect();
		classType = jb.getSelectedItem().toString();
		PreparedStatement pstmt = null;
		ResultSet rs;
	    pstmt = conn.prepareStatement("SELECT * from emp_logginrecordall where department = "+"'"+this.classType+"';");

		 rs = pstmt.executeQuery();
        idDate_str = new Hashtable<List<String>, String>();
        while(rs.next())
        {
        	String id = rs.getString("id");
        	String date = rs.getString("L_date");
        	String department = rs.getString("department");
        	List<String> splitList = splitDate(date);
        	String year = splitList.get(0);//year
        	String month = splitList.get(1);//month
        	if(this.current_year.endsWith(year) && this.current_month.equals(month) && department.equals(classType))
        	{
        		List<String> one_record = new LinkedList<String>();
            	one_record.add(id);//id
            	one_record.add(date);//date
            	idDate_str.put(one_record, "0");
        	}
        }
        Enumeration enumerate = idDate_str.keys();
        while(enumerate.hasMoreElements())
        {
        	List<String> a_record = (List<String>) enumerate.nextElement();
        }
        pstmt.close(); 
      	rs.close();
      	conn.close();
      	//System.exit(1);
	}
	
	public void searchStudentLoginAll() throws ClassNotFoundException, SQLException
	{
		this.connect();
		studentName = studentField.getText();
		PreparedStatement pstmt = null;
		ResultSet rs;
	    pstmt = conn.prepareStatement("SELECT * from emp_logginrecordall where L_name = "+"'"+studentName+"';");
        rs = pstmt.executeQuery();
        stuIdDate_str = new Hashtable<List<String>, String>(); 
        if(rs.next())
        {
        	String id = rs.getString("id");
        	String date = rs.getString("L_date");
        	List<String> one_record = new LinkedList<String>();
        	one_record.add(id);//id
        	one_record.add(date);//date
        	stuIdDate_str.put(one_record, "0");
        	while(rs.next())
            {
            	id = rs.getString("id");
            	date = rs.getString("L_date");
            	one_record = new LinkedList<String>();
            	one_record.add(id);//id
            	one_record.add(date);//date
            	stuIdDate_str.put(one_record, "0");
            }
        	studetExist = true;
        }
        else
        {
        	studetExist = false;
        	JOptionPane.showMessageDialog(null, "您输入的学生姓名不存在， 请输入正确的学生姓名！");
        }
        	
        pstmt.close(); 
      	rs.close();
      	conn.close();
	}
	public void calculateClassDayAttendence() throws SQLException
	{
		double actualCount = 0;
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		//currentDate = df.format(new Date());// new Date()为获取当前系统时间
		splitCurrentDate();
		Enumeration myEnumerate  = (Enumeration) idDate_str.keys();
		int counter = 0;
		while(myEnumerate.hasMoreElements())
		{
			counter++;
			List<String> id_date = (List<String>) myEnumerate.nextElement();
			String date = id_date.get(1);
			if(isValidDate(date, 0))
			{
				actualCount++;
			}		
		}
		classDayAttdRate = actualCount / shouldTotal;
		BigDecimal bd = new BigDecimal(classDayAttdRate*100);
		BigDecimal bd_actual = new BigDecimal(actualCount);
		BigDecimal bd_should = new BigDecimal(shouldTotal);
		int actual = bd_actual.setScale(1, BigDecimal.ROUND_HALF_UP).intValue();
		int should = bd_should.setScale(2, BigDecimal.ROUND_HALF_UP).intValue();
		area.setText(classType+":\n"+
				"应到"+should+"人, 实到"+actual+"人\n"+
				"今日出勤率: "+ bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
	}
	
	
	/*
	 * Calculate the Attendence Rate untill the day
	 */
	public void calculateClassMonth() throws ClassNotFoundException, SQLException
	{
		double actualMonth = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		currentDate = df.format(new Date());// new Date()为获取当前系统时间
		this.splitCurrentDate();
		Enumeration myEnumerate  = (Enumeration) idDate_str.keys();
		while(myEnumerate.hasMoreElements())
		{
			List<String> id_date = (List<String>) myEnumerate.nextElement();
			String date = id_date.get(1);
			if(isValidDate(date, 1))
			{
				actualMonth++;
			}		
		}
		double shouldMonth = id_list.size() * Double.valueOf(current_day);
		double classMonthAttend = actualMonth / shouldMonth;
		
		BigDecimal bd = new BigDecimal(classMonthAttend*100);
		area.setForeground(Color.BLACK);
		area.setText(this.classType+" 月出勤率: "+ bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
	}
	
	public void calculateStudentMonth()
	{
		double actualMonth = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		currentDate = df.format(new Date());// new Date()为获取当前系统时间
		this.splitCurrentDate();
		Enumeration myEnumerate  = (Enumeration) stuIdDate_str.keys();
		
		int counter = 0;
		while(myEnumerate.hasMoreElements())
		{
			counter++;
			List<String> id_date = (List<String>) myEnumerate.nextElement();
			String id = id_date.get(0);
			String date = id_date.get(1);
			if(isValidDate(date, 1) == true)
			{
				actualMonth++;
			}	
		}
		double shouldMonth = Double.valueOf(current_day);
		double studentMonthAttend = actualMonth / shouldMonth;
		BigDecimal bd = new BigDecimal(studentMonthAttend*100);
		BigDecimal bd_attend = new BigDecimal(actualMonth);
		bd_attend.setScale(2, BigDecimal.ROUND_HALF_UP).intValue();
		if(studetExist)
		{
			area.setForeground(Color.BLACK);
			area.setText("本月出勤"+actualMonth+"天\n");
			area.setText(this.studentName+"\n本月出勤"+bd_attend+"天"+"\n月出勤率: "+ bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
		}
	}
	
	/*
	 * check if the date is valid or not
	 */
	public boolean isValidDate(String checkDate, int type)
	{
		boolean valid = false;
		List<String> checkList = splitDate(checkDate);
		String check_year = checkList.get(0);
		String check_month = checkList.get(1);
		String check_day = checkList.get(2);
		if(type == 0)//Class Day Attendence
		{
			if(check_year.equals(current_year) && check_month.equals(current_month) && check_day.equals(current_day))
			{
				valid = true;
			}
		}
		else if(type == 1)//Class Month Attendence
		{
			if(check_year.equals(current_year) &&  (Integer.valueOf(check_month)> Integer.valueOf(current_month)-1
					&& Integer.valueOf(check_month)< Integer.valueOf(current_month)+1 ))
			{
				valid = true;
			}
		}
		return valid;
	}
	
	public void splitCurrentDate()
	{
		List<String> searchList = splitDate(currentDate);
		current_year = searchList.get(0);
		current_month = searchList.get(1);
		current_day = searchList.get(2);
	}
	
	public List<String> splitDate(String date)
	{
		String[] dateSplit = date.split("-");
		String year = dateSplit[0];
		String month = dateSplit[1];
		String day = dateSplit[2];
		List<String> list = new LinkedList<String>();
		list.add(year);//0: add year
		list.add(month);//1: add month
		list.add(day);//2: add day
		return list;
	}
		
		
	
	public void calculateStuMonth()
	{
		
	}
	
	public void readLogin() throws SQLException, ClassNotFoundException
	{
		connect();
		PreparedStatement pstmt1;
		ResultSet rs1;
        pstmt1 = null;
        pstmt1 = conn.prepareStatement("SELECT * from emp_logginRecord where department = '"+this.classType +"';");
        rs1 = pstmt1.executeQuery();

        loginList = new LinkedList<String>();
        int count = 0;
        while(rs1.next())
        {
        	loginList.add(rs1.getString("id"));
        	count++;
        }
        this.actualTotal = count;
        pstmt1.close(); 
      	rs1.close();
      	conn.close();
	}
	
	public boolean isValidName(String name)
	{
		boolean valid = false;
		for(int i=0;i<allClassTypes.length;i++)
		{
			String one_class_name = allClassTypes[i];
			if(classType.equals(one_class_name))
			{
				valid = true;//valid name
				break;
			}
		}
		return valid;
	}
	
	public void readConfigFile() throws IOException
	{
    	Collection<String> deptss= EmployeeActionManager.getDepartmentList();
    	
    	deptss.remove("园长办公室");
    	deptss.remove("教工部");
    	deptss.remove("综合办公室");
	
		this.allClassTypes = new String[deptss.size()];
		int i=0;
		for(String dept:deptss)
		{
			allClassTypes[i++] = dept;
		}
	}
	
	/*
	 * Clear the TextArea
	 */
	public void clearInfoArea()
	{
		if(!area.getText().equals(""))
		{
			area.setText("");
		}
	}
	
	public void outputExcel(String excelFile) throws IOException, WriteException, ClassNotFoundException, SQLException
	{
		 File file = new File(excelFile);
		 if(!file.exists())
		 {
			 file.createNewFile();
		 }
        createExcel(file);
	}
	
	public void createExcel(File file) throws WriteException,IOException, ClassNotFoundException, SQLException{
        //try{
        	//创建工作薄
			WritableWorkbook workbook = Workbook.createWorkbook(file);
	
			 //创建新的一页
	        WritableSheet sheet = workbook.createSheet("User",0);
	        for(int i=0;i<31;i++)
	        {
	        	sheet.setColumnView(i+1,3);
	        }
	        
       //创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
	        
	        System.out.println("00000000000----");

	    WritableFont font0=new WritableFont(WritableFont.createFont("宋体"),20,WritableFont.NO_BOLD); 
        WritableFont font1=new WritableFont(WritableFont.createFont("宋体"),11,WritableFont.NO_BOLD); 
        font0.setColour(Colour.BLUE);
        font1.setColour(Colour.BLACK);
        WritableCellFormat format0=new WritableCellFormat(font0);
        WritableCellFormat format1=new WritableCellFormat(font1);

        format0.setAlignment(jxl.format.Alignment.CENTRE);
        format0.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        
        
        format1.setAlignment(jxl.format.Alignment.CENTRE);
        format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        
        Label title = new Label(16, 0, "出勤统计", format0);
        sheet.addCell(title);
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月");//设置日期格式
		currentDate = df.format(new Date());// new Date()为获取当前系统时间
        Label dateLabel = new Label(32, 0, this.currentDate, format1);
        sheet.addCell(dateLabel);
        
        Label name = new Label(0,1,"姓名", format1);//column 1
        sheet.addCell(name);
        for(int i=1; i<=31; i++)
        {
        	Label day = new Label(i, 1, String.valueOf(i), format1);
        	sheet.addCell(day);
        }
        Label attend = new Label(32, 1, "出勤", format1);//column 32
        sheet.addCell(attend);
        
        Label className = new Label(33, 1, "班级", format1);//column 32
        sheet.addCell(className);
        
        connect();
        PreparedStatement pstmt = null;
		ResultSet rs;
        pstmt = conn.prepareStatement("SELECT * from emp_id where security_level='LEVEL 5';");
        rs = pstmt.executeQuery();
        //List<String> all_names = new LinkedList<String>();
        List<String> all_id = new LinkedList<String>();
        Hashtable<String, String> id_name = new Hashtable<String, String>();
        Hashtable<String, String> name_department = new Hashtable<String, String>();
        while(rs.next())
        {
        	String one_id = rs.getString("id");
        	String one_name = rs.getString("name_company_address_book");
        	String department = rs.getString("department");
        	id_name.put(one_id, one_name);
        	all_id.add(one_id);
        	name_department.put(one_name, department);
        	//all_names.add(one_name);//add one name
        }
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		currentDate = df1.format(new Date());// new Date()为获取当前系统时间
		List<String> currDateSplit = splitDate(currentDate);
		String year_current = currDateSplit.get(0);
		String month_current = currDateSplit.get(1);
		String day_current = currDateSplit.get(2);
        PreparedStatement pstmt1 = null;
		ResultSet rs1;
		pstmt1 = conn.prepareStatement("SELECT * from emp_logginrecordall l join emp_id i on l.id=i.id where i.security_level='level 5' and l.L_date like '"+year_current+"-"+month_current+"%';" );
        System.err.println("SELECT * from emp_logginrecordall e join emp_id i on e.id=i.id where i.security_level='level 5' and e.L_date like '"+year_current+"-"+month_current+"';");
		rs1 = pstmt1.executeQuery();
        Hashtable<List<String>, String> idDate_str = new Hashtable<List<String>, String>();
	        while(rs1.next())
	        {
	        	String id = rs1.getString("id");
	        	String name_ = rs1.getString("L_name");
	        	String date = rs1.getString("L_date");
	        	List<String> one_record = new LinkedList<String>();
	        	one_record.add(id);//id
	        	one_record.add(date);//date
	        	idDate_str.put(one_record, "0");
	        }
	        Hashtable<String, String[]> id_days = new Hashtable<String, String[]>();
	        for(int i=0;i<all_id.size();i++)
	        {
	        	//String a_name = all_names.get(i);//one name
	        	String a_id = all_id.get(i);
	        	//String a_name = id_name.get(a_id);
	        	String[] a_dayArray = new String[31];
	        	for(int j=0;j<a_dayArray.length;j++)
	        	{
	        		a_dayArray[j] = "0";
	        	}
	        	id_days.put(a_id, a_dayArray);	
	        }
	        Enumeration enumeration = idDate_str.keys();
	        while(enumeration.hasMoreElements())
	        {
	        	List<String> id_date = (List<String>) enumeration.nextElement();
	        	String a_id = id_date.get(0);//name
	        	String date = id_date.get(1);//date
	        	List<String> split = splitDate(date);
	        	String year_record = split.get(0);//year
	        	String month_record = split.get(1);//month
	        	String day_record = split.get(2);//day
	        	int day_int = Integer.parseInt(day_record);
	        	String[] a_dayArray = new String[31];
	        	a_dayArray = id_days.get(a_id);
	        	/*
	            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	    		currentDate = df1.format(new Date());// new Date()为获取当前系统时间
	    		List<String> currDateSplit = splitDate(currentDate);
	    		String year_current = currDateSplit.get(0);
	    		String month_current = currDateSplit.get(1);
	    		String day_current = currDateSplit.get(2);
	    		*/
	    		if(year_record.equals(year_current) && month_record.equals(month_current))
	    		{
		        	a_dayArray[day_int-1] = "1";
	    		}
	        	id_days.put(a_id, a_dayArray);
	        }
	        for(int i=0;i<all_id.size();i++)
	        {
	        	//String a_name = all_names.get(i);
	        	String a_id = all_id.get(i);
	        	String a_name = id_name.get(a_id);
	        	Label a_name_label = new Label(0,i+1,a_name, format1);
	        	sheet.addCell(a_name_label);
	        	
	        	String a_department = name_department.get(a_name);
	        	Label a_department_label = new Label(33,i+1,a_department, format1);
	    	    sheet.addCell(a_department_label);
	        	String[] attendence_array = id_days.get(a_id);
	        	/*
	        	System.err.println("a_id: "+a_id);
	        	for(int m=0;m<attendence_array.length;m++)
	        	{
	        		System.out.print(attendence_array[m]+",");
	        	}*/
	        	//System.exit(1);
	        	int sum = 0;
	        	int j;
	        	for(j=0;j<attendence_array.length;j++)
	        	{
	        		String attend_status = attendence_array[j];
	        		String attend_label;
	        		
	        		if(attend_status.equals("1"))
	        		{
	        			attend_label = String.valueOf((char)8730);
	        		}
	        		else
	        		{
	        			attend_label = "";
	        		}
	        		Label a_attend = new Label(j+1, i+1, attend_label, format1);
	        		sheet.addCell(a_attend);
	        		int a_cell_value;
	        		if(sheet.getCell(j+1, i+1).getContents().equals(String.valueOf((char)8730)))
	        		{
	        			a_cell_value = 1;
	        		}
	        		else
	        		{
	        			a_cell_value = 0;
	        		}
	        		//int a_cell_value = Integer.parseInt(sheet.getCell(j+1, i+1).getContents());??
	        		sum += a_cell_value;
	        	}
	        	Label sumLabel = new Label(j+1, i+1, String.valueOf(sum), format1);
	        	sheet.addCell(sumLabel);
	        }
        //把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
       /*
        }catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "程序不能访问这个文件, 因为该文件正在被另一进程所占用, 请先关闭该文件.");
		}*/
    }	
	public void todayTotal() throws ClassNotFoundException, SQLException
	{
		 connect();
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			currentDate = df.format(new Date());
	        PreparedStatement pstmt = null;
			ResultSet rs;
	        pstmt = conn.prepareStatement("SELECT * from emp_logginrecordall where L_date='"+this.currentDate+"' and department<>'教工部' " +
	        		"and department<>'综合办公室' and department<>'园长办公室';");
	        rs = pstmt.executeQuery();
	        Hashtable<List<String>, String> idDate_str = new Hashtable<List<String>, String>();
	        while(rs.next())
	        {
	        	List<String> a_record = new LinkedList<String>();
	        	String id = rs.getString("id");
	        	String date = rs.getString("L_date");
	        	a_record.add(id);
	        	a_record.add(date);
	        	idDate_str.put(a_record, "0");
	        }
	        
	        PreparedStatement pstmt1 = null;
	        pstmt1 = conn.prepareStatement("select count(*) as count from emp_id where security_level='level 5';" );
	        ResultSet rs1 = pstmt1.executeQuery();
	        rs1.next();
	        String shouldTotal = rs1.getString(1);
	        todayTotal = idDate_str.size();
	        BigDecimal bd_today = new BigDecimal(todayTotal); 
	        int today = bd_today.setScale(2, BigDecimal.ROUND_HALF_UP).intValue();
	        area.setForeground(Color.BLACK);
	        
	        double attendenceRate = todayTotal/ Double.parseDouble(shouldTotal);//the attendence rate of doday
	        BigDecimal bd = new BigDecimal(attendenceRate*100);
	        area.setText("应到"+shouldTotal+"人, 今日实到"+today+"人\n"+
	        		"今日出勤率: "+bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
	}
	
	public void absenceStudents() throws ClassNotFoundException, SQLException
	{
		Connection conn = connect();	
		PreparedStatement pstmt, pstmt_original;
		ResultSet rs;
		pstmt = null;
		pstmt_original = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		currentDate = df.format(new Date());
		pstmt = conn.prepareStatement("SELECT * from emp_logginRecordall where L_date='"+currentDate+"' " +
				"and department<>'综合办公室' and department<>'教工部';");
        rs = pstmt.executeQuery();
        Hashtable<List<String>, String> nameDate_str_today = new Hashtable<List<String>, String>();
        int counter=0;
        while(rs.next())
        {
        	String name = rs.getString("L_name");
        	String date = rs.getString("L_date");
        	List<String> one_record = new LinkedList<String>();
        	one_record.add(name);
        	one_record.add(date);
        	nameDate_str_today.put(one_record, "0");
        	counter++;
        }
        pstmt_original = conn.prepareStatement("SELECT * from emp_id where security_level = 'level 5';");
        ResultSet rs_original = pstmt_original.executeQuery();
        List<String> list_original = new LinkedList<String>();
        Hashtable<String, String> name_department = new Hashtable<String, String>();
        while(rs_original.next())
        {
        	String name = rs_original.getString("name_company_address_book");
        	String department = rs_original.getString("department");
        	name_department.put(name, department);
        	if(!list_original.contains(name)){
            	list_original.add(name);	
        	}
        }
        Enumeration eumeration_today = nameDate_str_today.keys();
        List<String> list_today = new LinkedList<String>();
        while(eumeration_today.hasMoreElements())
        {
        	List<String> one = (List<String>) eumeration_today.nextElement();
        	String name = one.get(0);
        	if(!list_today.contains(name))
        	{
        	 	list_today.add(name);
        	}
        }
        String absenceNames = "今日缺勤名单:\n";
        int count=0;
        for(int i=0;i<list_original.size();i++)
        {
        	String a_name = list_original.get(i);
        	if(!list_today.contains(a_name))
        	{
        		count++;
        		absenceNames += String.valueOf(count)+". "+a_name+"  "+name_department.get(a_name)+"\n";
          	}
        }
        this.area.setText(absenceNames);
	}
}
