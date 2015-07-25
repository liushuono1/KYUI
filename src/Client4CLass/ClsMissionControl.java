package Client4CLass;

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


class ClsMissionControl extends MissionControl
{
	public static final int SERVERPORT = 991;
	int number = 1;
	

	ClsMissionControl(MissionControlledObj Obj) {
		super(Obj);
		this.getAction("Class action1").StratAction(null);
		TimerTask task = new TimerTask()
		 {
			 @Override
			public void run() 
			 {
				 getAction("Class action1").TimeRepeatAction(null);
				 getAction("Class action1").RepeatAction(null);
				
			 }
		 };
		 Timer timer = new Timer();
		 timer.schedule(task, 0, 3600*1000);
	}
	
	@Override
	public void run()
	{
		 try {
        	 ServerSocket s= new ServerSocket(SERVERPORT);
             // 已经连接上的客户端的序号。
             int number = 1;
             // 建立服务器端倾听套接字。
             System.out.println("Started: " + s);
         
         
             while (true) {
            // 等待并接收请求，建立连接套接字。
            	 Socket incoming = s.accept();
            	System.out.println("Connection " + number + " accepted: ");
            	System.out.println(incoming);
            // 启动一个线程来进行服务器端和客户端的数据传输。
            // 主程序继续监听是否有请求到来。
            	Thread t = new EchoThread(incoming,getAction("Class action1"));
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
    @Override
	public void run() {
    	String recivedTxt="";
    	
        try {
            			// 新建网络连接的输入流。
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            			// 新建网络连接的自动刷新的输出流。
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())),true);
            //System.out.println("Hello! Enter BYE to exit.");
            // 回显客户端的输入。
            while (true) {
                // 从网络连接读取一行，即接收客户端的数据。
                String line = in.readLine();
               // 如果接收到的数据为空（如果直接按Enter，不是空数据），则退出循环，关闭连接。
                if (line == null) {
                    break;
                } else {
                	System.out.println(line);
                	
                    if (line.trim().equals("EOT")) {
                        //System.out.println("The client finish trans, will be closed!");
                        break;
                    }
                    //System.out.println("Echo " + n + ": " + line);
                    // 向网络连接输出一行，即向客户端发送数据。
                    out.println("GOT");
                    recivedTxt+=(line+"\n");
                }
            }
            // 关闭套接字。
            s.close();
        } catch (IOException e) {
            System.err.println("IOException");
        }
        
        System.out.println(recivedTxt);
        if(!recivedTxt.trim().equals(""))
        {
        	n.RepeatAction(null);
        }
        
        
        //new AePlayWave(KYClassUI.audioFile).start();
        KYMainUI.getInstance().pushNotification("", recivedTxt);
        
    }
}
