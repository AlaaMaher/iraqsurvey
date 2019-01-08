package com.example.asamir.iraqproject.comments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.asamir.iraqproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity implements CommentItemClick {
    private FirebaseDatabase firebaseDatabase;
    private List<Comment> comments;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private ArrayList<Comment> patients;
    private RecyclerView mRecyclerView;
    private CommentAdapter adapter;
    private ProgressBar mLoadingIndicator;
    private FirebaseAuth mFirebaseAuth;
    String officeid;

    ArrayList<String> govIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        govIds = new ArrayList<>();
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
        Bundle bundle = getIntent().getExtras();
        databaseReference = firebaseDatabase.getReference().child("OFFICE_DATA");
        attachDatabaseReadListener();
        comments = new ArrayList<>();

    }

    @Override
    public void EditBtn(View v, int position) {

    }


    private void attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        try {
                            Log.e("]]]", dataSnapshot1.getValue().toString());
                            if (Integer.valueOf(dataSnapshot1.child("has_comment").getValue().toString()) == 0) {


                                String notes = (String) dataSnapshot1.child("notes").getValue();
                                String userId = (String) dataSnapshot1.child("userID").getValue();
                                String projectName = (String) dataSnapshot1.child("projectName").getValue();
                                String office_name_or_id = (String) dataSnapshot1.child("office_name_or_id").getValue();

                                if (userId.equals(mFirebaseAuth.getUid())) {
                                    Comment comment = new Comment(office_name_or_id, projectName, notes);
                                    comments.add(comment);
                                    Toast.makeText(CommentsActivity.this, comment.getComment(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        } catch (Exception g) {
                        }

                    }

                    adapter.setCommentData(comments , CommentsActivity.this);
                    Toast.makeText(CommentsActivity.this, comments.size()+"", Toast.LENGTH_SHORT).show();
                    mRecyclerView.setAdapter(adapter);

                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(final DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            databaseReference.addChildEventListener(childEventListener);
        }
    }
}
