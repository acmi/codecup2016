/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

import com.google.common.base.Objects;
import com.google.common.base.Supplier;
import java.io.Serializable;

public final class Suppliers {
    public static <T> Supplier<T> ofInstance(T t2) {
        return new SupplierOfInstance<T>(t2);
    }

    private static class SupplierOfInstance<T>
    implements Supplier<T>,
    Serializable {
        final T instance;

        SupplierOfInstance(T t2) {
            this.instance = t2;
        }

        @Override
        public T get() {
            return this.instance;
        }

        public boolean equals(Object object) {
            if (object instanceof SupplierOfInstance) {
                SupplierOfInstance supplierOfInstance = (SupplierOfInstance)object;
                return Objects.equal(this.instance, supplierOfInstance.instance);
            }
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(this.instance);
        }

        public String toString() {
            return "Suppliers.ofInstance(" + this.instance + ")";
        }
    }

}

