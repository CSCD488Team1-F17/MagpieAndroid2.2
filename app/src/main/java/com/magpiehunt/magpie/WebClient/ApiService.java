package com.magpiehunt.magpie.WebClient;

import com.magpiehunt.magpie.Entities.Collection;
import com.magpiehunt.magpie.Entities.Landmark;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by James on 1/20/2018.
 *
 * This interface handles all Retrofit API calls to the CMS
 */

public interface ApiService {

    @GET("collections")
    Call<List<Collection>> getCollections();

    @GET("landmark/{cid}")
    Call<List<Landmark>> getLandmarks(@Path("cid") Integer cid);

    @GET("landmarkimages/{lid}")
    Call<ResponseBody> landmarkImageDownload(@Path("lid") Integer lid);

    @GET("https://sites.ewu.edu/oktoberfest/files/2017/05/JFKLibrary.jpg")
    Call<ResponseBody> downloadTestImage();

}
