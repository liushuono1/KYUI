package KYUI;

import java.util.List;

public abstract class MissionControl implements Runnable {

	MissionControlledObj Obj;
	List<MissionControlAction> actions;
	
	protected MissionControl(MissionControlledObj Obj)
	{
		this.Obj=Obj;
		actions = this.Obj.getAction();
	}
	@Override
	public void run() {
	}
	
	
	public MissionControlledObj getControlledObject()
	{
		return Obj;
	}

	public MissionControlAction getAction(String name)
	{
		for(MissionControlAction action:actions)
		{
			if(action.getName().equals(name))
				return action;
		}
		
		return new MissionControlAction(){

			@Override
			public void setname(String name) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void StratAction(String[] parms) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void FinishAction(String[] parms) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void TimeRepeatAction(String[] parms) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void RepeatAction(String[] parms) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}
}
