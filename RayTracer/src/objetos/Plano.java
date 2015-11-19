package objetos;

import data.Rayo;
import data.Vector4;

public class Plano extends Objeto {

	private Vector4 normal;
	private Vector4 punto;
	
	public Plano(Vector4 normal, Vector4 punto, Material m) {
		this.normal = normal;
		this.punto = punto;
		super.material = m;
	}
	
	@Override
	public Double interseccion(Rayo ray) {
		
	}
}
