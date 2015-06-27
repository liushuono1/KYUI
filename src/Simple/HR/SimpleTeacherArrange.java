package Simple.HR;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SimpleTeacherArrange extends JPanel{
	
	SimpleTeacherArrange()
	{
		
	}
	
	
	public static void main(String args[])
	{
		JFrame f = new JFrame("Base Frame");
		f.setSize(400, 600);
		f.getContentPane().add(new SimpleTeacherArrange());
		f.setVisible(true);
	}

}
