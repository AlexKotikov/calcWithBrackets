package org.kotikov.calc;

public class InstructionsLine {

	
	/**
	 * Объекты класса содержат пару:  индекс узла и выражение которое должно быть 
	 * подсчитано.
	 * Используется в дальнейшем для создания страниц. 
	 *  
	 */
	
	int treelevel;
	String expression;
	 
	
	 
	@Override
	public String toString() {
		 
		return   "   " + treelevel + "|"+ expression;//+  "\n"
	}

	public InstructionsLine(int treelevel, String expression) {		 
		this.treelevel = treelevel;
		this.expression = expression;
	}
 
	public int getTreelevel() {
		return treelevel;
	}
 
	public String getExpression() {
		return expression;
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
