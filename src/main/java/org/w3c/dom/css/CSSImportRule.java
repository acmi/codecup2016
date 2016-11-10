/*
 * Decompiled with CFR 0_119.
 */
package org.w3c.dom.css;

import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.stylesheets.MediaList;

public interface CSSImportRule
extends CSSRule {
    public String getHref();

    public MediaList getMedia();

    public CSSStyleSheet getStyleSheet();
}

