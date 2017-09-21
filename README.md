# Java-Jframe-Graphing-Calculator
This is a graphing calculator made in java with swing.

I have made every part of this calculator. The Calculator.class is something that I have made a while ago and with a few tweeks I have made it into a graphing calculator.

# There are different settings for anti-aliasing in my calculator
```
MSAA (Default):
  1. Slightly Faster.
  2. Looks Much Better For Thin Lines. 
  3. Does Not Look As Good For Bigger Shapes. 
    
SSAA (Beta):
  1. Slightly Slower.
  2. Looks Very Faint For Thin Lines. (Less Than or Greater Than Is Strongly Recomended)
  3. Smooths Out Edges Much Better For Bigger Shapes. 
```

### When it comes to solid shapes like a circle, SSAA is the obvious choice

```
Equation: x^2 + y^2 < 2025

Maximum X: 50

Maximum Y: 50

Minimum X: -50

Minimum Y: -50
```

#### MSAA Circle Example, 2X Sample:

![MSAA 2x Sample On Circle](https://github.com/Spl1ce/Java-Jframe-Graphing-Calculator/blob/master/Picture%20Examples/MSAA%202X%20Multplyer%20(Circle%20Example).png)

#### SSAA Circle Example, 1X Sample:
![SSAA 1x Sample On Circle](https://github.com/Spl1ce/Java-Jframe-Graphing-Calculator/blob/master/Picture%20Examples/SSAA%201X%20Multplyer%20(Circle%20Example).png)

### As you can see, SSAA blows MSAA out of the water even at half of the sample size.

### But SSAA is not obvious choice for this calculator. When it comes to thin lines, there is no beating MSAA.

```
Equation: 2x = y

Maximum X: 10

Maximum Y: 10

Minimum X: -10

Minimum Y: -10
```

#### MSAA Thin Line Example, 1X Sample:
![MSAA 1x Sample On Thin Line](https://github.com/Spl1ce/Java-Jframe-Graphing-Calculator/blob/master/Picture%20Examples/MSAA%201X%20Multplyer%20(Thin%20Line%20Example).png)

#### SSAA Thin Line Example, 1X Sample:
![SSAA 1x Sample On Thin Line](https://github.com/Spl1ce/Java-Jframe-Graphing-Calculator/blob/master/Picture%20Examples/SSAA%201X%20Multplyer%20(Thin%20Line%20Example).png)

### A good rule of thumb is: if you are using "<" or ">" you should use SSAA, but if you are using "=" you should use MSAA.

## Here is a good example of SSAA.
![SSAA Example](https://github.com/Spl1ce/Java-Jframe-Graphing-Calculator/blob/master/Picture%20Examples/SSAA%20Example.png)
