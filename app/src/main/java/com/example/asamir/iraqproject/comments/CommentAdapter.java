package com.example.asamir.iraqproject.comments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asamir.iraqproject.R;

import java.util.Collections;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    public List<Patient> data = Collections.emptyList();
    Patient current;
    int currentPos = 0;
    private PatientItemClick lOnClickListener;
    public PatientAdapter(PatientItemClick listener) {
        lOnClickListener = listener;
    }
    public void setProductData(List<Patient> recipesIn, Context context) {
        data = recipesIn;
        mContext = context;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.patient_item_list;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        MyHolder viewHolder = new MyHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        current = data.get(position);
        myHolder.name.setText(current.getPat_name());
        myHolder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lOnClickListener.deleteBtn(v, position);
            }
        });

        myHolder.show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lOnClickListener.showBtn(v, position);
            }
        });

        myHolder.message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lOnClickListener.messageBtn(v, position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyHolder extends RecyclerView.ViewHolder {
        ImageView message_btn, show_btn, delete_btn;
        TextView name;

        public MyHolder(View itemView) {
            super(itemView);
            message_btn = (ImageView) itemView.findViewById(R.id.message_btn);
            show_btn = (ImageView) itemView.findViewById(R.id.show_btn);
            delete_btn = (ImageView) itemView.findViewById(R.id.delete_btn);
            name = (TextView) itemView.findViewById(R.id.patient_name);
        }
    }
    public void clear() {
        final int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0, size);
    }
}