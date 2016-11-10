/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.cmdline.getopt;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.apache.xalan.xsltc.cmdline.getopt.IllegalArgumentException;
import org.apache.xalan.xsltc.cmdline.getopt.MissingOptArgException;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;

public class GetOpt {
    private Option theCurrentOption = null;
    private ListIterator theOptionsIterator;
    private List theOptions = new ArrayList();
    private List theCmdArgs = null;
    private OptionMatcher theOptionMatcher = null;

    public GetOpt(String[] arrstring, String string) {
        int n2;
        String string2;
        int n3 = 0;
        this.theCmdArgs = new ArrayList();
        this.theOptionMatcher = new OptionMatcher(string);
        for (n2 = 0; n2 < arrstring.length; ++n2) {
            int n4;
            string2 = arrstring[n2];
            int n5 = string2.length();
            if (string2.equals("--")) {
                n3 = n2 + 1;
                break;
            }
            if (string2.startsWith("-") && n5 == 2) {
                this.theOptions.add(new Option(string2.charAt(1)));
                continue;
            }
            if (string2.startsWith("-") && n5 > 2) {
                for (n4 = 1; n4 < n5; ++n4) {
                    this.theOptions.add(new Option(string2.charAt(n4)));
                }
                continue;
            }
            if (string2.startsWith("-")) continue;
            if (this.theOptions.size() == 0) {
                n3 = n2;
                break;
            }
            n4 = 0;
            n4 = this.theOptions.size() - 1;
            Option option = (Option)this.theOptions.get(n4);
            char c2 = option.getArgLetter();
            if (!option.hasArg() && this.theOptionMatcher.hasArg(c2)) {
                option.setArg(string2);
                continue;
            }
            n3 = n2;
            break;
        }
        this.theOptionsIterator = this.theOptions.listIterator();
        for (n2 = n3; n2 < arrstring.length; ++n2) {
            string2 = arrstring[n2];
            this.theCmdArgs.add(string2);
        }
    }

    public void printOptions() {
        ListIterator listIterator = this.theOptions.listIterator();
        while (listIterator.hasNext()) {
            Option option = (Option)listIterator.next();
            System.out.print("OPT =" + option.getArgLetter());
            String string = option.getArgument();
            if (string != null) {
                System.out.print(" " + string);
            }
            System.out.println();
        }
    }

    public int getNextOption() throws IllegalArgumentException, MissingOptArgException {
        int n2 = -1;
        if (this.theOptionsIterator.hasNext()) {
            this.theCurrentOption = (Option)this.theOptionsIterator.next();
            char c2 = this.theCurrentOption.getArgLetter();
            boolean bl = this.theOptionMatcher.hasArg(c2);
            String string = this.theCurrentOption.getArgument();
            if (!this.theOptionMatcher.match(c2)) {
                ErrorMsg errorMsg = new ErrorMsg("ILLEGAL_CMDLINE_OPTION_ERR", new Character(c2));
                throw new IllegalArgumentException(errorMsg.toString());
            }
            if (bl && string == null) {
                ErrorMsg errorMsg = new ErrorMsg("CMDLINE_OPT_MISSING_ARG_ERR", new Character(c2));
                throw new MissingOptArgException(errorMsg.toString());
            }
            n2 = c2;
        }
        return n2;
    }

    public String getOptionArg() {
        String string = null;
        String string2 = this.theCurrentOption.getArgument();
        char c2 = this.theCurrentOption.getArgLetter();
        if (this.theOptionMatcher.hasArg(c2)) {
            string = string2;
        }
        return string;
    }

    public String[] getCmdArgs() {
        String[] arrstring = new String[this.theCmdArgs.size()];
        int n2 = 0;
        ListIterator listIterator = this.theCmdArgs.listIterator();
        while (listIterator.hasNext()) {
            arrstring[n2++] = (String)listIterator.next();
        }
        return arrstring;
    }

    static class OptionMatcher {
        private String theOptString = null;

        public OptionMatcher(String string) {
            this.theOptString = string;
        }

        public boolean match(char c2) {
            boolean bl = false;
            if (this.theOptString.indexOf(c2) != -1) {
                bl = true;
            }
            return bl;
        }

        public boolean hasArg(char c2) {
            boolean bl = false;
            int n2 = this.theOptString.indexOf(c2) + 1;
            if (n2 == this.theOptString.length()) {
                bl = false;
            } else if (this.theOptString.charAt(n2) == ':') {
                bl = true;
            }
            return bl;
        }
    }

    static class Option {
        private char theArgLetter;
        private String theArgument = null;

        public Option(char c2) {
            this.theArgLetter = c2;
        }

        public void setArg(String string) {
            this.theArgument = string;
        }

        public boolean hasArg() {
            return this.theArgument != null;
        }

        public char getArgLetter() {
            return this.theArgLetter;
        }

        public String getArgument() {
            return this.theArgument;
        }
    }

}

