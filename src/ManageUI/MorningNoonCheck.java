package ManageUI;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;



public class MorningNoonCheck {
	public JTextField field_begin;
	public JComboBox jb;
	public JTextField field_end;
	private String passDate="";
	public int shouldCount;
	public int actualCount;
	String className ;
	public JFrame f;
	public JPanel panel;
	
	public MorningNoonCheck()
	{
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("晨检、午检记录"));
		panel.setLayout(new GridLayout(3,1));
		
		JPanel classPanel = new JPanel();
		classPanel.setLayout(new GridLayout(1,2));
		JLabel classLabel = new JLabel("选择班级");
		jb = new JComboBox();
		//jb.addItem("");
		List<String> allClassTypes = getClassNames();
		for(int i=0;i<allClassTypes.size();i++)
		{
			String a_className = allClassTypes.get(i); 
			jb.addItem(a_className);
		}
		
		JTextField classField = new JTextField(5);
		classField.setText("中班");
		classPanel.add(classLabel);
		classPanel.add(jb);
		
		panel.add(classPanel);
		panel.add(DatePanel());
	}
	
	public JPanel DatePanel()
	{

		JPanel datePanel = new JPanel();
		//datePanel.setLayout(new GridLayout(1,4));
		JLabel startLabel = new JLabel("从");
		JButton startbtn = new JButton("日期");
		startbtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//getDate(0);//begin button
				newFrame(field_begin);
			}
		});
		field_begin = new JTextField(5);
		field_begin.setName("BEGIN");
		field_begin.setEditable(false);
		//this.pop.buildCalendarPanel(year, month);
		
		datePanel.add(startLabel);
		datePanel.add(startbtn);
		datePanel.add(field_begin);
		
		JLabel endLabel = new JLabel("到");
		JButton endbtn = new JButton("日期");
		endbtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//getDate(1);//end button
				newFrame(field_end);
				className = jb.getSelectedItem().toString();
			}
		});
		field_end = new JTextField(5);
		field_end.setName("END");
		field_end.setEditable(false);
		final JButton exportBtn = new JButton("导出");
		exportBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "xls");
				chooser.setFileFilter(filter);
				int option = chooser.showOpenDialog(null);
				if(option == JFileChooser.APPROVE_OPTION)
				{
					String path = chooser.getSelectedFile().getAbsolutePath();
					System.err.println("path:"+path);
					outputExcel(path);
				}
				//frame.dispose();
			}
		});
		datePanel.add(endLabel);
		datePanel.add(endbtn);
		datePanel.add(field_end);
		datePanel.add(exportBtn);
		return datePanel;
	}
	
	public void newFrame(JTextField settedField)
	{
		JPanel menu = null; 
		f = new JFrame("日历");
		//if(count == 0)
		{ 
			//System.out.println("counter = "+counter);
			//counter++;
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
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			//Continue = true;
		}
		///return menu;
	}
	
	public List<String> getClassNames()
	{
		List<String> classNames = new LinkedList<String>();
		try {
			Connection conn = connect();
			PreparedStatement pstmt = null;
			ResultSet rs;
			pstmt = conn.prepareStatement("select distinct department from emp_id where department<>'综合办公室' and department<>'教工部' and department<>'园长办公室';");
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				classNames.add(rs.getString("department"));
			}
			rs.close();
			pstmt.close();
			conn.close();
			//return classNames;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return classNames;
	}
	
	public void outputExcel(String excelFile) 
	{
		try {
			Connection conn=connect();
	    	String end = this.field_end.getText();
	    	String begin = this.field_begin.getText();
	    	//System.out.println("Output :" +begin +"  "+ end +"  "+ className );		
		 File file = new File(excelFile);
		 if(!file.exists())
		 {
			 file.createNewFile();
		 }
			//创建工作薄
			WritableWorkbook workbook = Workbook.createWorkbook(file);
	
			 //创建新的一页
	        WritableSheet sheet = workbook.createSheet("User",0);
	        sheet.setFooter( "" ,  "" ,  "0" );  
	        sheet.setColumnView(0,15);
	        sheet.setColumnView(1,16);
	        sheet.setColumnView(2,15);
	        sheet.setColumnView(3,15);
	        sheet.setColumnView(4,15);
	        sheet.setColumnView(5,16);

	       //sheet.setRowView(0, 600,false);

	        sheet.getSettings().setRightMargin( 0.55 ); 
	        sheet.getSettings().setLeftMargin( 0.55);
	      int i=0;
	      List<String> dateList = new LinkedList<String>();
	      if(validDate(begin, end))
			{
				String currentDate = begin;
				while(validDate(currentDate, end))
				{ 
					//System.err.println("currentDate = "+currentDate+"   end = "+end+"  "+validDate(currentDate, end));
					dateList.add(currentDate);
					currentDate = nextDate1(currentDate);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "请选择正确的日期");
			}
	      	System.out.println(dateList);
	      	List<String> dateList_new = offDate(dateList);
	      
	        for(i=0;i<dateList_new.size();i++)
	        {
	        	String date = dateList_new.get(i);
	       
	        		createExcel(i,date, sheet);
	   
	     
	        }
	      	System.err.println(dateList_new);
	      	//System.exit(1);
	        workbook.write();
	        workbook.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 public Connection connect() throws ClassNotFoundException, SQLException
		{
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://192.168.1.100:3307/bb2_test";
			String user = "root"; 
			String password = "root";
	        Class.forName(driver);
	        Connection conn = DriverManager.getConnection(url, user, password);
	        if(!conn.isClosed()) 
	        {
	        	//System.out.println("Succeeded connecting to the Database!");
	        }
	        return conn;
		}
	 
		public String nextDate1(String specifiedDay){ 
			Calendar c = Calendar.getInstance(); 
			Date date=null; 
			try { 
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay); 
			} catch (ParseException e) { 
			e.printStackTrace(); 
			} 
			c.setTime(date); 
			int day=c.get(Calendar.DATE); 
			c.set(Calendar.DATE,day+1); 
			String dayAfter=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()); 
			//System.err.println(specifiedDay+"   "+dayAfter);
			return dayAfter; 
			} 
		
		public boolean validDate(String begin, String end)
		{
			boolean flag = true;
			String[] array1 = begin.split("-");
			String[] array2 = end.split("-");
			int year1 = Integer.valueOf(array1[0]);
			int month1 = Integer.valueOf(array1[1]);
			int day1 = Integer.valueOf(array1[2]);
			
			int year2 = Integer.valueOf(array2[0]);
			int month2 = Integer.valueOf(array2[1]);
			int day2 = Integer.valueOf(array2[2]);
			
			if(year1 > year2)
			{
				flag = false;
			}
			else if(year1 == year2)
			{
				if(month1 > month2)
				{
					flag = false;
				}
				else if(month1 == month2)
				{
					if(day1 > day2)
					{
						flag = false;	
					}
				}
			}
			return flag;
		}
		
		public List<String> offDate(List<String> dateList)
		{
			List<String> new_list = new LinkedList<String>();
			boolean flag = false;
			try {
				Connection conn = connect();
				PreparedStatement pstmt = null;
				ResultSet rs;
				int counter;
				for(int i=0;i<dateList.size();i++)
				{
					counter = 0;
					String date = dateList.get(i);
					pstmt = conn.prepareStatement("SELECT * from emp_logginrecordall where L_date='"+date+"';");
					rs = pstmt.executeQuery();
					int count=0;
					while(rs.next())
					{
						count++;
					}
					if(count == 0)
					{
						flag = true;
					}
					if(!flag)
					{
						new_list.add(date);
					}
					flag = false;
				}
				pstmt.close();
				conn.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new_list;
		}
		
		public void createExcel(int page, String Date, WritableSheet sheet){
			int rowsperpage=53;
			System.err.println("Date = "+Date);
	        try{	        
		    //字体
		     WritableFont NormalFont  =   new  WritableFont(WritableFont.ARIAL,  12 );    
		     WritableFont tableFont  =   new  WritableFont(WritableFont.ARIAL,  12 , WritableFont.NO_BOLD);  
		    
		    //for the title of table
		     WritableCellFormat wcf_tabletitle  =   new  WritableCellFormat(tableFont);  
		     wcf_tabletitle.setBorder(Border.ALL, BorderLineStyle.MEDIUM);  //  线条   
		     wcf_tabletitle.setVerticalAlignment(VerticalAlignment.CENTRE);  //  垂直对齐   
		     wcf_tabletitle.setAlignment(Alignment.CENTRE);  //  水平对齐      
		        
		     WritableCellFormat format_left  =   new  WritableCellFormat(tableFont); 
		     WritableCellFormat format_right  =   new  WritableCellFormat(tableFont); 
		     format_left.setBorder(Border.LEFT, BorderLineStyle.MEDIUM);
		     format_right.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM);
		     
		      
		    WritableFont font23=new WritableFont(WritableFont.createFont("宋体"),23,WritableFont.NO_BOLD); 
		    WritableFont font18=new WritableFont(WritableFont.createFont("宋体"),18,WritableFont.NO_BOLD); 
		   // WritableFont upFont  =   new  WritableFont(WritableFont.ARIAL,  12 , WritableFont.BOLD);
		    WritableFont font00=new WritableFont(WritableFont.createFont("宋体"),12,WritableFont.NO_BOLD); 
	        WritableFont font_date=new WritableFont(WritableFont.createFont("宋体"),18,WritableFont.NO_BOLD);   
	        
	        font23.setColour(Colour.BLUE);
	        
	        font_date.setColour(Colour.BLACK);
	        WritableCellFormat format23 = new WritableCellFormat(font23);
	        WritableCellFormat format18 = new WritableCellFormat(font18);
	        WritableCellFormat format00 = new WritableCellFormat(font00);
	        WritableCellFormat format_date = new WritableCellFormat(font_date);
	        
	        WritableCellFormat format_top = new WritableCellFormat(NormalFont);
	        format_top.setBorder(Border.TOP, BorderLineStyle.MEDIUM);
	        
	        WritableCellFormat format_Normal = new WritableCellFormat(NormalFont);
	        
	        format18.setAlignment(jxl.format.Alignment.CENTRE);
	        format18.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	        format18.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
	        format18.setWrap(true);
	        format23.setAlignment(jxl.format.Alignment.LEFT);
	        format23.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	        
	         
	        format00.setAlignment(jxl.format.Alignment.CENTRE);
	        format00.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	        format00.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
	        format00.setWrap(true);
	        
	        format_date.setAlignment(jxl.format.Alignment.LEFT);
	        
	        format_left.setAlignment(jxl.format.Alignment.CENTRE);
	        format_left.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	       
	        search(Date);
	        
	        Label title = new Label(1, rowsperpage*page + 0, "开元国际幼儿园晨检、午检记录", format23);
	        sheet.addCell(title);
	        
	        //Column * row
	        Label Class = new Label(0,rowsperpage*page + 1,"班级:", format_date);//column 0
	        sheet.addCell(Class);
	        Label ClassN = new Label(1,rowsperpage*page + 1,className, format_date);//column 0
	        sheet.addCell(ClassN);
	        
	        Label number = new Label(1,rowsperpage*page + 2,"全班人数", format18);//column 3
	        sheet.addCell(number);
	        Label shouldNumber = new Label(2,rowsperpage*page + 2, String.valueOf(shouldCount), format18);//column 3
	        sheet.addCell(shouldNumber);
	        
	        Label actual = new Label(3,rowsperpage*page + 2,"实到人数", format18);//column 3
	        sheet.addCell(actual);
	        Label actualNumber = new Label(4,rowsperpage*page + 2, String.valueOf(actualCount), format18);//column 3
	        sheet.addCell(actualNumber);

	       List<String> morningTeacher = new LinkedList<String>();
	       
	        Label temperatureTitle = new Label(1,rowsperpage*page + 5,"幼儿体温", format18);//************************************101
	        sheet.addCell(temperatureTitle);
	        List<List<String>> tempStuff = check(1);
	        for(int i=0;i<tempStuff.size();i++)
	        {
	        	WritableCellFormat format = null;
	        	if(i ==0 )
	        	{
	        		format = format_top;
	        	}
	        	else
	        	{
	        		format = format_Normal;
	        	}
	        	
	        		List<String> one = tempStuff.get(i);
	        		String info = one.get(0)+"  "+one.get(1)+"  "+one.get(2);
	        	    Label name = new Label(2,rowsperpage*page+5+i,info, format);//column 1
	                sheet.addCell(name);
	                String teacher_id = one.get(3);
	                if(!morningTeacher.contains(teacher_id))
	                {
	                	morningTeacher.add(teacher_id);
	                }
	        }
	        
	        Label mental = new Label(1,rowsperpage*page + 12,"精神状态", format18);//column 1
	        sheet.addCell(mental);
	        
	        List<List<String>> mentalStuff = check(2);
	        for(int i=0;i<mentalStuff.size();i++)
	        {
	        	WritableCellFormat format = null;
	        	if(i ==0 )
	        	{
	        		format = format_top;
	        	}
	        	else
	        	{
	        		format = format_Normal;
	        	}
	        		List<String> one = mentalStuff.get(i);	
	        		String info = one.get(0)+"  "+one.get(1)+"  "+one.get(2);
	        		
	        	    Label name = new Label(2,rowsperpage*page + 12+i,info, format);//column 1
	        	    sheet.addCell(name);
	        	    String teacher_id = one.get(3);
	                if(!morningTeacher.contains(teacher_id))
	                {
	                	morningTeacher.add(teacher_id);
	                }
	        }

	        Label danger = new Label(1,rowsperpage*page + 18,"有  无\r\n带异物", format18);//column 1
	        sheet.addCell(danger);

	        List<List<String>> dangerStuff = check(3);
	       
	        for(int i=0;i<dangerStuff.size();i++)
	        {
	        	WritableCellFormat format = null;
	        	if(i ==0 )
	        	{
	        		format = format_top;
	        	}
	        	else
	        	{
	        		format = format_Normal;
	        	}
	        	//if(i<6)
	        	{
	        		List<String> one = dangerStuff.get(i);		
	        		String info = one.get(0)+"  "+one.get(1)+"  "+one.get(2);
	        		
	        	    Label name = new Label(2,rowsperpage*page + 18+i,info, format);//column 1
	                sheet.addCell(name);
	                String teacher_id = one.get(3);
	                if(!morningTeacher.contains(teacher_id))
	                {
	                	morningTeacher.add(teacher_id);
	                }
	        	}
	        }
	        	Label teacher = new Label(5,rowsperpage*page + 2,"晨检教师", format18);//column 6
	            sheet.addCell(teacher);
	            String morningTeacherInfo = "";
	            for(int i=0;i<morningTeacher.size();i++)
	            {
	            	morningTeacherInfo += morningTeacher.get(i)+"\r\n";
	            }
	            Label teacher_name = new Label(5,rowsperpage*page + 5,morningTeacherInfo, format00);//************************************101
	            sheet.addCell(teacher_name); 
	            
	        
	        
	        Label all = new Label(1,rowsperpage*page + 24,"全班人数", format18);//column 1
	        sheet.addCell(all);
	        Label all_should = new Label(2,rowsperpage*page + 24,String.valueOf(shouldCount), format18);//column 1
	        sheet.addCell(all_should);
	        
	        Label abscence = new Label(3,rowsperpage*page + 24,"有无缺勤", format18);//column 1
	        sheet.addCell(abscence);
	        String abscent;
	        if(shouldCount == actualCount)
	        {
	        	abscent = "无";
	        }
	        else
	        {
	        	abscent = "有";
	        }
	        Label abscenceOrNot = new Label(4,rowsperpage*page + 24,abscent, format18);//column 1
	        sheet.addCell(abscenceOrNot);
	        
	        Label sleep = new Label(1,rowsperpage*page + 27,"幼  儿\r\n睡眠情况", format18);//column 1
	        sheet.addCell(sleep);
	        
	        List<String> noonTeacherList = new LinkedList<String>(); 
	       
	        List<List<String>> sleepStuff = check(4);
	        for(int i=0;i<sleepStuff.size();i++)
	        {
	        	WritableCellFormat format = null;
	        	if(i ==0 )
	        	{
	        		format = format_top;
	        	}
	        	else
	        	{
	        		format = format_Normal;
	        	}
	        	//if(i<3)
	        	{
	        		List<String> one = sleepStuff.get(i);
	        		String info = one.get(0)+"  "+one.get(1)+"  "+one.get(2);
	        	    Label name = new Label(2,rowsperpage*page + 27+i,info, format);//column 1
	                sheet.addCell(name);
	                String teacher_id = one.get(3);
	                if(!noonTeacherList.contains(teacher_id))
	                {
	                	noonTeacherList.add(teacher_id);
	                }
	        	}
	        }
	        Label body = new Label(1,rowsperpage*page + 34,"幼  儿\r\n身体状况", format18);//column 1
	        sheet.addCell(body);
	        
	        List<List<String>> physicalStuff = check(5);
	        for(int i=0;i<physicalStuff.size();i++)
	        {
	        	WritableCellFormat format = null;
	        	if(i ==0 )
	        	{
	        		format = format_top;
	        	}
	        	else
	        	{
	        		format = format_Normal;
	        	}
	        	//if(i<3)
	        	{
	        		List<String> one = physicalStuff.get(i);
	        		String info = one.get(0)+"  "+one.get(1)+"  "+one.get(2);
	        	    Label name = new Label(2,rowsperpage*page + 34+i,info, format);//column 1
	                sheet.addCell(name);
	                String teacher_id = one.get(3);
	                if(!noonTeacherList.contains(teacher_id))
	                {
	                	noonTeacherList.add(teacher_id);
	                }
	        	}
	        }
	        
	        Label date = new Label(4,rowsperpage*page + 1,"日期: "+Date, format_date);//column 6
	        sheet.addCell(date);
	        
	     
	        // 合并单元格,注意mergeCells(col0,row0,col1,row1) --列从0开始,col1为你要合并到第几列,行也一样   
	      //  sheet.mergeCells( 1 , 3 ,  1 ,  7 );  
	         temperatureTitle = new Label(1,rowsperpage*page + 3,"幼儿体温", format18);//************************************101
	        sheet.addCell(temperatureTitle);
	        
	        Label morningCheck = new Label(0,rowsperpage*page + 2,"晨\r\n\r\n检", format18);//************************************101
	        sheet.addCell(morningCheck);
	        Label noonCheck = new Label(0,rowsperpage*page + 24,"午\r\n\r\n检", format18);//************************************101
	        sheet.addCell(noonCheck); 
	        
	        Label event = new Label(0,rowsperpage*page + 40,"突发\r\n事件", format18);//************************************101
	        sheet.addCell(event); 
	        Label event_detail = new Label(1,rowsperpage*page + 40,"", format18);//************************************101
	        sheet.addCell(event_detail);
	        
	        Label comment = new Label(0,rowsperpage*page + 45,"其它", format18);//************************************101
	        sheet.addCell(comment); 
	        Label comment_detail = new Label(1,rowsperpage*page + 45,"", format18);//************************************101
	        sheet.addCell(comment_detail); 
	        
	        //if(true){
	        sheet.mergeCells(1, rowsperpage*page +45, 5, rowsperpage*page +49);
	        sheet.mergeCells(1, rowsperpage*page +40, 5, rowsperpage*page +44);
	        sheet.mergeCells(5, rowsperpage*page +27, 5, rowsperpage*page +39);  
	        sheet.mergeCells(5, rowsperpage*page +24, 5, rowsperpage*page +26);
	        sheet.mergeCells(1, rowsperpage*page +34, 1, rowsperpage*page +39);
	        sheet.mergeCells(1, rowsperpage*page +27, 1, rowsperpage*page +33);
	        sheet.mergeCells(4, rowsperpage*page +24, 4, rowsperpage*page +26);
	        sheet.mergeCells(3, rowsperpage*page +24, 3, rowsperpage*page +26);
	        sheet.mergeCells(2, rowsperpage*page +24, 2, rowsperpage*page +26);
	        sheet.mergeCells(1, rowsperpage*page +24, 1, rowsperpage*page +26);
	        sheet.mergeCells(5, rowsperpage*page +5, 5, rowsperpage*page +23);
	        sheet.mergeCells(1, rowsperpage*page +18, 1, rowsperpage*page +23);
	        sheet.mergeCells(1, rowsperpage*page +12, 1, rowsperpage*page +17);
	        sheet.mergeCells(1, rowsperpage*page +5, 1, rowsperpage*page +11);
	        sheet.mergeCells(5, rowsperpage*page +2, 5, rowsperpage*page +4);
	        sheet.mergeCells(4, rowsperpage*page +2, 4, rowsperpage*page +4);
	        sheet.mergeCells(3, rowsperpage*page +2, 3, rowsperpage*page +4);
	        sheet.mergeCells(2, rowsperpage*page +2, 2, rowsperpage*page +4);
	        sheet.mergeCells(1, rowsperpage*page +2, 1, rowsperpage*page +4);

	        sheet.mergeCells(0, rowsperpage*page +45, 0, rowsperpage*page +49);
	        sheet.mergeCells(0, rowsperpage*page +40, 0, rowsperpage*page +44);
	        sheet.mergeCells(0, rowsperpage*page +24, 0, rowsperpage*page +39);
	        sheet.mergeCells( 0 , rowsperpage*page +2 ,  0 ,  rowsperpage*page +23);         
	        for(int i= 5;i<24;i++)
	        {
	        	 sheet.mergeCells(2, rowsperpage*page +i, 4, rowsperpage*page +i);
	        }
	        for(int i= 27;i<40;i++)
	        {
	        	 sheet.mergeCells(2, rowsperpage*page +i, 4, rowsperpage*page +i);
	        }
	        

	        Label onduty = new Label(5,rowsperpage*page + 24,"值班教师", format18);//column 6
	        sheet.addCell(onduty);
	        
	        String noonTeacherInfo = "";
	        for(int i=0;i<noonTeacherList.size();i++)
	        {
	        	noonTeacherInfo += noonTeacherList.get(i)+"\n";
	        }
	        Label onduty_name = new Label(5,rowsperpage*page + 27,noonTeacherInfo, format00);//column 6
	        sheet.addCell(onduty_name);

	        
	        //把创建的内容写入到输出流中，并关闭输出流
	        //workbook.write();
	        //workbook.close();
	       
	        }catch(SQLException e)
			{
				JOptionPane.showMessageDialog(null, "程序不能访问这个文件, 因为该文件正在被另一进程所占用, 请先关闭该文件.");
			} catch (RowsExceededException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public List<List<String>> check(int type) throws SQLException
		{
			List<List<String>> allItems = new LinkedList<List<String>>();
			Connection conn;
			try {
				conn = connect();
			
			PreparedStatement pstmt = null;
			ResultSet rs;
			switch(type)
			{
				case 1:
					pstmt = conn.prepareStatement("select i.name_company_address_book, h.H_time, h.H_detail, h.H_collectorid from emp_healthcheckdata h join emp_id i on h.id= i.id where h.H_type='101' " +
							" and h.H_date='"+this.passDate+"';");
					break;
				case 2:
					pstmt = conn.prepareStatement("select i.name_company_address_book, h.H_time, h.H_detail, h.H_collectorid from emp_healthcheckdata h join emp_id i on h.id= i.id where h.H_type='102' " +
							" and h.H_date='"+this.passDate+"';");
					break;
				case 3:
					pstmt = conn.prepareStatement("select i.name_company_address_book, h.H_time, h.H_detail, h.H_collectorid from emp_healthcheckdata h join emp_id i on h.id= i.id where h.H_type='103' " +
							" and h.H_date='"+this.passDate+"';");
					break;
				case 4:
					pstmt = conn.prepareStatement("select i.name_company_address_book, h.H_time, h.H_detail, h.H_collectorid from emp_healthcheckdata h join emp_id i on h.id= i.id where h.H_type='104' " +
							" and h.H_date='"+this.passDate+"';");
					break;
				case 5:
					pstmt = conn.prepareStatement("select i.name_company_address_book, h.H_time, h.H_detail, h.H_collectorid from emp_healthcheckdata h join emp_id i on h.id= i.id where h.H_type='105' " +
							" and h.H_date='"+this.passDate+"';");
					break;
				default:;
			}
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				List<String> one_piece = new LinkedList<String>();
				String name = rs.getString("name_company_address_book");
				String time = rs.getString("H_time");
				String detail = rs.getString("H_detail");
				String teacher_id = rs.getString("H_collectorid");
				one_piece.add(name);
				one_piece.add(time);
				one_piece.add(detail);
				one_piece.add(teacher_id);
				allItems.add(one_piece);
			}
			rs.close();
			pstmt.close();
			conn.close();
			
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return allItems;
		}
		
		public void getDatefromCalender(String Date,JTextField settedText)
		{
			this.passDate = Date;
			settedText.setText(passDate);
			//if(settedText.getName().equals("END"))
				//outputExcel("d://study//a.xls");
			f.dispose();
		}
		
		public int search(String passedDate) throws  ClassNotFoundException
		{
			try {
				Connection conn = connect();
				PreparedStatement pstmt = null;
				ResultSet rs;
		        pstmt = conn.prepareStatement("SELECT * from emp_id where department = "+"'"+className+"' and security_level='level 5';");
		        rs = pstmt.executeQuery();
		        shouldCount = 0;
		        while(rs.next())
		        {
		        	shouldCount++;//should
		        }
		        System.out.println("pass date = "+this.passDate);
		        pstmt = conn.prepareStatement("SELECT * from emp_logginrecordall where department = "+"'"+className+"' and L_date = '"+passedDate+"';");
		        //System.out.println("SELECT * from emp_logginrecordall where department = "+"'"+className+"' and L_date = '"+passDate+"';");
		        rs = pstmt.executeQuery();
		        Hashtable<List<String>, Integer> student_count = new Hashtable<List<String>, Integer>(); 
		        while(rs.next())
		        {
		        	String id = rs.getString("id");
		        	String date = rs.getString("L_date");
		        	List<String> a_student = new LinkedList<String>();
		        	a_student.add(id);
		        	a_student.add(date);
		        	
		        	student_count.put(a_student, 1);
		        }
		        actualCount = student_count.size();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return 0;
		}
}
