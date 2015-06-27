package FinaceUI.Finace;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Hashtable;
//import java.util.Properties;
import java.util.LinkedList;


public class OneRecord {
	
	static private String[] attList =new String[]{"id","f_date","f_type","detail",
		"unit_price","amount","expense","income","account","comment","operator_id"
		,"operate_date","operate_time"};
	
	static public String[] showList =new String[]{"id","f_date","f_type","detail",
		"unit_price","amount","expense","income","account","comment"};
	
	Hashtable<String, Object> item_value =new Hashtable<String, Object>() ;
	private String id;
	private Date date;
	//private Time time;
	private String detail;
	private String type;
	private double unit_price;
	private double amount;
	private double expense;
	private double income;
	private String account;
	private String comment;
	private String operator_id;
	private Date op_date;
	private Time op_time;
	
	
	
	public OneRecord()
	{
		
		
	}
	
	public OneRecord(String input) //for testing 
	{
		this.setDetail(input);		
	}
	public OneRecord(Hashtable<String, Object> item_value)
	{
		this.item_value.putAll(item_value);
		id = (String) item_value.get("id");
		date =  (Date) item_value.get("f_date");
		type=(String) item_value.get("f_type");
		detail = (String) item_value.get("detail");
		try{
			unit_price= (Double) item_value.get("unit_price");
		}catch(Exception e)
		{
			unit_price=0;
		}
		try{
		amount=(Double) item_value.get("amount");
		}catch(Exception e)
		{
			amount=0;
		}
		
		try{
		expense=(Double) item_value.get("expense");
		}catch(Exception e)
		{
			expense=0;
		}
		
		try{
		income=(Double) item_value.get("income");
		}catch(Exception e)
		{
			income=0;
		}
		
		account =(String) item_value.get("account");
		comment = (String) item_value.get("comment");
		operator_id = (String) item_value.get("operator_id");
		op_date =  (Date) item_value.get("operate_date");
		op_time = (Time) item_value.get("operate_time");
	}
	
	public OneRecord(ResultSet item_value) throws SQLException
	{
		this.setID((String) item_value.getString("id"));
		this.setDate( (Date) item_value.getDate("f_date"));
		this.setType((String) item_value.getString("f_type"));
		this.setDetail((String) item_value.getString("detail"));
		this.setUnitPrice((double) item_value.getDouble("unit_price"));
		this.setAmount((double) item_value.getDouble("amount"));
		this.setExpense((double) item_value.getDouble("expense"));
		this.setIncome((double) item_value.getDouble("income"));
		this.setAccount((String) item_value.getString("account"));
		this.setComment((String) item_value.getString("comment"));
		this.setOperatorID((String) item_value.getString("operator_id"));
		this.setOperatDate((Date) item_value.getDate("operate_date"));
		this.setOperatTime(op_time = (Time) item_value.getTime("operate_time"));
	}
	
	public OneRecord(Date date, String type, String detail, double unitPrice, double amount, double expense, 
			double income, String account, String comment, String operator, Date opearateDate, Time operateTime)
	{
		
		this.setDate( date); 
		
		this.setType(type);
		
		this.setDetail( detail);
		
		this.setUnitPrice(unitPrice);
		
		this.setAmount( amount);

		this.setExpense(expense);

		this.setIncome( income);

		this.setAccount( account);

		this.setComment( comment);

		this.setOperatorID(operator);

		this.setOperatDate(opearateDate);

		this.setOperatTime(operateTime);

	}
	
	
	public void setValues(Date date, String type, String detail, double unitPrice, double amount, double expense, 
			double income, String account, String comment, String operator, Date opearateDate, Time operateTime)
	{
		
		this.setDate( date); 
		
		this.setType(type);
		
		this.setDetail( detail);
		
		this.setUnitPrice(unitPrice);
		
		this.setAmount( amount);

		this.setExpense(expense);

		this.setIncome( income);

		this.setAccount( account);

		this.setComment( comment);

		this.setOperatorID(operator);

		this.setOperatDate(opearateDate);

		this.setOperatTime(operateTime);

	}
	public String getString(int idx)
	{
		Object r = this.item_value.get(this.attList[idx]);
		if(r!=null)
			return r.toString();
		else
		{
			return "";
		}
	}
	
	
	
	public String getId()
	{
		return id;
	}
	
	public Date getDate()
	{
		
		return date;
	}
	

	
	public String getDetail()
	{
		
		return detail;
	}

	
	
	public String getComment()
	{
		return comment;
	}

	public String getType() {
		// TODO Auto-generated method stub
		return type;
	}

	public double getUnitPrice() {
		// TODO Auto-generated method stub
		return this.unit_price;
	}

	public double getAmount() {
		// TODO Auto-generated method stub
		return this.amount;
	}

	public double getExpense() {
		// TODO Auto-generated method stub
		return this.expense;
	}

	public double getIncome() {
		// TODO Auto-generated method stub
		return this.income;
	}

	public String getAccount() {
		// TODO Auto-generated method stub
		return this.account;
	}

	public String getOperatorID() {
		// TODO Auto-generated method stub
		return this.operator_id;
	}

	
	public String getOperateDate() {
		// TODO Auto-generated method stub
		return this.op_date.toString();
	}
	
	public String getOperateTime() {
		// TODO Auto-generated method stub
		return this.op_time.toString();
	}

	
	public void setID(String ID) {
		// TODO Auto-generated method stub
		this.id=ID;
		this.item_value.put("id", ID);
	}

	public void setDate(Date date2) {
		// TODO Auto-generated method stub
		this.date=date2;
		this.item_value.put("f_date", date2);
		
	}

	public void setType(String type2) {
		// TODO Auto-generated method stub
		this.type=type2;
		this.item_value.put("f_type", type2);
		
	}

	public void setDetail(String detail2) {
		// TODO Auto-generated method stub
		this.detail=detail2;
		this.item_value.put("detail", detail2);
		
	}

	public void setUnitPrice(double unitPrice) {
		// TODO Auto-generated method stub
		this.unit_price=unitPrice;
		this.item_value.put("unit_price", unitPrice);
		
	}

	public void setAmount(double amount2) {
		// TODO Auto-generated method stub
		this.amount=amount2;
		this.item_value.put("amount", amount2);
		
	}

	
	
	public void setExpense(double expense2) {
		// TODO Auto-generated method stub
		this.expense=expense2;
		this.item_value.put("expense", expense2);
		
	}

	public void setIncome(double income2) {
		// TODO Auto-generated method stub
		this.income=income2;
		this.item_value.put("income", income2);
		
	}

	public void setAccount(String account2) {
		// TODO Auto-generated method stub
		this.account=account2;
		this.item_value.put("account", account2);
		
	}

	public void setComment(String comment2) {
		// TODO Auto-generated method stub
		this.comment=comment2;
		this.item_value.put("comment", comment2);
	
		
	}

	public void setOperatorID(String operatorID) {
		// TODO Auto-generated method stub
		this.operator_id=operatorID;
		this.item_value.put("operator_id", operatorID);
		
	}

	public void setOperatDate(Date operateDate) {
		// TODO Auto-generated method stub
		this.op_date=operateDate;
		this.item_value.put("operator_date", operateDate);
		
	}
	

	public void setOperatTime(Time operateTime) {
		// TODO Auto-generated method stub
		this.op_time=operateTime;
		this.item_value.put("operator_time", operateTime);
		
	}
}
