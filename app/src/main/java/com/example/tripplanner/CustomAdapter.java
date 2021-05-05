package com.example.tripplanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private Context context;
    private Activity activity;
    private ArrayList trip_id, trip_location, trip_date, trip_time;

    CustomAdapter(Activity activity, Context context, ArrayList trip_id, ArrayList trip_location, ArrayList trip_date,
                  ArrayList trip_time){
        this.activity = activity;
        this.context = context;
        this.trip_id = trip_id;
        this.trip_location = trip_location;
        this.trip_date = trip_date;
        this.trip_time = trip_time;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.trip_id_txt.setText(String.valueOf(trip_id.get(position)));
        holder.trip_location_txt.setText(String.valueOf(trip_location.get(position)));
        holder.trip_date_txt.setText(String.valueOf(trip_date.get(position)));
        holder.trip_time_txt.setText(String.valueOf(trip_time.get(position)));
        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(trip_id.get(position)));
                intent.putExtra("location", String.valueOf(trip_location.get(position)));
                intent.putExtra("date", String.valueOf(trip_date.get(position)));
                intent.putExtra("time", String.valueOf(trip_time.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return trip_id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView trip_id_txt, trip_location_txt, trip_date_txt, trip_time_txt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            trip_id_txt = itemView.findViewById(R.id.trip_id_txt);
            trip_location_txt = itemView.findViewById(R.id.trip_location_txt);
            trip_date_txt = itemView.findViewById(R.id.trip_date_txt);
            trip_time_txt = itemView.findViewById(R.id.trip_time_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }

    }

}
