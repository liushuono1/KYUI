package kyHRUI.Stuff;

import java.awt.Component;
import java.awt.Dimension;
import java.lang.ref.WeakReference;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import bb.common.CompanyVO;
import bb.common.EmployeeCardVO;
import bb.common.VO;
import bb.gui.ClientUtil;
import bb.gui.CommonUI;
import bb.gui.ServerActionException;
import bb.gui.base.AddActionPane;
import bb.gui.base.ClientUI;
import bb.gui.hr.EmployeeActionManager;
import bb.gui.hr.HumanResourceUtil;
import bb.gui.server.CompanySettingServerActionManager;
import bb.gui.server.HRServerActionManager;
import bb.gui.swing.AbstractFinishAction;
import bb.gui.swing.TableLayout;
import bb.gui.swing.UWCustomPropertiesPane;
import bb.gui.swing.UpdateFinishAction;
import bb.gui.swing.UpdatePane;
import bb.gui.swing.UpdateStep;


public class StuffAddPane extends AddActionPane {

	
	static String classType;
	String stuID;
	public StuffAddPane() {
		borderLayout1 = new TableLayout(5, 5);
		jLabel1 = new JLabel();
		txtId = new JTextField();
		jLabel2 = new JLabel();
		txtLastName = new JTextField();
		jLabe3 = new JLabel();
		txtFirstName = new JTextField();
		jLabe4 = new JLabel();
		txtUserName = new JTextField();
		isAutoAdd = false;
		try {
			CompanyVO vo = CompanySettingServerActionManager.getInstance()
					.getCompanySettings();
			isAutoAdd = vo.isAutoEmployeeId();
			init();
		} catch (Exception ex) {
			ClientUtil.showException(ex);
		}
	}

	
	public StuffAddPane(String id,String classtype) {
		StuffAddPane.classType=classtype;
		this.stuID=id;
		borderLayout1 = new TableLayout(5, 5);
		jLabel1 = new JLabel();
		txtId = new JTextField();
		txtId.setText(id);
		txtId.setEditable(false);
		jLabel2 = new JLabel();
		txtLastName = new JTextField();
		jLabe3 = new JLabel();
		txtFirstName = new JTextField();
		jLabe4 = new JLabel();
		txtUserName = new JTextField();
		isAutoAdd = false;
		try {
			CompanyVO vo = CompanySettingServerActionManager.getInstance()
					.getCompanySettings();
			isAutoAdd = vo.isAutoEmployeeId();
			init();
		} catch (Exception ex) {
			ClientUtil.showException(ex);
		}
	}
	
	
	private void init() {
		jLabel1.setText("职工编号");
		setLayout(borderLayout1);
		jLabel2.setText("姓");
		jLabe3.setText("名字");
		jLabe4.setText(HumanResourceUtil
				.getString("EmployeeAddPane.SystemUserName")+"(拼音)");
		txtLastName.setPreferredSize(new Dimension(120, 22));
		txtFirstName.setPreferredSize(new Dimension(120, 22));
		txtUserName.setPreferredSize(new Dimension(120, 22));
		if (!isAutoAdd) {
			add(jLabel1, null);
			add(txtId, null);
			ClientUtil.setRequisiteLabel(jLabel1);
		}
		add(jLabel2, null);
		add(txtLastName, null);
		add(jLabe3, null);
		add(txtFirstName);
		add(jLabe4);
		add(txtUserName);
		ClientUtil.setRequisiteLabel(jLabel2);
		ClientUtil.setRequisiteLabel(jLabe3);
		ClientUtil.setRequisiteLabel(jLabe4);
	}

	@Override
	public boolean add() throws Exception {
		ClientUtil.trimWindowTextFields(this);
		String id = txtId.getText().trim();
		String lastName = txtLastName.getText().trim();
		String FirstName = txtFirstName.getText().trim();
		String UserName = txtUserName.getText().trim();
		if (!isAutoAdd && id.equals("")) {
			CommonUI.showMessage(this, HumanResourceUtil
					.getString("EmployeeAddPane.PleaseinputEmployeeID"));
			txtId.requestFocus();
			return false;
		}
		if (id.trim().length() > 20) {
			CommonUI.showMessage(this, HumanResourceUtil
					.getString("EmployeeAddPane.LengthMoreThan20"));
			txtId.requestFocus();
			return false;
		}
		if (id.contains("'") || id.contains("\"")) {
			CommonUI.showMessage(this, HumanResourceUtil
					.getString("EmployeeAddPane.EmployeeIDNoQuote"));
			txtId.requestFocus();
			return false;
		}
		if (lastName.equals("")) {
			CommonUI.showMessage(this, HumanResourceUtil
					.getString("EmployeeAddPane.PleaseinputLastName"));
			txtLastName.requestFocus();
			return false;
		}
		if (FirstName.equals("")) {
			CommonUI.showMessage(this, HumanResourceUtil
					.getString("EmployeeAddPane.PleaseinputFirstName"));
			txtFirstName.requestFocus();
			return false;
		}
		if (UserName.equals("")) {
			CommonUI.showMessage(this, HumanResourceUtil
					.getString("EmployeeAddPane.PleaseinputUserName"));
			txtUserName.requestFocus();
			return false;
		}
		if (EmployeeActionManager.isEmployeeExist(id)) {
			CommonUI.showMessage(this, HumanResourceUtil
					.getString("EmployeeAddPane.duplicateIDpleasehageyouID"));
			txtId.requestFocus();
			txtId.selectAll();
			return false;
		}
		EmployeeCardVO vo = new EmployeeCardVO();
		if (!id.equalsIgnoreCase(""))
			vo.setId(id);
		vo.setLastName(lastName);
		vo.setFirstName(FirstName);
		vo.setUserName(UserName);
		vo.setGender(true);
		try {
			empId = HRServerActionManager.getInstance().addEmployeeCard(vo,	isAutoAdd);
			EmployeeActionManager.clearCache();
			return true;
		} catch (ServerActionException se) {
			se.printStackTrace();
			CommonUI.showException(this, se);
			return false;
		}
	}
	
	
	
	
	public static boolean valid_tuijianka(String cardInfo)
	{
		if(cardInfo!="")
			JOptionPane.showMessageDialog(null, "此功能暂不可用，请持 "+cardInfo+"推荐卡的家长联系园长办公室");
		return false;
	}
	
	@Override
	public ClientUI getFollowingUI() throws Exception {
		if (empId != null) {
			return createUpdateUI(this, empId);
		} else {
			CommonUI.showNoData(this);
			return null;
		}
	}

	TableLayout borderLayout1;
	JLabel jLabel1;
	JTextField txtId;
	JLabel jLabel2;
	JTextField txtLastName;
	JLabel jLabe3;
	JTextField txtFirstName;
	JLabel jLabe4;
	JTextField txtUserName;
	private String empId;
	private boolean isAutoAdd;
	public static final String ADD_TITLE = "添加新的员工";
		//	HumanResourceUtil .getString("EmployeeActionManager.AddanEmployeeCard");
	private static EmployeeCardVO vo;
	private static WeakReference reference;
	public static ClientUI createUpdateUI(Component parent, String id) {
		
		try {
			EmployeeCardVO vo = HRServerActionManager.getInstance()
					.getEmployeeCardById(id);
			StuffAddPane.vo=vo;
			vo.setDepartment(classType);
			//vo.setPosition(classType+"学生");
			StuffAddPane.reference = new WeakReference(parent);
			Vector steps = new Vector();
			steps.add(new UWEmployeeStuffPane(vo));
			bb.gui.swing.UpdateFinishAction finishAction = new AbstractFinishAction(
					vo, parent) {

				@Override
				public Object submitToServer() throws ServerActionException {
					String employeeId = HRServerActionManager.getInstance()
							.updateEmployeeCard((EmployeeCardVO) vo);
					return HRServerActionManager.getInstance()
							.getEmployeeCardById(employeeId);
				}

				@Override
				public ClientUI getDetailUI() {
					return new StuffUI();
				}

			};
			return createUpdateUI(HumanResourceUtil.getString(
					"EmployeeActionManager.UpdateEmployeeCard", id), steps,
					finishAction);
		} catch (ServerActionException e) {
			ClientUtil.showException(parent, e);
		}
		return null;
	}
	
	public static ClientUI createUpdateUI(String title, Collection steps,
			UpdateFinishAction finishAction, String updateButtonText) {
		UpdateStep stepsArray[] = new UpdateStep[steps.size()];
		AbstractFinishAction action = (AbstractFinishAction) finishAction;
		Object obj = StuffAddPane.vo;
		UWCustomPropertiesPane customPropertiesPane = null;
		int index = -1;
		if (obj instanceof VO) {
			VO vo = (VO) obj;
			ClientUtil.initCustomProperties(vo);
			if (vo.getCustomProperties() != null
					&& !vo.getCustomProperties().isEmpty()) {
				customPropertiesPane = new UWCustomPropertiesPane(vo);
				stepsArray = new UpdateStep[steps.size() + 1];
				index = (steps.size() + 1) / 2;
				stepsArray[index] = customPropertiesPane;
			}
		}
		Iterator it = steps.iterator();
		for (int i = 0; it.hasNext(); i++) {
			if (i == index)
				i++;
			stepsArray[i] = (UpdateStep) it.next();
		}

		return createUpdateUI(title, stepsArray, finishAction, updateButtonText);
	}

	public static ClientUI createUpdateUI(String title, Collection steps,
			UpdateFinishAction finishAction) {
		return createUpdateUI(title, steps, finishAction, null);
	}
	public static ClientUI createUpdateUI(String title, UpdateStep steps[],
			UpdateFinishAction finishAction, String updateButtonText) {
		UpdatePane updatePane = new UpdatePane(finishAction);
		updatePane.setUpdateButtonText(updateButtonText);
		updatePane.setWizardSteps(steps);
		updatePane.setTitle(title);
		updatePane.updateComponents();
		ClientUtil.setupWindow(updatePane);
		updatePane.setHighlightColor();
		if (finishAction instanceof AbstractFinishAction) {
			AbstractFinishAction action = (AbstractFinishAction) finishAction;
			Component parent = (Component) StuffAddPane.reference.get();
			if (parent != null) {
				ClientUI parentWindow = ClientUtil
						.getClientUIForComponent(parent);
				ClientUI targetWindow = parentWindow;
				ClientUI parentInstalledWindow = ClientUtil
						.getRefreshFlag(parentWindow);
				if (parentInstalledWindow == null)
					targetWindow = parentWindow;
				else
					targetWindow = parentInstalledWindow;
				ClientUtil.installRefreshFlag(updatePane, targetWindow);
			}
			action.setWizardWindow(updatePane);
		}
		return updatePane;
	}



}