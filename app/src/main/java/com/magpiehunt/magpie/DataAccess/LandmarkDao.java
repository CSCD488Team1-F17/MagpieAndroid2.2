package com.magpiehunt.magpie.DataAccess;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.magpiehunt.magpie.Entities.Landmark;

import java.util.List;

/**
 * Created by James on 1/12/2018.
 */

@Dao
public interface LandmarkDao {

    @Query("SELECT * FROM Landmarks WHERE CID = :cid")
    List<Landmark> getLandmarks(int cid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addLandmark(Landmark... landmarks);

}
