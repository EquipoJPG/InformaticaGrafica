package multithreading;

import java.awt.Color;
import java.util.ArrayList;

import data.Par;
import data.Rayo;
import data.Vector4;
import objetos.Objeto;
import trazador.ColorOperations;
import trazador.Foco;

public class Worker implements Runnable {

	private int id;
	private Escena escena;
	private int numPixels;

	public Worker(int id, Escena escena) {
		this.id = id;
		this.escena = escena;
		this.numPixels = 0;
		escena.inicializarTrabajo(id);
	}

	@Override
	public void run() {
		boolean terminar = false;
		
		while (!terminar) {
			Color pixel = null;
			Rayo rayoPrimario = escena.getRayo(id);
			
			/* Traza los rayos de un pixel */
			for (int k = 0; k < escena.ANTIALIASING; k++) {
				
				/* Se crea el rayo que sale del ojo hacia el pixel(j,i) */
				terminar = rayoPrimario == null;
	
				if (rayoPrimario != null) {
					
					/* Pinta el pixel(i,j) del color devuelto por el rayo */
					Color colorPixel = trazar(rayoPrimario, 0, new ArrayList<Objeto>());
					if (pixel != null && colorPixel != null) {
						pixel = ColorOperations.addMedios(pixel, colorPixel);
					} else {
						pixel = colorPixel;
					}
				}
			}
			if (!terminar) {
				int color = pixel.getRGB();
				escena.pintarPixel(id, color);
				numPixels++;
			}				
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
	private Color trazar(Rayo rayo, int rebotes, ArrayList<Objeto> refractadoItems) {

		Objeto objeto = null;
		double minDistancia = Double.POSITIVE_INFINITY;
		double lambda = -1;
		Vector4 pIntersec = null;
		Vector4 pIntersecFinal = null;

		/* Para cada objeto de la escena, se intenta interseccionar */
		for (int k = 0; k < escena.objetos.size(); k++) {

			Par returned = escena.objetos.get(k).interseccion(rayo);
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

			if (!objeto.estaDentro(rayo, pIntersecFinal) && escena.TERMINO_AMBIENTAL) {

				/* Termino ambiental */
				finalColor = luzAmbiental(escena.LUZ_AMBIENTAL, objeto); // ka*ia
				finalColor = ColorOperations.escalar(finalColor, objeto.getMaterial().getKd());
			}

			Color colorReflejado = null;
			Color colorRefractado = null;

			for (Foco f : escena.focos) {

				/*
				 * Comprueba si el objeto recibe luz en el punto de interseccion
				 */
				double epsilon = escena.EPSILON;
				Vector4 direccion = Vector4.sub(f.getPosicion(), pIntersecFinal).normalise();
				Vector4 origen = Vector4.add(pIntersecFinal, Vector4.mulEscalar(direccion, epsilon));
				Rayo sombra = new Rayo(origen, direccion);

				double maxDistancia = Vector4.distancia(sombra.getOrigen(), f.getPosicion());
				boolean shadow = false;

				for (Objeto o : escena.objetos) {
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
					if (!objeto.estaDentro(rayo, pIntersecFinal) && escena.TERMINO_DIFUSO) {

						/* Reflexion difusa */
						Vector4 normal = objeto.normal(pIntersecFinal, rayo);

						double angulo = Math.max(Vector4.dot(sombra.getDireccion(), normal), 0);
						Color difusa = ColorOperations.difuso(objeto, f, angulo);
						finalColor = ColorOperations.add(finalColor, difusa);
					}
					if (!objeto.estaDentro(rayo, pIntersecFinal) && escena.TERMINO_ESPECULAR) {

						/* Reflexion especular */
						Rayo especular = Rayo.rayoReflejado(sombra, objeto, pIntersecFinal, escena.EPSILON);
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
			if (rebotes < escena.MAX_REBOTES_RAYO) {

				/*
				 * Si el material del objeto es reflectante se lanza el rayo
				 * reflejado
				 */
				if (!objeto.estaDentro(rayo, pIntersecFinal) && objeto.getMaterial().isReflectante()
						&& escena.TERMINO_REFLEJADO) {
					Rayo vista = new Rayo(pIntersecFinal, Vector4.negate(rayo.getDireccion()));
					Rayo reflejado = Rayo.rayoReflejado(vista, objeto, pIntersecFinal, escena.EPSILON);
					colorReflejado = trazar(reflejado, rebotes + 1, new ArrayList<Objeto>());
					colorReflejado = ColorOperations.escalar(colorReflejado, objeto.getMaterial().getKr());
				}
				/*
				 * Si el material del objeto es refractante se lanza el rayo
				 * refractado
				 */
				if (objeto.getMaterial().isTransparente() && escena.TERMINO_REFRACTADO) {
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
					refractado = Rayo.rayoRefractado(rayo, objeto, pIntersecFinal, escena.EPSILON, nI, nT);
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
	private Color luzAmbiental(double luzAmbiental, Objeto objeto) {
		Color c = objeto.getMaterial().getColor();
		return ColorOperations.escalar(c, luzAmbiental);
	}
	
}
