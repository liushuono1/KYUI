package kyHRUI.Student;

import java.util.Collection;
import KYUI.KYMainUI;
import bb.gui.base.BaseHomePage;
import bb.gui.base.ClientUI;
import bb.gui.hr.EmployeeActionManager;
import bb.gui.swing.homepage.HomePageGroupPane;



public class RegHome extends BaseHomePage {

	public RegHome() {
		super(1);
		
		if(KYMainUI.department.equals("")||
				KYMainUI.department.equals("园长办公室") ||
				KYMainUI.department.equals("教工部") ||
				KYMainUI.department.equals("综合办公室"))
		{
					Collection<String> deptss= EmployeeActionManager.getDepartmentList();
    	
					deptss.remove("园长办公室");
					deptss.remove("教工部");
					deptss.remove("综合办公室");
					deptss.remove("");
					this.setTitle("注册学生主页");
					HomePageGroupPane group = getTabPane(0).getGroupPane(0);
					
					for(final String dept:deptss)
					{
						newStu ui= new newStu(dept)
						{
							@Override
							public String getButtonText() {
								return "添加新的"+dept+"学生";
							}
						};
						group.addSearchPane(ui);
					}
					
		}else
		{
		
			this.setTitle("注册学生主页");
			HomePageGroupPane group = getTabPane(0).getGroupPane(0);
			newStu ui= new newStu(KYMainUI.department)
			{
				@Override
				public String getButtonText() {
					return "添加新的"+KYMainUI.department+"学生";
				}
			};
			group.addSearchPane(ui);
		}
		
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