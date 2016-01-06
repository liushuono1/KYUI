package Simple;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import KYUI.KYMainUI;
import KYUI.KYUIUtil;
import KYUI.KYUtils;
import KYUI.ScrollInfoPane;

public class TaskManager {

	static boolean connectionState; 
	static TaskManager instance;
	public static String SQLIP = "192.168.1.100";

	public boolean testsign = true;
	public int testInt=0;
			
	static long sync_interval = 5*1000;

	public static TaskManager getInstance() {
		if (instance == null)
			new TaskManager();
		return instance;
	}

	public static boolean isConnectionState() {
		return connectionState;
	}

	public static void main(String[] args) {
		try {
			TaskManager.start();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public static synchronized void processedDuty(Duty dt) throws SQLException {
		System.err.println("******"+dt.getQuestTitle()+" is trigged at"+(new Date()));
		dt.setTrigged(true);
		ScrollInfoPane.getInstance().pushMsg(dt);
		//ScrollInfoPane.getInstance().setFdbp(dt.getFeedback().getFeedBackPane());
		//ScrollInfoPane.getInstance().pushMsg(dt.questTitle);
		TaskManager.getInstance().refreshTasks();
		
	}
	public static void setConnectionState(boolean connectionState) {
		TaskManager.connectionState = connectionState;
	}

	public static void start() throws ClassNotFoundException, SQLException
	{
		getInstance();
		
		TimerTask RemoteSync = new TimerTask()
				{

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							setConnectionState(getInstance().SyncLocalDB());
							//System.err.println(isConnectionState()+" synced");
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						try {
							getInstance().refreshTasks();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			
				};
		Timer t= new Timer();
		t.schedule(RemoteSync, 3000, sync_interval);
		getInstance().initLocalDB();
		getInstance().SyncLocalDB();
		//getInstance().GeneDutyFromLocalDB();
		getInstance().refreshTasks();
		
	}

	String className;

	public TaskManager() {
		instance = this;
	}

	public void addDuty(Duty d) {
		DutyList.getInstance().addDuty(d);
	}

	/*
	 * Invalid duty: the duties that are not appear in dutyList but exist in
	 * trigger_list
	 */
	
	/*
	 * public void removeInvalidDuty() { List<triggerEvent> triggers_list =
	 * Trigger.getInstance();//The ones in here List<Duty> duty_list =
	 * DutyList.getDutyList();//not in here for(int
	 * i=0;i<triggers_list.size();i++) { Duty a_duty =
	 * triggers_list.get(i).getDuty(); if(invalid(a_duty)) {
	 * triggers_list.remove(a_duty); } } }
	 */
	
	/*
	 * public boolean invalid(Duty duty) { boolean invalid = true; List<Duty>
	 * duty_list = DutyList.getDutyList();//not in here
	 * 
	 * for(int j=0;j<duty_list.size();j++) { Duty a_duty = duty_list.get(j);
	 * if(duty.isSame(a_duty))//same duty { invalid = false; break; } } return
	 * invalid; }
	 */

	public void displayTask(Duty duty) {
		System.out.println("type: " + duty.getQuestType() + "\tstart time: " + duty.getStartTime().toString());
	}

	public void exeteTasks() {
		/*
		 * System.out.println(new Time(System.currentTimeMillis()).toString());
		 * String time1 = (new
		 * Time(System.currentTimeMillis()+10000)).toString(); Duty d1 = new
		 * Duty(); d1.setQuestType("type1"); d1.setStartTime(new
		 * Timestamp(System.currentTimeMillis()+10000 )); timeTrigger(time1,d1);
		 * 
		 * System.out.println(new Time(System.currentTimeMillis()).toString());
		 * String time2 = (new
		 * Time(System.currentTimeMillis()+20000)).toString(); Duty d2 = new
		 * Duty(); d2.setQuestType("type2"); d2.setStartTime(new
		 * Timestamp(System.currentTimeMillis()+20000 ));
		 * 
		 * timeTrigger(time2,d2);
		 */

		if (DutyList.getInstance() == null)
			new DutyList();
		getTasks();
	}

	private List<Duty> GeneDutyFromLocalDB() throws SQLException {
		// TODO Auto-generated method stub
		List<Duty> ret = new LinkedList<Duty>();
		Connection LocalConn=null;
		if(testsign)
		{
			testInt=0;
		}
		try {
			Class.forName("org.sqlite.JDBC");
			LocalConn = DriverManager.getConnection("jdbc:sqlite:VCLib.dll");
			
			PreparedStatement p = null;
			ResultSet r = null;
			// 从数据库读取常规任务 ，加入ret
			p = LocalConn.prepareStatement("select * from duty");
			r = p.executeQuery();
			// buildRecordfromResultSet(r);
			while (r.next()) {
				String task_type = r.getString("task_type");
				String task_title = r.getString("task_title");
				
				Timestamp start_time = new Timestamp(System.currentTimeMillis());
				
				//System.out.println(r.getTimestamp("start_time"));
				start_time.setHours(r.getTimestamp("start_time").getHours());
				start_time.setMinutes(r.getTimestamp("start_time").getMinutes());
				start_time.setSeconds(r.getTimestamp("start_time").getSeconds());
				
				Timestamp end_time= new Timestamp(System.currentTimeMillis());
				end_time.setHours(r.getTimestamp("end_time").getHours());
				end_time.setMinutes(r.getTimestamp("end_time").getMinutes());
				end_time.setSeconds(r.getTimestamp("end_time").getSeconds());
				
				if(testsign)
				{
					start_time = new Timestamp(System.currentTimeMillis()+(testInt+1)*5000L);
					end_time= new Timestamp(System.currentTimeMillis()+(testInt+1)*5000L+4000L);
				}
				
				//int finishOrNot = r.getInt("finishOrNot");
				int feedbackOrNot = r.getInt("needFeedbackOrNot");
				//String feedback = r.getString("feedback");
				String teacher = r.getString("teacher");
				String className = r.getString("class_name");
	
				
				Object followingDuty=null; 
				if(r.getBytes("followingDuty")!=null)
					followingDuty= KYUtils.ByteToObject( r.getBytes("followingDuty"));
				
				
				Duty d = new Duty();
				
				d.setQuestType(task_type);
				d.setQuestTitle(task_title);
				d.setStartTime(start_time);
				d.setEndTime(end_time);
				
				if(followingDuty!=null)
				{
					d.setFollowingDuty((Duty)followingDuty);
				}
				
				//d.setNoFeedBack();//根据类型修改返回值
				
				if(feedbackOrNot == 0)
				{
					d.setNoFeedBack();
				}else
				{
					if(task_type==null)
					{
						List tl=new LinkedList();  //测试模式
						tl.add("老师1");
						tl.add("老师2");
						tl.add("老师3");
						d.setOptionFeedBack(tl);
					}else
					{   if(testsign)
						{
						List tl=new LinkedList();  //测试模式
						tl.add("老师1");
						tl.add("老师2");
						tl.add("老师3");
						d.setOptionFeedBack(tl);
						}else
						try{
							List tl=(List) KYUIUtil.getTeachers();
							d.setOptionFeedBack(tl);
						}catch(Exception e)
						{
							List tl=new LinkedList();  //测试模式
							tl.add("老师1");
							tl.add("老师2");
							tl.add("老师3");
							//d.setOptionFeedBack(tl);
							d.setNoFeedBack();
						}
					}
				}
				
				
				
				
				if (feedbackOrNot == 1) {
					d.setFeedbackable(true);
				} else {
					d.setFeedbackable(false);
				}
				d.setTeacher(teacher);
				d.setClass(className);
				ret.add(d);
				// System.out.println("????????????????"+start_time.toString());
				// timeTrigger(start_time.toString(),d);
				
				
				
			}
			
			// 从数据库读取非常规任务，加入ret
			p = LocalConn.prepareStatement("select * from unregularduty");
			r = p.executeQuery();
			// buildRecordfromResultSet(r);
			while (r.next()) {
				String task_type = r.getString("task_type");
				String task_title = r.getString("task_title");
				Timestamp start_time = r.getTimestamp("start_time");
				Timestamp end_time = r.getTimestamp("end_time");
				//int finishOrNot = r.getInt("finishOrNot");
				int feedbackOrNot = r.getInt("needFeedbackOrNot");
				String feedback = r.getString("feedback");
				String teacher = r.getString("teacher");
				String className = r.getString("class_name");

				Duty d = new Duty();
				d.setId(r.getInt("id"));
				d.setQuestType(task_type);
				d.setQuestTitle(task_title);
				d.setStartTime(start_time);
				d.setEndTime(end_time);
				
				if (feedbackOrNot == 1) {
					d.setFeedbackable(true);
				} else {
					d.setFeedbackable(false);
				}
				d.setTeacher(teacher);
				d.setClass(className);
				ret.add(d);
				// System.out.println("????????????????"+start_time.toString());
				// timeTrigger(start_time.toString(),d);
				
				
				
			}
			for(Duty d:ret)
			{
				//System.out.println("$"+d);
			}
			
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			LocalConn.close();
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			LocalConn.close();
			e1.printStackTrace();
		}

		// 从数据库读取非常规任务，加入ret

		return ret;
	}

	/*
	 * public void timeTrigger(String exeTime, final Duty d) { // 一天的毫秒数 long
	 * daySpan = 24 * 60 * 60 * 1000;
	 * 
	 * // 规定的每天时间15:33:30运行 final SimpleDateFormat sdf = new
	 * SimpleDateFormat(exeTime); // 首次运行时间 Date startTime = null; try {
	 * startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"
	 * ).parse(sdf.format(new Date())); } catch (ParseException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } // 如果今天的已经过了
	 * 首次运行时间就改为明天 if(System.currentTimeMillis() > startTime.getTime()) {
	 * startTime = new Date(startTime.getTime() + daySpan); } Timer t = new
	 * Timer(); TimerTask task = new TimerTask(){
	 * 
	 * @Override public void run() { // 要执行的代码 //System.err.println(d); } }; //
	 * 以每24小时执行一次 t.scheduleAtFixedRate(task, startTime, daySpan); }
	 * 
	 */
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		if(KYMainUI.isInstanced())
		{
			Connection conn;
			conn=KYMainUI.bds.getConnection();
			return conn;
		}else
		{
			Connection conn;
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://" + SQLIP + ":3307/bb2_test";
			String user = "root";
			String password = "root";
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			return conn;
		}
		
		
	}

	public List<Duty> getTasks() // 测试用方法
	{
		List<Duty> all_duty = new LinkedList<Duty>();
		
		try {
			Class.forName("org.sqlite.JDBC");
			Connection LocalConn = DriverManager.getConnection("jdbc:sqlite:VCLib.dll");
			LocalConn = this.getConnection();
			PreparedStatement p = null;
			ResultSet r = null;
			p = LocalConn.prepareStatement("select * from duty");
			r = p.executeQuery();
			// buildRecordfromResultSet(r);
			while (r.next()) {
				String task_type = r.getString("task_type");
				String task_title = r.getString("task_title");
				Timestamp start_time = r.getTimestamp("start_time");
				Timestamp end_time = r.getTimestamp("end_time");
				int finishOrNot = r.getInt("finishOrNot");
				int feedbackOrNot = r.getInt("needFeedbackOrNot");
				String feedback = r.getString("feedback");
				String teacher = r.getString("teacher");
				String className = r.getString("class_name");

				Duty d = new Duty();
				d.setQuestType(task_type);
				d.setQuestTitle(task_title);
				d.setStartTime(start_time);
				d.setEndTime(end_time);
				if (finishOrNot == 1) {
					d.setFinished(true);
				} else {
					d.setFinished(true);
				}
				if (feedbackOrNot == 1) {
					d.setFeedbackable(true);
				} else {
					d.setFeedbackable(false);
				}
				// d.setFeedback(feedback);//??????????????????????????????????????????????????/
				d.setTeacher(teacher);
				d.setClass(className);
				all_duty.add(d);
				// System.out.println("????????????????"+start_time.toString());
				// timeTrigger(start_time.toString(),d);
			}
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return all_duty;
	}

	
	

	public void initLocalDB() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");

		Connection conn = DriverManager.getConnection("jdbc:sqlite:VCLib.dll");

		PreparedStatement s = conn
				.prepareStatement("SELECT COUNT(*) FROM sqlite_master where type='table' and name='duty';");
		ResultSet rs = s.executeQuery();
		rs.next();
		int c = rs.getInt(1);
		if (c == 0) {

			String create = "CREATE TABLE `duty` (" + "`id` int(11) NOT NULL ,"
					+ "`task_type` varchar(10) default NULL," + "`task_title` varchar(20) default NULL,"
					+ "`start_time` timestamp NULL,"
					+ "`end_time` timestamp NOT NULL default '1999-01-01 00:00:01',"
					//+ "`finishOrNot` tinyint(1) default NULL," 
					+ "`needFeedBackOrNot` tinyint(1) default NULL,"
					//+ "`feedback` varchar(20) default NULL," 
					  +"`day` varchar(20) default NULL,   "
					  +"`week` varchar(20) default NULL,  "
					  +"`month` varchar(20) default NULL, "
					  +"`year` varchar(20) default NULL,  "
					  +"`teacher` varchar(20) default NULL,"
					+ "`class_name` varchar(20) default NULL," 
					+ "`followingDuty` blob,"  
					+ "PRIMARY KEY  (`id`)" + ");";

			s = conn.prepareStatement(create);

			s.execute();
		}

		s = conn.prepareStatement("SELECT COUNT(*) FROM sqlite_master where type='table' and name='unregularduty';");
		rs = s.executeQuery();
		rs.next();
		c = rs.getInt(1);
		if (c == 0) {

			String create = "CREATE TABLE `unregularduty` (" + "`id` int(11) NOT NULL,"
					+ "`task_type` varchar(10) default NULL," + "`task_title` varchar(50) default NULL,"
					+ "`start_time` timestamp NOT NULL,"
					+ "`end_time` timestamp NOT NULL default '0000-00-00 00:00:00',"
					+ "`finishOrNot` tinyint(1) default NULL," + "`needFeedBackOrNot` tinyint(1) default NULL,"
					+ "`feedback` varchar(50) default NULL," + "`teacher` varchar(20) default NULL,"
					+ "`class_name` varchar(20) default NULL," 
					+ "`followingDuty` blob,"
					+ "PRIMARY KEY  (`id`)" + ");";

			s = conn.prepareStatement(create);
			s.execute();
		}

		s = conn.prepareStatement("SELECT COUNT(*) FROM sqlite_master where type='table' and name='feedbackduty';");
		rs = s.executeQuery();
		rs.next();
		c = rs.getInt(1);
		if (c == 0) {

			String create = "CREATE TABLE `feedbackduty` (" + "`id` int(11) NOT NULL,"
					+ "`task_type` varchar(10) default NULL," + "`task_title` varchar(50) default NULL,"
					+ "`start_time` timestamp NOT NULL,"
					+ "`end_time` timestamp NOT NULL default '0000-00-00 00:00:00',"
					+ "`finishOrNot` tinyint(1) default NULL," + "`feedback` bolb default NULL,"
					+ "`teacher` varchar(20) default NULL," + "`class_name` varchar(20) default NULL,"
					+ "PRIMARY KEY  (`id`)" + ");";

			s = conn.prepareStatement(create);
			s.execute();
		}
		
		
		s = conn
				.prepareStatement("SELECT COUNT(*) FROM sqlite_master where type='table' and name='newgeneduty';");
		rs = s.executeQuery();
		rs.next();
		c = rs.getInt(1);
		if (c == 0) {

			String create = "CREATE TABLE `newgeneduty` (" + "`id` int(11),"
					+ "`task_type` varchar(10) default NULL," + "`task_title` varchar(20) default NULL,"
					+ "`start_time` timestamp NULL,"
					+ "`end_time` timestamp NOT NULL default '1999-01-01 00:00:01',"
					//+ "`finishOrNot` tinyint(1) default NULL," 
					+ "`needFeedBackOrNot` tinyint(1) default NULL,"
					//+ "`feedback` bolb default NULL," 
					  +"`day` varchar(20) default NULL,   "
					  +"`week` varchar(20) default NULL,  "
					  +"`month` varchar(20) default NULL, "
					  +"`year` varchar(20) default NULL,  "
					  +"`teacher` varchar(20) default NULL,"
					+ "`class_name` varchar(20) default NULL," 
					+ "`followingDuty` blob default NULL"  
					+ ");";

			s = conn.prepareStatement(create);

			s.execute();
		}
		
		s.close();
		conn.close();

	}

	public void refreshTasks() throws SQLException // 从本地SqlLite生成本地任务
	{

		List<Duty> dutys = GeneDutyFromLocalDB();

		for (Duty u : dutys) {
			this.addDuty(u);
		}
		this.submit();
		setAllDutyTrigger(DutyList.getInstance());

	}

	public void setAllDutyTrigger(DutyList dl) {
		List<Duty> list = dl.dutyList;
		for (int i = 0; i < list.size(); i++) {
			Duty a_duty = list.get(i);
			if (!Trigger.isTriggered(a_duty)) {
				setTrigger(a_duty);
			}

		}
		// the duties that are not appear in dutyList
		// go through trigger list, remove NOT valid duty
		Trigger.clean();
	}

	public void setTrigger(Duty d) {
		Trigger trig = new Trigger(d);
		
		// trig.addToTriggerList();
	}

	public void submit() {
		DutyList.getInstance().submit();
	}

	private boolean SyncLocalDB() throws SQLException //throws SQLException , ClassNotFoundException// 和服务器同步本地数据库

	{
		// 测试远程连接，失败则返回连接失败，跳出。
		Connection RemoteConn, LocalConn = null;
        try{
        	RemoteConn = getConnection();
        	if(RemoteConn==null)
        		return false;
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	return false;
        }
        //RemoteConn =null;
		// 从远程选取对应的客户端本日的任务

        PreparedStatement p = null;
		ResultSet Duty_rs = null ,unregularDuty_rs=null;
		//------------------------获取本班基本信息--------------------------
		String ClassName="测试";
		
		if(KYMainUI.isInstanced())
			ClassName=KYMainUI.getInstance().department;
		Calendar ca = Calendar.getInstance();//创建一个日期实例

		ca.setTime(new Date());//实例化一个日期

		int week=ca.get(Calendar.DAY_OF_WEEK);
		int month=ca.get(Calendar.DAY_OF_MONTH);
		int year=ca.get(Calendar.DAY_OF_YEAR);

		
		//-----------------------------------------------------
		try{
		p = RemoteConn.prepareStatement("select * from duty where class_name=? and (day='1' OR week=? OR month=? OR year=?);"); //需要加入限制本班的条件
		p.setString(1, ClassName);
		p.setInt(2, week);
		p.setInt(3, month);
		p.setInt(4, year);
		System.out.println(RemoteConn);
		System.out.println(p);
		Duty_rs = p.executeQuery();
		
		System.out.println(Duty_rs.next());
		
		
		Timestamp today_start = (new Timestamp(System.currentTimeMillis())),
				today_end = (new Timestamp(System.currentTimeMillis()));
		today_start.setHours(0);
		today_start.setMinutes(0);
		today_start.setSeconds(0);
		today_end.setHours(23);
		today_end.setMinutes(59);
		today_end.setSeconds(59);
		p = RemoteConn.prepareStatement("select * from unregularduty where class_name=? and (start_time <= ?) and (end_time >=? );" ); //需要加入限制本班的条件
		p.setString(1, ClassName);
		p.setTimestamp(2, today_end);
		p.setTimestamp(3, today_start);

		
		
		unregularDuty_rs = p.executeQuery();
        
        Class.forName("org.sqlite.JDBC");

		LocalConn = DriverManager.getConnection("jdbc:sqlite:VCLib.dll");
		// 清空本地常规任务和非常规任务表

		PreparedStatement p1 =  LocalConn.prepareStatement("delete from duty;"); //清空本地表
		p1.execute();
		p1 =  LocalConn.prepareStatement("delete from unregularduty;"); //清空本地表
		p1.execute();
		// 插入本地对应的表
		if(1==1/2)
			return true;
		while (Duty_rs.next()) {
					
			
			System.out.println("rowget:"+Duty_rs.getRow());
			 p1 = LocalConn.prepareStatement("INSERT INTO `duty`"
	           +"(`id`,"
	           +"`task_type`,"
	           +"`task_title`,"
	           +"`start_time`,"//                            "
	           +"`end_time`,"//                              "
	          // +"`finishOrNot`,"//                           "
	           +"`needFeedBackOrNot`,"//                     "
	           //+"`feedback`,"//                              "
	           +"`day`,"//                                   "
	           +"`week`,"//                                  "
	           +"`month`,"//                                 "
	           +"`year`, "//                                 "
	           +"`teacher`,"//                               "
	           +"`class_name`,"
	           + "`followingDuty`)" 
	           +"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);"
	           ); 
			 for(int i=1;i<=Duty_rs.getMetaData().getColumnCount();i++)
			 {
				 p1.setObject(i, Duty_rs.getObject(i));
			 }
			 
			// System.out.println(p1);
			 p1.execute();			
		}
		
		while (unregularDuty_rs.next()) {
			//System.out.println(unregularDuty_rs.getRow());
			 p1 = LocalConn.prepareStatement("INSERT INTO unregularduty"
                     +"(`id`,"
                     +"`task_type`,"
                     +"`task_title`,"
                     +"`start_time`,"
                     +"`end_time`,"
                    // +"`finishOrNot`,"
                     +"`needFeedBackOrNot`,"
                    // +"`feedback`,"
                     +"`teacher`,"
                     +"`class_name`,"
                     + "`followingDuty`)"
                     +"VALUES"
                     +"(?,?,?,?,?,?,?,?,?);"
	           ); 
			 for(int i=1;i<=unregularDuty_rs.getMetaData().getColumnCount();i++)
			 {
				 p1.setObject(i, unregularDuty_rs.getObject(i));
				 
			 }
			 
			// System.out.println(p1);
			 p1.execute();			
		}

		// 把本地反馈表的内容插入服务器,未实现功能

		
		p1 =  LocalConn.prepareStatement("select * from feedbackduty;"); //获得反馈
		
		ResultSet feedback= p1.executeQuery();
		
		while (feedback.next()) {
			//System.out.println(unregularDuty_rs.getRow());
			 p = RemoteConn.prepareStatement("INSERT INTO `feedbackduty`"
                                           +"(`id`,"
                                           +"`task_type`,"
                                           +"`task_title`,"
                                           +"`start_time`,"
                                           +"`end_time`,"
                                           +"`finishOrNot`,"
                                           +"`feedback`,"
                                           +"`teacher`,"
                                           +"`class_name`)"
                                           +"VALUES"
                                           +"(?,?,?,?,?,?,?,?,?);"); 
			 for(int i=1;i<=feedback.getMetaData().getColumnCount();i++)
			 {
				 p.setObject(i, feedback.getObject(i));
			 }
			 
			// System.out.println(p1);
			 p.execute();			
		}
		p1 =  LocalConn.prepareStatement("delete from feedbackduty;");//清空同步后的队列
		p1.execute();
		
		
		p1 =  LocalConn.prepareStatement("select * from newgeneduty;"); //获得后续任务
		
		ResultSet NewGeneDuty= p1.executeQuery();
		
		while (NewGeneDuty.next()) {
			//System.out.println(unregularDuty_rs.getRow());
			 p = RemoteConn.prepareStatement("INSERT INTO `unregularduty`"
					         +"(`id`,              "
							 +"`task_type`,        "
							 +"`task_title`,       "
							 +"`start_time`,       "
							 +"`end_time`,         "
							 +"`needFeedBackOrNot`,"
							 +"`teacher`,          "
							 +"`class_name`,       "
							 +"`followingDuty`)    "
							 +"VALUES              "
							 +"(?,?,?,?,?,?,?,?,?);"); 
			 for(int i=1;i<=NewGeneDuty.getMetaData().getColumnCount();i++)
			 {
				 p.setObject(i, NewGeneDuty.getObject(i));
			 }
			 
			// System.out.println(p1);
			 p.execute();			
		}
		
		p1 =  LocalConn.prepareStatement("delete from newgeneduty;");//清空同步后的队列
		
		p1.execute();
		
		RemoteConn.close();
		LocalConn.close();
		}catch(Exception e)
		{
			RemoteConn.close();
			LocalConn.close();
		}
		
		return true;
	}

	public void processedFeedbackLocal(Duty duty) {
		// TODO Auto-generated method stub
		
			try {
				Class.forName("org.sqlite.JDBC");
				Connection LocalConn = DriverManager.getConnection("jdbc:sqlite:VCLib.dll");
				PreparedStatement p = null;
				ResultSet r = null;
			
				p = LocalConn.prepareStatement("INSERT INTO feedbackduty "
                        + "(`duty_id`," //1
						+ "`task_type`,"//2
                        + "`task_title`,"//3
                        + "`start_time`,"//4
                        + "`end_time`,"//5
                        + "`finishOrNot`,"//6
                        + "`feedback`,"//7
                        + "`teacher`,"//8
                        + "`class_name`)"//9
                        + "VALUES"
                        + "(?,?,"
                        + "?,"
                        + "?,"
                        + "?,"
                        + "?,"
                        + "?,"
                        + "?,"
                        + "?);");
				
				p.setInt(1, duty.getId());
				p.setString(2, duty.getQuestType());
				p.setString(3, duty.getQuestTitle());
				p.setTimestamp(4, duty.getStartTime());
				KY.console.appendline("Start: "+duty.getStartTime().toString());
				KY.console.appendline("end: "+duty.getEndTime().toString());
				p.setTimestamp(5, duty.getEndTime());
				p.setInt(6, duty.isFinished()?1:0);
				try{
					
					System.out.println("duty"+ duty.getQuestTitle());
					System.out.println("type od duty"+(duty.getFeedback() instanceof OptionFeedBack));
					
					Object feedback =duty.getFeedback().getFeedBackMsg();
					p.setBytes(7, KYUtils.ObjectToByte(feedback));
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				p.setString(8, duty.getTeacher());
				p.setString(9, duty.getClassName());

				System.out.println(p.getMetaData());
				p.execute();
				

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
	}

}
