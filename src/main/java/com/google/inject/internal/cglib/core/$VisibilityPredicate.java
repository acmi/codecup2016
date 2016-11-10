/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.core.$Predicate;
import com.google.inject.internal.cglib.core.$TypeUtils;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

public class $VisibilityPredicate
implements $Predicate {
    private boolean protectedOk;
    private String pkg;
    private boolean samePackageOk;

    public $VisibilityPredicate(Class class_, boolean bl) {
        this.protectedOk = bl;
        this.samePackageOk = class_.getClassLoader() != null;
        this.pkg = $TypeUtils.getPackageName($Type.getType(class_));
    }

    public boolean evaluate(Object object) {
        Member member = (Member)object;
        int n2 = member.getModifiers();
        if (Modifier.isPrivate(n2)) {
            return false;
        }
        if (Modifier.isPublic(n2)) {
            return true;
        }
        if (Modifier.isProtected(n2) && this.protectedOk) {
            return true;
        }
        return this.samePackageOk && this.pkg.equals($TypeUtils.getPackageName($Type.getType(member.getDeclaringClass())));
    }
}

