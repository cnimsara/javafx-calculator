package application;

public class Model {
	
	 public double calculate(double num1, double num2, String operator) {

	        if (operator == null) {
	            throw new IllegalArgumentException("Empty :Add some values");
	        }

	        if (operator.equals("+")) {
	            return num1 + num2;

	        } else if (operator.equals("-")) {
	            return num1 - num2;

	        } else if (operator.equals("*")) {
	            return num1 * num2;

	        } else if (operator.equals("/")) {
	            if (num2 == 0) {
	                throw new ArithmeticException("Can't Divide by Zero");
	            }
	            return num1 / num2;

	        } else {
	            throw new IllegalArgumentException("Unknown operator: " + operator);
	        }
	    }

}
