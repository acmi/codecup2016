/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.cmdline;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Vector;
import org.apache.xalan.xsltc.cmdline.getopt.GetOpt;
import org.apache.xalan.xsltc.cmdline.getopt.GetOptsException;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;

public final class Compile {
    private static int VERSION_MAJOR = 1;
    private static int VERSION_MINOR = 4;
    private static int VERSION_DELTA = 0;

    public static void printUsage() {
        StringBuffer stringBuffer = new StringBuffer("XSLTC version " + VERSION_MAJOR + "." + VERSION_MINOR + (VERSION_DELTA > 0 ? new StringBuffer().append(".").append(VERSION_DELTA).toString() : ""));
        System.err.println(stringBuffer + "\n" + new ErrorMsg("COMPILE_USAGE_STR"));
    }

    public static void main(String[] arrstring) {
        try {
            boolean bl;
            int n2;
            boolean bl2 = false;
            boolean bl3 = false;
            boolean bl4 = false;
            GetOpt getOpt = new GetOpt(arrstring, "o:d:j:p:uxhsinv");
            if (arrstring.length < 1) {
                Compile.printUsage();
            }
            XSLTC xSLTC = new XSLTC();
            xSLTC.init();
            block13 : while ((n2 = getOpt.getNextOption()) != -1) {
                switch (n2) {
                    case 105: {
                        bl3 = true;
                        continue block13;
                    }
                    case 111: {
                        xSLTC.setClassName(getOpt.getOptionArg());
                        bl4 = true;
                        continue block13;
                    }
                    case 100: {
                        xSLTC.setDestDirectory(getOpt.getOptionArg());
                        continue block13;
                    }
                    case 112: {
                        xSLTC.setPackageName(getOpt.getOptionArg());
                        continue block13;
                    }
                    case 106: {
                        xSLTC.setJarFileName(getOpt.getOptionArg());
                        continue block13;
                    }
                    case 120: {
                        xSLTC.setDebug(true);
                        continue block13;
                    }
                    case 117: {
                        bl2 = true;
                        continue block13;
                    }
                    case 110: {
                        xSLTC.setTemplateInlining(true);
                        continue block13;
                    }
                }
                Compile.printUsage();
            }
            if (bl3) {
                if (!bl4) {
                    System.err.println(new ErrorMsg("COMPILE_STDIN_ERR"));
                }
                bl = xSLTC.compile(System.in, xSLTC.getClassName());
            } else {
                String[] arrstring2 = getOpt.getCmdArgs();
                Vector<URL> vector = new Vector<URL>();
                for (int i2 = 0; i2 < arrstring2.length; ++i2) {
                    String string = arrstring2[i2];
                    URL uRL = bl2 ? new URL(string) : new File(string).toURL();
                    vector.addElement(uRL);
                }
                bl = xSLTC.compile(vector);
            }
            if (bl) {
                xSLTC.printWarnings();
                if (xSLTC.getJarFileName() != null) {
                    xSLTC.outputToJar();
                }
            } else {
                xSLTC.printWarnings();
                xSLTC.printErrors();
            }
        }
        catch (GetOptsException getOptsException) {
            System.err.println(getOptsException);
            Compile.printUsage();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

