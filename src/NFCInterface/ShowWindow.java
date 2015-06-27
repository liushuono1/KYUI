package NFCInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import KYUI.KYMainUI;
import KYUI.ScrollInfoPane;
import bb.common.EmployeeCardVO;
import bb.gui.base.ClientUI;
public class ShowWindow extends ClientUI {
	
	static ShowWindow instence=null;
	static KYMainUI mainUI;
	
	Image img = null,imgP=null;
	public String defaultFile = "syspic//psb.jpg";
	public String defaultFileP = "syspic//psbP.jpg";
	public JFrame frame;
    public BufferedImage image;
    public JButton button1,button2;
    public JPanel panel;
	public int type = -1;
	public Hashtable<String, String> showInfo;
	public JPanel infoPanel,TimePanel;
	public ScrollInfoPane si;
	public static Properties statusPro;
	public JLabel infoLabel;
	public JLabel timeLabel,timeLabel2;
    public SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd E \n HH:mm:ss");//;
    public String currentTime;
    String lastreadfileName="";
    EmployeeCardVO emp;
	
	
	public ShowWindow(KYMainUI mainUI)
	{
		instence=this;
		this.mainUI=mainUI;
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initSwing();
		
		this.mainUI.setVisible(false);
	}
	
	public void reinstance()
	{
		instence=null;
	}
	
	public static ShowWindow getinstence(KYMainUI mainUI)
	{
		if(instence == null)
		{
			 new ShowWindow(mainUI);
		} 
		
		return instence;
		
	}
	
	
	
	private void initSwing()
	{
		
		try {
			img = ImageIO.read(new File(defaultFile));
			imgP = ImageIO.read(new File(defaultFileP));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.setLayout(new GridLayout(1, 2));
		
		
		JPanel dp = initdrawPanel();
		
		infoPanel=initInfoPanel();
		//infoPanel.setBackground(Color.BLACK);
		this.add(infoPanel);
		this.add(dp);
		this.setVisible(true);	
		this.repaint();
		
	}
	
	
	private JPanel initInfoPanel()
	{
		JPanel infopanel = new JPanel();
		infopanel.setLayout(new GridLayout(3, 1));
		infopanel.setBackground(Color.BLACK);
		int status = 0;//(Integer) this.statusPro.get("status");
		String info = "";
		if(status != 1)
		{
			info = welcomeinfo();
		}
		  
		infoLabel = new JLabel(info,JLabel.CENTER);
		infoLabel.setForeground(Color.MAGENTA);
		int size;
		Color c;
		size = 150;
		c = Color.MAGENTA;
		
		infoLabel.setFont(new   java.awt.Font("SimHei",   1,   size)); 
		
		
		TimePanel=timePanel();
		
		KYMainUI.getInstance().setScrollInfoPane(new ScrollInfoPane(2,"开元国际幼儿园欢迎您！","") );
		si= KYMainUI.getInstance().getScrollInfoPane();
		infopanel.add(si);
		infopanel.add(infoLabel); 
		infopanel.add(TimePanel);
		Timer timer = new Timer();
		TimerTask task = new TimerTask()
		 {
			 public void run() 
			 {
				 updateTimeLable();
				
			 }
		 };
		timer.schedule(task, 0, 60*1000);
		
		return infopanel;
	}
	
	
	private JPanel timePanel()
	{
		JPanel base = new JPanel();
		base.setLayout(new GridLayout(3, 2));
		
		base.setBackground(Color.BLACK);
		
		timeLabel = new JLabel(currentTime, JLabel.CENTER);
		timeLabel.setForeground(Color.LIGHT_GRAY);
		timeLabel.setFont(new java.awt.Font("SimHei",   1,  140));
		
		timeLabel2 = new JLabel(currentTime, JLabel.CENTER);
		timeLabel2.setForeground(Color.LIGHT_GRAY);
		timeLabel2.setFont(new java.awt.Font("SimHei",   1,   40));
		
		base.add(timeLabel);
		base.add(new JLabel(""));
		base.add(timeLabel2);
		base.add(new JLabel(""));
		base.add(new JLabel(""));
		base.add(new JLabel(""));
		
		return base;

	}
	
	private void updateTimeLable()
	{
		Date d= new Date();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");//
		timeLabel.setText(df.format(d));
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy年M月d日 E");//
		timeLabel2.setText("  "+df1.format(d));
		
		TimePanel.repaint();
	}
	
	private JPanel initdrawPanel()
	{
		JPanel drawPanel = new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
				BufferedImage buff = (BufferedImage) imgP;
				g.drawImage(buff,getWidth()-440,getHeight()-400, getWidth()-40,getHeight(), 0, 0, buff.getWidth(), buff.getHeight(), null);
			}
		};
		return drawPanel;
	}
	
	
	   public String welcomeinfo()
	   {   java.sql.Time CT = new java.sql.Time(System.currentTimeMillis());
	       int timeint = Integer.parseInt(CT.toString().replace(":", ""));
	       
		   if(timeint<113000)
			   return "早上好！";
		   else if(timeint<140000 && timeint > 113000)
			   return "中午好！";
		   else if(timeint>140000)
			   return "下午好！";
		   else
		       return "欢迎！";
	   }
	   
	   public void showTimeLimitMsg()
	   {
		   updateLable(2);
		   repaint();
	   }
	   
	   public void showEmp(EmployeeCardVO emp)
	   {
		   this.emp=emp;
		   refreshPicture("LocalPic\\"+this.emp.getId().toUpperCase()+".jpg","LocalPic\\"+this.emp.getId().toUpperCase()+"P.jpg");
		   updateLable(1);
		   repaint();
	   }
	   
	   public void showCardErr()
	   {
		   refreshPicture("syspic\\error.jpg","syspic\\psbP.jpg");
		   updateLable(0);
		   repaint();
	   }
	   
	   public void showCardNoReg()
	   {
		   refreshPicture("syspic\\Carderr.jpg","syspic\\psbP.jpg");
		   updateLable(0);
		   repaint();
	   }
	   
	   public void showBlank()
	   {
		   refreshPicture("syspic\\psb.jpg","syspic\\psbP.jpg");
		   updateLable(0);
		   repaint();
	   }
	
	
		public void refreshPicture(String fileName,String fileName2) 
		{
			if(!fileName.equals(lastreadfileName)){
				try {
					img = ImageIO.read(new File(fileName));
					
					 //updateLable(status);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					
					//e.printStackTrace();
					try {
						img = ImageIO.read(new File("syspic\\nophoto.jpg"));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			this.lastreadfileName = fileName;
			try {
				imgP = ImageIO.read(new File(fileName2));
				 //updateLable(status);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				//e.printStackTrace();
				try {
					imgP = ImageIO.read(new File("syspic\\nophoto.jpg"));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}
		
		public void updateLable(int status)
		{
			String info = "";
			if(status == 1)
			{
				if(emp.getSecurityLevel().equals("LEVEL 5"))//student
				{
					info = "<html><body>"+"学号: "+ emp.getId()+"<br>"+"宝宝姓名: "+emp.getCompanyAddressBookName()+"<br>"+"班      级: "+emp.getDepartment()
							+"<br>"+"家长姓名: "+coverName(emp.getManager())+"<br>"+"<body></html>"; 
				}
				else//staff
				{
					info = "<html><body>"+"员工编号: "+ emp.getId()+"<br>"+"员工姓名: "+emp.getCompanyAddressBookName()
			 		+"<br>"+"部      门: "+emp.getDepartment()+"<br>"+"职      位: "+emp.getJobId()+emp.getPosition()+"<body></html>";   
				}  
			}
			else if (status ==0)
			{
				info = welcomeinfo();
			}else if (status ==2 )
			{
				String time= (new java.sql.Time(System.currentTimeMillis())).toString();
				String differ = String.valueOf(163000-Integer.parseInt(time.replace(":", ""))).substring(2,	 4);
				info = "<html><body>离刷卡开始还有 "+ differ +"分钟，<br>请耐心等待<body></html>";
			}
			int size=50;
			Color c=Color.MAGENTA;
			if(status == 0)
			{
				size = 150;
				c = Color.MAGENTA;
			}
			else if(status == 1)
			{
				size = 50;
				c = Color.CYAN;
			}
			else if(status == 2)
			{
				size = 60;
				c = Color.CYAN;
			}
			infoLabel.setForeground(c);
			infoLabel.setFont(new   java.awt.Font("SimHei",   1,  size)); 
			infoLabel.setText(info);
				if(showInfo!=null )
					currentTime = showInfo.get("time");
				else
					currentTime =(new Date()).toString();
			this.updateTimeLable();
				//timeLabel.setText(currentTime);
		}
	   
		private String coverName(String pName)
		{
  			int plength = pName.length();
  				if(plength>2)
  				{
  					pName=pName.replace(pName.charAt(1), '*');
  				}
  				else
  				{
  				
  					pName=pName.replace(pName.charAt(0), '*');
  					System.out.println(pName);
  				}
  			
  				return pName;
		}
	   
	public static ShowWindow getinstence()// throws Exception   // 如果没有在主界面实例化 ，则调用抛出异常
	{
		/*if(instence == null)
		{
			throw new Exception();
		} */
		
		return instence;
		
	}

}
