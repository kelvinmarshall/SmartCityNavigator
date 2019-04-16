package dev.marshalll.smartcitytraveller;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.android.material.card.MaterialCardView;


public class FragmentHome extends Fragment {

    MaterialCardView viewnairobi,vMsa,vEld,vKisumu,vThika,vNyeri,vNakuru,vMalindi,vKiambu,vNaivasha;


    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewnairobi=view.findViewById(R.id.nairobi);
        vMsa=view.findViewById(R.id.mombasa);
        vEld=view.findViewById(R.id.eldoret);
        vKisumu=view.findViewById(R.id.kisumu);
        vThika=view.findViewById(R.id.Thika);
        vNyeri=view.findViewById(R.id.nyeri);
        vNakuru=view.findViewById(R.id.nakuru);
        vMalindi=view.findViewById(R.id.malindi);
        vKiambu=view.findViewById(R.id.kiambu);
        vNaivasha=view.findViewById(R.id.naivasha);


        viewnairobi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nairobi=new Intent(getActivity(),City.class);
                nairobi.putExtra("title","Nairobi");
                startActivity(nairobi);
            }
        });
        vMsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msa=new Intent(getActivity(),City.class);
                msa.putExtra("title","Mombasa");
                startActivity(msa);
            }
        });
        vEld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eld=new Intent(getActivity(),City.class);
                eld.putExtra("title","Eldoret");
                startActivity(eld);
            }
        });
        vKisumu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kisumu=new Intent(getActivity(),City.class);
                kisumu.putExtra("title","Kisumu");
                startActivity(kisumu);
            }
        });
        vMalindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent malindi=new Intent(getActivity(),City.class);
                malindi.putExtra("title","Malindi");
                startActivity(malindi);
            }
        });
        vThika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent thika=new Intent(getActivity(),City.class);
                thika.putExtra("title","Thika");
                startActivity(thika);
            }
        });
        vNyeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nyeri=new Intent(getActivity(),City.class);
                nyeri.putExtra("title","Nyeri");
                startActivity(nyeri);
            }
        });
        vNakuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nakuru=new Intent(getActivity(),City.class);
                nakuru.putExtra("title","Nakuru");
                startActivity(nakuru);
            }
        });
        vKiambu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kiambu=new Intent(getActivity(),City.class);
                kiambu.putExtra("title","Kiambu");
                startActivity(kiambu);
            }
        });
        vNaivasha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent naivasha=new Intent(getActivity(),City.class);
                naivasha.putExtra("title","Naivasha");
                startActivity(naivasha);
            }
        });

        // Set up the tool bar
        setUpToolbar(view);
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
                getContext(),
                view.findViewById(R.id.constraint_main),
                new AccelerateDecelerateInterpolator(),
                getContext().getResources().getDrawable(R.drawable.menu), // Menu open icon
                getContext().getResources().getDrawable(R.drawable.close_menu))); // Menu close icon
    }
}
