package KYUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ScrollInfoPane extends JButton implements Runnable{

	  /**
     * 参考纵坐标，控制动画效果
     */
    int y,y2,textspeed;   
    int strWidth ,noticeWidth,fontsize;
    public int lines=1;
    public static MsgQueue  msgQueue = new MsgQueue();
    public static String textContent ="";
    public static String NoticeContent = "";

  
    
	public ScrollInfoPane(int Lines,String initmsg,String InitNotice)
	{
		this.lines=Lines;
		pushMsg(initmsg);
		NoticeContent=InitNotice;
		System.out.println("here3"+System.currentTimeMillis());
		this.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int s= JOptionPane.showConfirmDialog(null, NoticeContent);
				if(s==0)
				{
					NoticeContent="";  //还需加入信息到操作日志（未完成）
				}
				
				
			}});
		
	}
	
	public ScrollInfoPane(int Lines)

	{
		this.lines=Lines;
		
	}
	
	public void pushMsg(String m)
	{
		msgQueue.addMsg(m);
	}
	
	public void pushNotice(String m)
	{
		NoticeContent=m;
	}
	
	public void pushOnlyMsg(String m)
	{
		msgQueue.clearMsg();
		msgQueue.addMsg(m);
	}
	
	public void setLines(int lines)
	{
		this.lines=lines;
		y=getSize().width;
		y2=getSize().width;
	}
	
	
	private void resize()

	{

		if(getSize().getHeight()>100)
		{
			if(lines==2)
				fontsize=44;
			else
				fontsize=90;
		}
		else
		{
			if(lines==2)
				fontsize=(int)(getSize().getHeight()*0.9/2);
			else
				fontsize=(int)(getSize().getHeight()*0.9);
			
		}
		textspeed=(int)(getSize().getWidth()*0.01);
	}
	public void startshow()
	{
	
		
		
		new Thread(this).start();
		System.out.println(getSize().height +"    "+getSize().width);
	}
	
	 public void run()
	    {
		
	    	while (true)
	    	{
	    		resize();
	    		
		    		y -= textspeed;
		            if (y < -strWidth)
		            {
		            	textContent=msgQueue.nextMsg();
		            	
		            	y = this.getSize().width;
		            }

		            
		            if(noticeWidth<=getSize().width)
		            {
		            	y2=(getSize().width-noticeWidth)/2;
		            }
		            else
		            {
		            	y2 -= textspeed;
			            if (y2 < -noticeWidth)
			            {
			            	y2 = this.getSize().width;
			            }
		            }
		            
		            try
		            {
		            	Thread.sleep(100);
		            }
		            catch (InterruptedException e)
		            {
		            }
		            
		            repaint();
	    		
	    	}
	    }

	    public void paint(Graphics comp)
	    {
    		Graphics2D com2D = (Graphics2D) comp;
	        Font type = new Font("monospaced", Font.BOLD, fontsize);
	        com2D.setFont(type);
	        strWidth = comp.getFontMetrics().stringWidth(textContent);
	        com2D.setColor(Color.black);
	        com2D.fillRect(0, 0, getSize().width, getSize().height);
	    	
	    	if(lines==2)
	    	{

		        com2D.setColor(Color.GREEN);
		        com2D.drawString(textContent, y, fontsize+2);
		        
		        if(!NoticeContent.equals(""))
		        {
		        	 noticeWidth = comp.getFontMetrics().stringWidth(NoticeContent);
		        	 if((System.currentTimeMillis()/300)%2==0)
		        	 {
		        		 com2D.setColor(Color.RED);
		        		 
		        		 com2D.drawString(NoticeContent, y2 , 2*fontsize+4);
		        	 }
		        }
	    	}else if(lines==1)
	    	{
	    		if(!NoticeContent.equals(""))
		        {
		    		if((System.currentTimeMillis()/3000)%4!=0)
		    		{
				        com2D.setColor(Color.GREEN);
				        com2D.drawString(textContent, y, fontsize+2);
		    		}else
		    		{
				        
				        	 noticeWidth = comp.getFontMetrics().stringWidth(NoticeContent);
				        	 if((System.currentTimeMillis()/300)%2==0)
				        	 {
				        		 com2D.setColor(Color.RED);
				        		 
				        		 com2D.drawString(NoticeContent, y2 , fontsize+2);
				        	 }
				        
		    		}
		        }else
		        {
			        com2D.setColor(Color.GREEN);
			        com2D.drawString(textContent, y, fontsize+2);
		        	
		        }
	    	}
	    }
	    
	    
	public static void main(String args[])
	{
		JFrame f =new JFrame();
		f.setSize(900, 100);
		f.setType(Window.Type.UTILITY);
		f.setUndecorated(true);
		ScrollInfoPane si=new ScrollInfoPane(2);
		f.getContentPane().add(si);
		f.setVisible(true);
		si.startshow();
		
		try {
			Thread.sleep(5000);
			si.pushMsg("测试1babababababba");
			si.pushNotice("通知测试");
			Thread.sleep(30000);
			si.pushMsg("测试2babababababba");
			Thread.sleep(10000);
			si.pushOnlyMsg("测试3babababababba");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

class MsgQueue
{
	int maxMsg =5,pointer=0;
	Queue<String> msgs = new LinkedList<String>();
	

	public void addAll(Collection<String> c)
	{
		for(String m:c)
		{
			addMsg(m);
		}
	}
	
	public void addAll(String[] c)
	{
		for(int i=0;i<c.length;i++)
		{
			addMsg(c[i]);
		}
	}
	
	public void addMsg(String m)
	{
		msgs.add(m);
		if(msgs.size()>(maxMsg>0?maxMsg:Integer.MAX_VALUE))
		{
			int count= msgs.size()-maxMsg;
			while(count-->0)
			{
				msgs.poll();
			}
		}
	}
	
	public String nextMsg()
	{	
		if(msgs.size()!=0)
		{

			if(pointer>=msgs.size())
			{
				pointer=0;
			}
			
			return (String)(msgs.toArray()[pointer++]);
		}
		else
		{
			return "";
		}
		
	}
	
	public void clearMsg()
	{
		msgs.clear();
	}

}

