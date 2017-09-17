import java.util.ArrayList;

public class Calculator {
	
	private static boolean error = false;
	
	public static boolean isDeg = false;
	
	public static String[] operations = {"<", ">","=","(",")","+","-","*","/","^","%","!","sqrt","root","sin","cos","tan","abs","log","ln","pi","e","x","y"};

	public static boolean calculateXYFunction(String input, double x, double y){
		try{
		ArrayList<String> equation = createList(input);
		
		int split = 0;
		
		String comparison = "";
		
		boolean foundSplit = false;
		
		for(int index = 0; index < equation.size(); index++){
			if(equation.get(index).equals("<") || equation.get(index).equals(">") || equation.get(index).equals("=")){
				split = index;
				comparison = equation.get(index);
				foundSplit = true;
				break;
			}
		}
		
		if(foundSplit){
			String firstHalf = "";
			String secoundHalf = "";
			
			for(int i = 0; i < split; i++){
				firstHalf += equation.get(i);
			}
			for(int i = split+1; i < equation.size(); i++){
				secoundHalf += equation.get(i);
			}
			
			double numFirstHalf = calculateStringPrivate(firstHalf, x, y);
			double numSecoundHalf = calculateStringPrivate(secoundHalf, x, y);
			
			if(!error){
				switch(comparison){
				case "=":
					return (numFirstHalf)==(numSecoundHalf);
				case "<":
					return (numFirstHalf)<(numSecoundHalf);
				case ">":
					return (numFirstHalf)>(numSecoundHalf);
				case "<=":
					return (numFirstHalf)<=(numSecoundHalf);
				case ">=":
					return (numFirstHalf)>=(numSecoundHalf);
					
				
				}
			}
		}
		} catch (ArrayIndexOutOfBoundsException e) {}
		error = true;
		return false;
	}
	
	private static double calculateStringPrivate(String input, double x, double y){
		try {
			ArrayList<String> equation = createList(input);
			
			/*** PARENTHESES ***/
			int pCounter = 0;
			boolean useIndex = false;
			int indexStart = 0;
			int indexEnd = 0;
			boolean didSomething = false;
			String section = "";
			
			boolean done = false;
			while(!done){
				done = true;
				
				/*** SINGLE OPERATOR ***/
				for(int i = 0; i < equation.size(); i++){
				    didSomething = false;
					switch(equation.get(i)){
					
					case "e":
					    equation.set(i, "2.718281828459045");
						done = false;
						didSomething = true;
						break;
					
					case "pi":
					    equation.set(i, "3.141592653589793");
						done = false;
						didSomething = true;
						break;
						
					case "x":
					    equation.set(i, String.valueOf(x));
						done = false;
						didSomething = true;
						break;
					
					case "y":
					    equation.set(i, String.valueOf(y));
						done = false;
						didSomething = true;
						break;
						
					default :
						break;
					}
					
					if(didSomething){
    				    try {
    						if(!(isValid(equation.get(i+1)))){
    						    equation.add(i+1, "*");
    						}
    					} catch (Exception e) {
    					    
    					} 
    					try {
    						if(!(isValid(equation.get(i-1)))){
    							equation.add(i, "*");
    						}
    					} catch (Exception e) {
    					    
    					} 
				    }
				}
			}
			done = false;
			while(!done) {
				done = false;
				pCounter = 0;
				useIndex = false;
				indexStart = 0;
				indexEnd = 0;
				section = "";
				
				for(int i = 0; i < equation.size(); i++){
					if(equation.get(i).equals("(")){
						if(pCounter == 0) { indexStart = i; }
						pCounter++;
					}
					if(equation.get(i).equals(")")){
						pCounter--;
						if(pCounter == 0){ indexEnd = i; useIndex = true; break; }
					}
				}
				if (useIndex){
					for(int i = indexStart+1; i < indexEnd; i++){
						section += equation.get(i);
					}
					equation.set(indexStart, Double.toString(calculateStringPrivate(section, x, y)));
					for(int i = 0; i < Math.abs(indexEnd-indexStart); i++){
						equation.remove(indexStart+1);
					}
					done = false;
					try {
						if(!(isValid(equation.get(indexStart+1)))){
						    equation.add(indexStart+1, "*");
						}
					} catch (Exception e) {} 
					try {
						if (!(isValid(equation.get(indexStart-1)))){
							equation.add(indexStart, "*");
						}
					} catch (Exception e) {} 
				} else { done = true; }
			}
			
			done = false;
			while(!done){
			    done = true;
			    // NumberFormatException
			    if(equation.get(0).equals("-")) {
			        equation.set(0, Double.toString(0-Double.parseDouble(equation.get(1))));
        			done = false;
        			equation.remove(1);
			    }
			    for(int i = 1; i < equation.size()-1; i++){
			        if(equation.get(i).equals("-")){
    			        try{
    			            Double.parseDouble(equation.get(i-1));
    			        } catch (NumberFormatException e) {
        			        equation.set(i, Double.toString(0-Double.parseDouble(equation.get(i+1))));
        					done = false;
        					equation.remove(i+1);
    			        }
			        }
			    }
			}
			
			/*** OTHER STUFF ***/
			done = false;
			while(!done){
				done = true;
				
				/*** SINGLE OPERATOR ***/
				for(int i = 0; i < equation.size(); i++){
				    didSomething = false;
					switch(equation.get(i)){
					
					case "sqrt":
					    equation.set(i, Double.toString(Math.pow(Double.parseDouble(equation.get(i+1)), 0.5)));
						done = false;
						didSomething = true;
						equation.remove(i+1);
						break;
					
					case "sin":
						if (isDeg){
							equation.set(i, Double.toString(Math.sin(Math.toRadians(Double.parseDouble(equation.get(i+1))))));
						} else {
							equation.set(i, Double.toString(Math.sin(Double.parseDouble(equation.get(i+1)))));
						}
						didSomething = true;
						done = false;
						equation.remove(i+1);
						break;
					
					case "cos":
						if (isDeg){
							equation.set(i, Double.toString(Math.cos(Math.toRadians(Double.parseDouble(equation.get(i+1))))));
						} else {
							equation.set(i, Double.toString(Math.cos(Double.parseDouble(equation.get(i+1)))));
						}
						didSomething = true;
						done = false;
						equation.remove(i+1);
						break;
					
					case "tan":
						if (isDeg){
							equation.set(i, Double.toString(Math.tan(Math.toRadians(Double.parseDouble(equation.get(i+1))))));
						} else {
							equation.set(i, Double.toString(Math.tan(Double.parseDouble(equation.get(i+1)))));
						}
						didSomething = true;
						done = false;
						equation.remove(i+1);
						break;
					
					case "ln":
					    equation.set(i, Double.toString(Math.log(Double.parseDouble(equation.get(i+1)))));
						done = false;
						didSomething = true;
						equation.remove(i+1);
						break;
					
					case "log":
					    equation.set(i, Double.toString(Math.log10(Double.parseDouble(equation.get(i+1)))));
						done = false;
						didSomething = true;
						equation.remove(i+1);
						break;
						
					case "abs":
					    equation.set(i, Double.toString(Math.abs(Double.parseDouble(equation.get(i+1)))));
						done = false;
						didSomething = true;
						equation.remove(i+1);
						break;
					
					case "!":
						equation.set(i, Double.toString(fac(Double.parseDouble(equation.get(i-1)))));
						done = false;
						didSomething = true;
						equation.remove(i-1);
						i--;
						break;
					
					default :
						break;
					}
					if(didSomething){
    				    try {
    						if(!(isValid(equation.get(i+1)))){
    						    equation.add(i+1, "*");
    						}
    					} catch (Exception e) {
    					    
    					} 
    					try {
    						if(!(isValid(equation.get(i-1)))){
    							equation.add(i, "*");
    						}
    					} catch (Exception e) {
    					    
    					} 
				    }
				}
			}
			done = false;
			while(!done){
				done = true;
				
				/*** MULTI OPERATOR ***/
				for(int i = 0; i < equation.size(); i++){
					switch(equation.get(i)){
					
					case "root":
					    equation.set(i, Double.toString((Math.round(Math.pow(Double.parseDouble(equation.get(i+1)), (1/Double.parseDouble(equation.get(i-1))))*Math.pow(10, 13)))/Math.pow(10, 13)));
						done = false;
						equation.remove(i-1);
						equation.remove(i);
						i -= 2;
						break;
					
					case "^":
					    equation.set(i, Double.toString(Math.pow(Double.parseDouble(equation.get(i-1)), Double.parseDouble(equation.get(i+1)))));
						done = false;
						equation.remove(i-1);
						equation.remove(i);
						i -= 2;
						break;

					
					default :
						break;
					}
				}
			}
			done = false;
			while(!done){	
				done = true;
				/*** MULTIPLY/DIVIDE ***/
				for(int i = 0; i < equation.size(); i++){
					switch(equation.get(i)){
					
					case "*":
						equation.set(i, Double.toString(Double.parseDouble(equation.get(i-1))*Double.parseDouble(equation.get(i+1))));
						done = false;
						equation.remove(i-1);
						equation.remove(i);
						i -= 2;
						break;
					
					case "/":
						equation.set(i, Double.toString(Double.parseDouble(equation.get(i-1))/Double.parseDouble(equation.get(i+1))));
						done = false;
						equation.remove(i-1);
						equation.remove(i);
						i -= 2;
						break;
						
					case "%":
						equation.set(i, Double.toString(Double.parseDouble(equation.get(i-1))%Double.parseDouble(equation.get(i+1))));
						done = false;
						equation.remove(i-1);
						equation.remove(i);
						i -= 2;
						break;
					
					default :
						break;
					}
				}
			}
			done = false;
			while(!done){
				done = true;
				/*** ADD/SUBTRACT ***/
				for(int i = 1; i < equation.size(); i++){
					switch(equation.get(i)){
					
					case "+":
						equation.set(i, Double.toString(Double.parseDouble(equation.get(i-1))+Double.parseDouble(equation.get(i+1))));
						done = false;
						equation.remove(i-1);
						equation.remove(i);
						i -= 2;
						break;
					
					case "-":
						equation.set(i, Double.toString(Double.parseDouble(equation.get(i-1))-Double.parseDouble(equation.get(i+1))));
						done = false;
						equation.remove(i-1);
						equation.remove(i); // after removing a num, no need to add 1
						i -= 2;
						break;
						
					default :
						break;
					}
				}
			}
			if (equation.size() > 1) { 
		        error = true;
			}
			return Double.valueOf(equation.get(0));
		} catch(Exception e) {
			error = true;
			return 0.0;
		} 
	}

	
	private static boolean isValid(String input){
		for(int o = 0; o < operations.length; o++){
			if (input.equals(operations[o])){
				return true;
			}
		}
		return false;
	}
	
	private static double fac(double a){
		a = Math.round(a);
		if (a > 1){
			return a*fac(a-1);
		} else{ return 1; }
	}
	
	private static ArrayList<String> createList(String input){
		ArrayList<String> output = new ArrayList<String>();
		String temp = null;
		
		input += " ";
		
		for (int i = 0; i < input.length(); i++){
			temp = "";
			
			boolean numFound = false;
			while(i < input.length()){
				if (isNum(input.charAt(i)) || input.charAt(i) == '.'){
					temp += String.valueOf(input.charAt(i));
					i++;
					numFound = true;
				} else if (numFound) { i--; output.add(temp); break; }
				else { break; }
			}
			
			for(int o = 0; o < operations.length; o++){
			    for(int j = 0; j < operations[o].length(); j++){
    				if (input.charAt(i) == operations[o].charAt(j)){
    					temp += String.valueOf(operations[o].charAt(j));
    					if(!(j < operations[o].length()-1)) { o = operations.length; output.add(temp); break; }
    					i++;
    				} else { temp = ""; i -= j; break; }
			    }
			}
		}
		
		return output;
	}
	
	private static boolean isNum(char num){
		for(int i = 0; i < 10; i++){
			if (num == Character.forDigit(i, 10)){
				return true;
			}
		}
		return false;
	}
}
