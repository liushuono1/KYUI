package Simple.SQL;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

public class GeneralRecord {

	Hashtable<String,Object> DATA_table;
	String[] Class_table;
	Object[] raw_data;


	public GeneralRecord() {
		DATA_table= new Hashtable<String,Object> ();
	}

	

	public static void main(String[] args) {
		try {
			Integer x1 = 22;
			Object x2 = (Object) x1;

			System.out.println(x2.getClass());
			Class xxss = (Class.forName("java.lang.Integer"));
			System.out.println(x2.getClass() == xxss);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public void addData(Object string) {
		// TODO Auto-generated method stub
		
	}



}
