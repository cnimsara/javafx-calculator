package application;

import application.model.MathsRule;
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
	private boolean start = true;
	
	@FXML
	private Button backspaceButton;
	@FXML
	private Button percentButton;
	@FXML
	private Label answer;
	
	private MathsRule mathsRule = new MathsRule();
	@FXML
	private Pane root;
	
	
	@FXML
	public void initialize() {

	    root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

	        if (event.getCode() == KeyCode.ENTER) {

	            handleOperator("=");
	            event.consume();
	        }
	    });
	}

	@FXML
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
		 if (code == KeyCode.DIGIT9 && event.isShiftDown()) {
			    appendWithAutoMultiply("(");
 
			    return;
			}
		 if (code == KeyCode.DIGIT5 && event.isShiftDown()) {
			    handleOperator("%");   
			    return;
			}

			if (code == KeyCode.DIGIT0 && event.isShiftDown()) {
			    result.setText(result.getText() + ")");
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
        case SLASH:
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
	
	
	
	private void handleOperatorInput(String value) {
		if (result.getText().isEmpty()) 
			
			return;
		
		   
		String text = result.getText();
	    char lastChar = text.charAt(text.length() - 1);
	    

	    if(lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/') {
	    	
	    	
	    	
		    	
		    	  result.setText(text.substring(0, text.length() - 1) + value);
		          return;
		    }

		    start = true;

		    result.setText(text + value);
	}
		
	public void resetCalculatorState()
	{
		start = true;
        answer.setText(""); 
        result.setText("");
        
	}
	public void displayNumber(String value) {
		
		if(start) {
			start = false;
		}
		appendWithAutoMultiply(value);
	    liveCalculate();

 	}
	
	
	public void inputNumber(ActionEvent event) {
		Button inputButton = (Button)event.getSource();
		displayNumber(inputButton.getText());

	}
	
	private void insertBracket() {

	    String text = result.getText();

	    int openCount = 0;
	    int closeCount = 0;

	    for (int i = 0; i < text.length(); i++) {
	        char c = text.charAt(i);

	        if (c == '(') {
	            openCount++;
	        } else if (c == ')') {
	            closeCount++;
	        }
	    }

	    if (openCount == closeCount ||
	        text.endsWith("(") ||
	        text.endsWith("+") ||
	        text.endsWith("-") ||
	        text.endsWith("*") ||
	        text.endsWith("/")) {

	        appendWithAutoMultiply("(");
 
	    } else {
	        result.setText(text + ")");
	    }
	    
	    
	    liveCalculate();
	}

	public void inputOparator(ActionEvent event) {
		
		Button inputButton =(Button)event.getSource();
	    String text = inputButton.getText();

		 if (inputButton == backspaceButton) {
		        handleOperator("Delete");
		        return;
		    }
		 
		 if (inputButton == percentButton) {
		        handleOperator("%");
		        return;
		    }
		 
		 if (inputButton.getText().equals("(")) {
			    appendWithAutoMultiply("(");
			    return;
			}


		    if (text.equals("()")) {
		        insertBracket();
		        return;
		    }
		    if (text.equals("-/+")) {
		        toggleSign();
		        return;
		    }
		
	    handleOperator(inputButton.getText());

		
	}   
    
    private void appendWithAutoMultiply(String value) {

        String text = result.getText();


        if (!text.isEmpty()) {
            char lastChar = text.charAt(text.length() - 1);

            boolean needMultiply =
                    (lastChar == ')' && (Character.isDigit(value.charAt(0)) || value.equals("("))) ||
                    (lastChar == '%' && (Character.isDigit(value.charAt(0)) || value.equals("("))) ||
                    ((Character.isDigit(lastChar) || lastChar == '.') && value.equals("("));

            if (needMultiply) {
                result.setText(text + "*");
                
            }
        }

        result.setText(result.getText() + value);
        
          
        
    }



    private void toggleSign() {

        String text = result.getText();
        if (text.isEmpty()) 
        	return;

        int i = text.length() - 1;

         while (i >= 0 && (Character.isDigit(text.charAt(i)) || text.charAt(i) == '.')) {
            i--;
        }

        int numberStart = i + 1;
        if (numberStart >= text.length()) return;

        String number = text.substring(numberStart);
        String before = text.substring(0, numberStart);

         if (before.endsWith("(") && text.endsWith(")")) {
            return;  
        }

         if (before.endsWith("(") && number.endsWith(")")) {
            return;
        }

         if (numberStart >= 2 &&
            text.charAt(numberStart - 1) == '(' &&
            text.endsWith(")")) {

            String inner = text.substring(numberStart, text.length() - 1);

            if (inner.startsWith("-")) {
                result.setText(
                    text.substring(0, numberStart - 1) + "(" + inner.substring(1) + ")"
                );
            } else {
                result.setText(
                    text.substring(0, numberStart - 1) + "(-" + inner + ")"
                );
            }
            return;
        }

         if (number.startsWith("-")) {
            result.setText(before + "(" + number.substring(1) + ")");
        } else {
            result.setText(before + "(-" + number + ")");
        }
    }


 
   
   
    private void liveCalculate() {
        try {
        	 double value = mathsRule.liveCalculateValue(result.getText());
             answer.setText(String.valueOf(value));
         } catch (ArithmeticException e) {
             answer.setText("Can't Divide by Zero");
        }
            catch (Exception e) {
            	
        }
    }

	private void handleOperator(String value) {

 
	    if (value.equals("C")) {
	        result.clear();
	        answer.setText("");
	        resetCalculatorState();
	        return;
	    }

	    
	    if (value.equals("Delete")) {
	        String text = result.getText();
	        if (!text.isEmpty()) {
	            result.setText(text.substring(0, text.length() - 1));
	            liveCalculate(); 
	            
	        }
	        return;
	    }


	    
	    
	    if (!value.equals("=")) {
	        liveCalculate();
	    }

	 
	    if (value.equals("=")) {
	        try {
	            double finalResult = mathsRule.liveCalculateValue(result.getText());
	            answer.setText(String.valueOf(finalResult));
	        } catch (ArithmeticException e) {
	            answer.setText("Can't Divide by Zero");
	        } catch (Exception e) {
	            answer.setText("Error");

	        }
	        return;
	    }
 
	      
	handleOperatorInput(value);}
	    
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
		    if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/') {
				displayNumber("0.");
				return;
		    }
		    if(lastChar == ')' ) {
				displayNumber("*0.");
				return;

		    }
		    int i = text.length() - 1;
		    
		    while (i>= 0 && (Character.isDigit(text.charAt(i)) || text.charAt(i) == '.')) {
		          	
		    	i--;
		    }
		    
		    String currentNumber = text.substring(i+1);
		    if(currentNumber.contains(".")) {
		    	return;
		    	
		    }
		    
		    displayNumber(".");
		
		
		
	}
	
}
 