package kyHRUI.Student;

import javax.swing.JPanel;

import FinaceUI.Manege.paymentUI;
import FinaceUI.Manege.setFees;
import ManageUI.CardRegUI;
import bb.common.EmployeeCardVO;
import bb.gui.base.BaseHomePage;
import bb.gui.base.ClientUI;
import bb.gui.swing.MulHomePage;
import bb.gui.swing.homepage.HomePageGroupPane;

public class StuProceedUI extends BaseHomePage {

	EmployeeCardVO VO =new EmployeeCardVO();
	
	
	public StuProceedUI() {
		super(1);
		

	}
	
	
	public StuProceedUI(EmployeeCardVO vo) {
		super(1);
		setVO(vo);

	}
	
	public void setVO(EmployeeCardVO vo)
	{
		VO=vo;
		this.setTitle(VO.getCompanyAddressBookName()+"������Ϣ");
		HomePageGroupPane group = getTabPane(0).getGroupPane(0);
		
		group.addSearchPane(null,"����");
		MulHomePage lookUpAndSearchPane = new MulHomePage();
		javax.swing.JPanel pane  = new BasicActionPane(VO)
		{
			
			@Override
			public  String getAddUiTitle()
			{
				return "�鿴��ϸ��Ϣ";
			};
			
			@Override
			public ClientUI getSearchResultUI() {
				StudentUI ui = new StudentUI();
				ui.setVO(VO);
				return  ui;
			}
		};
		lookUpAndSearchPane.add(pane);
		group.addSearchPane(lookUpAndSearchPane);
		
		
		lookUpAndSearchPane = new MulHomePage();
		pane  = new BasicActionPane(VO)
			{
				
				@Override
				public  String getAddUiTitle()
				{
					return "���Ϳ�ע��";
				};
				
				@Override
				public ClientUI getSearchResultUI() {
					CardRegUI ui = new CardRegUI(VO.getId());
					return  ui;
				}
			};
			lookUpAndSearchPane.add(pane);
			group.addSearchPane(lookUpAndSearchPane);
			
			
			lookUpAndSearchPane = new MulHomePage();
			pane  = new BasicActionPane(VO)
				{
					
					@Override
					public  String getAddUiTitle()
					{
						return "԰�ѽ���";
					};
					
					@Override
					public ClientUI getSearchResultUI() {
						paymentUI ui = new paymentUI(VO);
						return  ui;
					}
				};
				lookUpAndSearchPane.add(pane);
				group.addSearchPane(lookUpAndSearchPane);
  //-------------------------------
				
				
				lookUpAndSearchPane = new MulHomePage();
				pane  = new BasicActionPane(VO)
					{
						
						@Override
						public  String getAddUiTitle()
						{
							return "������԰";
						};
						
						@Override
						public ClientUI getSearchResultUI() {
							setFees.chargeFinalFees(VO.getId());
							
							paymentUI ui = new paymentUI(VO);
							return  ui;
						}
					};
					lookUpAndSearchPane.add(pane);
					group.addSearchPane(lookUpAndSearchPane);

	}
}