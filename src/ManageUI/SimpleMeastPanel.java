package ManageUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import bb.common.EmployeeCardVO;
import bb.gui.ServerActionException;
import bb.gui.server.HRServerActionManager;
import Client4CLass.sqlQueryArea;
import KYUI.EastPanel;
import KYUI.KYMainUI;
import KYUI.ScrollInfoPane;



public class SimpleMeastPanel extends EastPanel{
	sqlQueryArea medTxt;
	JPanel base;
	JButton[] btnArray;
	SimpleMeastPanel()
	{
		super();
		this.setLayout(new BorderLayout());
		
		ScrollInfoPane si=KYMainUI.getInstance().getScrollInfoPane();
		si.setPreferredSize(new Dimension(getSize().width,100));   ///此处代码需要统一为eastPanewidget
		
		this.add(BorderLayout.CENTER,getTeacherPane());
		this.add(BorderLayout.NORTH,si);
		this.add(BorderLayout.SOUTH,getMedPane());
		si.startshow();
		
	}
	
	private JPanel getTeacherPane() {
		JPanel AbsentPane =new JPanel();
		AbsentPane.setLayout(new BorderLayout());
		AbsentPane.setBackground(new Color(161,205,95));
		
		JLabel Lbl= new JLabel("本日教工出勤");
		Lbl.setPreferredSize(new Dimension(300,50));
		Hashtable<String,List<String[]>> empststus = new Hashtable<String,List<String[]>>();
		Collection<EmployeeCardVO> EmpList = getEmpList(empststus);
		
		JPanel buttonPanel =new JPanel();
		int col=2;
		int row=EmpList.size()/2;
		if(EmpList.size()%2!=0) row++;
		btnArray= new JButton[row*col];
		if(row>5)
		  buttonPanel.setLayout(new GridLayout(row,col));
		else
			buttonPanel.setLayout(new GridLayout(5,col));
		Font btnFont =(new Font("SimHei",0,20));
		for(int i=0;i<btnArray.length;i++)
		{
			btnArray[i]= new JButton();
			btnArray[i].setPreferredSize(new Dimension(150,70));
			
			btnArray[i].setBackground(new Color(161,205,95));
			btnArray[i].setFont(btnFont);
			buttonPanel.add(btnArray[i]);
		}
		if(row<5)
		{
			for(int i= btnArray.length;i<5*col;i++)
				buttonPanel.add(new JLabel(""));
		}
		
		System.out.println(btnArray.length);
		//System.out.println(btnArray[0]);
		int btnCount=0;
		for(EmployeeCardVO e:EmpList)
		{
			List<String[]> states=empststus.get(e.getId());
			if(states!=null)
			{
				
				String[] state_str=null;
				String btnTips="";
				for(String[] temp:states)
				{
					state_str=temp;
					
					if(state_str[0].equals("0"))
					{
						btnTips+=(state_str[1]+"<br>");
					}else if(Integer.parseInt(state_str[0])%2==0)
					{
						btnTips+=(" <br>"+state_str[1]+"<br>");
					}
					else if(Integer.parseInt(state_str[0])%2==1)
					{
						btnTips+=(" | <br>"+state_str[1]+"<br>");
					}
					
					
				}
				
				btnTips="<html><body>"+btnTips+"<body></html>";
				
				btnArray[btnCount].setText("<html><body>"+e.getCompanyAddressBookName()+"<br>"+state_str[1]+"<body></html>");
				btnArray[btnCount].setToolTipText(btnTips);
				if(Integer.parseInt(state_str[0])%2==0)
				{
					btnArray[btnCount].setBackground(new Color(161,205,95));
				}else
				{
					btnArray[btnCount].setBackground(Color.ORANGE);
				}
				 
				
			}else
			{
				btnArray[btnCount].setText(e.getCompanyAddressBookName());
				btnArray[btnCount].setBackground(Color.LIGHT_GRAY);
			}
			
			
			
			btnCount++;;
			//System.out.println(e.getCompanyAddressBookName());
		}
		
		
		JScrollPane jsp = new JScrollPane(buttonPanel);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setPreferredSize(new Dimension(300,300));
		
		AbsentPane.add(Lbl,BorderLayout.NORTH);
		AbsentPane.add(jsp,BorderLayout.CENTER);

		return AbsentPane;
	}
	
	
	
	@Override
	public void refresh()
	{
		
		
		
		Hashtable<String,List<String[]>> empststus = new Hashtable<String,List<String[]>>();
		Collection<EmployeeCardVO> EmpList = getEmpList(empststus);
		
		
		System.out.println(btnArray.length);
		//System.out.println(btnArray[0]);
		int btnCount=0;
		for(EmployeeCardVO e:EmpList)
		{
			List<String[]> states=empststus.get(e.getId());
			if(states!=null)
			{
				
				String[] state_str=null;
				String btnTips="";
				for(String[] temp:states)
				{
					state_str=temp;
					
					if(state_str[0].equals("0"))
					{
						btnTips+=(state_str[1]+"<br>");
					}else if(Integer.parseInt(state_str[0])%2==0)
					{
						btnTips+=(" <br>"+state_str[1]+"<br>");
					}
					else if(Integer.parseInt(state_str[0])%2==1)
					{
						btnTips+=(" | <br>"+state_str[1]+"<br>");
					}
					
					
				}
				
				btnTips="<html><body>"+btnTips+"<body></html>";
				
				btnArray[btnCount].setText("<html><body>"+e.getCompanyAddressBookName()+"<br>"+state_str[1]+"<body></html>");
				btnArray[btnCount].setToolTipText(btnTips);
				if(Integer.parseInt(state_str[0])%2==0)
				{
					btnArray[btnCount].setBackground(new Color(161,205,95));
				}else
				{
					btnArray[btnCount].setBackground(Color.ORANGE);
				}
				 
				
			}else
			{
				btnArray[btnCount].setText(e.getCompanyAddressBookName());
				btnArray[btnCount].setBackground(Color.LIGHT_GRAY);
			}
			
			
			
			btnCount++;;
			//System.out.println(e.getCompanyAddressBookName());
		}
		
		
		medTxt.refresh();
	}
	
	private Collection<EmployeeCardVO> getEmpList(Hashtable<String,List<String[]>> empststus) {
		// TODO Auto-generated method stub
		
		Collection<EmployeeCardVO> ret= new HashSet<EmployeeCardVO>();
		
		Collection<EmployeeCardVO> emps;
		try {
			emps = HRServerActionManager.getInstance().findEmployeeCardsByDepartment("教工部", false, 0, 1000);
			for(EmployeeCardVO e:emps)
			{
				if(e.getSecurityLevel().equals("LEVEL 3"))
					ret.add(e);
			}
			
			emps=HRServerActionManager.getInstance().findEmployeeCardsByDepartment("综合办公室", false, 0, 1000);
			for(EmployeeCardVO e:emps)
			{
				if(e.getSecurityLevel().equals("LEVEL 3"))
					ret.add(e);
			}
		} catch (ServerActionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			Connection conn =KYMainUI.bds.getConnection();
			PreparedStatement p= conn.prepareStatement("select id,L_status,L_time from emp_logginrecord;");

			ResultSet rs = p.executeQuery();
			
			while(rs.next())
			{
				if(empststus.get(rs.getString("id"))==null)
				{
					String[] state_time= {rs.getString("L_status"),rs.getString("L_time")};
					List<String[]> temp = new LinkedList<String[]>();
					temp.add(state_time);
					empststus.put(rs.getString("id"),temp);
				}else
				{
					List<String[]> temp = empststus.get(rs.getString("id"));
					String[] state_time= {rs.getString("L_status"),rs.getString("L_time")};
					temp.add(state_time);
					empststus.put(rs.getString("id"),temp);
				}
				
			}
			
			rs.close();
			p.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ret;
	}

	private JPanel getMedPane() {
		// TODO Auto-generated method stub
		JPanel medPane =new JPanel();
		medPane.setLayout(new BorderLayout());
		medPane.setBackground(new Color(161,205,95));
		
		JLabel medLbl= new JLabel("请假记录");
		medLbl.setPreferredSize(new Dimension(300,50));
		medTxt = new sqlQueryArea() ;
		
		medTxt.setDataSource(KYMainUI.bds);
		String[] Title={"姓名","是否中午外出","请假原因"};
		String[] attributes={"app_name","app_currentNoon","app_reason"};
		java.sql.Date currdate = new  java.sql.Date(System.currentTimeMillis());
		
		medTxt.setTitles(Title);
		medTxt.setAttributes(attributes);
		medTxt.setSqlTxt("select * from emp_askforleave where (app_from='"+currdate.toString()+"' and app_currentNoon='1') or (app_from<='"+currdate.toString()+"' and app_to>='"+currdate.toString()+"' and app_currentNoon='0') order by app_currentNoon;");
		medPane.add(medLbl,BorderLayout.NORTH);
		
		JScrollPane jsp = new JScrollPane(medTxt);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//jsp.setPreferredSize(new Dimension(300,300));
		medPane.add(jsp,BorderLayout.CENTER);
		
		medTxt.refresh();
		return medPane;
	}
	

	


}
