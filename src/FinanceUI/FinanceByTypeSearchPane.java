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
		
		  super(new String[] {"���"}, new JComponent[] {
		            getTypeComboBox()
		        }, "���������");
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
			  return new KYUI.BlankUI("������Ȩ�ޣ�����");
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
	            BufferedReader br = new BufferedReader(new InputStreamReader(FinanceAddUI.class.getResourceAsStream(file)));//����һ��BufferedReader������ȡ�ļ�
		           //����һ��BufferedReader������ȡ�ļ�
	            String s = null;
	            while((s = br.readLine())!=null){//ʹ��readLine������һ�ζ�һ��
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
