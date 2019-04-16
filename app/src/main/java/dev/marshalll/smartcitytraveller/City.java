package dev.marshalll.smartcitytraveller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import dev.marshalll.smartcitytraveller.model.Model_city;

public class City extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    ViewPager viewPager;
    TabLayout tablayout;

    TextView map,events;

    Toolbar toolbar;

    String title;
    Model_city city;
    ImageView hotel_image;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city);

        database=FirebaseDatabase.getInstance();

        map=findViewById(R.id.map);
        events=findViewById(R.id.events);

        collapsingToolbarLayout=findViewById(R.id.collapsing_toolbar);

        hotel_image=findViewById(R.id.hotel_image);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(City.this,Maps.class));
            }
        });
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(City.this,Events.class));
            }
        });
        toolbar=findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        //get productId from Intent
        if(getIntent() !=null)
            title=getIntent().getStringExtra("title");
        if(!title.isEmpty())
        {
            toolbar.setTitle(title);
            databaseReference=database.getReference().child("City").child(title);
            loadcity_info(databaseReference);

        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(City.this,Smart_city.class));
            }
        });

        viewPager= findViewById(R.id.viewpager);
        tablayout= findViewById(R.id.tablayout);
        //create and set ViewPager adapter
        setupViewPager(viewPager);
        tablayout.setupWithViewPager(viewPager);
        //change selected tab when viewpager changed page
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

        //change viewpager page when tab selected
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void loadcity_info(DatabaseReference databaseReference) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                city=dataSnapshot.getValue(Model_city.class);

                Glide.with(getApplicationContext())
                        .load(city.getImage_url())
                        .placeholder(getResources().getDrawable(R.drawable.custom_circular_dialog))
                        .into(hotel_image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentHotel(
                ContextCompat.getColor(this, R.color.colorAccent)), "Hotels & Restaurant");
        adapter.addFrag(new FragmentNightLife(
                ContextCompat.getColor(this, R.color.colorAccent)), "NightLife");
        adapter.addFrag(new FragmentHospitals(
                ContextCompat.getColor(this, R.color.colorAccent)), "Hospitals");
        adapter.addFrag(new FragmentPicnic(
                ContextCompat.getColor(this, R.color.colorAccent)), "Picnic Site");
        adapter.addFrag(new FragmentParks(
                ContextCompat.getColor(this, R.color.colorAccent)), "Parks");
        adapter.addFrag(new FragmentMosque_churches(
                ContextCompat.getColor(this, R.color.colorAccent)), "Mosque & Churches");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    

}
