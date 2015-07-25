package Simple.Registration;

import bb.gui.base.BaseHomePage;
import bb.gui.base.ClientUI;
import bb.gui.swing.homepage.HomePageGroupPane;



public class RegHome extends BaseHomePage {

	public RegHome() {
		super(1);
		this.setTitle("注册学生主页");
		HomePageGroupPane group = getTabPane(0).getGroupPane(0);
		newStu ui= new  newStu(0)
		{
			@Override
			public String getButtonText() {
				return "添加新的宝一班学生";
			}
		};
		group.addSearchPane(ui);
		
		 ui= new  newStu(1)
		{
			@Override
			public String getButtonText() {
				return "添加新的宝二班学生";
			}
		};
		group.addSearchPane(ui);
		
		 ui= new  newStu(2)
		{
			@Override
			public String getButtonText() {
				return "添加新的小一班学生";
			}
		};
		group.addSearchPane(ui);
		 ui= new  newStu(3)
		{
			@Override
			public String getButtonText() {
				return "添加新的小二班学生";
			}
		};
		group.addSearchPane(ui);
		
		 ui= new  newStu(4)
		{
			@Override
			public String getButtonText() {
				return "添加新的中班学生";
			}
		};
		group.addSearchPane(ui);
		
		 ui= new  newStu(5)
		{
			@Override
			public String getButtonText() {
				return "添加新的大班学生";
			}
		};
		group.addSearchPane(ui);
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

	public static ClientUI getInstance() {
		// TODO Auto-generated method stub
		return new RegHome();
	}
}