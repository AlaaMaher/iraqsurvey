package com.example.asamir.iraqproject;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asamir.iraqproject.AddFormData.NewUiSurveyScreen;
import com.example.asamir.iraqproject.AddFormData.OfflineSurvayActivity;
import com.example.asamir.iraqproject.AddFormData.SurvayScreen;
import com.example.asamir.iraqproject.Models.ProjectsModel;
import com.example.asamir.iraqproject.OfflineWork.Database;
import com.example.asamir.iraqproject.OfflineWork.Entities.UserProjectsEntity;
import com.example.asamir.iraqproject.util.ConnectivityHelper;
import com.example.asamir.iraqproject.util.Sesstion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectsActivity extends AppCompatActivity {


    @BindView(R.id.tvp1)
    LinearLayout tvp1;
    @BindView(R.id.tvp2)
    LinearLayout tvp2;
    @BindView(R.id.tvp3)
    LinearLayout tvp3;
//    @BindView(R.id.tvTootBarTitle)
//    TextView tvTootBarTitle;
    @BindView(R.id.pro_name)
    TextView pro_name;
    @BindView(R.id.pro_name2)
    TextView pro_name2;
    @BindView(R.id.pro_name3)
    TextView pro_name3;




    ArrayList<ProjectsModel> projectsModels = new ArrayList<>();
    ArrayList<UserProjectsEntity> userProjectsEntities = new ArrayList<>();
//    @BindView(R.id.progressBar)
//    ProgressBar progressBar;
    private Database userProjectsDB;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ButterKnife.bind(this);
       // tvTootBarTitle.setText("المشاريع");
        Database survayDB = Room.databaseBuilder(ProjectsActivity.this,
                Database.class, "survayTable").allowMainThreadQueries().build();

        userProjectsDB = Room.databaseBuilder(getApplicationContext(),
                Database.class, "userProjects").allowMainThreadQueries().build();

        tvp1.setVisibility(View.GONE);
        tvp2.setVisibility(View.GONE);
        tvp3.setVisibility(View.GONE);
        userProjectsDB = Room.databaseBuilder(getApplicationContext(),
                Database.class, "userProjects").allowMainThreadQueries().build();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String id = FirebaseAuth.getInstance().getUid();


        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms*/

                if (ConnectivityHelper.isConnectedToNetwork(ProjectsActivity.this)) {
                    if (id != null) {
                        //   progressBar.setVisibility(View.VISIBLE);
                        DatabaseReference ref = database.getReference("Project_user").child(id);
                        // Attach a listener to read the data at our posts reference
                        if (ref != null)
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //    progressBar.setVisibility(View.GONE);
                                    tvp1.setVisibility(View.GONE);
                                    tvp2.setVisibility(View.GONE);
                                    tvp3.setVisibility(View.GONE);
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Log.e("DATA-->", dataSnapshot1.child("project_id").getValue().toString());
                                        projectsModels.add(new ProjectsModel(dataSnapshot1.child("project_id").getValue().toString(), dataSnapshot1.child("project_name").getValue().toString()));
                                    }

                                    if (projectsModels.size() == 3) {
                                        tvp1.setVisibility(View.VISIBLE);
                                        tvp2.setVisibility(View.VISIBLE);
                                        tvp3.setVisibility(View.VISIBLE);
                                        //TODO

                                        pro_name.setText(projectsModels.get(0).getName());
                                        pro_name2.setText(projectsModels.get(1).getName());
                                        pro_name3.setText(projectsModels.get(2).getName());

                                    } else if (projectsModels.size() == 2) {
                                        tvp1.setVisibility(View.VISIBLE);
                                        tvp2.setVisibility(View.VISIBLE);
//TODO

                                        pro_name.setText(projectsModels.get(0).getName());
                                        pro_name2.setText(projectsModels.get(1).getName());

                                        tvp3.setVisibility(View.GONE);
                                    } else if (projectsModels.size() == 1) {
                                        tvp1.setVisibility(View.VISIBLE);

                                /* tvp1.setText(projectsModels.get(0).getName());*/
                                        tvp3.setVisibility(View.GONE);
                                        tvp2.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    //     progressBar.setVisibility(View.GONE);
                                    System.out.println("The read failed: " + databaseError.getCode());

                                }
                            });


                        tvp1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (checkIfBareedOrNot(pro_name.getText().toString())) {
                                    ConstMethods.SaveProjectId(projectsModels.get(0).getId(), ProjectsActivity.this);
                                    ConstMethods.SaveIsBareed("Yes", ProjectsActivity.this);
                                    //Intent i=new Intent(ProjectsActivity.this, SurvayScreen.class);
                                    //i.putExtra("pn",projectsModels.get(0).getName());
                                    //startActivity(i);
                                    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                                    editor.putString("pn", projectsModels.get(0).getName());
                                    editor.apply();
                                    startActivity(new Intent(ProjectsActivity.this, NewUiSurveyScreen.class));

                                    finish();

                                } else {
                                    ConstMethods.SaveIsBareed("No", ProjectsActivity.this);

                                    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                                    editor.putString("pn", projectsModels.get(0).getName());
                                    editor.apply();
                                    startActivity(new Intent(ProjectsActivity.this, NewUiSurveyScreen.class));
                                    finish();

                                    ConstMethods.SaveProjectId(projectsModels.get(0).getId(), ProjectsActivity.this);
                                }

                            }
                        });

                        tvp2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (checkIfBareedOrNot(pro_name2.getText().toString())) {
                                    ConstMethods.SaveIsBareed("Yes", ProjectsActivity.this);
                                    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                                    editor.putString("pn", projectsModels.get(0).getName());
                                    editor.apply();
                                    startActivity(new Intent(ProjectsActivity.this, NewUiSurveyScreen.class));
                                    finish();

                                    ConstMethods.SaveProjectId(projectsModels.get(1).getId(), ProjectsActivity.this);
                                } else {
                                    ConstMethods.SaveIsBareed("No", ProjectsActivity.this);
                                    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                                    editor.putString("pn", projectsModels.get(0).getName());
                                    editor.apply();
                                    startActivity(new Intent(ProjectsActivity.this, NewUiSurveyScreen.class));
                                    finish();

                                    ConstMethods.SaveProjectId(projectsModels.get(1).getId(), ProjectsActivity.this);
                                }
                            }
                        });

                        tvp3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (checkIfBareedOrNot(pro_name3.getText().toString())) {
                                    ConstMethods.SaveIsBareed("Yes", ProjectsActivity.this);
                                    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                                    editor.putString("pn", projectsModels.get(0).getName());
                                    editor.apply();
                                    startActivity(new Intent(ProjectsActivity.this, NewUiSurveyScreen.class));
                                    finish();

                                    ConstMethods.SaveProjectId(projectsModels.get(2).getId(), ProjectsActivity.this);
                                } else {
                                    ConstMethods.SaveIsBareed("No", ProjectsActivity.this);
                                    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                                    editor.putString("pn", projectsModels.get(0).getName());
                                    editor.apply();
                                    startActivity(new Intent(ProjectsActivity.this, NewUiSurveyScreen.class));
                                    finish();

                                    ConstMethods.SaveProjectId(projectsModels.get(2).getId(), ProjectsActivity.this);
                                }
                            }
                        });

                        //Toast.makeText(ProjectsActivity.this, "Test!", Toast.LENGTH_SHORT).show();

                    } else {

                        for (int i = 0; i < userProjectsDB.userDao().getAllUserProjects().size(); i++) {

                            userProjectsEntities.add(new UserProjectsEntity(userProjectsDB.userDao().getAllUserProjects().get(i).getProject_id(),
                                    userProjectsDB.userDao().getAllUserProjects().get(i).getProject_name(),
                                    userProjectsDB.userDao().getAllUserProjects().get(i).getUserId())
                            );

                            Log.e("PRO NAME", userProjectsDB.userDao().getAllUserProjects().get(i).getProject_name());


                        }

            /*
                    uid = ConstMethods.getSavedUserId(ProjectsActivity.this);

                    //Toast.makeText(ProjectsActivity.this, "Test!", Toast.LENGTH_SHORT).show();


                    for (int i = 0; i < userProjectsDB.userDao().getUserProjectsbyId(uid).size(); i++) {

                        userProjectsEntities.add(new UserProjectsEntity(userProjectsDB.userDao().getUserProjectsbyId(uid).get(i).getProject_id(),
                                userProjectsDB.userDao().getUserProjectsbyId(uid).get(i).getProject_name(),
                                userProjectsDB.userDao().getUserProjectsbyId(uid).get(i).getUserId())
                        );

                        Log.e("PRO NAME", userProjectsDB.userDao().getUserProjectsbyId(uid).get(i).getProject_name());


                    }

                    */


                        if (userProjectsEntities.size() == 3) {
                            tvp1.setVisibility(View.VISIBLE);
                            tvp2.setVisibility(View.VISIBLE);
                            tvp3.setVisibility(View.VISIBLE);
                            //TODO

                            pro_name.setText(userProjectsEntities.get(0).getProject_name());
                            pro_name2.setText(userProjectsEntities.get(1).getProject_name());
                            pro_name3.setText(userProjectsEntities.get(2).getProject_name());

                        } else if (userProjectsEntities.size() == 2) {
                            tvp1.setVisibility(View.VISIBLE);
                            tvp2.setVisibility(View.VISIBLE);
                            tvp3.setVisibility(View.GONE);
                            //TODO

                            pro_name.setText(userProjectsEntities.get(0).getProject_name());
                            pro_name2.setText(userProjectsEntities.get(1).getProject_name());

                            tvp3.setVisibility(View.GONE);
                        } else if (userProjectsEntities.size() == 1) {
                            tvp1.setVisibility(View.VISIBLE);
                            tvp2.setVisibility(View.GONE);
                            tvp3.setVisibility(View.GONE);
                            //TODO

                            pro_name.setText(userProjectsEntities.get(0).getProject_name());

                            tvp3.setVisibility(View.GONE);
                            tvp2.setVisibility(View.GONE);
                        }


                        /////////////

                        tvp1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (checkIfBareedOrNot(pro_name.getText().toString())) {
                                    ConstMethods.SaveProjectId(userProjectsEntities.get(0).getProject_id(), ProjectsActivity.this);
                                    ConstMethods.SaveIsBareed("Yes", ProjectsActivity.this);
                                    startActivity(new Intent(ProjectsActivity.this, OfflineSurvayActivity.class));

                                } else {
                                    ConstMethods.SaveIsBareed("No", ProjectsActivity.this);
                                    startActivity(new Intent(ProjectsActivity.this, OfflineSurvayActivity.class));
                                    ConstMethods.SaveProjectId(userProjectsEntities.get(0).getProject_id(), ProjectsActivity.this);
                                }

                            }
                        });

                        tvp2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (checkIfBareedOrNot(pro_name2.getText().toString())) {
                                    ConstMethods.SaveIsBareed("Yes", ProjectsActivity.this);
                                    startActivity(new Intent(ProjectsActivity.this, OfflineSurvayActivity.class));
                                    ConstMethods.SaveProjectId(userProjectsEntities.get(1).getProject_id(), ProjectsActivity.this);
                                } else {
                                    ConstMethods.SaveIsBareed("No", ProjectsActivity.this);
                                    startActivity(new Intent(ProjectsActivity.this, OfflineSurvayActivity.class));
                                    ConstMethods.SaveProjectId(userProjectsEntities.get(1).getProject_id(), ProjectsActivity.this);
                                }
                            }
                        });

                        tvp3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (checkIfBareedOrNot(pro_name3.getText().toString())) {
                                    ConstMethods.SaveIsBareed("Yes", ProjectsActivity.this);
                                    startActivity(new Intent(ProjectsActivity.this, OfflineSurvayActivity.class));
                                    ConstMethods.SaveProjectId(userProjectsEntities.get(2).getProject_id(), ProjectsActivity.this);
                                } else {
                                    ConstMethods.SaveIsBareed("No", ProjectsActivity.this);
                                    startActivity(new Intent(ProjectsActivity.this, OfflineSurvayActivity.class));
                                    ConstMethods.SaveProjectId(userProjectsEntities.get(2).getProject_id(), ProjectsActivity.this);
                                }
                            }
                        });
                    }

            /*}
        }, 3000);*/

              try {
                  Log.e("SURVAY DATA -->", Room.databaseBuilder(getApplicationContext(),
                          Database.class, "survayTable").allowMainThreadQueries().build().userDao().getAllSurvies().toString());
              }
              catch (Exception f) {}

                }
    }

    public void btnNext(View view) {
        startActivity(new Intent(ProjectsActivity.this, SurvayScreen.class));
    }

    public void userLogout(View view) {

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


    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        ConstMethods.saveUserLoginInfo(null , null , ProjectsActivity.this);

        startActivity(new Intent(ProjectsActivity.this, LoginActivity.class));
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

        FirebaseAuth.getInstance().signOut();
        //////////////////////////////////BY_Mohammed
        Sesstion.getInstance(this).logout();
      //////////////////////////////////////////
    }

    public boolean checkIfBareedOrNot(String name) {
        if (name.equals("البريد"))
            return true;
        else
            return false;
    }

    public void closeScreen(View view) {
        finish();
    }

}
