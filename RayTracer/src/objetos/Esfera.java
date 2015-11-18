package objetos;

import data.Rayo;
import data.Vector4;

public class Esfera extends Objeto {
	
	private Vector4 centro;
	private double radio;
	
	/**
	 * @param centro: centro de la esfera (0,0,0) seria lo correcto
	 * @param radio: radio de la esfera
	 */
	public Esfera(Vector4 centro, double radio){
		this.centro = centro;
		this.radio = radio;
	}
	
	/**
	 * @param radio: radio de la esfera
	 */
	public Esfera(double radio){
		this.centro = new Vector4();
		this.radio = radio;
	}

	/**
	 * Interseccion con el rayo @param ray.
	 * @return <null> si no intersecta y el lambda de la primera 
	 * interseccion (o unica) si intersecta
	 */
	@Override
	public Double interseccion(Rayo ray) {
		double A = Vector4.dot(ray.getDireccion(), 
				ray.getDireccion());	// A = d . d
		double B = Vector4.dot(Vector4.sub(ray.getOrigen(), this.centro), 
				ray.getDireccion());	// B = (a-c).d
		double C = Vector4.dot(
					Vector4.sub(ray.getOrigen(), this.centro), 
					Vector4.sub(ray.getOrigen(), this.centro))
				- Math.pow(this.radio, 2);	// C = (a-c).(a-c) - r^2
		
		double D = Math.pow(B, 2) - A*C;	// D = B^2 - AC
		
		if(D < 0){
			return null;
		}
		else{
			double lambda1, lambda2;
			// l = [-2B +- raiz(4B^2-4AC)] / [2A]
			lambda1 = (-2*B + Math.pow(4*Math.pow(B, 2) - 4*A*C, 0.5)) / (2*A);
			lambda2 = (-2*B - Math.pow(4*Math.pow(B, 2) - 4*A*C, 0.5)) / (2*A);
			
			double min = Math.min(lambda1, lambda2);
			double max = Math.max(lambda1, lambda2);
			
			if(min < 0){
				if(max < 0){	// min < 0 && max < 0
					return null;
				}
				else{	// min < 0 && max > 0
					return max;
				}
			}
			else{	// min > 0
				return min;
			}
		}
	}
}
