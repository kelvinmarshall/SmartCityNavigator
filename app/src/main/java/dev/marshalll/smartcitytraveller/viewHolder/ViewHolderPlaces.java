package dev.marshalll.smartcitytraveller.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dev.marshalll.smartcitytraveller.Interface.ItemClickListener;
import dev.marshalll.smartcitytraveller.R;

public class ViewHolderPlaces extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView imageView;
    private ItemClickListener itemClickListener;
    public TextView hotel_name,location;

    public FloatingActionButton save;

    public ViewHolderPlaces(@NonNull View itemView) {

        super(itemView);

        imageView=itemView.findViewById(R.id.image);
        hotel_name=itemView.findViewById(R.id.name);
        location=itemView.findViewById(R.id.location);
        save=itemView.findViewById(R.id.save);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
