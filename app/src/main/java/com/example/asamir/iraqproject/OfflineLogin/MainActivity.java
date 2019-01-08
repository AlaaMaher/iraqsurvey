package com.example.asamir.iraqproject.OfflineLogin;

import android.arch.lifecycle.Observer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asamir.iraqproject.R;


public class MainActivity extends AppCompatActivity {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    private UsersViewModel mUsersViewModel;
    private EditText email;
    private EditText password;
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);

        email = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        button = findViewById(R.id.button7);
        textView = findViewById(R.id.textView6);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        RecyclerView recyclerView = findViewById(R.id.recyclerview);
//        final WordListAdapter adapter = new WordListAdapter(this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get a new or existing ViewModel from the ViewModelProvider.

        //mUsersViewModel = ViewModelProviders.of(this).get(UsersViewModel.class);
        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.

        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUser(email.getText().toString(),
                        password.getText().toString());
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mUsersViewModel.getUser(email.getText().toString(),
                        password.getText().toString()).observe(MainActivity.this, new Observer<User>() {
                    @Override
                    public void onChanged(@Nullable User user) {
                        assert user != null;
                        textView.setText(String.format("%s => %s => %s", user.getEmail(), user.getPassword(), user.getId()));
                    }

                });
            }
        });


//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
//                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
//            }
//        });
    }

    public void insertUser(String email, String password)

    {

        User user = new User(email, password, "dddddddddddd");
        mUsersViewModel.insert(user);

    }
}
