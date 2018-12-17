package com.example.asamir.iraqproject.ViewFormData;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asamir.iraqproject.AddFormData.SurvayScreen;
import com.example.asamir.iraqproject.LoginActivity;
import com.example.asamir.iraqproject.Models.CitiesModels;
import com.example.asamir.iraqproject.Models.DataCollectionModel;
import com.example.asamir.iraqproject.Models.DistrictsModels;
import com.example.asamir.iraqproject.Models.GovModels;
import com.example.asamir.iraqproject.OfflineWork.Database;
import com.example.asamir.iraqproject.ProjectsActivity;
import com.example.asamir.iraqproject.R;
import com.example.asamir.iraqproject.RegistedList;
import com.example.asamir.iraqproject.adapter.CitiesSpinnerAdapter;
import com.example.asamir.iraqproject.adapter.DistricSpinnerAdapter;
import com.example.asamir.iraqproject.adapter.GovSpinnerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedDataListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.spinnerGov)
    Spinner spinnerGov;
    @BindView(R.id.spinnerCities)
    Spinner spinnerCities;
    @BindView(R.id.spinnerDistrict)
    Spinner spinnerDistrict;
    @BindView(R.id.spinnerOfficeName)
    Spinner spinnerOfficeName;
    @BindView(R.id.btn_view_saved_data)
    Button btn_view_saved_data;

    List<GovModels> govList = new ArrayList<>();
    List<CitiesModels> citiesList = new ArrayList<>();
    List<DistrictsModels> districsList = new ArrayList<>();
    List<DistrictsModels> officesList = new ArrayList<>();
    @BindView(R.id.tvTootBarTitle)
    TextView tvTootBarTitle;
    private String strGovId;
    private String strCityId;
    private String strDistricId;
    private String strofficeid;
    private DataCollectionModel dataCollectionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_data_list);
        ButterKnife.bind(this);
        iniGovSpinner();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTootBarTitle.setText("عرض البيانات المسجلة");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btn_view_saved_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("OFFICE_DATA/" + strofficeid);
                // Attach a listener to read the data at our posts reference
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            if (dataSnapshot1.child("userID").getValue().toString().equals(FirebaseAuth.getInstance().getUid())) {

                                String projectobjectid = dataSnapshot1.getKey();
                                String gov = dataSnapshot1.child("gov").getValue().toString();
                                String cityid = dataSnapshot1.child("cityId").getValue().toString();
                                String districtId = dataSnapshot1.child("districId").getValue().toString();
                                String address = dataSnapshot1.child("address").getValue().toString();
                                String phone = dataSnapshot1.child("phone").getValue().toString();
                                String hasInternet = dataSnapshot1.child("hasInternet").getValue().toString();
                                String isNetwork = dataSnapshot1.child("isNetwork").getValue().toString();
                                String internetSeed = dataSnapshot1.child("internetSeed").getValue().toString();
                                String projectId = dataSnapshot1.child("office_name_or_id").getValue().toString();
                                String office_name_or_id = dataSnapshot1.child("office_name_or_id").getValue().toString();
                                //---
                                String shiftType = dataSnapshot1.child("shiftType").getValue().toString();
                                String morning_shift_from = dataSnapshot1.child("morning_shift_from").getValue().toString();
                                String morning_shift_to = dataSnapshot1.child("morning_shift_to").getValue().toString();
                                String evening_shift_from = dataSnapshot1.child("evening_shift_from").getValue().toString();
                                String evening_shift_to = dataSnapshot1.child("evening_shift_to").getValue().toString();
                                //---
                                String computer_count = dataSnapshot1.child("computer_count").getValue().toString();
                                String computer_notes = dataSnapshot1.child("computer_notes").getValue().toString();
                                String printers_count = dataSnapshot1.child("printers_count").getValue().toString();
                                String printers_notes = dataSnapshot1.child("printers_notes").getValue().toString();
                                String scanners_count = dataSnapshot1.child("scanners_count").getValue().toString();
                                String scanners_notes = dataSnapshot1.child("scanners_notes").getValue().toString();

                                //--
                                String visitDate = dataSnapshot1.child("visitDate").getValue().toString();
                                String notes = dataSnapshot1.child("notes").getValue().toString();
                                String lat = dataSnapshot1.child("lat").getValue().toString();
                                String lng = dataSnapshot1.child("lng").getValue().toString();

                                // add sketch data
                                String sketchData = dataSnapshot1.child("sketchData").getValue().toString();
                                String outDoorPhotos = dataSnapshot1.child("outDoorPhotos").getValue().toString();
                                String inDoorPhotos = dataSnapshot1.child("inDoorPhotos").getValue().toString();
                                String rooms_count = dataSnapshot1.child("rooms_count").getValue().toString();
                                String posisionData = dataSnapshot1.child("postionData").getValue().toString();
                                String other_city = dataSnapshot1.child("otherCity").getValue().toString();
                                String other_district = dataSnapshot1.child("otherDistric").getValue().toString();
                                dataCollectionModel = new DataCollectionModel(
                                        gov,
                                        cityid,
                                        districtId,
                                        address,
                                        phone,
                                        hasInternet,
                                        isNetwork,
                                        internetSeed,
                                        projectId,
                                        shiftType,
                                        morning_shift_from,
                                        morning_shift_to,
                                        evening_shift_from,
                                        evening_shift_to,
                                        computer_count,
                                        computer_notes,
                                        printers_count,
                                        printers_notes,
                                        scanners_count,
                                        scanners_notes,
                                        rooms_count,
                                        notes,
                                        FirebaseAuth.getInstance().getUid(),
                                        visitDate,
                                        lat,
                                        lng,
                                        sketchData,
                                        outDoorPhotos,
                                        inDoorPhotos,
                                        posisionData,
                                        office_name_or_id,
                                        projectobjectid,
                                        other_city,
                                        other_district
                                );


                                Intent intent = new Intent(getBaseContext(), BasicInfoActivity.class);
                                intent.putExtra("data", dataCollectionModel);
                                startActivity(intent);


                            } else {
                                Toast.makeText(SavedDataListActivity.this, "لا يوجد مشاريع لديك لعرضها", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
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
            startActivity(new Intent(SavedDataListActivity.this, RegistedList.class));
            finish();
        } else if (id == R.id.nav_add_new) {
            startActivity(new Intent(SavedDataListActivity.this, SurvayScreen.class));
            finish();
        } else if (id == R.id.nav_change_project) {
            startActivity(new Intent(SavedDataListActivity.this, ProjectsActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(SavedDataListActivity.this, LoginActivity.class));
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

    public void iniGovSpinner() {
        // Spinner click listener

        spinnerGov.setPrompt("أختار المحافظة");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Governorate");
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.e("NAME AND VALUE -->", dataSnapshot1.child("name").getValue() + "\n" + dataSnapshot1.getKey());
                    govList.add(new GovModels(dataSnapshot1.getKey(), dataSnapshot1.child("name").getValue().toString()));
                }
                GovSpinnerAdapter govSpinnerAdapter = new GovSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, govList);
                spinnerGov.setAdapter(govSpinnerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        spinnerGov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strGovId = govList.get(position).getId();
                Log.e("KEY-->", strGovId);
                citiesList.clear();


                iniCitiesSpinner();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    public void iniCitiesSpinner() {
        // Spinner click listener
        spinnerCities.setOnItemSelectedListener(this);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("City").child(strGovId);
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    citiesList.add(new CitiesModels(dataSnapshot1.getKey(), dataSnapshot1.child("name").getValue().toString()));
                }
                CitiesSpinnerAdapter citiesSpinnerAdapter = new CitiesSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, citiesList);
                spinnerCities.setAdapter(citiesSpinnerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (citiesList.size() != 0) {
                    strCityId = citiesList.get(position).getId();
                    iniDistrictsSpinner();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void iniDistrictsSpinner() {

        // Spinner Drop down elements


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("District").child(strCityId);
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    districsList.add(new DistrictsModels(dataSnapshot1.getKey(), dataSnapshot1.child("name").getValue().toString()));
                }
                DistricSpinnerAdapter citiesSpinnerAdapter = new DistricSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, districsList);
                spinnerDistrict.setAdapter(citiesSpinnerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (districsList.size() != 0) {
                    strDistricId = districsList.get(position).getId();
                    iniOfficesSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void iniOfficesSpinner() {

        // Spinner Drop down elements


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Office").child(strDistricId);
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    officesList.add(new DistrictsModels(dataSnapshot1.getKey(), dataSnapshot1.child("office_name").getValue().toString()));
                }
                DistricSpinnerAdapter citiesSpinnerAdapter = new DistricSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, officesList);
                spinnerOfficeName.setAdapter(citiesSpinnerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        spinnerOfficeName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (officesList.size() != 0) {
                    strofficeid = officesList.get(position).getId();
                    Log.e("OFFICE ID -->", strofficeid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
