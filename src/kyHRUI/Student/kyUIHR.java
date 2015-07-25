package kyHRUI.Student;

import java.awt.BorderLayout;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JPanel;

import bb.gui.base.ClientUI;


public class kyUIHR extends ClientUI {
	
	public kyUIHR() throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		System.out.println("this line out.....1234");
		JPanel eastPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		rollCall roll = new rollCall();
		centerPanel = roll.wholePanel;
		
		Attendance attend = new Attendance();
		eastPanel = attend.whole;
		
		this.setLayout(new BorderLayout());
		this.add(BorderLayout.CENTER, centerPanel);
		this.add(BorderLayout.WEST, eastPanel);
		this.setVisible(true);
	}
	
	@Override
	public String getTitle()
	{
		return "È«Ô±³öÇÚ";
	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		//kyUIHR test = new kyUIHR();
	}

}
