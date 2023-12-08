package com.video.victusadownloaders.VideoSaver.DownloaderAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dailymotion.android.player.sdk.PlayerWebView;
import com.dailymotion.android.player.sdk.PlayerWebView.PlayerEventListener;
import com.dailymotion.android.player.sdk.events.FullScreenToggleRequestedEvent;
import com.dailymotion.android.player.sdk.events.PlayerEvent;
import com.video.victusadownloaders.VideoSaver.BrowserVideoActivityVideo;
import com.video.victusadownloaders.VideoSaver.DownloaderAdapters.DailyVideoAdapter.DMViewHolder;
import com.video.victusadownloaders.VideoSaver.FullScreenVideoActivityVideo;
import com.video.victusadownloaders.VideoSaver.Interface.CustomItemClickListener;
import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;
import com.video.victusadownloaders.VideoSaver.SampleActivityVideo;
import com.video.victusadownloaders.VideoSaver.SplashActivityScr;
import com.video.victusadownloaders.VideoSaver.model.dailymotion.DmVideo;
import com.video.victusadownloaders.VideoSaver.utils.NumberUtils;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import butterknife.ButterKnife;
import io.awesome.gagtube.R;

public class DailyVideoAdapter extends Adapter<DMViewHolder> {

    public static final String TAG = "DailymotionVideoAdapter";
    public static RefreshData refreshData;
    //new
    public Context context;
    CustomItemClickListener listener;
    PrefManagerVideo prf;
    int ITEM_AD_TYPE = 13;
    private int adPositioninterval;
    private Context mContext;
    private int mCurrentLimit = 10;
    private PlayerWebView mCurrentPlayer = null;
    private LinkedList<DmVideo> mDmVideosList;
    private boolean mFullscreen = false;
    private boolean mIsLoading;
    private ProgressBar mLoadMoreProgress;
    private int mVideoPlayerPosition = -1;
    private String mWhoIs = "";
    private FrameLayout frameLayout;


    public DailyVideoAdapter(Context context, LinkedList<DmVideo> linkedList, CustomItemClickListener customItemClickListener, String str, ProgressBar progressBar) {
        this.mContext = context;
        this.listener = customItemClickListener;
        this.mDmVideosList = linkedList;
        this.mWhoIs = str;
        setHasStableIds(true);
        this.context = context;
        prf = new PrefManagerVideo(context);

//        if (this.mWhoIs.equalsIgnoreCase(TAG)) {
//            this.adPositioninterval = (int) FirebaseRemoteConfig.getInstance().getDouble("ad_position_trending");
//        } else {
//            this.adPositioninterval = (int) FirebaseRemoteConfig.getInstance().getDouble("ad_position_videos");
//        }
        this.mLoadMoreProgress = progressBar;

    }

    public static void setOnRefreshData(RefreshData refreshData2) {
        refreshData = refreshData2;
    }

    public long getItemId(int i) {
        return (long) i;
    }

    boolean checkEnabled(String tag) {
        return prf.getBoolean(tag);
    }

    public void onViewDetachedFromWindow(DMViewHolder dMViewHolder) {
        if (dMViewHolder != null && dMViewHolder.getItemViewType() == 4 && this.mVideoPlayerPosition != -1 && dMViewHolder.getAdapterPosition() == this.mVideoPlayerPosition) {
            PlayerWebView playerWebView = this.mCurrentPlayer;
            if (playerWebView != null) {
                playerWebView.stopLoading();
                this.mCurrentPlayer.pause();
                this.mCurrentPlayer.getPosition();
                this.mCurrentPlayer = null;
                this.mVideoPlayerPosition = -1;
            }
        }
        super.onViewDetachedFromWindow(dMViewHolder);
    }

    public DMViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        View view;
        @SuppressLint("WrongConstant") LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        view = layoutInflater.inflate(R.layout.item_video_view, viewGroup, false);
        return new DMViewHolder(view);


    }

    public void onBindViewHolder(DMViewHolder dMViewHolder, int position) {
        int i2 = this.mCurrentLimit;
        int i3 = position + 1;
        if (i2 == i3 && i2 <= this.mDmVideosList.size() && !this.mIsLoading) {
            this.mLoadMoreProgress.setVisibility(View.VISIBLE);
            this.mIsLoading = true;
            DailyVideoAdapter.this.loadThumbnail(0, i3);
        }
        DmVideo dmVideo = (DmVideo) this.mDmVideosList.get(position);
        if (this.mWhoIs.equalsIgnoreCase(TAG)) {
            if (dmVideo.getItemVieType() == 2) {
                dMViewHolder.bindTopContainer();
            } else if (dmVideo.getItemVieType() == 3) {
                dMViewHolder.bindSearchButton();
            } else if (dmVideo.getItemVieType() == 1) {
                try {
//                    if (FirebaseRemoteConfig.getInstance().getBoolean("fb_banner_trending_home")) {
//                        dMViewHolder.bindNativeAd();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                int freq = 4;
                int div = position % freq;
                if (div == 0 && position != 0) {
                    dMViewHolder.bindNativeAd();
                } else {
                    dMViewHolder.bindVideo((DmVideo) this.mDmVideosList.get(position));
                }
            }
        } else if (dmVideo.getItemVieType() != 1) {
            int freq = 4;
            int div = position % freq;
            if (div == 0 && position != 0) {
                dMViewHolder.bindNativeAd();
            } else {
                dMViewHolder.bindVideo((DmVideo) this.mDmVideosList.get(position));
            }
        }

    }

    @SuppressLint("CheckResult")
    private void loadThumbnail(final int i, final int i2) {
        if (i <= 2) {
            try {
                if (i2 < this.mDmVideosList.size()) {
                    if (this.mDmVideosList.get(i2) == null) {
                        loadThumbnail(i, i2 + 1);
                    } else {


                        Glide.with(this.mContext).load(((DmVideo) this.mDmVideosList.get(i2)).getThumbnail_480_url()).downloadOnly(new SimpleTarget<File>() {
                            public void onResourceReady(File file, Transition<? super File> glideAnimation) {
                                DailyVideoAdapter.this.loadThumbnail(i + 1, i2 + 1);
                            }

                            public void onLoadFailed(Exception exc, Drawable drawable) {
                                DailyVideoAdapter.this.loadThumbnail(i + 1, i2 + 1);
                            }
                        });
                    }
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                loadThumbnail(i + 1, i2 + 1);
            }
        }
        this.mIsLoading = false;
        this.mCurrentLimit += 10;
        notifyItemChanged(i2 - i);
        this.mLoadMoreProgress.setVisibility(View.GONE);
    }

    public int getItemViewType(int position) {
        int freq = 4;
        int div = position % freq;
        if (div == 0 && position != 0) {
            return ITEM_AD_TYPE;
        } else {
            return ((DmVideo) this.mDmVideosList.get(position)).getItemVieType();
        }
    }

    public int getItemCount() {
        return Math.min(this.mCurrentLimit, this.mDmVideosList.size());
    }

    private void showRateUsIfHaveTo() {
        try {
//            if (new UtilMethods(this.mContext).shouldShowRateUsDialog()) {
//                ((MainActivity) this.mContext).rateUs();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface RefreshData {
        void onRefreshData(PlayerWebView playerWebView);
    }

    public class DMViewHolder extends ViewHolder {
        Button mImgIcDownload;
        ImageView mImgIcPlay;
        ImageView mImgIcShare;
        ImageView mImgThumbnail;
        PlayerWebView mPlayerWebView;
        TextView mTitle;
        TextView mTvTotalLikes;
        TextView mTvTotalViews;
        private LinearLayout dailymotionLayout;
        private LinearLayout fbLayout;
        private LinearLayout instaLayout;
        private Button mBtnSearch;
        private LinearLayout twitterLayout;
        private LinearLayout vimeoLayout;
        private LinearLayout whatsLayout;


        public DMViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindVideo(final DmVideo dmVideo) {
            if (dmVideo != null) {
                init(this.itemView);
                this.mTitle.setText(dmVideo.getTitle());
                this.mTvTotalLikes.setText(NumberUtils.coolFormat(dmVideo.getLikesTotal(), 0));
                this.mTvTotalViews.setText(NumberUtils.coolFormat(dmVideo.getViewsTotal(), 0));
                try {
                    Glide.with(DailyVideoAdapter.this.mContext).load(dmVideo.getThumbnail_480_url()).diskCacheStrategy(DiskCacheStrategy.ALL).into(this.mImgThumbnail);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                this.mImgIcPlay.setOnClickListener(new OnClickListener() {


                    public final void onClick(View view) {


                        Intent i = new Intent(DailyVideoAdapter.this.mContext, SampleActivityVideo.class);
                        i.putExtra("searchid", dmVideo.getId());
                        mContext.startActivity(i);
                        //ADZ

                    }

                });
                this.mImgThumbnail.setOnClickListener(new OnClickListener() {

                    public final void onClick(View view) {

                        Intent i = new Intent(DailyVideoAdapter.this.mContext, SampleActivityVideo.class);
                        i.putExtra("searchid", dmVideo.getId());
                        DailyVideoAdapter.this.mContext.startActivity(i);

                    }
                });
                if (checkEnabled(SplashActivityScr.TAG_DOWNLOAD_BUTTON_ENABLED)) {
                    this.mImgIcDownload.setVisibility(View.VISIBLE);

                } else {
                    this.mImgIcDownload.setVisibility(View.GONE);
                }
                this.mImgIcDownload.setOnClickListener(new OnClickListener() {
                    public final void onClick(View view) {
                        DailyVideoAdapter.this.listener.onItemClick(null, DMViewHolder.this.getAdapterPosition());
                    }
                });
                this.mImgIcShare.setOnClickListener(new OnClickListener() {
                    public final void onClick(View view) {
                        DailyVideoAdapter.this.listener.onShareClick(view, DMViewHolder.this.getAdapterPosition());
                    }
                });
                this.mPlayerWebView.setPlayerEventListener(new PlayerEventListener() {
                    public void onEvent(PlayerEvent playerEvent) {
                        if (playerEvent instanceof FullScreenToggleRequestedEvent) {
                            FullScreenVideoActivityVideo.start(DailyVideoAdapter.this.mContext, dmVideo);
                            DMViewHolder.this.mPlayerWebView.setFullscreenButton(false);
                        }
                    }
                });
            }
        }

        @SuppressLint("WrongConstant")
        private void init(View view) {
            this.mImgThumbnail = (ImageView) view.findViewById(R.id.imgIcThumbnailItemVideo);
            this.mPlayerWebView = (PlayerWebView) view.findViewById(R.id.playerViewItemVideo);
            this.mTitle = (TextView) view.findViewById(R.id.tvVideoTitleItemVideo);
            this.mImgIcShare = (ImageView) view.findViewById(R.id.imgIcShareItemVideo);
            this.mImgIcDownload = (Button) view.findViewById(R.id.imgIcDownloadItemVideo);
            this.mTvTotalViews = (TextView) view.findViewById(R.id.tvTotalViewsItemVideo);
            this.mTvTotalLikes = (TextView) view.findViewById(R.id.tvTotalLikesItemVideo);
            this.mImgIcPlay = (ImageView) view.findViewById(R.id.imgIcPlayItemVideo);
            this.mImgIcPlay.setVisibility(0);
            this.mImgThumbnail.setVisibility(0);
            this.mPlayerWebView.setVisibility(8);
            this.mPlayerWebView.stopLoading();
            this.mPlayerWebView.pause();
            this.mPlayerWebView.getPosition();
        }

        @SuppressLint("WrongConstant")
        private void playVideo(DmVideo dmVideo) {
            DailyVideoAdapter.this.mCurrentPlayer = this.mPlayerWebView;
            DailyVideoAdapter.this.mVideoPlayerPosition = getAdapterPosition();
            this.mImgIcPlay.setVisibility(8);
            this.mImgThumbnail.setVisibility(8);
            this.mPlayerWebView.setVisibility(0);
            this.mPlayerWebView.load(dmVideo.getId(), new HashMap());
            this.mPlayerWebView.setFullscreenButton(true);
            this.mPlayerWebView.getPosition();
            if (DailyVideoAdapter.refreshData != null) {
                DailyVideoAdapter.refreshData.onRefreshData(this.mPlayerWebView);
            }
        }

        public void bindSearchButton() {
            this.mBtnSearch = (Button) this.itemView.findViewById(R.id.btnSearchHome);
            this.mBtnSearch.setOnClickListener(new OnClickListener() {
                public final void onClick(View view) {
                    BrowserVideoActivityVideo.start(DailyVideoAdapter.this.mContext, "");
//                    ADZ
                }

            });
        }

        public void bindTopContainer() {
            this.fbLayout = (LinearLayout) this.itemView.findViewById(R.id.fb_layout);
            this.vimeoLayout = (LinearLayout) this.itemView.findViewById(R.id.vimeo_layout);
            this.whatsLayout = (LinearLayout) this.itemView.findViewById(R.id.whatsapp_layout);
            this.dailymotionLayout = (LinearLayout) this.itemView.findViewById(R.id.dailymotion_layout);
            this.twitterLayout = (LinearLayout) this.itemView.findViewById(R.id.twitter_layout);
            this.instaLayout = (LinearLayout) this.itemView.findViewById(R.id.instagram_layout);
            this.fbLayout.setOnClickListener(new OnClickListener() {
                public final void onClick(View view) {

                }
            });

            this.vimeoLayout.setOnClickListener(new OnClickListener() {

                public final void onClick(View view) {
                    DailyVideoAdapter.this.showRateUsIfHaveTo();
                }
            });
            this.whatsLayout.setOnClickListener(new OnClickListener() {
                public final void onClick(View view) {
                    DailyVideoAdapter.this.showRateUsIfHaveTo();
                }
            });
            this.dailymotionLayout.setOnClickListener(new OnClickListener() {
                public final void onClick(View view) {

                }
            });
            this.twitterLayout.setOnClickListener(new OnClickListener() {
                public final void onClick(View view) {
                    DailyVideoAdapter.this.showRateUsIfHaveTo();
                    BrowserVideoActivityVideo.start(DailyVideoAdapter.this.mContext, "https://www.tubidy.mobi");
                }
            });
            this.instaLayout.setOnClickListener(new OnClickListener() {
                public final void onClick(View view) {
                    DailyVideoAdapter.this.showRateUsIfHaveTo();
                }
            });
        }

        public void bindNativeAd() {

        }

    }

}