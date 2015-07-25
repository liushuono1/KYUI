package FinaceUI.Manege;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.dbcp2.BasicDataSource;




import KYUI.KYMainUI;

public class SimpleFinaceActionServer {

	static SimpleFinaceActionServer instance =null;
	
	private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private static final int INITIAL = 15;//初始化50个连接
    private static final int MAX_ACTIVE = 50;//最大值500个连接
    private static final int MAX_IDLE = 10;//最大空闲10
    private static final long MAX_WAIT = 5 * 1000;//超过500个访问，等待的时间
    static BasicDataSource bds;
    static{
        if(bds == null){
            bds = new BasicDataSource();
        }
        bds.setDriverClassName(DRIVER_NAME);
        bds.setInitialSize(INITIAL);
        bds.setMaxTotal(MAX_ACTIVE);
        bds.setMaxIdle(MAX_IDLE);
        bds.setMaxWaitMillis(MAX_WAIT);
    	bds.setUrl("jdbc:mysql://"+KYMainUI.SQLIP+":3307/bb2_"+KYMainUI.DBStr);//数据库的路径，
        bds.setTestWhileIdle(true);
        bds.setTimeBetweenEvictionRunsMillis(1000*60*5);
        bds.setMinEvictableIdleTimeMillis(1000*50*15);
        bds.setMaxConnLifetimeMillis(1000*60*29);
        bds.setRemoveAbandonedOnBorrow(true);
        bds.setRemoveAbandonedTimeout(180);
        bds.setValidationQuery("select 1;");
        bds.setUsername("root");//数据库用户
        bds.setPassword("root");//数据库密码
    }
    
    
    public SimpleFinaceActionServer()
    {
    	SimpleFinaceActionServer.instance=this;
    }
    
    
    public static SimpleFinaceActionServer getInstance()
    {
    	if(instance==null)
    	{
    		new SimpleFinaceActionServer();
    	}
    	
    	return instance;
    }
    
    public Collection<RecordVO> findRecordwithOneResttiction(String rst1, String rst2)
    {
    	
		Collection<RecordVO> recordSet = new HashSet();
    	
    	
    	return recordSet;
    	
    }
    
    
    private Collection<RecordVO> executQuries(String SearchStr)
    {
    	Collection<RecordVO> recordSet = new HashSet();
    	try {
			Connection conn= bds.getConnection();
			PreparedStatement pstmt=conn.prepareStatement("select * from emp_charge_refund_paid "+SearchStr+";");
			
			ResultSet rs =pstmt.executeQuery();
			
			while(rs.next())
			{
				RecordVO rVO= new RecordVO();
				rVO.setId(rs.getString("id"));
				rVO.setPaymentDate(rs.getDate("P_date"));
				rVO.setPaymentTime(rs.getTime("P_Time"));
				rVO.setPaymentDetail(rs.getString("detail"));
				rVO.setCharge(rs.getInt("charge"));
				rVO.setRefund(rs.getInt("refund"));
				rVO.setPayment(rs.getInt("pay"));
				rVO.setComment(rs.getString("comment"));
				recordSet.add(rVO);
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return recordSet;
    }
    
    
}
