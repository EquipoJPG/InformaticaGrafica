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

	public static Rayo rayoDifuso(Rayo original, Objeto o, Vector4 i) {
		
		/* Calcular variables */
		Vector4 normal = o.normal(i);
		double anguloIncidencia = Vector4.angulo(original.direccion, normal);
		double kRefr = o.getMaterial().getK_refraccion();
		double anguloRefractado = Math.asin(Math.sin(anguloIncidencia) / kRefr);
		
		/* Vector del rayo refractado */
		Vector4 primerTerm = Vector4.mulEscalar(normal,
				kRefr * Math.cos(anguloIncidencia) - Math.cos(anguloRefractado));
		Vector4 segundoTerm = Vector4.mulEscalar(original.getDireccion(), kRefr);
		Vector4 direccion = Vector4.sub(primerTerm, segundoTerm);
		direccion = direccion.normalise();
		
		/* Construccion del rayo devuelto */
		Rayo returned = new Rayo(i, direccion);
		return returned;
	}

	public static Rayo rayoEspecular(Rayo original, Objeto o, Vector4 i) {
		
		/* Calcular variables */
		Vector4 normal = o.normal(i);
		// dot = l.n, l= -direccion 
		double dotProduct = Vector4.dot(Vector4.negate(original.getDireccion()), normal);
		
		/* Calculo reflejado */
		// segundoTerm = (2*dot)n
		Vector4 segundoTerm = Vector4.mulEscalar(normal, 2*dotProduct);
		// reflejado = (2*(l.n))n - l
		Vector4 reflejado = Vector4.sub(segundoTerm, Vector4.negate(original.getDireccion()));
		reflejado.normalise();
		
		/* Construccion del rayo devuelto */
		double epsilon = 1e-12;
		Rayo returned = new Rayo(Vector4.add(i, Vector4.mulEscalar(reflejado, epsilon)),reflejado);
		return returned;
	}

}
