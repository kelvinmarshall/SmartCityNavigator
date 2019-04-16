package dev.marshalll.smartcitytraveller;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import dev.marshalll.smartcitytraveller.Database.DataSource.SavedRepository;
import dev.marshalll.smartcitytraveller.Database.Local.SavedDataSource;
import dev.marshalll.smartcitytraveller.Database.Local.SavedRoomDatabase;

public class Smart_city extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_city);

        mAuth = FirebaseAuth.getInstance();
        user =mAuth.getCurrentUser();

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(navlistener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new FragmentHome()).commit();

        initDB();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void initDB() {
        Utils.savedRoomDatabase= SavedRoomDatabase.getInstance(this);
        Utils.savedRepository= SavedRepository.getInstance(SavedDataSource.getInstance(Utils.savedRoomDatabase.savedDAO()));
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navlistener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragment=null;

                    int id=menuItem.getItemId();

                    if (id==R.id.home){
                        fragment=new FragmentHome();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment)
                                .commit();
                    }
                    else if(id==R.id.nearby){
                        fragment=new FragmentNearby();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment)
                                .commit();
                    }
                    else if(id==R.id.favourite){

                        if(user==null){
                            BottomSheetLogin bottomSheetLogin=new BottomSheetLogin();
                            bottomSheetLogin.show(getSupportFragmentManager(),bottomSheetLogin.getTag());
                        }else {
                            fragment=new FragmentSaved();
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment)
                                    .commit();
                        }
                    }
                    else {
                        FirebaseUser user =mAuth.getCurrentUser();

                        if(user==null){
                            BottomSheetLogin bottomSheetLogin=new BottomSheetLogin();
                            bottomSheetLogin.show(getSupportFragmentManager(),bottomSheetLogin.getTag());
                        }else {
                            fragment=new FragmentProfile();
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment)
                                    .commit();
                        }
                    }


                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_smartcity,menu);

        if(user!=null){
            menu.findItem(R.id.signin).setTitle("SignOut");
        }
        else{
            menu.findItem(R.id.signin).setTitle("SignIn");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if (id==R.id.settings){

        }
        else if (id==R.id.signin){
            if (user!=null){
                mAuth.signOut();
                item.setTitle("SignIn");

                Toast toast=Toast.makeText(Smart_city.this, "Signed Out Successfully", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();

            }
            else{
                BottomSheetLogin bottomSheetLogin=new BottomSheetLogin();
                bottomSheetLogin.show(getSupportFragmentManager(),bottomSheetLogin.getTag());

            }
        }
        return true;
    }

}
