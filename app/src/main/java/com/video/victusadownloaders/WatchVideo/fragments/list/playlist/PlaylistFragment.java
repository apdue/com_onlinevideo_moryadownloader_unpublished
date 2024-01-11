package com.video.victusadownloaders.WatchVideo.fragments.list.playlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;
import com.video.victusadownloaders.WatchVideo.database.GAGTubeDatabase;
import com.video.victusadownloaders.WatchVideo.database.playlist.model.PlaylistRemoteEntity;
import com.video.victusadownloaders.WatchVideo.fragments.BackPressable;
import com.video.victusadownloaders.WatchVideo.fragments.list.BaseListInfoFragment;
import com.video.victusadownloaders.WatchVideo.local.playlist.RemotePlaylistManager;
import com.video.victusadownloaders.WatchVideo.player.playqueue.PlayQueue;
import com.video.victusadownloaders.WatchVideo.player.playqueue.PlaylistPlayQueue;
import com.video.victusadownloaders.WatchVideo.report.UserAction;
import com.video.victusadownloaders.WatchVideo.util.AnimationUtils;
import com.video.victusadownloaders.WatchVideo.util.ExtractorHelper;
import com.video.victusadownloaders.WatchVideo.util.Localization;
import com.video.victusadownloaders.WatchVideo.util.NavigationHelper;
import com.video.victusadownloaders.WatchVideo.util.SharedUtils;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.ServiceList;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.playlist.PlaylistInfo;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.ButterKnife;
import io.awesome.gagtube.R;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

;

public class PlaylistFragment extends BaseListInfoFragment<PlaylistInfo> implements BackPressable {
	
	private CompositeDisposable disposables;
	private Subscription bookmarkReactor;
	private AtomicBoolean isBookmarkButtonReady;
	
	private RemotePlaylistManager remotePlaylistManager;
	private PlaylistRemoteEntity playlistEntity;
	
	// Views
	private Toolbar mToolbar;
	private MaterialButton headerPlayAllButton;
	private MaterialButton headerPopupButton;
	private View headerShareButton;
	private MenuItem playlistBookmarkButton;

	PrefManagerVideo prf;


	public static PlaylistFragment getInstance(int serviceId, String url, String name) {
		
		PlaylistFragment instance = new PlaylistFragment();
		instance.setInitialData(serviceId, url, name);
		return instance;
	}
	
	// LifeCycle
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		disposables = new CompositeDisposable();
		isBookmarkButtonReady = new AtomicBoolean(false);
		remotePlaylistManager = new RemotePlaylistManager(GAGTubeDatabase.getInstance(activity));
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_playlist, container, false);
		ButterKnife.bind(this, view);

		prf = new PrefManagerVideo(getActivity());
		
		return view;
	}
	
	@Override
	protected void initViews(View rootView, Bundle savedInstanceState) {
		
		super.initViews(rootView, savedInstanceState);
		
		headerPlayAllButton = rootView.findViewById(R.id.playlist_ctrl_play_all_button);
		headerPopupButton = rootView.findViewById(R.id.playlist_ctrl_play_popup_button);
		headerShareButton = rootView.findViewById(R.id.playlist_ctrl_share);
		
		infoListAdapter.useMiniItemVariants(true);
		
		mToolbar = rootView.findViewById(R.id.default_toolbar);
		activity.getDelegate().setSupportActionBar(mToolbar);
		
		// show ad
		showBannerAd();
	}
	
	@Override
	public void onPause() {
//		if (adView != null) {
//			adView.pause();
//		}
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		showNativeAd();
		showBannerAd();
	}
	
	@Override
	public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
		
		super.onCreateOptionsMenu(menu, inflater);
		
		ActionBar actionBar = activity.getDelegate().getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowTitleEnabled(true);
		}
		
		inflater.inflate(R.menu.menu_playlist, menu);
		
		playlistBookmarkButton = menu.findItem(R.id.menu_item_bookmark);
		updateBookmarkButtons();
	}
	
	@Override
	public void onViewCreated(@NonNull View rootView, Bundle savedInstanceState) {
		
		super.onViewCreated(rootView, savedInstanceState);
		
		mToolbar.setNavigationOnClickListener(view -> onPopBackStack());
	}
	
	@Override
	public void onDestroyView() {
//		if (adView != null) {
//			adView.destroy();
//		}
		super.onDestroyView();
		
		if (isBookmarkButtonReady != null) isBookmarkButtonReady.set(false);
		
		if (disposables != null) disposables.clear();
		if (bookmarkReactor != null) bookmarkReactor.cancel();
		
		bookmarkReactor = null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if (disposables != null) disposables.dispose();
		
		disposables = null;
		remotePlaylistManager = null;
		playlistEntity = null;
		isBookmarkButtonReady = null;
	}
	
	// Load and handle
	@Override
	protected Single<ListExtractor.InfoItemsPage> loadMoreItemsLogic() {
		return ExtractorHelper.getMorePlaylistItems(serviceId, url, currentNextPage);
	}
	
	@Override
	protected Single<PlaylistInfo> loadResult(boolean forceLoad) {
		return ExtractorHelper.getPlaylistInfo(serviceId, url, forceLoad);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == R.id.menu_item_bookmark) {
			onBookmarkClicked();
		}
		return super.onOptionsItemSelected(item);
	}
	
	// Contract
	@Override
	public void showLoading() {
		
		super.showLoading();
		
		AnimationUtils.animateView(itemsList, false, 100);
	}
	
	@Override
	public void handleResult(@NonNull final PlaylistInfo result) {
		
		super.handleResult(result);

		String streamCount = result.getStreamCount() <= 0 ? "∞" : Localization.localizeStreamCount(activity, result.getStreamCount());
		mToolbar.setSubtitle(streamCount);
		
		if (!result.getErrors().isEmpty()) {
			showSnackBarError(result.getErrors(), UserAction.REQUESTED_PLAYLIST, ServiceList.YouTube.getServiceInfo().getName(), result.getUrl(), 0);
		}
		
		remotePlaylistManager.getPlaylist(result)
				.flatMap(lists -> getUpdateProcessor(lists, result), (lists, id) -> lists)
				.onBackpressureLatest()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(getPlaylistBookmarkSubscriber());
		headerPlayAllButton.setOnClickListener(view ->  NavigationHelper.playOnMainPlayer(activity, getPlayQueue(), true));

		headerPopupButton.setOnClickListener(view ->  NavigationHelper.playOnPopupPlayer(activity, getPlayQueue(), false));

		headerShareButton.setOnClickListener(view -> SharedUtils.shareUrl(activity));
	}
	
	private PlayQueue getPlayQueue() {
		return getPlayQueue(0);
	}
	
	private PlayQueue getPlayQueue(final int index) {
		final List<StreamInfoItem> infoItems = new ArrayList<>();
		for (InfoItem i : infoListAdapter.getItemsList()) {
			if (i instanceof StreamInfoItem) {
				infoItems.add((StreamInfoItem) i);
			}
		}
		return new PlaylistPlayQueue(
				currentInfo.getServiceId(),
				currentInfo.getUrl(),
				currentInfo.getNextPage(),
				infoItems,
				index
		);
	}
	
	@Override
	public void handleNextItems(ListExtractor.InfoItemsPage result) {
		
		super.handleNextItems(result);
		
		if (!result.getErrors().isEmpty()) {
			showSnackBarError(result.getErrors(), UserAction.REQUESTED_PLAYLIST, ServiceList.YouTube.getServiceInfo().getName(), "Get next page of: " + url, 0);
		}
	}
	
	// OnError
	@Override
	protected boolean onError(Throwable exception) {
		
		if (super.onError(exception)) return true;
		
		int errorId = exception instanceof ExtractionException ? R.string.parsing_error : R.string.general_error;
		onUnrecoverableError(exception, UserAction.REQUESTED_PLAYLIST, ServiceList.YouTube.getServiceInfo().getName(), url, errorId);
		return true;
	}
	
	// Utils
	private Flowable<Integer> getUpdateProcessor(@NonNull List<PlaylistRemoteEntity> playlists, @NonNull PlaylistInfo result) {
		
		final Flowable<Integer> noItemToUpdate = Flowable.just(-1);
		if (playlists.isEmpty()) return noItemToUpdate;
		
		final PlaylistRemoteEntity playlistEntity = playlists.get(0);
		if (playlistEntity.isIdenticalTo(result)) return noItemToUpdate;
		
		return remotePlaylistManager.onUpdate(playlists.get(0).getUid(), result).toFlowable();
	}
	
	private Subscriber<List<PlaylistRemoteEntity>> getPlaylistBookmarkSubscriber() {
		
		return new Subscriber<List<PlaylistRemoteEntity>>() {
			
			@Override
			public void onSubscribe(Subscription s) {
				
				if (bookmarkReactor != null) bookmarkReactor.cancel();
				bookmarkReactor = s;
				bookmarkReactor.request(1);
			}
			
			@Override
			public void onNext(List<PlaylistRemoteEntity> playlist) {
				
				playlistEntity = playlist.isEmpty() ? null : playlist.get(0);
				
				updateBookmarkButtons();
				isBookmarkButtonReady.set(true);
				
				if (bookmarkReactor != null) bookmarkReactor.request(1);
			}
			
			@Override
			public void onError(Throwable throwable) {
				PlaylistFragment.this.onError(throwable);
			}
			
			@Override
			public void onComplete() {
				
			}
		};
	}
	
	private void onBookmarkClicked() {
		
		if (isBookmarkButtonReady == null || !isBookmarkButtonReady.get() || remotePlaylistManager == null) return;
		
		final Disposable action;
		
		if (currentInfo != null && playlistEntity == null) {
			action = remotePlaylistManager.onBookmark(currentInfo)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(ignored -> Toast.makeText(getContext(), getString(R.string.added_playlist_to_bookmark), Toast.LENGTH_SHORT).show(), this::onError);
		}
		else if (playlistEntity != null) {
			action = remotePlaylistManager.deletePlaylist(playlistEntity.getUid())
					.observeOn(AndroidSchedulers.mainThread())
					.doFinally(() -> playlistEntity = null)
					.subscribe(ignored -> Toast.makeText(getContext(), getString(R.string.removed_playlist_from_bookmark), Toast.LENGTH_SHORT).show(), this::onError);
		}
		else {
			action = Disposables.empty();
		}
		
		disposables.add(action);
	}
	
	private void updateBookmarkButtons() {
		
		if (playlistBookmarkButton == null || activity == null) return;
		
		final int iconAttr = playlistEntity == null ? R.drawable.ic_playlist_add_white_24dp_rand : R.drawable.ic_playlist_add_check_white_rand;
		
		final int titleRes = playlistEntity == null ? R.string.bookmark_playlist : R.string.removed_playlist_from_bookmark;
		
		playlistBookmarkButton.setIcon(iconAttr);
		playlistBookmarkButton.setTitle(titleRes);
	}
	
	private void showNativeAd() {
		// adz
	}
	
	private void showBannerAd() {
	//ADZ

	}
	
	private void onPopBackStack() {
		
		// pop back stack
		if (getFragmentManager() != null) {
			getFragmentManager().popBackStack();
		}
	}
	
	@Override
	public boolean onBackPressed() {
		
		onPopBackStack();
		return true;
	}
}