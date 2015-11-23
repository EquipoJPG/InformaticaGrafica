package trazador;

import java.awt.Color;

public class ColorOperations {
	
	public static Color add (Color c1, Color c2){
		int red = (int) (Math.min(255, (c1.getRed() + c2.getRed())/2));
		int green = (int) (Math.min(255, (c1.getGreen() + c2.getGreen())/2));
		int blue = (int) (Math.min(255, (c1.getBlue() + c2.getBlue())/2));
		
		return new Color(red, green, blue);
	}
	
	public static Color superAdd (Color c1, Color c2){
		int red = (int) (Math.min(255, (c1.getRed() + c2.getRed())));
		int green = (int) (Math.min(255, (c1.getGreen() + c2.getGreen())));
		int blue = (int) (Math.min(255, (c1.getBlue() + c2.getBlue())));
		
		return new Color(red, green, blue);
	}
	
	public static Color escalar (Color c, double k){
		int red = (int) (Math.min(255, (c.getRed() * k)));
		int green = (int) (Math.min(255, (c.getGreen() * k)));
		int blue = (int) (Math.min(255, (c.getBlue() * k)));
		
		return new Color(red, green, blue);
	}

	/**
	 * c1 = reflejado
	 * c2 = refractado
	 * @return c1 * kd + c2 * (1-kd)
	 */
	public static Color fresnel(Color c1, Color c2, double kd){
		return add(escalar(c1, kd), escalar(c2, (1-kd)));
	}
}
