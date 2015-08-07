package Simple.HR;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import KYUI.KYMainUI;

public class SimpleNoFeesCount extends JFrame{
	
	public SimpleNoFeesCount()
	{
		String output="";
		try {
			Connection conn =KYMainUI.bds.getConnection();
			String input=(String) JOptionPane.showInputDialog(this,"请输入学生学号:");
			String date= (new java.sql.Date(System.currentTimeMillis())).toString();
			String year_month=date.split("-")[0]+"-"+date.split("-")[1];
			PreparedStatement p= conn.prepareStatement("select L_time,L_date,L_name from emp_logginrecordall where  id='"+input+"' and L_date like '"+year_month+"%"+"';");

			System.out.println(p);
			ResultSet rs=p.executeQuery();
			rs = p.executeQuery();
			while(rs.next())
			{
				output+=(rs.getString("L_name")+"\t"+rs.getString("L_date")+"\t"+rs.getString("L_time")+"\n");
			}
			System.out.println(output);
			rs.close();
			p.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.setTitle("本月出勤");
		this.setSize(400, 800);
		this.setLayout(new BorderLayout());
		JTextArea area = new JTextArea(output);
		area.setText(output);
		JScrollPane jsp=new JScrollPane(area);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.getContentPane().add(BorderLayout.CENTER,jsp);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		
		
	}
	
    public static void main(String[] args)
    {
    	new SimpleNoFeesCount();
    }
	
}
