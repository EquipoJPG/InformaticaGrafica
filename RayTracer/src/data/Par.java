package data;

import objetos.Objeto;

public class Par {

	/* Atributos privados */
	private Double interseccion;
	private Objeto objeto;
	
	public Par(Double i, Objeto o){
		interseccion = i;
		objeto = o;
	}

	public Double getInterseccion() {
		return interseccion;
	}

	public void setInterseccion(Double interseccion) {
		this.interseccion = interseccion;
	}

	public Objeto getObjeto() {
		return objeto;
	}

	public void setObjeto(Objeto objeto) {
		this.objeto = objeto;
	}
}
