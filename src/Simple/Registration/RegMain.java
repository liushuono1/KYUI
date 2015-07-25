package Simple.Registration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.Properties;

import javax.swing.JMenuItem;

import bb.common.CompanyVO;
import bb.common.EmployeeCardVO;
import bb.gui.ClientConst;
import bb.gui.ClientContext;
import bb.gui.ClientUtil;
import bb.gui.CommonUI;
import bb.gui.Main;
import bb.gui.ServerActionException;
import bb.gui.base.BB2Node;
import bb.gui.base.ClientUI;
import bb.gui.i18n.BB2Translator;
import bb.gui.server.CompanySettingServerActionManager;
import bb.gui.server.HRServerActionManager;

import free.AppMainUI;
import free.FreeMenuBar;
import free.FreeRootMenu;
import free.LoginUI;

public class RegMain extends AppMainUI {
	
	public RegMain() {
        FreeMenuBar menubar = getKYMenubar();
        
        try
        {
          EmployeeCardVO empVO = HRServerActionManager.getInstance().getEmployeeSelf();
          if (empVO != null)
          {
            String systemName = empVO.getCompanyAddressBookName();
            if ((systemName != null) && (!"".equals(systemName))) {
               System.out.println(systemName);
            }
          }
        }
        catch (ServerActionException e)
        {
          ClientUtil.showException(e);
        }
		

        
        
    }


	private BB2Node createStudioSearchNode() {
		BB2Node node = new BB2Node();
		node.setName("Studio");
		node.putClientProperty("homeClass",
				kyHRUI.Student.StudentAddPane.class);
		node.putClientProperty("nameKey", "Studio");
		node.setIcon("/bb/gui/images/submodule.png");
		return node;
	}

	@Override
	public boolean isDisplayTree() {
		return false;
	}

	@Override
	protected String getProductName() {
		return "2BizBox Studio";
	}


	@Override
	public String getMainTreeXML()
	  {
	    return "/bb/gui/conf/MainTreeKY.xml";
	  }
	
	@Override
	public String getMenuBarXML() {
		return "/KYUI/menubar.xml";
	}
 
	
	
	public FreeMenuBar getKYMenubar()
	{
 
		FreeMenuBar menubar = getFreeMenuBar();
		menubar.add(createMenu("新生注册"));
		
        menubar.add(createFuncItem("功能"));
       // menubar.add(createRefreshItem("刷新"));
        
        menubar.add(createExitMenu("退出"));

        
        return menubar;
           
	}
	private FreeRootMenu createRefreshItem(String text) {
		FreeRootMenu menu = new FreeRootMenu(text);
		JMenuItem item = new JMenuItem(text);
	    item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
	 

	    });
	    menu.add(item);
	    return menu;
	}
	
	private FreeRootMenu createFuncItem(String text) {
		FreeRootMenu menu = new FreeRootMenu(text);
		JMenuItem item = new JMenuItem("查看名单");
	    item.addActionListener(new ActionListener() {
	 
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	ClientUI ui = new SearchHome();
		        showTab(ui);
	        }
	    });
	    menu.add(item);
	    return menu;
	}
	
	private FreeRootMenu createMenu(String text) {
	    FreeRootMenu menu = new FreeRootMenu(text);
	    JMenuItem item = new JMenuItem("注册");
	    item.addActionListener(new ActionListener() {
	 
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            ClientUI ui = new RegHome();
	            showTab(ui);
	        }
	    });
	    menu.add(item);
	    return menu;
	}
	
	
	@Override
	public void closing() {

			int answer = CommonUI.showWarningConfirm(this,
					BB2Translator.getString("MainUI.CloseBizBox"), 0);
			System.out.println(answer);
			if (answer == 0) {
				//instanced = false;
				dispose();
				
			
		}
	}

	private FreeRootMenu createExitMenu(String text) {
	    FreeRootMenu menu = new FreeRootMenu(text);
	    JMenuItem item = new JMenuItem("退出系统");
	    item.addActionListener(new ActionListener() {
	 
	        @Override
	       
	        public void actionPerformed(ActionEvent e) {
	        	 closing();      
	            
	        }
	    });
	    menu.add(item);
	    return menu;
	}
	
    @Override
   	
    public String getTitle() {
    	String returnTxt="";
    	try {
			CompanyVO vo = CompanySettingServerActionManager.getInstance()
					.getCompanySettings();
			returnTxt += vo.getCompanyName();
		} catch (ServerActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	SimpleDateFormat dateformat=new SimpleDateFormat("yyyy年MM月dd日  E ");
    	returnTxt += ("["+dateformat.format((new java.util.Date()))+"]");
    	
        return returnTxt;
    }
 
    public static void main(String[] args) {
        
    	ClientContext.getLocale();
    	
    	//LoginUI x= LoginUI.getInstance(true);
    	
    	//x.setBackground(Color.blue);
    	//x.setOpacity(0.5f);
    	//System.out.println(LoginUI.getInstance().getName());
    	
    	Main.launchBizBox(RegMain.class.getName(),null,null,false);
    	
    	LoginUI x= LoginUI.getInstance();
    	Properties result = new Properties();
		File file = new File(ClientConst.getLoginMessPropertyFile());
		if (file.exists())
			try {
				FileInputStream is = new FileInputStream(file);
				result.load(is);
				is.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

    	
    	
    }
    
	

	}
	