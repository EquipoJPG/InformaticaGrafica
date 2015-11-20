package objetos;

import data.Rayo;
import data.Vector4;

public class Plano extends Objeto {
	
	/* Atributos de la clase plano */
	Vector4 normal;
	Vector4 p1;		// top left corner
	Vector4 p2;		// top right corner
	Vector4 p3;		// bottom left corner
	Vector4 p4;		// bottom right corner
	
	public Plano(Vector4 normal, Vector4 p1, Vector4 p2,
			Vector4 p3, Vector4 p4, Material m) {

		this.normal = normal;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
		super.material = m;
	}
	
	@Override
	public Double interseccion(Rayo ray) {
		Vector4 d = ray.getDireccion();
		Vector4 n = normal;
		Vector4 a = ray.getOrigen();
		
		double numerador = Vector4.dot(Vector4.sub(p1, a), n); 	// (p1 - a) * n
		double denominador = Vector4.dot(d, n); 				// (d * n)
		Double lambda = null;
		
		if (denominador == 0) {
			// no hay interseccion
			lambda = null;
		}
		else {
			// hay interseccion
			
			/* Comprueba si la interseccion esta dentro de los limites del plano (cuadrado) */
			Vector4 pI = Rayo.getInterseccion(ray, numerador / denominador);
			
			double s1 = Vector4.dot(Vector4.cross(Vector4.sub(p3, p1), Vector4.sub(pI, p1)), n);
			double s2 = Vector4.dot(Vector4.cross(Vector4.sub(p1, p2), Vector4.sub(pI, p2)), n);
			double s3 = Vector4.dot(Vector4.cross(Vector4.sub(p4, p3), Vector4.sub(pI, p3)), n);
			double s4 = Vector4.dot(Vector4.cross(Vector4.sub(p2, p4), Vector4.sub(pI, p4)), n);
			
			if (Math.signum(s1) == Math.signum(s2) &&
					Math.signum(s1) == Math.signum(s3) &&
					Math.signum(s1) == Math.signum(s4) ) {
				
				/* Calcula lambda solo si la interseccion esta dentro del plano */
				lambda = numerador / denominador;
			}
		}
		
		return lambda;
	}
	
	@Override
	public Vector4 normal(Vector4 interseccion) {
		return normal;
	}

	@Override
	public Vector4 normal(Vector4 interseccion, Rayo ray) {
		return normal(interseccion);
	}
}
