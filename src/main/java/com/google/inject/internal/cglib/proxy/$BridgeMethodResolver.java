/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.proxy;

import com.google.inject.internal.asm.$ClassReader;
import com.google.inject.internal.asm.$ClassVisitor;
import com.google.inject.internal.asm.$MethodVisitor;
import com.google.inject.internal.cglib.core.$Signature;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class $BridgeMethodResolver {
    private final Map declToBridge;

    public $BridgeMethodResolver(Map map) {
        this.declToBridge = map;
    }

    public Map resolveAll() {
        HashMap hashMap = new HashMap();
        for (Map.Entry entry : this.declToBridge.entrySet()) {
            Class class_ = (Class)entry.getKey();
            Set set = (Set)entry.getValue();
            try {
                new $ClassReader(class_.getName()).accept(new BridgedFinder(set, hashMap), 6);
            }
            catch (IOException iOException) {}
        }
        return hashMap;
    }

    private static class BridgedFinder
    extends $ClassVisitor {
        private Map resolved;
        private Set eligableMethods;
        private $Signature currentMethod = null;

        BridgedFinder(Set set, Map map) {
            super(327680);
            this.resolved = map;
            this.eligableMethods = set;
        }

        public void visit(int n2, int n3, String string, String string2, String string3, String[] arrstring) {
        }

        public $MethodVisitor visitMethod(int n2, String string, String string2, String string3, String[] arrstring) {
            $Signature $Signature = new $Signature(string, string2);
            if (this.eligableMethods.remove($Signature)) {
                this.currentMethod = $Signature;
                return new $MethodVisitor(327680){

                    public void visitMethodInsn(int n2, String string, String string2, String string3, boolean bl) {
                        if (n2 == 183 && BridgedFinder.this.currentMethod != null) {
                            $Signature $Signature = new $Signature(string2, string3);
                            if (!$Signature.equals(BridgedFinder.this.currentMethod)) {
                                BridgedFinder.this.resolved.put(BridgedFinder.this.currentMethod, $Signature);
                            }
                            BridgedFinder.this.currentMethod = null;
                        }
                    }
                };
            }
            return null;
        }

    }

}

