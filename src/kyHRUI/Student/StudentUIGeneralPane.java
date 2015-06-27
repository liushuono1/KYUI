package kyHRUI.Student;


import bb.gui.hr.HumanResourceUtil;
import bb.gui.hr.PictureLabel;
import bb.gui.swing.*;

import java.awt.BorderLayout;

import javax.swing.*;

// Referenced classes of package bb.gui.hr:
//            PictureLabel, HumanResourceUtil

public class StudentUIGeneralPane extends JPanel {

	public StudentUIGeneralPane() {
		super(false);
		isTemp = false;
		mainPane = new JPanel();
		headerPane = new TabPaneHeader();
		lbFirstName = new JLabel();
		lbWorkAddress = new JLabel();
		txtFirstName = new DisplayTextField();
		lbID = new JLabel();
		txtID = new DisplayTextField();
		lbMiddleName = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.MiddleName"));
		txtMiddleName = new DisplayTextField();
		lbLastName = new JLabel();
		txtLastName = new DisplayTextField();
		lbBirth = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Birthday"));
		lbGender = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Gender"));
		txtBirth = new DisplayTextField();
		txtGender = new DisplayTextField();
		txtWorkAddress = new DisplayTextField();
		lbDepartment = new JLabel();
		txtDepartment = new DisplayTextField();
		lbPosition = new JLabel("学生身份");
		txtPosition = new DisplayTextField();
		lbManager = new JLabel("家长姓名");
				//HumanResourceUtil.getString("UWEmployeeGeneralPane.Manager"));
		txtManager = new DisplayTextField();
		lbPhone = new JLabel();
		txtPhone = new DisplayTextField();
		lbExtension = new JLabel();
		txtExtension = new DisplayTextField();
		lbEmail = new JLabel();
		emailPane = new JPanel(new BorderLayout());
		txtEmail = new DisplayTextField();
		btnEmail = new EmailButton();
		lbStartDate = new JLabel();
		txtStartDate = new DisplayTextField();
		lbUserName = new JLabel();
		txtUserName = new DisplayTextField();
		lbMobile = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Mobile"));
		txtMobile = new DisplayTextField();
		lbFax = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Fax"));
		txtFax = new DisplayTextField();
		lbContractExpires = new JLabel(
				HumanResourceUtil
						.getString("EmployeeUIGeneralPane.ContractExpires"));
		txtContractExpires = new DisplayTextField();
		lbIDCardNumber = new JLabel("宝宝"+
				HumanResourceUtil.getString("EmployeeUIGeneralPane.IDCard"));
		txtIDCardNumber = new DisplayTextField();
		lbCompanyAddressBookName = new JLabel("宝宝全名");
			//	HumanResourceUtil
				//		.getString("EmployeeUIGeneralPane.CompanyAddressBookName"));
		txtCompanyAddressBookName = new DisplayTextField();
		lbListInCompanyAddressBook = new JLabel(
				HumanResourceUtil
						.getString("EmployeeUIGeneralPane.ListInCompanyAddressBook"));
		txtListInCompanyAddressBook = new YesNoTextField();
		lbTerminatedDate = new JLabel(
				HumanResourceUtil
						.getString("EmployeeUIGeneralPane.TerminatedDate"));
		txtTerminatedDate = new DisplayTextField();
		lbBirthPlace = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.BirthPlace"));
		txtBirthPlace = new DisplayTextField();
		lbEducation = new JLabel("家长教育水平");
				//HumanResourceUtil.getString("EmployeeUIGeneralPane.Education"));
		txtEducation = new DisplayTextField();
		lbSource = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Source"));
		txtSource = new DisplayTextField();
		lbComments = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Comments"));
		txtComments = new JTextAreaLabel();
		lbTransferDate = new JLabel(
				HumanResourceUtil
						.getString("EmployeeUIGeneralPane.TransferDate"));
		txtTransferDate = new DisplayTextField();
		tabPaneLayout = new TableLayoutEx(columns, rows, 5, 0);
		optionPane = new JPanel(tabPaneLayout);
		txtShowHomePhone = new YesNoTextField();
		txtShowMobile = new YesNoTextField();
		txtShowFax = new YesNoTextField();
		lbPicture = new PictureLabel();
		init();
	}

	public StudentUIGeneralPane(boolean isTemp) {
		this.isTemp = false;
		mainPane = new JPanel();
		headerPane = new TabPaneHeader();
		lbFirstName = new JLabel();
		lbWorkAddress = new JLabel();
		txtFirstName = new DisplayTextField();
		lbID = new JLabel();
		txtID = new DisplayTextField();
		lbMiddleName = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.MiddleName"));
		txtMiddleName = new DisplayTextField();
		lbLastName = new JLabel();
		txtLastName = new DisplayTextField();
		lbBirth = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Birthday"));
		lbGender = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Gender"));
		txtBirth = new DisplayTextField();
		txtGender = new DisplayTextField();
		txtWorkAddress = new DisplayTextField();
		lbDepartment = new JLabel();
		txtDepartment = new DisplayTextField();
		lbPosition = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Position"));
		txtPosition = new DisplayTextField();
		lbManager = new JLabel(
				HumanResourceUtil.getString("UWEmployeeGeneralPane.Manager"));
		txtManager = new DisplayTextField();
		lbPhone = new JLabel();
		txtPhone = new DisplayTextField();
		lbExtension = new JLabel();
		txtExtension = new DisplayTextField();
		lbEmail = new JLabel();
		emailPane = new JPanel(new BorderLayout());
		txtEmail = new DisplayTextField();
		btnEmail = new EmailButton();
		lbStartDate = new JLabel();
		txtStartDate = new DisplayTextField();
		lbUserName = new JLabel();
		txtUserName = new DisplayTextField();
		lbMobile = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Mobile"));
		txtMobile = new DisplayTextField();
		lbFax = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Fax"));
		txtFax = new DisplayTextField();
		lbContractExpires = new JLabel(
				HumanResourceUtil
						.getString("EmployeeUIGeneralPane.ContractExpires"));
		txtContractExpires = new DisplayTextField();
		lbIDCardNumber = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.IDCard"));
		txtIDCardNumber = new DisplayTextField();
		lbCompanyAddressBookName = new JLabel(
				HumanResourceUtil
						.getString("EmployeeUIGeneralPane.CompanyAddressBookName"));
		txtCompanyAddressBookName = new DisplayTextField();
		lbListInCompanyAddressBook = new JLabel(
				HumanResourceUtil
						.getString("EmployeeUIGeneralPane.ListInCompanyAddressBook"));
		txtListInCompanyAddressBook = new YesNoTextField();
		lbTerminatedDate = new JLabel(
				HumanResourceUtil
						.getString("EmployeeUIGeneralPane.TerminatedDate"));
		txtTerminatedDate = new DisplayTextField();
		lbBirthPlace = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.BirthPlace"));
		txtBirthPlace = new DisplayTextField();
		lbEducation = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Education"));
		txtEducation = new DisplayTextField();
		lbSource = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Source"));
		txtSource = new DisplayTextField();
		lbComments = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Comments"));
		txtComments = new JTextAreaLabel();
		lbTransferDate = new JLabel(
				HumanResourceUtil
						.getString("EmployeeUIGeneralPane.TransferDate"));
		txtTransferDate = new DisplayTextField();
		tabPaneLayout = new TableLayoutEx(columns, rows, 5, 0);
		optionPane = new JPanel(tabPaneLayout);
		txtShowHomePhone = new YesNoTextField();
		txtShowMobile = new YesNoTextField();
		txtShowFax = new YesNoTextField();
		lbPicture = new PictureLabel();
		this.isTemp = isTemp;
		init();
	}

	private void init() {
		setLayout(new BorderLayout());
		lbFirstName.setText(HumanResourceUtil
				.getString("EmployeeUIGeneralPane.EmployeeFirstName"));
		lbWorkAddress.setText("家庭住址");//HumanResourceUtil
				//.getString("EmployeeUIGeneralPane.EmployeeWorkAddress"));
		lbID.setText("学生编号");//HumanResourceUtil
				//.getString("EmployeeUIGeneralPane.EmployeeIDNumber"));
		lbLastName.setText(HumanResourceUtil
				.getString("EmployeeUIGeneralPane.EmployeeLastName"));
		lbDepartment.setText("班级");//HumanResourceUtil
				//.getString("EmployeeUIGeneralPane.EmployeeDepartment"));
		lbPhone.setText(HumanResourceUtil
				.getString("EmployeeUIGeneralPane.EmployeePhone"));
		lbExtension.setText("电话");//HumanResourceUtil
				//.getString("EmployeeUIGeneralPane.EmployeeExtension"));
		lbEmail.setText(HumanResourceUtil
				.getString("EmployeeUIGeneralPane.EmployeeEmail"));
		lbStartDate.setText(HumanResourceUtil
				.getString("EmployeeUIGeneralPane.EmployeeStartDate"));
		lbUserName.setText(HumanResourceUtil
				.getString("EmployeeUIGeneralPane.EmployeeSystemUserName"));
		add(headerPane, "North");
		createPane();
	}

	private void createPane() {
		JPanel mainPanel = new JPanel();
		mainPane.setLayout(new TableLayoutEx(new double[] { -1D, -2D },
				new double[] { -2D }, 5, 0));
		JPanel centerPanel = new JPanel();
		headerPane.setTitle(HumanResourceUtil
				.getString("EmployeeUIGeneralPane.TemporaryWorkerInfomation"));
		centerPanel.setLayout(new TableLayoutEx(new double[] { -2D, -1D, -2D,
				-1D }, new double[] { -2D, -2D, -2D, -2D, -2D, -2D, -2D, -2D,
				-2D, -2D, -2D, -2D, -2D, -2D, -2D, -2D, -2D }, 5, 5));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		centerPanel.add(lbID, "0,0");
		centerPanel.add(txtID, "1,0");
		centerPanel.add(lbLastName, "2,0");
		centerPanel.add(txtLastName, "3,0");
		centerPanel.add(lbMiddleName, "0,1");
		centerPanel.add(txtMiddleName, "1,1");
		centerPanel.add(lbFirstName, "2,1");
		centerPanel.add(txtFirstName, "3,1");
		centerPanel.add(lbUserName, "0,2");
		centerPanel.add(txtUserName, "1,2");
		centerPanel.add(lbBirth, "0,3");
		centerPanel.add(txtBirth, "1,3");
		centerPanel.add(lbGender, "2,2");
		centerPanel.add(txtGender, "3,2");
		if (!isTemp) {
			centerPanel.add(lbEducation, "0,4");
			centerPanel.add(txtEducation, "1,4");
		}
		if (isTemp) {
			centerPanel.add(lbIDCardNumber, "0,4");
			centerPanel.add(txtIDCardNumber, "1,4");
		} else {
			centerPanel.add(lbIDCardNumber, "2,3");
			centerPanel.add(txtIDCardNumber, "3,3");
		}
		if (!isTemp) {
			centerPanel.add(lbBirthPlace, "0,5");
			centerPanel.add(txtBirthPlace, "1,5");
		}
		if (isTemp) {
			centerPanel.add(lbMobile, "2,3");
			centerPanel.add(txtMobile, "3,3");
		} else {
			centerPanel.add(lbMobile, "2,4");
			centerPanel.add(txtMobile, "3,4");
		}
		centerPanel.add(lbEmail, "2,5");
		emailPane.add(txtEmail, "Center");
		emailPane.add(btnEmail, "East");
		centerPanel.add(emailPane, "3,5");
		centerPanel.add(lbWorkAddress, "0,6");
		centerPanel.add(txtWorkAddress, "1,6");
		centerPanel.add(lbDepartment, "2,6");
		centerPanel.add(txtDepartment, "3,6");
		centerPanel.add(lbManager, "0,7");
		centerPanel.add(txtManager, "1,7");
		centerPanel.add(lbPosition, "2,7");
		centerPanel.add(txtPosition, "3,7");
		centerPanel.add(lbPhone, "0,8");
		centerPanel.add(txtPhone, "1,8");
		centerPanel.add(lbExtension, "2,8");
		centerPanel.add(txtExtension, "3,8");
		centerPanel.add(lbFax, "0,9");
		centerPanel.add(txtFax, "1,9");
		centerPanel.add(lbContractExpires, "2,9");
		centerPanel.add(txtContractExpires, "3,9");
		centerPanel.add(lbStartDate, "0,10");
		centerPanel.add(txtStartDate, "1,10");
		centerPanel.add(lbTerminatedDate, "2,10");
		centerPanel.add(txtTerminatedDate, "3,10");
		if (!isTemp) {
			centerPanel.add(lbTransferDate, "0,11");
			centerPanel.add(txtTransferDate, "1,11");
			centerPanel.add(lbSource, "2,11");
			centerPanel.add(txtSource, "3,11");
			centerPanel.add(lbListInCompanyAddressBook, "0,12");
			centerPanel.add(txtListInCompanyAddressBook, "1,12");
			centerPanel.add(lbCompanyAddressBookName, "2,12");
			centerPanel.add(txtCompanyAddressBookName, "3,12");
			centerPanel.add(
					new JLabel(HumanResourceUtil
							.getString("EmployeeUIGeneralPane.ShowHomePhone")),
					"0,13");
			centerPanel.add(txtShowHomePhone, "1,13");
			centerPanel.add(
					new JLabel(HumanResourceUtil
							.getString("EmployeeUIGeneralPane.ShowMobile")),
					"2,13");
			centerPanel.add(txtShowMobile, "3,13");
			centerPanel.add(
					new JLabel(HumanResourceUtil
							.getString("EmployeeUIGeneralPane.ShowFax")),
					"0,14");
			centerPanel.add(txtShowFax, "1,14");
			centerPanel.add(lbComments, "0,15");
			centerPanel.add(txtComments, "1,15,3,15");
			headerPane.setTitle(HumanResourceUtil
					.getString("EmployeeUIGeneralPane.Title"));
		} else {
			centerPanel.add(lbComments, "0,11");
			centerPanel.add(txtComments, "1,11,3,11");
		}
		mainPane.add(centerPanel, "0,0");
		mainPane.add(lbPicture, "1,0");
		add(mainPane, "Center");
	}

	boolean isTemp;
	JPanel mainPane;
	TabPaneHeader headerPane;
	JLabel lbFirstName;
	JLabel lbWorkAddress;
	JTextField txtFirstName;
	JLabel lbID;
	JTextField txtID;
	JLabel lbMiddleName;
	DisplayTextField txtMiddleName;
	JLabel lbLastName;
	JTextField txtLastName;
	JLabel lbBirth;
	JLabel lbGender;
	DisplayTextField txtBirth;
	DisplayTextField txtGender;
	JTextField txtWorkAddress;
	JLabel lbDepartment;
	JTextField txtDepartment;
	JLabel lbPosition;
	DisplayTextField txtPosition;
	JLabel lbManager;
	DisplayTextField txtManager;
	JLabel lbPhone;
	JTextField txtPhone;
	JLabel lbExtension;
	JTextField txtExtension;
	JLabel lbEmail;
	JPanel emailPane;
	JTextField txtEmail;
	EmailButton btnEmail;
	JLabel lbStartDate;
	JTextField txtStartDate;
	JLabel lbUserName;
	JTextField txtUserName;
	JLabel lbMobile;
	DisplayTextField txtMobile;
	JLabel lbFax;
	DisplayTextField txtFax;
	JLabel lbContractExpires;
	DisplayTextField txtContractExpires;
	JLabel lbIDCardNumber;
	DisplayTextField txtIDCardNumber;
	JLabel lbCompanyAddressBookName;
	DisplayTextField txtCompanyAddressBookName;
	JLabel lbListInCompanyAddressBook;
	YesNoTextField txtListInCompanyAddressBook;
	JLabel lbTerminatedDate;
	DisplayTextField txtTerminatedDate;
	JLabel lbBirthPlace;
	DisplayTextField txtBirthPlace;
	JLabel lbEducation;
	DisplayTextField txtEducation;
	JLabel lbSource;
	DisplayTextField txtSource;
	JLabel lbComments;
	JTextAreaLabel txtComments;
	JLabel lbTransferDate;
	DisplayTextField txtTransferDate;
	double rows[] = { -2D };
	double columns[] = { -2D, -1D, -2D, -1D, -2D, -1D };
	TableLayoutEx tabPaneLayout;
	JPanel optionPane;
	YesNoTextField txtShowHomePhone;
	YesNoTextField txtShowMobile;
	YesNoTextField txtShowFax;
	PictureLabel lbPicture;
}

