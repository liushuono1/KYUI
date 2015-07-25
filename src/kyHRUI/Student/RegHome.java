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
				KYMainUI.department.equals("԰���칫��") ||
				KYMainUI.department.equals("�̹���") ||
				KYMainUI.department.equals("�ۺϰ칫��"))
		{
					Collection<String> deptss= EmployeeActionManager.getDepartmentList();
    	
					deptss.remove("԰���칫��");
					deptss.remove("�̹���");
					deptss.remove("�ۺϰ칫��");
					deptss.remove("");
					this.setTitle("ע��ѧ����ҳ");
					HomePageGroupPane group = getTabPane(0).getGroupPane(0);
					
					for(final String dept:deptss)
					{
						newStu ui= new newStu(dept)
						{
							@Override
							public String getButtonText() {
								return "����µ�"+dept+"ѧ��";
							}
						};
						group.addSearchPane(ui);
					}
					
		}else
		{
		
			this.setTitle("ע��ѧ����ҳ");
			HomePageGroupPane group = getTabPane(0).getGroupPane(0);
			newStu ui= new newStu(KYMainUI.department)
			{
				@Override
				public String getButtonText() {
					return "����µ�"+KYMainUI.department+"ѧ��";
				}
			};
			group.addSearchPane(ui);
		}
		
    	//group.addSearchPane(null,"�����Զ���ķָ�����ǩ");
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