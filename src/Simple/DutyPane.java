package Simple;

import javax.swing.JPanel;

public class DutyPane extends JPanel {

	public static JPanel instance;
	DutyPane ()
	{
		instance=this;
		//任务列表~~
	}
	
	
	public static JPanel getInstance()
	{
		if(instance==null)
		{
			new DutyPane();
		}
		
		return instance;
	}
	
}
