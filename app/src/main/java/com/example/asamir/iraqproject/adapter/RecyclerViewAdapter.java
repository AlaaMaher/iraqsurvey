package com.example.asamir.iraqproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.example.asamir.iraqproject.Models.PhotoModel;
import com.example.asamir.iraqproject.R;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.IDN;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.ViewHolder>{

    List<PhotoModel> mValues = new ArrayList<>();
    Context mContext;
    protected ItemListener mListener;
    int type;

    // if type ==1 mean load url for image
    // if type == 0 mean load URI for image

    public RecyclerViewAdapter(Context context, List<PhotoModel> values, ItemListener itemListener,int type) {

        mValues = values;
        mContext = context;
        mListener=itemListener;
        this.type=type;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

      //  public TextView textView;
        public ImageView imageView;
        PhotoModel item;
        public Button txtDelete;
        public ViewHolder(View v) {

            super(v);

            v.setOnClickListener(this);
            imageView = (ImageView) v.findViewById(R.id.imageOffice);

            txtDelete=(Button) v.findViewById(R.id.txt_delete);

        }

        public void setData(PhotoModel item) {
            this.item = item;
            imageView.setImageURI(item.getOfficephoto());



            //imageView.setImageResource(item.drawable);
           // relativeLayout.setBackgroundColor(Color.parseColor(item.color));

        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.photo_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, final int position) {
        if (type ==1)
        {
            String imageUrl=mValues.get(position).getImageUrl();
            Log.e("Image-->",imageUrl);
            Picasso.with(mContext).load(imageUrl).resize(100, 100).into(Vholder.imageView);
            Vholder.txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(position);
                    String imageId=mValues.get(position).getImageid();
                }
            });



        }else {
            Uri imageUri=mValues.get(position).getOfficephoto();
            Picasso.with(mContext).load(imageUri).resize(100, 100).into(Vholder.imageView);
            Vholder.txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(position);



                }
            });
        }

    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(PhotoModel item);
    }
    public void removeItem(int position){
        mValues.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mValues.size());
    }

}