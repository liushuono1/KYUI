package Simple.SQL;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Time;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import AuthModule.AuthLV1;
import AuthModule.CardAuth;
import KYUI.KYMainUI;
import bb.common.EmployeeCardVO;
import bb.gui.ServerActionException;
import bb.gui.base.ClientUI;
import bb.gui.server.HRServerActionManager;

public class SQLTableUImm extends ClientUI implements AuthLV1{
	static SQLTableUImm instance = null;
	private String passDate="";
	private JFrame f;
	private JTextField dateF;
	private JTextField unitPriceF;
	private JTextField amountF;
	private JTextField detailF;
	private JTextField expenseF;
	private JTextField incomeF;
	private JComboBox jb;
	private JComboBox jb_account;
	private JTextField commentF;
	private JTable table;
	private List<OneRecord> record_list =new LinkedList<OneRecord>();
	private List<OneRecord> delete_list =new LinkedList<OneRecord>();
	private JButton confirmBtn;
	public CheckboxGroup cbg;
	private String[] attributeArray;
	private OneRecord currentRecord;
	private Checkbox cb_expense;
	private Checkbox cb_income;
	private List<String> mark_list;
	
	public SQLTableUImm()
	{
		if(CardAuth.ID_Auth(this, 0)==0)
			this.dispose();;
		this.setLayout(new BorderLayout());
		this.add(BorderLayout.NORTH, getInputPanel());
		this.add(BorderLayout.CENTER,this.getShowPanel());
		this.add(BorderLayout.SOUTH, btnPanel());
		currentRecord = new OneRecord();
	}
	
	public SQLTableUImm(List<OneRecord> itemList)
	{
		this.record_list.addAll(itemList);
	    if(record_list.size() != 0)
	    {
			currentRecord =record_list.get(0);
			this.setLayout(new BorderLayout());
			this.add(BorderLayout.NORTH, getInputPanel());
			this.add(BorderLayout.CENTER,this.getShowPanel());
			this.add(BorderLayout.SOUTH, btnPanel());
			mark_list = new LinkedList<String>();
			for(int i=0;i<record_list.size();i++)
			{
				mark_list.add(record_list.get(i).getMark());
			}
	    }
	    else
	    {
	    	JOptionPane.showMessageDialog(null, "没有相应的记录");
	    }
	}
	
	
	public JPanel getShowPanel()
	{
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("显示录入信息"));
		panel.setLayout(new BorderLayout());
		JTable table = createTable();
		
		//table.setSize(panel.getWidth(), panel.getHeight());
		JScrollPane ScrollBtnPanel = new JScrollPane(table);
		ScrollBtnPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		ScrollBtnPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BorderLayout());
		JButton cancelBtn = new JButton("删除记录");
		cancelBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cancelHandler();
			}
			
		});
		
		btnPanel.add(BorderLayout.EAST, cancelBtn);
		panel.add(ScrollBtnPanel);
		panel.add(BorderLayout.SOUTH, btnPanel);
		return panel;
	}
	
	public void cancelHandler()
	{
		int row = table.getSelectedRow();
		if(row == -1)
		{
			JOptionPane.showMessageDialog(null, "必须选中要删除的记录");
		}
		else
		{
			this.record_list.get(row).setMark("222222222");
			this.delete_list.add(this.record_list.get(row));
			this.record_list.remove(row);
			int rows = record_list.size();
			int columns = attributeArray.length;
			Object[][] records = new Object[rows][columns];
			for(int i=0;i<rows;i++)
			{
				OneRecord r = record_list.get(i);
				for(int j=0;j<columns;j++)
				{
					records[i][j] = r.getString(j+1);//row*column
				}	
			}
			table.setModel(MutliTableModel(records, attributeArray));
		}
	}
	
	public void cancelAllRecords()
	{
		int row = table.getRowCount();
		for(int i=0;i<row;i++)
		{
			record_list.remove(i);
		}
		int rows = record_list.size();
		 int columns = attributeArray.length;
		 Object[][] records = new Object[rows][columns];
		 for(int i=0;i<rows;i++)
		 {
			 OneRecord r = record_list.get(i);
			 for(int j=0;j<columns;j++)
			 {
				 records[i][j] = r.getString(j+1);//row*column
		     }	
		 }
		 table.setModel(MutliTableModel(records, attributeArray));
	}
	
	public void deleteFields()
	{
		this.dateF.setText("");
		this.detailF.setText("");
		this.commentF.setText("");
		this.unitPriceF.setText("");
		this.amountF.setText("");
		this.expenseF.setText("");
		this.incomeF.setText("");
		this.jb.setSelectedItem("");
		this.jb_account.setSelectedItem("");
	}
	/*
	public JPanel getSubmitPanel()
	{
		JPanel btnPanel = new JPanel();
		JButton submitBtn = new JButton("提交");
		submitBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				submitHandler();
				System.err.println("record_list: "+record_list);
			}
			
		});
		btnPanel.add(submitBtn);
		return btnPanel;
	}*/
	private String combineMark(String mark1,String mark2)
	{
		String Ret="";
		for(int i=0;i<mark1.length();i++)
		{
			if(mark1.charAt(i)=='1' || mark2.charAt(i)=='1')
			{
				Ret+="1";
			}else
			{
				Ret+="0";
			}
		}
		
		return Ret;
	}
	
	
	public void submitHandler()
	{
		if(CardAuth.ID_Auth(this, 2)!=1)
			return;
		List<OneRecord> submit_list = new LinkedList<OneRecord>();
		System.out.println(mark_list);
		for(int i=0;i<record_list.size();i++)
		{
			if(!record_list.get(i).getMark().equals("000000000"))
			{
				record_list.get(i).setMark(combineMark(mark_list.get(i),record_list.get(i).getMark()));
				submit_list.add(record_list.get(i));
			}
		}
		String OP_ID;
		try{
			OP_ID = HRServerActionManager.getInstance().getEmployeeSelf().getId();	
		}catch(Exception e)
		{
			return;
		}
		
		System.out.println("submit List---->"+submit_list);
		if(submit_list.size()>0)
		     FinanceUtil.update(submit_list, OP_ID);
		
		if(this.delete_list.size()>0)
		{
			FinanceUtil.delete(delete_list, OP_ID);
		}
		dispose();
	}
	
	public JPanel getInputPanel()
	{
		confirmBtn = new JButton("确定");
		//expenseF = new JTextField(8);
		incomeF = new JTextField(8);
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("添加信息"));
		panel.setLayout(new GridLayout(5,2));
		
		JPanel dateP = new JPanel();
		dateP.setLayout(new GridLayout(1,2));
		JLabel dateL = new JLabel("选择日期(*):");
		JButton btn = new JButton("日历");
		dateP.add(dateL);
		dateP.add(btn);
		dateF = new JTextField(5);
		btn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				newFrame(dateF);
			}
		});
		panel.add(dateP);
		panel.add(dateF);
		
		JLabel unitPriceL = new JLabel("单价(元)(*):");
		unitPriceF = new JTextField(5);
		panel.add(unitPriceL);
		panel.add(unitPriceF);
		
		JLabel typeL = new JLabel("类目(*):");
		jb = new JComboBox();
		String types = txt2String("/FinanceUI/types.txt");
		String[] typeArray = types.trim().split(":");
		jb.addItem("");
		for(int i=0;i<typeArray.length;i++)
		{
			jb.addItem(typeArray[i]);
		}
		panel.add(typeL);
		panel.add(jb);
		
		JLabel amountL = new JLabel("数量(选填):");
		amountF = new JTextField(5);
		panel.add(amountL);
		panel.add(amountF);
		
		JLabel detailL = new JLabel("详情(*):");
		detailF = new JTextField(5);
		panel.add(detailL);panel.add(detailF);
		
		cbg = new CheckboxGroup(); //定义组 
		cb_expense = new Checkbox("支出(元):", cbg, true);
		//expenseF.setEditable(false);
		incomeF.setEditable(false);
		cb_income = new Checkbox("收入(元):", cbg, false);
		cb_expense.setName("expense");
		cb_income.setName("income");
		
		ItemListener it= new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(((Checkbox)e.getSource()).getName().equals("expense"))
				{
					incomeF.setText("");
					incomeF.setEditable(false);
					expenseF.setEditable(true);
				}
				else if(((Checkbox)e.getSource()).getName().equals("income"))
				{
					expenseF.setText("");
					expenseF.setEditable(false);
					incomeF.setEditable(true);
				}
			}
		};
		cb_expense.addItemListener(it);
		cb_income.addItemListener(it);
		expenseF = new JTextField(8);
		panel.add(cb_expense);
		panel.add(expenseF);
		
		JLabel accountL = new JLabel("账户类型:");
		jb_account = new JComboBox();
		jb_account.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				confirmBtn.setEnabled(true);
			}
			
		});
		jb_account.addItem("");
		jb_account.addItem("现金");
		jb_account.addItem("银行卡");
		panel.add(accountL);
		panel.add(jb_account);
		
		//incomeF = new JTextField(8);
		panel.add(cb_income);
		panel.add(incomeF);
		
		JLabel commentL = new JLabel("相关说明:");
		commentF = new JTextField(15);
		panel.add(commentL);
		panel.add(commentF);
		
		JPanel confirmP = new JPanel();
		confirmP.setLayout(new GridLayout(1,4));
		//confirmBtn = new JButton("确定");??
		//record_list = new LinkedList<OneRecord>();
		confirmBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(dateF.getText().equals(""))//|| detailF.getText().equals("") || commentF.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "请选择日期");
				}
				else if(detailF.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "请输入详情");
				}
				else if(unitPriceF.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "请输入价钱");
				}
				else if(cb_expense.getState()!=true && cb_income.getState()!=true)
				{
					JOptionPane.showMessageDialog(null, "请确定此笔记录是支出或收入");
				}
				else if(!amountF.getText().equals(""))
				{
					if(!isNumeric(amountF.getText()))
					{
						JOptionPane.showMessageDialog(null, "数量应该为数字");
					}
				}
				
				
				if(!dateF.getText().equals("") && !detailF.getText().equals("") && !unitPriceF.getText().equals(""))
				{
					if(!isNumeric(unitPriceF.getText()))
					{
						JOptionPane.showMessageDialog(null, "价格应该为数字");
					}
					else 
					{
						if(cb_expense.getState()==true)
						{
							if(!expenseF.getText().equals(""))
							{
								if(!isNumeric(expenseF.getText()))
								{
									JOptionPane.showMessageDialog(null, "支出应为数字");
								}
								else
								{
									double unitD = Double.parseDouble(unitPriceF.getText());
									double amountD = 0;
									if(!amountF.getText().equals(""))
									{
										amountD = Double.parseDouble(amountF.getText());
									}
									else
									{
										amountD = 1.0;
									}
									System.err.println(unitD*amountD+"  "+Double.parseDouble(expenseF.getText()));
									if(unitD*amountD == Double.parseDouble(expenseF.getText()))
									{
										confirmHandler();
										confirmBtn.setEnabled(false);
										cancelInput();
										//currentRecord = new OneRecord();
									}
									else
									{
										JOptionPane.showMessageDialog(null, "单价与数量的乘积与支出不符");
									}
									
								}
							}
							else
							{
								JOptionPane.showMessageDialog(null, "请输入支出的金额");
							}
						}
						else if(cb_income.getState()==true)
						{
							if(!incomeF.getText().equals(""))
							{
								if(!isNumeric(incomeF.getText()))
								{
									JOptionPane.showMessageDialog(null, "收入应为数字");
								}
								else
								{

									double unitD = Double.parseDouble(unitPriceF.getText());
									double amountD = 0;
									if(!amountF.getText().equals(""))
									{
										amountD = Double.parseDouble(amountF.getText());
									}
									else
									{
										amountD = 1.0;
									}
									System.err.println(unitD*amountD+"  "+Double.parseDouble(incomeF.getText()));
									if(unitD*amountD == Double.parseDouble(incomeF.getText()))
									{
										confirmHandler();
										confirmBtn.setEnabled(false);
										cancelInput();
									}
									else
									{
										JOptionPane.showMessageDialog(null, "单价与数量的乘积与收入不符");
									}
								}
							}
							else
							{
								JOptionPane.showMessageDialog(null, "请输入收入的金额");
							}
						}
					}
				}
			}
		});
		JButton cancelBtn = new JButton("取消");
		cancelBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cancelInput();
			}
			
		});
		confirmP.add(new JLabel());
		confirmP.add(new JLabel());
		confirmP.add(new JLabel());
		confirmP.add(confirmBtn);
		//confirmP.add(cancelBtn);
		
		panel.add(new JLabel());
		panel.add(confirmP);
		return panel;
	}
	
	public JPanel btnPanel()
	{
		JPanel panel = new JPanel();
		JButton updateBtn = new JButton("提交");
		updateBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				submitHandler();
			}
			
		});
		JButton deleteBtn = new JButton("取消");
		deleteBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
			
		});
		panel.add(updateBtn);
		panel.add(deleteBtn);
		return panel;
	}

	
	public  boolean isNumeric(String str){
		  for (int i = 0; i < str.length(); i++){
		   System.out.println(str.charAt(i));
		   if(!Character.isDigit(str.charAt(i)) && str.charAt(i)!='.')
		   {
			   return false;
		   }
		  }
		  return true;
		 }

	
	public void cancelInput()
	{
		this.dateF.setText("");
		this.detailF.setText("");
		this.commentF.setText("");
		this.unitPriceF.setText("");
		this.amountF.setText("");
		this.expenseF.setText("");
		this.incomeF.setText("");
		this.jb.setSelectedItem("");
		this.jb_account.setSelectedItem("");
	}
	
	
	
	public OneRecord getOneRecord(OneRecord r)
	{
		String mark = "";
		Date date =Date.valueOf(dateF.getText());//1
		if(!r.getDate().equals(date))
		{
			mark += "1";
		}
		else
			mark += "0";
		String type = this.jb.getSelectedItem().toString();//2
		if(!r.getType().equals(type))
		{
			mark += "1";
		}
		else
			mark += "0";
		String detail = this.detailF.getText();//3
		if(!r.getDetail().equals(detail))
		{
			mark += "1";
		}
		else
			mark += "0";
		double unitPrice;
		try{
			unitPrice= Double.parseDouble(this.unitPriceF.getText());//4
		}catch(Exception e)
		{
			unitPrice=0;
		}
		if(r.getUnitPrice() != unitPrice)
		{
			mark += "1";
		}
		else
			mark += "0";
		double amount;
		try{
			amount= Double.parseDouble(this.amountF.getText());//5
		}
		catch(Exception e)
		{
			amount=0;
		}
		if(r.getAmount() != amount)
		{
			mark += "1";
		}
		else
			mark += "0";
		
		double expense;
		try{
			expense = Double.parseDouble(this.expenseF.getText());//6
		}
		catch(Exception e)
		{
			expense=0;
		}
		if(r.getExpense() != expense)
		{
			mark += "1";
		}
		else
			mark += "0";
		
		
		double income;
		try{
			income = Double.parseDouble(this.incomeF.getText());//7
		}catch(Exception e)
		{
			income=0;
		}
		if(r.getIncome() != income)
		{
			mark += "1";
		}
		else
			mark += "0";

		String account = jb_account.getSelectedItem().toString();//8
		if(!r.getAccount().equals(account))
		{
			mark += "1";
		}
		else
			mark += "0";
		String comment = this.commentF.getText();//9
		if(!r.getComment().equals(comment))
		{
			mark += "1";
		}
		else
			mark += "0";
		
		EmployeeCardVO user;
		String operator = null;
		try {
			user = HRServerActionManager.getInstance().getEmployeeSelf();
			operator = user.getId();//10
		} catch (ServerActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date currentDate = new Date(System.currentTimeMillis());//11
		Time currentTime = new Time(System.currentTimeMillis());//12
		r.setValues(date,type,detail,unitPrice,amount,expense,income,account,comment,operator,currentDate,currentTime,mark);
		return r;
	}
	
	public void confirmHandler()
	{
		OneRecord a_record = getOneRecord(currentRecord);
	
		int selRow = table.getSelectedRow();
		
		System.out.println("selRow = "+selRow);
		OneRecord selRecord = record_list.get(selRow);
		refreashTable();
	}
	
	public void refreashTable()
	{
		
		int rows = record_list.size();
		if(rows!=0)
		{
			record_list.get(0);
			int columns = OneRecord.showList.length;
			 Object[][] records = new Object[rows][columns];
			 for(int i=0;i<rows;i++)
			 {
				 OneRecord r = record_list.get(i);
				 for(int j=0;j<columns;j++)
				 {
					 records[i][j] = r.getString(j+1);//row*column
			     }	
			 }
			 table.setModel(MutliTableModel(records, attributeArray));
		}
	}
	
	public boolean containRecord(List<OneRecord> list, OneRecord r)
	{
		boolean flag = false;
		for(int i=0;i<list.size();i++)
		{
			if(this.sameRecord(list.get(i), r))
			{
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	public boolean sameRecord(OneRecord r1, OneRecord r2)
	{

		boolean flag = true;
		for(int i=0;i<OneRecord.showList.length;i++)
		{
			if(!r1.getString(i).equals(r2.getString(i)))
			{
				flag = false;
				break;
			}
		}
		return flag;
	}
	
	public void newFrame(JTextField settedField)
	{
		JPanel menu = null; 
		f = new JFrame("日历");
		String className = "中班";//classField.getText();
		CalendarPop1 pop = new CalendarPop1(className,this,settedField);
		menu = pop.mainPanel; 
		f.add(menu);
		int a10 = Toolkit.getDefaultToolkit().getScreenSize().width; // 取得屏幕长度
		int b10 = Toolkit.getDefaultToolkit().getScreenSize().height; // 取得屏幕宽度
		f.setLocation((a10 - 600) / 2, (b10 - 500) / 2); // 设定位置（屏幕中心）
		f.setSize(300, 280); // 设定大小
		f.setVisible(true); // 设定不能缩放
		f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		//f.dispose();
	}
	
	public void getDatefromCalender(String Date,JTextField settedText)
	{
		this.passDate = Date;
		settedText.setText(passDate);
		f.dispose();
	}
	
	public JTable createTable()
	{
		String attributeNames = txt2String("/FinanceUI/attributes.txt");
		attributeArray = attributeNames.trim().split(":");
		String[][] aaa= new String[1][attributeNames.length()];
		table = new JTable(MutliTableModel(aaa,attributeArray));
		table.addMouseListener(new MouseAdapter(){
			
		});
		
		table.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				//System.err.println(table.getSelectedRow()+" nnnn "+table.getSelectedRowCount());
				if(table.getSelectedRow()==-1)
				{
					if(table.getRowCount()==0)
						return;
					else
					{
						table.setRowSelectionInterval(0, 0);
					}
				}
				if(e.getClickCount() == 1)// && e.getButton() == MouseEvent.BUTTON1)
				{
					int rowNumber = table.getSelectedRow();
					Hashtable<String, String> name_vlaue = readTable(table, rowNumber);
					writeToFields(name_vlaue);
				}
				confirmBtn.setEnabled(true);
			}
		});
		
		refreashTable();
		return table;
	}
	
	public Hashtable<String, String> readTable(JTable table, int row)
	{
		
		currentRecord = this.record_list.get(row);
		Hashtable<String, String> name_vlaue = new Hashtable<String, String>();
		String date = (String) table.getModel().getValueAt(row, 0);
		String type = (String) table.getModel().getValueAt(row, 1);
		String detail = (String) table.getModel().getValueAt(row, 2);
		String unitPrice = (String) table.getModel().getValueAt(row, 3);
		String amount = (String) table.getModel().getValueAt(row, 4);
		String expense = (String) table.getModel().getValueAt(row, 5);
		String income = (String) table.getModel().getValueAt(row, 6);
		String account = (String) table.getModel().getValueAt(row, 7);
		String comment = (String) table.getModel().getValueAt(row, 8);
		
		name_vlaue.put("date", date);
		name_vlaue.put("type", type);
		name_vlaue.put("detail", detail);
		name_vlaue.put("unitPrice", unitPrice);
		name_vlaue.put("amount", amount);
		name_vlaue.put("expense", expense);
		name_vlaue.put("income", income);
		name_vlaue.put("account", account);
		name_vlaue.put("comment", comment);
		
		if(Double.parseDouble(expense) == 0){
			//cb_income.enable();
			incomeF.setEditable(true);
			this.expenseF.setEditable(false);
			cbg.setSelectedCheckbox(cb_income);
			//cb_expense.disable();
		}else{
			expenseF.setEditable(true);
			incomeF.setEditable(false);
			cbg.setSelectedCheckbox(cb_expense);
		}
		return name_vlaue;
	}
	
	public void writeToFields(Hashtable<String, String> name_vlaue)
	{
		dateF.setText(name_vlaue.get("date"));
		detailF.setText(name_vlaue.get("detail"));
		commentF.setText(name_vlaue.get("comment"));
		unitPriceF.setText(name_vlaue.get("unitPrice"));
		amountF.setText(name_vlaue.get("amount"));
		expenseF.setText(name_vlaue.get("expense"));
		incomeF.setText(name_vlaue.get("income"));
		jb.setSelectedItem(name_vlaue.get("type"));//name_vlaue.get("type")
		jb_account.setSelectedItem(name_vlaue.get("account"));//name_vlaue.get("account")
	}

	 private DefaultTableModel MutliTableModel(Object[][] items,String[] Attributes)
	 {
		 return new DefaultTableModel(items,Attributes);	
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
	 
	 public String txt2String(String file){
	        String result = "";
	        try{
	            BufferedReader br = new BufferedReader(new InputStreamReader(FinanceAddUI.class.getResourceAsStream(file)));//构造一个BufferedReader类来读取文件
	            String s = null;
	            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
	                result += s+"\n";
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return result;
	    }
	 
	 public String getButtonText()
	 {
		 return "添加记录";
	 }
	 
	 public JPanel getAddActionPane()
	 {
		 return getinstence();
	 }
	 
	public static ClientUI getinstence() {
		// TODO Auto-generated method stub
		if(instance == null)
			instance = new SQLTableUImm();
		return instance;
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return Level;
	}


}
