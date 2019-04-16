package dev.marshalll.smartcitytraveller.Database.DataSource;

import java.util.List;

import dev.marshalll.smartcitytraveller.Database.Model.Saved;
import io.reactivex.Flowable;

public class SavedRepository implements ISavedDataSource {

    private ISavedDataSource iSavedDataSource;

    public SavedRepository(ISavedDataSource iSavedDataSource) {
        this.iSavedDataSource = iSavedDataSource;
    }

    private  static SavedRepository instance;

    public   static SavedRepository getInstance(ISavedDataSource iSavedDataSource)
    {
        if (instance ==null)
            instance=new SavedRepository(iSavedDataSource);
        return instance;

    }


    @Override
    public Flowable<List<Saved>> getSavItems() {
        return iSavedDataSource.getSavItems();
    }

    @Override
    public int isSaved(int itemId) {
        return iSavedDataSource.isSaved(itemId);
    }

    @Override
    public void insertSav(Saved... saveds) {
        iSavedDataSource.insertSav(saveds);
    }

    @Override
    public void delete(Saved saved) {
        iSavedDataSource.delete(saved);
    }
}
