package com.magpiehunt.magpie.DataAccess;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.magpiehunt.magpie.Entities.Collection;

import java.util.List;

/**
 * Created by James on 1/12/2018.
 */

@Dao
public interface CollectionDao {

    @Query("SELECT * FROM Collections WHERE Available = 1")
    List<Collection> getCollections();
    @Query("SELECT * FROM Collections WHERE CID = :cid")
    Collection getCollection(int cid);
    @Update
    void updateCollection(Collection... collections);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCollection(Collection... collections);





}
