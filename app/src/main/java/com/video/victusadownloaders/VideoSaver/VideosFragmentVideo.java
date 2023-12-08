package com.video.victusadownloaders.VideoSaver;



import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dailymotion.android.player.sdk.PlayerWebView;
import com.google.android.gms.ads.AdRequest;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;
import io.awesome.gagtube.R;
import com.video.victusadownloaders.VideoSaver.Download_HLS.DownloadService;
import com.video.victusadownloaders.VideoSaver.DownloaderAdapters.DailyVideoAdapter;
import com.video.victusadownloaders.VideoSaver.DownloaderAdapters.DailyVideoAdapter.RefreshData;
import com.video.victusadownloaders.VideoSaver.Interface.CustomItemClickListener;
import com.video.victusadownloaders.VideoSaver.Interface.DailymotionApiInterface;
import com.video.victusadownloaders.VideoSaver.Interface.VideoApiInterface;
import com.video.victusadownloaders.VideoSaver.Network.RetrofitClientInstance;
import com.video.victusadownloaders.VideoSaver.app.AVD;
import com.video.victusadownloaders.VideoSaver.downloaderactivities.MessageEvent;
import com.video.victusadownloaders.VideoSaver.model.HLS.Example;
import com.video.victusadownloaders.VideoSaver.model.HLS.Format;
import com.video.victusadownloaders.VideoSaver.model.dailymotion.DmChannel;
import com.video.victusadownloaders.VideoSaver.model.dailymotion.DmVideo;
import com.video.victusadownloaders.VideoSaver.model.dailymotion.VideosList;
import com.video.victusadownloaders.VideoSaver.utils.BSUtils_D;
import com.video.victusadownloaders.VideoSaver.utils.BSUtils_D.BSCallBack;
import com.video.victusadownloaders.VideoSaver.utils.DialogUtils;
import com.video.victusadownloaders.VideoSaver.utils.UtilMethods;

public class VideosFragmentVideo extends Fragment {
    public static final String TAG = "VideosFragment";
    public static VideosFragmentVideo sInstance;
    private static AdRequest adRequest;
    VideoApiInterface apiInterface;
    private DmChannel mChannel;
    private Context mContext;
    private LinkedList<DmVideo> mDmVideosList;
    private PlayerWebView mPlayerWebView;
    @BindView(R.id.progressBarFragment_videos)
    ProgressBar mProgressBar;
    @BindView(R.id.progressBarLoadMoreVideosFragment)
    ProgressBar mProgressBarLoadMore;
    @BindView(R.id.recyclerVideoFragment)
    public RecyclerView mRecyclerVideosList;
    private DailyVideoAdapter mVideoAdapter;
//    public static InterstitialAd mInterstitialAd;

    //new
    public static Context context;






    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        DailyVideoAdapter.setOnRefreshData(new RefreshData() {
            public void onRefreshData(PlayerWebView playerWebView) {
                VideosFragmentVideo.this.mPlayerWebView = playerWebView;
            }
        });
    }

    public Context getContext() {
        return this.mContext;
    }

    public static Fragment newInstance(DmChannel dmChannel, int i) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("channel", dmChannel);
        bundle.putInt("page_position", i);
        VideosFragmentVideo videosFragmentVideo = new VideosFragmentVideo();
        videosFragmentVideo.setArguments(bundle);
        return videosFragmentVideo;
    }

       public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.xfragment_videos_view, viewGroup, false);



        context = getActivity();

        ButterKnife.bind(this, inflate);
        this.apiInterface = (VideoApiInterface) RetrofitClientInstance.getRetrofitInstance().create(VideoApiInterface.class);
        sInstance = this;


        return inflate;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        PlayerWebView playerWebView = this.mPlayerWebView;
        if (playerWebView != null) {
            playerWebView.pause();
        }
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        try {
            if (AVD.getINSTANCE().getChannelVideosMap().containsKey(getChannel().getId())) {
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(AVD.getINSTANCE().getChannelVideosMap().get(getChannel().getId()));
                Log.i("Channel id", sb.toString());
                setAdapter((LinkedList) AVD.getINSTANCE().getChannelVideosMap().get(getChannel().getId()));
                return;
            }
            getVideosList(getRandonPage(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getRandonPage() {
        return String.valueOf(new Random().nextInt(10) + 1);
    }

    private void getVideosList(String str, final boolean z) {
        this.mProgressBar.setVisibility(View.VISIBLE);
        DailymotionApiInterface dailymotionApiInterface = (DailymotionApiInterface) new Builder().baseUrl("https://api.dailymotion.com/").addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(DailymotionApiInterface.class);
        HashMap hashMap = new HashMap();
        hashMap.put("fields", "channel,country,created_time,duration,id,language,likes_total,thumbnail_180_url,title,views_total");
        hashMap.put("sort", "visited");
        hashMap.put("page", str);
        hashMap.put("limit", "100");
        dailymotionApiInterface.getVideosByChannel(getChannel().getId(), hashMap).enqueue(new Callback<VideosList>() {
            public void onResponse(Call<VideosList> call, Response<VideosList> response) {
                if (((VideosList) response.body()).getVideosList() != null && ((VideosList) response.body()).getVideosList().size() > 0) {
                    if (!AVD.getINSTANCE().getChannelVideosMap().containsKey(VideosFragmentVideo.this.getChannel().getId())) {
                        AVD.getINSTANCE().getChannelVideosMap().put(VideosFragmentVideo.this.getChannel().getId(), new LinkedList(((VideosList) response.body()).getVideosList()));
                    }
                    VideosFragmentVideo.this.setAdapter(new LinkedList(((VideosList) response.body()).getVideosList()));
                } else if (!z) {
                    VideosFragmentVideo.this.getVideosList("1", true);
                }
            }

            public void onFailure(Call<VideosList> call, Throwable th) {
                VideosFragmentVideo.this.showToast("Error! Please try again");
            }
        });
    }

    private void showToast(String str) {
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

    private void setAdapter(LinkedList<DmVideo> linkedList) {
        this.mDmVideosList = new LinkedList<>();
        this.mDmVideosList.addAll(linkedList);
        this.mProgressBar.setVisibility(View.GONE);
        Collections.shuffle(this.mDmVideosList);
//        int i = (int) FirebaseRemoteConfig.getInstance().getDouble("ad_position_videos");
//        for (int i2 = i; i2 < this.mDmVideosList.size(); i2 += i) {
//            DmVideo dmVideo = new DmVideo();
//            dmVideo.setItemVieType(1);
//            this.mDmVideosList.add(i2, dmVideo);
//        }
        DailyVideoAdapter dailymotionVideoAdapter = new DailyVideoAdapter(getContext(), this.mDmVideosList, new CustomItemClickListener() {
            public void onItemClick(View view, final int i) {
                if (view == null) {
                    ((BaseActivity) VideosFragmentVideo.this.getContext()).getStoragePermission(new BaseActivity.PermissionCallBack() {
                        public void permissionGranted() {
                            if (AVD.getINSTANCE().getChannelVideosMap() == null || VideosFragmentVideo.this.mDmVideosList == null) {
                                VideosFragmentVideo.this.showToast("Please Try again!");
                                return;
                            }
                            VideosFragmentVideo videosFragmentVideo = VideosFragmentVideo.this;
                            StringBuilder sb = new StringBuilder();
                            sb.append("https://www.dailymotion.com/video/");
                            sb.append(((DmVideo) VideosFragmentVideo.this.mDmVideosList.get(i)).getId());
                            videosFragmentVideo.fetchVideoQualities(sb.toString());
                        }
                    });
                }
            }

            public void onShareClick(View view, int i) {
                if (VideosFragmentVideo.this.mDmVideosList != null) {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setAction("android.intent.action.SEND");
                    StringBuilder sb = new StringBuilder();
                    sb.append("https://www.dailymotion.com/video/");
                    sb.append(((DmVideo) VideosFragmentVideo.this.mDmVideosList.get(i)).getId());
                    intent.putExtra("android.intent.extra.TEXT", sb.toString());
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.SUBJECT", ((DmVideo) VideosFragmentVideo.this.mDmVideosList.get(i)).getTitle());
                    VideosFragmentVideo.this.startActivity(intent);
                    return;
                }
                VideosFragmentVideo.this.showToast("Please try later!");
            }
        }, TAG, this.mProgressBarLoadMore);
        this.mVideoAdapter = dailymotionVideoAdapter;
        this.mRecyclerVideosList.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mRecyclerVideosList.setAdapter(this.mVideoAdapter);
    }

    private void fetchVideoQualities(String str) {
        DialogUtils.showSimpleProgressDialog(getActivity());
        this.apiInterface.getUrl(str).enqueue(new Callback<Example>() {
            public void onResponse(Call<Example> call, Response<Example> response) {
                DialogUtils.closeProgressDialog();
                if (response != null) {
                    Example example = (Example) response.body();
                    if (example != null) {
                        VideosFragmentVideo.this.manageResponse(example);
                    } else {
                        VideosFragmentVideo.this.showToast("Something went wrong");
                    }
                } else {
                    VideosFragmentVideo.this.showToast("Check Internet Connection");
                }
            }

            public void onFailure(Call<Example> call, Throwable th) {
                DialogUtils.closeProgressDialog();
                Log.i("Video ", th.getMessage());
            }
        });
    }

    private void manageResponse(Example example) {
        if (example != null) {
            List<Format> formats = example.getInfo().getFormats();
            ArrayList arrayList = new ArrayList();
            for (Format format : formats) {
                String formatId = format.getFormatId();
                if (formatId.contains("hls-144-0")) {
                    format.setFormatId("144");
                    arrayList.add(format);
                } else if (formatId.contains("hls-240-0")) {
                    format.setFormatId("240");
                    arrayList.add(format);
                } else if (formatId.contains("hls-380-0")) {
                    format.setFormatId("380");
                    arrayList.add(format);
                } else if (formatId.contains("hls-480-0")) {
                    format.setFormatId("480");
                    arrayList.add(format);
                } else if (formatId.contains("hls-720-0")) {
                    format.setFormatId("720");
                    arrayList.add(format);
                }
            }
            showDM_DownloadSheet(arrayList, example.getInfo().getDescription());
            return;
        }
        Toast.makeText(this.mContext, "SomeThing Wrong Try Again ", Toast.LENGTH_SHORT).show();
    }

    private void showDM_DownloadSheet(List<Format> list, String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        StringBuilder sb123 = new StringBuilder();
        sb123.append("Video_");
        sb123.append(simpleDateFormat.format(new Date()));
        final String sb12 = sb123.toString();
        new BSUtils_D(getContext()).showListSheet(str, "", list, TAG, new BSCallBack() {
            public void onBsHidden() {
            }

            public void onDownloadClicked(Format format) {
                StringBuilder sb = new StringBuilder();
                sb.append(Environment.getExternalStorageDirectory());
                sb.append(File.separator);
                sb.append("Download");
                sb.append(File.separator);
                sb.append("VideoDownloader");
                sb.append(File.separator);
                sb.append("AllVideoDownloads");
                sb.append(File.separator);
                String sb2 = sb.toString();
                Intent intent = new Intent(VideosFragmentVideo.this.getContext(), DownloadService.class);
                intent.putExtra("URL", format.getUrl());
                intent.putExtra("TITLE", sb12);
                intent.putExtra("PATH", sb2);
                new UtilMethods(VideosFragmentVideo.this.getContext()).showCustomToast("Download Started...", 1);
                if (VERSION.SDK_INT >= 26) {
                    VideosFragmentVideo.this.mContext.startForegroundService(intent);
                } else {
                    VideosFragmentVideo.this.mContext.startService(intent);
                }
            }
        });
    }

    private DmChannel getChannel() {
        if (this.mChannel == null) {
            this.mChannel = (DmChannel) getArguments().getSerializable("channel");
        }
        return this.mChannel;
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }










}
