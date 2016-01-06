package kyHRUI.Stuff;


import bb.common.*;
import bb.gui.*;
import bb.gui.base.DepartmentComboBox;
import bb.gui.base.MultiLanguageComboBox;
import bb.gui.hr.EmployeeStatusComboBox;
import bb.gui.hr.HumanResourceUtil;
import bb.gui.server.SecurityServerActionManager;
import bb.gui.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

// Referenced classes of package bb.gui.hr:
//            EmployeeStatusComboBox, HumanResourceUtil

public class UWEmployeeStuffPane extends JPanel implements UpdateStep {

	public UWEmployeeStuffPane(EmployeeCardVO vo) {
		layout = new BorderLayout();
		this.vo = null;
		lbImage = new JLabel();
		mainPane = new JPanel();
		mainPaneLayout = new BorderLayout();
		lbWorkPhone = new JLabel();
		lbWorkAddress = new JLabel();
		infoPane = new JPanel();
		lbManager = new JLabel("主管姓名 ");
				//HumanResourceUtil.getString("UWEmployeeGeneralPane.Manager"));
		txtManager = new JTextField();
		lbSystemUserName = new JLabel();
		txtPosition = new JTextField();
		txtStatus = new EmployeeStatusComboBox();
		txtStartDate = new JDatePicker();
		txtTerminatedDate = new JDatePicker();
		lbEmployeeID = new JLabel();
		lbStatus = new JLabel();
		txtEmail = new JTextField();
		lbFirstName = new JLabel();
		txtWorkAddress = new JTextField();
		lbLastName = new JLabel();
		lbUploadPicture = new JLabel(
				HumanResourceUtil
						.getString("UWEmployeeGeneralPane.UploadPicture"));
		lbSecurityLevel = new JLabel();
		lbTerminatedDate = new JLabel();
		txtWorkPhone = new JTextField();
		lbPhoneExtension = new JLabel();
		txtFirstName = new JTextField();
		txtFirstName.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				txtCompanyAddressBookName.setText(txtLastName.getText()+txtFirstName.getText());
			}
			
		});
		lbMiddleName = new JLabel(
				HumanResourceUtil.getString("UWEmployeeGeneralPane.MiddleName"));
		txtMiddleName = new JTextField();
		lbStartDate = new JLabel();
		txtLastName = new JTextField();
		txtLastName.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				txtCompanyAddressBookName.setText(txtLastName.getText()+txtFirstName.getText());
			}
			
		});
		txtDepartment = new DepartmentComboBox();
		lbBirth = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Birthday"));
		lbGender = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Gender"));
		txtBirth = new JDatePicker();
		genderPane = new JPanel();
		lbMale = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Male"));
		btMale = new JRadioButton();
		lbFemale = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Female"));
		btFemale = new JRadioButton();
		lbPosition = new JLabel();
		btnUploadPicture = new OpenFileButton();
		picturePanel = new JPanel();
		txtPhoneExtension = new JTextField();
		txtSystemUserName = new JTextField();
		lbMobile = new JLabel(
				HumanResourceUtil.getString("UWEmployeeGeneralPane.Mobile"));
		txtMobile = new JTextField();
		lbFax = new JLabel(
				HumanResourceUtil.getString("UWEmployeeGeneralPane.Fax"));
		txtFax = new JTextField();
		lbIDCardNumber = new JLabel(
				HumanResourceUtil.getString("UWEmployeeGeneralPane.IDCard"));
		txtIDCardNumber = new JTextField();
		lbCompanyAddressBookName = new JLabel();
		txtCompanyAddressBookName = new JTextField();
		
		lbListInCompanyAddressBook = new JLabel("家庭住址");
		txtListInCompanyAddressBook = new YesNoComboBox();
		txtSecurityLevel = new StuffSecurityLevelBox();
		txtSecurityLevel.setSelectedItem(bb.common.Constant.EmployeeSecurityLevel.LEVEL3);
		txtSecurityLevel.setEditable(false);
		txtPicture = new JTextField();
		txtEmployeeID = new DisplayTextField();
		lbEmail = new JLabel();
		lbDepartment = new JLabel();
		this.icon = null;
		txtShowMobile = new YesNoComboBox();
		txtShowHomePhone = new YesNoComboBox();
		txtShowFax = new YesNoComboBox();
		btGroup = new ButtonGroup();
		lbBirthPlace = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.BirthPlace"));
		txtBirthPlace = new JTextField();
		lbEducation = new JLabel("教育程度");
		txtEducation = new JTextField();
		lbSource = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Source"));
		txtSource = new JTextField();
		lbComments = new JLabel(
				HumanResourceUtil.getString("EmployeeUIGeneralPane.Comments"));
		txtComments = new JTextArea(3, 5);
		lbTransferDate = new JLabel(
				HumanResourceUtil
						.getString("EmployeeUIGeneralPane.TransferDate"));
		txtTransferDate = new JDatePicker();
		lbContractExpires = new JLabel(
				HumanResourceUtil
						.getString("EmployeeUIGeneralPane.ContractExpires"));
		txtContractExpires = new JDatePicker();
		picDelPanel = new JPanel(new BorderLayout());
		buttonPicDelete = new BB2UIButton(
				HumanResourceUtil
						.getString("UWEmployeeGeneralPane.Deletepicture"));
		init();
		this.vo = vo;
		if (vo != null) {
			if (vo.getPicture() != null) {
				javax.swing.Icon icon = ClientUtil.getThumbnail(BB2Util
						.getImageIconFromByteArray(vo.getPicture()).getImage(),
						200, 300, Color.white);
				lbImage.setIcon(icon);
			}
			txtEducation.setText(vo.getEducation());
			txtBirthPlace.setText(vo.getBirthPlace());
			txtTerminatedDate.setSelectedDate(vo.getTerminatedDate());
			txtSource.setText(vo.getSource());
			txtDepartment.setSelectedItem(vo.getDepartment());
			txtEmail.setText(vo.getEmail());
			txtEmployeeID.setText(vo.getId());
			txtFirstName.setText(vo.getFirstName());
			txtMiddleName.setText(vo.getMiddleName());
			
			txtLastName.setText(vo.getLastName());
			txtBirth.setSelectedDate(vo.getBirthDate());
			if (vo.isGender())
				btMale.setSelected(true);
			else
				btFemale.setSelected(true);
			txtPhoneExtension.setText(vo.getExtension());
			txtMobile.setText(vo.getMobile());
			txtFax.setText(vo.getFax());
			txtIDCardNumber.setText(vo.getIdCard());
			txtSecurityLevel.setSelectedItem(vo.getSecurityLevel());
			txtStartDate.setSelectedDate(vo.getStartDate());
			txtTerminatedDate.setSelectedDate(vo.getTerminatedDate());
			txtStatus
					.setSelectedItem(vo.getStatus().equalsIgnoreCase("") ? "FULL TIME"
							: ((Object) (vo.getStatus())));
			txtSystemUserName.setText(vo.getUserName());
			txtWorkAddress.setText(vo.getWorkAddress());
			txtWorkPhone.setText(vo.getPhone());
			txtPosition.setText(vo.getPosition());
			txtManager.setText(vo.getManager());
			txtCompanyAddressBookName.setText(vo.getCompanyAddressBookName());
			txtCompanyAddressBookName.setText(txtLastName.getText()+txtFirstName.getText());
			txtListInCompanyAddressBook.setSelectedItem(vo
					.getListInAddressBook());
			txtShowFax.setSelectedItem(vo.getShowFax());
			txtShowMobile.setSelectedItem(vo.getShowMobile());
			txtShowHomePhone.setSelectedItem(vo.getShowHomePhone());
			txtComments.setText(vo.getComments());
			txtContractExpires.setSelectedDate(vo.getContractExpires());
			setCompanyAddressBookEnabled();
		}
	}

	private void init() {
		infoPane.setLayout(new TableLayoutEx(
				new double[] { -2D, -1D, -2D, -1D }, new double[] { -2D, -2D,
						-2D, -2D, -2D, -2D, -2D, -2D, -2D, -2D, -2D, -2D, -2D,
						-2D, -2D, -2D, -2D }, 5, 5));
		picturePanel.setLayout(new BorderLayout());
		setLayout(layout);
		btGroup.add(btMale);
		btGroup.add(btFemale);
		lbImage.setBorder(BorderFactory.createEmptyBorder());
		lbImage.setOpaque(true);
		lbImage.setHorizontalAlignment(0);
		lbImage.setHorizontalTextPosition(0);
		lbImage.setVerticalAlignment(0);
		lbImage.setVerticalTextPosition(3);
		mainPane.setLayout(mainPaneLayout);
		lbWorkPhone.setText("家庭电话");
		lbWorkAddress.setText("家庭住址 ");
		lbSystemUserName.setText(HumanResourceUtil
				.getString("UWEmployeeGeneralPane.SystemUserName"));
		lbEmployeeID.setText("员工编号");
		lbStatus.setText(HumanResourceUtil
				.getString("UWEmployeeGeneralPane.Status"));
		lbFirstName.setText(HumanResourceUtil
				.getString("UWEmployeeGeneralPane.FirstName"));
		lbLastName.setText(HumanResourceUtil
				.getString("UWEmployeeGeneralPane.LastName"));
		lbSecurityLevel.setText("在职状态");
			///	HumanResourceUtil			.getString("UWEmployeeGeneralPane.SecurityLevel"));
		lbPhoneExtension.setText("电话");
		lbStartDate.setText(HumanResourceUtil
				.getString("UWEmployeeGeneralPane.StartDate"));
		lbTerminatedDate.setText(HumanResourceUtil
				.getString("UWEmployeeGeneralPane.TerminatedDate"));
		lbPosition.setText("职位");
		lbEmail.setText(HumanResourceUtil
				.getString("UWEmployeeGeneralPane.E-mail"));
		lbDepartment.setText("部门");
		lbCompanyAddressBookName.setText("全名");
		ClientUtil.setRequisiteLabel(lbLastName);
		ClientUtil.setRequisiteLabel(lbFirstName);
		ClientUtil.setRequisiteLabel(lbSystemUserName);
		createPane();
	}

	@Override
	public void afterActive() {
	}

	public boolean beforeCancel() {
		int answer = CommonUI.showCancelWarningMessage(this);
		return answer == 0;
	}

	@Override
	public boolean beforeFinish() {
		return true;
	}

	public String getDescription() {
		String key = "UWEmployeeGeneralPane.Description";
		String text = HumanResourceUtil.getString(key, vo.getId());
		return text;
	}

	public String getTitle() {
		return "填写员工详细信息";//HumanResourceUtil.getString("UWEmployeeGeneralPane.EmployeeGeneralInformation");
	}

	@Override
	public void doHelp() {
	}

	@Override
	public boolean isCancelable() {
		return true;
	}

	@Override
	public boolean isFinishable() {
		return true;
	}

	public boolean setTerminatedDate() {
		if (ClientUtil.isNullDate(HumanResourceUtil
				.getString("UWEmployeeGeneralPane.TerminatedDate"),
				txtTerminatedDate, txtStatus)) {
			txtTerminatedDate.setSelectedDate(BB2TimeFactory.getBB2Time());
			return false;
		}
		if (txtTerminatedDate.getSelectedDate().compareTo(
				txtStartDate.getSelectedDate()) < 0) {
			CommonUI.showMessage(txtStatus, HumanResourceUtil
					.getString("UWEmployeeGeneralPane.WrongTerminatedDate"));
			txtTerminatedDate.requestFocus();
			txtTerminatedDate.setSelectedDate(BB2TimeFactory.getBB2Time());
			return false;
		} else {
			return true;
		}
	}

	public void createPane() {
		JPanel mainPanel = new JPanel();
		mainPane.setLayout(new TableLayoutEx(new double[] { -1D, -2D },
				new double[] { -2D }, 5, 0));
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new TableLayoutEx(new double[] { -2D, -1D, -2D,
				-1D }, new double[] { -2D, -2D, -2D, -2D, -2D, -2D, -2D, -2D,
				-2D, -2D, -2D, -2D, -2D, -2D, -2D, -2D, -2D }, 5, 5));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		centerPanel.add(lbEmployeeID, "0,0");
		centerPanel.add(txtEmployeeID, "1,0");
		centerPanel.add(lbLastName, "2,0");
		centerPanel.add(txtLastName, "3,0");
		centerPanel.add(lbMiddleName, "0,1");
		centerPanel.add(txtMiddleName, "1,1");
		centerPanel.add(lbFirstName, "2,1");
		centerPanel.add(txtFirstName, "3,1");
		centerPanel.add(lbSystemUserName, "0,2");
		centerPanel.add(txtSystemUserName, "1,2");
		centerPanel.add(lbBirth, "0,3");
		centerPanel.add(txtBirth, "1,3");
		centerPanel.add(lbGender, "0,4");
		centerPanel.add(genderPane, "1,4");
		genderPane.setLayout(new FlowLayout(0));
		genderPane.add(btMale);
		genderPane.add(lbMale);
		genderPane.add(btFemale);
		genderPane.add(lbFemale);
		centerPanel.add(lbEducation, "2,2");
		centerPanel.add(txtEducation, "3,2");
		centerPanel.add(lbIDCardNumber, "2,3");
		centerPanel.add(txtIDCardNumber, "3,3");
		centerPanel.add(lbBirthPlace, "0,5");
		centerPanel.add(txtBirthPlace, "1,5");
		centerPanel.add(lbMobile, "2,4");
		centerPanel.add(txtMobile, "3,4");
		centerPanel.add(lbEmail, "2,6");
		centerPanel.add(txtEmail, "3,6");
		centerPanel.add(lbUploadPicture, "2,5");
		centerPanel.add(picturePanel, "3,5");
		picturePanel.add(txtPicture, "Center");
		picDelPanel.add(btnUploadPicture);
		picDelPanel.add(buttonPicDelete, "East");
		picturePanel.add(picDelPanel, "East");
		centerPanel.add(lbWorkAddress, "0,6");
		centerPanel.add(txtWorkAddress, "1,6");
		centerPanel.add(lbDepartment, "2,7");
		centerPanel.add(txtDepartment, "3,7");
		centerPanel.add(lbManager, "0,7");
		centerPanel.add(txtManager, "1,7");
		centerPanel.add(lbPosition, "2,8");
		centerPanel.add(txtPosition, "3,8");
		centerPanel.add(lbWorkPhone, "0,8");
		centerPanel.add(txtWorkPhone, "1,8");
		centerPanel.add(lbStatus, "2,9");
		centerPanel.add(txtStatus, "3,9");
		centerPanel.add(lbPhoneExtension, "0,9");
		centerPanel.add(txtPhoneExtension, "1,9");
		centerPanel.add(lbSecurityLevel, "2,10");
		centerPanel.add(txtSecurityLevel, "3,10");
		centerPanel.add(lbFax, "0,10");
		centerPanel.add(txtFax, "1,10");
		centerPanel.add(lbContractExpires, "2,11");
		centerPanel.add(txtContractExpires, "3,11");
		centerPanel.add(lbStartDate, "0,11");
		centerPanel.add(txtStartDate, "1,11");
		centerPanel.add(lbTerminatedDate, "2,12");
		centerPanel.add(txtTerminatedDate, "3,12");
		centerPanel.add(lbTransferDate, "0,12");
		centerPanel.add(txtTransferDate, "1,12");
		centerPanel.add(lbSource, "2,13");
		centerPanel.add(txtSource, "3,13");
		centerPanel.add(lbListInCompanyAddressBook, "0,13");
		centerPanel.add(txtListInCompanyAddressBook, "1,13");
		centerPanel.add(lbCompanyAddressBookName, "2,14");
		centerPanel.add(txtCompanyAddressBookName, "3,14");
		centerPanel.add(
				new JLabel(HumanResourceUtil
						.getString("EmployeeUIGeneralPane.ShowHomePhone")),
				"0,14");
		centerPanel.add(txtShowHomePhone, "1,14");
		centerPanel
				.add(new JLabel(HumanResourceUtil
						.getString("EmployeeUIGeneralPane.ShowMobile")), "2,15");
		centerPanel.add(txtShowMobile, "3,15");
		centerPanel.add(
				new JLabel(HumanResourceUtil
						.getString("EmployeeUIGeneralPane.ShowFax")), "0,15");
		centerPanel.add(txtShowFax, "1,15");
		centerPanel.add(lbComments, "0,16");
		centerPanel.add(new JScrollPane(txtComments), "1,16,3,16");
		buttonPicDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonPicDelete_click();
			}

	
		});
		btnUploadPicture.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				icon = FileChooserManager.getSelectedImageIcon(mainPane);
				if (icon != null) {
					txtPicture.setText(icon.getDescription());
					icon = ClientUtil.getThumbnail(icon.getImage(), 200, 300,
							Color.white);
					lbImage.setIcon(icon);
				}
			}


		});
		txtStatus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ("TERMINATED".equals(txtStatus.getSelectedItem()))
					setTerminatedDate();
			}

	
		});
		txtListInCompanyAddressBook.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setCompanyAddressBookEnabled();
			}


		});
		mainPane.add(centerPanel, "0,0");
		mainPane.add(lbImage, "1,0");
		add(mainPane, "Center");
	}

	private void buttonPicDelete_click() {
		vo.setPicture(null);
		lbImage.setIcon(null);
		icon = null;
	}

	@Override
	public boolean beforeLeave() {
		ClientUtil.trimWindowTextFields(this);
		if (ClientUtil.isNullString(
				HumanResourceUtil.getString("UWEmployeeGeneralPane.FirstName"),
				txtFirstName, this))
			return false;
		if (ClientUtil.isNullString(
				HumanResourceUtil.getString("UWEmployeeGeneralPane.LastName"),
				txtLastName, this))
			return false;
		if (ClientUtil.isNullString(HumanResourceUtil
				.getString("UWEmployeeGeneralPane.SystemUserName"),
				txtSystemUserName, this))
			return false;
		if (!"".equals(txtEmail.getText())
				&& !StringUtil.isValidEmailAddress(txtEmail, this))
			return false;
		if (txtListInCompanyAddressBook.getSelectedItem().toString()
				.equalsIgnoreCase("YES")
				&& ClientUtil
						.isNullString(
								HumanResourceUtil
										.getString("UWEmployeeGeneralPane.CompanyAddressBookName"),
								txtCompanyAddressBookName, this))
			return false;
		if ("TERMINATED".equals(txtStatus.getSelectedItem()))
			if (0 == JOptionPane.showConfirmDialog(this, HumanResourceUtil
					.getString("UWEmployeeGeneralPane.lock"), HumanResourceUtil
					.getString("UWEmployeeGeneralPane.LockUser"), 0))
				try {
					SecurityServerActionManager.getInstance()
							.lockUserByEmployeeId(vo.getId());
					CommonUI.showMessage(
							this,
							HumanResourceUtil
									.getString("UWEmployeeGeneralPane.Theuserhasbeenlocked"));
				} catch (ServerActionException e) {
					ClientUtil.showException(e);
					return false;
				}
			else
				return false;
		if (vo != null) {
			if ("TERMINATED".equals(txtStatus.getSelectedItem())) {
				if (setTerminatedDate())
					vo.setTerminatedDate(txtTerminatedDate.getSelectedDate());
				else
					return false;
			} else {
				vo.setTerminatedDate(null);
			}
			vo.setDepartment(txtDepartment.getSelectedItem().toString());
			vo.setEmail(txtEmail.getText());
			vo.setExtension(txtPhoneExtension.getText());
			vo.setPosition(txtPosition.getText());
			vo.setManager(txtManager.getText());
			vo.setFirstName(txtFirstName.getText());
			vo.setMiddleName(txtMiddleName.getText());
			vo.setBirthDate(txtBirth.getDate());
			vo.setBirthPlace(txtBirthPlace.getText());
			vo.setEducation(txtEducation.getText());
			vo.setSource(txtSource.getText());
			vo.setComments(txtComments.getText());
			vo.setTransferDate(txtTransferDate.getDate());
			boolean gender = false;
			if (btMale.isSelected())
				gender = Boolean.TRUE.booleanValue();
			if (btFemale.isSelected())
				gender = Boolean.FALSE.booleanValue();
			vo.setGender(gender);
			vo.setLastName(txtLastName.getText());
			vo.setPhone(txtWorkPhone.getText());
			if (icon != null)
				vo.setPicture(BB2Util.getByteArrayFromImageIcon(icon));
			vo.setFax(txtFax.getText());
			vo.setMobile(txtMobile.getText());
			vo.setIdCard(txtIDCardNumber.getText());
			vo.setSecurityLevel(txtSecurityLevel.getSelectedItem().toString());
			vo.setStartDate(txtStartDate.getSelectedDate());
			vo.setStatus(txtStatus.getSelectedItem().toString());
			vo.setUserName(txtSystemUserName.getText());
			vo.setWorkAddress(txtWorkAddress.getText());
			vo.setCompanyAddressBookName(txtCompanyAddressBookName.getText());
			vo.setListInAddressBook(txtListInCompanyAddressBook
					.getSelectedItem().toString());
			vo.setShowFax(txtShowFax.getSelectedItem().toString());
			vo.setShowHomePhone(txtShowHomePhone.getSelectedItem().toString());
			vo.setShowMobile(txtShowMobile.getSelectedItem().toString());
			vo.setTerminatedDate(txtTerminatedDate.getSelectedDate());
			vo.setContractExpires(txtContractExpires.getSelectedDate());
		}
		return true;
	}

	private void setCompanyAddressBookEnabled() {
		if (txtListInCompanyAddressBook.getSelectedItem().toString()
				.equalsIgnoreCase("YES")) {
			lbCompanyAddressBookName.setText(HumanResourceUtil
					.getString("UWEmployeeGeneralPane.CompanyAddressBookName"));
			ClientUtil.setRequisiteLabel(lbCompanyAddressBookName);
			txtCompanyAddressBookName.setEnabled(true);
			txtCompanyAddressBookName.setEditable(true);
			txtShowHomePhone.setEnabled(true);
			txtShowMobile.setEnabled(true);
			txtShowFax.setEnabled(true);
			txtShowHomePhone.setSelectedItem("YES");
			txtShowMobile.setSelectedItem("YES");
			txtShowFax.setSelectedItem("YES");
		} else {
			lbCompanyAddressBookName.setText(HumanResourceUtil
					.getString("UWEmployeeGeneralPane.CompanyAddressBookName"));
			txtCompanyAddressBookName.setEnabled(false);
			txtCompanyAddressBookName.setEditable(false);
			txtShowHomePhone.setEnabled(false);
			txtShowMobile.setEnabled(false);
			txtShowFax.setEnabled(false);
			txtShowHomePhone.setSelectedItem("NO");
			txtShowMobile.setSelectedItem("NO");
			txtShowFax.setSelectedItem("NO");
		}
	}

	BorderLayout layout;
	EmployeeCardVO vo;
	JLabel lbImage;
	JPanel mainPane;
	BorderLayout mainPaneLayout;
	JLabel lbWorkPhone;
	JLabel lbWorkAddress;
	JPanel infoPane;
	JLabel lbManager;
	JTextField txtManager;
	JLabel lbSystemUserName;
	JTextField txtPosition;
	EmployeeStatusComboBox txtStatus;
	JDatePicker txtStartDate;
	JDatePicker txtTerminatedDate;
	JLabel lbEmployeeID;
	JLabel lbStatus;
	JTextField txtEmail;
	JLabel lbFirstName;
	JTextField txtWorkAddress;
	JLabel lbLastName;
	JLabel lbUploadPicture;
	JLabel lbSecurityLevel;
	JLabel lbTerminatedDate;
	JTextField txtWorkPhone;
	JLabel lbPhoneExtension;
	JTextField txtFirstName;
	JLabel lbMiddleName;
	JTextField txtMiddleName;
	JLabel lbStartDate;
	JTextField txtLastName;
	DepartmentComboBox txtDepartment;
	JLabel lbBirth;
	JLabel lbGender;
	JDatePicker txtBirth;
	JPanel genderPane;
	JLabel lbMale;
	JRadioButton btMale;
	JLabel lbFemale;
	JRadioButton btFemale;
	JLabel lbPosition;
	OpenFileButton btnUploadPicture;
	JPanel picturePanel;
	JTextField txtPhoneExtension;
	JTextField txtSystemUserName;
	JLabel lbMobile;
	JTextField txtMobile;
	JLabel lbFax;
	JTextField txtFax;
	JLabel lbIDCardNumber;
	JTextField txtIDCardNumber;
	JLabel lbCompanyAddressBookName;
	JTextField txtCompanyAddressBookName;
	JLabel lbListInCompanyAddressBook;
	YesNoComboBox txtListInCompanyAddressBook;
	MultiLanguageComboBox txtSecurityLevel;
	JTextField txtPicture;
	JTextField txtEmployeeID;
	JLabel lbEmail;
	JLabel lbDepartment;
	ImageIcon icon;
	YesNoComboBox txtShowMobile;
	YesNoComboBox txtShowHomePhone;
	YesNoComboBox txtShowFax;
	ButtonGroup btGroup;
	JLabel lbBirthPlace;
	JTextField txtBirthPlace;
	JLabel lbEducation;
	JTextField txtEducation;
	JLabel lbSource;
	JTextField txtSource;
	JLabel lbComments;
	JTextArea txtComments;
	JLabel lbTransferDate;
	JDatePicker txtTransferDate;
	JLabel lbContractExpires;
	JDatePicker txtContractExpires;
	JPanel picDelPanel;
	BB2UIButton buttonPicDelete;

}
