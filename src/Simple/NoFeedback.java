package Simple;

import javax.swing.JFrame;

public class NoFeedback implements FeedBackObj{

	FeedBackPane fdb;
	String fdbMsg;
	public NoFeedback()
	{
		fdb=new FeedBackPane();
	}
	
	@Override
	public FeedBackPane getFeedBackPane() {
		// TODO Auto-generated method stub
		return fdb;
	}

	@Override
	public String getFeedBackMsg() {
		// TODO Auto-generated method stub
		return fdbMsg;
	}

	@Override
	public void closingHostWindow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dutyFinishing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHostwindow(JFrame host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "No Feedback";
	}

}
