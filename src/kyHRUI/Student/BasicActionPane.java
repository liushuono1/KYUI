package kyHRUI.Student;

import bb.common.EmployeeCardVO;
import bb.gui.ColorStore;
import bb.gui.IconStore;
import bb.gui.base.ClientUI;
import bb.gui.swing.homepage.HomePageBaseSearchPane;



public class BasicActionPane extends HomePageBaseSearchPane {

	EmployeeCardVO vo;
	public BasicActionPane(EmployeeCardVO vo) {
		super("");
		getSearchButton().setText(getButtonText());
		setBackground(ColorStore.BACKGROUND_HOMEPAGE_ADD);
		getSearchButton().setIcon(IconStore.ADD_ICON);
		getSearchButton().setIcon(IconStore.NEW_ICON);
		this.vo=vo;
	}

	public String getButtonText() {
		return (new StringBuilder()).append("<html><font color = blue>")
				.append(getAddUiTitle()).toString();
	}


	public  String getAddUiTitle()
	{
		return "";
	};
	
	public ClientUI getSearchResultUI() {
		
		return null;
	}

	protected boolean isPack() {
		return true;
	}
}