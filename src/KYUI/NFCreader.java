package KYUI;

import java.awt.BorderLayout;
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
import javax.swing.JFrame;
import javax.swing.JLabel;

public class NFCreader implements Runnable{

	JFrame TempShow;
	NFCInterface parent = null;
	public CardTerminal terminal;
	boolean Timeswitch;
	
	public NFCreader(NFCInterface parent)
	{
		this.parent=parent;
		TempShow =new JFrame("ÇëË¢¿¨");
		TempShow.add(BorderLayout.CENTER,new JLabel("Çë°Ñ¿¨·Åµ½¶Á¿¨Æ÷ÉÏ£¡"));
		TempShow.setSize(300, 150);
		
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
	
	public void run()
	{
		TempShow.setVisible(true);
		String cardID="";
		long startx=0,endx=0;
		String currentTime;
        
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
							parent.NFCAction(prams);
						}
						else
						{
							String prams[]={""};
							parent.NFCAction(prams);
						}
									
			  }
			    
			 }catch (CardException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					 String prams[]={"CARDERROR"};
					 parent.NFCAction(prams);
					
			 } 
			
			TempShow.setVisible(false);
			

		
	}
	
	
}
