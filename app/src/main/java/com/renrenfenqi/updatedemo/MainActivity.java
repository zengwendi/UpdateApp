package com.renrenfenqi.updatedemo;

import android.app.Notification;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.renrenfenqi.update.UpdateManager;
import com.renrenfenqi.update.listener.UpdateListener;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "http://test-1251233192.coscd.myqcloud.com/1_1.apk";
//    private static final String URL = "http://imtt.dd.qq.com/16891/D6904858360641D10AAFAF8D2A901E06.apk?fsname=com.weiclicai.vc_android_2.1.1_6.apk&csr=1bbd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void doClick(View view){
//        versionCheck = new VersionCheck(1, "2.1.1", "发现新版本", "http://imtt.dd.qq.com/16891/D6904858360641D10AAFAF8D2A901E06.apk?fsname=com.weiclicai.vc_android_2.1.1_6.apk&csr=1bbd");
        UpdateManager.create(this)
                .setDownloadUrl(URL)//设置下载apk网络连接
                .setStoreDir("dalong")//设置保存路径
                .setAppName("下载:新版好车库")
                .setDownloadSuccessNotificationFlag(Notification.DEFAULT_ALL)
                .setDownloadErrorNotificationFlag(Notification.DEFAULT_ALL)
                .setAutoInstall(false)//是否自动安装 设置false会弹出安装对话框 但是如果是强制的 设置为true也会弹出对话框
                .setForceUpdate(true)
                .setUpdateListener(new UpdateListener() {
                    @Override
                    public void start() {
                    }

                    @Override
                    public void update(int progress) {
                    }

                    @Override
                    public void success(String filePath) {
                    }

                    @Override
                    public void error() {

                    }
                })
                .build();

    }


    protected void onDestroy() {
        super.onDestroy();
        UpdateManager.create(this).unregisterReceiver();
    }
}
