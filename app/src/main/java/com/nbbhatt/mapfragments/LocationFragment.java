package com.nbbhatt.mapfragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;



public class LocationFragment extends Fragment {

    Switch locationBtn;
    TextView txt_time;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    MainActivity activity = new MainActivity();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_location, container, false);

        locationBtn = view.findViewById(R.id.location_switch_btn);
        txt_time = view.findViewById(R.id.txt_time);


        locationBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    //check condition
                    startLocationService();

                } else {
                    stopLocationService();
                }
            }
        });


        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startLocationService();
            }else{
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager =
                (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo services :
                    activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(services.service.getClassName())) {
                    if (services.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }

        return false;
    }


    private  void startLocationService(){
        if(!isLocationServiceRunning()){
            Intent intent = new Intent(getActivity(),LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            getActivity().startService(intent);
            Toast.makeText(getActivity(), "location service started", Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(),LocationService.latitude+","+LocationService.longitude,Toast.LENGTH_SHORT).show();

        }
    }

    private  void  stopLocationService(){
        if(isLocationServiceRunning()){
            Intent intent = new Intent(getActivity(),LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            getActivity().startService(intent);
            Toast.makeText(getActivity(), "location service stopped", Toast.LENGTH_SHORT).show();
        }
    }



}
