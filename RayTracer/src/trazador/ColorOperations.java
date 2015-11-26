package trazador;

import java.awt.Color;

import objetos.Objeto;

public class ColorOperations {
	
	/**
	 * color = obj.color * (f.intensidad * f.color) * angulo * kd
	 * @param obj
	 * @param f
	 * @param angulo
	 * @return
	 */
	public static Color difuso(Objeto obj, Foco f, double angulo){
		Color c = obj.getMaterial().getColor();
		double kd = obj.getMaterial().getKd();
		int intensidad = f.getIntensidad();
		
		double r = f.getColor().getRed() / 255;
		double g = f.getColor().getGreen() / 255;
		double b = f.getColor().getBlue() / 255;
		
		c = new Color(
				clamp((int) (c.getRed() * angulo * r * kd * intensidad), 0, 255),
				clamp((int) (c.getGreen() * angulo * g * kd * intensidad), 0, 255),
				clamp((int) (c.getBlue() * angulo * b * kd * intensidad), 0, 255)
				);
		return c;
	}
	
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

	public static Color addMedios(Color c1, Color c2) {
		int red = clamp((c1.getRed() + c2.getRed())/2, 0, 255);
		int green = clamp((c1.getGreen() + c2.getGreen())/2, 0, 255);
		int blue = clamp((c1.getBlue() + c2.getBlue())/2, 0, 255);
		
		return new Color(red, green, blue);
	}
}
