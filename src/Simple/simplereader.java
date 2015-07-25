package Simple;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

public class simplereader {

	
	simplereader()
	{
	Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			
			@Override
			public void eventDispatched(AWTEvent event) {
				// TODO Auto-generated method stub
				if(((KeyEvent)event).getID()==KeyEvent.KEY_TYPED){
					//System.out.print(((KeyEvent)event).getKeyChar());
					keyReceiver.get(((KeyEvent)event).getKeyChar());
				}
			}
		}, AWTEvent.KEY_EVENT_MASK);
		JFrame f = new JFrame();
		f.setSize(100, 100);
		f.setVisible(true);
		
		JFrame f1 = new JFrame();
		f1.setSize(120, 120);
		f1.setVisible(true);
	}
	
	
	public static void main(String [] args) { 
		new simplereader();
}
	
	
	public static String codeTrans(String code)
	{
		long ori=Long.parseLong(code);
		
		String s= Long.toHexString(ori);
		s=("00"+s).substring(s.length()-8,s.length()-1);
		if(s.length()==8)
		{
			return s.substring(6,8)+s.substring(4,6)+s.substring(2,4)+s.substring(0,2);
		}else
		{
			return "00000000";
		}
		
	}
}

class keyReceiver
{
	static String keyStor="";
    public static void get(char s)
    {
    	if(s=='\n')
    	{
    		System.out.println(keyStor);
    		System.out.println(codeTrans(keyStor));
    		keyStor="";
    	}
    	else
    	{
    		keyStor+=s;
    	}

    }
	public static String codeTrans(String code)
	{
		long ori=Long.parseLong(code);
		
		String s= Long.toHexString(ori);
		System.out.println(s);
		s=("0000"+s).substring(s.length()-4,s.length()+4);
		System.out.println(s);
		if(s.length()==8)
		{
			return (s.substring(6,8)+s.substring(4,6)+s.substring(2,4)+s.substring(0,2)).toUpperCase();
		}else
		{
			return "00000000";
		}
		
	}

}
