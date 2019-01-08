package com.example.asamir.iraqproject.ViewFormData;

import android.app.Activity;
import android.app.Dialog;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asamir.iraqproject.AddFormData.IndoorPhotos;
import com.example.asamir.iraqproject.AddFormData.NewUiSurveyScreen;
import com.example.asamir.iraqproject.AddFormData.OutdoorPhotos;
import com.example.asamir.iraqproject.AddFormData.SketchPlace;
import com.example.asamir.iraqproject.AddFormData.SurvayScreen;
import com.example.asamir.iraqproject.ConstMethods;
import com.example.asamir.iraqproject.LoginActivity;
import com.example.asamir.iraqproject.Models.DataCollectionModel;
import com.example.asamir.iraqproject.Models.SketchModel;
import com.example.asamir.iraqproject.OfflineWork.Database;
import com.example.asamir.iraqproject.ProjectsActivity;
import com.example.asamir.iraqproject.R;
import com.example.asamir.iraqproject.RegistedList;
import com.example.asamir.iraqproject.comments.CommentsActivity;
import com.example.asamir.iraqproject.util.ConnectivityHelper;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewSketchImageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    DatabaseReference databaseReference;
    StorageReference sref;
    DatabaseReference dref;
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
    @BindView(R.id.btn_delete_photo)
    Button btnDeletePhoto;


    private String userChoosenTask;
    private Uri picUri;
    private String imageUrl;
    private String currentDateandTime;
    private String ImageUploadId;
    private DataCollectionModel dataCollectionModel;
    boolean click=false;

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
        btnDeletePhoto.setVisibility(View.GONE);




        dataCollectionModel = getIntent().getExtras().getParcelable("data");
        try {
            JSONObject jsonObject = new JSONObject(dataCollectionModel.getSketchData());
           Picasso.with(ViewSketchImageActivity.this).load(jsonObject.getString("sketchImageUrl")).resize(100,100).into(ivPicImage);
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

        ivPicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        storageReference = FirebaseStorage.getInstance().getReference();
        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        sref = FirebaseStorage.getInstance().getReference();


        dref = FirebaseDatabase.getInstance().getReference(Database_Path);



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
        else if(id ==R.id.nav_comment){
            startActivity(new Intent(ViewSketchImageActivity.this, CommentsActivity.class));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        ConstMethods.saveUserLoginInfo(null , null , ViewSketchImageActivity.this);

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

    private void selectImage() {
        final CharSequence[] items = {"التقاط صورة", "اختيار من الاستديو",
                "إلغاء"};

        final Dialog dialog  = new Dialog(ViewSketchImageActivity.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.sketckdialog);
        TextView  pic=dialog.findViewById(R.id.pic);
        ImageButton btnPic = dialog.findViewById(R.id.picImage);
        ImageButton btnChoose=dialog.findViewById(R.id.chooseImage);
        Window window = dialog.getWindow();
        window.setLayout(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        final boolean result = Utility.checkPermission(getApplicationContext());
        btnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result) {
                    cameraIntent();
                }
                dialog.cancel();


            }
        });
        dialog.show();


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result) {
                    galleryIntent();
                }
                dialog.cancel();

            }
        });
        dialog.show();



//
//        final CharSequence[] items = {"التقاط صورة", "اختيار من الاستديو",
//                "إلغاء"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
//        builder.setTitle("إضافة صورة رسم كروكي للمكان");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                boolean result = Utility.checkPermission(getApplicationContext());
//
//                if (items[item].equals("التقاط صورة")) {
//                    userChoosenTask = "Take Photo";
//                    if (result)
//                        cameraIntent();
//
//                } else if (items[item].equals("اختيار من الاستديو")) {
//                    userChoosenTask = "Choose from Library";
//                    if (result)
//                        galleryIntent();
//
//                } else if (items[item].equals("إلغاء")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
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


    private void uploadImage(Uri tempUri) {
        if (tempUri != null) {

            if (ConnectivityHelper.isConnectedToNetwork(ViewSketchImageActivity.this)) {
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
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                            Toast.makeText(getApplicationContext(), "تم رفع الصوره بنجاح اضغط التالي لاستكمال المسح الميداني ", Toast.LENGTH_SHORT).show();
//                            if (click){
//                                databaseReference.child(ImageUploadId).removeValue();
//
//                            }



                        } else {
                            Toast.makeText(getApplicationContext(), "حدث خطأ برجاء المحاولة مرة أخري ", Toast.LENGTH_SHORT).show();
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
            } else {

                imageUrl = tempUri.toString();
                ImageUploadId = "";

            }

        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getApplicationContext().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                picUri = onSelectFromGalleryResult(data);
                ivPicImage.setImageURI(picUri);
                btnDeletePhoto.setVisibility(View.VISIBLE);
                uploadImage(picUri);

            } else if (requestCode == REQUEST_CAMERA) {
                picUri = onCaptureImageResult(data);
                ivPicImage.setImageURI(picUri);
                btnDeletePhoto.setVisibility(View.VISIBLE);
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

        picUri = getImageUri(getApplicationContext(), thumbnail);
        //ivImage.setImageBitmap(thumbnail);
        return picUri;
    }

    @SuppressWarnings("deprecation")
    private Uri onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        picUri = getImageUri(getApplicationContext(), bm);
        return picUri;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void deleteImage(View view) {
        click=true;
        ConstMethods.saveSketch(ViewSketchImageActivity.this, "");
        btnDeletePhoto.setVisibility(View.GONE);

        ivPicImage.setImageDrawable(getResources().getDrawable(R.drawable.blueprint));

    }


    public void goTOBack(View view) {
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
