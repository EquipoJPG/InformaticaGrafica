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

	/**
	 * 
	 * @param vista (del POV al punto de interseccion)
	 * @param o
	 * @param i
	 * @return
	 */
	public static Rayo rayoRefractado(Rayo vista, Objeto o, Vector4 i) {
		// suponemos que dentro de los objetos, indice de refraccion = 1.2
		double ior = 1.2;
		
		Rayo vistaNegado = new Rayo(i, Vector4.negate(vista.getDireccion()));
		Vector4 normal = o.normal(i, vistaNegado);
		boolean inside = Vector4.dot(normal, vista.getDireccion())> 0;	// true si estamos dentro del objeto
		
		double indexRefraccion = 1;
		if(inside){
			indexRefraccion = indexRefraccion / ior;
		}
		else{
			indexRefraccion = ior / indexRefraccion;
		}
		
		double cosIncidente = Vector4.dot(normal, vistaNegado.getDireccion());
		double cosTransmitido1 = 1 - Math.pow(indexRefraccion, 2) * (1 - Math.pow(cosIncidente, 2));
		double cosTransmitido = Math.pow(cosTransmitido1, 0.5);
		
		Vector4 T = Vector4.sub(Vector4.mulEscalar(normal, (indexRefraccion * cosIncidente - cosTransmitido)), 
				Vector4.mulEscalar(vistaNegado.getDireccion(), indexRefraccion));
		
		T = T.normalise();
		
		double bias = 1e-12;
		
		Vector4 origen = Vector4.add(i, Vector4.mulEscalar(T, bias));
		return new Rayo(origen, T);
		
//		/* Calcular variables */
//		Vector4 normal = o.normal(i,original);
//		double anguloIncidencia = Vector4.angulo(original.direccion, normal);
//		double kRefr = o.getMaterial().getKs();
//		double anguloRefractado = Math.asin(Math.sin(anguloIncidencia) / kRefr);
//		
//		/* Vector del rayo refractado */
//		Vector4 primerTerm = Vector4.mulEscalar(normal,
//				kRefr * Math.cos(anguloIncidencia) - Math.cos(anguloRefractado));
//		Vector4 segundoTerm = Vector4.mulEscalar(original.getDireccion(), kRefr);
//		Vector4 direccion = Vector4.sub(primerTerm, segundoTerm);
//		direccion = direccion.normalise();
//		
//		/* Construccion del rayo devuelto */
//		Rayo returned = new Rayo(i, direccion);
//		return returned;
	}

	/**
	 * Rayo reflejado(IL, N) = IL - 2N(IL.N)
	 */
	public static Rayo rayoReflejado(Rayo sombra, Objeto o, Vector4 i) {
		Vector4 luz = sombra.getDireccion();
		Vector4 normal = o.normal(i,sombra);
		
		double iln = Vector4.dot(luz, normal);
		Vector4 _2n = Vector4.mulEscalar(normal, 2);
		Vector4 _2ndot = Vector4.mulEscalar(_2n, iln);
		
		Vector4 reflejado = Vector4.sub(luz, _2ndot);
		reflejado = reflejado.normalise();
		
		/* Construccion del rayo devuelto */
		double epsilon = 1e-12;
		Rayo returned = new Rayo(Vector4.add(i, Vector4.mulEscalar(reflejado, epsilon)),reflejado);
		return returned;
	}

}
