/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.math3.geometry.euclidean.threed;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3DFormat;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Vector3D
implements Serializable,
Vector<Object> {
    public static final Vector3D ZERO = new Vector3D(0.0, 0.0, 0.0);
    public static final Vector3D PLUS_I = new Vector3D(1.0, 0.0, 0.0);
    public static final Vector3D MINUS_I = new Vector3D(-1.0, 0.0, 0.0);
    public static final Vector3D PLUS_J = new Vector3D(0.0, 1.0, 0.0);
    public static final Vector3D MINUS_J = new Vector3D(0.0, -1.0, 0.0);
    public static final Vector3D PLUS_K = new Vector3D(0.0, 0.0, 1.0);
    public static final Vector3D MINUS_K = new Vector3D(0.0, 0.0, -1.0);
    public static final Vector3D NaN = new Vector3D(Double.NaN, Double.NaN, Double.NaN);
    public static final Vector3D POSITIVE_INFINITY = new Vector3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    public static final Vector3D NEGATIVE_INFINITY = new Vector3D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    private final double x;
    private final double y;
    private final double z;

    public Vector3D(double d2, double d3, double d4) {
        this.x = d2;
        this.y = d3;
        this.z = d4;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public double getNorm() {
        return FastMath.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double getNormSq() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public Vector3D add(Vector<Object> vector) {
        Vector3D vector3D = (Vector3D)vector;
        return new Vector3D(this.x + vector3D.x, this.y + vector3D.y, this.z + vector3D.z);
    }

    public Vector3D subtract(Vector<Object> vector) {
        Vector3D vector3D = (Vector3D)vector;
        return new Vector3D(this.x - vector3D.x, this.y - vector3D.y, this.z - vector3D.z);
    }

    public Vector3D normalize() throws MathArithmeticException {
        double d2 = this.getNorm();
        if (d2 == 0.0) {
            throw new MathArithmeticException(LocalizedFormats.CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR, new Object[0]);
        }
        return this.scalarMultiply(1.0 / d2);
    }

    public Vector3D scalarMultiply(double d2) {
        return new Vector3D(d2 * this.x, d2 * this.y, d2 * this.z);
    }

    public boolean isNaN() {
        return Double.isNaN(this.x) || Double.isNaN(this.y) || Double.isNaN(this.z);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Vector3D) {
            Vector3D vector3D = (Vector3D)object;
            if (vector3D.isNaN()) {
                return this.isNaN();
            }
            return this.x == vector3D.x && this.y == vector3D.y && this.z == vector3D.z;
        }
        return false;
    }

    public int hashCode() {
        if (this.isNaN()) {
            return 642;
        }
        return 643 * (164 * MathUtils.hash(this.x) + 3 * MathUtils.hash(this.y) + MathUtils.hash(this.z));
    }

    public double dotProduct(Vector<Object> vector) {
        Vector3D vector3D = (Vector3D)vector;
        return MathArrays.linearCombination(this.x, vector3D.x, this.y, vector3D.y, this.z, vector3D.z);
    }

    public Vector3D crossProduct(Vector<Object> vector) {
        Vector3D vector3D = (Vector3D)vector;
        return new Vector3D(MathArrays.linearCombination(this.y, vector3D.z, - this.z, vector3D.y), MathArrays.linearCombination(this.z, vector3D.x, - this.x, vector3D.z), MathArrays.linearCombination(this.x, vector3D.y, - this.y, vector3D.x));
    }

    public String toString() {
        return Vector3DFormat.getInstance().format(this);
    }
}

