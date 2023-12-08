package com.video.victusadownloaders.WatchVideo.util;

import android.content.Context;
import android.content.Intent;

import io.awesome.gagtube.R;
import com.video.victusadownloaders.WatchVideo.util.dialog.DialogUtils;

public class SharedUtils {
	
	public static void shareUrl(Context context) {

		String sharedText = "\uD83C\uDF1F Install now \uD83C\uDF1F\n\n" + "https://play.google.com/store/apps/details?id=" + context.getPackageName();

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
		intent.putExtra(Intent.EXTRA_TEXT, sharedText);
		context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_dialog_title)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}
	
	public static void rateApp(Context context) {
		DialogUtils.showEnjoyAppDialog(context,
									   // positive
									   (dialog, which) -> {
										   // dismiss dialog
										   dialog.dismiss();
										   // show dialog ask to rate
										   DialogUtils.showAskRatingAppDialog(context,
																			  // positive
																			  (dialog1, which1) -> {
																				  // open play store
																				  NavigationHelper.openGooglePlayStore(context, context.getPackageName());
																				  // dismiss dialog
																				  dialog1.dismiss();
																			  },
																			  // negative
																			  (dialog1, which1) -> {
																				  // dismiss dialog
																				  dialog1.dismiss();
																			  });
									   },
									   // negative
									   (dialog, which) -> {
										   // dismiss dialog
										   dialog.dismiss();
										   // show dialog feedback
										   DialogUtils.showFeedBackDialog(context,
																		  // positive
																		  (dialog2, which2) -> {
																			  // open email app
																			  NavigationHelper.composeEmail(context, context.getString(R.string.app_name) + " Android Feedback");
																			  // dismiss dialog
																			  dialog2.dismiss();
																		  },
																		  // negative
																		  (dialog2, which2) -> {
																			  // dismiss dialog
																			  dialog2.dismiss();
																		  });
									   });
	}
}