package trazador;

import java.awt.Color;
import data.Vector4;

public class Foco {
	
	private Vector4 posicion;
	private Color color;
	
	/**
	 * Constructor
	 * @param posicion: posicion del foco de luz puntual
	 * @param color: color de la luz puntual
	 */
	public Foco(Vector4 posicion, Color color){
		this.posicion = posicion;
		this.color = color;
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

}
