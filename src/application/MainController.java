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
	private boolean start = true;
	
	@FXML
	private Button backspaceButton;
	@FXML
	private Button percentButton;
	@FXML
	private Label answer;
	
	private Model model = new Model();
	
 	
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

	    for (char c : text.toCharArray()) {
	        if (c == '(') openCount++;
	        if (c == ')') closeCount++;
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
	
	
	private double applyAddSubtract(ArrayList<String> tokens) {

        for (int i = 0; i < tokens.size(); i++) {

            String op = tokens.get(i);

            if (op.equals("+") || op.equals("-")) {

                double left = Double.parseDouble(tokens.get(i - 1));
                double right = Double.parseDouble(tokens.get(i + 1));

                double result = model.calculate(left, right, op);

                tokens.set(i - 1, String.valueOf(result));
                tokens.remove(i);
                tokens.remove(i);
                i--;
            }
        }

        return Double.parseDouble(tokens.get(0));
    }
    
    private void applyMultiplyDivide(ArrayList<String> tokens) {

        for (int i = 0; i < tokens.size(); i++) {

            String op = tokens.get(i);

            if (op.equals("*") || op.equals("/")) {

                double left = Double.parseDouble(tokens.get(i - 1));
                double right = Double.parseDouble(tokens.get(i + 1));

                double result = model.calculate(left, right, op);

                tokens.set(i - 1, String.valueOf(result));
                tokens.remove(i);
                tokens.remove(i);
                i--;
            }
        }
    }

    private ArrayList<String> tokenize(String expr) {

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



   
    private void applyPercentage(ArrayList<String> tokens) {

        for (int i = 0; i < tokens.size(); i++) {

            if (tokens.get(i).equals("%")) {

                double percent = Double.parseDouble(tokens.get(i - 1));
                double percentValue;

                if (i < 2) {
                    percentValue = percent / 100;
                } else {
                    String prevOperator = tokens.get(i - 2);

                    if (prevOperator.equals("+") || prevOperator.equals("-")) {
                        double base = Double.parseDouble(tokens.get(i - 3));
                        percentValue = base * percent / 100;
                    } else {
                        percentValue = percent / 100;
                    }
                }

                tokens.set(i - 1, String.valueOf(percentValue));
                tokens.remove(i);
                i--;
            }
        }
    }
    
    private double evaluateWithBrackets(ArrayList<String> tokens) {

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

            double innerResult = evaluateExpression(inner);

            for (int i = closeIndex; i >= openIndex; i--) {
                tokens.remove(i);
            }

            tokens.add(openIndex, String.valueOf(innerResult));
        }

        return evaluateExpression(tokens);
    }
    
   



    private void appendWithAutoMultiply(String value) {

        String text = result.getText();

        if (!text.isEmpty() && value.equals("(")) {

            char lastChar = text.charAt(text.length() - 1);

            boolean lastIsValue =
                    Character.isDigit(lastChar) ||
                    lastChar == ')' ||
                    lastChar == '%';

            if (lastIsValue) {
                result.setText(text + "*");
            }
        }

        result.setText(result.getText() + value);
    }



    private void toggleSign() {

        String text = result.getText();
        if (text.isEmpty()) return;

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


    private double evaluateExpression(ArrayList<String> tokens) {

        applyPercentage(tokens);
        applyMultiplyDivide(tokens);
         return applyAddSubtract(tokens);
    }
    private boolean areBracketsBalanced(String expr) {
    	int count = 0; 
    	for (char c : expr.toCharArray()) { 
    		if (c == '(') count++; 
    		if (c == ')') count--; 
    		if (count < 0)
    			return false; 
    	} 
    	
    	return count == 0; 
    		
    }
    private void liveCalculate() {

        try {
            String expr = result.getText();

            if (expr.isEmpty()) return;

            char last = expr.charAt(expr.length() - 1);
            if ("+-*/(".indexOf(last) != -1) return;

            if (!areBracketsBalanced(expr)) return;

            ArrayList<String> tokens = tokenize(expr);
            double value = evaluateExpression(tokens);

            answer.setText(String.valueOf(value));

        } 
        catch (ArithmeticException e) {
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
 	       
	    	  ArrayList<String> tokens = tokenize(result.getText());
	    	    double finalResult = evaluateWithBrackets(tokens);

	    	    answer.setText(String.valueOf(finalResult));
	    	}catch (ArithmeticException e) {
	    		
	            answer.setText("Error");

	    		
	    	}
	    	return;

	      
	}handleOperatorInput(value);}
	    
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
			return;
		}
		displayNumber(".");
	}
	
}
 