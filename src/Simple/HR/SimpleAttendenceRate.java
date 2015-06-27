package Simple.HR;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;


public class SimpleAttendenceRate {
	private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private static final int INITIAL = 15;//初始化50个连接
    private static final int MAX_ACTIVE = 50;//最大值500个连接
    private static final int MAX_IDLE = 10;//最大空闲10
    private static final long MAX_WAIT = 5 * 1000;//超过500个访问，等待的时间
    public static BasicDataSource bds;
    static{
        if(bds == null){
            bds = new BasicDataSource();
        }
        bds.setDriverClassName(DRIVER_NAME);
        bds.setInitialSize(INITIAL);
        bds.setMaxTotal(MAX_ACTIVE);
        bds.setMaxIdle(MAX_IDLE);
        bds.setMaxWaitMillis(MAX_WAIT);
        bds.setTestWhileIdle(true);
        bds.setTimeBetweenEvictionRunsMillis(1000*60*5);
        bds.setMinEvictableIdleTimeMillis(1000*50*15);
        bds.setMaxConnLifetimeMillis(1000*60*29);
        bds.setRemoveAbandonedOnBorrow(true);
        bds.setRemoveAbandonedTimeout(180);
        bds.setValidationQuery("select 1;");
        bds.setUrl("jdbc:mysql://"+"192.168.1.100"+":3307/bb2_"+ "TEST");//数据库的路径，

        bds.setUsername("root");//数据库用户
        bds.setPassword("root");//数据库密码
    }
    
    
	public static Hashtable<String,Double> getAttendenceRate(String Year_Month, BasicDataSource BDS) 
	{
		
		Hashtable<String,Double> dept_att_ratio=new Hashtable<String, Double>();
		
		List<String> Sats= new LinkedList();
		List<Date> working_day = new LinkedList<Date>();
		List<String> IDs = new LinkedList<String>();
		Hashtable<String,Date> ID_Adate= new Hashtable(),
				ID_Ldate=new Hashtable();
		Hashtable<String,String> ID_Dept= new Hashtable(),ID_Level=new Hashtable();
		
		Hashtable<String,Integer> DeptDate_Num = new Hashtable();
		Hashtable<String,Double> DeptDate_Att = new Hashtable(),DeptDate_Att_Sat = new Hashtable();
		
		//setConnectingUrl();
		 try {
			Connection conn= BDS.getConnection();
			
			PreparedStatement pstmt = null;
            
            pstmt = conn.prepareStatement("select distinct(L_date) from emp_logginrecordall where L_date like ? order by L_date;");   
            // 结果集
            pstmt.setString(1, Year_Month+"%");
            ResultSet rs = pstmt.executeQuery();
            String ID ="",holderID="";
            while(rs.next())
            {
               	 working_day.add(rs.getDate("L_date"));
               	 
               	 if(isSaturday(rs.getString("L_date")))
               	 {
               		 Sats.add(rs.getString("L_date"));
               	 }
               	 //System.out.println(ID);
            }
            
            System.out.println(Sats);

            pstmt = conn.prepareStatement("select * from emp_id where temp_worker<>'1';");   
            // 结果集
            rs = pstmt.executeQuery();
            
            while(rs.next())
            {
            	
            	ID = rs.getString("ID");
            	IDs.add(ID);
            	
            	//System.out.println(ID);
            	ID_Adate.put(ID, rs.getDate("ADATE"));
            	ID_Ldate.put(ID, rs.getDate("LDATE"));
            	ID_Level.put(ID, rs.getString("security_level"));
            	ID_Dept.put(ID, rs.getString("department"));
               	 //System.out.println(ID);
            }
            
            
            
            
            for(String id : IDs)
            {
            	for(Date day:working_day)
            	{
            		
            		//System.out.println(ID_Level.get(id)+"   "+day);
            		String Key =ID_Dept.get(id)+"\t"+day.toString();
            		if(ID_Level.get(id).equals("LEVEL 5")||ID_Level.get(id).equals("LEVEL 3"))
                	{
            			
                		if(!ID_Adate.get(id).after(day))
                		{
                			
                			
                			if(DeptDate_Num.containsKey(Key))
                			{
                				int value =DeptDate_Num.get(Key)+1;
                			    DeptDate_Num.put(Key, value);
                			}
                			else
                			{
                				 DeptDate_Num.put(Key, 1);
                			}
                		}
                	}
            		
            		if(ID_Level.get(id).equals("LEVEL 4"))
                	{
                		if(ID_Ldate.get(id).after(day))
                		{
                			
                			if(DeptDate_Num.containsKey(Key))
                			{
                				int value =DeptDate_Num.get(Key)+1;
                			    DeptDate_Num.put(Key, value);
                			}else
                			{
               				 DeptDate_Num.put(Key, 1);
               			}
                		}
                	}
            		
            		if(ID_Level.get(id).equals("TERMINATED"))
                	{
                		if(ID_Ldate.get(id).after(day))
                		{
                			
                			if(DeptDate_Num.containsKey(Key))
                			{
                				int value =DeptDate_Num.get(Key)+1;
                			    DeptDate_Num.put(Key, value);
                			}
                			else
                			{
                				 DeptDate_Num.put(Key, 1);
                			}
                		}
                	}
            	}
            	
            	
            }
            for(String key:DeptDate_Num.keySet())
            {
            	//System.out.println(key+"\t"+String.valueOf(DeptDate_Num.get(key)));
            }
            
           // System.out.println(DeptDate_Num);
            
            
            
            pstmt = conn.prepareStatement("select L_date, count(distinct(emp_logginrecordall.id)) as Att,emp_id.department from emp_logginrecordall join emp_id on emp_id.id= emp_logginrecordall.id where  L_date like ? and L_date group by emp_id.department,emp_logginrecordall.L_date ;");   
            pstmt.setString(1, Year_Month+"%");
            System.out.println(pstmt);
            // 结果集
            rs = pstmt.executeQuery();
            
            while(rs.next())
            {
            	String date  = rs.getString("L_date");
            	String dept=  rs.getString("department");
            	if(!dept.equals("教工部") && !dept.equals("综合办公室") && !dept.equals("园长办公室"))
            	{
            	 	System.out.println(dept+"\t"+date+"   :  "+ rs.getDouble("Att"));
                	if(!Sats.contains(date))
                	{
                		DeptDate_Att.put(dept+"\t"+date, rs.getDouble("Att")/((double)DeptDate_Num.get(dept+"\t"+date)));
                	}
                	else
                	{
                		DeptDate_Att_Sat.put(dept+"\t"+date, rs.getDouble("Att")/((double)DeptDate_Num.get(dept+"\t"+date)));
                	}
                	
                	System.out.println(dept+"\t"+date+"\t"+(DeptDate_Num.get(dept+"\t"+date)-rs.getDouble("Att")));
            	}
   
            	
            }
            
           
            //System.out.println(DeptDate_Att_Sat);
            double sum_a1=0;
            for(double s:DeptDate_Att.values())
            {
            	sum_a1+=s;
            }
            
            double sum_a2=0;
            for(double s:DeptDate_Att_Sat.values())
            {
            	sum_a2+=s;
            }
            
            double ratio= (sum_a1/DeptDate_Att.size())/(sum_a2/DeptDate_Att_Sat.size());
            System.out.println("ratio"+ratio);
            
            
            
            for(String key:DeptDate_Att.keySet())
            {
            	String Key = key.split("\t")[0];
            	if(dept_att_ratio.containsKey(Key))
    			{
    				double value =dept_att_ratio.get(Key) + DeptDate_Att.get(key)/working_day.size();
    				dept_att_ratio.put(Key, value);
    			}
    			else
    			{
    				dept_att_ratio.put(Key, DeptDate_Att.get(key)/working_day.size());
    			}
            }
            
            for(String key:DeptDate_Att_Sat.keySet())
            {
            	String Key = key.split("\t")[0];
            	if(dept_att_ratio.containsKey(Key))
    			{
    				double value =dept_att_ratio.get(Key) + DeptDate_Att_Sat.get(key)/working_day.size()*ratio;
    				dept_att_ratio.put(Key, value);
    			}
    			else
    			{
    				dept_att_ratio.put(Key, DeptDate_Att_Sat.get(key)/working_day.size()*ratio);
    			}
            }
            
            System.out.println(dept_att_ratio);
            
            
            
            rs.close();
            conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 return dept_att_ratio;
		 
	}
	
	

	private void setConnectingUrl()
	{
		bds.setUrl("jdbc:mysql://"+"192.168.1.100"+":3307/bb2_"+ "TEST");//数据库的路径，
	}
	
	public static boolean isSaturday(String bDate)
	{
		Calendar cal = Calendar.getInstance();
		try {
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");       
			java.util.Date bdate;
			bdate = format1.parse(bDate);
		    cal.setTime(bdate);
		    
	    } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY)
	    {
	  	  	return true;
	  	}
	    else 
	    	return false;
	}
	
	
	public static void main(String args[])
	{
		

		SimpleAttendenceRate.getAttendenceRate("2015-01",bds);
	}
}
