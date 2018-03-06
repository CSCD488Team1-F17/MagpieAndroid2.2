package com.magpiehunt.magpie.Helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.magpiehunt.magpie.Entities.Landmark;
import com.magpiehunt.magpie.WebClient.ApiService;
import com.magpiehunt.magpie.WebClient.ServiceGenerator;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by James on 3/2/2018.
 */

public class ImageDownloader {
    private final String TAG = "ImageDownloader";

    public void setImage(final ImageView img)
    {
        final Bitmap[] ret = {null};
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<ResponseBody> call = apiService.downloadTestImage();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody rb = response.body();
                img.setImageBitmap(BitmapFactory.decodeStream(response.body().byteStream()));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    public Landmark downloadImage(final Landmark landmark)
    {
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<ResponseBody> call = apiService.downloadTestImage();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody rb = response.body();
                InputStream in = null;
                BufferedInputStream out = null;

                try {
                    byte[] bytes = response.body().bytes();
                    log.e(TAG, "Call");

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log.e(TAG, "Call Failed");
            }
        });
        log.e(TAG,"");
        return landmark;
    }
}
