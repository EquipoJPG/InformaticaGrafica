package trazador;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import data.Rayo;
import data.Vector4;
import objetos.Caja;
import objetos.Esfera;
import objetos.Figura;
import objetos.Objeto;

public class Trazador {

	/* Variables globales del trazador de rayos */

	// imagen
	final private static int IMAGE_COLS = 1024; // width
	final private static int IMAGE_ROWS = 720; // height
	private static String IMAGE_FILE_NAME = "escena.png";
	final private static int ANTIALIASING = 7;

	// puntos de interes
	final private static int MAX_REBOTES_RAYO = 7;
	final private static int DISTANCIA_FOCAL = 30;
	final private static Vector4 POV = new Vector4(40, 60, 40, 1);//(80, -50, 80, 1);
	final private static Vector4 POSICION_LUZ = new Vector4(50, 50, 50, 1);
	final private static Color COLOR_LUZ = new Color(255, 255, 255);
	final private static double LUZ_AMBIENTAL = 0.1;

	// contenido de la escena
	private static ArrayList<Objeto> objetos = new ArrayList<Objeto>();
	private static BufferedImage img = new BufferedImage(IMAGE_COLS, IMAGE_ROWS, BufferedImage.TYPE_INT_RGB);

	public static void main(String[] args) {

		System.out.printf("Preparando escena...");

		/* Define la escena */
		Foco luz = new Foco(POSICION_LUZ, COLOR_LUZ);
		Camara camara = new Camara(POV, Vector4.sub(new Vector4(0, 0, 0, 1), POV), DISTANCIA_FOCAL, IMAGE_COLS,
				IMAGE_ROWS);
		
//		objetos = EscenaCajas.crear3(POV);
		IMAGE_FILE_NAME = "escena.png";
		objetos = Escena.crear(POV);
		objetos.add(utils.PLYConverter.getFigure("untitled.ply"));

		System.out.println("OK");
		System.out.printf("Lanzando rayos...");

		/*
		 * Para cada pixel de la pantalla se lanza un rayo y se buscan los
		 * objetos de la escena con los que intersecciona
		 */
		for (int j = 0; j < IMAGE_COLS; j++) {
			int jj = j - IMAGE_COLS / 2;
			for (int i = 0; i < IMAGE_ROWS; i++) {
				int ii = i - IMAGE_ROWS / 2;
				Color pixel = null;

				for (int k = 0; k < ANTIALIASING; k++) {
					/* Se crea el rayo que sale del ojo hacia el pixel(i,j) */
					Rayo rayoPrimario = camara.rayoToPixel(ii, jj);

					/* Pinta el pixel(i,j) del color devuelto por el rayo */
					Color colorPixel = trazar(rayoPrimario, 0);
					if (pixel != null && colorPixel != null) {
						pixel = mix(pixel, colorPixel);
					} else {
						pixel = colorPixel;
					}

				}
				int color = pixel.getRGB();
				img.setRGB(j, i, color);
			}
		}

		System.out.println("OK");
		System.out.printf("Guardando imagen...");

		/* Escribe la imagen resultante en un fichero */
		try {

			File f = new File(IMAGE_FILE_NAME);
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
	private static Color trazar(Rayo rayo, int rebotes) {

		Objeto objeto = null;
		double minDistancia = Double.POSITIVE_INFINITY;
		double lambda;
		Vector4 pIntersec = null;
		Vector4 pIntersecFinal = null;

		/* Para cada objeto de la escena, se intenta interseccionar */
		for (int k = 0; k < objetos.size(); k++) {

			Double landa = objetos.get(k).interseccion(rayo);

			if (landa != null) {

				/* Se ha producido una interseccion */
				lambda = (double) landa;

				if (lambda >= 0) {

					/* Se comprueba cual es el objeto visible mas cercano */
					pIntersec = Rayo.getInterseccion(rayo, lambda);
					double distance = Vector4.distancia(rayo.getOrigen(), pIntersec);

					/* Se extrae el objeto mas cercano */
					if (distance < minDistancia) {
						objeto = objetos.get(k);
						if(objeto instanceof Figura){
							Figura f = (Figura) objeto;
							objeto = f.getObjeto(rayo);
						}
						if(objeto instanceof Caja){
							Caja c = (Caja) objeto;
							objeto = c.getObjeto(rayo);
						}
						pIntersecFinal = pIntersec;
						minDistancia = distance;
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
		 * Si existe al menos un objeto visible, objeto ser� distinto de null, y
		 * se lanzan los rayos correspondientes
		 */
		if (objeto != null) {

			/* Color inicial */
			//finalColor = luzAmbiental(LUZ_AMBIENTAL, objeto);

			/* Comprueba si el objeto recibe luz en el punto de interseccion */
			double epsilon = 1e-12;
			Vector4 direccion = Vector4.sub(POSICION_LUZ, pIntersecFinal).normalise();
			Vector4 origen = Vector4.add(pIntersecFinal, Vector4.mulEscalar(direccion, epsilon));
			Rayo sombra = new Rayo(origen, direccion);
			
			double maxDistancia = Vector4.distancia(sombra.getOrigen(), POSICION_LUZ);
			boolean shadow = false;
			
			/* Para cada objeto de la escena, se intenta interseccionar */
			for (int k = 0; k < objetos.size(); k++) {

				Double landa = objetos.get(k).interseccionSombra(sombra);
				if (landa != null) {

					if (landa >= 0) {

						/* Se comprueba cual es el objeto visible mas cercano */
						Vector4 interseccionSombra = Rayo.getInterseccion(sombra, landa);
						double dist = Vector4.distancia(sombra.getOrigen(), interseccionSombra);

						/* Se extrae el objeto mas cercano */
						if (dist <= maxDistancia) {
							shadow = true;
							break;
						}
					}
				}
			}
			/*
			for(Objeto o : objetos){
				if(o != objeto){
					Double landa = o.interseccionSombra(sombra);
					if(landa != null){
						if(o instanceof Figura){
							Figura f = (Figura) objeto;
							o = f.getObjeto(sombra);
						}
						if(o instanceof Caja){
							Caja c = (Caja) objeto;
							o = c.getObjeto(sombra);
						}
						Vector4 aux = Rayo.getInterseccion(sombra, landa);
						double dist = Vector4.distancia(sombra.getOrigen(), aux);
						
						if(dist < maxDistancia){
							shadow = true;
							break;
						}
					}
				}
			}
			*/

			int red, green, blue;
			if(!shadow){
				
				/* Reflexion difusa */
				Vector4 normal = objeto.normal(pIntersecFinal,rayo);
	
				double angulo = Math.cos(Vector4.angulo(sombra.getDireccion(), normal));
				if (angulo < 0) angulo = 0;
				if (angulo > 1) angulo = 1;
	
				red = (int) (objeto.getMaterial().getColor().getRed() * angulo);
				green = (int) (objeto.getMaterial().getColor().getGreen() * angulo);
				blue = (int) (objeto.getMaterial().getColor().getBlue() * angulo);
	
				Color difusa = new Color(red, green, blue);
				finalColor = difusa;
				/* fin reflexion difusa */

				/* Reflexion especular */
				Rayo reflejado = Rayo.rayoReflejado(sombra, objeto, pIntersecFinal);
				Vector4 R = reflejado.getDireccion().normalise();
				Vector4 V = Vector4.negate(rayo.getDireccion()).normalise();
	
				double coseno = Vector4.dot(R, V);
				if (coseno < 0) coseno = 0;
	
				double n = objeto.getMaterial().getShiny();
				double terminoEspecular = Math.pow(coseno, n);
	
				red = (int) (255 * Math.abs(terminoEspecular));
				green = (int) (255 * Math.abs(terminoEspecular));
				blue = (int) (255 * Math.abs(terminoEspecular));
				
				Color specular = new Color(red, green, blue);
				finalColor = ColorOperations.add(ColorOperations.escalar(finalColor, objeto.getMaterial().getKd()),
						ColorOperations.escalar(specular, objeto.getMaterial().getKs()));
				/* fin reflexion especular */

				finalColor = ColorOperations.add(finalColor, luzAmbiental(LUZ_AMBIENTAL, objeto));
			}
			else{
				
				/* Si un punto no esta iluminado recibe luz ambiental para no quedar a negro */
				finalColor = luzAmbiental(LUZ_AMBIENTAL, objeto);
			}

			/* Intenta lanzar nuevos rayos si todavia quedan rebotes */
			if (rebotes < MAX_REBOTES_RAYO) {
				
				Color colorReflejado = null;
				Color colorRefractado = null;
				
				/* Si el material del objeto es reflectante se lanza el rayo reflejado */
				if (objeto.getMaterial().isReflectante()) {
					// TODO link a rayo reflejado
					Rayo reflejado = Rayo.rayoReflejado(rayo, objeto, pIntersecFinal);
					colorReflejado = trazar(reflejado, rebotes + 1);
					colorReflejado = ColorOperations.escalar(colorReflejado, objeto.getMaterial().getKd());
				}
				
				/* Si el material del objeto es refractante se lanza el rayo refractado */
				if (objeto.getMaterial().isTransparente()) {
					// TODO link a rayo refractado
					Rayo refractado = Rayo.rayoRefractado(rayo, objeto, pIntersecFinal);
					colorRefractado = trazar(refractado, rebotes + 1);
					colorRefractado = ColorOperations.escalar(colorRefractado, objeto.getMaterial().getKs());
				}
				
				/* (FRESNEL?) Mezcla los colores obtenidos por el rayo reflejado y refractado */
				if (colorReflejado != null && colorRefractado != null) {
					Color fresnelColor = ColorOperations.fresnel(colorReflejado,colorRefractado,objeto.getMaterial().getKd());
					finalColor = ColorOperations.add(finalColor, fresnelColor);
				}
				else if (colorReflejado != null ) {
					finalColor = ColorOperations.add(finalColor, colorReflejado);
				}
				else if (colorRefractado != null ) {
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
		int r = (int) (c.getRed() * luzAmbiental);
		int b = (int) (c.getBlue() * luzAmbiental);
		int g = (int) (c.getGreen() * luzAmbiental);
		return new Color(r, g, b);
	}

	/**
	 * Mezcla los colores dados
	 */
	private static Color mix(Color pixel, Color acum) {
		int pixelr = pixel.getRed();
		int pixelb = pixel.getBlue();
		int pixelg = pixel.getGreen();

		int acumr = acum.getRed();
		int acumb = acum.getBlue();
		int acumg = acum.getGreen();

		int finalr = (pixelr + acumr) / 2;
		int finalb = (pixelb + acumb) / 2;
		int finalg = (pixelg + acumg) / 2;

		return new Color(finalr, finalg, finalb);
	}
}
