package dev.marshalll.smartcitytraveller.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import dev.marshalll.smartcitytraveller.Interface.ItemClickListener;
import dev.marshalll.smartcitytraveller.R;

public class ViewHolderComments extends RecyclerView.ViewHolder implements View.OnClickListener {
    public CircleImageView image;
    private ItemClickListener itemClickListener;
    public TextView username,comment;


    public ViewHolderComments(@NonNull View itemView) {

        super(itemView);

        image=itemView.findViewById(R.id.user_image);
        username=itemView.findViewById(R.id.username);
        comment=itemView.findViewById(R.id.comment);

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
