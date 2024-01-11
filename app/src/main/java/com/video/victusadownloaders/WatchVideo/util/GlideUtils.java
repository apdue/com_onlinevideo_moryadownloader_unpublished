package com.video.victusadownloaders.WatchVideo.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.awesome.gagtube.R;

public class GlideUtils {
	
	public static void loadAvatar(Context context, ImageView imageView, String imageUrl) {
		Glide.with(context).load(imageUrl)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.placeholder(R.drawable.user_default_rand)
				.error(R.drawable.user_default_rand)
				.fallback(R.drawable.user_default_rand)
				.into(imageView);
	}
	
	public static void loadThumbnail(Context context, ImageView imageView, String imageUrl) {
		Glide.with(context).load(imageUrl)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.placeholder(R.drawable.no_image_rand)
				.error(R.drawable.no_image_rand)
				.fallback(R.drawable.no_image_rand)
				.into(imageView);
	}
	
	public static void loadChannelBanner(Context context, ImageView imageView, String imageUrl) {
		Glide.with(context).load(imageUrl)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.placeholder(R.drawable.channel_banner_rand)
				.error(R.drawable.channel_banner_rand)
				.fallback(R.drawable.channel_banner_rand)
				.into(imageView);
	}
}
