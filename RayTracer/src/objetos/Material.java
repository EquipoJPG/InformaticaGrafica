package objetos;

import java.awt.Color;

public class Material {

	private Color color; // color del material
	private double kd, ks; // ks = 1 - kd
	private int shiny;	// mas shiny, mas agudo es el reflejo especular [1, 200]

	/**
	 * Material y sus propiedades
	 */
	public Material(Color color, double kd, double ks, int shiny) {
		this.color = color;
		this.kd = kd;
		this.ks = ks;
		this.shiny = shiny;
	}

	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
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

	
	public boolean isTransparente() {
		return ks > 0;
	}

	public boolean isReflectante() {
		return kd > 0;
	}

	public int getShiny() {
		return shiny;
	}

	public void setShiny(int shiny) {
		this.shiny = shiny;
	}
}
