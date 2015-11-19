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
	 * La anchura y altura reales de la pantalla son el doble de px.
	 */
	public Camara(Vector4 posicion, Vector4 g, double f, int columnas, int filas){
		this.posicion = posicion;
		this.direccion = g;
		this.f = f;
		
		this.filas = filas;
		this.columnas = columnas;
		
		this.anchura = columnas / 15;
		this.altura = filas / 15;
		
		
		/* u, v, w */
		Vector4 up = new Vector4(0, 1, 0, 0);
		w = Vector4.div(Vector4.negate(direccion), g.normaL2());	// w = -g / norm(g)
		Vector4 aux = Vector4.cross(up, w);
		u = Vector4.div(aux, aux.normaL2());
		v = Vector4.cross(w, u);
	}
	
	/**
	 * @return el rayo que va desde el ojo al pixel (i,j) de la pantalla
	 */
	public Rayo rayoToPixel(int i, int j){
		double diffu = (double) anchura / (double) (columnas - 1);
		double diffv = (double) altura / (double) (filas - 1);
		
//		System.out.println("DIFFU: " + diffu +", DIFFV: " + diffv);
		
		Random r = new Random();
		double varu = r.nextDouble() - 0.5;	// [-0.5, 0.5]
		double varv = r.nextDouble() - 0.5;	// [-0.5, 0.5]
		
		Vector4 local = new Vector4(i*diffu + varu*diffu, j*diffv + varv*diffv, -f, 1);
		Vector4 mundo = Vector4.cambioDeBase(local, u, v, w, posicion);
		
		return new Rayo(mundo, Vector4.sub(mundo, posicion).normalise());
	}
}
