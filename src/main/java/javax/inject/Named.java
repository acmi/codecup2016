/*
 * Decompiled with CFR 0_119.
 */
package javax.inject;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;

@Qualifier
@Documented
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Named {
    public String value() default "";
}

