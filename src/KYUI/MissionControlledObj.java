package KYUI;

import java.util.LinkedList;
import java.util.List;

public interface MissionControlledObj {
	
	public List<MissionControlAction> missionControlAction = new LinkedList<MissionControlAction>();
	
	public List<MissionControlAction> getAction();
	
	public void addAction(MissionControlAction action);

}
