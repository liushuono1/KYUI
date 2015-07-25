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
			PreparedStatement p= conn.prepareStatement("select distinct(P_date) from emp_charge_refund_pay where charge <>'' order by P_date desc limit 10;");
			//p.setString(2, KYMainUI.department);

			ResultSet rs = p.executeQuery();
			rs.last();
			int count =rs.getRow();
			rs.beforeFirst();
			String[] dates =new String[count];
			int i=0;
			while(rs.next())
			{
				dates[i++]=rs.getString("P_date");
			}
			
			String input=(String) JOptionPane.showInputDialog(this,"请选择本月的第一个计费日期：","选择起始日",JOptionPane.INFORMATION_MESSAGE
					,null,dates,"");
			
			
			p= conn.prepareStatement("select department,emp_charge_refund_pay.id,emp_id.name_company_address_book,emp_charge_refund_pay.charge from emp_charge_refund_pay join emp_id on emp_charge_refund_pay.id =emp_id.id where P_date>=? and  charge<>'' and  emp_charge_refund_pay.id not in (select id from emp_charge_refund_pay where P_date>=? and (pay<>'' and pay <>'0')) order by department;");
			p.setString(1, input);
			p.setString(2, input);
			System.out.println(p);
			rs = p.executeQuery();
			while(rs.next())
			{
				output+=(rs.getString("department")+"\t"+rs.getString("id")+"\t"+rs.getString("name_company_address_book")+"\t"+rs.getString("charge")+"\n");
				
			}
			System.out.println(output);
			rs.close();
			p.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.setTitle("未缴费名单");
		this.setSize(400, 800);
		this.setLayout(new BorderLayout());
		JTextArea area = new JTextArea(output);
		area.setText(output);
		JScrollPane jsp=new JScrollPane(area);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.getContentPane().add(BorderLayout.CENTER,jsp);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		
		
	}
	
}
