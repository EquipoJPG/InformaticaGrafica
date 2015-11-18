package objetos;

import java.awt.Color;

import data.Rayo;

public abstract class Objeto {
	
	private Color color;
	
	/**
	 * @return color
	 */
	public Color getColor(){
		return color;
	}
	
	/**
	 * @param ray: rayo con el que intersectar
	 * @return <null> si no intersecta o el lambda mas 
	 * cercano con el que intersecta
	 */
	public abstract Double interseccion(Rayo ray);

}
