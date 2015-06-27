package kyHRUI.Stuff;


import bb.app.lite2.Lite2MainUI;
import bb.common.*;
import bb.gui.*;
import bb.gui.base.*;
import bb.gui.hr.AddEmployeeToTempPane;

import bb.gui.hr.EmployeeHomeInfoUI;
import bb.gui.hr.EmployeePrintGenerator;
import bb.gui.hr.EmployeePrintSettingPane;
import bb.gui.hr.EmployeeUITrainingRecordPane;
import bb.gui.hr.HumanResourceUtil;
import bb.gui.print.*;
import bb.gui.server.HRServerActionManager;
import bb.gui.swing.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.*;

// Referenced classes of package bb.gui.hr:
//            EmployeeUIGeneralPane, EmployeeUITrainingRecordPane, EmployeePrintSettingPane, EmployeePrintGenerator, 
//            EmployeeAddPane, HumanResourceUtil, PictureLabel, EmployeeActionManager, 
//            EmployeeHomeInfoUI, TrainingRecordTable, JobExperienceTable, EducationExperienceTable, 
//            TrainingTable, AddEmployeeToTempPane

public class StuffUI extends ClientUI implements UpdateableUI,
		PrintableDetailUI {

	public StuffUI() {
		mainPane = new JPanel();
		windowButtonPane = ButtonPaneFactory
				.createButtonPane("help,print,refresh@false,edit,add,lookup|close");
		tab = new JTabbedPane();
		btnAddtoTemp = new BB2UIButton(IconStore.COPY_ICON);
		mainPaneLayout = new BorderLayout();
		generalPane = new StuffUIGeneralPane();
		//trainingPane = new EmployeeUITrainingRecordPane(this);
		PrintSettingPane = null;
		vo = null;
		imagePane = new JPanel();
		borderLayout3 = new BorderLayout();
		centerPane = new JPanel();
		borderLayout4 = new BorderLayout();
		btnShowHome = new BB2UIButton(
				HumanResourceUtil.getString("EmployeeUI.HomeInformation"));
		btnShowTrain = new BB2UIButton(
				HumanResourceUtil.getString("EmployeeUI.TrainingRecord"));
		showTrain = false;
		init();
		ClientUtil.setHighlightComponent(generalPane.txtID);
	}

	public PrintSettingPane getConcretSettingPane() {
		PrintSettingPane = new EmployeePrintSettingPane(this, showTrain, vo);
		return PrintSettingPane;
	}

	public TemplateGenerator getConcretGenerator() {
		return new EmployeePrintGenerator(PrintSettingPane);
	}

	public Object retrieveVO(Object key) throws ServerActionException {
		String pk = null;
		if (key == null)
			pk = vo.getId();
		else
			pk = (String) key;
		EmployeeCardVO tempvo = HRServerActionManager.getInstance()
				.getEmployeeCardById(pk);
		if (tempvo != null) {
			vo = tempvo;
			return vo;
		} else {
			return null;
		}
	}

	public Object getLookUpPK(String text) {
		return text;
	}

	public void setVO(final EmployeeCardVO vo) {
		this.vo = vo;
		//final EmployeeUITrainingRecordPane trainingPane = new EmployeeUITrainingRecordPane(this);
		if (vo != null) {
			windowButtonPane.getLookUpPane().setLookupText(vo.getId());
			String title = "";
			if (ClientContext.getLanguageCode().equalsIgnoreCase(
					ClientConst.DEFAULT_LOCALE.toString())) {
				String key = "EmployeeUI.ENTitle";
				String args[] = { vo.getFirstName(), vo.getLastName() };
				title = HumanResourceUtil.getString(key, args);
			} else {
				String key = "EmployeeUI.CNTitle";
				String args[] = { vo.getLastName(), vo.getFirstName() };
				title = HumanResourceUtil.getString(key, args);
			}
			setTitle(title);
			String level = "";
			if (vo.getSecurityLevel() != null
					&& !vo.getSecurityLevel().trim().isEmpty())
				level = HumanResourceUtil.getString((new StringBuilder())
						.append("UWEmployeeGeneralPane.")
						.append(vo.getSecurityLevel().replaceAll(" ", ""))
						.toString());
			String status = "";
			if (vo.getStatus() != null && !vo.getStatus().trim().isEmpty())
				status = HumanResourceUtil.getString((new StringBuilder())
						.append("EmployeeStatusComboBox.")
						.append(vo.getStatus().replaceAll(" ", "")
								.replaceAll("/", "")).toString());
			String pictureTitle = (new StringBuilder())
					.append("<html><center>").append(status).append("<br>")
					.append(level).toString();
			generalPane.lbPicture.setPicture(pictureTitle,
					BB2Util.getImageIconFromByteArray(vo.getPicture()));
			generalPane.txtShowMobile.setText(vo.getShowMobile());
			generalPane.txtShowHomePhone.setText(vo.getShowHomePhone());
			generalPane.txtShowFax.setText(vo.getShowFax());
			generalPane.txtID.setText(vo.getId());
			generalPane.txtLastName.setText(vo.getLastName());
			generalPane.txtMiddleName.setText(vo.getMiddleName());
			generalPane.txtFirstName.setText(vo.getFirstName());
			generalPane.txtBirth.setText(vo.getBirthDate());
			String gender = "";
			if (vo.isGender())
				gender = HumanResourceUtil
						.getString("EmployeePrintGenerator.Male");
			else
				gender = HumanResourceUtil
						.getString("EmployeePrintGenerator.Female");
			generalPane.txtGender.setText(gender);
			generalPane.txtWorkAddress.setText(vo.getWorkAddress());
			generalPane.txtDepartment.setText(vo.getDepartment());
			generalPane.txtManager.setText(vo.getManager());
			generalPane.txtPosition.setText(vo.getPosition());
			generalPane.txtPhone.setText(vo.getPhone());
			generalPane.txtExtension.setText(vo.getExtension());
			generalPane.txtMobile.setText(vo.getMobile());
			generalPane.txtFax.setText(vo.getFax());
			generalPane.txtContractExpires.setText(vo.getContractExpires());
			generalPane.txtIDCardNumber.setText(vo.getIdCard());
			generalPane.txtEmail.setText(vo.getEmail());
			generalPane.txtBirthPlace.setText(vo.getBirthPlace());
			generalPane.txtEducation.setText(vo.getEducation());
			generalPane.txtSource.setText(vo.getSource());
			generalPane.txtComments.setText(vo.getComments());
			generalPane.txtTransferDate.setText(vo.getTransferDate());
			generalPane.btnEmail.setEmail(vo.getEmail());
			if (ClientUtil.isNullObject(generalPane.txtEmail.getText().trim()))
				generalPane.btnEmail.setEnabled(false);
			else
				generalPane.btnEmail.setEnabled(true);
			generalPane.txtStartDate.setText(ClientUtil.getDateLocalString(vo
					.getStartDate()));
			generalPane.txtTerminatedDate.setText(ClientUtil
					.getDateLocalString(vo.getTerminatedDate()));
			generalPane.txtUserName.setText(vo.getUserName());
			generalPane.txtCompanyAddressBookName.setText(vo
					.getCompanyAddressBookName());
			generalPane.txtListInCompanyAddressBook.setText(vo
					.getListInAddressBook());
			btnShowHome.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						EmployeeHomeVO homeInfoVO = HRServerActionManager
								.getInstance().getEmployeeHomeByPk(vo.getId());
						if (homeInfoVO == null) {
							homeInfoVO = new EmployeeHomeVO();
							homeInfoVO.setId(vo.getId());
						}
						EmployeeHomeInfoUI ui = new EmployeeHomeInfoUI();
						ui.setVO(homeInfoVO);
						AbstractMainUI.getInstance().showTab(ui,
								StuffUI.this);
					} catch (ServerActionException e1) {
						ClientUtil.showException(e1);
					}
				}

			});
			btnShowTrain.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						java.util.Collection train = HRServerActionManager
								.getInstance().getEmployeeRecordsOfEmployee(
										vo.getId());
						java.util.Collection job = HRServerActionManager
								.getInstance().getEWEByEmployeeId(vo.getId());
						java.util.Collection experience = HRServerActionManager
								.getInstance().getEEByEmployeeId(vo.getId());
						java.util.Collection trainColl = HRServerActionManager
								.getInstance().getEmployeeTrainingById(
										vo.getId());
						
						/*
						tab.add(trainingPane, HumanResourceUtil
								.getString("EmployeeUI.TrainingRecord"));
						tab.setSelectedComponent(trainingPane);
						trainingPane.table.installTableData(train);
						trainingPane.jobTable.installTableData(job);
						trainingPane.educationTable
								.installTableData(experience);
						trainingPane.trainable.installTableData(trainColl);*/
						showTrain = false;
						btnShowTrain.setVisible(false);
						ClientUtil.setupWindow(StuffUI.this);
					} catch (ServerActionException e1) {
						ClientUtil.showException(e1);
						btnShowHome.setEnabled(false);
					}
				}


			});
			ClientUtil.setupWindow(this);
		}
	}

	private void init() {
		setLayout(new BorderLayout());
		mainPane.setLayout(mainPaneLayout);
		mainPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		imagePane.setLayout(borderLayout3);
		centerPane.setLayout(borderLayout4);
		mainPane.add(tab, "Center");
		tab.add(generalPane, HumanResourceUtil.getString("EmployeeUI.General"));
		add(windowButtonPane, "South");
		add(centerPane, "Center");
		centerPane.add(mainPane, "Center");
		windowButtonPane.getLeftButtonPane().add(btnShowHome);
		windowButtonPane.getLeftButtonPane().add(btnShowTrain);
		btnAddtoTemp.setText(HumanResourceUtil
				.getString("Employee.ChangeToTemporaryWorker"));
		btnAddtoTemp.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String id = "";
				try {
					id = HRServerActionManager.getInstance().getNextEmployeeId(
							true);
				} catch (ServerActionException se) {
					ClientUtil.showException(se);
					return;
				}
				ClientUI ui = AddUI.createAddDialog(new AddEmployeeToTempPane(
						vo.getId(), id), HumanResourceUtil
						.getString("EmployeeActionManager.AddanTemporaryCard"));
				AbstractMainUI.getInstance().showTab(ui);
			}


		});
		if (!(AbstractMainUI.getInstance() instanceof Lite2MainUI))
			windowButtonPane.getLeftButtonPane().add(btnAddtoTemp);
	}

	public ClientUI getUpdateUI() {
		return StuffActionManager.createUpdateUI(this, vo.getId());
	}

	public ClientUI getAddUI() {
		return AddUI.createAddDialog(new StuffAddPane(), HumanResourceUtil
				.getString("EmployeeActionManager.AddanEmployeeCard"));
	}

	public Object[] getTemplatePrintData() {
		return (new Object[] { vo });
	}

	JPanel mainPane;
	ButtonPane windowButtonPane;
	JTabbedPane tab;
	BB2UIButton btnAddtoTemp;
	BorderLayout mainPaneLayout;
	StuffUIGeneralPane generalPane;
	EmployeeUITrainingRecordPane trainingPane;
	EmployeePrintSettingPane PrintSettingPane;
	EmployeeCardVO vo;
	JPanel imagePane;
	BorderLayout borderLayout3;
	JPanel centerPane;
	BorderLayout borderLayout4;
	JButton btnShowHome;
	JButton btnShowTrain;
	boolean showTrain;
}

