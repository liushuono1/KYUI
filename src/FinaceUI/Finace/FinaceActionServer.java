package FinaceUI.Finace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.sql.Date;


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
		
		String[] constraint1 = {"detail","contain:押金 "};
		constraintList.add(constraint1);
		
		Collection<OneRecord> coll = findRecordWithManyConstraints(constraintList);
		for(OneRecord one:coll)
		{
			System.out.println(one);
		}
	}
	
	/**
	 * get a db connection
	 */
	public Connection Connection() throws ClassNotFoundException, SQLException
	{
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://192.168.1.101:3307/bb2_default";
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
	public Collection<OneRecord> findRecordWithOneConstraint(String[] constraintPair){		
		Collection<OneRecord> collectionRecord = new LinkedList<OneRecord>();
		String attribute = constraintPair[0];
		String attributeValue = constraintPair[1];
		try {
			Connection conn = Connection();
			PreparedStatement p = null;
			ResultSet r = null;
			
				String[] dateRange = attributeValue.split(";");
				String from = dateRange[0];
				String to = dateRange[1];
				p = conn.prepareStatement("select * from emp_finance where "+constraintPair[0]+ buildQueryStr(constraintPair[0]) +";");
				r = p.executeQuery();
			
			while(r.next())
			{
				OneRecord a_record = new OneRecord(r);
				collectionRecord.add(a_record);
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

	private String buildQueryStr(String str) {
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
			ret=" "+searchStrs[0]+"'"+searchStrs[1]+"' ";
		}
		
		return ret;
	}
	
	private String buildMutliQueryStr(List<String[]> constraintList) {
		String ret ="";
		
		for(String[] onePair : constraintList )
		{  
			if(!ret.equals(""))
				ret+="AND";
			ret+=onePair[0]+buildQueryStr(onePair[1]);
			
		}
		return ret;
	}

	/**
	 * 
	 * @param constraintList
	 */
	public Collection<OneRecord> findRecordWithManyConstraints(List<String[]> constraintList){
		Collection<OneRecord> collectionRecord = new LinkedList<OneRecord>();
		try {
			Connection conn = this.Connection();
			PreparedStatement p = null;
			ResultSet r = null;
			String sqlStatement = "select * from emp_finance where ";
			//for(String[] onePair:constraintList)

			sqlStatement +=this.buildMutliQueryStr(constraintList);
			System.out.println(sqlStatement);
			p = conn.prepareStatement(sqlStatement);
			r = p.executeQuery();
			while(r.next())
			{
				OneRecord a_record = new OneRecord(r);
				collectionRecord.add(a_record);
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

	/**
	 * general insert into datbase
	 * 
	 * @param record
	 * @param operatorId
	 * @param type: 0:normal inset; 1:update;  2:delete
	 */
	public int insert(Collection<OneRecord> records, String operatorId, int type){
		try {
			Connection conn = Connection();
			PreparedStatement p = null;
			
			//------------------------get id;
			int id=1;
			p = conn.prepareStatement("SELECT  max(CAST(id as SIGNED))  from emp_finance;");
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
							+"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);");
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
					System.out.println(p);
					p.execute();
				}
			}
			else if(type == 1)//update
			{
				
			}
			else//delete
			{
				
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