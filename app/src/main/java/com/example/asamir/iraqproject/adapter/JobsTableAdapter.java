package com.example.asamir.iraqproject.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.asamir.iraqproject.AddFormData.PositionTableScreen;
import com.example.asamir.iraqproject.ConstMethods;
import com.example.asamir.iraqproject.Models.JobsModel;
import com.example.asamir.iraqproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JobsTableAdapter extends RecyclerView.Adapter<JobsTableAdapter.ClubViewHolder> implements Filterable
{
    private static final int TYPE_ROW = 0;
    private static final int TYPE_ROW_COLORFUL = 1;

    private List<JobsModel> roomList;
    private List<JobsModel> filteredroomList;
    private Context context;
    ArrayList<JobsModel> projectsModels=new ArrayList<>();
    private String jobName;
    public JobsTableAdapter(Context context, List<JobsModel> roomList)
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
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.positionitem, viewGroup, false);
            return new ClubViewHolder(view);
        } else
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.positionitemcolored,
                    viewGroup, false);
            return new ClubViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ClubViewHolder holder, final int position)
    {
        final JobsModel club = filteredroomList.get(position);

        holder.txtName.setText(club.name);
        holder.tvNumInJob.setText(club.num);
        holder.tvNotes.setText(club.note);
        // delete the item in the table
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomList.remove(position);
                notifyData(roomList);
            }
        });

        // edit the iem in the table
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog(club.num,club.note,position);
            }
        });
    }



    public void showAddDialog( String num,String jobNote, final int pos) {
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.addjobdialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final EditText edt_rooms_count = promptsView.findViewById(R.id.edt_roomCount);
        edt_rooms_count.setText(num);
        final EditText edt_job_note = promptsView.findViewById(R.id.edt_job_note);
        edt_job_note.setText(jobNote);
        final Spinner spinnerJobs=promptsView.findViewById(R.id.spinnerJobs);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("positions").child(ConstMethods.getSavedprogectid(context));
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projectsModels.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                    projectsModels.add(new JobsModel(dataSnapshot1.child("position_name").getValue().toString(),dataSnapshot1.child("position_name").getValue().toString(),""));
                }
                JobsSpinnerAdapter citiesSpinnerAdapter  = new JobsSpinnerAdapter(context,R.layout.spinneritem,projectsModels);
                spinnerJobs.setAdapter(citiesSpinnerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        spinnerJobs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jobName=projectsModels.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("تعديل",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                roomList.remove(pos);
                                final String roomCount = edt_rooms_count.getText().toString();
                                final String note = edt_job_note.getText().toString();
                                roomList.add(new JobsModel(jobName, roomCount,note));
                                notifyData(roomList);


                                dialog.cancel();
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
    @Override
    public int getItemCount()
    {
        return filteredroomList.size();
    }

    public class ClubViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtName, tvNumInJob,tvNotes;
        public ImageView ivEdit,ivDelete;

        public ClubViewHolder(View view)
        {
            super(view);
            txtName = view.findViewById(R.id.tvjobTitle);
            tvNumInJob = view.findViewById(R.id.tvNumInJob);
            ivEdit=view.findViewById(R.id.ivEdit);
            ivDelete=view.findViewById(R.id.ivDelete);
            tvNotes=view.findViewById(R.id.tvJobNotes);


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
                    List<JobsModel> filteredList = new ArrayList<>();
                    for (JobsModel club : roomList)
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
                filteredroomList = (ArrayList<JobsModel>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public void notifyData(List<JobsModel> myList) {

        this.roomList = myList;
        notifyDataSetChanged();
    }


}