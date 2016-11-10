/*
 * Decompiled with CFR 0_119.
 */
import com.a.b.a.a.e.a;
import com.a.b.a.a.e.b;
import com.a.b.a.a.e.d;
import com.a.b.c;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.text.StringUtil;
import com.google.common.base.Preconditions;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.log4j.Logger;

public final class LocalTestRunner {
    public static void main(String[] arrstring) throws IOException {
        int n2;
        Logger.getRootLogger().removeAllAppenders();
        Properties properties = Preconditions.checkNotNull(LocalTestRunner.getCombinedProperties(arrstring), "Can't load properties.");
        Long l2 = LocalTestRunner.getSeed(arrstring, properties);
        MutableBoolean mutableBoolean = new MutableBoolean(LocalTestRunner.getBoolean(properties, "render-to-screen"));
        MutableBoolean mutableBoolean2 = new MutableBoolean(LocalTestRunner.getBoolean(properties, "render-to-screen-sync"));
        String string = LocalTestRunner.getRenderToScreenSize(properties);
        int n3 = LocalTestRunner.getRenderToScreenTick(properties);
        String string2 = StringUtil.trimToEmpty(properties.getProperty("results-file"));
        String string3 = StringUtil.trimToEmpty(properties.getProperty("log-file"));
        String string4 = StringUtil.trimToEmpty(properties.getProperty("replay-file"));
        int n4 = LocalTestRunner.getBaseAdapterPort(properties);
        boolean bl = LocalTestRunner.getBoolean(properties, "skills-enabled");
        String string5 = StringUtil.trimToEmpty(properties.getProperty("map"));
        string5 = Pattern.compile("[^_01-9a-zA-Z.\\-]+").matcher(string5).replaceAll("");
        if (!string5.isEmpty() && !string5.endsWith(".map")) {
            string5 = string5 + ".map";
        }
        int n5 = LocalTestRunner.getDuration(properties);
        int n6 = LocalTestRunner.getTeamSize(properties);
        int n7 = LocalTestRunner.getPlayerCount(properties);
        int n8 = LocalTestRunner.getPsychoLevel(properties);
        String string6 = LocalTestRunner.getPluginsDirectory(properties);
        String[] arrstring2 = new String[n7];
        String[] arrstring3 = new String[n7];
        String[] arrstring4 = new String[n7];
        LocalTestRunner.setupPlayers(properties, mutableBoolean, mutableBoolean2, n7, arrstring2, arrstring3, arrstring4);
        ArrayList<String> arrayList = new ArrayList<String>();
        if (n5 > 0) {
            arrayList.add("-duration=" + n5);
        }
        arrayList.add("-render-to-screen=" + mutableBoolean.booleanValue());
        arrayList.add("-render-to-screen-sync=" + mutableBoolean2.booleanValue());
        arrayList.add("-render-to-screen-size=" + string);
        if (n3 > 0) {
            arrayList.add("-render-to-screen-tick=" + n3);
        }
        arrayList.add("-results-file=" + string2);
        arrayList.add("-write-to-text-file=" + string3);
        arrayList.add("-replay-file=" + string4);
        arrayList.add("-map=" + string5);
        arrayList.add("-debug=true");
        arrayList.add("-base-adapter-port=" + n4);
        if (bl) {
            arrayList.add("-skills-enabled=true");
        }
        if (l2 != null) {
            arrayList.add("-seed=" + l2);
        }
        if (n8 > 0) {
            arrayList.add("-psycho-level=" + n8);
        }
        if (string6 != null) {
            arrayList.add("-plugins-directory=" + string6);
        }
        for (n2 = 0; n2 < n7; ++n2) {
            arrayList.add("-p" + (n2 + 1) + "-name=" + arrstring2[n2]);
            arrayList.add("-p" + (n2 + 1) + "-team-size=" + n6);
            String string7 = arrstring4[n2];
            if (!StringUtil.isNotBlank(string7)) continue;
            arrayList.add("-p" + (n2 + 1) + "-startup-command=" + string7);
        }
        for (n2 = 0; n2 < n7; ++n2) {
            arrayList.add(arrstring3[n2]);
        }
        new c(arrayList.toArray(new String[arrayList.size()])).run();
    }

    private static Properties getCombinedProperties(String[] arrstring) throws IOException {
        String string;
        Properties properties = null;
        for (int i2 = 0; i2 < arrstring.length && !StringUtil.isBlank(string = arrstring[i2]) && string.endsWith(".properties"); ++i2) {
            File file = new File(string);
            if (!file.isFile()) continue;
            Properties properties2 = new Properties();
            FileInputStream fileInputStream = new FileInputStream(file);
            Throwable throwable = null;
            try {
                properties2.load(new InputStreamReader((InputStream)fileInputStream, StandardCharsets.UTF_8));
            }
            catch (Throwable throwable2) {
                Throwable throwable3 = throwable2;
                throw throwable2;
            }
            finally {
                if (fileInputStream != null) {
                    if (throwable != null) {
                        try {
                            fileInputStream.close();
                        }
                        catch (Throwable throwable4) {
                            throwable.addSuppressed(throwable4);
                        }
                    } else {
                        fileInputStream.close();
                    }
                }
            }
            if (properties == null) {
                properties = properties2;
                continue;
            }
            for (String string2 : properties2.stringPropertyNames()) {
                if (properties.containsKey(string2)) continue;
                properties.setProperty(string2, properties2.getProperty(string2));
            }
        }
        return properties;
    }

    private static void setupPlayers(Properties properties, MutableBoolean mutableBoolean, MutableBoolean mutableBoolean2, int n2, String[] arrstring, String[] arrstring2, String[] arrstring3) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(n2);
        boolean bl = false;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3 = i2 + 1;
            String string = StringUtil.trimToEmpty(properties.getProperty("p" + n3 + "-type"));
            String string2 = StringUtil.trimToEmpty(properties.getProperty("p" + n3 + "-name"));
            String string3 = StringUtil.trimToNull(properties.getProperty("p" + n3 + "-startup-command"));
            switch (string) {
                case "Local": {
                    arrstring2[i2] = "#LocalTestPlayer";
                    if (string2.isEmpty()) {
                        string2 = "MyStrategy";
                    }
                    arrstring3[n3] = string3;
                    break;
                }
                case "Quick": {
                    arrstring2[i2] = b.class.getSimpleName() + ".class";
                    if (!string2.isEmpty()) break;
                    string2 = "QuickStartGuy";
                    break;
                }
                case "Smart": {
                    arrstring2[i2] = d.class.getSimpleName() + ".class";
                    if (!string2.isEmpty()) break;
                    string2 = "SmartGuy";
                    break;
                }
                case "Keyboard": {
                    if (bl) {
                        throw new IllegalArgumentException("Can't add two or more keyboard players.");
                    }
                    bl = true;
                    mutableBoolean.setValue(true);
                    mutableBoolean2.setValue(true);
                    arrstring2[i2] = "#KeyboardPlayer";
                    if (!string2.isEmpty()) break;
                    string2 = "KeyboardPlayer";
                    break;
                }
                default: {
                    arrstring2[i2] = a.class.getSimpleName() + ".class";
                    string2 = string2.isEmpty() ? "EmptyPlayer" : string2;
                }
            }
            Object object = (Integer)hashMap.get(string2);
            object = object == null ? 1 : object.intValue() + 1;
            hashMap.put(string2, object);
            arrstring[i2] = object.intValue() == 1 ? string2 : String.format("%s (%d)", string2, object);
        }
    }

    private static boolean getBoolean(Properties properties, String string) {
        return Boolean.parseBoolean(StringUtil.trimToEmpty(properties.getProperty(string)));
    }

    private static Long getSeed(String[] arrstring, Properties properties) {
        for (int i2 = 1; i2 < arrstring.length; ++i2) {
            try {
                return Long.valueOf(StringUtil.trimToEmpty(arrstring[i2]));
            }
            catch (NumberFormatException numberFormatException) {
                continue;
            }
        }
        try {
            return Long.valueOf(StringUtil.trimToEmpty(properties.getProperty("seed")));
        }
        catch (NumberFormatException numberFormatException) {
            return null;
        }
    }

    private static String getRenderToScreenSize(Properties properties) {
        return StringUtil.trimToEmpty(properties.getProperty("render-to-screen-size"));
    }

    private static int getRenderToScreenTick(Properties properties) {
        return LocalTestRunner.getInt(properties, "render-to-screen-tick", 0, 10000000, 0);
    }

    private static int getBaseAdapterPort(Properties properties) {
        return LocalTestRunner.getInt(properties, "base-adapter-port", 1, 65535, 31001);
    }

    private static int getDuration(Properties properties) {
        return LocalTestRunner.getInt(properties, "duration", 1000, 10000000, Integer.MIN_VALUE);
    }

    private static int getTeamSize(Properties properties) {
        return LocalTestRunner.getInt(properties, "team-size", 1, 5, 1);
    }

    private static int getPlayerCount(Properties properties) {
        return LocalTestRunner.getInt(properties, "player-count", 2, 10, 10);
    }

    private static int getPsychoLevel(Properties properties) {
        return LocalTestRunner.getInt(properties, "psycho-level", 0, 255, 0);
    }

    private static String getPluginsDirectory(Properties properties) {
        String string = properties.getProperty("plugins-directory");
        if (StringUtil.isBlank(string) || !new File(string.trim()).isDirectory()) {
            string = null;
        }
        return string;
    }

    private static int getInt(Properties properties, String string, int n2, int n3, int n4) {
        try {
            int n5 = Integer.parseInt(StringUtil.trimToEmpty(properties.getProperty(string)));
            return Math.max(Math.min(n5, n3), n2);
        }
        catch (NumberFormatException numberFormatException) {
            return n4;
        }
    }
}

