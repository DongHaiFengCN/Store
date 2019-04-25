package net.qn;

import android.util.Log;

import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;

public class QnToken {


    /**
     * code : 0
     * message : OK
     * data : {"token":"uKqKFuKeoY8mdMIuNyooO_nzLzTnEX-s8VDA4bzy:3tosVN4GfregRhphRQ2diIs7zP8=:eyJzY29wZSI6ImRpc2hpbWFnZXMiLCJkZWFkbGluZSI6MTU1NjA5NTE0N30=","expiresIn":0,"region":"pic.yaodiandian.net"}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * token : uKqKFuKeoY8mdMIuNyooO_nzLzTnEX-s8VDA4bzy:3tosVN4GfregRhphRQ2diIs7zP8=:eyJzY29wZSI6ImRpc2hpbWFnZXMiLCJkZWFkbGluZSI6MTU1NjA5NTE0N30=
         * expiresIn : 0
         * region : pic.yaodiandian.net
         */

        private String token;
        private int expiresIn;
        private String region;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(int expiresIn) {
            this.expiresIn = expiresIn;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }
    }



    static UploadManager uploadManager;
    static {

        Configuration config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
                .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);
    }


    /**
     * 上传方法
     * @param file 文件
     * @param token 凭证
     */
    public static void sendToQn(File file, String token){

        uploadManager.put(file, null, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if(info.isOK()) {
                            Log.e("qiniu", "Upload Success");
                        } else {
                            Log.e("qiniu", "Upload Fail");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.e("qiniu", key + ",\r\n " + info + ",\r\n " + res);                    }
                },new UploadOptions(null, null, false, new UpProgressHandler() {
                    @Override
                    public void progress(String s, double v) {
                        Log.e("qiniu", s + ": " + v);
                    }
                },null));




    }
}

