package application.model;

import java.util.ArrayList;




public class MathsRule {
	MathematicalOperations mathematicalOperations = new MathematicalOperations();
	
	   public double evaluateExpression(ArrayList<String> tokens) {

	    	 applyPercentage(tokens);

	    	 applyMultiplyDivide(tokens);
	         return   applyAddSubtract(tokens);
	    } 
	
	   public void applyPercentage(ArrayList<String> tokens) {

		   for (int i = 0; i < tokens.size(); i++) {

		        if (tokens.get(i).equals("%")) {

		            double percent = Double.parseDouble(tokens.get(i - 1));
		            double percentValue;

		            String prevOp = null;
		            String nextOp = null;

		            if (i >= 2) {
		                prevOp = tokens.get(i - 2);
		            }

		            if (i + 1 < tokens.size()) {
		                nextOp = tokens.get(i + 1);
		            }
		          
 		        if ("*".equals(nextOp) || "/".equals(nextOp)) {
		            percentValue = percent / 100;
		        }
 		        else if ("+".equals(prevOp) || "-".equals(prevOp)) {
		            double base = Double.parseDouble(tokens.get(i - 3));
		            percentValue = base * percent / 100;
		        }
		        else {
		            percentValue = percent / 100;
		        }

		        tokens.set(i - 1, String.valueOf(percentValue));
		        tokens.remove(i);
		        i--;
		    }
		}
	   }
	 public double evaluateWithBrackets(ArrayList<String> tokens) {

	        while (tokens.contains("(")) {

	            int closeIndex = tokens.indexOf(")");
	            int openIndex = closeIndex;

	            while (!tokens.get(openIndex).equals("(")) {
	                openIndex--;
	            }

	            ArrayList<String> inner = new ArrayList<>();
	            for (int i = openIndex + 1; i < closeIndex; i++) {
	                inner.add(tokens.get(i));
	            }

	            double innerResult =  evaluateExpression(inner);

	            for (int i = closeIndex; i >= openIndex; i--) {
	                tokens.remove(i);
	            }

	            tokens.add(openIndex, String.valueOf(innerResult));
	        }

	        return  evaluateExpression(tokens);
	    }
	    
	 public boolean areBracketsBalanced(String expr) {
	    	int count = 0; 
	    	for (char c : expr.toCharArray()) { 
	    		if (c == '(') count++; 
	    		if (c == ')') count--; 
	    		if (count < 0)
	    			return false; 
	    	} 
	    	
	    	return count == 0; 
	    		
	    }
	public void applyMultiplyDivide(ArrayList<String> tokens) {

	        for (int i = 0; i < tokens.size(); i++) {

	            String op = tokens.get(i);

	            if (op.equals("*") || op.equals("/")) {

	                double left = Double.parseDouble(tokens.get(i - 1));
	                double right = Double.parseDouble(tokens.get(i + 1));

	                double result = mathematicalOperations.calculate(left, right, op);

	                tokens.set(i - 1, String.valueOf(result));
	                tokens.remove(i);
	                tokens.remove(i);
	                i--;
	            }
	        }
	    }
	public double applyAddSubtract(ArrayList<String> tokens) {

        for (int i = 0; i < tokens.size(); i++) {

            String op = tokens.get(i);

            if (op.equals("+") || op.equals("-")) {

                double left = Double.parseDouble(tokens.get(i - 1));
                double right = Double.parseDouble(tokens.get(i + 1));

                double result = mathematicalOperations.calculate(left, right, op);

                tokens.set(i - 1, String.valueOf(result));
                tokens.remove(i);
                tokens.remove(i);
                i--;
            }
        }

        return Double.parseDouble(tokens.get(0));
    }
	
	
	 public ArrayList<String> tokenize(String expr) {

	        ArrayList<String> tokens = new ArrayList<>();
	        String number = "";

	        for (int i = 0; i < expr.length(); i++) {
	            char ch = expr.charAt(i);

	             if (ch == '-' && (i == 0 || expr.charAt(i - 1) == '(')) {
	                number += ch;
	                continue;
	            }

	            if (Character.isDigit(ch) || ch == '.') {
	                number += ch;
	            } else {
	                if (!number.isEmpty()) {
	                    tokens.add(number);
	                    number = "";
	                }
	                tokens.add(String.valueOf(ch));
	            }
	        }

	        if (!number.isEmpty()) {
	            tokens.add(number);
	        }

	        return tokens;
	    }
	 
	 public double liveCalculateValue(String expression) {

		    if (expression == null || expression.isEmpty()) {
		        throw new IllegalArgumentException();
		    }

		    char last = expression.charAt(expression.length() - 1);
		    if (last == '+' || last == '-' || last == '*' || last == '/') {
		        throw new IllegalStateException();
		    }

		    if (!areBracketsBalanced(expression)) {
		        throw new IllegalStateException();
		    }

		    ArrayList<String> tokens = tokenize(expression);
		    return evaluateWithBrackets(tokens);
		}



}
