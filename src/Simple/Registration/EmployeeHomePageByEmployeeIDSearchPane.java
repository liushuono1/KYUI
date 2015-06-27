package Simple.Registration;

import bb.gui.ServerActionException;
import bb.gui.base.AbstractActionManager;
import bb.gui.base.ClientUI;
import bb.gui.base.ListOperationPane;
import bb.gui.hr.EmployeeListUI;
import bb.gui.hr.HumanResourceUtil;
import bb.gui.server.HRServerActionManager;
import bb.gui.swing.homepage.HomePageSingleTextFieldSearchPane;

import java.util.Collection;

import kyHRUI.Student.StudentActionManager;
import kyHRUI.Student.StudentListUI;

// Referenced classes of package bb.gui.hr:
//            HumanResourceUtil, EmployeeListUI

public class EmployeeHomePageByEmployeeIDSearchPane extends
		HomePageSingleTextFieldSearchPane {

	public EmployeeHomePageByEmployeeIDSearchPane() {
		super("查看名单",true);
		/*super(HumanResourceUtil
				.getString("EmployeeHomePageByEmployeeIDSearchPane.Super"),
				true);*/
	}

	protected ClientUI getSearchResultUI(final String input)
			throws ServerActionException {
		    EmployeeListUI ui = new EmployeeListUI() {
			
			protected AbstractActionManager createActionManager() {
				
				System.out.println("Self action added");
				
				//System.out.println(getActionManager());
				
				AbstractActionManager action = new StudentActionManager(this);
				return action;
			}

			
			public String getTitle()
			{
				return "宝宝名单";
				
			}

			public Collection getDataOnQuickSearch(String searchText,
					int firstIndex, int length) throws ServerActionException {
				System.out.println(((StudentActionManager)getActionManager()).s);
				System.out.println("serach text is ---->"+searchText);
				return HRServerActionManager.getInstance()
						.findEmployeeCardsByEmployeeId(searchText, false,
								firstIndex, length);
			}

			public int getCountOnQuickSearch(String searchText)
					throws ServerActionException {
				return HRServerActionManager.getInstance()
						.getEmployeeCardsCountByEmployeeId(searchText, false);
			}

			public Collection queryFromServer(int firstIndex, int maxLength)
					throws ServerActionException {

				System.out.println(((StudentActionManager)getActionManager()).s);
				System.out.println("in the method------------------->"+input);
				return HRServerActionManager.getInstance()
						.findEmployeeCardsByEmployeeId(input, false,
								firstIndex, maxLength);
			}

			public int getTotalCount() throws ServerActionException {
				return HRServerActionManager.getInstance()
						.getEmployeeCardsCountByEmployeeId(input, false);
			}

			public String getQuickSearchTitle(String searchText) {
				return HumanResourceUtil.getString(
						"EmployeeHomePageByEmployeeIDSearchPane.Header",
						searchText);
			} 

		
		};
		
		System.out.println("----------------------->return UI"+input);
		ui.setTitle("自定义学生名单");
		
		/*ui.setTitle(HumanResourceUtil
				.getString("EmployeeHomePageByEmployeeIDSearchPane.Title"));
		ui.setHeaderTitle(HumanResourceUtil.getString(
				"EmployeeHomePageByEmployeeIDSearchPane.Header", input));*/
		return ui;
		
		//System.out.println("7777777777");
		//return null;
	}
}

