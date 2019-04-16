package dev.marshalll.smartcitytraveller;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.marshalll.smartcitytraveller.Database.Model.Saved;
import dev.marshalll.smartcitytraveller.Interface.ItemClickListener;
import dev.marshalll.smartcitytraveller.model.Model_Hotel;
import dev.marshalll.smartcitytraveller.model.Model_MChurch;
import dev.marshalll.smartcitytraveller.viewHolder.ViewHolderPlaces;

public class FragmentMosque_churches extends Fragment {

    int color;

    FirebaseRecyclerAdapter adapter;

    RecyclerView recyclerHotel;

    public FragmentMosque_churches() {
    }

    @SuppressLint("ValidFragment")
    public FragmentMosque_churches(int color) {
        this.color = color;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_viewpager, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerHotel=view.findViewById(R.id.recyclerView);

        recyclerHotel.setLayoutManager(layoutManager);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("M_church")
                .limitToLast(50);

        FirebaseRecyclerOptions<Model_MChurch> options =
                new FirebaseRecyclerOptions.Builder<Model_MChurch>()
                        .setQuery(query, Model_MChurch.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Model_MChurch, ViewHolderPlaces>(options) {
            @Override
            public ViewHolderPlaces onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_place2, parent, false);

                return new ViewHolderPlaces(view);
            }

            @Override
            protected void onBindViewHolder(final ViewHolderPlaces holder, int position, final Model_MChurch model) {
                // Bind the Chat object to the ChatHolder
                // ...

                final String id=adapter.getRef(position).getKey();
                holder.hotel_name.setText(model.getName());
                holder.location.setText(model.getLocation());
                if (getActivity() !=null)
                    Glide.with(getActivity())
                            .load(model.getImage_url()).into(holder.imageView);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent=new Intent(getActivity(),DetailM_Churches.class);
                        intent.putExtra("id",id);
                        startActivity(intent);
                    }
                });
                //add saved
                if (Utils.savedRepository.isSaved(Integer.parseInt(id))==1)
                    holder.save.setImageResource(R.drawable.favorite);
                else
                    holder.save.setImageResource(R.drawable.favorite_border);
                holder.save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Utils.savedRepository.isSaved(Integer.parseInt(id))!=1)
                        {
                            addOrRemoveToSaved(id,true,model);
                            holder.save.setImageResource(R.drawable.favorite);
                        }
                        else
                        {
                            addOrRemoveToSaved(id,false,model);
                            holder.save.setImageResource(R.drawable.favorite_border);
                        }
                    }
                });
            }


        };

        recyclerHotel.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void addOrRemoveToSaved(String id, boolean isAdd, Model_MChurch model) {

        Saved saved = new Saved();
        saved.id= Integer.parseInt(id);
        saved.image=model.getImage_url();
        saved.name=model.getName();
        saved.location=model.getLocation();
        saved.category="Mosque and churches";

        if (isAdd)
        {
            Utils.savedRepository.insertSav(saved);
        }

        else{
            Utils.savedRepository.delete(saved);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
