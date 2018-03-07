package com.magpiehunt.magpie.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.magpiehunt.magpie.DataAccess.CollectionDao;
import com.magpiehunt.magpie.DataAccess.LandmarkDao;
import com.magpiehunt.magpie.Entities.Collection;
import com.magpiehunt.magpie.Entities.Landmark;

/**
 * Created by James on 1/12/2018.
 */
@Database(entities = {Collection.class, Landmark.class}, version = 5)

public abstract class MagpieDatabase extends RoomDatabase {

    private static MagpieDatabase INSTANCE;

    public static MagpieDatabase getMagpieDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    MagpieDatabase.class, "MagpieDB")
                    //remove the next line before production - queries must be run on worker threads
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration() //TODO remove before release and implement proper migrations, this is just for testing
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract CollectionDao collectionDao();

    public abstract LandmarkDao landmarkDao();



}
