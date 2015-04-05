package org.kotikov.calc;

import java.util.ArrayList;
import java.util.List;

public class InstructionsBuilder {

	/**
	 * @param args
	 * 
	 * Класс преобразует строку (3-(2+1)+(-2-9))
	 * 
	 * В вид:
	   1|3-
	   2|2+1
	   1|+
	   2|-2-9
	   1|3-
	   2|2+1
	   1|+
	   2|-2-9
	  
	 * который можно разбирать дальше на выражения и затем вычислять
	 * 
	 */
	private StringBuilder inString;
	
	
	public InstructionsBuilder (String in) {
		 inString  = new StringBuilder(in);
	}
	
	
	public  List<InstructionsLine> makeInstructionsFromExpression() {
		
		List<InstructionsLine> expressionsStore = new ArrayList<InstructionsLine>(); 

		StringBuilder kit = new StringBuilder(""); 
		int levelOfNode = 0;  
		 
		
		if (inString.indexOf("(") ==-1)  // если нет скобок то добавляем все в коллекцию сразу же c нулевым уровнем
				expressionsStore.add( new InstructionsLine( levelOfNode , inString.toString()));		  
		   	else
			    for(int i=0; i< inString.length(); i++) {
					
			    	char ch = inString.charAt(i); //смотрим все последовательно	символ за символом		    	
			    	
			    	if (ch=='(') { 		
			    		
			    		if (kit.length() >0) {
				    		expressionsStore.add(new InstructionsLine( levelOfNode ,  kit.toString()));
				    		kit.setLength(0); 
			                //System.out.println("kit " + kit.toString());
			    			}
			    		
			    		
			    		
		    		    levelOfNode++;
                     	   if (inString.charAt(i+1)=='(') {
                               expressionsStore.add(new InstructionsLine( levelOfNode ,  "0+")); //добавляем все что есть в tmp
                             	}
                     	    
			    	    continue;
			     	}
			    	
			    	if (ch==')') 
			    	   {
			    		
			    		if (kit.length() >0) {
				    		expressionsStore.add(new InstructionsLine( levelOfNode ,  kit.toString()));
				    		kit.setLength(0); 
			                //System.out.println("kit " + kit.toString());
			    			}
			    		
			    		
			    		 levelOfNode--;
			    		 
                         continue;	
			    	     }
			    	
			    kit.append(Character.toString(ch));
			   
			     //если после последней итерации что-то осталось то добавить это тоже
			    if ((kit.length()>0) &&  (i == inString.length()-1))
			    	expressionsStore.add(new InstructionsLine( levelOfNode ,  kit.toString()));
			    	
			    }  
			   
		  
	//System.out.println(expressionsStore); //отладка   
	return expressionsStore;
   }
	/**
	 * Входящая стринга должна подаваться без пробелов, иначе работать не будет
	 * Создает список который используется для построения выражений
	 */
	public  List<InstructionsLine> makeInstructionsFromExpression2() {
		
		
		List<InstructionsLine> expressionsStore = new ArrayList<InstructionsLine>(); 
		//В конце тут будут хранятся инструкции для построения дерева выражений
		//лист хранит индексы будущего дерева, а так же последовательность выражений для правильного вычисления
		
		StringBuilder kit = new StringBuilder("");	//используется для хранения частей выражения	
		int levelOfNode = 0; //тут уровни для строительства дерева приоритетов для каждого выражения
		//expressionsStore.add("\n");
		
		if (inString.indexOf("(") ==-1)  // если нет скобок то добавляем все в коллекцию сразу же c нулевым уровнем
				expressionsStore.add( new InstructionsLine( levelOfNode , inString.toString()));		  
		   	else
			    for(int i=0; i< inString.length(); i++) {
					
			    	char ch = inString.charAt(i); //смотрим все последовательно	символ за символом	
			          
			         if (ch =='(')
			                 {			    					        
						       if (kit.length() >0)  
						    	       {  	
							    	   expressionsStore.add(new InstructionsLine( levelOfNode ,  kit.toString())); //добавляем все есть в tmp
							           kit.setLength(0); // чистим tmp, оно устарело.
							               }
						      levelOfNode++; //скобка повышает уровень
						    continue;
			             } else 
			                   if  (ch ==')'){                	   
			        	            if (kit.length() >0) expressionsStore.add(new InstructionsLine( levelOfNode ,  kit.toString()));
					                kit.setLength(0);           	               
			                      levelOfNode--;
			                      continue;
			                } else {
			                		
			                	kit.append(ch); // добавляем в буфер все, что не скобки
			                	
			                	if (i == inString.length()-1)  // если достигли конца строки то добавляем в коллекцию все что нашли  
			                			if (kit.length() >0)  expressionsStore.add(new InstructionsLine( levelOfNode ,  kit.toString()));
			                		}
			      }
		  
	//System.out.println(expressionsStore); //отладка   
	return expressionsStore;
   }
	
	
	
	/**
	 Для тестирования
	*/	
	public void show  (List<InstructionsLine> inclist)
	{
		for (int i=0; i< inclist.size();i++)
			
			 System.out.println(inclist.get(i));
		
	}
	
		
	public static void main(String[] args) {
		
		InstructionsBuilder ins = new InstructionsBuilder
				("(3-(2+1)+(-2-9))");
			///6+0+(5+(4-(3-(2+1))))+((((1+2)*3)+4)-5
			//   ((7+((1+2)*3)+4)-5)
		ins.show(ins.makeInstructionsFromExpression());
		ins.show(ins.makeInstructionsFromExpression());
		System.out.println();
		
	}


	

}
