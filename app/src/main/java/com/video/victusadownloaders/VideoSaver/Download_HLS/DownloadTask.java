package com.video.victusadownloaders.VideoSaver.Download_HLS;

import android.app.NotificationManager;
import android.content.Context;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy.Builder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class DownloadTask extends ModernAsyncTask<Object, Integer, Object> {
    private DownloadingListener downloadingListener;
    private Queue<byte[]> mBuffers = new ConcurrentLinkedQueue();
    private Context mContext;
    private int mDownloadedCount;
    private long mDownloadedSize = 0;
    private File mDstFile;
    private int mId;
    private String mM3u8;
    private NotificationManager mNm;
    private FileOutputStream mOutput;
    private Exception mThreadException;
    private int mUrlCount;
    private volatile int mWriteIdx = 0;
    private int progress = 1;

    public interface DownloadingListener {
        void onDownloadingCancel();

        void onDownloadingFailed(String str);

        void onDownloadingFinish();

        void onDownloadingProgress(int i, int i2);

        void onDownloadingStart();
    }

    DownloadTask(Context context, int i, String str, String str2, File file, DownloadingListener downloadingListener2) {
        StrictMode.setVmPolicy(new Builder().build());
        this.mId = i;
        this.mM3u8 = str;
        this.mContext = context;
        this.mNm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.mDstFile = getDstFile(file, str2);
        this.downloadingListener = downloadingListener2;
    }

    private static ArrayList<String> downloadM3u(String str) throws IOException, InterruptedException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(str).openStream(), "utf-8"));
        ArrayList<String> arrayList = new ArrayList<>();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                return arrayList;
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            } else if (!readLine.startsWith("#") && readLine.length() > 0) {
                arrayList.add(readLine);
            }
        }
    }

    private static File getDstFile(File file, String str) {
        String convertFilename = convertFilename(str);
        file.mkdirs();
        StringBuilder sb = new StringBuilder();
        sb.append(convertFilename);
        sb.append(".mp4");
        return new File(file, sb.toString());
    }

    private static String convertFilename(String str) {
        return str.replaceAll("./:\\*\\?\\|<>", "_");
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.downloadingListener.onDownloadingStart();
    }

    protected void onCancelled() {
        super.onCancelled();
        this.downloadingListener.onDownloadingCancel();
    }

    protected void onPostExecute(Object obj) {
        this.mBuffers.clear();
        super.onPostExecute(obj);
        StringBuilder sb = new StringBuilder();
        sb.append("onPostExecute: ");
        sb.append(obj.toString());
        Log.d("DownloadTask", sb.toString());
        if (obj.equals("success")) {
            this.downloadingListener.onDownloadingFinish();
        } else {
            this.downloadingListener.onDownloadingFailed(obj.toString());
        }
    }

    protected void onProgressUpdate(Integer... numArr) {
        super.onProgressUpdate(numArr);
        StringBuilder sb = new StringBuilder();
        sb.append("showDownloadProgress: ");
        sb.append(numArr[0]);
        Log.d("DownloadTask", sb.toString());
        this.downloadingListener.onDownloadingProgress(numArr[0].intValue(), this.mUrlCount);
    }
    @Override
    protected Object doInBackground(Object... objArr) {
        FileOutputStream fileOutputStream = null;
        long currentTimeMillis = System.currentTimeMillis();
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(6);
        try {
            this.mDstFile.delete();
            this.mOutput = new FileOutputStream(this.mDstFile);
            ArrayList<String> downloadM3u = downloadM3u(this.mM3u8);
            this.mUrlCount = downloadM3u.size();
            final int i = 0;
            for (final String str : downloadM3u) {
                int i2 = i + 1;
                newFixedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DownloadTask.this.downloadFile(i, new URI(DownloadTask.this.mM3u8).resolve(str).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            DownloadTask.this.mThreadException = e;
                            DownloadTask.this.cancel(true);
                        }
                    }
                });
            }
            newFixedThreadPool.shutdown();
            do {
            } while (!newFixedThreadPool.awaitTermination(500, TimeUnit.MILLISECONDS));
            long currentTimeMillis2 = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();
            sb.append("Download time:");
            sb.append((currentTimeMillis2 - currentTimeMillis) / 1000);
            sb.append("sec");
            Log.d("DownloadService", sb.toString());
            boolean z = !isCancelled();
            String str2 = "success";
            FileOutputStream fileOutputStream2 = this.mOutput;
            if (fileOutputStream2 != null) {
                try {
                    fileOutputStream2.close();
                    this.mOutput = null;
                    if (!z) {
                        this.mDstFile.delete();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return str2;
        } catch (Exception e2) {
            try {
                if (this.mThreadException != null) {
                    e2 = this.mThreadException;
                }
                this.mDstFile.delete();
                newFixedThreadPool.shutdownNow();
                e2.printStackTrace();
                isCancelled();
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                        this.mOutput = null;
                        if (0 == 0) {
                            this.mDstFile.delete();
                        }
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
                return e2;
            } finally {
                fileOutputStream = this.mOutput;
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                        this.mOutput = null;
                        if (0 == 0) {
                            this.mDstFile.delete();
                        }
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
            }
        } catch (Throwable th) {
            newFixedThreadPool.shutdown();
            throw th;
        }
    }

    private int downloadFile(int i, String str) throws MalformedURLException, IOException, InterruptedException {
        InputStream inputStream = null;
        try {
            inputStream = new URL(str).openStream();
            byte[] bArr = (byte[]) this.mBuffers.poll();
            if (bArr == null) {
                bArr = new byte[1024000];
            }
            int i2 = 0;
            while (true) {
                int read = inputStream.read(bArr, i2, bArr.length - i2);
                if (read < 0) {
                    break;
                }
                i2 += read;
                if (bArr.length - i2 == 0) {
                    int length = (bArr.length * 3) / 2;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Grow buf ");
                    sb.append(bArr.length);
                    sb.append(" => ");
                    sb.append(length);
                    Log.d("DownloadTask", sb.toString());
                    byte[] bArr2 = new byte[length];
                    System.arraycopy(bArr, 0, bArr2, 0, i2);
                    bArr = bArr2;
                    break;
                }
            }
            this.mDownloadedCount++;
            write(i, bArr, i2);
            int i3 = this.progress;
            this.progress = i3 + 1;
            onProgressUpdate(Integer.valueOf(i3));
            return 0;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private void write(int i, byte[] bArr, int i2) throws InterruptedException, IOException {
        this.mDownloadedSize += (long) i2;
//        while (this.mWriteIdx != i) {
//            Thread.sleep(125);
//        }
        synchronized (this.mOutput) {
            this.mOutput.write(bArr, 0, i2);
            this.mWriteIdx++;
        }
        this.mBuffers.add(bArr);
    }
}
