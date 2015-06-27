package kyHRUI.Student;



import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class KeepCheckingDatabase{
	Timer timer;
	rollCall rollcall = new rollCall() ;
	public KeepCheckingDatabase(rollCall rollcall) throws InterruptedException, ClassNotFoundException, SQLException, IOException
	{
		
		 //rollcall=rollcall;
		 TimerTask task = new TimerTask()
		 {
			 @Override
			public void run() 
			 {
				rollcalling();
			 }
		 };
		 timer = new Timer();
		 timer.schedule(task, 0, 5000);//√ø∏Ù5√Îread data
		 Thread.sleep(40*1000);//should be 60s*30mins in my case
		 timer.cancel();
	}
	
	public void rollcalling()
	{
		 rollcall.readDatabase();
	}
}
