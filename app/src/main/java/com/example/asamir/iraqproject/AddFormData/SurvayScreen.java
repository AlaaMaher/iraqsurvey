package com.example.asamir.iraqproject.AddFormData;

import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.asamir.iraqproject.ConstMethods;
import com.example.asamir.iraqproject.LoginActivity;
import com.example.asamir.iraqproject.Models.CitiesModels;
import com.example.asamir.iraqproject.Models.DistrictsModels;
import com.example.asamir.iraqproject.Models.GovModels;
import com.example.asamir.iraqproject.OfflineWork.Database;
import com.example.asamir.iraqproject.ProjectsActivity;
import com.example.asamir.iraqproject.R;
import com.example.asamir.iraqproject.RegistedList;
import com.example.asamir.iraqproject.adapter.CitiesSpinnerAdapter;
import com.example.asamir.iraqproject.adapter.DistricSpinnerAdapter;
import com.example.asamir.iraqproject.adapter.GovSpinnerAdapter;
import com.example.asamir.iraqproject.util.ConnectivityHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurvayScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.spinnerGov)
    Spinner spinnerGov;
    @BindView(R.id.edt_other_cities)
    EditText edtOtherCities;
    @BindView(R.id.edt_other_district)
    EditText edtOtherDistrict;
    @BindView(R.id.spinnerCities)
    Spinner spinnerCities;
    @BindView(R.id.spinnerDistrict)

    Spinner spinnerDistrict;
    //    @BindView(R.id.spinnerProjects)
//    Spinner spinnerProjects;
    @BindView(R.id.edt_address)
    EditText edt_address;
    @BindView(R.id.edt_phone)
    EditText edt_phone;
    @BindView(R.id.radioIsNetWork)
    RadioGroup radioIsNetWork;
    @BindView(R.id.radioHasInternet)
    RadioGroup radioHasInternet;
    @BindView(R.id.edt_internetSeed)
    EditText edt_internetSeed;
    @BindView(R.id.spinnerOfficeName)
    Spinner spinnerOfficeName;
    @BindView(R.id.radioOwner)
    RadioButton radioOwner;
    @BindView(R.id.radioRent)
    RadioButton radioRent;
    @BindView(R.id.radioOwnerShipType)
    RadioGroup radioOwnerShipType;
    private String strGovId;
    private String strCityId;
    private String strDisrtric;
    private String strProjectName;
    private String strofficeid;
    private DatabaseReference databaseDisReference;
    private DatabaseReference databaseCityReference;
    /////////////////////////////////////////////////////////////
    @BindView(R.id.edt_morning_shift_from)
    EditText edt_morning_shift_from;
    @BindView(R.id.edt_morning_shift_to)
    EditText edt_morning_shift_to;
    @BindView(R.id.edt_evening_shift_from)
    EditText edt_evening_shift_from;
    @BindView(R.id.edt_evening_shift_to)
    EditText edt_evening_shift_to;
    @BindView(R.id.btn_morning)
    Button btnMorning;
    @BindView(R.id.btn_afternoon)
    Button btnAfternoon;
    @BindView(R.id.btn_both_time)
    Button btnBothTime;
    @BindView(R.id.text_input_layout)
    TextInputLayout error1;
    @BindView(R.id.text_input_layout2)
    TextInputLayout error2;
    //////////////////////////////////////////////////////
    @BindView(R.id.edt_computer_count)
    EditText edt_computer_count;
    @BindView(R.id.edt_computer_notes)
    EditText edt_computer_notes;
    @BindView(R.id.edt_printers_count)
    EditText edt_printers_count;
    @BindView(R.id.edt_printers_notes)
    EditText edt_printers_notes;
    @BindView(R.id.edt_scanners_count)
    EditText edt_scanners_count;
    @BindView(R.id.edt_scanners_notes)
    EditText edt_scanners_notes;
    Boolean isOnePressed = false, isSecondOne = false, isThirdOne = false;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
    Date inTime;
    Date outTime;
    String hasInternet="1", isNetwork="1";
    List<GovModels> govList = new ArrayList<>();
    List<CitiesModels> citiesList = new ArrayList<>();
    final List<DistrictsModels> districsList = new ArrayList<>();
    private List<DistrictsModels> officesList = new ArrayList<>();
    String strShiftType = "+";
    @BindView(R.id.tvTootBarTitle)
    TextView tvTootBarTitle;
    String strOwnerShipType="1";

    /**
     * NOTE :
     * Shift type ---> 1 Morning
     * Shift type ---> 2 Evening
     * Shift type ---> 3 Both
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survay_screen);

        ButterKnife.bind(this);
        tvTootBarTitle.setText("المسح الميداني");
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
        edt_morning_shift_from.setVisibility(View.GONE);
        edt_morning_shift_to.setVisibility(View.GONE);
        edt_evening_shift_from.setVisibility(View.GONE);
        edt_evening_shift_to.setVisibility(View.GONE);

        iniRadio();
        iniGovSpinner();

        databaseDisReference = FirebaseDatabase.getInstance().getReference("District");
        ////////////////////////////////////////////////////////////////////////////////////////
        btnMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // edt_morning_shift_to.setVisibility(View.VISIBLE);
                edt_morning_shift_from.setVisibility(View.VISIBLE);
                edt_morning_shift_to.setVisibility(View.VISIBLE);

                edt_evening_shift_from.setVisibility(View.GONE);
                edt_evening_shift_to.setVisibility(View.GONE);
                error2.setVisibility(View.GONE);
                error1.setVisibility(View.VISIBLE);
                isOnePressed = true;
                btnMorning.setBackgroundColor(Color.GRAY);
                strShiftType = "1";
                if (isSecondOne) {
                    btnAfternoon.setBackgroundColor(SurvayScreen.this.getResources().getColor(R.color.colorPrimary));
                    isSecondOne = false;
                } else if (isThirdOne) {
                    btnBothTime.setBackgroundColor(SurvayScreen.this.getResources().getColor(R.color.colorPrimary));
                    isThirdOne = false;
                }

            }
        });

        btnAfternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // edt_morning_shift_to.setVisibility(View.GONE);
                edt_morning_shift_from.setVisibility(View.GONE);
                edt_evening_shift_from.setVisibility(View.VISIBLE);
                edt_evening_shift_to.setVisibility(View.VISIBLE);
                error1.setVisibility(View.GONE);
                error2.setVisibility(View.VISIBLE);
                isSecondOne = true;
                btnAfternoon.setBackgroundColor(Color.GRAY);
                strShiftType = "2";
                if (isOnePressed) {
                    btnMorning.setBackgroundColor(SurvayScreen.this.getResources().getColor(R.color.colorPrimary));
                    isOnePressed = false;

                } else if (isThirdOne) {
                    btnBothTime.setBackgroundColor(SurvayScreen.this.getResources().getColor(R.color.colorPrimary));
                    isThirdOne = false;
                }


            }
        });
        btnBothTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_morning_shift_to.setVisibility(View.VISIBLE);
                edt_morning_shift_from.setVisibility(View.VISIBLE);
                edt_evening_shift_from.setVisibility(View.VISIBLE);
                edt_evening_shift_to.setVisibility(View.VISIBLE);
                error1.setVisibility(View.VISIBLE);
                error2.setVisibility(View.VISIBLE);
                btnBothTime.setBackgroundColor(Color.GRAY);
                strShiftType = "3";
                isThirdOne = true;
                if (isSecondOne) {
                    btnAfternoon.setBackgroundColor(SurvayScreen.this.getResources().getColor(R.color.colorPrimary));
                    isSecondOne = false;

                } else if (isOnePressed) {
                    btnMorning.setBackgroundColor(SurvayScreen.this.getResources().getColor(R.color.colorPrimary));
                    isOnePressed = false;
                }


            }
        });

        edt_morning_shift_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(SurvayScreen.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = " " + " PM";
                        } else {
                            amPm = " " + "  AM";
                        }
                        edt_morning_shift_from.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                        try {
                            inTime = sdf.parse(edt_morning_shift_from.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();
            }
        });

        edt_morning_shift_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);


                timePickerDialog = new TimePickerDialog(SurvayScreen.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = " " + " PM";
                        } else {
                            amPm = " " + "  AM";
                        }
                        edt_morning_shift_to.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                        try {
                            outTime = sdf.parse(edt_morning_shift_to.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (!isTimeAfter(inTime, outTime)) {
                            error1.setError("قم بادخال وقت الانتهاء من العمل الصحيح للدوام الصباحى");

                        } else {
                            error1.setError(null);

                        }


                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();


            }
        });

        edt_evening_shift_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(SurvayScreen.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = " " + " PM";
                        } else {
                            amPm = " " + "  AM";
                        }
                        edt_evening_shift_from.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                        try {
                            inTime = sdf.parse(edt_evening_shift_from.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();


            }
        });
        edt_evening_shift_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(SurvayScreen.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = " " + " PM";
                        } else {
                            amPm = " " + " AM";
                        }
                        edt_evening_shift_to.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                        try {
                            outTime = sdf.parse(edt_evening_shift_to.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (!isTimeAfter(inTime, outTime)) {
                            error2.setError("قم بادخال وقت الانتهاء من العمل الصحيح للدوام المسائى");

                        } else {
                            error2.setError(null);

                        }


                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();


            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////////////////


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
                            startActivity(new Intent(SurvayScreen.this, RegistedList.class));
                            finish();
                        }
                    })
                    .setNegativeButton("الغاء", null)
                    .show();


        } else if (id == R.id.nav_add_new) {
            if (ConnectivityHelper.isConnectedToNetwork(SurvayScreen.this)) {
                startActivity(new Intent(SurvayScreen.this, SurvayScreen.class));
                finish();
            } else {
                startActivity(new Intent(SurvayScreen.this, OfflineSurvayActivity.class));
                finish();
            }

        } else if (id == R.id.nav_change_project) {
            startActivity(new Intent(SurvayScreen.this, ProjectsActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(SurvayScreen.this, LoginActivity.class));
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

    public void goTONext(View view) {


        /**
         *
         * NOTE :
         * Shift type ---> 1 Morning
         * Shift type ---> 2 Evening
         * Shift type ---> 3 Both
         * */


        if (strShiftType.equals("1")) {
            if (!validateEditText(idsMor)) {
                saveData();
                startActivity(new Intent(SurvayScreen.this, PositionTableScreen.class));

            } else {
                Toast.makeText(this, "برجاء أدخال الحقول الفارغة", Toast.LENGTH_SHORT).show();
            }
        } else if (strShiftType.equals("2")) {
            if (!validateEditText(idsEv)) {
                saveData();
                startActivity(new Intent(SurvayScreen.this, PositionTableScreen.class));

            } else {
                Toast.makeText(this, "برجاء أدخال الحقول الفارغة", Toast.LENGTH_SHORT).show();
            }
        } else if (strShiftType.equals("3")) {
            if (!validateEditText(ids)) {
                saveData();
                startActivity(new Intent(SurvayScreen.this, PositionTableScreen.class));

            } else {

            }
        } else if (strShiftType.equals("+")) {
            Toast.makeText(this, "برجاء أختيار  نوع الدوام", Toast.LENGTH_SHORT).show();
        }


    }

    ///////////////////////////////////
    public void saveData() {
        Map<String, String> basicInfoMap = new HashMap<>();
        basicInfoMap.put("gov", strGovId);
        basicInfoMap.put("city", strCityId);
        basicInfoMap.put("district", strDisrtric);
        basicInfoMap.put("address", edt_address.getText().toString());
        basicInfoMap.put("phone", edt_phone.getText().toString());
        basicInfoMap.put("hasInternet", hasInternet);
        basicInfoMap.put("isNetwork", isNetwork);
        basicInfoMap.put("internetSeed", edt_internetSeed.getText().toString());
        basicInfoMap.put("office_name_or_id", strofficeid);
        basicInfoMap.put("shiftType", strShiftType);
        basicInfoMap.put("OwnerShipType",strOwnerShipType);
        basicInfoMap.put("morning_shift_from", edt_morning_shift_from.getText().toString());
        basicInfoMap.put("morning_shift_to", edt_morning_shift_to.getText().toString());
        basicInfoMap.put("evening_shift_from", edt_evening_shift_from.getText().toString());
        basicInfoMap.put("evening_shift_to", edt_evening_shift_to.getText().toString());
        basicInfoMap.put("computer_count", edt_computer_count.getText().toString());
        basicInfoMap.put("computer_notes", edt_computer_notes.getText().toString());
        basicInfoMap.put("printers_count", edt_printers_count.getText().toString());
        basicInfoMap.put("printers_notes", edt_printers_notes.getText().toString());
        basicInfoMap.put("scanners_count", edt_scanners_count.getText().toString());
        basicInfoMap.put("scanners_notes", edt_scanners_notes.getText().toString());
        basicInfoMap.put("other_city", edtOtherCities.getText().toString());
        basicInfoMap.put("other_district", edtOtherDistrict.getText().toString());
        JSONObject jsonObject = new JSONObject(basicInfoMap);
        Log.e("STRing---JSON-BasicInfo", jsonObject.toString());
        ConstMethods.saveBasicInformationData(jsonObject.toString(), SurvayScreen.this);

    }

    //////////////////////////////////////////////////////////////////

    boolean isTimeAfter(Date startTime, Date endTime) {
        if (endTime.before(startTime)) {
            return false;
        } else {
            return true;
        }
    }

    //////////////////////////////////////////////////////////////
    int[] ids = new int[]
            {
                    R.id.edt_address, R.id.edt_phone, R.id.edt_internetSeed,
                    R.id.edt_computer_count, R.id.edt_computer_notes, R.id.edt_printers_count,
                    R.id.edt_printers_notes, R.id.edt_scanners_count, R.id.edt_scanners_notes,
                    R.id.edt_evening_shift_from, R.id.edt_evening_shift_to, R.id.edt_morning_shift_from,
                    R.id.edt_morning_shift_to


            };
    int[] idsEv = new int[]
            {
                    R.id.edt_address, R.id.edt_phone, R.id.edt_internetSeed,
                    R.id.edt_computer_count, R.id.edt_computer_notes, R.id.edt_printers_count,
                    R.id.edt_printers_notes, R.id.edt_scanners_count, R.id.edt_scanners_notes,
                    R.id.edt_evening_shift_from, R.id.edt_evening_shift_to


            };
    int[] idsMor = new int[]
            {
                    R.id.edt_address, R.id.edt_phone, R.id.edt_internetSeed,
                    R.id.edt_computer_count, R.id.edt_computer_notes, R.id.edt_printers_count,
                    R.id.edt_printers_notes, R.id.edt_scanners_count, R.id.edt_scanners_notes,
                    R.id.edt_morning_shift_from,
                    R.id.edt_morning_shift_to


            };

    public boolean validateEditText(int[] ids) {
        boolean isEmpty = false;

        for (int id : ids) {
            EditText et = (EditText) findViewById(id);

            if (TextUtils.isEmpty(et.getText().toString())) {
                et.setError("برجاء ادخال الحقول المطلوبة");
                isEmpty = true;
            }
        }

        return isEmpty;
    }

    public void iniRadio() {
        radioHasInternet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedRadioButtonID = radioHasInternet.getCheckedRadioButtonId();
                if (selectedRadioButtonID == R.id.hasinternetNo) {
                    hasInternet = "0";
                    edt_internetSeed.setVisibility(View.GONE);

                } else {
                    hasInternet = "1ا";
                    edt_internetSeed.setVisibility(View.VISIBLE);
                    edt_internetSeed.setText("-");


                }
            }
        });
        radioIsNetWork.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedRadioButtonID = radioIsNetWork.getCheckedRadioButtonId();
                if (selectedRadioButtonID == R.id.isNetworkNo) {
                    isNetwork = "0";
                } else {
                    isNetwork = "1";
                }
            }
        });

        radioOwnerShipType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedRadioButtonID = radioOwnerShipType.getCheckedRadioButtonId();
                if (selectedRadioButtonID == R.id.radioOwner) {
                    strOwnerShipType = "1";
                } else {
                    strOwnerShipType = "2";
                }
            }
        });

    }

    public void iniGovSpinner() {
        // Spinner click listener

        spinnerGov.setPrompt("أختار المحافظة");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Governorate");
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                govList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.e("NAME AND VALUE -->", dataSnapshot1.child("name").getValue() + "\n" + dataSnapshot1.getKey());
                    govList.add(new GovModels(dataSnapshot1.getKey(), dataSnapshot1.child("name").getValue().toString()));
                }

                GovSpinnerAdapter govSpinnerAdapter = new GovSpinnerAdapter(SurvayScreen.this, R.layout.spinneritem, govList);
                spinnerGov.setAdapter(govSpinnerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        spinnerGov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strGovId = govList.get(position).getId();
                Log.e("KEY-->", strGovId);
                citiesList.clear();


                iniCitiesSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SurvayScreen.this, "Please Choose Your Gov", Toast.LENGTH_SHORT).show();


            }
        });


    }


    public void iniCitiesSpinner() {
        // Spinner click listener
        spinnerCities.setPrompt("أختار المدينة");
        spinnerCities.setOnItemSelectedListener(this);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("City").child(strGovId);
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                citiesList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    citiesList.add(new CitiesModels(dataSnapshot1.getKey(), dataSnapshot1.child("name").getValue().toString()));
                }
                CitiesSpinnerAdapter citiesSpinnerAdapter = new CitiesSpinnerAdapter(SurvayScreen.this, R.layout.spinneritem, citiesList);
                spinnerCities.setAdapter(citiesSpinnerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (citiesList.size() != 0) {

                    String city = citiesList.get(position).getName();
                    String other = new String("اخري");


                    if (city.equals(other)) {
                        edtOtherCities.setVisibility(View.VISIBLE);
                        edtOtherCities.requestFocus();
                        if (!edtOtherCities.hasFocus()) {

                            String cityId = databaseCityReference.push().getKey();
                            databaseDisReference.child(cityId).setValue(new DistrictsModels(cityId, edtOtherCities.getText().toString()));
                            strCityId = cityId;
                        }
                    } else {

                        strCityId = citiesList.get(position).getId();
                        districsList.clear();
                        iniDistrictsSpinner();
                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SurvayScreen.this, "Please Choose Your city", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void iniDistrictsSpinner() {

        // Spinner Drop down elements
        spinnerDistrict.setPrompt("أختار الحي");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("District").child(strCityId);
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                districsList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    districsList.add(new DistrictsModels(dataSnapshot1.getKey(), dataSnapshot1.child("name").getValue().toString()));
                }
                DistricSpinnerAdapter citiesSpinnerAdapter = new DistricSpinnerAdapter(SurvayScreen.this, R.layout.spinneritem, districsList);
                spinnerDistrict.setAdapter(citiesSpinnerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (districsList.size() != 0) {

                    String dist = districsList.get(position).getName();
                    String other = new String("اخرى");

                    if (dist.equals(other)) {

                        if (edtOtherDistrict.hasFocus()) {

                            String Disid = databaseDisReference.push().getKey();
                            databaseDisReference.child(Disid).setValue(new DistrictsModels(Disid, edtOtherDistrict.getText().toString()));
                            strDisrtric = Disid;

                        }

                    } else {
                        strDisrtric = districsList.get(position).getId();
                        iniOfficesSpinner();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SurvayScreen.this, "Please Choose Your District", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void iniOfficesSpinner() {

        // Spinner Drop down elements
        spinnerOfficeName.setPrompt("أختار المكتب");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Office").child(strDisrtric);
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                officesList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                    officesList.add(new DistrictsModels(dataSnapshot1.getKey(), dataSnapshot1.child("office_name").getValue().toString()));
                }
                DistricSpinnerAdapter citiesSpinnerAdapter = new DistricSpinnerAdapter(SurvayScreen.this, R.layout.spinneritem, officesList);
                spinnerOfficeName.setAdapter(citiesSpinnerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        spinnerOfficeName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (officesList.size() != 0) {
                    strofficeid = officesList.get(position).getId();
                    Log.e("OFFICE ID -->", strofficeid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void closeScreen(View view) {
        finish();
    }
}
