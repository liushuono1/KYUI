package KYUI.NoticeCenter;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import KYUI.KYMainUI;

public class Taskman {

	KYMainUI UI;
	List<KYTask> tasks;
	
	public Taskman(KYMainUI ui)
	{
		UI=ui;
	}
	
	public void getTasks()
	{
		
	}
}
class KYTask
{
	Date start,end;
	String Title,detail;
	int status;   // 预定义  0--无状态，不需要回写数据库  1--任务起始  2--任务完成  
	public KYTask ()  //空任务，测试用
	{
		start = new Date();  
		end = new Date(System.currentTimeMillis()+1000*5*60);
		Title="测试任务";detail ="测试任务内容";
	}
	
	public String getTitle()
	{
		return Title;
	}
	
	public String getDetail()
	{
		return detail;
	}
	
	public KYTask(String[] prams,int state)  //String {起始，结束，标题，内容，模式}
	{
		start = StringToDate(prams[0]);  
		end = StringToDate(prams[1]);
		Title=prams[2];detail =prams[3];
	}
	
	public boolean isValidNow()
	{
		if(DueTime()==0)
		{
			return true;
		}else
		{
			return false;
		}
	}
	
	public long DueTime(){
		long due = System.currentTimeMillis()- start.getTime();
		long overdue = System.currentTimeMillis()- end.getTime();
		if(overdue<0)
		{
			return -1;
		}
		
		if(due<0)
		{
			return 0;
		}

		return due;
		
	}
	
    public static Date StringToDate(String dateStr){  //需要转移到公共Util类
    
    	String formatStr= "HH:mm:ss";
    	DateFormat sdf=new SimpleDateFormat(formatStr);
        Date date=null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
		 date.setDate(new Date().getDate());
		 date.setMonth(new Date().getMonth());
		 date.setYear(new Date().getYear());
        return date;
    }
	
	
}