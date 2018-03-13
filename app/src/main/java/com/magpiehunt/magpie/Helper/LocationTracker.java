package com.magpiehunt.magpie.Helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.magpiehunt.magpie.Fragments.GoogleMapFragment;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by evan g on 3/13/2018.
 */

//after initialization you can call hasLocPermission in order to check if mermissions were accepted or not
//that way your fragment/activity can handle the results appropriatly
public class LocationTracker {
    private Context context;
    private GoogleMap gMap;
    private boolean hasPermission = false;
    //private GPSTracker gpsTracker;
    private static double validDistanceInMeters = 50;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private Location currLoc;
    private LatLng coords;

    public LocationTracker(Context cxt, GoogleMap gMap){//use null for gMap if not using this feature
        context = cxt;
        this.gMap = gMap;
        checkLocationPermission();
    }

    public boolean isValidDistance(Location to){
        if(distanceToPoint(to) <= validDistanceInMeters){
            return true;
        }
        return false;
    }

    public double distanceToPoint(Location loc){
        return currLoc.distanceTo(loc);
    }

    public boolean checkLocationPermission() {
        if(hasPermission){return true;}
        if(PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            hasPermission = true;
            /*gpsTracker = new GPSTracker(context);
            if (gpsTracker.canGetLocation()) {
                coords = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                currLoc = new Location("");
                currLoc.setLatitude(coords.latitude);
                currLoc.setLongitude(coords.longitude);
            }//*/
            mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    currLoc = location;
                    coords = new LatLng(location.getLatitude(), location.getLongitude());
                    if(gMap != null)
                    {
                        gMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 20));
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {
                    if(gMap != null && coords != null)
                    {
                        gMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 20));
                    }
                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, mLocationListener);
            currLoc = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            coords = new LatLng(currLoc.getLatitude(), currLoc.getLongitude());
            return true;
        }
        return false;
    }



    public boolean hasLocPermission(){
        return hasPermission;
    }

    public Location getCurrLoc(){
        return currLoc;
    }

    public void setgMap(GoogleMap map){
        gMap = map;
    }

    public void shutDown(){
        hasPermission = false;//this forces another permissions check on activity restart and also re-initializes everything
        //gpsTracker.stopUsingGPS();
    }
}
