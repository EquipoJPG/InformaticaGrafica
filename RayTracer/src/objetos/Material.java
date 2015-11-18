package objetos;

import java.awt.Color;

public class Material {

	private double k_reflexion; // kd, coeficiente de difusion
	private double k_refraccion; // ks, coeficiente de refraccion
	private Color color; // color del material

	/**
	 * Material y sus propiedades
	 * 
	 * @param k_reflexion
	 * @param k_refraccion
	 * @param color
	 */
	public Material(double k_reflexion, double k_refraccion, Color color) {
		this.k_reflexion = k_reflexion;
		this.k_refraccion = k_refraccion;
		this.color = color;
	}
	
	/**
	 * @return color
	 */
	public Color getColor(){
		return color;
	}

}
