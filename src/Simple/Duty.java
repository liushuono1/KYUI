package Simple;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Duty implements Serializable{
	private static long serialVersionUID=827381798489012L;
	Timestamp startTime;
	Timestamp endTime;
	String questType;
	String questTitle;
	boolean isFinished;
	boolean isFeedbackable;
	FeedBackObj feedback;
	String teacher;
	String className;
	List<Object> duty;
	int id;
	boolean trigged;
	Trigger trig;
	Timer FinishTimer;
	Duty followingDuty=null;
	public Duty getFollowingDuty() {
		return followingDuty;
	}

	public void setFollowingDuty(Duty followingDuty) {
		this.followingDuty = followingDuty;
	}

	public Trigger getTrigger() {
		return trig;
	}

	private Duty getself() {
		return this;
	}

	public void setTrigger(Trigger trig) {

		this.trig = trig;
	}

	private void startFinishTimer() {
		// TODO Auto-generated method stub
		// 一天的毫秒数
		long daySpan = 24 * 60 * 60 * 1000;
		Date FinishTime = null;
		System.out.println(this.getEndTime().toString());
		
		FinishTime = new Date(this.getEndTime().getTime());
		
		// 如果今天的已经过了 首次运行时间就改为明天
		if (System.currentTimeMillis() > FinishTime.getTime()) {
			FinishTime = new Date(FinishTime.getTime() + daySpan);
		}

		FinishTimer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// 要执行的代码
				getself().finishing();
			}
		};
		// 以每24小时执行一次

		System.out.println(FinishTime);
		FinishTimer.schedule(task, FinishTime);
		//FinishTimer.scheduleAtFixedRate(task, startTime, daySpan);

	}

	protected void finishing() {
		// TODO Auto-generated method stub
		StackTraceElement[] stack = (new Throwable()).getStackTrace();

		System.out.println(stack[1].getMethodName());
		System.out.println("=======finishing");
		
		this.setFinished(true);
		FinishTimer.cancel();
		
		if (this.isFeedbackable)
			this.addFeedback();

	}

	private void addFeedback() {
		// TODO Auto-generated method stub
		// this.getFeedback();
		TaskManager.getInstance().processedFeedbackLocal(this);
		
	}

	public int getId() {
		return id;
	}

	public boolean isTrigged() {
		return trigged;
	}

	public void setTrigged(boolean trigged) {
		if (trigged) {
			startFinishTimer();
		}
		this.trigged = trigged;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Duty() {

	}

	private void refreshList() {
		duty = new LinkedList<Object>();
		duty.add(this.id);
		duty.add(this.questType);// pe
		duty.add(this.questTitle);// title
		duty.add(this.startTime);
		duty.add(this.endTime);
		duty.add(this.isFeedbackable);
		duty.add(this.teacher);
		duty.add(this.className);
		duty.add(this.isFinished);
		duty.add(this.feedback);
	}

	public int size() {
		return duty.size();
	}

	public void setNoFeedBack() // 类型1，不需要反馈
	{
		this.setFeedback(new NoFeedback());
	}

	public void setOptionFeedBack(List Options) // 类型1，不需要反馈
	{
		this.setFeedback(new OptionFeedBack(Options){
			public void dutyFinishing()
			{
				System.err.println("OUTOUTOUT");
				finishing();//联动反馈面板按钮，
			}
		});
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {

		this.startTime = startTime;
		refreshList();
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
		refreshList();
	}

	public String getQuestType() {
		return questType;
	}

	public void setQuestType(String questType) {
		this.questType = questType;
		refreshList();
	}

	public String getQuestTitle() {
		return questTitle;
	}

	public void setQuestTitle(String questTitle) {
		this.questTitle = questTitle;
		refreshList();
	}

	public String getTeacher() {
		return this.teacher;
	}

	public String getClassName() {
		return this.className;

	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
		refreshList();
	}

	public boolean isFeedbackable() {
		return isFeedbackable;
	}

	public void setFeedbackable(boolean isFeedbackable) {
		this.isFeedbackable = isFeedbackable;
		refreshList();
	}

	public FeedBackObj getFeedback() {
		return feedback;
	}

	public void setFeedback(FeedBackObj feedback) {
		this.feedback = feedback;
		refreshList();
	}

	public void setTeacher(String teacherName) {
		this.teacher = teacherName;
		refreshList();
	}

	public void setClass(String className) {
		this.className = className;
		refreshList();
	}

	public boolean isSame(Duty d2) {
		boolean same = true;
		List<Object> d1_obj = this.duty;
		List<Object> d2_obj = d2.duty;
		for (int i = 0; i < d1_obj.size() - 2; i++) {
			// System.out.println(d1_obj.get(i)+"\n"+d2);
			if (d1_obj.get(i) != null && d2_obj.get(i) != null) {
				if (d1_obj.get(i) instanceof Timestamp) {
					SimpleDateFormat dateformatAll = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义返回的日期格式
					// dateformatAll.format(getCurrentTimestamp());

					if (!dateformatAll.format(d1_obj.get(i)).toString()
							.equals(dateformatAll.format(d2_obj.get(i)).toString())) {

						same = false;
						break;
					}
				} else if (!d1_obj.get(i).toString().equals(d2_obj.get(i).toString())) {

					same = false;
					break;
				}
			} else if (d1_obj.get(i) == null && d2_obj.get(i) == null) {

			} else {
				same = false;
				break;
			}
		}
		// System.err.println("DUTY1"+d1_obj+"\n"+"DUTY2"+d2_obj+"\n"+same);
		return same;
	}

	public String toString() {
		String output = "";

		if (isTrigged() && !isFinished()) // 被触发的状态只显示标题
		{
			output = this.questTitle;
		} else {
			/*
			 * System.out.println(questType); System.out.println(questTitle);
			 * System.out.println(startTime.toString());
			 * System.out.println(endTime.toString());
			 */
			output += this.id + "\t" + this.questType + "\t" + this.questTitle + "\t" + this.startTime.toString() + "\t"
					+ this.endTime.toString() + "\t";
			if (isFinished) {
				output += "already finish\t";
			} else {
				output += "have not finish\t";
			}

			if (isFeedbackable) {
				output += "needs feedback\t";
			} else {
				output += "does not need feedback\t";
			}
			// output += feedback.getFeedBackMsg()+"\t";
			output += this.teacher + "\t" + this.className;
		}

		return output;
	}

	public static void maina(String args[]) {
		// final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss");

		final SimpleDateFormat sdf = new SimpleDateFormat("2015-11-11 12:00:59");

		// 首次运行时间
		Date startTime = null;
		try {

			startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-11-11 12:00:59");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println(startTime);
	}
}