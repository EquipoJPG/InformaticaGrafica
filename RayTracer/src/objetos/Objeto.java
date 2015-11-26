package objetos;

import Jama.Matrix;
import data.Rayo;
import data.Vector4;

public abstract class Objeto {
	
	protected Material material;
	protected Matrix T = utils.TransformacionesAfines.getIdentity();
	
	public Matrix getT(){
		return T;
	}
	
	public Matrix getInvT(){
		return T.inverse();
	}
	
	public void setT(Matrix T){
		this.T = T;
	}
	
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

	public abstract Double interseccionSombra(Rayo ray);
	
	public abstract Vector4 getLowerBound();
	
	public abstract Vector4 getUpperBound();

}
