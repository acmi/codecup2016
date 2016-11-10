/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xpath.regex;

import java.text.CharacterIterator;
import org.apache.xerces.impl.xpath.regex.REUtil;

public class Match
implements Cloneable {
    int[] beginpos = null;
    int[] endpos = null;
    int nofgroups = 0;
    CharacterIterator ciSource = null;
    String strSource = null;
    char[] charSource = null;

    public synchronized Object clone() {
        Match match = new Match();
        if (this.nofgroups > 0) {
            match.setNumberOfGroups(this.nofgroups);
            if (this.ciSource != null) {
                match.setSource(this.ciSource);
            }
            if (this.strSource != null) {
                match.setSource(this.strSource);
            }
            int n2 = 0;
            while (n2 < this.nofgroups) {
                match.setBeginning(n2, this.getBeginning(n2));
                match.setEnd(n2, this.getEnd(n2));
                ++n2;
            }
        }
        return match;
    }

    protected void setNumberOfGroups(int n2) {
        int n3 = this.nofgroups;
        this.nofgroups = n2;
        if (n3 <= 0 || n3 < n2 || n2 * 2 < n3) {
            this.beginpos = new int[n2];
            this.endpos = new int[n2];
        }
        int n4 = 0;
        while (n4 < n2) {
            this.beginpos[n4] = -1;
            this.endpos[n4] = -1;
            ++n4;
        }
    }

    protected void setSource(CharacterIterator characterIterator) {
        this.ciSource = characterIterator;
        this.strSource = null;
        this.charSource = null;
    }

    protected void setSource(String string) {
        this.ciSource = null;
        this.strSource = string;
        this.charSource = null;
    }

    protected void setSource(char[] arrc) {
        this.ciSource = null;
        this.strSource = null;
        this.charSource = arrc;
    }

    protected void setBeginning(int n2, int n3) {
        this.beginpos[n2] = n3;
    }

    protected void setEnd(int n2, int n3) {
        this.endpos[n2] = n3;
    }

    public int getNumberOfGroups() {
        if (this.nofgroups <= 0) {
            throw new IllegalStateException("A result is not set.");
        }
        return this.nofgroups;
    }

    public int getBeginning(int n2) {
        if (this.beginpos == null) {
            throw new IllegalStateException("A result is not set.");
        }
        if (n2 < 0 || this.nofgroups <= n2) {
            throw new IllegalArgumentException("The parameter must be less than " + this.nofgroups + ": " + n2);
        }
        return this.beginpos[n2];
    }

    public int getEnd(int n2) {
        if (this.endpos == null) {
            throw new IllegalStateException("A result is not set.");
        }
        if (n2 < 0 || this.nofgroups <= n2) {
            throw new IllegalArgumentException("The parameter must be less than " + this.nofgroups + ": " + n2);
        }
        return this.endpos[n2];
    }

    public String getCapturedText(int n2) {
        if (this.beginpos == null) {
            throw new IllegalStateException("match() has never been called.");
        }
        if (n2 < 0 || this.nofgroups <= n2) {
            throw new IllegalArgumentException("The parameter must be less than " + this.nofgroups + ": " + n2);
        }
        int n3 = this.beginpos[n2];
        int n4 = this.endpos[n2];
        if (n3 < 0 || n4 < 0) {
            return null;
        }
        String string = this.ciSource != null ? REUtil.substring(this.ciSource, n3, n4) : (this.strSource != null ? this.strSource.substring(n3, n4) : new String(this.charSource, n3, n4 - n3));
        return string;
    }
}

