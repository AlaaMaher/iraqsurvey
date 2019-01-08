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
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    public List<Comment> data = Collections.emptyList();
    Comment current;
    int currentPos = 0;
    private CommentItemClick lOnClickListener;
    public CommentAdapter(CommentItemClick listener) {
        lOnClickListener = listener;
    }
    public void setCommentData(List<Comment> recipesIn, Context context) {
        data = recipesIn;
        mContext = context;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.comment_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        MyHolder viewHolder = new MyHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        current = data.get(position);
        myHolder.project_name.setText(current.getProjectName());

        myHolder.comment_id.setText(current.getComment());

        myHolder.commentEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lOnClickListener.EditBtn(v, position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyHolder extends RecyclerView.ViewHolder {

        TextView project_name ,comment_id ;
        ImageView commentEdit ;

        public MyHolder(View itemView) {
            super(itemView);
            project_name = (TextView) itemView.findViewById(R.id.project_name);
            comment_id = (TextView) itemView.findViewById(R.id.comment_id);
            commentEdit = (ImageView) itemView.findViewById(R.id.commentEdit);
        }
    }
    public void clear() {
        final int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0, size);
    }
}