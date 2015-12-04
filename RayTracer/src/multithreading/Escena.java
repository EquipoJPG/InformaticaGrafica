package multithreading;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import data.Rayo;
import objetos.Objeto;
import trazador.Camara;
import trazador.Foco;
import utils.XMLFormatter;

public class Escena {
	
	/* Imagen */
	private int i;
	private int j;
	int ANTIALIASING;

	/* Variables escena */
	int MAX_REBOTES_RAYO;
	double LUZ_AMBIENTAL;
	double EPSILON;

	/* Contenido de la escena */
	private BufferedImage img;
	List<Objeto> objetos;
	List<Foco> focos;
	Camara camara;

	/* FLAGS DEBUG */
	boolean TERMINO_AMBIENTAL;
	boolean TERMINO_DIFUSO;
	boolean TERMINO_ESPECULAR;
	boolean TERMINO_REFLEJADO;
	boolean TERMINO_REFRACTADO;
	
	/* Variables workers */
	List<Pixel> trabajos = new ArrayList<Pixel>();
	
	public Escena(String xml) {
		this.i = 0;
		this.j = -1;
		
		camara = XMLFormatter.getCamara(xml);
		img = new BufferedImage(camara.getCols(), camara.getRows(), BufferedImage.TYPE_INT_RGB);
		objetos = XMLFormatter.getObjetos(xml);
		focos = XMLFormatter.getFocos(xml);

		MAX_REBOTES_RAYO = XMLFormatter.getRebotes(xml);
		LUZ_AMBIENTAL = XMLFormatter.getLuzAmbiente(xml);
		ANTIALIASING = XMLFormatter.getAntialiasing(xml);
		boolean[] flags = XMLFormatter.setFlags(xml);
		if (flags != null) {
			TERMINO_AMBIENTAL = flags[0];
			TERMINO_DIFUSO = flags[1];
			TERMINO_ESPECULAR = flags[2];
			TERMINO_REFLEJADO = flags[3];
			TERMINO_REFRACTADO = flags[4];
		}
		EPSILON = XMLFormatter.getEpsilon(xml);
	}
	
	public synchronized void inicializarTrabajo(int id) {
		Pixel pixelVacio = new Pixel(-1,-1);
		trabajos.add(id, pixelVacio);
	}
	
	public synchronized void pintarPixel(int id, int color){
		Pixel pixel = trabajos.get(id);
		Pixel pixelVacio = new Pixel(-1,-1);
		trabajos.set(id, pixelVacio);
		int ii = pixel.getI();
		int jj = pixel.getJ();
		img.setRGB(jj, ii, color);
	}
	
	public synchronized ArrayList<Rayo> getRayo(int id){
		
		ArrayList<Rayo> rayos = new ArrayList<Rayo>();
		
		/* Crea el rayo que pasa por el pixel j, i */
		if (j < camara.getCols() - 1) {
			j++;
		}
		else {
			if (i < camara.getRows() - 1) {
				i++;
				j = 0;
			}
			else {
				return null;
			}
		}
		
		int ii = i - camara.getRows() / 2;
		int jj = j - camara.getCols() / 2;
		
		/* Genera el rayo y actualiza el estado de los trabajadores */
		for (int k = 0; k < ANTIALIASING; k++) {
			rayos.add(camara.rayoToPixel(jj, ii));
		}
		Pixel workingPixel = new Pixel(i, j);
		trabajos.set(id, workingPixel);
		
		return rayos;
	}
	
	public BufferedImage getImg(){
		return img;
	}
}
