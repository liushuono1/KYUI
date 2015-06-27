package kyHRUI.Stuff;

import bb.gui.base.BaseHomePage;
import bb.gui.swing.MulHomePage;
import bb.gui.swing.homepage.HomePageGroupPane;


public class SearchHome extends BaseHomePage {

	public SearchHome() {
		super(1);
		this.setTitle("注册主页");
		HomePageGroupPane group = getTabPane(0).getGroupPane(0);
		
		group.addSearchPane(null,"查找");
		MulHomePage lookUpAndSearchPane = new MulHomePage();
		javax.swing.JPanel pane  = new EmployeeHomePageByIDSearchPane();
		lookUpAndSearchPane.add(pane);
		group.addSearchPane(lookUpAndSearchPane);
		//group.addSearchPane(null,"这是自定义的分格栏标签");
		/*MulHomePage lookUpAndSearchPane = new MulHomePage();
		javax.swing.JPanel pane;
		javax.swing.JPanel pane = new EmployeeHomePageLookupSearchPane();
		lookUpAndSearchPane.add(pane);
		pane = new EmployeeHomePageByEmployeeIDSearchPane();
		lookUpAndSearchPane.add(pane);
		group.addSearchPane(lookUpAndSearchPane);
		pane = new EmployeeHomePageByLastNameSearchPane();
		group.addSearchPane(pane);
		group.addSearchPane(null, HumanResourceUtil
				.getString("EmployeeHomePage.EmployeeTraining"));
		pane = new EmployeeHomePageTrainingSearchPane();
		group.addSearchPane(pane);
		group.addSearchPane(new LabelHomePageMakeEmployeeLabels());*/
	}
}