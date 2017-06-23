package com.example.anlee.searchnews.utils;

import com.example.anlee.searchnews.BuildConfig;
import com.example.anlee.searchnews.model.*;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.anlee.searchnews.model.Constant.BASE_URL;


/**
 * Created by An Lee on 6/21/2017.
 */

public class RetrofitUtils {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final Gson GSON = new Gson();

    public static Retrofit create() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client())
                .build();
    }

    private static OkHttpClient client() {
        return new OkHttpClient.Builder()
                .addInterceptor(apiKeyInterceptor())
                .addInterceptor(responseInterceptor())
                .addInterceptor(loggingInterceptor())
                .build();
    }

    private static Interceptor apiKeyInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url()
                        .newBuilder()
                        .addQueryParameter("api_key", BuildConfig.API_KEY)
                        .build();
                request = request.newBuilder()
                        .url(url)
                        .build();
                return chain.proceed(request);
            }
        };
    }

    private static Interceptor responseInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                ResponseBody body = response.body();
                ApiResponse apiResponse = GSON.fromJson(body.string(), ApiResponse.class);
                body.close();

                response= response.newBuilder()
                        .body(ResponseBody.create(JSON, GSON.toJson(apiResponse.getResponse())))
                        .build();
                return response;
            }
        };
    }

    private static HttpLoggingInterceptor loggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor =new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }
}
