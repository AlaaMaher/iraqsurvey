package com.example.asamir.iraqproject.AddFormData;

import android.app.Activity;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asamir.iraqproject.ConstMethods;
import com.example.asamir.iraqproject.LoginActivity;
import com.example.asamir.iraqproject.Models.JobsModel;
import com.example.asamir.iraqproject.OfflineWork.Database;
import com.example.asamir.iraqproject.OfflineWork.Entities.JobEntity;
import com.example.asamir.iraqproject.OfflineWork.OfflineAdapters.JobsOfflineSpinnerAdapter;
import com.example.asamir.iraqproject.ProjectsActivity;
import com.example.asamir.iraqproject.R;
import com.example.asamir.iraqproject.RegistedList;
import com.example.asamir.iraqproject.ViewFormData.PositionTablesActivity;
import com.example.asamir.iraqproject.ViewFormData.SavedDataListActivity;
import com.example.asamir.iraqproject.adapter.JobsSpinnerAdapter;
import com.example.asamir.iraqproject.adapter.JobsTableAdapter;
import com.example.asamir.iraqproject.comments.CommentsActivity;
import com.example.asamir.iraqproject.util.ConnectivityHelper;
import com.example.asamir.iraqproject.util.FixedGridLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PositionTableScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    int scrollX = 0;
    ArrayList<JobsModel> jobList = new ArrayList<>();
    @BindView(R.id.rvJobs)
    RecyclerView rvJobs;
    @BindView(R.id.tvEmptyList)
    LinearLayout tvEmptyList;
    JobsTableAdapter roomsTableAdapter;
    Dialog myDialog;
    private String result;
    @BindView(R.id.tvTootBarTitle)
    TextView tvToolBar;
    @BindView(R.id.logoXmarks)
    ImageView logo;
    ArrayList<JobsModel> projectsModels = new ArrayList<>();
    private String jobName;
    ArrayList<String>  jobNameList=new ArrayList();
     EditText edt_rooms_count;
    List<JobEntity> jobList1 = new ArrayList<>();
    private String pn;
    private Database jobDataBase;


    @BindView(R.id.text_next_click)
    TextView nextClick;
    @BindView(R.id.text_back_click)
    TextView backClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_screen);
        findViewById(R.id.add_job_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvToolBar.setText("الوظائف");
        logo.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        jobDataBase = Room.databaseBuilder(getApplicationContext(),
                Database.class, "jobTable").allowMainThreadQueries().build();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setUpRecyclerView();
        rvJobs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollX += dx;


            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        myDialog = new Dialog(getApplicationContext());

    nextClick.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (jobList.size()==0)
            {
                Toast.makeText(getApplicationContext(), "يجب ادخال وظيفة واحدة علي الاقل ", Toast.LENGTH_LONG).show();
            }else {
                saveData();
                startActivity(new Intent(PositionTableScreen.this, RoomTable.class));
            }
        }
    });


    backClick.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            new AlertDialog.Builder(PositionTableScreen.this)
                    .setMessage("سوف يتم فقد بيانات مسجلة بهذه الصفحة هل أنت متاكد من الخروج من الصفحة ؟ ")
                    .setCancelable(false)
                    .setPositiveButton("متابعة", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("الغاء", null)
                    .show();

        }
    });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            new AlertDialog.Builder(this)
                    .setMessage("هل تريد حقاً الخروج من البحث الميدانى؟")
                    .setCancelable(false)
                    .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            logOut();
                        }
                    })
                    .setNegativeButton("لا", null)
                    .show();
        } else if (id == R.id.nav_list) {
            new AlertDialog.Builder(this)
                    .setMessage("سوف يتم فقد جميع البيانات المسجله هل أنت متاكد من الخروج من الصفحة ؟ ")
                    .setCancelable(false)
                    .setPositiveButton("متابعة", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(PositionTableScreen.this, RegistedList.class));
                            finish();
                        }
                    })
                    .setNegativeButton("الغاء", null)
                    .show();

        } else if (id == R.id.nav_add_new) {
            if (ConnectivityHelper.isConnectedToNetwork(PositionTableScreen.this)) {
                startActivity(new Intent(PositionTableScreen.this, NewUiSurveyScreen.class));
                finish();
            } else {
                startActivity(new Intent(PositionTableScreen.this, OfflineSurvayActivity.class));
                finish();
            }
        } else if (id == R.id.nav_change_project) {
            startActivity(new Intent(PositionTableScreen.this, ProjectsActivity.class));
            finish();
        }else if(id ==R.id.nav_comment){
            startActivity(new Intent(PositionTableScreen.this, CommentsActivity.class));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        ConstMethods.saveUserLoginInfo(null , null , PositionTableScreen.this);

        startActivity(new Intent(PositionTableScreen.this, LoginActivity.class));
        Database govDataBase = Room.databaseBuilder(getApplicationContext(),
                Database.class, "govTable").allowMainThreadQueries().build();
        Database citiesDataBase = Room.databaseBuilder(getApplicationContext(),
                Database.class, "cityTable").allowMainThreadQueries().build();

        Database districDataBase = Room.databaseBuilder(getApplicationContext(),
                Database.class, "districTable").allowMainThreadQueries().build();

        Database officeDataBase = Room.databaseBuilder(getApplicationContext(),
                Database.class, "officeTable").allowMainThreadQueries().build();

        Database userProjectsDB = Room.databaseBuilder(getApplicationContext(),
                Database.class, "userProjects").allowMainThreadQueries().build();
        govDataBase.userDao().deleteGovData();
        citiesDataBase.userDao().deleteCityData();
        districDataBase.userDao().deleteDistrictsData();
        officeDataBase.userDao().deleteOfficeData();
        userProjectsDB.userDao().deleteUserProjectsData();

        finish();
        Toast.makeText(getApplicationContext(), "تم تسجيل الخروج بنجاح", Toast.LENGTH_LONG).show();
    }

    public void goTONext(View view) {

        if (jobList.size()==0)
        {
            Toast.makeText(getApplicationContext(), "يجب ادخال وظيفة واحدة علي الاقل ", Toast.LENGTH_LONG).show();
        }else {
            saveData();
            startActivity(new Intent(PositionTableScreen.this, RoomTable.class));
        }
        }

    public void saveData() {
        Gson gson = new Gson();
        String roomsArray = gson.toJson(jobList);
        ConstMethods.savePositions(getApplicationContext(), roomsArray);
    }

    private void setUpRecyclerView() {
        if (jobList.isEmpty()) {
            rvJobs.setVisibility(View.GONE);
            tvEmptyList.setVisibility(View.VISIBLE);
        } else {
            rvJobs.setVisibility(View.VISIBLE);
            tvEmptyList.setVisibility(View.GONE);
        }
        roomsTableAdapter = new JobsTableAdapter(PositionTableScreen.this, jobList,jobNameList);
        FixedGridLayoutManager manager = new FixedGridLayoutManager();
        manager.setTotalColumnCount(1);
        rvJobs.setLayoutManager(manager);
        rvJobs.setAdapter(roomsTableAdapter);
        rvJobs.addItemDecoration(new DividerItemDecoration(PositionTableScreen.this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addroommenu, menu);
        return true;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        getMenuInflater().inflate(R.menu.addroommenu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_add:
                showAddDialog();
                return false;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                result = data.getStringExtra("job");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(PositionTableScreen.this, "لم يتم اختيار وظيفة", Toast.LENGTH_LONG).show();
            }
        }
    }//o

    public void showAddDialog() {


        LayoutInflater li = LayoutInflater.from(PositionTableScreen.this);
        final View promptsView = li.inflate(R.layout.addjobdialog, null);
      //  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PositionTableScreen.this);
        //alertDialogBuilder.setView(promptsView);
        final Dialog dialog  = new Dialog(PositionTableScreen.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.addjobdialog);
        Button addBtn = dialog.findViewById(R.id.button_add_dialog);
        Button cancelBtn=dialog.findViewById(R.id.button_cancel_dialog);
       // alertDialogBuilder.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

         edt_rooms_count = dialog.findViewById(R.id.edt_roomCount);
        final EditText edt_job_note = dialog.findViewById(R.id.edt_job_note);
        final Spinner spinnerJobs = dialog.findViewById(R.id.spinnerJobs);

        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {*/
                // Do something after 5s = 5000ms
                if (ConnectivityHelper.isConnectedToNetwork(PositionTableScreen.this)) {
                    // Spinner click listener

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("positions").child(ConstMethods.getSavedprogectid(PositionTableScreen.this));
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projectsModels.clear();
                projectsModels.add(0,new JobsModel("--أختر--", "", "dummyid"));
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    projectsModels.add(new JobsModel(dataSnapshot1.child("position_name").getValue().toString(), dataSnapshot1.child("position_name").getValue().toString(), dataSnapshot1.getKey()));
                }
                JobsSpinnerAdapter citiesSpinnerAdapter = new JobsSpinnerAdapter(PositionTableScreen.this, R.layout.spinneritem, projectsModels);
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
                jobName = projectsModels.get(position).getName();
                if(jobNameList.contains(jobName)) {
                    Toast.makeText(PositionTableScreen.this, "هذا الوظيفة مضافة من قبل بالفعل ", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
                }else{
                    pn=ConstMethods.getSavedprogectid(PositionTableScreen.this);

        /*
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms*/
                    // Spinner click listener
                    spinnerJobs.setPrompt("أختار الوظيفة");
                    jobList1.add(0,new JobEntity("dummyid","--أختر--",""));
                    for (int i = 0; i < jobDataBase.userDao().getJobsByProject(pn).size(); i++) {
                        Log.e("Gov DATA --->", jobDataBase.userDao().getJobsByProject(pn).get(i).toString());
                        jobList1.add(new JobEntity(jobDataBase.userDao().getJobsByProject(pn).get(i).getJobId(), jobDataBase.userDao().getJobsByProject(pn).get(i).getJobName(),pn));
                    }
                    JobsOfflineSpinnerAdapter govofflineSpinnerAdapter = new JobsOfflineSpinnerAdapter(PositionTableScreen.this, R.layout.spinneritem, jobList1);
                    spinnerJobs.setAdapter(govofflineSpinnerAdapter);
                    spinnerJobs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            jobName = jobList1.get(position).getJobName();
                            //Log.e("KEY-->", strGovId);
                            //citiesList.clear();
                            //iniCitiesSpinner();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            Toast.makeText(PositionTableScreen.this, "Please Choose Your Job", Toast.LENGTH_SHORT).show();


                        }
                    });

                }
            /*}
        }, 1000);*/

                    addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_job_note.getText().toString().trim().isEmpty() &&edt_rooms_count.getText().toString().trim().isEmpty()&&jobName.equals("--أختر--"))
                {
                    Toast.makeText(PositionTableScreen.this,"برجاء ادخال جميع الحقول المطلوبة واختيار الوظيفة",Toast.LENGTH_LONG).show();


                }
                else if(edt_rooms_count.getText().toString().trim().isEmpty()){
                    //  Toast.makeText(PositionTableScreen.this,"برجاء ادخال العدد",Toast.LENGTH_LONG).show();
                    edt_rooms_count.setError("برجاء ادخال العدد");
                }
                else if(jobName.equals("--أختر--")){
                    Toast.makeText(PositionTableScreen.this,"اختر ليست وظيفه",Toast.LENGTH_LONG).show();
                }

                else  {



                    if(jobNameList.contains(jobName))
                    {
                        Toast.makeText(PositionTableScreen.this,"هذا الوظيفة مضافة من قبل بالفعل ",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(PositionTableScreen.this,"تم أضافة("+jobName +")كوظيفة جديدة ",Toast.LENGTH_LONG).show();
                        final String roomCount = edt_rooms_count.getText().toString();

                        final String note = edt_job_note.getText().toString();
                        jobNameList.add(jobName);
                        jobList.add(new JobsModel(jobName, roomCount, note));
                        roomsTableAdapter.notifyData(jobList,jobNameList);
                        if (jobList.isEmpty()) {
                            rvJobs.setVisibility(View.GONE);
                            tvEmptyList.setVisibility(View.VISIBLE);

                        } else {

                            rvJobs.setVisibility(View.VISIBLE);
                            tvEmptyList.setVisibility(View.GONE);
                        }
                    }

                    dialog.cancel();
                }

            }
        });

        dialog.show();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        // set dialog message

      /*  alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("إضافة",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (edt_job_note.getText().toString().trim().isEmpty() &&edt_rooms_count.getText().toString().trim().isEmpty()&&jobName.equals("--أختر--"))
                                {
                                    Toast.makeText(PositionTableScreen.this,"برجاء ادخال جميع الحقول المطلوبة واختيار الوظيفة",Toast.LENGTH_LONG).show();


                                }
                                else if(edt_rooms_count.getText().toString().trim().isEmpty()){
                              //  Toast.makeText(PositionTableScreen.this,"برجاء ادخال العدد",Toast.LENGTH_LONG).show();
                                    edt_rooms_count.setError("برجاء ادخال العدد");
                                }
                                else if(jobName.equals("--أختر--")){
                                    Toast.makeText(PositionTableScreen.this,"اختر ليست وظيفه",Toast.LENGTH_LONG).show();
                                }

                                else  {



                                    if(jobNameList.contains(jobName))
                                    {
                                        Toast.makeText(PositionTableScreen.this,"هذا الوظيفة مضافة من قبل بالفعل ",Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(PositionTableScreen.this,"تم أضافة("+jobName +")كوظيفة جديدة ",Toast.LENGTH_LONG).show();
                                        final String roomCount = edt_rooms_count.getText().toString();

                                        final String note = edt_job_note.getText().toString();
                                        jobNameList.add(jobName);
                                        jobList.add(new JobsModel(jobName, roomCount, note));
                                        roomsTableAdapter.notifyData(jobList);
                                        if (jobList.isEmpty()) {
                                            rvJobs.setVisibility(View.GONE);
                                            tvEmptyList.setVisibility(View.VISIBLE);

                                        } else {

                                            rvJobs.setVisibility(View.VISIBLE);
                                            tvEmptyList.setVisibility(View.GONE);
                                        }
                                    }

                                 //  dialog.cancel();
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
        */
    }
    public void closeScreen(View view) {
        new AlertDialog.Builder(this)
                .setMessage("سوف يتم فقد بيانات مسجلة بهذه الصفحة هل أنت متاكد من الخروج من الصفحة ؟ ")
                .setCancelable(false)
                .setPositiveButton("متابعة", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("الغاء", null)
                .show();
    }

    public void add_fad(View view) {
        showAddDialog();
    }

    public void goTOback(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
