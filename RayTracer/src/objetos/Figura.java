package objetos;

import java.util.ArrayList;
import java.util.Iterator;

import data.Rayo;
import data.Vector4;

public class Figura extends Objeto {

	/* Atributos privados */
	private ArrayList<Objeto> lista;
	private Vector4 lowerBound; // Tiene las minimas variables
	private Vector4 upperBound; // Tiene las maximas variables

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
	public Double interseccion(Rayo ray) {
		if (lista.size() == 0) {
			return null;
		} else {
			Iterator<Objeto> it = lista.iterator();
			Objeto o = it.next();
			Double interseccion = o.interseccion(ray);
			while (it.hasNext()) {
				o = it.next();
				Double iterInterseccion = o.interseccion(ray);
				if (iterInterseccion != null) {
					if (interseccion == null) {
						interseccion = o.interseccion(ray);
					} else {
						if (iterInterseccion < interseccion) {
							interseccion = iterInterseccion;
						}
					}
				} else {
					interseccion = iterInterseccion;
				}
			}
			return interseccion;
		}
	}

	public Vector4 normal(Vector4 in, Rayo ray) {
		if (lista.size() == 0 || ray == null) {
			return null;
		} else {
			Iterator<Objeto> it = lista.iterator();
			Objeto o = it.next();
			Objeto ourObject = o;
			Double interseccion = o.interseccion(ray);
			while (it.hasNext()) {
				o = it.next();
				Double iterInterseccion = o.interseccion(ray);
				if (iterInterseccion != null) {
					if (interseccion == null) {
						interseccion = o.interseccion(ray);
						ourObject = o;
					} else {
						if (iterInterseccion < interseccion) {
							interseccion = iterInterseccion;
							ourObject = o;
						}
					}
				} else {
					interseccion = iterInterseccion;
				}
			}
			return ourObject.normal(in, ray);
		}
	}

	@Override
	public Double interseccionSombra(Rayo ray) {
		if (lista.size() == 0) {
			return null;
		} else {
			Iterator<Objeto> it = lista.iterator();
			Objeto o = it.next();
			Double interseccion = o.interseccionSombra(ray);
			while (it.hasNext()) {
				o = it.next();
				Double iterInterseccion = o.interseccionSombra(ray);
				if (iterInterseccion != null) {
					if (interseccion == null) {
						interseccion = o.interseccionSombra(ray);
					} else {
						if (iterInterseccion < interseccion) {
							interseccion = iterInterseccion;
						}
					}
				} else {
					interseccion = iterInterseccion;
				}
			}
			return interseccion;
		}
	}

	private void updateBounds() {
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
}
