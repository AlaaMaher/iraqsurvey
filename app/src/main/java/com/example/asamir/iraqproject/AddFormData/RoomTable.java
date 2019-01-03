package com.example.asamir.iraqproject.AddFormData;

import android.app.Dialog;
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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asamir.iraqproject.ConstMethods;
import com.example.asamir.iraqproject.LoginActivity;
import com.example.asamir.iraqproject.Models.RoomsModel;
import com.example.asamir.iraqproject.OfflineWork.Database;
import com.example.asamir.iraqproject.ProjectsActivity;
import com.example.asamir.iraqproject.R;
import com.example.asamir.iraqproject.RegistedList;
import com.example.asamir.iraqproject.adapter.RoomsTableAdapter;
import com.example.asamir.iraqproject.util.ConnectivityHelper;
import com.example.asamir.iraqproject.util.FixedGridLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomTable extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    int scrollX = 0;
    ArrayList<RoomsModel> roomList = new ArrayList<>();
    @BindView(R.id.rvRooms)
    RecyclerView rvRooms;


    @BindView(R.id.tvEmptyList)
    TextView tvEmptyList;

    @BindView(R.id.tvTootBarTitle)
            TextView tvToolBar;

    @BindView(R.id.logoXmarks)
    ImageView logo;

    RoomsTableAdapter roomsTableAdapter;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_table);

        ButterKnife.bind(this);

        tvToolBar.setText("الغرف");
        logo.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUpRecyclerView();
        rvRooms.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        myDialog = new Dialog(this);
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
                            startActivity(new Intent(RoomTable.this, RegistedList.class));
                            finish();
                        }
                    })
                    .setNegativeButton("الغاء", null)
                    .show();
        } else if (id == R.id.nav_add_new) {
            if (ConnectivityHelper.isConnectedToNetwork(RoomTable.this))
            {
                startActivity(new Intent(RoomTable.this, SurvayScreen.class));
                finish();
            }else {
                startActivity(new Intent(RoomTable.this, OfflineSurvayActivity.class));
                finish();
            }
        }else if (id == R.id.nav_change_project) {
            startActivity(new Intent(RoomTable.this, ProjectsActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(RoomTable.this, LoginActivity.class));Database govDataBase = Room.databaseBuilder(getApplicationContext(),
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

    public void goTONext(View view) {
//        new AlertDialog.Builder(this)
//                .setMessage("هل أنت متاكد من الخروج من الصفحة ؟ ")
//                .setCancelable(false)
//                .setPositiveButton("متابعة", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                    }
//                })
//                .setNegativeButton("الغاء", null)
//                .show();

        saveData();
        startActivity(new Intent(RoomTable.this,SketchPlace.class));
}
    public void saveData(){
        Gson gson = new Gson();
        String roomsArray = gson.toJson(roomList);
        ConstMethods.saveRooms(getApplicationContext(),roomsArray);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addroommenu, menu);
        return true;
    }

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



    private void setUpRecyclerView() {
        if (roomList.isEmpty())
        {
            rvRooms.setVisibility(View.GONE);
            tvEmptyList.setVisibility(View.VISIBLE);
        }else {
            rvRooms.setVisibility(View.VISIBLE);
            tvEmptyList.setVisibility(View.GONE);
        }
        roomsTableAdapter = new RoomsTableAdapter(RoomTable.this, roomList);
        FixedGridLayoutManager manager = new FixedGridLayoutManager();
        manager.setTotalColumnCount(1);
        rvRooms.setLayoutManager(manager);
        rvRooms.setAdapter(roomsTableAdapter);
        rvRooms.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
    }



    public void showAddDialog() {


        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        final View promptsView = li.inflate(R.layout.addroomdialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("إضافة",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText edt_roomName = promptsView.findViewById(R.id.edt_roomName);
                                final String roomName = edt_roomName.getText().toString();
                                EditText edt_rooms_count = promptsView.findViewById(R.id.edt_roomCount);
                                final String roomCount = edt_rooms_count.getText().toString();
                                EditText edt_roomFre = promptsView.findViewById(R.id.edt_roomFre);
                                final String roomFer = edt_roomFre.getText().toString();

                                if (roomName.trim().isEmpty())
                                {
                                    Toast.makeText(RoomTable.this, "برجاء ادخال اسم الغرفه ! ", Toast.LENGTH_LONG).show();

                                } else if (roomCount.trim().isEmpty()) {
                                    Toast.makeText(RoomTable.this, "برجاء ادخال عدد الغرف ! ", Toast.LENGTH_LONG).show();
                                }

//                                else if (roomFer.trim().isEmpty()) {
//                                    Toast.makeText(RoomTable.this,"برجاء ادخال الاثاث الموجود في الغرفه ! ",Toast.LENGTH_LONG).show();
//                                }
                                else {
                                    roomList.add(new RoomsModel(roomName, roomCount, roomFer));
                                    roomsTableAdapter.notifyData(roomList);
                                    if (roomList.isEmpty())
                                    {
                                        rvRooms.setVisibility(View.GONE);
                                        tvEmptyList.setVisibility(View.VISIBLE);
                                    }else {
                                        rvRooms.setVisibility(View.VISIBLE);
                                        tvEmptyList.setVisibility(View.GONE);
                                    }
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

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setMessage("سوف يتم فقد بيانات مسجلة بهذه الصفحة هل أنت متاكد من الخروج من الصفحة ؟ ")
                .setCancelable(false)
                .setPositiveButton("متابعة", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RoomTable.super.onBackPressed();
                    }
                })
                .setNegativeButton("الغاء", null)
                .show();
    }
}
