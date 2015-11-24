package objetos;

import java.util.ArrayList;
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

	@Override
	public Double interseccion(Rayo ray) {
		if (lista.size() == 0) {
			return null;
		} else {
			Double returned = null;
			for (Objeto o : lista) {
				Double iterInterseccion = o.interseccion(ray);
				// Comprobar si hay interseccion con ese objeto
				if (iterInterseccion != null) {
					// Comprobar si la landa es mayor o igual que 0
					if (iterInterseccion >= 0) {
						// Si no tenemos valor para devolver guardado,
						// almacenamos el nuevo
						if (returned == null) {
							returned = iterInterseccion;
						} else {
							// Si tenemos valor guardado y el nuevo es menor
							// guardar el nuevo
							if (iterInterseccion < returned) {
								returned = iterInterseccion;
							}
						}
					}
				}
			}
			return returned;
		}
	}

	public Vector4 normal(Vector4 in, Rayo ray) {
		if (lista.size() == 0 || ray == null) {
			return null;
		} else {
			Objeto ourObject = null;
			for (Objeto obj : lista) {
				Double iterInterseccion = obj.interseccion(ray);
				if (iterInterseccion != null) {
					if (in.equals(iterInterseccion)) {
						ourObject = obj;
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

	@Override
	public Double interseccionSombra(Rayo ray) {
		if (lista.size() == 0) {
			return null;
		} else {
			Double returned = null;
			for (Objeto o : lista) {
				Double iterInterseccion = o.interseccionSombra(ray);
				// Comprobar si hay interseccion con ese objeto
				if (iterInterseccion != null) {
					// Comprobar si la landa es mayor o igual que 0
					if (iterInterseccion >= 0) {
						// Si no tenemos valor para devolver guardado,
						// almacenamos el nuevo
						if (returned == null) {
							returned = iterInterseccion;
						} else {
							// Si tenemos valor guardado y el nuevo es menor
							// guardar el nuevo
							if (iterInterseccion < returned) {
								returned = iterInterseccion;
							}
						}
					}
				}
			}
			return returned;
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

	public void addObjeto(Objeto o) {
		lista.add(o);
		updateBounds();
	}
	
	public Objeto getObjeto(Rayo ray) {
		if (lista.size() == 0) {
			return null;
		} else {
			Double returned = null;
			Objeto returnedObj = null;
			for (Objeto o : lista) {
				Double iterInterseccion = o.interseccionSombra(ray);
				// Comprobar si hay interseccion con ese objeto
				if (iterInterseccion != null) {
					// Comprobar si la landa es mayor o igual que 0
					if (iterInterseccion >= 0) {
						// Si no tenemos valor para devolver guardado,
						// almacenamos el nuevo
						if (returned == null) {
							returned = iterInterseccion;
							returnedObj = o;
						} else {
							// Si tenemos valor guardado y el nuevo es menor
							// guardar el nuevo
							if (iterInterseccion < returned) {
								returned = iterInterseccion;
								returnedObj = o;
							}
						}
					}
				}
			}
			return returnedObj;
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
}
