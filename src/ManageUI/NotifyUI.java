package ManageUI;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.commons.dbcp2.BasicDataSource;

import KYUI.KYMainUI;
import bb.common.EmployeeCardVO;
import bb.gui.base.ClientUI;


public class NotifyUI extends JPanel{
	LinkedList<Checkbox> box_list ;
	JTextArea msgArea;
	JComboBox JB;
	CheckboxGroup cbg;
	
	NotifyUI(Object test) //����Ŀ�ĵĹ�����
	{
		JFrame f = new JFrame();
		f.setSize(400, 300);
		this.setLayout(new BorderLayout());
		f.add(this);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JB = new JComboBox();
		List<NotifyClient> clients=getAllClient();
		for(NotifyClient client :clients)
		{
			//if(client.online)
				JB.addItem(client);
		}
		this.add(BorderLayout.NORTH,JB);
		
		JPanel p1 = new JPanel();
		p1.setBorder(BorderFactory.createTitledBorder("֪ͨ����"));
		p1.setLayout(new GridLayout(8,1));
		cbg = new CheckboxGroup();
		box_list = new LinkedList<Checkbox>();
		String[] regularItems ={"������Ϣ","�����Ϣ","����֪ͨ","������ʾ","������Ϣ","ȡ��֪ͨ","���ˢ��ʱ��","��֪ͨˢ��"};
		String[] regularNames={"ADDONLYMSG","ADDMSG","ADDNOTICE","POPUPMSG","VOICEMSG","ADDNOTICE","CHANGETIMELIMIT","REFRESH"};
		
		ItemListener itemlistener = new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent itemevent) {
				// TODO Auto-generated method stub
				String selectedBox = ((Checkbox)itemevent.getSource()).getLabel();
				
				  if(selectedBox.equals("ȡ��֪ͨ") || selectedBox.equals("���ˢ��ʱ��")||selectedBox.equals("��֪ͨˢ��"))
				  {
					 
					  msgArea.setEnabled(false);
				  }else
				  {
					  msgArea.setEnabled(true);
				  }
			}
			
		};
		
		for(int i=0;i<regularItems.length;i++)
		{
			   Checkbox box = new Checkbox(regularItems[i],cbg,false);
			   box.addItemListener(itemlistener);
			   box.setName(regularNames[i]);
			   box_list.add(box);
			   p1.add(box);
		}
		box_list.get(0).setState(true); 
		
		msgArea = new JTextArea();
		this.add(BorderLayout.WEST,p1);
		this.add(BorderLayout.CENTER,msgArea);
		
		JButton btn = new JButton("����");
		btn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent actionevent) {
				// TODO Auto-generated method stub
				sedHandler();
			}
			
		});
		f.add(BorderLayout.SOUTH,btn);
		System.out.println();
		f.setVisible(true);
	}
	
	private void sedHandler()
	{
		NotifyClient client=(NotifyClient)JB.getSelectedItem();
		String msg=msgArea.getText();
		
		String selectedBox = cbg.getSelectedCheckbox().getLabel();
		if(selectedBox.equals("ȡ��֪ͨ") || selectedBox.equals("���ˢ��ʱ��")||selectedBox.equals("��֪ͨˢ��"))
		{
			String[] passStr={cbg.getSelectedCheckbox().getName(),""};
			notifyClient(client,passStr);
		}else
		{
			if(msg=="")
				return;
			String[] passStr={cbg.getSelectedCheckbox().getName(),client.ClientID,msg};

			notifyClient(client,passStr);
		}
		  
	}
	
	
	private void notifyClient(final NotifyClient client,final String[] msg)
	{
		Thread t= new Thread(){
		int SERVERPORT=990;
		String ClientIP =client.ClientIP;
		
		public void run(){
			if(ClientIP.contains("0.0.0.0"))
				return;
			if(ClientIP.contains("192.168.0.199"))
			{
				ClientIP="192.168.1.199";
			}
			System.out.println("this ip---------------->"+ClientIP);
			 try {
	             // ���������׽��֡�
	             Socket s = new Socket("192.168.1.100",SERVERPORT);
	             System.out.println("socket = " + s);
	             
	             // �½��������ӵ���������
	             BufferedReader in = new BufferedReader(new InputStreamReader(s
	                     .getInputStream()));
	             
	             // �½��������ӵ��Զ�ˢ�µ��������
	             PrintWriter out = new PrintWriter(new BufferedWriter(
	                     new OutputStreamWriter(s.getOutputStream())),true);
	             
	             // ��ʹ��System.in����InputStreamReader���ٹ���BufferedReader��
	             BufferedReader stdin = new BufferedReader(
	                     new InputStreamReader(System.in));
	             
	             System.out.println("Enter a string�� Enter BYE to exit! ");
	             
	             
	                 // ��ȡ�ӿ���̨������ַ��������������������������������˷������ݡ�
	             		
	             	out.println(ClientIP);
	             	String str = in.readLine();
	             	for(int i=0;i<msg.length;i++)
	             	{
	             		out.println(msg[i]);
	             		str = in.readLine();
	             	}
	               
	                out.println("EOT");
	            
	             s.close();
	         } catch (IOException e) {
	             System.err.println("IOException" + e.getMessage());
	         }
		 }
		};
		t.start();
	}
	
	private List<NotifyClient> getAllClient()
	{
		List<NotifyClient> ret = new LinkedList<NotifyClient>();
		
		try 
		{
			
			Connection conn=bds.getConnection();
			PreparedStatement pstmt =null;
	        ResultSet rs=null;
	        
	        pstmt=conn.prepareStatement("SELECT bizbox_user.*,emp_id.user_name,emp_id.job_id FROM bb2_test.bizbox_user join emp_id on emp_id.id=bizbox_user.ref_id;");
	       	rs=  pstmt.executeQuery();
	       	while(rs.next())
	       	{
	       		NotifyClient NC = new NotifyClient();
	       		NC.ClientID=  rs.getString("job_ID");
	       		NC.ClientIP= rs.getString("ip");
	       		NC.OP_ID= rs.getString("user_name");
	       		NC.Reg_Date=rs.getTimestamp("last_login");
	       		NC.online = rs.getBoolean("status");
	       		ret.add(NC);
	       	}
	       	rs.close();
	       	pstmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
		
	}
	
	
	public static void main(String[] args)
	{
		new NotifyUI(null);
	}
	
	
	private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private static final int INITIAL = 15;//��ʼ��50������
    private static final int MAX_ACTIVE = 50;//���ֵ500������
    private static final int MAX_IDLE = 10;//������10
    private static final long MAX_WAIT = 5 * 1000;//����500�����ʣ��ȴ���ʱ��
    public static BasicDataSource bds;
    static{
        if(bds == null){
            bds = new BasicDataSource();
        }
        bds.setDriverClassName(DRIVER_NAME);
        bds.setInitialSize(INITIAL);
        bds.setMaxTotal(MAX_ACTIVE);
        bds.setMaxIdle(MAX_IDLE);
        bds.setMaxWaitMillis(MAX_WAIT);
        bds.setTestWhileIdle(true);
        bds.setTimeBetweenEvictionRunsMillis(1000*60*5);
        bds.setMinEvictableIdleTimeMillis(1000*50*15);
        bds.setMaxConnLifetimeMillis(1000*60*29);
        bds.setRemoveAbandonedOnBorrow(true);
        bds.setRemoveAbandonedTimeout(180);
        bds.setValidationQuery("select 1;");
        bds.setUrl("jdbc:mysql://192.168.1.100:3307/bb2_test");

        bds.setUsername("root");//���ݿ��û�
        bds.setPassword("root");//���ݿ�����
    }
}

class NotifyClient
{
	public String ClientID, OP_ID, ClientIP;
	public java.sql.Timestamp Reg_Date;
	public java.sql.Time Reg_Time;
	public boolean online;
	public String toString()
	{
		return  ClientID+" "+OP_ID+" "+ClientIP+"  "+online;
	}
}
