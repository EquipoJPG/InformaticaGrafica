package data;

public class Vector4 {

	/* Atributos de la clase PuntoVectorH */
	private double x;
	private double y;
	private double z;
	private int h;		// h == 1 -> punto, h == 0 -> vector
	
	/**
	 * Constructor por defecto (vector de (0,0,0))
	 */
	public Vector4() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.h = 0;
	}
	
	/**
	 * Construye un vector|punto (depende de @param h) con coordenadas
	 * (@param x, @param y, @param z)
	 */
	public Vector4(double x, double y, double z, int h) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.h = h;
	}
	
	/**
	 * @param v: primer vector|punto a sumar
	 * @param w: segundo vector|punto a sumar
	 * @return un nuevo vector|punto suma de ambos
	 */
	public static Vector4 add(Vector4 v, Vector4 w){
		double x = v.x + w.x;
		double y = v.y + w.y;
		double z = v.z + w.z;
		int h = 0;
		
		if( ( esVector(v) && esPunto(w) ) ||
			( esPunto(v) && esVector(w) )){
			h = 1;
		}
		else if( esPunto(v) && esPunto(w)){
			h = 1;
		}
		return new Vector4(x, y, z, h);
	}
	
	/**
	 * @param v: vector|punto del que restar
	 * @param w: vector|punto que resta
	 * @return @param v - @param w
	 */
	public static Vector4 sub(Vector4 v, Vector4 w){
		double x = v.x - w.x;
		double y = v.y - w.y;
		double z = v.z - w.z;
		int h = 0;
		
		return new Vector4(x, y, z, h);
	}
	
	public static Vector4 mulEscalar(Vector4 v, double escalar){
		return new Vector4(v.x * escalar, v.y * escalar, v.z * escalar, v.h);
	}
	
	/**
	 * @return <true> si @param v es vector
	 */
	public static boolean esVector(Vector4 v){
		return v.h == 0;
	}
	
	/**
	 * @return <true> si @param v es un punto
	 */
	public static boolean esPunto(Vector4 v){
		return !esVector(v);
	}
	
}
