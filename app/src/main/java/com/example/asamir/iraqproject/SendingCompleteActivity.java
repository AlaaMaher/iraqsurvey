package com.example.asamir.iraqproject;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.asamir.iraqproject.AddFormData.IndoorPhotos;
import com.example.asamir.iraqproject.AddFormData.OfflineSurvayActivity;
import com.example.asamir.iraqproject.AddFormData.SurvayScreen;
import com.example.asamir.iraqproject.OfflineWork.Database;
import com.example.asamir.iraqproject.comments.CommentsActivity;
import com.example.asamir.iraqproject.util.ConnectivityHelper;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendingCompleteActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.tvTootBarTitle)
    TextView tvTootBarTitleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_complete);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTootBarTitleTextView.setText("تم ارسال البيانات بنجاح");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public void goToMain(View view) {
        if (ConnectivityHelper.isConnectedToNetwork(SendingCompleteActivity.this)) {
            ConstMethods.saveSketch(SendingCompleteActivity.this, "");
            ConstMethods.saveInDoorPhotos(SendingCompleteActivity.this, "");
            ConstMethods.saveOutDoorPhotos(SendingCompleteActivity.this, "");
            startActivity(new Intent(SendingCompleteActivity.this, SurvayScreen.class));
            finish();

        } else {
            ConstMethods.saveSketch(SendingCompleteActivity.this, "");
            ConstMethods.saveInDoorPhotos(SendingCompleteActivity.this, "");
            ConstMethods.saveOutDoorPhotos(SendingCompleteActivity.this, "");
            startActivity(new Intent(SendingCompleteActivity.this, OfflineSurvayActivity.class));
            finish();
        }

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
                            startActivity(new Intent(SendingCompleteActivity.this, RegistedList.class));
                            finish();
                        }
                    })
                    .setNegativeButton("الغاء", null)
                    .show();


        } else if (id == R.id.nav_add_new) {
            if (ConnectivityHelper.isConnectedToNetwork(SendingCompleteActivity.this)) {
                startActivity(new Intent(SendingCompleteActivity.this, SurvayScreen.class));
                finish();
            } else {
                startActivity(new Intent(SendingCompleteActivity.this, OfflineSurvayActivity.class));
                finish();
            }

        } else if (id == R.id.nav_change_project) {
            startActivity(new Intent(SendingCompleteActivity.this, ProjectsActivity.class));
            finish();
        }else if(id ==R.id.nav_comment){
            startActivity(new Intent(SendingCompleteActivity.this, CommentsActivity.class));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(SendingCompleteActivity.this, LoginActivity.class));
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

    public void closeScreen(View view) {
        if (ConnectivityHelper.isConnectedToNetwork(SendingCompleteActivity.this)) {
            startActivity(new Intent(SendingCompleteActivity.this, SurvayScreen.class));
            finish();
        } else {
            startActivity(new Intent(SendingCompleteActivity.this, OfflineSurvayActivity.class));
            finish();
        }
    }
}
