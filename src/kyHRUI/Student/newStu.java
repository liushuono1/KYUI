package kyHRUI.Student;

import java.util.Collection;
import bb.common.EmployeeCardVO;
import bb.gui.ServerActionException;
import bb.gui.base.AddActionPane;
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

	return "KY"+String.valueOf(intID+1);     //δ������ݱ仯 ���Ժ���
}


public String firstIDbyType(String classtype)
{
	if(classtype.contains("��һ��"))
	{
		return "KY2015001";
	}else if(classtype.contains("������"))
	{
		return "KY2015101";
	}else if(classtype.contains("������"))
	{
		return "KY2015701";
	}else if(classtype.contains("Сһ��"))
	{
		return "KY2015201";
	}else if(classtype.contains("С����"))
	{
		return "KY2015301";
	}else if(classtype.contains("�а�"))
	{
		return "KY2015501";
	}else if(classtype.contains("���"))
	{
		return "KY2015601";
	}
	
	return "";
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
