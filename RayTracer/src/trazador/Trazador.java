package trazador;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import data.PuntoVectorH;
import data.Rayo;
import objetos.Objeto;

public class Trazador {

	/* Variables globales del trazador de rayos */
	
	// imagen
	final private static int IMAGE_WIDTH = 600;
	final private static int IMAGE_HEIGHT = 800;
	final private static String IMAGE_FILE_NAME = "escena.png";
	
	// puntos de interes
	final private static int MAX_RAY_DEPTH = 3;
	final private static PuntoVectorH POV = new PuntoVectorH(0,0,0,1);
	final private static PuntoVectorH LIGHT_POS = new PuntoVectorH(0,0,0,1);
	final private static double AMBIENT_LIGHT = 0.2;
	
	// contenido de la escena
	private static ArrayList<Objeto> objects = new ArrayList<Objeto>();
	private static BufferedImage img = new BufferedImage(
			IMAGE_HEIGHT, IMAGE_WIDTH, BufferedImage.TYPE_INT_RGB);
	
	
	public static void main(String[] args) {
		
		/*  Para cada pixel de la pantalla se lanza un rayo y se buscan
		 * los objetos de la escena con los que intersecciona */
		for (int j = 0; j < IMAGE_HEIGHT; j++) {
			for (int i = 0; i < IMAGE_WIDTH; i++) {
				
				/* Se crea el rayo que pasa por el pixel(i,j) y se lanza */
				//PuntoVectorH pixel = new PuntoVectorH(i,j,0,1);
				//Rayo primaryRay = new Rayo(pixel.substract(POV));
				
				// pintar en el pixel(i,j) el color obtenido por "trace(primaryRay, 0);"
				img.setRGB(j, i, 0);
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
	
//	private static Color trace(Rayo ray, int depth){
//		
//		Color finalColor = Color.BLACK;
//		Objeto object = null;
//		double minDistance = Double.POSITIVE_INFINITY;
//		PuntoVectorH pIntersect;
//		PuntoVectorH nIntersect;
//		
//		/* Para cada objeto de la escena, se intenta interseccionar */
//		for (int k = 0; k < objects.size(); k++) {
//			
//			double lambda = objects.get(k).intersect(ray);
//			if (lambda >= 0) {
//				
//				/* Se comprueba cual es el objeto visible mas cercano */
//				pIntersect = ray.getIntersection(lambda);
//				double distance = ray.origin.distance(pIntersect);
//				
//				if (distance < minDistance) {
//					object = objects.get(k);
//					minDistance = distance;
//				}
//			}
//		}
//		
//		/* Si existe al menos un objeto visible, se lanzan los rayos
//		 * correspondientes */
//		if (object != null) {
//			
//			// objeto no opaco
//			if (object.isGlass() && depth < MAX_RAY_DEPTH) {
//				
//				/* Lanza el rayo de reflexion */
//				Rayo reflectionRay = ray.reflectionRay();
//				Color reflectionColor = trace(reflectionRay, depth + 1);
//				
//				/* Lanza el rayo de refraccion */
//				Rayo refractionRay = ray.refractionRay();
//				Color refractionColor = trace(refractionRay, depth + 1);
//				
//				// finalColor =  reflected * object.Kd + refracted * (1 - object.Kd)
//			}
//			
//			// objeto opaco
//			else {
//
//				/* Lanza el rayo sombra */
//				Rayo shadowRay = ray.shadowRay(LIGHT_POS.substract(pIntersect));
//				boolean isShadow = false;
//				
//				/* Si el rayo sombra intersecciona con algun objeto, el punto
//				 * desde el que sale no recibe luz */
//				for (int k = 0; !isShadow && k < objects.size(); k++) {
//					isShadow = (objects.get(k).intersect(ray) > 0);
//				}
//				
//				/* Si no llega luz al objeto se le aplica la iluminacion ambiente */
//				if (isShadow) {
//					finalColor = object.color * AMBIENT_LIGHT;
//				}
//			}
//			
//		}
//		
//		return finalColor;
//	}
}
