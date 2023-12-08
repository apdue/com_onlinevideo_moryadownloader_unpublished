package com.video.victusadownloaders.StatusVideoDownloader;

import static com.video.victusadownloaders.StatusVideoDownloader.util.Utils.createFileFolder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;

import com.video.victusadownloaders.PermissionUtil;
import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;
import com.video.victusadownloaders.VideoSaver.SplashActivityScr;
import com.video.victusadownloaders.StatusVideoDownloader.activity.AllGamesActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.ChingariActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.FacebookActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.GalleryActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.InstagramActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.JoshActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.LikeeActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.MXTakaTakActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.MitronActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.MojActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.RoposoActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.ShareChatActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.SnackVideoActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.TikTokActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.TwitterActivity;
import com.video.victusadownloaders.StatusVideoDownloader.activity.WhatsappActivity;
import com.video.victusadownloaders.StatusVideoDownloader.util.ClipboardListener;
import com.video.victusadownloaders.StatusVideoDownloader.util.Utils;
import com.video.victusadownloaders.utilities.AdsWork;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;

import io.awesome.gagtube.R;
import io.awesome.gagtube.databinding.ActivityStatusMainBinding;

public class StatusMainActivityVideo extends AppCompatActivity implements View.OnClickListener {

    StatusMainActivityVideo activity;
    ActivityStatusMainBinding binding;
//    String[] permissions = new String[]{
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//    };
    String CopyKey = "";
    String CopyValue = "";
    PrefManagerVideo prf;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_status_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_status_main);
        activity = this;
        prf = new PrefManagerVideo(this);

        AdsWork.loadNativeAd(this, (ViewGroup) findViewById(R.id.nativeAd), R.layout.ad_unified_medium);
        AdsWork.loadNativeAd(this, (ViewGroup) findViewById(R.id.nativeAdSmall), R.layout.ad_unified_small);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
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

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions(0);
        }


        if (checkEnabled(SplashActivityScr.TAG_JOSH)) {
            binding.rvJosh.setVisibility(View.VISIBLE);
        } else {
            binding.rvJosh.setVisibility(View.GONE);
        }

        if (checkEnabled(SplashActivityScr.TAG_CHINGARI)) {
            binding.rvChingari.setVisibility(View.VISIBLE);
        } else {
            binding.rvChingari.setVisibility(View.GONE);
        }

        if (checkEnabled(SplashActivityScr.TAG_MITRON)) {
            binding.rvMitron.setVisibility(View.VISIBLE);
        } else {
            binding.rvMitron.setVisibility(View.GONE);
        }

        if (checkEnabled(SplashActivityScr.TAG_SHARE_CHAT)) {
            binding.rvShareChat.setVisibility(View.VISIBLE);
        } else {
            binding.rvShareChat.setVisibility(View.GONE);
        }

        if (checkEnabled(SplashActivityScr.TAG_ROPOSO)) {
            binding.rvRoposo.setVisibility(View.VISIBLE);
        } else {
            binding.rvRoposo.setVisibility(View.GONE);
        }

        if (checkEnabled(SplashActivityScr.TAG_MOJ)) {
            binding.rvMoj.setVisibility(View.VISIBLE);
        } else {
            binding.rvMoj.setVisibility(View.GONE);
        }

        if (checkEnabled(SplashActivityScr.TAG_LIKEE)) {
            binding.rvLikee.setVisibility(View.VISIBLE);

        } else {
            binding.rvLikee.setVisibility(View.GONE);
        }

        if (checkEnabled(SplashActivityScr.TAG_INSTAGRAM)) {
            binding.rvInsta.setVisibility(View.VISIBLE);

        } else {
            binding.rvInsta.setVisibility(View.GONE);
        }

        if (checkEnabled(SplashActivityScr.TAG_WHATSAPP)) {
            binding.rvWhatsApp.setVisibility(View.VISIBLE);
        } else {
            binding.rvWhatsApp.setVisibility(View.GONE);
        }

        if (checkEnabled(SplashActivityScr.TAG_TIKTOK)) {
            binding.rvTikTok.setVisibility(View.VISIBLE);
        } else {
            binding.rvTikTok.setVisibility(View.GONE);
        }

        if (checkEnabled(SplashActivityScr.TAG_FACEBOOK)) {
            binding.rvFB.setVisibility(View.VISIBLE);
        } else {
            binding.rvFB.setVisibility(View.GONE);
        }

        if (checkEnabled(SplashActivityScr.TAG_TAKATAK)) {
            binding.rvMX.setVisibility(View.VISIBLE);
        } else {
            binding.rvMX.setVisibility(View.GONE);
        }

        binding.rvLikee.setOnClickListener(this);
        binding.rvInsta.setOnClickListener(this);
        binding.rvWhatsApp.setOnClickListener(this);
        binding.rvTikTok.setOnClickListener(this);
        binding.rvFB.setOnClickListener(this);
        binding.rvTwitter.setOnClickListener(this);
        binding.rvGallery.setOnClickListener(this);
        binding.rvAbout.setOnClickListener(this);
        binding.rvShareApp.setOnClickListener(this);
        binding.rvRateApp.setOnClickListener(this);
        binding.rvMoreApp.setOnClickListener(this);
        binding.rvSnack.setOnClickListener(this);
        binding.rvShareChat.setOnClickListener(this);
        binding.rvRoposo.setOnClickListener(this);
        binding.rvJosh.setOnClickListener(this);
        binding.rvChingari.setOnClickListener(this);
        binding.rvMitron.setOnClickListener(this);
        binding.rvMoj.setOnClickListener(this);
        binding.rvMX.setOnClickListener(this);
        binding.rvGames.setOnClickListener(this);


        createFileFolder();

    }

    boolean checkEnabled(String tag) {
        return prf.getBoolean(tag);
    }

    private void callText(String CopiedText) {
        try {
            if (CopiedText.contains("likee")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(100);
                } else {
                    callLikeeActivity();
                }
            } else if (CopiedText.contains("instagram.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(101);
                } else {
                    callInstaActivity();
                }
            } else if (CopiedText.contains("facebook.com") || CopiedText.contains("fb")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(104);
                } else {
                    callFacebookActivity();
                }
            } else if (CopiedText.contains("tiktok.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(103);
                } else {
                    callTikTokActivity();
                }
            } else if (CopiedText.contains("twitter.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(106);
                } else {
                    callTwitterActivity();
                }
            } else if (CopiedText.contains("sharechat")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(107);
                } else {
                    callShareChatActivity();
                }
            } else if (CopiedText.contains("roposo")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(108);
                } else {
                    callRoposoActivity();
                }
            } else if (CopiedText.contains("snackvideo") || CopiedText.contains("sck.io")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(109);
                } else {
                    callSnackVideoActivity();
                }
            } else if (CopiedText.contains("josh")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(110);
                } else {
                    callJoshActivity();
                }
            } else if (CopiedText.contains("chingari")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(111);
                } else {
                    callChingariActivity();
                }
            } else if (CopiedText.contains("mitron")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(112);
                } else {
                    callMitronActivity();
                }
            } else if (CopiedText.contains("mxtakatak")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(113);
                } else {
                    callMXActivity();
                }
            } else if (CopiedText.contains("moj")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(114);
                } else {
                    callMojActivity();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent i = null;

        switch (v.getId()) {
            case R.id.rvLikee:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(100);
                } else {
                    callLikeeActivity();
                }
                break;
            case R.id.rvInsta:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(101);
                } else {
                    callInstaActivity();
                }
                break;

            case R.id.rvWhatsApp:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(102);
                } else {
                    callWhatsappActivity();
                }
                break;
            case R.id.rvTikTok:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(103);
                } else {
                    callTikTokActivity();
                }
                break;
            case R.id.rvFB:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(104);
                } else {
                    callFacebookActivity();
                }
                break;
            case R.id.rvGallery:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(105);
                } else {
                    callGalleryActivity();
                }
                break;
            case R.id.rvTwitter:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(106);
                } else {
                    callTwitterActivity();
                }
                break;
            case R.id.rvShareApp:
                Utils.ShareApp(activity);
                break;

            case R.id.rvRateApp:
                Utils.RateApp(activity);
                break;
            case R.id.rvMoreApp:
                Utils.MoreApp(activity);
                break;
            case R.id.rvShareChat:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(107);
                } else {
                    callShareChatActivity();
                }
                break;
            case R.id.rvRoposo:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(108);
                } else {
                    callRoposoActivity();
                }
                break;
            case R.id.rvSnack:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(109);
                } else {
                    callSnackVideoActivity();
                }
                break;
            case R.id.rvJosh:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(110);
                } else {
                    callJoshActivity();
                }
                break;
            case R.id.rvChingari:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(111);
                } else {
                    callChingariActivity();
                }
                break;
            case R.id.rvMitron:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(112);
                } else {
                    callMitronActivity();
                }
                break;
            case R.id.rvMX:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(113);
                } else {
                    callMXActivity();
                }
                break;

            case R.id.rvMoj:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(114);
                } else {
                    callMojActivity();
                }
                break;
            case R.id.rvGames:
                callGamesActivity();
                break;

        }
    }

    public void callJoshActivity() {
        AdsWork.showInterAds(StatusMainActivityVideo.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, JoshActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callChingariActivity() {
        AdsWork.showInterAds(StatusMainActivityVideo.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, ChingariActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });

    }

    public void callMitronActivity() {
        AdsWork.showInterAds(StatusMainActivityVideo.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, MitronActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });


    }

    public void callMXActivity() {
        AdsWork.showInterAds(StatusMainActivityVideo.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, MXTakaTakActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callMojActivity() {
        AdsWork.showInterAds(StatusMainActivityVideo.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, MojActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callLikeeActivity() {
        AdsWork.showInterAds(StatusMainActivityVideo.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, LikeeActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callInstaActivity() {
        AdsWork.showInterAds(StatusMainActivityVideo.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, InstagramActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callWhatsappActivity() {
        AdsWork.showInterAds(StatusMainActivityVideo.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, WhatsappActivity.class);
                startActivity(i);
            }
        });

    }

    public void callTikTokActivity() {
        AdsWork.showInterAds(StatusMainActivityVideo.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, TikTokActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });

    }

    public void callFacebookActivity() {
        AdsWork.showInterAds(StatusMainActivityVideo.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, FacebookActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callTwitterActivity() {
        AdsWork.showInterAds(StatusMainActivityVideo.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, TwitterActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callGalleryActivity() {
        AdsWork.showInterAds(StatusMainActivityVideo.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, GalleryActivity.class);
                startActivity(i);
            }
        });
    }

    public void callRoposoActivity() {
        AdsWork.showInterAds(StatusMainActivityVideo.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, RoposoActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callShareChatActivity() {
        AdsWork.showInterAds(StatusMainActivityVideo.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, ShareChatActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });

    }

    public void callSnackVideoActivity() {
        AdsWork.showInterAds(StatusMainActivityVideo.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, SnackVideoActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callGamesActivity() {
        AdsWork.showInterAds(StatusMainActivityVideo.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Intent i = new Intent(activity, AllGamesActivity.class);
                startActivity(i);
            }
        });

    }

    public void showNotification(String Text) {
        if (Text.contains("instagram.com") || Text.contains("facebook.com") || Text.contains("fb") || Text.contains("tiktok.com")
                || Text.contains("twitter.com") || Text.contains("likee")
                || Text.contains("sharechat") || Text.contains("roposo") || Text.contains("snackvideo") || Text.contains("sck.io")
                || Text.contains("chingari") || Text.contains("myjosh") || Text.contains("mitron")) {
            Intent intent = new Intent(activity, StatusMainActivityVideo.class);
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
                    .setSmallIcon(R.drawable.icon)
                    .setColor(getResources().getColor(R.color.black))
                    .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(),
                            R.drawable.icon))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle("Copied text")
                    .setContentText(Text)
                    .setChannelId(getResources().getString(R.string.app_name))
                    .setFullScreenIntent(pendingIntent, true);
            notificationManager.notify(1, notificationBuilder.build());
        }
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
        if (!PermissionUtil.isAllStoragePermissionsGranted(StatusMainActivityVideo.this)) {
//            ActivityCompat.requestPermissions((Activity) (VistaplyFirstActivity.this),
//                    listPermissionsNeeded.toArray(new
//                            String[listPermissionsNeeded.size()]), type);
            PermissionUtil.requestAllStoragePermissions(StatusMainActivityVideo.this,type);
            return false;
        } else {
            if (type == 100) {
                callLikeeActivity();
            } else if (type == 101) {
                callInstaActivity();
            } else if (type == 102) {
                callWhatsappActivity();
            } else if (type == 103) {
                callTikTokActivity();
            } else if (type == 104) {
                callFacebookActivity();
            } else if (type == 105) {
                callGalleryActivity();
            } else if (type == 106) {
                callTwitterActivity();
            } else if (type == 107) {
                callShareChatActivity();
            } else if (type == 108) {
                callRoposoActivity();
            } else if (type == 109) {
                callSnackVideoActivity();
            } else if (type == 110) {
                callJoshActivity();
            } else if (type == 111) {
                callChingariActivity();
            } else if (type == 112) {
                callMitronActivity();
            } else if (type == 113) {
                callMXActivity();
            } else if (type == 114) {
                callMojActivity();
            }

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callLikeeActivity();
            } else {
            }
            return;
        } else if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callInstaActivity();
            } else {
            }
            return;
        } else if (requestCode == 102) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callWhatsappActivity();
            } else {
            }
            return;
        } else if (requestCode == 103) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callTikTokActivity();
            } else {
            }
            return;
        } else if (requestCode == 104) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callFacebookActivity();
            } else {
            }
            return;
        } else if (requestCode == 105) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callGalleryActivity();
            } else {
            }
            return;
        } else if (requestCode == 106) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callTwitterActivity();
            } else {
            }
            return;
        } else if (requestCode == 107) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callShareChatActivity();
            } else {
            }
            return;
        } else if (requestCode == 108) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callRoposoActivity();
            } else {
            }
            return;
        } else if (requestCode == 109) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callSnackVideoActivity();
            } else {
            }
            return;
        } else if (requestCode == 110) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callJoshActivity();
            }
        } else if (requestCode == 111) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callChingariActivity();
            }
        } else if (requestCode == 112) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callMitronActivity();
            }
        } else if (requestCode == 113) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callMXActivity();
            }
        } else if (requestCode == 114) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callMojActivity();
            }
        }

    }

    @Override
    public void onBackPressed() {
        StatusMainActivityVideo.super.onBackPressed();
    }

    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        Intent refresh = new Intent(StatusMainActivityVideo.this, StatusMainActivityVideo.class);
        startActivity(refresh);
        finish();
    }
}