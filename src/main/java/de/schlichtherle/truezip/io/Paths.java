/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.io;

import java.io.File;
import java.util.Locale;

public final class Paths {
    private Paths() {
    }

    public static String normalize(String string, char c2) {
        return new Normalizer(c2).normalize(string);
    }

    public static int prefixLength(String string, char c2, boolean bl) {
        int n2 = string.length();
        if (n2 <= 0) {
            return 0;
        }
        char c3 = string.charAt(0);
        if ('\\' == File.separatorChar) {
            if (2 <= n2 && ':' == string.charAt(1) && ('a' <= c3 && c3 <= 'z' || 'A' <= c3 && c3 <= 'Z')) {
                return 3 <= n2 && c2 == string.charAt(2) ? 3 : 2;
            }
            if (c2 == c3) {
                if (2 <= n2 && c2 == string.charAt(1)) {
                    if (!bl) {
                        return 2;
                    }
                    int n3 = string.indexOf(c2, 2) + 1;
                    if (0 == n3) {
                        return n2;
                    }
                    int n4 = string.indexOf(c2, n3) + 1;
                    if (0 == n4) {
                        return n2;
                    }
                    return n4;
                }
                return 1;
            }
            return 0;
        }
        return c2 == c3 ? 1 : 0;
    }

    public static boolean contains(String string, String string2, char c2) {
        int n2;
        if ('\\' == File.separatorChar) {
            string = string.toLowerCase(Locale.getDefault());
            string2 = string2.toLowerCase(Locale.getDefault());
        }
        if (!string2.startsWith(string)) {
            return false;
        }
        int n3 = string.length();
        if (n3 == (n2 = string2.length())) {
            return true;
        }
        if (n3 < n2) {
            return string2.charAt(n3) == c2;
        }
        return false;
    }

    public static class Splitter {
        private final char separatorChar;
        private final int fixum;
        private String parentPath;
        private String memberName;

        public Splitter(char c2, boolean bl) {
            this.separatorChar = c2;
            this.fixum = bl ? 1 : 0;
        }

        public Splitter split(String string) {
            int n2;
            int n3 = Paths.prefixLength(string, this.separatorChar, false);
            if (n3 > (n2 = string.length() - 1)) {
                this.parentPath = null;
                this.memberName = "";
                return this;
            }
            n2 = Splitter.lastIndexNot(string, this.separatorChar, n2);
            int n4 = string.lastIndexOf(this.separatorChar, n2) + 1;
            if (n3 >= ++n2) {
                this.parentPath = null;
                this.memberName = "";
            } else if (n3 >= n4) {
                this.parentPath = 0 >= n3 ? null : string.substring(0, n3);
                this.memberName = string.substring(n3, n2);
            } else {
                int n5 = Splitter.lastIndexNot(string, this.separatorChar, n4 - 1) + 1;
                if (n3 >= n5) {
                    this.parentPath = string.substring(0, n3);
                    this.memberName = string.substring(n4, n2);
                } else {
                    this.parentPath = string.substring(0, n5 + this.fixum);
                    this.memberName = string.substring(n4, n2);
                }
            }
            return this;
        }

        private static int lastIndexNot(String string, char c2, int n2) {
            while (c2 == string.charAt(n2) && --n2 >= 0) {
            }
            return n2;
        }

        public String getParentPath() {
            return this.parentPath;
        }

        public String getMemberName() {
            return this.memberName;
        }
    }

    public static class Normalizer {
        private final char separatorChar;
        private final StringBuilder builder = new StringBuilder();
        private String path;

        public Normalizer(char c2) {
            this.separatorChar = c2;
        }

        public String normalize(String string) {
            String string2;
            int n2 = Paths.prefixLength(string, this.separatorChar, false);
            int n3 = string.length();
            this.path = string.substring(n2, n3);
            this.builder.setLength(0);
            this.builder.ensureCapacity(n3);
            this.normalize(0, n3 - n2);
            this.builder.insert(0, string.substring(0, n2));
            int n4 = this.builder.length();
            if (n3 > 0 && string.charAt(n3 - 1) == this.separatorChar || n3 > 1 && string.charAt(n3 - 2) == this.separatorChar && string.charAt(n3 - 1) == '.') {
                this.slashify();
                n4 = this.builder.length();
            }
            if (n4 == string.length()) {
                assert (string.equals(this.builder.toString()));
                string2 = string;
            } else {
                string2 = this.builder.toString();
                if (string.startsWith(string2)) {
                    string2 = string.substring(0, n4);
                }
            }
            assert (!string2.equals(string) || string2 == string);
            return string2;
        }

        private int normalize(int n2, int n3) {
            int n4;
            assert (n2 >= 0);
            if (0 >= n3) {
                return n2;
            }
            int n5 = this.path.lastIndexOf(this.separatorChar, n3 - 1);
            String string = this.path.substring(n5 + 1, n3);
            if (0 >= string.length() || ".".equals(string)) {
                return this.normalize(n2, n5);
            }
            if ("..".equals(string)) {
                n4 = this.normalize(n2 + 1, n5) - 1;
                if (0 > n4) {
                    return 0;
                }
            } else {
                if (0 < n2) {
                    int n6 = this.normalize(n2 - 1, n5);
                    this.slashify();
                    return n6;
                }
                assert (0 == n2);
                n4 = this.normalize(0, n5);
                assert (0 == n4);
            }
            this.slashify();
            this.builder.append(string);
            return n4;
        }

        private void slashify() {
            int n2 = this.builder.length();
            if (n2 > 0 && this.builder.charAt(n2 - 1) != this.separatorChar) {
                this.builder.append(this.separatorChar);
            }
        }
    }

}

