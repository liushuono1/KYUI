package FinaceUI.Manege;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;


public class setStuRange {
	public JFrame frame;
	private JTable table;
	public List<String> classNameList;
	public JComboBox jb;
	public CheckboxGroup cbg;
	public JTextArea area;
	public Checkbox cb1;
	public Checkbox cb2;
	public Checkbox cb3;
	//public List<String> selectedClassList;
	public String studentList;
	public String[] selStudArray;
	public Hashtable<String, String> id_typeItemID;
	public Hashtable<String, String> type_price;
	public int retParameter;
	

	
	public setStuRange()
	{   
		this.classNameList = getClassList();
		frame = new JFrame("���÷�Χ");
		int a10 = Toolkit.getDefaultToolkit().getScreenSize().width; // ȡ����Ļ����
		int b10 = Toolkit.getDefaultToolkit().getScreenSize().height; // ȡ����Ļ���
		frame.setLocation((a10 - 1466) / 2, (b10 - 1000) / 2); // �趨λ�ã���Ļ���ģ�
		frame.setSize(500, 700); // �趨��С
		frame.setResizable(true); // �趨��������
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		JLabel label = new JLabel("��ѡ�����õ�ѧ����Χ");
		frame.add(BorderLayout.NORTH, label);
		frame.add(bodyPanel());
		frame.add(BorderLayout.SOUTH, btnPanel());
		frame.setVisible(true);
	}
	
	
	public Connection connect() throws ClassNotFoundException, SQLException
	{
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://192.168.1.100:3307/bb2_test";
		String user = "root"; 
		String password = "root";
        Class.forName(driver);
       // Connection conn = DriverManager.getConnection(url, user, password);
        //if(!conn.isClosed()) 
        	//System.out.println("Succeeded connecting to the Database!");
        	return setFees.connect();
	}
	
	public List<String> getClassList()
	  {
	   List<String> class_list = new LinkedList<String>();
	   try {
	    Connection conn = connect();
	    PreparedStatement p = null;
	    ResultSet r = null;
	    p = conn.prepareStatement("select distinct(department) from emp_id where security_level = 'level 5' OR security_level ='Level 4' ");
	    r = p.executeQuery();
	    while(r.next())
	    {
	     String className = r.getString("department");
	     class_list.add(className);
	    }
	    r.close();
	    p.close();
	   // conn.close();
	   } catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	   } catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	   }
	   return class_list;
	  }
	public JPanel bodyPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3,1));
		cbg = new CheckboxGroup(); 
		cb1 = new Checkbox("ȫ��ѧ��", cbg, true);
		cb2 = new Checkbox("ѡ��༶", cbg, false);
		cb3 = new Checkbox("��дѧ��", cbg, false);
				
		area = new JTextArea();
		
		JPanel allPanel = new JPanel();
		allPanel.setBorder(BorderFactory.createTitledBorder("ȫ��ѧ��"));
		allPanel.setLayout(new GridLayout(1,2));
		allPanel.add(cb1);allPanel.add(new JLabel(""));
		
		//panel.add(cb1); panel.add(new JLabel(""));
		JPanel classPanel = new JPanel();
		classPanel.setBorder(BorderFactory.createTitledBorder("���ְ༶ѧ��"));
		classPanel.setLayout(new GridLayout(1,2));
		classPanel.add(cb2); classPanel.add(getClassPanel());
		
		JPanel individualPanel = new JPanel();
		individualPanel.setBorder(BorderFactory.createTitledBorder("����ѧ��(����дѧ��,���÷ֺŷַָ�,��:KY2014102;KY2014103;)"));
		individualPanel.setLayout(new GridLayout(1,2));
		individualPanel.add(cb3); individualPanel.add(area);
		
		panel.add(allPanel);
		panel.add(classPanel);
		panel.add(individualPanel);
		System.out.println(cbg.getSelectedCheckbox().toString().split("label=")[1].split(",")[0]);
		return panel;
	}
	
	public JPanel getClassPanel()
	{
		JPanel classPanel = new JPanel();
		classPanel.setBorder(BorderFactory.createTitledBorder("ѡ��༶"));
		classPanel.setLayout(new GridLayout(3,1));
	
		JPanel itemPanel = new JPanel();
		JLabel item_label = new JLabel("�༶����");
		jb = new JComboBox();
		//List<String> classNames = getClassNames();
	
		
		for(int i=0;i<classNameList.size();i++)
		{
			jb.addItem(classNameList.get(i));
		}
		itemPanel.add(item_label);
		itemPanel.add(jb);
		
		JPanel btnPanel = new JPanel();
		Icon icon1 = new ImageIcon(getClass().getResource("down.jpg"));
		Icon icon2 = new ImageIcon(getClass().getResource("up.jpg"));
		JButton btn1 = new JButton(icon1);
		btn1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				downHandler();
			}
			
		});
		JButton btn2 = new JButton(icon2);
		btn2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				upHandler();
			}
			
		});
		btnPanel.add(btn1);
		btnPanel.add(btn2);
		
		JTable table = createTable();
		table.setSize(classPanel.getWidth(), classPanel.getHeight());
		
		classPanel.add(itemPanel);
		classPanel.add(btnPanel);
		classPanel.add(table);
		return classPanel; 
	}

	 public void downHandler()
	 {
		 String item = jb.getSelectedItem().toString();
		 if(!classNameList.contains(item))
		 {
			 classNameList.add(item);
		 }
		 table.setModel(this.SingleTableModel(classNameList));
	 }
	
	public void upHandler()
	{
		int row = table.getSelectedRow();
		classNameList.remove(row);
		table.setModel(SingleTableModel(classNameList));
	}
	
	public JTable createTable()
	{
		classNameList = new LinkedList<String>();
		table = new JTable(SingleTableModel(classNameList));
		return table;
	}
	
	 private DefaultTableModel SingleTableModel(List itemList)
	 {
		 int rows= itemList.size();
		 Object[][] x=  new Object[rows][1];
		 for(int i=0;i<rows;i++)
		 {
			 x[i][0]=itemList.get(i);
	     }	
		 return new DefaultTableModel(x, new String[]{"������1"});	
	 }
	
	public JPanel btnPanel()
	{
		JPanel panel = new JPanel();
		JButton btn1 = new JButton("ȷ��");
		btn1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				confirmHandler();
			}
			
		});
		JButton btn2 = new JButton("ȡ��");
		btn2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.dispose();
			}
			
		});
		panel.add(btn1);
		panel.add(btn2);
		return panel;
	}
	
	public void confirmHandler()
	{
		String selection = cbg.getSelectedCheckbox().toString().split("label=")[1].split(",")[0]; 
		System.err.println("!!!!!!!!selection = "+selection);	
		if(selection.equals("ȫ��ѧ��"))
		{
			retParameter = 0;
		}
		else if(selection.equals("ѡ��༶"))
		{
			retParameter = 1;
			JOptionPane.showMessageDialog(null, "ѡ��༶�Ĺ�����ʱ������");
		}
		else if(selection.equals("��дѧ��"))
		{
			retParameter = 2;
			String students = area.getText();
			selStudArray = students.split(";");
			for(int i=0;i<selStudArray.length;i++)
			{
				System.out.println(selStudArray[i]);
			}
		}
		System.out.println("retParameter = "+retParameter);
	}
	
	public static void main(String[] args)
	{
		//setStuRange test = new setStuRange();
	}

}
