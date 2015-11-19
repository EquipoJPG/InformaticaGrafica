package objetos;

import java.awt.Color;

public class Material {

	private double k_reflexion; // kd, coeficiente de especular
	private double k_refraccion; // ks, coeficiente de difusion
	private boolean transparente;	// true si es transparente
	private Color color; // color del material
	
	private boolean refleja; // true si refleja

	/**
	 * Material y sus propiedades
	 * 
	 * @param k_reflexion
	 * @param k_refraccion
	 * @param color
	 */
	public Material(double k_reflexion, double k_refraccion, Color color,
			boolean transp, boolean ref) {
		this.setK_reflexion(k_reflexion);
		this.setK_refraccion(k_refraccion);
		this.color = color;
		this.transparente = transp;
		
		this.refleja = ref;
	}
	
	/**
	 * @return color
	 */
	public Color getColor(){
		return color;
	}

	public double getK_reflexion() {
		return k_reflexion;
	}

	public void setK_reflexion(double k_reflexion) {
		this.k_reflexion = k_reflexion;
	}

	public double getK_refraccion() {
		return k_refraccion;
	}

	public void setK_refraccion(double k_refraccion) {
		this.k_refraccion = k_refraccion;
	}

	public void setTransparente(boolean transparente) {
		this.transparente = transparente;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isRefleja() {
		return refleja;
	}

	public void setRefleja(boolean refleja) {
		this.refleja = refleja;
	}

	public boolean isTransparente() {
		return transparente;
	}
}
