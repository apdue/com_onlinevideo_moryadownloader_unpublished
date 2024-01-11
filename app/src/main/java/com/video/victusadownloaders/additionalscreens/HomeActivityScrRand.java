package com.video.victusadownloaders.additionalscreens;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.video.victusadownloaders.PermissionUtil;
import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;
import com.video.victusadownloaders.VideoSaver.SplashActivityScr;
import com.video.victusadownloaders.StatusVideoDownloader.StatusMainActivityVideoRand;
import com.video.victusadownloaders.StatusVideoDownloader.activity.InstagramActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.TwitterActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.WhatsappActivity;
import com.video.victusadownloaders.StatusVideoDownloader.util.ClipboardListener;
import com.video.victusadownloaders.WatchVideo.activities.ExitActivity;
import com.video.victusadownloaders.WatchVideo.activities.MainActivity;
import com.video.victusadownloaders.utilities.AdsWork;
import com.video.victusadownloaders.utilities.ApplicationClassFonts;
import com.video.victusadownloaders.utilities.NativeAdNew;

import java.util.Objects;
import java.util.regex.Matcher;

import io.awesome.gagtube.R;

public class HomeActivityScrRand extends AppCompatActivity {
//    String[] permissions = new String[]{
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//    };
    String CopyKey = "";
    String CopyValue = "";
    HomeActivityScrRand activity;
    private ClipboardManager clipBoard;

    public static String extractLinks(String text) {
        Matcher m = Patterns.WEB_URL.matcher(text);
        String url = "";
        while (m.find()) {
            url = m.group();
            Log.d("New URL", "URL extracted: " + url);

            break;
        }
        return url;
    }

    public void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);

        if (activity.getIntent().getExtras() != null) {
            for (String key : activity.getIntent().getExtras().keySet()) {
                CopyKey = key;
                String value = activity.getIntent().getExtras().getString(CopyKey);
                if (CopyKey.equals("android.intent.extra.TEXT")) {
                    CopyValue = activity.getIntent().getExtras().getString(CopyKey);
                    CopyValue = extractLinks(CopyValue);
                    callText(value);
                } else {
                    CopyValue = "";
                    callText(value);
                }
            }
        }
        if (clipBoard != null) {
            clipBoard.addPrimaryClipChangedListener(new ClipboardListener() {
                @Override
                public void onPrimaryClipChanged() {
                    try {
                        showNotification(Objects.requireNonNull(clipBoard.getPrimaryClip().getItemAt(0).getText()).toString());
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void showNotification(String Text) {
        if (Text.contains("instagram.com") || Text.contains("facebook.com") || Text.contains("fb") || Text.contains("tiktok.com")
                || Text.contains("twitter.com") || Text.contains("likee")
                || Text.contains("sharechat") || Text.contains("roposo") || Text.contains("snackvideo") || Text.contains("sck.io")
                || Text.contains("chingari") || Text.contains("myjosh") || Text.contains("mitron")) {
            Intent intent = new Intent(activity, StatusMainActivityVideoRand.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Notification", Text);
            PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name),
                        getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableLights(true);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder notificationBuilder;
            notificationBuilder = new NotificationCompat.Builder(activity, getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.icon_rand_rand)
                    .setColor(getResources().getColor(R.color.black))
                    .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(),
                            R.drawable.icon_rand_rand))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle("Copied text")
                    .setContentText(Text)
                    .setChannelId(getResources().getString(R.string.app_name))
                    .setFullScreenIntent(pendingIntent, true);
            notificationManager.notify(1, notificationBuilder.build());
        }
    }


    private void callText(String CopiedText) {
        try {
            if (CopiedText.contains("instagram.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(101);
                } else {
                    callInstaActivity();
                }
            } else if (CopiedText.contains("twitter.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(106);
                } else {
                    callTwitterActivity();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    TextView tvButton,social_video_btnName,insta_downloader_btnName,yt_downloader_btnName;
    @Override
    public void onBackPressed() {
        Intent intent;
        if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_six_back_enabled).contains("true")) {
            intent = new Intent(this, DummySixScrRand.class);
        }
        else if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_five_back_enabled).contains("true")) {
            intent = new Intent(this, DummyFiveScrRand.class);
        }
        else if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_four_back_enabled).contains("true")) {
            intent = new Intent(this, DummyFourScrRand.class);
        }
        else if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_three_back_enabled).contains("true")) {
            intent = new Intent(this, DummyThreeScrRand.class);
        } else if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_two_back_enabled).contains("true")) {
            intent = new Intent(this, DummyTwoScrRand.class);
        } else if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_one_back_enabled).contains("true")) {
            intent = new Intent(this, DummyOneScrRand.class);
        } else {
            intent = new Intent(this, ExitActivity.class);
        }
        AdsWork.showInterAds(this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                startActivity(intent);


            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClassFonts.hideStatus(this);
        setContentView(R.layout.activity_home);

        activity = HomeActivityScrRand.this;
        tvButton = (TextView) findViewById(R.id.ytButtonName);
        social_video_btnName = (TextView) findViewById(R.id.social_video_btnName);
        insta_downloader_btnName = (TextView) findViewById(R.id.insta_downloader_btnName);
        yt_downloader_btnName = (TextView) findViewById(R.id.yt_downloader_btnName);

        tvButton.setText(new PrefManagerVideo(this).getString(SplashActivityScr.yt_button_name));
        social_video_btnName.setText(new PrefManagerVideo(this).getString(SplashActivityScr.social_button_name));
        insta_downloader_btnName.setText(new PrefManagerVideo(this).getString(SplashActivityScr.insta_button_name));

        if (new PrefManagerVideo(this).getString(SplashActivityScr.home_screen).contains("ad")) {
            findViewById(R.id.ivAd).setVisibility(View.GONE);
            NativeAdNew.showNativeAd(this, findViewById(R.id.nativeAd), R.layout.ad_unified_medium);
        }

        checkButton(new PrefManagerVideo(this).getString(SplashActivityScr.youtube_downloader), findViewById(R.id.btnYt));
        checkButton(new PrefManagerVideo(this).getString(SplashActivityScr.social_downloader), findViewById(R.id.btnSocial));
        checkButton(new PrefManagerVideo(this).getString(SplashActivityScr.instagram_downloader), findViewById(R.id.btnInsta));

        AdsWork.loadNativeAd(this, findViewById(R.id.nativeAdSmall), R.layout.ad_unified_small);

        findViewById(R.id.btnYt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivityScrRand.this, MainActivity.class);
                AdsWork.showInterAds(HomeActivityScrRand.this, new AdsWork.AdFinished() {
                    @Override
                    public void onAdFinished() {
                        startActivity(i);
                    }
                });
            }
        });

        findViewById(R.id.btnInsta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(101);
                } else {
                    callInstaActivity();
                }
            }
        });
        findViewById(R.id.btnSocial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivityScrRand.this, StatusMainActivityVideoRand.class);
                AdsWork.showInterAds(HomeActivityScrRand.this, new AdsWork.AdFinished() {
                    @Override
                    public void onAdFinished() {
                        startActivity(i);
                    }
                });
            }
        });

    }

    private void checkButton(String string, View viewById) {
        if (!string.contains("true")) {
            viewById.setVisibility(View.GONE);
        }
    }

    public void callInstaActivity() {
        AdsWork.showInterAds(HomeActivityScrRand.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, InstagramActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callTwitterActivity() {
        AdsWork.showInterAds(HomeActivityScrRand.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, TwitterActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callWhatsappActivity() {
        AdsWork.showInterAds(activity, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, WhatsappActivity.class);
                startActivity(i);
            }
        });

    }

    private boolean checkPermissions(int type) {
        //        int result;
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        for (String p : permissions) {
//            result = ContextCompat.checkSelfPermission(VistaplyFirstActivity.this, p);
//            if (result != PackageManager.PERMISSION_GRANTED) {
//                listPermissionsNeeded.add(p);
//            }
//        }
        if (!PermissionUtil.isAllStoragePermissionsGranted(HomeActivityScrRand.this)) {
//            ActivityCompat.requestPermissions((Activity) (VistaplyFirstActivity.this),
//                    listPermissionsNeeded.toArray(new
//                            String[listPermissionsNeeded.size()]), type);
            PermissionUtil.requestAllStoragePermissions(HomeActivityScrRand.this,type);
            return false;
        } else {
            if (type == 101) {
                callInstaActivity();
            } else if (type == 102) {
                callWhatsappActivity();
            } else if (type == 106) {
                callTwitterActivity();
            }

        }
        return true;
    }

}