package KYUI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import Simple.Duty;

public class KYUtils {

    public static Object ByteToObject(byte[] bytes) {
	Object obj = null;
	try {
		// bytearray to object
		ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
		ObjectInputStream oi = new ObjectInputStream(bi);

		obj = oi.readObject();
		bi.close();
		oi.close();
	} catch (Exception e) {
		System.out.println("translation" + e.getMessage());
		e.printStackTrace();
	}
        return obj;
    }

	public static byte[] ObjectToByte(java.lang.Object obj) {
		byte[] bytes = null;
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			System.out.println("OBJ type :" + (obj instanceof Duty) );
			oo.writeObject(obj);

			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		} catch (NotSerializableException e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
	}
	
	public static void main(String[] args)
	{

		try {
			Class.forName("org.sqlite.JDBC");
			Connection LocalConn = DriverManager.getConnection("jdbc:sqlite:VCLib.dll");
			
			PreparedStatement p = null;
			ResultSet r = null;
			// 从数据库读取常规任务 ，加入ret
			p = LocalConn.prepareStatement("insert into main values('1',?);");
			String  dd[]={"123","abc","=-["};
			byte[] ba=KYUtils.ObjectToByte(dd);
			p.setBytes(1, ba);
			p.execute();
			
			p = LocalConn.prepareStatement("select * from main where id='1';");
			r=p.executeQuery();
			while(r.next())
			{
				byte[] obs= r.getBytes(2);
				Object o= KYUtils.ByteToObject(obs);
				String  cc[]=(String[])o;
				System.out.println(cc[0]+"  "+cc[2]);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static Properties readProperties(String Filename)
	{
		Properties prop =new Properties();
		File file = new File(Filename);
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

	public static void writeProperties(Properties prop,String Filename)
	{
		File file = new File(Filename);
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
