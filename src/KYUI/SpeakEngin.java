package KYUI;

import java.util.LinkedList;
import java.util.Queue;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class SpeakEngin implements Runnable{

	static ActiveXComponent sap;
	static Dispatch sapo;
	boolean runSwitch =true;
	Queue<String> queue = new LinkedList();
	SpeakEngin()
	{
		sap = new ActiveXComponent("Sapi.SpVoice");
		sapo = sap.getObject();
	}
	
	SpeakEngin(String initwords)
	{
		sap = new ActiveXComponent("Sapi.SpVoice");
		sapo = sap.getObject();
		try {

	        Dispatch.call(sapo, "Speak", new Variant(initwords));

	    } catch (Exception e) 
	    {
	        e.printStackTrace();
	        }
		    
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(runSwitch)
		{
			try {
				synchronized(this)
				{
					if(queue.size()==0)
						this.wait();
					Dispatch.call(sapo, "Speak", new Variant(queue.poll()));	
				}
			} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
		
        sapo.safeRelease();
        sap.safeRelease();
		
	}
	
	public synchronized void say(String word)
	{
		queue.add(word);
		this.notify();
		
	}
	
	public boolean getRunningState()
	{
		return runSwitch;
	}
	
	public void finallize()
	{
		
		//System.out.println("HERE");
		runSwitch =false;
		say("ты╪Ш");
		
	}
	
	public static void main(String args[])
	{ 
		SpeakEngin ss= new SpeakEngin("123");
		(new Thread(ss)).start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ss.say("4 5 6");
		
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ss.finallize();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("end");
		//ss.say("789");

		
		
		
	}

}
