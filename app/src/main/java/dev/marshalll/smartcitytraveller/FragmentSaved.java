package dev.marshalll.smartcitytraveller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.marshalll.smartcitytraveller.Adapter.SavedAdapter;
import dev.marshalll.smartcitytraveller.Database.DataSource.SavedRepository;
import dev.marshalll.smartcitytraveller.Database.Local.SavedRoomDatabase;
import dev.marshalll.smartcitytraveller.Database.Model.Saved;
import dev.marshalll.smartcitytraveller.Interface.RecyclerTouchHelperListener;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.common.internal.service.Common;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FragmentSaved extends Fragment implements RecyclerTouchHelperListener{

    private RecyclerView recycler_saved;
    private LinearLayoutManager layoutManager;


    private CompositeDisposable compositeDisposable;
    private SavedAdapter favouriteAdapter;

    RecyclerTouchHelperListener recyclerTouchHelperListener;

    private FrameLayout rootlayout;

    private List<Saved> savedList=new ArrayList<>();

    public FragmentSaved() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.saved, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        compositeDisposable=new CompositeDisposable();

        rootlayout= view.findViewById(R.id.root_saved);
        recycler_saved= view.findViewById(R.id.recycler_saved);
        recycler_saved.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(getActivity());
        recycler_saved.setLayoutManager(layoutManager);

        ItemTouchHelper.SimpleCallback simpleCallback=new
                RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_saved);


        loadsavedItem();

    }

    private void loadsavedItem() {
        compositeDisposable.add(Utils.savedRepository.getSavItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Saved>>() {
                    @Override
                    public void accept(List<Saved> saved) {
                        displaySavedItem(saved);
                    }
                }));
    }

    private void displaySavedItem(List<Saved> saved) {

        savedList=saved;
        favouriteAdapter=new SavedAdapter(getActivity(),saved);
        recycler_saved.setAdapter(favouriteAdapter);
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        String name=savedList.get(viewHolder.getAdapterPosition()).name;
        final Saved deletedItem=savedList.get(viewHolder.getAdapterPosition());
        final int deletedIndex=viewHolder.getAdapterPosition();

        //delete item from adapter
        favouriteAdapter.removeItem(deletedIndex);
        //delete from room database
        Utils.savedRepository.delete(deletedItem);

        Snackbar snackbar = Snackbar.make(rootlayout, name + "removed from favourites!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favouriteAdapter.restoreItem(deletedItem, deletedIndex);
                Utils.savedRepository.insertSav(deletedItem);
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();

    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadsavedItem();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
