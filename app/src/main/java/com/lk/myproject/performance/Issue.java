package com.lk.myproject.performance;

import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Issue {
    private static String TAG = "_Issue";
    public static final int TYPE_UI_BLOCK = 0;
    public static final int TYPE_FPS = 1;
    public static final int TYPE_IPC = 2;
    public static final int TYPE_THREAD = 3;
    /** @deprecated */
    @Deprecated
    public static final int TYPE_HANDLER = 4;
    private static volatile ExecutorService taskService = Executors.newSingleThreadExecutor();
    private static SimpleDateFormat dateFormat;
    protected int type = -1;
    protected String msg = "";
    protected String createTime = "";
    protected Object data;
    protected byte[] dataBytes;
    private static String ISSUES_CACHE_DIR_NAME;
    private static File ISSUES_CACHE_DIR;
    private static PERF.IssueSupplier gIssueSupplier;
    private static final int MAX_CACHE_SIZE = 10485760;
    private static final int BUFFER_SIZE = 1048576;
    private static File gLogFile;
    private static RandomAccessFile gRandomAccessFile;
    private static MappedByteBuffer gMappedByteBuffer;
    private static byte[] gLineBytes;
    private static final int gLineBytesLength;
    private static final String LINE_FORMAT;

    public Issue(int type, String msg, Object data) {
        this.type = type;
        this.msg = msg;
        this.createTime = dateFormat.format(new Date());
        this.data = data;
    }

    public int getType() {
        return this.type;
    }

    public String getMsg() {
        return this.msg;
    }

    public Object getData() {
        return this.data;
    }

    protected String typeToString() {
        String str = null;
        switch(this.type) {
            case 0:
                str = "UI BLOCK";
                break;
            case 1:
                str = "FPS";
                break;
            case 2:
                str = "IPC";
                break;
            case 3:
                str = "THREAD";
                break;
            default:
                str = "NONE";
        }

        return str;
    }

    private void buildIssueString() {
        String dataString = null;
        if (null == this.dataBytes) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n=================================================\n");
            sb.append("type: ").append(this.typeToString()).append('\n');
            sb.append("msg: ").append(this.msg).append('\n');
            sb.append("create time: ").append(this.createTime).append('\n');
            this.buildOtherString(sb);
            if (this.data instanceof List) {
                sb.append("trace:\n");
                this.buildListString(sb, (List)this.data);
            } else if (null != this.data) {
                sb.append("data: ").append(this.data).append('\n');
            }

            dataString = sb.toString();
            this.dataBytes = dataString.getBytes();
            this.data = null;
            this.log(TAG, dataString);
        } else {
            this.log(TAG, new String(this.dataBytes));
        }

    }

    protected void buildOtherString(StringBuilder sb) {
    }

    protected void buildListString(StringBuilder sb, List<?> dataList) {
        int i = 0;

        for(int len = dataList.size(); i < len; ++i) {
            Object item = dataList.get(i);
            sb.append('\t').append(item).append('\n');
        }

    }

    protected void log(String tag, String msg) {
        xLog.w(tag, msg);
    }

    public void print() {
        this.buildIssueString();
        saveIssue(this);
    }

    static void saveIssue(Issue issue) {
        executorService().execute(new SaveIssueTask(issue));
    }

    static ExecutorService executorService() {
        return taskService;
    }

    protected static MappedByteBuffer gMappedByteBuffer() {
        if (null == gMappedByteBuffer) {
            initMappedByteBuffer();
        }

        return gMappedByteBuffer;
    }

    protected static void createLogFileAndBuffer() {
        xLog.e(TAG, "createLogFileAndBuffer gBuffer:" + gMappedByteBuffer);
        if (null != gMappedByteBuffer) {
            gMappedByteBuffer.force();
            gMappedByteBuffer = null;
        }

        if (null != gRandomAccessFile) {
            try {
                gRandomAccessFile.close();
            } catch (IOException var3) {
                xLog.e(TAG, "gRandomAccessFile IOException", var3);
            }

            gRandomAccessFile = null;
        }

        if (null != gLogFile) {
            zipLogFile(gLogFile);
            gLogFile = null;
        }

        String fileName = "issues_" + SystemClock.elapsedRealtimeNanos() + ".log";
        gLogFile = new File(ISSUES_CACHE_DIR, fileName);
        if (gLogFile.exists()) {
            gLogFile.delete();
        }

        try {
            gLogFile.createNewFile();
            xLog.e(TAG, "create log file :" + gLogFile.getAbsolutePath());
            gRandomAccessFile = new RandomAccessFile(gLogFile.getAbsolutePath(), "rw");
            gMappedByteBuffer = gRandomAccessFile.getChannel().map(MapMode.READ_WRITE, 0L, 1048576L);
            gLineBytes = String.format(Locale.US, LINE_FORMAT, 0).getBytes();
            gMappedByteBuffer.put(gLineBytes);
        } catch (IOException var2) {
            xLog.e(TAG, "gRandomAccessFile IOException", var2);
        }

        deleteOldFiles();
    }

    protected static void initMappedByteBuffer() {
        if (taskService == null) {
            taskService = Executors.newSingleThreadExecutor();
        }

        File[] files = ISSUES_CACHE_DIR.listFiles();
        if (null != files && files.length != 0) {
            Arrays.sort(files, new Comparator<File>() {
                public int compare(File o1, File o2) {
                    return (int)(o2.lastModified() - o1.lastModified());
                }
            });
            File lastLogFile = null;

            for(int i = 0; i < files.length; ++i) {
                final File file = files[i];
                if (!file.isDirectory() && file.isFile()) {
                    if (file.getName().endsWith(".log")) {
                        if (lastLogFile == null) {
                            lastLogFile = file;
                        } else {
                            zipLogFile(file);
                        }
                    } else if (file.getName().endsWith(".zip")) {
                        executorService().execute(new Runnable() {
                            public void run() {
                                boolean uploadResult = Issue.doUploadZipLogFile(file);
                                if (uploadResult) {
                                    file.delete();
                                }

                            }
                        });
                    }
                }
            }

            xLog.e(TAG, "initMappedByteBuffer lastLogFile:" + lastLogFile);
            if (null != lastLogFile) {
                try {
                    gLogFile = lastLogFile;
                    gRandomAccessFile = new RandomAccessFile(lastLogFile.getAbsolutePath(), "rw");
                    gMappedByteBuffer = gRandomAccessFile.getChannel().map(MapMode.READ_WRITE, 0L, 1048576L);
                    gMappedByteBuffer.get(gLineBytes);
                    String gLineString = (new String(gLineBytes)).trim();
                    int lastPosition;
                    if (!TextUtils.isEmpty(gLineString)) {
                        gLineBytes = String.format(Locale.US, LINE_FORMAT, 0).getBytes();
                        gMappedByteBuffer.put(gLineBytes);
                        lastPosition = gMappedByteBuffer().position();
                    } else {
                        lastPosition = Integer.parseInt((new String(gLineBytes)).trim());
                    }

                    xLog.e(TAG, "initMappedByteBuffer lastPosition:" + lastPosition);
                    if (lastPosition >= 1048576) {
                        createLogFileAndBuffer();
                    } else {
                        gMappedByteBuffer.position(lastPosition);
                    }

                    deleteOldFiles();
                } catch (IOException var4) {
                    xLog.e(TAG, "initMappedByteBuffer", var4);
                    createLogFileAndBuffer();
                }
            } else {
                createLogFileAndBuffer();
            }

        } else {
            createLogFileAndBuffer();
        }
    }

    protected static void zipLogFile(final File logFile) {
        xLog.e(TAG, "zipLogFile:" + logFile);
        executorService().submit(new Runnable() {
            public void run() {
                File zipLogFile = Issue.doZipLogFile(logFile);
                boolean uploadSuccess = Issue.doUploadZipLogFile(zipLogFile);
                if (uploadSuccess) {
                    zipLogFile.delete();
                }

            }
        });
    }

    static File doZipLogFile(File logFile) {
        File zipLogFileDir = logFile.getParentFile();
        String zipLogFileName = logFile.getName().replace(".log", ".zip");
        File zipLogFile = new File(zipLogFileDir, zipLogFileName);
        if (zipLogFile.exists()) {
            logFile.delete();
            return zipLogFile;
        } else {
            try {
                xLog.e(TAG, "doZipLogFile src:" + logFile.getAbsolutePath());
                xLog.e(TAG, "doZipLogFile dst:" + zipLogFile.getAbsolutePath());
                FileOutputStream fos = new FileOutputStream(zipLogFile);
                ZipOutputStream zop = new ZipOutputStream(fos);
                ZipEntry zipEntry = new ZipEntry(logFile.getName());
                zop.putNextEntry(zipEntry);
                byte[] bytes = new byte[65536];
                FileInputStream fip = new FileInputStream(logFile);

                int length;
                while((length = fip.read(bytes)) >= 0) {
                    zop.write(bytes, 0, length);
                }

                zop.closeEntry();
                zop.close();
                fos.close();
                fip.close();
            } catch (IOException var13) {
                var13.printStackTrace();
            } finally {
                logFile.delete();
            }

            return zipLogFile;
        }
    }

    static boolean doUploadZipLogFile(File zipLogFile) {
        return zipLogFile != null && zipLogFile.exists() ? gIssueSupplier.upLoad(zipLogFile) : false;
    }

    static void deleteOldFiles() {
        final long maxCacheSize = gIssueSupplier.maxCacheSize();
        taskService.submit(new Runnable() {
            public void run() {
                File[] files = Issue.ISSUES_CACHE_DIR.listFiles();
                if (null != files && files.length != 0) {
                    Arrays.sort(files, new Comparator<File>() {
                        public int compare(File o1, File o2) {
                            return (int)(o2.lastModified() - o1.lastModified());
                        }
                    });
                    long fileLength = 0L;

                    for(int i = 0; i < files.length; ++i) {
                        File file = files[i];
                        if (file.isFile() && file.getName().endsWith("zip")) {
                            if (fileLength >= maxCacheSize) {
                                file.delete();
                            } else {
                                fileLength += file.length();
                            }
                        }
                    }

                }
            }
        });
    }

    static void resetTag(String tag) {
        TAG = tag + "_Issue";
    }

    protected static void init(PERF.IssueSupplier issueSupplier) {
        if (null == issueSupplier) {
            issueSupplier = new DefaultIssueSupplier();
        }

        gIssueSupplier = (PERF.IssueSupplier)issueSupplier;
        ISSUES_CACHE_DIR = new File(((PERF.IssueSupplier)issueSupplier).cacheRootDir(), ISSUES_CACHE_DIR_NAME);
        ISSUES_CACHE_DIR.mkdirs();
        xLog.e(TAG, "issues save in:" + ISSUES_CACHE_DIR.getAbsolutePath());
    }

    static {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        ISSUES_CACHE_DIR_NAME = "issues";
        gLineBytes = String.valueOf(1048576).getBytes();
        gLineBytesLength = gLineBytes.length;
        LINE_FORMAT = "%-" + gLineBytesLength + "d";
    }

    static class DefaultIssueSupplier implements PERF.IssueSupplier {
        DefaultIssueSupplier() {
        }

        public long maxCacheSize() {
            return 10485760L;
        }

        public File cacheRootDir() {
            return Environment.getExternalStorageDirectory();
        }

        public boolean upLoad(File issueFile) {
            return false;
        }
    }

    static class SaveIssueTask implements Runnable {
        Issue issue;

        public SaveIssueTask(Issue issue) {
            this.issue = issue;
        }

        public void run() {
            MappedByteBuffer buffer = Issue.gMappedByteBuffer();
            if (buffer.remaining() < this.issue.dataBytes.length) {
                Issue.createLogFileAndBuffer();
                buffer = Issue.gMappedByteBuffer();
            }

            buffer.put(this.issue.dataBytes);
            int dataPosition = buffer.position();
            Issue.gLineBytes = String.format(Locale.US, Issue.LINE_FORMAT, dataPosition).getBytes();
            buffer.position(0);
            buffer.put(Issue.gLineBytes);
            buffer.position(dataPosition);
            this.issue.dataBytes = null;
        }
    }
}

