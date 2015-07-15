package NFCInterface;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.swing.JButton;
import javax.swing.JFrame;

import KYUI.MissionControl;

public class NFCMissionControl extends MissionControl{
	public CardTerminal terminal;
	public boolean Timeswitch=true,timelimite=true,terminalAvailable=true;
	

	protected NFCMissionControl(NFCStart controlledUI) {
		super(controlledUI);
		// TODO Auto-generated constructor stub
		this.getAction("NFCControl").StratAction(null);
		TimerTask task = new TimerTask()
		 {
			 public void run() 
			 {
				 getAction("NFCControl").TimeRepeatAction(null);;
				
			 }
		 };
		 Timer timer = new Timer();
		 timer.schedule(task, 0, 29*60*1000);
		
		 
		 initCardTerminal();
		
	}
	
	
	
	public void initCardTerminal()
	{
		 TerminalFactory factory = TerminalFactory.getDefault();
		 
	        List<CardTerminal> terminals;
			try {
				terminals = factory.terminals().list();
				
				 terminal = terminals.get(0);
				 System.out.println(terminal.getName()+" is sucessfully connected");
			} catch (CardException e) {
				// TODO Auto-generated catch block
				
				
				if(e.getMessage().contains("list() failed"))
				{
					
					terminalAvailable=false;
				}
				e.printStackTrace();
			}
	}
	public void reInitReader()
	{
		this.Timeswitch=false;
		TerminalFactory factory = TerminalFactory.getDefault();
		 
        List<CardTerminal> terminals;
		try {
			terminals = factory.terminals().list();
			
			 terminal = terminals.get(0);
			 System.out.println(terminal.getName()+" is sucessfully connected");
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.Timeswitch=false;
		this.run();
	}
	
	public void SwitchTimeLimite(String oo)
	{
		if(oo.toUpperCase().equals("ON"))
			this.timelimite=true;
		else if(oo.toUpperCase().equals("OFF"))
			this.timelimite=false;
	}
	
	public boolean isTimeLimite()
	{
			return this.timelimite;
	}
	
	public void run()
	{
		String cardID="";
		long startx=0,endx=0;
		String currentTime;
        if(terminalAvailable)
        {        
    		while(Timeswitch)
    		{
    			boolean readness = false;
    			try{ 
    				if(!terminal.isCardPresent())
    				{
    					cardID = "";
    					readness=true;
    				}
    				else if(cardID.equals(""))
    				{
    					readness=true;
    				}
    				if(readness)
    				{
    					if(terminal.waitForCardPresent(5000)) 
    					{
    						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ú¸ï¿½Ê½
    						currentTime = df.format(new Date());
    						startx=System.currentTimeMillis();
    						endx=System.currentTimeMillis();
    						System.out.println("card present = "+terminal.isCardPresent());
    						Card card = terminal.connect("*");
    						CardChannel channel = card.getBasicChannel();
    						
    						ResponseAPDU answer = channel.transmit(new CommandAPDU(new byte[] { (byte)0xFF, (byte)0xCA, (byte)0x00, (byte)0x00, (byte)0x00 } ) );
    						byte[] uid = answer.getBytes();
    						for(int i=0;i<uid.length;i++)
    						{
    							System.out.print(Integer.toHexString(uid[i] & 0xFF)+" ");
    							cardID += (Integer.toHexString(uid[i] & 0xFF));
    						}
    						
    						System.err.println("Swiped Card is: "+cardID);
    					}
    					
    					  if( cardID.endsWith("adae1526900"))
    						{
    							//JOptionPane.showMessageDialog(null,"into manage");
    							//resetconnections();
    						}else if (!cardID.equals(""))
    						{
    							
    							String prams[]={cardID.toUpperCase()};
    							java.sql.Time tt= new java.sql.Time(System.currentTimeMillis());
    							int timeint = Integer.parseInt(tt.toString().replace(":",""));		
    							System.out.println("time-----------"+timeint);
    							if( this.timelimite && timeint>161000 && timeint<163000)
    								prams[0]="TIMELIMIT";
    							this.getAction("NFCControl").RepeatAction(prams);
    						}
    						else
    						{
    							String prams[]={""};
    							this.getAction("NFCControl").RepeatAction(prams);
    						}
    									
    			  }
    			  else
    			  {
    				  
    				  endx=System.currentTimeMillis();
    				  System.out.println("card prensented some duration :"+(endx-startx)/1000+ " s");
    				  if( cardID.endsWith("adae1526900"))
    					{
    					  if((endx-startx)/1000>=5){
    						//JOptionPane.showMessageDialog(null,"into manage");
    						  //resetconnections();
    						  //controller.restartMonotor();// ÖØÖÃ¶Á¿¨Æ÷£»
    						}
    					  	else if((endx-startx)/1000>=10){
    						  
    					  	}
    					}
    				  
    				  Thread.sleep(500);
    				  
    				  if ((endx-startx)/1000>=3)
    				  {
    					  String prams[]={"WAITNEXT"};
    					  this.getAction("NFCControl").RepeatAction(prams);
    				  }
    			  }  
    			 }catch(InterruptedException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    			 } catch (CardException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    					 String prams[]={"CARDERROR"};
    					 this.getAction("NFCControl").RepeatAction(prams);
    					
    			 } 
    			

    		}
        }else
        {
        	JFrame f= new JFrame();
        	f.setLayout(new GridLayout(5,1));
        	f.setSize(300,400);
        	f.setAlwaysOnTop(true);
        	JButton btn1= new JButton("²âÊÔ1");
        	btn1.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent actionevent) {
					// TODO Auto-generated method stub
					 String prams[]={"CARDERROR"};
					 getAction("NFCControl").RepeatAction(prams);
				}
        		
        		
        	});
        	
           	JButton btn2= new JButton("²âÊÔ2");
        	btn2.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent actionevent) {
					// TODO Auto-generated method stub
					String prams[]={"TIMELIMIT"};
					getAction("NFCControl").RepeatAction(prams);
				}
        		
        		
        	});
        	
           	JButton btn3= new JButton("²âÊÔ3");
        	btn3.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent actionevent) {
					// TODO Auto-generated method stub
					
				}
        		
        		
        	});
        	
           	JButton btn4= new JButton("²âÊÔ4");
        	btn4.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent actionevent) {
					// TODO Auto-generated method stub
					 String prams[]={"WAITNEXT"};
					 getAction("NFCControl").RepeatAction(prams);
				}
        		
        		
        	});
        	
           	JButton btn5= new JButton("²âÊÔ5");
        	btn5.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent actionevent) {
					// TODO Auto-generated method stub
					
				}
        		
        		
        	});
        	f.add(btn1);
        	f.add(btn2);
        	f.add(btn3);
        	f.add(btn4);
        	f.add(btn5);
        	//f.setVisible(true);
        
        	
        		
        	
        	
        	
        }
	}

}


