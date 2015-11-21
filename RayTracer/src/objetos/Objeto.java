package objetos;

import data.Rayo;
import data.Vector4;

public abstract class Objeto {
	
	protected Material material;
	
	public Material getMaterial(){
		return material;
	}
	
	/**
	 * @param ray: rayo con el que intersectar
	 * @return <null> si no intersecta o el lambda mas 
	 * cercano con el que intersecta
	 */
	public abstract Double interseccion(Rayo ray);
	
	/**
	 * @param interseccion: punto de la interseccion con el objeto
	 * @return la normal del objeto
	 */
	public abstract Vector4 normal(Vector4 interseccion, Rayo ray);

}
