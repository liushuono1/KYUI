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
		this.setTitle("������ҳ");
		HomePageGroupPane group = getTabPane(0).getGroupPane(0);
	
		FinanceAddButton fui = new FinanceAddButton();
		group.add(fui);
		group.addSearchPane(fui);
		
		group.addSearchPane(null,"���");
		JPanel panel1 = new FinanceByTypeSearchPane();
		group.add(panel1);
		
		group.addSearchPane(null,"����");
		JPanel panel2 = new FinanceByDateSearchPane();
		group.add(panel2);

		group.addSearchPane(null,"����");
		JPanel panel4 = new FinanceByDetailSearchPane();
		group.add(panel4);
		
		HomePageGroupPane group1 = getTabPane(0).getGroupPane(1);
		
		group1.addSearchPane(null,"��������");
		JPanel panel3 = new FinanceByTypeDateSearchPane();
		group1.add(panel3);
		
		group1.addSearchPane(null,"�鿴�ֽ����");
		JPanel panel5 = new GetCashTotalPane(null);
		group1.add(panel5);
		  
		  
		group1.addSearchPane(null,"�鿴��ǰ���");
		JPanel panel6 = new  getCashTillDatePane(null);
		group1.add(panel6);  
		 
		
	}
}
