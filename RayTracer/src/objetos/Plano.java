package objetos;

import data.Rayo;
import data.Vector4;

public class Plano extends Objeto {
	
	/* Atributos de la clase plano */
	Vector4 normal;
	Vector4 punto;		// distancia al origen de coordenadas
	double anchura;
	double altura;
	
	public Plano(Vector4 normal, Vector4 punto, double anchura, double altura, Material m) {
		this.normal = normal;
		this.punto = punto;
		this.anchura = anchura;
		this.altura = altura;
		super.material = m;
	}
	
	@Override
	public Double interseccion(Rayo ray) {
		Vector4 d = ray.getDireccion();
		Vector4 n = normal;
		Vector4 p1 = punto;
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
			Rayo.getInterseccion(ray, numerador / denominador);
			
			
			
			lambda = numerador / denominador;
		}
		
		return lambda;
	}
	
	@Override
	public Vector4 normal(Vector4 interseccion) {
		return normal;
	}
}
