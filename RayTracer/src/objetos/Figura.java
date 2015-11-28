package objetos;

import java.util.ArrayList;

import Jama.Matrix;
import data.Par;
import data.Rayo;
import data.Vector4;
import utils.TransformacionesAfines;

public class Figura extends Objeto {

	/* Atributos privados */
	private ArrayList<Objeto> lista;
	private Vector4 lowerBound; // Tiene las minimas variables
	private Vector4 upperBound; // Tiene las maximas variables
	private Objeto lastObjetoIntersectado = null;
	private Rayo lastRayoUsado = null;

	public Figura() {
		lista = new ArrayList<Objeto>();
		lowerBound = null;
		upperBound = null;
	}

	public Figura(Objeto o) {
		lista = new ArrayList<Objeto>();
		lista.add(o);
		lowerBound = o.getLowerBound();
		upperBound = o.getUpperBound();
	}

	public Figura(ArrayList<Objeto> l) {
		lista = l;
		updateBounds();
	}

	public void addObjeto(Objeto o) {
		lista.add(o);
		updateBounds();
	}

	@Override
	public Par interseccion(Rayo ray) {
		if (lista.size() == 0) {
			return null;
		} else {
			Par returned = null;
			for (Objeto o : lista) {
				Par ret = o.interseccion(ray);
				if (ret != null) {
					Double iterIntersecciong = ret.getInterseccion();
					// Comprobar si hay interseccion con ese objeto
					if (iterIntersecciong != null) {
						double iterInterseccion = (double) iterIntersecciong;
						// Comprobar si la landa es mayor o igual que 0
						if (iterInterseccion >= 0) {
							// Si no tenemos valor para devolver guardado,
							// almacenamos el nuevo
							if (returned == null) {
								returned = ret;
							} else {
								// Si tenemos valor guardado y el nuevo es menor
								// guardar el nuevo
								if (iterInterseccion < returned.getInterseccion()) {
									returned = ret;
								}
							}
						}
					}
				}
			}
			if(returned!=null){
				lastObjetoIntersectado = returned.getObjeto();
				lastRayoUsado = ray;
			}
			return returned;
		}
	}

	@Override
	public Par interseccionSombra(Rayo ray) {
		if (lista.size() == 0) {
			return null;
		} else {
			Par returned = null;
			for (Objeto o : lista) {
				Par ret = o.interseccionSombra(ray);
				if (ret != null) {
					Double iterIntersecciong = ret.getInterseccion();
					// Comprobar si hay interseccion con ese objeto
					if (iterIntersecciong != null) {
						double iterInterseccion = (double) iterIntersecciong;
						// Comprobar si la landa es mayor o igual que 0
						if (iterInterseccion >= 0) {
							// Si no tenemos valor para devolver guardado,
							// almacenamos el nuevo
							if (returned == null) {
								returned = ret;
							} else {
								// Si tenemos valor guardado y el nuevo es menor
								// guardar el nuevo
								if (iterInterseccion < returned.getInterseccion()) {
									returned = ret;
								}
							}
						}
					}
				}
			}
			if(returned!=null){
				lastObjetoIntersectado = returned.getObjeto();
				lastRayoUsado = ray;
			}
			return returned;
		}
	}

	public Vector4 normal(Vector4 in, Rayo ray) {
		/* Comprobar si lo que nos hemos guardado nos sirve */
		if (lastRayoUsado != null && ray.equals(lastRayoUsado)) {
			// Si es el mismo rayo, podemos devolver la normal
			// del ultimo objeto que nos guardamos
			return lastObjetoIntersectado.normal(in, ray);
		} else {
			// Si no coincide pues habra que buscarlo
			if (lista.size() == 0 || ray == null) {
				return null;
			} else {
				Objeto ourObject = null;
				for (Objeto obj : lista) {
					Par p = obj.interseccion(ray);
					if (p != null) {
						Double iterInterseccion = p.getInterseccion();
						if (iterInterseccion != null) {
							if (in.equals(iterInterseccion)) {
								ourObject = p.getObjeto();
							}
						}
					}
				}
				if (ourObject == null) {
					return null;
				} else {
					return ourObject.normal(in, ray);
				}
			}
		}
	}

	@Override
	public Vector4 getLowerBound() {
		if (lowerBound == null) {
			try {
				throw new Exception("Can't get bounds from empty figure");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lowerBound;
	}

	@Override
	public Vector4 getUpperBound() {
		if (upperBound == null) {
			try {
				throw new Exception("Can't get bounds from empty figure");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return upperBound;
	}

	@Override
	public void updateBounds() {
		// Update lower bound
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double minZ = Double.POSITIVE_INFINITY;
		ArrayList<Vector4> temp = new ArrayList<Vector4>();
		for (Objeto o : lista) {
			temp.add(o.getLowerBound());
			temp.add(o.getUpperBound());
		}

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

	@Override
	public void setT(Matrix T) {
		// Don't do anything
		this.T = TransformacionesAfines.getIdentity();
	}

	@Override
	public void addTransformation(Matrix m) {
		for (Objeto a : lista) {
			a.addTransformation(m);
		}
		;
	}

	@Override
	public boolean estaDentro(Rayo r) {
		if (lista.size() == 0 || r == null) {
			return false;
		} else {
			boolean returned = false;
			for (Objeto obj : lista) {
				if(obj instanceof Triangulo || obj instanceof Plano){
					//do nothing
				}
				else{
					Par p = obj.interseccion(r);
					if (p != null) {
						if(p.getObjeto()!=null){
							returned = p.getObjeto().estaDentro(r);
						}
					}
				}
			}
			return returned;
		}
	}
}
