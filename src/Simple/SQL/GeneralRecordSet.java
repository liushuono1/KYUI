package Simple.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class GeneralRecordSet {
	List<GeneralRecord> contents;
	String[] Type_table;
	String[] Class_table;
	String[] Col_name;
	
	public void GeneralRecordSet()
	{
		contents=new LinkedList<GeneralRecord>();
	}
	public static GeneralRecord buildRecordfromResultSet(ResultSet rs) {
		int col;
		
		GeneralRecordSet recordSet= new GeneralRecordSet();

		try {
			ResultSetMetaData data = rs.getMetaData();
			col=data.getColumnCount();
			recordSet.Type_table=new String[col];
			recordSet.Class_table=new String[col];
			recordSet.Col_name=new String[col];
			for (int i = 1; i <= data.getColumnCount(); i++) {
				// ��������е���Ŀ��ʵ������
				int columnCount = data.getColumnCount();
				// ���ָ���е�����
				String columnName = data.getColumnName(i);
				recordSet.Col_name[i-1]=columnName;
				// ���ָ���е���ֵ
				//int columnType = data.getColumnType(i);
				// ���ָ���е�����������
				String columnTypeName = data.getColumnTypeName(i);
				recordSet.Type_table[i-1]= columnTypeName;
				// ���ڵ�Catalog����
				//String catalogName = data.getCatalogName(i);
				// ��Ӧ�������͵���
				String columnClassName = data.getColumnClassName(i);
				recordSet.Class_table[i-1]=columnClassName;
				// �����ݿ������͵�����ַ�����
				//int columnDisplaySize = data.getColumnDisplaySize(i);
				// Ĭ�ϵ��еı���
				//String columnLabel = data.getColumnLabel(i);
				// ����е�ģʽ
				//String schemaName = data.getSchemaName(i);
				// ĳ�����͵ľ�ȷ��(���͵ĳ���)
				//int precision = data.getPrecision(i);
				// С������λ��
				//int scale = data.getScale(i);
				// ��ȡĳ�ж�Ӧ�ı���
				//String tableName = data.getTableName(i);
				// �Ƿ��Զ�����
				//boolean isAutoInctement = data.isAutoIncrement(i);
				// �����ݿ����Ƿ�Ϊ������
				//boolean isCurrency = data.isCurrency(i);
				// �Ƿ�Ϊ��
				//int isNullable = data.isNullable(i);
				// �Ƿ�Ϊֻ��
				//boolean isReadOnly = data.isReadOnly(i);
				// �ܷ������where��
				//boolean isSearchable = data.isSearchable(i);
				System.out.println("�����"+i+"�ֶ�����:"+columnName);
				System.out.println("�����"+i+"������������:"+columnTypeName);
				System.out.println("�����"+i+"��Ӧ�������͵���:"+columnClassName);
				System.out.println();
			}
			
			while(rs.next())
			{
				
				rs.updateTimestamp(4, new Timestamp(System.currentTimeMillis()+2000));
			//	GeneralRecord record=GeneralRecordSet.GenerateRecordFromResultSet(rs);
				
			}
			
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	private GeneralRecord GenerateRecordFromResultSet(ResultSet rs) {
		// TODO Auto-generated method stub
		GeneralRecord record = new GeneralRecord();
	
		try {
			for(int i=0;i<rs.getMetaData().getColumnCount();i++)
			{
				if(rs.getMetaData().getColumnClassName(i).contains("String"))
				{
					record.addData(rs.getString(i));
				}
				else if(rs.getMetaData().getColumnClassName(i).contains("Timestamp"))
				{
					record.addData(rs.getTimestamp(i));
				}
				else if(rs.getMetaData().getColumnClassName(i).contains("Boolean"))
				{
					record.addData(rs.getInt(i));
				}
				else if(rs.getMetaData().getColumnClassName(i).contains("Integer"))
				{
					record.addData(rs.getInt(i));
				}
				else if(rs.getMetaData().getColumnClassName(i).contains("Date"))
				{
					record.addData(rs.getDate(i));
				}
				else if(rs.getMetaData().getColumnClassName(i).contains("Time"))
				{
					record.addData(rs.getTime(i));
				}
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return record;
	}
	
	
	public static void main(String[] args)
	{
		//Connection conn;
		ResultSet r = null;
		try {
			Connection conn;
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://"+"192.168.1.100"+":3307/bb2_test";
			String user = "root"; 
			String password = "root";
	        Class.forName(driver);
	        conn = DriverManager.getConnection(url, user, password);
			PreparedStatement p = null;
			
			p = conn.prepareStatement("select * from duty",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE,ResultSet.HOLD_CURSORS_OVER_COMMIT);
			r = p.executeQuery();
			
		
		}catch(SQLException e)
		{
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		buildRecordfromResultSet(r);
		
	}
	
}
