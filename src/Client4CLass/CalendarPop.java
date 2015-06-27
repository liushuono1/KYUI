package Client4CLass;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicArrowButton;

import KYUI.AskForLeaveUI;
import calendar.ActionButtonListener;
import calendar.DayClickListener;
import calendar.DayCompCreateListener;
import calendar.DayPanel;


/**
 *@author 梦科 email:wqjsir@foxmail.com
 *@version 创建时间：2010-12-6 下午05:21:44
 * @类描述：
 * 如果外部程序不显示的调用public void updateDate(Calendar calendar)方法，
 * 则初始构建日历面板将采用当前日历对象，且首先显示的面板不会分发日期对象创建事件DayCompCreateListener。
 * 如果要在首次显示日历面板时能分发DayCompCreateListener事件，则要求外部程序在添加监听之后再显示的调用updateDate（Calendar calendar）方法。
 */
public class CalendarPop extends JPopupMenu{
	public JFrame frame;
	//主面板，包括顶部的操作按钮面板、中间的日历面板、底部的信息面板
	public JPanel mainPanel;
	//顶部按钮的动作面板
	public ActionPanel actionPanel;
	// 日历面板
	public JPanel calendarPanel; 
	//底部信息面板
	public JPanel bottomPanel;
	//添加到底部面板的标签对象，用来呈现底部面板的信息
	public JLabel bottomInfo;
	//日历面板的网格布局对象
	public GridLayout gridLayout;
	//简单的日期格式化，用户底部显示日期信息
	public static SimpleDateFormat sipleDF = new SimpleDateFormat("EEE,yyyy-MM-dd");
	//简单的日期格式化，用户顶部显示当前的年月日期
	static  final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
	
	public List<DayClickListener> listeners = new ArrayList<DayClickListener>();
	public List<DayCompCreateListener> createListeners = new ArrayList<DayCompCreateListener>();
	public List<ActionButtonListener> actionListeners = new ArrayList<ActionButtonListener>();
	public String passDate="";
	public JPanel centerPanel;
	//public String[] classNames={"宝一班"};
	public Connection conn;
	public int shouldCount;

	public String className;
	public boolean flag = true;
	public Properties proper;
	public boolean pause = true;
	AskForLeaveUI O = null;
	JTextField settedField=null;
	
	public CalendarPop(String className,AskForLeaveUI O,JTextField settedField) {
		this.O=O;
		this.settedField=settedField;
		init_CalendarPop(className);
	}
	
	public CalendarPop(String className) {
		init_CalendarPop(className);
	}
	
	public void init_CalendarPop(String className)
	{
		proper = new Properties();
		proper.put("status", pause);
		this.className = className;
			frame = new JFrame();
			gridLayout = new GridLayout(0, 7, 3, 3);
			//setPreferredSize(new Dimension(170,210));
			calendarPanel = new JPanel(gridLayout);
			calendarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.white));
			calendarPanel.setOpaque(false);
			
			bottomInfo = new JLabel("",SwingConstants.CENTER);
			bottomInfo.setFont(new Font(Font.SERIF,Font.PLAIN,15));
			bottomPanel = new JPanel(new BorderLayout());
			bottomPanel.setPreferredSize(new Dimension(100,25));
			bottomPanel.setMinimumSize(new Dimension(100,30));
			bottomPanel.setMaximumSize(new Dimension(100,30));
			bottomInfo.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
			bottomPanel.add(bottomInfo,BorderLayout.CENTER);
			bottomPanel.setOpaque(false);
			
			actionPanel = new ActionPanel();
			centerPanel = new JPanel(new GridBagLayout());
			centerPanel.setOpaque(false);
			centerPanel.add(buildWeekDayPanel(), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));//0:
			centerPanel.add(calendarPanel, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));//1:
			centerPanel.add(new JLabel(), new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));//2:
			centerPanel.setPreferredSize(new Dimension(160,160));
			centerPanel.setMinimumSize(new Dimension(160,160));
			centerPanel.setMaximumSize(new Dimension(160,160));
			mainPanel = new JPanel(new GridBagLayout());
			mainPanel.add(actionPanel, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));//0:
			mainPanel.add(centerPanel, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));//1:
			mainPanel.add(bottomPanel, new GridBagConstraints(0, 2, 1, 1,1, 0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));//2:

			//初始化采用当前日期
			
			Calendar initCalendar =Calendar.getInstance();
			buildCalendarPanel(initCalendar.get(Calendar.YEAR), initCalendar.get(Calendar.MONTH));
			
			setLayout(new GridBagLayout());
			add(mainPanel, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0), 0, 0));
	}

	/**
	 * 构建星期标识标签
	 * @return
	 */
	public JPanel buildWeekDayPanel() {
		JPanel weekDayPanel;
		weekDayPanel = new JPanel(new GridLayout(1, 7, 3, 3));
		weekDayPanel.setOpaque(false);
		weekDayPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.white));
		
		DayPanel day = null;
		day = new DayPanel("日");
		day.setForeground(Color.blue);
		weekDayPanel.add(day);
		weekDayPanel.add(new DayPanel("一"));
		weekDayPanel.add(new DayPanel("二"));
		weekDayPanel.add(new DayPanel("三"));
		weekDayPanel.add(new DayPanel("四"));
		weekDayPanel.add(new DayPanel("五"));
		day = new DayPanel("六");
		day.setForeground(Color.blue);
		weekDayPanel.add(day);
		return weekDayPanel;
	}

	/**
	 * 构建日历控件
	 */
	public String buildCalendarPanel(final int year, final int month) {
		System.out.println("year = "+year+"  month = "+month);
		calendarPanel.removeAll();
		// 设置年月日
		Calendar calender = Calendar.getInstance();
		calender.set(Calendar.YEAR, year);
		calender.set(Calendar.MONTH, month);
		calender.set(Calendar.DATE, 1);//设置时间为当前月的第一天
		//将当前月的第一天之前的星期用空白的DayPanel填充
		int weekDay = calender.get(Calendar.DAY_OF_WEEK);
		for (int i = Calendar.SUNDAY; i < weekDay; i++) {
			calendarPanel.add(new DayPanel(""));
		}
		int i = 1;
		do {
			//根据天生成一个DayPanel对象
			final DayPanel day = new DayPanel(calender);
			//如果为当前的日期，则显示一个边框
			if (Calendar.getInstance().get(Calendar.YEAR)==calender.get(Calendar.YEAR)&&
					Calendar.getInstance().get(Calendar.MONTH) == calender.get(Calendar.MONTH)&&
							Calendar.getInstance().get(Calendar.DAY_OF_MONTH)==calender.get(Calendar.DAY_OF_MONTH)) {
				day.setCurrentDay(true);
			}
			day.setActionMoseEvent(true);
			day.getLabel().addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e)
				{
					passDate = "";
					passDate+=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+day.day;
					pause = false;
					O.getDatefromCalender(passDate,settedField);
					fireDayClicked(day,e);
				}
				@Override
				public void mouseEntered(MouseEvent e) {
					updateBottomInfo(sipleDF.format(day.getCalendar().getTime()));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					updateBottomInfo("");
				}
			});
			fireDayCreated(day);
			calendarPanel.add(day);
			//天数增加一，进入下一天的day对象创建
			calender.add(Calendar.DAY_OF_MONTH, 1);
			i++;
			//System.out.println(calender.get(Calendar.MONTH)+"?????"+month);
		} while (calender.get(Calendar.MONTH) == month);
		return passDate;
	}
	/*
	public void getData() throws ClassNotFoundException, SQLException
	{
		connect();
		PreparedStatement pstmt = null;
		ResultSet rs;
		pstmt = conn.prepareStatement("select * from emp_healthcheckdata where H_date = '"+passDate+"';");
		System.out.println("select * from emp_healthcheckdata where H_date = '"+passDate+"';");
		rs = pstmt.executeQuery();
		int c = 0;
		while(rs.next())
		{
			c++;
		}
		System.out.println("count = "+c);
	}
	
	public void connect() throws ClassNotFoundException, SQLException
	{
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://192.168.1.100:3307/bb2_test";
		String user = "root"; 
		String password = "root";
	    Class.forName(driver);
	    conn = DriverManager.getConnection(url, user, password);
	    if(!conn.isClosed()) 
	      	System.out.println("Succeeded connecting to the Database!");
	}
*/
	/**
	 * 更新底部面板信息
	 * @param text
	 */
	public void updateBottomInfo(String text){
		bottomInfo.setText(text);
	}
	
	/**
	 *以指定的日期对象来构建日历面板
	 * @param calendar
	 */
	public void updateDate(Calendar calendar) {
		String date = dateFormat.format(calendar.getTime());
		actionPanel.setDateInfo(date);
		buildCalendarPanel(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
	}

	/**
	 * 添加日期点击监听
	 * @param listener
	 */
	public void addDayClickListener(DayClickListener listener){
		listeners.add(listener);
	}
	
	/**
	 * 移除日期点击监听
	 * @param listener
	 */
	public void removeDayClickListener(DayClickListener listener){
		listeners.remove(listener);
	}
	
	/**
	 * 发布日期单击事件
	 * @param day
	 * @param e
	 */
	private void fireDayClicked(DayPanel day,MouseEvent e){
		for(DayClickListener listener:listeners){
			listener.dayClicked(day, e);
			
		}
	}
	
	/**
	 * 添加日期创建监听器
	 * @param createListener
	 */
	public void addDayCreateListener(DayCompCreateListener createListener){
         createListeners.add(createListener);
	}
	
	/**
	 * 删除日期创建监听器
	 * @param createListener
	 */
	public void removeCreateListener(DayCompCreateListener createListener){
		createListeners.remove(createListener);
	}
	
	/*
	 * 发布日期创建事件
	 */
	private void fireDayCreated(DayPanel day){
		for(DayCompCreateListener listener:createListeners){
			listener.dayCompCreated(day);
		}
	}
	
	/**
	 * 添加动作按钮监听器
	 * @param listener
	 */
	public void addActionButtonListener(ActionButtonListener listener){
		actionListeners.add(listener);		
	}
	
	/**
	 * 移除动作按钮监听器
	 * @param listener
	 */
	public void removeActionButtonListener(ActionButtonListener listener){
		actionListeners.remove(listener);
	}
	
	/**
	 * 发布动作按钮动作事件
	 * @param button
	 * @param e
	 */
	private void fireActionButtonActionPerfored(JButton button,ActionEvent e){
		for(ActionButtonListener listener:actionListeners){
			listener.actionButtonactionPerformed(button, e);
		}
	}
	class ActionPanel extends JPanel implements ActionListener{
		//用来显示日期的标签对象
		private JLabel dateInfoLabel;
		//向后退一年的按钮
		private JButton beforeYear;
		//向后退一个月的按钮
		private JButton beforeMouth;
		//增加一个月按钮
		private JButton nextMouth;
		//增加一年按钮
		private JButton nextYear;
		
		public ActionPanel(){
			setOpaque(false);
			beforeYear = new BasicArrowButton(SwingConstants.WEST){
				@Override
				public void paintTriangle(Graphics g, int x, int y, int size, int direction, boolean isEnabled){
					super.paintTriangle(g, x, y, size, direction, isEnabled);
					super.paintTriangle(g, x-3, y, size, direction, isEnabled);
				} 
				 
			};
			beforeMouth = new BasicArrowButton(SwingConstants.WEST);
			nextMouth = new BasicArrowButton(SwingConstants.EAST);
			nextYear = new BasicArrowButton(SwingConstants.EAST){
				@Override
				public void paintTriangle(Graphics g, int x, int y, int size, int direction, boolean isEnabled){
					super.paintTriangle(g, x, y, size, direction, isEnabled);
					super.paintTriangle(g, x+3, y, size, direction, isEnabled);
				} 
				 
			};
			
			//初始化，采用当前日期
			dateInfoLabel = new JLabel(dateFormat.format(Calendar.getInstance().getTime()));

			beforeYear.addActionListener(this);
			beforeMouth.addActionListener(this);
			nextMouth.addActionListener(this);
			nextYear.addActionListener(this);

			add(beforeYear, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 8, 0, 0), 0, 0));
			add(beforeMouth, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			add(dateInfoLabel, new GridBagConstraints(2, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0,5, 0, 5), 0, 0));
			add(nextMouth, new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			add(nextYear, new GridBagConstraints(4, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 0, 0,8), 0, 0));
		}
		
		/**
		 * 获得当前标签所表示的日历对象
		 * @return
		 */
		public Calendar getDate() {
			Calendar calendar = Calendar.getInstance();
			String date = dateInfoLabel.getText();
			System.err.println("date>>>>"+date);
			String[] fields = date.split("-");
			calendar.set(Calendar.YEAR, Integer.parseInt(fields[0]));
			calendar.set(Calendar.MONTH, Integer.parseInt(fields[1]) - 1);
			return calendar;
		}
		
		/**
		 * 设置顶部的日期信息
		 * @param text
		 */
		public void setDateInfo(String text) {
			this.dateInfoLabel.setText(text);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == beforeYear) {
				Calendar calendar = getDate();
				calendar.add(Calendar.YEAR, -1);
				fireActionButtonActionPerfored(beforeYear,e);
			} else if (e.getSource() == beforeMouth) {
				Calendar calendar = getDate();
				calendar.add(Calendar.MONTH, -1);
				fireActionButtonActionPerfored(beforeMouth,e);
				updateDate(calendar);
			} else if (e.getSource() == nextMouth) {
				Calendar calendar = getDate();
				calendar.add(Calendar.MONTH, 1);
				fireActionButtonActionPerfored(nextMouth,e);
				updateDate(calendar);
			} else if (e.getSource() == nextYear) {
				Calendar calendar = getDate();
				calendar.add(Calendar.YEAR, 1);
				fireActionButtonActionPerfored(nextYear,e);
				updateDate(calendar);
			}
		}
	}
	/*
	public void outputExcel(String excelFile) throws IOException, WriteException, ClassNotFoundException
	{
		try {
			connect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			//createExcel(classNames[0], excelFile);
			createExcel(excelFile);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	

	
	
	
	public void splitDate(String date)
	{
		String[] array = date.split("-");
		String year = array[0];
		String month = array[1];
		String day = array[2];        
	}
	
	/** 
	* 获得指定日期的后一天 
	* @param specifiedDay 
	* @return 
	*/ 

	


	
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		//CalendarPop test = new CalendarPop();
		//Output.main(null);
	}
}