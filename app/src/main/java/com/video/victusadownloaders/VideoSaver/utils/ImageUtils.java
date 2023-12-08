package com.video.victusadownloaders.VideoSaver.utils;

import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

public class ImageUtils {
    public static String getMimeType(File file) {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString()));
    }
}
