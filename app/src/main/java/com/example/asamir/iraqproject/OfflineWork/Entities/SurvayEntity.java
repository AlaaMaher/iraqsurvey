package com.example.asamir.iraqproject.OfflineWork.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "survayTable")
public class SurvayEntity {


    @PrimaryKey(autoGenerate = true)
    @NonNull
    public   int id ;
    private String gov;
    private String cityId;
    private String districId;
    private String address;
    private String phone;
    private String hasInternet;
    private String isNetwork;
    private String internetSeed;
    private String projectName;
    //---
    private String shiftType;
    private String morning_shift_from;
    private String morning_shift_to;
    private String evening_shift_from;
    private String evening_shift_to;
    //---
    private String computer_count;
    private String computer_notes;
    private String printers_count;
    private String printers_notes;
    private String scanners_count;
    private String scanners_notes;
    private String rooms_count;
    //---
    private String notes;
    private String visitDate;
    private String lat;
    private String lng;
    //--
    private String userID;
    private String sketchData;
    private String outDoorPhotos;
    private  String inDoorPhotos;
    private String postionData;
    private String office_name_or_id;

    private String projectObjectId;

    private String otherCity;
    private String otherDistric ;

    private String isUploaded;

    public SurvayEntity(String gov, String cityId,
                               String districId, String address, String phone, String hasInternet,
                               String isNetwork, String internetSeed, String projectName,
                               String shiftType, String morning_shift_from, String morning_shift_to,
                               String evening_shift_from, String evening_shift_to, String computer_count,
                               String computer_notes, String printers_count, String printers_notes, String scanners_count,
                               String scanners_notes, String rooms_count,
                               String notes, String userID, String visitDate, String lat, String lng, String sketchData,
                               String outDoorPhotos, String inDoorPhotos, String postionData, String office_name_or_id,  String otherCity,
                               String otherDistric,String isUploaded ) {
        this.gov = gov;
        this.cityId=cityId;
        this.districId=districId;
        this.address = address;
        this.phone = phone;
        this.hasInternet = hasInternet;
        this.isNetwork = isNetwork;
        this.internetSeed = internetSeed;
        this.shiftType = shiftType;
        this.morning_shift_from = morning_shift_from;
        this.morning_shift_to = morning_shift_to;
        this.evening_shift_from = evening_shift_from;
        this.evening_shift_to = evening_shift_to;
        this.computer_count = computer_count;
        this.computer_notes = computer_notes;
        this.printers_count = printers_count;
        this.printers_notes = printers_notes;
        this.scanners_count = scanners_count;
        this.scanners_notes = scanners_notes;
        this.rooms_count = rooms_count;
        this.projectName = projectName;
        this.notes = notes;
        this.userID = userID;
        this.visitDate = visitDate;
        this.lat = lat;
        this.lng = lng;
        this.sketchData = sketchData;
        this.outDoorPhotos = outDoorPhotos;
        this.inDoorPhotos = inDoorPhotos;
        this.postionData = postionData;
        this.office_name_or_id = office_name_or_id;
        this.otherCity=otherCity;
        this.otherDistric=otherDistric;
        this.isUploaded=isUploaded;
    }


    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getGov() {
        return gov;
    }

    public void setGov(String gov) {
        this.gov = gov;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistricId() {
        return districId;
    }

    public void setDistricId(String districId) {
        this.districId = districId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHasInternet() {
        return hasInternet;
    }

    public void setHasInternet(String hasInternet) {
        this.hasInternet = hasInternet;
    }

    public String getIsNetwork() {
        return isNetwork;
    }

    public void setIsNetwork(String isNetwork) {
        this.isNetwork = isNetwork;
    }

    public String getInternetSeed() {
        return internetSeed;
    }

    public void setInternetSeed(String internetSeed) {
        this.internetSeed = internetSeed;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public String getMorning_shift_from() {
        return morning_shift_from;
    }

    public void setMorning_shift_from(String morning_shift_from) {
        this.morning_shift_from = morning_shift_from;
    }

    public String getMorning_shift_to() {
        return morning_shift_to;
    }

    public void setMorning_shift_to(String morning_shift_to) {
        this.morning_shift_to = morning_shift_to;
    }

    public String getEvening_shift_from() {
        return evening_shift_from;
    }

    public void setEvening_shift_from(String evening_shift_from) {
        this.evening_shift_from = evening_shift_from;
    }

    public String getEvening_shift_to() {
        return evening_shift_to;
    }

    public void setEvening_shift_to(String evening_shift_to) {
        this.evening_shift_to = evening_shift_to;
    }

    public String getComputer_count() {
        return computer_count;
    }

    public void setComputer_count(String computer_count) {
        this.computer_count = computer_count;
    }

    public String getComputer_notes() {
        return computer_notes;
    }

    public void setComputer_notes(String computer_notes) {
        this.computer_notes = computer_notes;
    }

    public String getPrinters_count() {
        return printers_count;
    }

    public void setPrinters_count(String printers_count) {
        this.printers_count = printers_count;
    }

    public String getPrinters_notes() {
        return printers_notes;
    }

    public void setPrinters_notes(String printers_notes) {
        this.printers_notes = printers_notes;
    }

    public String getScanners_count() {
        return scanners_count;
    }

    public void setScanners_count(String scanners_count) {
        this.scanners_count = scanners_count;
    }

    public String getScanners_notes() {
        return scanners_notes;
    }

    public void setScanners_notes(String scanners_notes) {
        this.scanners_notes = scanners_notes;
    }

    public String getRooms_count() {
        return rooms_count;
    }

    public void setRooms_count(String rooms_count) {
        this.rooms_count = rooms_count;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSketchData() {
        return sketchData;
    }

    public void setSketchData(String sketchData) {
        this.sketchData = sketchData;
    }

    public String getOutDoorPhotos() {
        return outDoorPhotos;
    }

    public void setOutDoorPhotos(String outDoorPhotos) {
        this.outDoorPhotos = outDoorPhotos;
    }

    public String getInDoorPhotos() {
        return inDoorPhotos;
    }

    public void setInDoorPhotos(String inDoorPhotos) {
        this.inDoorPhotos = inDoorPhotos;
    }

    public String getPostionData() {
        return postionData;
    }

    public void setPostionData(String postionData) {
        this.postionData = postionData;
    }

    public String getOffice_name_or_id() {
        return office_name_or_id;
    }

    public void setOffice_name_or_id(String office_name_or_id) {
        this.office_name_or_id = office_name_or_id;
    }

    public String getProjectObjectId() {
        return projectObjectId;
    }

    public void setProjectObjectId(String projectObjectId) {
        this.projectObjectId = projectObjectId;
    }

    public String getOtherCity() {
        return otherCity;
    }

    public void setOtherCity(String otherCity) {
        this.otherCity = otherCity;
    }

    public String getOtherDistric() {
        return otherDistric;
    }

    public void setOtherDistric(String otherDistric) {
        this.otherDistric = otherDistric;
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }

    @Override
    public String toString() {
        return "SurvayEntity{" +
                "id=" + id +
                ", gov='" + gov + '\'' +
                ", cityId='" + cityId + '\'' +
                ", districId='" + districId + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", hasInternet='" + hasInternet + '\'' +
                ", isNetwork='" + isNetwork + '\'' +
                ", internetSeed='" + internetSeed + '\'' +
                ", projectName='" + projectName + '\'' +
                ", shiftType='" + shiftType + '\'' +
                ", morning_shift_from='" + morning_shift_from + '\'' +
                ", morning_shift_to='" + morning_shift_to + '\'' +
                ", evening_shift_from='" + evening_shift_from + '\'' +
                ", evening_shift_to='" + evening_shift_to + '\'' +
                ", computer_count='" + computer_count + '\'' +
                ", computer_notes='" + computer_notes + '\'' +
                ", printers_count='" + printers_count + '\'' +
                ", printers_notes='" + printers_notes + '\'' +
                ", scanners_count='" + scanners_count + '\'' +
                ", scanners_notes='" + scanners_notes + '\'' +
                ", rooms_count='" + rooms_count + '\'' +
                ", notes='" + notes + '\'' +
                ", visitDate='" + visitDate + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", userID='" + userID + '\'' +
                ", sketchData='" + sketchData + '\'' +
                ", outDoorPhotos='" + outDoorPhotos + '\'' +
                ", inDoorPhotos='" + inDoorPhotos + '\'' +
                ", postionData='" + postionData + '\'' +
                ", office_name_or_id='" + office_name_or_id + '\'' +
                ", projectObjectId='" + projectObjectId + '\'' +
                ", otherCity='" + otherCity + '\'' +
                ", otherDistric='" + otherDistric + '\'' +
                ", isUploaded='" + isUploaded + '\'' +
                '}';
    }
}
