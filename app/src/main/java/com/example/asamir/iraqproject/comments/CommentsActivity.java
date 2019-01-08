package com.example.asamir.iraqproject.comments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.asamir.iraqproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity implements CommentItemClick {
    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private ArrayList<Comment> patients;
    private RecyclerView mRecyclerView;
    private CommentAdapter adapter;
    private ProgressBar mLoadingIndicator;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_patients);
        adapter = new CommentAdapter(this);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
        }
        mRecyclerView.setHasFixedSize(true);


        patients = new ArrayList<>();
        mFirebaseAuth = FirebaseAuth.getInstance();
   //     patients = (ArrayList<Comment>) response.body().getPatients();
        adapter.setCommentData(patients, CommentsActivity.this);
        mRecyclerView.setAdapter(adapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Project_user").child(mFirebaseAuth.getUid());

        attachDatabaseReadListener();
    }

    @Override
    public void EditBtn(View v, int position) {

    }


    private void attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {}
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(final DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            databaseReference.addChildEventListener(childEventListener);
        }
    }
}
