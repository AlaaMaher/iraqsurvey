package com.example.asamir.iraqproject.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.asamir.iraqproject.AddFormData.RoomTable;
import com.example.asamir.iraqproject.Models.RoomsModel;
import com.example.asamir.iraqproject.R;
import com.example.asamir.iraqproject.ViewFormData.RoomsTableActivity;

import java.util.ArrayList;
import java.util.List;

public class RoomsTableAdapter extends RecyclerView.Adapter<RoomsTableAdapter.ClubViewHolder> implements Filterable
{
    private static final int TYPE_ROW = 0;
    private static final int TYPE_ROW_COLORFUL = 1;

    private List<RoomsModel> roomList;
    private List<RoomsModel> filteredroomList;
    private Context context;

    public RoomsTableAdapter(Context context, List<RoomsModel> roomList)
    {
        this.context = context;
        this.roomList = roomList;
        this.filteredroomList = roomList;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position % 2 == 0)
        {
            return TYPE_ROW_COLORFUL;
        }

        return TYPE_ROW;
    }

    @Override
    public ClubViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        if (viewType == TYPE_ROW)
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.roomitem, viewGroup, false);
            return new ClubViewHolder(view);
        } else
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.roomitemcolored,
                    viewGroup, false);
            return new ClubViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ClubViewHolder holder, final int position)
    {
        final RoomsModel club = filteredroomList.get(position);

        holder.txtName.setText(club.getName());
        holder.tvRoomNums.setText(club.getNum());
        holder.tvRoomsFer.setText(club.getRoomThings());

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setMessage("هل تود مسج هذا العنصر ")
                        .setCancelable(false)
                        .setPositiveButton("مسح", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                roomList.remove(position);
                                notifyData(roomList);
                            }
                        })
                        .setNegativeButton("الغاء", null)
                        .show();


            }
        });

        // edit the iem in the table
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setMessage("هل تود تعديل هذا العنصر ")
                        .setCancelable(false)
                        .setPositiveButton("تعديل", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                showAddDialog(club.name,club.num,club.getRoomThings(),position);
                            }
                        })
                        .setNegativeButton("الغاء", null)
                        .show();

            }
        });

    }

    @Override
    public int getItemCount()
    {
        return filteredroomList.size();
    }

    public class ClubViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtName, tvRoomNums, tvDic,tvRoomsFer;
        public ImageView ivEdit,ivDelete;

        public ClubViewHolder(View view)
        {
            super(view);
            txtName = view.findViewById(R.id.tvRoomName);
            tvRoomNums = view.findViewById(R.id.tvRoomNums);
            tvDic = view.findViewById(R.id.tvRoomsThings);
            tvRoomsFer=view.findViewById(R.id.tvRoomsfer);
            ivEdit=view.findViewById(R.id.ivEdit);
            ivDelete=view.findViewById(R.id.ivDelete);
        }
    }

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charString = charSequence.toString();
                if (charString.isEmpty())
                {
                    filteredroomList = roomList;
                } else
                {
                    List<RoomsModel> filteredList = new ArrayList<>();
                    for (RoomsModel club : roomList)
                    {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name
                        if (club.name.toLowerCase().contains(charString.toLowerCase()) )
                        {
                            filteredList.add(club);
                        }
                    }

                    filteredroomList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredroomList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                filteredroomList = (ArrayList<RoomsModel>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public void notifyData(List<RoomsModel> myList) {

        this.roomList = myList;
        notifyDataSetChanged();
    }

    public void showAddDialog(String roomName, String  roomNum, String roomFer, final int pos) {



            LayoutInflater li = LayoutInflater.from(context);
            final View promptsView = li.inflate(R.layout.addroomdialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);
            final EditText edt_roomName = promptsView.findViewById(R.id.edt_roomName);
            edt_roomName.setText(roomName);

            final EditText edt_rooms_count = promptsView.findViewById(R.id.edt_roomCount);
            edt_rooms_count.setText(roomNum);
            final EditText edt_roomFre = promptsView.findViewById(R.id.edt_roomFre);
            edt_roomFre.setText(roomFer);
            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("تعديل ",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    roomList.remove(pos);
                                    final String roomName = edt_roomName.getText().toString();

                                    final String roomCount = edt_rooms_count.getText().toString();

                                    final String roomFer = edt_roomFre.getText().toString();

                                    if (roomName.trim().isEmpty()) {
                                        Toast.makeText(context, "برجاء ادخال اسم الغرفه ! ", Toast.LENGTH_LONG).show();

                                    } else if (roomCount.trim().isEmpty()) {
                                        Toast.makeText(context, "برجاء ادخال عدد الغرف ! ", Toast.LENGTH_LONG).show();
                                    }
//                                else if (roomFer.trim().isEmpty()) {
//                                    Toast.makeText(context,"برجاء ادخال الاثاث الموجود في الغرفه ! ",Toast.LENGTH_LONG).show();
//                                }
                                    else {
                                        // final String roomDisc = edt_desc.getText().toString();
                                        roomList.add(new RoomsModel(roomName, roomCount, roomFer));
                                        notifyData(roomList);

                                        dialog.cancel();
                                    }
                                }
                            })
                    .setNegativeButton("إلغاء",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }


}