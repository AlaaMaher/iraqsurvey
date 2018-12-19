package com.example.asamir.iraqproject.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class DataCollectionModel implements Parcelable {
    String gov;
    String cityId;
    String districId;
    String address;
    String phone;
    String hasInternet;
    String isNetwork;
    String internetSeed;
    String projectName;
    //---
    String shiftType;
    String morning_shift_from;
    String morning_shift_to;
    String evening_shift_from;
    String evening_shift_to;
    //---
    String computer_count;
    String computer_notes;
    String printers_count;
    String printers_notes;
    String scanners_count;
    String scanners_notes;
    String rooms_count;
    //---
    String notes;
    String visitDate;
    String lat;
    String lng;
    //--
    String userID;
    String sketchData;
    String outDoorPhotos;
    String inDoorPhotos;
    String postionData;
    String office_name_or_id;

    String projectObjectId;

    String otherCity;
    String otherDistric ;
    String owenerShipType;

    public DataCollectionModel(String gov, String cityId,
                               String districId, String address, String phone, String hasInternet,
                               String isNetwork, String internetSeed, String projectName,
                               String shiftType, String morning_shift_from, String morning_shift_to,
                               String evening_shift_from, String evening_shift_to, String computer_count,
                               String computer_notes, String printers_count, String printers_notes, String scanners_count,
                               String scanners_notes, String rooms_count,
                               String notes, String userID, String visitDate, String lat, String lng, String sketchData,
                               String outDoorPhotos, String inDoorPhotos, String postionData, String office_name_or_id,  String otherCity,
                                       String otherDistric,String owenerShipType ) {
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
        this.owenerShipType=owenerShipType;
    }

    public DataCollectionModel(String gov, String cityId,
                               String districId, String address, String phone, String hasInternet,
                               String isNetwork, String internetSeed, String projectName,
                               String shiftType, String morning_shift_from, String morning_shift_to,
                               String evening_shift_from, String evening_shift_to, String computer_count,
                               String computer_notes, String printers_count, String printers_notes, String scanners_count,
                               String scanners_notes, String rooms_count,
                               String notes, String userID, String visitDate, String lat, String lng, String sketchData,
                               String outDoorPhotos, String inDoorPhotos, String postionData, String office_name_or_id,String projectObjectId, String otherCity,
                               String otherDistric,String owenerShipType ) {
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
        this.projectObjectId=projectObjectId;
        this.otherDistric=otherDistric;
        this.otherCity=otherCity;
        this.owenerShipType=owenerShipType;
    }

    public String getOwenerShipType() {
        return owenerShipType;
    }

    public void setOwenerShipType(String owenerShipType) {
        this.owenerShipType = owenerShipType;
    }

    public String getProjectObjectId() {
        return projectObjectId;
    }

    public String getGov() {
        return gov;
    }

    public String getCityId() {
        return cityId;
    }

    public String getDistricId() {
        return districId;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getHasInternet() {
        return hasInternet;
    }

    public String getIsNetwork() {
        return isNetwork;
    }

    public String getInternetSeed() {
        return internetSeed;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getShiftType() {
        return shiftType;
    }

    public String getMorning_shift_from() {
        return morning_shift_from;
    }

    public String getMorning_shift_to() {
        return morning_shift_to;
    }

    public String getEvening_shift_from() {
        return evening_shift_from;
    }

    public String getEvening_shift_to() {
        return evening_shift_to;
    }

    public String getComputer_count() {
        return computer_count;
    }

    public String getComputer_notes() {
        return computer_notes;
    }

    public String getPrinters_count() {
        return printers_count;
    }

    public String getPrinters_notes() {
        return printers_notes;
    }

    public String getScanners_count() {
        return scanners_count;
    }

    public String getScanners_notes() {
        return scanners_notes;
    }

    public String getRooms_count() {
        return rooms_count;
    }

    public String getNotes() {
        return notes;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getUserID() {
        return userID;
    }

    public String getSketchData() {
        return sketchData;
    }

    public String getOutDoorPhotos() {
        return outDoorPhotos;
    }

    public String getInDoorPhotos() {
        return inDoorPhotos;
    }

    public String getPostionData() {
        return postionData;
    }

    public String getOffice_name_or_id() {
        return office_name_or_id;
    }

    public String getOtherCity() {
        return otherCity;
    }

    public String getOtherDistric() {
        return otherDistric;
    }

    @Override
    public String toString() {
        return "DataCollectionModel{" +
                "gov='" + gov + '\'' +
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
                '}';
    }

    protected DataCollectionModel(Parcel in) {
        gov = in.readString();
        cityId = in.readString();
        districId = in.readString();
        address = in.readString();
        phone = in.readString();
        hasInternet = in.readString();
        isNetwork = in.readString();
        internetSeed = in.readString();
        projectName = in.readString();
        shiftType = in.readString();
        morning_shift_from = in.readString();
        morning_shift_to = in.readString();
        evening_shift_from = in.readString();
        evening_shift_to = in.readString();
        computer_count = in.readString();
        computer_notes = in.readString();
        printers_count = in.readString();
        printers_notes = in.readString();
        scanners_count = in.readString();
        scanners_notes = in.readString();
        rooms_count = in.readString();
        notes = in.readString();
        visitDate = in.readString();
        lat = in.readString();
        lng = in.readString();
        userID = in.readString();
        sketchData = in.readString();
        outDoorPhotos = in.readString();
        inDoorPhotos = in.readString();
        postionData = in.readString();
        office_name_or_id = in.readString();
        projectObjectId=in.readString();
        otherCity=in.readString();
        otherDistric=in.readString();
        owenerShipType=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gov);
        dest.writeString(cityId);
        dest.writeString(districId);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(hasInternet);
        dest.writeString(isNetwork);
        dest.writeString(internetSeed);
        dest.writeString(projectName);
        dest.writeString(shiftType);
        dest.writeString(morning_shift_from);
        dest.writeString(morning_shift_to);
        dest.writeString(evening_shift_from);
        dest.writeString(evening_shift_to);
        dest.writeString(computer_count);
        dest.writeString(computer_notes);
        dest.writeString(printers_count);
        dest.writeString(printers_notes);
        dest.writeString(scanners_count);
        dest.writeString(scanners_notes);
        dest.writeString(rooms_count);
        dest.writeString(notes);
        dest.writeString(visitDate);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(userID);
        dest.writeString(sketchData);
        dest.writeString(outDoorPhotos);
        dest.writeString(inDoorPhotos);
        dest.writeString(postionData);
        dest.writeString(office_name_or_id);
        dest.writeString(projectObjectId);

        dest.writeString(otherCity);
        dest.writeString(otherDistric);
        dest.writeString(owenerShipType);
    }



    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DataCollectionModel> CREATOR = new Parcelable.Creator<DataCollectionModel>() {
        @Override
        public DataCollectionModel createFromParcel(Parcel in) {
            return new DataCollectionModel(in);
        }

        @Override
        public DataCollectionModel[] newArray(int size) {
            return new DataCollectionModel[size];
        }
    };
}