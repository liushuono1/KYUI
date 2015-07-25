package FinanceUI;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import bb.gui.ClientUtil;
import bb.gui.ServerActionException;
import bb.gui.base.ClientUI;
import bb.gui.swing.JDatePicker;
import bb.gui.swing.homepage.HomePageMultiRowSearchPane;

public class FinanceByTypeDateSearchPane extends HomePageMultiRowSearchPane {

	public FinanceByTypeDateSearchPane()
	 {
		  super(new String[] {"类别","开始日期:","截至日期:" },
				  new JComponent[] {  getTypeComboBox(),new JDatePicker(ClientUtil.getMonthFirstDay()), new JDatePicker()
		        }, "凭类别和日期搜索");

	 }
	
	 private static JComboBox getTypeComboBox()
	 {
		 JComboBox jb = new JComboBox();
			
			String types = txt2String("/FinanceUI/types.txt");
			String[] typeArray = types.trim().split(":");
			jb.addItem("");
			for(int i=0;i<typeArray.length;i++)
			{
				jb.addItem(typeArray[i]);
			}
			return jb;
	 }
	 
	 public static String txt2String(String file){
	        String result = "";
	        try{
	            BufferedReader br = new BufferedReader(new InputStreamReader(FinanceAddUI.class.getResourceAsStream(file)));//构造一个BufferedReader类来读取文件
		           //构造一个BufferedReader类来读取文件
	            String s = null;
	            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
	                result += s+"\n" ;
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return result;
	    }
	 
	@Override
	public ClientUI getSearchResultUI()
    throws ServerActionException
    {/*
		final String input = ((JComboBox)getInputComponent(0)).getSelectedItem().toString();
		  
		  Collection<OneRecord> recordList= FinanceUtil.searchByType( input);
		  FinanceSearchUI ui = new FinanceSearchUI((List<OneRecord>) recordList);
			*/
		  final String input = ((JComboBox)getInputComponent(0)).getSelectedItem().toString();
		  final Date from = ((JDatePicker)getInputComponent(1)).getSelectedDate();
		  final Date to = ((JDatePicker)getInputComponent(2)).getSelectedDate();
		  Collection<OneRecord> recordList = FinanceUtil.searchByTypeDate(input, from, to);
		 FinanceSearchUI ui = new FinanceSearchUI((List<OneRecord>)recordList);
		 return ui;
		 
    }
	
}
