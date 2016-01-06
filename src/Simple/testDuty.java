package Simple;

import java.awt.Window;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import com.sun.jmx.snmp.Timestamp;

import KYUI.ScrollInfoPane;

public class testDuty {
	
	List<Duty> dutys ; //测试任务列表
	public testDuty()
	{
		JFrame f =new JFrame();
		f.setSize(900, 100);
		//f.setType(Window.Type.UTILITY);
		f.setUndecorated(true);
		ScrollInfoPane si=new ScrollInfoPane(2);
		f.getContentPane().add(si);
		f.setVisible(true);
		
		si.startshow();
		//Duty testduty = new Duty();
		
		//testduty.setOptionFeedBack(null);
		//testduty.setQuestTitle("任务测试11");
		
		try {
			TaskManager.start();
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//-----------------------测试代码--------------------
		 
		   Timer t = new Timer();
		   TimerTask task = new TimerTask(){
		    @Override
		    public void run() 
		    {
		     // 要执行的代码
		    	//testUpdate();
		    	System.err.println(DutyList.getInstance());
		    	Trigger.printTriggers();
		    }
		   };
		   t.schedule(task, 0, 10000);//测试更新，每10秒一次
		//------------------------------测试代码----------------------
		   
		try {
			Thread.sleep(7000);
			System.err.print("----remove----");
			//dutys.remove(3);
			//dutys_copy.remove(3);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testUpdate()
	{
		//for(int i=0;i<dutys.size();i++)
		for(Duty d:dutys)
	    {
			TaskManager.getInstance().addDuty(d);
	    }
		TaskManager.getInstance().submit();
		TaskManager.getInstance().setAllDutyTrigger(DutyList.getInstance());
	}
	
	public  static void main(String args[])
	{
		new testDuty();
	}

}
