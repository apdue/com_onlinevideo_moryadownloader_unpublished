package com.video.victusadownloaders.VideoSaver.p003di.component;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Provider;

import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import com.video.victusadownloaders.VideoSaver.model.DatabaseModel;
import com.video.victusadownloaders.VideoSaver.p003di.module.AppModule;
import com.video.victusadownloaders.VideoSaver.p003di.module.AppModule_ProvideApplicationFactory;
import com.video.victusadownloaders.VideoSaver.p003di.module.AppModule_ProvideDatabaseModelFactory;
import com.video.victusadownloaders.VideoSaver.p003di.module.AppModule_ProvideOkHttpClientFactory;
import com.video.victusadownloaders.VideoSaver.p003di.module.AppModule_ProvideRetrofitBuilderFactory;
import com.video.victusadownloaders.VideoSaver.p003di.module.AppModule_ProvideSharedPreferencesFactory;

/* renamed from: com.downloader.videodownloader.hdvideo.anyvideodownloader.di.component.DaggerAppComponent */
public final class DaggerAppComponent implements AppComponent {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private Provider<Context> provideApplicationProvider;
    private Provider<DatabaseModel> provideDatabaseModelProvider;
    private Provider<OkHttpClient> provideOkHttpClientProvider;
    private Provider<Retrofit> provideRetrofitBuilderProvider;
    private Provider<SharedPreferences> provideSharedPreferencesProvider;

    /* renamed from: com.downloader.videodownloader.hdvideo.anyvideodownloader.di.component.DaggerAppComponent$Builder */
    public static final class Builder {
        /* access modifiers changed from: private */
        public AppModule appModule;

        private Builder() {
        }

        public AppComponent build() {
            if (this.appModule != null) {
                return new DaggerAppComponent(this);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(AppModule.class.getCanonicalName());
            sb.append(" must be set");
            throw new IllegalStateException(sb.toString());
        }

        public Builder appModule(AppModule appModule2) {
            this.appModule = (AppModule) Preconditions.checkNotNull(appModule2);
            return this;
        }
    }

    private DaggerAppComponent(Builder builder) {
        initialize(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    private void initialize(Builder builder) {
        this.provideApplicationProvider = DoubleCheck.provider(AppModule_ProvideApplicationFactory.create(builder.appModule));
        this.provideOkHttpClientProvider = DoubleCheck.provider(AppModule_ProvideOkHttpClientFactory.create(builder.appModule));
        this.provideRetrofitBuilderProvider = DoubleCheck.provider(AppModule_ProvideRetrofitBuilderFactory.create(builder.appModule, this.provideOkHttpClientProvider));
        this.provideSharedPreferencesProvider = DoubleCheck.provider(AppModule_ProvideSharedPreferencesFactory.create(builder.appModule));
        this.provideDatabaseModelProvider = DoubleCheck.provider(AppModule_ProvideDatabaseModelFactory.create(builder.appModule));
    }

    public Context context() {
        return (Context) this.provideApplicationProvider.get();
    }

    public Retrofit retrofit() {
        return (Retrofit) this.provideRetrofitBuilderProvider.get();
    }

    public SharedPreferences sharedPreferences() {
        return (SharedPreferences) this.provideSharedPreferencesProvider.get();
    }
}
