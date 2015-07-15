package FinanceUI;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class FinanceUtil {
	private static FinaceActionServer fas = FinaceActionServer.getInstance();
	
	public static void insert(List<OneRecord> records,String operatorID)
	{
		fas.insert(records, operatorID, 0);
	}
	
	public static void update(List<OneRecord> item_values,String operatorID)
	{
		fas.insert(item_values, operatorID, 1);
	}
	
	public static void delete(List<OneRecord> item_value,String operatorID)
	{
		fas.insert(item_value, operatorID, 2);
	}
	
	public static Collection<OneRecord> searchByDetail(String input)
	{
		String[] constraintPair = {"detail","CONTAIN:"+input};	
		Collection<OneRecord> record_collection = fas.findRecordWithOneConstraint(constraintPair, 1);
		return record_collection;
	}
	
	public static Collection<OneRecord> searchByType(String input)
	{
		String[] constraintPair = {"f_type","CONTAIN:"+input};	
		Collection<OneRecord> record_collection = fas.findRecordWithOneConstraint(constraintPair, 1);
		return record_collection;
	}
	
	public static Collection<OneRecord> searchByDate(Date from, Date to)
	{
		System.err.println("from : "+from+" to: "+to);
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		String[] constraintPair = {"f_date","BETWEEN:"+s.format(from)+":"+s.format(to)};	
		Collection<OneRecord> record_collection = fas.findRecordWithOneConstraint(constraintPair,1);
		return record_collection;
	}
	
	public static Collection<OneRecord> searchByTypeDate(String input, Date from, Date to)
	{
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		List<String[]> constraintList = new LinkedList<String[]>();
		String[] constraintPair1 = {"f_type","CONTAIN:"+input};
		constraintList.add(constraintPair1);
		String[] constraintPair2 = {"f_date","BETWEEN:"+s.format(from)+":"+s.format(to)};
		constraintList.add(constraintPair2);
		Collection<OneRecord> record_collection = fas.findRecordWithManyConstraints(constraintList, 1);
		return record_collection;
	}
	
	public static Collection<OneRecord> getRecordCollection(String input)
	{
		String[] splitInput = input.split("\\年|\\月");//2014年1月
		String transform = "";
		String year = splitInput[0];
		String month = splitInput[1];
		if(month.length()<2)
		{
			month = "0"+month;
		}
		transform += year+"-";
		transform += month+"-";
		System.err.println("transform = "+transform);
		List<String[]> list = new LinkedList<String[]>();
		String[] constraintPair = {"f_date","CONTAIN:"+transform};	
		String[] constraintPair1 = {"f_type","<>:园内转账"}; 
		list.add(constraintPair);
		list.add(constraintPair1);
		Collection<OneRecord> record_collection = fas.findRecordWithManyConstraints(list, 1);
		return record_collection;
	}
	
	public static Collection<OneRecord> getRecordSoFar(String input)
	{
		String[] splitInput = input.split("\\年|\\月|\\日");//2014年12月31日
		String year = splitInput[0];
		String month = splitInput[1];
		int year_int = Integer.parseInt(year);
		int month_int = Integer.parseInt(month);
		if(month_int <12)
		{
			month_int += 1;
		}
		else
		{
			month_int = 1;
			year_int += 1;
		}
		String boundaryDate = String.valueOf(year_int)+"-"+String.valueOf(month_int)+"-1";
		List<String[]> list = new LinkedList<String[]>();
		String[] constraintPair = {"f_date","<:"+boundaryDate};	
		String[] constraintPair1 = {"f_type","<>:园内转账"}; 
		list.add(constraintPair);
		list.add(constraintPair1);
		System.out.println(list);
		Collection<OneRecord> record_collection = fas.findRecordWithManyConstraints(list, 1);
		return record_collection;
	}
	
	 public static Collection<OneRecord> getRecordsByAccount(String date,String account)
	 {
		 List<String[]>  constraints = new LinkedList<String[]> ();
		 String[] constraintPair0 = {"account","=:"+account}; 
		 constraints.add(constraintPair0);
		 String[] constraintPair1 = {"f_date","<=:"+date}; 
		 constraints.add(constraintPair1);
	   Collection<OneRecord> record_collection = fas.findRecordWithManyConstraints(constraints, 1);
	  return record_collection;
	 }
	
	public static Collection<OneRecord> getLastTenRecords()
	{
		Collection<OneRecord> record_collection = fas.findLastTenRecords(1);
		return record_collection;
	}
}
