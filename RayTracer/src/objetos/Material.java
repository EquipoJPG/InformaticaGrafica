package objetos;

import java.awt.Color;

public class Material {

	private double kd, ks;
	
	private double k_reflexion; // kd, coeficiente de especular
	private double k_refraccion; // ks, coeficiente de difusion
	private boolean transparente;	// true si es transparente
	private Color color; // color del material
	
	private boolean refleja; // true si refleja
	private int reflejo;	// cuan difuso es el reflejo

	/**
	 * Material y sus propiedades
	 * 
	 * @param k_reflexion
	 * @param k_refraccion
	 * @param color
	 */
	public Material(double k_reflexion, double k_refraccion, double kd, double ks, Color color,
			boolean transp, boolean ref, int refl) {
		this.kd = kd;
		this.ks = ks;
		this.setK_reflexion(k_reflexion);
		this.setK_refraccion(k_refraccion);
		this.color = color;
		this.transparente = transp;
		
		this.refleja = ref;
		this.reflejo = refl;
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

	public int getReflejo() {
		return reflejo;
	}

	public void setReflejo(int reflejo) {
		this.reflejo = reflejo;
	}

	public double getKd() {
		return kd;
	}

	public void setKd(double kd) {
		this.kd = kd;
	}

	public double getKs() {
		return ks;
	}

	public void setKs(double ks) {
		this.ks = ks;
	}
}
