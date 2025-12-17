package application;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
	
	public void procesNumber(ActionEvent event) {
		Button inputButon = (Button)event.getSource();
		String value = inputButon.getText();
		if (start) 
			 
			start = false;
			
		
		result.setText(result.getText() + value);
	    expression.append(value);
		
	}
	
	public void procesOperator(ActionEvent event) {
		
		
		Button inputButon = (Button)event.getSource();
		String value = inputButon.getText();
		
		if (value.equals("Clear")) {
	        result.setText("");
	        answer.setText("");
	        expression.setLength(0);
	        number1 = 0;
	        operat = "";
	        start = true;
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
		if(!value.equals("=")) {
	        if (result.getText().isEmpty() ) 
				
				return ;
	        String text = result.getText();
	        String[] parts = text.split("[+\\-*/]");
	        
	        double number2 =Double.parseDouble(parts[parts.length -1]);
	        
	        

	        
	        if (!operat.isEmpty()) {
	        	
	        	number1 = model.Calculate(number1, number2, operat);
	        }else {
	        	number1 = number2;
	        }
	        
			operat = value;
			
			start = true;
			 expression.append(value);
			 
			 result.setText(expression.toString());
		 
		}else {
			 String text = result.getText();
			    String[] parts = text.split("[+\\-*/]");
			    double number2 = Double.parseDouble(parts[parts.length - 1]);
			    double output = model.Calculate(number1, number2, operat);
			    answer.setText(String.valueOf(output));

			    start = true;
			    operat = "";
			    expression.setLength(0);
			
			}
	}
	
}
