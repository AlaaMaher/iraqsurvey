package com.example.asamir.iraqproject.ViewFormData;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asamir.iraqproject.AddFormData.SurvayScreen;
import com.example.asamir.iraqproject.LoginActivity;
import com.example.asamir.iraqproject.Models.DataCollectionModel;
import com.example.asamir.iraqproject.OfflineWork.Database;
import com.example.asamir.iraqproject.ProjectsActivity;
import com.example.asamir.iraqproject.R;
import com.example.asamir.iraqproject.RegistedList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewSketchImageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    DatabaseReference databaseReference;
    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";
    // Root Database Name for Firebase Database.
    String Database_Path = "All_Image_Uploads_Database";
    @BindView(R.id.tvTootBarTitle)
    TextView tvTootBarTitle;
    private View view;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    @BindView(R.id.ivPicImage)
    ImageView ivPicImage;
    @BindView(R.id.btn_next)
    Button btn_next;


    private String userChoosenTask;
    private Uri picUri;
    private String imageUrl;
    private String currentDateandTime;
    private String ImageUploadId;
    private DataCollectionModel dataCollectionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sketch_image);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTootBarTitle.setText("رسم كروكي للمكان");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        dataCollectionModel = getIntent().getExtras().getParcelable("data");
        try {
            JSONObject jsonObject = new JSONObject(dataCollectionModel.getSketchData());
            Glide.with(ViewSketchImageActivity.this).load(jsonObject.getString("sketchImageUrl")).into(ivPicImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), ViewoOutdoorPhotosActivity.class);
                intent.putExtra("data", dataCollectionModel);
                startActivity(intent);

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
                    .show();        } else if (id == R.id.nav_list) {
            startActivity(new Intent(ViewSketchImageActivity.this, RegistedList.class));
            finish();
        } else if (id == R.id.nav_add_new) {
            startActivity(new Intent(ViewSketchImageActivity.this, SurvayScreen.class));
            finish();
        } else if (id == R.id.nav_change_project) {
            startActivity(new Intent(ViewSketchImageActivity.this, ProjectsActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ViewSketchImageActivity.this, LoginActivity.class));
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
        finish();
    }
}
