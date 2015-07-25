package KYUI;

import javax.swing.JLabel;

import bb.gui.base.ClientUI;

public class BlankUI extends ClientUI {

	ClientUI instances =null;
	
	String Title="建设中。。。";
	
	
	public BlankUI()
	
	{
		this.Title="空白页面，发生错误或者建设中。。。";
		
		this.add(new JLabel("空白页面，发生错误或者建设中。。。"));
	}
	
	
	public BlankUI(String Str)
	
	{
		this.Title=Str;
		
		this.add(new JLabel(Str));
	}


	@Override
	public String getTitle()
	{
		return Title;
	}
	
}
