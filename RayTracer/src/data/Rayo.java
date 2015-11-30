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
	 * @param vista
	 *            (del POV al punto de interseccion)
	 * @param o
	 * @param i
	 * @return
	 */
	public static Rayo rayoRefractado(Rayo vista, Objeto o, Vector4 i, double eps, double nI, double nT) {
		Vector4 normal = o.normal(i, vista);
		if(o.estaDentro(vista, i)){
			normal = Vector4.negate(normal);
		}
		Vector4 vistaNegada = Vector4.negate(vista.getDireccion().normalise());
		double nR = nI / nT;
		double nR2 = Math.pow(nR, 2);
		double dotNI = Vector4.dot(normal, vistaNegada);
		double dotNI2 = Math.pow(dotNI, 2);
		double insideRoot = 1 - (nR2 * (1 - dotNI2));
		double root = Math.sqrt(insideRoot);
		double cosTitaT = root;
		double cosTitaI = dotNI;
//		System.out.println("AngIncid: "+cosTitaI+" AngTrans: "+cosTitaT);
		
		Vector4 primerTerm = Vector4.mulEscalar(normal, (nR * cosTitaI) - cosTitaT);
		Vector4 segundoTerm = Vector4.mulEscalar(vistaNegada, nR);
//		System.out.println("**********************");
//		Vector4 direccionT = (nR*dotNI)
//		System.out.println(normal.toString());
//		System.out.println(primerTerm.toString());
//		System.out.println(nR);
//		System.out.println(cosTitaI);
//		System.out.println(cosTitaT);
//		System.out.println("**********************");
		Vector4 direccionT = Vector4.sub(primerTerm, segundoTerm).normalise();
		Vector4 origenT = Vector4.add(i, Vector4.mulEscalar(direccionT, eps)); 
//		System.out.println(direccionT.toString() + "\n" + vista.getDireccion().normalise().toString() + "\n==============\n");
		return new Rayo(origenT, direccionT);

		/*
		 * // suponemos que dentro de los objetos, indice de refraccion = 1.2
		 * double ior = 1.2;
		 * 
		 * Rayo vistaNegado = new Rayo(i, Vector4.negate(vista.getDireccion()));
		 * Vector4 normal = o.normal(i, vistaNegado); boolean inside =
		 * o.estaDentro(vista, i);// true si estamos dentro del objeto
		 * 
		 * double indexRefraccion = 1 / ior; if(inside) indexRefraccion = ior;
		 * 
		 * double cosIncidente = Vector4.dot(normal,
		 * vistaNegado.getDireccion()); double cosTransmitido1 = 1 -
		 * Math.pow(indexRefraccion, 2) * (1 - Math.pow(cosIncidente, 2));
		 * double cosTransmitido = Math.pow(cosTransmitido1, 0.5);
		 * 
		 * Vector4 T = Vector4.sub(Vector4.mulEscalar(normal, (indexRefraccion *
		 * cosIncidente - cosTransmitido)),
		 * Vector4.mulEscalar(vistaNegado.getDireccion(), indexRefraccion));
		 * 
		 * T = T.normalise();
		 * 
		 * Vector4 origen = Vector4.add(i, Vector4.mulEscalar(T, eps)); return
		 * new Rayo(origen, T);
		 */
	}

	/**
	 * Rayo reflejado(IL, N) = 2N(IL.N) -IL <- negado de lo que pone en las
	 * diapas
	 */
	public static Rayo rayoReflejado(Rayo sombra, Objeto o, Vector4 i, double eps) {
		Vector4 luz = sombra.getDireccion();
		Vector4 normal = o.normal(i, sombra);

		double iln = Vector4.dot(luz, normal);
		Vector4 _2n = Vector4.mulEscalar(normal, 2);
		Vector4 _2ndot = Vector4.mulEscalar(_2n, iln);

		Vector4 reflejado = Vector4.sub(luz, _2ndot);
		reflejado = reflejado.normalise();
		reflejado = Vector4.negate(reflejado);

		/* Construccion del rayo devuelto */
		double epsilon = eps;
		Rayo returned = new Rayo(Vector4.add(i, Vector4.mulEscalar(reflejado, epsilon)), reflejado);
		return returned;
	}

}
