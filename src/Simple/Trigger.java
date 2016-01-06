package Simple;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Trigger {

	static List<Trigger> TriggerList =new LinkedList<Trigger>();
	static int i=0,count=0;
	Duty duty;
	Timer t;

	static List<triggerEvent> triggerList = new LinkedList<triggerEvent>();;

	public Trigger() {
		triggerList = new LinkedList<triggerEvent>();
	}

	public static List<triggerEvent> getInstance() {
		return triggerList;
	}

	public static  void printTriggers()
	{	System.out.println("Trigger");
		int i=1;
		for(Trigger t:TriggerList)
		{
			System.out.println("Trigger"+count+(i++)+"\t:"+t);
		}
	}
	public Trigger(Duty d) {
		duty = d;
		this.timeTrigger(d);
		TriggerList.add(this);
		d.setTrigger(this);
		
	}

	public void distroy()
	{
		//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		t.cancel();
		TriggerList.remove(this);
	}
	
	
	public static boolean isTriggered(Duty d)
	{
		boolean ret =false;
		for(Trigger T:TriggerList)
		{
			if(T.getDuty().isSame(d))
			{
				ret=true;
				break;
			}
		}
		
		return ret;
	}
	
	public boolean isSame(Trigger t2)
	{
		return this.getDuty().isSame(t2.getDuty());
	}
	
	static void clean()
	{
		DutyList dt;

		for(int i=0;i<TriggerList.size();i++)//(Trigger T:TriggerList)
		{   Trigger t=TriggerList.get(i);
			if(!DutyList.contains(t.getDuty()))
			{   
				System.err.println("triggrt removed"+"\n"+t.getDuty()+"\ndutylist\n"+DutyList.getInstance());
				System.err.println(DutyList.comparestring);
				t.distroy();
			}
		}
	}
	
	public Duty getDuty() {
		return this.duty;
	}

	public String toString()
	{
		return duty.getStartTime().toString();
	}
	public void addToTriggerList() {
		//String start_time = duty.getStartTime().toString();
		//triggerEvent te = new triggerEvent(start_time, duty);
		//timeTrigger(duty);?
		//triggerList.add(te);
	}

	public static boolean existInTriggerList(triggerEvent te) {
		boolean exist = true;
		for (int i = 0; i < triggerList.size(); i++) {
			triggerEvent a_te = triggerList.get(i);
			if (compareTriggerEvent(te, a_te)) {
				exist = false;
				break;
			}
		}
		return exist;
	}

	public static boolean compareTriggerEvent(triggerEvent te1, triggerEvent te2) {
		boolean same = true;
		String time1 = te1.getTime();
		Duty duty1 = te1.getDuty();

		String time2 = te1.getTime();
		Duty duty2 = te1.getDuty();
		if (time1.equals(time2)) {
			List<Object> d1_list = duty1.duty;
			List<Object> d2_list = duty2.duty;
			for (int i = 0; i < d1_list.size(); i++) {
				if (!d1_list.get(i).toString().equals(d2_list.get(i).toString())) {
					same = false;
					break;
				}
			}
		} else {
			same = false;
		}

		return same;
	}

	public void timeTrigger(final Duty duty) {
		// 一天的毫秒数
		long daySpan = 24 * 60 * 60 * 1000;

		// 规定的每天时间15:33:30运行
		final SimpleDateFormat sdf = new SimpleDateFormat(duty.getStartTime().toString());
		// 首次运行时间
		Date startTime = null;
		try {
			startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 如果今天的已经过了 首次运行时间就改为明天
		if (System.currentTimeMillis() > startTime.getTime()) {
			startTime = new Date(startTime.getTime() + daySpan);
		}
		t = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// 要执行的代码
				System.err.println((i++)+"  "+duty);
				try {
					TaskManager.processedDuty(duty);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		// 以每24小时执行一次
		count++;
		t.scheduleAtFixedRate(task, startTime, daySpan);
	}
}