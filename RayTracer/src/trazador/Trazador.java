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
import objetos.Objeto;

public class Trazador {

	/* Variables globales del trazador de rayos */
	
	// imagen
	final private static int IMAGE_WIDTH = 400;
	final private static int IMAGE_HEIGHT = 400;
	final private static String IMAGE_FILE_NAME = "escena.png";
	
	// puntos de interes
	final private static int MAX_REBOTES_RAYO = 0;
	final private static Vector4 POV = new Vector4(10,10,10,1);
	final private static Vector4 POSICION_LUZ = new Vector4(0,0,0,1);
	final private static Color COLOR_LUZ = new Color(255,255,255);
	final private static double LUZ_AMBIENTAL = 0.2;
	
	// contenido de la escena
	private static ArrayList<Objeto> objetos = new ArrayList<Objeto>();
	private static BufferedImage img = new BufferedImage(
			IMAGE_HEIGHT, IMAGE_WIDTH, BufferedImage.TYPE_INT_RGB);
	
	
	public static void main(String[] args) {
		
		/* Define la escena */
		Foco foco = new Foco(POSICION_LUZ,COLOR_LUZ);
		
		/* Define los objetos de la escena */
		Esfera esfera = new Esfera(10.0,Color.RED);
		objetos.add(esfera);
		
		/*  Para cada pixel de la pantalla se lanza un rayo y se buscan
		 * los objetos de la escena con los que intersecciona */
		for (int j = 0; j < IMAGE_HEIGHT; j++) {
			for (int i = 0; i < IMAGE_WIDTH; i++) {
				
				/* Se crea el rayo que sale del ojo hacia el pixel(i,j) */
				Vector4 pixel = new Vector4(j,i,0,1);
				Rayo rayoPrimario = new Rayo(POV, pixel);
				
				/* Pinta el pixel(i,j) del color devuelto por el rayo */
				Color colorPixel = trazar(rayoPrimario, 0);
				int color = colorPixel.getRGB();
				img.setRGB(j, i, color);
			}
		}
		
		/* Escribe la imagen resultante en un fichero */
		try {
			
			File f = new File(IMAGE_FILE_NAME);
			if (!ImageIO.write(img, "PNG", f)) {
				throw new RuntimeException("Unexpected error writing image");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param ray: rayo lanzado
	 * @param depth: numero de rebotes actuales
	 * @return color calculado para el 
	 */
	private static Color trazar(Rayo rayo, int rebotes){
		
		Color colorFinal = Color.BLACK;
		Objeto objeto = null;
		double minDistancia = Double.POSITIVE_INFINITY;
		double lambda;
		Vector4 pIntersec;
		Vector4 nIntersec;
		
		/* Para cada objeto de la escena, se intenta interseccionar */
		for (int k = 0; k < objetos.size(); k++) {
			
			Double landa = objetos.get(k).interseccion(rayo);
			
			if (landa != null) {
				
				/* Se ha producido una interseccion */
				lambda = (double) landa;
				
				if (lambda >= 0) {
					
					/* Se comprueba cual es el objeto visible mas cercano */
					pIntersec = Rayo.getInterseccion(rayo, lambda);
					double distance =  Vector4.distancia(rayo.getOrigen(), pIntersec);
					
					if (distance < minDistancia) {
						objeto = objetos.get(k);
						minDistancia = distance;
					}
				}
			}
			
		}
		colorFinal = objeto.getColor();
		
		/* Si existe al menos un objeto visible, se lanzan los rayos
		 * correspondientes */
//		if (object != null) {
//			
//			// objeto no opaco
//			if (object.esCristal() && rebotes < MAX_REBOTES_RAYO) {
//				
//				/* Lanza el rayo de reflexion */
//				Rayo rayoReflejado = rayo.rayoReflejado();
//				Color colorReflexion = trazar(rayoReflejado, rebotes + 1);
//				
//				/* Lanza el rayo de refraccion */
//				Rayo rayoRefractado = rayo.rayoRefractado();
//				Color colorRefraccion = trazar(rayoRefractado, rebotes + 1);
//				
//				// colorFinal = reflected * object.Kd + refracted * (1 - object.Kd)
//			}
//			
//			// objeto opaco
//			else {
//
//				/* Lanza el rayo sombra */
//				Rayo rayoSombra = Rayo.RayoPcpioFin(pIntersec, LIGHT_POS);
//				boolean esSombra = false;
//				
//				/* Si el rayo sombra intersecciona con algun objeto, el punto
//				 * desde el que sale no recibe luz */
//				for (int k = 0; !esSombra && k < objetos.size(); k++) {
//					
//					Double landa = objetos.get(k).interseccion(rayo);
//					esSombra = (landa != null);
//				}
//				
//				/* Si no llega luz al objeto se le aplica la iluminacion ambiente */
//				if (esSombra) {
//					colorFinal = object.color * AMBIENT_LIGHT;
//				}
//			}
//			
//		}
		
		return colorFinal;
	}
}
