package FinanceUI;

import bb.gui.base.BaseHomePage;
import bb.gui.swing.homepage.HomePageGroupPane;

public class StatisticHomepage extends BaseHomePage {
	public StatisticHomepage()
	{
		super(1);
		this.setTitle("统计主页");
		HomePageGroupPane group = getTabPane(0).getGroupPane(0);
	
		group.addSearchPane(null,"月报表");
		//JPanel panel1 = new FinanceByTypeSearchPane();
		//group.add(panel1);
		
		StatisticByMonthPane fui = new StatisticByMonthPane();
		group.add(fui);
		/*
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
		*/
	}
}
