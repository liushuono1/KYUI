package FinanceUI;

import java.util.Collection;
import java.util.List;

import AuthModule.CardAuth;
import KYUI.KYMainUI;
import bb.gui.ServerActionException;
import bb.gui.base.ClientUI;
import bb.gui.swing.homepage.HomePageSingleTextFieldSearchPane;

public class FinanceByDetailSearchPane extends HomePageSingleTextFieldSearchPane {

	public FinanceByDetailSearchPane()
	{
		super("按详情搜索", true);
	}
	
	 @Override
	protected ClientUI getSearchResultUI(final String input)
     throws ServerActionException
     {
		 
		 if(CardAuth.ID_Auth(KYMainUI.getInstance(), 0)==1)
		  {		
			   Collection<OneRecord> recordList =  FinanceUtil.searchByDetail(input);
			   FinanceSearchUI ui = new FinanceSearchUI((List<OneRecord>) recordList);
			   return ui;
		  } else
		  {
			  return new KYUI.BlankUI("请申请权限！！！");
		  }

		 
		 

     }
}
