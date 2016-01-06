package Simple.HR;

import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import bb.common.EmployeeCardVO;
import bb.gui.ServerActionException;
import bb.gui.base.DepartmentComboBox;
import bb.gui.hr.EmployeeActionManager;
import bb.gui.server.HRServerActionManager;

public class SimpleTeacherArrange {
	
	public SimpleTeacherArrange(EmployeeCardVO vo)
	{
		
		DepartmentComboBox dc=new DepartmentComboBox();
		Collection<String> departments= EmployeeActionManager.getDepartmentList();
		
		
		departments.remove("教工部");
		departments.remove("综合办公室");
		departments.remove("园长办公室");

		String title="";
		if(vo.getJobId().equals(""))
		{
			title=vo.getPosition()+vo.getCompanyAddressBookName()+"没有被分配到任何一个班级。";
		}else
		{
			title=vo.getPosition()+vo.getCompanyAddressBookName()+"当前班级为'"+vo.getJobId()+"'";
		}
		
		String input=(String) JOptionPane.showInputDialog(null,title,"请选择分配到的班级:",JOptionPane.INFORMATION_MESSAGE
			,null,departments.toArray(),"");
		
		if(JOptionPane.showConfirmDialog(null, "请确认"+vo.getCompanyAddressBookName()+"被分配至'"+input+"'")==JOptionPane.YES_OPTION)
		{
			vo.setJobId(input);
			try {
				String employeeId = HRServerActionManager.getInstance()
						.updateEmployeeCard(vo);
				System.out.println("---------------"+HRServerActionManager.getInstance()
					.getEmployeeCardById(employeeId).getJobId());
			} catch (ServerActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}


}
