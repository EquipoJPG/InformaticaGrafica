package objetos;

import data.Rayo;
import data.Vector4;

public class Triangulo extends Objeto {

	/* Atributos privados */
	// TODO generate getters and setters
	private Vector4 p1;
	private Vector4 p2;
	private Vector4 p3;
	
	/**
	 * @param centro: centro de la esfera (0,0,0) seria lo correcto
	 * @param radio: radio de la esfera
	 */
	public Triangulo(Vector4 p1, Vector4 p2, Vector4 p3, Material m){
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		super.material = m;
	}
	
	@Override
	public Double interseccion(Rayo ray) {
		Vector4 a = ray.getOrigen();
		Vector4 d = ray.getDireccion();
		Vector4 term1 = Vector4.sub(p2, p1);
		Vector4 term2 = Vector4.sub(v, w)
		return null;
	}

	@Override
	public Vector4 normal(Vector4 interseccion) {
		// TODO Auto-generated method stub
		return null;
	}

}
