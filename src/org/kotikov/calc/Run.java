package org.kotikov.calc;

public class Run {

	private String incoming;

	/**
	 * Класс для запуска калькулятора со скобками.
	 *  
	 * @author ak
	 */
	
	
	public Run(String inconming) throws Exception {
		
		this.incoming = inconming.trim();	
	}
	
	
	public float  calculate()  throws Exception {
	  return this.calculate(this.incoming);
	}
	
	
	public float calculate (String inconming) throws Exception {
		
		this.incoming = inconming.trim(); //Входящее выражение
		
		
		//Класс парсит все скобки
		//Результат: список выражений с индексами
		InstructionsBuilder instr = new InstructionsBuilder (this.incoming); 
		
		//Делает маппинги выражений чтобы потом их посчитать
		//Результат: список инструкций для подсчетов
        PageBuilder mapping =new PageBuilder(instr.makeInstructionsFromExpression());

        //Считает все выражения (я назвал их страницами) 
        //Результат - число float
		PageCalculator expressions = new PageCalculator(mapping.getPages());
		
		return expressions.calcAllPages();
	} 
	
	public static void main(String[] args) throws Exception {
		Run calc = null;
		
		if (args.length >0 )
		    calc  = new Run(args[0]);
			
		else {
			System.out.println("Please specify an expression in command line");
			System.exit(0);
		}
			
		
		//Вывод подсчетов
		System.out.println("Total: "+ (calc.calculate()));
		
	}

}
