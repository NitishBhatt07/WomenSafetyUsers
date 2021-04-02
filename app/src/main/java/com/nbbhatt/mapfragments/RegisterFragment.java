package com.nbbhatt.mapfragments;

import android.hardware.usb.UsbRequest;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.HashSet;


public class RegisterFragment extends Fragment {

    Button btnRegister,btnLogin;
    EditText etUsername,etPassword,etEmail,etNumber;

    String username,pass,email,number;

    // for auth
    private FirebaseAuth mAuth;

    FirebaseFirestore fireStore;
    String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register,container,false);


        etUsername = view.findViewById(R.id.etUserName);
        etEmail = view.findViewById(R.id.etEmail);
        etNumber = view.findViewById(R.id.etNumber);
        etPassword = view.findViewById(R.id.etPassword);

        btnRegister = view.findViewById(R.id.btnRegister);
        btnLogin = view.findViewById(R.id.btnLogin);


        //..................
        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginFragment loginFragment = new LoginFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, loginFragment);
                fragmentTransaction.commit();
            }
        });


        return view;
    }


    // this is for creating the user in firebase..............
    private  void createUser(){

        username = etUsername.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        pass = etPassword.getText().toString().trim();
        number = etNumber.getText().toString().trim();


        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if (!pass.isEmpty() && !username.isEmpty() && !number.isEmpty()){

                mAuth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(),"Registered",Toast.LENGTH_SHORT).show();
                                    userId = mAuth.getCurrentUser().getUid();
                                    //if user is registered then we push the data to database
                                    storeData(userId);
                                }else{
                                    Toast.makeText(getContext(),"Registration error",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Registration error",Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                etUsername.setError("Empty field not allowed");
            }
        }else if(email.isEmpty()){
            etEmail.setError("Empty field not allowed");
        }else{
            etEmail.setError("Please enter correct email");
        }
    }

    private void storeData(final String userId){
        //creating the collection called "Users" and every user has a document which is unique userId of every user
        DocumentReference documentReference = fireStore.collection("Users").document(userId);
        HashMap<String,String>  user = new HashMap<>();
        user.put("Name",username);
        user.put("Email",email);
        user.put("Number",number);
        user.put("Password",pass);
        user.put("UserLatitude",null);
        user.put("UserLongitude",null);

        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(),"Data Stored "+userId,Toast.LENGTH_SHORT).show();
            }
        });



    }


}
