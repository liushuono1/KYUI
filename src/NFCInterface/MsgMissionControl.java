package NFCInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

//import KYUI.AePlayWave;
import KYUI.KYMainUI;
import KYUI.MissionControl;
import KYUI.MissionControlAction;
import KYUI.MissionControlledObj;


class MsgMissionControl extends MissionControl
{
	public static final int SERVERPORT = 991;
	int number = 1;
	

	MsgMissionControl(MissionControlledObj Obj) {
		super(Obj);
		/*this.getAction("MsgControl").StratAction(null);
		TimerTask task = new TimerTask()
		 {
			 public void run() 
			 {
				 getAction("MsgControl").TimeRepeatAction(null);
				 getAction("MsgControl").RepeatAction(null);
				
			 }
		 };
		 Timer timer = new Timer();
		 timer.schedule(task, 0, 3600*1000);*/
	}
	
	public void run()
	{
		 try {
        	 ServerSocket s= new ServerSocket(SERVERPORT);
             // �Ѿ������ϵĿͻ��˵���š�
             int number = 1;
             // �����������������׽��֡�
             System.out.println("Started: " + s);
         
         
             while (true) {
            // �ȴ����������󣬽��������׽��֡�
            	 Socket incoming = s.accept();
            	System.out.println("Connection " + number + " accepted: ");
            	System.out.println(incoming);
            // ����һ���߳������з������˺Ϳͻ��˵����ݴ��䡣
            // ��������������Ƿ�����������
            	Thread t = new EchoThread(incoming,getAction("MsgControl"));
            	t.start();
            	number++;
             }
		} catch (IOException e) {
             System.err.println("IOException");
	}
	
}
}
class EchoThread extends Thread {
    private Socket s;
    MissionControlAction n;
    public EchoThread(Socket incoming,MissionControlAction missionControlAction) {
        s = incoming;
        n = missionControlAction;
    }
    public void run() {
    	String recivedTxt="";
    	
        try {
            			// �½��������ӵ���������
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            			// �½��������ӵ��Զ�ˢ�µ��������
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())),true);
            //System.out.println("Hello! Enter BYE to exit.");
            // ���Կͻ��˵����롣
            while (true) {
                // ���������Ӷ�ȡһ�У������տͻ��˵����ݡ�
                String line = in.readLine();
               // ������յ�������Ϊ�գ����ֱ�Ӱ�Enter�����ǿ����ݣ������˳�ѭ�����ر����ӡ�
                if (line == null) {
                    break;
                } else {
                	System.out.println(line);
                	
                    if (line.trim().equals("EOT")) {
                        //System.out.println("The client finish trans, will be closed!");
                        break;
                    }
                    //System.out.println("Echo " + n + ": " + line);
                    // �������������һ�У�����ͻ��˷������ݡ�
                    out.println("GOT");
                    recivedTxt+=(line+"\n");
                }
            }
            // �ر��׽��֡�
            s.close();
        } catch (IOException e) {
            System.err.println("IOException");
        }
        
        System.out.println(recivedTxt);
        
        String msg[] = recivedTxt.split("\n");
        n.RepeatAction(msg);// ������Ϣ
       
        	
        
        
        
        
        
    }
}