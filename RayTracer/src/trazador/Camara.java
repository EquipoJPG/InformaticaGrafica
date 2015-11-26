package trazador;

import java.util.Random;

import data.Rayo;
import data.Vector4;

public class Camara {
	
	private Vector4 posicion;	// posicion del ojo
	private Vector4 direccion;	// direccion del ojo
	
	private Vector4 u, v, w;	// sistema de la camara
	private double f;	// separacion ojo-pantalla
	
	private int anchura, altura;	// anchura y altura de la pantalla
	
	private int filas, columnas;	// pixeles de anchura y altura

	/**
	 * Constructor de la camara en @param posicion mirando a @param g a una
	 * distancia @param f de la pantalla con @param px_width pixeles de anchura
	 * y @param px_height pixeles de altura.
	 * 
	 */
	public Camara(Vector4 posicion, Vector4 g, double f, int columnas, int filas, int anchura, int altura){
		this.posicion = posicion;
		this.direccion = g;
		this.f = f;
		
		this.filas = filas;
		this.columnas = columnas;
		
		this.anchura = anchura;
		this.altura = altura;
		
		
		/* u, v, w */
		Vector4 up = new Vector4(0, 0, 1, 0);
		w = Vector4.div(Vector4.negate(direccion), g.normaL2());	// w = -g / norm(g)
		Vector4 aux = Vector4.cross(up, w);
		u = aux.normalise();
		v = Vector4.cross(w, u);
		
		System.out.println("U: " + u.toString() + "\nV: " + v.toString() + "\nW: " + w.toString());
	}
	
	/**
	 * @return el rayo que va desde el ojo al pixel (i,j) de la pantalla
	 */
	public Rayo rayoToPixel(int i, int j){
		double diffu = (double) anchura / (double) (columnas - 1);
		double diffv = (double) altura / (double) (filas - 1);
		
		// antialiasing por supermestreo random
		Random r = new Random();
		double varu = r.nextDouble() - 0.5;	// [-0.5, 0.5]
		double varv = r.nextDouble() - 0.5;	// [-0.5, 0.5]
		
		Vector4 local = new Vector4(i*diffu + varu*diffu, j*diffv + varv*diffv, -f, 1);
		Vector4 mundo = Vector4.cambioDeBase(local, u, v, w, posicion);
		
		return new Rayo(mundo, Vector4.sub(mundo, posicion).normalise());
	}

	public int getRows() {
		return filas;
	}

	public int getCols() {
		return columnas;
	}
}
