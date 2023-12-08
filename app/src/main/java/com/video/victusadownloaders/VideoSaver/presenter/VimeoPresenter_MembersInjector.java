package com.video.victusadownloaders.VideoSaver.presenter;

import javax.inject.Provider;

import dagger.MembersInjector;
import com.video.victusadownloaders.VideoSaver.model.DownloadModel;

public final class VimeoPresenter_MembersInjector implements MembersInjector<VimeoPresenter> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<DownloadModel> downloadModelProvider;

    public VimeoPresenter_MembersInjector(Provider<DownloadModel> provider) {
        this.downloadModelProvider = provider;
    }

    public static MembersInjector<VimeoPresenter> create(Provider<DownloadModel> provider) {
        return new VimeoPresenter_MembersInjector(provider);
    }

    public void injectMembers(VimeoPresenter vimeoPresenter) {
        if (vimeoPresenter != null) {
            vimeoPresenter.downloadModel = (DownloadModel) this.downloadModelProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }
}
