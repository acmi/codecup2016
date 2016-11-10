/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.c.k;
import com.google.gson.annotations.Until;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class j {
    private final long id;
    @Until(value=1.0)
    private final k type;
    private final int tick;
    @Until(value=1.0)
    private final Long affectedUnitId;
    @Until(value=1.0)
    private final Double x;
    @Until(value=1.0)
    private final Double y;
    @Until(value=1.0)
    private final Double angle;
    @Until(value=1.0)
    private final Map<String, Object> attributes;

    public j(long l2, k k2, int n2, Long l3, Double d2, Double d3, Double d4) {
        this.id = l2;
        this.type = k2;
        this.tick = n2;
        this.affectedUnitId = l3;
        this.x = d2;
        this.y = d3;
        this.angle = d4;
        this.attributes = null;
    }

    public j(long l2, k k2, int n2, Long l3, Double d2, Double d3, Double d4, Map<String, Object> map) {
        this.id = l2;
        this.type = k2;
        this.tick = n2;
        this.affectedUnitId = l3;
        this.x = d2;
        this.y = d3;
        this.angle = d4;
        this.attributes = new HashMap<String, Object>(map);
    }

    public long getId() {
        return this.id;
    }

    public k getType() {
        return this.type;
    }

    public int getTick() {
        return this.tick;
    }

    public Long getAffectedUnitId() {
        return this.affectedUnitId;
    }

    public Double getX() {
        return this.x;
    }

    public Double getY() {
        return this.y;
    }

    public Double getAngle() {
        return this.angle;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes == null ? null : Collections.unmodifiableMap(this.attributes);
    }

    public Object getAttribute(String string) {
        return this.attributes == null ? null : this.attributes.get(string);
    }
}

