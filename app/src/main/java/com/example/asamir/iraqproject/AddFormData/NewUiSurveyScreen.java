package com.example.asamir.iraqproject.AddFormData;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.asamir.iraqproject.ConstMethods;
import com.example.asamir.iraqproject.LoginActivity;
import com.example.asamir.iraqproject.Models.CitiesModels;
import com.example.asamir.iraqproject.Models.DistrictsModels;
import com.example.asamir.iraqproject.Models.GovModels;
import com.example.asamir.iraqproject.Models.OfficeModel;
import com.example.asamir.iraqproject.OfflineWork.Database;
import com.example.asamir.iraqproject.ProjectsActivity;
import com.example.asamir.iraqproject.R;
import com.example.asamir.iraqproject.RegistedList;
import com.example.asamir.iraqproject.adapter.CitiesSpinnerAdapter;
import com.example.asamir.iraqproject.adapter.DistricSpinnerAdapter;
import com.example.asamir.iraqproject.adapter.GovSpinnerAdapter;
import com.example.asamir.iraqproject.adapter.OfficeAdapter;
import com.example.asamir.iraqproject.util.ConnectivityHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;

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

public class NewUiSurveyScreen extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.spinnerGov)
    Spinner spinnerGov;
    @BindView(R.id.spinnerCities)
    Spinner spinnerCities;
    @BindView(R.id.spinnerDistrict)
    Spinner spinnerDistrict;
    @BindView(R.id.spinnerOffices)
    Spinner spinnerOfficeName;
    @BindView(R.id.edit_address)
    EditText edt_address;
    @BindView(R.id.edit_mobile)
    EditText edt_phone;
    @BindView(R.id.text_owner)
    TextView radioOwner;
    @BindView(R.id.text_rental)
    TextView radioRent;

    @BindView(R.id.text_inside_net_yes)
    TextView radioIsNetWorkYes;
    @BindView(R.id.text_inside_net_no)
    TextView radioIsNetWorkNo;
    @BindView(R.id.text_internet_yes)
    TextView radioHasInternetYes;
    @BindView(R.id.text_internet_no)
    TextView radioHasInternetNo;
    @BindView(R.id.edit_internet_speed)
    EditText editInternetSpeed;
    @BindView(R.id.edit_computers_num)
    EditText edt_computer_count;
    @BindView(R.id.edit_computers_notes)
    EditText edt_computer_notes;
    @BindView(R.id.edit_printers_num)
    EditText edt_printers_count;
    @BindView(R.id.edit_printers_notes)
    EditText edt_printers_notes;
    @BindView(R.id.edit_scanners_num)
    EditText edt_scanners_count;
    @BindView(R.id.edit_scanners_notes)
    EditText edt_scanners_notes;

    @BindView(R.id.text_morning_shift)
    TextView morningShift;
    @BindView(R.id.text_night_shift)
    TextView nightShift;
    @BindView(R.id.text_both_shift)
    TextView bothShift;
    @BindView(R.id.linear_next_click)
    RelativeLayout nextClick;

    @BindView(R.id.imageView_add_new_city)
    ImageView addNewCity;



    @BindView(R.id.imageView_add_new_district)
    ImageView addNewDistrict;



    private String strGovId;
    private String strCityId;
    private String strDisrtric;
    private String strProjectName;
    private String strofficeid;
    private DatabaseReference databaseDisReference;
    private DatabaseReference databaseCityReference;
    /////////////////////////////////////////////////////////////

    String strOwnerShipType="1";


    //////////////////////////////////////////////////////

    Boolean isOnePressed = false, isSecondOne = false, isThirdOne = false;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    int currentHour1;
    int currentMinute1;
    String amPm;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
    Date inTime;
    Date outTime;
    String hasInternet="1", isNetwork="1";
    List<GovModels> govList = new ArrayList<>();
    List<CitiesModels> citiesList = new ArrayList<>();
    final List<DistrictsModels> districsList = new ArrayList<>();
    private List<OfficeModel> officesList = new ArrayList<>();
    String strShiftType = "+";

    String officeVisit;
    String visit;

    Boolean valMor=false;
    Boolean valEve=false;
    boolean clicked=false;
    Boolean impty=false;
    String govName,cityName,disName,officeName;



    Calendar cal;
    Calendar cal1=Calendar.getInstance();
    Calendar cal12=Calendar.getInstance();



    String otherCity , otherDistrict;
    String morningShiftTxtFrom , morningShiftTxtTo  , nightShiftRxtFrom ,nightShiftTextTo ;

    /**
     * NOTE :
     * Shift type ---> 1 Morning
     * Shift type ---> 2 Evening
     * Shift type ---> 3 Both
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ui_survey_screen);


        ButterKnife.bind(this);
       // tvTootBarTitle.setText("المسح الميداني");
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
      //  getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       /*
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

*/

        iniRadio();
        iniGovSpinner();

        databaseDisReference = FirebaseDatabase.getInstance().getReference("District");
        ////////////////////////////////////////////////////////////////////////////////////////
        morningShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                edt_morning_shift_from.setFocusable(true);
                // edt_morning_shift_to.setVisibility(View.VISIBLE);
                edt_morning_shift_from.setVisibility(View.VISIBLE);
                btnDeleteFromMor.setVisibility(View.VISIBLE);
                btnDeletetoMor.setVisibility(View.VISIBLE);
                btnDeleteFromEve.setVisibility(View.GONE);
                btnDeleteToEve.setVisibility(View.GONE);
                edt_morning_shift_to.setVisibility(View.VISIBLE);


                edt_evening_shift_from.setVisibility(View.GONE);
                edt_evening_shift_to.setVisibility(View.GONE);
                error2.setVisibility(View.GONE);
                error1.setVisibility(View.VISIBLE);
                  */

                isOnePressed = true;
                morningShift.setBackgroundResource(R.drawable.sun_active);
                nightShift.setBackgroundResource(R.drawable.moon_nun);
                bothShift.setBackgroundResource(R.drawable.sun_non);

                strShiftType = "1";


                // Single tap here.
                cal = Calendar.getInstance();
                currentHour = cal.get(Calendar.HOUR_OF_DAY);
                currentMinute = cal.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(NewUiSurveyScreen.this, new TimePickerDialog.OnTimeSetListener() {
                    //  @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = " " + " PM";
                        } else {
                            amPm = " " + " AM";
                        }
                        morningShiftTxtFrom = String.format("%02d:%02d", hourOfDay, minutes) + amPm;


                        try {
                            inTime = sdf.parse(morningShiftTxtFrom);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }



                    }
                }, currentHour, currentMinute, false);
                clicked=true;


                timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "التالي", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if (clicked) {

                            cal = Calendar.getInstance();
                            currentHour = cal.get(Calendar.HOUR_OF_DAY);
                            currentMinute = cal.get(Calendar.MINUTE);


                            timePickerDialog = new TimePickerDialog(NewUiSurveyScreen.this, new TimePickerDialog.OnTimeSetListener() {
                                //    @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                    if (hourOfDay >= 12) {
                                        amPm = " " + " PM";
                                    } else {
                                        amPm = " " + " AM";
                                    }
                                    morningShiftTxtTo = String.format("%02d:%02d", hourOfDay, minutes) + amPm;


                                    try {
                                        outTime = sdf.parse(morningShiftTxtTo);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                    if (outTime.getHours() == 0 && amPm.equals("  PM")) {
                                        //error1.setError(null);
                                        //error11.setError(null);

                                    } else {
                                        if (!isTimeAfter(inTime, outTime)) {

                                            Toast.makeText(NewUiSurveyScreen.this, "قم بادخال وقت الانتهاء من العمل الصحيح للدوام الصباحى", Toast.LENGTH_SHORT).show();
                                            valMor = true;

                                        } else {


                                            // Error Null
                                        }
                                    }


                                }
                            }, currentHour, currentMinute, false);
                            timePickerDialog.show();


                            timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "موافق", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "الغاء", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(NewUiSurveyScreen.this, "من فضلك ادخل بدايه العمل اولا", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


                timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "الغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });


                timePickerDialog.show();


            }


        });

        nightShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                edt_morning_shift_from.setFocusable(true);
                // edt_morning_shift_to.setVisibility(View.GONE);
                btnDeleteFromEve.setVisibility(View.VISIBLE);
                btnDeleteToEve.setVisibility(View.VISIBLE);
                btnDeleteFromMor.setVisibility(View.GONE);
                btnDeletetoMor.setVisibility(View.GONE);
                edt_morning_shift_from.setVisibility(View.GONE);
                edt_evening_shift_from.setVisibility(View.VISIBLE);
                edt_evening_shift_to.setVisibility(View.VISIBLE);
                error1.setVisibility(View.GONE);
                error2.setVisibility(View.VISIBLE);
                */
                isSecondOne = true;
                nightShift.setBackgroundResource(R.drawable.moon_nun);
                strShiftType = "2";

                morningShift.setBackgroundResource(R.drawable.sun_non);
                nightShift.setBackgroundResource(R.drawable.moon_active);
                bothShift.setBackgroundResource(R.drawable.sun_non);



                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(NewUiSurveyScreen.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = " " + " PM";
                        } else {
                            amPm = " " + "  AM";
                        }
                        nightShiftRxtFrom = String.format("%02d:%02d", hourOfDay, minutes) + amPm;
                        try {
                            inTime = sdf.parse(nightShiftRxtFrom);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }



                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();
                clicked=true;

                timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "التالي", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if(clicked) {
                            calendar = Calendar.getInstance();
                            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                            currentMinute = calendar.get(Calendar.MINUTE);
                            timePickerDialog = new TimePickerDialog(NewUiSurveyScreen.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                    if (hourOfDay >= 12) {
                                        amPm = " " + " PM";
                                    } else {
                                        amPm = " " + " AM";
                                    }
                                    nightShiftTextTo = String.format("%02d:%02d", hourOfDay, minutes) + amPm;
                                    try {
                                        outTime = sdf.parse(nightShiftTextTo);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }



                                    if (!isTimeAfter(inTime, outTime)) {
                                        Toast.makeText(NewUiSurveyScreen.this, "قم بادخال وقت الانتهاء من العمل الصحيح للدوام المسائى", Toast.LENGTH_SHORT).show();

                                        valEve = true;

                                    } else {

                                                // Error null
                                    }


                                }
                            }, currentHour, currentMinute, false);
                            timePickerDialog.show();
                        }else {
                            Toast.makeText(NewUiSurveyScreen.this, "من فضلك ادخل بدايه العمل اولا", Toast.LENGTH_SHORT).show();

                        }



                    }
                });


                timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "الغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                    }
                });


              /*
                if (isOnePressed) {
                    btnMorning.setBackgroundColor(SurvayScreen.this.getResources().getColor(R.color.colorPrimary));
                    isOnePressed = false;

                } else if (isThirdOne) {
                    btnBothTime.setBackgroundColor(SurvayScreen.this.getResources().getColor(R.color.colorPrimary));
                    isThirdOne = false;
                }
*/










            }
        });

        bothShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*
                edt_morning_shift_from.setFocusable(true);
                btnDeleteFromEve.setVisibility(View.VISIBLE);
                btnDeleteToEve.setVisibility(View.VISIBLE);
                btnDeleteFromMor.setVisibility(View.VISIBLE);
                btnDeletetoMor.setVisibility(View.VISIBLE);
                edt_morning_shift_to.setVisibility(View.VISIBLE);
                edt_morning_shift_from.setVisibility(View.VISIBLE);
                edt_morning_shift_from.setFocusable(false);
                edt_evening_shift_from.setVisibility(View.VISIBLE);
                edt_evening_shift_to.setVisibility(View.VISIBLE);
                error1.setVisibility(View.VISIBLE);
                error2.setVisibility(View.VISIBLE);
                */
                strShiftType = "3";
                isThirdOne = true;

                morningShift.setBackgroundResource(R.drawable.sun_non);
                nightShift.setBackgroundResource(R.drawable.moon_nun);
                bothShift.setBackgroundResource(R.drawable.sun_active);

                /*
                if (isSecondOne) {
                    btnAfternoon.setBackgroundColor(SurvayScreen.this.getResources().getColor(R.color.colorPrimary));
                    isSecondOne = false;

                } else if (isOnePressed) {
                    btnMorning.setBackgroundColor(SurvayScreen.this.getResources().getColor(R.color.colorPrimary));
                    isOnePressed = false;
                }
*/

            }
        });






        ///////////////////////////////////////////////////////////////////////////////////////////////////



        addNewCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(NewUiSurveyScreen.this);
                dialog.setContentView(R.layout.add_new_city);

                final EditText cityNameEdit = dialog.findViewById(R.id.edit_city_name_dialog);
                Button addNewCityBtnDialog = dialog.findViewById(R.id.button_add_city_dialog);
                Button cancelCityDialog = dialog.findViewById(R.id.button_cancel_city_dialog);



                addNewCityBtnDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (cityNameEdit.getText().toString().length() != 0)
                           otherCity = cityNameEdit.getText().toString();
                        else
                           cityNameEdit.setError("أدخل اسم مدينة");
                    }
                });


                cancelCityDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                Window window = dialog.getWindow();
                window.setLayout(500, DrawerLayout.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);

                dialog.show();
            }
        });




        addNewDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(NewUiSurveyScreen.this);
                dialog.setContentView(R.layout.add_new_district);

                final EditText districtNameEdit = dialog.findViewById(R.id.edit_district_name_dialog);
                Button addNewDistrictBtnDialog = dialog.findViewById(R.id.button_add_district_dialog);
                Button cancelDistrictDialog = dialog.findViewById(R.id.button_cancel_district_dialog);



                addNewDistrictBtnDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (districtNameEdit.getText().toString().length() != 0)
                            otherDistrict = districtNameEdit.getText().toString();
                        else
                            districtNameEdit.setError("أدخل اسم الحي");
                    }
                });


                cancelDistrictDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                Window window = dialog.getWindow();
                window.setLayout(500, DrawerLayout.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);

                dialog.show();
            }
        });

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
                            startActivity(new Intent(NewUiSurveyScreen.this, RegistedList.class));
                            finish();
                        }
                    })
                    .setNegativeButton("الغاء", null)
                    .show();


        } else if (id == R.id.nav_add_new) {
            if (ConnectivityHelper.isConnectedToNetwork(NewUiSurveyScreen.this)) {
                startActivity(new Intent(NewUiSurveyScreen.this, SurvayScreen.class));
                finish();
            } else {
                startActivity(new Intent(NewUiSurveyScreen.this, OfflineSurvayActivity.class));
                finish();
            }

        } else if (id == R.id.nav_change_project) {
            startActivity(new Intent(NewUiSurveyScreen.this, ProjectsActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(NewUiSurveyScreen.this, LoginActivity.class));
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

    public  boolean fun(String asd){
        try{
            double d = Double.parseDouble(asd);
            return false ;
        }catch(NumberFormatException ex){
            return true ;
        }

    }

    public void goTONext(View view) {


        /**
         *
         * NOTE :
         * Shift type ---> 1 Morning
         * Shift type ---> 2 Evening
         * Shift type ---> 3 Both
         * */


        saveData();
        startActivity(new Intent(NewUiSurveyScreen.this, PositionTableScreen.class));

           /*
        if (!TextUtils.isEmpty(edtOtherCities.getText().toString() )&& !TextUtils.isEmpty(edtOtherDistrict.getText().toString()) && !fun(edtOtherCities.getText().toString())&&!fun(edtOtherDistrict.getText().toString()))
        {



            if (strShiftType.equals("1")) {
                if (!validateEditText(idsMor) && error1.getError() == null && error11.getError() == null ) {
                    saveData();
                    startActivity(new Intent(NewUiSurveyScreen.this, PositionTableScreen.class));


                } else {
                    Toast.makeText(this, "برجاء أختيار  نوع الدوام", Toast.LENGTH_SHORT).show();
                }
            } else if (strShiftType.equals("2")) {
                if (!validateEditText(idsEv) && error2.getError() == null && error12.getError() == null ) {
                    saveData();
                    startActivity(new Intent(NewUiSurveyScreen.this, PositionTableScreen.class));
                } else {
                    Toast.makeText(this, "برجاء أختيار  نوع الدوام", Toast.LENGTH_SHORT).show();
                }

            } else if (strShiftType.equals("3")) {
                if (!validateEditText(ids) && error1.getError() == null && error2.getError() == null && error11.getError() == null && error12.getError() == null) {
                    saveData();
                    startActivity(new Intent(NewUiSurveyScreen.this, PositionTableScreen.class));

                } else {

                    Toast.makeText(this, "برجاء أختيار  نوع الدوام", Toast.LENGTH_SHORT).show();
                }
            } else if (strShiftType.equals("+")) {
                Toast.makeText(this, "برجاء أدخال الحقول الفارغة", Toast.LENGTH_SHORT).show();
            }
        }else {
            if (strCityId.equals("dummyid")&&strDisrtric.equals("dummyid")&&strofficeid.equals("dummyid"))
            {
                Toast.makeText(this, "برجاء أختيار قيمة من القائمة او ادخال المحافظة والمدينة بشكل صحيح", Toast.LENGTH_SHORT).show();

            }else {
                if (strShiftType.equals("1")) {
                    if (!validateEditText(idsMor) && error1.getError() == null && error11.getError() == null ) {
                        saveData();
                        startActivity(new Intent(NewUiSurveyScreen.this, PositionTableScreen.class));


                    } else {
                        Toast.makeText(this, "برجاء أختيار  نوع الدوام", Toast.LENGTH_SHORT).show();
                    }
                } else if (strShiftType.equals("2")) {
                    if (!validateEditText(idsEv) && error2.getError() == null && error12.getError() == null ) {
                        saveData();
                        startActivity(new Intent(NewUiSurveyScreen.this, PositionTableScreen.class));
                    } else {
                        Toast.makeText(this, "برجاء أختيار  نوع الدوام", Toast.LENGTH_SHORT).show();
                    }

                } else if (strShiftType.equals("3")) {
                    if (!validateEditText(ids) && error1.getError() == null && error2.getError() == null && error11.getError() == null && error12.getError() == null) {
                        saveData();
                        startActivity(new Intent(NewUiSurveyScreen.this, PositionTableScreen.class));

                    } else {

                        Toast.makeText(this, "برجاء أختيار  نوع الدو" +
                                "ام", Toast.LENGTH_SHORT).show();
                    }
                } else if (strShiftType.equals("+")) {
                    Toast.makeText(this, "برجاء أدخال الحقول الفارغة", Toast.LENGTH_SHORT).show();
                }
            }

        }

 */

    }
    //


    ///////////////////////////////////
   /*
    public void deleteMorningFrom(View view){
        edt_morning_shift_from.setText("");
    }
    public void deleteMorningTo(View view){
        edt_morning_shift_to.setText("");
    }
    public void deleteEveningFrom(View view){
        edt_evening_shift_from.setText("");
    }
    public void deleteEveningTo(View view){
        edt_evening_shift_to.setText("");
    }
    */
    public void saveData() {
        Map<String, String> basicInfoMap = new HashMap<>();
        basicInfoMap.put("gov", strGovId);
        basicInfoMap.put("gov_name", govName);
        basicInfoMap.put("city", strCityId);
        basicInfoMap.put("city_name", cityName);
        basicInfoMap.put("district", strDisrtric);
        basicInfoMap.put("district_name",disName);
        basicInfoMap.put("address", edt_address.getText().toString());
        basicInfoMap.put("phone", edt_phone.getText().toString());
        basicInfoMap.put("hasInternet", hasInternet);
        basicInfoMap.put("isNetwork", isNetwork);
        basicInfoMap.put("internetSeed", editInternetSpeed.getText().toString());
        basicInfoMap.put("office_name_or_id", strofficeid);
        basicInfoMap.put("office_name", officeName);
        basicInfoMap.put("office_visit", officeVisit);
        basicInfoMap.put("shiftType", strShiftType);
        basicInfoMap.put("OwnerShipType",strOwnerShipType);
        basicInfoMap.put("morning_shift_from", "");
        basicInfoMap.put("morning_shift_to", "");
        basicInfoMap.put("evening_shift_from", "");
        basicInfoMap.put("evening_shift_to", "");
        basicInfoMap.put("computer_count", edt_computer_count.getText().toString());
        basicInfoMap.put("computer_notes", edt_computer_notes.getText().toString());
        basicInfoMap.put("printers_count", edt_printers_count.getText().toString());
        basicInfoMap.put("printers_notes", edt_printers_notes.getText().toString());
        basicInfoMap.put("scanners_count", edt_scanners_count.getText().toString());
        basicInfoMap.put("scanners_notes", edt_scanners_notes.getText().toString());
        basicInfoMap.put("other_city", "");
        basicInfoMap.put("other_district", "");
        JSONObject jsonObject = new JSONObject(basicInfoMap);
        Log.e("STRing---JSON-BasicInfo", jsonObject.toString());
        ConstMethods.saveBasicInformationData(jsonObject.toString(), NewUiSurveyScreen.this);

    }

    //////////////////////////////////////////////////////////////////

    boolean isTimeAfter(Date startTime, Date endTime) {

        if (startTime != null && endTime != null){
        if (endTime.before(startTime)) {
            return false;
        } else if (endTime.equals(startTime)) {
            return false;
        } else {
            return true;
        }
    }

    return true ;
    }






    //////////////////////////////////////////////////////////////

  /*
    int[] ids = new int[]
            {
                    R.id.edt_address, R.id.edt_phone, R.id.edt_internetSeed,
                    R.id.edt_computer_count, R.id.edt_printers_count, R.id.edt_scanners_count,
                    R.id.edt_evening_shift_from, R.id.edt_evening_shift_to, R.id.edt_morning_shift_from,
                    R.id.edt_morning_shift_to


            };
    int[] idsEv = new int[]
            {
                    R.id.edt_address, R.id.edt_phone, R.id.edt_internetSeed,
                    R.id.edt_computer_count, R.id.edt_printers_count, R.id.edt_scanners_count,
                    R.id.edt_evening_shift_from, R.id.edt_evening_shift_to


            };
    int[] idsMor = new int[]
            {
                    R.id.edt_address, R.id.edt_phone, R.id.edt_internetSeed,
                    R.id.edt_computer_count, R.id.edt_printers_count, R.id.edt_scanners_count,
                    R.id.edt_morning_shift_from,
                    R.id.edt_morning_shift_to


            };

    */
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

        radioHasInternetYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasInternet  = "1";

                radioHasInternetYes.setBackgroundResource(R.drawable.blue_toggle);
                radioHasInternetYes.setTextColor(ContextCompat.getColor(NewUiSurveyScreen.this, R.color.colorWhite));

                radioHasInternetNo.setBackgroundResource(R.drawable.white_toggle_left);
                radioHasInternetNo.setTextColor(ContextCompat.getColor(NewUiSurveyScreen.this, R.color.colorPrimaryText));
                editInternetSpeed.setVisibility(View.VISIBLE);

            }
        });
        radioHasInternetNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasInternet  = "0";
                editInternetSpeed.setVisibility(View.GONE);

                radioHasInternetYes.setBackgroundResource(R.drawable.white_toggle);
                radioHasInternetYes.setTextColor(ContextCompat.getColor(NewUiSurveyScreen.this, R.color.colorPrimaryText));

                radioHasInternetNo.setBackgroundResource(R.drawable.blue_toggle_left);
                radioHasInternetNo.setTextColor(ContextCompat.getColor(NewUiSurveyScreen.this, R.color.colorWhite));


            }
        });


        radioIsNetWorkYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNetwork="1";

                radioIsNetWorkYes.setBackgroundResource(R.drawable.blue_toggle);
                radioIsNetWorkYes.setTextColor(ContextCompat.getColor(NewUiSurveyScreen.this, R.color.colorWhite));

                radioIsNetWorkNo.setBackgroundResource(R.drawable.white_toggle_left);
                radioIsNetWorkNo.setTextColor(ContextCompat.getColor(NewUiSurveyScreen.this, R.color.colorPrimaryText));
            }
        });

        radioIsNetWorkNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNetwork="0";

                radioIsNetWorkYes.setBackgroundResource(R.drawable.white_toggle);
                radioIsNetWorkYes.setTextColor(ContextCompat.getColor(NewUiSurveyScreen.this, R.color.colorPrimaryText));

                radioIsNetWorkNo.setBackgroundResource(R.drawable.blue_toggle_left);
                radioIsNetWorkNo.setTextColor(ContextCompat.getColor(NewUiSurveyScreen.this, R.color.colorWhite));

            }
        });

        radioOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strOwnerShipType = "1";
                radioOwner.setBackgroundResource(R.drawable.blue_toggle);
                radioOwner.setTextColor(ContextCompat.getColor(NewUiSurveyScreen.this, R.color.colorWhite));
                radioRent.setTextColor(ContextCompat.getColor(NewUiSurveyScreen.this, R.color.colorPrimaryText));
                radioRent.setBackgroundResource(R.drawable.white_toggle_left);

            }
        });

        radioRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strOwnerShipType = "2";
                radioRent.setBackgroundResource(R.drawable.blue_toggle_left);
                radioOwner.setBackgroundResource(R.drawable.white_toggle);
                radioOwner.setTextColor(ContextCompat.getColor(NewUiSurveyScreen.this, R.color.colorPrimaryText));
                radioRent.setTextColor(ContextCompat.getColor(NewUiSurveyScreen.this, R.color.colorWhite));

            }
        });
        /*
        radioHasInternet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedRadioButtonID = radioHasInternet.getCheckedRadioButtonId();
                if (selectedRadioButtonID == R.id.hasinternetNo) {
                    hasInternet = "0";
                    edt_internetSeed.setVisibility(View.GONE);
                    edt_internetSeed.setText("-");

                } else {
                    hasInternet = "1";
                    edt_internetSeed.setVisibility(View.VISIBLE);
                    edt_internetSeed.setText(" ");

                }
            }
        });
*/
       /*
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
*/
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
                govList.add(0,new GovModels("dummyid","--اختر المحافظة--"));
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.e("NAME AND VALUE -->", dataSnapshot1.child("name").getValue() + "\n" + dataSnapshot1.getKey());

                    govList.add(new GovModels(dataSnapshot1.getKey(), dataSnapshot1.child("name").getValue().toString()));
                }

                GovSpinnerAdapter govSpinnerAdapter = new GovSpinnerAdapter(NewUiSurveyScreen.this, R.layout.spinneritem, govList);
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
                govName=govList.get(position).getName();
                Log.e("KEY-->", strGovId);
                citiesList.clear();


                iniCitiesSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(NewUiSurveyScreen.this, "Please Choose Your Gov", Toast.LENGTH_SHORT).show();


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
                citiesList.add(0,new CitiesModels("dummyid","--اختر المدينة --"));
                if (!strGovId.equals("dummyid"))
                {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        citiesList.add(new CitiesModels(dataSnapshot1.getKey(), dataSnapshot1.child("name").getValue().toString()));
                    }


                    CitiesSpinnerAdapter citiesSpinnerAdapter = new CitiesSpinnerAdapter(NewUiSurveyScreen.this, R.layout.spinneritem, citiesList);
                    spinnerCities.setAdapter(citiesSpinnerAdapter);
                }else {
                    CitiesSpinnerAdapter citiesSpinnerAdapter = new CitiesSpinnerAdapter(NewUiSurveyScreen.this, R.layout.spinneritem, citiesList);
                    spinnerCities.setAdapter(citiesSpinnerAdapter);
                }

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
                    cityName=citiesList.get(position).getName();

                    String city = citiesList.get(position).getName();
                    String other = new String("اخري");

                    strCityId = citiesList.get(position).getId();
                    districsList.clear();

                    iniDistrictsSpinner();
                    /*
                    if (city.equals(other)) {

                        edtOtherCities.setVisibility(View.VISIBLE);
                        edtOtherCities.requestFocus();
                        if (!edtOtherCities.hasFocus()) {
                            impty=true;

                            String cityId = databaseCityReference.push().getKey();
                            databaseDisReference.child(cityId).setValue(new DistrictsModels(cityId, edtOtherCities.getText().toString()));
                            strCityId = cityId;

                        }
                    } else {

                        strCityId = citiesList.get(position).getId();
                        districsList.clear();

                        iniDistrictsSpinner();
                    }

*/
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(NewUiSurveyScreen.this, "Please Choose Your city", Toast.LENGTH_SHORT).show();

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
                districsList.add(0,new DistrictsModels("dummyid","--اختر الحى --"));
                if (!strCityId.equals("dummyid"))
                {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        districsList.add(new DistrictsModels(dataSnapshot1.getKey(), dataSnapshot1.child("name").getValue().toString()));
                    }


                }
                else {
                    DistricSpinnerAdapter citiesSpinnerAdapter = new DistricSpinnerAdapter(NewUiSurveyScreen.this, R.layout.spinneritem, districsList);
                    spinnerDistrict.setAdapter(citiesSpinnerAdapter);
                }


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

                    disName = districsList.get(position).getName();

                    String dist = districsList.get(position).getName();
                    String other = new String("اخرى");
                    strDisrtric = districsList.get(position).getId();
                    iniOfficesSpinner();
/*
                    if (dist.equals(other)) {


                        if (edtOtherDistrict.hasFocus()) {

                            String Disid = databaseDisReference.push().getKey();
                            databaseDisReference.child(Disid).setValue(new DistrictsModels(Disid, edtOtherDistrict.getText().toString()));
                            strDisrtric = Disid;
                            impty=true;

                        }

                    } else {
                        strDisrtric = districsList.get(position).getId();
                        iniOfficesSpinner();
                    }
*/
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(NewUiSurveyScreen.this, "Please Choose Your District", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void iniOfficesSpinner() {

        // Spinner Drop down elements
        spinnerOfficeName.setPrompt("أختار المكتب");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Office").child(strDisrtric);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                officesList.clear();
                officesList.add(0, new OfficeModel("dummyid", "--اختر المكتب--","0"));
                if (!strDisrtric.equals("dummyid")) {


                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Log.e("Visited",dataSnapshot1.child("visited").getValue().toString());
                        if (dataSnapshot1.child("project_id").getValue().toString().equals(ConstMethods.getSavedprogectid(NewUiSurveyScreen.this)) ) {
                            if (Integer.valueOf(dataSnapshot1.child("visited").getValue().toString()) == 0)  {

                                officesList.add(new OfficeModel(dataSnapshot1.getKey(), dataSnapshot1.child("office_name").getValue().toString(), "1"));
                                DatabaseReference ref2 = database.getReference("Office").child(strofficeid);

                                //officesList.remove(new OfficeModel(dataSnapshot1.getKey(), dataSnapshot1.child("visited").getValue().toString(), visit));
                            }

                        }

                    }



                } else {
                    OfficeAdapter officeAdapter = new OfficeAdapter(NewUiSurveyScreen.this, R.layout.spinneritem, officesList);
                    spinnerOfficeName.setAdapter(officeAdapter);
                }

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
                    officeName=officesList.get(position).getName();
                    officeVisit=officesList.get(position).getVis();

                    Log.e("OFFICE ID -->", strofficeid);
                    Log.e("OFFICE Vis -->", officeVisit);

                    // ConstMethods.saveOffice(strofficeid,SurvayScreen.this);

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
