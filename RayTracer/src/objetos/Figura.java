package objetos;

import java.util.ArrayList;
import java.util.Iterator;

import data.Rayo;
import data.Vector4;

public class Figura extends Objeto {

	/* Atributos privados */
	private ArrayList<Objeto> lista;
	
	public Figura(){
		lista = new ArrayList<Objeto>();
	}
	
	public Figura(Objeto o){
		lista = new ArrayList<Objeto>();
		lista.add(o);
	}
	
	public Figura(ArrayList<Objeto> l){
		lista = l;
	}
	
	public void addObjeto(Objeto o){
		lista.add(o);
	}
	
	@Override
	public Double interseccion(Rayo ray) {
		if(lista.size()==0){
			return null;
		}
		else{
			Iterator<Objeto> it = lista.iterator();
			Objeto o = it.next();
			Double interseccion = o.interseccion(ray);
			while(it.hasNext()){
				o = it.next();
				Double iterInterseccion = o.interseccion(ray);
				if(iterInterseccion!=null){
					if(interseccion==null){
						interseccion = o.interseccion(ray);
					}
					else{
						if(iterInterseccion<interseccion){
							interseccion = iterInterseccion;
						}
					}
				}
				else{
					interseccion = iterInterseccion;
				}
			}
			return interseccion;
		}
	}

	@Override
	public Vector4 normal(Vector4 interseccion) {
		try {
			throw new Exception("No se puede computar la normal de una figura sin el rayo incidente");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Vector4 normal(Vector4 in, Rayo ray){
		if(lista.size()==0 || ray == null){
			return null;
		}
		else{
			Iterator<Objeto> it = lista.iterator();
			Objeto o = it.next();
			Objeto ourObject = o;
			Double interseccion = o.interseccion(ray);
			while(it.hasNext()){
				o = it.next();
				Double iterInterseccion = o.interseccion(ray);
				if(iterInterseccion!=null){
					if(interseccion==null){
						interseccion = o.interseccion(ray);
						ourObject = o;
					}
					else{
						if(iterInterseccion<interseccion){
							interseccion = iterInterseccion;
							ourObject = o;
						}
					}
				}
				else{
					interseccion = iterInterseccion;
				}
			}
			return ourObject.normal(in);
		}
	}
	
}
