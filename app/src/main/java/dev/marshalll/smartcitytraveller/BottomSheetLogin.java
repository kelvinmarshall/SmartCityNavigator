package dev.marshalll.smartcitytraveller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BottomSheetLogin extends BottomSheetDialogFragment {

    TextInputEditText email,password;
    MaterialButton login ,register;
    ContentLoadingProgressBar progressBar;
    CoordinatorLayout coordinatorLayout;


    String Email,Pass;
    private FirebaseAuth mAuth;


    public BottomSheetLogin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email=view.findViewById(R.id.email);
        password=view.findViewById(R.id.pass);
        login=view.findViewById(R.id.login);
        register=view.findViewById(R.id.register);
        progressBar=view.findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
        coordinatorLayout=view.findViewById(R.id.bottomSheetLogin);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetRegister bottomSheetRegister=new BottomSheetRegister();
                bottomSheetRegister.show(getChildFragmentManager(),bottomSheetRegister.getTag());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
                email.setEnabled(false);
                password.setEnabled(false);
                coordinatorLayout.setAlpha((float) 0.8);
            }
        });


    }

    private void loginUser() {
        Email=email.getText().toString();
        Pass=password.getText().toString();

        if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Pass)){
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(Email, Pass)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                coordinatorLayout.setAlpha((float) 1.0);
                                dismiss();
                                Toast toast=Toast.makeText(getActivity(), "Signed In Successfully", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());

                            }

                        }
                    });
        }
        else
            Toast.makeText(getActivity(), "Make sure the fields are not empty", Toast.LENGTH_SHORT).show();
    }
}