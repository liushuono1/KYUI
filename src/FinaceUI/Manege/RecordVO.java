package FinaceUI.Manege;

import java.sql.Date;
import java.sql.Time;

import bb.common.EmployeeCardVO;
import bb.common.VO;

public class RecordVO extends VO{
	private String id;
	private Date	P_date;
	private Time P_Time;
	private String	PaymentDetail;
	private double	refund;
	private double  charge;
	private double  paid;
	private double  balance;
	private String  comment;	
	
	public RecordVO()
	{
		id="";
		P_date=new Date(System.currentTimeMillis());
		P_Time=new Time(System.currentTimeMillis());
		PaymentDetail="";
		refund=0;
		charge=0;
		paid=0;
		balance=0;
		comment="";		
		
		
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public void setPaymentDate(Date paymentDate) {
		this.P_date = paymentDate;
	}

	public Date getPaymentDate() {
		return P_date;
	}
	
	
	public void setPaymentDetail(String PaymentDetail) {
		this.PaymentDetail = PaymentDetail;
	}

	public String getPaymentDetail() {
		return PaymentDetail;
	}
	
	
	public void setCharge(double charge)
	{
		this.charge=charge;
	}
	
	public double getCharge()
	{
		return charge;
		
	}
	
	public void setPayment(double payment)
	{
		this.paid=payment;
	}
	
	public double getPayment()
	{
		return paid;
		
	}
	
	public void setRefund(double refund)
	{
		this.refund=refund;
	}
	
	public double getRefund()
	{
		return refund;
		
	}
	
	
	
	public void setBalance(double balance)
	{
		this.balance=balance;
	}
	
	
	public double getBalance()
	{
		return balance;
		
	}
	
	
	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setPaymentTime(Time time) {
		// TODO Auto-generated method stub
		this.P_Time=time;
	}
	
	public Time getPaymentTime()
	{
		return P_Time;
	}
	
	
}
