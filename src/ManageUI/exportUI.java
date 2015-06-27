package ManageUI;

import bb.gui.base.BaseHomePage;
import bb.gui.hr.HumanResourceUtil;
import bb.gui.swing.homepage.HomePageGroupPane;


public class exportUI extends BaseHomePage{

	public exportUI()
	{
		super(2);
		HomePageGroupPane group0 = getTabPane(0).getGroupPane(0);
		MedcinePanel mp = new MedcinePanel();
		group0.add(mp.medicinePanel);
		
		MorningNoonCheck mnc = new MorningNoonCheck();
		group0.add(mnc.panel);
		

		HomePageGroupPane group1 = getTabPane(0).getGroupPane(1);
		MonthAttendence ma = new MonthAttendence();
		group1.add(ma.attendencePanel);//±¾ÔÂ³öÇÚ
		setTitle(HumanResourceUtil.getString("EmployeeHomePage.Title"));
	}
}
