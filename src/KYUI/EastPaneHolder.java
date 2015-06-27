/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package KYUI;

import bb.gui.ClientUtil;

import bb.gui.message.notice.NoticeCenterUI;

import free.FreeOutlookHeader;
import free.FreeOutlookSplitListener;
import free.FreeSplitButton;

import free.MainUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Referenced classes of package free:
//            FreeOutlookSplitListener, FreeSplitButton, FreeUtil, MainUI, 
//            FreeOutlookHeader

public class EastPaneHolder extends JPanel {

	public EastPaneHolder(String xml_name) {
		header = new FreeOutlookHeader() {

			protected void updateDock(boolean dock) {
				if (dock)
					EastPaneHolder.this.dock();
				else
					undock();
			}

		};
		headerPane = new JPanel(new BorderLayout());
		splitListener = new FreeOutlookSplitListener(header);
		noticeCenterUI = new NoticeCenterUI();
		//mainTree = null;
		split = new FreeSplitButton()
		{


		};
		centerPane = new JPanel(new BorderLayout());
		splitMouseOver = false;
		splitLastMoveTime = 0L;
		popupPane = new JPanel();
		mouseDelay = 50;
		needPopup = ClientUtil.getPopupMainTreeOnMouseOver();
		//mainTree = new MainUITreePane(xml_name);
		init();
	}

	private void init() {
		split.setCursor(Cursor.getPredefinedCursor(10));
		split.addMouseListener(splitListener);
		split.addMouseMotionListener(splitListener);
		split.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1)
					header.changeShrink();
			}

		});
		split.getHandlerButton().addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				header.changeShrink();
			}

		});
		//noticeCenterUI.getTreePane().setBackground(FreeUtil.ALL_UI_BACKGROUD_COLOR);
		//mainTree.getTabbedPane().setTabPlacement(3);
		//mainTree.setBackground(FreeUtil.ALL_UI_BACKGROUD_COLOR);
		//centerPane.add(noticeCenterUI.getTreePane(), "South");
		//centerPane.add(mainTree, "Center");
		setLayout(new BorderLayout());
		add(centerPane, "Center");
		add(split, "West");
		headerPane.setOpaque(true);
		headerPane.add(header, "Center");
		centerPane.add(headerPane, "North");
		split.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (header.isShrinked())
					header.changeShrink();
			}
		});
		MouseAdapter splitMouseHandler = new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
				splitMouseOver = true;
			}

			public void mouseExited(MouseEvent e) {
				splitMouseOver = false;
			}

			public void mouseMoved(MouseEvent e) {
				splitLastMoveTime = System.currentTimeMillis();
			}

			public void mouseClicked(MouseEvent e) {
				splitLastMoveTime = System.currentTimeMillis();
			}

			public void mousePressed(MouseEvent e) {
				splitLastMoveTime = System.currentTimeMillis();
			}

			public void mouseReleased(MouseEvent e) {
				splitLastMoveTime = System.currentTimeMillis();
			}


		};
		split.addMouseListener(splitMouseHandler);
		split.addMouseMotionListener(splitMouseHandler);
		ActionListener popupTreeAction = new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				if (needPopup && splitMouseOver && !centerPane.isShowing()) {
					long now = System.currentTimeMillis();
					long time = now - splitLastMoveTime;
					if (time > (long) mouseDelay)
						popupTree();
				}
			}


		};
		(new Timer(mouseDelay, popupTreeAction)).start();
		MouseAdapter popupMouseHandler = new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
				hideTree();
			}

			public void mouseMoved(MouseEvent e) {
				hideTree();
			}

		};
		popupPane.addMouseListener(popupMouseHandler);
		popupPane.addMouseMotionListener(popupMouseHandler);
		popupPane.setVisible(false);
		popupPane.setOpaque(false);
		popupPane.setBorder(BorderFactory.createEmptyBorder(42, 0, 25, 200));
		popupPane.setLayout(new BorderLayout());
		MainUI.getInstance().getLayeredPane().add(popupPane, JLayeredPane.DRAG_LAYER);
	}

	public FreeOutlookHeader getHeader() {
		return header;
	}

	public NoticeCenterUI getNoticeCenterUI() {
		return noticeCenterUI;
	}



	public void changeDock() {
		header.changeShrink();
	}

	public void dock() {
		splitMouseOver = false;
		splitLastMoveTime = System.currentTimeMillis();
		split.setCursor(Cursor.getPredefinedCursor(0));
		split.setSplitClosed(true);
		remove(centerPane);
		revalidate();
		repaint();
	}

	public void REMOVEALL()
	{
		centerPane.removeAll();
	}
	
	public void ADD(JPanel comp,Object position)
	{
		mainPane=comp;
		centerPane.add(comp, position);
	}
	public void undock() {
		splitMouseOver = false;
		splitLastMoveTime = System.currentTimeMillis();
		add(centerPane, "Center");
		split.setCursor(Cursor.getPredefinedCursor(10));
		split.setSplitClosed(false);
		//centerPane.add(mainTree, "Center");
		revalidate();
		repaint();
	}

	private void popupTree() {
		if (needPopup) {
			int x =  MainUI.getInstance().getContentPane().getWidth()-centerPane.getWidth();
			int y = 0;
			int width = 500;
			int height = MainUI.getInstance().getContentPane().getHeight();
			popupPane.setBounds(x, y, width, height);
			mainPane.setOpaque(false);
			popupPane.add(mainPane, "Center");
			popupPane.setVisible(true);
		}
	}

	private void hideTree() {
		mainPane.setOpaque(true);
		//mainTree.setOpaque(true);
		popupPane.setVisible(false);
		centerPane.add(mainPane, "Center");
	}

	private FreeOutlookHeader header;
	private JPanel headerPane;
	private FreeOutlookSplitListener splitListener;
	private NoticeCenterUI noticeCenterUI;
	//private MainUITreePane mainTree;
	private FreeSplitButton split;
	private JPanel centerPane;
	private JPanel mainPane;
	private boolean splitMouseOver;
	private long splitLastMoveTime;
	private JPanel popupPane;
	private int mouseDelay;
	private boolean needPopup;

}


