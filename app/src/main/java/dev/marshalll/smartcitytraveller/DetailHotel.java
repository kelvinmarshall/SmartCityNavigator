package dev.marshalll.smartcitytraveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.marshalll.smartcitytraveller.Database.Model.Saved;
import dev.marshalll.smartcitytraveller.Interface.ItemClickListener;
import dev.marshalll.smartcitytraveller.model.Model_Hospitals;
import dev.marshalll.smartcitytraveller.model.Model_Hotel;
import dev.marshalll.smartcitytraveller.model.Model_User;
import dev.marshalll.smartcitytraveller.model.Model_comments;
import dev.marshalll.smartcitytraveller.viewHolder.ViewHolderComments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DetailHotel extends AppCompatActivity {

    private ImageView image_Hotel,image_select;
    private TextView amenities,text_select;
    private TextView checkin;
    private TextView checkout;
    private TextView details;
    private TextView name;
    private TextView opendays;
    private TextView openingtime;
    private RatingBar rating;
    private TextView location;

    private MaterialButton directions,show_map,submit_comment;
    TextInputEditText comment;
    FloatingActionButton write_comment;

    FirebaseDatabase database;
    DatabaseReference databaseReference,user;
    FirebaseRecyclerAdapter adapter;
    FirebaseUser firebaseUser;
    String hotelId;

    RecyclerView recycler_comments;

    Model_Hotel model_hotel;


    FloatingActionButton btnsave;
    Toolbar toolbar;
    ConstraintLayout layoutBottomSheet;
    BottomSheetBehavior sheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_res_hotels);

        toolbar=findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        layoutBottomSheet=findViewById(R.id.comment_sheet);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });


        database=FirebaseDatabase.getInstance();

        image_Hotel=findViewById(R.id.img_hotel);
        image_select=findViewById(R.id.image_select);
        amenities=findViewById(R.id.amenities);
        text_select=findViewById(R.id.textView5);
        details=findViewById(R.id.details);
        name=findViewById(R.id.textView4);
        opendays=findViewById(R.id.open_days);
        openingtime=findViewById(R.id.opening_time);
        location=findViewById(R.id.address);
        rating=findViewById(R.id.rating);
        checkout=findViewById(R.id.checkout);
        checkin=findViewById(R.id.checkin);
        directions=findViewById(R.id.directions);
        show_map=findViewById(R.id.show_on_map);
        btnsave=findViewById(R.id.btnsave);
        write_comment=findViewById(R.id.write_comment);
        submit_comment=findViewById(R.id.submit_comment);
        comment=findViewById(R.id.comment);
        recycler_comments=findViewById(R.id.recycler_comments);

        recycler_comments.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recycler_comments.setLayoutManager(layoutManager);
        submit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (firebaseUser!=null){
                    postComment();
                }
                else {
                    BottomSheetLogin bottomSheetLogin=new BottomSheetLogin();
                    bottomSheetLogin.show(getSupportFragmentManager(),bottomSheetLogin.getTag());
                }

            }
        });




        if(getIntent() !=null)
            hotelId=getIntent().getStringExtra("id");
        if(!hotelId.isEmpty())
        {
            load_info();
            retrieveCommnets();
        }
        //add saved
        if (Utils.savedRepository.isSaved(Integer.parseInt(hotelId))==1)
            btnsave.setImageResource(R.drawable.favorite);
        else
            btnsave.setImageResource(R.drawable.favorite_border);


        write_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });


    }
    private void retrieveCommnets() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Parks").child(hotelId).child("comments")
                .limitToLast(50);

        FirebaseRecyclerOptions<Model_comments> options =
                new FirebaseRecyclerOptions.Builder<Model_comments>()
                        .setQuery(query, Model_comments.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Model_comments, ViewHolderComments>(options) {
            @NonNull
            @Override
            public ViewHolderComments onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_show_comment, parent, false);

                return new ViewHolderComments(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderComments holder, int position, @NonNull final Model_comments model) {
                user=database.getReference().child("Users").child(model.getUserId());

                user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            Model_User model_user=dataSnapshot.getValue(Model_User.class);
                            assert model_user != null;
                            holder.username.setText(model_user.getUsername());

                            Glide.with(getApplicationContext()).load(model_user.getImage())
                                    .placeholder(R.drawable.custom_circular_dialog2)
                                    .into(holder.image);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.comment.setText(model.getMessage());



                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });

            }


        };

        recycler_comments.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void postComment() {

        DatabaseReference comments=FirebaseDatabase.getInstance().getReference()
                .child("Parks").child(hotelId).child("comments").push();

        String Comment=comment.getText().toString();

        Model_comments  model_comments=new Model_comments();
        model_comments.setMessage(Comment);
        model_comments.setUserId(firebaseUser.getUid());

        comments.setValue(model_comments).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DetailHotel.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                comment.setText("");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailHotel.this, "Comment post failed", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void load_info() {
        databaseReference=database.getReference().child("Hotel_restaurant").child(hotelId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model_hotel=dataSnapshot.getValue(Model_Hotel.class);
                if (dataSnapshot.exists()){

                    toolbar.setTitle(model_hotel.getName());
                    amenities.setText(model_hotel.getAmenities());
                    details.setText(model_hotel.getDetails());
                    name.setText(model_hotel.getName());
                    opendays.setText(model_hotel.getOpendays());
                    openingtime.setText(model_hotel.getOpeningtime());
                    location.setText(model_hotel.getLocation());
                    rating.setRating(Float.parseFloat(model_hotel.getRating()));
                    checkout.setText(model_hotel.getCheckout());
                    checkin.setText(model_hotel.getCheckin());

                    Glide.with(getApplicationContext())
                            .load(model_hotel.getImage_url()).into(image_Hotel);

                    directions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent direction=new Intent(DetailHotel.this,Directions.class);
                            direction.putExtra("location",model_hotel.getLocation());
                            direction.putExtra("lat",model_hotel.getLat());
                            direction.putExtra("long",model_hotel.getLong());
                            direction.putExtra("name",model_hotel.getName());
                            direction.putExtra("image",model_hotel.getImage_url());

                            startActivity(direction);
                        }
                    });
                    show_map.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent map=new Intent(DetailHotel.this,Maps.class);
                            map.putExtra("location",model_hotel.getLocation());
                            map.putExtra("lat",model_hotel.getLat());
                            map.putExtra("long",model_hotel.getLong());
                            map.putExtra("name",model_hotel.getName());
                            map.putExtra("image",model_hotel.getImage_url());
                            startActivity(map);
                        }
                    });
                    btnsave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(DetailHotel.this, hotelId, Toast.LENGTH_SHORT).show();
                            if (Utils.savedRepository.isSaved(Integer.parseInt(hotelId))!=1)
                            {
                                addOrRemoveToSaved(hotelId,true,model_hotel);
                                btnsave.setImageResource(R.drawable.favorite);
                            }
                            else
                            {
                                addOrRemoveToSaved(hotelId,false,model_hotel);
                                btnsave.setImageResource(R.drawable.favorite_border);
                            }
                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void addOrRemoveToSaved(String parkId, boolean isAdd, Model_Hotel model_parks) {
        Saved saved = new Saved();
        saved.id= Integer.parseInt(parkId);
        saved.image=model_parks.getImage_url();
        saved.name=model_parks.getName();
        saved.location=model_parks.getLocation();
        saved.category="Hotel and Restaurant";

        if (isAdd)
            Utils.savedRepository.insertSav(saved);
        else
            Utils.savedRepository.delete(saved);
    }
}
