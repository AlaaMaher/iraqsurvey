package com.example.asamir.iraqproject.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.asamir.iraqproject.LoginActivity;
import com.example.asamir.iraqproject.util.Sesstion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyService extends Service {
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    public final static int REQUEST_LOCATION = 199;
    private static final String TAG = "BOOMBOOMTESTGPS";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        firebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
       // Toast.makeText(this, Sesstion.getInstance(this).getUser().getUserID(), Toast.LENGTH_SHORT).show();
        databaseReference = firebaseDatabase.getReference().child("Project_user").child(mFirebaseAuth.getUid());
        CheckUser();
          attachDatabaseReadListener();
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");

    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartService = new Intent(getApplicationContext(), this.getClass());
        restartService.setPackage(getPackageName());
        startService(restartService);
        super.onTaskRemoved(rootIntent);
    }

    public void CheckUser() {
        mFirebaseAuth.signInWithEmailAndPassword(Sesstion.getInstance(this).getUser().getPassowrd(),
                Sesstion.getInstance(this).getUser().getUserID()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Log.e("Pass_____----->", Sesstion.getInstance(MyService.this).getUser().getPassowrd());
                    Log.e("Mail_____----->", Sesstion.getInstance(MyService.this).getUser().getUserName());
                    Log.e("ID_____----->", Sesstion.getInstance(MyService.this).getUser().getUserID());

                    DatabaseReference userProjectsRef = firebaseDatabase.getReference("User").child(mFirebaseAuth.getUid());
                    userProjectsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("is_active").getValue().toString().equals("1")) {

                            } else {
                                logOut();
                                Toast.makeText(MyService.this, "هذا الحساب غير مفعل !", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

//                    DatabaseReference userProjectsRef2 = firebaseDatabase.getReference("Project_user").child(mFirebaseAuth.getUid());
//                    userProjectsRef2.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            int i = 0;
//                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                                Log.e("AAAAA>>", postSnapshot.getKey());
//                                i++;
//                            }
//                            Log.e("Zize Of Snapshot", String.valueOf(i));
//
//                            final int finalI = i;
//                            new android.os.Handler().postDelayed(
//                                    new Runnable() {
//                                        public void run() {
//                                            if (finalI == 0) {
//                                                logOut();
//                                                Toast.makeText(MyService.this, "لا يوجد اي مشاريع لديك !", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    }, 10000);
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });

                } else {
                    logOut();
                    Toast.makeText(getApplicationContext(), "تم تسجيل الخروج ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        Sesstion.getInstance(MyService.this).logout();
        startActivity(new Intent(MyService.this, LoginActivity.class));

    }

    private void attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    //    Toast.makeText(MyService.this, "onChildChanged", Toast.LENGTH_SHORT).show();

                }

                public void onChildRemoved(final DataSnapshot dataSnapshot) {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    int i = 0;
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        // Log.e("AAAAA" , postSnapshot.getKey() ) ;
                                        i++;
                                    }
                                   // Toast.makeText(MyService.this, "" + i, Toast.LENGTH_SHORT).show();

                                    if (i == 0) {
                                        logOut();
                                        Toast.makeText(MyService.this, "لا يوجد اي مشاريع لديك !", Toast.LENGTH_SHORT).show();
                                      //  Toast.makeText(MyService.this, "empty", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, 10000);


                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            databaseReference.addChildEventListener(childEventListener);
        }
    }


}
