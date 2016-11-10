/*
 * Decompiled with CFR 0_119.
 */
package com.google.thirdparty.publicsuffix;

enum PublicSuffixType {
    PRIVATE(':', ','),
    ICANN('!', '?');
    
    private final char innerNodeCode;
    private final char leafNodeCode;

    private PublicSuffixType(char c2, char c3) {
        this.innerNodeCode = c2;
        this.leafNodeCode = c3;
    }

    char getLeafNodeCode() {
        return this.leafNodeCode;
    }

    char getInnerNodeCode() {
        return this.innerNodeCode;
    }

    static PublicSuffixType fromCode(char c2) {
        for (PublicSuffixType publicSuffixType : PublicSuffixType.values()) {
            if (publicSuffixType.getInnerNodeCode() != c2 && publicSuffixType.getLeafNodeCode() != c2) continue;
            return publicSuffixType;
        }
        throw new IllegalArgumentException("No enum corresponding to given code: " + c2);
    }

    static PublicSuffixType fromIsPrivate(boolean bl) {
        return bl ? PRIVATE : ICANN;
    }
}

