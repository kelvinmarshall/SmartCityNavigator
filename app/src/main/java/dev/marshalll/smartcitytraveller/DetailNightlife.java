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
import dev.marshalll.smartcitytraveller.model.Model_Nightlife;
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

public class DetailNightlife extends AppCompatActivity {

    private ImageView image_nightlife;
    private TextView details;
    private TextView name;
    private TextView opendays;
    private TextView openingtime;
    private TextView location;

    private MaterialButton directions,show_map,submit_comment;
    TextInputEditText comment;
    FloatingActionButton btnsave, write_comment;

    RecyclerView recycler_comments;
    FirebaseDatabase database;
    DatabaseReference databaseReference,user;
    FirebaseRecyclerAdapter adapter;
    FirebaseUser firebaseUser;
    String clubId;

    Toolbar toolbar;
    ConstraintLayout layoutBottomSheet;
    BottomSheetBehavior sheetBehavior;

    Model_Nightlife model_nightlife;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_nightlife);
        database=FirebaseDatabase.getInstance();
        image_nightlife=findViewById(R.id.img_club);
        details=findViewById(R.id.details);
        name=findViewById(R.id.textView4);
        opendays=findViewById(R.id.open_days);
        openingtime=findViewById(R.id.opening_time);
        location=findViewById(R.id.address);
        directions=findViewById(R.id.directions);
        btnsave=findViewById(R.id.btnsave);
        write_comment=findViewById(R.id.write_comment);
        submit_comment=findViewById(R.id.submit_comment);
        comment=findViewById(R.id.comment);
        show_map=findViewById(R.id.show_on_map);
        recycler_comments=findViewById(R.id.recycler_comments);

        recycler_comments.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recycler_comments.setLayoutManager(layoutManager);

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


        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailNightlife.this,Directions.class));
            }
        });
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
            clubId=getIntent().getStringExtra("id");
        if(!clubId.isEmpty())
        {
            load_info();
            retrieveCommnets();
        }

        //add saved
        if (Utils.savedRepository.isSaved(Integer.parseInt(clubId))==1)
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
                .child("Parks").child(clubId).child("comments")
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
                .child("Parks").child(clubId).child("comments").push();

        String Comment=comment.getText().toString();

        Model_comments  model_comments=new Model_comments();
        model_comments.setMessage(Comment);
        model_comments.setUserId(firebaseUser.getUid());

        comments.setValue(model_comments).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DetailNightlife.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                comment.setText("");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailNightlife.this, "Comment post failed", Toast.LENGTH_SHORT).show();
            }
        });



    }
    private void load_info() {
        databaseReference=database.getReference().child("Nightlife").child(clubId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model_nightlife=dataSnapshot.getValue(Model_Nightlife.class);
                if (dataSnapshot.exists()){
                    toolbar.setTitle(model_nightlife.getName());
                    details.setText(model_nightlife.getDetails());
                    name.setText(model_nightlife.getName());
                    opendays.setText(model_nightlife.getOpendays());
                    openingtime.setText(model_nightlife.getOpeningtime());
                    location.setText(model_nightlife.getLocation());

                    Glide.with(getApplicationContext()).load(model_nightlife.getImage_url()).into(image_nightlife);

                    directions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent direction=new Intent(DetailNightlife.this,Directions.class);
                            direction.putExtra("location",model_nightlife.getLocation());
                            direction.putExtra("lat",model_nightlife.getLat());
                            direction.putExtra("long",model_nightlife.getLong());
                            direction.putExtra("name",model_nightlife.getName());
                            direction.putExtra("image",model_nightlife.getImage_url());
                            startActivity(direction);
                        }
                    });
                    show_map.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent map=new Intent(DetailNightlife.this,Maps.class);
                            map.putExtra("location",model_nightlife.getLocation());
                            map.putExtra("lat",model_nightlife.getLat());
                            map.putExtra("long",model_nightlife.getLong());
                            map.putExtra("name",model_nightlife.getName());
                            map.putExtra("image",model_nightlife.getImage_url());
                            startActivity(map);
                        }
                    });
                    btnsave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(DetailNightlife.this, clubId, Toast.LENGTH_SHORT).show();
                            if (Utils.savedRepository.isSaved(Integer.parseInt(clubId))!=1)
                            {
                                addOrRemoveToSaved(clubId,true,model_nightlife);
                                btnsave.setImageResource(R.drawable.favorite);
                            }
                            else
                            {
                                addOrRemoveToSaved(clubId,false,model_nightlife);
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
    private void addOrRemoveToSaved(String parkId, boolean isAdd, Model_Nightlife model_parks) {
        Saved saved = new Saved();
        saved.id= Integer.parseInt(parkId);
        saved.image=model_parks.getImage_url();
        saved.name=model_parks.getName();
        saved.location=model_parks.getLocation();
        saved.category="NightLife";

        if (isAdd)
            Utils.savedRepository.insertSav(saved);
        else
            Utils.savedRepository.delete(saved);
    }
}
