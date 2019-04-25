package net.download;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 使用Streaming 方式 Retrofit 不会一次性将ResponseBody 读取进入内存
 * 否则文件很多容易OOM
 */
public interface DownloadService {
    @GET
    @Streaming
    Flowable<ResponseBody> download(@Url String url);
    //返回值使用 ResponseBody 之后会对ResponseBody 进行读取
}
