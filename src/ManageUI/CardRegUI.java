package ManageUI;

import AuthModule.CardAuth;
import AuthModule.CardInfo;




import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bb.common.EmployeeCardVO;
import bb.gui.CommonUI;
import bb.gui.ServerActionException;
import bb.gui.base.ClientUI;
import bb.gui.hr.EmployeeActionManager;
import bb.gui.hr.HumanResourceUtil;
import bb.gui.server.HRServerActionManager;

public class CardRegUI extends ClientUI{

	
	JLabel LC ;
	JTextField LP = new JTextField();
	JLabel L1 ;
	JLabel L2 ;
	JTextField T1;
	JTextField T2;
	JTextField MOBILE = new JTextField();
	JComboBox C1;
	JButton search_btn ;
	JButton Unreg_btn;
	JButton OK_btn ;
	JButton Cardsearch_btn ;
	JButton Photo_btn ;
	JButton Cancel_btn ;
	List<CardInfo> UserKey =new LinkedList<CardInfo>();
	CardInfo currentCard=null;
	public CardRegUI()
	{
		this.setLayout(new GridLayout(2,2));
		this.add(baseRegPanel());
		this.add(new JLabel());
	}
	@Override
	public String getTitle()
	{
		return "ע�����/���ڿ�";
		
	}
	
	public CardRegUI(String ID)
	{
		this.setLayout(new GridLayout(2,2));
		this.add(baseRegPanel());
		this.add(new JLabel());
		this.T1.setText(ID);
		init(ID);
	}
	
	
	private void init(String ID)
	{

		findByID(ID);
		refreashUI();
         
	}
	
	
	private void refreashUI(){
		
		if(C1.getSelectedItem() != null)
		{
			LC.setText("������"+((CardInfo)C1.getSelectedItem()).getUser().getCompanyAddressBookName());
			T1.setText(((CardInfo)C1.getSelectedItem()).getUser().getId());
			
			if(this.currentCard!=null)
			{
				for(int i = 0;i<C1.getItemCount();i++)
				{
					System.out.println(((CardInfo)C1.getItemAt(i)).getCardID()+"   curr " +currentCard.getCardID());
					if(((CardInfo)C1.getItemAt(i)).getCardID().equals(currentCard.getCardID()))
					{
						System.out.println(i);
				       C1.setSelectedIndex(i);
					}
				}
			}
			
			if(((CardInfo)C1.getSelectedItem()).getHolder()!=null)
			{
				LP.setText(((CardInfo)C1.getSelectedItem()).getHolder().getManager());  //���ݿ�ҳ���������¼��manager,Ӧ����Ϊname company addr,
 				MOBILE.setText(((CardInfo)C1.getSelectedItem()).getHolder().getMobile());
			}
			

		}
		else
		{
			LP.setText("");
			MOBILE.setText("");
		}
		
	}
	private void findByID(String ID)
	{
		if(!ID.equals(""))
		{
			UserKey.removeAll(UserKey);
		       List cards =CardAuth.GetCardInfobyID(ID);
		       UserKey.addAll(cards);
				
		       C1.removeAllItems();
				for(CardInfo card :UserKey)
				{
					System.out.println(card);
					C1.addItem(card);
				}
		       refreashUI();
		}else
		{
			   UserKey.removeAll(UserKey);
		       C1.removeAllItems();
		       refreashUI();
		}
	       

	}
	
	
	private void findByCard()
	{
		   currentCard=null;
	       CardInfo card =CardAuth.GetCardInfobyCard();
	       if(card.getUser()==null)
	       {
	    	   JOptionPane.showMessageDialog(null, "û���ҵ����ſ���ע����Ϣ����");
	    	   return;
	       }
	       
	       findByID(card.getID());
	       currentCard=card;
	       refreashUI();
	}
	
	
	public JPanel baseRegPanel()
	{
		JPanel regWindow = new JPanel();
		
		
		regWindow.setSize(600, 600);
		regWindow.setLayout(new GridLayout(7,2));
		L1 = new JLabel("��ԱID");
		L2 = new JLabel("ע�Ῠ��");
		T1= new JTextField();
		C1= new JComboBox();
				
		C1.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				refreashUI();
				System.out.println(e);
				
			}
			
		});
		//C1.setEditable(true);
		search_btn = new JButton("����");
        search_btn.addActionListener(new ActionListener()
        {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			           findByID(T1.getText().toUpperCase());// ?
				       if(C1.getItemCount()==0)
				       {
				    	   JOptionPane.showMessageDialog(null, "û������û������Ŀ�!");
				       }
			}
       	 
        });
        
		Unreg_btn = new JButton("ȡ��ע��");
		Unreg_btn.setBackground(Color.RED);
		Unreg_btn.addActionListener(new ActionListener()
        {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				CardInfo card=(CardInfo)C1.getSelectedItem();
				CardAuth.UnRegCard(card);
				C1.removeItem(card);
				findByID(card.getID());
				
				
			}


       	 
        });
		
		OK_btn = new JButton("ע��");
		OK_btn.setBackground(Color.GREEN);
		
		OK_btn.addActionListener(new ActionListener()
        {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				
				//ID��֤
				
				EmployeeCardVO stu = CardAuth.getEmpByID(T1.getText().toUpperCase(),false);
				
				//��� һ���ҳ�
				if(stu==null)
				{
					JOptionPane.showMessageDialog(null, "ָ����ѧ�Ų����ڻ��ߴ�������");
				}else
				{
					findByID(T1.getText().toUpperCase());
					try {
						addParent(stu);
						refreashUI();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				findByID(stu.getId());
				
			}


       	 
        });
		Cancel_btn = new JButton("�ر�");
		Cancel_btn.addActionListener(new ActionListener()
        {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			//?
				
			}


       	 
        });
		
		
		Cardsearch_btn = new JButton("������ѯ");
		
		Cardsearch_btn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				findByCard();//?
			}
			
		});
		
		Photo_btn = new JButton("������Ƭ");
		Photo_btn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			//?
			}
			
		});
		
		
		LC=new JLabel("������");
		//LP=new JTextField();
		regWindow.add(LC);
		regWindow.add(LP);
		
		regWindow.add(L1);
	    regWindow.add(L2);
	    regWindow.add(T1);
	   // regWindow.add(T2);
	    regWindow.add(C1);
	    //MOBILE = new JTextField();
		regWindow.add(MOBILE);
		
		JButton upgrade_btn=new JButton("����");
		upgrade_btn.setEnabled(false);
		regWindow.add(upgrade_btn);
		regWindow.add(search_btn);
		regWindow.add(Cardsearch_btn);
		regWindow.add(Photo_btn);
	    regWindow.add(Cancel_btn);
	    regWindow.add(OK_btn);
	    regWindow.add(Unreg_btn);
	    
	    return regWindow;
	}
	
	
	
public boolean addParent(EmployeeCardVO stu) throws Exception {
		boolean isAutoAdd= true;
		EmployeeCardVO holder=null;
		
		String id = GeneNewParentID(stu);
		System.out.println(id);
		String lastName = stu.getLastName();
		String FirstName = stu.getFirstName();
		String UserName = stu.getUserName()+id.split("P")[1];
		String manager = JOptionPane.showInputDialog("������ֿ��˵�������");
		String mobile =JOptionPane.showInputDialog("������ֿ��˵ĵ绰��");
		if (!isAutoAdd && id.equals("")) {
			CommonUI.showMessage(this, HumanResourceUtil
					.getString("EmployeeAddPane.PleaseinputEmployeeID"));
			//txtId.requestFocus();
			return false;
		}
		if (id.trim().length() > 20) {
			CommonUI.showMessage(this, HumanResourceUtil
					.getString("EmployeeAddPane.LengthMoreThan20"));
			//txtId.requestFocus();
			return false;
		}
		if (id.contains("'") || id.contains("\"")) {
			CommonUI.showMessage(this, HumanResourceUtil
					.getString("EmployeeAddPane.EmployeeIDNoQuote"));
			//txtId.requestFocus();
			return false;
		}
		if (lastName.equals("")) {
			CommonUI.showMessage(this, HumanResourceUtil
					.getString("EmployeeAddPane.PleaseinputLastName"));
			//txtLastName.requestFocus();
			return false;
		}
		if (FirstName.equals("")) {
			CommonUI.showMessage(this, HumanResourceUtil
					.getString("EmployeeAddPane.PleaseinputFirstName"));
			//txtFirstName.requestFocus();
			return false;
		}
		
		

		EmployeeCardVO vo = new EmployeeCardVO();
		if (!id.equalsIgnoreCase(""))
			vo.setId(id);
		vo.setLastName(lastName);
		vo.setFirstName(FirstName);
		vo.setManager(manager);
		vo.setMobile(mobile);
		//vo.setUserName(UserName);
		vo.setGender(true);
		vo.setTempWorker(true);   //�ҳ���������Ϊ TRUE
		try {
			
			CardInfo card=CardAuth.GetCardInfobyCard();
			if(card==null) return false;
			if(card.getUser()!=null || card.getHolder()!=null)
			{
				JOptionPane.showMessageDialog(null, "���ſ��Ѿ���ע���"+card.getUser().getId());
				return false;
			}
			
			card = CardAuth.RegCardInfo( card, stu, vo);
			
			UserKey.add(card);
			
			HRServerActionManager.getInstance().addTemporaryEmployee(vo);
			
			EmployeeActionManager.clearCache();
			return true;
		} catch (ServerActionException se) {
			se.printStackTrace();
			CommonUI.showException(this, se);
			return false;
		}
	}




	private String GeneNewParentID(EmployeeCardVO stu)
	{
		int num[] = new int[this.UserKey.size()];
		String prefix = stu.getId();
		int i=0,max=0;
		for(CardInfo card :UserKey)
		{
			if(card.getHolder()!=null)
				num[i]=Integer.parseInt(card.getHolder().getId().split("P")[1]);
			else
				num[i]=0;
			if(num[i]>max) max=num[i];
			i++;
		}
		if(max==999)
			return prefix+"P"+String.valueOf(max);
		else
			return prefix+"P"+String.valueOf(max+1);
		
	}
	
	
	
}
