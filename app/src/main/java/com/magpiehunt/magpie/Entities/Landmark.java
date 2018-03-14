package com.magpiehunt.magpie.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by James on 1/10/2018.
 */
@Parcel
@Entity(tableName = "Landmarks", foreignKeys = @ForeignKey(
        entity = Collection.class,
        parentColumns = "cID",
        childColumns = "cID"))
public class Landmark {

    @SerializedName("LID")
    @Expose
    @PrimaryKey
    protected int lID;
    @SerializedName("CID")
    @Expose
    protected int cID;
    @SerializedName("LandmarkName")
    @Expose
    protected String LandmarkName;
    @SerializedName("Latitude")
    @Expose
    protected double latitude;
    @SerializedName("Longitude")
    @Expose
    protected double longitude;
    @SerializedName("LandmarkDescription")
    @Expose
    protected String landmarkDescription;
    @SerializedName("QRCode")
    @Expose
    protected String qRCode;
    @SerializedName("PicID")
    @Expose
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    protected byte[] picID;
    @SerializedName("BadgeID")
    @Expose
    protected int badgeID;
    @SerializedName("OrderNum")
    @Expose
    protected int orderNum;
    @SerializedName("Subtitle")
    @Expose
    protected String subtitle;

    @SerializedName("Completed")
    @Expose
    protected boolean completed;

    //@Ignore
    //Bitmap img;

    //@ColumnInfo(typeAffinity = ColumnInfo.BLOB)
   // protected byte[] img;

    /*public byte[] getImg()
    {
        return img;
    }
    public void setImg(byte[] img)
    {
        this.img = img;
    }*/

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

    public byte[] getPicID() {
        return picID;
    }

    public void setPicID(byte[] picID) {
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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
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