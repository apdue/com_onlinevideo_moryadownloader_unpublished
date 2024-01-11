package com.video.victusadownloaders.WatchVideo.player;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.video.victusadownloaders.WatchVideo.activities.MainActivity;
import com.video.victusadownloaders.WatchVideo.base.BaseActivity;
import com.video.victusadownloaders.WatchVideo.fragments.OnScrollBelowItemsListener;
import com.video.victusadownloaders.WatchVideo.local.dialog.PlaylistAppendDialog;
import com.video.victusadownloaders.WatchVideo.player.event.PlayerEventListener;
import com.video.victusadownloaders.WatchVideo.player.playqueue.PlayQueue;
import com.video.victusadownloaders.WatchVideo.player.playqueue.PlayQueueAdapter;
import com.video.victusadownloaders.WatchVideo.player.playqueue.PlayQueueItem;
import com.video.victusadownloaders.WatchVideo.player.playqueue.PlayQueueItemBuilder;
import com.video.victusadownloaders.WatchVideo.player.playqueue.PlayQueueItemHolder;
import com.video.victusadownloaders.WatchVideo.player.playqueue.PlayQueueItemTouchCallback;
import com.video.victusadownloaders.WatchVideo.util.Constants;
import com.video.victusadownloaders.WatchVideo.util.Localization;
import com.video.victusadownloaders.WatchVideo.util.NavigationHelper;
import com.video.victusadownloaders.WatchVideo.util.SharedUtils;
import com.video.victusadownloaders.WatchVideo.util.ThemeHelper;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.video.victusadownloaders.utilities.AdsWork;

import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.stream.StreamInfo;

import java.util.Collections;
import java.util.List;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;
import io.awesome.gagtube.R;

public abstract class ServicePlayerActivity extends BaseActivity implements PlayerEventListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {
	
	private boolean serviceBound;
	private ServiceConnection serviceConnection;
	
	protected BasePlayer player;
	
	private boolean seeking;
	private boolean redraw;
	
	// Views
	private static final int RECYCLER_ITEM_POPUP_MENU_GROUP_ID = 47;
	private static final int SMOOTH_SCROLL_MAXIMUM_DISTANCE = 80;
	
	private View rootView;
	private RecyclerView itemsList;
	private ItemTouchHelper itemTouchHelper;
	
	private LinearLayout metadata;
	private TextView metadataTitle;
	private TextView metadataArtist;
	
	private SeekBar progressSeekBar;
	private TextView progressCurrentTime;
	private TextView progressEndTime;
	private TextView progressLiveSync;
	private TextView seekDisplay;
	
	private ImageButton repeatButton;
	private ImageButton backwardButton;
	private ImageButton playPauseButton;
	private ImageButton forwardButton;
	private ImageButton shuffleButton;
	private ProgressBar progressBar;

	// Abstracts
	public abstract String getTag();
	
	public abstract String getSupportActionTitle();
	
	public abstract Intent getBindIntent();
	
	public abstract void startPlayerListener();
	
	public abstract void stopPlayerListener();
	
	public abstract boolean onPlayerOptionSelected(MenuItem item);
	
	// Activity Lifecycle
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setTheme(ThemeHelper.getSettingsThemeStyle(this));
		
		setContentView(R.layout.activity_player_queue_control);
		ButterKnife.bind(this);
		
		rootView = findViewById(R.id.main_content);
		
		final Toolbar toolbar = rootView.findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(getSupportActionTitle());
		}
		
		serviceConnection = getServiceConnection();
		bind();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// show ad
		showNativeAd();
		
		if (redraw) {
			recreate();
			redraw = false;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_play_queue, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
			
			case android.R.id.home:
				finish();
				return true;
			
			case R.id.action_append_playlist:
				appendAllToPlaylist();
				return true;
			
			case R.id.action_switch_main:
				this.player.setRecovery();
				getApplicationContext().startActivity(getSwitchIntent(MainActivity.class, MainPlayer.PlayerType.VIDEO)
															  .putExtra(BasePlayer.START_PAUSED, !this.player.isPlaying()));
				return true;
		}
		return onPlayerOptionSelected(item) || super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbind();
	}
	
	protected Intent getSwitchIntent(final Class clazz, final MainPlayer.PlayerType playerType) {
		return NavigationHelper.getPlayerIntent(getApplicationContext(), clazz,
												this.player.getPlayQueue(), this.player.getRepeatMode(),
												this.player.getPlaybackSpeed(), this.player.getPlaybackPitch(),
												this.player.getPlaybackSkipSilence(),
												null,
												true,
												!this.player.isPlaying(),
												this.player.isMuted())
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				.putExtra(Constants.KEY_LINK_TYPE, StreamingService.LinkType.STREAM)
				.putExtra(Constants.KEY_URL, this.player.getVideoUrl())
				.putExtra(Constants.KEY_TITLE, this.player.getVideoTitle())
				.putExtra(Constants.KEY_SERVICE_ID, this.player.getCurrentMetadata().getMetadata().getServiceId())
				.putExtra(VideoPlayer.PLAYER_TYPE, playerType);
	}
	
	// Service Connection
	private void bind() {
		final boolean success = bindService(getBindIntent(), serviceConnection, BIND_AUTO_CREATE);
		if (!success) {
			unbindService(serviceConnection);
		}
		serviceBound = success;
	}
	
	private void unbind() {
		if (serviceBound) {
			unbindService(serviceConnection);
			serviceBound = false;
			stopPlayerListener();
			
			if (player != null && player.getPlayQueueAdapter() != null) {
				player.getPlayQueueAdapter().unsetSelectedListener();
			}
			if (itemsList != null) itemsList.setAdapter(null);
			if (itemTouchHelper != null) itemTouchHelper.attachToRecyclerView(null);
			
			itemsList = null;
			itemTouchHelper = null;
			player = null;
		}
	}
	
	private ServiceConnection getServiceConnection() {
		
		return new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				// unimplemented
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				if (service instanceof PlayerServiceBinder) {
					player = ((PlayerServiceBinder) service).getPlayerInstance();
				}
				else if (service instanceof MainPlayer.LocalBinder) {
					player = ((MainPlayer.LocalBinder) service).getPlayer();
				}
				
				if (player == null || player.getPlayQueue() == null || player.getPlayQueueAdapter() == null || player.getPlayer() == null) {
					unbind();
					finish();
				}
				else {
					buildComponents();
					startPlayerListener();
				}
			}
		};
	}
	
	// Component Building
	private void buildComponents() {
		buildQueue();
		buildMetadata();
		buildSeekBar();
		buildControls();
	}
	
	private void buildQueue() {
		
		itemsList = findViewById(R.id.play_queue);
		itemsList.setLayoutManager(new LinearLayoutManager(this));
		itemsList.setAdapter(player.getPlayQueueAdapter());
		itemsList.setClickable(true);
		itemsList.setLongClickable(true);
		itemsList.clearOnScrollListeners();
		itemsList.addOnScrollListener(getQueueScrollListener());
		
		itemTouchHelper = new ItemTouchHelper(getItemTouchCallback());
		itemTouchHelper.attachToRecyclerView(itemsList);
		
		player.getPlayQueueAdapter().setSelectedListener(getOnSelectedListener());
	}
	
	private void buildMetadata() {
		
		metadata = rootView.findViewById(R.id.metadata);
		metadataTitle = rootView.findViewById(R.id.song_name);
		metadataArtist = rootView.findViewById(R.id.artist_name);
		
		metadata.setOnClickListener(this);
		metadataTitle.setSelected(true);
		metadataArtist.setSelected(true);
	}
	
	private void buildSeekBar() {
		
		progressCurrentTime = rootView.findViewById(R.id.current_time);
		progressSeekBar = rootView.findViewById(R.id.seek_bar);
		progressEndTime = rootView.findViewById(R.id.end_time);
		progressLiveSync = rootView.findViewById(R.id.live_sync);
		seekDisplay = rootView.findViewById(R.id.seek_display);
		
		progressSeekBar.setOnSeekBarChangeListener(this);
		progressLiveSync.setOnClickListener(this);
	}
	
	private void buildControls() {
		
		repeatButton = rootView.findViewById(R.id.control_repeat);
		backwardButton = rootView.findViewById(R.id.control_backward);
		playPauseButton = rootView.findViewById(R.id.control_play_pause);
		forwardButton = rootView.findViewById(R.id.control_forward);
		shuffleButton = rootView.findViewById(R.id.control_shuffle);
		progressBar = rootView.findViewById(R.id.control_progress_bar);
		
		repeatButton.setOnClickListener(this);
		backwardButton.setOnClickListener(this);
		playPauseButton.setOnClickListener(this);
		forwardButton.setOnClickListener(this);
		shuffleButton.setOnClickListener(this);
	}
	
	private void buildItemPopupMenu(final PlayQueueItem item, final View view) {
		
		final PopupMenu menu = new PopupMenu(this, view, Gravity.END, 0, R.style.mPopupMenu);
		
		final MenuItem detail = menu.getMenu().add(RECYCLER_ITEM_POPUP_MENU_GROUP_ID, 1,
												   Menu.NONE, R.string.play_queue_stream_detail);
		detail.setOnMenuItemClickListener(menuItem -> {
			onOpenDetail(item.getServiceId(), item.getUrl(), item.getTitle());
			return true;
		});
		
		// add to playlist
		final MenuItem append = menu.getMenu().add(RECYCLER_ITEM_POPUP_MENU_GROUP_ID, 0, Menu.NONE, R.string.append_playlist);
		append.setOnMenuItemClickListener(menuItem -> {
			openPlaylistAppendDialog(Collections.singletonList(item));
			return true;
		});
		
		// share
		final MenuItem share = menu.getMenu().add(RECYCLER_ITEM_POPUP_MENU_GROUP_ID, 1, Menu.NONE, R.string.share);
		share.setOnMenuItemClickListener(menuItem -> {
			SharedUtils.shareUrl(this);
			return true;
		});
		
		// remove
		final MenuItem remove = menu.getMenu().add(RECYCLER_ITEM_POPUP_MENU_GROUP_ID, 2, Menu.NONE, R.string.play_queue_remove);
		remove.setOnMenuItemClickListener(menuItem -> {
			if (player == null) return false;
			final int index = player.getPlayQueue().indexOf(item);
			if (index != -1) player.getPlayQueue().remove(index);
			return true;
		});
		
		menu.show();
	}
	
	// Component Helpers
	private OnScrollBelowItemsListener getQueueScrollListener() {
		
		return new OnScrollBelowItemsListener() {
			
			@Override
			public void onScrolledDown(RecyclerView recyclerView) {
				
				if (player != null && player.getPlayQueue() != null && !player.getPlayQueue().isComplete()) {
					player.getPlayQueue().fetch();
				}
				else if (itemsList != null) {
					itemsList.clearOnScrollListeners();
				}
			}
		};
	}
	
	private ItemTouchHelper.SimpleCallback getItemTouchCallback() {
		
		return new PlayQueueItemTouchCallback() {
			
			@Override
			public void onMove(int sourceIndex, int targetIndex) {
				if (player != null) player.getPlayQueue().move(sourceIndex, targetIndex);
			}
			
			@Override
			public void onSwiped(int index) {
				if (index != -1) {
					player.getPlayQueue().remove(index);
				}
			}
		};
	}
	
	private PlayQueueItemBuilder.OnSelectedListener getOnSelectedListener() {
		
		return new PlayQueueItemBuilder.OnSelectedListener() {
			
			@Override
			public void selected(PlayQueueItem item, View view) {
				
				if (player != null) {
					player.onSelected(item);
					// show ad
					showNativeAd();
				}
			}
			
			@Override
			public void held(PlayQueueItem item, View view) {
				
				if (player == null) return;
				
				final int index = player.getPlayQueue().indexOf(item);
				if (index != -1) buildItemPopupMenu(item, view);
			}
			
			@Override
			public void onStartDrag(PlayQueueItemHolder viewHolder) {
				if (itemTouchHelper != null) itemTouchHelper.startDrag(viewHolder);
			}
		};
	}
	
	private void onOpenDetail(final int serviceId, final String videoUrl, final String videoTitle) {
		NavigationHelper.openVideoDetail(this, serviceId, videoUrl, videoTitle);
	}
	
	private void scrollToSelected() {
		
		if (player == null) return;
		
		final int currentPlayingIndex = player.getPlayQueue().getIndex();
		final int currentVisibleIndex;
		if (itemsList.getLayoutManager() instanceof LinearLayoutManager) {
			final LinearLayoutManager layout = ((LinearLayoutManager) itemsList.getLayoutManager());
			currentVisibleIndex = layout.findFirstVisibleItemPosition();
		}
		else {
			currentVisibleIndex = 0;
		}
		
		final int distance = Math.abs(currentPlayingIndex - currentVisibleIndex);
		if (distance < SMOOTH_SCROLL_MAXIMUM_DISTANCE) {
			itemsList.smoothScrollToPosition(currentPlayingIndex);
		}
		else {
			itemsList.scrollToPosition(currentPlayingIndex);
		}
	}
	
	// Component On-Click Listener
	@Override
	public void onClick(View view) {
		
		if (player == null) return;
		
		if (view.getId() == repeatButton.getId()) {
			player.onRepeatClicked();
			// show ad
			showNativeAd();
		}
		else if (view.getId() == backwardButton.getId()) {
			player.onPlayPrevious();
			// show ad
			showNativeAd();
		}
		else if (view.getId() == playPauseButton.getId()) {
			player.onPlayPause();
			// show ad
			showNativeAd();
		}
		else if (view.getId() == forwardButton.getId()) {
			player.onPlayNext();
			// show ad
			showNativeAd();
		}
		else if (view.getId() == shuffleButton.getId()) {
			player.onShuffleClicked();
			// show ad
			showNativeAd();
		}
		else if (view.getId() == metadata.getId()) {
			scrollToSelected();
		}
		else if (view.getId() == progressLiveSync.getId()) {
			player.seekToDefault();
		}
	}
	
	// Seekbar Listener
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (fromUser) {
			final String seekTime = Localization.getDurationString(progress / 1000);
			progressCurrentTime.setText(seekTime);
			seekDisplay.setText(seekTime);
		}
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		seeking = true;
		seekDisplay.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (player != null) player.seekTo(seekBar.getProgress());
		seekDisplay.setVisibility(View.GONE);
		seeking = false;
	}
	
	// Playlist append
	private void appendAllToPlaylist() {
		if (player != null && player.getPlayQueue() != null) {
			openPlaylistAppendDialog(player.getPlayQueue().getStreams());
		}
	}
	
	private void openPlaylistAppendDialog(final List<PlayQueueItem> playlist) {
		PlaylistAppendDialog.fromPlayQueueItems(playlist).show(getSupportFragmentManager(), getTag());
	}
	
	// Binding Service Listener
	
	@Override
	public void onQueueUpdate(final PlayQueue queue) {
	}
	
	@Override
	public void onPlaybackUpdate(int state, int repeatMode, boolean shuffled, PlaybackParameters parameters) {
		
		onStateChanged(state);
		onPlayModeChanged(repeatMode, shuffled);
		onMaybePlaybackAdapterChanged();
	}
	
	@Override
	public void onProgressUpdate(int currentProgress, int duration, int bufferPercent) {
		
		// Set buffer progress
		progressSeekBar.setSecondaryProgress((int) (progressSeekBar.getMax() * ((float) bufferPercent / 100)));
		
		// Set Duration
		progressSeekBar.setMax(duration);
		progressEndTime.setText(Localization.getDurationString(duration / 1000));
		
		// Set current time if not seeking
		if (!seeking) {
			progressSeekBar.setProgress(currentProgress);
			progressCurrentTime.setText(Localization.getDurationString(currentProgress / 1000));
		}
		
		if (player != null) {
			progressLiveSync.setClickable(!player.isLiveEdge());
		}
		
		final ViewGroup.LayoutParams currentTimeParams = progressCurrentTime.getLayoutParams();
		currentTimeParams.width = progressEndTime.getWidth();
		progressCurrentTime.setLayoutParams(currentTimeParams);
	}
	
	@Override
	public void onMetadataUpdate(StreamInfo info, final PlayQueue queue) {
		if (info != null) {
			metadataTitle.setText(info.getName());
			metadataArtist.setText(info.getUploaderName());
			
			progressEndTime.setVisibility(View.GONE);
			progressLiveSync.setVisibility(View.GONE);
			switch (info.getStreamType()) {
				case LIVE_STREAM:
				case AUDIO_LIVE_STREAM:
					progressLiveSync.setVisibility(View.VISIBLE);
					break;
				default:
					progressEndTime.setVisibility(View.VISIBLE);
					break;
			}
			
			scrollToSelected();
		}
	}
	
	@Override
	public void onServiceStopped() {
		unbind();
		finish();
	}
	
	// Binding Service Helper
	private void onStateChanged(final int state) {
		switch (state) {
			case BasePlayer.STATE_PAUSED:
				playPauseButton.setImageResource(R.drawable.ic_play_arrow_white_24dp_rand);
				break;
			
			case BasePlayer.STATE_PLAYING:
				playPauseButton.setImageResource(R.drawable.ic_pause_white_24dp_rand);
				break;
			
			case BasePlayer.STATE_COMPLETED:
				playPauseButton.setImageResource(R.drawable.ic_replay_white_24dp_rand);
				break;
			
			default:
				break;
		}
		
		switch (state) {
			case BasePlayer.STATE_PAUSED:
			case BasePlayer.STATE_PLAYING:
			case BasePlayer.STATE_COMPLETED:
				playPauseButton.setClickable(true);
				playPauseButton.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				break;
			
			default:
				playPauseButton.setClickable(false);
				playPauseButton.setVisibility(View.INVISIBLE);
				progressBar.setVisibility(View.VISIBLE);
				break;
		}
	}
	
	private void onPlayModeChanged(final int repeatMode, final boolean shuffled) {
		
		switch (repeatMode) {
			
			case Player.REPEAT_MODE_OFF:
				repeatButton.setImageResource(R.drawable.controls_repeat_off_rand);
				break;
			
			case Player.REPEAT_MODE_ONE:
				repeatButton.setImageResource(R.drawable.controls_repeat_one_rand);
				break;
			
			case Player.REPEAT_MODE_ALL:
				repeatButton.setImageResource(R.drawable.controls_repeat_all_rand);
				break;
		}
		
		final int shuffleAlpha = shuffled ? 255 : 77;
		shuffleButton.setImageAlpha(shuffleAlpha);
	}
	
	private void onMaybePlaybackAdapterChanged() {
		
		if (itemsList == null || player == null) return;
		final PlayQueueAdapter maybeNewAdapter = player.getPlayQueueAdapter();
		if (maybeNewAdapter != null && itemsList.getAdapter() != maybeNewAdapter) {
			itemsList.setAdapter(maybeNewAdapter);
		}
	}
	
	private void showNativeAd() {
		AdsWork.loadNativeAd(ServicePlayerActivity.this, findViewById(R.id.template_view), R.layout.ad_unified_small);
	}
}