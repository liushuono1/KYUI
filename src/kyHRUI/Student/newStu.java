package kyHRUI.Student;

import java.util.Collection;
import java.util.List;

import bb.common.EmployeeCardVO;
import bb.gui.ServerActionException;
import bb.gui.base.AddActionPane;
import bb.gui.hr.EmployeeActionManager;
import bb.gui.hr.HumanResourceUtil;
import bb.gui.server.HRServerActionManager;
import bb.gui.swing.homepage.HomePageAddPane;

public class newStu extends HomePageAddPane {


	String id="";
	String classType;
public newStu() {
	
}

public newStu(String ClassType) {

	classType=ClassType;
}


@Override
public AddActionPane getAddActionPane() {
	
	String id=geneID();
	
	return new StudentAddPane(id,classType);
}


public String geneID()
{
	Collection<EmployeeCardVO> temp=null;
	try {
		
		temp= HRServerActionManager.getInstance().findEmployeeCardsByDepartment(classType, false, 0,200);
	} catch (ServerActionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	if(temp.size()==0)
	{
		return firstIDbyType(classType);
	}
	
	int intID=0;
	for(EmployeeCardVO empvo:temp)
	{
		int tempid=0;
		System.out.println(empvo.getId());
		 tempid=Integer.parseInt(empvo.getId().replace("KY", ""));
		if(tempid>intID)
			intID=tempid;
	}
	String newID="KY"+String.valueOf(++intID); 
	
	
	try {
		//System.out.println("if new ID "+newID+" "+HRServerActionManager.getInstance().isCustomEmployeeExist(newID));
		while(HRServerActionManager.getInstance().isEmployeeExistent(newID))
		{
			newID="KY"+String.valueOf(++intID);
		}
	} catch (ServerActionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	String year=String.valueOf(new java.sql.Date(System.currentTimeMillis()).getYear()+1900);
	System.out.println(year);
	if(newID.contains("KY"+year)) //�ж��������ȡ��ID��������Ҫ�����û��
	{
		return newID; 
	}else
	{
		return firstIDbyType(classType);
	}
}


public String firstIDbyType(String classtype)
{
	String year=String.valueOf(new java.sql.Date(System.currentTimeMillis()).getYear());
	Collection<String> deptss= EmployeeActionManager.getDepartmentList();
	deptss.remove("԰���칫��");
	deptss.remove("�̹���");
	deptss.remove("�ۺϰ칫��");
	deptss.remove("");
	deptss.remove("��ҵ");  //��Ҫ���ⲿ�������⡣����
	
	int d =((List<String>)deptss).indexOf(classtype);
	
	String newID = "KY"+year+String.valueOf(d)+"00"; //������Ҫ���ⲿ����
	
	return newID;
}


@Override
public String getAddUiTitle() {
	return HumanResourceUtil
			.getString("EmployeeActionManager.AddanEmployeeCard");
}

@Override
public String getButtonText() {
	return "����µ�ѧ��";
}






}
