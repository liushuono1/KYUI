package kyHRUI.Stuff;

import java.util.Collection;
import KYUI.KYMainUI;
import bb.gui.base.BaseHomePage;
import bb.gui.base.ClientUI;
import bb.gui.swing.homepage.HomePageGroupPane;



public class RegHome extends BaseHomePage {

	public RegHome() {
		super(1);
		this.setTitle("ע��Ա����ҳ");
		HomePageGroupPane group = getTabPane(0).getGroupPane(0);
		newStuff ui= new newStuff(KYMainUI.department)
		{
			@Override
			public String getButtonText() {
				return "����µ�"+KYMainUI.department+"Ա��";
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

	public RegHome(Collection<String> depts) {
		super(1);
		this.setTitle("ע��ѧ����ҳ");
		HomePageGroupPane group = getTabPane(0).getGroupPane(0);//�̹�����Ҫ�����ݶ����ȡ
	
			newStuff ui= new newStuff("�̹���")
			{
				@Override
				public String getButtonText() {
					return "����µ�Ա��";
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