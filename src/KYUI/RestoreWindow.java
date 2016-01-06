package KYUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import AuthModule.CardAuth;
import bb.gui.base.ClientUI;
import bb.gui.swing.DetachUI;



public class RestoreWindow extends ClientUI {
	static RestoreWindow instance=null;
	KYMainUI mainUI= null;
	ScrollInfoPane si= null;
	static DetachUI outFrame=null;
	
	public RestoreWindow()
	{
		this.setLayout( new BorderLayout());
		JButton restoreBtn=new JButton("打开窗口");
		restoreBtn.setFont(new java.awt.Font("SimHei",   1,  15));
		restoreBtn.setBackground(new Color(161,205,95));
		restoreBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent actionevent) {
				// TODO Auto-generated method stub
				mainUIrestore();
			}
			
		});
		restoreBtn.setPreferredSize(new Dimension(100,100));
		this.add(BorderLayout.CENTER,restoreBtn);
		instance=this;
		
	}
	public RestoreWindow(KYMainUI mainUI)
	{
		JButton restoreBtn=new JButton("打开窗口");
		restoreBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent actionevent) {
				// TODO Auto-generated method stub
				mainUIrestore();
			}
			
		});
		restoreBtn.setPreferredSize(new Dimension(150,150));
		restoreBtn.setBackground(new Color(161,205,96));
		this.add(restoreBtn);
		this.mainUI=mainUI;
		instance=this;
	}
	
	public static RestoreWindow getInstance()
	{
		if(instance== null)		
		{
			new RestoreWindow();
		}
		return instance;
		
	}
	
	public static DetachUI getDetachRestoreWindow()
	{
		if(outFrame==null)
		{
			if(instance.si==null)
			{
				outFrame=new DetachUI(getInstance());
				outFrame.setSize(100, 100);
				int x = Toolkit.getDefaultToolkit().getScreenSize().width ,
				y = Toolkit.getDefaultToolkit().getScreenSize().height ;
				outFrame.setLocation(x-170,y-190);
				outFrame.setType(Window.Type.UTILITY);
				outFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				outFrame.setUndecorated(true);
				outFrame.setAlwaysOnTop(true);
			}else
			{
				outFrame=new DetachUI(getInstance());
				outFrame.setSize(100, 125);
				int x = Toolkit.getDefaultToolkit().getScreenSize().width ,
				y = Toolkit.getDefaultToolkit().getScreenSize().height ;
				outFrame.setLocation(x-170,y-215);
				outFrame.setType(Window.Type.UTILITY);
				outFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				outFrame.setUndecorated(true);
				outFrame.setAlwaysOnTop(true);
				
				
			}
		}
		return outFrame;
	}
	
	
	public void setMainUI(KYMainUI mainUI)
	{
		this.mainUI=mainUI;
	}
	
	public void setScrollInfoPane(ScrollInfoPane si)
	{
		this.si=si;
		this.add(BorderLayout.NORTH,si);
	}

	public void mainUIrestore()
	{
		
	//	if(CardAuth.ID_Auth(KYMainUI.getInstance(), 0)!=1)
		//{
			//return;
		//}
		if(mainUI!=null)
		{
			mainUI.setExtendedState(Frame.MAXIMIZED_BOTH);
			mainUI.setVisible(true);
			outFrame.setVisible(false);
				
			mainUI.restore();
			
		}
	}

}
