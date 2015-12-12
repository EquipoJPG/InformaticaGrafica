/**
 * <h1>Par</h1>
 * Estructura de datos que guarda el <b>lambda</b> con el que un rayo 
 * intersecta contra un objeto, asi como el <b>objeto</b> intersectado.
 * 
 * @author Patricia Lazaro Tello (554309)
 * @author Alejandro Royo Amondarain (560285)
 * @author Jaime Ruiz-Borau Vizarraga (546751)
 * 
 * @version 1.0
 */

package data;

import objetos.Objeto;

public class Par {

	/* Atributos privados */
	private Double interseccion;
	private Objeto objeto;
	
	/**
	 * Crea un nuevo objeto Par
	 * @param i lambda con la que intersecta el rayo
	 * @param o objeto con el que intersecta
	 */
	public Par(Double i, Objeto o){
		interseccion = i;
		objeto = o;
	}

	/**
	 * @return lambda de la interseccion
	 */
	public Double getInterseccion() {
		return interseccion;
	}

	/**
	 * @param interseccion lambda de la interseccion
	 */
	public void setInterseccion(Double interseccion) {
		this.interseccion = interseccion;
	}

	/**
	 * @return objeto con el que intersecta el rayo
	 */
	public Objeto getObjeto() {
		return objeto;
	}

	/**
	 * @param objeto objeto con el que intersecta el rayo
	 */
	public void setObjeto(Objeto objeto) {
		this.objeto = objeto;
	}
}
