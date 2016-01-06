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
		
		
		departments.remove("�̹���");
		departments.remove("�ۺϰ칫��");
		departments.remove("԰���칫��");

		String title="";
		if(vo.getJobId().equals(""))
		{
			title=vo.getPosition()+vo.getCompanyAddressBookName()+"û�б����䵽�κ�һ���༶��";
		}else
		{
			title=vo.getPosition()+vo.getCompanyAddressBookName()+"��ǰ�༶Ϊ'"+vo.getJobId()+"'";
		}
		
		String input=(String) JOptionPane.showInputDialog(null,title,"��ѡ����䵽�İ༶:",JOptionPane.INFORMATION_MESSAGE
			,null,departments.toArray(),"");
		
		if(JOptionPane.showConfirmDialog(null, "��ȷ��"+vo.getCompanyAddressBookName()+"��������'"+input+"'")==JOptionPane.YES_OPTION)
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
