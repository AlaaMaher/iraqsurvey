package com.example.asamir.iraqproject.AddFormData;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asamir.iraqproject.ConstMethods;
import com.example.asamir.iraqproject.LoginActivity;
import com.example.asamir.iraqproject.Models.DataCollectionModel;
import com.example.asamir.iraqproject.OfflineWork.Database;
import com.example.asamir.iraqproject.OfflineWork.Entities.SurvayEntity;
import com.example.asamir.iraqproject.ProjectsActivity;
import com.example.asamir.iraqproject.R;
import com.example.asamir.iraqproject.RegistedList;
import com.example.asamir.iraqproject.SendingCompleteActivity;
import com.example.asamir.iraqproject.comments.CommentsActivity;
import com.example.asamir.iraqproject.util.ConnectivityHelper;
import com.example.asamir.iraqproject.util.GpsTracker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.edt_notes)
    EditText edt_notes;
    @BindView(R.id.tvVisitDate)
    TextView tvVisitDate;
    @BindView(R.id.tvLatLang)
    TextView tvLatLang;
    private GpsTracker gpsTracker;
    private double latitude;
    private double longitude;
    @BindView(R.id.btnSendData)
    Button btnSendData;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    private Database survayoffDB;
    @BindView(R.id.tvTootBarTitle)
    TextView tvTootBarTitle;
   DatabaseReference df2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTootBarTitle.setText("الملاحظات");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        tvVisitDate.setText(formattedDate);
        //////////////
        /*gpsTracker = new GpsTracker(NoticeActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

        } else {
            gpsTracker.showSettingsAlert();
        }
        tvLatLang.setText(String.valueOf(latitude) + " -- " + String.valueOf(longitude));*/
        ////////////////






        tvLatLang.setText(String.valueOf(latitude) + " -- " + String.valueOf(longitude));

        getLocation();

        databaseReference = FirebaseDatabase.getInstance().getReference("OFFICE_DATA");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Office");
        survayoffDB = Room.databaseBuilder(getApplicationContext(),
                Database.class, "survayTable").allowMainThreadQueries().build();
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        getLocation();
    }*/

/*
    @Override
    protected void onRestart() {
        super.onRestart();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                String restoredText = prefs.getString("latitude", null);
                if (restoredText != null)
                {
                    //mSaved.setText(restoredText, TextView.BufferType.EDITABLE);
                    latitude = Double.parseDouble(prefs.getString("latitude", ""));
                    longitude = Double.parseDouble(prefs.getString("longitude", ""));

                }

                tvLatLang.setText(String.valueOf(latitude) + " -- " + String.valueOf(longitude));
            }
        }, 3000);

    }
    */

    /*
    @Override
    protected void onStart() {
        super.onStart();
        getLocation();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        //getLocation();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                String restoredText = prefs.getString("latitude", null);
                if (restoredText != null)
                {
                    //mSaved.setText(restoredText, TextView.BufferType.EDITABLE);
                    latitude = Double.parseDouble(prefs.getString("latitude", ""));
                    longitude = Double.parseDouble(prefs.getString("longitude", ""));

                }

                tvLatLang.setText(String.valueOf(latitude) + " -- " + String.valueOf(longitude));
            }
        }, 6000);
    }



    public void getLocation(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                gpsTracker = new GpsTracker(NoticeActivity.this);
                if (gpsTracker.canGetLocation()) {
                    latitude = gpsTracker.getLatitude();
                    longitude = gpsTracker.getLongitude();

                    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                    editor.putString("latitude", String.valueOf(latitude));
                    editor.putString("longitude", String.valueOf(longitude));
                    editor.apply();

                } else {
                    gpsTracker.showSettingsAlert();
                }
                tvLatLang.setText(String.valueOf(latitude) + " -- " + String.valueOf(longitude));
            }
        }, 1000);
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
                    .show();        } else if (id == R.id.nav_list) {
            startActivity(new Intent(NoticeActivity.this, RegistedList.class));
            finish();
        } else if (id == R.id.nav_add_new) {
            if (ConnectivityHelper.isConnectedToNetwork(NoticeActivity.this))
            {
                startActivity(new Intent(NoticeActivity.this, NewUiSurveyScreen.class));
                finish();
            }else {
                startActivity(new Intent(NoticeActivity.this, OfflineSurvayActivity.class));
                finish();
            }
        } else if (id == R.id.nav_change_project) {
            startActivity(new Intent(NoticeActivity.this, ProjectsActivity.class));
            finish();
        }
        else if(id ==R.id.nav_comment){
            startActivity(new Intent(NoticeActivity.this, CommentsActivity.class));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        ConstMethods.saveUserLoginInfo(null , null , NoticeActivity.this);

        startActivity(new Intent(NoticeActivity.this, LoginActivity.class));Database govDataBase = Room.databaseBuilder(getApplicationContext(),
                Database.class, "govTable").allowMainThreadQueries().build();
        Database citiesDataBase = Room.databaseBuilder(getApplicationContext(),
                Database.class, "cityTable").allowMainThreadQueries().build();

        Database districDataBase = Room.databaseBuilder(getApplicationContext(),
                Database.class, "districTable").allowMainThreadQueries().build();

        Database officeDataBase = Room.databaseBuilder(getApplicationContext(),
                Database.class, "officeTable").allowMainThreadQueries().build();

        Database userProjectsDB=Room.databaseBuilder(getApplicationContext(),
                Database.class, "userProjects").allowMainThreadQueries().build();
        govDataBase.userDao().deleteGovData();
        citiesDataBase.userDao().deleteCityData();
        districDataBase.userDao().deleteDistrictsData();
        officeDataBase.userDao().deleteOfficeData();
        userProjectsDB.userDao().deleteUserProjectsData();

        finish();
        Toast.makeText(getApplicationContext(), "تم تسجيل الخروج بنجاح", Toast.LENGTH_LONG).show();
    }

    public void saveData(View view) {


        try {
            JSONObject basicInfoData = new JSONObject(ConstMethods.getSavedBasicInformationData(NoticeActivity.this));
            //JSONObject jsonObjectPageFour = new JSONObject(ConstMethods.getPageFourData(NoticeActivity.this));

            String gov = basicInfoData.getString("gov");
            String address = basicInfoData.getString("address");
            String phone = basicInfoData.getString("phone");
            String hasInternet = basicInfoData.getString("hasInternet");
            String isNetwork = basicInfoData.getString("isNetwork");
            String internetSeed = basicInfoData.getString("internetSeed");
            String projectName = ConstMethods.getSavedprogectid(NoticeActivity.this);
            final String office_name_or_id = basicInfoData.getString("office_name_or_id");
            String officeVisit = basicInfoData.getString("office_visit");

            String  OwnerShipType= basicInfoData.getString("OwnerShipType");
            //---
            String shiftType = basicInfoData.getString("shiftType");
            String morning_shift_from = basicInfoData.getString("morning_shift_from");
            String morning_shift_to = basicInfoData.getString("morning_shift_to");
            String evening_shift_from = basicInfoData.getString("evening_shift_from");
            String evening_shift_to = basicInfoData.getString("evening_shift_to");
            //---

            String other_city = basicInfoData.getString("other_city");
            String other_district = basicInfoData.getString("other_district");
            String cityid = basicInfoData.getString("city");
            String districtId = basicInfoData.getString("district");
            if (other_city.trim().length() != 0) {
                cityid = "";

            }
            if (other_district.trim().length() != 0) {
                districtId = "";
            }
            String computer_count = basicInfoData.getString("computer_count");
            String computer_notes = basicInfoData.getString("computer_notes");
            String printers_count = basicInfoData.getString("printers_count");
            String printers_notes = basicInfoData.getString("printers_notes");
            String scanners_count = basicInfoData.getString("scanners_count");
            String scanners_notes = basicInfoData.getString("scanners_notes");

            String rooms_count = ConstMethods.getRooms(NoticeActivity.this);
            String visitDate = tvVisitDate.getText().toString();
            String notes = edt_notes.getText().toString();
            String lat = String.valueOf(latitude);
            String lng = String.valueOf(longitude);
            // add sketch data
            String sketchData = ConstMethods.getSketch(NoticeActivity.this);
            String outDoorPhotos = ConstMethods.getOutDoorPhotos(NoticeActivity.this);
            String inDoorPhotos = ConstMethods.getInDoorPhotos(NoticeActivity.this);
            //--
            String posisionData = ConstMethods.getPositions(NoticeActivity.this);
            String id = databaseReference.push().getKey();


            /**
             * TODO: make office id changed from survay screen
             * */

            if (ConnectivityHelper.isConnectedToNetwork(NoticeActivity.this)) {
                DataCollectionModel dataCollectionModel=new DataCollectionModel(
                        gov,
                        cityid,
                        districtId,
                        address,
                        phone,
                        hasInternet,
                        isNetwork,
                        internetSeed,
                        projectName,
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
                        other_city,
                        other_district,
                        OwnerShipType);



                HashMap<String, Object> result = new HashMap<>();
                result.put("visited", 1);
                databaseReference2.child(districtId).child(office_name_or_id).updateChildren(result);
                databaseReference.child(office_name_or_id).child(id).setValue(dataCollectionModel);


                ConstMethods.saveSketch(NoticeActivity.this, "");
                ConstMethods.saveInDoorPhotos(NoticeActivity.this, "");
                ConstMethods.saveOutDoorPhotos(NoticeActivity.this, "");

                startActivity(new Intent(NoticeActivity.this, SendingCompleteActivity.class));
                finish();
            } else {


                // save data offline
                survayoffDB.userDao().insertSurvay(new SurvayEntity(
                        gov,
                        cityid,
                        districtId,
                        address,
                        phone,
                        hasInternet,
                        isNetwork,
                        internetSeed,
                        projectName,
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
                        other_city,
                        other_district,
                        OwnerShipType,
                        "false"
                ));
                ConstMethods.saveSketch(NoticeActivity.this, "");
                ConstMethods.saveInDoorPhotos(NoticeActivity.this, "");
                ConstMethods.saveOutDoorPhotos(NoticeActivity.this, "");
                startActivity(new Intent(NoticeActivity.this, SendingCompleteActivity.class));
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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


}
