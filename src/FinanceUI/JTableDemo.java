package FinanceUI;

import java.awt.BorderLayout;

import java.awt.Dimension;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import javax.swing.JMenuItem;

import javax.swing.JOptionPane;

import javax.swing.JPanel;

import javax.swing.JPopupMenu;

import javax.swing.JScrollPane;

import javax.swing.JTable;

import javax.swing.SwingUtilities;

import javax.swing.table.DefaultTableModel;



public class JTableDemo extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTable tbl;

	private DefaultTableModel tm;

	private JPopupMenu[] pms = new JPopupMenu[2];



	public JTableDemo() {

		String[][] data = { { "R1C1", "R1C2" }, { "R2C1", "R2C2" } };

		String[] title = { "Column1", "Column2" };

		tm = new DefaultTableModel(data, title) {

			private static final long serialVersionUID = 1L;



			@Override

			public boolean isCellEditable(int row, int column) {

				return false;

			}

		};

		tbl = new JTable(tm);

		tbl.addMouseListener(new MouseAdapter() {

			@Override

			public void mouseClicked(MouseEvent e) {

				int sr;

//				if ((sr = tbl.getSelectedRow()) == -1) {

//					return;

//				}

				if (e.getButton() == MouseEvent.BUTTON3) {

					pms[tbl.rowAtPoint(e.getPoint())].show(tbl, e.getX(), e.getY());

				}

			}

		});



		JScrollPane jsp = new JScrollPane();

		jsp.setViewportView(tbl);



		JPanel pan = new JPanel();

		pan.setOpaque(true);

		this.setContentPane(pan);

		pan.setLayout(new BorderLayout());

		pan.add(jsp, BorderLayout.CENTER);



		JPopupMenu pm = new JPopupMenu();

		pm.add(new JMenuItem("Row1"));

		pms[0] = pm;

		pm = new JPopupMenu();

		pm.add(new JMenuItem("Row2"));

		pms[1] = pm;

	}



	private static void createAndShowGui() {

		JTableDemo td = new JTableDemo();

		td.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		td.setMinimumSize(new Dimension(500, 400));

		td.setLocationRelativeTo(null);

		td.setVisible(true);

	}



	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override

			public void run() {

				createAndShowGui();

			}

		});

	}

}