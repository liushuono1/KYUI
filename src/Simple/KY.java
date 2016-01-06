package Simple;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class KY {

  public static final PrintStream out;
  public static final PrintStream err;
  public static final KYOutputPanel console = KYOutputPanel.getInstance();

  static{
	 // ByteArrayOutputStream baoStream = new ByteArrayOutputStream(1024);
	  out = System.out;//new PrintStream(baoStream);
	  err = System.err;
  }
	
  
  public static void append(String str)
  {
	  KYOutputPanel.getInstance().append(str);
  }
  
  

  public static void main(String[] args) {
	  	 KY.console.append("123232");
	  	 KY.out.println("asddsss");
	  	 KY.err.println("(&@%#");
		}


}


class KYOutputPanel extends JFrame
{
	  static KYOutputPanel instance =null;
	 
	  JTextArea text;
	  
	  KYOutputPanel()
	  {
		  super("KY Console");
		  this.setSize(600, 300);
		  text = new JTextArea();
		 
		  JScrollPane jsp= new JScrollPane(text);
		  this.add(jsp);
		  this.setVisible(true);
	  }
	  
	  public void append(String str)
	  {
		  if(!instance.isVisible())
			  instance.setVisible(true);
		  text.append(str);
	  }
	  
	  public void appendline(String str)
	  {
		  if(!instance.isVisible())
			  instance.setVisible(true);
		  text.append(str+"\n");
	  }
	  
	  public static KYOutputPanel getInstance()
	  {
		  if(instance ==null)
		  {
			  instance= new KYOutputPanel();			  
		  }
		  
		  return instance;
	  }
			  
}
