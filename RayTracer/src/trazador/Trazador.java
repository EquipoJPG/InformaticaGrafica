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
import objetos.Plano;

public class Trazador {

	/* Variables globales del trazador de rayos */
	
	// imagen
	final private static int IMAGE_COLS = 1024;	// width
	final private static int IMAGE_ROWS = 720;	// height
	final private static String IMAGE_FILE_NAME = "escena.png";
	
	// puntos de interes
	final private static int MAX_REBOTES_RAYO = 7;
	final private static int DISTANCIA_FOCAL = 100;
	final private static Vector4 POV = new Vector4(100,100,100,1);
	final private static Vector4 POSICION_LUZ = new Vector4(50,50,50,1);
	final private static Color COLOR_LUZ = new Color(255,255,255);
	final private static double LUZ_AMBIENTAL = 0.5;
	
	// contenido de la escena
	private static ArrayList<Objeto> objetos = new ArrayList<Objeto>();
	private static BufferedImage img = new BufferedImage(
			IMAGE_COLS, IMAGE_ROWS, BufferedImage.TYPE_INT_RGB);
	
	public static void main(String[] args) {

		System.out.printf("Preparando escena...");
		
		/* Define la escena */
		Foco luz = new Foco(POSICION_LUZ,COLOR_LUZ);
		Camara camara = new Camara(POV, Vector4.sub(new Vector4(0, 0, 0, 1), POV),
									DISTANCIA_FOCAL, IMAGE_COLS, IMAGE_ROWS);
		
		/* Define los objetos de la escena */
		Esfera esfera1 = new Esfera(20, new Material(0.2, 0, Color.RED));
		Esfera esfera2 = new Esfera(new Vector4(10, 0, 10, 1), 20, new Material(0.3, 0, Color.CYAN));
		
		Vector4 normalPlano1 = Vector4.sub(new Vector4(0, 0, 0, 1), POV); //new Vector4(1, 0, 0, 0);
		Vector4 centroPlano1 = new Vector4(0, 0, 0, 1);
		Plano plano1 = new Plano(normalPlano1, centroPlano1, 30, 30, new Material(0.2, 0.5, Color.GREEN));
		
		objetos.add(esfera1);
		objetos.add(esfera2);
		//objetos.add(plano1);
		
		System.out.println("OK");
		System.out.printf("Lanzando rayos...");
		
		/*  Para cada pixel de la pantalla se lanza un rayo y se buscan
		 * los objetos de la escena con los que intersecciona */
		for (int j = 0; j < IMAGE_COLS; j++) {
			int jj = j - IMAGE_COLS/2;
			for (int i = 0; i < IMAGE_ROWS; i++) {
				int ii = i - IMAGE_ROWS/2;
				
				/* Se crea el rayo que sale del ojo hacia el pixel(i,j) */
				Rayo rayoPrimario = camara.rayoToPixel(ii, jj);
				
				/* Pinta el pixel(i,j) del color devuelto por el rayo */
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
		
		Color colorFinal = Color.WHITE;
		Objeto objeto = null;
		double minDistancia = Double.POSITIVE_INFINITY;
		double lambda;
		Vector4 pIntersec = null;
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
		
		/* Si existe al menos un objeto visible, se lanzan los rayos
		 * correspondientes */
		if (objeto != null) {
			
			// objeto no opaco
			if (objeto.getMaterial().getK_refraccion() > 0 && rebotes < MAX_REBOTES_RAYO) {
				
				/* Lanza el rayo de reflexion */
				Rayo rayoReflejado = Rayo.rayoReflejado(rayo, objeto, pIntersec);
				Color colorReflexion = trazar(rayoReflejado, rebotes + 1);
				
				/* Lanza el rayo de refraccion */
				Rayo rayoRefractado = Rayo.rayoRefractado(rayo, objeto, pIntersec);
				Color colorRefraccion = trazar(rayoRefractado, rebotes + 1);
				
				/* Calcula el color resultante */
				double Kd = objeto.getMaterial().getK_reflexion();
				int rFinal =(int) ((int) ((int) colorReflexion.getRed() * Kd) + ((int) colorRefraccion.getRed() * (1 - Kd)));
				int gFinal =(int) ((int) ((int) colorReflexion.getGreen() * Kd) + ((int) colorRefraccion.getGreen() * (1 - Kd)));
				int bFinal =(int) ((int) ((int) colorReflexion.getBlue() * Kd) + ((int) colorRefraccion.getBlue() * (1 - Kd)));
				colorFinal = new Color(rFinal, gFinal, bFinal);
			}
			
			// objeto opaco
			else {

				/* Lanza el rayo sombra */
				Rayo rayoSombra = Rayo.RayoPcpioFin(pIntersec, POSICION_LUZ);
				boolean esSombra = false;
				
				/* Si el rayo sombra intersecciona con algun objeto, el punto
				 * desde el que sale no recibe luz */
				for (int k = 0; !esSombra && k < objetos.size(); k++) {
					
					Double landa = objetos.get(k).interseccion(rayo);
					esSombra = (landa != null);
				}
				
				/* Si no llega luz al objeto se le aplica la iluminacion ambiente */
				if (esSombra) {
					
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
				}
			}
			
		}
		
		return colorFinal;
	}
}
