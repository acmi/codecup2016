/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.math3.geometry.euclidean.threed;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Locale;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.VectorFormat;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.CompositeFormat;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Vector3DFormat
extends VectorFormat<Object> {
    public Vector3DFormat() {
        super("{", "}", "; ", CompositeFormat.getDefaultNumberFormat());
    }

    public Vector3DFormat(NumberFormat numberFormat) {
        super("{", "}", "; ", numberFormat);
    }

    public static Vector3DFormat getInstance() {
        return Vector3DFormat.getInstance(Locale.getDefault());
    }

    public static Vector3DFormat getInstance(Locale locale) {
        return new Vector3DFormat(CompositeFormat.getDefaultNumberFormat(locale));
    }

    @Override
    public StringBuffer format(Vector<Object> vector, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        Vector3D vector3D = (Vector3D)vector;
        return this.format(stringBuffer, fieldPosition, vector3D.getX(), vector3D.getY(), vector3D.getZ());
    }
}

