package Client4CLass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTextArea;

import org.apache.commons.dbcp2.BasicDataSource;

public class sqlQueryArea extends JTextArea {
	JTextArea showArea;
	BasicDataSource bds=null;
	String[] Title, Attributes;
	PreparedStatement pstmt=null;
	String SQLTxt="";
	public sqlQueryArea()
	{
		showArea=this;
		
	}
	
	public sqlQueryArea(BasicDataSource bds,String[] title,String[] attributes)
	{
		showArea=this;
		setDataSource(bds);
		setTitles(title);
		setAttributes(attributes);
	}
	
	public void setDataSource(BasicDataSource bds) {
		// TODO Auto-generated method stub
		this.bds=bds;
	}
	public void setSqlTxt(String sql)
	{
		this.SQLTxt=sql;
	}
	public void setTitles(String[] title)
	{
		this.Title=title;
	}
	
	public void setAttributes(String[] attributes)
	{
		this.Attributes=attributes;
	}
	
	 public void refresh()
	{
	  String output = "";
	  String title = "";
	  for(String t:Title)
	  {
		  title+=(t+"\t");
	  }
	  title += "\n";
	  output += title;
	  try {
		   Connection conn = connect();
		   PreparedStatement p = conn.prepareStatement(SQLTxt);
		   ResultSet r = null;
		   r = p.executeQuery();
		   while(r.next())
		   {
			   String one_record = getOneRecordfromRS(r);
			   output+=one_record;
		   }
		   r.close();
		   p.close();
		   conn.close();
	  } catch (SQLException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	  //this.frame.remove(jsp);
	  showArea.setText(output);
	  //jsp = new JScrollPane(showArea);
	  //frame.add(BorderLayout.CENTER, jsp);
	  repaint();
	 }
	public String getOneRecordfromRS(ResultSet resultSet) throws SQLException {
		String one_record="";
		for(String attribute:Attributes)
		   {
			one_record+= (resultSet.getString(attribute)+"\t");
		   }
		return one_record+"\n";
	}

	private Connection connect() throws SQLException {
		// TODO Auto-generated method stub
		return bds.getConnection();
	}
}
