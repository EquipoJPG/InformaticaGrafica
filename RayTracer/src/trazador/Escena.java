package trazador;

import java.awt.Color;
import java.util.ArrayList;

import data.Vector4;
import objetos.Esfera;
import objetos.Material;
import objetos.Objeto;
import objetos.Plano;

public class Escena {
	
	public static ArrayList<Objeto> crear(Vector4 POV){
		ArrayList<Objeto> objetos = new ArrayList<Objeto>();
		
		/* Define los objetos de la escena */

		// esferas
//		Esfera esfera1 = new Esfera(3, new Material(Color.RED, 0.9, 0.4, 50));
		Material mesf2 = new Material(Color.CYAN, 0.4, 0.1, 0.5, 0, 70);
		Esfera esfera2 = new Esfera(new Vector4(600, 600, 600, 1), 100, mesf2);
		
		Material luz = new Material(Color.RED, 0.4, 0.6, 0.6, 0.1, 200);
		Esfera sol = new Esfera(new Vector4(1500, 1500, 1500, 0), 200, luz);

		// PLANOS
		
		// pared izquierda (y = 0)
		Vector4 p1Plano2 = new Vector4(5000, 0, 5000, 1);
		Vector4 p2Plano2 = new Vector4(0, 0, 5000, 1);
		Vector4 p3Plano2 = new Vector4(5000, 0, 0, 1);
		Vector4 p4Plano2 = new Vector4(0, 0, 0, 1);
		Plano plano2 = new Plano(p1Plano2, p2Plano2,
				p3Plano2, p4Plano2, new Material(Color.GREEN, 0.5, 0.5, 0, 0, 200));
		
		// pared derecha (x = 0)
		Vector4 p1Plano3 = new Vector4(0, 0, 5000, 1);
		Vector4 p2Plano3 = new Vector4(0, 5000, 5000, 1);
		Vector4 p3Plano3 = new Vector4(0, 0, 0, 1);
		Vector4 p4Plano3 = new Vector4(0, 5000, 0, 1);
		Plano plano3 = new Plano(p1Plano3, p2Plano3,
				p3Plano3, p4Plano3, new Material(Color.BLUE, 0.5, 0.5, 0, 0, 200));
		
		// suelo (z = 0)
		Vector4 p1Plano1 = new Vector4(0, 0, 0, 1);
		Vector4 p2Plano1 = new Vector4(0, 5000, 0, 1);
		Vector4 p3Plano1 = new Vector4(5000, 0, 0, 1);
		Vector4 p4Plano1 = new Vector4(5000, 5000, 0, 1);
		Plano plano1 = new Plano(p1Plano1, p2Plano1,
				p3Plano1, p4Plano1, new Material(Color.RED, 0.5, 0.5, 0, 0, 200));
		
		// triangulos
//		Vector4 p1Triang1 = new Vector4(0, 0, 0, 1);
//		Vector4 p2Triang1 = new Vector4(-100, 100, 0, 1);
//		Vector4 p3Triang1 = new Vector4(30, 30, 0, 1);
//		Triangulo triangulo1 = new Triangulo(p1Plano1, p2Plano1, p3Plano1, new Material(Color.WHITE, 0.8, 0.2, 1));

//		Esfera esferaT1 = new Esfera(p1Plano1, 5, new Material(Color.RED, 0.2, 0.2, 1));
//		Esfera esferaT2 = new Esfera(p2Plano1, 5, new Material(Color.GREEN, 0.2, 0.2, 1));
//		Esfera esferaT3 = new Esfera(p3Plano1, 5, new Material(Color.BLUE, 0.2, 0.2, 1));
//		Esfera esferaT4 = new Esfera(p4Plano1, 5, new Material(Color.WHITE, 0.2, 0.2, 1));
		
		
		// agrega todos los objetos
//		objetos.add(esfera1);
		objetos.add(esfera2);
		objetos.add(sol);
		
		objetos.add(plano1);
		objetos.add(plano2);
		objetos.add(plano3);
		
		return objetos;
	}
}
