package AuthModule;

import bb.common.EmployeeCardVO;

public class CardInfo
{
	String CardID;
	EmployeeCardVO CardUser;
	EmployeeCardVO CardHolder;
	
	
	CardInfo(String CardID)
	{
		this.CardID=CardID.toUpperCase();
		CardUser=null;
		CardHolder=null;
	}
	
	public void setCardUser(EmployeeCardVO user)
	{
		this.CardUser=user;
	}
	
	
	public void setCardHolder(EmployeeCardVO holder)
	{
		this.CardHolder=holder;
	}
	
	
	public String getCardID()
	{
		return this.CardID;
	}
	
	
	
	public String getID()
	{
		if(CardUser!= null)
		   return this.CardUser.getId();
		else
			return "";
	}
	
	
	public String getHolderID()
	{
		if(CardHolder!= null)
		   return this.CardHolder.getId();
		else
			return "";
	}
	
	public boolean isNull()
	{
		if(CardID==null)
			return true;
		else
			return false;
	}
	
	
	
	@Override
	public String toString()
	{
		String HolderID="1P999";
		String ID="1P999";
		//System.out.println(CardID +"     "+HolderID);
		if(CardHolder!=null) HolderID=CardHolder.getId();
		if(CardUser!=null) ID=CardUser.getId();
		return CardID+"  P"+HolderID.split("P")[1];
		
	}

	public  EmployeeCardVO getHolder() {
		// TODO Auto-generated method stub
		return CardHolder;
	}
	
	public  EmployeeCardVO getUser() {
		// TODO Auto-generated method stub
		return CardUser;
	}
}