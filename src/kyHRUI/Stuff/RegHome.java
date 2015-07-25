package kyHRUI.Stuff;

import java.util.Collection;
import KYUI.KYMainUI;
import bb.gui.base.BaseHomePage;
import bb.gui.base.ClientUI;
import bb.gui.swing.homepage.HomePageGroupPane;



public class RegHome extends BaseHomePage {

	public RegHome() {
		super(1);
		this.setTitle("注册员工主页");
		HomePageGroupPane group = getTabPane(0).getGroupPane(0);
		newStuff ui= new newStuff(KYMainUI.department)
		{
			@Override
			public String getButtonText() {
				return "添加新的"+KYMainUI.department+"员工";
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

	public RegHome(Collection<String> depts) {
		super(1);
		this.setTitle("注册学生主页");
		HomePageGroupPane group = getTabPane(0).getGroupPane(0);//教工部需要从数据定义读取
	
			newStuff ui= new newStuff("教工部")
			{
				@Override
				public String getButtonText() {
					return "添加新的员工";
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