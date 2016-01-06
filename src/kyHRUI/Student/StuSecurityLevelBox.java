package kyHRUI.Student;

import bb.common.Constant;
import bb.gui.base.MultiLanguageComboBox;
import bb.gui.hr.HumanResourceUtil;
import java.util.LinkedHashMap;
import java.util.Map;

public class StuSecurityLevelBox extends MultiLanguageComboBox {

	public StuSecurityLevelBox() {
	}

	protected Map getMap() {
		LinkedHashMap map = new LinkedHashMap();
		
		map.put(bb.common.Constant.EmployeeSecurityLevel.LEVEL4,
				"“—¿Î‘∞");
		map.put(bb.common.Constant.EmployeeSecurityLevel.LEVEL5,
				"‘⁄‘∞");
		map.put(bb.common.Constant.EmployeeSecurityLevel.TERMINATED,
				"“—ÕÀ‘∞");
		return map;
	}
}