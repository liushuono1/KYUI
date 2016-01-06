package Simple.HR;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;

public class SimpleDayCount {
	SimpleDayCount()
	{
		
	}
	
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
        bds.setUrl("jdbc:mysql://192.168.1.100:3307/bb2_test");

        bds.setUsername("root");//数据库用户
        bds.setPassword("root");//数据库密码
    }
    
    
    public static void TeacherDayCount()
    {
    	String Year_Month="2015-09";
    	List<TimeRecord> list_all= new LinkedList<TimeRecord>();
    	 try {
 			Connection conn= bds.getConnection();
 			
 			PreparedStatement pstmt = null;
             
             pstmt = conn.prepareStatement("select emp_logginrecordall.id,emp_id.name_company_address_book,L_date,L_time,L_status from emp_logginrecordall join emp_id on emp_logginrecordall.id=emp_id.id where security_level='LEVEL 3' and L_date like ? order by emp_logginrecordall.id ,L_date,L_time;");   
             // 结果集
             pstmt.setString(1, Year_Month+"%");
             ResultSet rs = pstmt.executeQuery();
             TimeRecord tr = new TimeRecord();
             while(rs.next())
             {
            	if(tr.id==null)
            	{
            		tr.id= rs.getString("id");
            		tr.date=rs.getString("L_date");
            		tr.name=rs.getString("name_company_address_book");
            		tr.stime="00:00:00";
            		tr.etime=rs.getString("L_time");
            		tr.status="-1";
            		
            		
            		list_all.add(tr);
            		tr = new TimeRecord();
            		tr.id= rs.getString("id");
            		tr.date=rs.getString("L_date");
            		tr.name=rs.getString("name_company_address_book");
            		tr.stime=rs.getString("L_time");
            		//tr.etime=rs.getString("L_time");
            		tr.status=rs.getString("L_status");
            		
            	}else if(!tr.date.equals(rs.getString("L_date")))
            	{
            		tr.id= rs.getString("id");
            		tr.date=rs.getString("L_date");
            		tr.name=rs.getString("name_company_address_book");
            		tr.stime="00:00:00";
            		tr.etime=rs.getString("L_time");
            		tr.status="-1";
            		list_all.add(tr);
            		
            		tr = new TimeRecord();
            		tr.id= rs.getString("id");
            		tr.date=rs.getString("L_date");
            		tr.name=rs.getString("name_company_address_book");
            		tr.stime=rs.getString("L_time");
            		//tr.etime=rs.getString("L_time");
            		tr.status=rs.getString("L_status");
            	}else
            	{
            		tr.etime=rs.getString("L_time");
            		list_all.add(tr);
            		tr = new TimeRecord();
            		tr.id= rs.getString("id");
            		tr.date=rs.getString("L_date");
            		tr.name=rs.getString("name_company_address_book");
            		tr.stime=rs.getString("L_time");
            		//tr.etime=rs.getString("L_time");
            		tr.status=rs.getString("L_status");
            	}
            	 
            	
            	 
            	 
             }
    	 }catch(Exception e)
    	 {
    		 e.printStackTrace();
         }
    	 String[] times ={"07:00:00","07:30:00","12:00:00","14:00:00","17:10:00"};
    	 List<TimeRecord> list_all_p= new LinkedList<TimeRecord>();
    	 for(TimeRecord tr:list_all)
    	 {
    		 list_all_p.addAll(tr.split(times));
    	 }
    	 
    	 for(TimeRecord tr:list_all_p)
    	 {
    		 //System.out.println(tr);
    		 if( (str2int(tr.stime)>70000 && str2int(tr.etime)<120000)
    				 ||((str2int(tr.stime)>140000 && str2int(tr.etime)<171000)))
    		 {
    			 if(Math.abs(str2int(tr.status)%2)==1 && (str2int(tr.etime)/100-str2int(tr.stime)/100)>=5)
    			 {
    				 System.err.println(tr+"\t"+(str2int(tr.etime)/100-str2int(tr.stime)/100));
    			 }
    		 }
    		 
    	 }
    }
    
	private static int str2int(String timeStr)
	{
		return Integer.parseInt(timeStr.replace(":", ""));
	}
	
    public static void main(String[] args)
    {
    	TimeRecord tr = new TimeRecord();
    	System.out.println((-1)%2);
    	TeacherDayCount();
    }
}

class TimeRecord
{
	public String id,date,stime,etime,status,name;
	
	TimeRecord()
	{
		
	}
	private TimeRecord(String id,String date,String stime,String etime,String status,String name)
	{
		 this.id=id;
		 this.date=date;
		 this.stime=stime;
		 this.etime=etime;
		 this.status=status;
		 this.name=name;
	}
	@Override
	public String toString()
	{
		return id+"\t"+date+"\t"+name+"\t"+stime+"--"+etime+"\t"+status;
	}
	
	public List<TimeRecord> split(String[] times)
	{
		List<TimeRecord> ret = new LinkedList<TimeRecord>();
		String currstart= stime;
		for(int i =0;i<times.length;i++)
		{
			if(str2int(times[i])>str2int(stime) && str2int(times[i])<str2int(etime))
			{
				ret.add(new TimeRecord(id, date, currstart, times[i], status, name));
				currstart=times[i];
			}else if(str2int(times[i])>str2int(etime))
			{
				ret.add(new TimeRecord(id, date, currstart, etime, status, name));
				break;
			}
		}
		
		return ret;
		
	}
	
	private int str2int(String timeStr)
	{
		return Integer.parseInt(timeStr.replace(":", ""));
	}
}


