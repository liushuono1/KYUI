package Simple;

public class triggerEvent{
	 String start_time;
	 Duty duty;
	 public triggerEvent(String time, Duty d)
	 {
	  this.start_time = time;
	  this.duty = d;
	 }
	 
	 public void setTime(String time)
	 {
	  this.start_time = time;
	 }

	 public void setDuty(Duty d)
	 {
	  this.duty = d;
	 }
	 
	 public String getTime()
	 {
	  return this.start_time;
	 }
	 
	 public Duty getDuty()
	 {
	  return this.duty;
	 }
	}