package FinanceUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import AuthModule.CardAuth;
import KYUI.KYMainUI;
import bb.gui.base.ClientUI;

public class RecentRecords extends ClientUI{

	JTable table;
	public RecentRecords()
	{

		{
			JPanel panel = new JPanel();
			panel.setBorder(BorderFactory.createTitledBorder("显示录入信息"));
			panel.setLayout(new BorderLayout());
			table = createTable();
			
			//table.setSize(panel.getWidth(), panel.getHeight());
			JScrollPane ScrollBtnPanel = new JScrollPane(table);
			ScrollBtnPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			ScrollBtnPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			panel.add(BorderLayout.CENTER,ScrollBtnPanel);
			panel.add(BorderLayout.SOUTH,refreashBtn());
			
			this.add(panel);
		}
		
		if(CardAuth.ID_Auth(KYMainUI.getInstance(), 0)!=1)
		{
			clearTable();
		}
	}
	
	public JButton refreashBtn()
	{
		JButton btn= new JButton("刷新");
		btn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(CardAuth.ID_Auth(KYMainUI.getInstance(), 0)==1)
				{
					 refreashTable();
				}
				else
				{
					 clearTable();
				}
			}
			
		});
		return btn;
	}
	
	private void clearTable()
	{
		String attributeNames = FinanceAddUI.txt2String("/FinanceUI/attributes.txt");
		String[] attributeArray = attributeNames.trim().split(":");
		String[][] aaa= new String[1][attributeNames.length()];
		
		List<OneRecord> record_list = (List<OneRecord>) FinanceUtil.getLastTenRecords();
		int rows = record_list.size();
		int columns = attributeArray.length;
		 Object[][] records = new Object[rows][columns];
		 for(int i=0;i<rows;i++)
		 {
			 OneRecord r = record_list.get(i);
			 for(int j=0;j<columns;j++)
			 {
				 records[i][j] = "";//row*column
		     }	
		 }
		 
		table.setModel(MutliTableModel(records, attributeArray));
	}
	
	private void refreashTable()
	{
		String attributeNames = FinanceAddUI.txt2String("/FinanceUI/attributes.txt");
		String[] attributeArray = attributeNames.trim().split(":");
		String[][] aaa= new String[1][attributeNames.length()];
		
		List<OneRecord> record_list = (List<OneRecord>) FinanceUtil.getLastTenRecords();
		int rows = record_list.size();
		int columns = attributeArray.length;
		 Object[][] records = new Object[rows][columns];
		 for(int i=0;i<rows;i++)
		 {
			 OneRecord r = record_list.get(i);
			 for(int j=0;j<columns;j++)
			 {
				 records[i][j] = r.getString(j+1);//row*column
		     }	
		 }
		 
		table.setModel(MutliTableModel(records, attributeArray));
	}
	
	private JTable createTable()
	{
		String attributeNames = FinanceAddUI.txt2String("/FinanceUI/attributes.txt");
		String[] attributeArray = attributeNames.trim().split(":");
		String[][] aaa= new String[1][attributeNames.length()];
		
		List<OneRecord> record_list = (List<OneRecord>) FinanceUtil.getLastTenRecords();
		int rows = record_list.size();
		int columns = attributeArray.length;
		 Object[][] records = new Object[rows][columns];
		 for(int i=0;i<rows;i++)
		 {
			 OneRecord r = record_list.get(i);
			 for(int j=0;j<columns;j++)
			 {
				 records[i][j] = r.getString(j+1);//row*column
		     }	
		 }
		
		
		
		JTable table = new JTable(MutliTableModel(aaa,attributeArray));
		table.setModel(MutliTableModel(records, attributeArray));
		
		return table;
	}
	
	
	 private DefaultTableModel MutliTableModel(Object[][] items,String[] Attributes)
	 {
		 return new DefaultTableModel(items,Attributes);	
	 }
	 
	 private DefaultTableModel SingleTableModel(List itemList)
	 {
		 
		 int rows= itemList.size();
		 Object[][] x=  new Object[rows][1];
		 for(int i=0;i<rows;i++)
		 {
			 x[i][0]=itemList.get(i);
	     }	
		 return new DefaultTableModel(x, new String[]{"测试行1"});	
	 }
}
