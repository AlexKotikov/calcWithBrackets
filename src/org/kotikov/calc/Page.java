package org.kotikov.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Page {

	/**
	 * @param args
	 * @author ak
	 * 
	 * Page - строчки arrayList которые нужно собрать в выражение для дальнейших подсчетов
	 */
	
	private List<InstructionsLine> linesOfPage;
	
	public Page(List<InstructionsLine> lines) {
		 
		this.linesOfPage = lines;
	}

	
	/**
     * Для подсчитанных страниц. Вернуть сумму.
     * @return
     */
	public float resultOfPage() {
	return Float.parseFloat(linesOfPage.get(0).expression);
	}
	
	
	/**
	 Для тестирования
	*/
	public  List<InstructionsLine> getlines() {
		
		return linesOfPage;
	} 
	
	
	/**
	 * Вычисляет отдельно строящие строчки в списке
	 * @return
	 * @throws Exception
	 */
	public List<InstructionsLine>  computeSepLines() throws Exception {
		
		this.сalcSeparateLines();
		
		//нужна проверка для таких случаев как 
		
				   // 1|=====================
				   // 1|3-
				   // 1|2+1
				   // 2|=====================
				   // 2|*3
		      //нельзя сшивать без *3

			
		return linesOfPage;
		
	}
	
	  /**
	    * Если страница является числом то вычислять нечего, вернуть true
	    * @return
	    */
		public boolean isDone(){	
			Matcher m;
	    	Pattern digit;
	        
	    	digit = Pattern.compile("^(-?\\d+(\\.\\d{0,})?)$");   
	        m =  digit.matcher(linesOfPage.get(0).expression);    
			
			if ((linesOfPage.size() ==1)  && (m.matches())) return true;
					
			return false;
		}
		
		/**
		 * Проверяет является страница "обрывком" предыдущей страницы
		 * @return
		 */
		public boolean isLastPartOfExpression() {
			
			if (linesOfPage.size() >1) return false;

			String  line= linesOfPage.get(0).expression;
			
			
		   	if (      (line.charAt(0)=='+') 
		    	      ||  (line.charAt(0)=='-')
		    	      ||  (line.charAt(0)=='*')
		    	      ||  (line.charAt(0)=='/')		
		    		    )     
		    			{
		   					return 	true;
		    			}
			
			
			
			return false;
		}
	
	protected int getValueOfFirstLinkOnPage() {
		
		   //ссылки второго типа всегда ссылаются на предыдущую страницу
           if (linesOfPage.size()==1) return getNumberOfPage ()-1;
		
           
           
           //ссылки первого типа нужно искать, потому что их может быть несколько.          
           for (int i=0; i< linesOfPage.size();i++)   //идет по всей странице с начала до конца
	        {
				  String line = linesOfPage.get(i).expression; 	
				  //Попалась ссылка -  распарсить и вернуть ее значение
		   	      if  ( (line.charAt(0)=='{') &&  (line.charAt(line.length()-1)=='}'))
		   		     return Integer.parseInt(line.substring(1,line.length()-1));
 	        }
           
           
		return -1;
	}
	
	
	protected void returnValueByLink(int valueOfLink, Float valueFromPrevPage) {
		 
		 int NumberOfThisPage = getNumberOfPage ();	
		
		 if (linesOfPage.size()==1) 
		 {
		      
			String line= linesOfPage.get(0).expression;
			 
			//TODO НЕТ ОБРАБОТКИ ЛИНКОВ ВТОРОГО ТИПА
			 
			 if    ( (line.charAt(line.length()-1)=='+') 
			    	 ||  (line.charAt(line.length()-1)=='-')
			    	 ||  (line.charAt(line.length()-1)=='*')
			    	 ||  (line.charAt(line.length()-1)=='/')		
			    		   )     
			    	{ 
					  linesOfPage.add(new InstructionsLine(NumberOfThisPage, Float.toString(valueFromPrevPage)));
			    			}
		 
			 
			 	
			   	if (     (line.charAt(0)=='+') 
			    	      ||  (line.charAt(0)=='-')
			    	      ||  (line.charAt(0)=='*')
			    	      ||  (line.charAt(0)=='/')		
			    		    )    
			    			{
			   		linesOfPage.add(0, new InstructionsLine( NumberOfThisPage , Float.toString(valueFromPrevPage)));
			   		//Вставить значение до знака
			    			}
			 
			 
			 } 
					
					
		//ссылки первого типа нужно искать, потому что их может быть несколько.          
        for (int i=0; i< linesOfPage.size();i++)   //идет по всей странице с начала до конца
	        {
				  String line = linesOfPage.get(i).expression; 	
				  //Попалась ссылка -  распарсить и вернуть ее значение
		   	      if  ( (line.charAt(0)=='{') &&  (line.charAt(line.length()-1)=='}'))
		   	      {
		   	    	  if (Integer.parseInt(line.substring(1,line.length()-1))==valueOfLink)
		   	    		linesOfPage.set(i, new InstructionsLine(NumberOfThisPage, Float.toString(valueFromPrevPage)));
		   	    	  break;
		   	      }
	        }
		
		
	}
	
	
	
	
	
	/**
	 * Собрать страницу в выражение и посчитать результат
	 * @throws Exception
	 */
	
	
	protected void sewPageAndCalculate() throws Exception{
		
		 //теперь сшить страницу в строку
        StringBuilder str = new StringBuilder("");
        
        int NumberOfThisPage = getNumberOfPage ();	
        
        this.applyRulesOfSigns();
		
		for (InstructionsLine element : linesOfPage) {
			
			str.append(element.expression);
		}
		
		ExpressionCalculator clc = new ExpressionCalculator("");
		
		
		linesOfPage.clear();
		//Посчитать что получилось и положить обратно
		linesOfPage.add(new InstructionsLine(NumberOfThisPage, Float.toString(clc.calc(str.toString()))));		
	}
	
	
	protected boolean pageNeedsLinkToBeCalculated(){
     //Если попался обломок на страницу в одну строку, то это тоже ссылка
	   	
		if (this.isDone()) return false;

		for (int i=0; i< linesOfPage.size();i++)   //идет по всей странице с начала до конца
	        {
	    
			  String line = linesOfPage.get(i).expression; 	
		
	    //Попалась ссылка - точно нелья сшить
	   	if  ( (line.charAt(0)=='{') &&  (line.charAt(line.length()-1)=='}'))
	   		return true;
 
	        		
	   	//знак идет первый
	   	if ( (    (line.charAt(0)=='+') 
	    	      ||  (line.charAt(0)=='-')
	    	      ||  (line.charAt(0)=='*')
	    	      ||  (line.charAt(0)=='/')		
	    		    )  &&  (linesOfPage.size()==1) )
	    			{
	   		         return true;
	    			}
	   	
	   	
	   		   	
	   	//знак идет последний		
	   	if ( (    (line.charAt(line.length()-1)=='+') 
	    	      ||  (line.charAt(line.length()-1)=='-')
	    	      ||  (line.charAt(line.length()-1)=='*')
	    	      ||  (line.charAt(line.length()-1)=='/')		
	    		    )  &&    (linesOfPage.size()==1) )
	    			{  
	   		return true;
	       }
	   	
	   }  			
		return false;
	}
	
	
  protected boolean canSewPage()
	{
		if (this.isDone()) return false;	 
		
	if (linesOfPage.size()==1)  return false; //Потому что такие надо считать, а не сшивать  
		
		
	    for (int i=0; i< linesOfPage.size();i++)   //идет по всей странице с начала до конца
	        {
	       String line = linesOfPage.get(i).expression; 
		
	       //      3|=====================
	       // >    3|{2}
	       //      3|+40
	       

	       //Попалась ссылка - точно нельзя сшить
	   	if  ( (line.charAt(0)=='{') &&  (line.charAt(line.length()-1)=='}'))
	   		return false;
 
	        }	
		return true;
	}
	
 
	
	
  
 
	
	protected void сalcSeparateLines() throws Exception
	{		
		ExpressionCalculator clc = new ExpressionCalculator("");
	    
	     int NumberOfThisPage = getNumberOfPage ();	
	    
	     		//Первым делом проходит первый цикл и считает все что возможно посчитать в одной строке
	            for (int i=0; i< linesOfPage.size();i++)   //идет по всей странице с начала до конца
			      {
						       String line = linesOfPage.get(i).expression; 
				                 						       
						       //Если что-то можно посчитать то считаем
						       if (clc.canBeCalculated(line)) {
						    	   linesOfPage.set(i,  new InstructionsLine( NumberOfThisPage , Float.toString(clc.calc(line)))) ;  //Считаем и кладем обратно
						    	   //Внимание: это не считает те обломки которые  возможно посчитать
						    	   //типо таких 3-3*343- или 34-43.2-
						       }
				 }				         
	}
    
	
	


    /**
     * сшить одну с другой страницей.
     * Метод еще тестируется.
     * @param i
     * @param l
     */
	  
	protected void sewWith(int i, String l) {		
    linesOfPage.add( new InstructionsLine(i,l) );		
	}

	private int getNumberOfPage (){
		 return linesOfPage.get(0).treelevel;
		
		}
	
	
	/**
	 * Хранит правила замены знаков,когда требуется чтобы страница была собрана в выражение
	 */
	private void applyRulesOfSigns() {
		
		 int NumberOfThisPage = getNumberOfPage ();	
		
		for (int i2=0; i2< linesOfPage.size()-1;i2++)    
       {        
       	 String line = 	 linesOfPage.get(i2).expression;
       	 String nextLine = linesOfPage.get(i2+1).expression;
       	 
       	 
       				//Если одинокий знак минус и после него число тоже с минусом
       	 			//то заменить минус на плюс
		        if  ((line.equals("-") &&
      	            (Float.parseFloat(nextLine) <0)))
      	                 
      	                { 
		        	       linesOfPage.set(i2,new InstructionsLine(NumberOfThisPage, "+"));
		        	       linesOfPage.set(i2+1, new InstructionsLine(NumberOfThisPage, Float.toString(Math.abs(Float.parseFloat(nextLine)))));
		        	       
		        	           continue;
      	                }
		
		              //Если встретился одинокий плюс и после него число меньше нуля
		        	  //то удалить этот плюс, чтобы остался минус от числа
		        if  (  (line.equals("+")
	        	          &&
	        	            (Float.parseFloat(nextLine) <0))
	        	          )       
	        	                { 
		        				linesOfPage.set(i2,new InstructionsLine(NumberOfThisPage, ""));
			        	        
			        	         
			        	           continue;
	        	                }
		        
		               //Если нашелся обломок  5+  и след. число с минусом то вырезать этот плюс    
		        if  ((line.charAt(line.length()-1) =='+')			        
			    		   &&
	        	            (Float.parseFloat(nextLine) <0))
	        	           {
					    	    String subst = line.substring(0,line.length()-1 )  ;
					    	    linesOfPage.set(i2,new InstructionsLine(NumberOfThisPage, subst));
					    	    
					    	    continue;
	        	          }  
		        
		        
		         //Если нашелся обломок  5-  и след. число с минусом 
		        //то вырезать этот минус
		        //а число сделать положительным, вырезав первый плюс
		        if  ((line.charAt(line.length()-1) =='-')			        
			    		   &&
	        	            (Float.parseFloat(nextLine) <0))
	        	           {     
		        	
		        	 			float temp = Math.abs(Float.parseFloat(nextLine));
					    	    String subst = line.substring(0,line.length()-1 );
					    	    linesOfPage.set(i2,new InstructionsLine(NumberOfThisPage, subst+"+"));
					    	    linesOfPage.set(i2+1,new InstructionsLine(NumberOfThisPage, Float.toString(temp)));
					    	    continue;
	        	          }       
		    }	
	
	}

}
