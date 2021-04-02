package com.nbbhatt.mapfragments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    ArrayList<Model> dataList;

    public MyAdapter(ArrayList<Model> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyler_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.Email.setText(dataList.get(position).getEmail());
        holder.Name.setText(dataList.get(position).getName());

        holder.Number.setText(dataList.get(position).getNumber());

        holder.Password.setText(dataList.get(position).getPassword());
        holder.UserLatitude.setText(dataList.get(position).getUserLatitude());
        holder.UserLongitude.setText(dataList.get(position).getUserLongitude());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Name,Email,Number,Password,UserLatitude,UserLongitude;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.userText);
            Email = itemView.findViewById(R.id.emailText);
            Number = itemView.findViewById(R.id.numberText);

            Password = itemView.findViewById(R.id.passwordText);
            UserLatitude = itemView.findViewById(R.id.latitudeText);
            UserLongitude = itemView.findViewById(R.id.longitudeText);

        }
    }





}
