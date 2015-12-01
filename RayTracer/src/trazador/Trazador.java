package trazador;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import data.Par;
import data.Rayo;
import data.Vector4;
import objetos.Objeto;
import utils.XMLFormatter;

public class Trazador {

	/*
	 * SEGUN RayTracer.cpp
	 * 
	 * si el objeto es transparente o reflectante reflejo = calcular rayo
	 * reflejado <- recursion si es transparente refraccion = calcular rayo
	 * refractado <- recursion surfaceColor = reflejo * fresnel + refraccion *
	 * (1-fresnel) * transparente * surfaceColor sino surfaceColor = componente
	 * difusa
	 */

	/* Variables globales del trazador de rayos */

	// imagen
	private static String IMAGE_FILE_NAME;
	private static int ANTIALIASING;

	private static int MAX_REBOTES_RAYO;
	private static double LUZ_AMBIENTAL;
	private static double EPSILON;

	// contenido de la escena
	private static List<Objeto> objetos;
	private static List<Foco> focos;
	private static BufferedImage img;

	/* FLAGS DEBUG */
	private static boolean TERMINO_AMBIENTAL;
	private static boolean TERMINO_DIFUSO;
	private static boolean TERMINO_ESPECULAR;
	private static boolean TERMINO_REFLEJADO;
	private static boolean TERMINO_REFRACTADO;

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		mainWork();
		long endTime = System.nanoTime();

		long duration = (endTime - startTime) / (long) (1000000.0);
		System.out.println("Execution time: " + duration + " miliseconds");
	}

	public static void mainWork() {
		String xml = "escenaEsqueleto.xml";

		System.out.printf("Preparando escena...");

		Camara camara = XMLFormatter.getCamara(xml);
		img = new BufferedImage(camara.getCols(), camara.getRows(), BufferedImage.TYPE_INT_RGB);
		objetos = XMLFormatter.getObjetos(xml);
		focos = XMLFormatter.getFocos(xml);

		MAX_REBOTES_RAYO = XMLFormatter.getRebotes(xml);
		LUZ_AMBIENTAL = XMLFormatter.getLuzAmbiente(xml);
		ANTIALIASING = XMLFormatter.getAntialiasing(xml);
		IMAGE_FILE_NAME = XMLFormatter.getFile(xml);
		boolean[] flags = XMLFormatter.setFlags(xml);
		if (flags != null) {
			TERMINO_AMBIENTAL = flags[0];
			TERMINO_DIFUSO = flags[1];
			TERMINO_ESPECULAR = flags[2];
			TERMINO_REFLEJADO = flags[3];
			TERMINO_REFRACTADO = flags[4];
		}
		EPSILON = XMLFormatter.getEpsilon(xml);

		System.out.println("OK");
		System.out.printf("Lanzando rayos...");

		/*
		 * Para cada pixel de la pantalla se lanza un rayo y se buscan los
		 * objetos de la escena con los que intersecciona
		 */
		for (int i = 0; i < camara.getRows(); i++) {
			int ii = i - camara.getRows() / 2;

			for (int j = 0; j < camara.getCols(); j++) {
				int jj = j - camara.getCols() / 2;
				Color pixel = null;

				for (int k = 0; k < ANTIALIASING; k++) {
					/* Se crea el rayo que sale del ojo hacia el pixel(j,i) */
					Rayo rayoPrimario = camara.rayoToPixel(jj, ii);

					/* Pinta el pixel(i,j) del color devuelto por el rayo */
					Color colorPixel = trazar(rayoPrimario, 0, new ArrayList<Objeto>());
					if (pixel != null && colorPixel != null) {
						pixel = ColorOperations.addMedios(pixel, colorPixel);
					} else {
						pixel = colorPixel;
					}

				}
				int color = pixel.getRGB();
				img.setRGB(j, camara.getRows() - 1 - i, color);
			}
		}

		System.out.println("OK");
		System.out.printf("Guardando imagen...");

		/* Escribe la imagen resultante en un fichero */
		try {

			File f = new File(IMAGE_FILE_NAME);
			if (f.exists()) {
				String strong = IMAGE_FILE_NAME + "_" + System.currentTimeMillis() + ".png";
				f = new File(strong);
			}
			if (!ImageIO.write(img, "PNG", f)) {
				throw new RuntimeException("ERROR. Unexpected error writing image");
			} else {
				System.out.println("OK");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param rayo:
	 *            rayo lanzado
	 * @param rebotes:
	 *            numero de rebotes actuales
	 * @return color calculado para el punto desde el que se lanza el rayo rayo
	 */
	private static Color trazar(Rayo rayo, int rebotes, ArrayList<Objeto> refractadoItems) {

		Objeto objeto = null;
		double minDistancia = Double.POSITIVE_INFINITY;
		double lambda = -1;
		Vector4 pIntersec = null;
		Vector4 pIntersecFinal = null;

		/* Para cada objeto de la escena, se intenta interseccionar */
		for (int k = 0; k < objetos.size(); k++) {

			Par returned = objetos.get(k).interseccion(rayo);
			if (returned != null) {

				Double landa = returned.getInterseccion();
				if (landa != null) {

					/* Se ha producido una interseccion */
					lambda = (double) landa;

					if (lambda >= 0) {

						/* Se comprueba cual es el objeto visible mas cercano */
						pIntersec = Rayo.getInterseccion(rayo, lambda);
						double distance = Vector4.distancia(rayo.getOrigen(), pIntersec);

						/* Se extrae el objeto mas cercano */
						if (distance < minDistancia) {
							objeto = returned.getObjeto();
							pIntersecFinal = pIntersec;
							minDistancia = distance;
						}
					}
				}
			}
		}

		/*
		 * pIntersecFinl contiene el punto de interseccion objeto contiene el
		 * objeto con el que se ha intersectado
		 */

		Color finalColor = Color.BLACK;
		/*
		 * Si existe al menos un objeto visible, objeto sera distinto de null, y
		 * se lanzan los rayos correspondientes
		 */
		if (objeto != null) {

			if (!objeto.estaDentro(rayo, pIntersecFinal) && TERMINO_AMBIENTAL) {

				/* Termino ambiental */
				finalColor = luzAmbiental(LUZ_AMBIENTAL, objeto); // ka*ia
				finalColor = ColorOperations.escalar(finalColor, objeto.getMaterial().getKd());
			}

			Color colorReflejado = null;
			Color colorRefractado = null;

			for (Foco f : focos) {

				/*
				 * Comprueba si el objeto recibe luz en el punto de interseccion
				 */
				double epsilon = EPSILON;
				Vector4 direccion = Vector4.sub(f.getPosicion(), pIntersecFinal).normalise();
				Vector4 origen = Vector4.add(pIntersecFinal, Vector4.mulEscalar(direccion, epsilon));
				Rayo sombra = new Rayo(origen, direccion);

				double maxDistancia = Vector4.distancia(sombra.getOrigen(), f.getPosicion());
				boolean shadow = false;

				for (Objeto o : objetos) {
					Par returned = o.interseccionSombra(sombra);
					if (returned != null) {
						if (!returned.getObjeto().getMaterial().isTransparente()) {
							Double landa = returned.getInterseccion();
							if (landa != null) {

								Vector4 aux = Rayo.getInterseccion(sombra, landa);
								double dist = Vector4.distancia(sombra.getOrigen(), aux);
								if (dist < maxDistancia) {
									shadow = true;
									break;
								}
							}
						}
					}
				}

				if (!shadow) {
					if (!objeto.estaDentro(rayo, pIntersecFinal) && TERMINO_DIFUSO) {

						/* Reflexion difusa */
						Vector4 normal = objeto.normal(pIntersecFinal, rayo);

						double angulo = Math.max(Vector4.dot(sombra.getDireccion(), normal), 0);
						Color difusa = ColorOperations.difuso(objeto, f, angulo);
						finalColor = ColorOperations.add(finalColor, difusa);
					}
					if (!objeto.estaDentro(rayo, pIntersecFinal) && TERMINO_ESPECULAR) {

						/* Reflexion especular */
						Rayo especular = Rayo.rayoReflejado(sombra, objeto, pIntersecFinal, EPSILON);
						Vector4 R = especular.getDireccion().normalise();
						Vector4 V = Vector4.negate(rayo.getDireccion()).normalise();

						double coseno = Vector4.dot(R, V);
						if (coseno < 0)
							coseno = 0;

						double n = objeto.getMaterial().getShiny();
						double terminoEspecular = Math.abs(Math.pow(coseno, n));

						Color specular = ColorOperations.escalar(f.getColor(), terminoEspecular);
						specular = ColorOperations.escalar(specular, objeto.getMaterial().getKs());
						finalColor = ColorOperations.add(finalColor, specular);
					}
				}
			}

			/* Intenta lanzar nuevos rayos si todavia quedan rebotes */
			if (rebotes < MAX_REBOTES_RAYO) {

				/*
				 * Si el material del objeto es reflectante se lanza el rayo
				 * reflejado
				 */
				if (!objeto.estaDentro(rayo, pIntersecFinal) && objeto.getMaterial().isReflectante()
						&& TERMINO_REFLEJADO) {
					Rayo vista = new Rayo(pIntersecFinal, Vector4.negate(rayo.getDireccion()));
					Rayo reflejado = Rayo.rayoReflejado(vista, objeto, pIntersecFinal, EPSILON);
					colorReflejado = trazar(reflejado, rebotes + 1, new ArrayList<Objeto>());
					colorReflejado = ColorOperations.escalar(colorReflejado, objeto.getMaterial().getKr());
				}
				/*
				 * Si el material del objeto es refractante se lanza el rayo
				 * refractado
				 */
				if (objeto.getMaterial().isTransparente() && TERMINO_REFRACTADO) {
					Rayo refractado = null;
					double nI = 1.0;
					double nT = 1.0;
					
					/* Calcula los indices de refraccion nI y nT */
					if(refractadoItems.size() == 0){

						/* El medio anterior es el aire: nI = 1 y nT = Ir del objeto intersectado */
						refractadoItems.add(objeto);
						nI = 1.0;
						nT = objeto.getMaterial().getIr();
					}
					else{
						
						/* El rayo esta dentro de al menos 1 objeto */
						nI = refractadoItems.get(refractadoItems.size() - 1).getMaterial().getIr();
						
						boolean encontrado = false;
						int index = 0;
						while(!encontrado && index < refractadoItems.size()){
							encontrado = refractadoItems.get(index).equals(objeto);
							index++;
						}
						
						if(encontrado){

							/* Se ha interseccionado por segunda vez con un objeto, saliendo de el */
							refractadoItems.remove(index-1);
							
							if (refractadoItems.size() == 0) {
								
								/* El rayo ha salido al aire desde el ultimo objeto en el que estaba contenido */
								nT = 1.0;
							}
							else {
								nT = refractadoItems.get(refractadoItems.size() - 1).getMaterial().getIr();
							}
						}
						else{
							
							/* Se ha interseccionado por primera vez con un objeto, entrando en el */
							refractadoItems.add(objeto);
							nT = refractadoItems.get(refractadoItems.size() - 1).getMaterial().getIr();
						}
					}
					
					/* Lanza el rayo refractado */
					refractado = Rayo.rayoRefractado(rayo, objeto, pIntersecFinal, EPSILON, nI, nT);
					colorRefractado = trazar(refractado, rebotes + 1,refractadoItems);
					colorRefractado = ColorOperations.escalar(colorRefractado, objeto.getMaterial().getKt());
				}

				/* agregar reflejado y transmitido */
				if (colorReflejado != null && colorRefractado != null) {
					Color fresnelColor = ColorOperations.add(colorReflejado, colorRefractado);
					finalColor = ColorOperations.add(finalColor, fresnelColor);
				} else if (colorReflejado != null) {
					finalColor = ColorOperations.add(finalColor, colorReflejado);
				} else if (colorRefractado != null) {
					finalColor = ColorOperations.add(finalColor, colorRefractado);
				}

			}
		}
		return finalColor;

	}

	/**
	 * @param luzAmbiental
	 * @param objeto
	 * @return color al aplicar luz ambiental al objeto
	 */
	private static Color luzAmbiental(double luzAmbiental, Objeto objeto) {
		Color c = objeto.getMaterial().getColor();
		return ColorOperations.escalar(c, luzAmbiental);
	}
}
