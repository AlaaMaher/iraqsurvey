package com.example.asamir.iraqproject;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.asamir.iraqproject.OfflineWork.Database;
import com.example.asamir.iraqproject.OfflineWork.Entities.CityEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.DistricEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.GovEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.OfficeEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.UserProjectsEntity;
import com.example.asamir.iraqproject.OfflineWork.OfflineAdapters.GovofflineSpinnerAdapter;
import com.example.asamir.iraqproject.util.ConnectivityHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LoginActivity extends AppCompatActivity {
    private static Database govDataBase, citiesDataBase,districDataBase,officeDataBase,userProjectsDB;
    @BindView(R.id.spinnerGov)
    Spinner spinnerGov;
    private FirebaseAuth mAuth;
    EditText edit_user_name, edtPass;
    private static final int PERMISSION_REQUEST_CODE = 200;
    static ArrayList<GovEntity> govEntities = new ArrayList<>();
    static FirebaseDatabase firebaseDatabase;
    private static String govId;
    List<GovEntity> govList = new ArrayList<>();
    GovofflineSpinnerAdapter govofflineSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        edit_user_name = findViewById(R.id.edit_user_name);
        edtPass = findViewById(R.id.edtPass);
        mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, ProjectsActivity.class));
            ConstMethods.saveOutDoorPhotos(LoginActivity.this,"");
            ConstMethods.saveInDoorPhotos(LoginActivity.this,"");
            ConstMethods.saveSketch(LoginActivity.this,"");
            finish();
        }
        if (!checkPermission()) {
            requestPermission();

        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        govDataBase = Room.databaseBuilder(getApplicationContext(),
                Database.class, "govTable").allowMainThreadQueries().build();
        citiesDataBase = Room.databaseBuilder(getApplicationContext(),
                Database.class, "cityTable").allowMainThreadQueries().build();

        districDataBase = Room.databaseBuilder(getApplicationContext(),
                Database.class, "districTable").allowMainThreadQueries().build();

        officeDataBase = Room.databaseBuilder(getApplicationContext(),
                Database.class, "officeTable").allowMainThreadQueries().build();

        userProjectsDB=Room.databaseBuilder(getApplicationContext(),
                Database.class, "userProjects").allowMainThreadQueries().build();



    }




    public static void getGoves() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Governorate");
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    govId = dataSnapshot1.getKey();
                    govDataBase.userDao().insertGov(new GovEntity(govId, dataSnapshot1.child("name").getValue().toString()));
                    saveCities(govId);

                }
                Log.e("Gov Database Created"," =====>  Done");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

    }


    public static void saveCities(String id) {

//        for (int i = 0; i < govDataBase.userDao().getGovs().size(); i++) {
//            latId=i;
//            final String id = govDataBase.userDao().getGovs().get(i).getGovId();
        DatabaseReference CitiesRef = firebaseDatabase.getReference("City").child(id);
        CitiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshotCity : dataSnapshot.getChildren()) {
                    String cityId = dataSnapshotCity.getKey();
                    citiesDataBase.userDao().insertCity(new CityEntity(cityId, dataSnapshotCity.child("name").getValue().toString(), dataSnapshot.getKey()));
                    saveDistrict(cityId);
                }



                Log.e("Cities DB Created "," =====>  Done");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        // }

    }

    public static void saveDistrict(String id)
    {
//        for (int i = 0; i < citiesDataBase.userDao().getAllCities().size(); i++) {
        //final String id = citiesDataBase.userDao().getAllCities().get(i).getCityId();

        Log.e("ID is of Ciy ->", id);
        DatabaseReference DistricRef = firebaseDatabase.getReference("District").child(id);
        DistricRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshotDistrict : dataSnapshot.getChildren()) {
                    String districId = dataSnapshotDistrict.getKey();
                    districDataBase.userDao().insertDistric(new DistricEntity( dataSnapshot.getKey(), dataSnapshotDistrict.child("name").getValue().toString(), districId));
                    saveOfficies(districId);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.e("District DB Created "," =====>  Done");
//        }
    }
    public static void saveOfficies(String id)
    {
//        for (int i = 0; i < districDataBase.userDao().getAllDistrict().size(); i++) {
//            final String id = districDataBase.userDao().getAllDistrict().get(i).getDistricId();
        Log.e("ID is of Ciy ->", id);
        DatabaseReference DistricRef = firebaseDatabase.getReference("Office").child(id);
        DistricRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot officeSnapShot : dataSnapshot.getChildren()) {
                    String districId = dataSnapshot.getKey();
                    officeDataBase.userDao().insertoffice(new OfficeEntity( officeSnapShot.getKey(),officeSnapShot.child("office_name").getValue().toString(),districId, officeSnapShot.child("project_id").getValue().toString()));
                }
                Log.e("Office DB Created "," =====>  Done");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //  }
    }


    public void userLogin(View view) {
        //startActivity(new Intent(LoginActivity.this,MainActivity.class));
        if (ConnectivityHelper.isConnectedToNetwork(LoginActivity.this))
        {
            CheckUser();
        }else {

            Toast.makeText(LoginActivity.this, "الرجاء التحقق من الاتصال بالانترنت ", Toast.LENGTH_LONG).show();
        }

    }

    public void CheckUser() {

        if (edit_user_name.getText().toString().isEmpty()&&edtPass.getText().toString().isEmpty() ) {
            Toast.makeText(LoginActivity.this, "الرجاء ادخال كلمة اسم المستخدم و كلمة المرور ", Toast.LENGTH_LONG).show();

        }
        else if (edit_user_name.getText().toString().isEmpty() ) {
            Toast.makeText(LoginActivity.this, "الرجاء ادخال اسم المستخدم ", Toast.LENGTH_LONG).show();
        }
        else if(edtPass.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "الرجاء ادخال كلمة المرور ", Toast.LENGTH_LONG).show();

        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("برجاء الانتظار جاري تسجيل الدخول ... ");
            progressDialog.setCancelable(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(edit_user_name.getText().toString(), edtPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        DatabaseReference userProjectsRef = firebaseDatabase.getReference("User").child(mAuth.getUid());
                        userProjectsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                if (dataSnapshot.child("is_active").getValue().toString().equals("1"))
                                {
                                    DatabaseReference userProjectsRef = firebaseDatabase.getReference("Project_user").child(FirebaseAuth.getInstance().getUid());
                                    userProjectsRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            progressDialog.cancel();
                                            for (DataSnapshot dataSnapshotProjects : dataSnapshot.getChildren()) {
                                                Log.e("PROJECTNAME-->",dataSnapshotProjects.child("project_name").getValue().toString());
                                                userProjectsDB.userDao().insertProjects(new UserProjectsEntity( dataSnapshotProjects.child("project_id").getValue().toString(),
                                                        dataSnapshotProjects.child("project_name").getValue().toString(), FirebaseAuth.getInstance().getUid()));
                                                Toast.makeText(LoginActivity.this,edit_user_name.getText().toString()+"مرحبا - ",Toast.LENGTH_LONG).show();

                                            }
                                            getGoves();
                                            Log.e("Projects DB Created "," =====>  Done");
                                            startActivity(new Intent(LoginActivity.this, ProjectsActivity.class));
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }else {
                                    progressDialog.cancel();
                                    Toast.makeText(LoginActivity.this, " هذا المستخدم غير مفعل  ", Toast.LENGTH_LONG).show();
                                    FirebaseAuth.getInstance().signOut();
                                }





                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                    } else {

                        Toast.makeText(LoginActivity.this, "خطا في البريد  وكلمة المرور ", Toast.LENGTH_LONG).show();
                    }
                }
            });


//            mAuth.signInWithEmailAndPassword(edit_user_name.getText().toString(), edtPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                        progressDialog.cancel();
//
//
//
//
//                    } else {
//                        progressDialog.cancel();
//                        Toast.makeText(LoginActivity.this, "خطا في البريد  وكلمة المرور ", Toast.LENGTH_LONG).show();
//                    }
//                }
//            });

        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED &&
                result3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("تم", okListener)
                .setNegativeButton("الغاء", null)
                .create()
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readStorageAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted && readStorageAccepted && writeStorageAccepted)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("التطبيق يحتاج بعض الصلاحيات",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    //}
                }


                break;
        }
    }

    public void deleteDBData(View view) {
        govDataBase.userDao().deleteGovData();
        citiesDataBase.userDao().deleteCityData();
        districDataBase.userDao().deleteDistrictsData();
    }

    public void createDbData(View view) {
        getGoves();
    }



}

