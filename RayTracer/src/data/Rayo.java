package data;

public class Rayo {

	/* Atributos de la clase Rayo */
	private Vector4 origin;
	private Vector4 intersection;
	private double phi;
	private double theta;
	
	public Rayo(Vector4 origin, double phi, double theta) {
		this.origin = origin;
		this.phi = phi;
		this.theta = theta;
	}
	
	public Vector4 getIntersection() {
		
		return intersection;
	}
	
}
