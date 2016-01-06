package Simple.SQL;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;

import java.util.Queue;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import Simple.timepicker;

public class SQLTableModel {
	Connection conn;
	ResultSet rs;
	String SQLQuery, ConnStr, user, pass;
	String[] ColName, ColType;
	Object[][] Data;
	JPanel buttonPanel;
	DefaultTableModel dm = null;
	int insertRowNum = 0;
	Queue<int[]> updateQueue = new LinkedList<int[]>();
	boolean updateAble, instantlyUpdate = true, RowInserting = false;

	public boolean isInstantlyUpdate() {
		return instantlyUpdate;
	}

	private void setInstantlyUpdate(boolean instantlyUpdate) {
		this.instantlyUpdate = instantlyUpdate;
	}

	SQLTableModel() {
		SQLQuery = "Select * from emp_NFCID";
		ConnStr = "jdbc:mysql://" + "192.168.1.100" + ":3307/bb2_test";
		user = "root";
		this.pass = "root";
	}

	SQLTableModel(String query) {
		SQLQuery = query;
		ConnStr = "jdbc:mysql://" + "192.168.1.100" + ":3307/bb2_test";
		user = "root";
		this.pass = "root";
	}

	SQLTableModel(String query, boolean InstantlyUpdate) {
		SQLQuery = query;
		ConnStr = "jdbc:mysql://" + "192.168.1.100" + ":3307/bb2_test";
		user = "root";
		this.pass = "root";
		this.setInstantlyUpdate(InstantlyUpdate);
	}

	SQLTableModel(String query, String connectStr, String User, String Pass) {
		SQLQuery = query;
		ConnStr = connectStr;
		user = User;
		this.pass = Pass;
	}

	private Object[][] getData() {
		return Data;
	}

	private void setData(Object[][] Data) {
		this.Data = Data;
	}

	SQLTableModel(ResultSet rs) {
		this.rs = rs;
		try {
			getherInfoFromResultset();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JPanel btnPanel() {
		if (buttonPanel != null)
			return buttonPanel;

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton actionBtn = new JButton("提交");
		actionBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				UpdateTableDB();
			}

		});
		buttonPanel.add(actionBtn);
		return buttonPanel;
	}

	public void setTableMOdel(JTable table) {
		table.setModel(getSwingTableModel());
		for (int i = 0; i < this.ColType.length; i++) {
			System.out.println(ColType[i]);
			if (ColType[i].contains("Timestamp")) {
				System.err.println(ColType[i]);
				TimestampCellRender x = new TimestampCellRender(this);
				table.getColumnModel().getColumn(i).setCellRenderer(x);

				table.getColumnModel().getColumn(i).setCellEditor(x);

			}
		}

	}

	private DefaultTableModel getSwingTableModel() {
		StackTraceElement[] stack = (new Throwable()).getStackTrace();

		System.out.println(stack[1].getMethodName());
		if (conn == null) {
			try {
				initConection();
				getherInfoFromResultset();
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// final DefaultTableModel
		dm = new DefaultTableModel(Data, ColName);
		dm.addRow(new Object[] {});
		this.insertRowNum = dm.getRowCount() - 1;
		dm.addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				// TODO Auto-generated method stub
				// System.out.println(e.getColumn());
				if (RowInserting) {
					RowInserting = false;
					return;
				}

				if (e.getFirstRow() == insertRowNum) // 判断为新插入行
				{
					RowInserting = true;
					dm.addRow(new Object[] {});
					System.out.println(Data.length + "  " + Data[0].length);
					Data = (Object[][]) arrayAddLength(Data, 1);
					System.out.println("After" + Data.length + "  " + Data[0].length);
					insertRowNum = dm.getRowCount() - 1;
					System.out.println("new row inserted");

				} // else // 不是新插入行则执行更新

				updateTimestampValue(dm, e.getFirstRow(), e.getColumn());

				/*
				 * { System.out.println(e.getFirstRow() + "  " + e.getColumn());
				 * System.out.println(dm.getValueAt(e.getFirstRow(),
				 * e.getColumn()));
				 * System.out.println(Data[e.getFirstRow()].length);
				 * System.out.println(Data[e.getFirstRow()][e.getColumn()]); if
				 * (dm.getValueAt(e.getFirstRow(),
				 * e.getColumn()).equals(Data[e.getFirstRow()][e.getColumn()]))
				 * { System.out.println("true"); } else {
				 * System.out.println("false");
				 * Data[e.getFirstRow()][e.getColumn()] =
				 * dm.getValueAt(e.getFirstRow(), e.getColumn()); int
				 * updatePos[] = { e.getFirstRow(), e.getColumn() };
				 * updateQueue.add(updatePos); //判断新加行的Timestamp是否为空，加入合法的默认值
				 * for(int i=0;i<ColType.length;i++) {
				 * if(ColType[i].contains("Timestamp")) { if(false)
				 * if(dm.getValueAt(e.getFirstRow(), i)==null) {
				 * 
				 * SimpleDateFormat sdfgmt = new SimpleDateFormat(
				 * "yyyy-MM-dd HH:mm:ss.0");
				 * 
				 * Timestamp ts=null; try { ts = new
				 * Timestamp(sdfgmt.parse(sdfgmt.format(new
				 * Date(System.currentTimeMillis()))).getTime()); } catch
				 * (ParseException e1) { // TODO Auto-generated catch block
				 * e1.printStackTrace(); }
				 * 
				 * dm.setValueAt(ts,e.getFirstRow(), i);
				 * Data[e.getFirstRow()][i]=ts; int TimePos[] = {
				 * e.getFirstRow(), i }; updateQueue.add(TimePos); }; } } if
				 * (instantlyUpdate) { UpdateTableDB(); } } }
				 */

			}

		});

		return dm;
	}

	public void updateTimestampValue(TableModel dm, int row, int col) {
		StackTraceElement[] stack = (new Throwable()).getStackTrace();

		System.err.println(stack[1].getMethodName());
		{
			System.out.println("DM  :" + dm);
			System.out.println(row + "  " + col);
			System.out.println(dm.getValueAt(row, col));
			System.out.println(Data[row].length);
			System.out.println(Data[row][col]);
			if(dm.getValueAt(row, col).equals("NOVALUE")) //在没有改变的情况下不改变值
			{
				dm.setValueAt(Data[row][col],row, col);
				return;
			}
			for (int i = 0; i < dm.getRowCount() - 1; i++) {
				for (int j = 0; j < dm.getColumnCount(); j++) {
					System.out.print(dm.getValueAt(i, j) + "\t");
				}
				System.out.println();
			}
			if (dm.getValueAt(row, col).equals(Data[row][col])) {
				System.out.println("true");
			} else {
				System.out.println("false");
				Data[row][col] = dm.getValueAt(row, col);
				int updatePos[] = { row, col };
				updateQueue.add(updatePos);
				// 判断新加行的Timestamp是否为空，加入合法的默认值
				for (int i = 0; i < ColType.length; i++) {
					if (ColType[i].contains("Timestamp")) {
						if (dm.getValueAt(row, i) == null) {

							SimpleDateFormat sdfgmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");

							Timestamp ts = null;
							try {
								ts = new Timestamp(
										sdfgmt.parse(sdfgmt.format(new Date(System.currentTimeMillis()))).getTime());
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							dm.setValueAt(ts, row, i);
							Data[row][i] = ts;
							int TimePos[] = { row, i };
							updateQueue.add(TimePos);
						}
						;
					}
				}
				if (instantlyUpdate) {
					UpdateTableDB();
				}
			}
		}
		for (int i = 0; i < dm.getRowCount() - 1; i++) {
			for (int j = 0; j < dm.getColumnCount(); j++) {
				System.out.print(dm.getValueAt(i, j) + "\t");
			}
			System.out.println();
		}
	}

	private void initConection() throws ClassNotFoundException, SQLException {
		Connection conn;
		String driver = "com.mysql.jdbc.Driver";
		String url = ConnStr;
		String user = this.user;
		String password = this.pass;
		Class.forName(driver);
		conn = DriverManager.getConnection(url, user, password);
		if (!conn.isClosed())
			System.out.println("Succeeded connecting to the Database!");

		PreparedStatement p = null;

		p = conn.prepareStatement(this.SQLQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE,
				ResultSet.HOLD_CURSORS_OVER_COMMIT);
		rs = p.executeQuery();

	}

	public void getherInfoFromResultset() throws SQLException {
		int col = rs.getMetaData().getColumnCount();
		ColName = new String[col];
		ColType = new String[col];
		for (int i = 0; i < col; i++) {
			ColName[i] = rs.getMetaData().getColumnLabel(i + 1);
			ColType[i] = rs.getMetaData().getColumnClassName(i + 1);
		}
		rs.last();
		int row = rs.getRow();
		rs.beforeFirst();

		Data = new Object[row][col];
		System.out.println(Arrays.asList(ColType));
		while (rs.next()) {
			for (int i = 0; i < col; i++) {
				try {
					if (ColType[i].contains("String")) {
						Data[rs.getRow() - 1][i] = rs.getString(i + 1);
					} else if (ColType[i].contains("Integer")) {
						Data[rs.getRow() - 1][i] = rs.getInt(i + 1);
					} else if (ColType[i].contains("Timestamp")) {
						Data[rs.getRow() - 1][i] = rs.getTimestamp(i + 1);
					} else if (ColType[i].contains("Date")) {
						Data[rs.getRow() - 1][i] = rs.getDate(i + 1);
					} else if (ColType[i].contains("Time")) {
						Data[rs.getRow() - 1][i] = rs.getTime(i + 1);
					} else if (ColType[i].contains("Double")) {
						Data[rs.getRow() - 1][i] = rs.getDouble(i + 1);
					}
				} catch (Exception e) {
					Data[rs.getRow() - 1][i] = new Timestamp(new java.util.Date().getTime());
					// e.printStackTrace();
				}

			}
		}

		// System.out.println(Arrays.asList(Data[1]));
		System.out.println(Arrays.asList(ColType));

	}

	public void setTableActionListeners(final JTable table) {
		table.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN
						|| e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT
						|| e.getKeyCode() == KeyEvent.VK_ENTER) {
					// System.out.println(table.getSelectedColumn());
					// System.out.println(table.getSelectedRow());
					// System.out.println(table.getModel().getValueAt(table.getSelectedRow(),
					// table.getSelectedColumn()));
					// UpdateTableDB(table.getSelectedColumn(),table.getSelectedRow(),table.getModel().getValueAt(table.getSelectedRow(),
					// table.getSelectedColumn()));
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});

	}

	private boolean UpdateTableDB() {
		while (updateQueue.size() != 0) {
			int[] a = updateQueue.poll();
			int row = a[0], col = a[1];
			System.out.println(row + "   " + col + "   " + Data[row][col]);
			try {
				rs.last();
				if (rs.getRow() < (row + 1)) {
					rs.moveToInsertRow();
					rs.insertRow();
				}
				rs.absolute(row + 1);
				if (ColType[col].contains("String")) {
					rs.updateString(col + 1, (String) Data[row][col]);
					// System.out.println("String");
					rs.updateRow();
				} else if (ColType[col].contains("Integer")) {
					rs.updateInt(col + 1, (Integer) Data[row][col]);
					rs.updateRow();
				} else if (ColType[col].contains("Timestamp")) {
					SimpleDateFormat sdfgmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
					if (Data[row][col] instanceof String) {
						Data[row][col] = new Timestamp(sdfgmt.parse((String) Data[row][col]).getTime());
					}
					rs.updateTimestamp(col + 1, (Timestamp) Data[row][col]);
					rs.updateRow();
				} else if (ColType[col].contains("Date")) {
					rs.updateDate(col + 1, (Date) Data[row][col]);
					rs.updateRow();
				} else if (ColType[col].contains("Time")) {
					rs.updateTime(col + 1, (Time) Data[row][col]);
					rs.updateRow();
				} else if (ColType[col].contains("Double")) {
					rs.updateDouble(col + 1, (Double) Data[row][col]);
					rs.updateRow();
				}

				System.out.println(">>>" + rs.getString(col + 1));
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("type error");
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		/*
		 * Connection conn; ResultSet r = null; try { conn = connect();
		 * PreparedStatement p = null;
		 * 
		 * p = conn.prepareStatement(
		 * "select * from emp_id where adate like '2015%'"
		 * ,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE,
		 * ResultSet.HOLD_CURSORS_OVER_COMMIT); r = p.executeQuery();
		 * 
		 * }catch(Exception e) { e.printStackTrace(); }
		 */
		JFrame f = new JFrame();
		f.setSize(800, 600);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(getShowPanel((new SQLTableModel("select * from duty;"))));
		f.setVisible(true);
		;

	}

	public static JPanel getShowPanel(SQLTableModel sqlTableModel) {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("显示录入信息"));
		panel.setLayout(new BorderLayout());
		JTable table = new JTable();
		sqlTableModel.setTableMOdel(table);
		// sqlTableModel.setTableActionListeners(table);
		table.updateUI();
		table.setRowHeight(40);
		// table.setSize(panel.getWidth(), panel.getHeight());
		// table.getModel().addTableModelListener(l);
		JScrollPane ScrollBtnPanel = new JScrollPane(table);
		ScrollBtnPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		ScrollBtnPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BorderLayout());

		// btnPanel.add(BorderLayout.EAST, cancelBtn);
		panel.add(ScrollBtnPanel);
		panel.add(BorderLayout.SOUTH, btnPanel);

		return panel;
	}

	public static Connection connect() throws ClassNotFoundException, SQLException {
		Connection conn;
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://" + "192.168.1.100" + ":3307/bb2_test";
		String user = "root";
		String password = "root";
		Class.forName(driver);
		conn = DriverManager.getConnection(url, user, password);
		if (!conn.isClosed())
			System.out.println("Succeeded connecting to the Database!");
		return conn;
	}

	public static Object arrayAddLength(Object[][] oldArray, int addLength) {

		System.out.println(oldArray.length + "  " + oldArray[0].length);
		int col = oldArray.length, row = oldArray[0].length;
		System.out.println("row" + row + "col" + col);
		Object[][] newArray = new Object[col + 1][row];
		System.out.println(newArray.length + "  " + newArray[0].length);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				newArray[j][i] = oldArray[j][i];
			}
		}

		return newArray;
	}

}

// 渲染 器 编辑器
class MyRender extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

	// private static final long serialVersionUID = 1L;
	private Hashtable<String, JButton> buttons = new Hashtable<String, JButton>();
	private SQLTableModel sqlTableMOdel;

	Object retvalue;

	public MyRender(SQLTableModel sqlTableMOdel) {
		this.sqlTableMOdel = sqlTableMOdel;
	}

	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return retvalue;
	}

	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value, boolean isSelected,
			boolean hasFocus, final int row, final int column) {
		// TODO Auto-generated method stub

		final JButton button = new JButton(value == null ? "" : value.toString());
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("VBVBVBVB" + column + sqlTableMOdel.dm.getValueAt(row, column));
				System.out.println("VVVVVB" + value);
				for (int i = 0; i < sqlTableMOdel.dm.getRowCount(); i++) {
					System.out.println("]]]:" + i + "," + column + sqlTableMOdel.dm.getValueAt(row, column));
				}
				// sqlTableMOdel.dm.setValueAt("2015-01-01 12:01:10.0", row,
				// column);
				// sqlTableMOdel.updateTimestampValue(table.getModel(),row,column);
				retvalue = "2015-01-01 12:01:10.0";
				System.out.println("VVVVV");
				for (int i = 0; i < table.getModel().getRowCount(); i++) {
					// System.out.println("]]]:"+i+","+column+table.getModel().getValueAt(
					// row, column));
				}
				// button.setText(sqlTableMOdel.dm.getValueAt( row,
				// column).toString());
				/*
				 * SimpleDateFormat sdfgmt = new SimpleDateFormat(
				 * "yyyy-MM-dd HH:mm:ss.0");
				 * 
				 * Timestamp ts=null; try { ts = new
				 * Timestamp(sdfgmt.parse(sdfgmt.format(new
				 * Date(System.currentTimeMillis()))).getTime()); } catch
				 * (ParseException e1) { // TODO Auto-generated catch block
				 * e1.printStackTrace(); } TableModel dm = table.getModel();
				 * dm.setValueAt(ts,row, column); Data[e.getFirstRow()][i]=ts;
				 * int TimePos[] = { e.getFirstRow(), i };
				 * updateQueue.add(TimePos);
				 */

			}
		});

		buttons.put(row + "," + column, button);

		return button;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		// TODO Auto-generated method stub
		return buttons.get(row + "," + column);
	}

}

class TimestampCellRender extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

	// private static final long serialVersionUID = 1L;
	private Hashtable<String, timepicker> buttons = new Hashtable<String, timepicker>();
	private SQLTableModel sqlTableMOdel;

	Object retvalue="NOVALUE";

	public TimestampCellRender(SQLTableModel sqlTableMOdel) {
		this.sqlTableMOdel = sqlTableMOdel;
	}

	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		Object ret = retvalue;
		retvalue="NOVALUE";
		return ret;
	}

	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value, boolean isSelected,
			boolean hasFocus, final int row, final int column) {
		// TODO Auto-generated method stub

		System.out.println(value);
		java.util.Date date = value == null ? (new java.util.Date(0))
				: (new java.util.Date(((Timestamp) value).getTime()));

		final timepicker dateTimePicker = new timepicker();
		dateTimePicker.setFormats(DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM));
		dateTimePicker.setTimeFormat(DateFormat.getTimeInstance(DateFormat.MEDIUM));
		dateTimePicker.setDate(date);
		dateTimePicker.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("time changes" + dateTimePicker.getDate());
				retvalue = new Timestamp(dateTimePicker.getDate().getTime());

			}
		});

		buttons.put(row + "," + column, dateTimePicker);

		return dateTimePicker;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		// TODO Auto-generated method stub
		retvalue = new Timestamp(buttons.get(row + "," + column).getDate().getTime());
		return buttons.get(row + "," + column);
	}

}
