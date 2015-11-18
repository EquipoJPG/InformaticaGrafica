package data;

public class Vector4 {

	/* Atributos de la clase PuntoVectorH */
	private int x;
	private int y;
	private int z;
	private int h;		// h == 1 -> punto, h == 0 -> vector
	
	public Vector4() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.h = 0;
	}
	
	public Vector4(int x, int y, int z, int h) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.h = h;
	}

	/**
	 * 
	 * @param subVector: Vector4 sumado
	 * @return this + subVector
	 */
	public Vector4 add(Vector4 addVector){
		Vector4 v = this;
		
		v.x += addVector.x;
		v.y += addVector.y;
		v.z += addVector.z;
		
		return v;
	}
	
	/**
	 * 
	 * @param subVector: Vector4 restado
	 * @return this - subVector
	 */
	public Vector4 substract(Vector4 subVector){
		Vector4 v = this;
		
		v.x -= subVector.x;
		v.y -= subVector.y;
		v.z -= subVector.z;
		v.h -= subVector.h;
		
		return v;
	}
	
}
