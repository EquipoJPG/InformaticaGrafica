package objetos;

import Jama.Matrix;
import data.Par;
import data.Rayo;
import data.Vector4;
import utils.TransformacionesAfines;

public class Esfera extends Objeto {

	// Atributos privados
	private Vector4 centro;
	private double radio;
	private Vector4 lowerBound;
	private Vector4 upperBound;

	/**
	 * @param centro:
	 *            centro de la esfera (0,0,0) seria lo correcto
	 * @param radio:
	 *            radio de la esfera
	 * 
	 * @deprecated use {@link #Esfera(Material m, Matrix T)} instead.
	 */
	@Deprecated
	public Esfera(Vector4 centro, double radio, Material m, Matrix T) {
		this.centro = centro;
		this.radio = radio;
		lowerBound = new Vector4(centro.getX() - radio, centro.getY() - radio, centro.getZ() - radio, 1);
		upperBound = new Vector4(centro.getX() + radio, centro.getY() + radio, centro.getZ() + radio, 1);
		super.material = m;
		super.T = T;
		updateBounds();
	}

	/**
	 * @param radio:
	 *            radio de la esfera
	 * 
	 * @deprecated use {@link #Esfera(Material m, Matrix T)} instead.
	 */
	@Deprecated
	public Esfera(double radio, Material m, Matrix T) {
		this.centro = new Vector4(0, 0, 0, 1);
		this.radio = radio;
		lowerBound = new Vector4(centro.getX() - radio, centro.getY() - radio, centro.getZ() - radio, 1);
		upperBound = new Vector4(centro.getX() + radio, centro.getY() + radio, centro.getZ() + radio, 1);
		super.material = m;
		super.T = T;
		updateBounds();
	}

	/**
	 * @param centro:
	 *            centro de la esfera (0,0,0) seria lo correcto
	 * @param radio:
	 *            radio de la esfera
	 * 
	 * @deprecated use {@link #Esfera(Material m, Matrix T)} instead.
	 */
	@Deprecated
	public Esfera(Vector4 centro, double radio, Material m) {
		this.centro = centro;
		this.radio = radio;
		lowerBound = new Vector4(centro.getX() - radio, centro.getY() - radio, centro.getZ() - radio, 1);
		upperBound = new Vector4(centro.getX() + radio, centro.getY() + radio, centro.getZ() + radio, 1);
		super.material = m;
		updateBounds();
	}

	/**
	 * @param radio:
	 *            radio de la esfera
	 * 
	 * @deprecated use {@link #Esfera(Material m, Matrix T)} instead.
	 */
	@Deprecated
	public Esfera(double radio, Material m) {
		this.centro = new Vector4(0, 0, 0, 1);
		this.radio = radio;
		lowerBound = new Vector4(centro.getX() - radio, centro.getY() - radio, centro.getZ() - radio, 1);
		upperBound = new Vector4(centro.getX() + radio, centro.getY() + radio, centro.getZ() + radio, 1);
		super.material = m;
		updateBounds();
	}

	/**
	 * @param material:
	 *            material de la esfera
	 */
	public Esfera(Material m) {
		this.centro = new Vector4(0, 0, 0, 1);
		this.radio = 1;
		super.material = m;
		updateBounds();
	}

	/**
	 * @param radio:
	 *            radio de la esfera
	 * 
	 * @param radio:
	 *            radio de la esfera
	 */
	public Esfera(Material m, Matrix T) {
		this.centro = new Vector4(0, 0, 0, 1);
		TransformacionesAfines.multiplyVectorByMatrix(this.centro, T);
		this.radio = T.get(0, 0);
		super.material = m;
		super.T = T;
		updateBounds();
	}

	/**
	 * Interseccion con el rayo @param ray.
	 * 
	 * @return <null> si no intersecta y el lambda de la primera interseccion (o
	 *         unica) si intersecta
	 */
	@Override
	public Par interseccion(Rayo ray) {
		return interseccionSombra(ray);
		/*
		 * double A = Vector4.dot(ray.getDireccion(), ray.getDireccion()); // A
		 * = d // . d double B = 2 * Vector4.dot(Vector4.sub(ray.getOrigen(),
		 * this.centro), ray.getDireccion()); // B // = // (a-c).d double C =
		 * Vector4.dot(Vector4.sub(ray.getOrigen(), this.centro),
		 * Vector4.sub(ray.getOrigen(), this.centro)) - Math.pow(this.radio, 2);
		 * // C = (a-c).(a-c) - r^2
		 * 
		 * double D = Math.pow(B, 2) - 4 * A * C; // D = B^2 - AC
		 * 
		 * if (D < 0) { return null; } else { double lambda1, lambda2; // l =
		 * [-B +- raiz(B^2-4AC)] / [2A] lambda1 = (-B + Math.pow(Math.pow(B, 2)
		 * - 4 * A * C, 0.5)) / (2 * A); lambda2 = (-B - Math.pow(Math.pow(B, 2)
		 * - 4 * A * C, 0.5)) / (2 * A);
		 * 
		 * double min = Math.min(lambda1, lambda2);
		 * 
		 * return new Par(min,this);
		 * 
		 * }
		 */
	}

	/**
	 * Interseccion con el rayo @param ray.
	 * 
	 * @return <null> si no intersecta y el lambda de la primera interseccion (o
	 *         unica) si intersecta
	 */
	@Override
	public Par interseccionSombra(Rayo ray) {

		double A = Vector4.dot(ray.getDireccion(), ray.getDireccion()); // A = d
																		// . d
		double B = 2 * Vector4.dot(Vector4.sub(ray.getOrigen(), this.centro), ray.getDireccion()); // B
																									// =
																									// (a-c).d
		double C = Vector4.dot(Vector4.sub(ray.getOrigen(), this.centro), Vector4.sub(ray.getOrigen(), this.centro))
				- Math.pow(this.radio, 2); // C = (a-c).(a-c) - r^2

		double D = Math.pow(B, 2) - 4 * A * C; // D = B^2 - AC

		if (D < 0) {
			return null;
		} else {
			double lambda1, lambda2;
			// l = [-B +- raiz(B^2-4AC)] / [2A]
			lambda1 = (-B + Math.pow(Math.pow(B, 2) - 4 * A * C, 0.5)) / (2 * A);
			lambda2 = (-B - Math.pow(Math.pow(B, 2) - 4 * A * C, 0.5)) / (2 * A);

			double min = Math.min(lambda1, lambda2);
			double max = Math.max(lambda1, lambda2);

			if (min < 0 && max >= 0)
				return new Par(max, this);
			else if (min < 0 && max < 0)
				return null;
			else
				return new Par(min, this);
		}
	}

	/**
	 * @return la normal de la esfera respecto a @param interseccion
	 */
	public Vector4 normal(Vector4 interseccion) {
		return Vector4.sub(interseccion, centro).normalise();
	}

	@Override
	public Vector4 normal(Vector4 interseccion, Rayo ray) {
		return normal(interseccion);
	}

	@Override
	public Vector4 getLowerBound() {
		return lowerBound;
	}

	@Override
	public Vector4 getUpperBound() {
		return upperBound;
	}

	@Override
	public void updateBounds() {
		lowerBound = new Vector4(centro.getX() - radio, centro.getY() - radio, centro.getZ() - radio, 1);
		upperBound = new Vector4(centro.getX() + radio, centro.getY() + radio, centro.getZ() + radio, 1);
	}

	@Override
	public boolean estaDentro(Rayo r, Vector4 interseccion) {
		Vector4 normal = this.normal(interseccion);
		return Vector4.dot(normal, r.getDireccion()) >= 0;
	}
}
