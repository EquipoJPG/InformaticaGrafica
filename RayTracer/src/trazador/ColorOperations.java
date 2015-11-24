package trazador;

import java.awt.Color;

public class ColorOperations {
	
	public static Color add (Color c1, Color c2){
		int red = clamp((c1.getRed() + c2.getRed()), 0, 255);
		int green = clamp((c1.getGreen() + c2.getGreen()), 0, 255);
		int blue = clamp((c1.getBlue() + c2.getBlue()), 0, 255);
		
		return new Color(red, green, blue);
	}
	
	public static Color escalar (Color c, double k){
		int red = clamp((int)(c.getRed()*k), 0, 255);
		int green = clamp((int)(c.getGreen()*k), 0, 255);
		int blue = clamp((int)(c.getBlue()*k), 0, 255);
		
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
	
	public static int clamp(int color, int min, int max){
		// set minimo
		color = Math.max(color, min);	// maximo entre el color y 0
		
		// set maximo
		color = Math.min(color, max);	// minimo entre el color y 255
		
		return color;
	}
}
