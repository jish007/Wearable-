package com.myproj.wear.helperclasses;

public class PatientHelperClass {

    private String username, email, phoneNo, password, gender, date_of_birth, cTName, cTNum,active;





    public PatientHelperClass() {
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getcTNum() {
        return cTNum;
    }

    public void setcTNum(String cTNum) {
        this.cTNum = cTNum;
    }

    public String getcTName() {
        return cTName;
    }

    public void setcTName(String cTName) {
        this.cTName = cTName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getDate_of_birth() { return date_of_birth; }

    public void setDate_of_birth(String date_of_birth) { this.date_of_birth = date_of_birth; }
}
