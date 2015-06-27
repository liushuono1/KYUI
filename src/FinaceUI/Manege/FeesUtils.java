package FinaceUI.Manege;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.LinkedList;
import java.util.List;

import FinaceUI.Manege.setFees.one_fee;



public class FeesUtils {
	
	public static void chargeRegularFee(Connection conn, String id, double sum,String ChargeCode)
	{

		try {
			PreparedStatement p = null;
			ResultSet r = null;
			p = conn.prepareStatement("select * from emp_charge_refund_pay where id = '"+id+"';");
			r = p.executeQuery();
			List<List> all_payItems = new LinkedList<List>();
			while(r.next())
			{
				List<String> a_payItem = new LinkedList<String>();
				String date = r.getString("P_date");
				String chargeItems = r.getString("detail");//P_typeTitle
				a_payItem.add(date);
				a_payItem.add(chargeItems);
				all_payItems.add(a_payItem);
			}
			java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
	
			String dateStr = currentDate.toString();
			
			List<String> regularItem = new LinkedList<String>();
			regularItem.add(dateStr);//date
			regularItem.add(ChargeCode);//item title
			if(!all_payItems.contains(regularItem))//check whether this "charge item" has been inserted into database or not
			{
				p = conn.prepareStatement("insert into emp_charge_refund_pay values (?,?,?,?,?,?,?,?);");
				Time currentTime = new Time(System.currentTimeMillis());
				p.setString(1, id);
				p.setDate(2, currentDate);
				p.setTime(3, currentTime);
				p.setString(4, ChargeCode);
				p.setString(5, "");
				p.setString(6, String.valueOf(sum));
				p.setString(7, "");
				p.setString(8, "");
				//p.setString(9, "");
				System.out.println(p);
				p.execute();
			}
			r.close();
			p.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
