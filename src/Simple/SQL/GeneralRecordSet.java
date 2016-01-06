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
				// 获得所有列的数目及实际列数
				int columnCount = data.getColumnCount();
				// 获得指定列的列名
				String columnName = data.getColumnName(i);
				recordSet.Col_name[i-1]=columnName;
				// 获得指定列的列值
				//int columnType = data.getColumnType(i);
				// 获得指定列的数据类型名
				String columnTypeName = data.getColumnTypeName(i);
				recordSet.Type_table[i-1]= columnTypeName;
				// 所在的Catalog名字
				//String catalogName = data.getCatalogName(i);
				// 对应数据类型的类
				String columnClassName = data.getColumnClassName(i);
				recordSet.Class_table[i-1]=columnClassName;
				// 在数据库中类型的最大字符个数
				//int columnDisplaySize = data.getColumnDisplaySize(i);
				// 默认的列的标题
				//String columnLabel = data.getColumnLabel(i);
				// 获得列的模式
				//String schemaName = data.getSchemaName(i);
				// 某列类型的精确度(类型的长度)
				//int precision = data.getPrecision(i);
				// 小数点后的位数
				//int scale = data.getScale(i);
				// 获取某列对应的表名
				//String tableName = data.getTableName(i);
				// 是否自动递增
				//boolean isAutoInctement = data.isAutoIncrement(i);
				// 在数据库中是否为货币型
				//boolean isCurrency = data.isCurrency(i);
				// 是否为空
				//int isNullable = data.isNullable(i);
				// 是否为只读
				//boolean isReadOnly = data.isReadOnly(i);
				// 能否出现在where中
				//boolean isSearchable = data.isSearchable(i);
				System.out.println("获得列"+i+"字段名称:"+columnName);
				System.out.println("获得列"+i+"的数据类型名:"+columnTypeName);
				System.out.println("获得列"+i+"对应数据类型的类:"+columnClassName);
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
