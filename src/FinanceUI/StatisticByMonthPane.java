package FinanceUI;

import java.util.Collection;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import bb.gui.ServerActionException;
import bb.gui.base.ClientUI;
import bb.gui.swing.homepage.HomePageMultiRowSearchPane;

public class StatisticByMonthPane extends HomePageMultiRowSearchPane {
	public StatisticByMonthPane()
	{
		super(new String[] {"年月:" },
				  new JComponent[] { getYearMonthComboBox()}, "生成财务报表");
	}
	
	 private static JComboBox getYearMonthComboBox()
	 {
		 JComboBox jb = new JComboBox();
		 String str = "";
		 for(int i=2014;i<3000;i++)
		 {
			 String year = String.valueOf(i)+"年"; 
			 for(int j=1;j<13;j++)
			 {
				 if(i == 2014)
				 {
					 if(j >=9){
						 str += year+String.valueOf(j)+"月";
						 jb.addItem(str);
					 }
				 }
				 else
				 {
					 str += year+String.valueOf(j)+"月";
					 jb.addItem(str);
				 }
				 str = "";
			 }
		 }
		 return jb;
	 }
	 
	  @Override
	public ClientUI getSearchResultUI()       
	  		throws ServerActionException
	  {
		  final String input = ((JComboBox)getInputComponent(0)).getSelectedItem().toString();
		  System.out.println("input>>"+input);
		  Collection<OneRecord> recordList= FinanceUtil.getRecordCollection(input);
		  Collection<OneRecord> validRecords = FinanceUtil.getRecordSoFar(input);
		  FinanceReportUI ui = new FinanceReportUI(recordList, validRecords, input);
	      return ui;
	  }
}
