package org.kotikov.calc.tests;

import org.testng.annotations.Test;
import org.kotikov.calc.ExpressionCalculator;
import org.testng.Assert;
 

public class Version0_1_Tests {
  @Test
  public void SimpleMultOperations() throws Exception {
	  ExpressionCalculator c=  new ExpressionCalculator("");
	  
	//Assert.assertEquals (Float.toString(c.calc("")) , "");
	
	Assert.assertEquals (Float.toString(c.calc("2*2")) , "4.0");
	Assert.assertEquals (Float.toString(c.calc("-2*2")) , "-4.0");
	Assert.assertEquals (Float.toString(c.calc("-2*-2")) , "4.0" );
	Assert.assertEquals (Float.toString(c.calc("2*-2")) , "-4.0");
	Assert.assertEquals (Float.toString(c.calc("0*-2")) , "0.0");
	Assert.assertEquals (Float.toString(c.calc("-0*-2")) , "0.0" );
	Assert.assertEquals (Float.toString(c.calc("-0*2")) , "0.0");
	Assert.assertEquals (Float.toString(c.calc("-0*0")) , "0.0");
	Assert.assertEquals (Float.toString(c.calc("-0*-0")) , "0.0");
	Assert.assertEquals (Float.toString(c.calc("0*0")) , "0.0");	   	    
  }
  @Test
  public void SimpleDivOperations() throws Exception    {
	  ExpressionCalculator c=  new ExpressionCalculator("");
	  
	
 
	Assert.assertEquals (Float.toString(c.calc("2/2")) , "1.0");
	Assert.assertEquals (Float.toString(c.calc("-2/2")) , "-1.0");
	Assert.assertEquals (Float.toString(c.calc("-2/-2")) , "1.0" );
	Assert.assertEquals (Float.toString(c.calc("2/-2")) , "-1.0");
	Assert.assertEquals (Float.toString(c.calc("0/-2")) , "0.0");
	Assert.assertEquals (Float.toString(c.calc("-0/-2")) , "0.0" );
	Assert.assertEquals (Float.toString(c.calc("-0/2")) , "0.0");
	
	try {
	Assert.assertEquals (Float.toString(c.calc("-0/0.000")) , "");
	} catch (Exception e) {
		try {	
		Assert.assertEquals (Float.toString(c.calc("-0/-0")) , "");
		} catch (Exception e1) {
			try {	
				Assert.assertEquals (Float.toString(c.calc("0/0")) , "");	   
			}  catch (Exception e2) {}
	
		}
	}
  }
  
  
  
  @Test
  public void SimplePlusMinusOperations() throws Exception {
	  ExpressionCalculator c =  new ExpressionCalculator("");
      
	  Assert.assertEquals (Float.toString(c.calc("1+1")), "2.0");
	  Assert.assertEquals (Float.toString(c.calc("1-1")), "0.0");
	  Assert.assertEquals (Float.toString(c.calc("-1-1")), "-2.0");
	  Assert.assertEquals (Float.toString(c.calc("-0-1")), "-1.0");
	  Assert.assertEquals (Float.toString(c.calc("1000-1")), "999.0");
  }
  
  @Test
  public void testSignOrder() throws Exception {
  
	  ExpressionCalculator c =  new ExpressionCalculator("");
	  
    Assert.assertEquals (Float.toString(c.calc("1+1*5")), "6.0");
    Assert.assertEquals (Float.toString(c.calc("1-1*5")), "-4.0");
    Assert.assertEquals (Float.toString(c.calc("-1-1*5")), "-6.0");
    
    Assert.assertEquals (Float.toString(c.calc("-1/-1*5")), "5.0");
    Assert.assertEquals (Float.toString(c.calc("1+3+2+4+5+7")), "22.0");
     
    Assert.assertEquals (Float.toString(c.calc("1+3-2+4-5+7")), "8.0");
	Assert.assertEquals (Float.toString(c.calc("-1+3-2+4-5+7")), "6.0");
	Assert.assertEquals (Float.toString(c.calc("-1.34+3.0-2+4-5+7")),  "5.66");
	Assert.assertEquals (Float.toString(c.calc("-1.34-3.0-2-4-5-7")), "-22.34");
	Assert.assertEquals (Float.toString(c.calc("1+2*3+4")), "11.0");
	Assert.assertEquals (Float.toString(c.calc("-1+2*3+4")), "9.0");
	Assert.assertEquals (Float.toString(c.calc("-1+2*-3+4")), "-3.0");
	Assert.assertEquals (Float.toString(c.calc("-1+2*-30+4")),  "-57.0");
	Assert.assertEquals (Float.toString(c.calc("-3-38*0*-99.3090")), "-3.0");
	Assert.assertEquals (Float.toString(c.calc("-9-38*0*-99.3090")), "-9.0");
	Assert.assertEquals (Float.toString(c.calc("-9+0.0*-99.3090")), "-9.0");
  }
  
  
  @Test
  public void expressionsTest() throws Exception {
	ExpressionCalculator c =  new ExpressionCalculator("");

	Assert.assertEquals (Float.toString(c.calc("-3-38*-0*-99.3090/-99*30/1*-2+6-2*-933")),"1869.0");
	Assert.assertEquals (Float.toString(c.calc("-3-38*-0*-99.3090/-99*30/1*-2.0+6-2.32*-933.222/32+0*32*0+3/32-3/-3*11/2*-0+32/21/222+2*9")),  "88.75921");
	Assert.assertEquals (Float.toString(c.calc("-0*-3-38*-0*-99.3090/-9+10/9*-2/9-8-7+9*30/1*-2.0+6-2.32*-93/3.222/32+0*32*0+3/32-3/-3*11/2*-0+32/21/222+2*9/-100")), "-547.23364");  

  }

  @Test
  public void longExpression() throws Exception {
	  ExpressionCalculator c =  new ExpressionCalculator("");
  
	  Assert.assertEquals (Float.toString(c.calc("-0*-3-38*-0*-99.3090/-9+10/9*-2/9-8-7+9*30/1*-2.0+6-2.32*-93/3.222/32+0*32*0+3/32-3/-3*11/2*-0+32/21/222+2*9/-100-0*-3.001-38*-0*-99.9090/-9+10/9*-2/9-8-7+9*90/1*-2.0+6-2.32*-93/3.232/32+0*32*0+3/32-3/-3*11/2*-0+32/21/2.22+2*9/-100-0*-3-0.38*-0*-99.3090/-9+10/9*-2/9-8-7+9*30/1*-2.0+6-2.32*-93/3.0222/32+0*32*0+3/32-3/-3*11/2*-0+32/21/222+2*9/-100-0*-3-38*-0*-99.3090/-9+10/9*-2/9-8-7+9*30/1*-2.0+6-2.32*-93/3.222/32+0*32*0+3/32-3/-3*11/2*-0+32/21/222+2*9/-1000.1000")), 
			  "-3267.9612");  

  }
}
