package application;

import java.util.ArrayList;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class MainController {

	
	@FXML
	private TextField result;
	private double number1 =0;
	private String operat  = "";
	private boolean start = true;
	@FXML
	private Label answer;
	
	private Model model = new Model();
	
	StringBuilder expression = new StringBuilder();
	
	@FXML
	private Pane root;
	
	
	@FXML
	//handle inputs by keyboard
	private void handleKeyPressed(KeyEvent event) {
		
		KeyCode code = event.getCode();	
	
		
		 if (code == KeyCode.EQUALS && event.isShiftDown()) {
		        handleOperator("+");
		        return;
		    }
		 
		 if (code == KeyCode.DIGIT8 && event.isShiftDown()) {
			    handleOperator("*");
			    return;
			}
		 if(code.isDigitKey()) {
			    displayNumber(event.getText());
			    return;
			}
		 
		switch (code) {
        case ADD:
        case PLUS:
            handleOperator("+");
            break;

        case SUBTRACT:
        case MINUS:
            handleOperator("-");
            break;

        case MULTIPLY:
            handleOperator("*");
            break;

        case DIVIDE:
            handleOperator("/");
            break;
            
        case ENTER:
        case EQUALS:
            handleOperator("=");
            break;
        case BACK_SPACE:
            handleOperator("Delete");
            break;        

        case ESCAPE:
            resetCalculatorState();
            break;
            
        case PERIOD:
        case DECIMAL:
        	handleDecimalPoint();
        	break;

        default:
            break;
    }
		
	}
	
	
	
	private void proccesOfNumber(String value) {
		if (result.getText().isEmpty()) 
			
			return;
		
		   
		String text = result.getText();
	    char lastChar = text.charAt(text.length() - 1);
	    
	    if (text.isEmpty()) {
	        return;
	    }

	    if(lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/') {
	    	
	    	
	    	
		    	
		    	  result.setText(text.substring(0, text.length() - 1) + value);
		          operat = value; 
		          return;
		    }

		    operat = value;
		    start = true;

		    result.setText(text + value);
	}
		
	public void resetCalculatorState()
	{
		number1 = 0;
        operat = "";
        start = true;
        expression.setLength(0);
        result.setText("");
	}
	public void displayNumber(String value) {
		
		if(start) {
			start = false;
		}
		result.setText(result.getText() + value);
		expression.append(value);
	}
	
	
	public void inputNumber(ActionEvent event) {
		Button inputButton = (Button)event.getSource();
		displayNumber(inputButton.getText());

	}
	

	public void inputOparator(ActionEvent event) {
		
		Button inputButton =(Button)event.getSource();
		
	    handleOperator(inputButton.getText());

		
	}
	
	
	

	private void handleOperator(String value) {

 
	    if (value.equals("Clear")) {
	        result.clear();
	        answer.setText("");
	        resetCalculatorState();
	        return;
	    }

	    
	    if (value.equals("Delete")) {
	        String text = result.getText();
	        if (!text.isEmpty()) {
	            result.setText(text.substring(0, text.length() - 1));
	            if (expression.length() > 0) {
	                expression.setLength(expression.length() - 1);
	            }
	        }
	        return;
	    }

	    
	 
	    if (value.equals("=")) {

 	       
	       ArrayList<String> listOfInput = new ArrayList<String>();
	       
	       String number = "";
	       
	       for(int i = 0 ; i < result.getText().length(); i++) {
	    	   char ch = result.getText().charAt(i);
	    	   
	    	   
	    	   if(Character.isDigit(ch) || ch == '.' ) {
	    		   number = number + ch;
	    	   }else {
	    		   listOfInput.add(number);
	    		   listOfInput.add(String.valueOf(ch));
	    		   number = "";
	    	   }
	       }
	       
	       listOfInput.add(number);
	       
	       for(int i = 0; i < listOfInput.size(); i++) {
	    	   String oparator = listOfInput.get(i);
	    	   
	    	   if(oparator.equals("*") || oparator.equals("/")) {
	    		   
	    		   double number1 = Double.parseDouble(listOfInput.get(i-1));
	    		   double number2 = Double.parseDouble(listOfInput.get(i+1));
	    		   
	    		   double resultsOfNum = model.calculate(number1, number2, oparator);
	    		   
	    		   
	    		   listOfInput.set(i-1, String.valueOf(resultsOfNum));
	    		   listOfInput.remove(i);
	    		   listOfInput.remove(i);
	    		   i--;
	    	   }
	    	  
	    	   }
	       for (int i = 0; i < listOfInput.size(); i++) {
   	        String oparator = listOfInput.get(i);
   	        	if(oparator.equals("+") || oparator.equals("-"))   {
   		   
		   		   double number1 = Double.parseDouble(listOfInput.get(i-1));
		   		   double number2 = Double.parseDouble(listOfInput.get(i+1));
		   		   
		   		   double resultsOfNum = model.calculate(number1, number2, oparator);
		   		   
		   		   
		   		   listOfInput.set(i-1, String.valueOf(resultsOfNum));
		   		   listOfInput.remove(i);
		   		   listOfInput.remove(i);
		   		   i--;
			     	  
			     }
	       }
	      
	       answer.setText(listOfInput.get(0));
	       resetCalculatorState();
	       return;
	       }

	    proccesOfNumber(value);
	}
	public void inputDecimal(ActionEvent event) {
		handleDecimalPoint();
	}
	private void handleDecimalPoint() {
		String text = result.getText();
		
		if(text.isEmpty()) {
			displayNumber("0.");
			return;
		}
		char lastChar = text.charAt(text.length() - 1);


		
		if(lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/'){
			displayNumber("0.");
		}else if(text.contains(".")) {
			return;
		}
		displayNumber(".");
	}
	
}
 