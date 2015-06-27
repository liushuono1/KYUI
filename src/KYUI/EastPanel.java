package KYUI;

import java.awt.Container;

import javax.swing.JPanel;

import bb.gui.AbstractMainUI;

public class EastPanel extends JPanel implements KYRefreshable{

	private static EastPanel instance = null;
	private static boolean instanced = false;
	private JPanel contentPane;
	
	public EastPanel()
	{
		instance=this;
		instanced=true;
	}
	
	public static synchronized EastPanel getInstance() {
		if (instance == null) {
			instance = new EastPanel();
			instanced = true;
		}
		
		return instance;
	}
	public JPanel getContentPane()
	{
		return this;
	}
	public static synchronized boolean isInstanced() {
		return instanced;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(String[] msg) {
		// TODO Auto-generated method stub
		
	}
}
