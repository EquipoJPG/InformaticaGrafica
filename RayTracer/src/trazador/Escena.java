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
		Esfera esfera1 = new Esfera(20, new Material(0.2, 0, 0, 0, Color.RED, false, false, 0));
		Esfera esfera2 = new Esfera(new Vector4(10, 0, 10, 1), 20, new Material(0.3, 0, 0, 0, Color.CYAN, false, false, 0));
		
		// planos
		Vector4 normalPlano1 = Vector4.sub(POV, new Vector4(-30, 0, 0, 1));
		Vector4 p1Plano1 = new Vector4(-30, 40, 100, 1);
		Vector4 p2Plano1 = new Vector4(-30, 40, 0, 1);
		Vector4 p3Plano1 = new Vector4(-30, 0, 0, 1);
		Vector4 p4Plano1 = new Vector4(-30, 0, 100, 1);
		Plano plano1 = new Plano(normalPlano1, p1Plano1, p2Plano1,
				p3Plano1, p4Plano1, new Material(0.2, 0, 0, 0, Color.GREEN, false, false, 0));
		
		// triangulos
		Vector4 p1Triang1 = new Vector4(0, 0, 0, 1);
		Vector4 p2Triang1 = new Vector4(-100, 100, 0, 1);
		Vector4 p3Triang1 = new Vector4(30, 30, 0, 1);
		Triangulo triangulo1 = new Triangulo(p1Triang1, p2Triang1, p3Triang1, new Material(0, 0, 0, 0, Color.GRAY, false, false, 0));

//		Esfera esferaT1 = new Esfera(p1Triang1, 5, new Material(0.2, 0, Color.RED, false, false));
//		Esfera esferaT2 = new Esfera(p2Triang1, 5, new Material(0.2, 0, Color.GREEN, false, false));
//		Esfera esferaT3 = new Esfera(p3Triang1, 5, new Material(0.2, 0, Color.BLUE, false, false));
		
		// agrega todos los objetos
		objetos.add(esfera1);
		objetos.add(esfera2);
		objetos.add(plano1);
//		objetos.add(triangulo1);
		
//		objetos.add(esferaT1);
//		objetos.add(esferaT2);
//		objetos.add(esferaT3);
		
		return objetos;
	}
}
