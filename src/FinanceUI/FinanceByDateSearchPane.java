package FinanceUI;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import AuthModule.CardAuth;
import KYUI.KYMainUI;
import bb.gui.ClientUtil;
import bb.gui.ServerActionException;
import bb.gui.base.ClientUI;
import bb.gui.swing.JDatePicker;
import bb.gui.swing.homepage.HomePageMultiRowSearchPane;

public class FinanceByDateSearchPane extends HomePageMultiRowSearchPane {

	 public FinanceByDateSearchPane()
	 {
		  super(new String[] {"��ʼ����:","��������:" },
				  new JComponent[] {new JDatePicker(ClientUtil.getMonthFirstDay()), new JDatePicker()
		        }, "ƾ����������¼");

	 }
	 
	 public ClientUI getSearchResultUI()
     throws ServerActionException
     {
		 
		 if(CardAuth.ID_Auth(KYMainUI.getInstance(), 0)==1)
		  {		
			  final Date from = ((JDatePicker)getInputComponent(0)).getSelectedDate();
			  final Date to = ((JDatePicker)getInputComponent(1)).getSelectedDate();
			  Collection<OneRecord> recordList= FinanceUtil.searchByDate(from, to);
			  FinanceSearchUI ui = new FinanceSearchUI((List<OneRecord>) recordList);
				
		      return ui;
		  } else
		  {
			  return new KYUI.BlankUI("������Ȩ�ޣ�����");
		  }

     }
	 
}
