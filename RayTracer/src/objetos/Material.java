package objetos;

import java.awt.Color;

public class Material {

	private double kd, ks; // ks = 1 - kd
	
	private double k_reflexion;
	private double k_refraccion;
	private boolean transparente;	// true si es transparente
	private Color color; // color del material
	
	private boolean reflectante; // true si refleja
	private int shiny;	// mas shiny, mas agudo es el reflejo especular [1, 200]

	/**
	 * Material y sus propiedades
	 */
	public Material(double k_reflexion, double k_refraccion, double kd, double ks, Color color,
			boolean transparente, boolean reflectante, int shiny) {
		this.kd = kd;
		this.ks = ks;
		
		this.k_reflexion = k_reflexion;
		this.k_refraccion = k_refraccion;
		
		this.color = color;
		
		this.transparente = transparente;
		this.reflectante = reflectante;
		
		this.shiny = shiny;
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

	public boolean isTransparente() {
		return transparente;
	}

	public void setTransparente(boolean transparente) {
		this.transparente = transparente;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isReflectante() {
		return reflectante;
	}

	public void setReflectante(boolean reflectante) {
		this.reflectante = reflectante;
	}

	public int getShiny() {
		return shiny;
	}

	public void setShiny(int shiny) {
		this.shiny = shiny;
	}
}
