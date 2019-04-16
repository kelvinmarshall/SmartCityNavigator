package dev.marshalll.smartcitytraveller.Database.Local;


import java.util.List;

import dev.marshalll.smartcitytraveller.Database.DataSource.ISavedDataSource;
import dev.marshalll.smartcitytraveller.Database.Model.Saved;
import io.reactivex.Flowable;

public class SavedDataSource implements ISavedDataSource {

    private SavedDAO savedDAO;
    private  static SavedDataSource instance;

    public SavedDataSource(SavedDAO savedDAO) {
        this.savedDAO = savedDAO;
    }

    public  static SavedDataSource getInstance(SavedDAO savedDAO)
    {
        if (instance == null)
            instance=new SavedDataSource(savedDAO);
        return instance;
    }


    @Override
    public Flowable<List<Saved>> getSavItems() {
        return savedDAO.getSavItems();
    }

    @Override
    public int isSaved(int itemId) {
        return savedDAO.isSaved(itemId);
    }

    @Override
    public void insertSav(Saved... saveds) {
        savedDAO.insertSav(saveds);
    }

    @Override
    public void delete(Saved saved) {
        savedDAO.delete(saved);
    }
}
