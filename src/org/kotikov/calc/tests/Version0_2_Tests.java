package org.kotikov.calc.tests;

import org.kotikov.calc.Run;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Version0_2_Tests {
 
  @Test
  public void testVersion_0_2 () throws Exception {
	  //Version 0.2
	  
	  Run calc = new Run("");
	  
	  Assert.assertEquals (Float.toString(calc.calculate("(5+(4-(3-(2+1))))")), "9.0");
	  
	  Assert.assertEquals (Float.toString(calc.calculate("((((1+2)*3)+4)-5)")), "8.0");
	  
	  Assert.assertEquals (Float.toString(calc.calculate("(4-(3/(2+1)+(-20-3)/3)*4)")), "30.666666");
		 
	  Assert.assertEquals (Float.toString(calc.calculate("-1+(5+(4-(3/(2+1)+(-20-3)/3)*4))")), "34.666664");
	 
  }
  
 
  
  
}
