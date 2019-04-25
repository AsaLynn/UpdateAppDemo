package com.ycbjie.update;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.zxn.update.BuildConfig;
import com.zxn.update.PermissionUtils;
import com.zxn.update.UpdateFragment;
import com.zxn.update.UpdateUtils;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    //这个是你的包名
    private static final String apkName = "apk";
    private static final String firstUrl = "http://ucan.25pp.com/Wandoujia_web_seo_baidu_homepage.apk";
    private static final String secondUrl = "http://img1.haowmc.com/hwmc/test/android_apk/2018101027122399.apk";
    private static final String url = "http://img1.haowmc.com/hwmc/test/android_";
    private static final String[] mPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MainActivity","onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MainActivity","onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity","onPause");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },3000);

        PermissionUtils.init(this);
        boolean granted = PermissionUtils.isGranted(mPermission);
        if(!granted){
            PermissionUtils permission = PermissionUtils.permission(mPermission);
            permission.callback(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {

                }
                @Override
                public void onDenied() {
                    PermissionUtils.openAppSettings();
                    Toast.makeText(MainActivity.this,"请允许权限",Toast.LENGTH_SHORT).show();
                }
            });
            permission.request();
        }

        findViewById(R.id.tv_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置自定义下载文件路径
                UpdateUtils.APP_UPDATE_DOWN_APK_PATH = "apk" + File.separator + "downApk";
                String  desc = getResources().getString(R.string.update_content_info);
                /*
                 * @param isForceUpdate             是否强制更新
                 * @param desc                      更新文案
                 * @param url                       下载链接
                 * @param apkFileName               apk下载文件路径名称
                 * @param packName                  包名
                 */
                UpdateFragment.showFragment(MainActivity.this,
                        false,firstUrl,apkName,desc, BuildConfig.APPLICATION_ID);
            }
        });


        findViewById(R.id.tv_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  desc = getResources().getString(R.string.update_content_info1);
                UpdateFragment.showFragment(MainActivity.this,
                        true,firstUrl,apkName,desc, BuildConfig.APPLICATION_ID);
            }
        });

        findViewById(R.id.tv_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  desc = getResources().getString(R.string.update_content_info1);
                UpdateFragment.showFragment(MainActivity.this,
                        false,url,apkName,desc, BuildConfig.APPLICATION_ID);
            }
        });

        findViewById(R.id.tv_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUtils.clearDownload();
            }
        });
    }



}
