package FinanceUI;

import javax.swing.JPanel;

import bb.gui.base.BaseHomePage;
import bb.gui.hr.EmployeeHomePageByEmployeeIDSearchPane;
import bb.gui.hr.EmployeeHomePageByLastNameSearchPane;
import bb.gui.swing.MulHomePage;
import bb.gui.swing.homepage.HomePageGroupPane;

public class Homepage extends BaseHomePage {
	
	public Homepage()
	{
		super(2);
		this.setTitle("财务主页");
		HomePageGroupPane group = getTabPane(0).getGroupPane(0);
	
		FinanceAddButton fui = new FinanceAddButton();
		group.add(fui);
		group.addSearchPane(fui);
		
		group.addSearchPane(null,"类别");
		JPanel panel1 = new FinanceByTypeSearchPane();
		group.add(panel1);
		
		group.addSearchPane(null,"日期");
		JPanel panel2 = new FinanceByDateSearchPane();
		group.add(panel2);

		group.addSearchPane(null,"详情");
		JPanel panel4 = new FinanceByDetailSearchPane();
		group.add(panel4);
		
		HomePageGroupPane group1 = getTabPane(0).getGroupPane(1);
		
		group1.addSearchPane(null,"类别和日期");
		JPanel panel3 = new FinanceByTypeDateSearchPane();
		group1.add(panel3);
		
		group1.addSearchPane(null,"查看现金余额");
		JPanel panel5 = new GetCashTotalPane(null);
		group1.add(panel5);
		  
		  
		group1.addSearchPane(null,"查看以前余额");
		JPanel panel6 = new  getCashTillDatePane(null);
		group1.add(panel6);  
		 
		
	}
}
