/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.d;

import com.a.b.a.a.b.e.k;
import com.a.b.a.a.b.e.l;
import com.a.b.a.a.b.e.m;
import com.a.b.a.a.b.e.o;
import com.a.b.a.a.b.e.q;
import com.a.b.a.a.b.e.x;
import com.a.b.a.a.c.A;
import com.a.b.a.a.c.B;
import com.a.b.a.a.c.D;
import com.a.b.a.a.c.E;
import com.a.b.a.a.c.g;
import com.a.b.a.a.c.h;
import com.a.b.a.a.c.i;
import com.a.b.a.a.c.r;
import com.a.b.a.a.c.t;
import com.a.b.a.a.c.v;
import com.a.b.a.a.c.w;
import com.a.b.a.a.c.y;
import com.a.b.a.a.c.z;
import com.a.b.a.a.d.j;
import com.a.b.a.a.e.a.c;
import com.codeforces.commons.collection.ArrayUtil;
import com.codeforces.commons.geometry.Circle2D;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.io.FileUtil;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.math.NumberUtil;
import com.codeforces.commons.pair.DoublePair;
import com.codeforces.commons.process.ThreadUtil;
import com.codeforces.commons.reflection.MethodSignature;
import com.codeforces.commons.reflection.ReflectionUtil;
import com.codeforces.commons.text.Patterns;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;
import java.io.File;
import java.lang.invoke.LambdaMetafactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class a
implements j {
    private static final Logger a;
    private static final long[] b;
    private static final Color c;
    private static final Color d;
    private static final int e;
    private static final Color f;
    private static final int g;
    private static final int h;
    private static final int i;
    private static final int j;
    private static final int k;
    private static final Color l;
    private static final Color m;
    private static final Color n;
    private static final Color o;
    private static final Color p;
    private static final Color q;
    private static final float[] r;
    private static final float[] s;
    private static final float[] t;
    private static final double[] u;
    private static final double[] v;
    private static final int[] w;
    private static final int[] x;
    private static final Map<com.a.b.a.a.c.l, Color> y;
    private static final int[] z;
    private static final int[] A;
    private static final int[] B;
    private static final Color[] C;
    private static final Color[] D;
    private final Preferences E = Preferences.userRoot().node("CodeWizards2016/LocalRunner/UI");
    private final com.a.b.a.a.a.b F;
    private final int G;
    private final int H;
    private final AtomicLong I = new AtomicLong(Long.MIN_VALUE);
    private final ConcurrentMap<Integer, Long> J = new ConcurrentHashMap<Integer, Long>();
    private volatile double K = 1250.0;
    private volatile double L;
    private volatile double M;
    private volatile double N;
    private final Lock O = new ReentrantLock();
    private Frame P;
    private Panel Q;
    private volatile Future<?> R;
    private volatile BufferedImage S;
    private BufferedImage T;
    private BufferedImage U;
    private final BlockingQueue<e> V;
    private final d W;
    private final AtomicLong X = new AtomicLong(17);
    private final AtomicBoolean Y = new AtomicBoolean();
    private final AtomicInteger Z = new AtomicInteger();
    private final AtomicInteger aa = new AtomicInteger(2);
    private final AtomicBoolean ab = new AtomicBoolean();
    private final AtomicBoolean ac = new AtomicBoolean(true);
    private final AtomicInteger ad = new AtomicInteger();
    private final c.a ae = new c.a();
    private final ConcurrentMap<Long, ConcurrentMap<Integer, f>> af = new ConcurrentHashMap<Long, ConcurrentMap<Integer, f>>();
    private final ConcurrentMap<Long, int[][]> ag = new ConcurrentHashMap<Long, int[][]>();
    private final Random ah = new Random();
    private final ExecutorService ai;
    private final Circle2D[] aj;

    public a(com.a.b.a.a.a.b b2) {
        this.ai = Executors.newFixedThreadPool(4, new com.a.b.a.a.d.b(this));
        this.aj = new Circle2D[4096];
        this.F = b2;
        this.G = b2.o();
        this.H = b2.C();
        this.L = this.K * (double)b2.c() / (double)b2.b();
        this.W = a.a(b2);
        this.a(b2.b(), b2.c());
        this.V = new LinkedBlockingQueue<e>(b2.n() ? 1 : Math.max(300, NumberUtil.toInt((double)Runtime.getRuntime().maxMemory() / 1.073741824E9 * 2000.0)));
        new Thread(new com.a.b.a.a.d.c(this, b2)).start();
    }

    private static d a(com.a.b.a.a.a.b b2) {
        File file = b2.k();
        if (FileUtil.isDirectory(file)) {
            ArrayList<ClassLoader> arrayList = new ArrayList<ClassLoader>();
            try {
                arrayList.add(new URLClassLoader(new URL[]{file.toURI().toURL()}));
            }
            catch (MalformedURLException malformedURLException) {
                a.error("Can't convert plugins directory to URL.", malformedURLException);
            }
            arrayList.add(a.class.getClassLoader());
            Object object = a.a("LocalTestRendererListener", arrayList);
            return new c(object, null);
        }
        return new c(null, null);
    }

    private static Object a(String string, List<ClassLoader> list) {
        for (ClassLoader classLoader : list) {
            try {
                Class class_ = classLoader.loadClass("LocalTestRendererListener");
                return class_.getConstructor(new Class[0]).newInstance(new Object[0]);
            }
            catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
                a.error(String.format("Can't create an instance of %s using %s.", string, classLoader), reflectiveOperationException);
                continue;
            }
        }
        return null;
    }

    private void a(int n2, int n3) {
        this.S = new BufferedImage(n2, n3, 1);
        this.Q = new com.a.b.a.a.d.d(this);
        com.a.b.a.a.d.e e2 = new com.a.b.a.a.d.e(this);
        this.Q.setSize(n2, n3);
        this.Q.setPreferredSize(new Dimension(n2, n3));
        this.Q.setFocusTraversalKeysEnabled(false);
        this.Q.addKeyListener(e2);
        this.P = new Frame("CodeWizards 2016");
        this.d();
        this.P.addWindowListener(new com.a.b.a.a.d.f(this));
        this.P.setFocusTraversalKeysEnabled(false);
        this.P.addKeyListener(e2);
        this.f();
        this.P.add(this.Q);
        this.P.setResizable(false);
        this.P.setVisible(true);
        this.P.pack();
        this.g();
    }

    private void d() {
        if (!this.getClass().getPackage().getName().contains(Patterns.WHITESPACE_PATTERN.matcher(this.P.getTitle()).replaceAll("").toLowerCase())) {
            a.warn("Illegal frame title: '" + this.P.getTitle() + "'.");
        }
    }

    private void e() {
        try {
            GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            DisplayMode displayMode = graphicsDevice.getDisplayMode();
            int n2 = displayMode.getWidth();
            int n3 = displayMode.getHeight();
            this.E.putInt("displayWidth", n2);
            this.E.putInt("displayHeight", n3);
            Point point = this.P.getLocation();
            this.E.putInt("windowLeft", Math.max(Math.min(NumberUtil.toInt(point.getX()), n2 - 10), 0));
            this.E.putInt("windowTop", Math.max(Math.min(NumberUtil.toInt(point.getY()), n3 - 10), 0));
            try {
                this.E.flush();
            }
            catch (BackingStoreException backingStoreException) {}
        }
        catch (Throwable throwable) {
            a.error("Got unexpected throwable while saving window location.", throwable);
        }
    }

    private void f() {
        try {
            GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            DisplayMode displayMode = graphicsDevice.getDisplayMode();
            try {
                this.E.sync();
            }
            catch (BackingStoreException backingStoreException) {
                // empty catch block
            }
            int n2 = this.E.getInt("displayWidth", displayMode.getWidth());
            int n3 = this.E.getInt("displayHeight", displayMode.getHeight());
            if (n2 == displayMode.getWidth() && n3 == displayMode.getHeight()) {
                this.P.setLocation(Math.max(Math.min(this.E.getInt("windowLeft", 0), n2 - 10), 0), Math.max(Math.min(this.E.getInt("windowTop", 0), n3 - 10), 0));
            }
        }
        catch (Throwable throwable) {
            a.error("Got unexpected throwable while loading window location.", throwable);
        }
    }

    @Override
    public void a(i i2) {
        this.b(i2);
    }

    @Override
    public void close() {
        this.b(null);
    }

    private void b(i i2) {
        do {
            try {
                if (i2 == null || i2.isLastTick() || i2.getTickIndex() >= this.G) {
                    this.V.put(new e(i2, null));
                }
                return;
            }
            catch (InterruptedException interruptedException) {
                continue;
            }
            break;
        } while (true);
    }

    private void g() {
        Graphics graphics = a.a(this.S.getGraphics());
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, this.S.getWidth(), this.S.getHeight());
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Times New Roman", 1, this.a(30.0)));
        FontMetrics fontMetrics = graphics.getFontMetrics();
        String string = "Waiting for game client to connect...";
        int n2 = fontMetrics.stringWidth(string);
        graphics.drawString(string, (this.Q.getWidth() - n2) / 2, this.Q.getHeight() / 2 - fontMetrics.getHeight());
        String string2 = "\u041e\u0436\u0438\u0434\u0430\u043d\u0438\u0435 \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u044f \u0441\u0442\u0440\u0430\u0442\u0435\u0433\u0438\u0438...";
        int n3 = fontMetrics.stringWidth(string2);
        graphics.drawString(string2, (this.Q.getWidth() - n3) / 2, this.Q.getHeight() / 2);
        this.Q.repaint();
    }

    private void a(e e2) {
        this.h();
        i i2 = Preconditions.checkNotNull(e2.a());
        com.a.b.a.a.c.m m2 = k.a(0, i2.getTickCount(), this.F);
        this.e(i2);
        Future<BufferedImage> future = this.ai.submit(() -> this.h(i2));
        Future<BufferedImage> future2 = this.ai.submit(() -> this.g(i2));
        this.c(i2);
        Future future3 = this.ai.submit(() -> {
            this.d(i2);
        }
        );
        BufferedImage bufferedImage = new BufferedImage(this.S.getWidth(), this.S.getHeight(), this.S.getType());
        if (this.H > 0) {
            this.S.copyData(bufferedImage.getRaster());
        }
        Graphics graphics = a.a(bufferedImage.getGraphics());
        graphics.setColor(this.H > 0 ? com.a.a.a.a.a.a(Color.WHITE, 255 - this.H) : Color.WHITE);
        graphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        graphics.setColor(Color.BLACK);
        this.W.a(graphics, i2, m2, bufferedImage.getWidth(), bufferedImage.getHeight(), this.M, this.N, this.K, this.L);
        this.a(i2, graphics);
        if (this.aa.get() >= 2) {
            this.b(i2, graphics);
        }
        this.e(i2, graphics);
        this.f(i2, graphics);
        this.g(i2, graphics);
        this.c(i2, graphics);
        this.d(i2, graphics);
        this.h(i2, graphics);
        BufferedImage bufferedImage2 = a.a(future2);
        if (bufferedImage2 != null) {
            graphics.drawImage(bufferedImage2, 0, 0, null);
        }
        this.i(i2, graphics);
        this.j(i2, graphics);
        this.W.b(graphics, i2, m2, bufferedImage.getWidth(), bufferedImage.getHeight(), this.M, this.N, this.K, this.L);
        bufferedImage2 = a.a(future);
        if (bufferedImage2 != null) {
            graphics.drawImage(bufferedImage2, 0, 0, null);
        }
        a.a(future3);
        if (this.ac.get() && this.T != null) {
            graphics.drawImage(this.T, 0, this.S.getHeight() - this.T.getHeight(), null);
            graphics.drawImage(this.U, 0, this.S.getHeight() - this.U.getHeight(), null);
        }
        a.a(this.R);
        this.R = this.ai.submit(() -> this.Q.getGraphics().drawImage(bufferedImage, 0, 0, null));
        this.S = bufferedImage;
    }

    private void h() {
        while (this.Y.get() && this.Z.getAndDecrement() <= 0) {
            this.Z.incrementAndGet();
            ThreadUtil.sleep(Math.max(this.X.get(), 1));
        }
        this.Z.set(0);
    }

    private void c(i i2) {
        List list;
        if (this.T != null) {
            return;
        }
        Object object = i2.getSystemData();
        List list2 = list = object instanceof Map ? (List)((Map)object).get("units") : null;
        if (list == null) {
            return;
        }
        b b2 = this.a(200.0, 200.0);
        this.T = new BufferedImage(b2.a(), b2.b(), 2);
        Graphics graphics = a.a(this.T.getGraphics());
        graphics.setColor(new Color(245, 245, 245, 192));
        graphics.fillRect(0, 0, this.T.getWidth(), this.T.getHeight());
        graphics.setColor(Color.BLACK);
    }

    private void d(i i2) {
        Point2D point2D;
        if (this.U == null) {
            b b2 = this.a(200.0, 200.0);
            this.U = new BufferedImage(b2.a(), b2.b(), 2);
        }
        int n2 = this.U.getWidth();
        int n3 = this.U.getHeight();
        Graphics graphics = this.U.getGraphics();
        if (!(graphics instanceof Graphics2D)) {
            return;
        }
        Graphics2D graphics2D = (Graphics2D)a.a(graphics);
        graphics2D.setBackground(new Color(0, 0, 0, 0));
        graphics.clearRect(0, 0, n2, n3);
        double d4 = i2.getWidth();
        double d5 = i2.getHeight();
        double d6 = 1.25;
        graphics.setColor(Color.GREEN);
        for (B b3 : i2.getTreesUnsafe()) {
            double d7 = b3.getRadius() * d6;
            double d8 = 2.0 * d7;
            Point2D point2D2 = a.a(b3.getX() - d7, b3.getY() - d7, 0.0, 0.0, d4, d5, (double)n2, (double)n3);
            point2D = a.a(d8, d8, d4, d5, n2, n3);
            graphics2D.fill(new Ellipse2D.Double(point2D2.getX(), point2D2.getY(), point2D.getX(), point2D.getY()));
        }
        ArrayUtil.forEach(o2 -> {
            double d5 = o2.getRadius() * d6;
            double d6 = 2.0 * d5;
            Point2D point2D = a.a(o2.getX() - d5, o2.getY() - d5, 0.0, 0.0, d4, d5, (double)n2, (double)n3);
            Point2D point2D2 = a.a(d6, d6, d4, d5, n2, n3);
            graphics.setColor(a.a(o2.getFaction(), (E)i2));
            graphics2D.fill(new Ellipse2D.Double(point2D.getX(), point2D.getY(), point2D2.getX(), point2D2.getY()));
        }
        , i2.getBuildingsUnsafe(), i2.getMinionsUnsafe());
        B[] arrb = i2.getWizards();
        Arrays.sort(arrb, (d2, d3) -> {
            if (Math.abs(d2.getX() - d3.getX()) < Math.abs(d2.getY() - d3.getY())) {
                if (d2.getY() > d3.getY()) {
                    return 1;
                }
                if (d2.getY() < d3.getY()) {
                    return -1;
                }
            } else {
                if (d2.getX() > d3.getX()) {
                    return 1;
                }
                if (d2.getX() < d3.getX()) {
                    return -1;
                }
            }
            return Long.compare(d2.getId(), d3.getId());
        }
        );
        for (B b4 : arrb) {
            double d9 = b4.getRadius() * 1.6 * d6;
            double d10 = 2.0 * d9;
            point2D = a.a(b4.getX() - d9, b4.getY() - d9, 0.0, 0.0, d4, d5, (double)n2, (double)n3);
            Point2D point2D3 = a.a(d10, d10, d4, d5, n2, n3);
            graphics.setColor(new Color(0, 0, 0, 50));
            graphics2D.fill(new Ellipse2D.Double(point2D.getX() + 1.0, point2D.getY() + 1.0, point2D3.getX(), point2D3.getY()));
            graphics.setColor(a.a(b4.getOwnerPlayerId()));
            graphics2D.fill(new Ellipse2D.Double(point2D.getX(), point2D.getY(), point2D3.getX(), point2D3.getY()));
        }
        if (this.K < 4000.0 || this.L < 4000.0) {
            Point2D point2D4 = a.a(this.M, this.N, 0.0, 0.0, d4, d5, (double)n2, (double)n3);
            Point2D point2D5 = a.a(this.K, this.L, d4, d5, n2, n3);
            graphics.setColor(new Color(0, 0, 0, 75));
            graphics2D.draw(new Rectangle2D.Double(point2D4.getX(), point2D4.getY(), point2D5.getX(), point2D5.getY()));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void e(i var1_1) {
        block13 : {
            var2_2 = var1_1.getWizards();
            var3_3 = var2_2.length;
            if (this.I.get() != Long.MIN_VALUE) ** GOTO lbl27
            Arrays.sort(var2_2, (java.util.Comparator)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;Ljava/lang/Object;)I, a(com.a.b.a.a.c.D com.a.b.a.a.c.D ), (Lcom/a/b/a/a/c/D;Lcom/a/b/a/a/c/D;)I)());
            for (var4_4 = 0; var4_4 < var3_3; ++var4_4) {
                this.J.putIfAbsent(var4_4, var2_2[var4_4].getId());
            }
            var4_5 = null;
            var5_6 = var1_1.getDecoratedPlayerById();
            if (var5_6 == null) ** GOTO lbl19
            for (t var9_13 : var1_1.getPlayersUnsafe()) {
                var10_14 = var5_6.get(var9_13.getId());
                if (var10_14 == null || !var10_14.isKeyboardPlayer()) continue;
                var4_5 = var9_13;
                break;
            }
            if (var4_5 != null) ** GOTO lbl22
lbl19: // 2 sources:
            if (var3_3 <= 0) ** GOTO lbl27
            this.I.set(var2_2[0].getId());
            ** GOTO lbl27
lbl22: // 2 sources:
            for (var6_9 = 0; var6_9 < var3_3; ++var6_9) {
                var7_11 = var2_2[var6_9];
                if (var7_11.getOwnerPlayerId() != var4_5.getId()) continue;
                this.I.set(var7_11.getId());
                break;
            }
lbl27: // 5 sources:
            if (this.I.get() == Long.MIN_VALUE) {
                return;
            }
            var4_5 = null;
            var5_7 = 0;
            while (var5_7 < var3_3) {
                var6_8 = var2_2[var5_7];
                if (var6_8.getId() == this.I.get()) {
                    var4_5 = var6_8;
                    if (var4_5 != null) break block13;
                    return;
                }
                ++var5_7;
            }
            return;
        }
        this.O.lock();
        try {
            this.M = var4_5.getX() - this.K * 0.5;
            if (this.M < 0.0 || this.K >= var1_1.getWidth()) {
                this.M = 0.0;
            } else if (this.M + this.K > var1_1.getWidth()) {
                this.M = var1_1.getWidth() - this.K;
            }
            this.N = var4_5.getY() - this.L * 0.5;
            if (this.N < 0.0 || this.L >= var1_1.getHeight()) {
                this.N = 0.0;
                return;
            }
            if (this.N + this.L <= var1_1.getHeight()) return;
            this.N = var1_1.getHeight() - this.L;
            return;
        }
        finally {
            this.O.unlock();
        }
    }

    private void a(i i2, Graphics graphics) {
        List list;
        Object object = i2.getSystemData();
        List list2 = list = object instanceof Map ? (List)((Map)object).get("units") : null;
        if (list == null) {
            return;
        }
        for (Object e2 : list) {
            this.a(graphics, e2);
        }
        this.a(graphics, 0.0, 0.0, i2.getWidth(), 0.0);
        this.a(graphics, i2.getWidth(), 0.0, i2.getWidth(), i2.getHeight());
        this.a(graphics, i2.getWidth(), i2.getHeight(), 0.0, i2.getHeight());
        this.a(graphics, 0.0, i2.getHeight(), 0.0, 0.0);
    }

    private void a(Graphics graphics, Object object) {
    }

    private void b(i i2, Graphics graphics) {
        Color color = graphics.getColor();
        graphics.setColor(c);
        int n2 = 0;
        while ((double)n2 <= i2.getWidth()) {
            this.a(graphics, n2, 0.0, (double)n2, i2.getHeight());
            n2 = (int)((double)n2 + i2.getWidth() / 10.0);
        }
        n2 = 0;
        while ((double)n2 <= i2.getHeight()) {
            this.a(graphics, 0.0, (double)n2, i2.getWidth(), (double)n2);
            n2 = (int)((double)n2 + i2.getHeight() / 10.0);
        }
        graphics.setColor(color);
    }

    private void f(i i2) {
        a.a(this.R);
        Graphics graphics = a.a(this.S.getGraphics());
        Font font = new Font("Courier New", 1, this.a(48.0));
        t[] arrt = i2.getPlayers();
        Arrays.sort(arrt, q.b());
        int n2 = arrt.length;
        int n3 = 1;
        double d2 = n2 > 6 ? 500.0 / (double)n2 : 75.0;
        b b2 = this.b(200.0, 400.0 - 0.5 * d2 * (double)n2);
        int n4 = this.b(0.0, d2).b();
        Color color = graphics.getColor();
        for (int i3 = 0; i3 < n2; ++i3) {
            int n5;
            t t2 = arrt[i3];
            if (i3 == 0 || t2.getScore() != arrt[i3 - 1].getScore()) {
                n3 = n5 = i3 + 1;
            } else {
                n5 = n3;
            }
            String string = String.format("%d. %-20s: %d", n5, t2.getName(), t2.getScore());
            int n6 = b2.a();
            int n7 = b2.b() + n4 * i3;
            graphics.setColor(new Color(255, 255, 255, 64));
            graphics.setFont(font);
            for (int i4 = -2; i4 <= 2; ++i4) {
                for (int i5 = -2; i5 <= 2; ++i5) {
                    if (Math.abs(i4) == 2 && Math.abs(i5) == 2 || i4 == 0 && i5 == 0) continue;
                    graphics.drawString(string, n6 + i4, n7 + i5);
                }
            }
            graphics.setColor(a.a(t2.getId()));
            graphics.setFont(font);
            graphics.drawString(string, n6, n7);
        }
        graphics.setColor(color);
        this.Q.repaint();
    }

    private void c(i i2, Graphics graphics) {
        Color color = graphics.getColor();
        for (com.a.b.a.a.c.q q2 : i2.getMinionsUnsafe()) {
            this.a(i2, graphics, q2);
        }
        graphics.setColor(color);
    }

    private void a(i i2, Graphics graphics, com.a.b.a.a.c.q q2) {
        if (o.a(q2)) {
            return;
        }
        graphics.setColor(a.a(q2.getFaction(), (E)i2));
        this.b(graphics, q2.getX(), q2.getY(), q2.getRadius());
        this.a(graphics, q2);
        this.b(graphics, q2);
        this.c(graphics, q2);
        this.a(graphics, (com.a.b.a.a.c.o)q2);
        if (this.aa.get() >= 2) {
            this.a(graphics, (com.a.b.a.a.c.o)q2, 0.25);
        }
        if (this.ab.get()) {
            this.a(graphics, q2, 0.4, 0.8);
        }
    }

    private void a(Graphics graphics, com.a.b.a.a.c.q q2) {
        double d2 = 0.5;
        double d3 = q2.getRadius() / 6.0;
        double d4 = q2.getRadius() * 0.5 - d3;
        double d5 = q2.getRadius() * 0.22;
        Point2D point2D = new Point2D(q2.getX(), q2.getY()).add(new Vector2D(d4, d5).rotate(q2.getAngle()));
        this.g(graphics, point2D, d3);
        this.f(graphics, point2D, d2 * d3);
        Point2D point2D2 = new Point2D(q2.getX(), q2.getY()).add(new Vector2D(d4, - d5).rotate(q2.getAngle()));
        this.g(graphics, point2D2, d3);
        this.f(graphics, point2D2, d2 * d3);
    }

    private void b(Graphics graphics, com.a.b.a.a.c.q q2) {
        double d2 = q2.getRadius() / 3.0;
        double d3 = d2 + (1.0 - (double)q2.getLife() / (double)q2.getMaxLife()) * q2.getRadius();
        double d4 = q2.getRadius() * 0.8 - d3;
        Vector2D vector2D = new Vector2D(d4, 0.0).rotate(q2.getAngle()).add(q2.getX(), q2.getY());
        this.b(graphics, vector2D.getX(), vector2D.getY(), d3, q2.getAngle() + 0.7853981633974483 * d2 / d3, -1.5707963267948966 * d2 / d3);
    }

    private void c(Graphics graphics, com.a.b.a.a.c.q q2) {
        double d2 = q2.getRadius() / 14.0;
        double d3 = 0.5235987755982988;
        double d4 = q2.getRadius() / 3.0;
        double d5 = q2.getRadius();
        int n2 = o.b(q2) - q2.getRemainingActionCooldownTicks();
        if (q2.getRemainingActionCooldownTicks() > 0 && n2 < v.length) {
            d3 += v[n2];
        }
        Point2D point2D = new Point2D(q2.getX(), q2.getY()).add(l.a(new Vector2D(q2.getRadius() - d2, 0.0), q2.getAngle() - 1.5707963267948966));
        Point2D point2D2 = point2D.copy().add(l.a(new Vector2D(d4, 0.0), q2.getAngle() - d3));
        Point2D point2D3 = new Point2D(q2.getX(), q2.getY()).add(l.a(new Vector2D(q2.getRadius() - d2, 0.0), q2.getAngle() + 1.5707963267948966));
        Point2D point2D4 = point2D3.copy().add(l.a(new Vector2D(d4, 0.0), q2.getAngle() + d3));
        this.a(graphics, point2D, point2D2);
        this.a(graphics, point2D3, point2D4);
        if (q2.getType() == r.ORC_WOODCUTTER) {
            double d6 = -0.7853981633974483;
            Point2D point2D5 = point2D4.copy().add(l.a(new Vector2D(d5, 0.0), q2.getAngle() + d3 + d6));
            this.a(graphics, point2D4, point2D5);
            this.a(graphics, point2D5, q2.getRadius() / 2.5, q2.getAngle() + d3 + d6, -3.141592653589793);
        } else if (q2.getType() == r.FETISH_BLOWDART) {
            if (graphics instanceof Graphics2D) {
                Graphics2D graphics2D = (Graphics2D)graphics;
                graphics2D.setTransform(new AffineTransform());
                Point2D point2D6 = this.f(q2.getX(), q2.getY());
                Point2D point2D7 = this.d(q2.getRadius() * 0.8, 0.0);
                Point2D point2D8 = this.d(q2.getRadius() / 2.0, q2.getRadius() / 8.0);
                graphics2D.translate(point2D6.getX(), point2D6.getY());
                graphics2D.rotate(q2.getAngle());
                graphics2D.translate(point2D7.getX(), (- point2D8.getY()) / 2.0);
                graphics2D.fill(new Rectangle2D.Double(0.0, 0.0, point2D8.getX(), point2D8.getY()));
                graphics2D.setTransform(new AffineTransform());
            }
        } else {
            throw new IllegalArgumentException("Unsupported minion type: " + (Object)((Object)q2.getType()) + '.');
        }
    }

    private void d(i i2, Graphics graphics) {
        Color color = graphics.getColor();
        for (D d2 : i2.getWizardsUnsafe()) {
            this.a(i2, d2);
            z z2 = m.b(d2, A.HASTENED);
            if (z2 != null) {
                ConcurrentMap<Integer, f> concurrentMap = this.af.get(d2.getId());
                int n2 = z2.getWizardId() == -1 ? NumberUtil.toInt(2400.0) : 600;
                for (int i3 = Math.max((int)(i2.getTickIndex() - Math.min((int)10, (int)(n2 - z2.getRemainingDurationTicks()))), (int)0); i3 < i2.getTickIndex(); ++i3) {
                    this.a(i2, graphics, d2, concurrentMap.get(i3), (Integer)(110 - (i2.getTickIndex() - i3) * 10));
                }
            }
            this.a(i2, graphics, d2, null, null);
        }
        graphics.setColor(color);
    }

    private void a(i i2, D d2) {
        ConcurrentMap<Integer, f> concurrentMap = this.af.get(d2.getId());
        if (concurrentMap == null) {
            this.af.putIfAbsent(d2.getId(), new ConcurrentHashMap());
            concurrentMap = this.af.get(d2.getId());
        }
        concurrentMap.put(i2.getTickIndex(), new f(new Point2D(d2.getX(), d2.getY()), d2.getAngle(), null));
    }

    private void a(i i2, Graphics graphics, D d2, f f2, Integer n2) {
        if (x.a(d2)) {
            return;
        }
        Color color = a.a(d2.getOwnerPlayerId());
        if (f2 != null && n2 != null) {
            color = com.a.a.a.a.a.a(color, n2);
        }
        graphics.setColor(color);
        Point2D point2D = f2 == null ? new Point2D(d2.getX(), d2.getY()) : f2.a();
        this.g(graphics, point2D, d2.getRadius());
        if (f2 != null && n2 != null) {
            return;
        }
        z z2 = m.a(d2, A.SHIELDED);
        if (z2 != null) {
            int n3 = 5;
            double d3 = 6.283185307179586 / (double)n3 / 2.0;
            double d4 = 2.0 * (double)z2.getRemainingDurationTicks() * 0.017453292519943295;
            for (int i3 = n3 - 1; i3 >= 0; --i3) {
                this.b(graphics, point2D, d2.getRadius() * 1.1, 2.0 * d3 * (double)i3 + d4, d3);
            }
        }
        this.b(i2, graphics, d2);
        this.c(i2, graphics, d2);
        this.d(i2, graphics, d2);
        this.a(graphics, (com.a.b.a.a.c.o)d2);
        if (f2 == null && this.ab.get()) {
            this.a(graphics, i2, d2);
        }
        if (f2 == null && this.aa.get() >= 2) {
            this.a(i2, graphics, d2);
        }
        if (f2 == null && this.ab.get()) {
            this.a(graphics, d2, 0.4, 0.8);
        }
    }

    private void a(i i2, Graphics graphics, D d2) {
        double d3 = d2.getRadius();
        if (this.ab.get()) {
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font("Times New Roman", 1, this.a(0.5 * d3)));
            b b2 = this.e(d2.getX() - 0.5 * d3, d2.getY());
            for (t t2 : i2.getPlayersUnsafe()) {
                if (t2.getId() != d2.getOwnerPlayerId()) continue;
                graphics.drawString(t2.getName(), b2.a(), b2.b());
                break;
            }
        }
        this.a(graphics, (com.a.b.a.a.c.o)d2, 0.4);
        this.a(graphics, d2, 0.4);
    }

    private void b(i i2, Graphics graphics, D d2) {
        Map<Long, h> map = i2.getDecoratedWizardById();
        h h2 = map == null ? null : map.get(d2.getId());
        boolean bl = h2 != null && h2.getRemainingHitRecoverTicks() != null;
        boolean bl2 = ((long)i2.getTickIndex() + d2.getId() * 3) / 15 % 2 == 0;
        double d3 = 0.5 + 0.2 * ((double)d2.getMana() / (double)d2.getMaxMana() - 0.5);
        double d4 = d2.getRadius() / 6.0;
        double d5 = d2.getRadius() * (bl ? 0.45 : 0.5) - d4;
        double d6 = d2.getRadius() * 0.22;
        Point2D point2D = new Point2D(d2.getX(), d2.getY()).add(new Vector2D(d5, d6).rotate(d2.getAngle()));
        double d7 = bl ? (bl2 ? 1.2 : 0.8) * d4 : d4;
        this.g(graphics, point2D, d7);
        this.f(graphics, point2D, d3 * d7);
        Point2D point2D2 = new Point2D(d2.getX(), d2.getY()).add(new Vector2D(d5, - d6).rotate(d2.getAngle()));
        double d8 = bl ? (bl2 ? 0.8 : 1.2) * d4 : d4;
        this.g(graphics, point2D2, d8);
        this.f(graphics, point2D2, d3 * d8);
    }

    private void c(i i2, Graphics graphics, D d2) {
        double d3;
        double d4;
        Map<Long, h> map = i2.getDecoratedWizardById();
        h h2 = map == null ? null : map.get(d2.getId());
        boolean bl = h2 != null && h2.getRemainingHitRecoverTicks() != null;
        double d5 = d2.getRadius() / 3.0;
        if (bl) {
            d4 = d5;
            d3 = d2.getRadius() * 0.55 + d4;
        } else {
            d4 = d5 + (1.0 - (double)d2.getLife() / (double)d2.getMaxLife()) * d2.getRadius();
            d3 = d2.getRadius() * 0.8 - d4;
        }
        Vector2D vector2D = new Vector2D(d3, 0.0).rotate(d2.getAngle()).add(d2.getX(), d2.getY());
        this.b(graphics, vector2D.getX(), vector2D.getY(), d4, d2.getAngle() + 0.7853981633974483 * d5 / d4 - (bl ? 3.141592653589793 : 0.0), -1.5707963267948966 * d5 / d4);
    }

    private void d(i i2, Graphics graphics, D d2) {
        h h2;
        Map<Long, h> map = i2.getDecoratedWizardById();
        h h3 = h2 = map == null ? null : map.get(d2.getId());
        if (h2 == null) {
            return;
        }
        double d3 = d2.getRadius() / 14.0;
        double d4 = 0.5235987755982988;
        double d5 = d2.getRadius() / 3.0;
        double d6 = d2.getRadius();
        if (h2.getLastAction() != com.a.b.a.a.c.a.NONE && i2.getTickIndex() - h2.getLastActionTickIndex() < v.length) {
            d4 += v[i2.getTickIndex() - h2.getLastActionTickIndex()];
        }
        double d7 = -0.7853981633974483;
        Point2D point2D = new Point2D(d2.getX(), d2.getY()).add(l.a(new Vector2D(d2.getRadius() - d3, 0.0), d2.getAngle() - 1.5707963267948966));
        Point2D point2D2 = point2D.copy().add(l.a(new Vector2D(d5, 0.0), d2.getAngle() - d4));
        Point2D point2D3 = new Point2D(d2.getX(), d2.getY()).add(l.a(new Vector2D(d2.getRadius() - d3, 0.0), d2.getAngle() + 1.5707963267948966));
        Point2D point2D4 = point2D3.copy().add(l.a(new Vector2D(d5, 0.0), d2.getAngle() + d4));
        Point2D point2D5 = point2D4.copy().add(l.a(new Vector2D(d6, 0.0), d2.getAngle() + d4 + d7));
        this.a(graphics, point2D, point2D2);
        this.a(graphics, point2D3, point2D4);
        this.a(graphics, point2D4, point2D5);
        double d8 = 0.15;
        double d9 = 0.05;
        int[] arrn = m.c(d2, A.EMPOWERED) ? w : x;
        Color color = graphics.getColor();
        int n2 = arrn.length;
        while (--n2 >= 0) {
            graphics.setColor(com.a.a.a.a.a.a(color, arrn[n2]));
            double d10 = d8 + d9 * (double)n2;
            this.f(graphics, point2D5, d2.getRadius() * d10);
        }
        graphics.setColor(color);
    }

    private void a(Graphics graphics, i i2, D d2) {
        Set<y> set = x.a;
        int n2 = set.size();
        Preconditions.checkArgument(n2 == u.length, String.format("Unsupported ultimate skill count: expected %d, but got %d.", u.length, n2));
        Color color = graphics.getColor();
        double d3 = d2.getRadius() * 0.35;
        int n3 = 0;
        for (y y2 : set) {
            if (!x.a(d2, y2)) {
                ++n3;
                continue;
            }
            Point2D point2D = new Point2D(d2.getX(), d2.getY()).add(new Vector2D(d2.getRadius() * 1.4, 0.0).rotate(u[n3] + (double)i2.getTickIndex() * 0.017453292519943295 * 2.0 + (double)d2.getId() * 6.283185307179586 / 10.0));
            switch (y2) {
                case ADVANCED_MAGIC_MISSILE: {
                    this.a(graphics, point2D, d3);
                    break;
                }
                case FROST_BOLT: {
                    this.b(graphics, point2D, d3);
                    break;
                }
                case FIREBALL: {
                    this.c(graphics, point2D, d3);
                    break;
                }
                case HASTE: {
                    this.d(graphics, point2D, d3);
                    break;
                }
                case SHIELD: {
                    this.e(graphics, point2D, d3);
                    break;
                }
            }
            ++n3;
        }
        graphics.setColor(color);
    }

    private void a(Graphics graphics, Point2D point2D, double d2) {
        graphics.setColor(com.a.a.a.a.a.a(l, 200));
        double d3 = 2.0 * d2;
        this.a(graphics, point2D.getX() - d2, point2D.getY() - 0.6 * d2, d3, 1.2 * d2, d3, 1.2 * d2);
    }

    private void b(Graphics graphics, Point2D point2D, double d2) {
        graphics.setColor(com.a.a.a.a.a.a(m, 200));
        this.a(graphics, new Point2D(point2D.copy().subtract(0.0, d2)), new Point2D(point2D.copy().add(d2, 0.0)), new Point2D(point2D.copy().add(0.0, d2)), new Point2D(point2D.copy().subtract(d2, 0.0)));
    }

    private void c(Graphics graphics, Point2D point2D, double d2) {
        graphics.setColor(com.a.a.a.a.a.a(n, 200));
        this.f(graphics, point2D, d2);
    }

    private void d(Graphics graphics, Point2D point2D, double d2) {
        graphics.setColor(com.a.a.a.a.a.a(o, 200));
        double d3 = 2.0 * d2 * 0.25;
        double d4 = 2.0 * d2 * 0.3;
        Point2D[] arrpoint2D = new Point2D[]{new Point2D(point2D.getX() - 2.0 * d3, point2D.getY()), new Point2D(point2D.getX() - d3, point2D.getY() - d4), new Point2D(point2D.getX(), point2D.getY() - d4), new Point2D(point2D.getX() - d3, point2D.getY()), new Point2D(point2D.getX(), point2D.getY() + d4), new Point2D(point2D.getX() - d3, point2D.getY() + d4)};
        this.a(graphics, arrpoint2D);
        int n2 = arrpoint2D.length;
        while (--n2 >= 0) {
            arrpoint2D[n2].add(2.0 * d3, 0.0);
        }
        this.a(graphics, arrpoint2D);
    }

    private void e(Graphics graphics, Point2D point2D, double d2) {
        b b2 = this.e(point2D.getX() - d2, point2D.getY() - d2);
        b b3 = this.c(2.0 * d2, 2.0 * d2);
        if (b3.a() <= 1 || b3.b() <= 1) {
            return;
        }
        BufferedImage bufferedImage = new BufferedImage(b3.a(), b3.b(), 2);
        Graphics graphics2 = a.a(bufferedImage.getGraphics());
        if (graphics instanceof Graphics2D && graphics2 instanceof Graphics2D) {
            Graphics2D graphics2D = (Graphics2D)graphics2;
            graphics2D.setColor(p);
            graphics2D.fill(new Rectangle2D.Double(0.0, 0.0, b3.a(), (double)b3.b() / 2.0));
            graphics2D.fill(new RoundRectangle2D.Double(0.0, 0.0, b3.a(), b3.b(), (double)b3.a() * 0.875, (double)b3.b() * 0.875));
            Graphics2D graphics2D2 = (Graphics2D)graphics;
            Composite composite = graphics2D2.getComposite();
            graphics2D2.setComposite(AlphaComposite.getInstance(3, 0.78431374f));
            graphics.drawImage(bufferedImage, b2.a(), b2.b(), bufferedImage.getWidth(), bufferedImage.getHeight(), null);
            graphics2D2.setComposite(composite);
        }
    }

    private void e(i i2, Graphics graphics) {
        Color color = graphics.getColor();
        graphics.setColor(Color.GREEN);
        for (B b2 : i2.getTreesUnsafe()) {
            this.a(graphics, b2);
        }
        graphics.setColor(color);
    }

    private void a(Graphics graphics, B b2) {
        double d2 = Math.max(Math.min((double)b2.getLife() / (double)b2.getMaxLife(), 1.0), 0.0);
        if (d2 >= 1.0) {
            this.a(graphics, b2.getX(), b2.getY(), b2.getRadius());
        } else {
            this.b(graphics, b2.getX(), b2.getY(), b2.getRadius());
            this.a(graphics, b2.getX(), b2.getY(), b2.getRadius(), -1.5707963267948966, -6.283185307179586 * d2);
        }
    }

    private void f(i i2, Graphics graphics) {
        Color color = graphics.getColor();
        for (com.a.b.a.a.c.b b2 : i2.getBonusesUnsafe()) {
            this.a(graphics, b2);
        }
        graphics.setColor(color);
    }

    private void a(Graphics graphics, com.a.b.a.a.c.b b2) {
        switch (b2.getType()) {
            case EMPOWER: {
                this.b(graphics, b2);
                break;
            }
            case HASTE: {
                this.c(graphics, b2);
                break;
            }
            case SHIELD: {
                this.d(graphics, b2);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported bonus type: " + (Object)((Object)b2.getType()) + '.');
            }
        }
        graphics.setColor(Color.BLACK);
        this.b(graphics, b2.getX(), b2.getY(), b2.getRadius());
    }

    private void b(Graphics graphics, com.a.b.a.a.c.b b2) {
        graphics.setColor(new Color(7185109));
        double d2 = b2.getRadius() * 0.3;
        double d3 = b2.getRadius() * 0.25;
        Point2D[] arrpoint2D = new Point2D[]{new Point2D(b2.getX() - d2, b2.getY()), new Point2D(b2.getX() - d2, b2.getY() - d3), new Point2D(b2.getX(), b2.getY() - 2.0 * d3), new Point2D(b2.getX() + d2, b2.getY() - d3), new Point2D(b2.getX() + d2, b2.getY()), new Point2D(b2.getX(), b2.getY() - d3)};
        this.a(graphics, arrpoint2D);
        int n2 = arrpoint2D.length;
        while (--n2 >= 0) {
            arrpoint2D[n2].add(0.0, 2.0 * d3);
        }
        this.a(graphics, arrpoint2D);
    }

    private void c(Graphics graphics, com.a.b.a.a.c.b b2) {
        graphics.setColor(o);
        double d2 = b2.getRadius() * 0.25;
        double d3 = b2.getRadius() * 0.3;
        Point2D[] arrpoint2D = new Point2D[]{new Point2D(b2.getX() - 2.0 * d2, b2.getY()), new Point2D(b2.getX() - d2, b2.getY() - d3), new Point2D(b2.getX(), b2.getY() - d3), new Point2D(b2.getX() - d2, b2.getY()), new Point2D(b2.getX(), b2.getY() + d3), new Point2D(b2.getX() - d2, b2.getY() + d3)};
        this.a(graphics, arrpoint2D);
        int n2 = arrpoint2D.length;
        while (--n2 >= 0) {
            arrpoint2D[n2].add(2.0 * d2, 0.0);
        }
        this.a(graphics, arrpoint2D);
    }

    private void d(Graphics graphics, com.a.b.a.a.c.b b2) {
        graphics.setColor(p);
        double d2 = b2.getRadius() / 2.0;
        double d3 = b2.getRadius() / 2.0;
        this.b(graphics, b2.getX() - d2, b2.getY() - d3, 2.0 * d2, d3);
        this.a(graphics, b2.getX() - d2, b2.getY() - d3, 2.0 * d2, 2.0 * d3, d2 * 1.75, d3 * 1.75);
    }

    private void g(i i2, Graphics graphics) {
        Color color = graphics.getColor();
        for (com.a.b.a.a.c.d d2 : i2.getBuildingsUnsafe()) {
            this.a(i2, graphics, d2);
        }
        graphics.setColor(color);
    }

    private void a(i i2, Graphics graphics, com.a.b.a.a.c.d d2) {
        graphics.setColor(a.a(d2.getFaction(), (E)i2));
        double d3 = d2.getRadius();
        this.b(graphics, d2.getX(), d2.getY(), d3);
        this.b(graphics, d2.getX(), d2.getY(), d3 * 0.55);
        if (graphics instanceof Graphics2D) {
            double d4 = (double)d2.getLife() / (double)d2.getMaxLife();
            Point2D point2D = this.f(d2.getX() - 0.25 * d3 * d4, d2.getY() - 0.5 * d3);
            Point2D point2D2 = this.d(0.5 * d3 * d4, d3);
            Graphics2D graphics2D = (Graphics2D)graphics;
            graphics2D.fill(new Ellipse2D.Double(point2D.getX(), point2D.getY(), point2D2.getX(), point2D2.getY()));
        }
        if (this.aa.get() >= 2) {
            this.a(graphics, (com.a.b.a.a.c.o)d2, 0.25);
        }
        if (this.ab.get()) {
            this.a(graphics, d2, 0.35, 0.45);
        }
    }

    private void h(i i2, Graphics graphics) {
        Color color = graphics.getColor();
        for (v v2 : i2.getProjectilesUnsafe()) {
            this.a(i2, graphics, v2);
        }
        graphics.setColor(color);
    }

    private void a(i i2, Graphics graphics, v v2) {
        Color color;
        switch (v2.getType()) {
            case MAGIC_MISSILE: {
                color = l;
                break;
            }
            case FROST_BOLT: {
                color = m;
                break;
            }
            case FIREBALL: {
                color = n;
                break;
            }
            case DART: {
                color = a.a(v2.getFaction(), (E)i2);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported projectile type: " + (Object)((Object)v2.getType()) + '.');
            }
        }
        graphics.setColor(com.a.a.a.a.a.a(color, color.getAlpha() / 8));
        this.a(graphics, v2.getX() - v2.getSpeedX(), v2.getY() - v2.getSpeedY(), v2.getRadius());
        graphics.setColor(com.a.a.a.a.a.a(color, color.getAlpha() / 4));
        this.a(graphics, v2.getX() - v2.getSpeedX() / 2.0, v2.getY() - v2.getSpeedY() / 2.0, v2.getRadius());
        graphics.setColor(color);
        this.a(graphics, v2.getX(), v2.getY(), v2.getRadius());
    }

    private void a(Graphics graphics, com.a.b.a.a.c.o o2, double d2) {
        float f2;
        float[] arrf;
        double d3 = o2.getRadius();
        float f3 = (float)o2.getLife() / (float)o2.getMaxLife();
        b b2 = this.e(o2.getX() - d3, o2.getY() - (1.0 + d2) * d3 - 8.0);
        if (b2.b() < 0) {
            b2 = this.e(o2.getX() - d3, o2.getY() + (1.0 + d2) * d3 + 4.0);
        }
        b b3 = this.c(2.0 * d3 * (double)f3, 4.0);
        b b4 = this.c(2.0 * d3, 4.0);
        if (f3 >= 0.5f) {
            f2 = 1.0f - f3;
            arrf = r;
        } else {
            f2 = f3;
            arrf = t;
        }
        float f4 = f2 * f2 * 4.0f;
        float f5 = 1.0f - f4;
        graphics.setColor(new Color(s[0] * f4 + arrf[0] * f5, s[1] * f4 + arrf[1] * f5, s[2] * f4 + arrf[2] * f5, 0.7f));
        graphics.fillRect(b2.a(), b2.b(), b3.a(), b3.b());
        if (b4.a() > b3.a()) {
            graphics.setColor(q);
            graphics.fillRect(b2.a() + b3.a(), b2.b(), b4.a() - b3.a(), b4.b());
        }
    }

    private void a(Graphics graphics, D d2, double d3) {
        double d4 = d2.getRadius();
        double d5 = (double)d2.getMana() / (double)d2.getMaxMana();
        b b2 = this.e(d2.getX() - d4, d2.getY() - (1.0 + d3) * d4 - 4.0);
        if (b2.b() < 0) {
            b2 = this.e(d2.getX() - d4, d2.getY() + (1.0 + d3) * d4 + 8.0);
        }
        b b3 = this.c(2.0 * d4 * d5, 4.0);
        b b4 = this.c(2.0 * d4, 4.0);
        graphics.setColor(new Color(0.0f, 0.0f, 1.0f, 0.7f));
        graphics.fillRect(b2.a(), b2.b(), b3.a(), b3.b());
        if (b4.a() > b3.a()) {
            graphics.setColor(q);
            graphics.fillRect(b2.a() + b3.a(), b2.b(), b4.a() - b3.a(), b4.b());
        }
    }

    private void a(Graphics graphics, com.a.b.a.a.c.o o2, double d2, double d3) {
        double d4 = o2.getRadius();
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Times New Roman", 1, this.a(d2 * d4)));
        String string = "L: " + o2.getLife() + " / " + o2.getMaxLife();
        b b2 = this.e(o2.getX(), o2.getY() - (1.0 + d3 + (o2 instanceof D ? d2 : 0.0)) * d4);
        graphics.drawString(string, b2.a() - graphics.getFontMetrics().stringWidth(string) / 2, b2.b());
    }

    private void a(Graphics graphics, D d2, double d3, double d4) {
        this.a(graphics, (com.a.b.a.a.c.o)d2, d3, d4);
        double d5 = d2.getRadius();
        String string = "M: " + d2.getMana() + " / " + d2.getMaxMana();
        b b2 = this.e(d2.getX(), d2.getY() - (1.0 + d4) * d5);
        graphics.drawString(string, b2.a() - graphics.getFontMetrics().stringWidth(string) / 2, b2.b());
    }

    private void a(Graphics graphics, com.a.b.a.a.c.o o2) {
        z z2 = m.a(o2, A.FROZEN);
        if (z2 == null) {
            return;
        }
        if (!this.a(a.a(o2.getX(), o2.getY(), o2.getRadius() * 2.0))) {
            return;
        }
        Color color = graphics.getColor();
        graphics.setColor(new Color(103, 199, 246, 153));
        long l2 = o2.getId() * 1000003 + z2.getWizardId() * 100000007;
        this.a(graphics, o2, l2);
        l2 = o2.getId() * 1000000403 + z2.getWizardId() * 10000339;
        this.a(graphics, o2, l2);
        graphics.setColor(color);
    }

    private void a(Graphics graphics, com.a.b.a.a.c.o o2, long l2) {
        int n2;
        Random random = new Random(l2);
        int n3 = (6 + random.nextInt(7)) * 2;
        double[] arrd = new double[n3];
        double[] arrd2 = new double[n3];
        Point2D[] arrpoint2D = new Point2D[n3];
        double d2 = o2.getRadius();
        for (n2 = 0; n2 < n3; n2 += 2) {
            arrd[n2] = d2 * (0.75 + random.nextDouble() * 0.75);
            arrd2[n2] = 6.283185307179586 / (double)n3 * 2.0 * ((double)n2 + 0.05 + random.nextDouble() * 0.9);
        }
        for (n2 = n3 - 1; n2 > 0; n2 -= 2) {
            arrd[n2] = d2 * (0.25 + random.nextDouble() * 0.5);
            double d3 = arrd2[n2 - 1];
            double d4 = arrd2[(n2 + 1) % n3];
            arrd2[n2] = d3 + (d4 - d3) * (0.05 + random.nextDouble() * 0.9);
        }
        for (n2 = 0; n2 < n3; ++n2) {
            arrpoint2D[n2] = new Point2D(o2.getX(), o2.getY()).add(new Vector2D(arrd[n2], 0.0).rotate(arrd2[n2]));
        }
        this.a(graphics, arrpoint2D);
    }

    private BufferedImage g(i i2) {
        D d2 = a.a(i2, this.I.get());
        if (d2 == null) {
            return null;
        }
        BufferedImage bufferedImage = new BufferedImage(this.F.b(), this.F.c(), 2);
        Graphics graphics = a.a(bufferedImage.getGraphics());
        boolean bl = false;
        try {
            D[] arrd = i2.getWizardsUnsafe();
            com.a.b.a.a.c.q[] arrq = i2.getMinionsUnsafe();
            com.a.b.a.a.c.d[] arrd2 = i2.getBuildingsUnsafe();
            B[] arrb = i2.getTreesUnsafe();
            int n2 = arrd.length;
            int n3 = arrq.length;
            int n4 = arrd2.length;
            int n5 = arrb.length;
            int n6 = n2 + n3 + n4 + n5;
            com.a.b.a.a.c.o[] arro = new com.a.b.a.a.c.o[n6];
            System.arraycopy(arrd, 0, arro, 0, n2);
            System.arraycopy(arrq, 0, arro, n2, n3);
            System.arraycopy(arrd2, 0, arro, n2 + n3, n4);
            System.arraycopy(arrb, 0, arro, n2 + n3 + n4, n5);
            a.a.f.a.f f2 = new a.a.f.a.f(n6);
            int n7 = n6;
            while (--n7 >= 0) {
                f2.b(arro[n7].getId());
            }
            for (Long l2 : this.ag.keySet().toArray(new Long[this.ag.size()])) {
                if (f2.a(l2)) continue;
                this.ag.remove(l2);
            }
            bl = ((Stream)Arrays.stream(arro).parallel()).mapToInt(o2 -> this.b(graphics, o2) ? 1 : 0).max().orElse(0) == 1;
        }
        catch (Exception exception) {
            a.error("Got unexpected exception while drawing burning status.", exception);
        }
        return bl ? bufferedImage : null;
    }

    private boolean b(Graphics graphics, com.a.b.a.a.c.o o2) {
        int n2;
        int n3;
        if (!m.c(o2, A.BURNING)) {
            this.ag.remove(o2.getId());
            return false;
        }
        if (!this.a(a.a(o2.getX(), o2.getY(), o2.getRadius() * 2.0))) {
            return false;
        }
        int[][] arrn = this.ag.get(o2.getId());
        if (arrn == null) {
            arrn = new int[128][128];
            this.ag.put(o2.getId(), arrn);
        }
        int n4 = 2;
        while (--n4 >= 0) {
            int n5;
            int n6;
            int n7 = 96;
            for (n6 = 32; n6 < n7; ++n6) {
                n5 = z[n6];
                arrn[n6][n5] = this.ah.nextDouble() < 0.25 ? D.length - 1 : 0;
            }
            n7 = 127;
            for (n6 = 0; n6 < n7; ++n6) {
                n5 = A[n6];
                n2 = B[n6];
                for (n3 = n5; n3 <= n2; ++n3) {
                    arrn[n3][n6] = Math.avg(n3 > 0 ? arrn[n3 - 1][n6 + 1] : 0, arrn[n3][n6 + 1], arrn[n3][n6 + 2], n3 < 127 ? arrn[n3 + 1][n6 + 1] : 0);
                }
            }
        }
        BufferedImage bufferedImage = new BufferedImage(128, 128, 2);
        DataBuffer dataBuffer = bufferedImage.getRaster().getDataBuffer();
        if (!(dataBuffer instanceof DataBufferInt)) {
            return false;
        }
        int[] arrn2 = ((DataBufferInt)dataBuffer).getData();
        for (n2 = 0; n2 < 128; ++n2) {
            n3 = n2 * 128;
            for (int i2 = 0; i2 < 128; ++i2) {
                arrn2[n3 + i2] = D[arrn[i2][n2]].getRGB();
            }
        }
        b b2 = this.e(o2.getX() - 2.0 * o2.getRadius(), o2.getY() - 2.0 * o2.getRadius());
        b b3 = this.c(4.0 * o2.getRadius(), 4.0 * o2.getRadius());
        graphics.drawImage(bufferedImage, b2.a(), b2.b(), b3.a(), b3.b(), null);
        return true;
    }

    private void i(i i2, Graphics graphics) {
        Color color = graphics.getColor();
        com.a.b.a.a.c.j[] arrj = i2.getEffects();
        Arrays.sort(arrj, (j2, j3) -> Integer.compare(j3.getTick(), j2.getTick()));
        block4 : for (com.a.b.a.a.c.j j4 : arrj) {
            switch (j4.getType()) {
                case WIZARD_CONDITION_CHANGE: {
                    this.a(i2, graphics, j4);
                    continue block4;
                }
                case BUILDING_ATTACK: {
                    this.b(i2, graphics, j4);
                    continue block4;
                }
                default: {
                    throw new IllegalArgumentException("Unsupported effect type: " + (Object)((Object)j4.getType()) + '.');
                }
            }
        }
        graphics.setColor(color);
    }

    private void a(i i2, Graphics graphics, com.a.b.a.a.c.j j2) {
        Object object;
        Object object2;
        Object object3;
        Long l2 = j2.getAffectedUnitId();
        if (l2 == null) {
            return;
        }
        D d2 = a.a(i2, l2);
        if (d2 == null) {
            return;
        }
        double d3 = d2.getRadius();
        Font font = new Font("Times New Roman", 1, this.a((0.7 - 0.35 * (double)j2.getTick() / (double)j2.getType().getDuration()) * d3));
        graphics.setFont(font);
        double d4 = 0.45 * d3;
        double d5 = 1.25 * d3 + 10.0 + (this.ab.get() ? 0.7 * d3 : 0.0) + 1.0 * (double)j2.getTick();
        int n2 = NumberUtil.toInt(Math.round(255.0 * (1.0 - 0.75 * (double)j2.getTick() / (double)j2.getType().getDuration())));
        Integer n3 = NumberUtil.toInt(j2.getAttribute("lifeChange"));
        if (n3 != null && Math.abs(n3) > 1) {
            object2 = this.e(d2.getX() - d4, d2.getY() - d5);
            if (n3 > 0) {
                object3 = "+" + n3;
                object = Color.GREEN;
            } else {
                object3 = Integer.toString(n3);
                object = Color.RED;
            }
            graphics.setColor(com.a.a.a.a.a.a((Color)object, n2));
            graphics.drawString((String)object3, object2.a() - graphics.getFontMetrics().stringWidth((String)object3) / 2, object2.b());
        }
        if ((object2 = NumberUtil.toInt(j2.getAttribute("manaChange"))) != null && Math.abs(object2.intValue()) > 1) {
            Color color;
            object3 = this.e(d2.getX() + d4, d2.getY() - d5);
            if (object2.intValue() > 0) {
                object = "+" + object2;
                color = Color.BLUE;
            } else {
                object = Integer.toString(object2.intValue());
                color = Color.BLUE;
            }
            graphics.setColor(com.a.a.a.a.a.a(color, n2));
            graphics.drawString((String)object, object3.a() - graphics.getFontMetrics().stringWidth((String)object) / 2, object3.b());
        }
    }

    private void b(i i2, Graphics graphics, com.a.b.a.a.c.j j2) {
        Long l2 = NumberUtil.toLong(j2.getAttribute("buildingId"));
        Long l3 = NumberUtil.toLong(j2.getAttribute("targetId"));
        if (l2 == null || l3 == null) {
            return;
        }
        com.a.b.a.a.c.o o2 = a.b(i2, l2);
        com.a.b.a.a.c.o o3 = a.b(i2, l3);
        com.a.b.a.a.c.l l4 = o2 == null ? com.a.b.a.a.c.l.valueOf(j2.getAttribute("buildingFaction").toString()) : o2.getFaction();
        graphics.setColor(a.a(l4, (E)i2));
        double d2 = 11.0;
        double d3 = 7.0;
        for (double d4 = (double)j2.getTick() * 2.0 + d2 * ((double)l2.longValue() / 7.0 + (double)l3.longValue() / 101.0); d4 > 0.0; d4 -= 2.0 * d2) {
        }
        Point2D point2D = o2 == null ? new Point2D(NumberUtil.toDouble(j2.getAttribute("buildingX")), NumberUtil.toDouble(j2.getAttribute("buildingY"))) : new Point2D(o2.getX(), o2.getY());
        Point2D point2D2 = o3 == null ? new Point2D(NumberUtil.toDouble(j2.getAttribute("targetX")), NumberUtil.toDouble(j2.getAttribute("targetY"))) : new Point2D(o3.getX(), o3.getY());
        Vector2D vector2D = new Vector2D(point2D, point2D2).copy().normalize();
        Vector2D vector2D2 = vector2D.copy().multiply(d2);
        Vector2D vector2D3 = vector2D.copy().multiply(d3).rotateHalfPi();
        double d5 = point2D.getDistanceTo(point2D2);
        for (double d6 = d4; d6 < d5; d6 += 2.0 * d2) {
            Point2D point2D3 = point2D.copy().add(vector2D.copy().multiply(d6)).add(vector2D3);
            Point2D point2D4 = point2D3.copy().add(vector2D2).subtract(vector2D3).subtract(vector2D3);
            Point2D point2D5 = point2D3.copy().add(vector2D2).add(vector2D2);
            if (d6 >= - d2) {
                this.a(graphics, point2D3, point2D4);
            }
            if (d6 + d2 >= d5) continue;
            this.a(graphics, point2D4, point2D5);
        }
    }

    private static D a(E e2, long l2) {
        D[] arrd = e2.getWizardsUnsafe();
        int n2 = arrd.length;
        while (--n2 >= 0) {
            D d2 = arrd[n2];
            if (d2.getId() != l2) continue;
            return d2;
        }
        return null;
    }

    private static com.a.b.a.a.c.o b(E e2, long l2) {
        D[] arrd = e2.getWizardsUnsafe();
        int n2 = arrd.length;
        while (--n2 >= 0) {
            D d2 = arrd[n2];
            if (d2.getId() != l2) continue;
            return d2;
        }
        com.a.b.a.a.c.q[] arrq = e2.getMinionsUnsafe();
        int n3 = arrq.length;
        while (--n3 >= 0) {
            com.a.b.a.a.c.q q2 = arrq[n3];
            if (q2.getId() != l2) continue;
            return q2;
        }
        com.a.b.a.a.c.d[] arrd2 = e2.getBuildingsUnsafe();
        int n4 = arrd2.length;
        while (--n4 >= 0) {
            com.a.b.a.a.c.d d3 = arrd2[n4];
            if (d3.getId() != l2) continue;
            return d3;
        }
        B[] arrb = e2.getTreesUnsafe();
        int n5 = arrb.length;
        while (--n5 >= 0) {
            B b2 = arrb[n5];
            if (b2.getId() != l2) continue;
            return b2;
        }
        return null;
    }

    private void j(i i2, Graphics graphics) {
        int n2;
        int n3;
        if (this.aa.get() < 1) {
            return;
        }
        graphics.setFont(new Font("Courier New", 1, this.a(15.0)));
        t[] arrt = i2.getPlayers();
        Arrays.sort(arrt, (t2, t3) -> Long.valueOf(t2.getId()).compareTo(t3.getId()));
        if (arrt.length == 10) {
            n2 = 5;
            n3 = 2;
        } else {
            n2 = 2;
            n3 = 4;
        }
        for (int i3 = 0; i3 < n3; ++i3) {
            double d2 = 20.0 + 250.0 * (double)i3;
            for (int i4 = 0; i4 < n2; ++i4) {
                double d3 = 30.0 + 16.0 * (double)i4;
                if (arrt.length <= i4 + i3 * n2) continue;
                this.a(graphics, d2, d3, arrt[i4 + i3 * n2]);
            }
        }
        b b2 = this.b(1070.0, 770.0);
        String string = "" + i2.getTickIndex() + " / " + i2.getTickCount();
        if (string.length() < 20) {
            string = Strings.repeat(" ", 20 - string.length()) + string;
        }
        graphics.drawString(string, b2.a(), b2.b());
        b b3 = this.b(1070.0, 30.0);
        String string2 = "Speed: " + this.i();
        if (string2.length() < 20) {
            string2 = Strings.repeat(" ", 20 - string2.length()) + string2;
        }
        graphics.drawString(string2, b3.a(), b3.b());
        b b4 = this.b(1070.0, 55.0);
        String string3 = "FPS: " + this.ad.get();
        if (string3.length() < 20) {
            string3 = Strings.repeat(" ", 20 - string3.length()) + string3;
        }
        graphics.drawString(string3, b4.a(), b4.b());
    }

    private String i() {
        long l2 = this.X.get();
        int n2 = Arrays.binarySearch(b, l2);
        switch (n2) {
            case 0: {
                return "benchmark";
            }
            case 1: {
                return "fast forward";
            }
            case 2: {
                return "very fast";
            }
            case 3: {
                return "fast";
            }
            case 4: {
                return "normal";
            }
            case 5: {
                return "slow";
            }
            case 6: {
                return "very slow";
            }
            case 7: {
                return "slideshow";
            }
        }
        throw new IllegalStateException(String.format("Illegal current screen interval index %d for interval %d ms.", n2, l2));
    }

    private void a(Graphics graphics, double d2, double d3, t t2) {
        Color color = graphics.getColor();
        b b2 = this.b(d2, d3);
        Object[] arrobject = new Object[2];
        arrobject[0] = (t2.isStrategyCrashed() ? "? " : "") + t2.getName();
        arrobject[1] = t2.getScore();
        String string = String.format("%-20s: %d", arrobject);
        graphics.setColor(new Color(0, 0, 0, 25));
        graphics.drawString(string, b2.a() + 1, b2.b() + 1);
        graphics.setColor(a.a(t2.getId()));
        graphics.drawString(string, b2.a(), b2.b());
        graphics.setColor(color);
    }

    private BufferedImage h(i i2) {
        D d2 = a.a(i2, this.I.get());
        if (d2 == null) {
            return null;
        }
        BufferedImage bufferedImage = new BufferedImage(this.F.b(), this.F.c(), 2);
        Graphics graphics = bufferedImage.getGraphics();
        if (!(graphics instanceof Graphics2D)) {
            return null;
        }
        Graphics2D graphics2D = (Graphics2D)a.a(graphics);
        DataBuffer dataBuffer = bufferedImage.getRaster().getDataBuffer();
        if (dataBuffer instanceof DataBufferInt) {
            this.a(i2, d2, (DataBufferInt)dataBuffer);
        } else {
            this.a(i2, d2, bufferedImage, graphics2D);
        }
        return bufferedImage;
    }

    private void a(i i2, D d2, DataBufferInt dataBufferInt) {
        double[] arrd;
        double d5;
        double d3;
        int n6;
        Object object;
        int n3;
        reference var24_19;
        int n4;
        double d4;
        int n5;
        int n2;
        int n7 = this.F.b();
        int n8 = this.F.c();
        Circle2D[] arrcircle2D = this.a(i2, d2.getFaction());
        int n9 = arrcircle2D.length;
        Object[] arrobject = new DoublePair[n9];
        int[] arrn = dataBufferInt.getData();
        Arrays.fill(arrn, e);
        double d6 = this.K / (double)n7;
        double d7 = this.L / (double)n8;
        for (n3 = 0; n3 < n8; ++n3) {
            d4 = this.N + ((double)n3 + 0.5) * d7;
            n6 = 0;
            n4 = n9;
            while (--n4 >= 0) {
                arrd = arrcircle2D[n4].getXs(d4, 1.0E-7);
                if (arrd.length == 2) {
                    arrobject[n6++] = new DoublePair((double)arrd[0], (double)arrd[1]);
                    continue;
                }
                if (arrd.length != 1) continue;
                object = arrd[0];
                arrobject[n6++] = new DoublePair((double)object, (double)object);
            }
            if (n6 <= 0) continue;
            Arrays.sort(arrobject, 0, n6);
            n4 = 0;
            while (n4 < n6) {
                arrd = arrobject[n4++];
                object = arrd.getFirst();
                d3 = arrd.getSecond();
                while (n4 < n6 && (arrd = arrobject[n4]).getFirst() < d3) {
                    d3 = Math.max(d3, arrd.getSecond());
                    ++n4;
                }
                var24_19 = (object - this.M) / d6;
                d5 = (d3 - this.M) / d6;
                n2 = Math.max(NumberUtil.toInt(Math.ceil((double)var24_19)), 0);
                n5 = Math.min(NumberUtil.toInt(Math.floor(d5)), n7);
                if (n5 <= n2) continue;
                int n10 = n3 * n7;
                Arrays.fill(arrn, n10 + n2, n10 + n5, g);
                if (n5 < n7 && d5 > (double)n5) {
                    a.a(arrn, n10 + n5, d5 - (double)n5);
                }
                if (n2 <= 0 || var24_19 >= (double)n2) continue;
                a.a(arrn, n10 + n2 - 1, (double)n2 - var24_19);
            }
        }
        for (n3 = 0; n3 < n7; ++n3) {
            d4 = this.M + ((double)n3 + 0.5) * d6;
            n6 = 0;
            n4 = n9;
            while (--n4 >= 0) {
                arrd = arrcircle2D[n4].getYs(d4, 1.0E-7);
                if (arrd.length == 2) {
                    arrobject[n6++] = new DoublePair((double)arrd[0], (double)arrd[1]);
                    continue;
                }
                if (arrd.length != 1) continue;
                object = arrd[0];
                arrobject[n6++] = new DoublePair((double)object, (double)object);
            }
            if (n6 <= 0) continue;
            Arrays.sort(arrobject, 0, n6);
            n4 = 0;
            while (n4 < n6) {
                arrd = arrobject[n4++];
                object = arrd.getFirst();
                d3 = arrd.getSecond();
                while (n4 < n6 && (arrd = arrobject[n4]).getFirst() < d3) {
                    d3 = Math.max(d3, arrd.getSecond());
                    ++n4;
                }
                var24_19 = (object - this.N) / d7;
                d5 = (d3 - this.N) / d7;
                n2 = Math.max(NumberUtil.toInt(Math.ceil((double)var24_19)), 0);
                n5 = Math.min(NumberUtil.toInt(Math.floor(d5)), n8);
                if (n5 <= n2) continue;
                if (n5 < n8 && d5 > (double)n5) {
                    a.a(arrn, n5 * n7 + n3, d5 - (double)n5);
                }
                if (n2 <= 0 || var24_19 >= (double)n2) continue;
                a.a(arrn, (n2 - 1) * n7 + n3, (double)n2 - var24_19);
            }
        }
    }

    private static void a(int[] arrn, int n2, double d2) {
        Color color = new Color(arrn[n2], true);
        if (!color.equals(f)) {
            Preconditions.checkArgument(d2 >= 0.0 && d2 <= 1.0);
            int n3 = NumberUtil.toInt(Math.round((1.0 - d2) * (double)color.getAlpha() + d2 * (double)h));
            int n4 = NumberUtil.toInt(Math.round((1.0 - d2) * (double)color.getRed() + d2 * (double)i));
            int n5 = NumberUtil.toInt(Math.round((1.0 - d2) * (double)color.getGreen() + d2 * (double)j));
            int n6 = NumberUtil.toInt(Math.round((1.0 - d2) * (double)color.getBlue() + d2 * (double)k));
            arrn[n2] = new Color(n4, n5, n6, n3).getRGB();
        }
    }

    private void a(i i2, D d2, BufferedImage bufferedImage, Graphics2D graphics2D) {
        graphics2D.setColor(d);
        graphics2D.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        graphics2D.setComposite(AlphaComposite.getInstance(1));
        graphics2D.setColor(f);
        for (D d3 : i2.getWizardsUnsafe()) {
            if (d2.getFaction() != d3.getFaction()) continue;
            this.a((Graphics)graphics2D, d3.getX(), d3.getY(), 600.0);
        }
        for (D d3 : i2.getBuildingsUnsafe()) {
            if (d2.getFaction() != d3.getFaction()) continue;
            double d4 = d3.getType() == com.a.b.a.a.c.e.FACTION_BASE ? 800.0 : 600.0;
            this.a((Graphics)graphics2D, d3.getX(), d3.getY(), d4);
        }
        for (D d5 : i2.getMinionsUnsafe()) {
            if (d2.getFaction() != d5.getFaction()) continue;
            this.a((Graphics)graphics2D, d5.getX(), d5.getY(), 400.0);
        }
    }

    private Circle2D[] a(i i2, com.a.b.a.a.c.l l2) {
        int n2 = 0;
        D[] arrd = i2.getWizardsUnsafe();
        int n3 = arrd.length;
        while (--n3 >= 0) {
            D d2 = arrd[n3];
            if (l2 != d2.getFaction()) continue;
            this.aj[n2++] = new Circle2D(new Point2D(d2.getX(), d2.getY()), 600.0);
        }
        com.a.b.a.a.c.d[] arrd2 = i2.getBuildingsUnsafe();
        int n4 = arrd2.length;
        while (--n4 >= 0) {
            com.a.b.a.a.c.d d3 = arrd2[n4];
            if (l2 != d3.getFaction()) continue;
            double d4 = d3.getType() == com.a.b.a.a.c.e.FACTION_BASE ? 800.0 : 600.0;
            this.aj[n2++] = new Circle2D(new Point2D(d3.getX(), d3.getY()), d4);
        }
        com.a.b.a.a.c.q[] arrq = i2.getMinionsUnsafe();
        int n5 = arrq.length;
        while (--n5 >= 0) {
            com.a.b.a.a.c.q q2 = arrq[n5];
            if (l2 != q2.getFaction()) continue;
            this.aj[n2++] = new Circle2D(new Point2D(q2.getX(), q2.getY()), 400.0);
        }
        Circle2D[] arrcircle2D = new Circle2D[n2];
        System.arraycopy(this.aj, 0, arrcircle2D, 0, n2);
        return arrcircle2D;
    }

    private int a(double d2) {
        b b2 = new b(d2 * (double)this.F.b() / 1280.0, d2 * (double)this.F.c() / 800.0, false, null);
        return Math.min(b2.a(), b2.b());
    }

    private b a(double d2, double d3) {
        double d4 = Math.min((double)this.F.b() / 1280.0, (double)this.F.c() / 800.0);
        return new b(d2 * d4, d3 * d4, null);
    }

    private b b(double d2, double d3) {
        return new b(d2 * (double)this.F.b() / 1280.0, d3 * (double)this.F.c() / 800.0, null);
    }

    private b c(double d2, double d3) {
        return a.a(d2, d3, 0.0, 0.0, this.K, this.L, this.Q.getWidth(), this.Q.getHeight());
    }

    private Point2D d(double d2, double d3) {
        return a.a(d2, d3, 0.0, 0.0, this.K, this.L, (double)this.Q.getWidth(), (double)this.Q.getHeight());
    }

    private static Point2D a(double d2, double d3, double d4, double d5, int n2, int n3) {
        return a.a(d2, d3, 0.0, 0.0, d4, d5, (double)n2, (double)n3);
    }

    private b e(double d2, double d3) {
        return a.a(d2, d3, this.M, this.N, this.K, this.L, this.Q.getWidth(), this.Q.getHeight());
    }

    private static b a(double d2, double d3, double d4, double d5, double d6, double d7, int n2, int n3) {
        return new b((d2 - d4) * (double)n2 / d6, (d3 - d5) * (double)n3 / d7, null);
    }

    private Point2D f(double d2, double d3) {
        return a.a(d2, d3, this.M, this.N, this.K, this.L, (double)this.Q.getWidth(), (double)this.Q.getHeight());
    }

    private static Point2D a(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        return new Point2D((d2 - d4) * d8 / d6, (d3 - d5) * d9 / d7);
    }

    private void a(Graphics graphics, double d2, double d3, double d4, double d5) {
        if (!this.a(a.a(d2, d3, d4, d5))) {
            return;
        }
        if (graphics instanceof Graphics2D) {
            Point2D point2D = this.f(d2, d3);
            Point2D point2D2 = this.f(d4, d5);
            Graphics2D graphics2D = (Graphics2D)graphics;
            graphics2D.draw(new Line2D.Double(point2D.getX(), point2D.getY(), point2D2.getX(), point2D2.getY()));
        } else {
            b b2 = this.e(d2, d3);
            b b3 = this.e(d4, d5);
            graphics.drawLine(b2.a(), b2.b(), b3.a(), b3.b());
        }
    }

    private void a(Graphics graphics, Point2D point2D, Point2D point2D2) {
        this.a(graphics, point2D.getX(), point2D.getY(), point2D2.getX(), point2D2.getY());
    }

    private void a(Graphics graphics, double d2, double d3, double d4) {
        if (!this.a(a.a(d2, d3, d4))) {
            return;
        }
        if (graphics instanceof Graphics2D) {
            Point2D point2D = this.f(d2 - d4, d3 - d4);
            Point2D point2D2 = this.d(2.0 * d4, 2.0 * d4);
            Graphics2D graphics2D = (Graphics2D)graphics;
            graphics2D.fill(new Ellipse2D.Double(point2D.getX(), point2D.getY(), point2D2.getX(), point2D2.getY()));
        } else {
            b b2 = this.e(d2 - d4, d3 - d4);
            b b3 = this.e(d2 + d4, d3 + d4).a(b2);
            graphics.fillOval(b2.a(), b2.b(), b3.a(), b3.b());
        }
    }

    private void f(Graphics graphics, Point2D point2D, double d2) {
        this.a(graphics, point2D.getX(), point2D.getY(), d2);
    }

    private void b(Graphics graphics, double d2, double d3, double d4) {
        if (!this.a(a.a(d2, d3, d4))) {
            return;
        }
        if (graphics instanceof Graphics2D) {
            Point2D point2D = this.f(d2 - d4, d3 - d4);
            Point2D point2D2 = this.d(2.0 * d4, 2.0 * d4);
            Graphics2D graphics2D = (Graphics2D)graphics;
            graphics2D.draw(new Ellipse2D.Double(point2D.getX(), point2D.getY(), point2D2.getX(), point2D2.getY()));
        } else {
            b b2 = this.e(d2 - d4, d3 - d4);
            b b3 = this.e(d2 + d4, d3 + d4).a(b2);
            graphics.drawOval(b2.a(), b2.b(), b3.a(), b3.b());
        }
    }

    private void g(Graphics graphics, Point2D point2D, double d2) {
        this.b(graphics, point2D.getX(), point2D.getY(), d2);
    }

    private void a(Graphics graphics, Point2D point2D, double d2, double d3, double d4) {
        this.a(graphics, point2D.getX(), point2D.getY(), d2, d3, d4);
    }

    private void a(Graphics graphics, double d2, double d3, double d4, double d5, double d6) {
        if (!this.a(a.a(d2, d3, d4))) {
            return;
        }
        if (graphics instanceof Graphics2D) {
            Point2D point2D = this.f(d2 - d4, d3 - d4);
            Point2D point2D2 = this.d(2.0 * d4, 2.0 * d4);
            ((Graphics2D)graphics).fill(new Arc2D.Double(point2D.getX(), point2D.getY(), point2D2.getX(), point2D2.getY(), (- d5) * 57.29577951308232, (- d6) * 57.29577951308232, 2));
        } else {
            b b2 = this.e(d2 - d4, d3 - d4);
            b b3 = this.e(d2 + d4, d3 + d4).a(b2);
            graphics.fillArc(b2.a(), b2.b(), b3.a(), b3.b(), NumberUtil.toInt(Math.round((- d5) * 57.29577951308232)), NumberUtil.toInt(Math.round((- d6) * 57.29577951308232)));
        }
    }

    private void b(Graphics graphics, Point2D point2D, double d2, double d3, double d4) {
        this.b(graphics, point2D.getX(), point2D.getY(), d2, d3, d4);
    }

    private void b(Graphics graphics, double d2, double d3, double d4, double d5, double d6) {
        if (!this.a(a.a(d2, d3, d4))) {
            return;
        }
        if (graphics instanceof Graphics2D) {
            Point2D point2D = this.f(d2 - d4, d3 - d4);
            Point2D point2D2 = this.d(2.0 * d4, 2.0 * d4);
            ((Graphics2D)graphics).draw(new Arc2D.Double(point2D.getX(), point2D.getY(), point2D2.getX(), point2D2.getY(), (- d5) * 57.29577951308232, (- d6) * 57.29577951308232, 0));
        } else {
            b b2 = this.e(d2 - d4, d3 - d4);
            b b3 = this.e(d2 + d4, d3 + d4).a(b2);
            graphics.drawArc(b2.a(), b2.b(), b3.a(), b3.b(), NumberUtil.toInt(Math.round((- d5) * 57.29577951308232)), NumberUtil.toInt(Math.round((- d6) * 57.29577951308232)));
        }
    }

    private void b(Graphics graphics, double d2, double d3, double d4, double d5) {
        if (!this.a(a.b(d2, d3, d4, d5))) {
            return;
        }
        if (graphics instanceof Graphics2D) {
            Point2D point2D = this.f(d2, d3);
            Point2D point2D2 = this.d(d4, d5);
            Graphics2D graphics2D = (Graphics2D)graphics;
            graphics2D.fill(new Rectangle2D.Double(point2D.getX(), point2D.getY(), point2D2.getX(), point2D2.getY()));
        } else {
            b b2 = this.e(d2, d3);
            b b3 = this.e(d2 + d4, d3 + d5).a(b2);
            graphics.fillRect(b2.a(), b2.b(), b3.a(), b3.b());
        }
    }

    private void a(Graphics graphics, double d2, double d3, double d4, double d5, double d6, double d7) {
        if (!this.a(a.b(d2, d3, d4, d5))) {
            return;
        }
        if (graphics instanceof Graphics2D) {
            Point2D point2D = this.f(d2, d3);
            Point2D point2D2 = this.d(d4, d5);
            Point2D point2D3 = this.d(d6, d7);
            ((Graphics2D)graphics).fill(new RoundRectangle2D.Double(point2D.getX(), point2D.getY(), point2D2.getX(), point2D2.getY(), point2D3.getX(), point2D3.getY()));
        } else {
            b b2 = this.e(d2, d3);
            b b3 = this.e(d2 + d4, d3 + d5).a(b2);
            b b4 = this.c(d6, d7);
            graphics.fillRoundRect(b2.a(), b2.b(), b3.a(), b3.b(), b4.a(), b4.b());
        }
    }

    private /* varargs */ void a(Graphics graphics, Point2D ... arrpoint2D) {
        int n2 = arrpoint2D.length;
        if (n2 < 3) {
            throw new IllegalArgumentException("Length of the argument 'gamePoints' is less than 3.");
        }
        int[] arrn = new int[n2];
        int[] arrn2 = new int[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            Point2D point2D = arrpoint2D[i2];
            b b2 = this.e(point2D.getX(), point2D.getY());
            arrn[i2] = b2.a();
            arrn2[i2] = b2.b();
        }
        graphics.fillPolygon(arrn, arrn2, n2);
    }

    private boolean a(a a2) {
        return a2.c() >= this.M && a2.a() <= this.M + this.K && a2.d() >= this.N && a2.b() <= this.N + this.L;
    }

    private static a a(double d2, double d3, double d4, double d5) {
        return new a(Math.min(d2, d4), Math.min(d3, d5), Math.max(d2, d4), Math.max(d3, d5), null);
    }

    private static a a(double d2, double d3, double d4) {
        return new a(d2 - d4, d3 - d4, d2 + d4, d3 + d4, null);
    }

    private static a b(double d2, double d3, double d4, double d5) {
        return new a(d2, d3, d2 + d4, d3 + d5, null);
    }

    private static Graphics a(Graphics graphics) {
        if (graphics instanceof Graphics2D) {
            Graphics2D graphics2D = (Graphics2D)graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        }
        return graphics;
    }

    private static Color a(long l2) {
        switch (NumberUtil.toInt(l2)) {
            case 1: {
                return com.a.a.a.a.a.a();
            }
            case 2: {
                Color color = com.a.a.a.a.a.e();
                return new Color(Math.max(NumberUtil.toInt((double)color.getRed() * 0.93), 0), Math.max(NumberUtil.toInt((double)color.getGreen() * 0.93), 0), Math.max(NumberUtil.toInt((double)color.getBlue() * 0.93), 0), color.getAlpha());
            }
            case 3: {
                return com.a.a.a.a.a.f();
            }
            case 4: {
                return com.a.a.a.a.a.g();
            }
            case 5: {
                return com.a.a.a.a.a.d().brighter();
            }
            case 6: {
                return com.a.a.a.a.a.b();
            }
            case 7: {
                return com.a.a.a.a.a.c();
            }
            case 8: {
                return com.a.a.a.a.a.i();
            }
            case 9: {
                return com.a.a.a.a.a.h();
            }
            case 10: {
                return com.a.a.a.a.a.j().brighter();
            }
        }
        throw new IllegalArgumentException("Can't get color for Player {id=" + l2 + "}.");
    }

    private static Color a(com.a.b.a.a.c.l l2, E e2) {
        Color color = y.get((Object)l2);
        if (color == null) {
            color = com.a.a.a.a.a.a((Color[])Arrays.stream(e2.getPlayersUnsafe()).filter(t2 -> t2.getFaction() == l2).map(t2 -> a.a(t2.getId())).toArray(n2 -> new Color[n2]));
            if (color == null) {
                color = Color.BLACK;
            }
            y.put(l2, color);
        }
        return color;
    }

    private static <R> R a(Future<R> future) {
        if (future != null) {
            try {
                return future.get();
            }
            catch (InterruptedException | ExecutionException exception) {
                // empty catch block
            }
        }
        return null;
    }

    public c.a a() {
        return this.ae;
    }

    private static /* synthetic */ int a(D d2, D d3) {
        return Long.compare(d2.getId(), d3.getId());
    }

    static /* synthetic */ AtomicLong a(a a2) {
        return a2.X;
    }

    static /* synthetic */ AtomicInteger b(a a2) {
        return a2.ad;
    }

    static /* synthetic */ void a(a a2, e e2) {
        a2.a(e2);
    }

    static /* synthetic */ void a(a a2, i i2) {
        a2.f(i2);
    }

    static /* synthetic */ void c(a a2) {
        a2.e();
    }

    static /* synthetic */ BlockingQueue d(a a2) {
        return a2.V;
    }

    static /* synthetic */ BufferedImage e(a a2) {
        return a2.S;
    }

    static /* synthetic */ AtomicBoolean f(a a2) {
        return a2.Y;
    }

    static /* synthetic */ AtomicBoolean g(a a2) {
        return a2.ac;
    }

    static /* synthetic */ AtomicInteger h(a a2) {
        return a2.Z;
    }

    static /* synthetic */ AtomicInteger i(a a2) {
        return a2.aa;
    }

    static /* synthetic */ long[] c() {
        return b;
    }

    static /* synthetic */ AtomicBoolean j(a a2) {
        return a2.ab;
    }

    static /* synthetic */ Lock k(a a2) {
        return a2.O;
    }

    static /* synthetic */ double l(a a2) {
        return a2.K;
    }

    static /* synthetic */ double m(a a2) {
        return a2.L;
    }

    static /* synthetic */ double a(a a2, double d2) {
        a2.K = d2;
        return a2.K;
    }

    static /* synthetic */ double b(a a2, double d2) {
        a2.L = d2;
        return a2.L;
    }

    static /* synthetic */ com.a.b.a.a.a.b n(a a2) {
        return a2.F;
    }

    static /* synthetic */ ConcurrentMap o(a a2) {
        return a2.J;
    }

    static /* synthetic */ AtomicLong p(a a2) {
        return a2.I;
    }

    static /* synthetic */ c.a q(a a2) {
        return a2.ae;
    }

    static /* synthetic */ double r(a a2) {
        return a2.M;
    }

    static /* synthetic */ double c(a a2, double d2) {
        a2.M = d2;
        return a2.M;
    }

    static /* synthetic */ double s(a a2) {
        return a2.N;
    }

    static /* synthetic */ double d(a a2, double d2) {
        a2.N = d2;
        return a2.N;
    }

    static {
        double[] arrd;
        double d2;
        int n3;
        a = LoggerFactory.getLogger(a.class);
        b = new long[]{0, 8, 10, 13, 17, 34, 67, 134};
        c = new Color(0, 0, 0, 17);
        d = new Color(0, 0, 0, 125);
        e = d.getRGB();
        f = new Color(255, 255, 255, 0);
        g = f.getRGB();
        h = f.getAlpha();
        i = f.getRed();
        j = f.getGreen();
        k = f.getBlue();
        l = com.a.a.a.a.a.m();
        m = com.a.a.a.a.a.l();
        n = com.a.a.a.a.a.k();
        o = new Color(14764067);
        p = new Color(6606627);
        q = new Color(0, 0, 0, 34);
        r = Color.GREEN.getRGBColorComponents(null);
        s = Color.YELLOW.getRGBColorComponents(null);
        t = Color.RED.getRGBColorComponents(null);
        u = new double[]{1.5707963267948966, 0.3141592653589793, 2.827433388230814, -0.9424777960769379, -2.199114857512855};
        v = IntStream.of(15, 30, 45, 60, 45, 30, 15, 0, -15).mapToDouble(n2 -> (double)n2 * 0.017453292519943295).toArray();
        w = new int[]{255, 191, 127, 95, 63, 47, 31, 23};
        x = new int[]{255, 127, 63, 31};
        y = Collections.synchronizedMap(new EnumMap(com.a.b.a.a.c.l.class));
        z = new int[128];
        A = new int[128];
        B = new int[128];
        double d3 = 32.0;
        Object object = new Circle2D(new Point2D(2.0 * d3, 2.0 * d3), d3);
        for (n3 = 0; n3 < 128; ++n3) {
            d2 = (double)n3 + 0.5;
            arrd = object.getYs(d2, 1.0E-7);
            a.z[n3] = arrd.length == 0 ? 64 : NumberUtil.toInt(Math.floor(arrd[1]));
        }
        for (n3 = 0; n3 < 64; ++n3) {
            a.A[n3] = 0;
            a.B[n3] = 127;
        }
        for (n3 = 64; n3 < 128; ++n3) {
            d2 = (double)n3 + 0.5;
            arrd = object.getXs(d2, 1.0E-7);
            if (arrd.length == 0) {
                a.A[n3] = 128;
                a.B[n3] = -1;
                continue;
            }
            a.A[n3] = NumberUtil.toInt(Math.floor(arrd[0]));
            a.B[n3] = NumberUtil.toInt(Math.floor(arrd[1]));
        }
        C = new Color[]{new Color(0, 0, 0, 0), new Color(222, 51, 5, 255), new Color(240, 118, 9, 255), new Color(254, 182, 28, 255), new Color(254, 227, 27, 255), new Color(254, 254, 254, 255)};
        D = new Color[20 * (C.length - 1) + 1];
        for (int i2 = 0; i2 < C.length - 1; ++i2) {
            Color color = C[i2];
            object = C[i2 + 1];
            for (n3 = 0; n3 < 20; ++n3) {
                int n4 = NumberUtil.toInt(Math.round((double)color.getRed() + (double)((object.getRed() - color.getRed()) * n3) / 20.0));
                int n5 = NumberUtil.toInt(Math.round((double)color.getGreen() + (double)((object.getGreen() - color.getGreen()) * n3) / 20.0));
                int n6 = NumberUtil.toInt(Math.round((double)color.getBlue() + (double)((object.getBlue() - color.getBlue()) * n3) / 20.0));
                int n7 = NumberUtil.toInt(Math.round((double)color.getAlpha() + (double)((object.getAlpha() - color.getAlpha()) * n3) / 20.0));
                a.D[i2 * 20 + n3] = new Color(n4, n5, n6, n7);
            }
        }
        a.D[a.D.length - 1] = C[C.length - 1];
    }

    private static final class c
    implements d {
        private final Object a;
        private volatile MethodSignature b;
        private volatile Method c;
        private final AtomicInteger d = new AtomicInteger();
        private volatile MethodSignature e;
        private volatile Method f;
        private final AtomicInteger g = new AtomicInteger();
        private volatile E h;
        private volatile com.a.b.a.a.c.m i;
        private volatile Object j;
        private volatile Object k;

        private c(Object object) {
            this.a = object;
            if (this.a == null) {
                return;
            }
            Map<MethodSignature, Method> map = ReflectionUtil.getPublicMethodBySignatureMap(object.getClass());
            for (Map.Entry<MethodSignature, Method> entry : map.entrySet()) {
                MethodSignature methodSignature = entry.getKey();
                List list = methodSignature.getParameterTypes();
                if ("beforeDrawScene".equals(methodSignature.getName()) && list.size() == 9 && list.get(0) == Graphics.class && "World".equals(list.get(1).getSimpleName()) && list.get(1).getPackage() != null && "model".equals(list.get(1).getPackage().getName()) && "Game".equals(list.get(2).getSimpleName()) && list.get(2).getPackage() != null && "model".equals(list.get(2).getPackage().getName()) && list.get(3) == Integer.TYPE && list.get(4) == Integer.TYPE && list.get(5) == Double.TYPE && list.get(6) == Double.TYPE && list.get(7) == Double.TYPE && list.get(8) == Double.TYPE) {
                    this.b = methodSignature;
                    this.c = entry.getValue();
                    continue;
                }
                if (!"afterDrawScene".equals(methodSignature.getName()) || list.size() != 9 || list.get(0) != Graphics.class || !"World".equals(list.get(1).getSimpleName()) || list.get(1).getPackage() == null || !"model".equals(list.get(1).getPackage().getName()) || !"Game".equals(list.get(2).getSimpleName()) || list.get(2).getPackage() == null || !"model".equals(list.get(2).getPackage().getName()) || list.get(3) != Integer.TYPE || list.get(4) != Integer.TYPE || list.get(5) != Double.TYPE || list.get(6) != Double.TYPE || list.get(7) != Double.TYPE || list.get(8) != Double.TYPE) continue;
                this.e = methodSignature;
                this.f = entry.getValue();
            }
        }

        @Override
        public void a(Graphics graphics, E e2, com.a.b.a.a.c.m m2, int n2, int n3, double d2, double d3, double d4, double d5) {
            if (this.c == null || this.d.get() > 10) {
                return;
            }
            try {
                List list = this.b.getParameterTypes();
                Object object = e2 == this.h ? this.j : c.a(e2, list.get(1));
                Object object2 = m2 == this.i ? this.k : c.a(m2, list.get(2));
                this.h = e2;
                this.i = m2;
                this.j = object;
                this.k = object2;
                this.c.invoke(this.a, graphics, object, object2, n2, n3, d2, d3, d4, d5);
            }
            catch (ReflectiveOperationException | RuntimeException exception) {
                this.d.incrementAndGet();
                a.error("Can't invoke beforeDrawScene(...) method of custom renderer.", exception);
            }
        }

        @Override
        public void b(Graphics graphics, E e2, com.a.b.a.a.c.m m2, int n2, int n3, double d2, double d3, double d4, double d5) {
            if (this.f == null || this.g.get() > 10) {
                return;
            }
            try {
                List list = this.e.getParameterTypes();
                Object object = e2 == this.h ? this.j : c.a(e2, list.get(1));
                Object object2 = m2 == this.i ? this.k : c.a(m2, list.get(2));
                this.h = e2;
                this.i = m2;
                this.j = object;
                this.k = object2;
                this.f.invoke(this.a, graphics, object, object2, n2, n3, d2, d3, d4, d5);
            }
            catch (ReflectiveOperationException | RuntimeException exception) {
                this.g.incrementAndGet();
                a.error("Can't invoke afterDrawScene(...) method of custom renderer.", exception);
            }
        }

        private static Object a(Object object, Class<?> class_) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return com.a.a.a.a.d.a(object, class_);
        }

        /* synthetic */ c(Object object, com.a.b.a.a.d.b b2) {
            this(object);
        }
    }

    private static final class b {
        private int a;
        private int b;

        private b(double d2, double d3, boolean bl) {
            if (bl) {
                this.a = NumberUtil.toInt(Math.round(d2));
                this.b = NumberUtil.toInt(Math.round(d3));
            } else {
                this.a = NumberUtil.toInt(d2);
                this.b = NumberUtil.toInt(d3);
            }
        }

        private b(double d2, double d3) {
            this(d2, d3, true);
        }

        private b() {
        }

        public int a() {
            return this.a;
        }

        public int b() {
            return this.b;
        }

        public b a(b b2) {
            this.a -= b2.a;
            this.b -= b2.b;
            return this;
        }

        /* synthetic */ b(double d2, double d3, boolean bl, com.a.b.a.a.d.b b2) {
            this(d2, d3, bl);
        }

        /* synthetic */ b(double d2, double d3, com.a.b.a.a.d.b b2) {
            this(d2, d3);
        }
    }

    private static interface d {
        public void a(Graphics var1, E var2, com.a.b.a.a.c.m var3, int var4, int var5, double var6, double var8, double var10, double var12);

        public void b(Graphics var1, E var2, com.a.b.a.a.c.m var3, int var4, int var5, double var6, double var8, double var10, double var12);
    }

    private static final class e {
        private final i a;

        private e(i i2) {
            this.a = i2;
        }

        public i a() {
            return this.a;
        }

        /* synthetic */ e(i i2, com.a.b.a.a.d.b b2) {
            this(i2);
        }
    }

    private static final class a {
        private final double a;
        private final double b;
        private final double c;
        private final double d;

        private a(double d2, double d3, double d4, double d5) {
            this.a = d2;
            this.b = d3;
            this.c = d4;
            this.d = d5;
        }

        public double a() {
            return this.a;
        }

        public double b() {
            return this.b;
        }

        public double c() {
            return this.c;
        }

        public double d() {
            return this.d;
        }

        /* synthetic */ a(double d2, double d3, double d4, double d5, com.a.b.a.a.d.b b2) {
            this(d2, d3, d4, d5);
        }
    }

    private static final class f {
        private final Point2D a;
        private final double b;

        private f(Point2D point2D, double d2) {
            this.a = point2D;
            this.b = d2;
        }

        public Point2D a() {
            return this.a;
        }

        /* synthetic */ f(Point2D point2D, double d2, com.a.b.a.a.d.b b2) {
            this(point2D, d2);
        }
    }

}

