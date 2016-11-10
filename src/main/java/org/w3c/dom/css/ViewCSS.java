/*
 * Decompiled with CFR 0_119.
 */
package org.w3c.dom.css;

import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.views.AbstractView;

public interface ViewCSS
extends AbstractView {
    public CSSStyleDeclaration getComputedStyle(Element var1, String var2);
}

