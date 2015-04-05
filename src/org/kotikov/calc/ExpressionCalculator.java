package org.kotikov.calc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionCalculator {
	   static final String plusPattern = "\\+";
	    static final String minusPattern = "\\-";
	    static final String multPattern = "\\*";
	    static final String divPattern = "\\/";
	    static final String floatdigit = "-?\\d+(\\.\\d{0,})?"; //
	    static final String secondfloatdigit = "\\d+(\\.\\d{0,})?"; //
	    static final String completePattern = "^\\-?\\d+(\\.\\d{0,})?";
		    	    
	    private StringBuilder incoming ;
	    
        /**
	 * @author ak
	 * 
	 * Класс считает любое евыражение без скобок
	 */
	    
	    
    public ExpressionCalculator(String  inconing) {
	    this.incoming=  new StringBuilder(inconing);	
	  }
	    
	    /**
	     * Вычислить выражение
	     */
   public float calc(String  inconing) throws Exception {
	    	this.incoming=  new StringBuilder(inconing);
	    	return computeGivenExpression();
	    } 
	   
   
   public boolean canBeCalculated(String expression) {
	    	
	    	Matcher m;
	    	Pattern digit;
	    	    
	    	digit = Pattern.compile("^(-?\\d+(\\.\\d{0,})?)$");   
	        m =  digit.matcher(expression);    
	        if (m.matches())  return false;
	    	
	        
	        digit = Pattern.compile("^(-?\\d+(\\.\\d{0,})?)");    
	        m =  digit.matcher(expression);   
	        if (m.find()) 
	        {
	        	digit = Pattern.compile("(-?\\d+(\\.\\d{0,})?)$");    
		        m =  digit.matcher(expression);   
		        if (m.find()) 
		        	return true ;  
	        } 	        
			return  false;
		}  
	    
	    
   public float computeGivenExpression() throws Exception{	      	    	
	    	//регэксп для того чтобы понять когда закончить считать выражение
	    	Pattern complete = Pattern.compile(completePattern); 
	        Matcher expression =  complete.matcher(incoming.toString());
	        
	        
	       while (!expression.matches()){   //является ли строка выражением или готовым числом которое можно вернуть? 
	    		 							//если выражением, то надо его вычислять c пом. computeTwoOperands()
	    		 							//а значение вставлять обратно в ту же строку пока оно не станет обычным числом
	    	   	        
	    		 //Получаем два операнда, и вычисляем их
	    		 float firstDigit = (float) this.getFirstDigit();
	    		 float secondDigit = (float) this.getSecondDigit();	    		 
	    		 float result = this.computeTwoOperands(firstDigit, secondDigit);
	    		 
	    		 //Полученное выражение надо вставить от start до end в зависимости от правил знаков
	    		 int repStart = getPositionOfFirstDigit();
	    		 int repEnd = getLastIndexOfSecondDigitInString();
	    		 String plus =""; //переменная для случаев когда надо поставить "плюс" в выражение перед числом
	    		 
	     
	    		   //Правила поглощения знаков в зависимости от result и след. знака в выражении
	    	if (repStart >0) {
	    						//если результат получился нулевой,
	    						//а перед ним стоит минус или плюс то добавить плюс
	    					if (Float.toString(result).equals("0.0"))
	    						if ((this.incoming.charAt(repStart)=='-') ||
	    						   (this.incoming.charAt(repStart)=='+'))
	    							plus ="+";
	    		
				    		 if (result <0)  //правило: минус на минус дает плюс
				    			 if (this.incoming.charAt(repStart-1)=='+')
				    		    	repStart--;
				    		       
				    		   
				    		 if (result <0) //Вырезать не значащий минус
					    		    if (this.incoming.charAt(repStart-1)=='-')
					    		    	result = Math.abs(result);
				    		 
				    		 
				    		 if (result >0)  //правило: минус на минус дает плюс				    			     			 
				    			 if (firstDigit < 0) 
				    		          plus ="+"; 
				    		 
				    		 if (result >0) //флоат не хранит "плюс", поэтому надо добавить его вручную
				    		 if (this.incoming.charAt(repStart)=='+') plus ="+";				    		 
	    		 }
	     
	    		    	
	    		//Дебаг вычислений  Выводит подсчеты по шагам
	    	   // System.out.println( "["+incoming.toString().substring(repStart,repEnd) +"] ="+ Float.toString(result));
	    	 	
	    	 	//вставить результат в строку где было выражение
	    	  	//это единственное место в классе где входная строка меняется
				 incoming.replace(repStart, repEnd,  plus +  Float.toString(result)  ); //; "0" Float.toString(result)
			
				 
	    	     //каждый раз надо обновлять выражение, чтобы понять когда выходить из цикла
	    		 expression =  complete.matcher(incoming.toString());
	      }
	       return   Float.parseFloat(incoming.toString());
	    }
	    
	    
   private int  getPositionOfSign ()
	    {
			    int tmp =this.incoming.indexOf("*");
			    int tmp2 =this.incoming.indexOf("/");
		   
			    if 	((tmp==-1) && (tmp2==-1))  
			    { 
			    	  tmp =this.incoming.indexOf("-",1);  //не брать минус вначале потому что он от числа
			    	  tmp2 =this.incoming.indexOf("+");
			          if ((tmp==-1) && (tmp2==-1)) return 0;     	  
			    }
			    
			     if (tmp==-1) return tmp2;
			     if (tmp2==-1) return tmp;  
			     if (tmp < tmp2) return tmp; else return tmp2;
	    }
	    
     
	    
   private  int getPositionOfFirstDigit() {
	    	int result =0;
	    
	    	int frst = getPositionOfSign ();
	        char currentSign = this.incoming.charAt(getPositionOfSign ()); 	  
	    
	    	 //взять подстроку с самого начала до текущего знака
	 	     String strtmp = this.incoming.substring(0, frst).toString()
	                    .replaceAll(plusPattern, "X") 
	                    .replaceAll(minusPattern, "X")
	                    .replaceAll(multPattern, "X")
	                    .replaceAll(divPattern, "X");
	 	     			//заменить все знаки иксами
	 	                //чтобы потом их подсчитать и понять откуда начинается число
	 	     
	 	     // 4 кейса
	 	     // 	Последнее число 
	 	     //			1  Положительное
	 	     //			2 Отрицательное
	 	     //		Не последнее число
	 	     //			3 Если * / то  бери с минусом / плюсом
	 	     //			4 Если + - то бери без знака	 	     
	 	    if (strtmp.indexOf("X",1)==-1)
	 	      return  0; //Если это последнее число в строке то брать его сначала строчки 	
	 	    else   	
	 	    	if ((currentSign=='*') || (currentSign=='/'))  
	 	    	    return result = strtmp.lastIndexOf("X"); //бери от знака
	 	    	else
	 	    		return result = strtmp.lastIndexOf("X")+1; //бери после знака
	    } 
	      
	      
    
	private  float getFirstDigit () throws Exception
	   {
         int fr = getPositionOfFirstDigit(); // вычислить начало этого знака
         int end = getPositionOfSign (); // конец там, где начинается знак
         
         float result = Float.parseFloat(this.incoming.substring(fr, end)); 
       
         //Мне кажется так проверить флоат надежнее всего
         if ( Float.toString(result).equals("-0.0"))
        	 result =  Math.abs(result);
         
		return result;
      }
	    
	 
	    
    private  float getSecondDigit() {  
	    	//смысл метода в том чтобы различать и возвращать положительные и отрицательные числа
	    	//в зависимости от случая
	    	//Случаи: 1)2-3 и 2)2*-3   
	    	// 1) 3 берется как положительное
	    	// 2) 3 берется как отрицательное
	     float result;
	        
		        //Вначале проверяется есть ли два знака подряд: *-2 или /-2          
		Pattern digit = Pattern.compile("["+divPattern+"|"+multPattern+ "]+" + floatdigit); //"[\\*|\\/]+-?\\d+(\\.\\d{0,})?"   
        Matcher m =  digit.matcher(this.incoming);
        
         //если есть, то число отрицательное и нужно взять его знак
        if (m.find(this.getPositionOfSign ())) {   
        	result = Float.parseFloat(this.incoming.substring(m.start()+1, m.end()));
         }
        else { //если нет, то не трогать этот знак, он не от числа. Число оказалось положительным.
        	digit = Pattern.compile(secondfloatdigit);
        	m =  digit.matcher(this.incoming);
        	m.find(this.getPositionOfSign ());
        	result = Float.parseFloat(m.group());
        }
        
        //Почистить минус
        if ( Float.toString(result).equals("-0.0"))
        	result =  Math.abs(result);
        
	     return result;
     } 
	    
	    
    private   int getLastIndexOfSecondDigitInString () {
	    	//Вычисляет до какого места надо будет вырезать полученное далее выражение.
	    	Pattern digit = Pattern.compile(secondfloatdigit);  //число флоат с точкой   
            Matcher m =  digit.matcher(this.incoming);
            m.find(getPositionOfSign ());
            return   m.end();
       } 
	    
	//Посчитать два числа
    private float computeTwoOperands(float first,  float second) throws Exception 
	    {	    	
	    	float result = Float.NaN; 
	    	//Получить знак для вычислений
	    	char sign = this.incoming.charAt(this.getPositionOfSign()); 
	    	
	    	if (sign == '-') result = first - second;
	    	    if (sign == '+') result = first + second;
	    	     if (sign == '/') result = first / second;
	    	      if (sign == '*') result =  first * second;
      
	    	      if ((Float.isInfinite(result)  || (Float.isNaN(result))))
	    	         throw new Exception("Devide by zero, or result is not a digit");
	    	     
	    	      if ( Float.toString(result).equals("-0.0"))
	    			   result =  Math.abs(result);
	    	      
	    	     return result;
	    } 

	    /**
	     *  таблица истинности 
	     *  На вход:    Вернет:
	    	//+-* /dddd   no    
	    	//-ddd        no    
	    	//*ddd*ddd*   no 
	    	//*ddd*		  no
	    	//*dd*d*dd*   no
	    	//+,-,*,/	  no    
	    	//d+/-*		  no    
	    	//d*d+d-d	  yes
	    	//ddd*fdsf    no* 
	    	//-ddd*-fdsf  yes
	    	//ddd*fdsf    yes
	     */
	 
	 
	public static void main(String[] args) throws Exception {
		
		ExpressionCalculator c =  new ExpressionCalculator("3*4-30/40+80");
		
		System.out.println("Total: " + c.computeGivenExpression());	
		System.out.println("canBE");
		System.out.println(c.canBeCalculated("123.2"));
		System.out.println(c.canBeCalculated("-123.2"));
		System.out.println(c.canBeCalculated("/123.2"));
		System.out.println(c.canBeCalculated("+123.2"));
		System.out.println(c.canBeCalculated("*123.2"));
		System.out.println();
		System.out.println(c.canBeCalculated("/"));
		System.out.println(c.canBeCalculated("-"));
		System.out.println(c.canBeCalculated("+"));
		System.out.println(c.canBeCalculated("*"));
		System.out.println();
		System.out.println(c.canBeCalculated("123.2-"));
		System.out.println(c.canBeCalculated("123.2/"));
		System.out.println(c.canBeCalculated("123.2*"));
		System.out.println(c.canBeCalculated("123.2+"));
		System.out.println(c.canBeCalculated("*123.2+21.2*4\21-"));
		System.out.println(c.canBeCalculated("2*2"));
		System.out.println(c.canBeCalculated("-4/-232.9/32.3/32-3/-21*-3/3-0+2.2+1"));
	}
}
