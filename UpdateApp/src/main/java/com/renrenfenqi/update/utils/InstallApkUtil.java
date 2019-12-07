package com.renrenfenqi.update.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * 安装apk工具类
 * Created by zhouweilong on 16/8/22.
 */

public class InstallApkUtil {

    /**
     * 安装intent
     *
     * @param path
     * @return
     */
    public static Intent installIntent(Context context, String path) {
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        FileProviderUtil.setIntentDataAndType(context, installIntent, "application/vnd.android.package-archive", new File(path), true);
        return installIntent;
    }

    /**
     * 跳转浏览器下载intent
     *
     * @param downloadUrl
     * @return
     */
    public static Intent webLauncher(String downloadUrl) {
        Uri download = Uri.parse(downloadUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, download);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}
