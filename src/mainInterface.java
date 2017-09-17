import java.awt.*;
import java.util.Scanner;

import javax.swing.*;

public class mainInterface {
	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		
		System.out.println("---MATH---");
		System.out.print("Enter X/Y Equation: ");
		String equation = input.nextLine();
		
		System.out.println("\n\n---DISPLAY DIMENSIONS---");
		System.out.print("Enter Maximum X Cordinate: ");
		double maxX = Double.valueOf(input.nextLine());
		System.out.print("\nEnter Minimum X Cordinate: ");
		double minX = Double.valueOf(input.nextLine());
		System.out.print("\nEnter Maximum Y Cordinate: ");
		double maxY = Double.valueOf(input.nextLine());
		System.out.print("\nEnter Minimum Y Cordinate: ");
		double minY = Double.valueOf(input.nextLine());
		
		System.out.println("\n\n---MISC. VISUAL SETTINGS---");
		System.out.println("Type \"MSAA\" Or \"SSAA\" To Choose Anti-Aliasing Method:");
		System.out.println("\n	MSAA (Default): ");
		System.out.println("		1. Slightly Faster. ");
		System.out.println("		2. Looks Much Better For Thin Lines. ");
		System.out.println("		3. Does Not Look As Good For Bigger Shapes. ");
		System.out.println("\n	SSAA (Alpha): ");
		System.out.println("		1. Slightly Slower. ");
		System.out.println("		2. Looks Very Faint For Thin Lines. (Less Than or Greater Than Is Strongly Recomended)");
		System.out.println("		3. Smooths Out Edges Much Better For Bigger Shapes. ");
		System.out.print("\nEnter Anti-Aliasing Method: ");
		boolean ssaa = false;
		try {
			String aaMethod = input.nextLine();
			if(aaMethod.toLowerCase().charAt(0) == 's'){
				ssaa = true;
				System.out.print("\nEnter SSAA Sample Multplyer: ");
			} else {
				System.out.print("\nEnter MSAA Sample Multplyer: ");
			}
		} catch (Exception e){
			System.out.print("\nEnter MSAA Sample Multplyer: ");
		}
		double aaMultplyer = 0.5/Double.valueOf(input.nextLine());
		
		printGrid(equation,maxX,minX,maxY,minY,aaMultplyer,ssaa);
		
		input.close();
		
		System.out.println("\n\n\n-----FINISHED RENDERING-----\n\n\n");
	}
	
	public static void printGrid(String input,double maxX, double minX, double maxY, double minY,double decimal, boolean ssaa){
		JFrame frame = new JFrame("Graphing Calculator (0%): " + input);
		
		JTextField titlePercent;
		
		frame.setSize(800,800);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    frame.setLayout(new GridLayout((int) (Math.abs(minX)+Math.abs(maxX)+1), (int) (Math.abs(minY)+Math.abs(maxY)+1)));
	    
		Color color = null;
		boolean skipFail;
		double ssaaHits = 0;
		double ssaaTries = 0;
		double op;
		double temp;
		for(double y = maxY; y >= minY; y--){
			frame.setVisible(true);
			if (ssaa){
				titlePercent = new JTextField("Graphing Calculator (" + ((y-maxY)/(minY-maxY)*(100)) + "%) (SSAA: " + (0.5/decimal) + "X Sample Multplyer) (" + input + ")"); 
			} else {
				titlePercent = new JTextField("Graphing Calculator (" + ((y-maxY)/(minY-maxY)*(100)) + "%) (MSAA: " + (0.5/decimal) + "X Sample Multplyer) (" + input + ")"); 
			}
			frame.setTitle(titlePercent.getText());
			for(double x = minX; x <= maxX; x++){
				JPanel tempj = new JPanel();
				op = 0;
				skipFail = true;
				if (!ssaa){
					if(Calculator.calculateXYFunction(input, (x), (y))){
						op = 255;
						skipFail = false;
					} 
				} 
				if (ssaa || skipFail){
					ssaaHits = 0;
					ssaaTries = 0;
					for(double yy = 0.5; yy >= -0.5; yy-=decimal){
						for(double xx = -0.5; xx <= 0.5; xx+=decimal){
							xx = Math.round(xx / decimal) * decimal;
							yy = Math.round(yy / decimal) * decimal;
							temp = 0;
							if(Calculator.calculateXYFunction(input, (x+xx), (y+yy))){
								if(ssaa){
									ssaaHits += 255;
								} else {
									temp = (255-(Math.abs(xx*255)+Math.abs(yy*255)));
									if(temp > op) { op = Math.round(temp / decimal) * decimal; }
								}
							} 
							ssaaTries++;
						}
					}
				}
				if(ssaa){ op = ssaaHits/ssaaTries; }
				
				tempj.setBounds(((int) ((Math.abs(minX)+x)*10)), ((int) ((Math.abs(minY)+y)*10)), (int) ((Math.abs(minX)+Math.abs(maxX)+1)), (int) ((Math.abs(minY)+Math.abs(maxY)+1)));
				if (x == 0 || y == 0){
					color = new Color(Math.min(255, (255 - (int) (op))), 0, (int) (op));
				} else {
					color = new Color(255 - (int) (op), 255 - (int) (op), 255);
				} 
				tempj.setBackground(color);
				frame.add(tempj);
			}
		}
		if (ssaa){
			titlePercent = new JTextField("Graphing Calculator (100.0%) (SSAA: " + (0.5/decimal) + "X Sample Multplyer) (" + input + ")"); 
		} else {
			titlePercent = new JTextField("Graphing Calculator (100.0%) (MSAA: " + (0.5/decimal) + "X Sample Multplyer) (" + input + ")"); 
		}
		frame.setTitle(titlePercent.getText());
		frame.setVisible(true);
	}
}
