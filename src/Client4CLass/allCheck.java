package Client4CLass;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.apache.commons.dbcp2.BasicDataSource;
import KYUI.KYMainUI;
import bb.common.EmployeeCardVO;
/**
 *
 * @author Chel
 */
public class allCheck extends JFrame{
	static allCheck instence=null;
    private static JPanel p;
    public static Font font;
    public JPanel medcinePanel;
    public  JPanel morningPanel;
    public CheckboxGroup cbg_temperature;
    public CheckboxGroup cbg_mental;
    public CheckboxGroup cbg_danger;
    public CheckboxGroup cbg_sleep;
    public CheckboxGroup cbg_physical;
    public CheckboxGroup cbg_teacher;
    public String temperature;
    public String mental;
    public String danger;
    public String sleep;
    public String physical;
    public String a_comment;
    public String temperatureValue;//101
    public String mentalValue;//102
    public String dangerValue;//103
    public String sleepValue;//104
    public String physicalValue;//105
    public String totalComment;//106
    public String medicalName;//107
    public JComboBox jb;
    public String medicalReason;//108
    public String absenceReason;//109
    public String temperatureDetail;
    public String dangerDetail;
    public String mentalComment;
    public String dangerComment;
    public String sleepComment;
    public String physicalComment;
   // public Connection conn;
   // public String stuID;
    public String date;
    public String time;
    public JTextField absenceF;
    public JTextField dangerF;
    public JTextField tempF;
    public String teacher ;
    public String operator ;
    public JTextField commentField;
    //public JTextField medNameF;
    public JTextField medReasonF;
    public CheckboxGroup cbg_operator;
    //public List<String> teacherList;
    //public Hashtable<String, String> name_id;
    //public String stuName;
    String teacherID;
    public String classType=KYMainUI.department;
    BasicDataSource bds =KYClassUI.bds;
    EmployeeCardVO stu;
    Collection<EmployeeCardVO> teachers;
    private JTextField wayF;
    private String eatWay;
    private JTextField medNameField; 
    
    public allCheck(EmployeeCardVO stu, Collection<EmployeeCardVO> Teachers)
    {
    	super("�����/��ҩ��¼���");
    	instence=this;
    	this.stu = stu;
    	this.teachers = Teachers;
    	 medcinePanel = new JPanel();
    	 medcinePanel.setLayout(new BorderLayout());
         p=new JPanel();
         p.setLayout(new CardLayout());
         medNameField = new JTextField();

         //�½�����JPanel
         JPanel p2=new JPanel();
         JLabel lb=new JLabel("first panel");
         //p1.add(lb);
       
         lb = new JLabel("second panel");
         p2.add(lb);

         //���½�������JPanel p1,p2��ӵ�p��
         //p.add(p1,"frist");
         p.add(this.zeroPanel(),"zero");
         p.add(this.getMorningPanel1(),"first");
         p.add(getMedicinePanel(),"second");
        
         //����Ĭ����ʾfirst����Ӧ��JPanel p1
         ((CardLayout)p.getLayout()).show(p, "zero");

         JPanel buttonPanel = new JPanel();
         JButton cbtn=new JButton("�������");
         cbtn.addActionListener(new ActionListener(){
             @Override
			public void actionPerformed(ActionEvent e) {
                 //������Change��ťʱ����ʾsecond ��Ӧ��JPanel p2
                 ((CardLayout)p.getLayout()).show(p, "first");
             }            
         });
         JButton medcine=new JButton("��ҩ��¼");
         buttonPanel.add(cbtn);
         buttonPanel.add(medcine);
         medcine.addActionListener(new ActionListener(){
             @Override
			public void actionPerformed(ActionEvent e) {
                 //������Change��ťʱ����ʾsecond ��Ӧ��JPanel p2
                 ((CardLayout)p.getLayout()).show(p, "second");
             }            
         });
  		 this.setType(Window.Type.UTILITY);
         this.add(buttonPanel,BorderLayout.NORTH);
         this.add(p,BorderLayout.CENTER);
         this.setSize(420,350);
         this.setVisible(true);
         this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }
   
    public JPanel zeroPanel()
    {   
    	JPanel info = new JPanel();
    	info.setLayout(new GridLayout(6,1));
    	info.add(new JLabel("���� Name:"+stu.getCompanyAddressBookName()+" ѧ�� Stu. No.: "+stu.getId()));
    	JPanel subPanel = new JPanel(new GridLayout(1,2));
    	subPanel.add(new JLabel("����  Date of birth: "+ stu.getBirthDate()));
    	JButton earlyLeavebtn= new JButton("����");
    	earlyLeavebtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				java.sql.Time time = new java.sql.Time(System.currentTimeMillis());
				java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
				String login_name=stu.getCompanyAddressBookName();
				String login_id=stu.getId();
				 try {
					 	Connection conn=connect();
					 	PreparedStatement pstmt =null;
				
					 	pstmt = conn.prepareStatement("insert into emp_logginRecord (id,L_name, L_time, L_date, L_status,department) values (?,?,?,?,?,?)");
			        	pstmt.setString(1, login_id);
			        	pstmt.setString(2, login_name);
			        	pstmt.setTime(3, time);
			        	pstmt.setDate(4, date);

			        	pstmt.setString(6, classType);
			        	pstmt.setString(5, "2");
			       		pstmt.execute();
			        	pstmt = conn.prepareStatement("insert into emp_logginRecordall (id,L_name, L_time, L_date, L_status,department) values (?,?,?,?,?,?)");
			        	pstmt.setString(1, login_id);
			        	pstmt.setString(2, login_name);
			        	pstmt.setTime(3, time);
			        	pstmt.setDate(4, date);
			        	pstmt.setString(6, classType);
			       
			        	pstmt.setString(5, "2");
						pstmt.execute();
						pstmt.close();
						conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 KYMainUI.getInstance().getAction().get(0).RepeatAction(null);
				instence.dispose();
			}
    		
    	});
    	subPanel.add(earlyLeavebtn);
    	info.add(subPanel);
    	info.add(new JLabel("�ҳ� Parents' name : "+stu.getManager()));
    	info.add(new JLabel("�绰 Telephone: "+stu.getPhone() +"/"+stu.getMobile()+"/"+stu.getHomePhone()));
    	info.add(new JLabel("��ͥסַ Home address: "+ stu.getWorkAddress()));
    	info.add(new JLabel("��ע Comments: "+stu.getComments()));
		return info;
    }
    
    public JPanel getMorningPanel1()
    {
    	morningPanel = new JPanel();
  		JPanel noonPanel = new JPanel();
  		JLabel text = new JLabel("noon panel");
  		noonPanel.add(text);
  		JPanel eventPanel = new JPanel();
  		morningPanel.setLayout(new GridLayout(10,1));
  		
  		font = new Font("�꿬��",Font.BOLD,15);
  		
  		JPanel namePanel = new JPanel();
  		namePanel.setLayout(new GridLayout(1,2));
  		JLabel nameLabel = new JLabel("���� ������");
  		JLabel babyName = new JLabel(stu.getCompanyAddressBookName());
  		namePanel.add(nameLabel);
  		namePanel.add(babyName);
  		
  		//JFiled field = new JField("");
  		JLabel tempLabel = new JLabel("�׶�����: ");
  		
  		JPanel temperaturePanel = new JPanel();
  		temperaturePanel.setLayout(new GridLayout(1,2));
  		JPanel p1 = new JPanel();
  		//temperaturePanel.setLayout(new GridLayout(1,5));
  		cbg_temperature = new CheckboxGroup(); //������ 
	    Checkbox box1_2 = new Checkbox("����", cbg_temperature, true);
	    Checkbox box2_2 = new Checkbox("����", cbg_temperature, false);
	    JLabel tempL = new JLabel("����:");
	    tempF = new JTextField(8); 
	    JLabel degreeL = new JLabel("���϶�");
		p1.add(tempLabel);
		p1.add(box1_2); //���嵥ѡ����ӵ����� 
		p1.add(box2_2); //ͬ�ϡ��� 
	  
	    JPanel p2 = new JPanel();
	    p2.add(tempL);
	    p2.add(tempF);
	    p2.add(degreeL);
	    temperaturePanel.add(p1);
	    temperaturePanel.add(p2);
	    
	
  	    JPanel mentalPanel = new JPanel();
  	    mentalPanel.setLayout(new GridLayout(1,2));
  	    p1 = new JPanel();
  	    p2 = new JPanel();
	    JLabel mentalLabel = new JLabel("����״̬:");
	    cbg_mental = new CheckboxGroup(); //������ 
	    p1.add(mentalLabel);
	    box1_2 = new Checkbox("����", cbg_mental, true);
	    box2_2 = new Checkbox("��", cbg_mental, false);
	    p1.add(box1_2); //���嵥ѡ����ӵ����� 
	    p1.add(box2_2); //ͬ�ϡ��� 
	    mentalPanel.add(p1);
	    mentalPanel.add(p2);
	   
	    
	    JPanel dangerPanel = new JPanel();
	    dangerPanel.setLayout(new GridLayout(1,2));
	    p1 = new JPanel();
	    p2 = new JPanel();
	    JLabel dangerLabel = new JLabel("��������:");
	    cbg_danger = new CheckboxGroup(); //������ 
	    Checkbox box1_3 = new Checkbox("��", cbg_danger, true);
	    Checkbox box2_3 = new Checkbox("��", cbg_danger, false);
	    JLabel commentL = new JLabel("����:");
	    dangerF = new JTextField(8); 
	    p1.add(dangerLabel);
	    p1.add(box1_3); //���嵥ѡ����ӵ����� 
	    p1.add(box2_3); //ͬ�ϡ���
	    p2.add(commentL);
	    p2.add(dangerF);
	    dangerPanel.add(p1);
	    dangerPanel.add(p2);
	    
	    JPanel sleepPanel = new JPanel();
	    sleepPanel.setLayout(new GridLayout(1,2));
	    p1 = new JPanel();
	    p2 = new JPanel();
	    JLabel sleepLabel = new JLabel("˯�����:");
	    cbg_sleep = new CheckboxGroup(); //������ 
	    Checkbox box1_4 = new Checkbox("����", cbg_sleep, true);
	    Checkbox box2_4 = new Checkbox("��", cbg_sleep, false);
	    p1.add(sleepLabel);
	    p1.add(box1_4); //���嵥ѡ����ӵ����� 
	    p1.add(box2_4); //ͬ�ϡ���
	    sleepPanel.add(p1);
	    sleepPanel.add(p2);
	    //addSleepListener(box1_4, box2_4);
	    
	    JPanel physicalPanel = new JPanel();
	    physicalPanel.setLayout(new GridLayout(1,2));
	    p1 = new JPanel();
	    p2 = new JPanel();
	    JLabel physicalLabel = new JLabel("����״��:");
	    cbg_physical = new CheckboxGroup(); //������ 
	    Checkbox box1_5 = new Checkbox("����", cbg_physical, true);
	    Checkbox box2_5 = new Checkbox("��", cbg_physical, false);
	    p1.add(physicalLabel);
	    p1.add(box1_5); //���嵥ѡ����ӵ����� 
	    p1.add(box2_5); //ͬ�ϡ���
	    physicalPanel.add(p1);
	    physicalPanel.add(p2);
	    //addPhysicalListener(box1_5, box2_5);
	    
	    
  	    JPanel teacherPanel = new JPanel();
  	    JLabel techLabel = new JLabel("�����ʦ:");
  	    cbg_teacher = new CheckboxGroup(); //������ 
  	    teacherPanel.add(techLabel);
  	    for(EmployeeCardVO teacher :teachers)
  	    {
  	    	String name = teacher.getCompanyAddressBookName();
  	    	Checkbox tc =new Checkbox(name, cbg_teacher, false);
  	    	tc.setName(teacher.getId());
  	    	teacherPanel.add(tc); //���嵥ѡ����ӵ����� 
  	    }
  	    /*
  	    teacherPanel.add(new Checkbox("teacher1", cbg_teacher, true)); //���嵥ѡ����ӵ����� 
  	    teacherPanel.add(new Checkbox("teacher2", cbg_teacher, false)); //ͬ�ϡ��� 
  	    teacherPanel.add(new Checkbox("teacher3", cbg_teacher, false)); //ͬ�� 
  	   */
  	    JPanel commentPanel = new JPanel();
  	    JLabel commentLabel = new JLabel("��ע:");
  	    commentField = new JTextField(30);
  	    commentPanel.add(commentLabel);
  	    commentPanel.add(commentField);
  	    
  	    JPanel buttonPanel = new JPanel();
  	    JButton button1 = new JButton("�ύ");
  	    button1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					submitHandler();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
  	    	
  	    });
  	    
  	    JButton button2 = new JButton("�˳�");
  	    button2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				instence.dispose();
			}
  	    	
  	    });
  	    buttonPanel.add(button1);
  	    buttonPanel.add(button2);
  	    
  	    JPanel absence = new JPanel();
  	    //absence.setLayout(new GridLayout(1,2));
  	    absence.add(new JLabel("ȱ��ԭ��:"));
  	    this.absenceF =new JTextField(25);
  	    absence.add(absenceF);
  	    
  	    morningPanel.add(namePanel);
  	    morningPanel.add(temperaturePanel);
  	    morningPanel.add(mentalPanel);
  	    morningPanel.add(dangerPanel);
  	    morningPanel.add(sleepPanel);
  	    morningPanel.add(physicalPanel);
  	    morningPanel.add(absence);
  	    morningPanel.add(commentPanel);
  	    morningPanel.add(teacherPanel);
  	    morningPanel.add(buttonPanel);
  	    return morningPanel;
    }
    
    public void initValues()
    {
    	temperatureValue = "����";
    	mentalValue = "����";
    	dangerValue = "��";
    	sleepValue = "����";
    	physicalValue = "����";
    	temperatureDetail = "";
    	dangerDetail = "";
    	teacher = "";
    	totalComment = "";
    }
    
    public void submitHandler_med() throws ClassNotFoundException, SQLException
    {
    	teacherID="";
    	try{
    		operator = cbg_operator.getSelectedCheckbox().toString().split("label=")[1].split(",")[0];
    		teacherID = cbg_operator.getSelectedCheckbox().getName();
    		
    	}catch(NullPointerException e)
    	{
    		JOptionPane.showMessageDialog(null, "�����ύǰѡ�������ʦ��");
    		return;
    	}
    	medicalName = jb.getSelectedItem().toString();
    	medicalReason = medReasonF.getText();
    	eatWay = wayF.getText();
    	System.err.println("medicalName = "+medicalName+" medicalReason = "+medicalReason+" eatWay = "+eatWay);
	    if(!medicalName.equals("") && !medicalReason.equals("") && !eatWay.equals(""))
	    {
	    	System.out.println("medicalName = "+medicalName);
	    	if(medicalName.equals("����"))
	    	{
	    		medicalName = medNameField.getText(); 
	    		insertValues(7);
		    	dispose();
	    	}
	    	else
	    	{
	    		insertValues(7);
		    	dispose();
	    	}
	    }
	    else
	    {
	    	JOptionPane.showConfirmDialog(null, "�ύʧ��, �����������Ϣ!");
	    }	
    }
    
    public void submitHandler() throws ClassNotFoundException, SQLException
    {
    	teacherID="";
    	initValues();
    	String temperatureValue_new = cbg_temperature.getSelectedCheckbox().toString().split("label=")[1].split(",")[0];
    	String mentalValue_new = cbg_mental.getSelectedCheckbox().toString().split("label=")[1].split(",")[0];
    	String dangerValue_new = cbg_danger.getSelectedCheckbox().toString().split("label=")[1].split(",")[0];
    	String sleepValue_new = cbg_sleep.getSelectedCheckbox().toString().split("label=")[1].split(",")[0];
    	String physicalValue_new = cbg_physical.getSelectedCheckbox().toString().split("label=")[1].split(",")[0];
    	totalComment = commentField.getText(); 
    	try{
    		teacher = cbg_teacher.getSelectedCheckbox().toString().split("label=")[1].split(",")[0];
    	}catch(NullPointerException e){
    		JOptionPane.showMessageDialog(null, "�����ύǰѡ�������ʦ��");
    		return;
    	}
    	
    	teacherID = cbg_teacher.getSelectedCheckbox().getName();
    	System.err.println("temperatureValue_new = "+temperatureValue_new+"\n"+
    			"mentalValue_new = "+mentalValue_new+"\n"+
    			"dangerValue_new = "+dangerValue_new+"\n"+
    			"sleepValue_new = "+sleepValue_new+"\n"+
    			"physicalValue_new = "+physicalValue_new);    
    	try{
	    	if(!temperatureValue_new.equals(temperatureValue))
	    	{
	    		temperatureDetail = tempF.getText();
	    		insertValues(1);
	    		System.out.println("temperatureValue_new: "+temperatureValue_new+" temperatureDetail = "+temperatureDetail);
	    	}
	    	if(!mentalValue_new.equals(mentalValue))
	    	{
	    		insertValues(2);
	    		System.out.println("mentalValue_new: "+mentalValue_new);
	    	}
	    	if(!dangerValue_new.equals(dangerValue))
	    	{
	    		dangerDetail = dangerF.getText();
	    		insertValues(3);
	    		System.out.println("dangerValue_new: "+dangerValue_new);
	    	}
	    	if(!sleepValue_new.equals(sleepValue))
	    	{
	    		insertValues(4);
	    		System.out.println("sleepValue_new: "+sleepValue_new);
	    	}
	    	if(!physicalValue_new.equals(physicalValue))
	    	{
	    		insertValues(5);
	    		System.out.println("physicalValue_new: "+physicalValue_new);
	    	}
	    	if(!this.absenceF.getText().equals(""))
	    	{
	    		this.absenceReason=absenceF.getText();
	    		insertValues(9);
	    		System.out.println("absenceReason: "+absenceReason);
	    	}
    	}
    	catch(Exception e)
    	{
    		JOptionPane.showConfirmDialog(null, "�ύʧ�ܣ��뱨����԰��");
    		this.dispose();
    	}
    	this.dispose();
    }
    
	public Connection connect() throws SQLException 
	{
		Connection conn = null;
		
		conn=bds.getConnection();
        if(!conn.isClosed()) 
        	System.out.println("Succeeded connecting to the Database!"+conn);
        return conn;
	}
    
    public void insertValues(int type) throws ClassNotFoundException, SQLException
    {
    	Connection conn=connect();
    	PreparedStatement pstmt = null;
		switch(type)
		{
			case 1: //teperature
				pstmt = conn.prepareStatement("INSERT INTO emp_healthcheckdata (id, H_date, " +
						"H_time, H_type, H_detail, H_collectorid, H_comment)"+
				" VALUES('"+stu.getId()+"',?,?,"+"'101'"+",'"+temperatureDetail+"','"+teacherID+"','"+totalComment+"');");
				pstmt.setDate(1, (new java.sql.Date(System.currentTimeMillis())));
				pstmt.setTime(2, (new java.sql.Time(System.currentTimeMillis())));
				System.out.println(pstmt);
				pstmt.execute();
				break;
			case 2://mental
				pstmt = conn.prepareStatement("INSERT INTO emp_healthcheckdata (id, H_date, " +
						"H_time, H_type, H_detail, H_collectorid, H_comment)"+
				" VALUES('"+stu.getId()+"',?,?,"+"'102'"+",'','"+teacherID+"','"+totalComment+"');");
				pstmt.setDate(1, (new java.sql.Date(System.currentTimeMillis())));
				pstmt.setTime(2, (new java.sql.Time(System.currentTimeMillis())));
				System.err.println(pstmt);
				pstmt.execute();
				break;
			case 3://danger
				pstmt = conn.prepareStatement("INSERT INTO emp_healthcheckdata (id, H_date, " +
						"H_time, H_type, H_detail, H_collectorid, H_comment)"+
				" VALUES('"+stu.getId()+"',?,?,"+"'103'"+",'"+this.dangerDetail+"','"+teacherID+"','"+totalComment+"');");
				
				pstmt.setDate(1, (new java.sql.Date(System.currentTimeMillis())));
				pstmt.setTime(2, (new java.sql.Time(System.currentTimeMillis())));
				pstmt.execute();	
				break;
			case 4://sleep			
				pstmt = conn.prepareStatement("INSERT INTO emp_healthcheckdata (id, H_date, " +
						"H_time, H_type, H_detail, H_collectorid, H_comment)"+
				" VALUES('"+stu.getId()+"',?,?,"+"'104'"+",'','"+teacherID+"','"+totalComment+"');");
				pstmt.setDate(1, (new java.sql.Date(System.currentTimeMillis())));
				pstmt.setTime(2, (new java.sql.Time(System.currentTimeMillis())));
				pstmt.execute();	
				break;
			case 5://physical
				pstmt = conn.prepareStatement("INSERT INTO emp_healthcheckdata (id, H_date, " +
						"H_time, H_type, H_detail, H_collectorid, H_comment)"+
				" VALUES('"+stu.getId()+"',?,?,"+"'105'"+",'','"+teacherID+"','"+totalComment+"');");
				pstmt.setDate(1, (new java.sql.Date(System.currentTimeMillis())));
				pstmt.setTime(2, (new java.sql.Time(System.currentTimeMillis())));
				//System.out.println(pstmt5);
				pstmt.execute();	
				break;
			case 6://total comment
				pstmt = conn.prepareStatement("INSERT INTO emp_healthcheckdata (id, H_date, " +
						"H_time, H_type, H_detail, H_collectorid, H_comment)"+
				" VALUES('"+stu.getId()+"',?,?,"+"'106'"+",'','"+teacherID+"','"+totalComment+"');");
				pstmt.setDate(1, (new java.sql.Date(System.currentTimeMillis())));
				pstmt.setTime(2, (new java.sql.Time(System.currentTimeMillis())));
				pstmt.execute();	
				break;
			case 7://medical name
				pstmt = conn.prepareStatement("INSERT INTO emp_healthcheckdata (id, H_date, " +
						"H_time, H_type, H_detail, H_collectorid, H_comment)"+
				" VALUES('"+stu.getId()+"',?,?,"+"'107','"+medicalName+"','"+teacherID+"','"+medicalReason+eatWay+"');");
				pstmt.setDate(1, (new java.sql.Date(System.currentTimeMillis())));
				pstmt.setTime(2, (new java.sql.Time(System.currentTimeMillis())));
				pstmt.execute();
				break;
			case 9://medical reason
				pstmt = conn.prepareStatement("INSERT INTO emp_healthcheckdata (id, H_date, " +
						"H_time, H_type, H_detail, H_collectorid, H_comment)"+
				" VALUES('"+stu.getId()+"',?,?,"+"'109'"+",?,'"+teacherID+"','"+totalComment+"');");
				pstmt.setDate(1, (new java.sql.Date(System.currentTimeMillis())));
				pstmt.setTime(2, (new java.sql.Time(System.currentTimeMillis())));
				pstmt.setString(3, absenceReason);
				pstmt.execute();
				break;
			default: ;
		}	
		pstmt.close();
		conn.close();
    }
    
    public List<String> getMedicineNames(){
    	List<String> name_list = new LinkedList<String>();
    	try {
			Connection conn = connect();
			PreparedStatement p = null;
			ResultSet r;
			p = conn.prepareStatement("select * from emp_medicine");
			r = p.executeQuery();
			while(r.next())
			{
				String name = r.getString("m_name");
				name_list.add(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name_list;
    }
   
    public JPanel getMedicinePanel()
    {
    	medcinePanel.setLayout(new GridLayout(6,1));
    	JPanel infoPanel = new JPanel();
    	infoPanel.setLayout(new GridLayout(4,2));
    	
    	JPanel stuPanel = new JPanel();
    	stuPanel.setLayout(new GridLayout(1,2));
  		JLabel nameLabel = new JLabel("����������");
  		JLabel babyName = new JLabel(stu.getCompanyAddressBookName());
  		stuPanel.add(nameLabel);
  		stuPanel.add(babyName);
    	
    	JPanel namePanel = new JPanel();
    	namePanel.setLayout(new GridLayout(2,2));
    	jb = new JComboBox();
    	jb.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int index = ((JComboBox)e.getSource()).getSelectedIndex();//��ȡ��ѡ���������
				String Item = ((JComboBox)e.getSource()).getSelectedItem().toString();//��ȡ����
				if(Item.equals("����"))
				{
					medNameField.setEditable(true);
				}
				
				else
				{
					medNameField.setEditable(false);
				}
			}
    		
    	});
		jb.addItem("");
		//jb.setFont(font1);
		List<String> name_list = getMedicineNames();
		for(int i=0;i<name_list.size();i++)
		{
			jb.addItem(name_list.get(i));
		}
    	JLabel name = new JLabel("ҩ����");
    	//medNameField = new JTextField();
    	medNameField.setEditable(false);
    	namePanel.add(name);
    	namePanel.add(jb);
    	namePanel.add(new JLabel(""));
    	namePanel.add(medNameField);
    	
    	JPanel reasonPanel = new JPanel();
    	reasonPanel.setLayout(new GridLayout(1,2));
    	JLabel reason = new JLabel("����");
    	medReasonF = new JTextField();
    	reasonPanel.add(reason);
    	reasonPanel.add(medReasonF);
 
    	JPanel wayPanel = new JPanel();
    	wayPanel.setLayout(new GridLayout(1,2));
    	JLabel labelTake = new JLabel("���÷�ʽ");
    	wayF = new JTextField();
    	wayPanel.add(labelTake);
    	wayPanel.add(wayF);
    	
    	JPanel operatorPanel = new JPanel();
  	   	JLabel operateLabel = new JLabel("������:");
  	   	cbg_operator = new CheckboxGroup(); //������ 
  	   	operatorPanel.add(operateLabel);
  	    for(EmployeeCardVO teacher :teachers)
  	    {
  	    	String n = teacher.getCompanyAddressBookName();
  	    	Checkbox tc =new Checkbox(n, cbg_operator, false);
  	    	tc.setName(teacher.getId());
  	    	operatorPanel.add(tc); //���嵥ѡ����ӵ����� 
  	    }
  	    
    	JPanel buttonPanel = new JPanel();
    	JButton button1 = new JButton("�ύ");
  	    button1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					submitHandler_med();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
  	    });
  	    
  	    JButton button2 = new JButton("�˳�");
  	    button2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				instence.dispose();
			}  	    	
  	    });
  	    buttonPanel.add(button1);
  	    buttonPanel.add(button2);
    	medcinePanel.add(stuPanel);
    	medcinePanel.add(namePanel);
    	medcinePanel.add(reasonPanel);
    	medcinePanel.add(wayPanel);
    	medcinePanel.add(operatorPanel);
    	medcinePanel.add(BorderLayout.SOUTH,buttonPanel);
    	return medcinePanel;
    }
    /*
    public static void main(String[] args) 
    {
    	allCheck test = new allCheck();
    }*/
    
    public static allCheck getInstence()
    {
    	return instence;
    }
    
    public static void close()
    {
    	instence.dispose();
    	instence =null;
    	
    }
} 