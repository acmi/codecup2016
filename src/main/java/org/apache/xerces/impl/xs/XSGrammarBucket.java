/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.impl.xs.SchemaGrammar;

public class XSGrammarBucket {
    Hashtable fGrammarRegistry = new Hashtable();
    SchemaGrammar fNoNSGrammar = null;

    public SchemaGrammar getGrammar(String string) {
        if (string == null) {
            return this.fNoNSGrammar;
        }
        return (SchemaGrammar)this.fGrammarRegistry.get(string);
    }

    public void putGrammar(SchemaGrammar schemaGrammar) {
        if (schemaGrammar.getTargetNamespace() == null) {
            this.fNoNSGrammar = schemaGrammar;
        } else {
            this.fGrammarRegistry.put(schemaGrammar.getTargetNamespace(), schemaGrammar);
        }
    }

    public boolean putGrammar(SchemaGrammar schemaGrammar, boolean bl) {
        int n2;
        SchemaGrammar schemaGrammar2 = this.getGrammar(schemaGrammar.fTargetNamespace);
        if (schemaGrammar2 != null) {
            return schemaGrammar2 == schemaGrammar;
        }
        if (!bl) {
            this.putGrammar(schemaGrammar);
            return true;
        }
        Vector vector = schemaGrammar.getImportedGrammars();
        if (vector == null) {
            this.putGrammar(schemaGrammar);
            return true;
        }
        Vector vector2 = (Vector)vector.clone();
        int n3 = 0;
        while (n3 < vector2.size()) {
            SchemaGrammar schemaGrammar3 = (SchemaGrammar)vector2.elementAt(n3);
            SchemaGrammar schemaGrammar4 = this.getGrammar(schemaGrammar3.fTargetNamespace);
            if (schemaGrammar4 == null) {
                Vector vector3 = schemaGrammar3.getImportedGrammars();
                if (vector3 != null) {
                    n2 = vector3.size() - 1;
                    while (n2 >= 0) {
                        schemaGrammar4 = (SchemaGrammar)vector3.elementAt(n2);
                        if (!vector2.contains(schemaGrammar4)) {
                            vector2.addElement(schemaGrammar4);
                        }
                        --n2;
                    }
                }
            } else if (schemaGrammar4 != schemaGrammar3) {
                return false;
            }
            ++n3;
        }
        this.putGrammar(schemaGrammar);
        n2 = vector2.size() - 1;
        while (n2 >= 0) {
            this.putGrammar((SchemaGrammar)vector2.elementAt(n2));
            --n2;
        }
        return true;
    }

    public boolean putGrammar(SchemaGrammar schemaGrammar, boolean bl, boolean bl2) {
        int n2;
        if (!bl2) {
            return this.putGrammar(schemaGrammar, bl);
        }
        SchemaGrammar schemaGrammar2 = this.getGrammar(schemaGrammar.fTargetNamespace);
        if (schemaGrammar2 == null) {
            this.putGrammar(schemaGrammar);
        }
        if (!bl) {
            return true;
        }
        Vector vector = schemaGrammar.getImportedGrammars();
        if (vector == null) {
            return true;
        }
        Vector vector2 = (Vector)vector.clone();
        int n3 = 0;
        while (n3 < vector2.size()) {
            SchemaGrammar schemaGrammar3 = (SchemaGrammar)vector2.elementAt(n3);
            SchemaGrammar schemaGrammar4 = this.getGrammar(schemaGrammar3.fTargetNamespace);
            if (schemaGrammar4 == null) {
                Vector vector3 = schemaGrammar3.getImportedGrammars();
                if (vector3 != null) {
                    n2 = vector3.size() - 1;
                    while (n2 >= 0) {
                        schemaGrammar4 = (SchemaGrammar)vector3.elementAt(n2);
                        if (!vector2.contains(schemaGrammar4)) {
                            vector2.addElement(schemaGrammar4);
                        }
                        --n2;
                    }
                }
            } else {
                vector2.remove(schemaGrammar3);
            }
            ++n3;
        }
        n2 = vector2.size() - 1;
        while (n2 >= 0) {
            this.putGrammar((SchemaGrammar)vector2.elementAt(n2));
            --n2;
        }
        return true;
    }

    public SchemaGrammar[] getGrammars() {
        int n2 = this.fGrammarRegistry.size() + (this.fNoNSGrammar == null ? 0 : 1);
        SchemaGrammar[] arrschemaGrammar = new SchemaGrammar[n2];
        Enumeration enumeration = this.fGrammarRegistry.elements();
        int n3 = 0;
        while (enumeration.hasMoreElements()) {
            arrschemaGrammar[n3++] = (SchemaGrammar)enumeration.nextElement();
        }
        if (this.fNoNSGrammar != null) {
            arrschemaGrammar[n2 - 1] = this.fNoNSGrammar;
        }
        return arrschemaGrammar;
    }

    public void reset() {
        this.fNoNSGrammar = null;
        this.fGrammarRegistry.clear();
    }
}

