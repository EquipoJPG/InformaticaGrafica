package trazador;

import java.awt.Color;
import data.Vector4;

public class Foco {
	
	private Vector4 posicion;
	private Color color;
	private int intensidad;
	
	/**
	 * Constructor
	 * @param posicion: posicion del foco de luz puntual
	 * @param color: color de la luz puntual
	 */
	public Foco(Vector4 posicion, Color color, int intensidad){
		this.posicion = posicion;
		this.color = color;
		this.intensidad = intensidad;
	}
	
	/**
	 * @return color
	 */
	public Color getColor(){
		return color;
	}
	
	/**
	 * @return posicion
	 */
	public Vector4 getPosicion(){
		return posicion;
	}

	/**
	 * @return intensidad
	 */
	public int getIntensidad(){
		return intensidad;
	}
}
