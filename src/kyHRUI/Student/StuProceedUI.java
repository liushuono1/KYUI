package kyHRUI.Student;

import javax.swing.JPanel;

import FinaceUI.Manege.paymentUI;
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
		this.setTitle(VO.getCompanyAddressBookName()+"个人信息");
		HomePageGroupPane group = getTabPane(0).getGroupPane(0);
		
		group.addSearchPane(null,"查找");
		MulHomePage lookUpAndSearchPane = new MulHomePage();
		javax.swing.JPanel pane  = (JPanel) new BasicActionPane(VO)
		{
			
			public  String getAddUiTitle()
			{
				return "查看详细信息";
			};
			
			public ClientUI getSearchResultUI() {
				StudentUI ui = new StudentUI();
				ui.setVO(VO);
				return  ui;
			}
		};
		lookUpAndSearchPane.add(pane);
		group.addSearchPane(lookUpAndSearchPane);
		
		
		lookUpAndSearchPane = new MulHomePage();
		pane  = (JPanel) new BasicActionPane(VO)
			{
				
				public  String getAddUiTitle()
				{
					return "接送卡注册";
				};
				
				public ClientUI getSearchResultUI() {
					CardRegUI ui = new CardRegUI(VO.getId());
					return  ui;
				}
			};
			lookUpAndSearchPane.add(pane);
			group.addSearchPane(lookUpAndSearchPane);
			
			
			lookUpAndSearchPane = new MulHomePage();
			pane  = (JPanel) new BasicActionPane(VO)
				{
					
					public  String getAddUiTitle()
					{
						return "园费缴纳";
					};
					
					public ClientUI getSearchResultUI() {
						paymentUI ui = new paymentUI(VO);
						return  ui;
					}
				};
				lookUpAndSearchPane.add(pane);
				group.addSearchPane(lookUpAndSearchPane);

	}
}