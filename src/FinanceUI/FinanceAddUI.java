package FinanceUI;


import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import bb.common.EmployeeCardVO;
import bb.gui.ServerActionException;
import bb.gui.base.AddActionPane;
import bb.gui.base.ClientUI;
import bb.gui.server.HRServerActionManager;

public class FinanceAddUI extends AddActionPane {
	static FinanceAddUI instance = null;
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
	private List<OneRecord> record_list;
	private JButton confirmBtn;
	public CheckboxGroup cbg;
	private String[] attributeArray;
	private OneRecord currentRecord;
	private String OP_ID=null;
	
	public FinanceAddUI()
	{
		this.setLayout(new BorderLayout());
		this.add(BorderLayout.NORTH, getInputPanel());
		this.add(this.getShowPanel());
		//this.add(BorderLayout.SOUTH, getSubmitPanel());
		currentRecord = new OneRecord();
		try {
			OP_ID = HRServerActionManager.getInstance().getEmployeeSelf().getId();
			System.err.println(HRServerActionManager.getInstance());
			System.err.println(HRServerActionManager.getInstance().getEmployeeSelf());
			System.err.println(HRServerActionManager.getInstance().getEmployeeSelf().getId());
		} catch (ServerActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JPanel getShowPanel()
	{
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("��ʾ¼����Ϣ"));
		panel.setLayout(new BorderLayout());
		JTable table = createTable();
		
		//table.setSize(panel.getWidth(), panel.getHeight());
		JScrollPane ScrollBtnPanel = new JScrollPane(table);
		ScrollBtnPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		ScrollBtnPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		JPanel btnPanel = new JPanel();
		//btnPanel.setLayout(new BorderLayout());
		JButton cancelBtn = new JButton("ɾ����¼");
		cancelBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cancelHandler();
			}
			
		});
		JButton checkBtn = new JButton("�鿴��¼");
		checkBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				checkHandler();
			}
			
		});

		//btnPanel.add(checkBtn);
		btnPanel.add(cancelBtn);
		panel.add(ScrollBtnPanel);
		panel.add(BorderLayout.SOUTH, btnPanel);
		return panel;
	}
	
	public void cancelHandler()
	{
		int row = table.getSelectedRow();
		if(row == -1)
		{
			JOptionPane.showMessageDialog(null, "����ѡ��Ҫɾ���ļ�¼");
		}
		else
		{
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
		cancelInput();
	}
	
	public void checkHandler()
	{
		record_list = (List<OneRecord>) FinanceUtil.getLastTenRecords();
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
	
	public List<Hashtable<String, Object>> submitHandler()
	{
		List<Hashtable<String, Object>> hash_list = new LinkedList<Hashtable<String, Object>>();
		for(int i=0;i<record_list.size();i++)
		{
			OneRecord r = record_list.get(i);
			Hashtable<String, Object> attr_value = new Hashtable<String, Object>();
			attr_value.put("f_date", r.getDate());
			attr_value.put("f_type", r.getType());
			attr_value.put("detail", r.getDetail());
			attr_value.put("unit_price", r.getUnitPrice());
			attr_value.put("amount", r.getAmount());
			attr_value.put("expense", r.getExpense());
			attr_value.put("income", r.getIncome());
			attr_value.put("account", r.getAccount());
			attr_value.put("comment", r.getComment());
			attr_value.put("operator_id", r.getOperatorID());
			attr_value.put("operate_date", r.getOperateDate());
			attr_value.put("operate_time", r.getOperateTime());
			hash_list.add(attr_value);
		}
		cancelAllRecords();
		deleteFields();
		return hash_list;
	}
	
	public JPanel getInputPanel()
	{
		confirmBtn = new JButton("ȷ��");
		incomeF = new JTextField(8);
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("�����Ϣ"));
		panel.setLayout(new GridLayout(5,2));
		
		JPanel dateP = new JPanel();
		dateP.setLayout(new GridLayout(1,2));
		JLabel dateL = new JLabel("ѡ������(*):");
		JButton btn = new JButton("����");
		btn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				confirmBtn.setEnabled(true);
			}
			
		});
		dateP.add(dateL);
		dateP.add(btn);
		dateF = new JTextField(5);
		
		dateF.addKeyListener(new KeyListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//if(dateF.getText().equals(""))
				{
					confirmBtn.setEnabled(true);
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				confirmBtn.setEnabled(true);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		btn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				newFrame(dateF);
			}
		});
		panel.add(dateP);
		panel.add(dateF);
		
		JLabel unitPriceL = new JLabel("����(Ԫ)(*):");
		unitPriceF = new JTextField(5);
		unitPriceF.addKeyListener(new KeyListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				confirmBtn.setEnabled(true);
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				confirmBtn.setEnabled(true);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		panel.add(unitPriceL);
		panel.add(unitPriceF);
		
		JLabel typeL = new JLabel("��Ŀ(*):");
		jb = new JComboBox();
		jb.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				confirmBtn.setEnabled(true);
			}
			
		});
		
		System.out.println(FinanceAddUI.class.getResource("/").toString());
		String types = txt2String("/FinanceUI/types.txt");
		
		String[] typeArray = types.split(":");
		//jb.addItem("");
		for(int i=0;i<typeArray.length;i++)
		{
			jb.addItem(typeArray[i]);
		}
		panel.add(typeL);
		panel.add(jb);
		
		JLabel amountL = new JLabel("����(ѡ��):");
		amountF = new JTextField(5);
		amountF.addKeyListener(new KeyListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//if(dateF.getText().equals(""))
				{
					confirmBtn.setEnabled(true);
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				confirmBtn.setEnabled(true);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		panel.add(amountL);
		panel.add(amountF);
		
		JLabel detailL = new JLabel("����(*):");
		detailF = new JTextField(5);
		detailF.addKeyListener(new KeyListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//if(dateF.getText().equals(""))
				{
					confirmBtn.setEnabled(true);
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				confirmBtn.setEnabled(true);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		panel.add(detailL);panel.add(detailF);
		
		cbg = new CheckboxGroup(); //������ 
		final Checkbox cb_expense = new Checkbox("֧��(Ԫ):", cbg, true);
		//expenseF.setEditable(false);
		incomeF.setEditable(false);
		final Checkbox cb_income = new Checkbox("����(Ԫ):", cbg, false);
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
		expenseF.addKeyListener(new KeyListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				confirmBtn.setEnabled(true);
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				confirmBtn.setEnabled(true);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		panel.add(cb_expense);
		panel.add(expenseF);
		
		JLabel accountL = new JLabel("�˻�����:");
		jb_account = new JComboBox();
		jb_account.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				confirmBtn.setEnabled(true);
			}
			
		});
		//jb_account.addItem("");
		jb_account.addItem("�ֽ�");
		jb_account.addItem("���п�");
		panel.add(accountL);
		panel.add(jb_account);
		
		//incomeF = new JTextField(8);
		incomeF.addKeyListener(new KeyListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//if(dateF.getText().equals(""))
				{
					confirmBtn.setEnabled(true);
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				confirmBtn.setEnabled(true);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		panel.add(cb_income);
		panel.add(incomeF);
		
		JLabel commentL = new JLabel("���˵��:");
		commentF = new JTextField(15);
		commentF.addKeyListener(new KeyListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				{
					confirmBtn.setEnabled(true);
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				confirmBtn.setEnabled(true);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		panel.add(commentL);
		panel.add(commentF);
		
		JPanel confirmP = new JPanel();
		confirmP.setLayout(new GridLayout(1,4));
		//confirmBtn = new JButton("ȷ��");??
		record_list = new LinkedList<OneRecord>();
		confirmBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(dateF.getText().equals(""))//|| detailF.getText().equals("") || commentF.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "��ѡ������");
				}
				if(detailF.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "����������");
				}
				if(unitPriceF.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "�������Ǯ");
				}
				if(cb_expense.getState()!=true && cb_income.getState()!=true)
				{
					JOptionPane.showMessageDialog(null, "��ȷ���˱ʼ�¼��֧��������");
				}
				if(!amountF.getText().equals(""))
				{
					if(!isNumeric(amountF.getText()))
					{
						JOptionPane.showMessageDialog(null, "����Ӧ��Ϊ����");
					}
				}else
				{
					amountF.setText("1.0");
				}
				
				
				if(!dateF.getText().equals("") && !detailF.getText().equals("") && !unitPriceF.getText().equals(""))
				{
					if(!isNumeric(unitPriceF.getText()))
					{
						JOptionPane.showMessageDialog(null, "�۸�Ӧ��Ϊ����");
					}
					else 
					{
						if(cb_expense.getState()==true)
						{
							if(!expenseF.getText().equals(""))
							{
								if(!isNumeric(expenseF.getText()))
								{
									JOptionPane.showMessageDialog(null, "֧��ӦΪ����");
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
										currentRecord = new OneRecord();
									}
									else
									{
										JOptionPane.showMessageDialog(null, "�����������ĳ˻���֧������");
									}
									
								}
							}
							else
							{
								JOptionPane.showMessageDialog(null, "������֧���Ľ��");
							}
						}
						else if(cb_income.getState()==true)
						{
							if(!incomeF.getText().equals(""))
							{
								if(!isNumeric(incomeF.getText()))
								{
									JOptionPane.showMessageDialog(null, "����ӦΪ����");
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
										JOptionPane.showMessageDialog(null, "�����������ĳ˻������벻��");
									}
								}
							}
							else
							{
								JOptionPane.showMessageDialog(null, "����������Ľ��");
							}
						}
					}
				}
			}
		});
		JButton cancelBtn = new JButton("ȡ��");
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
		Date date =Date.valueOf(dateF.getText());//1
		String type = this.jb.getSelectedItem().toString();//2
		String detail = this.detailF.getText();//3
		
		double unitPrice;
		try{
			unitPrice= Double.parseDouble(this.unitPriceF.getText());//4
		}catch(Exception e)
		{
			unitPrice=0;
		}
		
		double amount;
		try{
			amount= Double.parseDouble(this.amountF.getText());//5
		}
		catch(Exception e)
		{
			amount=0;
		}
		
		
		
		double expense;
		try{
			expense = Double.parseDouble(this.expenseF.getText());//6
		}
		catch(Exception e)
		{
			expense=0;
		}
		
		
		double income;
		try{
			income = Double.parseDouble(this.incomeF.getText());//7
		}catch(Exception e)
		{
			income=0;
		}
		

		String account = jb_account.getSelectedItem().toString();//8
		String comment = this.commentF.getText();//9
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
		r.setValues(date,type,detail,unitPrice,amount,expense,income,account,comment,operator,currentDate,currentTime,"000000000");
		return r;
	}
	
	public void confirmHandler()
	{
		OneRecord a_record = getOneRecord(currentRecord);
		if(!containRecord(record_list,a_record))
		{
			record_list.add(a_record);
		}
		int rows = record_list.size();
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
		f = new JFrame("����");
		String className = "�а�";//classField.getText();
		CalendarPop pop = new CalendarPop(className,this,settedField);
		menu = pop.mainPanel; 
		f.add(menu);
		int a10 = Toolkit.getDefaultToolkit().getScreenSize().width; // ȡ����Ļ����
		int b10 = Toolkit.getDefaultToolkit().getScreenSize().height; // ȡ����Ļ���
		f.setLocation((a10 - 600) / 2, (b10 - 500) / 2); // �趨λ�ã���Ļ���ģ�
		f.setSize(300, 280); // �趨��С
		f.setVisible(true); // �趨��������
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
		attributeArray = attributeNames.split(":");
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
			}
		});
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
		 return new DefaultTableModel(x, new String[]{"������1"});	
	 }
	 
	 public static String txt2String(String file){
	        String result = "";
	        try{
	            BufferedReader br = new BufferedReader(new InputStreamReader(FinanceAddUI.class.getResourceAsStream(file)));//����һ��BufferedReader������ȡ�ļ�
	            
	            String s = null;
	            while((s = br.readLine())!=null){//ʹ��readLine������һ�ζ�һ��
	                result = result + "\n" +s;
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return result;
	    }
	 
	 public String getButtonText()
	 {
		 return "��Ӽ�¼";
	 }
	 
	 public JPanel getAddActionPane()
	 {
		 return getinstence();
	 }
	 
	public static AddActionPane getinstence() {
		// TODO Auto-generated method stub
		if(instance == null)
			instance = new FinanceAddUI();
		return instance;
	}

	@Override
	public boolean add() throws Exception {
		// TODO Auto-generated method stub
		if(record_list.size()==0)
		{
			JOptionPane.showMessageDialog(null, "û�м�¼�����!!");
			return false;
		}
		try {
			OP_ID = HRServerActionManager.getInstance().getEmployeeSelf().getId();
		} catch (ServerActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(record_list+"\n"+OP_ID);
		FinanceUtil.insert(record_list, OP_ID);
		return true;
	}

	@Override
	public ClientUI getFollowingUI() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
