package dev.marshalll.smartcitytraveller.Database.DataSource;

import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import dev.marshalll.smartcitytraveller.Database.Model.Saved;
import io.reactivex.Flowable;

public interface ISavedDataSource {


    Flowable<List<Saved>> getSavItems();

    int isSaved(int itemId);

    void  insertSav(Saved... saveds);

    void  delete(Saved saved);

}
