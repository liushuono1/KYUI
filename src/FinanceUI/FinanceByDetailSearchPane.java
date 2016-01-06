package FinanceUI;

import java.util.Collection;
import java.util.List;

import AuthModule.AuthLV1;
import AuthModule.CardAuth;
import KYUI.KYMainUI;
import bb.gui.ServerActionException;
import bb.gui.base.ClientUI;
import bb.gui.swing.homepage.HomePageSingleTextFieldSearchPane;

public class FinanceByDetailSearchPane extends HomePageSingleTextFieldSearchPane implements AuthLV1{

	public FinanceByDetailSearchPane()
	{
		super("����������", true);
	}
	
	 @Override
	protected ClientUI getSearchResultUI(final String input)
     throws ServerActionException
     {
		 
		 if(CardAuth.ID_Auth(this, 0)==1)
		  {		
			   Collection<OneRecord> recordList =  FinanceUtil.searchByDetail(input);
			   FinanceSearchUI ui = new FinanceSearchUI((List<OneRecord>) recordList);
			   return ui;
		  } else
		  {
			  return new KYUI.BlankUI("������Ȩ�ޣ�����");
		  }

		 
		 

     }

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return Level;
	}
}
