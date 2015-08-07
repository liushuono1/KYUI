package FinaceUI.Manege;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import KYUI.KYMainUI;

public class setFees extends JFrame{
	public JFrame frame; 
	private JTable table1;
	private JTable table2;
	private JTable tableDiscount;
	public List<String> type_list;
	public Hashtable<String, String> typeTitle_typeCode;
	public JComboBox jb;
	public JComboBox jbDiscount;
	public List<String> oneTimeItems;
	public List<String> discountItems;
	public JPanel oneTimepanel;
	public JPanel discountPanel;
	public static Hashtable<String, String> id_typeItemID;
	public static List<String> class_mini;
	public static List<String> class_small;
	public static List<String> class_middle;
	public static List<String> class_big;
	public String payFeesCodes;
	
	public String mealUnitFee;
	public static List<String> discount_id_list;
	public int attend_days; 
	public static Hashtable<String, String> type_price;
	public String year_month;
	public static List<List<String>> record_list1;
	public static List<FeeRecord> record_list;
	public static String[] regularItems = {"保育费","饭费","杂费","退饭费","退保育费"};
	public List<JCheckBox> box_list;
	public static List<String> selection;
	
	//thest variables are defined for simplifying the SL loop
	
	static Hashtable<String, String> all_fees;
	static String unit_meal_fees;
	static String miscellaneousFee;
	static workingdays workingDays;
	static Hashtable<String,List> ondutyofmonth;
	setStuRange setRange;
	static Connection conn=null;
	public setFees()
	{
		frame = new JFrame();
		frame = new JFrame("设置费用");
		int a10 = Toolkit.getDefaultToolkit().getScreenSize().width; // 取得屏幕长度
		int b10 = Toolkit.getDefaultToolkit().getScreenSize().height; // 取得屏幕宽度
		frame.setLocation((a10 - 1466) / 2, (b10 - 1000) / 2); // 设定位置（屏幕中心）
		frame.setSize(550, 400); // 设定大小
		frame.setResizable(true); // 设定不能缩放
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		//frame.setLayout(new GridLayout(3,1));
		
		JLabel label = new JLabel("请设置缴费需求:");
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,3));
		JPanel p1 = regularFeesPanel();
		//p1.setBorder(BorderFactory.createTitledBorder("常规费用"));

		//JPanel p2 = new JPanel();
		//p2.setBorder(BorderFactory.createTitledBorder("一次性费用"));
		
		//JPanel p3 = new JPanel();
		//p3.setBorder(BorderFactory.createTitledBorder("折扣"));
		
		mainPanel.add(p1);
		mainPanel.add(oneTimeFeesPanel());
		mainPanel.add(discountPanel());
		
		
		JPanel btnPanel = new JPanel();
		JButton btn1 = new JButton("范围设定");
		btn1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				 setRangeHandler();
			}
			
		});
		
		
		JButton btn2 = new JButton("提交");
		btn2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				submitHandler();
			}
			
		});
		JButton btn3 = new JButton("取消");
		btn3.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.dispose();
			}
		});
		btnPanel.add(btn1);
		btnPanel.add(btn2);
		btnPanel.add(btn3);
		frame.add(BorderLayout.NORTH, label);
		frame.add(mainPanel);
		frame.add(BorderLayout.SOUTH, btnPanel);
		frame.setVisible(true);
		
	}
	
	private void setRangeHandler() {
		// TODO Auto-generated method stub
		//List<String> selectList = new LinkedList<String>();
		setRange = new setStuRange();
		
		//return selectList;
	}
	
	private void getRangeSet()
	{
		int ret = setRange.retParameter;
		selection = new LinkedList<String>();
		System.out.println("ret = "+ret);
		if(ret == 2)//select individual students
		{
			String[] selectArray = setRange.selStudArray;
			for(int i=0;i<selectArray.length;i++)
			{
				selection.add(selectArray[i].toUpperCase());
			}
		}
		else if(ret == 0)//select all students
		{
			selection = getIdList();
		}
		System.err.println("selectList = "+selection);
	}

	public JPanel regularFeesPanel()
	{
		JPanel p1 = new JPanel();
		p1.setBorder(BorderFactory.createTitledBorder("常规费用"));
		p1.setLayout(new GridLayout(5,1));
		box_list = new LinkedList<JCheckBox>();
		for(int i=0;i<this.regularItems.length;i++)
		{
			JCheckBox box = new  JCheckBox(regularItems[i]);
			box_list.add(box);
			box.setSelected(true);
			p1.add(box);
		}
		/*
		JCheckBox box1 = new  JCheckBox("保育费");
		box1.setSelected(false);
		JCheckBox box2 = new  JCheckBox("饭费");
		box2.setSelected(true);
		JCheckBox box3 = new  JCheckBox("杂费");
		box3.setSelected(true);
		JCheckBox box4 = new  JCheckBox("退饭费");
		box4.setSelected(true);
		JCheckBox box5 = new  JCheckBox("退保育费");
		box5.setSelected(true);
		*//*
		try{
			System.err.println(box.getSelectedObjects()[0]);
		}catch(Exception e)
		{

			
			System.err.println(box.getLabel());
		}
		
		p1.add(box1);
		p1.add(box2);
		p1.add(box3);
		p1.add(box4);
		p1.add(box5);*/
		return p1;
	}
	
	public JPanel oneTimeFeesPanel()
	{
		oneTimepanel = new JPanel();
		oneTimepanel.setBorder(BorderFactory.createTitledBorder("一次性费用"));
		oneTimepanel.setLayout(new GridLayout(3,1));
	
		JPanel itemPanel = new JPanel();
		JLabel item_label = new JLabel("选择项目");
		jb = new JComboBox();
		
		getOneTimeItems();
		for(int i=0;i<type_list.size();i++)
		{
			jb.addItem(type_list.get(i));
		}
		itemPanel.add(item_label);
		itemPanel.add(jb);
		
		JPanel btnPanel = new JPanel();
		Icon icon1 = new ImageIcon(getClass().getResource("down.jpg"));
		Icon icon2 = new ImageIcon(getClass().getResource("up.jpg"));
		JButton btn1 = new JButton(icon1);
		btn1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				downHandler();
			}
			
		});
		JButton btn2 = new JButton(icon2);
		btn2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				upHandler();
			}
			
		});
		btnPanel.add(btn1);
		btnPanel.add(btn2);
		
		JTable table = createTable();
		table.setSize(oneTimepanel.getWidth(), oneTimepanel.getHeight());
		
		oneTimepanel.add(itemPanel);
		oneTimepanel.add(btnPanel);
		oneTimepanel.add(table);
		return oneTimepanel;
	}
	
	public JTable createTable()
	{
		oneTimeItems = new LinkedList<String>();
		table1 = new JTable(this.SingleTableModel(oneTimeItems));
		return table1;
	}
	
	public JTable createTableDiscount()
	{
		discountItems = new LinkedList<String>();
		table2 = new JTable(this.SingleTableModel(discountItems));
		return table2;
	}
	
	 private DefaultTableModel SingleTableModel(List itemList)
	 {
		 int rows= itemList.size();
		 Object[][] x=  new Object[rows][1];
		 for(int i=0;i<rows;i++)
		 {
			 x[i][0]=itemList.get(i);
	     }	
		 return new DefaultTableModel(x, new String[]{"测试行1"});	
	 }
	 
	 public void downHandler()
	 {
		 String item = jb.getSelectedItem().toString();
		 if(!oneTimeItems.contains(item))
		 {
			  oneTimeItems.add(item);
		 }
		 System.err.println(oneTimeItems);
		 this.table1.setModel(this.SingleTableModel(oneTimeItems));
	 }
	 
	 public void downHandlerDiscount()
	 {
		 String item = jbDiscount.getSelectedItem().toString();
		 if(!discountItems.contains(item))
		 {
			 discountItems.add(item);
		 }
		 this.table2.setModel(this.SingleTableModel(discountItems));
	 }
	 
	 public void upHandler()
	 {
		int row = table1.getSelectedRow();
		oneTimeItems.remove(row);
		table1.setModel(SingleTableModel(oneTimeItems));
	 }
	 

	 public void upHandlerDiscount()
	 {
		int row = table2.getSelectedRow();
		discountItems.remove(row);
		table2.setModel(SingleTableModel(discountItems));
	 }

		public void getOneTimeItems()
		{
			type_list = new LinkedList<String>();
			typeTitle_typeCode = new Hashtable<String, String>();
			try {
				Connection conn = connect();
				PreparedStatement p = null;
				ResultSet r = null;
				p = conn.prepareStatement("select * from emp_one_time_type where type_code like '0%';");
				r = p.executeQuery();
				while(r.next())
				{
					String a_code = r.getString("type_code").substring(2);
					String a_type = r.getString("title");
					type_list.add(a_type);
					typeTitle_typeCode.put(a_type, a_code);
				}
				r.close();
				p.close();
				//conn.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public JPanel discountPanel()
		{
			discountPanel = new JPanel();
			discountPanel.setBorder(BorderFactory.createTitledBorder("折扣"));
			discountPanel.setLayout(new GridLayout(3,1));
		
			JPanel itemPanel = new JPanel();
			JLabel item_label = new JLabel("选择折扣类型");
			jbDiscount = new JComboBox();
			getDiscountTypes();
			/*
			for(int i=0;i<discountItems.size();i++)
			{
				jbDiscount.addItem(discountItems.get(i));
			}*/
			
			//for(int i=0;i<discountItems.size();i++)
			{
				jbDiscount.addItem("长期折扣");
				jbDiscount.addItem("一次性折扣");
			}
			
			itemPanel.add(item_label);
			itemPanel.add(jbDiscount);
			
			JPanel btnPanel = new JPanel();
			Icon icon1 = new ImageIcon(getClass().getResource("down.jpg"));
			Icon icon2 = new ImageIcon(getClass().getResource("up.jpg"));
			JButton btn1 = new JButton(icon1);
			btn1.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					downHandlerDiscount();
				}
				
			});
			JButton btn2 = new JButton(icon2);
			btn2.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					upHandlerDiscount();
				}
				
			});
			btnPanel.add(btn1);
			btnPanel.add(btn2);
			
			JTable table = createTableDiscount();
			table.setSize(discountPanel.getWidth(), discountPanel.getHeight());
			
			discountPanel.add(itemPanel);
			discountPanel.add(btnPanel);
			discountPanel.add(table);
			return discountPanel; 
		}
		
		public void getDiscountTypes()
		{
			discountItems = new LinkedList<String>();
			try {
				Connection conn = connect();
				PreparedStatement p = null;
				ResultSet r = null;
				p = conn.prepareStatement("select * from emp_discount_type;");
				r = p.executeQuery();
				while(r.next())
				{
					String title = r.getString("title");
					discountItems.add(title);	
				}
				r.close();
				p.close();
				//conn.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//---------------------------start from here-------------------------------
		
		public void submitHandler()
		{
			initVariables();
			getRangeSet();
			List<String> selectedItems = new LinkedList<String>();
			for(int i=0;i<box_list.size();i++)
			{
				JCheckBox box = box_list.get(i);
				if(box.isSelected())
				{
					selectedItems.add(box.getLabel());
				}
			}
			selectedItems.addAll(this.oneTimeItems);
			selectedItems.addAll(this.discountItems);
			
			System.out.println(selectedItems);
			java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
			String year_month = "";
			String year;
			String month;
			year = String.valueOf(date.getYear()+ 1900);
			month = String.valueOf(date.getMonth() + 1);
			year_month = String.valueOf(year) + "-"+String.valueOf(month);
			
			int confirm = JOptionPane.showConfirmDialog(null, "请确认产生费用的月份是"+year+"年"+month+"月!");
			if(confirm == 0)
			{
				//List<String> id_list = getIdList();
				if(selection.size() == 0)
				{
					JOptionPane.showMessageDialog(null, "请设置学生范围");
					return;
				}
				System.out.println("???selection = "+selection);
				for(int i=0;i<selection.size();i++)//selection.size();
				{
					String id = selection.get(i);
					one_fee sum = checkFees(id,selectedItems,Integer.valueOf(year),Integer.valueOf(month),false);
					System.out.println(id+"   "+sum);
					chargeRegularFee(id, sum);//charge this id
				}
			}
		}
		
		
		
		public static void chargeFinalFees(String id)
		{
			initVariables();
			
			List<String> selectedItems = new LinkedList<String>();

			selectedItems.add("退饭费");
			selectedItems.add("退保育费");
			
			System.out.println(selectedItems);
			java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
			String year_month = "";
			String year;
			String month;
			year = String.valueOf(date.getYear()+ 1900);
			month = String.valueOf(date.getMonth() + 1);
			year_month = String.valueOf(year) + "-"+String.valueOf(month);
			
			int confirm = JOptionPane.showConfirmDialog(null, "请确认退费月份是"+year+"年"+month+"月!");
			if(confirm == 0)
			{
					one_fee sum = checkFees(id,selectedItems,Integer.valueOf(year),Integer.valueOf(month),true);
					System.out.println(id+"   "+sum);
					chargeRegularFee(id, sum);//charge this id
					chargeRegularFee(id, new one_fee("FinalFees"));
			}
		}
		
		private static void initVariables() {
			// TODO Auto-generated method stub
			all_fees=null;
			miscellaneousFee=null;
			unit_meal_fees =null;
			miscellaneousFee=null;
			workingDays=null;
			ondutyofmonth=null;
			getEachClassID();
		}

		public static class one_fee
		{
			public double sum;
			public String ChargeCode;
			@Override
			public String toString()
			{
				return sum + "  "+  ChargeCode;
			}
			
			public one_fee()
			{
				
			}
			public one_fee(String type)
			{
				if(type.equals("FinalFees"))
				{
					sum=200;
					ChargeCode="refund:退还入园押金:200";
				}
			}
			
		}
		
		public static void chargeRegularFee(String id, one_fee sum)
		{
			record_list = new LinkedList<FeeRecord>();
			record_list1 = new LinkedList<List<String>>();
			try {
				Connection conn = connect();
				PreparedStatement p = null;
				ResultSet r = null;
				p = conn.prepareStatement("select * from emp_charge_refund_pay where id = '"+id+"';");
				r = p.executeQuery();
				List<List> all_payItems = new LinkedList<List>();
				while(r.next())
				{
					List<String> a_payItem = new LinkedList<String>();
					String date = r.getString("P_date");
					String chargeItems = r.getString("detail");//P_typeTitle
					a_payItem.add(date);
					a_payItem.add(chargeItems);
					all_payItems.add(a_payItem);
				}
				java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
		
				String dateStr = currentDate.toString();
				
				List<String> regularItem = new LinkedList<String>();
				regularItem.add(dateStr);//date
				regularItem.add(sum.ChargeCode);//item title
				if(!all_payItems.contains(regularItem))//check whether this "charge item" has been inserted into database or not
				{
					p = conn.prepareStatement("insert into emp_charge_refund_pay values (?,?,?,?,?,?,?,?);");
					Time currentTime = new Time(System.currentTimeMillis());
					p.setString(1, id);
					p.setDate(2, currentDate);
					p.setTime(3, currentTime);
					p.setString(4, sum.ChargeCode);
					if(!sum.ChargeCode.contains("refund"))
					{
						p.setString(5, "");
						p.setString(6, String.valueOf(sum.sum));
					}else
					{
						p.setString(6, "");
						p.setString(5, String.valueOf(sum.sum));
					}
					
					p.setString(7, "");
					p.setString(8, "");
					//p.setString(9, "");
					System.out.println(p);
					p.execute();
				}
				r.close();
				p.close();
				//conn.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public List<String> getIdList()
		{
			List<String> id_list = new LinkedList<String>();
			try {
				Connection conn = connect();
				PreparedStatement p = null;
				ResultSet r = null;
				p = conn.prepareStatement("select * from emp_id where security_level = 'LEVEL 5' OR security_level = 'LEVEL 4'");
				r = p.executeQuery();
				while(r.next())
				{
					String id = r.getString("id");
					id_list.add(id);
				}
				r.close();
				p.close();
				//conn.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return id_list;
		}
		
		public static one_fee checkFees(String id, List<String> selectedItems , int year,int month,boolean final_fees)
		{
			one_fee sum = new one_fee();
			String[] child_care = get_child_care_typeFees(id);
			sum.ChargeCode = "";
			sum.sum = 0;
			{
				//-------------------------child care-----------------------------------
				if(selectedItems.contains(regularItems[0]))
				{
					//String[] child_care = get_child_care_typeFees(id);??
					String child_care_type = child_care[0];//get child-care type!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					
					//System.out.println(child_care_type);
					sum.ChargeCode +=  Integer.toHexString(Integer.parseInt(child_care_type, 10));
					String child_care_fee_str = child_care[1];//get child_care fee
					int child_care_fee = Integer.parseInt(child_care_fee_str);
					//output += "保育费: "+child_care_fee+"元\n\n";
					sum.sum += child_care_fee;
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
							+ "child_care_type = "+Integer.toHexString(Integer.parseInt(child_care_type, 10))+
							"     payFeesCodes="+sum.ChargeCode + "  sum:" + sum.sum);

				}
				else
				{
					sum.ChargeCode += "0";
				}
				//-------------------------Meal------------------------------------------
				if(selectedItems.contains(regularItems[1]))
				{
					String MealUnitFee = getUnitMealFees();
					int meal_fee = Integer.parseInt(MealUnitFee)*26+10-(Integer.parseInt(MealUnitFee)*26)%10;
					//payFeesCodes += String.valueOf(26);//add days of paying meal-fees
					sum.ChargeCode += Integer.toHexString(Integer.parseInt(String.valueOf(1), 10));//add days of paying meal-fees
					sum.sum += meal_fee;
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!26 = "
					+Integer.toHexString(Integer.parseInt(String.valueOf(26), 10))+
					"     payFeesCodes="+sum.ChargeCode +"  sum:" +sum.sum);

				}
				else
				{
					sum.ChargeCode += "0";
				}
				
				//-------------------------miscellaneous Fee------------------------------------------
				if(selectedItems.contains(regularItems[2]))
				{
					String miscellaneousFee = getMiscFees();
					String miscStr =Integer.toHexString(Integer.parseInt(String.valueOf(miscellaneousFee), 10));
					if(miscStr.length()<2)
						miscStr="0"+miscStr;
					sum.ChargeCode +=miscStr ;//add the miscellaneousFee fees						
					sum.sum += Integer.parseInt(miscellaneousFee);
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!MIsc= "
							+Integer.toHexString(Integer.parseInt(String.valueOf(miscellaneousFee), 10))+
							"     payFeesCodes="+sum.ChargeCode +"  sum:" +sum.sum);
				}
				else
				{
					sum.ChargeCode += "00";
				}
				
				//-------------------------Refund (Meal + ChildCare)------------------------------------------
				if(selectedItems.contains(regularItems[3]))//refund mealFee
				{
					int child_care_fee = Integer.parseInt(child_care[1]);
					int refund_fee;
					String[] passedCode =new String[]{sum.ChargeCode};
					if(selectedItems.contains(regularItems[4]))//refund childCare Fee
					{
						refund_fee = getRefundFees(id, child_care_fee, 0,passedCode,String.valueOf(year),String.valueOf(month),final_fees);//refund fee
					}
					else//not having childcareFee
					{
						refund_fee = getRefundFees(id, child_care_fee, 1,passedCode,String.valueOf(year),String.valueOf(month),final_fees);//refund fees
						//JOptionPane.showMessageDialog(null, "只退保育费,不退饭费的情况不可能发生, 请重新确认缴费/退费项目!");
					}
					sum.sum += (0 - refund_fee);
					sum.ChargeCode=passedCode[0];
				}
				else
				{
					sum.ChargeCode+="100";
				}
				//-------------------------discount---------------------------------------
			
				if(true) // this is the switch of discount 
				{
					List discount_type_and_fees =  getDiscountFee(id, child_care[1]);
					String discountType = (String) discount_type_and_fees.get(0);//get discount type!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					//payFeesCodes += discountType;//add the discount type

					int discountFees = (Integer) discount_type_and_fees.get(1);//get discount fee
					
					//--------------------------One_time Fees---------------------------------
					//int oneTime_fee = checkOneTimeFees(id);
					
					sum.sum +=  discountFees;// + oneTime_fee ;
					sum.ChargeCode += Integer.toHexString(Integer.parseInt(discountType, 10));//add the discount type
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!discountType = "+discountType+"     payFeesCodes= "+sum);
				}
			
			}
			
			return sum;
		}
		/*
		public void discount(String id)
		{
			this.getDiscountDeatails();
			String discount_type = id_typeItemID.get(id);
			
			
			
			//-------------------------discount---------------------------------------
			List discount_type_and_fees =  getDiscountFee(id, child_care_fee_str);
			String discountType = (String) discount_type_and_fees.get(0);//get discount type!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			//payFeesCodes += discountType;//add the discount type
			payFeesCodes += Integer.toHexString(Integer.parseInt(discountType, 10));//add the discount type
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!discountType = "+discountType+"     payFeesCodes="+payFeesCodes);
			int discountFees = (Integer) discount_type_and_fees.get(1);//get discount fee
		}*/
		
		/*
		 * This function return the "discount fees" if having one
		 */
		public static List getDiscountFee(String id, String child_care_str)
		{
			int fee = 0;
			List<String> discount_id_list= getDiscountDeatails();
			String discountTypeID = null;
			if(discount_id_list.contains(id))
			{
				discountTypeID = id_typeItemID.get(id);//get discount type (i.e., 0,1,2,3,4)
				if(discountTypeID.equals("3"))
				{
					fee  = -Integer.parseInt(child_care_str);
				}
				else
				{
					String price = type_price.get(discountTypeID);
					fee = Integer.parseInt(price);
				}
			}
			List type_and_fee = new LinkedList();
			if(discountTypeID ==null)
			{
				discountTypeID = String.valueOf(0);
			}
			type_and_fee.add(discountTypeID);//0: the discount type of the student
			type_and_fee.add(fee);//1: the discount fees 
			return type_and_fee;
		}
		
		public static int getRefundFees(String id, int child_care_fee, int status,String[] PayFeesCodes,String year,String month,boolean final_fees)
		{
			int refund_fees = 0;
			double refund_childCare_type;
			String prev_date;
			if(final_fees)
				prev_date =year+"-"+month;
			else
				prev_date= getPreviousYear_month(year+"-"+month);//the "year" & "month" of the previous month
			String[] array = prev_date.split("-");
			String[] first_last_day = lastDate(Integer.parseInt(array[0]), Integer.parseInt(array[1]));//last date of the previous month
			String first_date = first_last_day[0];
			String last_date = first_last_day[1];
			int abscentCount = 0;
			List<String> abscent_days = new LinkedList<String>();
			List<String> come_days = new LinkedList<String>();
			List<String> off_days = new LinkedList<String>();
			List<String> shouldWork_days = WorkingDaysofMonth(prev_date.split("-")[0], prev_date.split("-")[1]);
			int unit_meal_fee = Integer.parseInt(getUnitMealFees());
			int counter = 0;
			String temp_date = first_date;
			System.out.println(shouldWork_days);
			System.out.println(onDutyofMonth(year+"-"+month));
			
			/*while(validDate(temp_date, last_date))
			{
				List<String> attend_ids = onDuty(temp_date);//how many kid attend at this day
				if(attend_ids.size() != 0)//as long as having people
				{
					counter++;
					if(!attend_ids.contains(id))
					{
						abscentCount++;
						abscent_days.add(temp_date);//the days that the kid does not go to school
					}
					shouldWork_days.add(temp_date);//working
				}
				else 
				{
					off_days.add(temp_date);//off-work
				}
				temp_date = nextDate1(temp_date);
			}*/
			for(String day: shouldWork_days)
			{
				List<String> attend_ids = onDutyofMonth(day).get(day);
				if(attend_ids.size() != 0)//as long as having people
				{
					if(!attend_ids.contains(id))
					{
						abscentCount++;
						abscent_days.add(day);//the days that the kid does not go to school
					}else
					{
						counter++;
					}
				}
			}
			
			
			System.err.println("counter = "+counter+"  abscentCount = "+abscentCount);
			int refund_meal = (26-counter) * Integer.parseInt(getUnitMealFees()) ;
			//CODE
			PayFeesCodes[0] += decimalToHex(26-counter+16);
			System.out.println("after refund code:"+ PayFeesCodes[0]);
			int refund_childCare =0;
			//-------------correct childfees with longterm discount-------------------------
			
			List<String> discount_id_list= getDiscountDeatails();
			String discountTypeID = null;
			if(discount_id_list.contains(id))
			{
				discountTypeID = id_typeItemID.get(id);//get discount type (i.e., 0,1,2,3,4)
				if(discountTypeID.equals("3"))
				{
					child_care_fee=0;
				}
				else if(discountTypeID.equals("4"))
				{
					child_care_fee=child_care_fee;
				}
				else
				{
					String price = type_price.get(discountTypeID);
					child_care_fee = child_care_fee + Integer.parseInt(price);
				}
				
			}
			
			
			
			
			
			
			
		
				//------------------------------------refund childCare Fee----------------------------------------------------------
				
	
			
				System.err.println("shouldWork_days = "+shouldWork_days);
				refund_childCare_type = continueAbsence(abscent_days, shouldWork_days);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				
				if(refund_childCare_type==2)
				{
					refund_childCare = (int)(child_care_fee * refund_childCare_type /2); 
					refund_fees = refund_meal + refund_childCare+52; 
				}
				else if(refund_childCare_type==1 && counter<=3 && counter>0)
				{
					//refund_childCare_type=counter+2;
					refund_childCare = child_care_fee *(1-counter/26); 
					refund_fees = refund_meal + refund_childCare+(26-counter)/26*50;
				}else if(refund_childCare_type==1 && counter==0)
				{
					refund_childCare = child_care_fee ; 
					refund_fees = refund_meal + refund_childCare+50;
				}
				else 
				{
					refund_childCare = (int)(child_care_fee * refund_childCare_type /2); 
					refund_fees = refund_meal + refund_childCare; 
				}
				

				PayFeesCodes[0] += Integer.toHexString((int)refund_childCare_type); //add the "days" of refunding meal_fee
				System.out.println("after refund1 code:"+ PayFeesCodes[0]);
				
	
				
		
			
			System.out.println("charge code:  "+PayFeesCodes[0] +" refund  fees"+ refund_fees);
			return refund_fees;
		}
		
		/*
		public void refundChildCare(List<String> abscent_days )
		{
			int refund_childCare = 0;
			System.err.println("abscent_days>>"+abscent_days);
			temp_date = first_date;
			while(validDate(temp_date, last_date))
			{
				if(!abscent_days.contains(temp_date) && !isSunday(temp_date))
				{
					come_days.add(temp_date);
				}
				temp_date = nextDate1(temp_date);
			}
			System.err.println("shouldWork_days = "+shouldWork_days);
			int refund_childCare_type = continueAbsence(come_days, shouldWork_days);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			
			refund_childCare = child_care_fee * refund_childCare_type /2; 
			
		}*/
		
		public boolean isSunday(String date)
		{
			boolean flag = false;
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");        
			Date bdate;
			try {
				bdate = format1.parse(date);
				Calendar cal = Calendar.getInstance();
				cal.setTime(bdate);
				if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
				{
					flag = true;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return flag;
		}
		
		public String nextDate1(String specifiedDay)
		{ 
			Calendar c = Calendar.getInstance(); 
			Date date=null; 
			try { 
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay); 
			} catch (ParseException e) { 
			e.printStackTrace(); 
			} 
			c.setTime(date); 
			int day=c.get(Calendar.DATE); 
			c.set(Calendar.DATE,day+1); 
			String dayAfter=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()); 
			return dayAfter; 
		} 
		
		public static String getUnitMealFees()
		{
			if(unit_meal_fees == null)
			{
				try {
					Connection conn = connect();
					PreparedStatement p = null;
					ResultSet r = null;
					p = conn.prepareStatement("select * from emp_one_time_type where title='meal'");
					r = p.executeQuery();
					r.next();
					unit_meal_fees = r.getString("price");
					
					p = conn.prepareStatement("select * from emp_one_time_type where title='miscellaneous fee'");
					r = p.executeQuery();
					r.next();
					miscellaneousFee = r.getString("price");
					r.close();
					p.close();
					//conn.close();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return unit_meal_fees;
		}
		public static String getMiscFees()
		{
			if(miscellaneousFee == null)
			{
				try {
					Connection conn = connect();
					PreparedStatement p = null;
					ResultSet r = null;

					p = conn.prepareStatement("select * from emp_one_time_type where title='miscellaneous fee'");
					r = p.executeQuery();
					r.next();
					miscellaneousFee = r.getString("price");
					r.close();
					p.close();
					//conn.close();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return miscellaneousFee;
		}
		
		public static String[] get_child_care_typeFees(String id)
		{
			String[] ret = new String[2];
			String fee = null;
			Hashtable<String,String> all_fees = getFees();
			String classType = null;
			if(class_mini.contains(id))
			{
				classType = "1";
				fee = all_fees.get(classType);
			}
			else if(class_small.contains(id))
			{
				classType = "2";
				fee = all_fees.get(classType);
			}
			else if(class_middle.contains(id))
			{
				classType = "3";
				fee = all_fees.get(classType);
			}
			else if(class_big.contains(id))
			{
				classType = "4";
				fee = all_fees.get(classType);
			}
			ret[0] = classType;//the class type (i.e., 1,2,3,4<---->宝宝班, 小班, 中班, 大班)
			ret[1] = fee;//the correponding fees
			return ret;
		}
		
		public static void getEachClassID()
		{
			if(class_mini==null || class_small==null 
					|| class_middle==null || class_big==null)
			{
			class_mini = new LinkedList<String>();
			class_small = new LinkedList<String>();
			class_middle = new LinkedList<String>();
			class_big = new LinkedList<String>();
			String id;
			try {
				Connection conn = connect();
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				pstmt = conn.prepareStatement("select * from emp_id where department LIKE '宝%'");
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					id = rs.getString("id");
					class_mini.add(id);
				}
				
				pstmt = conn.prepareStatement("select * from emp_id where department LIKE '小%'");
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					id = rs.getString("id");
					class_small.add(id);
				}
				
				pstmt = conn.prepareStatement("select * from emp_id where department LIKE '中%'");
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					id = rs.getString("id");
					class_middle.add(id);
				}
				
				pstmt = conn.prepareStatement("select * from emp_id where department LIKE '大%'");
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					id = rs.getString("id");
					class_big.add(id);
				}
				rs.close();
				pstmt.close();
				//conn.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}

		
		/*
		 * Get the fees of each class-level
		 */
		public static Hashtable<String,String> getFees(){
			
			if (all_fees== null){
				all_fees = new Hashtable<String,String>();
				
				try {
					Connection conn = connect();
					PreparedStatement p = null;
					ResultSet r = null;
					p = conn.prepareStatement("select * from emp_child_care_fees;");
					r = p.executeQuery();
					
					while(r.next())
					{
						all_fees.put(r.getString("item_id"), r.getString("price"));
					
					}
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return all_fees;
		}
		

		
		public static List<String> getDiscountDeatails()
		{
			if(discount_id_list != null){
				return discount_id_list;
			
			}
			else
			{
			try {
				Connection conn = connect();
				PreparedStatement p = null;
				ResultSet r = null;
				p = conn.prepareStatement("select d.id, t.item_id, d.title,t.price from emp_discount_detail d join emp_discount_type t on d.item_id = t.item_id;");
				r = p.executeQuery();
				discount_id_list = new LinkedList<String>();
				id_typeItemID = new Hashtable<String, String>();
				while(r.next())
				{
					String a_id = r.getString("id");
					String a_itemID = r.getString("item_id");
					discount_id_list.add(a_id);
					id_typeItemID.put(a_id, a_itemID);
				}
				p = conn.prepareStatement("select * from emp_discount_type");
				r = p.executeQuery();
				type_price = new Hashtable<String, String>();
				while(r.next())
				{
					String a_typeTitle = r.getString("item_id");
					String a_price = r.getString("price");
					type_price.put(a_typeTitle, a_price);
				}
				r.close();
				p.close();
				//conn.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			return discount_id_list;
		}
		
		public static String getPreviousYear_month(String date)
		{
			String[] array = date.split("-");
			int year = Integer.parseInt(array[0]);
			int month = Integer.parseInt(array[1]);
			if(month >= 2)
			{
				month -= 1;
			}
			else 
			{
				month = 12;
				year -= 1;
			}
		    String date_previous = String.valueOf(year)+"-"+String.valueOf(month);
			return date_previous;
		}
		
		/*
		 * This method returns the 1st & last date of the specified "Month"
		 */
		public static String[] lastDate(int year, int month)
		{
			System.out.println("year = "+year +"  month = "+month);
			Calendar calendar=Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);//某一月(1月份是0));
			int end=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			int begin=calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
			System.out.println("end = "+end);
			String begin_date = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(begin);
			String end_date = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(end);
			System.out.println("begin_date = "+begin_date+"  end_date = "+end_date);
			String[] ret = new String[2];
			ret[0] = begin_date;
			ret[1] = end_date;
			return ret;
		}
		public static Hashtable<String,List> onDutyofMonth(String date)
		{
			int year = Integer.parseInt(date.split("-")[0]);
			int month = Integer.parseInt(date.split("-")[1]);
			return onDutyofMonth(year,month);
		}
		
		public static Hashtable<String,List> onDutyofMonth(int year ,int month)
		{
			String monthStr =("0"+String.valueOf(month));
		//	System.out.println(" dddddd->"+ondutyofmonth+"  "+year+"  "+month);
			if(ondutyofmonth!=null && ondutyofmonth.size() !=0 &&ondutyofmonth.keys().nextElement().contains(year+"-"+monthStr.substring(monthStr.length()-2, monthStr.length())))
			{
				return ondutyofmonth;
			}
			List<String> workingdays = WorkingDaysofMonth(year+"-"+month);
			ondutyofmonth = new Hashtable<String,List> ();
			for(String day:workingdays)
			{
				
				List<String> onDutyDay = onDuty(day);
				ondutyofmonth.put(day, onDutyDay);
			}
			
			System.out.println(ondutyofmonth.size());
			return ondutyofmonth;
		}
		
		
		
		public List<String> WorkingDaysofMonth(java.sql.Date date)
		{
			String year = String.valueOf(date.getYear());
			String month = String.valueOf(date.getMonth());
			return WorkingDaysofMonth(year,month);
		}
		
		
		public static List<String> WorkingDaysofMonth(String date)
		{
			String year = date.split("-")[0];
			String month = date.split("-")[1];
			return WorkingDaysofMonth(year,month);
		}
		
		
		public static List<String> WorkingDaysofMonth(String yearS ,String monthS)
		{
		    int year = Integer.parseInt(yearS);	
		    int month = Integer.parseInt(monthS);
			if(workingDays != null && workingDays.year==year &&workingDays.month==month)
			{
				return workingDays.Days;
			}
			
			workingDays = new workingdays(year,month);
			workingDays.Days= new LinkedList<String>();
			try {
				Connection conn = connect();
				PreparedStatement p = null;
				ResultSet rs = null;
				p = conn.prepareStatement("select distinct(L_date) from emp_logginrecordall l join emp_id i on l.id=i.id where (i.security_level='level 5' OR i.security_level = 'LEVEL 4') and l.L_date like ? order by L_date;" );
				
				String monthStr =("0"+String.valueOf(month));
				
				p.setString(1, year+"-"+monthStr.substring(monthStr.length()-2, monthStr.length())+"%");
				System.out.println(p);
				rs = p.executeQuery();
				while(rs.next())
				{
		        	String a_date = rs.getString("L_date");
		        	workingDays.Days.add(a_date);
		        	//System.out.println(a_date);
				}
			
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return workingDays.Days;
			
		}
		
		public static class workingdays
		{
			public int year=0;
			public int month =0;
			public List<String> Days;
			
			workingdays(int year,int month)
			{
				this.year=year;
				this.month=month;				
			}
		}
		
		public static List<String> onDuty(String date)
		{
			List<String> onDuty_id_list = new LinkedList<String>();
			try {
				Connection conn = connect();
				PreparedStatement p = null;
				ResultSet rs = null;
				p = conn.prepareStatement("SELECT * from emp_logginrecordall l join emp_id i on l.id=i.id where i.security_level='level 5' and l.L_date = '"+date+"';" );
				rs = p.executeQuery();
				Hashtable<List<String>, String> idDate_str = new Hashtable<List<String>, String>();
				while(rs.next())
				{
					String a_id = rs.getString("id");
		        	String a_date = rs.getString("L_date");
		        	List<String> one_record = new LinkedList<String>();
		        	one_record.add(a_id);//id
		        	one_record.add(a_date);//date
		        	idDate_str.put(one_record, "1");
				}
				Enumeration enumeration = idDate_str.keys();
			    while(enumeration.hasMoreElements())
			    {
			    	List<String> id_date = (List<String>) enumeration.nextElement();
			        String a_id = id_date.get(0);//name
			        onDuty_id_list.add(a_id);
			    }
				rs.close();
				p.close();
				//conn.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return onDuty_id_list;
		}
		
		public boolean validDate(String begin, String end)
		{
			boolean flag = true;
			String[] array1 = begin.split("-");
			String[] array2 = end.split("-");
			int year1 = Integer.valueOf(array1[0]);
			int month1 = Integer.valueOf(array1[1]);
			int day1 = Integer.valueOf(array1[2]);
			
			int year2 = Integer.valueOf(array2[0]);
			int month2 = Integer.valueOf(array2[1]);
			int day2 = Integer.valueOf(array2[2]);
			
			if(year1 > year2)
			{
				flag = false;
			}
			else if(year1 == year2)
			{
				if(month1 > month2)
				{
					flag = false;
				}
				else if(month1 == month2)
				{
					if(day1 > day2)
					{
						flag = false;	
					}	
				}
			}
			return flag;
		}
		
		public static double continueAbsence(List<String> Absenct_days, List<String> workingDays)
		{
			int ret = 0;
			if(workingDays.size() == 0)
			{
				return 0;
			}
			List<Integer> abList = new LinkedList<Integer>();
			int count=0;
			for(String thisDay:workingDays)
			{
				if(!Absenct_days.contains(thisDay))
				{
					if(count != 0)
						abList.add(count);
				count=0;
				System.out.println(thisDay);
				}
				else
				{
					System.err.println(thisDay);
					count++;
				}
			}
			if(count != 0)
				abList.add(count);
			
			System.out.println(abList+"\n"+Absenct_days);
			int max=0;
			for(int days:abList)
			{
				if(days>max)
					max=days;
			}
			
			if(max>=workingDays.size())
				return 2;
			else if (max>=13)
				return 1;
			return 0;//0:abscence<13 (no refund)
					 //1: >=13 (refund half)
			         //2: all absence (refund all)
		}
		
		public void getDiscountDetails()
		{
			try {
				Connection conn = connect();
				PreparedStatement p = null;
				ResultSet r = null;
				p = conn.prepareStatement("select d.id, t.item_id, d.title,t.price from emp_discount_detail d join emp_discount_type t on d.title = t.title;");
				r = p.executeQuery();
				id_typeItemID = new Hashtable<String, String>();
				while(r.next())
				{
					String a_id = r.getString("id");
					String a_itemID = r.getString("item_id");
					id_typeItemID.put(a_id, a_itemID);
				}
				p = conn.prepareStatement("select * from emp_discount_type");
				r = p.executeQuery();
				type_price = new Hashtable<String, String>();
				while(r.next())
				{
					String a_typeTitle = r.getString("item_id");
					String a_price = r.getString("price");
					type_price.put(a_typeTitle, a_price);
				}
				r.close();
				p.close();
				//conn.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public static String decimalToHex(int value)
		{
			System.out.println("10 to 16 : from " + value);
			String str = "";
			String hexStr = Integer.toHexString(value);
			if(hexStr.length() == 1)
			{
				hexStr = "0"+hexStr;
			}
			System.out.println("10 to 16 : result " + hexStr);
			return hexStr;
		}
		
		public static Connection connect() throws ClassNotFoundException, SQLException
		{
			if(KYMainUI.isInstanced())
				return KYMainUI.bds.getConnection();
			else
			{
			
			
			if(conn!=null && !conn.isClosed())
			{
				return conn;
			}else
			{
				String driver = "com.mysql.jdbc.Driver";
				String url = "jdbc:mysql://192.168.1.100:3307/bb2_test";
				String user = "root"; 
				String password = "root";
		        Class.forName(driver);
		        Connection conn = DriverManager.getConnection(url, user, password);
	        //if(!conn.isClosed()) 
	        	//System.out.println("Succeeded connecting to the Database!");
	        	return conn;
			}
			}
		}
	
	public static void main(String[] args)
	{
		setFees test = new setFees();
	}

}
