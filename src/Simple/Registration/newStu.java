package Simple.Registration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import kyHRUI.Student.StudentAddPane;
import bb.gui.base.AddActionPane;
import bb.gui.hr.HumanResourceUtil;
import bb.gui.swing.homepage.HomePageAddPane;

public class newStu extends HomePageAddPane {


	String id="";
	int classType;
public newStu() {
	
}


public newStu(String ID) {
	id=ID;
	
	System.out.println(id);
	System.out.println(getButtonText());
}


public newStu(int ClassType) {

	classType=ClassType;
}


@Override
public AddActionPane getAddActionPane() {
	return new StudentAddPane(geneID(),""); //错误的
}


public String geneID()
{
	Properties prop;
	String idcount;
	switch(classType)
	{
		case 0:
			 prop = this.readProperties();
			prop.put("class"+String.valueOf(classType),String.valueOf(Integer.valueOf(prop.getProperty("class"+String.valueOf(classType)))+1));
			this.writeProperties(prop);
			 idcount="0000"+prop.getProperty("class"+String.valueOf(classType));
			id="KY2015"+idcount.substring(idcount.length()-3);
			System.out.println(id);
			break;
		case 1:
			 prop = this.readProperties();
			prop.put("class"+String.valueOf(classType),String.valueOf(Integer.valueOf(prop.getProperty("class"+String.valueOf(classType)))+1));
			this.writeProperties(prop);
			 idcount="0000"+prop.getProperty("class"+String.valueOf(classType));
			id="KY20151"+idcount.substring(idcount.length()-2);
			System.out.println(id);
			break;
		
		case 2:
			 prop = this.readProperties();
			prop.put("class"+String.valueOf(classType),String.valueOf(Integer.valueOf(prop.getProperty("class"+String.valueOf(classType)))+1));
			this.writeProperties(prop);
			 idcount="0000"+prop.getProperty("class"+String.valueOf(classType));
			id="KY20152"+idcount.substring(idcount.length()-2);
			System.out.println(id);
			break;

		case 3:
			 prop = this.readProperties();
			prop.put("class"+String.valueOf(classType),String.valueOf(Integer.valueOf(prop.getProperty("class"+String.valueOf(classType)))+1));
			this.writeProperties(prop);
			 idcount="0000"+prop.getProperty("class"+String.valueOf(classType));
			id="KY20153"+idcount.substring(idcount.length()-2);
			System.out.println(id);
			break;

		case 4:
			 prop = this.readProperties();
			prop.put("class"+String.valueOf(classType),String.valueOf(Integer.valueOf(prop.getProperty("class"+String.valueOf(classType)))+1));
			this.writeProperties(prop);
			 idcount="0000"+prop.getProperty("class"+String.valueOf(classType));
			id="KY20155"+idcount.substring(idcount.length()-2);
			System.out.println(id);
			break;

		case 5:
			 prop = this.readProperties();
			prop.put("class"+String.valueOf(classType),String.valueOf(Integer.valueOf(prop.getProperty("class"+String.valueOf(classType)))+1));
			this.writeProperties(prop);
			 idcount="0000"+prop.getProperty("class"+String.valueOf(classType));
			id="KY20156"+idcount.substring(idcount.length()-2);
			System.out.println(id);
			break;

	
	}

	return id;
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



private Properties readProperties()
{
	Properties prop =new Properties();
	File file = new File("KY.properties");
	if (file.exists())
		try {
			FileInputStream is = new FileInputStream(file);
			prop.load(is);
			is.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	return prop;
}

private void writeProperties(Properties prop)
{
	File file = new File("KY.properties");
	if (file.exists())
		try {
			FileOutputStream out = new FileOutputStream(file);
			prop.store(out, "");
			
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
}



}
