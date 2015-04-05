package org.kotikov.calc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


   /**
    * Класс вычисляет содержимое всех страниц, и возвращает общий результат всего выражения.
    * 
    * @author ak
    *
    */
 
public class PageCalculator {

	private static final char PAGESEPARATOR = '=';
	/**
	 * 
	 * 
	 * Получает объект 
	 * Просматривает каждую страницу 
	 * 			вычисляет ее 
	 * 			результат кладет в новый лист результатов, где номер результата = номеру страницы
	 * 
	 * удаляет все 0+
	 */
	
	private List<InstructionsLine> pagesList;
	 
	
	
	public PageCalculator(List<InstructionsLine> pages) {
      this.pagesList =  pages;
	}
	
  public float calcAllPages() throws Exception{
		   
        this.normalizePages();
	    
		for (  InstructionsLine elem   :  this.pagesList )    {
			 System.out.println(elem);
		   }  
   
		int countOfPages	= getCountOfPages();
		
		
		List<Float> resultsofPagesList  = new ArrayList<Float> ();
		resultsofPagesList.add((float)555);
		
	 
		  
      for (int i=1; i<=getCountOfPages();i++)	  
      {
		  Page page = new Page(getLinesOfPage(i));
		 // System.out.println("page " + page.getlines()); 
		  
		  while (page.pageNeedsLinkToBeCalculated())
			 {
			  page.returnValueByLink(page.getValueOfFirstLinkOnPage(),resultsofPagesList.get(page.getValueOfFirstLinkOnPage()));
			  }
		  
		  page.computeSepLines();
		  
		     //Проверка что следующая страница это кусок от текущей, если да то взять ее значение
		  	 //а туда вставить ссылку на текущую страницу
		  if (i<= getCountOfPages()-1) {
	             Page NextPage = new Page(getLinesOfPage(i+1)); 
	                if (NextPage.isLastPartOfExpression()) {
	                	 
	                	String l = NextPage.getlines().get(0).expression;
	                	
	                	page.sewWith(i,l);
	                	 this.replaceValueWithLinkToPrevPage(i+1); //Заменить значение ее на ссылку {i}    	  
	                }
	             }
		  
		  page.sewPageAndCalculate();
		  resultsofPagesList.add((float) page.resultOfPage());	  
	 }	  
 
  //Для тестирования
 /* 
  System.out.println("values");
	for (  Float elem   :   resultsofPagesList )    {
		 System.out.println(elem);
	}
     */
	   
        //Результат последней страницы есть результат всего выражения
		return resultsofPagesList.get(resultsofPagesList.size()-1); //results.get(results.size())  ;
	}

	
	
	private void normalizePages() throws Exception{
				
		Iterator<InstructionsLine> iter =  this.pagesList.iterator();
		int pageNumber =1; 
		
		while (iter.hasNext()) {
			 
		 InstructionsLine line  = iter.next();
	 
		     //удалить незначащие нули
		 if	(line.expression=="0+") { 
			  iter.remove();
		      continue;
		     }
	     
		 
		 //находим идентификатор страницы чтобы потом пометить к какой странице относится эта строка
			  if (line.expression.charAt(0)==PAGESEPARATOR)
			    	 pageNumber =  line.treelevel;
		 
		 
		    // пометить каждую строку к какой странице она относится.  
		 if (line.expression.charAt(0)!=PAGESEPARATOR)
			 line.treelevel=pageNumber;
		 
	 
		 		// Привести шаблоны типа  {0}- ->  - ,   {0}+ ->  + и тд
		 if  ( (line.expression.charAt(0)=='{') &&
		        (line.expression.charAt(line.expression.length()-2)=='}')
			 )	 {line.expression = Character.toString(line.expression.charAt(line.expression.length()-1));}    
		
	
		 /*
		 ExpressionCalculator  calc = new ExpressionCalculator("");
		 if  (calc.canBeCalcualted(line.expression))   
			 line.expression = Float.toString(calc.calc(line.expression));*/
		}
			
	};
	
	private int getCountOfPages(){
		int page =1; 
		  
		//Нужная страница в самом конце
		for ( InstructionsLine elem   :  this.pagesList ) {
			 if (elem.treelevel >0) page = elem.treelevel;			 
		}

		return page;
	};
	
	private List<InstructionsLine> getLinesOfPage(int needPage){
		List<InstructionsLine>  res= new ArrayList<InstructionsLine> ();
		
		
		//брать все что с нужным индексом и при этом не заголовок страницы
		for ( InstructionsLine elem   :  this.pagesList ) {
			 if (elem.treelevel == needPage ) 
			 {
				 if (elem.expression.charAt(0)!=PAGESEPARATOR)
				     res.add(elem);
				 }
			  else 
				   continue;	 
		}	
		return res;
	};
	
	private void replaceValueWithLinkToPrevPage(int needPage) 
	{
			
		for ( InstructionsLine elem   :  this.pagesList ) 
		{
			 if (elem.treelevel == needPage ) 
			 {
				 if (elem.expression.charAt(0)!=PAGESEPARATOR)
				 {
					 elem.expression="{"+ (needPage-1) +"}";
				     //System.out.println("eee"+ elem.expression);
				 	
				     break;
				 }
			 }
	     }
   }
	
	
 
	
 
}
