package FinanceUI;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class Record {
	private String id;
	private String date;
	private String type;
	private String detail;
	private String unitPrice;
	private String amount;
	private String expense;
	private String income;
	private String account;
	private String comment;
	private String operator;
	private String operateDate;
	private String operateTime;
	public List<String> itemList;
	
	public Record(Hashtable<String, String> key_values)
	{
		this.setDate(key_values.get("date"));
		this.setType(key_values.get("type"));
		this.setDetail(key_values.get("detail"));
		this.setDetail(key_values.get("unitPrice"));
		this.setDetail(key_values.get("amount"));
		this.setDetail(key_values.get("expense"));
		this.setDetail(key_values.get("income"));
		this.setDetail(key_values.get("account"));
		this.setDetail(key_values.get("comment"));
		this.setDetail(key_values.get("operator"));
		this.setDetail(key_values.get("operateDate"));
		this.setDetail(key_values.get("operateTime"));
	}
	
	public Record()
	{
		itemList = new LinkedList<String>();
	}
	
	
	public Record(String input)
	{
		itemList = new LinkedList<String>();
	}
	
	
	public Record(String date, String type, String detail, String unitPrice, String amount, String expense, 
			String income, String account, String comment, String operator, String opearateDate, String operateTime)
	{
		itemList = new LinkedList<String>();
		this.date = date; 
		itemList.add(date);
		this.type = type;
		itemList.add(type);
		this.detail = detail;
		itemList.add(detail);
		this.unitPrice = unitPrice;
		itemList.add(unitPrice);
		this.amount = amount;
		itemList.add(amount);
		this.expense = expense;
		itemList.add(expense);
		this.income = income;
		itemList.add(income);
		this.account = account;
		itemList.add(account);
		this.comment = comment;
		itemList.add(comment);
		this.operator = operator;
		itemList.add(operator);
		this.operateDate = opearateDate;
		itemList.add(operateDate);
		this.operateTime = operateTime;
		itemList.add(operateTime);
	}
	
	
	
	public void setValues(String date, String type, String detail, String unitPrice, String amount, String expense, 
			String income, String account, String comment, String operator, String opearateDate, String operateTime)
	{
		itemList = new LinkedList<String>();
		this.date = date; 
		itemList.add(date);
		this.type = type;
		itemList.add(type);
		this.detail = detail;
		itemList.add(detail);
		this.unitPrice = unitPrice;
		itemList.add(unitPrice);
		this.amount = amount;
		itemList.add(amount);
		this.expense = expense;
		itemList.add(expense);
		this.income = income;
		itemList.add(income);
		this.account = account;
		itemList.add(account);
		this.comment = comment;
		itemList.add(comment);
		this.operator = operator;
		itemList.add(operator);
		this.operateDate = opearateDate;
		itemList.add(operateDate);
		this.operateTime = operateTime;
		itemList.add(operateTime);
	}
	public Record(String date, String type, String detail, String unitPrice, String amount, String expense, 
			String income, String account, String comment)
	{
		itemList = new LinkedList<String>();
		this.date = date; 
		itemList.add(date);
		this.type = type;
		itemList.add(type);
		this.detail = detail;
		itemList.add(detail);
		this.unitPrice = unitPrice;
		itemList.add(unitPrice);
		this.amount = amount;
		itemList.add(amount);
		this.expense = expense;
		itemList.add(expense);
		this.income = income;
		itemList.add(income);
		this.account = account;
		itemList.add(account);
		this.comment = comment;
		itemList.add(comment);
	}
	public String toString()
	{
		String str = "";
		str += date+"\t"+type+"\t"+detail+"\t"+unitPrice+"\t"+amount+"\t"+expense+"\t"+income+"\t"+account+"\t"+comment+"\t"+operator+"\t"+operateDate
		+"\t"+operateTime;
		return str;
	}
	public int size()
	{
		return itemList.size();
	}

	public void setDate(String date)
	{
		this.date = date;
	}
	public String getDate()
	{
		return date;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getType()
	{
		return this.type;
	}
	
	public void setDetail(String detail)
	{
		this.detail = detail;
	}
	public String getDetail()
	{
		return this.detail;
	}
	
	public void setUnit(String unit)
	{
		this.unitPrice = unit;
	}
	public String getUnit()
	{
		return this.unitPrice;
	}
	
	public void setAmount(String amount)
	{
		this.amount = amount;
	}
	public String getAmount()
	{
		return this.amount;
	}
	

	public void setExpense(String expense)
	{
		this.expense = expense;
	}
	public String getExpense()
	{
		return this.expense;
	}
	
	public void setIncome(String income)
	{
		this.income = income;
	}
	public String getIncome()
	{
		return this.income;
	}
	
	public void setAccount(String account)
	{
		this.account = account;
	}
	public String getAccount()
	{
		return this.account;
	}
	
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	public String getComment()
	{
		return this.comment;
	}
	
	public void setOperator(String operator)
	{
		this.operator = operator;
	}
	public String getOperator()
	{
		return this.operator;
	}
	
	public void setOperateDate(String operateD)
	{
		this.operateDate = operateD;
	}
	public String getOperateDate()
	{
		return this.operateDate;
	}
	
	public void setOperateTime(String operateT)
	{
		this.operateTime = operateT;
	}
	public String getOperateTime()
	{
		return this.operateTime;
	}
	
}
