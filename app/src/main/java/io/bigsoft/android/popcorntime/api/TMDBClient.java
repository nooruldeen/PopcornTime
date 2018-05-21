package io.bigsoft.android.popcorntime.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TMDBClient {
    public static Retrofit apiRetrofit = null;

    public static Retrofit getClient(String baseUrl){
        if (apiRetrofit == null){
            apiRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return apiRetrofit;
    }
}
