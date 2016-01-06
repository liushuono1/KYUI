package FinanceUI;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import AuthModule.AuthLV1;
import AuthModule.CardAuth;
import bb.gui.base.ClientUI;
import kyHRUI.Student.BasicActionPane;

public class GetTotalPane extends BasicActionPane implements AuthLV1{
String account;
	public GetTotalPane(String account) {
		super(null);
		this.account=account;
		// TODO Auto-generated constructor stub
	}

	@Override
	public ClientUI getSearchResultUI() {
		
		Collection<OneRecord> records = FinanceUtil.getRecordsByAccount(new java.sql.Date(System.currentTimeMillis()).toString(),account);
		final double balance = this.calculateBalance(records);
		if(balance != 0)
		{
			if(CardAuth.ID_Auth(this, 0)==1)
			{
				JOptionPane.showMessageDialog(null, "���Ϊ"+String.valueOf(balance)+"Ԫ");
			}else
			{
				JOptionPane.showMessageDialog(null,"������Ȩ�ޣ�����");
			}
			
		}
		else
		{
			JOptionPane.showMessageDialog(null, "û�м�¼!");
		}

		return null;
	}
	

	public double calculateBalance(Collection<OneRecord> records)
	{
		double balance;
		List<OneRecord> record_list = new LinkedList<OneRecord>();
		record_list.addAll(records);
		double sum_income = 0;
		double sum_expense = 0;
		if(record_list.size() != 0)
		{
			for(int i=0;i<record_list.size();i++)
			{
				OneRecord a_record = record_list.get(i);
				double income = a_record.getIncome();
				sum_income += income;
				double expense = a_record.getExpense();
				sum_expense += expense;
			}
			balance = sum_income - sum_expense;
			balance=Math.round(balance * 100) * 0.01d;
			//balance=((int)(balance*100))/100;   
 
		}
		else
		{
			balance = 0;
		}
		return balance;
	}

	
	@Override
	public String getButtonText() {
		return "��ѯ���";
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return Level;
	}
}
