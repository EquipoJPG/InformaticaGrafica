package objetos;

import java.awt.Color;

public class Material {

	private Color color; // color del material
	private double kd, ks; // ks = 1 - kd
	private double kr, kt;	// kreflejado, ktransmitido
	private int shiny;	// mas shiny, mas agudo es el reflejo especular [1, 200]

	/**
	 * Material y sus propiedades
	 */
	public Material(Color color, double kd, double ks, double kr, double kt, int shiny) {
		this.color = color;
		this.kd = kd;
		this.ks = ks;
		this.kr = kr;
		this.kt = kt;
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

	public double getKr() {
		return kr;
	}

	public void setKr(double kr) {
		this.kr = kr;
	}

	public double getKt() {
		return kt;
	}

	public void setKt(double kt) {
		this.kt = kt;
	}

	public boolean isTransparente() {
		return kt > 0;
	}

	public boolean isReflectante() {
		return kr > 0;
	}

	public int getShiny() {
		return shiny;
	}

	public void setShiny(int shiny) {
		this.shiny = shiny;
	}
}
