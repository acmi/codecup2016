/*
 * Decompiled with CFR 0_119.
 */
package org.w3c.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;

public interface CSSStyleRule
extends CSSRule {
    public String getSelectorText();

    public void setSelectorText(String var1) throws DOMException;

    public CSSStyleDeclaration getStyle();
}

