package com.video.victusadownloaders.VideoSaver.instastorysaver;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import com.video.victusadownloaders.VideoSaver.instastorysaver.data.DataManager;
import com.video.victusadownloaders.VideoSaver.instastorysaver.data.model.LocalDbUser;
import com.video.victusadownloaders.VideoSaver.instastorysaver.data.model.StoriesMediaResponse;
import com.video.victusadownloaders.VideoSaver.instastorysaver.data.model.StoriesMediaResponse.Item;
import com.video.victusadownloaders.VideoSaver.instastorysaver.data.model.StoriesMediaResponse.Item.ImageVersions2.Candidate;
import com.video.victusadownloaders.VideoSaver.instastorysaver.data.model.StoriesMediaResponse.Item.VideoVersion;
import com.video.victusadownloaders.VideoSaver.instastorysaver.network.Headers;
import com.video.victusadownloaders.VideoSaver.instastorysaver.network.RxDownloader;

public class AutoSynApi {
    private static List<do_subscribe> all_subscribers;
    private static int current_item_index;
    private static List<Item> items;
    private final DataManager mDataManager = DataManager.getInstance();
    private Subscription mSubscription;

    class do_subscribe extends Subscriber<Response<StoriesMediaResponse>> {
        private int count = 0;
        private Boolean done = Boolean.valueOf(false);

        public void onError(Throwable th) {
        }

        do_subscribe() {
        }

        public void onCompleted() {
            this.done = Boolean.valueOf(true);
            AutoSynApi.check_all_done();
        }

        public Boolean onDone() {
            return this.done;
        }

        public void onNext(Response<StoriesMediaResponse> response) {
            if (response.code() == 200) {
                AutoSynApi.items.addAll(((StoriesMediaResponse) response.body()).getItems());
                if (AutoSynApi.items != null) {
                    this.count += AutoSynApi.items.size();
                    if (this.count > 0) {
                        PrintStream printStream = System.out;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("***** stories are ******");
                        stringBuilder.append(this.count);
                        printStream.println(stringBuilder.toString());
                        return;
                    }
                    int i = this.count;
                }
            }
        }
    }

    private static class save_subscriber extends Subscriber<String> {
        public void onNext(String str) {
        }

        save_subscriber() {
        }

        public void onCompleted() {
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("***** the item at index ");
            stringBuilder.append(String.valueOf(AutoSynApi.current_item_index));
            stringBuilder.append(" saved successfully");
            printStream.println(stringBuilder.toString());
            AutoSynApi.current_item_index = AutoSynApi.current_item_index + 1;
            if (AutoSynApi.current_item_index < AutoSynApi.items.size()) {
                Item item = (Item) AutoSynApi.items.get(AutoSynApi.current_item_index);
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(Environment.DIRECTORY_DOWNLOADS);
                stringBuilder2.append("/Storygram/");
                AutoSynApi.download(item, 1, stringBuilder2.toString(), new save_subscriber());
            }
        }

        public void onError(Throwable th) {
            th.printStackTrace();
        }
    }

    public AutoSynApi() {
        items = new ArrayList();
        current_item_index = 0;
        all_subscribers = new ArrayList();
    }

    public void synchronise(List<LocalDbUser> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Total number of auto users are ");
        stringBuilder.append(String.valueOf(list.size()));
        Log.d("Fetching Stories", stringBuilder.toString());
        for (int i = 0; i < list.size(); i++) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Total number of auto users are ");
            stringBuilder2.append(String.valueOf(((LocalDbUser) list.get(i)).getUSERNAME()));
            Log.d("Name Stories", stringBuilder2.toString());
            loadMedia(((LocalDbUser) list.get(i)).getUSER_PK());
        }
    }

    public void loadMedia(String str) {
        this.mDataManager.setHeaders(Headers.Client.add(false, "Cookie", InstaStoryApplication.getInstance().getCookieManager().getCookie()));
        do_subscribe do_subscribe = new do_subscribe();
        all_subscribers.add(do_subscribe);
        getReelMediaObservable(str).subscribe(do_subscribe);
    }

    private Observable<Response<StoriesMediaResponse>> getReelMediaObservable(String str) {
        return this.mDataManager.getReelMedia(str).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }

    private static void download(Item item, int i, String str, Subscriber<String> subscriber) {
        String url = getUrl(item);
        RxDownloader instance = RxDownloader.getInstance(InstaStoryApplication.getInstance());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(item.getUser().getUsername());
        stringBuilder.append("_");
        stringBuilder.append(getFilename(url));
        instance.download(url, stringBuilder.toString(), i, str).subscribe(subscriber);
    }

    private static String getUrl(Item item) {
        String str = "";
        int i = Integer.MIN_VALUE;
        switch (item.getMediaType()) {
            case 1:
                for (Candidate candidate : item.getImageVersions2().getCandidates()) {
                    if (candidate.getWidth() > i) {
                        i = candidate.getWidth();
                        str = candidate.getUrl();
                    }
                }
                break;
            case 2:
                for (VideoVersion videoVersion : item.getVideoVersions()) {
                    if (videoVersion.getWidth() > i) {
                        i = videoVersion.getWidth();
                        str = videoVersion.getUrl();
                    }
                }
                break;
        }
        return str;
    }

    private static String getFilename(String str) {
        try {
            str = new URL(str).getPath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new File(str).getName();
    }

    private static void check_all_done() {
        Boolean valueOf = Boolean.valueOf(true);
        for (int i = 0; i < all_subscribers.size(); i++) {
            if (!((do_subscribe) all_subscribers.get(i)).onDone().booleanValue()) {
                valueOf = Boolean.valueOf(false);
                break;
            }
        }
        if (valueOf.booleanValue()) {
            System.out.println("******** all fetching completed  ******");
            if (items.size() > 0) {
                current_item_index = 0;
                Item item = (Item) items.get(current_item_index);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Environment.DIRECTORY_DOWNLOADS);
                stringBuilder.append("/Storygram/");
                download(item, 0, stringBuilder.toString(), new save_subscriber());
            }
        }
    }
}
