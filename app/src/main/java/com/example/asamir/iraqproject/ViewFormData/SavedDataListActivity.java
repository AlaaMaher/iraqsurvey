package com.example.asamir.iraqproject.ViewFormData;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.asamir.iraqproject.AddFormData.NoticeActivity;
import com.example.asamir.iraqproject.AddFormData.OfflineSurvayActivity;
import com.example.asamir.iraqproject.AddFormData.SurvayScreen;
import com.example.asamir.iraqproject.ConstMethods;
import com.example.asamir.iraqproject.LoginActivity;
import com.example.asamir.iraqproject.Models.CitiesModels;
import com.example.asamir.iraqproject.Models.DataCollectionModel;
import com.example.asamir.iraqproject.Models.DistrictsModels;
import com.example.asamir.iraqproject.Models.GovModels;
import com.example.asamir.iraqproject.Models.OfficeModel;
import com.example.asamir.iraqproject.OfflineWork.Database;
import com.example.asamir.iraqproject.OfflineWork.Entities.CityEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.DistricEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.GovEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.OfficeEntity;
import com.example.asamir.iraqproject.OfflineWork.OfflineAdapters.CitiesOfflineSpinnerAdapter;
import com.example.asamir.iraqproject.OfflineWork.OfflineAdapters.DistricofflineSpinnerAdapter;
import com.example.asamir.iraqproject.OfflineWork.OfflineAdapters.GovofflineSpinnerAdapter;
import com.example.asamir.iraqproject.OfflineWork.OfflineAdapters.OfficeofflineSpinnerAdapter;
import com.example.asamir.iraqproject.ProjectsActivity;
import com.example.asamir.iraqproject.R;
import com.example.asamir.iraqproject.RegistedList;
import com.example.asamir.iraqproject.adapter.CitiesSpinnerAdapter;
import com.example.asamir.iraqproject.adapter.DistricSpinnerAdapter;
import com.example.asamir.iraqproject.adapter.GovSpinnerAdapter;
import com.example.asamir.iraqproject.adapter.OfficeAdapter;
import com.example.asamir.iraqproject.util.ConnectivityHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

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
    List<OfficeModel> officesList = new ArrayList<>();
    @BindView(R.id.tvTootBarTitle)
    TextView tvTootBarTitle;
    private String strGovId;
    private String strCityId;
    private String strDistricId;
    private String strofficeid;
    private String strGovName;
    private String strCityName;
    private String strDistricName;
    private String strofficeName;
    private String strDisrtric;
    String officeVisit;
    private DataCollectionModel dataCollectionModel;
    private static Database govDataBase1, citiesDataBase1, districDataBase1, officeDataBase1;
    List<GovEntity> govList1 = new ArrayList<>();
    List<CityEntity> citiesList1 = new ArrayList<>();
    final List<DistricEntity> districsList1 = new ArrayList<>();
    private List<OfficeEntity> officesList1 = new ArrayList<>();
    private DatabaseReference databaseDisReference;
    private String pn;
    private Database officeByProjectDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_data_list);
        ButterKnife.bind(this);

        //pn = getIntent().getExtras().getString("pn");

        /*SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String restoredText = prefs.getString("pn", null);
        if (restoredText != null)
        {
            pn = prefs.getString("pn", "");
        }*/



        databaseDisReference = FirebaseDatabase.getInstance().getReference("District");

        govDataBase1 = Room.databaseBuilder(getApplicationContext(),
                Database.class, "govTable").allowMainThreadQueries().build();
        citiesDataBase1 = Room.databaseBuilder(getApplicationContext(),
                Database.class, "cityTable").allowMainThreadQueries().build();

        districDataBase1 = Room.databaseBuilder(getApplicationContext(),
                Database.class, "districTable").allowMainThreadQueries().build();

        officeDataBase1 = Room.databaseBuilder(getApplicationContext(),
                Database.class, "officeTable").allowMainThreadQueries().build();
        
        officeByProjectDataBase=Room.databaseBuilder(getApplicationContext(),Database.class,"cityTable").allowMainThreadQueries().build();

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

                try {
                    JSONObject basicInfoData = new JSONObject(ConstMethods.getSavedBasicInformationData(SavedDataListActivity.this));

                    officeVisit = basicInfoData.getString("office_visit");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (officeVisit.equals("1")) {

                    if (!strofficeid.equals("dummyid")) {

                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference("OFFICE_DATA/" + strofficeid);
                        // Attach a listener to read the data at our posts reference
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        if (dataSnapshot1.child("userID").getValue().toString().equals(FirebaseAuth.getInstance().getUid())) {
                                            try {
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
                                                String owenerShipType = dataSnapshot1.child("owenerShipType").getValue().toString();

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
                                                        other_district,
                                                        owenerShipType
                                                );


                                                Intent intent = new Intent(getBaseContext(), BasicInfoActivity.class);
                                                intent.putExtra("data", dataCollectionModel);
                                              intent.putExtra("GovId",strGovId);
                                              intent.putExtra("CityId",strCityId);
                                              intent.putExtra("DisId",strDistricId);
                                              intent.putExtra("OffId",strofficeid);
                                              intent.putExtra("GovName",strGovName);
                                                intent.putExtra("CityName",strCityName);
                                                intent.putExtra("DisName",strDistricName);
                                                intent.putExtra("OffName",strofficeName);
                                                startActivity(intent);
                                                break;

                                            } catch (NullPointerException e) {
                                                Toast.makeText(SavedDataListActivity.this, "لا يوجد مشاريع لديك لعرضها", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(SavedDataListActivity.this, "لا يوجد مشاريع لديك لعرضها", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(SavedDataListActivity.this, "لا يوجد مشاريع لديك لعرضها", Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });


                    } else {
                        Toast.makeText(SavedDataListActivity.this, "برجاء أختيار قيمة من القائمة او ادخال المحافظة والمدينة", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(SavedDataListActivity.this, "هذا المكتب لم تتم زيارته من قبل", Toast.LENGTH_SHORT).show();

                }
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

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

        if (ConnectivityHelper.isConnectedToNetwork(SavedDataListActivity.this)) {
            // Spinner click listener

            spinnerGov.setPrompt("أختار المحافظة");

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("Governorate");
            // Attach a listener to read the data at our posts reference
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    govList.clear();
                    govList.add(0, new GovModels("dummyid", "--اختر المحافظة--"));
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
                    strGovName = govList.get(position).getName();

                    Log.e("KEY-->", strGovId);
                    citiesList.clear();


                    iniCitiesSpinner();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else{
            // Spinner click listener
            spinnerGov.setPrompt("أختار المحافظة");
            govList1.add(0,new GovEntity("dummyid","--أختر--"));
            for (int i = 0; i < govDataBase1.userDao().getGovs().size(); i++) {
                Log.e("Gov DATA --->", govDataBase1.userDao().getGovs().get(i).toString());
                govList1.add(new GovEntity(govDataBase1.userDao().getGovs().get(i).getGovId(), govDataBase1.userDao().getGovs().get(i).getGovName()));

            }



            GovofflineSpinnerAdapter govofflineSpinnerAdapter = new GovofflineSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, govList1);
            spinnerGov.setAdapter(govofflineSpinnerAdapter);
            spinnerGov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    strGovId = govList1.get(position).getGovId();
                    strGovName = govList1.get(position).getGovName();
                    Log.e("KEY-->", strGovId);
                    citiesList1.clear();
                    iniCitiesSpinner();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(SavedDataListActivity.this, "Please Choose Your Gov", Toast.LENGTH_SHORT).show();


                }
            });
        }
            }
        }, 2000);


    }



    public void iniCitiesSpinner() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
        if (ConnectivityHelper.isConnectedToNetwork(SavedDataListActivity.this)) {
            // Spinner click listener
            spinnerCities.setOnItemSelectedListener(SavedDataListActivity.this);

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("City").child(strGovId);
            // Attach a listener to read the data at our posts reference
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    citiesList.clear();
                    citiesList.add(0, new CitiesModels("dummyid", "--اختر المدينة--"));
                    if (!strGovId.equals("dummyid")) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            citiesList.add(new CitiesModels(dataSnapshot1.getKey(), dataSnapshot1.child("name").getValue().toString()));
                        }
                        CitiesSpinnerAdapter citiesSpinnerAdapter = new CitiesSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, citiesList);
                        spinnerCities.setAdapter(citiesSpinnerAdapter);
                    } else {
                        CitiesSpinnerAdapter citiesSpinnerAdapter = new CitiesSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, citiesList);
                        spinnerCities.setAdapter(citiesSpinnerAdapter);
                    }

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
                        districsList.clear();
                        strCityId = citiesList.get(position).getId();
                        strCityName = citiesList.get(position).getName();

                        iniDistrictsSpinner();

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else{
            // Spinner click listener
            spinnerCities.setPrompt("أختار المدينة");
            spinnerCities.setOnItemSelectedListener(SavedDataListActivity.this);

            citiesList1.add(0,new CityEntity("dummyid","--أختر--",""));
            if (!strGovId.equals("dummyid"))
            {
                for (int i = 0; i < citiesDataBase1.userDao().getCityBygovId(strGovId).size(); i++) {

                    citiesList1.add(new CityEntity(citiesDataBase1.userDao().getCityBygovId(strGovId).get(i).getCityId(),
                            citiesDataBase1.userDao().getCityBygovId(strGovId).get(i).getCityName(), strGovId));
                }
                CitiesOfflineSpinnerAdapter citiesSpinnerAdapter = new CitiesOfflineSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, citiesList1);
                spinnerCities.setAdapter(citiesSpinnerAdapter);
            }else {
                CitiesOfflineSpinnerAdapter citiesSpinnerAdapter = new CitiesOfflineSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, citiesList1);
                spinnerCities.setAdapter(citiesSpinnerAdapter);
            }



            spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (citiesList1.size() != 0) {
                        strCityId = citiesList1.get(position).getCityId();
                        districsList1.clear();
                        iniDistrictsSpinner();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(SavedDataListActivity.this, "Please Choose Your city", Toast.LENGTH_SHORT).show();

                }
            });
        }
            }
        }, 1000);
    }

    public void iniDistrictsSpinner() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

        if (ConnectivityHelper.isConnectedToNetwork(SavedDataListActivity.this)) {

            // Spinner Drop down elements


            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("District").child(strCityId);
            // Attach a listener to read the data at our posts reference
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    districsList.clear();
                    districsList.add(0, new DistrictsModels("dummyid", "--اختر الحى--"));
                    if (!strCityId.equals("dummyid")) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            districsList.add(new DistrictsModels(dataSnapshot1.getKey(), dataSnapshot1.child("name").getValue().toString()));
                        }
                        DistricSpinnerAdapter citiesSpinnerAdapter = new DistricSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, districsList);
                        spinnerDistrict.setAdapter(citiesSpinnerAdapter);
                    } else {
                        DistricSpinnerAdapter citiesSpinnerAdapter = new DistricSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, districsList);
                        spinnerDistrict.setAdapter(citiesSpinnerAdapter);
                    }
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
                        officesList.clear();
                        strDistricId = districsList.get(position).getId();
                        strDistricName = districsList.get(position).getName();

                        iniOfficesSpinner();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else{
            // Spinner Drop down elements
            spinnerDistrict.setPrompt("أختار الحي");
            districsList1.add(0,new DistricEntity("dummyid","--أختر--",""));
            if (!strCityId.equals(""))
            {
                for (int i = 0; i < districDataBase1.userDao().getDistricByCityId(strCityId).size(); i++) {

                    districsList1.add(new DistricEntity(strCityId, districDataBase1.userDao().getDistricByCityId(strCityId).get(i).getDistricName(),
                            districDataBase1.userDao().getDistricByCityId(strCityId).get(i).getDistricId()));
                }

                DistricofflineSpinnerAdapter citiesSpinnerAdapter = new DistricofflineSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, districsList1);
                spinnerDistrict.setAdapter(citiesSpinnerAdapter);

            }else {
                DistricofflineSpinnerAdapter citiesSpinnerAdapter = new DistricofflineSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, districsList1);
                spinnerDistrict.setAdapter(citiesSpinnerAdapter);
            }



            spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (districsList1.size() != 0) {

                        strDisrtric = districsList1.get(position).getDistricId();
                        officesList1.clear();
                        iniOfficesSpinner();


                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(SavedDataListActivity.this, "Please Choose Your District", Toast.LENGTH_SHORT).show();

                }
            });
        }
            }
        }, 1000);
    }

    public void iniOfficesSpinner() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

        if (ConnectivityHelper.isConnectedToNetwork(SavedDataListActivity.this)) {

            // Spinner Drop down elements


            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("Office").child(strDistricId);
            // Attach a listener to read the data at our posts reference
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    officesList.clear();
                    officesList.add(0, new OfficeModel("dummyid", "--اختر المكتب--", "0"));
                    if (!strDistricId.equals("dummyid")) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            if (dataSnapshot1.child("project_id").getValue().toString().equals(ConstMethods.getSavedprogectid(SavedDataListActivity.this)))
                                officesList.add(new OfficeModel(dataSnapshot1.getKey(), dataSnapshot1.child("office_name").getValue().toString(), dataSnapshot1.child("visited").getValue().toString()));
                        }
                        OfficeAdapter citiesSpinnerAdapter = new OfficeAdapter(SavedDataListActivity.this, R.layout.spinneritem, officesList);
                        spinnerOfficeName.setAdapter(citiesSpinnerAdapter);
                    } else {
                        OfficeAdapter citiesSpinnerAdapter = new OfficeAdapter(SavedDataListActivity.this, R.layout.spinneritem, officesList);
                        spinnerOfficeName.setAdapter(citiesSpinnerAdapter);
                    }
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
                        officeVisit = officesList.get(position).getVis();
                        strofficeName = officesList.get(position).getName();


                        Log.e("OFFICE Vis -->", String.valueOf(officeVisit));


                        Log.e("OFFICE ID -->", strofficeid);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else {
            /*
            // Spinner Drop down elements
            spinnerOfficeName.setPrompt("أختار المكتب");
            officesList1.add(0,new OfficeEntity("dummyid","--أختر--","",""));
            if (!strDisrtric.equals(""))
            {
                for (int i = 0; i < officeDataBase1.userDao().getOfficeByDistricId(strDisrtric).size(); i++) {

                    officesList1.add(new OfficeEntity(officeDataBase1.userDao().getOfficeByDistricId(strDisrtric).get(i).getOfficeId(),
                            officeDataBase1.userDao().getOfficeByDistricId(strDisrtric).get(i).getOfficeName(),
                            strDisrtric, officeDataBase1.userDao().getOfficeByDistricId(strDisrtric).get(i).getProject_id()));
                }
                OfficeofflineSpinnerAdapter citiesSpinnerAdapter = new OfficeofflineSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, officesList1);
                spinnerOfficeName.setAdapter(citiesSpinnerAdapter);
            }else {
                OfficeofflineSpinnerAdapter citiesSpinnerAdapter = new OfficeofflineSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, officesList1);
                spinnerOfficeName.setAdapter(citiesSpinnerAdapter);
            }


            spinnerOfficeName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (officesList1.size() != 0) {
                        strofficeid = officesList1.get(position).getOfficeId();
                        Log.e("OFFICE ID -->", strofficeid);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            */


            pn=ConstMethods.getSavedprogectid(SavedDataListActivity.this);
            //Toast.makeText(SavedDataListActivity.this,pn,Toast.LENGTH_LONG).show();


            // Spinner Drop down elements
            spinnerOfficeName.setPrompt("أختار المكتب");
            officesList1.add(0,new OfficeEntity("dummyid","--أختر--","",""));
            if (!strDisrtric.equals(""))
            {
                for (int i = 0; i < officeDataBase1.userDao().getOfficeByDistricIdandProjectId(strDisrtric,pn).size(); i++) {

                    officesList1.add(new OfficeEntity(officeDataBase1.userDao().getOfficeByDistricIdandProjectId(strDisrtric,pn).get(i).getOfficeId(),
                            officeDataBase1.userDao().getOfficeByDistricIdandProjectId(strDisrtric,pn).get(i).getOfficeName(),
                            strDisrtric, officeDataBase1.userDao().getOfficeByDistricIdandProjectId(strDisrtric,pn).get(i).getProject_id()));
                }
                OfficeofflineSpinnerAdapter citiesSpinnerAdapter = new OfficeofflineSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, officesList1);
                spinnerOfficeName.setAdapter(citiesSpinnerAdapter);
            }else {
                OfficeofflineSpinnerAdapter citiesSpinnerAdapter = new OfficeofflineSpinnerAdapter(SavedDataListActivity.this, R.layout.spinneritem, officesList1);
                spinnerOfficeName.setAdapter(citiesSpinnerAdapter);
            }


            spinnerOfficeName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (officesList1.size() != 0) {
                        strofficeid = officesList1.get(position).getOfficeId();
                        Log.e("OFFICE ID -->", strofficeid);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
            }
        }, 1000);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void closeScreen(View view) {
        finish();
    }

}
