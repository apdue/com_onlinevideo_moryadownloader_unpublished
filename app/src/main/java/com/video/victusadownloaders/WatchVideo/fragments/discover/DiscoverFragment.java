package com.video.victusadownloaders.WatchVideo.fragments.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;
import com.video.victusadownloaders.VideoSaver.SplashActivityScr;
import com.video.victusadownloaders.WatchVideo.fragments.discover.adapter.TopViewPagerAdapter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.awesome.gagtube.R;
import com.video.victusadownloaders.WatchVideo.base.BaseFragment;
import com.video.victusadownloaders.WatchVideo.util.NavigationHelper;
import com.video.victusadownloaders.WatchVideo.util.ServiceHelper;
import com.video.victusadownloaders.utilities.AdsWork;


public class DiscoverFragment extends BaseFragment {
	
	@BindView(R.id.tab_layout) TabLayout tabLayout;
	@BindView(R.id.view_pager) ViewPager viewPager;
	@BindView(R.id.fab_play) ExtendedFloatingActionButton fab;
	private TopViewPagerAdapter adapter;

	PrefManagerVideo prf;
	
	public static DiscoverFragment getInstance() {
		return new DiscoverFragment();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TopViewPagerAdapter
		adapter = new TopViewPagerAdapter(getChildFragmentManager(), activity);
	}
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_discover, container, false);
		ButterKnife.bind(this, view);
		AdsWork.showInterAds(getActivity(), new AdsWork.AdFinished() {
			@Override
			public void onAdFinished() {

			}
		});
		prf = new PrefManagerVideo(getActivity());

		if (!prf.getBoolean(SplashActivityScr.TAG_YOUTUBE_SETTING_ENABLED)) {
			ImageButton imageButton = view.findViewById(R.id.action_settings);
			imageButton.setVisibility(View.GONE);
		}


		return view;
	}
	
	@Override
	protected void initViews(View rootView, Bundle savedInstanceState) {
		super.initViews(rootView, savedInstanceState);
		initAdapter();
	}
	
	private void initAdapter() {
		// set adapter to viewPager
		viewPager.setAdapter(adapter);
		// setup tabLayout with viewPager
		tabLayout.setupWithViewPager(viewPager);
	}
	
	@OnClick(R.id.action_search)
	void onSearch() {
		// open SearchFragment
		NavigationHelper.openSearchFragment(getFM(), ServiceHelper.getSelectedServiceId(activity), "");
	}
	
	@OnClick(R.id.action_settings)
	void onSettings() {
		// open Settings
		NavigationHelper.openSettings(activity);
	}
	
	@OnClick(R.id.fab_play)
	void playAll() {
		TopFragment fragment = (TopFragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
		fragment.playAll();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
