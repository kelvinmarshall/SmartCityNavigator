package dev.marshalll.smartcitytraveller.Interface;

import androidx.recyclerview.widget.RecyclerView;


public interface RecyclerTouchHelperListener{
    void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position);

}
