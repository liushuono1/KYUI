package Simple;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class DutyList {
	public static List<Duty> dutyList,submitList;
	static DutyList instance;
    static String comparestring="";
	public DutyList() {
		instance = this;
		dutyList = new LinkedList<Duty>();
		submitList= new LinkedList<Duty>();
	}

	public static DutyList getInstance() {
		if(instance==null)
		{
			new DutyList();
		}
		return instance;
	}

	

	public static boolean contains(Duty d) {
		boolean ret = false;
		
		//System.out.println(dutyList.size());
		
		if(dutyList.size()>0)
		for (int i=0;i<dutyList.size();i++) {
			Duty D = dutyList.get(i);
			if (D.isSame(d)) {
				ret = true;
				comparestring="";
				break;
			}
			comparestring="duty1:"+D+"\nduty2"+d+"\nfalse";
		}
		return ret;
	}
	
	public static boolean Rcontains(Duty d) {
		boolean ret = false;
		
		//System.out.println(submitList.size());
		if(submitList.size()>0){
			
		for (int i =0;i<submitList.size();i++) {
			Duty D= submitList.get(i);
			if (D.isSame(d)) {
				ret = true;
				//System.out.println("duty1:"+D+"\nduty2"+d+"\ntrue");
				comparestring="";
				break;
			}
			comparestring="duty1:"+D+"\nduty2"+d+"\nfalse";
		}
		}
		return ret;
	}

	/*
	 * clear the dutylist;read from DB
	 */
	public void addDuty(Duty d) {
		
		submitList.add(d);

	}
	
	public static synchronized List<Duty> getDutyList()
	{
		return dutyList;
	}
	
	public void submit()
	{
		List addList=new LinkedList();
		for(int i=0;i<submitList.size();i++)
		{
			Duty d= submitList.get(i);
			if(!contains(d))
			{
				addList.add(d);
				//getDutyList().add(d);
			}
		}
		getDutyList().addAll(addList);
		List delList=new LinkedList();
		//System.err.println("1:"+getDutyList().size());
		for(int i =0;i<getDutyList().size();i++)
		{	
			Duty d =getDutyList().get(i);
			if(!Rcontains(d))
			{
				delList.add(d);
				//System.err.println(DutyList.comparestring);
				//getDutyList().remove(d);
			}
		}
		try{
		getDutyList().removeAll(delList);
		}catch(NullPointerException e)
		{
			System.out.println("dellist "+delList);
			System.out.println("getDutyList() "+getDutyList());
			
		}
		//System.err.println("2:"+delList.size());
		submitList.clear();
	}
	public void clearAll()
	{
		this.dutyList = new LinkedList<Duty>();
	}

	/*
	public void delDuty(Duty d) {
		List<Object> delDuty = d.duty;
		List<Duty> reservedList = dutyList;
		for (int i = 0; i < delDuty.size(); i++) {
			String attribute_value = delDuty.get(i).toString();
			for (int j = 0; j < dutyList.size(); j++) {
				Duty a_duty = dutyList.get(j);
				String a_attr_value = a_duty.duty.get(i).toString();
				if (!attribute_value.equals(a_attr_value)) {
					reservedList.remove(j);
				}
			}
			if (reservedList.size() == 1) {
				break;
			}
		}
		dutyList.remove(reservedList.get(0));
	}*/

	/*
	 * public void compareDuty(Duty d1, Duty d2) { boolean same = true;
	 * List<Object> l1 = d1.duty; List<Object> l2 = d2.duty; for(int
	 * i=0;i<l1.size();i++) { String obj1 = l1.get(i).toString(); String obj2 =
	 * l2.get(i).toString(); if(obj1.equals(obj2)) {
	 * 
	 * } } }
	 */

	public void modifyDuty() {

	}

	public void check() {

	}

	public String toString() {
		String ret = super.toString() + "\n";
		int i = 0;
		for (Duty d : dutyList) {
			ret += "Duty" + (++i) + ":" + d.getStartTime() + "  " + d.getQuestTitle() + "\n";
		}

		return ret;

	}

	public int getSize() {
		return dutyList.size();
	}
}