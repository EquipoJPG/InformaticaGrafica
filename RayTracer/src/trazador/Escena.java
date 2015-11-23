package trazador;

import java.awt.Color;
import java.util.ArrayList;

import data.Vector4;
import objetos.Esfera;
import objetos.Material;
import objetos.Objeto;
import objetos.Plano;
import objetos.Triangulo;

public class Escena {
	
	public static ArrayList<Objeto> crear(Vector4 POV){
		ArrayList<Objeto> objetos = new ArrayList<Objeto>();
		
		/* Define los objetos de la escena */

		// esferas
		Esfera esfera1 = new Esfera(3, new Material(0.8, 0.2, 0.9, 0.4, Color.RED, false, true, 50));
		Material mesf2 = new Material(0.2, 0.1, 0.9, 0, Color.CYAN, false, true, 1);
		Esfera esfera2 = new Esfera(new Vector4(20, -20, 20, 1), 5, mesf2);
		
		Material luz = new Material(0.2, 0.1, 0.9, 1, Color.ORANGE, false, true, 1);
		Esfera sol = new Esfera(new Vector4(40, -40, 40, 0), 5, luz);

		// planos
		Vector4 normalPlano1 = Vector4.sub(POV, new Vector4(1, 0, 0, 1));
		Vector4 p1Plano1 = new Vector4(0, -40, 30, 1);
		Vector4 p2Plano1 = new Vector4(0, 40, 30, 1);
		Vector4 p3Plano1 = new Vector4(0, 40, 0, 1);
		Vector4 p4Plano1 = new Vector4(0, -40, 0, 1);
		Plano plano1 = new Plano(p1Plano1, p2Plano1,
				p3Plano1, p4Plano1, new Material(0.2, 0.6, 0.5, 0.5, Color.GREEN, false, true, 10));
		
		// triangulos
		Vector4 p1Triang1 = new Vector4(0, 0, 0, 1);
		Vector4 p2Triang1 = new Vector4(-100, 100, 0, 1);
		Vector4 p3Triang1 = new Vector4(30, 30, 0, 1);
		Triangulo triangulo1 = new Triangulo(p1Plano1, p2Plano1, p3Plano1, new Material(1, 1, 0.8, 0.2, Color.WHITE, false, true, 1));

		Esfera esferaT1 = new Esfera(p1Plano1, 5, new Material(0.2, 0, 0.2, 0.2, Color.RED, false, false, 1));
		Esfera esferaT2 = new Esfera(p2Plano1, 5, new Material(0.2, 0, 0.2, 0.2, Color.GREEN, false, false, 1));
		Esfera esferaT3 = new Esfera(p3Plano1, 5, new Material(0.2, 0, 0.2, 0.2, Color.BLUE, false, false, 1));
		Esfera esferaT4 = new Esfera(p3Plano1, 5, new Material(0.2, 0, 0.2, 0.2, Color.WHITE, false, false, 1));
		
		
		// agrega todos los objetos
		objetos.add(esfera1);
		objetos.add(esfera2);
		objetos.add(sol);
		objetos.add(plano1);
		objetos.add(triangulo1);
		
		objetos.add(esferaT1);
		objetos.add(esferaT2);
		objetos.add(esferaT3);
		objetos.add(esferaT4);
		
		return objetos;
	}
}
