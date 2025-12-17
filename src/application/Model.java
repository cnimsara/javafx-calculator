package application;

public class Model {
	
	public double Calculate(double num1,double num2 , String operator ) {
		
		if(operator.equals("+")) {
			
			return num1 + num2;
			
		}else if(operator.equals("-")){
			
			return num1-num2;
			
		}else if(operator.equals("*")) {
			
			return num1*num2;
			
		}else if(operator.equals("/")) {
			
			if(num2 == 0 ) {
				return Double.NaN;
				
			}else {
				
			return num1 / num2 ;
			
			}
		}else {
			return 0;
		}
		
		
	}

}
