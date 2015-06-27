package ManageUI;

import bb.gui.base.BaseHomePage;
import bb.gui.swing.homepage.HomePageGroupPane;


public class teacherExamineUI  extends BaseHomePage{

	public teacherExamineUI()
	{
		super(1);
		HomePageGroupPane group0 = getTabPane(0).getGroupPane(0);
		TeacherExaminePanel tep = new TeacherExaminePanel();
		group0.add(tep.examinePanel);
		
	}
}
