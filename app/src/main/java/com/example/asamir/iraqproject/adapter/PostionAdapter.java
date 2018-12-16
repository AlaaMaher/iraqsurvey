package com.example.asamir.iraqproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asamir.iraqproject.Models.JobsModel;
import com.example.asamir.iraqproject.R;

import java.util.List;

public class PostionAdapter extends RecyclerView.Adapter<PostionAdapter.MyViewHolder> {

    private List<JobsModel> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvJobName;

        public MyViewHolder(View view) {
            super(view);
            tvJobName = (TextView) view.findViewById(R.id.tvJobName);

        }
    }


    public PostionAdapter(List<JobsModel> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.popuppostionitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        JobsModel movie = moviesList.get(position);
        holder.tvJobName.setText(movie.getName());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}