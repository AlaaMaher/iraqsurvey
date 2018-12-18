package com.example.asamir.iraqproject.ViewFormData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asamir.iraqproject.AddFormData.SurvayScreen;
import com.example.asamir.iraqproject.ConstMethods;
import com.example.asamir.iraqproject.LoginActivity;
import com.example.asamir.iraqproject.Models.DataCollectionModel;
import com.example.asamir.iraqproject.Models.PhotoModel;
import com.example.asamir.iraqproject.Models.SketchModel;
import com.example.asamir.iraqproject.OfflineWork.Database;
import com.example.asamir.iraqproject.ProjectsActivity;
import com.example.asamir.iraqproject.R;
import com.example.asamir.iraqproject.RegistedList;
import com.example.asamir.iraqproject.adapter.RecyclerViewAdapter;
import com.example.asamir.iraqproject.util.Utility;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewInDoorPhotosActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemListener, NavigationView.OnNavigationItemSelectedListener {
    RecyclerViewAdapter adapter;
    List<PhotoModel> photoList = new ArrayList<>();
    Uri picUri;
    @BindView(R.id.tvTootBarTitle)
    TextView tvTootBarTitle;
    @BindView(R.id.logoXmarks)
    ImageView logoXmarks;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private View view;

    @BindView(R.id.recyclerViewPhoto)
    RecyclerView recyclerView;
    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    DatabaseReference databaseReference;
    DatabaseReference ImagedatabaseReference;
    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";
    // Root Database Name for Firebase Database.
    String Database_Path = "All_Image_Uploads_Database";
    private String imageUrl;
    private String ImageUploadId;
    private String currentDateandTime;

    ArrayList<SketchModel> outDoorList = new ArrayList<>();
    private DataCollectionModel dataCollectionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_in_door_photos);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTootBarTitle.setText("عرض المبني من الداخل");
        logoXmarks.setVisibility(View.GONE);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dataCollectionModel = getIntent().getExtras().getParcelable("data");
        try {
            JSONArray jsonArray = new JSONArray(dataCollectionModel.getInDoorPhotos());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.e("IMAGE URL--->", jsonObject.getString("imageUrl"));
                photoList.add(new PhotoModel(jsonObject.getString("imageUrl"), jsonObject.getString("id")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new RecyclerViewAdapter(ViewInDoorPhotosActivity.this, photoList, this, 1);
        recyclerView.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(ViewInDoorPhotosActivity.this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        databaseReference = FirebaseDatabase.getInstance().getReference("OFFICE_DATA");
        storageReference = FirebaseStorage.getInstance().getReference();
        // Assign FirebaseDatabase instance with root database name.
        ImagedatabaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
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
            startActivity(new Intent(ViewInDoorPhotosActivity.this, RegistedList.class));
            finish();
        } else if (id == R.id.nav_add_new) {
            startActivity(new Intent(ViewInDoorPhotosActivity.this, SurvayScreen.class));
            finish();
        } else if (id == R.id.nav_change_project) {
            startActivity(new Intent(ViewInDoorPhotosActivity.this, ProjectsActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ViewInDoorPhotosActivity.this, LoginActivity.class));
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

    @Override
    public void onItemClick(PhotoModel item) {

    }


    public void saveChanges(View view) {


        try {
            JSONObject basicInfoObject = new JSONObject(ConstMethods.getSavedBasicInformationData(ViewInDoorPhotosActivity.this));
            String gov = basicInfoObject.getString("gov");
            String cityid = basicInfoObject.getString("city");
            String districtId = basicInfoObject.getString("district");
            String address = basicInfoObject.getString("address");
            String phone = basicInfoObject.getString("phone");
            String hasInternet = basicInfoObject.getString("hasInternet");
            String isNetwork = basicInfoObject.getString("isNetwork");
            String internetSeed = basicInfoObject.getString("internetSeed");
            String projectName = ConstMethods.getSavedprogectid(ViewInDoorPhotosActivity.this);
            String office_name_or_id = basicInfoObject.getString("office_name_or_id");
            //---
            String shiftType = basicInfoObject.getString("shiftType");
            String morning_shift_from = basicInfoObject.getString("morning_shift_from");
            String morning_shift_to = basicInfoObject.getString("morning_shift_to");
            String evening_shift_from = basicInfoObject.getString("evening_shift_from");
            String evening_shift_to = basicInfoObject.getString("evening_shift_to");

            String other_city = basicInfoObject.getString("other_city");
            String other_district = basicInfoObject.getString("other_district");
            //---
            if (other_city.trim().length() != 0) {
                cityid = "";

            }
            if (other_district.trim().length() != 0) {
                districtId = "";
            }
            String computer_count = basicInfoObject.getString("computer_count");
            String computer_notes = basicInfoObject.getString("computer_notes");
            String printers_count = basicInfoObject.getString("printers_count");
            String printers_notes = basicInfoObject.getString("printers_notes");
            String scanners_count = basicInfoObject.getString("scanners_count");
            String scanners_notes = basicInfoObject.getString("scanners_notes");
            String rooms_count = ConstMethods.getRooms(ViewInDoorPhotosActivity.this);
            String visitDate = basicInfoObject.getString("visitDate");
            String notes = basicInfoObject.getString("notes");
            String lat = basicInfoObject.getString("lat");
            String lng = basicInfoObject.getString("lng");
            String projectObjectId = basicInfoObject.getString("projectObjectId");
            // add sketch data
            String sketchData = ConstMethods.getSketch(ViewInDoorPhotosActivity.this);
            String outDoorPhotos = ConstMethods.getOutDoorPhotos(ViewInDoorPhotosActivity.this);
            Gson gson = new Gson();
            String inDoorPhotos = gson.toJson(photoList);

            //--
            String posisionData = ConstMethods.getPositions(ViewInDoorPhotosActivity.this);
            // String id = databaseReference.push().getKey();

            databaseReference.child(office_name_or_id).child(projectObjectId).setValue(new DataCollectionModel(
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
                    other_district

            ));

            startActivity(new Intent(ViewInDoorPhotosActivity.this, ProjectsActivity.class));
            finish();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void uploadImage(Uri tempUri) {
        if (tempUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("برجاء الانتظار جاري رفع الصورة ... ");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final StorageReference ref = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(tempUri));
            UploadTask uploadTask = ref.putFile(tempUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        progressDialog.dismiss();
                        // Continue with the task to get the download URL
                        imageUrl = String.valueOf(downloadUri);
                        ImageUploadId = databaseReference.push().getKey();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");
                        currentDateandTime = sdf.format(new Date());
                        SketchModel imageUploadInfo = new SketchModel(ImageUploadId, currentDateandTime, currentDateandTime, imageUrl);
                        Log.e("Image Url--->", imageUrl);
                        // Adding image upload id s child element into databaseReference.
                        ImagedatabaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        Toast.makeText(ViewInDoorPhotosActivity.this, "تم رفع الصوره بنجاح اضغط التالي لاستكمال المسح الميداني او اضف صور اخري  ", Toast.LENGTH_SHORT).show();
                        outDoorList.add(imageUploadInfo);
                        Gson gson = new Gson();
                        String roomsArray = gson.toJson(outDoorList);
                        ConstMethods.saveOutDoorPhotos(ViewInDoorPhotosActivity.this, roomsArray);


                    } else {
                        Toast.makeText(ViewInDoorPhotosActivity.this, "حدث خطأ برجاء المحاولة مرة أخري ", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Getting image upload ID.

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    // Toast.makeText(UploadWallpaper.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = ViewInDoorPhotosActivity.this.getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("التقاط صورة"))
                        cameraIntent();
                    else if (userChoosenTask.equals("اختيار من الاستديو"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"التقاط صورة", "اختيار من الاستديو",
                "إلغاء"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("إضافة صورة للمبني من الداخل");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ViewInDoorPhotosActivity.this);

                if (items[item].equals("التقاط صورة")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("اختيار من الاستديو")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("إلغاء")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                picUri = onSelectFromGalleryResult(data);
                photoList.add(new PhotoModel(picUri));
                adapter = new RecyclerViewAdapter(ViewInDoorPhotosActivity.this, photoList, this, 0);
                recyclerView.setAdapter(adapter);
                GridLayoutManager manager = new GridLayoutManager(ViewInDoorPhotosActivity.this, 2, GridLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(manager);
                uploadImage(picUri);
            } else if (requestCode == REQUEST_CAMERA) {
                picUri = onCaptureImageResult(data);
                photoList.add(new PhotoModel(picUri));
                adapter = new RecyclerViewAdapter(ViewInDoorPhotosActivity.this, photoList, this, 0);
                recyclerView.setAdapter(adapter);
                GridLayoutManager manager = new GridLayoutManager(ViewInDoorPhotosActivity.this, 2, GridLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(manager);
                uploadImage(picUri);
            }
        }

    }

    private Uri onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        picUri = getImageUri(ViewInDoorPhotosActivity.this, thumbnail);
        //ivImage.setImageBitmap(thumbnail);
        return picUri;
    }

    @SuppressWarnings("deprecation")
    private Uri onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(ViewInDoorPhotosActivity.this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        picUri = getImageUri(ViewInDoorPhotosActivity.this, bm);
        return picUri;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
                selectImage();
                return false;
            default:
                break;
        }

        return false;
    }

    public void closeScreen(View view) {
        finish();
    }
}
