package FinanceUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import bb.gui.base.ClientUI;

public class FinanceReportUI extends ClientUI {
	private String[] attributeArray;
	private List<OneRecord> record_list;
	private List<OneRecord> validRecord_list;
	private String input;
	private JTable table;
	
	public FinanceReportUI(Collection<OneRecord> list, Collection<OneRecord> validRecords, String input){
		record_list = new LinkedList<OneRecord>();
		record_list.addAll(list);
		System.out.println("record_list>>>"+record_list.size());
		if(record_list.size() != 0)
		{
			validRecord_list = new LinkedList<OneRecord>();
			validRecord_list.addAll(validRecords);
			this.input = input;
			this.setLayout(new BorderLayout());
			this.add(tablePanel());
		}
		else
		{
			this.add(getNoticePanel());
		}
	}
	
	public JPanel tablePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JScrollPane ScrollBtnPanel = new JScrollPane(createTable());
		ScrollBtnPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		ScrollBtnPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(ScrollBtnPanel);
		panel.add(BorderLayout.SOUTH, btnPanel());
		return panel;
	}
	
	public JPanel getNoticePanel()
	{
		JPanel p = new JPanel();
		JTextArea area = new JTextArea("无本月记录");
		p.add(area);
		return p;
	}
	
	public JTable createTable()
	{
		String attributeNames = txt2String(new File("report_atts.txt"));
		attributeArray = attributeNames.split(":");
		String types = txt2String(new File("types.txt")).replace("\n", "");
		String[] types_array = types.split(":");
		double sum_income = 0;
		double sum_expense = 0;
		Hashtable<String, Double> types_sum_income = new Hashtable<String, Double>();
		List<String> type_list = new LinkedList<String>();
		for(int j=0;j<types_array.length;j++)
		{
			types_sum_income.put(types_array[j], 0.0);
			type_list.add(types_array[j]);
		}
		for(int i=0;i<record_list.size();i++)
		{
			OneRecord r = record_list.get(i);
			double income = r.getIncome();
			double expense = r.getExpense();
			String type = r.getType();
			//System.out.println("type =?? "+type);
			//System.out.println("type :: "+types_sum_income.get(type));
			//System.out.println("?????"+types_sum_income);
			types_sum_income.put(type, types_sum_income.get(type) + income- expense);
		}
		Hashtable<String, Double> item_cost = new Hashtable<String, Double>();
		Hashtable<String, Double> item_income = new Hashtable<String, Double>();
		for(int i=0;i<type_list.size();i++)
		{
			String type_item = type_list.get(i); 
			double money = types_sum_income.get(type_item);
			if(money >0)
			{
				if(type_item.equals("园费"))
				{
					item_income.put("园费",((item_income.get("园费")==null)?0:item_income.get("园费"))+ money);
					System.out.println(item_income);
				}
				else
				{
					item_income.put("其他",((item_income.get("其他")==null)?0:item_income.get("其他"))+ money);
				}
				sum_income += money;
			}
			if(money <0)
			{
				item_cost.put(type_item, money);
				sum_expense += money;
			}
		}		
		BigDecimal bd;
		String[][] table_content = new String[item_cost.size()+1][attributeArray.length];
		table_content[0][0] = "园费";
		if(item_income.get("园费") != null)
		{
			bd = new BigDecimal(item_income.get("园费"));
			bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
			table_content[0][1] = String.valueOf(bd);
		}
		else
		{
			table_content[0][1] = String.valueOf("0");
		}
		table_content[1][0] = "其他收入";
		if(item_income.get("其他") != null)
		{
			bd = new BigDecimal(item_income.get("其他"));
			bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
			table_content[1][1] = String.valueOf(bd);
		}
		else
		{
			table_content[1][1] = String.valueOf("0");
		}
		
		
		Enumeration e = (Enumeration) item_cost.keys();
		int k = 0;
		while(e.hasMoreElements())
		{
			String item = (String) e.nextElement();
			double money = -item_cost.get(item);
			table_content[k][2] = item;
			bd = new BigDecimal(money);
		    bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
			table_content[k][3] = String.valueOf(bd);
			k++;
		}
		table_content[k][0] = "合计";
		bd = new BigDecimal(sum_income);
	    bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		table_content[k][1] = String.valueOf(bd);

		bd = new BigDecimal(-sum_expense);
	    bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		table_content[k][3] = String.valueOf(bd);
		
		bd = new BigDecimal((sum_income + sum_expense));
	    bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		table_content[k][4] = String.valueOf(bd);
		
		bd = new BigDecimal(getTotalBalnce());
	    bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		table_content[k][5] = String.valueOf(bd);
	    
		table = new JTable(MutliTableModel(table_content,attributeArray));
		return table;
	}
	
	public double getTotalBalnce()
	{
		double sum_income = 0;
		double sum_expense = 0;
		for(int i=0;i<validRecord_list.size();i++)
		{
			OneRecord r = validRecord_list.get(i);
			double income = r.getIncome();
			double expense = r.getExpense();
			if(income != 0)
			{
				sum_income += income;
			}
			else if(expense != 0)
			{
				sum_expense += expense;
			}
		}
		double balance = sum_income - sum_expense;
		return balance;
	}
	
	 private DefaultTableModel MutliTableModel(Object[][] items,String[] Attributes)
	 {
		 return new DefaultTableModel(items,Attributes);	
	 }
	
	 public String txt2String(File file){
	        String result = "";
	        try{
	            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
	            String s = null;
	            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
	                result = result + "\n" +s;
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return result;
	    }
	
	public JPanel btnPanel()
	{
		JPanel panel = new JPanel();
		JButton exportBtn = new JButton("导出");
		exportBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "xls");
					chooser.setFileFilter(filter);
					int option = chooser.showOpenDialog(null);
					if(option == JFileChooser.APPROVE_OPTION)
					{
						String path = chooser.getSelectedFile().getAbsolutePath();
						exportHandler(path);
					}
				} catch (RowsExceededException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (WriteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		JButton cancelBtn = new JButton("取消");
		cancelBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}

			
		});
		JButton exportDetailBtn = new JButton("导出明细");
		exportDetailBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				/*
				try {
					exportDetailHandler("D:\\study\\detail.xls");
				} catch (WriteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				try {
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "xls");
					chooser.setFileFilter(filter);
					int option = chooser.showOpenDialog(null);
					if(option == JFileChooser.APPROVE_OPTION)
					{
						String path = chooser.getSelectedFile().getAbsolutePath();
						exportDetailHandler(path);
					}
				} catch (RowsExceededException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (WriteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		});
		panel.add(exportBtn);
		panel.add(exportDetailBtn);
		panel.add(cancelBtn);
		return panel;
	}
	
	public void exportHandler(String excelFile) throws RowsExceededException, WriteException 
	{
		File file = new File(excelFile);
		 if(!file.exists())
		 {
			 try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
       createExcel(file);
	}
	
	
	public void exportDetailHandler(String excelFile) throws WriteException
	{
		File file = new File(excelFile);
		 if(!file.exists())
		 {
			 try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 createDetailExcel(file);
	}
	
	public void createDetailExcel(File file) throws WriteException
	{
		WritableWorkbook workbook;
		try {
				workbook = Workbook.createWorkbook(file);
				WritableSheet sheet = workbook.createSheet("User",0);
				WritableFont font0=new WritableFont(WritableFont.createFont("宋体"),18,WritableFont.NO_BOLD);  
				font0.setColour(Colour.BLUE);
				WritableFont font1=new WritableFont(WritableFont.createFont("宋体"),13,WritableFont.NO_BOLD);
				WritableCellFormat format0=new WritableCellFormat(font0);
				format0.setBorder(Border.NONE, BorderLineStyle.MEDIUM);
				WritableCellFormat format10=new WritableCellFormat(font1);
				format10.setBorder(Border.NONE, BorderLineStyle.MEDIUM);
				

		        format0.setAlignment(jxl.format.Alignment.CENTRE);
		        format0.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		        

		        format10.setAlignment(jxl.format.Alignment.CENTRE);
		        format10.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
				
				Label title = new Label(3, 0, "开元国际幼儿园"+this.input+"账目明细", format0);//col*row
			    sheet.addCell(title);
			    
		    
			    String attributeNames = txt2String(new File("attributes.txt"));
				String[] attributeArray = attributeNames.split(":");
			    for(int i=0;i<attributeArray.length;i++)
			    {
			    	sheet.setColumnView(i,15);
			    	String attibute = attributeArray[i];
			        Label attrLable = new Label(i, 1, attibute, format10);
				    sheet.addCell(attrLable);
			    }
			    double sum_income = 0;
			    double sum_expense = 0;
			    int i;
			    for(i=0;i<record_list.size();i++)
				{
					OneRecord r = record_list.get(i);
					String date = r.getDate().toString();
			        Label dateL = new Label(0, i+2, date, format10);
				    sheet.addCell(dateL);
				    
				    String type = r.getType();
				    Label typeL = new Label(1, i+2, type, format10);
				    sheet.addCell(typeL);
				
				    String detail = r.getDetail();
				    Label detailL = new Label(2, i+2, detail, format10);
				    sheet.addCell(detailL);

				    double unitPrice = r.getUnitPrice();
				    String unitPriceStr;
				    if(unitPrice == 0)
				    {
				    	unitPriceStr = "";
				    }
				    else
				    	unitPriceStr = String.valueOf(unitPrice);
				    Label unitPriceL = new Label(3, i+2, unitPriceStr, format10);
				    sheet.addCell(unitPriceL);
				    
				    double amount = r.getAmount();
				    String amountStr;
				    if(amount == 0)
				    	amountStr = "";
				    else
				    	amountStr = String.valueOf(amount);
				    Label amountL = new Label(4, i+2, amountStr, format10);
				    sheet.addCell(amountL);
				    
				    double expense = r.getExpense();
				    String expenseStr;
				    if(expense == 0)
				    	expenseStr = "";
				    else
				    {
				    	sum_expense += expense;
				    	expenseStr = String.valueOf(expense);
				    }
				    Label expenseL = new Label(5, i+2, expenseStr, format10);
				    sheet.addCell(expenseL);

				    double income = r.getIncome();
				    String incomeStr;
				    if(income == 0)
				    	incomeStr = "";
				    else
				    {
				    	sum_income += income;
				    	incomeStr = String.valueOf(income);
				    }
				    Label incomeL = new Label(6, i+2, incomeStr, format10);
				    sheet.addCell(incomeL);

				    String account = String.valueOf(r.getAccount());
				    Label accountL = new Label(7, i+2, account, format10);
				    sheet.addCell(accountL);

				    String comment = String.valueOf(r.getComment());
				    Label commentL = new Label(8, i+2, comment, format10);
				    sheet.addCell(commentL);				
				}
			    /*
			    Label sumL = new Label(5, i+2, String.valueOf(sum_expense), format10);
			    sheet.addCell(sumL);				
			
			    Label sumL1 = new Label(6, i+2, String.valueOf(sum_income), format10);
			    sheet.addCell(sumL1);*/
			    workbook.write();
		        workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void createExcel(File file) throws RowsExceededException, WriteException
	{
		WritableWorkbook workbook;
		try {
			workbook = Workbook.createWorkbook(file);
			 //创建新的一页
		    WritableSheet sheet = workbook.createSheet("User",0);
		    WritableFont font0=new WritableFont(WritableFont.createFont("宋体"),16,WritableFont.NO_BOLD);  
		    font0.setColour(Colour.BLUE);
		    WritableFont font1=new WritableFont(WritableFont.createFont("宋体"),13,WritableFont.NO_BOLD); 
		    
		    WritableCellFormat format0=new WritableCellFormat(font0);
		    format0.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM);
		    
		    WritableCellFormat format10=new WritableCellFormat(font1);
		    format10.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
		    
		    WritableCellFormat format11=new WritableCellFormat(font1);
		    format11.setBorder(Border.LEFT, BorderLineStyle.MEDIUM);
		    
		    WritableCellFormat format12=new WritableCellFormat(font1);
		    format12.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM);
		    
		    WritableCellFormat format13=new WritableCellFormat(font1);
		    format13.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM);
		    
		    WritableCellFormat format14=new WritableCellFormat(font1);
		    format14.setBorder(Border.NONE, BorderLineStyle.MEDIUM);
		    
		    
	        format0.setAlignment(jxl.format.Alignment.CENTRE);
	        format0.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	        
	        format10.setAlignment(jxl.format.Alignment.CENTRE);
	        format10.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	        format10.setAlignment(jxl.format.Alignment.CENTRE);
	        format10.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	        format11.setAlignment(jxl.format.Alignment.CENTRE);
	        format11.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	        format12.setAlignment(jxl.format.Alignment.CENTRE);
	        format12.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	        format13.setAlignment(jxl.format.Alignment.CENTRE);
	        format13.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	        format14.setAlignment(jxl.format.Alignment.CENTRE);
	        format14.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	        
	        CellView navCellView = new CellView();  
	        navCellView.setAutosize(true); //设置自动大小
	        navCellView.setSize(15);
	        
	        sheet.setColumnView(1,10);
	        sheet.setColumnView(2,15);
	        sheet.setColumnView(3,15);
	        sheet.setColumnView(4,15);
	        sheet.setColumnView(5,15);
	        sheet.setColumnView(6,15);
	        
		    Label title = new Label(3, 0, "开元国际幼儿园"+this.input+"报表汇总", format0);
	        sheet.addCell(title);
	        
	        Label incomeL= new Label(1,1,"收入", format10);//col * row
	        sheet.addCell(incomeL);
	        
	        Label value1 = new Label(2,1,"金额", format10);
	        sheet.addCell(value1);
	        
	        Label expneseL = new Label(3,1,"支出", format10);
	        sheet.addCell(expneseL);
	        
	        Label value2 = new Label(4,1,"金额", format10);
	        sheet.addCell(value2);
	        
	        Label monthProfit = new Label(5,1,"本月小计", format10);
	        sheet.addCell(monthProfit);
	        
	        Label balance = new Label(6,1,"结余", format10);
	        sheet.addCell(balance);
	        
	        int row = table.getRowCount();
	        int col = attributeArray.length;
	        for(int i=0;i<row;i++)
	        {
	        	for(int j=0;j<col;j++)
	        	{
	        		String table_value = (String) table.getModel().getValueAt(i, j);
	        		Label cell = null;
	        		if(i == row-1)
	        		{
	        			cell = new Label(j+1,i+2,table_value, format10);
	        		}
	        		else if(j==0)
	        		{
	        			cell = new Label(j+1,i+2,table_value, format11);
	        		}
	        		else if(j == col-1)
	        		{
	        			cell = new Label(j+1,i+2,table_value, format12);
	        		}
	        		else
	        		{
	        			cell = new Label(j+1,i+2,table_value, format14);
	        		}
	        		sheet.addCell(cell);
	        	}
	        }
	        sheet.setColumnView(0, 20);
	        sheet.setRowView(0, 1000, false); //设置行高
	        workbook.write();
	        workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
