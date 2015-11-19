package data;

import objetos.Objeto;

public class Rayo {

	/* Atributos de la clase Rayo */
	private Vector4 origin;
	private Vector4 direccion;

	/**
	 * @param origin:
	 *            punto origen del rayo
	 * @param direction:
	 *            vector direccion del rayo
	 */
	public Rayo(Vector4 origin, Vector4 direction) {
		this.origin = origin;
		this.direccion = direction.normalise();
	}

	/**
	 * @return origin
	 */
	public Vector4 getOrigen() {
		return origin;
	}

	/**
	 * @return direccion
	 */
	public Vector4 getDireccion() {
		return direccion;
	}

	/**
	 * @param origin:
	 *            punto origen
	 * @param fin:
	 *            punto fin
	 * @return un nuevo rayo con @param origin como origen y
	 * @param fin
	 *            - @param origin como direccion
	 */
	public static Rayo RayoPcpioFin(Vector4 origin, Vector4 fin) {
		return new Rayo(origin, Vector4.sub(fin, origin).normalise());
	}

	public static Vector4 getInterseccion(Rayo ray, double lambda) {
		return Vector4.add(ray.origin, Vector4.mulEscalar(ray.direccion, lambda));
	}

	public Rayo rayoRefractado(Rayo original, Objeto o, Vector4 i) {
		// Calcular variables
		Vector4 normal = o.normal(i);
		double anguloIncidencia = Vector4.angulo(this.direccion, normal);
		double kRefr = o.getMaterial().getK_refraccion();
		double anguloRefractado = Math.asin(Math.sin(anguloIncidencia) / kRefr);
		// Vector del rayo refractado
		Vector4 primerTerm = Vector4.mulEscalar(normal,
				kRefr * Math.cos(anguloIncidencia) - Math.cos(anguloRefractado));
		Vector4 segundoTerm = Vector4.mulEscalar(original.getDireccion(), kRefr);
		Vector4 direccion = Vector4.sub(primerTerm, segundoTerm);
		direccion = direccion.normalise();
		// Construccion del rayo devuelto
		Rayo returned = new Rayo(i, direccion);
		return returned;
	}

	public Rayo rayoReflejado(Rayo original, Objeto o, Vector4 i) {
		// Calcular variables
		Vector4 normal = o.normal(i);
		double dotProduct = Vector4.dot(original.getDireccion(), normal);
		// Calculo reflejado
		Vector4 segundoTerm = Vector4.mulEscalar(normal, 2*dotProduct);
		Vector4 reflejado = Vector4.sub(original.getDireccion(), segundoTerm);
		reflejado.normalise();
		// Construccion del rayo devuelto
		Rayo returned = new Rayo(i,reflejado);
		return returned;
	}

}
