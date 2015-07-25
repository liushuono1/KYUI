package AuthModule;

import java.awt.BorderLayout;
import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CardUtils {
	
	static TerminalFactory factory=null; 
	static List<CardTerminal> terminals;
	static boolean getTerminal=false;
		
	static {

		
		for(int i=0;i<3 && !getTerminal ;i++)
		{
			try {
				factory= TerminalFactory.getDefault();
				terminals = factory.terminals().list();
				//System.out.println(terminals.get(0).getName());
			   
				CardTerminal terminal = terminals.get(0);
				
				getTerminal=true;
			}catch (CardException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				if(e.getMessage().contains("list() failed"))
				{
					if(i<2)
					{
						JOptionPane.showMessageDialog(null, "�����Ӻö��������ڵ��ȷ����");
					}else
					{
						JOptionPane.showMessageDialog(null, "������û��׼���ã�ֻ���л����Ĳ�ѯ���ܹ��ܡ�");
					}
				}
			}
		}
		
	}
	
	
	

	public static String GetCardID()
	{
		if(!CardUtils.getTerminal)
		{
			return null;  //û�ж�������ʱ����Ҫ�����֤������δ���
		}
		
		JFrame TempShow =new TempShowWindow("��ѿ��ŵ��������ϣ�");
		TempShow.setVisible(true);
		String cardID= "";
	
				int retry=0;
				boolean successful = false;
				while((retry++)<3 && !successful)
				{
					try {
						
						//System.out.println(terminals.get(0).getName());
						CardTerminal terminal = terminals.get(0);

						if (terminal.waitForCardPresent(5000)) {
							
							TempShow.dispose();
							Card card = terminal.connect("*");
							CardChannel channel = card.getBasicChannel();
							System.out.println(card.getATR().toString());
							cardID=(CardUtils.GetID(channel));
						}
						
						
						
						
						if(!cardID.equals("") && !cardID.equals("630"))
						{
							successful=true;
							System.out.println(">>>>>"+cardID);
						}else
						{
							if(!TempShow.isVisible())
							{
								TempShow =new TempShowWindow("����ʧ�ܣ���ѿ��ŵ��������ϣ�");
								TempShow.setVisible(true);
							}
						}
					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if(!TempShow.isVisible())
						{
							
							TempShow =new TempShowWindow("����ʧ�ܣ���ѿ��ŵ��������ϣ�");
							TempShow.setVisible(true);
						}
					}
				
				}
			
		
		
		if(TempShow.isVisible())
		{
			JOptionPane.showConfirmDialog(null, "����ȡʧ�ܣ���");
			TempShow.setVisible(false);
			return null;
		}
		
		
		System.out.println("<<<<<<<"+cardID);
		return cardID.toUpperCase();
		
	}
	
	
	
	
	public static String GetID(CardChannel channel) throws CardException
	{
		String response="";
		String s = "FFCA000000"; 
		ResponseAPDU answer = channel.transmit(new CommandAPDU(hexStringToBytes(s)) );
		
		//byte[] uid = answer.getData();
		byte[] uid = answer.getBytes();
		for(int i=0;i<uid.length;i++)
		{
			System.out.print(Integer.toHexString(uid[i] & 0xFF)+" ");
			response+=(Integer.toHexString(uid[i] & 0xFF));
		}
		System.out.println();
		return response;
		

	}
	
	public static String GetIDn(CardChannel channel) throws CardException
	{
		String response="";
		String s = "FFCA000000"; 
		ResponseAPDU answer = channel.transmit(new CommandAPDU(hexStringToBytes(s)) );
		
		//byte[] uid = answer.getData();
		byte[] uid = answer.getBytes();
		for(int i=0;i<uid.length;i++)
		{
			
			
			System.out.print(Integer.toHexString(uid[i] & 0xFF)+" ");
			if(Integer.toHexString(uid[i] & 0xFF).length()==2)
				response+=(Integer.toHexString(uid[i] & 0xFF));
			else
				response+=("0"+(Integer.toHexString(uid[i] & 0xFF)));
		}
		System.out.println();
		return response;
		

	}
	
	

	public static String GetCardIDn()
	{
		if(!CardUtils.getTerminal)
		{
			return null;  //û�ж�������ʱ����Ҫ�����֤������δ���
		}
		
		JFrame TempShow =new TempShowWindow("��ѿ��ŵ��������ϣ�");
		TempShow.setVisible(true);
		String cardID= "";
	
				int retry=0;
				boolean successful = false;
				while((retry++)<3 && !successful)
				{
					try {
						
						//System.out.println(terminals.get(0).getName());
						CardTerminal terminal = terminals.get(0);

						if (terminal.waitForCardPresent(5000)) {
							
							TempShow.dispose();
							Card card = terminal.connect("*");
							CardChannel channel = card.getBasicChannel();
							System.out.println(card.getATR().toString());
							cardID=(CardUtils.GetIDn(channel));
						}
						
						
						
						
						if(!cardID.equals("") && !cardID.equals("630"))
						{
							successful=true;
							System.out.println(">>>>>"+cardID);
						}else
						{
							if(!TempShow.isVisible())
							{
								TempShow =new TempShowWindow("����ʧ�ܣ���ѿ��ŵ��������ϣ�");
								TempShow.setVisible(true);
							}
						}
					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if(!TempShow.isVisible())
						{
							
							TempShow =new TempShowWindow("����ʧ�ܣ���ѿ��ŵ��������ϣ�");
							TempShow.setVisible(true);
						}
					}
				
				}
			
		
		
		if(TempShow.isVisible())
		{
			JOptionPane.showConfirmDialog(null, "����ȡʧ�ܣ���");
			TempShow.setVisible(false);
			return null;
		}
		
		
		System.out.println("<<<<<<<"+cardID);
		return cardID.toUpperCase();
		
	}
	
	
	 static String ReadBlock(CardChannel channel,int block) throws CardException
	{
		String response="",blockStr=String.valueOf(block);
		if(blockStr.length()==1)
			blockStr="0"+blockStr;
		
		String s = "FFB000"+blockStr+"10";
		ResponseAPDU answer = channel.transmit(new CommandAPDU(hexStringToBytes(s)) );
		
		byte[] uid = answer.getData();
		//answer.getData();
		for(int i=0;i<uid.length;i++)
		{
			System.out.print(Integer.toHexString(uid[i] & 0xFF)+":");
			response+=(Integer.toHexString(uid[i] & 0xFF));
		}
		System.out.println();
		if(response.equals("630"))
			return null;

		return response;
		

	}
	
	 static String ReadSector(CardChannel channel,int Sector,String Key) throws CardException
	{
		String ret="";
		int SectorOffset= Sector*2;
		LoadKey(channel,Key,0);
		AuthenticateBlock(channel,0,SectorOffset+0);
		ret+=ReadBlock(channel,SectorOffset+0);
		AuthenticateBlock(channel,0,SectorOffset+1);
		ret+=ReadBlock(channel,SectorOffset+1);
		
		return ret;
		
	}
	
	
	 static void LoadKey(CardChannel channel, String key, int i) {
		// TODO Auto-generated method stub
		
	}


	 static boolean AuthenticateBlock(CardChannel channel, int keyIndex,int block) throws CardException
	{
		String response="",blockStr=String.valueOf(block);
		if(blockStr.length()==1)
			blockStr="0"+blockStr;
		
		String s = "FF860000050100"+blockStr+"600"+String.valueOf(keyIndex); //Default KeyTypeA 600 ,TypeB 610
		ResponseAPDU answer = channel.transmit(new CommandAPDU(hexStringToBytes(s)) );
		
		byte[] uid = answer.getBytes();
		for(int i=0;i<uid.length;i++)
		{
			System.out.print(Integer.toHexString(uid[i] & 0xFF)+" ");
			response+=(Integer.toHexString(uid[i] & 0xFF));
		}
		
		System.out.println();
		
		if(response.equals("900"))
		{
			return true;
		}else
			return false;

	}
	
	
	static boolean loadKey(CardChannel channel,String Key, int keyIndex) throws  CardException
	{
		if(Key.length()<12)
			throw new CardException("Key length is not correct.");
		
		String response="";
		String s = "FF82000"+String.valueOf(keyIndex)+"06"+Key;
		ResponseAPDU answer = channel.transmit(new CommandAPDU(hexStringToBytes(s)) );
		
		byte[] uid = answer.getBytes();
		for(int i=0;i<uid.length;i++)
		{
			System.out.print(Integer.toHexString(uid[i] & 0xFF)+" ");
			response+=(Integer.toHexString(uid[i] & 0xFF));
		}
		
		System.out.println();
		if(response.equals("900"))
		{
			return true;
		}else
			return false;

	}

	
	public static byte[] hexStringToBytes(String hexString) {   
	    if (hexString == null || hexString.equals("")) {   
	        return null;   
	    }   
	    hexString = hexString.toUpperCase();   
	    int length = hexString.length() / 2;   
	    char[] hexChars = hexString.toCharArray();   
	    byte[] d = new byte[length];   
	    for (int i = 0; i < length; i++) {   
	        int pos = i * 2;   
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
	    }   
	    return d;   
	} 
	
	 private static byte charToByte(char c) {   
		    return (byte) "0123456789ABCDEF".indexOf(c);   
		}  
}


class TempShowWindow extends JFrame
{
	public TempShowWindow(String txt)
	{
		super("��ˢ��");
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.CENTER,new JButton(txt));
		setLocation(200, 200);
		setSize(300, 150);
	}

}
