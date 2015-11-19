package data;

import objetos.Material;

public class Rayo {

	/* Atributos de la clase Rayo */
	private Vector4 origin;
	private Vector4 direccion;
	
	/**
	 * @param origin: punto origen del rayo
	 * @param direction: vector direccion del rayo
	 */
	public Rayo (Vector4 origin, Vector4 direction){
		this.origin = origin;
		this.direccion = direction.normalise();
	}
	
	/**
	 * @return origin
	 */
	public Vector4 getOrigen(){
		return origin;
	}
	
	/**
	 * @return direccion
	 */
	public Vector4 getDireccion(){
		return direccion;
	}
	
	/**
	 * @param origin: punto origen
	 * @param fin: punto fin
	 * @return un nuevo rayo con @param origin como origen y 
	 * @param fin - @param origin como direccion
	 */
	public static Rayo RayoPcpioFin(Vector4 origin, Vector4 fin){
		return new Rayo(origin, Vector4.sub(fin, origin).normalise());
	}
	
	public static Vector4 getInterseccion(Rayo ray, double lambda){
		return Vector4.add(ray.origin, Vector4.mulEscalar(ray.direccion, lambda));
	}
	
	public Rayo rayoRefractado(Material m){
		// SKELETON ONLY
		return null;
	}
	
	public Rayo rayoReflejado(Material m){
		// SKELETON ONLY
		return null;
	}
	
}
