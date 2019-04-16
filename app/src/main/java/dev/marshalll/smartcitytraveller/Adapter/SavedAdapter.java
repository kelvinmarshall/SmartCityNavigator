package dev.marshalll.smartcitytraveller.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dev.marshalll.smartcitytraveller.Database.Model.Saved;
import dev.marshalll.smartcitytraveller.DetailHotel;
import dev.marshalll.smartcitytraveller.Interface.ItemClickListener;
import dev.marshalll.smartcitytraveller.R;
import dev.marshalll.smartcitytraveller.viewHolder.ViewHolderSaved;


public class SavedAdapter extends RecyclerView.Adapter<ViewHolderSaved>  {

    private Context context;
    private List<Saved> savedList;

    public SavedAdapter(Context context, List<Saved> savedList) {
        this.context = context;
        this.savedList = savedList;
    }

    @NonNull
    @Override
    public ViewHolderSaved onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context)
                .inflate(R.layout.saved_place,parent,false);
        return new ViewHolderSaved(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSaved holder, final int position) {

        holder.name.setText(savedList.get(position).name);
        holder.location.setText(String.format("KES %s",savedList.get(position).location));
        Glide.with(context).load(savedList.get(position).image)
                .into(holder.imageView);

        holder.type.setText(savedList.get(position).category);

        final Saved local=savedList.get(position);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent hoteldetail=new Intent(context, DetailHotel.class);
                hoteldetail.putExtra("id",savedList.get(position).id);//send Id to new activity
                context.startActivity(hoteldetail);
            }
        });

    }
    @Override
    public int getItemCount() {
        return savedList.size();
    }

    public void removeItem(int position)
    {
        savedList.remove(position);
        notifyItemRemoved(position);
    }
    public void restoreItem(Saved item,int position)
    {
        savedList.add(position,item);
        notifyItemInserted(position);
    }

    public Saved getItem(int position)
    {
        return  savedList.get(position);
    }
}
