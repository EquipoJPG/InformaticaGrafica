package utils;

import java.util.ArrayList;

import Jama.Matrix;
import data.Vector4;

public class TransformacionesAfines {

	// Variables finales publicas
	public static final double DEFAULT_TRASLATION = 0;
	public static final double DEFAULT_SCALE = 1;
	public static final boolean DEFAULT_SYMMETRY = false;
	public static final double DEFAULT_ROTATION = 0;
	public static final double DEFAULT_SHEAR = 0;

	public static void main(String[] args) {
		double[][] values = { { 5, 5, 5, 5 }, { 3, 1, 3, 3 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		Matrix a = new Matrix(values);
		Vector4 b = new Vector4(1, 2, 3, 1);
		multiplyVectorByMatrix(b, a);
		a.print(0, 0);
		System.out.println(a.get(1, 1));
	}

	public static Vector4 multiplyVectorByMatrix(Vector4 v, Matrix m) {
		double[][] t = { { v.getX(), v.getY(), v.getZ(), v.getH() } };
		Matrix temp = new Matrix(t);
		Matrix c = temp.times(m);
		return Vector4.matrixToVector4(c);
	}

	public static Matrix combine(ArrayList<Matrix> matArray) {
		Matrix returned = null;
		if (matArray.size() >= 2) {
			returned = matArray.get(0);
			for (int i = 1; i < matArray.size(); i++) {
				returned = returned.times(matArray.get(i));
			}
		} else {
			try {
				throw new Exception("There must be at least two matrices in matArray!");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		return returned;
	}

	/**
	 * Devuelve la matriz de transformacion afin en base a los parametros que le
	 * son pasados
	 * 
	 * @param xtraslation:
	 *            Traslacion en el eje X. Default 0
	 * @param ytraslation:
	 *            Traslacion en el eje Y. Default 0
	 * @param ztraslation:
	 *            Traslacion en el eje Z. Default 0
	 * @param xscale:
	 *            Escalado en el eje X. Default 1
	 * @param yscale:
	 *            Escalado en el eje Y. Default 1
	 * @param zscale:
	 *            Escalado en el eje Z. Default 1
	 * @param xsym:
	 *            Simetria en el eje X. Default false
	 * @param ysym:
	 *            Simetria en el eje Y. Default false
	 * @param zsym:
	 *            Simetria en el eje Z. Default false
	 * @param xrot:
	 *            Rotacion en radianes en el eje X. Default 0
	 * @param yrot:
	 *            Rotacion en radianes en el eje Y. Default 0
	 * @param zrot:
	 *            Rotacion en radianes en el eje Z. Default 0
	 * @param xshear:
	 *            Cizallas en el eje X. Default 0
	 * @param yshear:
	 *            Cizallas en el eje Y. Default 0
	 * @param zshear:
	 *            Cizallas en el eje Z. Default 0
	 * @return matriz afin
	 */

	public static Matrix affineMatrix(double xtraslation, double ytraslation, double ztraslation, double xscale,
			double yscale, double zscale, double gscale, boolean xsym, boolean ysym, boolean zsym, double xrot,
			double yrot, double zrot, double xshear, double yshear, double zshear) {
		ArrayList<Matrix> total = new ArrayList<Matrix>();

		// Add traslation matrix
		total.add(getGeneralTraslation(xtraslation, ytraslation, ztraslation));

		// Add scale matrix
		total.add(getGeneralScale(xscale, yscale, zscale));

		// Add global scale matrix
		total.add(getGlobalScale(gscale));

		// Add symmetric matrices
		if (xsym) {
			if (ysym) {
				if (zsym) {
					// Symmetry over the origin
					total.add(getOriginSymmetry());
				} else {
					// Symmetry over XY plane
					total.add(getXYSymmetry());
				}
			} else if (zsym) {
				// Symmetry over XZ plane
				total.add(getXZSymmetry());
			} else {
				// Symmetry over X axis
				total.add(getXSymmetry());
			}
		} else if (ysym) {
			if (zsym) {
				// Symmetry over YZ axis
				total.add(getYZSymmetry());
			} else {
				// Symmetry over Y axis
				total.add(getYSymmetry());
			}
		} else if (zsym) {
			// Symmetry over Z axis
			total.add(getZSymmetry());
		} else {
			// Do nothing buddy
		}

		// Add rotation matrices
		total.add(getXRotation(xrot));
		total.add(getYRotation(yrot));
		total.add(getZRotation(zrot));

		// Add shear matrices
		total.add(getGeneralShear(xshear, yshear, zshear));

		// Return value
		return combine(total);
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
		double[][] values = { { Math.cos(radian), Math.sin(radian), 0, 0 },
				{ -1 * Math.sin(radian), Math.cos(radian), 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getXShear(double sxy, double sxz) {
		double[][] values = { { 1, sxy, sxz, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getYShear(double syx, double syz) {
		double[][] values = { { 1, 0, 0, 0 }, { syx, 1, syz, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getZShear(double szx, double szy) {
		double[][] values = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { szx, szy, 1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getGeneralShear(double sxy, double sxz, double syx, double syz, double szx, double szy) {
		double[][] values = { { 1, sxy, sxz, 0 }, { syx, 1, syz, 0 }, { szx, szy, 1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getGeneralShear(double kx, double ky, double kz) {
		double[][] values = { { 1, kx, kx, 0 }, { ky, 1, ky, 0 }, { kz, kz, 1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getGeneralShear(double k) {
		double[][] values = { { 1, k, k, 0 }, { k, 1, k, 0 }, { k, k, 1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(values);
	}

	public static Matrix getGlobalScale(double h) {
		return getGeneralScale(h);
	}
}
