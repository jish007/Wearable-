package com.myproj.wear.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.myproj.wear.R;
import com.myproj.wear.common.LoginSignup.StartUpScreen;
import com.myproj.wear.databases.HealthDataDb;
import com.myproj.wear.databases.LoginDb;
import com.myproj.wear.databases.PatientDb;
import com.myproj.wear.helperclasses.HealthDataHelper;
import com.myproj.wear.helperclasses.PatientHelperClass;
import com.myproj.wear.helperclasses.homeAdapter.FeaturedAdapter;
import com.myproj.wear.helperclasses.homeAdapter.FeaturedHelperClass;

import java.util.ArrayList;
import java.util.Objects;

public class HomePagePatient extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    // variables
    RecyclerView featuredRecycler;
    RecyclerView.Adapter adapter;
    ImageView menu_icon;
    static final float END_SCALE = 0.7f;
    LinearLayout contentView;

    private String username;

    Button changeBtn;

    TextView heartRate;
    TextView bloodPressure;
    TextView motionSensor;
    LoginDb loginDb = new LoginDb(this);
    HealthDataDb healthDataDb = new HealthDataDb(this);
    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    PatientDb patientDb;

    PatientHelperClass patientHelperClass;

    ImageView refresh;

    TextView profileName,profileMail,profileNum,profileDob,profileGender,profileCtName,profileCtNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_page_patient);

        // asking permission for sms access
        ActivityCompat.requestPermissions(HomePagePatient.this, new String[]{Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        //hooks
        featuredRecycler = findViewById(R.id.featured_recycler);
        //for menu

        heartRate = findViewById(R.id.heart_rate);
        bloodPressure = findViewById(R.id.blood_pressure);
        motionSensor = findViewById(R.id.motionSensor);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menu_icon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.menu_content);
        refresh = findViewById(R.id.refresh);
        changeBtn = findViewById(R.id.change);

        profileName = findViewById(R.id.profile_name);
        profileMail = findViewById(R.id.profile_email);
        profileNum = findViewById(R.id.profile_number);
        profileDob = findViewById(R.id.profile_dob);
        profileGender = findViewById(R.id.profile_gender);
        profileCtName = findViewById(R.id.profile_ctName);
        profileCtNum = findViewById(R.id.profile_ctNum);

        Intent i = getIntent();
        username = i.getStringExtra("patientName");

        HealthDataHelper healthdata = healthDataDb.getLastUpdatedHealthData(username);
        Log.d("HEALTHDATA FOR PATIENT","healthdata"+healthdata);
         if( healthdata.getHeartRateReading()==null) {
             heartRate.setText("0.0");
         }

         if(healthdata.getBpReading()==null) {
            bloodPressure.setText("0.0");
        }

         if (healthdata.getMotionSensorReading()==null){
             motionSensor.setText("0.0");
         }
        else{
            bloodPressure.setText(healthdata.getBpReading());
            heartRate.setText(healthdata.getHeartRateReading());
            motionSensor.setText(healthdata.getMotionSensorReading());
        }

        navigationDrawer();         // popping up nav bar on clicking menu btn

        featuredRecycler();         // for card view in home

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HealthDataHelper healthdata = healthDataDb.getLastUpdatedHealthData(username);
                Log.d("HEALTHDATA FOR PATIENT","healthdata"+healthdata);
                if( healthdata.getHeartRateReading()==null) {
                    heartRate.setText("0.0");
                }

                if(healthdata.getBpReading()==null) {
                    bloodPressure.setText("0.0");
                }
                else{
                    bloodPressure.setText(healthdata.getBpReading());
                    heartRate.setText(healthdata.getHeartRateReading());
                }
            }
        });

        patientDb = new PatientDb(this);
        patientHelperClass = new PatientHelperClass();
        patientHelperClass = patientDb.getUserData(username);
        profileName.setText("Patient Name: " +patientHelperClass.getUsername());
        profileMail.setText("Patient Email: " +patientHelperClass.getEmail());
        profileNum.setText("Patient Number: " +patientHelperClass.getPhoneNo());
        profileGender.setText("Patient Gender: " +patientHelperClass.getGender());
        profileDob.setText("Patient Date Of Birth: " +patientHelperClass.getDate_of_birth());
        profileCtName.setText("Caretaker Name: " +patientHelperClass.getcTName());
        profileCtNum.setText("Caretaker Number: " +patientHelperClass.getcTNum());

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),UpdateOrDelUserInfo.class);
                i.putExtra("profileName", username);
                startActivity(i);
            }
        });
    }



    // nav drawer fn
    private void navigationDrawer() {

        //Navigation Drawer
        navigationView.bringToFront();          // interact with nav dr
        navigationView.setNavigationItemSelectedListener(this);         // for selecting
        navigationView.setCheckedItem(R.id.nav_home);

        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))        //drawer is visible close it
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(GravityCompat.START);           //else open it
            }
        });

        animateNavigationDrawer();              // for animate nav drawer
    }

    private void animateNavigationDrawer() {

        drawerLayout.setScrimColor(getResources().getColor(R.color.light_white));           // clr at right side on selecting nav drawer
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // scale the view based on current slide offset
                final float diffScaledOffset = slideOffset * (1- END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                //translate the view, according for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {                           // closing the nav drawer on first back btn and second one for closing the app

        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.nav_contacts:
                Intent i = new Intent(getApplicationContext(),PickContact.class);
                i.putExtra("username", username);
                startActivity(i);
                break;
            case R.id.nav_logout:
                Intent i2 = new Intent(getApplicationContext(),PatientLogin.class);
                startActivity(i2);
                loginDb.updateActiveUser("F",username);
                break;
        }

        return true;
    }

    // recycler and card fn
    private void featuredRecycler() {

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        ArrayList<FeaturedHelperClass> features = new ArrayList<>();

        features.add(new FeaturedHelperClass(R.drawable.smartwatch_card_img,"Smartwatch Pairing","Pair with smart watch and get your health data"));
        features.add(new FeaturedHelperClass(R.drawable.card_emergency,"Instead Medical Support","Can send SMS automatically on emergency situations"));
        features.add(new FeaturedHelperClass(R.drawable.card_monitor,"Caretaker can monitor your health","Caretaker can monitor your health from anywhere"));
        features.add(new FeaturedHelperClass(R.drawable.relax_oldman,"Just Relax","We will handle everything for you, As you have the app in your pocket and paired with your smartwatch you don't have to worry"));

        adapter = new FeaturedAdapter(features);            //passing arraylist
        featuredRecycler.setAdapter(adapter);
    }

}