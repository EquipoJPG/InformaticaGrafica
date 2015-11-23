package trazador;

import java.awt.Color;
import java.util.ArrayList;

import data.Vector4;
import objetos.Caja;
import objetos.Esfera;
import objetos.Figura;
import objetos.Material;
import objetos.Objeto;
import objetos.Plano;
import objetos.Triangulo;

public class EscenaCajas {
	
	public static ArrayList<Objeto> crear(Vector4 POV){
		ArrayList<Objeto> objetos = new ArrayList<Objeto>();
		
		/* Define los objetos de la escena */

		// esferas
		Esfera esfera1 = new Esfera(3, new Material(0.9, 0.4, Color.RED, false, true, 50));
		Material mesf2 = new Material(0.9, 0, Color.CYAN, false, true, 1);
		Esfera esfera2 = new Esfera(new Vector4(20, -20, 20, 1), 5, mesf2);
		
		Material luz = new Material(0.9, 1, Color.ORANGE, false, true, 1);
		
		
		// agrega todos los objetos
		objetos.add(esfera1);
		objetos.add(esfera2);
		Figura f = new Figura(objetos);
		Caja c = new Caja(f);
		ArrayList<Objeto> omg = new ArrayList<Objeto>();
		omg.add(c);
		
		return omg;
	}
}
