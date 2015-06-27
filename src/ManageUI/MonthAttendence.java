package ManageUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
//import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.format.Colour;
import java.util.Date;

public class MonthAttendence {
	public JPanel attendencePanel;
	public String currentDate;
	JLabel label_year;
	JTextField field_year;
	JLabel label_month;
	JTextField field_month;
	
	public MonthAttendence()
	{
		attendencePanel = new JPanel();
		attendencePanel.setLayout(new GridLayout(2,1));
		JPanel inputPanel = new JPanel();
		label_year = new JLabel("年");
		field_year = new JTextField(5);

		label_month = new JLabel("月");
		field_month = new JTextField(5);	
		inputPanel.add(label_year);
		inputPanel.add(field_year);
		inputPanel.add(label_month);
		inputPanel.add(field_month);
		attendencePanel.add(inputPanel);
		
		JButton outputButton = new JButton("输出当月总出勤");
		outputButton.addActionListener(new ActionListener(){

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
						field_year.setText("");
						field_month.setText("");
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
		attendencePanel.add(outputButton);
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
		//currentDate = df.format(new Date());// new Date()为获取当前系统时间
		String dateStr = field_year.getText()+"年"+this.field_month.getText()+"月";
        Label dateLabel = new Label(32, 0, dateStr, format1);
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
        
        Connection conn = connect();
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
        PreparedStatement pstmt1 = null;
		ResultSet rs1;
		
		String search_year = this.field_year.getText();
		String search_month = this.field_month.getText();
		/*
		if(isCurrentMonth(search_year, search_month))
		{
			??
		}*/
		String date_with_form = search_year+"-"+search_month;
		String date_with_form_next = nextDate();
		pstmt1 = conn.prepareStatement("SELECT l.* from emp_logginrecordall l join emp_id i on l.id = i.id where i.security_level='LEVEL 5' and l.L_date like '"+date_with_form+"%';");
		System.err.println( pstmt1);
		//pstmt1 = conn.prepareStatement("SELECT * from emp_logginrecordall where department <>'教工部' and " +
				//"department<>'综合办公室' and department<>'园长办公室' and department <> ''; ");
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
	        	System.out.println(a_id+"\t"+a_dayArray);
	        	id_days.put(a_id, a_dayArray);	
	        }
	        Enumeration enumeration = idDate_str.keys();
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    		
    		String year_current = this.field_year.getText();
    		String month_current = this.field_month.getText();
    		//String day_current = currDateSplit.get(2);
    		if(year_current.length()==0 || month_current.length()==0)
    		{
	    		currentDate = df1.format(new Date());// new Date()为获取当前系统时间
	    		List<String> currDateSplit = splitDate(currentDate);
	    		this.field_year.setText(currDateSplit.get(0));
	    		this.field_month.setText(currDateSplit.get(1));
    			return;
    		}
    		
    		
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
	        	
	        	System.out.println(a_id +"========="+ id_days.get(a_id));
	    		if(year_record.equals(year_current) && month_record.equals(month_current))
	    		{
	    			System.out.println("========="+day_int + "    ");
		        	a_dayArray[day_int-1] = "1";
	    		}
	    		//System.out.println(a_id);
	    		//System.out.println(a_dayArray);
	    		System.out.println("???"+a_id);
	    		System.err.println("???"+a_dayArray.length);
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
	        			System.err.println("--here---");
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
	
	public boolean isCurrentMonth(String year, String month)
	{
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM");//设置日期格式
		String currentDate = df1.format(new Date());// new Date()为获取当前系统时间
		String[] array = currentDate.split("-");
		String current_year = array[0];
		String current_month = array[1];
		if(year.equals(current_year)&& month.equals(current_month))
		{
			return true;
		}
		else
			return false;
	}
	
	public void createExcel_(File file) throws WriteException,IOException, ClassNotFoundException, SQLException{
        try{
        	//创建工作薄
			WritableWorkbook workbook = Workbook.createWorkbook(file);
	
			 //创建新的一页
	        WritableSheet sheet = workbook.createSheet("User",0);
	        for(int i=0;i<31;i++)
	        {
	        	sheet.setColumnView(i+1,3);
	        }
	        
       //创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容

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
        //SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月");//设置日期格式
		//currentDate = df.format(new Date());// new Date()为获取当前系统时间
		String dateStr = "";
		dateStr += field_year.getText()+"年"+this.field_month.getText()+"月";
        Label dateLabel = new Label(32, 0, dateStr, format1);
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
        
        Connection conn = connect();
        PreparedStatement pstmt = null;
		ResultSet rs;
        pstmt = conn.prepareStatement("SELECT * from emp_id where security_level='level 5';");
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
        }
        PreparedStatement pstmt1 = null;
		ResultSet rs1;
		
		String date_with_form = field_year.getText()+"-"+field_month.getText();
		String date_with_form_next = nextDate();
		pstmt1 = conn.prepareStatement("SELECT * from emp_logginrecordall where department <>'教工部' and " +
				"department<>'综合办公室' and department<>'园长办公室' and department <> '' and L_date>'"+date_with_form+"' and L_date<'"+date_with_form_next+"';");
		 //where L_date>'2014-10' and L_date<'2014-11'
        
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
	        	
	            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	    		currentDate = df1.format(new Date());// new Date()为获取当前系统时间
	    		List<String> currDateSplit = splitDate(currentDate);
	    		String year_current = currDateSplit.get(0);
	    		String month_current = currDateSplit.get(1);
	    		String day_current = currDateSplit.get(2);
	    		
	    		if(year_record.equals(year_current) && month_record.equals(month_current))
	    		{
		        	a_dayArray[day_int-1] = "1";
	    		}
	    		System.out.println(a_id);
	    		System.out.println(a_dayArray);
	        	id_days.put(a_id, a_dayArray);
	        }
	        for(int i=1;i<all_id.size();i++)
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
	        			System.err.println("--here---");
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
       
        }catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "程序不能访问这个文件, 因为该文件正在被另一进程所占用, 请先关闭该文件.");
		}
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
	
	public String nextDate()
	{
		String year = this.field_year.getText();
		String month = this.field_month.getText();
		String year_next = null;
		String month_next = null;
		if(Integer.parseInt(month) <= 11)
		{
			month_next = String.valueOf(Integer.parseInt(month)+1);
			year_next = year;
		}
		else if(Integer.parseInt(month) == 12)
		{
			month_next = String.valueOf(12);
			year_next = String.valueOf(Integer.parseInt(year)+1);
		}
		return year_next+"-"+month_next;
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
        	System.out.println("Succeeded connecting to the Database!");
        return conn;
	}
}
