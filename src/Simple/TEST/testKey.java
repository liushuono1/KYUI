package Simple.TEST;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import jni.KeyMouseListener;



public class testKey extends JFrame implements Runnable{

	//��jniʵ�ֵ�����ȫ�ּ�����
		private KeyMouseListener kml;
		//�洢��ȡ���ļ���ֵ��codeֵ
		private int keyCode;
		//�洢��ȡ�������λ�õ�X����
		private int mouseX;
		//�洢��ȡ�������λ�õ�Y����
		private int mouseY;
		//����
		private Graphics g;
		
		public static void main(String[] args) throws AWTException {
			testKey main= new testKey();
			main.initFrame();
			Thread th = new Thread(main);
			th.start();
		}

		private void initFrame() throws AWTException {
			this.setSize(300,200);
			this.setVisible(true);
			
			g = this.getGraphics();
		}

		@Override
		@SuppressWarnings("null")
		public void run() {
			boolean bool = true; 
			kml = new KeyMouseListener();
			while(bool){
				try {
					
					Thread.sleep(200);
					repaint();
					mouseX = kml.getMouseX();
					mouseY = kml.getMouseY();
					keyCode = kml.getKeyCode();
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		@Override
		public void paint(Graphics g){
			super.paint(g);
			StringBuffer sb = new StringBuffer();
			sb.append("X=");
			sb.append(mouseX);
			sb.append("  Y=");
			sb.append(mouseY);
		
			sb.append("  code=");
			sb.append(KeyEvent.getKeyText(keyCode));
			g.drawString(sb.toString(), 20, 90);
		}

}
