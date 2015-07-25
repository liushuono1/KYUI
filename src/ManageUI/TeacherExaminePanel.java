package ManageUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class TeacherExaminePanel{
	public List<String> itemList;
	public JTextField field_item;
	public Hashtable<String, String> title_type;
	public JComboBox jb_item;
	public JComboBox jb_teacher;
	public Hashtable<String, String> name_id;
	public List<String> className_list;
	public Hashtable<String, String> id_name;
	public List<String> id_list;
	public JPanel examinePanel;
	public JTextField describeF;
	public Hashtable<String, String> type_score;
	public JTable table;
	public JPanel southPanel;
	public String[] attributeArray = new String[] {"工号","姓名","日期","考核项目","扣绩效点"};
	
	public TeacherExaminePanel()
	{
		getTeachers();
		examinePanel = new JPanel();
		examinePanel.setLayout(new BorderLayout());
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(4,2));
		southPanel = new JPanel();
		JLabel label = new JLabel("人员:");
		
		jb_teacher = new JComboBox();
		List<String> teachers = getTeacherName();
		for(int i=0;i<teachers.size();i++)
		{
			String a_className = teachers.get(i);
			jb_teacher.addItem(a_className);
		}
		northPanel.add(label);
		northPanel.add(jb_teacher);
		
		JPanel panel_item = new JPanel();
		panel_item.setLayout(new GridLayout(1,4));
		JLabel label_item = new JLabel("考核项目:");
		jb_item = new JComboBox();
		
		itemList = new LinkedList<String>();
		getItems();
		for(int i=0;i<itemList.size();i++)
		{
			String a_className = itemList.get(i);
			jb_item.addItem(a_className);
		}
		JButton btn_submit = new JButton("提交");
		btn_submit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				submitHandler();
			}
		});
		
		JButton btn_export = new JButton("查看");
		btn_export.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				confirmHandler();
			}
		});
		northPanel.add(label_item);
		northPanel.add(jb_item);
		northPanel.add(new JLabel("说明:"));
		describeF = new JTextField();
		
		northPanel.add(describeF);
		northPanel.add(btn_submit);
		northPanel.add(btn_export);
		examinePanel.add(BorderLayout.NORTH,northPanel);
		JScrollPane ScrollBtnPanel = new JScrollPane(createTable());
		ScrollBtnPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		ScrollBtnPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		southPanel.add(ScrollBtnPanel);
		examinePanel.add(BorderLayout.SOUTH,southPanel);
		examinePanel.setVisible(true);
	}

	public void confirmHandler()
	{
		Connection conn;
		try {
			conn = this.connect();
			PreparedStatement p = null;
			ResultSet r = null;
			p = conn.prepareStatement("select * from emp_teacherexamine;");
			r = p.executeQuery();
			List<List<String>> all_records = new LinkedList<List<String>>();
			while(r.next())
			{
				List<String> a_record = new LinkedList<String>();
				String id = r.getString("id");
				String name = id_name.get(id);
				Date date = r.getDate("e_date");
				String item = r.getString("e_title");
				
				String comment = r.getString("comment");
				String score = type_score.get(title_type.get(item));
				a_record.add(id);
				a_record.add(name);
				a_record.add(date.toString());
				a_record.add(item);
				a_record.add(score);
				all_records.add(a_record);
			}
			int row = all_records.size();
			int col = this.attributeArray.length;
			Object[][] records = new Object[row][col];
			for(int i=0;i<row;i++)
			{
				List<String> a_record = all_records.get(i);
				for(int j=0;j<col;j++)
				{
					records[i][j] = a_record.get(j);//row*column
			    }	
			}
			table.setModel(MutliTableModel(records, attributeArray));
			DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		    render.setHorizontalAlignment(SwingConstants.CENTER);
		    for(int i=0;i<this.attributeArray.length;i++)
		    {
			    table.getColumn(attributeArray[i]).setCellRenderer(render);
		    }
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public JTable createTable()
	{
		//attributeArray = {"工号","姓名","日期","时间","考核项目","扣绩效点"};
		//String attributeNames = txt2String(new File("attributes.txt"));
		//attributeArray = attributeNames.split(":");
		String[][] aaa = new String[1][attributeArray.length];
		table = new JTable(MutliTableModel(aaa,attributeArray));
		return table;
	}
	

	
	 private DefaultTableModel MutliTableModel(Object[][] items,String[] Attributes)
	 {
		 return new DefaultTableModel(items,Attributes);	
	 }
	
	public void submitHandler()
	{		
		Calendar c = Calendar.getInstance(); 
		SimpleDateFormat date=null; 
		date = new SimpleDateFormat("yyyy-MM-dd"); 
		String strDate = date.format(c.getTime()); 
		String strTime = c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
		
		String itemTitle = jb_item.getSelectedItem().toString();
		String itemType = title_type.get(itemTitle);
		String teacherName = jb_teacher.getSelectedItem().toString();
		String teacherID = name_id.get(teacherName);
		try {
			Connection conn = connect();
			PreparedStatement pstmt = null;
			if(className_list.contains(teacherName))
			{
				List<String> relatedTeachers = correspondingTeachers(teacherName);
				for(int i=0;i<relatedTeachers.size();i++)
				{
					String a_teacher = relatedTeachers.get(i);
					String a_id = id_name.get(a_teacher);
					pstmt = conn.prepareStatement("insert into emp_teacherExamine  values " +
							"('"+a_id+"','"+strDate+"','"+strTime+"','"+itemType+"','"+itemTitle+"','"+describeF.getText()+"');");
					pstmt.execute();
				}
			}
			else
			{
				pstmt = conn.prepareStatement("insert into emp_teacherExamine values " +
						"('"+teacherID+"','"+strDate+"','"+strTime+"','"+itemType+"','"+itemTitle+"','"+describeF.getText()+"');");
				pstmt.execute();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<String> correspondingTeachers(String className)
	{
		List<String> teachers = new LinkedList<String>();
		try {
			Connection conn = connect();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			pstmt = conn.prepareStatement("select * from emp_id where security_level = 'level 3' and job_id='"+className+"';");
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				String name = rs.getString("name_company_address_book");
				teachers.add(name);
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
		return teachers;
	}
	public void getItems()
	{
		title_type = new Hashtable<String, String>();
		type_score = new Hashtable<String, String>();
		Connection conn;
		try {
			conn = connect();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			pstmt = conn.prepareStatement("SELECT * from emp_examineItems;");
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				String title = rs.getString("type_title");
				String type = rs.getString("type_id");
				String score = rs.getString("type_score");
				itemList.add(title);
				title_type.put(title, type);
				type_score.put(type, score); 
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
	
	public void getTeachers()
	{
		Connection conn;
		try {
			conn = connect();
			PreparedStatement pstmt = null;
			ResultSet rs;
			pstmt = conn.prepareStatement("select * from emp_id where security_level = 'level 3' or security_level = 'level 1';");
			
			rs = pstmt.executeQuery();
			id_list = new LinkedList<String>();
			id_name = new Hashtable<String, String>();
			while(rs.next())
			{
				String a_id = rs.getString("id");
				String a_name = rs.getString("name_company_address_book");
				id_name.put(a_id, a_name);
				id_name.put(a_name, a_id);
				id_list.add(a_id);
			}
			rs.close();
			pstmt.close();
			conn.close();
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<String> getTeacherName()
	{
		List<String> name_list = new LinkedList<String>();
		className_list = new LinkedList<String>();
		name_id = new Hashtable<String, String>();
		try {
			Connection conn = connect();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			pstmt = conn.prepareStatement("SELECT * from emp_id where security_level='level 3' or security_level='level 1';");
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				String name = rs.getString("name_company_address_book");
				String id = rs.getString("id");
				name_id.put(name, id);
				name_list.add(name);
			}
			pstmt = conn.prepareStatement("select distinct(department) from emp_id where security_level = 'level 5';");
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				String className = rs.getString("department");
				name_list.add(className);
				className_list.add(className);
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
		return name_list;
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
	

	public void outputExcel(String excelFile) throws RowsExceededException, WriteException 
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
		 WritableWorkbook workbook;
		try {
			workbook = Workbook.createWorkbook(file);
			 //创建新的一页
		    WritableSheet sheet = workbook.createSheet("User",0);
		    Label title = new Label(2, 0, "教师考核");
	        sheet.addCell(title);
	        
	        Label id = new Label(0,1,"工号 ");//column 1
	        sheet.addCell(id);
		    
	        Label name = new Label(1,1,"姓名 ");//column 1
	        sheet.addCell(name);
	        
	        Label date = new Label(2,1,"日期 ");//column 1
	        sheet.addCell(date);
	        
	        Label time = new Label(3,1,"时间 ");//column 1
	        sheet.addCell(time);
	        
	        Label checkItem = new Label(4,1,"考核项目");//column 1
	        sheet.addCell(checkItem);
	        
	        Label score = new Label(5,1,"扣分 ");//column 1
	        sheet.addCell(score);
	        
	        try {
				Connection conn = connect();
				PreparedStatement pstmt = null;
				ResultSet rs;
				pstmt = conn.prepareStatement("select * from emp_id where security_level = 'level 3' or security_level = 'level 1';");
				
				rs = pstmt.executeQuery();
				List<String> id_list = new LinkedList<String>();
				id_name = new Hashtable<String, String>();
				while(rs.next())
				{
					String a_id = rs.getString("id");
					String a_name = rs.getString("name_company_address_book");
					id_name.put(a_id, a_name);
					id_name.put(a_name, a_id);
					id_list.add(a_id);
				}
				
				pstmt = conn.prepareStatement("select e.*, i.type_score from emp_teacherexamine e join emp_examineitems i on e.e_type = i.type_id;");
				rs = pstmt.executeQuery();
				//Hashtable<List<String>, List<String>> recordHash = new Hashtable<List<String>, List<String>>();
				List<List<String>> all_records = new LinkedList<List<String>>();
				int c=0;
				while(rs.next())
				{
					String one_id = rs.getString("id");//0
					String one_date = rs.getString("e_date");//1
					String one_time = rs.getString("e_time");//2
					String one_title = rs.getString("e_title");//3
					String one_score = rs.getString("type_score");//4
					List<String> a_record = new LinkedList<String>();
					a_record.add(one_id);//0:
					a_record.add(one_date);//1:
					a_record.add(one_time);//2:
					a_record.add(one_title);//3:
					a_record.add(one_score);//4:
					all_records.add(a_record);
					c++;
				}
				int count = 0;
				for(int i=0;i<id_list.size();i++)
				{
					String teacher_id = id_list.get(i);
					//count = 0;
					for(int j=0;j<all_records.size();j++)
					{
						List<String> one_record = all_records.get(j);
						if(teacher_id.equals(one_record.get(0)))
						{
							Label id_label = new Label(0,count+2,teacher_id);//column 1
					        sheet.addCell(id_label);
					        Label name_label = new Label(1,count+2,id_name.get(teacher_id));//column 1
					        sheet.addCell(name_label);
					        Label date_label = new Label(2,count+2, one_record.get(1));//column 1
					        sheet.addCell(date_label);
					        Label time_label = new Label(3,count+2, one_record.get(2));//column 1
					        sheet.addCell(time_label);
					        Label item_label = new Label(4,count+2, one_record.get(3));//column 1
					        sheet.addCell(item_label);
					        Label score_label = new Label(5,count+2, one_record.get(4));//column 1
					        sheet.addCell(score_label);
					        //all_records.remove(j);
							count++;
						}
					}
				}
				rs.close();
				pstmt.close();
				conn.close();
				/*
		        Enumeration enumerate = recordHash.keys();
		        while(enumerate.hasMoreElements())
		        {
		        	List<String> key = (List<String>) enumerate.nextElement();
		        	String teacher_id = key.get(0);//teacher_id
		        	String time = key.get(1);//time
		        	
		        	List<String> detail = recordHash.get(key);
		        	String item_title = detail.get(0);
		        	String item_score = detail.get(1);
		        	fd
		        }*/
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        workbook.write();
	        workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
