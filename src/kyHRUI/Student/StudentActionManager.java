package kyHRUI.Student;


import java.awt.Component;
import java.util.Vector;

import bb.common.EmployeeCardVO;
import bb.gui.ClientUtil;
import bb.gui.ServerActionException;
import bb.gui.base.AddUI;
import bb.gui.base.ClientUI;
import bb.gui.hr.EmployeeActionManager;
import bb.gui.hr.EmployeeListUI;
import bb.gui.hr.HumanResourceUtil;
import bb.gui.server.HRServerActionManager;
import bb.gui.swing.AbstractFinishAction;
import bb.gui.swing.UpdateUIFactory;


// Referenced classes of package bb.gui.hr:
//            EmployeeAddPane, EmployeeUI, UWEmployeeGeneralPane, UWEmployeeHomeInfoUI, 
//            HumanResourceUtil, EmployeeListUI, EmployeeEducationListUI, EmployeeHomeInfoUI

public class StudentActionManager extends EmployeeActionManager {

	public static String s="KY";
	public StudentActionManager(EmployeeListUI ui) {
		super(ui);

	}


	@Override
	protected ClientUI getAddUI() {
		System.out.println("Runhere ----------------------1");
		return AddUI.createAddDialog(new StudentAddPane(), HumanResourceUtil
				.getString("EmployeeActionManager.AddanEmployeeCard"));
	}

	@Override
	protected ClientUI getEditUI(Object vo) {
		EmployeeCardVO emp = (EmployeeCardVO) vo;
		return createUpdateUI(getListUI(), emp.getId());
	}
	
	@Override
	protected ClientUI getDetailUI(Object o) throws ServerActionException {
		EmployeeCardVO infoVO = (EmployeeCardVO) o;
		EmployeeCardVO vo = HRServerActionManager.getInstance()
				.getEmployeeCardById(infoVO.getId());
		//StudentUI ui = new StudentUI();
		//ui.setVO(vo);                                 //原始的动作，直接打开详情页面，现在要进入下级处理页面
		StuProceedUI ui =new StuProceedUI(vo);
		
		
		return ui;
	}


	public static ClientUI createUpdateUI(Component parent, String id) {
		try {
			EmployeeCardVO vo = HRServerActionManager.getInstance()
					.getEmployeeCardById(id);
			Vector steps = new Vector();
			steps.add(new UWEmployeeStudentPane(vo));
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
					return new StudentUI();
				}

			};
			return UpdateUIFactory.createUpdateUI(HumanResourceUtil.getString(
					"EmployeeActionManager.UpdateEmployeeCard", id), steps,
					finishAction);
		} catch (ServerActionException e) {
			ClientUtil.showException(parent, e);
		}
		return null;
	}
	
	


}

