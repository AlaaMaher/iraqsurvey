package com.example.asamir.iraqproject.OfflineWork.BroadCastRecevier;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.asamir.iraqproject.AddFormData.NoticeActivity;
import com.example.asamir.iraqproject.ConstMethods;
import com.example.asamir.iraqproject.Models.DataCollectionModel;
import com.example.asamir.iraqproject.Models.SketchModel;
import com.example.asamir.iraqproject.OfflineWork.Database;
import com.example.asamir.iraqproject.OfflineWork.Entities.SurvayEntity;
import com.example.asamir.iraqproject.util.ConnectivityHelper;
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
import com.google.gson.JsonObject;

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
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

public class NetworkChangeReceiver extends BroadcastReceiver {
    StorageReference storageReference;
    DatabaseReference databaseReference;
    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";
    // Root Database Name for Firebase Database.
    String Database_Path = "All_Image_Uploads_Database";
    private String imageUrl;
    private String currentDateandTime;
    private String ImageUploadId;
    private DatabaseReference survayDatabaseReference;

    ArrayList<SketchModel> inDoorList=new ArrayList<>();
    ArrayList<SketchModel> outDoorList=new ArrayList<>();
    JSONObject sketchJsonObject;
    private String SketchImageURI;

    /**
     * TODO : NOTE
     *
     * IMAGE TYPE 1----> Sketch
     * IMAGE TYPE 2----> Indoor
     * IMAGE TYPE 3----> Outdoor
     * */

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (ConnectivityHelper.isConnectedToNetwork(context))
        {

            storageReference = FirebaseStorage.getInstance().getReference();
            // Assign FirebaseDatabase instance with root database name.
            databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
            survayDatabaseReference = FirebaseDatabase.getInstance().getReference("OFFICE_DATA");
            Database survayDB = Room.databaseBuilder(context,
                    Database.class, "survayTable").allowMainThreadQueries().build();


            Log.e("FROM BROAD SurvayData>",survayDB.userDao().getSurvayByIsUploaded("false").toString());
            for (int i=0;i<survayDB.userDao().getSurvayByIsUploaded("false").size();i++) {
                SurvayEntity survayEntity = survayDB.userDao().getSurvayByIsUploaded("false").get(i);

                try {
                    JSONArray jsonArrayIndoor, jsonArrayOutdoor;
                    if (!survayEntity.getInDoorPhotos().isEmpty()) {
                        jsonArrayIndoor = new JSONArray(survayEntity.getInDoorPhotos());
                            for (int index = 0; index < jsonArrayIndoor.length(); index++) {
                                    JSONObject jsonObjectIndoor = jsonArrayIndoor.getJSONObject(index);
                                    String inDoorImageUri = jsonObjectIndoor.getString("imageUrl");
                                    uploadImage(context, Uri.parse(inDoorImageUri), "2");
                                    Log.e("LAST IN INDooor--->", "No");

                            }

                    }
                    if (!survayEntity.getOutDoorPhotos().isEmpty()) {
                        jsonArrayOutdoor = new JSONArray(survayEntity.getOutDoorPhotos());
                            for (int index = 0; index < jsonArrayOutdoor.length(); index++) {

                                    JSONObject jsonObjectIndoor = jsonArrayOutdoor.getJSONObject(index);
                                    String inDoorImageUri = jsonObjectIndoor.getString("imageUrl");
                                    uploadImage(context, Uri.parse(inDoorImageUri), "3");
                                    Log.e("LAST IN INDooor--->", "No");
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (i == survayDB.userDao().getSurvayByIsUploaded("false").size()-1) {
                    /**
                     * send out door pic
                     * */
                    /**
                     * Send Data to Firebase
                     * */
                    Gson gson = new Gson();
                    String outDoorArray = gson.toJson(inDoorList);
                    String inDoorArray = gson.toJson(inDoorList);
                    uploadData(survayEntity.getGov(),
                            survayEntity.getCityId(),
                            survayEntity.getDistricId(),
                            survayEntity.getAddress(),
                            survayEntity.getPhone(),
                            survayEntity.getHasInternet(),
                            survayEntity.getIsNetwork(),
                            survayEntity.getInternetSeed(),
                            survayEntity.getProjectName(),
                            survayEntity.getShiftType(),
                            survayEntity.getMorning_shift_from(),
                            survayEntity.getMorning_shift_to(),
                            survayEntity.getEvening_shift_from(),
                            survayEntity.getEvening_shift_to(),
                            survayEntity.getComputer_count(),
                            survayEntity.getComputer_notes(),
                            survayEntity.getPrinters_count(),
                            survayEntity.getPrinters_notes(),
                            survayEntity.getScanners_count(),
                            survayEntity.getScanners_notes(),
                            survayEntity.getRooms_count(),
                            survayEntity.getNotes(),
                            survayEntity.getVisitDate(),
                            survayEntity.getLat(),
                            survayEntity.getLng(),
                            survayEntity.getSketchData(),
                            outDoorArray,
                            inDoorArray,
                            survayEntity.getPostionData(),
                            survayEntity.getOffice_name_or_id(),
                            survayEntity.getOtherCity(),
                            survayEntity.getOtherDistric()
                    );
                    survayDB.userDao().update("true", survayEntity.getId());
//                    ConstMethods.saveSketch(context, "");
//                    ConstMethods.saveInDoorPhotos(context, "");
//                    ConstMethods.saveOutDoorPhotos(context, "");
                }





            }
        }



    }


    public void uploadData(String gov, String cityId,
                           String districId, String address,
                           String phone, String hasInternet,
                           String isNetwork, String internetSeed, String projectName,
                           String shiftType, String morning_shift_from, String morning_shift_to,
                           String evening_shift_from, String evening_shift_to, String computer_count,
                           String computer_notes, String printers_count, String printers_notes, String scanners_count,
                           String scanners_notes, String rooms_count,
                           String notes, String visitDate, String lat, String lng, String sketchData,
                           String outDoorPhotos, String inDoorPhotos, String postionData, String office_name_or_id,  String otherCity,
                           String otherDistric )
    {


        // save Data online
        String id = survayDatabaseReference.push().getKey();
        survayDatabaseReference.child(office_name_or_id).child(id).setValue(new DataCollectionModel(
                gov,
                cityId,
                districId,
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
                postionData,
                office_name_or_id,
                otherCity,
                otherDistric
        ));




    }

    public void uploadImage(final Context context, Uri tempUri, final String imageType)
    {

        final StorageReference ref = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(tempUri,context));
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
                    //check image type
                    /**
                     * TODO : NOTE
                     *
                     * IMAGE TYPE 1----> Sketch
                     * IMAGE TYPE 2----> Indoor
                     * IMAGE TYPE 3----> Outdoor
                     * */

                    if (imageType.equals("1"))
                    {
                        Uri downloadUri = task.getResult();
                        imageUrl=String.valueOf(downloadUri);
                        // Continue with the task to get the download URL
                        ImageUploadId = databaseReference.push().getKey();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");
                        currentDateandTime = sdf.format(new Date());
                        SketchModel imageUploadInfo = new SketchModel(ImageUploadId,currentDateandTime,currentDateandTime,imageUrl);
                        Log.e("Image Url--->",imageUrl);
                        // Adding image upload id s child element into databaseReference.
                        databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        Map<String, String> sketchMap = new HashMap<>();
                        sketchMap.put("sketchImageId",ImageUploadId);
                        sketchMap.put("sketchImageUrl",imageUrl);
                        sketchJsonObject=new JSONObject(sketchMap);
                    }else if (imageType.equals("2"))
                    {
                        Uri downloadUri = task.getResult();
                        imageUrl=String.valueOf(downloadUri);
                        // Continue with the task to get the download URL
                        ImageUploadId = databaseReference.push().getKey();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");
                        currentDateandTime = sdf.format(new Date());
                        SketchModel imageUploadInfo = new SketchModel(ImageUploadId,currentDateandTime,currentDateandTime,imageUrl);
                        inDoorList.add(imageUploadInfo);
                        Log.e("Image Url--->",imageUrl);
                        // Adding image upload id s child element into databaseReference.
                        databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        inDoorList.add(imageUploadInfo);



                    }else if (imageType.equals("3")){
                        Uri downloadUri = task.getResult();
                        imageUrl=String.valueOf(downloadUri);
                        // Continue with the task to get the download URL
                        ImageUploadId = databaseReference.push().getKey();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");
                        currentDateandTime = sdf.format(new Date());
                        SketchModel imageUploadInfo = new SketchModel(ImageUploadId,currentDateandTime,currentDateandTime,imageUrl);
                        outDoorList.add(imageUploadInfo);
                        Log.e("Image Url--->",imageUrl);
                        // Adding image upload id s child element into databaseReference.
                        databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        outDoorList.add(imageUploadInfo);


                    }




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

                // Toast.makeText(UploadWallpaper.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri,Context context) {

        ContentResolver contentResolver = context.getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    private Uri onCaptureImageResult(Intent data,Context context,Uri picUri) {
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

        picUri =getImageUri(context,thumbnail);
        //ivImage.setImageBitmap(thumbnail);
        return picUri;
    }

    @SuppressWarnings("deprecation")
    private Uri onSelectFromGalleryResult(Intent data,Context context,Uri picUri) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        picUri=getImageUri(context,bm);
        return picUri;
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}