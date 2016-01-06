package FinanceUI;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;

import AuthModule.AuthLV1;
import AuthModule.CardAuth;
import KYUI.KYMainUI;
import bb.gui.ClientUtil;
import bb.gui.ServerActionException;
import bb.gui.base.ClientUI;
import bb.gui.swing.JDatePicker;
import bb.gui.swing.homepage.HomePageMultiRowSearchPane;

public class FinanceByDateSearchPane extends HomePageMultiRowSearchPane implements AuthLV1{

	 public FinanceByDateSearchPane()
	 {
		  super(new String[] {"开始日期:","截至日期:" },
				  new JComponent[] {new JDatePicker(ClientUtil.getMonthFirstDay()), new JDatePicker()
		        }, "凭日期搜索记录");

	 }
	 
	 @Override
	public ClientUI getSearchResultUI()
     throws ServerActionException
     {
		 
		 if(CardAuth.ID_Auth(this, 0)==1)
		  {		
			  final Date from = ((JDatePicker)getInputComponent(0)).getSelectedDate();
			  final Date to = ((JDatePicker)getInputComponent(1)).getSelectedDate();
			  Collection<OneRecord> recordList= FinanceUtil.searchByDate(from, to);
			  FinanceSearchUI ui = new FinanceSearchUI((List<OneRecord>) recordList);
				
		      return ui;
		  } else
		  {
			  return new KYUI.BlankUI("请申请权限！！！");
		  }

     }

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return Level;
	}
	 
}
