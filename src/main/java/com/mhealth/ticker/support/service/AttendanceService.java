package com.mhealth.ticker.support.service;

import com.google.gson.Gson;
import com.mhealth.ticker.support.data.Attendances;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public interface AttendanceService {

    @GET("attendances")
    Call<Attendances> getAttendances();

    class Factory {

        private static final String BASE_URL = "http://vn.manadrdev.com:5000/";

        static final Logger logger = Logger.getLogger(Factory.class.getName());

        private static long COUNTER = 0;

        public static AttendanceService create() {
            return create(GsonConverterFactory.create());
        }

        public static AttendanceService create(Converter.Factory factory) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder();
                        builder.method(original.method(), original.body());
                        return chain.proceed(builder.build());
                    })
                    .connectTimeout(50, TimeUnit.SECONDS)
                    .writeTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(factory)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            return retrofit.create(AttendanceService.class);
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.print(new Gson().toJson(AttendanceService.Factory.create().getAttendances().execute().body()));
    }
}
