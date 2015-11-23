package objetos;

import data.Rayo;
import data.Vector4;

public class Caja extends Objeto {

	// Atributos privados
	Vector4 lowerBound;
	Vector4 upperBound;
	Figura conjuntoObjetos;

	public Caja(Figura f) {
		lowerBound = f.getLowerBound();
		upperBound = f.getUpperBound();
		conjuntoObjetos = f;
	}

	@Override
	public Double interseccion(Rayo ray) {
		Vector4 p = ray.getOrigen();
		Vector4 m = ray.getDireccion();
		boolean h = false;
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
		if(h){
			return null;
		}
		else{
			return conjuntoObjetos.interseccion(ray);
		}
	}

	@Override
	public Vector4 normal(Vector4 interseccion, Rayo ray) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double interseccionSombra(Rayo ray) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector4 getLowerBound() {
		return lowerBound;
	}

	@Override
	public Vector4 getUpperBound() {
		return upperBound;
	}

}
