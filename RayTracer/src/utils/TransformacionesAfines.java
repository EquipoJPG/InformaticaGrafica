package utils;

import Jama.Matrix;
import Jama.LUDecomposition;

public class TransformacionesAfines {

	public static Matrix getIdentity() {
		double[][] values = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}
	
	public static Matrix getXTraslation(double x){
		double[][] values = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { x, 0, 0, 1 } };
		return new Matrix(values);
	}
	
	public static Matrix getYTraslation(double y){
		double[][] values = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, y, 0, 1 } };
		return new Matrix(values);
	}
	
	public static Matrix getZTraslation(double z){
		double[][] values = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, z, 1 } };
		return new Matrix(values);
	}
	
	public static Matrix getGeneralTraslation(double x, double y, double z){
		double[][] values = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { x, y, z, 1 } };
		return new Matrix(values);
	}
}
