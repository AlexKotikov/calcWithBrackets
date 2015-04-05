package org.kotikov.calc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.Position;

 
/*
 * 
 Класс
 преобразует:  [4|1+2, 3|*3, 2|+4, 1|-5, 0|/6]
				 
 В структуру:				 
			[4|1+2, 3|*3, 2|+4, 1|-5, 0|/6]
			1|--------
			4|1+2
			2|--------
			3|*3
			3|--------
			2|+4
			4|--------
			1|-5
			5|--------
			1|{4}
			0|/6
			-------------

Обработанный объект выглядит так, он больше не нужен			
			0|{5}
			3|{2}
			2|{3}
			-1|
			-1|

		//кластер - это последовательность строчек из ArrayList по которым будует строиться часть дерева в соответсвии
		//с правилами. 


  
*/



  public class PageBuilder implements Iterator {

	
	private static final char LINKMARKER = '{';
	private static final char MARKEREND = '}';
	private static final char PAGESEPARATOR = '=';
	
	private List<InstructionsLine> instructions;
    private  int position =0; //позиция элемента в коллекции для итератора
    private int numberOfPage =0 ; //счетчик страниц
    
  
    
	
    public PageBuilder(List<InstructionsLine> instructions) {	 
		this.instructions = instructions;
		 
	}

    public Iterator<InstructionsLine> createIterator(){						
		return this.buildPages().iterator();	
	}
    
    
    public List<InstructionsLine> getPages() 
    {
    	return this.buildPages();
    	
    }
    
    
	public Iterator<InstructionsLine> createIteratorForTesting(){		
		return this.instructions.iterator();
	
	}
	
	@Override
	public boolean hasNext() {
		 if (position < this.instructions.size()) return true;
		return false;
	}
	
	@Override
	public Object next() {
		InstructionsLine obj =   this.instructions.get(position);
		position++; 
        return  obj;
	}
	
	@Override
	public void remove() {
		
		try {
			throw new Exception ("Deleting is not allowed");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//[0|{5}, 4|{1}, 3|{2}, 2|{3}, -1|, -1|]
	public boolean isInstructionCalculated (){
		
		for (int i=0; i < this.instructions.size();i++){
			
			if (this.instructions.get(i).treelevel !=-1) 
				if (this.instructions.get(i).expression.charAt(0) !=LINKMARKER)
					return  false;
		}
		
		return true;
	}
	
    
	private List<InstructionsLine>  buildPages() {
		List<InstructionsLine>	result = new ArrayList<InstructionsLine>();
			
			while (!this.isInstructionCalculated()) {
				numberOfPage++;		
				result.add( //добавлять разделитель страницы
						new InstructionsLine(numberOfPage, Character.toString(PAGESEPARATOR)+"==="));	
			
				
				result.addAll(this.buildPage());  
			}		
			return result;
		}
		
	
    private int getWeightOfFirstNode() 
    {
       int max = 0;
       
         for (int i=0; i < instructions.size() ; i++) {
	    	if ((max < instructions.get(i).treelevel)
	    	 && (instructions.get(i).expression.charAt(0)!=LINKMARKER))
		          max = instructions.get(i).treelevel;
		     } 
    	
    	//первый - это максимальный
    	return max;
    }
   
    private int getNumberOfLineOfFirstNodeInList() 
    { 
       int max = 0;
       int index =0;
       
         for (int i=0; i < instructions.size() ; i++) {
	    	if ((max < instructions.get(i).treelevel)
	    		&& (instructions.get(i).expression.charAt(0)!=LINKMARKER))   
	    	 {
		          max = instructions.get(i).treelevel;
	    	      index = i; }
		     }
         
         ExpressionCalculator calc = new ExpressionCalculator("");
         
         if (index>0)
        	 if (  calc.canBeCalculated(instructions.get(index).expression))
	         if ( ( instructions.get(index-1).treelevel == max-1  )
	    	   && (( instructions.get(index-1).expression.charAt(  instructions.get(index-1).expression.length()-1)=='-')
	              ||
	              ( instructions.get(index-1).expression.charAt( instructions.get(index-1).expression.length()-1)=='+')
	              ||
	              ( instructions.get(index-1).expression.charAt( instructions.get(index-1).expression.length()-1)=='/')
	              ||
	              ( instructions.get(index-1).expression.charAt( instructions.get(index-1).expression.length()-1)=='*')
	               )) {
	    	         instructions.get(index-1).treelevel = max;
	    	         index--;
	         }
           
    	//первый - это максимальный
    	return index;
    }
	
 
	private List<InstructionsLine> buildPage() {
	
		List<InstructionsLine> kit = new  ArrayList<InstructionsLine>();		
		int weight = getWeightOfFirstNode()  ;
		int firstLineIndex = getNumberOfLineOfFirstNodeInList();
		
 
		   //Просмотр списка от первого элемента с самым высоким индексом до конца списка 
		   //Возможно что весь список состоит из таких элементов
		 for (int i=firstLineIndex; i < this.instructions.size();i++)
		      {	    
			           //Найти элемент-обломок если он стоит сразу до приоритетного элемента
			           //Изменить ему индекс на приоритетный и выйти из цикла чтобы в след. раз его забрать
			      String  test = this.instructions.get(i).expression;
			       
			           //Если нашли одиноко стоящий знак то надо его пометить иначе внешний while цикл никогда не закончится
			 		if (this.instructions.get(i).treelevel == weight-1  ) 
	     		 	  if  (this.instructions.get(i).expression.length()==1) 
	     		 	  {
		  
	     		 		this.instructions.set(i,
	     						new InstructionsLine(weight, LINKMARKER+ Integer.toString(numberOfPage)+MARKEREND+this.instructions.get(i).expression ) );
	     		 		 
	     		 	  }
			 			 else if (weight>0) break;
			 		
			 		     //Если нашли ссылку {0} то пропускать, а не пытаться переписывать
			 		if ((this.instructions.get(i).treelevel == 0) 
			 			&&  (weight==0 )
			 			&&  (this.instructions.get(i).expression.charAt(0)==LINKMARKER))
			 			  continue;
			 		
			 		
			 		//Добавить если нашли 1) элемент  2)маркер 
			 		if ( (this.instructions.get(i).treelevel==weight  )   //это элемент   
	                      		    		
			 				
			 			 ||    //это маркер    		    	 
			    	     ((this.instructions.get(i).treelevel==weight+1 )
			    		 &&	((this.instructions.get(i).expression.charAt(0)==LINKMARKER))))
				              {		
			 			       
			 			
				    		    kit.add(this.instructions.get(i)); //добавить в набор
				    		    this.instructions.set(i,new InstructionsLine(-1,""));  //и "удалить" из входящего набора
				    			    } 
				   else 
					   continue;
	
				  }	
		 
			//добавляет метки чтобы потом составить\сшить по ним узлы дерева арифметического выражения
			this.instructions.set(firstLineIndex,
					new InstructionsLine(weight, LINKMARKER+ Integer.toString(numberOfPage)+MARKEREND ));
		   
        return   kit;
	}
	
	public static void main(String[] args) throws Exception {
	
	     	InstructionsBuilder instr = new InstructionsBuilder
			
			("-1-(5-((22+22)*4-((2+1)+(-20-3)/3+(-21-2))-4*(22+22)))"); 
			     	
			PageBuilder claster =new PageBuilder(instr.makeInstructionsFromExpression());
			
			instr.show(instr.makeInstructionsFromExpression());
			System.out.println();
			Iterator<InstructionsLine> iter =  claster.createIterator();
			  
			
			while (iter.hasNext()) 
			{
					System.out.println(iter.next());	
			}
		
	}
		
	
}
