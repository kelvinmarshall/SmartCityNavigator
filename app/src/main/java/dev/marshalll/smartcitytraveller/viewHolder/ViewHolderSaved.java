package dev.marshalll.smartcitytraveller.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import dev.marshalll.smartcitytraveller.Interface.ItemClickListener;
import dev.marshalll.smartcitytraveller.R;

public class ViewHolderSaved extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView imageView;
    private ItemClickListener itemClickListener;
    public TextView name,location,type;

    public ConstraintLayout view_background;
    public ConstraintLayout view_foreground;


    public ViewHolderSaved(@NonNull View itemView) {

        super(itemView);

        type=itemView.findViewById(R.id.type);
        imageView=itemView.findViewById(R.id.image_place);
        name=itemView.findViewById(R.id.title_place);
        location=itemView.findViewById(R.id.address_place);
        view_background= itemView.findViewById(R.id.view_background);
        view_foreground= itemView.findViewById(R.id.view_foreground);

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
