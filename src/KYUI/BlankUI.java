package KYUI;

import javax.swing.JLabel;

import bb.gui.base.ClientUI;

public class BlankUI extends ClientUI {

	ClientUI instances =null;
	
	String Title="�����С�����";
	
	
	public BlankUI()
	
	{
		this.Title="�հ�ҳ�棬����������߽����С�����";
		
		this.add(new JLabel("�հ�ҳ�棬����������߽����С�����"));
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
