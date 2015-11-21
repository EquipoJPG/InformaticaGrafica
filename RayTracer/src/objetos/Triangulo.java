package objetos;

import data.Rayo;
import data.Vector4;

public class Triangulo extends Objeto {

	/* Atributos privados */
	private Vector4 p1;
	private Vector4 p2;
	private Vector4 p3;

	/**
	 * @param centro:
	 *            centro de la esfera (0,0,0) seria lo correcto
	 * @param radio:
	 *            radio de la esfera
	 */
	public Triangulo(Vector4 p1, Vector4 p2, Vector4 p3, Material m) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		super.material = m;
	}

	@Override
	public Double interseccion(Rayo ray) {
		Vector4 a = ray.getOrigen();
		Vector4 d = ray.getDireccion();
		Vector4 n = normal(null);
		Double lambda = null;
		
		double denominador = Vector4.dot(d, n);

		/* Comprobacion de si intersecciona */
		if (denominador == 0) {
			/* No intersecciona o no se ve */
			lambda = null;
		} else {
			/* Si que intersecciona */

			double numerador = Vector4.dot(Vector4.sub(p1, a), n);
			
			/* Interseccion */
			Vector4 p = Rayo.getInterseccion(ray, numerador / denominador);

			/*
			 * Comprobacion de que el punto de interseccion cae dentro del
			 * triangulo
			 */
			double s1 = Vector4.dot(Vector4.cross(Vector4.sub(p2, p1), Vector4.sub(p, p1)), n);
			double s2 = Vector4.dot(Vector4.cross(Vector4.sub(p3, p2), Vector4.sub(p, p2)), n);
			double s3 = Vector4.dot(Vector4.cross(Vector4.sub(p1, p3), Vector4.sub(p, p3)), n);

			/* Comprobar que las tres 's' tienen el mismo signo */
			if (Math.signum(s1) == Math.signum(s2) && Math.signum(s1) == Math.signum(s3)) {
				lambda = numerador / denominador;
			}

		}
		return lambda;
	}

	public Vector4 normal(Vector4 interseccion) {
		Vector4 term1 = Vector4.sub(p2, p1);
		Vector4 term2 = Vector4.sub(p3, p1);
		return Vector4.cross(term1, term2).normalise();
	}
	
	@Override
	public Vector4 normal(Vector4 interseccion, Rayo ray) {
		return normal(interseccion);
	}

	public Vector4 getP1() {
		return p1;
	}

	public void setP1(Vector4 p1) {
		this.p1 = p1;
	}

	public Vector4 getP2() {
		return p2;
	}

	public void setP2(Vector4 p2) {
		this.p2 = p2;
	}

	public Vector4 getP3() {
		return p3;
	}

	public void setP3(Vector4 p3) {
		this.p3 = p3;
	}

}
