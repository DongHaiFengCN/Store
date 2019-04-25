package com.example.ydd.store;

import net.User;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LoginService {

    @GET("/login")
    Observable<User> login(@Query("key") String key,@Query("phone") String phone,@Query("passwd") String passwd);

    @GET("/hello")
    Observable<ResponseBody> login1();


}
