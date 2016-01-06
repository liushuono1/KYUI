package FinanceUI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import AuthModule.AuthLV1;
import AuthModule.CardAuth;
import KYUI.KYMainUI;
import bb.gui.ServerActionException;
import bb.gui.base.ClientUI;
import bb.gui.swing.homepage.HomePageMultiRowSearchPane;

public class FinanceByTypeSearchPane extends HomePageMultiRowSearchPane implements AuthLV1{

	public FinanceByTypeSearchPane()
	{
		
		  super(new String[] {"类别"}, new JComponent[] {
		            getTypeComboBox()
		        }, "按类别搜索");
	}
	
	  @Override
	public ClientUI getSearchResultUI()       throws ServerActionException
	  {
		  if(CardAuth.ID_Auth(this, 0)==1)
		  {		
		  final String input = ((JComboBox)getInputComponent(0)).getSelectedItem().toString();
		  
		  Collection<OneRecord> recordList= FinanceUtil.searchByType( input);
		  FinanceSearchUI ui = new FinanceSearchUI((List<OneRecord>) recordList);
			
	      return ui;
		  } else
		  {
			  return new KYUI.BlankUI("请申请权限！！！");
		  }
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
	public int getLevel() {
		// TODO Auto-generated method stub
		return Level;
	}
	 
}
