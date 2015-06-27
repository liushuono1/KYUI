package KYUI;

public interface MissionControlAction {
	
	public void setname(String name);
	
	public String getName();
	
	public  void StratAction(String[] parms);
	
	public  void FinishAction(String[] parms);
	
	public  void TimeRepeatAction(String[] parms);
	
	public  void RepeatAction(String[] parms);

}
