package FinaceUI.Finace;

import java.sql.Date;
import java.sql.Time;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class FinanceUtil {
	private static FinaceActionServer fas = FinaceActionServer.getInstance();
	
	public static void insert(List<Hashtable<String, Object>> item_values,String operatorID)
	{
		List<OneRecord> records =new LinkedList<OneRecord>();
		for(Hashtable<String, Object> item:item_values)
		{
			OneRecord record = new OneRecord(item);
			records.add(record);
		}
		
		fas.insert(records, operatorID, 0);
	}
	
	public static void update(List<Hashtable<String, Object>> item_values,String operatorID)
	{
		List<OneRecord> records =new LinkedList<OneRecord>();
		for(Hashtable<String, Object> item:item_values)
		{
			OneRecord record = new OneRecord(item);
			records.add(record);
		}
	
		fas.insert(records, operatorID, 1);
	}
	
	public static void delete(Hashtable<String, Object> item_value,String operatorID)
	{
		List<OneRecord> records =new LinkedList<OneRecord>();
		OneRecord record = new OneRecord(item_value);
		records.add(record);
		fas.insert(records, operatorID, 2);
	}
	
	public static void main(String[] avgs)
	{
		Hashtable<String, Object> aa = new Hashtable<String, Object>();
		aa.put("detail", "11");
		aa.put("income", 102.3);
		List<Hashtable<String, Object>> it =new LinkedList();
		it.add(aa);
		insert(it,"KY00000000");
		
	}
}
