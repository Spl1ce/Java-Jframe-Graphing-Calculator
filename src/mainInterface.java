import java.awt.*;
import java.util.Scanner;

import javax.swing.*;

public class mainInterface {
	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		
		System.out.println("Cool Equations:");
		System.out.println("	1. x^2 + y^2 < 16. (8 by 8 Circle)");
		System.out.println("	2. (x^2 + y^2-1)^3-x^2*y^3 < 0. (A 4 by 4 Heart)");
		
		System.out.println("\n---MATH---");
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
		System.out.println("\n(Answer Must Be A Power Of 2)");
		System.out.println("(Preformance Hit Is Huge)");
		System.out.print("Internal Resolution Multiplier: ");
		double resMultiplier = nextPowerOf2(Double.valueOf(input.nextLine()));
		
		System.out.println("\n\nType \"MSAA\" Or \"SSAA\" To Choose Anti-Aliasing Method:");
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
				System.out.println("\n(Answer Must Be A Power Of 2 Or 0 For No SSAA)");
				System.out.print("Enter SSAA Sample Multiplier: ");
			} else {
				System.out.println("\n(Answer Must Be A Power Of 2 Or 0 For No MSAA)");
				System.out.print("Enter MSAA Sample Multiplier: ");
			}
		} catch (Exception e){
			System.out.println("\n(Answer Must Be A Power Of 2 Or 0 For No MSAA)");
			System.out.print("Enter MSAA Sample Multiplier: ");
		}
		
		double aaMultiplier = 0;
		
		double dInput = Double.valueOf(input.nextLine());
		
		if(dInput == 0) {
			printGrid(equation,maxX,minX,maxY,minY,0,resMultiplier,ssaa);
		} else {
			aaMultiplier = 0.5/nextPowerOf2(dInput);
			printGrid(equation,maxX,minX,maxY,minY,aaMultiplier,resMultiplier,ssaa);
		}
		input.close();
		
		System.out.println("\n\n\n-----FINISHED RENDERING-----\n\n\n");
	}
	
	public static void printGrid(String input,double maxX, double minX, double maxY, double minY,double decimal,double resMultiplier,boolean ssaa){
		JFrame frame = new JFrame("Graphing Calculator (0%): " + input);
		
		JTextField titlePercent;
		
		frame.setResizable(false);
		
		frame.setSize((int) (800*((maxX-minX)/(maxY-minY))),(800));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    frame.setLayout(new GridLayout((int) (Math.abs(minY*resMultiplier)+Math.abs(maxY*resMultiplier)+1), (int) (Math.abs(minX*resMultiplier)+Math.abs(maxX*resMultiplier)+1)));
	    
		Color color = null;
		boolean skipFail;
		double ssaaHits = 0;
		double ssaaTries = 0;
		double op;
		double temp;
		double stepMultiplier = 1/resMultiplier;
		double sampleStepMultiplier = decimal/resMultiplier;
		for(double y = maxY; y >= minY; y-= stepMultiplier){
			frame.setVisible(true);
			
			if (decimal!=0) {
				if (ssaa){
					titlePercent = new JTextField("Graphing Calculator (" + (int) ((y-maxY)/(minY-maxY)*(100)) + "%) (Resolution: " + resMultiplier + "X) (SSAA " + (int) (0.5/(decimal)) + "X) (" + input + ")"); 
				} else {
					titlePercent = new JTextField("Graphing Calculator (" + (int) ((y-maxY)/(minY-maxY)*(100)) + "%) (Resolution: " + resMultiplier + "X) (MSAA " + (int) (0.5/(decimal)) + "X) (" + input + ")"); 
				}
			} else {
				titlePercent = new JTextField("Graphing Calculator (" + (int) ((y-maxY)/(minY-maxY)*(100)) + "%) (Internal Resolution Multiplier: " + resMultiplier + "X) (" + input + ")"); 
			}
			
			frame.setTitle(titlePercent.getText());
			for(double x = minX; x <= maxX; x+=stepMultiplier){
				JPanel tempj = new JPanel();
				op = 0;
				skipFail = true;
				if (!ssaa && decimal!=0){
					if(Calculator.calculateXYFunction(input, (x), (y))){
						op = 255;
						skipFail = false;
					} 
				} 
				if ((ssaa || skipFail) && decimal!=0){
					ssaaHits = 0;
					ssaaTries = 0;
					for(double yy = 0.5/resMultiplier; yy >= -0.5/resMultiplier; yy-=sampleStepMultiplier){
						for(double xx = -0.5/resMultiplier; xx <= 0.5/resMultiplier; xx+=sampleStepMultiplier){
							xx = Math.round(xx / sampleStepMultiplier) * sampleStepMultiplier;
							yy = Math.round(yy / sampleStepMultiplier) * sampleStepMultiplier;
							temp = 0;
							if(Calculator.calculateXYFunction(input, (x+xx), (y+yy))){
								if(ssaa){
									ssaaHits += 255;
								} else {
									temp = (255-(Math.abs(xx*(255*resMultiplier))+Math.abs(yy*(255*resMultiplier))));
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
					color = new Color(Math.min(255, (459 - (int) (op))), 0, (int) (op));
				} else {
					color = new Color(255 - (int) (op), 255 - (int) (op), 255);
				} 
				tempj.setBackground(color);
				frame.add(tempj);
			}
		}
		if (decimal!=0) {
			if (ssaa){
				titlePercent = new JTextField("Graphing Calculator (100%) (Resolution: " + resMultiplier + "X) (SSAA " + (int) (0.5/(decimal)) + "X) (" + input + ")"); 
			} else {
				titlePercent = new JTextField("Graphing Calculator (100%) (Resolution: " + resMultiplier + "X) (MSAA " + (int) (0.5/(decimal)) + "X) (" + input + ")"); 
			}
		} else {
			titlePercent = new JTextField("Graphing Calculator (100%) (Internal Resolution Multiplier: " + resMultiplier + "X) (" + input + ")"); 
		}
		frame.setTitle(titlePercent.getText());
		frame.setVisible(true);
	}
	
	private static int nextPowerOf2(final double a)
    {
		
        int b = 1;
        while (b < (int) a)
        {
            b = b << 1;
        }
        return b;
    }
}
