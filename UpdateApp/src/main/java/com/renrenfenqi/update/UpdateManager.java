package com.renrenfenqi.update;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.renrenfenqi.update.listener.UpdateListener;
import com.renrenfenqi.update.service.UpdateService;
import com.renrenfenqi.update.utils.AppInfoUtil;

/**
 * 升级管理类
 * Created by zhouweilong on 16/8/22.
 */

public class UpdateManager {

    //日志tag
    public final static String TAG = "UpdateManager";
    private static UpdateManager mUpdateManager;
    public Context mContext;
    //下载地址
    private String downloadUrl;
    //下载通知大图标icon
    private int icoResId = UpdateService.DEFAULT_RES_ID;
    //下载通知小图标icon
    private int icoSmallResId = UpdateService.DEFAULT_RES_ID;
    //默认下载多少间隔发送通知更新
    private int updateProgress = UpdateService.UPDATE_NUMBER_SIZE;
    //下载包的存放路径 默认 sdcard/Android/package/update
    private String storeDir;
    //app 名字
    private String appName;
    //下载中通知flag
    private int downloadNotificationFlag;
    //下载成功通知flag
    private int downloadSuccessNotificationFlag;
    //下载失败通知flag
    private int downloadErrorNotificationFlag;
    //是否发送广播
    private boolean isSendBroadcast = true;
    //是否强制升级
    private boolean isForceUpdate;
    //是否自动安装
    private boolean isAutoInstall = true;

    private AlertDialog mFailDialog;

    private UpdateListener updateListener;//回调接口

    protected UpdateManager(Context mContext) {
        this.mContext = mContext;
    }

    public static UpdateManager create(Context mContext) {
        if (mContext == null) {
            throw new NullPointerException("mContext == null");
        }
        if (mUpdateManager == null) {
            mUpdateManager = new UpdateManager(mContext);
        }
        return mUpdateManager;
    }

    public UpdateManager setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public int getIcoResId() {
        return icoResId;
    }

    public UpdateManager setIcoResId(int icoResId) {
        this.icoResId = icoResId;
        return this;
    }

    /**
     * 设置下载进度
     *
     * @param updateListener
     * @return
     */
    public UpdateManager setUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
        return this;
    }

    public String getAppName() {
        return appName;
    }

    public UpdateManager setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public int getIcoSmallResId() {
        return icoSmallResId;
    }

    public UpdateManager setIcoSmallResId(int icoSmallResId) {
        this.icoSmallResId = icoSmallResId;
        return this;
    }

    public int getUpdateProgress() {
        return updateProgress;
    }

    public UpdateManager setUpdateProgress(int updateProgress) {
        if (updateProgress < 1) {
            throw new IllegalArgumentException("updateProgress < 1");
        }
        this.updateProgress = updateProgress;
        return this;
    }

    public String getStoreDir() {
        return storeDir;
    }

    public UpdateManager setStoreDir(String storeDir) {
        this.storeDir = storeDir;
        return this;
    }

    public int getDownloadNotificationFlag() {
        return downloadNotificationFlag;
    }

    public UpdateManager setDownloadNotificationFlag(int downloadNotificationFlag) {
        this.downloadNotificationFlag = downloadNotificationFlag;
        return this;
    }

    public int getDownloadSuccessNotificationFlag() {
        return downloadSuccessNotificationFlag;
    }

    public UpdateManager setDownloadSuccessNotificationFlag(int downloadSuccessNotificationFlag) {
        this.downloadSuccessNotificationFlag = downloadSuccessNotificationFlag;
        return this;
    }

    public int getDownloadErrorNotificationFlag() {
        return downloadErrorNotificationFlag;
    }

    public UpdateManager setDownloadErrorNotificationFlag(int downloadErrorNotificationFlag) {
        this.downloadErrorNotificationFlag = downloadErrorNotificationFlag;
        return this;
    }

    public boolean isSendBroadcast() {
        return isSendBroadcast;
    }

    public boolean isAutoInstall() {
        return isAutoInstall;
    }

    public UpdateManager setAutoInstall(boolean autoInstall) {
        isAutoInstall = autoInstall;
        return this;
    }

    public UpdateManager setIsSendBroadcast(boolean isSendBroadcast) {
        this.isSendBroadcast = isSendBroadcast;
        return this;
    }

    public boolean isForceUpdate() {
        return isForceUpdate;
    }

    public UpdateManager setForceUpdate(boolean forceUpdate) {
        isForceUpdate = forceUpdate;
        return this;
    }

    public UpdateManager build() {
        if (mContext == null) {
            throw new NullPointerException("context == null");
        }
        //注册广播
        IntentFilter intentFilter = new IntentFilter(UpdateService.ACTION);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        localBroadcastManager.registerReceiver(updateBroadcastReceiver, intentFilter);
        Intent intent = new Intent();
        intent.setClass(mContext, UpdateService.class);
        intent.putExtra(UpdateService.URL, downloadUrl);

        if (icoResId == UpdateService.DEFAULT_RES_ID) {
            icoResId = AppInfoUtil.getIcon(mContext);
        }

        if (icoSmallResId == UpdateService.DEFAULT_RES_ID) {
            icoSmallResId = icoResId;
        }

        intent.putExtra(UpdateService.ICO_RES_ID, icoResId);
        intent.putExtra(UpdateService.STORE_DIR, storeDir);
        intent.putExtra(UpdateService.ICO_SMALL_RES_ID, icoSmallResId);
        intent.putExtra(UpdateService.UPDATE_PROGRESS, updateProgress);
        intent.putExtra(UpdateService.DOWNLOAD_NOTIFICATION_FLAG, downloadNotificationFlag);
        intent.putExtra(UpdateService.DOWNLOAD_SUCCESS_NOTIFICATION_FLAG, downloadSuccessNotificationFlag);
        intent.putExtra(UpdateService.DOWNLOAD_ERROR_NOTIFICATION_FLAG, downloadErrorNotificationFlag);
        intent.putExtra(UpdateService.IS_SEND_BROADCAST, isSendBroadcast);
        intent.putExtra(UpdateService.IS_FORCE_UPDATE, isForceUpdate);
        intent.putExtra(UpdateService.IS_AUTO_INSTALL, isAutoInstall);
        intent.putExtra(UpdateService.APP_NAME, appName);
        mContext.startService(intent);

        return this;

    }

    /**
     * 注销广播
     */
    public void unregisterReceiver() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        localBroadcastManager.unregisterReceiver(updateBroadcastReceiver);
    }

    /**
     * 广播接收器
     */
    public BroadcastReceiver updateBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.v("UpdateService", "updateBroadcastReceiver");
            String action = intent.getAction();
            int status = intent.getIntExtra(UpdateService.STATUS, UpdateService.UPDATE_START_STATUS);
            int progress = intent.getIntExtra(UpdateService.PROGRESS, 0);
            final String filePath = intent.getStringExtra(UpdateService.FILEPATH);
            if (action.equals(UpdateService.ACTION)) {
                switch (status) {
                    case UpdateService.UPDATE_START_STATUS://开始下载

                        if (updateListener != null) {
                            updateListener.start();
                        }
                        break;
                    case UpdateService.UPDATE_PROGRESS_STATUS://下载中
                        if (updateListener != null) {
                            updateListener.update(progress);
                        }
                        break;
                    case UpdateService.UPDATE_SUCCESS_STATUS://成功下载
                        if (updateListener != null) {
                            updateListener.success(filePath);
                        }

                        unregisterReceiver();
                        break;
                    case UpdateService.UPDATE_ERROR_STATUS:// 下载失败
                        if (updateListener != null) {
                            updateListener.error();
                        }
                        unregisterReceiver();
                        showErrorDialog("下载失败", "请检查网络是否正常再重新下载");
                        break;
                }
            }
        }
    };


    /**
     * 失败dialog
     *
     * @param title
     * @param message
     */
    public void showErrorDialog(String title, String message) {
        AlertDialog.Builder failBuilder = new AlertDialog.Builder(mContext);
        failBuilder.setTitle(title);
        failBuilder.setMessage(message);
        failBuilder.setCancelable(false);
        failBuilder.setPositiveButton("重新下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.v(TAG, "点击了重新下载");
                if (mFailDialog != null) {
                    mFailDialog.dismiss();
                }
                UpdateManager.mUpdateManager.build();
            }
        });
        if (!isForceUpdate) {//如果不是强制升级的弹出安装对话框
            failBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.v(TAG, "点击了取消");
                    if (mFailDialog != null && mFailDialog.isShowing()) {
                        mFailDialog.dismiss();
                    }
                }
            });
        }
        mFailDialog = failBuilder.create();
        mFailDialog.show();
    }

}
