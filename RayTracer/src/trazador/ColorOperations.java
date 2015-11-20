package trazador;

import java.awt.Color;

public class ColorOperations {
	
	public static Color add (Color c1, Color c2){
		int red = (int) (Math.min(255, (c1.getRed() + c2.getRed())/2));
		int green = (int) (Math.min(255, (c1.getGreen() + c2.getGreen())/2));
		int blue = (int) (Math.min(255, (c1.getBlue() + c2.getBlue())/2));
		
		return new Color(red, green, blue);
	}
	
	public static Color escalar (Color c, double k){
		int red = (int) (Math.min(255, (c.getRed() * k)));
		int green = (int) (Math.min(255, (c.getGreen() * k)));
		int blue = (int) (Math.min(255, (c.getBlue() * k)));
		
		return new Color(red, green, blue);
	}

}
