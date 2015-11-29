package objetos;

import java.util.ArrayList;

import Jama.Matrix;
import data.Par;
import data.Rayo;
import data.Vector4;
import utils.TransformacionesAfines;

public class Plano extends Objeto {

	/* Atributos de la clase plano */
	private Vector4 normal;
	private Vector4 p1; // top left corner
	private Vector4 p2; // top right corner
	private Vector4 p3; // bottom left corner
	private Vector4 p4; // bottom right corner
	private Vector4 lowerBound;
	private Vector4 upperBound;

	public Plano(Vector4 p1, Vector4 p2, Vector4 p3, Vector4 p4, Material m, Matrix T) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
		this.normal = normal(null);
		super.T = T;

		// Update lower bound
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double minZ = Double.POSITIVE_INFINITY;
		ArrayList<Vector4> temp = new ArrayList<Vector4>();
		temp.add(TransformacionesAfines.multiplyVectorByMatrix(p1, T));
		temp.add(TransformacionesAfines.multiplyVectorByMatrix(p2, T));
		temp.add(TransformacionesAfines.multiplyVectorByMatrix(p3, T));
		temp.add(TransformacionesAfines.multiplyVectorByMatrix(p4, T));
		for (Vector4 e : temp) {
			if (e.getX() < minX) {
				minX = e.getX();
			}
			if (e.getY() < minY) {
				minY = e.getY();
			}
			if (e.getZ() < minZ) {
				minZ = e.getZ();
			}
		}
		lowerBound = new Vector4(minX, minY, minZ, 1);

		// Update upperBound
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double maxZ = Double.NEGATIVE_INFINITY;
		for (Vector4 e : temp) {
			if (e.getX() > maxX) {
				maxX = e.getX();
			}
			if (e.getY() > maxY) {
				maxY = e.getY();
			}
			if (e.getZ() > maxZ) {
				maxZ = e.getZ();
			}
		}
		upperBound = new Vector4(maxX, maxY, maxZ, 1);
		super.material = m;
	}

	public Plano(Vector4 p1, Vector4 p2, Vector4 p3, Vector4 p4, Material m) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
		this.normal = normal(null);

		// Update lower bound
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double minZ = Double.POSITIVE_INFINITY;
		ArrayList<Vector4> temp = new ArrayList<Vector4>();
		temp.add(p1);
		temp.add(p2);
		temp.add(p3);
		temp.add(p4);
		for (Vector4 e : temp) {
			if (e.getX() < minX) {
				minX = e.getX();
			}
			if (e.getY() < minY) {
				minY = e.getY();
			}
			if (e.getZ() < minZ) {
				minZ = e.getZ();
			}
		}
		lowerBound = new Vector4(minX, minY, minZ, 1);

		// Update upperBound
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double maxZ = Double.NEGATIVE_INFINITY;
		for (Vector4 e : temp) {
			if (e.getX() > maxX) {
				maxX = e.getX();
			}
			if (e.getY() > maxY) {
				maxY = e.getY();
			}
			if (e.getZ() > maxZ) {
				maxZ = e.getZ();
			}
		}
		upperBound = new Vector4(maxX, maxY, maxZ, 1);
		super.material = m;
	}

	@Override
	public Par interseccion(Rayo ray) {
		// TODO Modificar rayo con la inversa de la transformacion
		Vector4 d = ray.getDireccion();
		Vector4 n = normal;
		Vector4 a = ray.getOrigen();

		double numerador = Vector4.dot(Vector4.sub(p1, a), n); // (p1 - a) * n
		double denominador = Vector4.dot(d, n); // (d * n)
		Double lambda = null;

		if (denominador == 0) {
			// no hay interseccion
			lambda = null;
		} else {
			// hay interseccion

			/*
			 * Comprueba si la interseccion esta dentro de los limites del plano
			 * (cuadrado)
			 */
			Vector4 pI = Rayo.getInterseccion(ray, numerador / denominador);

			double s1 = Vector4.dot(Vector4.cross(Vector4.sub(p3, p1), Vector4.sub(pI, p1)), n);
			double s2 = Vector4.dot(Vector4.cross(Vector4.sub(p1, p2), Vector4.sub(pI, p2)), n);
			double s3 = Vector4.dot(Vector4.cross(Vector4.sub(p4, p3), Vector4.sub(pI, p3)), n);
			double s4 = Vector4.dot(Vector4.cross(Vector4.sub(p2, p4), Vector4.sub(pI, p4)), n);

			if (Math.signum(s1) == Math.signum(s2) && Math.signum(s1) == Math.signum(s3)
					&& Math.signum(s1) == Math.signum(s4)) {

				/*
				 * Calcula lambda solo si la interseccion esta dentro del plano
				 */
				lambda = numerador / denominador;
			}
		}

		return new Par(lambda,this);
	}

	@Override
	public Par interseccionSombra(Rayo ray) {
		// TODO Modificar rayo con la inversa de la transformacion
		Vector4 d = ray.getDireccion();
		Vector4 n = normal;
		Vector4 a = ray.getOrigen();

		double numerador = Vector4.dot(Vector4.sub(p1, a), n); // (p1 - a) * n
		double denominador = Vector4.dot(d, n); // (d * n)
		Double lambda = null;

		if (denominador == 0) {
			// no hay interseccion
			lambda = null;
		} else {
			// hay interseccion

			/*
			 * Comprueba si la interseccion esta dentro de los limites del plano
			 * (cuadrado)
			 */
			Vector4 pI = Rayo.getInterseccion(ray, numerador / denominador);

			double s1 = Vector4.dot(Vector4.cross(Vector4.sub(p3, p1), Vector4.sub(pI, p1)), n);
			double s2 = Vector4.dot(Vector4.cross(Vector4.sub(p1, p2), Vector4.sub(pI, p2)), n);
			double s3 = Vector4.dot(Vector4.cross(Vector4.sub(p4, p3), Vector4.sub(pI, p3)), n);
			double s4 = Vector4.dot(Vector4.cross(Vector4.sub(p2, p4), Vector4.sub(pI, p4)), n);

			if (Math.signum(s1) == Math.signum(s2) && Math.signum(s1) == Math.signum(s3)
					&& Math.signum(s1) == Math.signum(s4)) {

				/*
				 * Calcula lambda solo si la interseccion esta dentro del plano
				 */
				lambda = numerador / denominador;

				if (lambda < 0) {
					return null;
				}
			}
		}

		return new Par(lambda,this);
	}

	public Vector4 normal(Vector4 interseccion) {
		Vector4 term1 = Vector4.sub(p2, p1);
		Vector4 term2 = Vector4.sub(p3, p1);
		return Vector4.cross(term1, term2).normalise();
	}

	@Override
	public Vector4 normal(Vector4 interseccion, Rayo ray) {
		// TODO Modificar rayo con la inversa de la transformacion
		double res = Vector4.dot(ray.getDireccion(), normal);
		if (res < 0) {
			return normal;
		} else {
			return Vector4.negate(normal);
		}
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
	public boolean estaDentro(Rayo r, Vector4 interseccion){
		return false;
	}

	@Override
	public void updateBounds() {
		// Update lower bound
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double minZ = Double.POSITIVE_INFINITY;
		ArrayList<Vector4> temp = new ArrayList<Vector4>();
		temp.add(TransformacionesAfines.multiplyVectorByMatrix(p1, T));
		temp.add(TransformacionesAfines.multiplyVectorByMatrix(p2, T));
		temp.add(TransformacionesAfines.multiplyVectorByMatrix(p3, T));
		temp.add(TransformacionesAfines.multiplyVectorByMatrix(p4, T));
		for (Vector4 e : temp) {
			if (e.getX() < minX) {
				minX = e.getX();
			}
			if (e.getY() < minY) {
				minY = e.getY();
			}
			if (e.getZ() < minZ) {
				minZ = e.getZ();
			}
		}
		lowerBound = new Vector4(minX, minY, minZ, 1);

		// Update upperBound
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double maxZ = Double.NEGATIVE_INFINITY;
		for (Vector4 e : temp) {
			if (e.getX() > maxX) {
				maxX = e.getX();
			}
			if (e.getY() > maxY) {
				maxY = e.getY();
			}
			if (e.getZ() > maxZ) {
				maxZ = e.getZ();
			}
		}
		upperBound = new Vector4(maxX, maxY, maxZ, 1);
	}

}
