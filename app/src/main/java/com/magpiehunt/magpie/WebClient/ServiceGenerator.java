package com.magpiehunt.magpie.WebClient;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by James on 1/20/2018.
 *
 * This class creates a Retrofit Service
 *
 * An example of how to use this can be found in the SearchCollectionsFragment
 */

//TODO HawkAuthenticationInterceptor by Retrofit
public class ServiceGenerator {


    //Change this URL to testing IP or CMS (once it is functional)
    private static final String BASE_URL = "http://10.104.176.248/api/";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();


    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    public static <S> S createService(
            Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
