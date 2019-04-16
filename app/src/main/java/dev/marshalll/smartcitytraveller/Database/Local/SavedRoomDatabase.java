package dev.marshalll.smartcitytraveller.Database.Local;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import dev.marshalll.smartcitytraveller.Database.Model.Saved;

@Database(entities = {Saved.class},version = 1,exportSchema = false)
public abstract class SavedRoomDatabase extends RoomDatabase {

    public  abstract SavedDAO savedDAO();

    private  static SavedRoomDatabase instance;

    public static SavedRoomDatabase getInstance(Context context)
    {
        if (instance == null)
            instance = Room.databaseBuilder(context, SavedRoomDatabase.class,"SavedPlacesDB")
                    .allowMainThreadQueries()
                    .build();

        return instance;
    }
}
