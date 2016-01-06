package Simple;

import java.io.Serializable;

import javax.swing.JFrame;

public interface FeedBackObj extends Serializable{
	
	public FeedBackPane getFeedBackPane();
	public Object getFeedBackMsg();
	public void closingHostWindow();
	public void dutyFinishing(Object feedbackMsg);
	public void setHostwindow(JFrame host);
	public String getType();

}
