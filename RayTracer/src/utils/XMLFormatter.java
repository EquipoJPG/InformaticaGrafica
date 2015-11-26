package utils;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Jama.Matrix;
import data.Vector4;
import objetos.Esfera;
import objetos.Figura;
import objetos.Material;
import objetos.Objeto;
import objetos.Plano;
import objetos.Triangulo;
import trazador.Camara;
import trazador.Foco;

public class XMLFormatter {

	public static void main(String[] args) {
		String xml = "escena.xml";
		Camara c = getCamara(xml);
		List<Foco> focos = getFocos(xml);
		List<Objeto> objetos = getObjetos(xml);

		System.out.println(c.toString());
		for (Foco f : focos)
			System.out.println(f.toString());
		for (Objeto o : objetos)
			System.out.println(o.toString());
	}

	/**
	 * @param xml
	 * @return
	 */
	private static Document setup(String xml) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			return db.parse(new File(xml));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Obtiene una lista con los focos
	 * 
	 * @param xml
	 * @return
	 */
	public static List<Foco> getFocos(String xml) {
		Document doc = setup(xml);
		doc.getDocumentElement().normalize();

		List<Foco> focos = new ArrayList<Foco>();

		NodeList nl = doc.getElementsByTagName("focos");
		if (nl != null && nl.getLength() > 0) {
			Element e = (Element) nl.item(0);
			NodeList nl2 = e.getElementsByTagName("foco");
			for (int i = 0; i < nl2.getLength(); i++) {
				e = (Element) nl2.item(i);

				// posicion
				double x = Double.parseDouble(e.getAttribute("x"));
				double y = Double.parseDouble(e.getAttribute("y"));
				double z = Double.parseDouble(e.getAttribute("z"));
				Vector4 posicion = new Vector4(x, y, z, 1);

				// intensidad
				int intensidad = Integer.parseInt(e.getElementsByTagName("intensidad").item(0).getTextContent());

				// color
				int r = Integer.parseInt(((Element) e.getElementsByTagName("color").item(0)).getAttribute("r"));
				int g = Integer.parseInt(((Element) e.getElementsByTagName("color").item(0)).getAttribute("g"));
				int b = Integer.parseInt(((Element) e.getElementsByTagName("color").item(0)).getAttribute("b"));
				Color c = new Color(r, g, b);

				focos.add(new Foco(posicion, c, intensidad));
			}
		}

		return focos;
	}

	/**
	 * Obtiene la camara
	 * 
	 * @param xml
	 * @return
	 */
	public static Camara getCamara(String xml) {
		Document doc = setup(xml);
		doc.getDocumentElement().normalize();

		NodeList nl = doc.getElementsByTagName("pov");
		if (nl != null && nl.getLength() > 0) {
			Element e = (Element) nl.item(0);

			double x = Double.parseDouble(e.getAttribute("x"));
			double y = Double.parseDouble(e.getAttribute("y"));
			double z = Double.parseDouble(e.getAttribute("z"));
			Vector4 posicion = new Vector4(x, y, z, 1);

			int columnas = Integer.parseInt(((Element) e.getElementsByTagName("columnas").item(0)).getTextContent());
			int filas = Integer.parseInt(((Element) e.getElementsByTagName("filas").item(0)).getTextContent());
			int anchura = Integer.parseInt(((Element) e.getElementsByTagName("anchura").item(0)).getTextContent());
			int altura = Integer.parseInt(((Element) e.getElementsByTagName("altura").item(0)).getTextContent());

			int f = Integer.parseInt(((Element) e.getElementsByTagName("focal").item(0)).getTextContent());

			Vector4 g = Vector4.sub(new Vector4(0, 0, 0, 1), posicion).normalise();

			return new Camara(posicion, g, f, columnas, filas, anchura, altura);
		}
		return null;
	}

	public static List<Objeto> getObjetos(String xml) {
		Document doc = setup(xml);
		doc.getDocumentElement().normalize();
		List<Objeto> objetos = new ArrayList<Objeto>();

		NodeList nl = doc.getElementsByTagName("cajas");
		if (nl != null && nl.getLength() > 0) {
			NodeList nl2 = ((Element) nl.item(0)).getElementsByTagName("caja");
			for (int i = 0; i < nl2.getLength(); i++) {
				NodeList nl3_esferas = ((Element) nl2.item(i)).getElementsByTagName("esfera");
				NodeList nl3_triangulos = ((Element) nl2.item(i)).getElementsByTagName("triangulo");
				NodeList nl3_planos = ((Element) nl2.item(i)).getElementsByTagName("plano");
				NodeList nl3_figuras = ((Element) nl2.item(i)).getElementsByTagName("figura");

				// TODO esferas
				for (int j = 0; j < nl3_esferas.getLength(); j++) {
					Element e = (Element) nl3_esferas.item(j);

					//////////////////////////////////////////////////////////
					// traslacion
					double x = Double.parseDouble(e.getAttribute("x"));
					double y = Double.parseDouble(e.getAttribute("y"));
					double z = Double.parseDouble(e.getAttribute("z"));

					// escala global
					Element ee = (Element) e.getElementsByTagName("global").item(0);
					double global = Double.parseDouble(ee.getTextContent());

					// escala local
					ee = (Element) e.getElementsByTagName("escala").item(0);
					double escalaX = Double.parseDouble(ee.getAttribute("x"));
					double escalaY = Double.parseDouble(ee.getAttribute("y"));
					double escalaZ = Double.parseDouble(ee.getAttribute("z"));

					// rotacion
					ee = (Element) e.getElementsByTagName("rotacion").item(0);
					double rotacionX = Double.parseDouble(ee.getAttribute("x"));
					double rotacionY = Double.parseDouble(ee.getAttribute("y"));
					double rotacionZ = Double.parseDouble(ee.getAttribute("z"));

					// simetria
					ee = (Element) e.getElementsByTagName("simetria").item(0);
					int simetriaX = Integer.parseInt(ee.getAttribute("x"));
					int simetriaY = Integer.parseInt(ee.getAttribute("y"));
					int simetriaZ = Integer.parseInt(ee.getAttribute("z"));

					// cizalla
					ee = (Element) e.getElementsByTagName("cizalla").item(0);
					int cizallaX = Integer.parseInt(ee.getAttribute("x"));
					int cizallaY = Integer.parseInt(ee.getAttribute("y"));
					int cizallaZ = Integer.parseInt(ee.getAttribute("z"));
					///////////////////////////////////////////////////////////////////

					//////////////////////////////////////////////////////////////////
					// material
					ee = (Element) e.getElementsByTagName("material").item(0);

					// material -> color
					Element eee = (Element) e.getElementsByTagName("color").item(0);
					Color c = new Color(Integer.parseInt(eee.getAttribute("r")),
							Integer.parseInt(eee.getAttribute("g")), Integer.parseInt(eee.getAttribute("b")));

					// difusa
					double difusa = Double.parseDouble(ee.getElementsByTagName("difusa").item(0).getTextContent());

					// especular
					double especular = Double
							.parseDouble(ee.getElementsByTagName("especular").item(0).getTextContent());

					// reflectante
					double reflectante = Double
							.parseDouble(ee.getElementsByTagName("reflectante").item(0).getTextContent());

					// transparente
					double transparente = Double
							.parseDouble(ee.getElementsByTagName("transparente").item(0).getTextContent());

					// shiny
					int shiny = Integer.parseInt(ee.getElementsByTagName("shiny").item(0).getTextContent());
					//////////////////////////////////////////////////////////////////////////////

					Material m = new Material(c, difusa, especular, reflectante, transparente, shiny);
					Matrix T = TransformacionesAfines.affineMatrix(x, y, z, escalaX, escalaY, escalaZ, global,
							(simetriaX > 0), (simetriaY > 0), (simetriaZ > 0), rotacionX, rotacionY, rotacionZ,
							cizallaX, cizallaY, cizallaZ);

					Esfera esfera = new Esfera(1, m, T);
					objetos.add(esfera);
				}

				// TODO triangulos
				for (int j = 0; j < nl3_triangulos.getLength(); j++) {
					Element e = (Element) nl3_triangulos.item(j);

					/////////////////////////////////////////////////////////
					Element ee = (Element) e.getElementsByTagName("punto1").item(0);
					Vector4 punto1 = new Vector4(Double.parseDouble(e.getAttribute("x")),
							Double.parseDouble(e.getAttribute("y")), Double.parseDouble(e.getAttribute("z")), 1);

					ee = (Element) e.getElementsByTagName("punto2").item(0);
					Vector4 punto2 = new Vector4(Double.parseDouble(e.getAttribute("x")),
							Double.parseDouble(e.getAttribute("y")), Double.parseDouble(e.getAttribute("z")), 1);

					ee = (Element) e.getElementsByTagName("punto3").item(0);
					Vector4 punto3 = new Vector4(Double.parseDouble(e.getAttribute("x")),
							Double.parseDouble(e.getAttribute("y")), Double.parseDouble(e.getAttribute("z")), 1);
							////////////////////////////////////////////////////////

					//////////////////////////////////////////////////////////
					// traslacion
					double x = Double.parseDouble(e.getAttribute("x"));
					double y = Double.parseDouble(e.getAttribute("y"));
					double z = Double.parseDouble(e.getAttribute("z"));

					// escala global
					ee = (Element) e.getElementsByTagName("global").item(0);
					double global = Double.parseDouble(ee.getTextContent());

					// escala local
					ee = (Element) e.getElementsByTagName("escala").item(0);
					double escalaX = Double.parseDouble(ee.getAttribute("x"));
					double escalaY = Double.parseDouble(ee.getAttribute("y"));
					double escalaZ = Double.parseDouble(ee.getAttribute("z"));

					// rotacion
					ee = (Element) e.getElementsByTagName("rotacion").item(0);
					double rotacionX = Double.parseDouble(ee.getAttribute("x"));
					double rotacionY = Double.parseDouble(ee.getAttribute("y"));
					double rotacionZ = Double.parseDouble(ee.getAttribute("z"));

					// simetria
					ee = (Element) e.getElementsByTagName("simetria").item(0);
					int simetriaX = Integer.parseInt(ee.getAttribute("x"));
					int simetriaY = Integer.parseInt(ee.getAttribute("y"));
					int simetriaZ = Integer.parseInt(ee.getAttribute("z"));

					// cizalla
					ee = (Element) e.getElementsByTagName("cizalla").item(0);
					int cizallaX = Integer.parseInt(ee.getAttribute("x"));
					int cizallaY = Integer.parseInt(ee.getAttribute("y"));
					int cizallaZ = Integer.parseInt(ee.getAttribute("z"));
					///////////////////////////////////////////////////////////////////

					//////////////////////////////////////////////////////////////////
					// material
					ee = (Element) e.getElementsByTagName("material").item(0);

					// material -> color
					Element eee = (Element) e.getElementsByTagName("color").item(0);
					Color c = new Color(Integer.parseInt(eee.getAttribute("r")),
							Integer.parseInt(eee.getAttribute("g")), Integer.parseInt(eee.getAttribute("b")));

					// difusa
					double difusa = Double.parseDouble(ee.getElementsByTagName("difusa").item(0).getTextContent());

					// especular
					double especular = Double
							.parseDouble(ee.getElementsByTagName("especular").item(0).getTextContent());

					// reflectante
					double reflectante = Double
							.parseDouble(ee.getElementsByTagName("reflectante").item(0).getTextContent());

					// transparente
					double transparente = Double
							.parseDouble(ee.getElementsByTagName("transparente").item(0).getTextContent());

					// shiny
					int shiny = Integer.parseInt(ee.getElementsByTagName("shiny").item(0).getTextContent());
					//////////////////////////////////////////////////////////////////////////////

					Material m = new Material(c, difusa, especular, reflectante, transparente, shiny);
					Matrix T = TransformacionesAfines.affineMatrix(x, y, z, escalaX, escalaY, escalaZ, global,
							(simetriaX > 0), (simetriaY > 0), (simetriaZ > 0), rotacionX, rotacionY, rotacionZ,
							cizallaX, cizallaY, cizallaZ);

					Triangulo triangulo = new Triangulo(punto1, punto2, punto3, m, T);
					objetos.add(triangulo);
				}

				// TODO planos
				for (int j = 0; j < nl3_planos.getLength(); j++) {
					Element e = (Element) nl3_planos.item(j);

					/////////////////////////////////////////////////////////
					Element ee = (Element) e.getElementsByTagName("topleft").item(0);
					Vector4 topleft = new Vector4(Double.parseDouble(e.getAttribute("x")),
							Double.parseDouble(e.getAttribute("y")), Double.parseDouble(e.getAttribute("z")), 1);

					ee = (Element) e.getElementsByTagName("topright").item(0);
					Vector4 topright = new Vector4(Double.parseDouble(e.getAttribute("x")),
							Double.parseDouble(e.getAttribute("y")), Double.parseDouble(e.getAttribute("z")), 1);

					ee = (Element) e.getElementsByTagName("bottomleft").item(0);
					Vector4 bottomleft = new Vector4(Double.parseDouble(e.getAttribute("x")),
							Double.parseDouble(e.getAttribute("y")), Double.parseDouble(e.getAttribute("z")), 1);

					ee = (Element) e.getElementsByTagName("bottomright").item(0);
					Vector4 bottomright = new Vector4(Double.parseDouble(e.getAttribute("x")),
							Double.parseDouble(e.getAttribute("y")), Double.parseDouble(e.getAttribute("z")), 1);
							////////////////////////////////////////////////////////

					//////////////////////////////////////////////////////////
					// traslacion
					double x = Double.parseDouble(e.getAttribute("x"));
					double y = Double.parseDouble(e.getAttribute("y"));
					double z = Double.parseDouble(e.getAttribute("z"));

					// escala global
					ee = (Element) e.getElementsByTagName("global").item(0);
					double global = Double.parseDouble(ee.getTextContent());

					// escala local
					ee = (Element) e.getElementsByTagName("escala").item(0);
					double escalaX = Double.parseDouble(ee.getAttribute("x"));
					double escalaY = Double.parseDouble(ee.getAttribute("y"));
					double escalaZ = Double.parseDouble(ee.getAttribute("z"));

					// rotacion
					ee = (Element) e.getElementsByTagName("rotacion").item(0);
					double rotacionX = Double.parseDouble(ee.getAttribute("x"));
					double rotacionY = Double.parseDouble(ee.getAttribute("y"));
					double rotacionZ = Double.parseDouble(ee.getAttribute("z"));

					// simetria
					ee = (Element) e.getElementsByTagName("simetria").item(0);
					int simetriaX = Integer.parseInt(ee.getAttribute("x"));
					int simetriaY = Integer.parseInt(ee.getAttribute("y"));
					int simetriaZ = Integer.parseInt(ee.getAttribute("z"));

					// cizalla
					ee = (Element) e.getElementsByTagName("cizalla").item(0);
					int cizallaX = Integer.parseInt(ee.getAttribute("x"));
					int cizallaY = Integer.parseInt(ee.getAttribute("y"));
					int cizallaZ = Integer.parseInt(ee.getAttribute("z"));
					///////////////////////////////////////////////////////////////////

					//////////////////////////////////////////////////////////////////
					// material
					ee = (Element) e.getElementsByTagName("material").item(0);

					// material -> color
					Element eee = (Element) e.getElementsByTagName("color").item(0);
					Color c = new Color(Integer.parseInt(eee.getAttribute("r")),
							Integer.parseInt(eee.getAttribute("g")), Integer.parseInt(eee.getAttribute("b")));

					// difusa
					double difusa = Double.parseDouble(ee.getElementsByTagName("difusa").item(0).getTextContent());

					// especular
					double especular = Double
							.parseDouble(ee.getElementsByTagName("especular").item(0).getTextContent());

					// reflectante
					double reflectante = Double
							.parseDouble(ee.getElementsByTagName("reflectante").item(0).getTextContent());

					// transparente
					double transparente = Double
							.parseDouble(ee.getElementsByTagName("transparente").item(0).getTextContent());

					// shiny
					int shiny = Integer.parseInt(ee.getElementsByTagName("shiny").item(0).getTextContent());
					//////////////////////////////////////////////////////////////////////////////

					Material m = new Material(c, difusa, especular, reflectante, transparente, shiny);
					Matrix T = TransformacionesAfines.affineMatrix(x, y, z, escalaX, escalaY, escalaZ, global,
							(simetriaX > 0), (simetriaY > 0), (simetriaZ > 0), rotacionX, rotacionY, rotacionZ,
							cizallaX, cizallaY, cizallaZ);

					Plano plano = new Plano(topleft, topright, bottomleft, bottomright, m, T);
					objetos.add(plano);
				}

				// TODO figuras
				for (int j = 0; j < nl3_figuras.getLength(); j++) {
					Element e = (Element) nl3_figuras.item(j);
					String path = e.getAttribute("path");

					//////////////////////////////////////////////////////////
					// traslacion
					double x = Double.parseDouble(e.getAttribute("x"));
					double y = Double.parseDouble(e.getAttribute("y"));
					double z = Double.parseDouble(e.getAttribute("z"));

					// escala global
					Element ee = (Element) e.getElementsByTagName("global").item(0);
					double global = Double.parseDouble(ee.getTextContent());

					// escala local
					ee = (Element) e.getElementsByTagName("escala").item(0);
					double escalaX = Double.parseDouble(ee.getAttribute("x"));
					double escalaY = Double.parseDouble(ee.getAttribute("y"));
					double escalaZ = Double.parseDouble(ee.getAttribute("z"));

					// rotacion
					ee = (Element) e.getElementsByTagName("rotacion").item(0);
					double rotacionX = Double.parseDouble(ee.getAttribute("x"));
					double rotacionY = Double.parseDouble(ee.getAttribute("y"));
					double rotacionZ = Double.parseDouble(ee.getAttribute("z"));

					// simetria
					ee = (Element) e.getElementsByTagName("simetria").item(0);
					int simetriaX = Integer.parseInt(ee.getAttribute("x"));
					int simetriaY = Integer.parseInt(ee.getAttribute("y"));
					int simetriaZ = Integer.parseInt(ee.getAttribute("z"));

					// cizalla
					ee = (Element) e.getElementsByTagName("cizalla").item(0);
					double cizallaX = Double.parseDouble(ee.getAttribute("x"));
					double cizallaY = Double.parseDouble(ee.getAttribute("y"));
					double cizallaZ = Double.parseDouble(ee.getAttribute("z"));
					///////////////////////////////////////////////////////////////////

					//////////////////////////////////////////////////////////////////
					// material
					ee = (Element) e.getElementsByTagName("material").item(0);

					// difusa
					double difusa = Double.parseDouble(ee.getElementsByTagName("difusa").item(0).getTextContent());

					// especular
					double especular = Double
							.parseDouble(ee.getElementsByTagName("especular").item(0).getTextContent());

					// reflectante
					double reflectante = Double
							.parseDouble(ee.getElementsByTagName("reflectante").item(0).getTextContent());

					// transparente
					double transparente = Double
							.parseDouble(ee.getElementsByTagName("transparente").item(0).getTextContent());

					// shiny
					int shiny = Integer.parseInt(ee.getElementsByTagName("shiny").item(0).getTextContent());
					//////////////////////////////////////////////////////////////////////////////

					Material m = new Material(null, difusa, especular, reflectante, transparente, shiny);
					Figura f = PLYConverter.getFigura(path, m);

					Matrix T = TransformacionesAfines.affineMatrix(x, y, z, escalaX, escalaY, escalaZ, global,
							(simetriaX > 0), (simetriaY > 0), (simetriaZ > 0), rotacionX, rotacionY, rotacionZ,
							cizallaX, cizallaY, cizallaZ);

					f.setT(T);
					objetos.add(f);
				}
			}
		}
		return objetos;
	}
}