package Simple;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FeedBackPane extends JPanel {
	
	JButton ExpBtn;
	
	JPanel FeedbackPane;
	
	protected FeedBackPane()
	{
		this.setLayout(new BorderLayout());
		ExpBtn = new JButton("展开");
		ExpBtn.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent actionevent) {
						// TODO Auto-generated method stub
						JOptionPane.showConfirmDialog(null, "本班任务列表");
					}
				});
		
		this.add(BorderLayout.NORTH,ExpBtn);
		
	}
	
	public void setFeedbackPane(JPanel pane)
	{
		FeedbackPane=pane;
		this.add(BorderLayout.CENTER,FeedbackPane);
	}

}
