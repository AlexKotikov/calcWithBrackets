package org.kotikov.calc.tests;

import org.kotikov.calc.Run;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Version0_3_Tests {
	 
	
	  @Test
	  public void testVersion_0_3 () throws Exception {
		  //Version 0.3
		  //Еще в разработке
		  
		  Run calc = new Run("");
		  		 
		  Assert.assertEquals (Float.toString(calc.calculate("-1-(5-(4-(3/(2+1)+(-20-3)/3+(-21-2))-4))")),"23.66666");
		  Assert.assertEquals (Float.toString(calc.calculate("-1+(5+(4-(3/(2+1)+(-2-3)/3)*4)/(5+5)-8)-1")),"-4.333333");
		  Assert.assertEquals (Float.toString(calc.calculate("1+(-2+((3+4)*5))+5*(43+3/90*3+1/2)*7/(323+5)+1+(9*(7+9))+6*(454+3*23)+(21*(31+((21)+32)-1)*3312)*(3+1)")),
				  "23094585.652439");
	 

		  
	  }
}
