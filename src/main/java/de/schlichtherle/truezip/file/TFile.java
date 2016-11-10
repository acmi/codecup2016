/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.file;

import de.schlichtherle.truezip.entry.Entry;
import de.schlichtherle.truezip.file.TArchiveDetector;
import de.schlichtherle.truezip.file.TBIO;
import de.schlichtherle.truezip.file.TConfig;
import de.schlichtherle.truezip.fs.FsCompositeDriver;
import de.schlichtherle.truezip.fs.FsController;
import de.schlichtherle.truezip.fs.FsEntry;
import de.schlichtherle.truezip.fs.FsEntryName;
import de.schlichtherle.truezip.fs.FsManager;
import de.schlichtherle.truezip.fs.FsMountPoint;
import de.schlichtherle.truezip.fs.FsOutputOption;
import de.schlichtherle.truezip.fs.FsPath;
import de.schlichtherle.truezip.fs.FsScheme;
import de.schlichtherle.truezip.fs.FsUriModifier;
import de.schlichtherle.truezip.io.Paths;
import de.schlichtherle.truezip.util.BitField;
import de.schlichtherle.truezip.util.UriBuilder;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.Set;
import java.util.TreeSet;

public final class TFile
extends File {
    private static final String UNC_PREFIX = separator + separator;
    private static final Set<File> ROOTS = Collections.unmodifiableSet(new TreeSet<File>(Arrays.asList(TFile.listRoots())));
    private static final File CURRENT_DIRECTORY = new File(".");
    private transient File file;
    private transient TArchiveDetector detector;
    private transient TFile innerArchive;
    private transient TFile enclArchive;
    private transient FsEntryName enclEntryName;
    private volatile transient FsController<?> controller;

    public TFile(File file) {
        this(file, (TArchiveDetector)null);
    }

    public TFile(File file, TArchiveDetector tArchiveDetector) {
        super(file.getPath());
        if (file instanceof TFile) {
            TFile tFile = (TFile)file;
            this.file = tFile.file;
            this.detector = tFile.detector;
            this.enclArchive = tFile.enclArchive;
            this.enclEntryName = tFile.enclEntryName;
            this.innerArchive = tFile.isArchive() ? this : tFile.innerArchive;
            this.controller = tFile.controller;
        } else {
            this.file = file;
            this.detector = null != tArchiveDetector ? tArchiveDetector : TConfig.get().getArchiveDetector();
            this.scan(null);
        }
        assert (this.invariants());
    }

    public TFile(String string, TArchiveDetector tArchiveDetector) {
        super(string);
        this.file = new File(string);
        this.detector = null != tArchiveDetector ? tArchiveDetector : TConfig.get().getArchiveDetector();
        this.scan(null);
        assert (this.invariants());
    }

    public TFile(File file, String string) {
        this(file, string, null);
    }

    public TFile(File file, String string, TArchiveDetector tArchiveDetector) {
        super(file, string);
        this.file = new File(file, string);
        if (file instanceof TFile) {
            TFile tFile = (TFile)file;
            this.detector = null != tArchiveDetector ? tArchiveDetector : tFile.detector;
            this.scan(tFile);
        } else {
            this.detector = null != tArchiveDetector ? tArchiveDetector : TConfig.get().getArchiveDetector();
            this.scan(null);
        }
        assert (this.invariants());
    }

    private TFile(File file, TFile tFile, TArchiveDetector tArchiveDetector) {
        super(file.getPath());
        this.file = file;
        String string = file.getPath();
        if (null != tFile) {
            int n2 = tFile.getPath().length();
            if (string.length() == n2) {
                this.detector = tFile.detector;
                this.enclArchive = tFile.enclArchive;
                this.enclEntryName = tFile.enclEntryName;
                this.innerArchive = this;
                this.controller = tFile.controller;
            } else {
                this.detector = tArchiveDetector;
                this.innerArchive = this.enclArchive = tFile;
                try {
                    this.enclEntryName = new FsEntryName(new UriBuilder().path(string.substring(n2 + 1).replace(separatorChar, '/')).getUri(), FsUriModifier.CANONICALIZE);
                }
                catch (URISyntaxException uRISyntaxException) {
                    throw new AssertionError(uRISyntaxException);
                }
            }
        } else {
            this.detector = tArchiveDetector;
        }
        assert (this.invariants());
    }

    private void scan(TFile tFile) {
        String string = super.getPath();
        assert (tFile == null || string.startsWith(tFile.getPath()));
        assert (this.file.getPath().equals(string));
        assert (null != this.detector);
        StringBuilder stringBuilder = new StringBuilder(string.length());
        this.scan(tFile, this.detector, 0, string, stringBuilder, new Paths.Splitter(separatorChar, false));
        try {
            this.enclEntryName = 0 >= stringBuilder.length() ? null : new FsEntryName(new UriBuilder().path(stringBuilder.toString()).getUri(), FsUriModifier.CANONICALIZE);
        }
        catch (URISyntaxException uRISyntaxException) {
            throw new AssertionError(uRISyntaxException);
        }
    }

    private void scan(TFile tFile, TArchiveDetector tArchiveDetector, int n2, String string, StringBuilder stringBuilder, Paths.Splitter splitter) {
        if (string == null) {
            assert (null == this.enclArchive);
            stringBuilder.setLength(0);
            return;
        }
        splitter.split(string);
        String string2 = splitter.getParentPath();
        String string3 = splitter.getMemberName();
        if (0 != string3.length() && !".".equals(string3)) {
            if ("..".equals(string3)) {
                ++n2;
            } else if (0 < n2) {
                --n2;
            } else {
                int n3;
                if (null != tFile) {
                    int n4;
                    n3 = string.length();
                    if (n3 == (n4 = tFile.getPath().length())) {
                        this.enclArchive = tFile.innerArchive;
                        if (!tFile.isArchive()) {
                            if (tFile.isEntry()) {
                                assert (null != tFile.enclEntryName);
                                if (0 < stringBuilder.length()) {
                                    stringBuilder.insert(0, '/');
                                    stringBuilder.insert(0, tFile.enclEntryName.getPath());
                                } else {
                                    assert (this.enclArchive == tFile.enclArchive);
                                    stringBuilder.append(tFile.enclEntryName.getPath());
                                }
                            } else {
                                assert (null == this.enclArchive);
                                stringBuilder.setLength(0);
                            }
                        } else if (0 >= stringBuilder.length()) {
                            assert (this.enclArchive == tFile);
                            this.innerArchive = this;
                            this.enclArchive = tFile.enclArchive;
                            if (tFile.enclEntryName != null) {
                                stringBuilder.append(tFile.enclEntryName.getPath());
                            }
                        }
                        if (this != this.innerArchive) {
                            this.innerArchive = this.enclArchive;
                        }
                        return;
                    }
                    if (n3 < n4) {
                        tArchiveDetector = tFile.detector;
                        tFile = tFile.enclArchive;
                    }
                }
                int n5 = n3 = null != tArchiveDetector.getScheme(string) ? 1 : 0;
                if (0 < stringBuilder.length()) {
                    if (n3 != 0) {
                        this.enclArchive = new TFile(string, tArchiveDetector);
                        if (this.innerArchive != this) {
                            this.innerArchive = this.enclArchive;
                        }
                        return;
                    }
                    stringBuilder.insert(0, '/');
                    stringBuilder.insert(0, string3);
                } else {
                    if (n3 != 0) {
                        this.innerArchive = this;
                    }
                    stringBuilder.append(string3);
                }
            }
        }
        this.scan(tFile, tArchiveDetector, n2, string2, stringBuilder, splitter);
    }

    private boolean invariants() {
        File file = this.file;
        TFile tFile = this.innerArchive;
        TFile tFile2 = this.enclArchive;
        FsEntryName fsEntryName = this.enclEntryName;
        assert (null != file);
        assert (!(file instanceof TFile));
        assert (file.getPath().equals(super.getPath()));
        assert (null != this.detector);
        assert (null != tFile == (this.getInnerEntryName() != null));
        assert (null != tFile2 == (fsEntryName != null));
        assert (this != tFile2);
        assert (this == tFile ^ (tFile == tFile2 && null == this.controller));
        assert (null == tFile2 || Paths.contains(tFile2.getPath(), file.getParentFile().getPath(), separatorChar) && !fsEntryName.toString().isEmpty());
        return true;
    }

    @Override
    public String getParent() {
        return this.file.getParent();
    }

    @Override
    public TFile getParentFile() {
        File file = this.file.getParentFile();
        if (file == null) {
            return null;
        }
        TFile tFile = this.enclArchive;
        if (null != tFile && tFile.getPath().length() == file.getPath().length()) {
            assert (tFile.getPath().equals(file.getPath()));
            return tFile;
        }
        return new TFile(file, tFile, this.detector);
    }

    @Override
    public TFile getAbsoluteFile() {
        String string = this.getAbsolutePath();
        return string.equals(this.getPath()) ? this : new TFile(string, this.detector);
    }

    @Override
    public String getAbsolutePath() {
        return this.file.getAbsolutePath();
    }

    @Override
    public TFile getCanonicalFile() throws IOException {
        String string = this.getCanonicalPath();
        return string.equals(this.getPath()) ? this : new TFile(string, this.detector);
    }

    @Override
    public String getCanonicalPath() throws IOException {
        return this.file.getCanonicalPath();
    }

    @Override
    public String getPath() {
        return this.file.getPath();
    }

    @Override
    public String getName() {
        return this.file.getName();
    }

    public TArchiveDetector getArchiveDetector() {
        return this.detector;
    }

    public boolean isArchive() {
        return this == this.innerArchive;
    }

    public boolean isEntry() {
        return this.enclEntryName != null;
    }

    public TFile getInnerArchive() {
        return this.innerArchive;
    }

    public String getInnerEntryName() {
        FsEntryName fsEntryName;
        String string = this == this.innerArchive ? FsEntryName.ROOT.getPath() : (null == (fsEntryName = this.enclEntryName) ? null : fsEntryName.getPath());
        return string;
    }

    FsEntryName getInnerFsEntryName() {
        return this == this.innerArchive ? FsEntryName.ROOT : this.enclEntryName;
    }

    public TFile getEnclArchive() {
        return this.enclArchive;
    }

    public String getEnclEntryName() {
        return null == this.enclEntryName ? null : this.enclEntryName.getPath();
    }

    public TFile getTopLevelArchive() {
        TFile tFile = this.enclArchive;
        return null != tFile ? tFile.getTopLevelArchive() : this.innerArchive;
    }

    @Deprecated
    public File getFile() {
        return this.file;
    }

    FsController<?> getController() {
        FsMountPoint fsMountPoint;
        FsController fsController = this.controller;
        if (this != this.innerArchive || null != fsController) {
            return fsController;
        }
        File file = this.file;
        String string = Paths.normalize(file.getPath(), separatorChar);
        FsScheme fsScheme = this.detector.getScheme(string);
        if (null == fsScheme) {
            throw new ServiceConfigurationError("Unknown file system scheme for path \"" + string + "\"! Check run-time class path configuration.");
        }
        try {
            TFile tFile = this.enclArchive;
            FsEntryName fsEntryName = this.enclEntryName;
            assert (null != tFile == (null != fsEntryName));
            fsMountPoint = new FsMountPoint(fsScheme, null == tFile ? new FsPath(file) : new FsPath(tFile.getController().getModel().getMountPoint(), fsEntryName));
        }
        catch (URISyntaxException uRISyntaxException) {
            throw new AssertionError(uRISyntaxException);
        }
        this.controller = this.getController(fsMountPoint);
        return this.controller;
    }

    private FsController<?> getController(FsMountPoint fsMountPoint) {
        return TConfig.get().getFsManager().getController(fsMountPoint, this.detector);
    }

    @Override
    public boolean isAbsolute() {
        return this.file.isAbsolute();
    }

    @Override
    public boolean isHidden() {
        return this.file.isHidden();
    }

    @Override
    public int hashCode() {
        return this.file.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return this.file.equals(object);
    }

    @Override
    public int compareTo(File file) {
        return this.file.compareTo(file);
    }

    @Override
    public String toString() {
        return this.file.toString();
    }

    @Deprecated
    @Override
    public URL toURL() throws MalformedURLException {
        return null != this.innerArchive ? this.toURI().toURL() : this.file.toURL();
    }

    @Override
    public URI toURI() {
        try {
            if (this == this.innerArchive) {
                FsScheme fsScheme = this.getScheme();
                if (null != this.enclArchive) {
                    assert (null != this.enclEntryName);
                    return new FsMountPoint(fsScheme, new FsPath(new FsMountPoint(this.enclArchive.toURI(), FsUriModifier.CANONICALIZE), this.enclEntryName)).toUri();
                }
                return new FsMountPoint(fsScheme, new FsPath(this.file)).toUri();
            }
            if (null != this.enclArchive) {
                assert (null != this.enclEntryName);
                return new FsPath(new FsMountPoint(this.enclArchive.toURI(), FsUriModifier.CANONICALIZE), this.enclEntryName).toUri();
            }
            return this.file.toURI();
        }
        catch (URISyntaxException uRISyntaxException) {
            throw new AssertionError(uRISyntaxException);
        }
    }

    private FsScheme getScheme() {
        if (this != this.innerArchive) {
            return null;
        }
        FsController fsController = this.controller;
        if (null != fsController) {
            return fsController.getModel().getMountPoint().getScheme();
        }
        return this.detector.getScheme(this.file.getPath());
    }

    @Deprecated
    @Override
    public Path toPath() {
        throw new UnsupportedOperationException("Use a Path constructor or method instead!");
    }

    @Override
    public boolean exists() {
        if (null != this.innerArchive) {
            try {
                FsEntry fsEntry = this.innerArchive.getController().getEntry(this.getInnerFsEntryName());
                return null != fsEntry;
            }
            catch (IOException iOException) {
                return false;
            }
        }
        return this.file.exists();
    }

    @Override
    public boolean isFile() {
        if (null != this.innerArchive) {
            try {
                FsEntry fsEntry = this.innerArchive.getController().getEntry(this.getInnerFsEntryName());
                return null != fsEntry && fsEntry.isType(Entry.Type.FILE);
            }
            catch (IOException iOException) {
                return false;
            }
        }
        return this.file.isFile();
    }

    @Override
    public boolean isDirectory() {
        if (null != this.innerArchive) {
            try {
                FsEntry fsEntry = this.innerArchive.getController().getEntry(this.getInnerFsEntryName());
                return null != fsEntry && fsEntry.isType(Entry.Type.DIRECTORY);
            }
            catch (IOException iOException) {
                return false;
            }
        }
        return this.file.isDirectory();
    }

    @Override
    public boolean canRead() {
        if (null != this.innerArchive) {
            try {
                return this.innerArchive.getController().isReadable(this.getInnerFsEntryName());
            }
            catch (IOException iOException) {
                return false;
            }
        }
        return this.file.canRead();
    }

    @Override
    public boolean canWrite() {
        if (null != this.innerArchive) {
            try {
                return this.innerArchive.getController().isWritable(this.getInnerFsEntryName());
            }
            catch (IOException iOException) {
                return false;
            }
        }
        return this.file.canWrite();
    }

    @Override
    public boolean canExecute() {
        if (null != this.innerArchive) {
            try {
                return this.innerArchive.getController().isExecutable(this.getInnerFsEntryName());
            }
            catch (IOException iOException) {
                return false;
            }
        }
        return this.file.canExecute();
    }

    @Override
    public boolean setReadOnly() {
        if (null != this.innerArchive) {
            try {
                this.innerArchive.getController().setReadOnly(this.getInnerFsEntryName());
                return true;
            }
            catch (IOException iOException) {
                return false;
            }
        }
        return this.file.setReadOnly();
    }

    @Override
    public long length() {
        if (null != this.innerArchive) {
            FsEntry fsEntry;
            try {
                fsEntry = this.innerArchive.getController().getEntry(this.getInnerFsEntryName());
            }
            catch (IOException iOException) {
                return 0;
            }
            if (null == fsEntry) {
                return 0;
            }
            long l2 = fsEntry.getSize(Entry.Size.DATA);
            return -1 != l2 ? l2 : 0;
        }
        return this.file.length();
    }

    @Override
    public long lastModified() {
        if (null != this.innerArchive) {
            FsEntry fsEntry;
            try {
                fsEntry = this.innerArchive.getController().getEntry(this.getInnerFsEntryName());
            }
            catch (IOException iOException) {
                return 0;
            }
            if (null == fsEntry) {
                return 0;
            }
            long l2 = fsEntry.getTime(Entry.Access.WRITE);
            return -1 != l2 ? l2 : 0;
        }
        return this.file.lastModified();
    }

    @Override
    public boolean setLastModified(long l2) {
        if (null != this.innerArchive) {
            try {
                this.innerArchive.getController().setTime(this.getInnerFsEntryName(), BitField.of(Entry.Access.WRITE), l2, TConfig.get().getOutputPreferences());
                return true;
            }
            catch (IOException iOException) {
                return false;
            }
        }
        return this.file.setLastModified(l2);
    }

    @Override
    public String[] list() {
        if (null != this.innerArchive) {
            FsEntry fsEntry;
            try {
                fsEntry = this.innerArchive.getController().getEntry(this.getInnerFsEntryName());
            }
            catch (IOException iOException) {
                return null;
            }
            if (null == fsEntry) {
                return null;
            }
            Set<String> set = fsEntry.getMembers();
            return null == set ? null : set.toArray(new String[set.size()]);
        }
        return this.file.list();
    }

    @Override
    public String[] list(FilenameFilter filenameFilter) {
        if (null != this.innerArchive) {
            FsEntry fsEntry;
            try {
                fsEntry = this.innerArchive.getController().getEntry(this.getInnerFsEntryName());
            }
            catch (IOException iOException) {
                return null;
            }
            Set<String> set = TFile.members(fsEntry);
            if (null == set) {
                return null;
            }
            if (null == filenameFilter) {
                return set.toArray(new String[set.size()]);
            }
            ArrayList<String> arrayList = new ArrayList<String>(set.size());
            for (String string : set) {
                if (!filenameFilter.accept(this, string)) continue;
                arrayList.add(string);
            }
            return arrayList.toArray(new String[arrayList.size()]);
        }
        return this.file.list(filenameFilter);
    }

    public TFile[] listFiles() {
        return this.listFiles((FilenameFilter)null, this.detector);
    }

    public TFile[] listFiles(FilenameFilter filenameFilter) {
        return this.listFiles(filenameFilter, this.detector);
    }

    public TFile[] listFiles(FilenameFilter filenameFilter, TArchiveDetector tArchiveDetector) {
        if (null != this.innerArchive) {
            FsEntry fsEntry;
            try {
                fsEntry = this.innerArchive.getController().getEntry(this.getInnerFsEntryName());
            }
            catch (IOException iOException) {
                return null;
            }
            return this.filter(TFile.members(fsEntry), filenameFilter, tArchiveDetector);
        }
        return this.filter(TFile.list(this.file.list(filenameFilter)), (FilenameFilter)null, tArchiveDetector);
    }

    private static Set<String> members(FsEntry fsEntry) {
        return null == fsEntry ? null : fsEntry.getMembers();
    }

    private static List<String> list(String[] arrstring) {
        return null == arrstring ? null : Arrays.asList(arrstring);
    }

    private TFile[] filter(Collection<String> collection, FilenameFilter filenameFilter, TArchiveDetector tArchiveDetector) {
        if (null == collection) {
            return null;
        }
        if (null != filenameFilter) {
            ArrayList<TFile> arrayList = new ArrayList<TFile>(collection.size());
            for (String string : collection) {
                if (!filenameFilter.accept(this, string)) continue;
                arrayList.add(new TFile((File)this, string, tArchiveDetector));
            }
            return arrayList.toArray(new TFile[arrayList.size()]);
        }
        TFile[] arrtFile = new TFile[collection.size()];
        int n2 = 0;
        for (String string : collection) {
            arrtFile[n2++] = new TFile((File)this, string, tArchiveDetector);
        }
        return arrtFile;
    }

    public TFile[] listFiles(FileFilter fileFilter) {
        return this.listFiles(fileFilter, this.detector);
    }

    public TFile[] listFiles(FileFilter fileFilter, TArchiveDetector tArchiveDetector) {
        if (null != this.innerArchive) {
            FsEntry fsEntry;
            try {
                fsEntry = this.innerArchive.getController().getEntry(this.getInnerFsEntryName());
            }
            catch (IOException iOException) {
                return null;
            }
            return this.filter(TFile.members(fsEntry), fileFilter, tArchiveDetector);
        }
        return this.filter(TFile.list(this.file.list()), fileFilter, tArchiveDetector);
    }

    private TFile[] filter(Collection<String> collection, FileFilter fileFilter, TArchiveDetector tArchiveDetector) {
        if (null == collection) {
            return null;
        }
        if (null != fileFilter) {
            ArrayList<TFile> arrayList = new ArrayList<TFile>(collection.size());
            for (String string : collection) {
                TFile tFile = new TFile((File)this, string, tArchiveDetector);
                if (!fileFilter.accept(tFile)) continue;
                arrayList.add(tFile);
            }
            return arrayList.toArray(new TFile[arrayList.size()]);
        }
        TFile[] arrtFile = new TFile[collection.size()];
        int n2 = 0;
        for (String string : collection) {
            arrtFile[n2++] = new TFile((File)this, string, tArchiveDetector);
        }
        return arrtFile;
    }

    @Override
    public boolean createNewFile() throws IOException {
        if (null != this.innerArchive) {
            FsEntryName fsEntryName;
            FsController fsController = this.innerArchive.getController();
            if (null != fsController.getEntry(fsEntryName = this.getInnerFsEntryName())) {
                return false;
            }
            fsController.mknod(fsEntryName, Entry.Type.FILE, TConfig.get().getOutputPreferences().set(FsOutputOption.EXCLUSIVE), null);
            return true;
        }
        return this.file.createNewFile();
    }

    @Override
    public boolean mkdirs() {
        if (null == this.innerArchive) {
            return this.file.mkdirs();
        }
        TFile tFile = this.getParentFile();
        if (null != tFile && !tFile.exists()) {
            tFile.mkdirs();
        }
        return this.mkdir();
    }

    @Override
    public boolean mkdir() {
        if (null != this.innerArchive) {
            try {
                this.innerArchive.getController().mknod(this.getInnerFsEntryName(), Entry.Type.DIRECTORY, TConfig.get().getOutputPreferences(), null);
                return true;
            }
            catch (IOException iOException) {
                return false;
            }
        }
        return this.file.mkdir();
    }

    @Deprecated
    @Override
    public boolean delete() {
        try {
            TFile.rm(this);
            return true;
        }
        catch (IOException iOException) {
            return false;
        }
    }

    public static void rm(File file) throws IOException {
        if (file instanceof TFile) {
            TFile tFile = (TFile)file;
            if (null != tFile.innerArchive) {
                tFile.innerArchive.getController().unlink(tFile.getInnerFsEntryName(), TConfig.get().getOutputPreferences());
                return;
            }
            file = tFile.file;
        }
        if (!file.delete()) {
            throw new IOException(file + " (cannot delete)");
        }
    }

    @Override
    public void deleteOnExit() {
        if (this.innerArchive != null) {
            throw new UnsupportedOperationException();
        }
        this.file.deleteOnExit();
    }

    @Deprecated
    @Override
    public boolean renameTo(File file) {
        try {
            TFile.mv(this, file, this.detector);
            return true;
        }
        catch (IOException iOException) {
            return false;
        }
    }

    public static void mv(File file, File file2, TArchiveDetector tArchiveDetector) throws IOException {
        boolean bl;
        File file3;
        boolean bl2;
        File file4;
        if (file instanceof TFile) {
            TFile tFile = (TFile)file;
            bl = null != tFile.getInnerArchive();
            file3 = tFile.getFile();
        } else {
            bl = false;
            file3 = file;
        }
        if (file2 instanceof TFile) {
            TFile tFile = (TFile)file2;
            bl2 = null != tFile.getInnerArchive();
            file4 = tFile.getFile();
        } else {
            bl2 = false;
            file4 = file2;
        }
        if (!bl && !bl2) {
            if (file3.renameTo(file4)) {
                return;
            }
            throw new IOException(file + " (cannot rename to " + file2 + ")");
        }
        TBIO.mv(file, file2, tArchiveDetector);
    }
}

