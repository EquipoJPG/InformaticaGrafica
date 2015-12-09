package objetos;

import java.awt.Color;
import java.util.ArrayList;

import Jama.Matrix;
import data.Par;
import data.Rayo;
import data.Vector4;
import utils.TransformacionesAfines;

public class Caja extends Objeto {

	// Atributos privados
	private Vector4 lowerBound;
	private Vector4 upperBound;
	private Figura conjuntoObjetos;

	public Caja(){
		lowerBound = null;
		upperBound = null;
		conjuntoObjetos = new Figura();
	}
	
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
	
	public Caja(Objeto a){
		conjuntoObjetos = new Figura(a);
		lowerBound = conjuntoObjetos.getLowerBound();
		upperBound = conjuntoObjetos.getUpperBound();
	}
	
	public void addObjeto(Objeto a){
		conjuntoObjetos.addObjeto(a);
		lowerBound = conjuntoObjetos.getLowerBound();
		upperBound = conjuntoObjetos.getUpperBound();
	}
	
	public void debugBounds(){
		if(lowerBound!=null && upperBound!=null){
//			System.out.println(lowerBound + "\t" + upperBound);
			conjuntoObjetos.addObjeto(new Esfera(lowerBound,5,new Material(Color.YELLOW,1,0,0,0,1,0)));
			conjuntoObjetos.addObjeto(new Esfera(upperBound,5,new Material(Color.CYAN,1,0,0,0,1,0)));
			conjuntoObjetos.addObjeto(new Esfera(new Vector4(lowerBound.getX(),upperBound.getY(),upperBound.getZ(),1),2,new Material(Color.GREEN,1,0,0,0,1,0)));
			conjuntoObjetos.addObjeto(new Esfera(new Vector4(lowerBound.getX(),lowerBound.getY(),upperBound.getZ(),1),2,new Material(Color.GREEN,1,0,0,0,1,0)));			
			conjuntoObjetos.addObjeto(new Esfera(new Vector4(lowerBound.getX(),upperBound.getY(),lowerBound.getZ(),1),2,new Material(Color.GREEN,1,0,0,0,1,0)));
			conjuntoObjetos.addObjeto(new Esfera(new Vector4(upperBound.getX(),lowerBound.getY(),lowerBound.getZ(),1),2,new Material(Color.GREEN,1,0,0,0,1,0)));
			conjuntoObjetos.addObjeto(new Esfera(new Vector4(upperBound.getX(),lowerBound.getY(),upperBound.getZ(),1),2,new Material(Color.GREEN,1,0,0,0,1,0)));
			conjuntoObjetos.addObjeto(new Esfera(new Vector4(upperBound.getX(),upperBound.getY(),lowerBound.getZ(),1),2,new Material(Color.GREEN,1,0,0,0,1,0)));
			lowerBound = conjuntoObjetos.getLowerBound();
			upperBound = conjuntoObjetos.getUpperBound();
		}
	}

	@Override
	public Par interseccion(Rayo ray) {
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
			if (p.getZ() > upperBound.getZ() && m.getZ() >= 0) {
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
	public Par interseccionSombra(Rayo ray) {
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
			if (p.getZ() > upperBound.getZ() && m.getZ() >= 0) {
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

	@Override
	public void updateBounds() {
		conjuntoObjetos.updateBounds();
		lowerBound = conjuntoObjetos.getLowerBound();
		upperBound = conjuntoObjetos.getUpperBound();
	}
	
	@Override
	public void setT(Matrix T){
		// Don't do anything
		this.T = TransformacionesAfines.getIdentity();
	}

	@Override
	public void addTransformation(Matrix m) {
		conjuntoObjetos.addTransformation(m);
	}

	@Override
	public boolean estaDentro(Rayo r, Vector4 interseccion) {
		return conjuntoObjetos.estaDentro(r, interseccion);
	}
	
	public ArrayList<Objeto> getBounds(){
		ArrayList<Objeto> b = new ArrayList<Objeto>();
		b.add(new Esfera(lowerBound,5,new Material(Color.YELLOW,1,0,0,0,1,0)));
		b.add(new Esfera(upperBound,5,new Material(Color.CYAN,1,0,0,0,1,0)));
		b.add(new Esfera(new Vector4(lowerBound.getX(),upperBound.getY(),upperBound.getZ(),1),2,new Material(Color.GREEN,1,0,0,0,1,0)));
		b.add(new Esfera(new Vector4(lowerBound.getX(),lowerBound.getY(),upperBound.getZ(),1),2,new Material(Color.GREEN,1,0,0,0,1,0)));			
		b.add(new Esfera(new Vector4(lowerBound.getX(),upperBound.getY(),lowerBound.getZ(),1),2,new Material(Color.GREEN,1,0,0,0,1,0)));
		b.add(new Esfera(new Vector4(upperBound.getX(),lowerBound.getY(),lowerBound.getZ(),1),2,new Material(Color.GREEN,1,0,0,0,1,0)));
		b.add(new Esfera(new Vector4(upperBound.getX(),lowerBound.getY(),upperBound.getZ(),1),2,new Material(Color.GREEN,1,0,0,0,1,0)));
		b.add(new Esfera(new Vector4(upperBound.getX(),upperBound.getY(),lowerBound.getZ(),1),2,new Material(Color.GREEN,1,0,0,0,1,0)));
		return b;
	}
}
