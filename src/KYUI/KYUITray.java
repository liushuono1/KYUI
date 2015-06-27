package KYUI;



import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class KYUITray implements ActionListener, MouseListener {
	static KYUITray instance=null;
	private Image icon;
	private TrayIcon trayIcon;
	private SystemTray systemTray;// 
	private String MsgTitle=KYMainUI.department,Msg=KYMainUI.logonUser.getUserName()+"登录到服务器:"+KYMainUI.SQLIP+KYMainUI.DBStr;
	private boolean showMsg=true;
    private KYMainUI mainUI;
	private PopupMenu pop = new PopupMenu(); 
	
	private MenuItem screenshot = new MenuItem("ScreenShot");
	private MenuItem open = new MenuItem("Restore");
  


	private MenuItem exit =new MenuItem("Exit");
	public KYUITray(KYMainUI mainUI) {
		instance=this;
		this.mainUI = mainUI;
		
		
		System.out.println(this.getClass().getResource("/bb/gui/images/submodule.png"));
		icon = new ImageIcon(this.getClass().getResource(
				"/bb/gui/images/submodule.png")).getImage();

		if (SystemTray.isSupported()) {
			systemTray = SystemTray.getSystemTray();
			trayIcon = new TrayIcon(icon, "开元国际幼儿园管理系统", pop);
			pop.add(screenshot);
			pop.add(open);
			pop.add(exit);
			try {
				systemTray.add(trayIcon);
			} catch (AWTException e1) {
				e1.printStackTrace();
				trayIcon.addMouseListener(this);
			}
		}
		displayInfoListener();
		trayIcon.addMouseListener(this);
		exit.addActionListener(this);
		open.addActionListener(this);
		screenshot.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==exit){
			JOptionPane.showMessageDialog(null,"开元国际幼儿园管理系统: 请在菜单点击退出。");
		}else if (e.getSource() == open) {
			//displayInfo();
			//JOptionPane.showMessageDialog(null,"this is open");
			mainUI.setExtendedState(JFrame.NORMAL);
			mainUI.setVisible(true);
			//friendListSet(true);
			
		} else if (e.getSource() == screenshot) {
			//JOptionPane.showMessageDialog(null,"开元国际幼儿园管理系统");
		} 
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.getButton() != MouseEvent.BUTTON3) {
			
			mainUI.setExtendedState(JFrame.NORMAL);
			mainUI.setVisible(true);

		}
		if (e.getClickCount() == 2 && e.getButton() != MouseEvent.BUTTON3) {
			JOptionPane.showMessageDialog(null,"开元国际幼儿园管理系统");
		}
	}

	public void showTrayMsg(String Title,String Msg)
	{
		this.MsgTitle=Title;
		this.Msg=Msg;
		showMsg=true;
	}
	
	public void showTrayMsgDing(String Title,String Msg)
	{
		this.MsgTitle=Title;
		System.out.println("in TRY"+Msg);
		this.Msg=Msg;
		showMsg=true;
		play("/KYUI/189.wav");   //可以通过KYMainUI.wavfile 指定
	}
	
	private void friendListSet(boolean flag) {
		
		
		//myAlarm.setVisible(true);
		//myAlarm.setExtendedState(JFrame.NORMAL);
	}
	
	private boolean isDisplayMsg()
	{
		if(showMsg)
		{
			showMsg=false;
			return true;
		}else				
		return showMsg;
	}
	
	public void displayInfoListener() {
		new Thread(new Runnable() {// 
					public void run() {
						while (true) {
							try {
								Thread.sleep(5000);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if(isDisplayMsg()){
								 trayIcon.displayMessage(" "+MsgTitle,"消息:"+ Msg ,TrayIcon.MessageType.INFO);
							}
					}
					}
				}).start();
	}
	
	

	
	public static KYUITray getInstance()
	{
		if(instance==null)
		{
			throw new NullPointerException();
		}else
		{
			return instance;
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	public static void main(String[] args)
	{
		KYUITray myTray = new KYUITray(null);
	}
	

	public void play(String file){
		(new Thread(new AePlayWave(file))).start();;
	}
	
}
