package kyHRUI.Stuff;

import javax.swing.JPanel;

import FinaceUI.Manege.paymentUI;
import ManageUI.CardRegUI;
import bb.common.EmployeeCardVO;
import bb.gui.base.BaseHomePage;
import bb.gui.base.ClientUI;
import bb.gui.swing.MulHomePage;
import bb.gui.swing.homepage.HomePageGroupPane;
import kyHRUI.Student.BasicActionPane;

public class StffProceedUI extends BaseHomePage {

	EmployeeCardVO VO =new EmployeeCardVO();
	
	
	public StffProceedUI() {
		super(1);
		

	}
	
	
	public StffProceedUI(EmployeeCardVO vo) {
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
		javax.swing.JPanel pane  = new BasicActionPane(VO)
		{
			
			@Override
			public  String getAddUiTitle()
			{
				return "查看详细信息";
			};
			
			@Override
			public ClientUI getSearchResultUI() {
				StuffUI ui = new StuffUI();
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
					return "考勤卡注册";
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
						return "班级分配";
					};
					
					@Override
					public ClientUI getSearchResultUI() {
						paymentUI ui = new paymentUI(VO);
						return  ui;
					}
				};
				lookUpAndSearchPane.add(pane);
				group.addSearchPane(lookUpAndSearchPane);

	}
}