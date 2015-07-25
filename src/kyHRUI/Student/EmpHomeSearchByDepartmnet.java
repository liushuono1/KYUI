package kyHRUI.Student;



import bb.common.EmployeeCardVO;
import bb.gui.ServerActionException;
import bb.gui.base.*;
import bb.gui.hr.EmployeeListUI;
import bb.gui.hr.HumanResourceUtil;
import bb.gui.server.HRServerActionManager;
import bb.gui.swing.homepage.HomePageMultiRowSearchPane;

import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JComponent;

// Referenced classes of package bb.gui.hr:
//            HumanResourceUtil, EmployeeListUI

public class EmpHomeSearchByDepartmnet extends
		HomePageMultiRowSearchPane {
	static DepartmentComboBox dc=new DepartmentComboBox();
	static {
		dc.removeItem("教工部");
		dc.removeItem("综合办公室");
		dc.removeItem("园长办公室");
	}
	public EmpHomeSearchByDepartmnet() {
		super(
				new String[] { "按班级查询" },
				new JComponent[] { dc },
				"按班级查询");
		
	}

	@Override
	public ClientUI getSearchResultUI() throws ServerActionException {
		final String input = ((DepartmentComboBox) getInputComponent(0))
				.getSelectedItem().toString();
		EmployeeListUI ui = new EmployeeListUI() {

			@Override
			public Collection getDataOnQuickSearch(String searchText,
					int firstIndex, int length) throws ServerActionException {
				return HRServerActionManager.getInstance()
						.findEmployeeCardsByDepartment(searchText, false,
								firstIndex, length);
			}

			@Override
			public int getCountOnQuickSearch(String searchText)
					throws ServerActionException {
				return HRServerActionManager.getInstance()
						.getEmployeeCardsByDepartmentCount(searchText, false);
			}

			private String getDepartment() {
				return (String) getValue(0);
			}

			@Override
			public Collection queryFromServer(int firstIndex, int maxLength)
					throws ServerActionException {
				return HRServerActionManager.getInstance()
						.findEmployeeCardsByDepartment(getDepartment(), false,
								firstIndex, maxLength);
			}

			@Override
			public int getTotalCount() throws ServerActionException {
				return HRServerActionManager.getInstance()
						.getEmployeeCardsByDepartmentCount(getDepartment(),
								false);
			}

			@Override
			public String getQuickSearchTitle(String searchText) {
				return HumanResourceUtil.getString(
						"EmployeeHomePageByDepartmentSearchPane.Header",
						searchText);
			}


			private int getStudentcount( String Input) throws ServerActionException
			{
				
				Collection<EmployeeCardVO> Result = HRServerActionManager.getInstance()
						.findEmployeeCardsByDepartment(input, false,
								0, 10000); //暂时定为 10000人 
				
				int count=0;
				for(EmployeeCardVO emp:Result)
				{
					if(emp.getSecurityLevel().equals("LEVEL 4") || emp.getSecurityLevel().equals("LEVEL 5"))
					{
						count++;
					}
				}
				System.out.println("COUNT  "+count);
				return count;
			}
			
			private Collection findStudentCardsByDept( String Input, int firstIndex, int maxLength) throws ServerActionException
			{
				
				int start=firstIndex,end=firstIndex+maxLength;
				Collection<EmployeeCardVO> temp = HRServerActionManager.getInstance()
						.findEmployeeCardsByDepartment(input, false,
								0, 10000); //暂时定为 10000人 
				Collection<EmployeeCardVO> result =new LinkedList();
				for(EmployeeCardVO emp:temp)
				{
					if(emp.getSecurityLevel().equals("LEVEL 4") || emp.getSecurityLevel().equals("LEVEL 5"))
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
		
		
		{
			
		}
		};
		

		
		
		
		ui.setTitle("学生名单");
		ui.getOperationPane().setSearchText(input);
		String key = "EmployeeHomePageByDepartmentSearchPane.Header";
		String text = HumanResourceUtil.getString(key, input);
		ui.setHeaderTitle(text);
		return ui;
	}
}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\workspace\libs\gui.jar
	Total time: 20 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/