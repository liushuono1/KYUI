package Simple.Registration;

import bb.gui.base.BaseHomePage;
import bb.gui.base.ClientUI;
import bb.gui.hr.EmployeeHomePageByLastNameSearchPane;
import bb.gui.hr.EmployeeHomePageLookupSearchPane;
import bb.gui.hr.EmployeeHomePageTrainingSearchPane;
import bb.gui.hr.HumanResourceUtil;
import bb.gui.swing.MulHomePage;
import bb.gui.swing.homepage.HomePageGroupPane;
import bb.gui.ware.label.LabelHomePageMakeEmployeeLabels;



public class RegHome extends BaseHomePage {

	public RegHome() {
		super(1);
		this.setTitle("ע��ѧ����ҳ");
		HomePageGroupPane group = getTabPane(0).getGroupPane(0);
		newStu ui= new  newStu(0)
		{
			public String getButtonText() {
				return "�����µı�һ��ѧ��";
			}
		};
		group.addSearchPane(ui);
		
		 ui= new  newStu(1)
		{
			public String getButtonText() {
				return "�����µı�����ѧ��";
			}
		};
		group.addSearchPane(ui);
		
		 ui= new  newStu(2)
		{
			public String getButtonText() {
				return "�����µ�Сһ��ѧ��";
			}
		};
		group.addSearchPane(ui);
		 ui= new  newStu(3)
		{
			public String getButtonText() {
				return "�����µ�С����ѧ��";
			}
		};
		group.addSearchPane(ui);
		
		 ui= new  newStu(4)
		{
			public String getButtonText() {
				return "�����µ��а�ѧ��";
			}
		};
		group.addSearchPane(ui);
		
		 ui= new  newStu(5)
		{
			public String getButtonText() {
				return "�����µĴ��ѧ��";
			}
		};
		group.addSearchPane(ui);
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