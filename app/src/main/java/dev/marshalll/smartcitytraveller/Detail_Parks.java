package dev.marshalll.smartcitytraveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.marshalll.smartcitytraveller.Database.DataSource.SavedRepository;
import dev.marshalll.smartcitytraveller.Database.Model.Saved;
import dev.marshalll.smartcitytraveller.Interface.ItemClickListener;
import dev.marshalll.smartcitytraveller.model.Model_Parks;
import dev.marshalll.smartcitytraveller.model.Model_User;
import dev.marshalll.smartcitytraveller.model.Model_comments;
import dev.marshalll.smartcitytraveller.viewHolder.ViewHolderComments;
import dev.marshalll.smartcitytraveller.viewHolder.ViewHolderPlaces;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Detail_Parks extends AppCompatActivity {

    private ImageView image_park;
    private TextView amenities;
    private TextView details;
    private TextView name;
    private TextView opendays;
    private TextView openingtime;
    private TextView location;

    private MaterialButton directions,show_map,submit_comment;
    TextInputEditText comment;
    FloatingActionButton btnsave,write_comment;

    FirebaseDatabase database;
    DatabaseReference databaseReference,user;
    FirebaseRecyclerAdapter adapter;
    FirebaseUser firebaseUser;

    RecyclerView recycler_comments;
    String parkId;

    Model_Parks model_parks;

    Toolbar toolbar;

    ConstraintLayout layoutBottomSheet;
    BottomSheetBehavior sheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__parks);
        database=FirebaseDatabase.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

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


        image_park=findViewById(R.id.img_park);
        amenities=findViewById(R.id.amenities);
        details=findViewById(R.id.details);
        name=findViewById(R.id.textView4);
        opendays=findViewById(R.id.open_days);
        openingtime=findViewById(R.id.opening_time);
        location=findViewById(R.id.address);
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
            parkId=getIntent().getStringExtra("id");
        if(!parkId.isEmpty())
        {
            load_info();
            retrieveCommnets();
        }
        //add saved
        if (Utils.savedRepository.isSaved(Integer.parseInt(parkId))==1)
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
                .child("Parks").child(parkId).child("comments")
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
                .child("Parks").child(parkId).child("comments").push();

        String Comment=comment.getText().toString();

        Model_comments  model_comments=new Model_comments();
        model_comments.setMessage(Comment);
        model_comments.setUserId(firebaseUser.getUid());

        comments.setValue(model_comments).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Detail_Parks.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                comment.setText("");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Detail_Parks.this, "Comment post failed", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void load_info() {
        databaseReference=database.getReference().child("Parks").child(parkId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model_parks=dataSnapshot.getValue(Model_Parks.class);
                if (dataSnapshot.exists()){

                    amenities.setText(model_parks.getAmenities());
                    details.setText(model_parks.getDetails());
                    name.setText(model_parks.getName());
                    opendays.setText(model_parks.getOpendays());
                    openingtime.setText(model_parks.getOpeningtime());
                    location.setText(model_parks.getLocation());

                    toolbar.setTitle(model_parks.getName());

                    Glide.with(getApplicationContext()).load(model_parks.getImage_url()).into(image_park);

                    directions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent direction=new Intent(Detail_Parks.this,Directions.class);
                            direction.putExtra("location",model_parks.getLocation());
                            direction.putExtra("name",model_parks.getName());
                            direction.putExtra("Lat",model_parks.getLat());
                            direction.putExtra("Long",model_parks.getLong());
                            direction.putExtra("image",model_parks.getImage_url());
                            startActivity(direction);
                        }
                    });

                    show_map.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent map=new Intent(Detail_Parks.this,Maps.class);
                            map.putExtra("location",model_parks.getLocation());
                            map.putExtra("name",model_parks.getName());
                            map.putExtra("Lat",model_parks.getLat());
                            map.putExtra("Long",model_parks.getLong());
                            map.putExtra("image",model_parks.getImage_url());
                            startActivity(map);
                        }
                    });

                    btnsave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(Detail_Parks.this, parkId, Toast.LENGTH_SHORT).show();
                            if (Utils.savedRepository.isSaved(Integer.parseInt(parkId))!=1)
                            {
                                addOrRemoveToSaved(parkId,true,model_parks);
                                btnsave.setImageResource(R.drawable.favorite);
                            }
                            else
                            {
                                addOrRemoveToSaved(parkId,false,model_parks);
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

    private void addOrRemoveToSaved(String parkId, boolean isAdd, Model_Parks model_parks) {
        Saved saved = new Saved();
        saved.id= Integer.parseInt(parkId);
        saved.image=model_parks.getImage_url();
        saved.name=model_parks.getName();
        saved.location=model_parks.getLocation();
        saved.category="Parks";

        if (isAdd)
            Utils.savedRepository.insertSav(saved);
        else
            Utils.savedRepository.delete(saved);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
