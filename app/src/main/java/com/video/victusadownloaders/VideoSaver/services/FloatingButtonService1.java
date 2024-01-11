package com.video.victusadownloaders.VideoSaver.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import io.awesome.gagtube.R;
import com.video.victusadownloaders.VideoSaver.downloaderactivities.DailogActivity;
import com.video.victusadownloaders.VideoSaver.downloaderactivities.Download_Auto;
import timber.log.Timber;

public class FloatingButtonService1 extends Service {
    String TAG = "Refrance Tag";
    private ImageView chatheadImg;
    /* access modifiers changed from: private */
    public RelativeLayout chatheadView;
    private boolean isLeft = true;
    public boolean isclip = true;
    /* access modifiers changed from: private */
    public ClipboardManager mCM;
    private Context mContext;
    private MonitorTask mTask = new MonitorTask();
    /* access modifiers changed from: private */
    public WindowManager mWindowManager;
    String patternInsta = "https://www.instagram.com/p/.";
    /* access modifiers changed from: private */
    public ImageView removeImg;
    /* access modifiers changed from: private */
    public RelativeLayout removeView;
    private String sMsg = "";
    /* access modifiers changed from: private */
    public Point szWindow = new Point();
    String vidUrl;
    /* access modifiers changed from: private */
    public int x_init_cord;
    /* access modifiers changed from: private */
    public int x_init_margin;
    /* access modifiers changed from: private */
    public int y_init_cord;
    /* access modifiers changed from: private */
    public int y_init_margin;

    private class MonitorTask extends Thread {
        private volatile boolean mKeepRunning = false;
        private String mOldClip = null;

        public MonitorTask() {
            super("ClipboardMonitor");
        }

        public void cancel() {
            this.mKeepRunning = false;
            interrupt();
        }

        public void run() {
            this.mKeepRunning = true;
            do {
                doTask();
            } while (this.mKeepRunning);
        }

        private void doTask() {
            if (FloatingButtonService1.this.mCM != null) {
                ClipDescription primaryClipDescription = FloatingButtonService1.this.mCM.getPrimaryClipDescription();
                if (primaryClipDescription != null) {
                    primaryClipDescription.hasMimeType("text/plain");
                    if (FloatingButtonService1.this.mCM.hasPrimaryClip() && FloatingButtonService1.this.mCM.getPrimaryClip() != null) {
                        ClipData primaryClip = FloatingButtonService1.this.mCM.getPrimaryClip();
                        String str = "";
                        if (primaryClip != null) {
                            str = primaryClip.toString();
                        }
                        if (str.equals(this.mOldClip) || str.contains("NULL") || str == null) {
                            FloatingButtonService1.this.showToast("Copy Data Not found ");
                            return;
                        }
                        String str2 = FloatingButtonService1.this.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("detect new text clip: ");
                        sb.append(str.toString());
                        Log.i(str2, sb.toString());
                        this.mOldClip = str;
                        String str3 = FloatingButtonService1.this.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("new text clip inserted: ");
                        sb2.append(str.toString());
                        Log.i(str3, sb2.toString());
                        if (FloatingButtonService1.this.isclip) {
                            FloatingButtonService1.this.checkText();
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            Toast.makeText(FloatingButtonService1.this, "Please Copy again ", Toast.LENGTH_SHORT).show();
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return Service.START_STICKY;
    }

    public void onCreate() {
        this.mCM = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        this.mCM.setPrimaryClip(ClipData.newPlainText("", ""));
        this.mTask.start();
        super.onCreate();
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public Context getContext() {
        return this.mContext;
    }

    /* access modifiers changed from: private */
    public void handleStart() {
        this.mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int i = VERSION.SDK_INT >= 26 ? 2038 : 5022;
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.removeView = (RelativeLayout) layoutInflater.inflate(R.layout.remove, null);
        LayoutParams layoutParams = new LayoutParams(-2, -2, i, 8, -3);
        layoutParams.gravity = 51;
        this.removeView.setVisibility(View.GONE);
        this.removeImg = (ImageView) this.removeView.findViewById(R.id.remove_img);
        this.removeImg.setImageResource(R.drawable.cross_rand);
        this.mWindowManager.addView(this.removeView, layoutParams);
        this.chatheadView = (RelativeLayout) layoutInflater.inflate(R.layout.floating_bubble_view, null);
        this.chatheadImg = (ImageView) this.chatheadView.findViewById(R.id.bubble);
        this.chatheadImg.setImageResource(R.drawable.icon_rand_rand);
        this.isclip = false;
        if (VERSION.SDK_INT >= 11) {
            this.mWindowManager.getDefaultDisplay().getSize(this.szWindow);
        } else {
            this.szWindow.set(this.mWindowManager.getDefaultDisplay().getWidth(), this.mWindowManager.getDefaultDisplay().getHeight());
        }
        LayoutParams layoutParams2 = new LayoutParams(-2, -2, i, 8, -3);
        layoutParams2.gravity = 51;
        layoutParams2.x = 1200;
        layoutParams2.y = 200;
        this.mWindowManager.addView(this.chatheadView, layoutParams2);
        touchListener();
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void touchListener() {
        this.chatheadView.setOnTouchListener(new OnTouchListener() {
            Handler handler_longClick = new Handler();
            boolean inBounded = false;
            boolean isLongclick = false;
            boolean isclick = false;
            int remove_img_height = 0;
            int remove_img_width = 0;
            Runnable runnable_longClick = new Runnable() {
                public void run() {
                    FloatingButtonService1 r0 = FloatingButtonService1.this;
                    isLongclick = true;
                    FloatingButtonService1.this.removeView.setVisibility(View.VISIBLE);
                    FloatingButtonService1.this.chathead_longclick();
                }
            };
            long time_end = 0;
            long time_start = 0;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                LayoutParams layoutParams = (LayoutParams) FloatingButtonService1.this.chatheadView.getLayoutParams();
                int rawX = (int) motionEvent.getRawX();
                int rawY = (int) motionEvent.getRawY();
                switch (motionEvent.getAction()) {
                    case 0:
                        this.time_start = System.currentTimeMillis();
                        this.handler_longClick.postDelayed(this.runnable_longClick, 400);
                        this.remove_img_width = FloatingButtonService1.this.removeImg.getLayoutParams().width;
                        this.remove_img_height = FloatingButtonService1.this.removeImg.getLayoutParams().height;
                        FloatingButtonService1.this.x_init_cord = rawX;
                        FloatingButtonService1.this.y_init_cord = rawY;
                        FloatingButtonService1.this.x_init_margin = layoutParams.x;
                        FloatingButtonService1.this.y_init_margin = layoutParams.y;
                        break;
                    case 1:
                        this.isLongclick = false;
                        this.isclick = false;
                        FloatingButtonService1.this.removeView.setVisibility(View.GONE);
                        FloatingButtonService1.this.removeImg.getLayoutParams().height = this.remove_img_height;
                        FloatingButtonService1.this.removeImg.getLayoutParams().width = this.remove_img_width;
                        this.handler_longClick.removeCallbacks(this.runnable_longClick);
                        if (!this.inBounded) {
                            int access$400 = rawX - FloatingButtonService1.this.x_init_cord;
                            int access$500 = rawY - FloatingButtonService1.this.y_init_cord;
                            int access$700 = FloatingButtonService1.this.y_init_margin + access$500;
                            int access$900 = FloatingButtonService1.this.getStatusBarHeight();
                            if (access$700 < 0) {
                                access$700 = 0;
                            } else if (FloatingButtonService1.this.chatheadView.getHeight() + access$900 + access$700 > FloatingButtonService1.this.szWindow.y) {
                                access$700 = FloatingButtonService1.this.szWindow.y - (FloatingButtonService1.this.chatheadView.getHeight() + access$900);
                            }
                            layoutParams.y = access$700;
                            if (Math.abs(access$400) < 5 && Math.abs(access$500) < 5) {
                                this.time_end = System.currentTimeMillis();
                                if (this.time_end - this.time_start < 300) {
                                    FloatingButtonService1.this.stringurl();
                                    FloatingButtonService1.this.chatheadView.setVisibility(View.GONE);
                                    FloatingButtonService1.this.isclip = true;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("");
                                    sb.append(layoutParams.y);
                                    Log.i("y cordinates", sb.toString());
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("");
                                    sb2.append(layoutParams.x);
                                    Log.i("x cordinates", sb2.toString());
                                    if (layoutParams.y > FloatingButtonService1.getScreenHeight() / 3) {
                                        layoutParams.x = 10;
                                        layoutParams.y = 10;
                                        FloatingButtonService1.this.mWindowManager.updateViewLayout(FloatingButtonService1.this.chatheadView, layoutParams);
                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                isclick = false;
                                            }
                                        }, 0);
                                    }
                                }
                            }
                            this.inBounded = false;
                            FloatingButtonService1.this.resetPosition(rawX);
                            break;
                        } else {
                            new Intent(FloatingButtonService1.this, FloatingButtonService1.class);
                            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(FloatingButtonService1.this);
                            Timber.e("Switch value is %s", String.valueOf(defaultSharedPreferences.getBoolean(FloatingButtonService1.this.getString(R.string.key_allow_float_switch), false)));
                            Editor edit = defaultSharedPreferences.edit();
                            edit.putBoolean(FloatingButtonService1.this.getString(R.string.key_allow_float_switch), false);
                            edit.commit();
                            FloatingButtonService1.this.chatheadView.setVisibility(View.GONE);
                            FloatingButtonService1.this.isclip = true;
                            this.inBounded = false;
                            break;
                        }
                    case 2:
                        int access$600 = FloatingButtonService1.this.x_init_margin + (rawX - FloatingButtonService1.this.x_init_cord);
                        int access$7002 = FloatingButtonService1.this.y_init_margin + (rawY - FloatingButtonService1.this.y_init_cord);
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("");
                        sb3.append(access$7002);
                        Log.i("y axis cordinates", sb3.toString());
                        if (this.isLongclick) {
                            Log.i("Long Click", "Long click");
                            int i = (FloatingButtonService1.this.szWindow.x / 2) + ((int) (((double) this.remove_img_width) * 1.5d));
                            int i2 = FloatingButtonService1.this.szWindow.y - ((int) (((double) this.remove_img_height) * 1.5d));
                            if (rawX >= (FloatingButtonService1.this.szWindow.x / 2) - ((int) (((double) this.remove_img_width) * 1.5d)) && rawX <= i && rawY >= i2) {
                                this.inBounded = true;
                                int i3 = (int) ((((double) FloatingButtonService1.this.szWindow.x) - (((double) this.remove_img_height) * 1.5d)) / 2.0d);
                                int access$9002 = (int) (((double) FloatingButtonService1.this.szWindow.y) - ((((double) this.remove_img_width) * 1.5d) + ((double) FloatingButtonService1.this.getStatusBarHeight())));
                                if (FloatingButtonService1.this.removeImg.getLayoutParams().height == this.remove_img_height) {
                                    FloatingButtonService1.this.removeImg.getLayoutParams().height = (int) (((double) this.remove_img_height) * 1.5d);
                                    FloatingButtonService1.this.removeImg.getLayoutParams().width = (int) (((double) this.remove_img_width) * 1.5d);
                                    LayoutParams layoutParams2 = (LayoutParams) FloatingButtonService1.this.removeView.getLayoutParams();
                                    layoutParams2.x = i3;
                                    layoutParams2.y = access$9002;
                                    FloatingButtonService1.this.mWindowManager.updateViewLayout(FloatingButtonService1.this.removeView, layoutParams2);
                                }
                                layoutParams.x = i3 + (Math.abs(FloatingButtonService1.this.removeView.getWidth() - FloatingButtonService1.this.chatheadView.getWidth()) / 2);
                                layoutParams.y = access$9002 + (Math.abs(FloatingButtonService1.this.removeView.getHeight() - FloatingButtonService1.this.chatheadView.getHeight()) / 2);
                                FloatingButtonService1.this.mWindowManager.updateViewLayout(FloatingButtonService1.this.chatheadView, layoutParams);
                                break;
                            } else {
                                this.inBounded = false;
                                FloatingButtonService1.this.removeImg.getLayoutParams().height = this.remove_img_height;
                                FloatingButtonService1.this.removeImg.getLayoutParams().width = this.remove_img_width;
                                LayoutParams layoutParams3 = (LayoutParams) FloatingButtonService1.this.removeView.getLayoutParams();
                                int height = FloatingButtonService1.this.szWindow.y - (FloatingButtonService1.this.removeView.getHeight() + FloatingButtonService1.this.getStatusBarHeight());
                                layoutParams3.x = (FloatingButtonService1.this.szWindow.x - FloatingButtonService1.this.removeView.getWidth()) / 2;
                                layoutParams3.y = height;
                                FloatingButtonService1.this.mWindowManager.updateViewLayout(FloatingButtonService1.this.removeView, layoutParams3);
                            }
                        }
                        layoutParams.x = access$600;
                        layoutParams.y = access$7002;
                        FloatingButtonService1.this.mWindowManager.updateViewLayout(FloatingButtonService1.this.chatheadView, layoutParams);
                        break;
                }
                return true;
            }
        });
    }

    /* access modifiers changed from: private */
    public void resetPosition(int i) {
        if (i <= this.szWindow.x / 2) {
            this.isLeft = true;
            moveToLeft(i);
            return;
        }
        this.isLeft = false;
        moveToRight(i);
    }

    private void moveToLeft(int i) {
        final int i2 = this.szWindow.x - i;
        CountDownTimer r1 = new CountDownTimer(500, 5) {
            LayoutParams mParams = ((LayoutParams) FloatingButtonService1.this.chatheadView.getLayoutParams());

            public void onTick(long j) {
                long j2 = (500 - j) / 5;
                this.mParams.x = 0 - ((int) FloatingButtonService1.this.bounceValue(j2, (long) i2));
                FloatingButtonService1.this.mWindowManager.updateViewLayout(FloatingButtonService1.this.chatheadView, this.mParams);
            }

            public void onFinish() {
                this.mParams.x = 0;
                FloatingButtonService1.this.mWindowManager.updateViewLayout(FloatingButtonService1.this.chatheadView, this.mParams);
            }
        };
        r1.start();
    }

    private void moveToRight(int i) {
        final int i2 = i;
        CountDownTimer r0 = new CountDownTimer(500, 5) {
            LayoutParams mParams = ((LayoutParams) FloatingButtonService1.this.chatheadView.getLayoutParams());

            public void onTick(long j) {
                long j2 = (500 - j) / 5;
                this.mParams.x = (FloatingButtonService1.this.szWindow.x + ((int) FloatingButtonService1.this.bounceValue(j2, (long) i2))) - FloatingButtonService1.this.chatheadView.getWidth();
                FloatingButtonService1.this.mWindowManager.updateViewLayout(FloatingButtonService1.this.chatheadView, this.mParams);
            }

            public void onFinish() {
                this.mParams.x = FloatingButtonService1.this.szWindow.x - FloatingButtonService1.this.chatheadView.getWidth();
                FloatingButtonService1.this.mWindowManager.updateViewLayout(FloatingButtonService1.this.chatheadView, this.mParams);
            }
        };
        r0.start();
    }

    /* access modifiers changed from: private */
    public double bounceValue(long j, long j2) {
        double d = (double) j;
        return ((double) j2) * Math.exp(-0.055d * d) * Math.cos(d * 0.08d);
    }

    /* access modifiers changed from: private */
    public void chathead_longclick() {
        LayoutParams layoutParams = (LayoutParams) this.removeView.getLayoutParams();
        int height = this.szWindow.y - (this.removeView.getHeight() + getStatusBarHeight());
        layoutParams.x = (this.szWindow.x - this.removeView.getWidth()) / 2;
        layoutParams.y = height;
        this.mWindowManager.updateViewLayout(this.removeView, layoutParams);
    }

    /* access modifiers changed from: private */
    public int getStatusBarHeight() {
        return (int) Math.ceil((double) (getApplicationContext().getResources().getDisplayMetrics().density * 25.0f));
    }

    public void onDestroy() {
        super.onDestroy();
        this.mTask.cancel();
    }

    /* access modifiers changed from: private */
    public void checkText() {
        ClipboardManager clipboardManager = this.mCM;
        if (clipboardManager != null) {
            CharSequence text = clipboardManager.getText();
            if (text != null) {
                this.vidUrl = text.toString().trim();
            }
            if (this.vidUrl.contains("/^https:\\/\\/www\\.facebook\\.com\\/([^\\/?].+\\/)?video(s|\\.php)[\\/?].*$/gm") || this.vidUrl.contains("facebook.com")) {
                handle();
            } else if (this.vidUrl.contains("dailymotion.com")) {
                handle();
            } else if (!this.vidUrl.contains("twitter.com")) {
                if (this.vidUrl.contains("https://www.instagram.com/p")) {
                    handle();
                } else if (this.vidUrl.contains("youtu.be")) {
                    handle();
                } else {
                    showToast("Invalid url.");
                }
            }
        } else {
            showToast("Check copy url");
        }
    }

    /* access modifiers changed from: private */
    public void showToast(String str) {
        try {
            if (getContext() != null) {
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void stringurl() {
        String str = this.vidUrl.toString();
        Intent intent = new Intent(this, Download_Auto.class);
        intent.putExtra("Extra", str);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.chatheadView.setVisibility(View.GONE);
    }

    public void handle() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                String str = FloatingButtonService1.this.vidUrl;
                if (VERSION.SDK_INT < 23 || Settings.canDrawOverlays(FloatingButtonService1.this)) {
                    FloatingButtonService1.this.handleStart();
                    return;
                }
                Intent intent = new Intent(FloatingButtonService1.this, DailogActivity.class);
                intent.putExtra("Extra", str);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                FloatingButtonService1.this.startActivity(intent);
            }
        });
    }
}
