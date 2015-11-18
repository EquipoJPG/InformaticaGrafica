package objetos;

import data.Rayo;

public abstract class Objeto {
	
	/**
	 * @param ray: rayo con el que intersectar
	 * @return <null> si no intersecta o el lambda mas 
	 * cercano con el que intersecta
	 */
	public abstract Double interseccion(Rayo ray);

}
