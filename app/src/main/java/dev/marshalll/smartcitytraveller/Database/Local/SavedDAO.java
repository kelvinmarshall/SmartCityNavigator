package dev.marshalll.smartcitytraveller.Database.Local;


import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import dev.marshalll.smartcitytraveller.Database.Model.Saved;
import io.reactivex.Flowable;

@Dao
public interface SavedDAO {

    @Query("SELECT * FROM saved")
    Flowable<List<Saved>> getSavItems();

    @Query("SELECT EXISTS (SELECT 1 FROM saved  WHERE id=:itemId)")
    int isSaved(int itemId);

    @Insert
    void  insertSav(Saved... saveds);

    @Delete
    void  delete(Saved saved);

}
