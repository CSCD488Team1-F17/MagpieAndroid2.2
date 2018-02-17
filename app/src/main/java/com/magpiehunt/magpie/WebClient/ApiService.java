package com.magpiehunt.magpie.WebClient;

import com.magpiehunt.magpie.Entities.Collection;
import com.magpiehunt.magpie.Entities.Landmark;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by James on 1/20/2018.
 */

public interface ApiService {

    @GET("collections")
    Call<List<Collection>> getCollections();

    @GET("landmark/{cid}")
    Call<List<Landmark>> getLandmarks(@Path("cid") Integer cid);

}
