package com.video.victusadownloaders.VideoSaver;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import io.awesome.gagtube.R;
import com.video.victusadownloaders.VideoSaver.app.AVD;
import com.video.victusadownloaders.VideoSaver.downloaderactivities.MessageEvent;
import com.video.victusadownloaders.VideoSaver.model.dailymotion.ChannelsList;
import com.video.victusadownloaders.VideoSaver.model.dailymotion.DmChannel;


public class VideosViewDownActivityVideo extends BaseActivity {
    private static final String TAG = "VideosActivity";
    @BindView(R.id.bottomNavigationVideosActivity)
    BottomNavigationView mBottomNavigationView;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    @BindView(R.id.tabLayoutVideosActivity)
    TabLayout mTabLayout;
    @BindView(R.id.viewPagerVideosActivity)
    ViewPager mViewPager;
    private Toolbar mToolbar;

    PrefManagerVideo prf;

    public static Context context;


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public Fragment getItem(int i) {
            return VideosFragmentVideo.newInstance((DmChannel) AVD.getINSTANCE().getDmChannels().get(i), i);
        }

        public int getCount() {
            return AVD.getINSTANCE().getDmChannels().size();
        }

        public CharSequence getPageTitle(int i) {
            return ((DmChannel) AVD.getINSTANCE().getDmChannels().get(i)).getName();
        }
    }

    public static void start(Context context) {
        ((VideosViewDownActivityVideo) context).startActivityForResult(new Intent(context, VideosViewDownActivityVideo.class), 111);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.xactivity_videos_viewn);
        ButterKnife.bind(this);
        context = this;
        prf = new PrefManagerVideo(getApplicationContext());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if (mToolbar != null) {
            mToolbar.setVisibility(View.VISIBLE);
            mToolbar.setBackgroundColor(getResources().getColor(R.color.new_color_status));
            mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(getString(R.string.app_name));
            }
        }


        if (AVD.getINSTANCE().getDmChannels() == null || AVD.getINSTANCE().getDmChannels().size() == 0) {
            getDmChannelsList(new Callback<ChannelsList>() {
                public void onResponse(Call<ChannelsList> call, Response<ChannelsList> response) {
                    if (((ChannelsList) response.body()).getChannelsList() == null || ((ChannelsList) response.body()).getChannelsList().size() == 0) {
                        Toast.makeText(VideosViewDownActivityVideo.this, "No Videos Found!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    AVD.getINSTANCE().setDmChannels(new LinkedList(((ChannelsList) response.body()).getChannelsList()));
                    VideosViewDownActivityVideo.this.setPagerAdapter();
                }

                public void onFailure(Call<ChannelsList> call, Throwable th) {
                    VideosViewDownActivityVideo videosActivity = VideosViewDownActivityVideo.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error! ");
                    sb.append(th.getMessage());
                    Toast.makeText(videosActivity, sb.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            setPagerAdapter();
        }
        this.mBottomNavigationView.setItemIconTintList(null);
        this.mBottomNavigationView.setSelectedItemId(R.id.menuItemVideosVideosActivity);
        this.mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menuItemHomeVideosActivity) {
                    VideosViewDownActivityVideo.this.onBackPressed();

                }
                return false;
            }
        });
    }



    public void setPagerAdapter() {
        this.mTabLayout.setupWithViewPager(this.mViewPager);
        this.mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        this.mViewPager.setAdapter(this.mSectionsPagerAdapter);
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageSelected(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
                EventBus.getDefault().post(new MessageEvent());
            }
        });
        this.mViewPager.setCurrentItem(0);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }


    public void onPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        VideosViewDownActivityVideo.super.onBackPressed();

    }

}
