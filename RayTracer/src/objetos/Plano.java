package objetos;

import data.Rayo;
import data.Vector4;

public class Plano extends Objeto {
	
	/* Atributos de la clase plano */
	Vector4 normal;
	double puntoD;		// distancia al origen de coordenadas
	
	public Plano(Vector4 normal, double D) {
		this.normal = normal;
		this.puntoD = D;
	}
	
	@Override
	public Double interseccion(Rayo ray) {
		Vector4 d = ray.getDireccion();
		Vector4 n = normal;
		Vector4 a = ray.getOrigen();
		
		double numerador = Vector4.dot(n, a) + puntoD; 	// [n * a + D]
		double denominador = Vector4.dot(d, n); 		// (d * n)
		Double lambda = null;
		
		if (denominador == 0) {
			// no hay interseccion
			lambda = null;
		}
		else {
			// hay interseccion
			lambda = numerador / denominador;
		}
		
		return lambda;
	}
	
	@Override
	public Vector4 normal(Vector4 interseccion) {
		return normal;
	}
}
