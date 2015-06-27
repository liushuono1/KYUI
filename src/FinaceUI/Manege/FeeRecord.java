package FinaceUI.Manege;
import java.sql.Date;
import java.sql.Time;
import java.util.Hashtable;
//import java.util.Properties;


public class FeeRecord {
	private Hashtable<String, Object> item_value;
	public String id;
	private Date date;
	private Time time;
	private String detail;
	private String refund;
	private String charge;
	private String pay;
	private String comment;
	
	public FeeRecord(Hashtable<String, Object> item_value)
	{
		this.item_value = item_value;
	}
	
	public String getId()
	{
		id = (String) item_value.get("id");
		return id;
	}
	
	public Date getDate()
	{
		date =  (Date) item_value.get("date");
		return date;
	}
	
	public Time getTime()
	{
		time = (Time) item_value.get("time");
		return time;
	}
	
	public String getDetail()
	{
		detail = (String) item_value.get("detail");
		return detail;
	}

	public String getRefund()
	{
		refund = (String) item_value.get("refund");
		return refund;
	}
	
	public String getCharge()
	{
		charge = (String) item_value.get("charge");
		return charge;
	}
	
	public String getPay()
	{
		pay = (String) item_value.get("pay");
		return pay;
	}
	
	public String getComment()
	{
		comment = (String) item_value.get("pay");
		return comment;
	}
}
