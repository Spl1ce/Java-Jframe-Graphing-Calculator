import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class mainInterface {
	
	static JTextField equation, width, height, res, aaMult;
	static JComboBox<String> antiAlaising;
	
	static int percent;
	
	static JFrame menu = new JFrame("Graphing Calculator Menu");
	static JDialog frame;
	
	
	public static void main(String[] args){
		menu.getContentPane().setLayout(null);
		
		menu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		menu.setSize(600, 800);
		menu.setResizable(false);
		
		JLabel equationText = new JLabel("Equation:");
		equationText.setFont(new Font("Arial", Font.BOLD, 24));
		equationText.setBounds(50,53,125,35);
		menu.add(equationText);
		
		equation = new JTextField(10);
		equation.setFont(new Font("Arial", Font.PLAIN, 24));
		equation.setBounds(200,50,325,35);
		menu.add(equation);
		
		
		
		JLabel widthText = new JLabel("Width:");
		widthText.setFont(new Font("Arial", Font.BOLD, 24));
		widthText.setBounds(50,153,100,35);
		menu.add(widthText);
		
		width = new JTextField(10);
		width.setFont(new Font("Arial", Font.PLAIN, 24));
		width.setBounds(175,150,325,35);
		menu.add(width);
		
		JLabel heightText = new JLabel("Height:");
		heightText.setFont(new Font("Arial", Font.BOLD, 24));
		heightText.setBounds(50,203,100,35);
		menu.add(heightText);
		
		height = new JTextField(10);
		height.setFont(new Font("Arial", Font.PLAIN, 24));
		height.setBounds(175,200,325,35);
		menu.add(height);
		
		JLabel help = new JLabel("Following Answers Will Be Rounded To A Power Of Two.");
		help.setFont(new Font("Arial", Font.ITALIC, 16));
		help.setBounds(100,300,400,40);
		menu.add(help);
		
		JLabel resText = new JLabel("Resolution Multplyer:");
		resText.setFont(new Font("Arial", Font.BOLD, 20));
		resText.setBounds(50,353,225,35);
		menu.add(resText);
		
		res = new JTextField(10);
		res.setFont(new Font("Arial", Font.PLAIN, 24));
		res.setBounds(275,350,275,35);
		menu.add(res);
		
		JLabel aaMultText = new JLabel("Anti-Alaising Samples:");
		aaMultText.setFont(new Font("Arial", Font.BOLD, 20));
		aaMultText.setBounds(50,428,225,35);
		menu.add(aaMultText);
		
		aaMult = new JTextField(10);
		aaMult.setFont(new Font("Arial", Font.PLAIN, 24));
		aaMult.setBounds(275,425,275,35);
		menu.add(aaMult);
		
		JLabel aaText = new JLabel("Anti-Alaising Setting:");
		aaText.setFont(new Font("Arial", Font.BOLD, 20));
		aaText.setBounds(50,478,250,35);
		menu.add(aaText);
		
		String[] choices = {"None", "MSAA", "SSAA"};
		antiAlaising = new JComboBox<String>(choices);
		antiAlaising.setFont(new Font("Arial", Font.BOLD, 20));
		antiAlaising.setBounds(275,475,100,35);
		menu.add(antiAlaising);
		
		JButton bRun = new JButton("Click To Run!");
		bRun.setBounds(50,580,500,150);
		bRun.setFont(new Font("Arial", Font.PLAIN, 40));
		bRun.addActionListener(new ActionListener() {
			   @Override
			   public void actionPerformed(ActionEvent e) {
				   menu.setVisible(true);
				   runGraph();
			   }
			});
		menu.add(bRun);
		
		JLabel loadingText = new JLabel("Longer Equations/Higher Settings May Need To Load,");
		loadingText.setFont(new Font("Arial", Font.ITALIC, 16));
		loadingText.setBounds(100,510,600,50);
		menu.add(loadingText);
		
		JLabel loadingText2 = new JLabel("Check The Percent At The Top For Info.");
		loadingText2.setFont(new Font("Arial", Font.ITALIC, 16));
		loadingText2.setBounds(150,535,600,50);
		menu.add(loadingText2);
		
		menu.setVisible(true);
	}
	
	public static void printGrid(String input,double maxX, double minX, double maxY, double minY,double decimal,double resMultiplier,boolean ssaa){
		JTextField titlePercent;
		frame = new JDialog(menu, "");
		
		frame.setResizable(true);
		int fx = 0, fy =0;
		if ((maxX-minX)<(maxY-minY)){
			fx = (int) (800*((maxX-minX)/(maxY-minY)));
			fy = 800;
		} else {
			fx = (int) (800*((maxY-minY)/(maxX-minX)));
			fy = 800;
		}
		frame.setFocusableWindowState(false);
		
		frame.setSize(fx,fy);

	    frame.setLayout(new GridLayout((int) (Math.abs(minY*resMultiplier)+Math.abs(maxY*resMultiplier)+1), (int) (Math.abs(minX*resMultiplier)+Math.abs(maxX*resMultiplier)+1)));
	    
	    //System.setProperty("sun.java2d.opengl","True");
	    
		Color color = null;
		boolean skipFail;
		double ssaaHits = 0;
		double ssaaTries = 0;
		double op;
		double temp;
		double stepMultiplier = 1/resMultiplier;
		double sampleStepMultiplier = decimal/resMultiplier;
		for(double y = maxY; y >= minY; y-= stepMultiplier){
			percent = (int) ((y-maxY)/(minY-maxY)*(100));
			if (decimal!=0) {
				if (ssaa){
					titlePercent = new JTextField("Graphing Calculator (" + percent + "%) (Resolution: " + resMultiplier + "X) (SSAA " + (int) (0.5/(decimal)) + "X) (" + input + ")"); 
				} else {
					titlePercent = new JTextField("Graphing Calculator (" + percent + "%) (Resolution: " + resMultiplier + "X) (MSAA " + (int) (0.5/(decimal)) + "X) (" + input + ")"); 
				}
			} else {
				titlePercent = new JTextField("Graphing Calculator (" + percent + "%) (Internal Resolution Multiplier: " + resMultiplier + "X) (" + input + ")"); 
			}
			
			frame.setTitle(titlePercent.getText());
			frame.setVisible(true);
			for(double x = minX; x <= maxX; x+=stepMultiplier){
				JPanel tempj = new JPanel();
				tempj.setDoubleBuffered(true);
				op = 0;
				skipFail = true;
				if (!ssaa){
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

	
	public static void runGraph() {
		
		String sEquation;
		try {
			sEquation = equation.getText();
		} catch (Exception e){
			sEquation = " ";
		}
		
		double maxX;
		double minX;
		double maxY;
		double minY;
		try {
			maxX = Double.parseDouble(width.getText());
			minX = 0-Double.parseDouble(width.getText());
			maxY = Double.parseDouble(height.getText());
			minY = 0-Double.parseDouble(height.getText());
		}catch (Exception e){
			maxX = 5;
			minX = -5;
			maxY = 5;
			minY = -5;
		}
		
		double resMultiplier;
		try{
			resMultiplier = nextPowerOf2(Double.valueOf(res.getText()));
		} catch (Exception e) {
			resMultiplier = 1;
		}
		
		boolean ssaa = false;
		
		try {
			String aaMethod = (String) antiAlaising.getSelectedItem();
			if(aaMethod.toLowerCase().charAt(0) == 's'){
				ssaa = true;
			} else if (aaMethod.toLowerCase().charAt(0) == 'n') {
				printGrid(sEquation,maxX,minX,maxY,minY,0,resMultiplier,false);
				return;
			} else {
			}
		} catch (Exception e){
			printGrid(sEquation,maxX,minX,maxY,minY,0,resMultiplier,false);
			return;
		}
		
		double aaMultiplier;
		try{
			aaMultiplier = nextPowerOf2(Double.valueOf(aaMult.getText()));
		} catch (Exception e) {
			aaMultiplier = 0;
		}
		
		if(aaMultiplier == 0) {
			printGrid(sEquation,maxX,minX,maxY,minY,0,resMultiplier,ssaa);
			return;
		} else {
			aaMultiplier = 0.5/aaMultiplier;
			printGrid(sEquation,maxX,minX,maxY,minY,aaMultiplier,resMultiplier,ssaa);
			return;
		}
		
	}
}
