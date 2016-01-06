package kyHRUI.Stuff;

import bb.common.Constant;
import bb.gui.base.MultiLanguageComboBox;
import bb.gui.hr.HumanResourceUtil;
import java.util.LinkedHashMap;
import java.util.Map;

public class StuffSecurityLevelBox extends MultiLanguageComboBox {

	public StuffSecurityLevelBox() {
	}

	protected Map getMap() {
		LinkedHashMap map = new LinkedHashMap();

		map.put(bb.common.Constant.EmployeeSecurityLevel.LEVEL3,
				"在职");
		
		map.put(bb.common.Constant.EmployeeSecurityLevel.TERMINATED,
				"开除的");
		return map;
	}
}

