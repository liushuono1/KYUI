package kyHRUI.Stuff;

import bb.common.EmployeeCardVO;
import bb.gui.ServerActionException;
import bb.gui.base.AbstractActionManager;
import bb.gui.base.ClientUI;
import bb.gui.hr.EmployeeListUI;
import bb.gui.hr.HumanResourceUtil;
import bb.gui.server.HRServerActionManager;
import bb.gui.swing.homepage.HomePageSingleTextFieldSearchPane;

import java.util.Collection;
import java.util.LinkedList;


// Referenced classes of package bb.gui.hr:
//            HumanResourceUtil, EmployeeListUI

public class EmployeeHomePageByIDSearchPane extends
		HomePageSingleTextFieldSearchPane {


		public EmployeeHomePageByIDSearchPane() {
			super("查看名单",true);
			/*super(HumanResourceUtil
					.getString("EmployeeHomePageByEmployeeIDSearchPane.Super"),
					true);*/
		}

		@Override
		protected ClientUI getSearchResultUI(final String input)
				throws ServerActionException {
			    EmployeeListUI ui = new EmployeeListUI() {
				
				@Override
				protected AbstractActionManager createActionManager() {
					
					System.out.println("Self action added");
					
					//System.out.println(getActionManager());
					
					AbstractActionManager action = new StuffActionManager(this);
					return action;
				}

				
				@Override
				public String getTitle()
				{
					return "员工名单";
					
				}

				@Override
				public Collection getDataOnQuickSearch(String searchText,
						int firstIndex, int length) throws ServerActionException {

					Collection<EmployeeCardVO> ret = this.findStudentCardsByEmployeeId(searchText, firstIndex, length);
					return ret;
				}

				@Override
				public int getCountOnQuickSearch(String searchText)
						throws ServerActionException {
					//return HRServerActionManager.getInstance()
							//.getEmployeeCardsCountByEmployeeId(searchText, false);
					
					
					
					return this.getStudentcount(searchText);
				}

				@Override
				public Collection queryFromServer(int firstIndex, int maxLength)
						throws ServerActionException {
					Collection<EmployeeCardVO> ret = this.findStudentCardsByEmployeeId(searchText, firstIndex, maxLength);
					return ret;
				}

				@Override
				public int getTotalCount() throws ServerActionException {
					//return HRServerActionManager.getInstance()
					//		.getEmployeeCardsCountByEmployeeId(input, false);
					
			
					return this.getStudentcount(input);
				}

				@Override
				public String getQuickSearchTitle(String searchText) {
					return HumanResourceUtil.getString(
							"EmployeeHomePageByEmployeeIDSearchPane.Header",
							searchText);
				} 

				private int getStudentcount( String Input) throws ServerActionException
				{
					
					Collection<EmployeeCardVO> Result = HRServerActionManager.getInstance()
							.findEmployeeCardsByEmployeeId(input, false,
									0, 10000); //暂时定为 10000人 
					
					int count=0;
					for(EmployeeCardVO emp:Result)
					{
						if(emp.getSecurityLevel().equals("LEVEL 3"))
						{
							count++;
						}
					}
					System.out.println("COUNT  "+count);
					return count;
				}
				
				private Collection findStudentCardsByEmployeeId( String Input, int firstIndex, int maxLength) throws ServerActionException
				{
					
					int start=firstIndex,end=firstIndex+maxLength;
					Collection<EmployeeCardVO> temp = HRServerActionManager.getInstance()
							.findEmployeeCardsByEmployeeId(input, false,
									0, 10000); //暂时定为 10000人 
					Collection<EmployeeCardVO> result =new LinkedList();
					for(EmployeeCardVO emp:temp)
					{
						if(emp.getSecurityLevel().equals("LEVEL 3"))
						{
							result.add(emp);
						}
					}
					int count=0;
					Collection<EmployeeCardVO> Ret =new LinkedList();
					for(EmployeeCardVO emp:result)
					{
						if(count>=start && count<=end)
						{
							Ret.add(emp);
						}
						count++;
					}
					return Ret;
				}
			
			};
			
		
			
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

