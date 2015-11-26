package objetos;

import java.util.ArrayList;

import data.Rayo;
import data.Vector4;

public class Caja extends Objeto {

	// Atributos privados
	private Vector4 lowerBound;
	private Vector4 upperBound;
	private Figura conjuntoObjetos;

	public Caja(Figura f) {
		lowerBound = f.getLowerBound();
		upperBound = f.getUpperBound();
		conjuntoObjetos = f;
	}

	public Caja(ArrayList<Objeto> l) {
		conjuntoObjetos = new Figura(l);
		lowerBound = conjuntoObjetos.getLowerBound();
		upperBound = conjuntoObjetos.getUpperBound();
	}

	@Override
	public Double interseccion(Rayo ray) {
		boolean h = false;
		Vector4 p = ray.getOrigen();
		if (estaDentroDeLaCaja(p)) {
			// Do nothing, si que intersecta
		} else {
			Vector4 m = ray.getDireccion();
			// Si cumple cualquiera de las 6 condiciones no intersecta
			// Condicion 1
			if (p.getX() < lowerBound.getX() && m.getX() <= 0) {
				h = true;
			}
			// Condicion 2
			if (p.getY() < lowerBound.getY() && m.getY() <= 0) {
				h = true;
			}
			// Condicion 3
			if (p.getZ() < lowerBound.getZ() && m.getZ() <= 0) {
				h = true;
			}
			// Condicion 4
			if (p.getX() > upperBound.getX() && m.getX() >= 0) {
				h = true;
			}
			// Condicion 5
			if (p.getY() > upperBound.getY() && m.getY() >= 0) {
				h = true;
			}
			// Condicion 6
			if (p.getZ() < upperBound.getZ() && m.getZ() >= 0) {
				h = true;
			}
		}
		if (h) {
			return null;
		} else {
			return conjuntoObjetos.interseccion(ray);
		}
	}

	@Override
	public Vector4 normal(Vector4 interseccion, Rayo ray) {
		return conjuntoObjetos.normal(interseccion, ray);
	}

	@Override
	public Double interseccionSombra(Rayo ray) {
		boolean h = false;
		Vector4 p = ray.getOrigen();
		if (estaDentroDeLaCaja(p)) {
			// Do nothing, si que intersecta
		} else {
			Vector4 m = ray.getDireccion();
			// Si cumple cualquiera de las 6 condiciones no intersecta
			// Condicion 1
			if (p.getX() < lowerBound.getX() && m.getX() <= 0) {
				h = true;
			}
			// Condicion 2
			if (p.getY() < lowerBound.getY() && m.getY() <= 0) {
				h = true;
			}
			// Condicion 3
			if (p.getZ() < lowerBound.getZ() && m.getZ() <= 0) {
				h = true;
			}
			// Condicion 4
			if (p.getX() > upperBound.getX() && m.getX() >= 0) {
				h = true;
			}
			// Condicion 5
			if (p.getY() > upperBound.getY() && m.getY() >= 0) {
				h = true;
			}
			// Condicion 6
			if (p.getZ() < upperBound.getZ() && m.getZ() >= 0) {
				h = true;
			}
		}
		if (h) {
			return null;
		} else {
			return conjuntoObjetos.interseccionSombra(ray);
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

	public Objeto getObjeto(Rayo ray) {
		return conjuntoObjetos.getObjeto(ray);
	}

	/**
	 * Devuelve true si el punto esta dentro de la caja
	 * False en caso contrario
	 */
	private boolean estaDentroDeLaCaja(Vector4 p) {
		boolean returned = false;
		// Check X
		if (lowerBound.getX() <= p.getX() && upperBound.getX() >= p.getX()) {
			// Check Y
			if (lowerBound.getY() <= p.getY() && upperBound.getY() >= p.getY()) {
				// Check Z
				if (lowerBound.getX() <= p.getZ() && upperBound.getZ() >= p.getZ()) {
					returned = true;
				}
			}
		}

		return returned;
	}
}
