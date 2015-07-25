package FinanceUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.sql.Date;

import KYUI.KYMainUI;


/**
 * @version 1.0
 * @created 06-四月-2015 14:19:08
 */
public class FinaceActionServer {
	static FinaceActionServer Instance =null;
	public FinaceActionServer(){
		List<String[]> constraintList = new LinkedList<String[]>();
		
		//String[] constraint = {"金额","200"};
		//constraintList.add(constraint);
		
		//String[] constraint1 = {"detail","contain:押金 "};
		//constraintList.add(constraint1);
		
		//Collection<OneRecord> coll = findRecordWithManyConstraints(constraintList, 1);
		//for(OneRecord one:coll)
		//{
		//	System.out.println(one);
		//}
	}
	
	/**
	 * get a db connection
	 */
	public Connection Connection() throws ClassNotFoundException, SQLException
	{
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://"+KYMainUI.SQLIP+":3307/bb2_"+KYMainUI.DBStr;
		String user = "root"; 
		String password = "root";
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, user, password);
        //if(!conn.isClosed()) 
        	//System.out.println("Succeeded connecting to the Database!");
        	return conn;
	}

	/**
	 * 
	 * @param constraintPair
	 */
	public Collection<OneRecord> findRecordWithOneConstraint(String[] constraintPair,int status){		
		Collection<OneRecord> collectionRecord = new LinkedList<OneRecord>();
		try {
			Connection conn = Connection();
			PreparedStatement p = null;
			ResultSet r = null;
			
			p = conn.prepareStatement("select * from emp_finance where "+constraintPair[0]+ buildQueryStr(constraintPair[1]) +" order by f_date,id,operate_date,operate_time asc;");
			r = p.executeQuery();
			Hashtable<Integer, OneRecord> id_record  = new Hashtable<Integer, OneRecord>();
			List<Integer> id_order = new LinkedList<Integer>();
			while(r.next())
			{
				OneRecord a_record = new OneRecord(r);
				String id = a_record.getId();

				if(status == 0)
				{
					collectionRecord.add(a_record);
				}
				
				if(a_record.getMark().length()==9)//(earlierThan(id_record.get(Integer.parseInt(id)),a_record))
				{
					if(!id_order.contains(Integer.parseInt(id)))
					{
						//System.err.println("id = "+id+"\t"+a_record);
						id_order.add(Integer.parseInt(id));
					}else
					{
						id_order.remove(id_order.indexOf(Integer.parseInt(id)));
						id_order.add(Integer.parseInt(id));
					}
					id_record.put(Integer.parseInt(id), a_record);
				}
				   
			}
			if(status == 1)
			{
				for(int i=0;i<id_order.size();i++)
				{
					OneRecord re = id_record.get(id_order.get(i));
					if(!re.getMark().contains("2") && re.getMark().length()==9)
					   collectionRecord.add(re);
				}
			}
			r.close();
			p.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return collectionRecord;
	}
	
	public void sort(List<Integer> list)
	{
		int temp;
		for(int i=0;i<list.size()-1;i++)
		{
			for(int j=i+1;j<list.size();j++)
			{
				if(list.get(i) > list.get(j))
				{
					temp =  list.get(j);
					list.set(j, list.get(i));
					list.set(i,  temp);
				}
			}
		}
	}
	
	public void sortd(List<Integer> list)
	{
		int temp;
		for(int i=0;i<list.size()-1;i++)
		{
			for(int j=i+1;j<list.size();j++)
			{
				if(list.get(i) < list.get(j))
				{
					temp =  list.get(j);
					list.set(j, list.get(i));
					list.set(i,  temp);
				}
			}
		}
	}

	private String buildQueryStr(String str) {
		System.err.println("str: "+str);
		String[] searchStrs = str.split(":");
		String ret=null;
		if(searchStrs[0].toUpperCase().equals("BETWEEN"))
		{
			ret=" BETWEEN '"+searchStrs[1]+"' AND '"+searchStrs[2]+"' ";
		}else if(searchStrs[0].toUpperCase().equals("CONTAIN"))
		{
			ret=" LIKE '%"+searchStrs[1]+"%' ";
		}else if(searchStrs[0].toUpperCase().equals("STARTWITH"))
		{
			ret=" LIKE '"+searchStrs[1]+"%' ";
		}else if(searchStrs[0].toUpperCase().equals("ENDWITH"))
		{
			ret=" LIKE '%"+searchStrs[1]+"' ";
		}else
		{
			System.out.println(searchStrs[0]);
			System.out.println(searchStrs[1]);
			ret=" "+searchStrs[0]+"'"+searchStrs[1]+"' ";
		}
		return ret;
	}
	
	private String buildMutliQueryStr(List<String[]> constraintList) {
		String ret ="";
		
		for(String[] onePair : constraintList )
		{  
			if(!ret.equals(""))
				ret+="AND ";
			ret+=onePair[0]+buildQueryStr(onePair[1]);
		}
		return ret;
	}
	
	public Collection<OneRecord> findLastTenRecords(int status)
	{
		Collection<OneRecord> collectionRecord = new LinkedList<OneRecord>();
		try {
			Connection conn = Connection();
			PreparedStatement p = null;
			ResultSet r = null;
			String sqlStatement = "select * from emp_finance order by f_date desc,id desc,operate_date asc,operate_time asc limit 100;";
			p = conn.prepareStatement(sqlStatement);
			r = p.executeQuery();
			
			Hashtable<Integer, OneRecord> id_record  = new Hashtable<Integer, OneRecord>();
			List<Integer> id_order = new LinkedList<Integer>();
			while(r.next())
			{
				OneRecord a_record = new OneRecord(r);
				String id = a_record.getId();
				
				
				if(status == 0)
				{
					collectionRecord.add(a_record);
				}
				
				
				if(a_record.getMark().length()==9)//(earlierThan(id_record.get(Integer.parseInt(id)),a_record))
				{
					if(!id_order.contains(Integer.parseInt(id)))
					{
						//System.err.println("id = "+id+"\t"+a_record);
						id_order.add(Integer.parseInt(id));
					}else
					{
						id_order.remove(id_order.indexOf(Integer.parseInt(id)));
						id_order.add(Integer.parseInt(id));
					}
					id_record.put(Integer.parseInt(id), a_record);
				}
			}
			if(status == 1)
			{
			
				for(int i=0;i<id_order.size();i++)
				{
					
					OneRecord re = id_record.get(id_order.get(i));
					if(!re.getMark().contains("2")&& re.getMark().length()==9)
						collectionRecord.add(re);
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return collectionRecord;
	}

	/**
	 * 
	 * @param constraintList
	 */
	public Collection<OneRecord> findRecordWithManyConstraints(List<String[]> constraintList, int status){
		Collection<OneRecord> collectionRecord = new LinkedList<OneRecord>();
		try {
			Connection conn = this.Connection();
			PreparedStatement p = null;
			ResultSet r = null;
			String sqlStatement = "select * from emp_finance where ";
			sqlStatement +=this.buildMutliQueryStr(constraintList)+" order by f_date, id, operate_date, operate_time asc;";
			System.err.println(sqlStatement);
			p = conn.prepareStatement(sqlStatement);
			r = p.executeQuery();
			
			Hashtable<Integer, OneRecord> id_record  = new Hashtable<Integer, OneRecord>();
			List<Integer> id_order = new LinkedList<Integer>();
			while(r.next())
			{
				OneRecord a_record = new OneRecord(r);
				String id = a_record.getId();
				
				if(status == 0)
				{
					collectionRecord.add(a_record);
				}


				if(a_record.getMark().length()==9)//(earlierThan(id_record.get(Integer.parseInt(id)),a_record))
				{
					if(!id_order.contains(Integer.parseInt(id)))
					{
						System.err.println("id = "+id+"\t"+a_record);
						id_order.add(Integer.parseInt(id));
					}else
					{
						id_order.remove(id_order.indexOf(Integer.parseInt(id)));
						id_order.add(Integer.parseInt(id));
					}
					id_record.put(Integer.parseInt(id), a_record);
				}
			}
			if(status == 1)
			{
				for(int i=0;i<id_order.size();i++)
				{
					OneRecord re = id_record.get(id_order.get(i));
					if(!re.getMark().contains("2")&& re.getMark().length()==9)
						collectionRecord.add(re);
					
				}
			}
			r.close();
			p.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return collectionRecord;
	}
	
	public double getTotalBalance()
	{
		double profit = 0;
		try {
			Connection conn = this.Connection();
			PreparedStatement p = null;
			ResultSet r = null;
			p = conn.prepareStatement("select * from emp_finance where ;");
			//System.out.println("select * from emp_finance;");
			r = p.executeQuery();
			Hashtable<Integer, OneRecord> id_record = new Hashtable<Integer, OneRecord>();
			int count = 0;
			while(r.next())
			{	
				count++;
				OneRecord a_record = new OneRecord(r);
				//System.out.println(a_record);	
				String id = a_record.getId();
				//System.out.println("id: "+id+"\t"+r.getString("id")+"\t"+count);
				
				if(a_record.getMark().length()==9)//(earlierThan(id_record.get(Integer.parseInt(id)),a_record))
				{
					id_record.put(Integer.parseInt(id), a_record);
				}			
			}
			List<Integer> id_rank = new LinkedList<Integer>();
			
			id_rank.addAll(id_record.keySet());
			sort(id_rank);
			double sum_income = 0;
			double sum_expense = 0;
			for(int i=0;i<id_rank.size();i++)
			{
				OneRecord re = id_record.get(id_rank.get(i));
				double income = re.getIncome();
				double expense = re.getExpense();
				if(income != 0)
				{
					sum_income += income;
				}
				else if(expense != 0)
				{
					sum_expense += expense;
				}
			}
			profit = sum_income - sum_expense;
			r.close();
			p.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return profit;
	}

	/**
	 * general insert into datbase
	 * 
	 * @param record
	 * @param operatorId
	 * @param type: 0:normal inset; 1:update;  2:delete
	 */
	public int insert(Collection<OneRecord> records, String operatorId, int type){
		try {
			System.err.println("------------------execute here---------------------");
			Connection conn = Connection();
			PreparedStatement p = null;
			
			//------------------------get id;
			int id=1;
			p = conn.prepareStatement("SELECT max(CAST(id as SIGNED))  from emp_finance;");
			ResultSet rs =p.executeQuery();
			if(rs.next())
			{
				id=rs.getInt(1)+1;
			}
			
			
			if(type == 0)
			{
				for(OneRecord record:records)
				{
					p = conn.prepareStatement("INSERT INTO emp_finance "
							/*+"('id','f_date','f_time','f_type','detail', 'unit_price','amount','expense','income','account','comment','operator_id','operate_date','operate_time')"*/
							+"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
					String Id = String.valueOf(id);//1
					Date date = record.getDate();//2
					String itemType = record.getType();//3
					String detail = record.getDetail();//4
					double unitPrice = record.getUnitPrice();//5
					double amount = record.getAmount();//6
					double expense = record.getExpense();//7
					double income = record.getIncome();//8
					String account = record.getAccount();//9
					String comment = record.getComment();//10
					String operatorID = operatorId;//11
					Date currentDate = new Date(System.currentTimeMillis());//12
					Time currentTime = new Time(System.currentTimeMillis());//13
					String mark = record.getMark();//14
					
					p.setString(1, Id);
					p.setDate(2, date);
					p.setString(3, itemType);
					p.setString(4, detail);
					p.setDouble(5, unitPrice);
					p.setDouble(6, amount);
					p.setDouble(7, expense);
					p.setDouble(8, income);
					p.setString(9, account);
					p.setString(10, comment);
					p.setString(11, operatorID);
					p.setDate(12, currentDate);
					p.setTime(13, currentTime);
					p.setString(14, mark);
					System.out.println(p);
					p.execute();
					id++;
				}
			}
			else//update & delete
			{
				for(OneRecord record:records)
				{
					p = conn.prepareStatement("UPDATE emp_finance set mark=concat(mark,'1')where id=? AND LENGTH(mark)=9;");
					String Id = record.getId();//1
					p.setString(1, Id);
					p.execute();    // 被改动过的记录mark变为9位 ，正常查询不会被显示
					
					p = conn.prepareStatement("INSERT INTO emp_finance "
							/*+"('id','f_date','f_time','f_type','detail', 'unit_price','amount','expense','income','account','comment','operator_id','operate_date','operate_time')"*/
							+"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
					Id = record.getId();//1
					Date date = record.getDate();//2
					String itemType = record.getType();//3
					String detail = record.getDetail();//4
					double unitPrice = record.getUnitPrice();//5
					double amount = record.getAmount();//6
					double expense = record.getExpense();//7
					double income = record.getIncome();//8
					String account = record.getAccount();//9
					String comment = record.getComment();//10
					String operatorID = operatorId;//11
					Date currentDate = new Date(System.currentTimeMillis());//12
					Time currentTime = new Time(System.currentTimeMillis());//13
					String mark = record.getMark();//14
					p.setString(1, Id);
					p.setDate(2, date);
					p.setString(3, itemType);
					p.setString(4, detail);
					p.setDouble(5, unitPrice);
					p.setDouble(6, amount);
					p.setDouble(7, expense);
					p.setDouble(8, income);
					p.setString(9, account);
					p.setString(10, comment);
					p.setString(11, operatorID);
					p.setDate(12, currentDate);
					p.setTime(13, currentTime);
					p.setString(14, mark);
					System.out.println(p);
					p.execute();
				}
			}
		
			p.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;//the rows affected
	}
	
	public int getID()
	{
		int count_int = 0;
		try {
			Connection conn = Connection();
			PreparedStatement p = null;
			ResultSet r = null;
			p = conn.prepareStatement("select count(*) as count from emp_finance");
			if(r.next())
			{
				String count = r.getString("count");
				count_int = Integer.parseInt(count);
			}
			r.close();
			p.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count_int;
	}
	
	private boolean earlierThan(OneRecord record1,OneRecord record2)
	{
		if(Integer.parseInt(record1.getOperateDate().replace("-", ""))<Integer.parseInt(record2.getOperateDate().replace("-", ""))
				&& Integer.parseInt(record1.getOperateTime().replace(":", ""))<Integer.parseInt(record2.getOperateTime().replace(":", "")))
		{
			return true;
		}else
		{
			return false;
		}
	}

	/**
	 * initialize parameters
	 */
	public void setupDB(){

	}
	
	public static void main(String[] args)
	{
		FinaceActionServer test = new FinaceActionServer();
	}

	public static FinaceActionServer getInstance() {
		if(Instance==null)
			Instance =new FinaceActionServer();
		return Instance;
	}
	
	

}