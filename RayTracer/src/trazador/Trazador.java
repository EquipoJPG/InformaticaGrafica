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
	final private static int IMAGE_COLS = 1024;	// width
	final private static int IMAGE_ROWS = 720;	// height
	final private static String IMAGE_FILE_NAME = "escena.png";
	
	// puntos de interes
	final private static int MAX_REBOTES_RAYO = 7;
	final private static int DISTANCIA_FOCAL = 5;
	final private static Vector4 POV = new Vector4(10,10,10,1);
	final private static Vector4 POSICION_LUZ = new Vector4(0,0,0,1);
	final private static Color COLOR_LUZ = new Color(255,255,255);
	final private static double LUZ_AMBIENTAL = 0.2;
	
	// contenido de la escena
	private static ArrayList<Objeto> objetos = new ArrayList<Objeto>();
	private static BufferedImage img = new BufferedImage(
			IMAGE_COLS, IMAGE_ROWS, BufferedImage.TYPE_INT_RGB);
	
	
	public static void main(String[] args) {

		System.out.printf("Preparando escena...");
		
		/* Define la escena */
		Foco luz = new Foco(POSICION_LUZ,COLOR_LUZ);
		Camara camara = new Camara(POV, Vector4.sub(new Vector4(), POV),
									DISTANCIA_FOCAL, IMAGE_COLS, IMAGE_ROWS);
		
		/* Define los objetos de la escena */
		Esfera esfera = new Esfera(17.305, new Material(0.2, 0.5, Color.RED));
		objetos.add(esfera);
		
		System.out.println("OK");
		System.out.printf("Lanzando rayos...");
		
		/*  Para cada pixel de la pantalla se lanza un rayo y se buscan
		 * los objetos de la escena con los que intersecciona */
		for (int j = 0; j < IMAGE_COLS; j++) {
			int jj = j - IMAGE_COLS/2;
			for (int i = 0; i < IMAGE_ROWS; i++) {
				int ii = i - IMAGE_ROWS/2;
				
				/* Se crea el rayo que sale del ojo hacia el pixel(i,j) */
//				Vector4 pixel = new Vector4(j,i,0,1);
//				Rayo rayoPrimario = new Rayo(POV, pixel);
				Rayo rayoPrimario = camara.rayoToPixel(ii, jj);
				
				/* Pinta el pixel(i,j) del color devuelto por el rayo */
//				System.out.printf(numRayos + ") Lanzando rayo desde pixel(" + j + ", " + i + ")...");
				Color colorPixel = trazar(rayoPrimario, 0);
				int color = colorPixel.getRGB();
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
			}
			else {
				System.out.println("OK");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param rayo: rayo lanzado
	 * @param rebotes: numero de rebotes actuales
	 * @return color calculado para el punto desde el que se lanza el
	 * rayo rayo
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
		if (objeto != null) {
			/*
			 * La luz ambiental siempre es completamente blanca: (255, 255, 255)
			 * color_luz_ambiental * coeficiente_ambiental <- cuanta luz llega al objeto
			 * color_objeto * (luz que llega al objeto) <- termino ambiental
			 */
			Color ambiente = COLOR_LUZ;
			Color c = objeto.getMaterial().getColor();
			int r = (int) (c.getRed() * ( (ambiente.getRed() * LUZ_AMBIENTAL)/255 ));
			int b = (int) (c.getBlue() * ( (ambiente.getBlue() * LUZ_AMBIENTAL)/ 255));
			int g = (int) (c.getGreen() * ( (ambiente.getGreen() * LUZ_AMBIENTAL)/ 255));
			colorFinal = new Color(r, g, b);
			
//			System.out.printf("Interseccion, color: ");
//			System.out.println(colorFinal);
		}
		else {
//			System.out.println("NO HAY INTERSECCION D:");
		}
		
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
