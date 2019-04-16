package dev.marshalll.smartcitytraveller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import dev.marshalll.smartcitytraveller.model.Model_User;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BottomSheetRegister extends BottomSheetDialogFragment {

    TextInputEditText email,password,confirm,username;
    MaterialButton Register;

    String Email,Pass,Username,Confirm;
    ContentLoadingProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference users;


    private FirebaseAuth mAuth;

    public BottomSheetRegister() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email=view.findViewById(R.id.email);
        password=view.findViewById(R.id.pass);
        confirm=view.findViewById(R.id.confirm_pass);
        username=view.findViewById(R.id.username);
        Register=view.findViewById(R.id.register);
        progressBar=view.findViewById(R.id.progress);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                createuser();
            }
        });


    }

    private void createuser() {
        Email=email.getText().toString();
        Pass=password.getText().toString();
        Username=username.getText().toString();
        Confirm=confirm.getText().toString();

        if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Pass)&&!TextUtils.isEmpty(Username) &&!TextUtils.isEmpty(Confirm)){
            progressBar.setVisibility(View.INVISIBLE);
            mAuth.createUserWithEmailAndPassword(Email, Pass)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                Toast toast=Toast.makeText(getActivity(), "User created Successfully", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                                dismiss();
                                progressBar.setVisibility(View.GONE);

                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user!=null)
                                {
                                    users=database.getReference("Users").child(user.getUid());
                                    Model_User model_user=new Model_User();
                                    model_user.setImage("");
                                    model_user.setUsername(Username);

                                    users.setValue(model_user);
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            }
                        }
                    });
        }
    }

}