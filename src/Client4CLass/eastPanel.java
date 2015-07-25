package Client4CLass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import bb.common.EmployeeCardVO;
import KYUI.EastPanel;
import KYUI.KYMainUI;
import KYUI.ScrollInfoPane;



public class eastPanel extends EastPanel {

	sqlQueryArea medTxt;
	//JPanel base;
	eastPanel()
	{
		super();
		getContentPane().setLayout(new BorderLayout());
		
		ScrollInfoPane si=KYMainUI.getInstance().getScrollInfoPane();
		si.setPreferredSize(new Dimension(getSize().width,100));
		getContentPane().add(BorderLayout.NORTH,si);
		getContentPane().add(BorderLayout.CENTER,getAbsentPane());
		getContentPane().add(BorderLayout.SOUTH, getMedPane());
		si.startshow();
	}
	
	private JPanel getAbsentPane() {
		JPanel AbsentPane =new JPanel();
		AbsentPane.setLayout(new BorderLayout());
		AbsentPane.setBackground(new Color(161,205,95));
		
		JLabel Lbl= new JLabel("上日缺勤宝宝追踪");
		Lbl.setPreferredSize(new Dimension(300,50));
		
		Collection<EmployeeCardVO> Student_absent = getAbsentList();
		
		JPanel buttonPanel =new JPanel();
		int col=2;
		int row=Student_absent.size()/2;
		if(Student_absent.size()%2!=0) row++;
		JButton[] btnArray= new JButton[row*col];
		if(row>5)
		  buttonPanel.setLayout(new GridLayout(row,col));
		else
			buttonPanel.setLayout(new GridLayout(5,col));
		Font btnFont =(new Font("SimHei",0,20));
		for(int i=0;i<btnArray.length;i++)
		{
			btnArray[i]= new JButton();
			btnArray[i].setPreferredSize(new Dimension(150,100));
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
		for(EmployeeCardVO e:Student_absent)
		{
			btnArray[btnCount++].setText(e.getCompanyAddressBookName());
			//System.out.println(e.getCompanyAddressBookName());
		}
		
		
		JScrollPane jsp = new JScrollPane(buttonPanel);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//jsp.setPreferredSize(new Dimension(300,300));
		
		AbsentPane.add(Lbl,BorderLayout.NORTH);
		AbsentPane.add(jsp,BorderLayout.CENTER);
		
		
		
		return AbsentPane;
	}
	
	private Collection<EmployeeCardVO> getAbsentList() {
		// TODO Auto-generated method stub
		List<String> queryIDList = new LinkedList();
		Collection<EmployeeCardVO> totelStudennt =RollCallUI.getStudentPresent();
		
		Collection<EmployeeCardVO> ret= new HashSet<EmployeeCardVO>();
		ret.addAll(totelStudennt);
		try {
			Connection conn =KYClassUI.bds.getConnection();
			PreparedStatement p= conn.prepareStatement("select distinct(id) from emp_logginrecordall where L_date=? and department=?;");
			p.setString(2, KYMainUI.department);
			//p.setString(1,"2015-01-08");
			java.util.Date dd=new java.util.Date();
			SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd E ");
			System.out.println(time.format(dd));
			long datback=0;
			if(time.format(dd).contains("星期一"))
			{
				datback=2*1000*60*60*24;
			}else
			{
				datback=1000*60*60*24;
			}
			p.setDate(1,new java.sql.Date(System.currentTimeMillis()-datback));  //formal run un comment
			System.out.println(p);
			ResultSet rs = p.executeQuery();
			
			while(rs.next())
			{
				queryIDList.add(rs.getString("id"));
			}
			
			rs.close();
			p.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(EmployeeCardVO emp:totelStudennt)
		{
			if(queryIDList.contains(emp.getId()))
					{
						ret.remove(emp);
					}
		}
		
		
		return ret;
	}

	private JPanel getMedPane() {
		// TODO Auto-generated method stub
		JPanel medPane =new JPanel();
		medPane.setLayout(new BorderLayout());
		medPane.setBackground(new Color(161,205,95));
		
		JLabel medLbl= new JLabel("服药记录");
		medLbl.setPreferredSize(new Dimension(300,50));
		medTxt = new sqlQueryArea() ;
		
		medTxt.setDataSource(KYClassUI.bds);
		String[] Title={"姓名","服药记录"};
		String[] attributes={"name_company_address_book","H_detail"};
		medTxt.setTitles(Title);
		medTxt.setAttributes(attributes);
		medTxt.setSqlTxt("select emp_healthcheckdata.id,H_detail,emp_id.name_company_address_book,H_date "
				+ "from emp_healthcheckdata join emp_id on emp_healthcheckdata.id = emp_id.id  "
				+ "where department = '"+KYMainUI.department+"'and H_date='"+(new java.sql.Date(System.currentTimeMillis()))+"' and H_type = '107' "
						+ "order by H_time;");
		medPane.add(medLbl,BorderLayout.NORTH);
		
		JScrollPane jsp = new JScrollPane(medTxt);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setPreferredSize(new Dimension(300,300));
		medPane.add(jsp,BorderLayout.CENTER);
		
		medTxt.refresh();
		return medPane;
	}
	
	@Override
	public void refresh()
	{
		medTxt.refresh();
	}
	


}
