package FinanceUI;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import AuthModule.CardAuth;
import KYUI.KYMainUI;
import bb.gui.ServerActionException;
import bb.gui.base.AddActionPane;
import bb.gui.base.ClientUI;
import bb.gui.hr.HumanResourceUtil;
import bb.gui.swing.homepage.HomePageAddPane;
import bb.gui.swing.homepage.HomePageMultiRowSearchPane;
import bb.gui.swing.homepage.HomePageSingleTextFieldSearchPane;

public class FinanceByTypeSearchPane extends HomePageMultiRowSearchPane{

	public FinanceByTypeSearchPane()
	{
		
		  super(new String[] {"类别"}, new JComponent[] {
		            getTypeComboBox()
		        }, "按类别搜索");
	}
	
	  public ClientUI getSearchResultUI()       throws ServerActionException
	  {
		  if(CardAuth.ID_Auth(KYMainUI.getInstance(), 0)==1)
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
		 String[] typeArray = types.split(":");
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
	                result = result + "\n" +s;
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return result;
	    }
	 
}
