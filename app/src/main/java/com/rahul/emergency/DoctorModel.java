package com.rahul.emergency;

public class DoctorModel {

    String id;
    String name;
    String mobile;
    String specialist;
    String latitude;
    String longitude;
    String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//
//    public DoctorModel(String name, String mobile, String specialist, String latitude, String longitude, String address) {
//        this.name = name;
//        this.mobile = mobile;
//        this.specialist = specialist;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.address = address;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
