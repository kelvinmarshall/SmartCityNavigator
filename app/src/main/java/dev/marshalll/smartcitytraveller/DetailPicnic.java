package dev.marshalll.smartcitytraveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import dev.marshalll.smartcitytraveller.Database.Model.Saved;
import dev.marshalll.smartcitytraveller.model.Model_Hospitals;
import dev.marshalll.smartcitytraveller.model.Model_Hotel;
import dev.marshalll.smartcitytraveller.model.Model_Picnic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailPicnic extends AppCompatActivity {

    private ImageView image_picnic;
    private TextView details;
    private TextView name;
    private TextView amenities;
    private RatingBar rating;
    private TextView opendays;
    private TextView openingtime;
    private TextView location;

    private MaterialButton directions,show_map,submit_comment;
    TextInputEditText comment;
    FloatingActionButton write_comment;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String picnicId;

    Model_Picnic model_picnic;
    FloatingActionButton btnsave;

    Toolbar toolbar;
    ConstraintLayout layoutBottomSheet;
    BottomSheetBehavior sheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_picnic);

        database=FirebaseDatabase.getInstance();

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


        image_picnic=findViewById(R.id.img_picnic);
        amenities=findViewById(R.id.amenities);
        details=findViewById(R.id.details);
        name=findViewById(R.id.textView4);
        opendays=findViewById(R.id.open_days);
        openingtime=findViewById(R.id.opening_time);
        location=findViewById(R.id.address);
        rating=findViewById(R.id.rating);
        directions=findViewById(R.id.directions);
        show_map=findViewById(R.id.show_on_map);
        btnsave=findViewById(R.id.btnsave);
        write_comment=findViewById(R.id.write_comment);

        submit_comment=findViewById(R.id.submit_comment);
        comment=findViewById(R.id.comment);


        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailPicnic.this,Directions.class));
            }
        });

        if(getIntent() !=null)
            picnicId=getIntent().getStringExtra("id");
        if(!picnicId.isEmpty())
        {
            load_info();
        }
        write_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void load_info() {
        databaseReference=database.getReference().child("Picnics").child(picnicId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model_picnic=dataSnapshot.getValue(Model_Picnic.class);
                if (dataSnapshot.exists()){
                    amenities.setText(model_picnic.getAmenities());
                    details.setText(model_picnic.getDetails());
                    name.setText(model_picnic.getName());

                    toolbar.setTitle(model_picnic.getName());
                    opendays.setText(model_picnic.getOpendays());
                    openingtime.setText(model_picnic.getOpeningtime());
                    location.setText(model_picnic.getLocation());
                    rating.setRating(Float.parseFloat(model_picnic.getRating()));
                    Glide.with(getApplicationContext()).load(model_picnic.getImage_url()).into(image_picnic);

                    directions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent direction=new Intent(DetailPicnic.this,Directions.class);
                            direction.putExtra("location",model_picnic.getLocation());
                            direction.putExtra("lat",model_picnic.getLat());
                            direction.putExtra("long",model_picnic.getLong());
                            direction.putExtra("name",model_picnic.getName());
                            direction.putExtra("image",model_picnic.getImage_url());
                            startActivity(direction);
                        }
                    });
                    show_map.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent map=new Intent(DetailPicnic.this,Maps.class);
                            map.putExtra("location",model_picnic.getLocation());
                            map.putExtra("lat",model_picnic.getLat());
                            map.putExtra("long",model_picnic.getLong());
                            map.putExtra("name",model_picnic.getName());
                            map.putExtra("image",model_picnic.getImage_url());
                            startActivity(map);
                        }
                    });
                    btnsave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(DetailPicnic.this, picnicId, Toast.LENGTH_SHORT).show();
                            if (Utils.savedRepository.isSaved(Integer.parseInt(picnicId))!=1)
                            {
                                addOrRemoveToSaved(picnicId,true,model_picnic);
                                btnsave.setImageResource(R.drawable.favorite);
                            }
                            else
                            {
                                addOrRemoveToSaved(picnicId,false,model_picnic);
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
    private void addOrRemoveToSaved(String parkId, boolean isAdd, Model_Picnic model_parks) {
        Saved saved = new Saved();
        saved.id= Integer.parseInt(parkId);
        saved.image=model_parks.getImage_url();
        saved.name=model_parks.getName();
        saved.location=model_parks.getLocation();
        saved.category="Picnic";

        if (isAdd)
            Utils.savedRepository.insertSav(saved);
        else
            Utils.savedRepository.delete(saved);
    }
}
