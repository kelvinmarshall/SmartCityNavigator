package dev.marshalll.smartcitytraveller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Splash extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private TextView textView;
    private MaterialButton register,login,home,next;
    FirebaseUser firebaseUser;
    private ConstraintLayout layout_splash,layout_options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        imageView= findViewById(R.id.imageView);
        textView= findViewById(R.id.logo);
        register= findViewById(R.id.register);
        login= findViewById(R.id.login);
        home= findViewById(R.id.home);
        next=findViewById(R.id.next);

        next.setVisibility(View.GONE);

        layout_splash=findViewById(R.id.layout_splash);
        layout_options=findViewById(R.id.layout_options);

        layout_splash.setVisibility(View.VISIBLE);
        layout_options.setVisibility(View.INVISIBLE);


        Animation anim = AnimationUtils.loadAnimation(this,R.anim.splash_animation);
        Animation  animation=AnimationUtils.loadAnimation(this,R.anim.slide_from_right);
        imageView.startAnimation(animation);
        textView.startAnimation(anim);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        home.setOnClickListener(this);
        next.setOnClickListener(this);

        if (firebaseUser!=null){
            Thread timer = new Thread(){
                public void run(){
                    try {
                        sleep(5000);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    finally {
                        starthomepage();
                    }
                }
            };
            timer.start();
        }
        else{
            next.setAnimation(anim);
            next.setVisibility(View.VISIBLE);
        }



    }

    private void starthomepage() {
        startActivity(new Intent(this, Smart_city.class));
        finish();
    }

    @Override
    public void onClick(View v) {

        int id=v.getId();
        if (id==R.id.login){
            BottomSheetLogin bottomSheetLogin=new BottomSheetLogin();
            bottomSheetLogin.show(getSupportFragmentManager(),bottomSheetLogin.getTag());
        }
        else  if (id==R.id.next){
            layout_splash.setVisibility(View.INVISIBLE);
            layout_options.setVisibility(View.VISIBLE);
        }
        else if (id==R.id.register){
            BottomSheetRegister bottomSheetRegister=new BottomSheetRegister();
            bottomSheetRegister.show(getSupportFragmentManager(),bottomSheetRegister.getTag());
        }else {
            starthomepage();
        }
    }
}