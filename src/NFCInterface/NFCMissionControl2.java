package NFCInterface;


import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import KYUI.MissionControl;
import jni.KeyMouseListener;


public class NFCMissionControl2 extends MissionControl{

	public boolean Timeswitch=true,timelimite=true,terminalAvailable=true;
	private KeyMouseListener kml;
	static String cardID;
	Timer waittimer = new Timer();

	protected NFCMissionControl2(NFCStart controlledUI) {
		super(controlledUI);
		// TODO Auto-generated constructor stub
		this.getAction("NFCControl").StratAction(null);
		TimerTask task = new TimerTask()
		 {
			 @Override
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
		kml = new KeyMouseListener();
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
	
	public static String codeTrans(String code)
	{
		try{
		    long ori=Long.parseLong(code);
		    
		    String s= Long.toHexString(ori);
		    System.out.println(s);
		    s=("0000"+s).substring(s.length()-4,s.length()+4);
		    System.out.println(s);
		    if(s.length()==8)
		    {
		    	return (c(s.substring(6,8))+c(s.substring(4,6))+c(s.substring(2,4))+c(s.substring(0,2))).toUpperCase();
		    }else
		    {
		    	return "00000000";
		    }
		}catch(Exception e)
		{
			e.printStackTrace();
			return "CARDERROR";
		}
		
	}
	public static String c(String code)
	{
		if(code.charAt(0)=='0')
			return ""+code.charAt(1);
		else
			
			return code;
	}
	
	@Override
	public void run()
	{
		while(Timeswitch)
		{
		        String directKEY="";
		        int[] key = kml.getKeyCodes();
		        
		        for(int i=0;i<key.length;i++){
		        	if(key[i]==13)
		        		break;
		        	else
		        	{
		        		
		        		directKEY+=KeyEvent.getKeyText(key[i]);
		        	}
		        }
		        if(!directKEY.equals("000000000000"))
		        {
		             cardID=codeTrans(directKEY)+"900";
      	             	
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
    	             				
		             TimerTask task = new TimerTask()
		             {
		             	 @Override
		             	public void run() 
		             	 { 
		             		 
		             		 String prams[]={"WAITNEXT"};
		             		 getAction("NFCControl").RepeatAction(prams);
		             		 cardID="";
		             	 }
		              };
		              waittimer.cancel();
		              waittimer =new Timer();
		              waittimer.schedule(task,3*1000);
                     
    	             		
                     
		        }
		        try{
		        	Thread.sleep(500);
		        }catch(Exception e)
		        {}
		}
    }
        
	

}

