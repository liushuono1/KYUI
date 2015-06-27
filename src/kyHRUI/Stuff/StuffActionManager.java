package kyHRUI.Stuff;


import java.awt.Component;
import java.util.Vector;

import bb.common.EmployeeCardVO;
import bb.gui.ClientUtil;
import bb.gui.ServerActionException;
import bb.gui.base.AddUI;
import bb.gui.base.ClientUI;
import bb.gui.hr.EmployeeActionManager;
import bb.gui.hr.EmployeeListUI;
import bb.gui.hr.EmployeeUI;
import bb.gui.hr.HumanResourceUtil;
import bb.gui.hr.UWEmployeeGeneralPane;
import bb.gui.server.HRServerActionManager;
import bb.gui.swing.AbstractFinishAction;
import bb.gui.swing.UpdateUIFactory;


// Referenced classes of package bb.gui.hr:
//            EmployeeAddPane, EmployeeUI, UWEmployeeGeneralPane, UWEmployeeHomeInfoUI, 
//            HumanResourceUtil, EmployeeListUI, EmployeeEducationListUI, EmployeeHomeInfoUI

public class StuffActionManager extends EmployeeActionManager {

	public static String s="KY";
	public StuffActionManager(EmployeeListUI ui) {
		super(ui);

	}


	protected ClientUI getAddUI() {
		System.out.println("Runhere ----------------------1");
		return AddUI.createAddDialog(new StuffAddPane(), HumanResourceUtil
				.getString("EmployeeActionManager.AddanEmployeeCard"));
	}

	protected ClientUI getEditUI(Object vo) {
		EmployeeCardVO emp = (EmployeeCardVO) vo;
		return createUpdateUI(getListUI(), emp.getId());
	}
	
	protected ClientUI getDetailUI(Object o) throws ServerActionException {
		EmployeeCardVO infoVO = (EmployeeCardVO) o;
		EmployeeCardVO vo = HRServerActionManager.getInstance()
				.getEmployeeCardById(infoVO.getId());
		StuffUI ui = new StuffUI();
		ui.setVO(vo);
		return ui;
	}


	public static ClientUI createUpdateUI(Component parent, String id) {
		try {
			EmployeeCardVO vo = HRServerActionManager.getInstance()
					.getEmployeeCardById(id);
			Vector steps = new Vector();
			steps.add(new UWEmployeeStuffPane(vo));
			bb.gui.swing.UpdateFinishAction finishAction = new AbstractFinishAction(
					vo, parent) {

				public Object submitToServer() throws ServerActionException {
					String employeeId = HRServerActionManager.getInstance()
							.updateEmployeeCard((EmployeeCardVO) vo);
					return HRServerActionManager.getInstance()
							.getEmployeeCardById(employeeId);
				}

				public ClientUI getDetailUI() {
					return new StuffUI();
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

