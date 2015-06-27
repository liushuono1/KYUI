package FinaceUI.Manege;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bb.common.EmployeeCardVO;
import bb.gui.base.ClientUI;
import KYUI.KYMainUI;
import KYUI.NFCInterface;
import KYUI.NFCreader;

public class paymentUI extends ClientUI implements NFCInterface {
	public JPanel frame;
	public JPanel idPanel;
	public JPanel payPanel;
	public JPanel detaiPanle;
	public List<String> class_mini;
	public List<String> class_small;
	public List<String> class_middle;
	public List<String> class_big;
	public JTextField id_field;
	public List<String> discount_id_list;
	public Hashtable<String, String> id_typeItemID;
	public Hashtable<String, String> type_price;
	public JTextArea area;
	public String output = "";
	public Hashtable<String, String> id_name;
	public List<String> id_list;
	public JTextField payField;
	public String miscellaneousFee;
	public int child_care_fee ;
	public int meal_fee;
	public int refund_fee;
	public int sum;
	public int attend_days; 
	public int regular_fee;
	public List<String> type_list;
	public Hashtable<String, String> typeTitle_typeCode;
	public Hashtable<Integer, String> typeCode_typeTitle;
	public String mealUnitFee;
	public int refund_childCare_type;
	public String hexStr;
	public JButton payBtn;
	public JButton payCurMonBtn;
	public Hashtable<String, String> oneTimeTitle_oneTimePrice;
	public int discountFees;
	public int shouldPay;
	public Font f;
	public String a_childCareFee;
	
	public paymentUI()
	{
		f = new Font("Bold",0,20);
		
		frame = this;
		//frame = new JFrame("缴费");
		int a10 = Toolkit.getDefaultToolkit().getScreenSize().width; // 取得屏幕长度
		int b10 = Toolkit.getDefaultToolkit().getScreenSize().height; // 取得屏幕宽度
		frame.setLocation((a10  / 2)-790/2, (b10  / 2)-700/2); // 设定位置（屏幕中心）
		frame.setSize(790, 700); // 设定大小
		//frame.setResizable(true); // 设定不能缩放
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getAllID();
		getEachClassID();
		getDiscountDeatails();
		frame.setLayout(new BorderLayout());
		frame.add(BorderLayout.NORTH, this.getNorthPanel());
		frame.add(BorderLayout.CENTER, this.getDetailPanel());	
		frame.add(BorderLayout.SOUTH, this.getQuitPanel());
		frame.setVisible(true);
		f = new Font("SimHei",0,30);
	}
	
	public paymentUI(EmployeeCardVO vo)
	{
		f = new Font("Bold",0,20);
		
		frame = this;
		//frame = new JFrame("缴费");
		int a10 = Toolkit.getDefaultToolkit().getScreenSize().width; // 取得屏幕长度
		int b10 = Toolkit.getDefaultToolkit().getScreenSize().height; // 取得屏幕宽度
		frame.setLocation((a10  / 2)-790/2, (b10  / 2)-700/2); // 设定位置（屏幕中心）
		frame.setSize(790, 700); // 设定大小
		//frame.setResizable(true); // 设定不能缩放
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getAllID();
		getEachClassID();
		getDiscountDeatails();
		frame.setLayout(new BorderLayout());
		frame.add(BorderLayout.NORTH, this.getNorthPanel());
		frame.add(BorderLayout.CENTER, this.getDetailPanel());	
		frame.add(BorderLayout.SOUTH, this.getQuitPanel());
		frame.setVisible(true);
		f = new Font("SimHei",0,30);
		checkHandler(vo.getId());
	}
	
	
	public String getTitle()
	{
		return "学生缴费";
	}
	
	public JPanel getQuitPanel()
	{
		JPanel quitP = new JPanel();
		JButton btn = new JButton("退出");
		btn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.hide();
			}
			
		});
		quitP.add(btn);
		return quitP;
	}
	
	public Hashtable<String, String> getAllID()
	{
		id_name = new Hashtable<String, String>();
		id_list = new LinkedList<String>();
		try {
			Connection conn = connect();
			PreparedStatement p = null;
			ResultSet r = null;
			p = conn.prepareStatement("select * from emp_id where security_level = 'level 5'");
			r = p.executeQuery();
			while(r.next())
			{
				String id = r.getString("id");
				String id_lower = id.toLowerCase();
				id_list.add(id);
				id_list.add(id_lower);
				String name = r.getString("name_company_address_book");
				id_name.put(id, name);
				id_name.put(id_lower, name);
			}
			r.close();
			p.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id_name;
	}
	
	public JPanel getNorthPanel()
	{
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(2,1));
		northPanel.add(getIDPanel());
		northPanel.add(getPayPanel());
		return northPanel; 
	}
	
	public JPanel getIDPanel()
	{
		getOneTimeItems();
		idPanel = new JPanel();
		idPanel.setLayout(new GridLayout(1,5));
		JLabel label = new JLabel("学号:");label.setFont(f);
		id_field = new JTextField(8);
		System.out.println(paymentUI.class.getResource("check.png"));
		Icon icon1 = new ImageIcon(paymentUI.class.getResource("check.png"));
		JButton btn1 = new JButton(icon1);
		Icon icon2 = new ImageIcon(paymentUI.class.getResource("card.png"));
		JButton btn2 = new JButton(icon2);
		JButton btn3 = new JButton("查余额");
		btn3.setFont(f);
		btn1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				id_field.setText(id_field.getText().toUpperCase());
				checkHandler(id_field.getText());
				payBtn.setEnabled(true);
				payCurMonBtn.setEnabled(true);
			}
		});

		
		btn2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Thread t = new Thread(new NFCreader(getInstance()));
				t.run();
			}
		});
		
		btn3.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(id_field.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "请输入学号");
				}
				else
				{
					String showMessage = "";
					String id = id_field.getText();
					showMessage += "学号: "+id+"\n";
					showMessage += "姓名: "+ id_name.get(id)+"\n\n";
					int balance = calculateBalance(id_field.getText(), 0);
					if(balance < 0)
					{
						showMessage += "该学生帐户还余下"+(-balance)+"元。";
					}
					else if(balance > 0)
					{
						showMessage += "该学生还有"+balance+"元的欠款。";
					}
					else
					{
						showMessage += "该学生帐户里还余下0元。";
					}
					area.setText(showMessage);
				}
			}
		});
		idPanel.add(label);
		idPanel.add(id_field);
		idPanel.add(btn1);
		idPanel.add(btn2);
		idPanel.add(btn3);
		return idPanel;
	}
	
	private paymentUI getInstance()
	{
		return this;
	}
	
	public void checkHandler(String id)
	{
		sum = 0;
		if(id.equals(""))
		{
			JOptionPane.showMessageDialog(null, "请输入学号");
		}
		if(!id_list.contains(id))
		{
			JOptionPane.showMessageDialog(null, "此学号不存在,请输入正确的学号");
		}
		else
		{
			output = "学号: "+id+"\n";
			output += "姓名: "+ id_name.get(id)+"\n\n";
			int balance = calculateBalance(id,0);
			double sum=0;
			if(balance > 0)
			{
				
				List<String> codeStr_list = readCode(id);
				List<String[]> name_price = decode(codeStr_list);
				for(String[] one_item:name_price)
				{
					output+= one_item[0]+":\t"+one_item[1]+"元";
					System.out.println(one_item[0]+"\t"+one_item[1]);
					int day = 26+Integer.parseInt(one_item[1])/13;
					if(one_item[0].equals("退饭费"))
					{
						output += "（上月出勤 "+day+"天）";
					}
					output += "\n";
					System.err.println("!!!!!!"+one_item[1]);
					if(day == 0)
					{
						if(one_item[0].equals("退饭费"))
						{
							sum+= -340;
						}
						else
						{
							sum+= -Double.parseDouble(a_childCareFee);
						}
						
						//System.err.println("??????"+(-340)+"  sum = "+sum);
					}
					else
					{
						sum+= Integer.parseInt(one_item[1]);
					}
				}
				output += "总计: "+sum+"元\n";
				if((balance-sum)>0)
					output += "该帐户上次缴纳费用后还欠: "+Math.abs(balance-sum)+"元\n";
				else if((balance-sum)<0)
					output += "该帐户上次缴纳费用后余额为: "+Math.abs(balance-sum)+"元\n";
				
				shouldPay = balance;
				output += "实际应缴纳费用: "+ shouldPay+"元";
			}
			else if(balance < 0)
			{
				List<String> codeStr_list = readCode(id);
				List<String[]> name_price = decode(codeStr_list);
				
				for(String[] one_item:name_price)
				{
					output+= one_item[0]+":\t"+one_item[1]+"元\n";
					System.err.println(one_item[0]+"\t"+one_item[1]);
					sum+= Double.parseDouble(one_item[1]);
				}
				output += "\n该学生已交齐费用,且帐户里还余下"+String.valueOf(-balance)+"元。\n";
			}
			else if(balance == 0)
			{
				output += "\n该学生已交齐费用。\n";
			}
		}
		this.area.setFont(new Font("SimHei",0,36));
		this.area.setForeground(Color.BLUE);
		this.area.setText(output);
	}
	
	public List<Object> flagDateTime(String id)
	{
		List<Object> ret = new LinkedList<Object>();
		try {
			Connection conn = connect();
			PreparedStatement p = null;
			ResultSet r = null;
			p = conn.prepareStatement("select * from emp_charge_refund_pay where pay <> '' and id='"+id+"';");
			r = p.executeQuery();
			if(r.last())
			{
				Date date = r.getDate("P_date");
				Time time = r.getTime("P_time");
				ret.add(date);
				ret.add(time);
			}else
			{
				p = conn.prepareStatement("select * from emp_charge_refund_pay where id='"+id+"';");
				r = p.executeQuery();
				if(r.next()){
					Date date = r.getDate("P_date");
					
					Time time = r.getTime("P_time");
					ret.add(date);
					ret.add(time);
				}
				
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public List<String> readCode(String id)
	{
		String code;
		java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
		//String dateStr = currentDate.toString();
		//String year_month = dateStr.substring(0, 7);
		List<String> code_list = new LinkedList<String>();
		Connection conn;
		try {
			conn = connect();
			PreparedStatement p = null;
			p = conn.prepareStatement("select * from emp_charge_refund_pay where id = '"+id+"' and pay<>'';");
			ResultSet r = null;
			r =	p.executeQuery();
			
			int count = 0;
			while(r.next())
			{
				count++;
			}
			if(count > 0)//having code & having payment already
			{
				if(r.last())
				{
					Date date = r.getDate("P_date");
					Time time = r.getTime("P_time");
					p = conn.prepareStatement("select * from emp_charge_refund_pay where (P_date>'"+date+"' or (P_date ='"+date+"' and P_time >?)) and (charge<>'' or refund<>'') and id=?;");
					p.setTime(1, time);
					p.setString(2, id);
					r =	p.executeQuery();
					while(r.next())
					{
						code = r.getString("detail");
						code_list.add(code);
					}
				}
			}
			else if(count == 0)//not having payment yet
			{
				p = conn.prepareStatement("select * from emp_charge_refund_pay where id = '"+id+"';");
				r =	p.executeQuery();
				while(r.next())
				{
					code = r.getString("detail");
					code_list.add(code);
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return code_list;
	}
	
	public int decode(String code_str, String id)
	{
		int ret_fee = 0;
		String chileCare_type = String.valueOf(Integer.parseInt(String.valueOf(code_str.charAt(0)),16));
		String payMeal = String.valueOf(Integer.parseInt(String.valueOf(code_str.charAt(1)),16));
		miscellaneousFee = String.valueOf(Integer.parseInt(code_str.substring(2, 4),16));
		String refundMeal_days = String.valueOf(Integer.parseInt(code_str.substring(4, 6),16));
		String refundChildCare_type = String.valueOf(Integer.parseInt(String.valueOf(code_str.charAt(6)),16));
		String discountType = String.valueOf(Integer.parseInt(String.valueOf(code_str.charAt(7)),16));
		int len = code_str.length();
		System.err.println(code_str);
		System.out.println("chileCare_type: "+chileCare_type);
		if(len >8)
		{
			//--------------------------One-time Fees-------------------------------
			String oneTime_type = code_str.substring(8, 10);
			String binaryStr = Integer.toBinaryString(Integer.parseInt("1"+oneTime_type,16)).substring(1);
			String title = null, cost = null;
			for(int i=0;i<binaryStr.length();i++)
			{
				if(binaryStr.charAt(i)=='1')
				{
					int code = i+1;
					title = typeCode_typeTitle.get(code);//the type_title of the one-time fee
					cost = oneTimeTitle_oneTimePrice.get(title);//get the price of the one-time fee
					ret_fee = Integer.valueOf(cost);
					break;
				}
			}
			output += title+": "+String.valueOf(cost)+"元\n\n";
		}
		else
		{
			//-------------------------child care-----------------------------------
			Hashtable<String, String> type_childCarefees = getFees();//1
			
			String child_care_fee_str = type_childCarefees.get(chileCare_type);
			child_care_fee = Integer.parseInt(child_care_fee_str);
			if(child_care_fee != 0)
			{
				output += "保育费: "+child_care_fee+"元\n\n";
			}
			//-------------------------Meal------------------------------------------
			mealUnitFee = getUnitMealFees();//2
			meal_fee = Integer.parseInt(mealUnitFee)*26;
		
			if(meal_fee != 0)
			{
				output += "饭费: "+meal_fee+"元\n\n";
			}
			if(Integer.valueOf(miscellaneousFee) != 0)
			{
				output += "杂费: "+miscellaneousFee+"元\n\n";
			}
			//-------------------------discount---------------------------------------
			//int discount_type_and_fees = getDiscountFee(discountType, child_care_fee_str);?
			
			discountFees = getDiscountFee(id,  child_care_fee_str, discountType);//get discount fee

			//-------------------------Refund (Meal + ChildCare)------------------------------------------
			refund_fee = getRefundFees(refundMeal_days, child_care_fee, Integer.parseInt(refundChildCare_type));//refund fees 
			ret_fee = child_care_fee + meal_fee + Integer.parseInt(miscellaneousFee) + discountFees - refund_fee;
		}
		return ret_fee;
	}
	
	private List<String[]> decode(List<String> codeList)
	{
		
		List<String[]> ret =new LinkedList<String[]>();
		int[] rule ={1,1,2,2,1,1,2}; 
		for(String code:codeList)
		{
			System.out.println(code);
			if(code.contains("refund:"))
			{
				System.out.println(code);
				String[] tempstr = new String[2];
		    	tempstr[0]=code.split(":")[1];
		    	tempstr[1]="-"+code.split(":")[2];
		    	ret.add(tempstr);
		    	continue;
			}
			
			if(code.contains("charge:"))
			{
				System.out.println(code);
				String[] tempstr = new String[2];
		    	tempstr[0]=code.split(":")[1];
		    	tempstr[1]=code.split(":")[2];
		    	ret.add(tempstr);
		    	continue;
			}
			List<String> cutCode= cutCodebyRule(code,rule);
			//-------------------------child care-----------------------------------
			if(Integer.parseInt(cutCode.get(0),16) != 0)
			{
				Hashtable<String, String> type_childCarefees = getFees();//1
			    String child_care_fee_str = type_childCarefees.get(String.valueOf(Integer.parseInt(cutCode.get(0),16)));
		    	String[] tempstr = new String[2];
		    	tempstr[0]="保育费";
		    	tempstr[1]=child_care_fee_str;
		    	a_childCareFee = tempstr[1];
		    	ret.add(tempstr);
			}
			//-------------------------Meal------------------------------------------
			if(Integer.parseInt(cutCode.get(1),16) != 0)
			{
			   int mealUnitFee = Integer.parseInt(getUnitMealFees());//2
			   int meal_fee = mealUnitFee*26+   (10-(mealUnitFee*26)%10);// make the total sum to 340
		    	String[] tempstr = new String[2];
		    	tempstr[0]="饭费";
		    	tempstr[1]=String.valueOf(meal_fee);
		    	ret.add(tempstr);

			}
			
			if(Integer.parseInt(cutCode.get(2),16) != 0)
			{
		    	String[] tempstr = new String[2];
		    	tempstr[0]="杂费";
		    	tempstr[1]=String.valueOf(Integer.parseInt(cutCode.get(2),16));
		    	ret.add(tempstr);
			}
			int attendDays = 0;
			if(Integer.parseInt(cutCode.get(3),16)-16 != 0)
			{	
				int mealUnitFee= Integer.valueOf(getUnitMealFees());
				int refundDays = Integer.valueOf(cutCode.get(3),16)-16;
				attendDays = 26 - refundDays;
				int refund_meal = -refundDays * Integer.valueOf(mealUnitFee);
				System.err.println("????????????????days  "+cutCode.get(3)+"  "+(Integer.valueOf(cutCode.get(3),16)-16));
				String[] tempstr = new String[2];
				String[] tempstr1 = new String[2];
		    	tempstr[0]="退饭费";
		    	if(attendDays <= 3)
		    	{
		    		tempstr1[0] = "退杂费";
		    		tempstr1[1] = String.valueOf(-(int)(Double.valueOf(miscellaneousFee) - Double.valueOf(miscellaneousFee)/26*attendDays));
		    		
		    		if(attendDays == 0)
		    		{
		    			tempstr[1]=String.valueOf(-340);
		    		}
		    	}
		    	else
		    	{
		    		tempstr[1]=String.valueOf(refund_meal);
		    	}
		    	ret.add(tempstr);
		    	if(attendDays <= 3)
		    	{
		    		ret.add(tempstr1);
		    	}
			}
			//-------------------------discount---------------------------------------
			int discount=0;
			if(Integer.parseInt(cutCode.get(5),16) != 0)
			{
				Hashtable<String, String[]> discount_list= getLongtermDiscountType();
				String[] tempstr = discount_list.get(String.valueOf(Integer.parseInt(cutCode.get(5),16)));
				if(Integer.parseInt(cutCode.get(5),16)==3 )
				{
					Hashtable<String, String> type_childCarefees = getFees();
					tempstr[1]="-"+type_childCarefees.get(String.valueOf(Integer.parseInt(cutCode.get(0),16)));
				}
				
					discount=Integer.parseInt(tempstr[1]);
					if(Integer.parseInt(cutCode.get(5),16)==4 )  //extra magage fee dosen't count as discount
						discount=0;
		    	ret.add(tempstr);
			}
			
			//-------------------------Refund (Meal + ChildCare)------------------------------------------
			if(Integer.parseInt(cutCode.get(4),16) != 0)
			{	
				Hashtable<String, String> type_childCarefees = getFees();
				double child_care_fee = Integer.parseInt(type_childCarefees.get(String.valueOf(Integer.parseInt(cutCode.get(0),16))));
				//-----------------correct childCarefees with discount----------------
				child_care_fee =child_care_fee +discount;
				
				double refund_childCare; 
				if(attendDays <= 3)
				{
					refund_childCare = -child_care_fee + child_care_fee/26 * attendDays;
					System.err.println("attendDays = "+attendDays+"  child_care_fee = "+child_care_fee+" "+child_care_fee/26 * attendDays);
				}
				else
				{
					refund_childCare = -child_care_fee * Integer.parseInt(cutCode.get(4),16) /2;
				}
				String[] tempstr = new String[2];
		    	tempstr[0]="退保育费";
		    	tempstr[1]=String.valueOf((int)refund_childCare);
		    	ret.add(tempstr);
			}
			//-------------------------one time discount---------------------------------------
			if(cutCode.size()>6)
			{
				if(Integer.parseInt(cutCode.get(6),16) != 0)
				{
					getOneTimeItems();
					String binaryStr = Integer.toBinaryString(Integer.parseInt("1"+cutCode.get(6),16)).substring(1);
					String title = null, cost = null;
					
					for(int i=0;i<binaryStr.length();i++)
					{
						if(binaryStr.charAt(i)=='1')
						{
							int codE = i+1;
							title = typeCode_typeTitle.get(codE);//the type_title of the one-time fee
							cost = oneTimeTitle_oneTimePrice.get(title);//get the price of the one-time fee
							String[] tempstr = new String[2];
					    	tempstr[0]=title;
					    	tempstr[1]=cost;
					    	ret.add(tempstr);
						}
					}
				}
			}
		}
		return ret;
	}
	
	private List<String> cutCodebyRule(String code,int[] rule)
	{
		List<String> ret = new LinkedList<String>();
		int strlocal=0;
		for(int len : rule)
		{
			ret.add(code.substring(strlocal,strlocal+len));
			strlocal+=len;
			if(strlocal==code.length())
				break;
		}
		return ret;
	}
	
	public void chargeRegularFee(String id)
	{
		try {
			Connection conn = connect();
			PreparedStatement p = null;
			ResultSet r = null;
			p = conn.prepareStatement("select * from emp_charge_refund_pay where id = '"+id+"';");
			r = p.executeQuery();
			String balance = null;
			int balance_int = 0;
			List<List> all_payItems = new LinkedList<List>();
			while(r.next())
			{
				List<String> a_payItem = new LinkedList<String>();
				String date = r.getString("P_date");
				String chargeItem = r.getString("detail");//P_typeTitle
				a_payItem.add(date);
				a_payItem.add(chargeItem);
				all_payItems.add(a_payItem);
			}
			java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
	
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(new Date().getTime());
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = dateFormat.format(c.getTime());
			
			List<String> regularItem = new LinkedList<String>();
			regularItem.add(dateStr);//date
			regularItem.add("regular");//item title
			if(!all_payItems.contains(regularItem))//check whether this "charge item" has been inseted into database or not
			{
				if(r.last())
				{
					balance = r.getString("balance");
					if(balance != null)
					{
						balance_int = Integer.parseInt(balance);
					}
					else
					{
						balance_int = 0;
					}
				}
				balance_int += regular_fee;
				p = conn.prepareStatement("insert into emp_charge_refund_pay values (?,?,?,?,?,?,?,?);");
				Time currentTime = new Time(System.currentTimeMillis());
				p.setString(1, id);
				p.setDate(2, currentDate);
				p.setTime(3, currentTime);
				p.setString(4, "regular");
				p.setString(5, "");
				p.setString(6, String.valueOf(regular_fee));
				p.setString(7, "");
				p.setString(8, String.valueOf(balance_int));
				//p.setString(9, "");
				p.execute();
			}
			r.close();
			p.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doRefund(String id, int refund)
	{
		try {
			Connection conn = connect();
			PreparedStatement p = null;
			ResultSet r = null;
			p = conn.prepareStatement("select * from emp_charge_refund_pay where id = '"+id+"';");
			r = p.executeQuery();
			String balance = null;
			int balance_int = 0;
			List<List<String>> all_refundItems = new LinkedList<List<String>>();
			while(r.next())
			{
				List<String> a_payItem = new LinkedList<String>();
				String date = r.getString("P_date");
				String refund_money = r.getString("refund");
				a_payItem.add(date);
				a_payItem.add(refund_money);
				all_refundItems.add(a_payItem);
			}
			java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
			List<String> currentRefund = new LinkedList<String>();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(new Date().getTime());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = dateFormat.format(c.getTime());
			currentRefund.add(dateStr);//date
			currentRefund.add(String.valueOf(refund));//item title
			if(!all_refundItems.contains(currentRefund))//check whether this "charge item" has been inseted into database or not
			{
				if(r.last())
				{
					balance = r.getString("balance");
					if(balance != null)
					{
						balance_int = Integer.parseInt(balance);
					}
					else
					{
						balance_int = 0;
					}
				}
				p = conn.prepareStatement("insert into emp_charge_refund_pay values (?,?,?,?,?,?,?,?);");
				Time currentTime = new Time(System.currentTimeMillis());
				p.setString(1, id);
				p.setDate(2, currentDate);
				p.setTime(3, currentTime);
				p.setString(4, "退保育+退饭费");
				p.setString(5, String.valueOf(refund));
				p.setString(6, "");
				p.setString(7, "");
				p.setString(8, String.valueOf(balance_int - refund));
				//p.setString(9, "");
				p.execute();
			}
	
			r.close();
			p.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pay(String id, String pay_money)
	{
		try {
			Connection conn = connect();
			PreparedStatement p = null;
			//ResultSet r = null;
			
			//p = conn.prepareStatement("select * from emp_charge_refund_pay where id = '"+id+"';");
			//r = p.executeQuery();
			p = conn.prepareStatement("insert into emp_charge_refund_pay values (?,?,?,?,?,?,?,?);");
			java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
			Time currentTime = new Time(System.currentTimeMillis());
			p.setString(1, id);
			p.setDate(2, currentDate);
			p.setTime(3, currentTime);
			p.setString(4, "");
			p.setString(5, "");
			p.setString(6, "");
			p.setString(7, pay_money);
			p.setString(8, "");
			//p.setString(9, "clear");
			p.execute();
			//r.close();
			p.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int checkBalance(String id)
	{
		String balance = null;
		int balance_int = 0;
		try {
			Connection conn = connect();
			PreparedStatement p = null;
			ResultSet r = null;
			p = conn.prepareStatement("select * from emp_charge_refund_pay where id = '"+id+"';");
			r = p.executeQuery();
			if(r.last())
			{
				balance = r.getString("balance");
			}
			balance_int = Integer.parseInt(balance);
			
			r.close();
			p.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return balance_int;
	}
	
	/*
	 * This function return the "discount fees" if having one
	 */
	public int getDiscountFee(String id, String child_care_str, String discountType)
	{
		int fee = 0;
		//String discountTypeID = null;
		if(discount_id_list.contains(id))
		{
			//discountTypeID = id_typeItemID.get(id);//get discount type (i.e., 0,1,2,3,4)
			if(discountType.equals("3"))
			{
				fee  = -Integer.parseInt(child_care_str);
			}
			else
			{
				String price = type_price.get(discountType);
				fee = Integer.parseInt(price);
			}
		}
		if(fee != 0)
		{
			if(fee < 0)
			{
				this.output += "优惠: "+(0-fee)+"元\n\n";
			}else if(fee > 0){
				this.output += "另收回民宝宝费用: "+(0-fee)+"元\n\n";
			}
		} 
		return fee;
	}
	
	public void getDiscountDeatails()
	{
		try {
			Connection conn = connect();
			PreparedStatement p = null;
			ResultSet r = null;
			p = conn.prepareStatement("select d.id, t.item_id, d.title,t.price from emp_discount_detail d join emp_discount_type t on d.title = t.title;");
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
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getRefundFees(String refundMeal_days, int child_care_fee, int refund_childCare_type)
	{
		
		int refund_meal = Integer.valueOf(refundMeal_days) * Integer.valueOf(mealUnitFee);
		
		int refund_childCare = child_care_fee * refund_childCare_type /2; 
		System.out.println("refund_childCare = "+refund_childCare+" child_care_fee = "+child_care_fee+" refund_childCare_type = "+refund_childCare_type);
		int refund_fees = refund_meal + refund_childCare;  
		output += "上月退款:\n";
		if(refund_childCare == 0)
		{
			output += "    退饭费： "+refund_meal+"元 ("+refundMeal_days+"天)\n\n";
		}
		else
		{
			System.out.println("?????????????");
			output += "    退饭费： "+refund_meal+"元 （"+refundMeal_days+"天)\n"+"    退保育费: "+refund_childCare+"元\n\n";
		}
		return refund_fees;
	}
	/*
	 * 
	 */
	public int continueAbsence(List<String> come_days, List<String> workingDays)
	{
		List<Integer> abList = new LinkedList<Integer>();
		int count=0;
		for(String thisDay:workingDays)
		{
			if(come_days.contains(thisDay))
			{
				if(count != 0)
					abList.add(count);
			count=0;
			}
			else
			{
				count++;
			}
		}
		if(count != 0)
			abList.add(count);
		
		
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
		return 0;//0:abscence<13  1: >=13 2: all abssence
	}
	
	public int Gap(String come_date, String next_come_date, List<String> offDays)
	{
		int count = 0;
		while(validDate(come_date, next_come_date))//date1 < date2
		{
			come_date = this.nextDate1(come_date);
			if(!offDays.contains(come_date))
			{
				count++;
			}
		}
		return count;
	}
	
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
	
	public List<String> onDuty(String date)
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
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return onDuty_id_list;
	}
	
	/*
	 * This method returns the 1st & last date of the specified "Month"
	 */
	public String[] lastDate(int year, int month)
	{
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);//某一月(1月份是0));
		int end=calendar.getActualMaximum(calendar.DAY_OF_MONTH);
		int begin=calendar.getActualMinimum(calendar.DAY_OF_MONTH);
		String begin_date = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(begin);
		String end_date = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(end);
		String[] ret = new String[2];
		ret[0] = begin_date;
		ret[1] = end_date;
		return ret;
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
	
	public String getPreviousYear_month(String date)
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
	
	public String getUnitMealFees()
	{
		String unit_meal_fees = null;
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
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return unit_meal_fees;
	}
	
	/*
	 * Get the fees of each class-level
	 */
	public Hashtable<String, String> getFees(){
		List<String> all_fees = new LinkedList<String>();
		Hashtable<String, String> type_fees = new Hashtable<String, String>();
		try {
			Connection conn = connect();
			PreparedStatement p = null;
			ResultSet r = null;
			p = conn.prepareStatement("select * from emp_child_care_fees;");
			r = p.executeQuery();
			r.next(); String fees_mini = r.getString("price");
			all_fees.add(fees_mini);//0: add mini
			type_fees.put("1", fees_mini);
			
			r.next(); String fees_small = r.getString("price");
			all_fees.add(fees_small);//1: add small
			type_fees.put("2", fees_small);
			
			r.next(); String fees_middle = r.getString("price");
			all_fees.add(fees_middle);//2: add middle
			type_fees.put("3", fees_middle);
			
			r.next(); String fees_big = r.getString("price");
			all_fees.add(fees_big);//3: add big
			type_fees.put("4", fees_big);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return type_fees;
	}
	public Hashtable<String, String[]> getLongtermDiscountType()
	{
		Hashtable<String, String[]> discount_type = new Hashtable<String, String[]>();
		try {
			Connection conn = connect();
			PreparedStatement p = null;
			ResultSet r = null;
			p = conn.prepareStatement("SELECT * FROM bb2_test.emp_discount_type;");
			r = p.executeQuery();
			while(r.next())
			{
				discount_type.put(r.getString("item_id"),new String[]{r.getString("title"),r.getString("price")});
			}
			
			r.close();
			p.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return discount_type;
	}
	
	
	public void getEachClassID()
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
			pstmt = conn.prepareStatement("select * from emp_id where department = '宝一班' or department = '宝二班'");
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				id = rs.getString("id");
				class_mini.add(id);
			}
			
			pstmt = conn.prepareStatement("select * from emp_id where department = '小一班' or department = '小二班'");
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				id = rs.getString("id");
				class_small.add(id);
			}
			
			pstmt = conn.prepareStatement("select * from emp_id where department = '中班'");
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				id = rs.getString("id");
				class_middle.add(id);
			}
			
			pstmt = conn.prepareStatement("select * from emp_id where department = '大班'");
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				id = rs.getString("id");
				class_big.add(id);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JPanel getPayPanel()
	{
		payPanel = new JPanel();
		payPanel.setLayout(new GridLayout(1,5));
		JLabel label = new JLabel("缴费:");label.setFont(f);
		payField = new JTextField(8);
		payBtn = new JButton("提交金额");
		payBtn.setFont(f);
		//payBtn.setEnabled(false);
		payBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				//int balance = calculateBalance(id_field.getText());
				String id = id_field.getText();
				if(id.equals(""))
				{
					JOptionPane.showMessageDialog(null, "请输入学号");
				}
				else
				{
					payHandler();
				}
			}		
		});
		payCurMonBtn = new JButton("交齐本月全额");
		payCurMonBtn.setFont(f);payCurMonBtn.setBackground(Color.cyan);
		//payCurMonBtn.setEnabled(false);
		payCurMonBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				payCurrentMonthHandler();
			}		
		});
		
		payPanel.add(label);
		payPanel.add(payField);
		payPanel.add(payBtn);
		payPanel.add(payCurMonBtn);
		payPanel.add(new JLabel(""));
		return payPanel;
	}
	
	public void payHandler()
	{
		String id = id_field.getText();
		String name = this.id_name.get(id);
		String moneyPay = payField.getText();
		String showMessage = "";
		if(id.equals("") && moneyPay.equals(""))
		{
			JOptionPane.showMessageDialog(null, "请输入学号和缴费金额");
		}
		else if(id.equals(""))
		{
			JOptionPane.showMessageDialog(null, "请输入学号");
		}
		else if(moneyPay.equals(""))
		{
			JOptionPane.showMessageDialog(null, "请输入缴费金额");
		}
		else
		{
			pay(id, moneyPay);
			payField.setText("");
			showMessage += "学号: "+id+"\n";
			showMessage += "姓名: "+name+"\n\n";
			showMessage += "该学生缴费"+moneyPay+"元";
			area.setText(showMessage);//clean up the output area
		}
	}
	
	public void payCurrentMonthHandler()
	{
		String id = id_field.getText();
		if(id.equals(""))
		{
			JOptionPane.showMessageDialog(null, "请输入学号");
		}
		else
		{
			int balance = calculateBalance(id_field.getText(), 0);
			String showMessage = "";
			showMessage += "学号: "+id+"\n";
			showMessage += "姓名: "+id_name.get(id)+"\n\n";
			if(balance <= 0)//we owe him/her some money
			{
				int payMoney =0;
				pay(id, String.valueOf(payMoney));
				if(balance==0)
					showMessage += "该学生已交齐费用，本月无需再交任何费用。";
				else
					showMessage += "该学生已交齐费用,帐户上还有余额"+Math.abs(balance)+"元。";
			}
			else if(balance > 0)
			{
				pay(id, String.valueOf(shouldPay));
				showMessage += "该学生已交齐本月费用: "+ shouldPay+"元。";
			}
			area.setText(showMessage);
		}
	}
	
	public int calculateBalance(String id, int flag)
	{
		int balance = 0;
		List<Object> dateTime = flagDateTime(id);
		if(dateTime.size()==0)
		{
			JOptionPane.showMessageDialog(null, "还没有此人的缴费记录");
			return 0;
		}
		Date date = (Date) dateTime.get(0);
		Time time = (Time) dateTime.get(1);
		
	
		String dateStr = String.valueOf(date);
		String timeStr = String.valueOf(time);
		try {
			Connection conn = connect();
			PreparedStatement p = null;
			ResultSet r = null;
			if(flag == 1)
			{
				p = conn.prepareStatement("select sum(charge) as sum_charge from emp_charge_refund_pay where " +
						"(P_date <'"+dateStr+"' or P_date = '"+dateStr+"') and id=?;");
				p.setString(1, id);
			}
			else
			{
				p = conn.prepareStatement("select sum(charge) as sum_charge from emp_charge_refund_pay where id=?;");
				p.setString(1, id);
			}
			r = p.executeQuery();
			int charge_int=0; 
			if(r.next())
			{
				String charge = r.getString("sum_charge");
				if(charge!=null)
					charge_int = Integer.parseInt(charge);
			}
			
			if(flag == 1)
			{
				p = conn.prepareStatement("select sum(pay) as sum_pay from emp_charge_refund_pay where " +
						"(P_date <'"+dateStr+"' or P_date = '"+dateStr+"') and (P_time < '"+timeStr+"' or P_time = '"+timeStr+"') and id=?;");
				p.setString(1, id);
			}
			else
			{
				p = conn.prepareStatement("select sum(pay) as sum_pay from emp_charge_refund_pay where id=?;");
				p.setString(1, id);
			}
			r = p.executeQuery();
			int pay_int =0;
			if(r.next())
			{
				String pay = r.getString("sum_pay");
				if(pay!=null)
				pay_int = Integer.parseInt(pay);	
			}
			if(flag == 1)
			{
				p = conn.prepareStatement("select sum(refund) as sum_refund from emp_charge_refund_pay where " +
						"(P_date <'"+dateStr+"' or P_date = '"+dateStr+"') and (P_time < '"+timeStr+"' or P_time = '"+timeStr+"') and id=?;");
				p.setString(1, id);
			}
			else
			{
				p = conn.prepareStatement("select sum(refund) as sum_refund from emp_charge_refund_pay where id=?;");
				p.setString(1, id);
			}
			r = p.executeQuery();
			int refund_int =0;
			if(r.next()){
				String refund = r.getString("sum_refund");
				if(refund!=null)
				refund_int = Integer.parseInt(refund);
			}
			balance = charge_int - refund_int - pay_int;
			r.close();
			p.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return balance;
	}
	
	public void getOneTimeItems()
	{
		type_list = new LinkedList<String>();
		typeTitle_typeCode = new Hashtable<String, String>();
		typeCode_typeTitle = new Hashtable<Integer, String>();
		oneTimeTitle_oneTimePrice = new Hashtable<String, String>();
		try {
			Connection conn = connect();
			PreparedStatement p = null;
			ResultSet r = null;
			p = conn.prepareStatement("select * from emp_one_time_type where type_code like '0%';");
			r = p.executeQuery();
			while(r.next())
			{
				String a_code = r.getString("type_code");
				String a_type = r.getString("title");
				String a_price = r.getString("price");
				type_list.add(a_type);
				typeTitle_typeCode.put(a_type, a_code);
				typeCode_typeTitle.put(Integer.valueOf(a_code), a_type);
				oneTimeTitle_oneTimePrice.put(a_type, a_price);
			}
			r.close();
			p.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JScrollPane getDetailPanel()
	{
		detaiPanle = new JPanel();
		detaiPanle.setBorder(BorderFactory.createTitledBorder("缴费明细"));
		area = new JTextArea();
		area.setEditable(false);
		area.setLineWrap(true);
		
		area.setFont( new Font("标楷体",Font.BOLD,25));
		area.setBackground(Color.LIGHT_GRAY);
		JScrollPane jsp = new JScrollPane(area);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		detaiPanle.add(jsp);
		return jsp; 
	}
	
	public Connection connect() throws ClassNotFoundException, SQLException
	{
		/*String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://192.168.1.100:3307/bb2_test";
		String user = "root"; 
		String password = "root";
        Class.forName(driver);*/
		
      //  Connection conn = DriverManager.getConnection(url, user, password);
        
        Connection conn = KYMainUI.bds.getConnection();
        //if(!conn.isClosed()) 
        	//System.out.println("Succeeded connecting to the Database!");
        	return conn;
	}
		
	public static boolean checkOpenMachine()
	{
		boolean flag = true;
		/*String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://192.168.1.100:3307/bb2_test";
		String user = "root"; 
		String password = "root";*/
        try {
			//Class.forName(driver);
			Connection conn = KYMainUI.bds.getConnection();//DriverManager.getConnection(url, user, password);
			java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
			PreparedStatement p = null;
			ResultSet r = null;
			String curMonth= date.getYear()+"-"+date.getMonth();
			p = conn.prepareStatement("select * from operatingLog where o_month=?;");
			p.setString(1, curMonth);
			r = p.executeQuery();
			int times;
			if(r.next())
			{
				times = r.getInt("times");
				times += 1;
				p = conn.prepareStatement("update operatingLog set times = ? where o_month=?;");
				p.setInt(1, times);
				p.setString(2, curMonth);
				p.executeUpdate();
			}
			else
			{
				times = 0;
				
				p = conn.prepareStatement("insert into operatingLog values(?, ?);");
				p.setString(1, curMonth);
				p.setInt(2, times);
				p.execute();
			}
			if(times == 0)
			{
				flag = false;
			}
			
			r.close();
			p.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return flag;
	}
	

	@Override
	public void NFCAction(String[] prams) {
		// TODO Auto-generated method stub
		String id = null ;
		try {
			Connection conn = connect();
			PreparedStatement p = null;
			p = conn.prepareStatement("select * from emp_nfcid where id_nfc = ?");
			p.setString(1, prams[0]);
			ResultSet r = p.executeQuery();
			
			if(r.next())
			{
				id = r.getString("id");
			}
			else
			{
				JOptionPane.showMessageDialog(null, "没有对应的帐户！");
				r.close();
				p.close();
				conn.close();
				return;
			}
			r.close();
			p.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.id_field.setText(id.toUpperCase());
		this.checkHandler(id.toUpperCase());
	}
	
	public static void main(String[] args)
	{
		paymentUI test = new paymentUI();
	}
}
