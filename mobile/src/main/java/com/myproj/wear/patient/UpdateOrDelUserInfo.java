package com.myproj.wear.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.myproj.wear.R;
import com.myproj.wear.common.LoginSignup.StartUpScreen;
import com.myproj.wear.databases.HealthDataDb;
import com.myproj.wear.databases.LoginDb;
import com.myproj.wear.databases.PatientDb;
import com.myproj.wear.databases.SmsLimitHelperDb;
import com.myproj.wear.helperclasses.SmsHelperClass;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class UpdateOrDelUserInfo extends AppCompatActivity {

    Button update,delete;
    //EditText addField,addDetails;
    String username;

    PatientDb patientDb;

    LoginDb loginDb;

    HealthDataDb healthdataDb;

    SmsLimitHelperDb smsLimitHelperDb;

    String userOrCaretaker;

    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView;
    TextInputLayout selected;
    TextInputLayout selected1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_or_del_user_info);

       // addField = findViewById(R.id.getData);
      //  addDetails = findViewById(R.id.getData1);

        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete_data);

        textInputLayout = findViewById(R.id.menu_drop);
        autoCompleteTextView = findViewById(R.id.drop_items);
        selected = findViewById(R.id.input1);
        selected1 = findViewById(R.id.input2);

        Intent i = getIntent();
        username = i.getStringExtra("profileName");

        patientDb = new PatientDb(this);
        loginDb = new LoginDb(this);
        smsLimitHelperDb = new SmsLimitHelperDb(this);
        healthdataDb = new HealthDataDb(this);

        String [] items={"Caretaker","Patient Email","Patient Number","Delete Account"};
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(UpdateOrDelUserInfo.this,R.layout.dropdown_list, items);
        autoCompleteTextView.setAdapter(itemAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userOrCaretaker = parent.getItemAtPosition(position).toString();
                switch (position){
                    case 0://caretaker change
                        selected.setVisibility(View.VISIBLE);
                        selected1.setVisibility(View.VISIBLE);
                        update.setVisibility(View.VISIBLE);
                        selected.setHint("Enter Caretaker Name");
                        selected1.setHint("Enter Caretaker Number");

                        break;
                    case 1://Patient Email
                        selected.setVisibility(View.VISIBLE);
                        selected1.setVisibility(View.INVISIBLE);
                        update.setVisibility(View.VISIBLE);
                        selected.setHint("Enter Patient Email");

                        break;
                    case 2://Patient Number
                        selected.setVisibility(View.VISIBLE);
                        selected1.setVisibility(View.INVISIBLE);
                        update.setVisibility(View.VISIBLE);
                        selected.setHint("Enter Patient Number");

                        break;
                    case 3://Account Delete
                        delete.setVisibility(View.VISIBLE);
                        selected.setVisibility(View.INVISIBLE);
                        selected1.setVisibility(View.INVISIBLE);
                        update.setVisibility(View.INVISIBLE);

                        break;
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(userOrCaretaker);
             if(userOrCaretaker!=null && userOrCaretaker.toString().toLowerCase().equals("caretaker") /*&& addDetails!=null*/){

                String careTakerName =  selected.getEditText().getText().toString();
                 String careTakerPhonenumber = selected1.getEditText().getText().toString();
                 patientDb.updateHealth(careTakerName,careTakerPhonenumber,"","",username);
                 new SmsHelperClass().updateCaretaker(username,careTakerName,careTakerPhonenumber,loginDb);

             }
             else if (userOrCaretaker!=null && userOrCaretaker.toString().toLowerCase().equals("patient email") /*&& addDetails!=null*/) {
                 patientDb.updateHealth("","",selected.getEditText().getText().toString(),"",username);

             }
             else if((userOrCaretaker!=null && userOrCaretaker.toString().toLowerCase().equals("patient number") /*&& addDetails!=null*/)) {
                    patientDb.updateHealth("","","",selected.getEditText().getText().toString(),username);

                }

             Intent i = new Intent(getApplicationContext(),HomePagePatient.class);
                i.putExtra("patientName", username);
                startActivity(i);
            }
        });

        /*update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(addField!=null && addField.getText().toString().toLowerCase().equals("caretaker") && addDetails!=null){
                String careTakerName =  addDetails.getText().toString().split("-")[0];
                 String careTakerPhonenumber = addDetails.getText().toString().split("-")[1];
                 patientDb.updateHealth(careTakerName,careTakerPhonenumber,"","",username);
                 new SmsHelperClass().updateCaretaker(username,careTakerName,careTakerPhonenumber,loginDb);

             }
             else if (addField!=null && addField.getText().toString().toLowerCase().equals("patientemail") && addDetails!=null) {
                 patientDb.updateHealth("","",addDetails.getText().toString(),"",username);

             }
             else if((addField!=null && addField.getText().toString().toLowerCase().equals("patientnumber") && addDetails!=null)) {
                    patientDb.updateHealth("","","",addDetails.getText().toString(),username);

                }

             Intent i = new Intent(getApplicationContext(),HomePagePatient.class);
                i.putExtra("patientName", username);
                startActivity(i);
            }
        });*/

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    patientDb.deletePatient(username);
                    loginDb.deleteHealthData(username);
                    healthdataDb.deleteHealthData(username);
                    smsLimitHelperDb.deleteHealthData(username);
                    Toast.makeText(getApplicationContext(),"Account Deleted",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), StartUpScreen.class);
                     startActivity(i);

            }
        });

    }
}