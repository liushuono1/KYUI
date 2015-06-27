package ManageUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class MedcinePanel {
	public JPanel medicinePanel;
	public JTextField startF; 
	public JTextField endF;
	public Hashtable<String, String> id_name;
	public MedcinePanel()
	{
		medicinePanel = new JPanel();
		medicinePanel.setBorder(BorderFactory.createTitledBorder("服药记录"));
		JLabel startL = new JLabel("开始月份");
		startF = new JTextField(5);
		JLabel endL = new JLabel("结束月份");
		endF = new JTextField(5);
		JButton button = new JButton("导出");
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					connect();
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "xls");
					chooser.setFileFilter(filter);
					int option = chooser.showOpenDialog(null);
					if(option == JFileChooser.APPROVE_OPTION)
					{
						String path = chooser.getSelectedFile().getAbsolutePath();
						outputExcel_(path);
					}
				} catch (ClassNotFoundException e1) {
					
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		medicinePanel.add(startL);
		medicinePanel.add(startF);
		medicinePanel.add(endL);
		medicinePanel.add(endF);
		medicinePanel.add(button);
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
    
	public void outputExcel_(String excelFile)
	{
		 File file = new File(excelFile);
		 if(!file.exists())
		 {
			 try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
        createExcel(file);
	}

	public void createExcel(File file){
        try{
        	//创建工作薄
			WritableWorkbook workbook = Workbook.createWorkbook(file);
	
			 //创建新的一页
	        WritableSheet sheet = workbook.createSheet("User",0);
	        sheet.setColumnView(1,12);
	        sheet.setColumnView(3,12);
	        sheet.setColumnView(4,12);
	        sheet.setColumnView(5,80);
	        sheet.setColumnView(6,30);
	        
       //创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
	        WritableFont font0=new WritableFont(WritableFont.createFont("宋体"),20,WritableFont.NO_BOLD); 
	    WritableFont font00=new WritableFont(WritableFont.createFont("宋体"),12,WritableFont.NO_BOLD); 
        WritableFont font1=new WritableFont(WritableFont.createFont("宋体"),12,WritableFont.NO_BOLD);  
        WritableFont font_left=new WritableFont(WritableFont.createFont("宋体"),12,WritableFont.NO_BOLD); 
        font0.setColour(Colour.BLUE);
        font00.setColour(Colour.BLUE);
        font1.setColour(Colour.BLACK);
        WritableCellFormat format0=new WritableCellFormat(font0);
        WritableCellFormat format00=new WritableCellFormat(font00);
        WritableCellFormat format1=new WritableCellFormat(font1);
        WritableCellFormat format_left = new WritableCellFormat(font_left);
        
        format0.setAlignment(jxl.format.Alignment.CENTRE);
        format0.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        //format0.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
        format0.setBackground(jxl.format.Colour.YELLOW); // 设置单元格的背景颜色
         
        format00.setAlignment(jxl.format.Alignment.CENTRE);
        format00.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        format00.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
       
        format1.setAlignment(jxl.format.Alignment.CENTRE);
        format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        format1.setBorder(Border.RIGHT,BorderLineStyle.THIN,Colour.BLACK);
        //format1.setWrap(true);
        
        format_left.setAlignment(jxl.format.Alignment.CENTRE);
        format_left.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        format_left.setBorder(Border.ALL, BorderLineStyle.THIN,Colour.BLACK);
        
        Label title = new Label(5, 0, "开元国际幼儿园服药记录", format0);
        sheet.addCell(title);
        
        //Column * row
        Label order = new Label(0,1,"序号", format_left);//column 0
        sheet.addCell(order);
        
        Label id = new Label(1,1,"学号", format00);//column 3
        sheet.addCell(id);
        
        Label name = new Label(2,1,"姓名", format00);//column 3
        sheet.addCell(name);
        
        Label date = new Label(3,1,"日期", format00);//column 1
        sheet.addCell(date);
        
        Label time = new Label(4,1,"时间", format00);//column 2
        sheet.addCell(time);
        
        Label medicine = new Label(5,1,"药名", format00);//column 4
        sheet.addCell(medicine);
        
        Label reason = new Label(6,1,"病因", format00);//column 5
        sheet.addCell(reason);
        
        Label operator = new Label(7,1,"操作人", format00);//column 6
        sheet.addCell(operator);
        
        String startDate = startF.getText();
		String startDate_new = splitDate(startDate);
		String endDate = endF.getText();
		Connection conn = connect();
		PreparedStatement pstmt = null;
		ResultSet rs;
		String next_date;
		if(endDate.equals(""))//single-month
		{
			next_date = nextDate(startDate_new);
			pstmt = conn.prepareStatement("select * from emp_healthcheckdata where H_type='107' and H_date between '"+startDate_new+"' and '"+
					next_date+"';");
			
		}
		else//multiple-month
		{
			String endDate_new = splitDate(endDate);
			next_date = nextDate(endDate_new);
			pstmt = conn.prepareStatement("select * from emp_healthcheckdata where H_type='107' and H_date between '"+startDate_new+"' and '"+
					next_date+"';");
		}
		//pstmt = conn.prepareStatement("select * from emp_healthcheckdata where H_type='107' and H_date >='"+startDate_new+"' and  H_date <'"+endDate_new+"';"); 
		System.out.println("select * from emp_healthcheckdata where H_type='107' and H_date between '"+startDate_new+"' and '"+
				next_date+"';");
		rs = pstmt.executeQuery();
		getNames();
		int i=1;
		while(rs.next())//not empty
        {	
			Label a_order = new Label(0,i+1,String.valueOf(i), format1);//column 5
	        sheet.addCell(a_order);
	        
	        String studentID = rs.getString("id");//3,2
        	Label a_studentID = new Label(1,i+1,studentID, format1);//column 5
	        sheet.addCell(a_studentID);
	        
	        String studentName = id_name.get(studentID);
	        Label a_studentName = new Label(2,i+1,studentName, format1);//column 5
	        System.out.println("????"+studentID+"   "+studentName);
	        sheet.addCell(a_studentName);
        	
        	String dateValue = rs.getString("H_date");//1,2
        	Label a_date = new Label(3,i+1,dateValue, format1);//column 5
	        sheet.addCell(a_date);
        	
        	String timeValue = rs.getString("H_time");//2,2
        	Label a_time = new Label(4,i+1,timeValue, format1);//column 5
	        sheet.addCell(a_time);
        	
        	String medicalName = rs.getString("H_detail");//4,2
        	Label a_medicine = new Label(5,i+1,medicalName, format1);//column 5
	        sheet.addCell(a_medicine);
        	
        	String medicalReason = rs.getString("H_comment");//5,2
        	Label a_reason = new Label(6,i+1,medicalReason, format1);//column 5
	        sheet.addCell(a_reason);System.out.println("??????reason: "+medicalName);
        	
        	String operatorID = rs.getString("H_collectorid");//6,2
        	Label a_id = new Label(7,i+1,id_name.get(operatorID), format1);//column 5
	        sheet.addCell(a_id);
	        i++;//0,2
        }
		rs.close();
		pstmt.close();
		conn.close();
        //把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
       
        }catch(SQLException e)
		{
			JOptionPane.showMessageDialog(null, "程序不能访问这个文件, 因为该文件正在被另一进程所占用, 请先关闭该文件.");
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String splitDate(String date)
	{
		System.out.println(date+">>>>>>"+date.length()+"  "+(date.length()<6));
		if(date.length()<6)//the length is less than 6
		{
			JOptionPane.showMessageDialog(null, "请输入正确的6位数年月！");
		}
		else
		{
			for(int i=0;i<date.length();i++)
			{
				char ch = date.charAt(i);
				if(ch <48 || ch>57)
				{
					JOptionPane.showMessageDialog(null, "请输入合法的数字！");
					break;
				}
			}
		}
		String result="";
		int date_int = Integer.parseInt(date);//201410
		int quotient = date_int/100000;//2
		int remainer = date_int%100000;//01410
		result += quotient;
		
		quotient = remainer/10000;
		result += quotient;
		remainer = remainer%10000;
		
		quotient = remainer/1000;
		result += quotient;
		remainer = remainer%1000;
		
		quotient = remainer/100;
		result += quotient;
		remainer = remainer%100;
		result += "-";
		
		quotient = remainer/10;
		result += quotient;
		remainer = remainer%10;
		result += remainer;
		return result;
	}
	
	public String nextDate(String currentDate)
	{
		String[] split_str = currentDate.split("-");
		String current_year = split_str[0];
		String current_month = split_str[1];
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
	
	public void getNames() 
	{
		
		Connection conn;
		try {
			conn = connect();
		
			PreparedStatement pstmt = null;
			ResultSet rs;
			pstmt = conn.prepareStatement("select * from emp_id;");
			rs = pstmt.executeQuery();
			id_name = new Hashtable<String, String>();
			while(rs.next())
			{
				String id = rs.getString("id");
				String name = rs.getString("name_company_address_book");
				id_name.put(id, name);
			}
			
			rs.close();
			pstmt.close();
			conn.close();
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
