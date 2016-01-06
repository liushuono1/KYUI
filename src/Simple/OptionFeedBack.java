package Simple;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class OptionFeedBack implements FeedBackObj{
	FeedBackPane fdb;
	Object fdbMsg;
	JFrame Hostwindow;
	
	public OptionFeedBack()
	{
		fdb=new FeedBackPane();
		JPanel OpPanel= new JPanel();
		
		OpPanel.setLayout(new GridLayout(1,3));
		JButton b1,b2,b3;
		b1=new JButton("OP1");
		b2=new JButton("OP2");
		b3=new JButton("OP3");
		OpPanel.add(b1);
		OpPanel.add(b2);
		OpPanel.add(b3);
		
		ActionListener testAction=new ActionListener(){
            
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				// TODO Auto-generated method stub
				Object Source = actionevent.getSource();
				if(Source instanceof JButton)
				{
					dutyFinishing(((JButton)Source).getText());
				}else if(Source instanceof String)
				{
					dutyFinishing((String)Source);
				}
				
				closingHostWindow();
			}
			
		};
		b1.addActionListener(testAction);
		b1.addActionListener(testAction);
		b1.addActionListener(testAction);
		fdb.setFeedbackPane(OpPanel);
		
	}
	
	public void closingHostWindow() {
		// TODO Auto-generated method stub
		if(Hostwindow!=null)
			Hostwindow.setVisible(false);
	}
	
	
	public void dutyFinishing(Object feedbackMsg) {
		// TODO Auto-generated method stub
		KY.console.appendline(feedbackMsg.toString());
		this.fdbMsg=feedbackMsg;
		//System.err.println("INININININ");
	}

	public void setHostwindow(JFrame hostwindow) {
		Hostwindow = hostwindow;
	}

	public OptionFeedBack(final List Options)
	{
		fdb=new FeedBackPane();
		JPanel OpPanel= new JPanel();
		OpPanel.setLayout(new GridLayout(1,Options.size()));
		JButton[] btnOpt= new JButton[Options.size()];
		
		for( int i=0;i<Options.size();i++)
		{
			btnOpt[i]=new JButton(Options.get(i).toString());
			final String m = Options.get(i).toString();
			btnOpt[i].addActionListener(new ActionListener(){
            
				@Override
				public void actionPerformed(ActionEvent actionevent) {
					// TODO Auto-generated method stub
					//fdbMsg=m;
					dutyFinishing(m);
					closingHostWindow(); 
				}
				
			});
			OpPanel.add(btnOpt[i]);
		}
	
		fdb.setFeedbackPane(OpPanel);
		
	}
	
	@Override
	public FeedBackPane getFeedBackPane() {
		// TODO Auto-generated method stub
		return fdb;
	}

	
	@Override
	public Object getFeedBackMsg() {
		// TODO Auto-generated method stub
		return fdbMsg;
	}
	
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "Option Feedback";
	}

}
