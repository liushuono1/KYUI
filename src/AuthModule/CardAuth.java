package AuthModule;



import java.awt.BorderLayout;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.dbcp2.BasicDataSource;

import free.FreeTextField;
import free.LoginUI;
import bb.common.EmployeeCardVO;
import bb.gui.ServerActionException;
import bb.gui.base.CompanyComboBox;
import bb.gui.base.ServerComboBox;
import bb.gui.server.HRServerActionManager;
import KYUI.KYMainUI;

public class CardAuth {

	static JFrame TempShow =null;
	static String SQLIP="",db="";
	static BasicDataSource bds = null;
	
	public static void CardAuth(KYMainUI hostUI)
	{
		System.out.println(">>>>"+hostUI.getClass().getName()+"  "+KYMainUI.logonUser.getId());
		
		StackTraceElement[] stack = (new Throwable()).getStackTrace();

		System.out.println(stack[1].getMethodName());
		
	}
	
	
	
	public static CardInfo RegCardInfo(CardInfo Card, EmployeeCardVO user,EmployeeCardVO holder)
	{
		
		
		if(isManageCard(Card.CardID))
			return Card;             // �ж��ǲ����������
		Card.CardUser=user;
		Card.CardHolder=holder;
        // ������������
        try {
		
        // �������ݿ�
        	Connection conn = connect();

            if(conn.isClosed()) 
            {         
           	 System.out.println("Succeeded connecting to the Database!");
            }

            // statement����ִ��SQL���
             PreparedStatement pstmt = null;
         	 pstmt = conn.prepareStatement("insert into emp_nfcid (id,id_NFC,Holder_id) values(?,?,?)"); 
         	 pstmt.setString(2,Card.CardID.toUpperCase()); 
         	 pstmt.setString(1, Card.getID());
         	 pstmt.setString(3, Card.getHolderID());
         	 System.out.println(pstmt);
         	 pstmt.execute(); 
             conn.close();
            
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return  Card;
	}
	
	
	private static boolean isManageCard(String card) {
		// TODO  �ж��Ǻ��ֹ���
		
		System.out.println("is manage"+card);
		if(card.equals("367B9055900"))  //��Ҫ��ȡ�����б�
			return true;
		else
			return false;
	}



	public static boolean UnRegCard(CardInfo Card)
	{
		EmployeeCardVO user = Card.CardUser;
		EmployeeCardVO holder = Card.CardHolder;
        // ������������
        try {
		
        // �������ݿ�
        	Connection conn =connect();

            if(conn.isClosed()) 
            {         
           	 System.out.println("Succeeded connecting to the Database!");
            }

            // statement����ִ��SQL���
             PreparedStatement pstmt = null;
         	 pstmt = conn.prepareStatement("delete from emp_nfcid where id_NFC=?"); 
         	 pstmt.setString(1, Card.CardID);
         	 System.out.println(pstmt);
         	 pstmt.execute(); 
         	 
         	 HRServerActionManager.getInstance().deleteEmployeeCard(holder.getId());;
         	 
             conn.close();
            
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ServerActionException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return  true;
	}
	
	public static CardInfo GetCardInfobyCard()
	{
		CardInfo CARD = new CardInfo(CardUtils.GetCardID());
        // ������������
		if(!CARD.isNull())
        try {
		
        // �������ݿ�
      
        	Connection conn = connect();

            if(conn.isClosed()) 
            {         
           	 System.out.println("Succeeded connecting to the Database!");
            }

            // statement����ִ��SQL���
            PreparedStatement pstmt = null;
                   
            pstmt = conn.prepareStatement("select * from emp_nfcid where id_nfc like ?;");   
            // �����
            pstmt.setString(1, CARD.getCardID()+"%");
            ResultSet rs = pstmt.executeQuery();
            String ID ="",holderID="";
            while(rs.next())
            {
               	 ID=rs.getString("id");
               	 holderID=rs.getString("Holder_id");
            }
            
            if(ID!="")
            {
            	List<EmployeeCardVO> users=new LinkedList<EmployeeCardVO>();
            	users.addAll(HRServerActionManager.getInstance().findEmployeeCardsByEmployeeId(ID, false,0, 20));
            	if(users.size()>1 || users.size()==0)
            	{
            		JOptionPane.showMessageDialog(null, "�˻�"+ID+" �������⣬������� 007");
            		
            		return null;
            	
            	}else
            	{
            		CARD.setCardUser(users.get(0));
            	}
            	
            	 //�ֿ��˲����д����ƣ���Ǩ�� tempwork�� emp_id ��
            	if(holderID!="")
            	{
            		List<EmployeeCardVO> holder=new LinkedList<EmployeeCardVO>();
                	holder.addAll(HRServerActionManager.getInstance().findEmployeeCardsByEmployeeId(holderID, true,0, 20));
                	if(users.size()==0)
                	{
                		JOptionPane.showMessageDialog(null, "�˻�"+ID+" �������⣬������� 008");
                		
                		return null;
                	
                	}else
                	{
                		CARD.setCardHolder(holder.get(0));
                	}
            	}
            	else{
            		JOptionPane.showMessageDialog(null, "��"+CARD.CardID+" δ�趨�ֿ���");
            	}
            	
            	
            }
            
            rs.close();
            conn.close();
            
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServerActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return  CARD;
	}
	

	
	
	
	
	public static List<CardInfo> GetCardInfobyID(String ID)
	{
		
		List<CardInfo> ret = new LinkedList<CardInfo>();
		List<EmployeeCardVO> users=new LinkedList<EmployeeCardVO>();
		EmployeeCardVO user =null;

    	
        // ������������
        try {
        	
        	users.addAll(HRServerActionManager.getInstance().findEmployeeCardsByEmployeeId(ID, false,0, 20));
        	if(users.size()>1 || users.size()==0)
        	{
        		JOptionPane.showMessageDialog(null, "�û���"+ID+"���ִ�������ϵ");
        	
        	}else
        	{
        		user=users.get(0);
        	}
		
        // �������ݿ�
           System.out.println(user);
        	Connection conn = connect();

            if(conn.isClosed()) 
            {         
           	 System.out.println("Succeeded connecting to the Database!");
            }

            // statement����ִ��SQL���
            PreparedStatement pstmt = null;
                   
            pstmt = conn.prepareStatement("select * from emp_nfcid where id = ?;");   
            // �����
            pstmt.setString(1, ID);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next())
            {
            	CardInfo oneCard = new CardInfo(rs.getString("id_NFC"));
            	ID=rs.getString("id");
            	
            	oneCard.setCardUser(user);
            	oneCard.setCardHolder(getEmpByID(rs.getString("Holder_id"),true));
            	System.out.println(oneCard);
            	ret.add(oneCard);
            }
            
            
            
            rs.close();
            conn.close();
            
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServerActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		return  ret;
	}
	
	public static boolean Logon_Auth(LoginUI login, int requiredLevel)
	{
		String SQLIP="",DBstr="",username="";
		List<String> cardID=new LinkedList<String>();
    	for(Component comp : login.getContentPane().getComponents())
    	{
    		if(comp instanceof JPanel){
    			for(Component compi:((JPanel) comp).getComponents())
    			{
    	
    				if(compi instanceof ServerComboBox )
    				{
    					SQLIP=(((ServerComboBox)compi).getSelectedItem()).toString();
    				}
    				
    				if(compi instanceof CompanyComboBox )
    				{
    					DBstr=(((CompanyComboBox)compi).getSelectedItem()).toString();
    				}
    				
    				if(compi instanceof FreeTextField )
    				{
    					username=(((FreeTextField)compi).getText());
    				}
    				
    			}
    			
    		}
    	}
    	CardAuth.setBasicConnection(SQLIP,DBstr);
    	String vaild_position="";
    	
    	 try {
    			
    	        	Connection conn =connect();

    	            if(conn.isClosed()) 
    	            {         
    	           	 System.out.println("Succeeded connecting to the Database!");
    	            }

    	            // statement����ִ��SQL���
    	             PreparedStatement pstmt = null;
    	         	 pstmt = conn.prepareStatement("select * from bizbox_user left join emp_nfcid on bizbox_user.ref_id=emp_nfcid.id join emp_id on emp_id.id=bizbox_user.ref_id where username=?"); 
    	         	 pstmt.setString(1, username);
    	         	 System.out.println(pstmt);
    	         	 ResultSet rs= pstmt.executeQuery(); 
    	         	 
    	         	 if(rs.next())
    	         	 {
    	         		vaild_position= rs.getString("position");
    	         		cardID.add(rs.getString("id_NFC"));
    	         	 }
    	         	 
    	             conn.close();
    	            
    	        } catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				
    	        } catch (ClassNotFoundException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    	 boolean login_permit=false;
    	 
    	 System.out.println(vaild_position);
    	 
    	 if(ValidPositionType(vaild_position)>=requiredLevel)
    	 {
        	 if(ValidPositionType(vaild_position)==0)
        	 {
        		 if(login.getPasswordField().getText().equals(""))
        		 {
        			 if(ValidCardID(cardID))
        			 {
        				 login_permit=true;
        				 login.getPasswordField().setText("kygj1234");     //�˴�Ӧ��Ϊ��������
        			 }
        			 
        		 }
        		 login_permit=true;
        	 }else if(ValidPositionType(vaild_position)==1)
        	 {
        		 if(login.getPasswordField().getText().equals(""))
        		 {
        			JOptionPane.showMessageDialog(null, "���������룡����");// ˫����֤���뿨��ͬʱ��֤
        			 
        		 }else{
        			 boolean s= ValidCardID(cardID);
        			 System.out.println("-------------->"+s);
        			 if(s)
        			 {
        				 login_permit=true;
        			 }
        				 
        		 }
        		 
        	 }else if(ValidPositionType(vaild_position)==2)
        	 {
        		 if(login.getPasswordField().getText().equals(""))
        		 {
        			JOptionPane.showMessageDialog(null, "���������룡����");
        			 
        		 }else
        		 {
        			 login_permit=true;
        		 }
        	 }
    	 }
    	
    	 
    	return login_permit;
    		
	}
	
	
	private static boolean ValidCardID(List<String> cardIDs) {
		// TODO Auto-generated method stub
		
		
		
		String cardid=CardUtils.GetCardID();
		
		if(isManageCard(cardid))
			return true;
		
		System.out.println(cardid);
		
		if(cardIDs.contains(null))
		{
			JOptionPane.showMessageDialog(null, "û�д��û�������Ŀ�����");
			return false;
		}
		for(String cardID:cardIDs)
		{
			System.out.println(cardID+"   "+cardid );
			if(cardID.contains(cardid))
				return true;
		}
		
		return false;
	}



	private static int ValidPositionType(String position)
	{
		if(position.contains("����"))
			return 1;
		else if(position.contains("԰��"))
			return 2;
		else
			return 0;
				
		
	}
	public static int ID_Auth(KYMainUI hostUI,int mode)
	{
		
		/*
		 * 
		 * 
		 * 
		 * */
		
		

		
		if(hostUI==null)
		{
			return 0;
		}else
		{
			if(KYMainUI.clientLevel()==0)   //�Ƚ��ң���Ҫ��д��������
				return 1;
			
			if(ValidPositionType(KYMainUI.logonUser.getPosition())==2)
					return 1;
			
			
		}
		
		
		CardInfo CARD = GetCardInfobyCard();
		System.out.println(CARD +"   "+ mode);
		
		
		if(CARD==null)
		{
			return 0;
		}
		
	
        	
    	if(isManageCard(CARD.CardID))
        		return 1;     //������֤Ȩ��
      
		
		if(CARD.CardUser==null)
		{
			return 0;
		}
        
        
        if (mode==1)
        {
        	//������֤����Ҫȷ�����е�½ʱ�䣬 δʵ��
        	return 0;
        }else  if(mode ==0)
        {
        	//����֤������Ҫȷ����½��ʱ��
        	if(isManageCard(CARD.CardID))
        		return 1;
        	
        	if(CARD.getID().equals(KYMainUI.logonUser.getId()))
        		return 1;
		
        }else if(mode==2)
        {
        	if(isManageCard(CARD.CardID))
        		return 1;     //������֤Ȩ��
      
        }
        
       
		
		
		return 0;
	}
	private static int CardSearch()
	{
		TempShow =new JFrame("��ˢ��");
		TempShow.add(BorderLayout.CENTER,new JLabel("��ѿ��ŵ��������ϣ�"));
		TempShow.setSize(300, 150);
		TempShow.setVisible(true);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println();
		String cardID="";
  
		
		try {
			CardUtils.terminals = CardUtils.factory.terminals().list();
			//System.out.println(terminals.get(0).getName());
			CardTerminal terminal = CardUtils.terminals.get(0);

			if (terminal.waitForCardPresent(5000)) {
				
				TempShow.hide();
				Card card = terminal.connect("*");
				
				CardChannel channel = card.getBasicChannel();
				System.out.println(card.getATR().toString());
				//ResponseAPDU answer = channel.transmit(new CommandAPDU(new byte[] { (byte)0xFF, (byte)0xCA, (byte)0x00, (byte)0x00, (byte)0x00 } ) );
				
				CardUtils.loadKey(channel,"4778BC78DC40",0);
				CardUtils.loadKey(channel,"FFFFFFFFFFFF",1);
				System.out.println(CardUtils.AuthenticateBlock(channel,0,7));
				CardUtils.ReadBlock(channel,7); 
				
				System.out.println("Sector 6  :"+CardUtils.ReadSector(channel,6,"FFFFFFFFFFFF"));
				
				CardUtils.GetID(channel);
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(TempShow.isVisible())
		{
			JOptionPane.showConfirmDialog(null, "����ȡʧ�ܣ���");
			return 0;
		}

		{
			System.out.println("find card belong to" + cardID.toUpperCase());
			//Search(cardID.toUpperCase());
		}
				
		return 1;
	}
	
	
	public static EmployeeCardVO getEmpByID(String ID ,boolean temp)
	{
		List<EmployeeCardVO> users=new LinkedList<EmployeeCardVO>();
    	try {
			users.addAll(HRServerActionManager.getInstance().findEmployeeCardsByEmployeeId(ID, temp,0, 100));
			System.out.println("find   "+ID +" and get "+users.size());
		} catch (ServerActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(users.size()>1 || users.size()==0)
    	{
    		return null;
    	
    	}else
    	{
    		return users.get(0);
    	}
    	
	}
	
	
	private static Connection connect() throws SQLException, ClassNotFoundException
	{
		
		if(CardAuth.bds==null)
		{
			if(SQLIP=="" || db=="")
				throw new SQLException("no sever and DBstr set!");
			String driver = "com.mysql.jdbc.Driver";
			// URLָ��Ҫ���ʵ����ݿ���scutcs
			String url = "jdbc:mysql://"+SQLIP+":3307/bb2_"+db;
			// MySQL����ʱ���û���
			String user = "root"; 
			// MySQL����ʱ������
			String password = "root";
			// ������������
			
			Class.forName(driver);
				// �������ݿ�
			
			Connection conn = DriverManager.getConnection(url, user, password);

			
			if(conn.isClosed()) 
			{
				//conn = DriverManager.getConnection(url, user, password);          
				System.out.println("Succeeded connecting to the Database!");
			}
				
			return conn;
		}else
		{
			return CardAuth.bds.getConnection();
		}
     }

	public static void setBasicConnection(String sQLIP, String dBstr) {
		// TODO Auto-generated method stub
		CardAuth.SQLIP=sQLIP;
		CardAuth.db=dBstr;
		
	}	
	
	
	public static void setPooledConnection(BasicDataSource bDS) {
		// TODO Auto-generated method stub
		CardAuth.bds=bDS;
		
	}	
	
	
	public static void main(String[] args)
	{
		
		ID_Auth(null,0);
		
		String a="1D0099FFBBDFBAFF";
		String b="123FFFFD889994FF";
		long A= Long.parseLong(a, 16);
		long B= Long.parseLong(b, 16);
		System.out.println(Long.toHexString(A*B).toUpperCase());
	}



	
}


