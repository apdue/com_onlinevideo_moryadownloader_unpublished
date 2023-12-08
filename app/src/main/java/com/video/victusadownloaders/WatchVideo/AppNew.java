package com.video.victusadownloaders.WatchVideo;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.multidex.MultiDexApplication;
import androidx.preference.PreferenceManager;

import com.video.victusadownloaders.WatchVideo.activities.ReCaptchaActivity;
import com.video.victusadownloaders.WatchVideo.download.DownloaderImpl;
import com.video.victusadownloaders.WatchVideo.notification.NotificationOreo;
import com.video.victusadownloaders.WatchVideo.settings.GAGTubeSettings;
import com.google.firebase.FirebaseApp;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.downloader.Downloader;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

import io.awesome.gagtube.R;

import com.video.victusadownloaders.WatchVideo.util.ExtractorHelper;
import com.video.victusadownloaders.WatchVideo.util.Localization;
import com.video.victusadownloaders.WatchVideo.util.StateSaver;

import io.reactivex.annotations.NonNull;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

@SuppressLint("Registered")
public class AppNew extends MultiDexApplication {

	public static final String CHANEL_ID = "Alertify";

	private Intent downloadService;

	public static Context applicationContext;

	public static Context getAppContext() {
		return applicationContext;
	}

	private static AppNew a;

	public AppNew() {
		a = this;
	}
	public static Context getContext() {
		return a;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();

		applicationContext = this;
		downloadService = new Intent(getApplicationContext(), DownloadManager.class);
		createNotificationChangel();
		// initialize settings first because others inits can use its values
		GAGTubeSettings.initSettings(this);
		
		// initialize localization
		NewPipe.init(getDownloader(),
					 Localization.getPreferredLocalization(this),
					 Localization.getPreferredContentCountry(this));
		Localization.init();
		StateSaver.init(this);
		
		// image loader
		ImageLoader.getInstance().init(getImageLoaderConfigurations());
		
		// initialize notification channels for android-o
		NotificationOreo.init(this);
		initNotificationChannel();
		
		// initialize firebase
		FirebaseApp.initializeApp(this);
		
		configureRxJavaErrorHandler();

	}
	public Intent getDownloadService() {
		return downloadService;
	}

	private void createNotificationChangel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel serviceChanel = new NotificationChannel(CHANEL_ID, getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
			NotificationManager manager = getSystemService(NotificationManager.class);
			manager.createNotificationChannel(serviceChanel);
		}
	}
	protected Downloader getDownloader() {
		DownloaderImpl downloader = DownloaderImpl.init(null);
		setCookiesToDownloader(downloader);
		return downloader;
	}
	
	protected void setCookiesToDownloader(final DownloaderImpl downloader) {
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		final String key = getApplicationContext().getString(R.string.recaptcha_cookies_key);
		downloader.setCookie(ReCaptchaActivity.RECAPTCHA_COOKIES_KEY, prefs.getString(key, ""));
		downloader.updateYoutubeRestrictedModeCookies(getApplicationContext());
	}
	
	private void configureRxJavaErrorHandler() {
		
		// https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#error-handling
		RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
			
			@Override
			public void accept(@NonNull Throwable throwable) {
				
				if (throwable instanceof UndeliverableException) {
					// As UndeliverableException is a wrapper, get the cause of it to get the "real" exception
					throwable = throwable.getCause();
				}
				
				final List<Throwable> errors;
				if (throwable instanceof CompositeException) {
					errors = ((CompositeException) throwable).getExceptions();
				}
				else {
					errors = Collections.singletonList(throwable);
				}
				
				for (final Throwable error : errors) {
					if (isThrowableIgnored(error)) return;
					if (isThrowableCritical(error)) {
						reportException(error);
						return;
					}
				}
			}
			
			private boolean isThrowableIgnored(@NonNull final Throwable throwable) {
				
				// Don't crash the application over a simple network problem
				return ExtractorHelper.hasAssignableCauseThrowable(throwable, IOException.class, SocketException.class, // network api cancellation
																   InterruptedException.class, InterruptedIOException.class); // blocking code disposed
			}
			
			private boolean isThrowableCritical(@NonNull final Throwable throwable) {
				
				// Though these exceptions cannot be ignored
				return ExtractorHelper.hasAssignableCauseThrowable(throwable,
																   NullPointerException.class, IllegalArgumentException.class, // bug in app
																   OnErrorNotImplementedException.class, MissingBackpressureException.class,
																   IllegalStateException.class); // bug in operator
			}
			
			private void reportException(@NonNull final Throwable throwable) {
				
				// Throw uncaught exception that will trigger the report system
				Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), throwable);
			}
		});
	}
	
	private ImageLoaderConfiguration getImageLoaderConfigurations() {
		
		return new ImageLoaderConfiguration.Builder(this)
				.memoryCache(new LRULimitedMemoryCache(100 * 1024 * 1024))
				.diskCacheSize(500 * 1024 * 1024)
				.build();
	}
	
	public void initNotificationChannel() {
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
			return;
		}
		
		final String id = getString(R.string.notification_channel_id);
		final CharSequence name = getString(R.string.notification_channel_name);
		final String description = getString(R.string.notification_channel_description);
		
		// Keep this below DEFAULT to avoid making noise on every notification update
		final int importance = NotificationManager.IMPORTANCE_LOW;
		
		NotificationChannel mChannel = new NotificationChannel(id, name, importance);
		mChannel.setDescription(description);
		
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		if (mNotificationManager != null) {
			mNotificationManager.createNotificationChannel(mChannel);
		}
	}
}
