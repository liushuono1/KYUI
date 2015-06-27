package FinanceUI;

import java.math.BigDecimal;

public class test {

	public static void main(String[] args)
	{
	    double   d   =   13.4324;
	    BigDecimal   bd   =   new   BigDecimal(d);
	    bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);
	    System.out.println(bd );
	}
}
