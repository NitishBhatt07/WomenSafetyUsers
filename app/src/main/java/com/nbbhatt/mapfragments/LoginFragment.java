package com.nbbhatt.mapfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment{

    Button btnLogin ,btnRegister;
    EditText etEmail,etPassword;

    String email ,pass;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_login,container,false);

        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);

        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegister = view.findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFragment registerFragment = new RegisterFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, registerFragment);
                fragmentTransaction.commit();
            }
        });


        return view;
    }


    //this is for checking the user login....................
    private void loginUser(){
        email = etEmail.getText().toString();
        pass = etPassword.getText().toString();
        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if (!pass.isEmpty()){

                mAuth.signInWithEmailAndPassword(email,pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(getContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                                //open home
                                openUserMainPageFragment();
                                MainActivity activity = new MainActivity();
                                activity.setCurrentUserHeader();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Login failed",Toast.LENGTH_SHORT).show();
                    }
                });

            }else{
                etEmail.setError("Empty field not allowed");
            }
        }else if(email.isEmpty()){
            etEmail.setError("Empty field not allowed");
        }else{
            etEmail.setError("Please enter correct email");
        }

    }


    public void openUserMainPageFragment(){
        LocationFragment currentUserDataFragment = new LocationFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, currentUserDataFragment);
        fragmentTransaction.commit();
    }


}
