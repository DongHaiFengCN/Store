package net;

import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * 网络请求管理，初始化网络配置
 */
public class RxNetWorkManager {
    private static RxNetWorkManager ourInstance;
    private Retrofit retrofit;

    public static RxNetWorkManager getInstance(Context context) {

        if(ourInstance==null){
            synchronized (RxNetWorkManager.class){

                if(ourInstance==null){

                    ourInstance = new RxNetWorkManager();
                    ourInstance.initRetrofit(context);
                }
            }
        }
        return ourInstance;
    }

    /**
     * 初始化必要对象和参数
     */
    private void initRetrofit(Context context) {

        //加入okhttp的日志
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        //打印body级别的数据
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addNetworkInterceptor(new TokenInterceptor())
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
               /* .dns(OkHttpDns.getInstance(context.getApplicationContext()))*/
                .build();

        // 初始化Retrofit

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

    }


    public Retrofit getRetrofit(){

        return retrofit;
    }
}
