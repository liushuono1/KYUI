package NFCInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

import KYUI.MissionControlAction;

class NFCWaitingThread extends Thread 
{
	public CardTerminal terminal;
	 MissionControlAction n;
	 public boolean Timeswitch=true;
    public NFCWaitingThread(MissionControlAction missionControlAction) {
        n = missionControlAction;
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
    }
        
    @Override
	public void run()
    {
		String fileName,fileName2;
		String cardID="";
		long startx=0,endx=0;
		String currentTime;
        
		while(Timeswitch)
		{
			fileName="syspic//psb.jpg";
			fileName2="syspic//psbP.jpg";
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
						}else
						{
							
							String prams[]={cardID};

							n.RepeatAction(prams);
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
					  n.RepeatAction(prams);
				  }
			  }  
			 }catch(InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			 } catch (CardException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					 String prams[]={"CARDERROR"};
					  n.RepeatAction(prams);
					
			 } 
		  }
    }
    
    public static void main(String[] args)
    {
    	MissionControlAction testaction = new MissionControlAction(){

			@Override
			public void setname(String name) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void StratAction(String[] parms) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void FinishAction(String[] parms) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void TimeRepeatAction(String[] parms) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void RepeatAction(String[] parms) {
				// TODO Auto-generated method stub
				
				System.out.println("RAction p"+parms[0]);
				
			}
    		
    	};
    	
    	
    	Thread s = new NFCWaitingThread(testaction);
    	s.start();
    }
}
