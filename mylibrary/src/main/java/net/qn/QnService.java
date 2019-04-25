package net.qn;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface QnService {

    @GET("v1/auth/upload_token")
    Observable<QnToken> token();
}
