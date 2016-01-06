package FinanceUI;

import javax.swing.JPanel;

import AuthModule.AuthAble;
import AuthModule.AuthLV1;
import AuthModule.CardAuth;
import KYUI.KYMainUI;
import bb.gui.base.AddActionPane;
import bb.gui.swing.homepage.HomePageAddPane;

public class FinanceAddButton extends HomePageAddPane implements AuthLV1 {
	
	public FinanceAddButton()
	{
		
	}

	@Override
	public AddActionPane getAddActionPane() {
		// TODO Auto-generated method stub
		if(CardAuth.ID_Auth(this, 0)==1)
			return new FinanceAddUI();
		else
			return new FinanceAddUI(){
			
			@Override
			public JPanel getInputPanel()
			{
				return new KYUI.BlankUI("ÇëÉêÇëÈ¨ÏÞ£¡£¡£¡");
			}
			
			
			@Override
			public JPanel getShowPanel()
			{
				return new KYUI.BlankUI("ÇëÉêÇëÈ¨ÏÞ£¡£¡£¡");
			}
		};
	}

	@Override
	public String getAddUiTitle() {
		// TODO Auto-generated method stub
		return "";
	}
	
	 @Override
	public String getButtonText()
	 {
		 return "Ìí¼Ó¼ÇÂ¼";
	 }

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return Level;
	}

}
