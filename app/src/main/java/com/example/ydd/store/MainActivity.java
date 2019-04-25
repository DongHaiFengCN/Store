package com.example.ydd.store;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.session.MediaSession;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import net.BaseObserver;
import net.RxNetWorkManager;
import net.qn.QnService;
import net.qn.QnToken;

import org.json.JSONObject;

import java.io.File;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //6.0以上的系统动态获取读写权限

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }

        /**
         * 分为两步：
         *
         *
         * 第一步：获取token （获取一次就行）
         * 地址：https://www.yaodiandian.net/v1/auth/upload_token （Get请求）
         *（可以使用自己的网络请求，不一定用下边的这种封装）
         *
         *
         * 第二步：把token 和 要上传的文件 file
         * 传入QnToken.sendToQn(file,token);
         *
         */
        RxNetWorkManager.getInstance(getApplicationContext())
                .getRetrofit()
                .create(QnService.class)
                .token()
                .subscribeOn(Schedulers.io())//执行的线程
                .observeOn(AndroidSchedulers.mainThread())//结果返回的线程
                .subscribe(new BaseObserver<QnToken>() {
                    @Override
                    protected void onCustomNext(QnToken t) throws Exception {

                        Log.e("DOAING", t.getMessage());

                        if ("OK".equals(t.getMessage())) {

                            token = t.getData().getToken();

                            Log.e("DOAING", token);


                        } else {

                            throw new Exception("没有得到正确的授权返回");
                        }

                    }
                });


        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //测试的数据
                File file = new File("/sdcard/vlog.txt");

                QnToken.sendToQn(file,token);

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }


}
