package trazador;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import data.Rayo;
import data.Vector4;
import objetos.Esfera;
import objetos.Material;
import objetos.Objeto;

public class Trazador {

	/* Variables globales del trazador de rayos */

	// imagen
	final private static int IMAGE_COLS = 1024; // width
	final private static int IMAGE_ROWS = 720; // height
	final private static String IMAGE_FILE_NAME = "escena.png";
	final private static int ANTIALIASING = 7;

	// puntos de interes
	final private static int MAX_REBOTES_RAYO = 3;
	final private static int DISTANCIA_FOCAL = 100;
	final private static Vector4 POV = new Vector4(100, 100, -50, 1);
	final private static Vector4 POSICION_LUZ = new Vector4(0, 50, 0, 1);
	final private static Color COLOR_LUZ = new Color(255, 255, 255);
	final private static double LUZ_AMBIENTAL = 0.2;

	// contenido de la escena
	private static ArrayList<Objeto> objetos = new ArrayList<Objeto>();
	private static BufferedImage img = new BufferedImage(IMAGE_COLS, IMAGE_ROWS, BufferedImage.TYPE_INT_RGB);

	public static void main(String[] args) {

		System.out.printf("Preparando escena...");

		/* Define la escena */
		Foco luz = new Foco(POSICION_LUZ, COLOR_LUZ);
		Camara camara = new Camara(POV, Vector4.sub(new Vector4(0, 0, 0, 1), POV), DISTANCIA_FOCAL, IMAGE_COLS,
				IMAGE_ROWS);

		/* Define los objetos de la escena */
		Esfera esfera1 = new Esfera(20, new Material(0.8, 0.2, 0.9, 0.1, Color.RED, false, true, 1));
		// TODO IM CHANGING STUFF
		Material mesf2 = new Material(0.2, 0.1, 0.9, 1, Color.CYAN, false, false, 100);
		Esfera esfera2 = new Esfera(new Vector4(10, 0, 10, 1), 20, mesf2);

		// objetos.add(esfera1);
		objetos.add(esfera2);

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

			Double landa = objetos.get(k).interseccion(rayo, false);

			if (landa != null) {

				/* Se ha producido una interseccion */
				lambda = (double) landa;

				if (lambda >= 0) {

					/* Se comprueba cual es el objeto visible mas cercano */
					pIntersec = Rayo.getInterseccion(rayo, lambda);
					double distance = Vector4.distancia(rayo.getOrigen(), pIntersec);

					/* Se extrae el objeto m�s cercano */
					if (distance < minDistancia) {
						objeto = objetos.get(k);
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
			finalColor = luzAmbiental(LUZ_AMBIENTAL, objeto);

			/* Comprobar si le da la luz o no */
			double epsilon = 1e-12;
			Vector4 direccion = Vector4.sub(POSICION_LUZ, pIntersecFinal).normalise();
			Vector4 origen = Vector4.add(pIntersecFinal, Vector4.mulEscalar(direccion, epsilon));
			Rayo sombra = new Rayo(origen, direccion);

			///////////////////////////////////////////////////////////////////////
			// TODO link a reflexion difusa
			/* reflexion difusa */
			Vector4 normal = objeto.normal(pIntersecFinal);

			double angulo = Math.cos(Vector4.angulo(sombra.getDireccion(), normal));
			if (angulo < 0)
				angulo = 0;
			if (angulo > 1)
				angulo = 1;

			int red = (int) (objeto.getMaterial().getColor().getRed() * angulo);
			int green = (int) (objeto.getMaterial().getColor().getGreen() * angulo);
			int blue = (int) (objeto.getMaterial().getColor().getBlue() * angulo);

			Color difusa = new Color(red, green, blue);
			finalColor = ColorOperations.add(finalColor, difusa);
			/* fin reflexion difusa */

			/////////////////////////////////////////////////////////////////
			// TODO link a reflexion especular
			/* reflexion especular */
			Rayo reflejado = Rayo.rayoReflejado(sombra, objeto, pIntersecFinal);
			Vector4 R = reflejado.getDireccion().normalise();
			Vector4 V = Vector4.negate(rayo.getDireccion()).normalise();

			double coseno = -Vector4.dot(R, V);
			if (coseno < 0)
				coseno = 0;

			double n = objeto.getMaterial().getShiny();
			double terminoEspecular = Math.pow(coseno, n);

			red = (int) (objeto.getMaterial().getColor().getRed() * Math.abs(terminoEspecular));
			green = (int) (objeto.getMaterial().getColor().getGreen() * Math.abs(terminoEspecular));
			blue = (int) (objeto.getMaterial().getColor().getBlue() * Math.abs(terminoEspecular));
			Color specular = new Color(red, green, blue);

			finalColor = ColorOperations.add(ColorOperations.escalar(finalColor, objeto.getMaterial().getKd()),
					ColorOperations.escalar(specular, objeto.getMaterial().getKs()));
			/* fin reflexion especular */
			
			if((objeto.getMaterial().isTransparente() ||
					objeto.getMaterial().isReflectante())
					&& rebotes < MAX_REBOTES_RAYO){
				////////////////////////////////////////////////////////////////
				// TODO link a rayo reflejado
				if(objeto.getMaterial().isTransparente()){
					////////////////////////////////////////////////////////////
					// TODO link a rayo refractado
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
