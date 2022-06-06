package com.myproj.wear.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.myproj.wear.helperclasses.PatientHelperClass;

import java.util.Random;

public class PatientDb extends SQLiteOpenHelper {
        public static final String DATABASE_NAME= "PatientData.db";
        public static final String TABLE_NAME= "Patient_table";
        public static final String COL_1= "NAME";
        public static final String COL_2= "EMAIL";
        public static final String COL_3= "NUMBER";
        public static final String COL_4= "CTNAME";
        public static final String COL_5= "CTNUM";
        public static final String COL_6= "GENDER";
        public static final String COL_7= "DOB";

        public PatientDb(Context context) {
            super(context, DATABASE_NAME, null, 1 );
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + TABLE_NAME +"(NAME TEXT PRIMARY KEY,EMAIL TEXT ,NUMBER INTEGER ,CTNAME TEXT , CTNUM INTEGER, GENDER TEXT ,DOB DATE )" );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        public boolean insertData(PatientHelperClass patientInfo){
            String name = patientInfo.getUsername();
            String email = patientInfo.getEmail();
            String number = patientInfo.getPhoneNo();
            String ctName = patientInfo.getcTName();
            String ctNum = patientInfo.getcTNum();
            String gender = patientInfo.getGender();
            String dob = patientInfo.getDate_of_birth();
            SQLiteDatabase db= this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(COL_1,name);
            contentValues.put(COL_2,email);
            contentValues.put(COL_3,number);
            contentValues.put(COL_4,ctName);
            contentValues.put(COL_5,ctNum);
            contentValues.put(COL_6,gender);
            contentValues.put(COL_7,dob);
            long result = db.insert(TABLE_NAME,null,contentValues);
            if (result == -1)
                return false;
            else
                return true;
        }

        public String getPatiantInfo(String name) {
            SQLiteDatabase careTakerInfo = this.getWritableDatabase();
            Cursor cursor = careTakerInfo.rawQuery("SELECT * FROM Patient_table WHERE CTNAME=?",new String[]{name});
            final int nameIndex = cursor.getColumnIndex(COL_1);
            while(cursor.moveToNext()){
               return cursor.getString(nameIndex);
            }
            return "NO_PATIENT_FOUND";
        }

        public boolean patientOrNot(String name) {
            SQLiteDatabase careTakerInfo = this.getWritableDatabase();
            Cursor cursor = careTakerInfo.rawQuery("SELECT * FROM Patient_table WHERE NAME=?",new String[]{name});
            if(cursor.getCount()>0) {
                return true;
            }
            return false;
        }

    public String getCareTakerPhoneNumber(String name) {
        SQLiteDatabase careTakerInfo = this.getWritableDatabase();
        Cursor cursor = careTakerInfo.rawQuery("SELECT * FROM Patient_table WHERE NAME=?",new String[]{name});
        final int careTakerNumber = cursor.getColumnIndex(COL_5);
        while(cursor.moveToNext()){
            return cursor.getString(careTakerNumber);
        }
        return "";
    }

}
