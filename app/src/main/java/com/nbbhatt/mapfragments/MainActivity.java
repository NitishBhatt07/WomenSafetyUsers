package com.nbbhatt.mapfragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    //for logout button...........
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    static TextView currentUserName,currentUserEmail,currentUserNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runTimePermission();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);


        View view =  navigationView.inflateHeaderView(R.layout.drawer_header);
        currentUserName = view.findViewById(R.id.currentUserName);
        currentUserEmail = view.findViewById(R.id.currentUserEmail);
        currentUserNumber = view.findViewById(R.id.currentUserNumber);

        currentUserName.setText(" ");
        currentUserEmail.setText(" ");
        currentUserNumber.setText(" ");

        navigationView.setNavigationItemSelectedListener(this);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_fragment, new LocationFragment());
            fragmentTransaction.commit();
            setCurrentUserHeader();
        }else{
            //default fragment...............
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_fragment, new RegisterFragment());
            fragmentTransaction.commit();

        }


    }


    public void runTimePermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},100);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {

            case R.id.profile:
                openFragment(new UserProfileFragment());
                break;

            case R.id.login:
                LoginFragment fragment = new LoginFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, fragment);
                fragmentTransaction.commit();
                break;



            case R.id.nav_switch:

                Switch switch_id = findViewById(R.id.switch_id);

                switch_id.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(isChecked){
                            Toast.makeText(MainActivity.this,"On",Toast.LENGTH_SHORT).show();
                            }else{
                            Toast.makeText(MainActivity.this,"Off",Toast.LENGTH_SHORT).show();
                            }
                    }
                });

                break;


            case R.id.allUsers:
                openFragment(new HomeFragment());
                break;


            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this,"User SignOut",Toast.LENGTH_SHORT).show();
                currentUserName.setText(" ");
                currentUserEmail.setText(" ");
                currentUserNumber.setText(" ");
                openFragment(new LoginFragment());
                break;

            case R.id.map:

                GoogleMapFragment mapFragment = new GoogleMapFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, mapFragment);
                fragmentTransaction.commit();

                break;

        }

        return true;
    }

    public void openFragment(Fragment fragment){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, fragment);
        fragmentTransaction.commit();
    }


    public void setCurrentUserHeader(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();
        DocumentReference documentReference = database.document("Users/"+userId);

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){

                            String name = documentSnapshot.getString("Name");
                            String email = documentSnapshot.getString("Email");
                            String number = documentSnapshot.getString("Number");

                            currentUserName.setText(name);
                            currentUserEmail.setText(email);
                            currentUserNumber.setText(number);

                        }else{
                            Toast.makeText(MainActivity.this,"document do not exists",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"permission granted",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show();
        }

    }




}
