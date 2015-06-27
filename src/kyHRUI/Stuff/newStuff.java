package kyHRUI.Stuff;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

import Client4CLass.RollCallUI;
import FinaceUI.Manege.FeesUtils;
import FinaceUI.Manege.setFees.one_fee;
import KYUI.KYMainUI;
import bb.common.EmployeeCardVO;
import bb.gui.ClientConst;
import bb.gui.ServerActionException;
import bb.gui.base.AddActionPane;
import bb.gui.hr.HumanResourceUtil;
import bb.gui.server.HRServerActionManager;
import bb.gui.swing.homepage.HomePageAddPane;

public class newStuff extends HomePageAddPane {


	String id="";
	String classType;
public newStuff() {
	
}

public newStuff(String ClassType) {

	classType=ClassType;
}


public AddActionPane getAddActionPane() {
	
	String id=geneID();
	
	return new StuffAddPane(id,classType);
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
		 tempid=Integer.parseInt(empvo.getId().replace("K", ""));
		if(tempid>intID)
			intID=tempid;
	}

	return "K"+String.valueOf(intID+1);     //δ������ݱ仯 ���Ժ���
}


public String firstIDbyType(String classtype)
{
	String YEAR = String.valueOf((new java.sql.Date(System.currentTimeMillis())).getYear());
	
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
	}else if(classtype.contains("�̹���"))
	{
		return "K"+YEAR+"001";
	}
	
	
	return "";
}


public String getAddUiTitle() {
	return HumanResourceUtil
			.getString("EmployeeActionManager.AddanEmployeeCard");
}

public String getButtonText() {
	return "�����µ�ѧ��";
}






}