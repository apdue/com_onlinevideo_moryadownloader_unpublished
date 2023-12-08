package com.video.victusadownloaders.VideoSaver.instastorysaver.data;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request.Builder;
import okhttp3.Response;
import rx.Observable;
import com.video.victusadownloaders.VideoSaver.instastorysaver.data.model.SearchUserResponse;
import com.video.victusadownloaders.VideoSaver.instastorysaver.data.model.StoriesMediaResponse;
import com.video.victusadownloaders.VideoSaver.instastorysaver.data.model.StoriesTrayResponse;
import com.video.victusadownloaders.VideoSaver.instastorysaver.data.model.UserInfoResponse;
import com.video.victusadownloaders.VideoSaver.instastorysaver.network.ApiFactory;
import com.video.victusadownloaders.VideoSaver.instastorysaver.network.InstaService;

public class DataManager {
    private static DataManager mInstance;
    private String[] mHeaders;
    private Interceptor mHeadersInterceptListener = new C27621();
    private final InstaService mInstaService = ApiFactory.getInstaService(this.mHeadersInterceptListener);

    class C27621 implements Interceptor {
        C27621() {
        }

        public Response intercept(Chain chain) throws IOException {
            return chain.proceed(newBuilder(chain).headers(Headers.of(DataManager.this.mHeaders)).build());
        }

        @NonNull
        private Builder newBuilder(Chain chain) {
            return chain.request().newBuilder().method(chain.request().method(), chain.request().body());
        }
    }

    private DataManager() {
    }

    public static DataManager getInstance() {
        return mInstance;
    }

    public static void init() {
        if (mInstance == null) {
            mInstance = new DataManager();
        }
    }

    public void setHeaders(String[] strArr) {
        this.mHeaders = strArr;
    }

    public Observable<retrofit2.Response<StoriesTrayResponse>> getReelsTray() {
        return this.mInstaService.getReelsTray();
    }

    public Observable<retrofit2.Response<StoriesMediaResponse>> getReelMedia(String str) {
        return this.mInstaService.getReelMedia(str);
    }

    public Observable<retrofit2.Response<UserInfoResponse>> getUserInfo(String str) {
        return this.mInstaService.getUserInfo(str);
    }

    public Observable<retrofit2.Response<SearchUserResponse>> searchUser(String str, String str2) {
        return this.mInstaService.search(str, str2);
    }
}
