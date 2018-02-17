package com.magpiehunt.magpie.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by James on 1/10/2018.
 */

@Entity(tableName = "Landmarks" , foreignKeys = @ForeignKey(
                                            entity = Collection.class,
                                            parentColumns = "cID",
                                            childColumns = "cID"))
public class Landmark {

    @SerializedName("LID")
    @Expose
    @PrimaryKey
    private int lID;
    @SerializedName("CID")
    @Expose
    private int cID;
    @SerializedName("LandmarkName")
    @Expose
    private String LandmarkName;
    @SerializedName("Latitude")
    @Expose
    private double latitude;
    @SerializedName("Longitude")
    @Expose
    private double longitude;
    @SerializedName("LandmarkDescription")
    @Expose
    private String landmarkDescription;
    @SerializedName("QRCode")
    @Expose
    private String qRCode;
    @SerializedName("PicID")
    @Expose
    private int picID;
    @SerializedName("BadgeID")
    @Expose
    private int badgeID;
    @SerializedName("OrderNum")
    @Expose
    private int orderNum;


    public int getLID() {
        return lID;
    }

    public void setLID(int LID) {
        this.lID = LID;
    }

    public int getCID() {
        return cID;
    }

    public void setCID(int cID) {
        this.cID = cID;
    }

    public String getLandmarkName() {
        return LandmarkName;
    }

    public void setLandmarkName(String landmarkName) {
        LandmarkName = landmarkName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLandmarkDescription() {
        return landmarkDescription;
    }

    public void setLandmarkDescription(String landmarkDescription) {
        this.landmarkDescription = landmarkDescription;
    }

    public String getQRCode() {
        return qRCode;
    }

    public void setQRCode(String QRCode) {
        this.qRCode = QRCode;
    }

    public int getPicID() {
        return picID;
    }

    public void setPicID(int picID) {
        this.picID = picID;
    }

    public int getBadgeID() {
        return badgeID;
    }

    public void setBadgeID(int badgeID) {
        this.badgeID = badgeID;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

}
/*
query = "CREATE TABLE Landmarks(\n" +
                "  LID INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
                "  CID INTEGER NOT NULL,\n" +
                "  LandmarkName VARCHAR(100) NOT NULL,\n" +
                "  Latitude DOUBLE DEFAULT 0.0 NOT NULL,\n" +
                "  Longitude DOUBLE DEFAULT 0.0 NOT NULL,\n" +
                "  LandmarkDescription VARCHAR(1000) NOT NULL,\n" +
                "  QRCode VARCHAR(625) DEFAULT \"{ EMPTY }\",\n" +
                "  PicID INTEGER DEFAULT 0,\n" +
                "  BadgeID INTEGER DEFAULT 0,\n" +
                "  OrderNum INTEGER,\n" +
                "  \n" +
                "  FOREIGN KEY (CID) REFERENCES Collections(CID) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                "  \n" +
                ")";
 */