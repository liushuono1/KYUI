package kyHRUI.Stuff;

import java.util.Collection;
import bb.common.EmployeeCardVO;
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


@Override
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

	return "K"+String.valueOf(intID+1);     //未考虑年份变化 ，以后补齐
}


public String firstIDbyType(String classtype)
{
	String YEAR = String.valueOf((new java.sql.Date(System.currentTimeMillis())).getYear());
	
	if(classtype.contains("宝一班"))
	{
		return "KY"+YEAR+"001";
	}else if(classtype.contains("宝二班"))
	{
		return "KY"+YEAR+"101";
	}else if(classtype.contains("宝三班"))
	{
		return "KY"+YEAR+"701";
	}else if(classtype.contains("小一班"))
	{
		return "KY"+YEAR+"201";
	}else if(classtype.contains("小二班"))
	{
		return "KY"+YEAR+"301";
	}else if(classtype.contains("中班"))
	{
		return "KY"+YEAR+"501";
	}else if(classtype.contains("大班"))
	{
		return "KY"+YEAR+"601";
	}else if(classtype.contains("教工部"))
	{
		return "K"+YEAR+"001";
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
	return "添加新的学生";
}






}
