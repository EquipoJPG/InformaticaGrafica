package utils;

import Jama.Matrix;
import data.Vector4;

public class TransformacionesAfines {

	public static void main(String[] args) {
		double[][] values = { { 5, 5, 5, 5 }, { 3, 1, 3, 3 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		Matrix a = new Matrix(values);
		Vector4 b = new Vector4(1, 2, 3, 1);
		multiplyVectorByMatrix(b, a);
		a.print(0, 0);
		System.out.println(a.get(1, 1));
	}

	public static Vector4 multiplyVectorByMatrix(Vector4 v, Matrix m) {
		Matrix temp = null;
		if (Vector4.esPunto(v)) {
			double[][] t = { { v.getX(), v.getY(), v.getZ(), 1 } };
			temp = new Matrix(t);
		} else {
			double[][] t = { { v.getX(), v.getY(), v.getZ(), 0 } };
			temp = new Matrix(t);
		}
		Matrix c = temp.times(m);
		return Vector4.matrixToVector4(c);
	}

	public static Matrix getIdentity() {
		double[][] values = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getXTraslation(double x) {
		double[][] values = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { x, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getYTraslation(double y) {
		double[][] values = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, y, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getZTraslation(double z) {
		double[][] values = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, z, 1 } };
		return new Matrix(values);
	}

	public static Matrix getGeneralTraslation(double x, double y, double z) {
		double[][] values = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { x, y, z, 1 } };
		return new Matrix(values);
	}

	public static Matrix getGeneralTraslation(double k) {
		double[][] values = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { k, k, k, 1 } };
		return new Matrix(values);
	}

	public static Matrix getXScale(double a) {
		double[][] values = { { a, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getYScale(double b) {
		double[][] values = { { 1, 0, 0, 0 }, { 0, b, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getZScale(double c) {
		double[][] values = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, c, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getGeneralScale(double a, double b, double c) {
		double[][] values = { { a, 0, 0, 0 }, { 0, b, 0, 0 }, { 0, 0, c, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getGeneralScale(double k) {
		double[][] values = { { k, 0, 0, 0 }, { 0, k, 0, 0 }, { 0, 0, k, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getXSymmetry() {
		double[][] values = { { 1, 0, 0, 0 }, { 0, -1, 0, 0 }, { 0, 0, -1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getYSymmetry() {
		double[][] values = { { -1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, -1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getZSymmetry() {
		double[][] values = { { -1, 0, 0, 0 }, { 0, -1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getXYSymmetry() {
		double[][] values = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, -1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getYZSymmetry() {
		double[][] values = { { -1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getXZSymmetry() {
		double[][] values = { { 1, 0, 0, 0 }, { 0, -1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getOriginSymmetry() {
		double[][] values = { { -1, 0, 0, 0 }, { 0, -1, 0, 0 }, { 0, 0, -1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getXRotation(double radian) {
		double[][] values = { { 1, 0, 0, 0 }, { 0, Math.cos(radian), Math.sin(radian), 0 },
				{ 0, -1 * Math.sin(radian), Math.cos(radian), 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getYRotation(double radian) {
		double[][] values = { { Math.cos(radian), 0, -1 * Math.sin(radian), 0 }, { 0, 1, 0, 0 },
				{ Math.sin(radian), 0, Math.cos(radian), 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getZRotation(double radian) {
		double[][] values = { { Math.cos(radian), Math.sin(radian), 0, 0 }, { -1*Math.sin(radian), Math.cos(radian), 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}
}
