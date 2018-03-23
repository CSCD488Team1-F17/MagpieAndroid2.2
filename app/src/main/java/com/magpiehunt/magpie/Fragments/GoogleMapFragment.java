package com.magpiehunt.magpie.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.magpiehunt.magpie.Entities.Landmark;
import com.magpiehunt.magpie.Helper.LocationTracker;
import com.magpiehunt.magpie.Helper.MapLocationInfoWindow;
import com.magpiehunt.magpie.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * This fragment is used on the nav bar as well as when you click on the map tab within a collection to see a location
 * currently the alternate newInstance function is not used but when you want to implement collection data loading to the map use that.
 *
 */

//TODO handle denied permissions requests better
//TODO Fix map not reloading on reload of map fragment properly
public class GoogleMapFragment extends Fragment
        implements OnMapReadyCallback {

    private final int PERMISSION_REQUEST = 1;
    private LocationTracker userLocation;
    private List<String> locTitles;
    private List<LatLng> latLngs;
    private MenuItem addLocButton, saveLocButton;
    private float zoom;
    private MapLocationInfoWindow infoWindow;
    private Marker selectedMarker = null;//could be used in the future to allow for updating distance to location
    private ArrayList<Marker> markerList;
    private GoogleMap gMap = null;
    private OnFragmentInteractionListener mListener;

    public GoogleMapFragment() {}

    public static GoogleMapFragment newInstance() {
        GoogleMapFragment fragment = new GoogleMapFragment();
        return fragment;
    }

    //use this for loading Landmarks to map upon loading of map
    public static GoogleMapFragment newInstance(List<Landmark> landmarkList){
        GoogleMapFragment fragment = new GoogleMapFragment();
        if(landmarkList != null && landmarkList.size() != 0)
        {
            Bundle args = new Bundle();
            args.putInt("size", landmarkList.size());
            for(int i = 0; i < landmarkList.size(); i++){
                args.putDouble(Integer.toString(i) + "lat", landmarkList.get(i).getLatitude());
                args.putDouble(Integer.toString(i) + "long", landmarkList.get(i).getLatitude());
                args.putString(Integer.toString(i) + "title", landmarkList.get(i).getLandmarkName());
            }
        }
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set header information
        Toolbar toolbar = getActivity().findViewById(R.id.my_toolbar);
        toolbar.setTitle("Map View");
        setHasOptionsMenu(true);

        infoWindow = new MapLocationInfoWindow(getContext());
        markerList = new ArrayList<Marker>();

        if (savedInstanceState != null) {
            //do stuff if saved
        } else {
            zoom = -1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {// Inflate the layout for this fragment
        if(savedInstanceState != null){
            int numElements = savedInstanceState.getInt("size");
            latLngs = new ArrayList<LatLng>();
            for(int i = 0; i < numElements; i++){
                latLngs.add(new LatLng(savedInstanceState.getDouble(Integer.toString(i) + "lat"),
                        savedInstanceState.getDouble(Integer.toString(i) + "long")));
                locTitles.add(Integer.toString(i) + "title");
            }
        }
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        return v;
    }

    @Override
    public void onResume() {

        super.onResume();
        SupportMapFragment mapFrag = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);

        if (mapFrag == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFrag = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFrag).commit();
        }
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        if(userLocation != null){
            userLocation.shutDown();
        }
        userLocation = new LocationTracker(getActivity(), getContext(), gMap);
        initMap();
    }

    private void initMap() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMISSION_REQUEST);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                gMap.setMyLocationEnabled(true);
            } else {
                return;
            }
        }
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Location pressedLocation = new Location("");
                pressedLocation.setLatitude(marker.getPosition().latitude);
                pressedLocation.setLongitude(marker.getPosition().longitude);
                //double distanceInMeters = //currLoc.distanceTo(pressedLocation);
                marker.setSnippet(roundFloat((float) userLocation.distanceToPoint(pressedLocation)/*distanceInMeters*/, 2) + " meters to Location.");
                marker.showInfoWindow();
                selectedMarker = marker;
                return false;//this allows default behavior to take place
            }
        });
        gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        UiSettings mapSettings = gMap.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
        mapSettings.setCompassEnabled(true);
        mapSettings.setRotateGesturesEnabled(true);

        if(userLocation.checkLocationPermission()){
            moveToLocation(userLocation.getCurrLoc());
            if(latLngs != null){
                for(int i = 0; i < latLngs.size(); i++){
                    placeMarker(latLngs.get(i), locTitles.get(i));
                }
            }
        }
    }

    //TODO remove unused options menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_location:
                //addCurrentLocation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //use this function to place a marker on the map
    private void placeMarker(LatLng loc, String title) {
        gMap.addMarker(new MarkerOptions().position(loc).title(title).icon(BitmapDescriptorFactory.defaultMarker(190)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        userLocation.shutDown();//gpsTracker.stopUsingGPS();
        addLocButton.setVisible(false);
        saveLocButton.setVisible(false);


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        addLocButton = menu.findItem(R.id.add_location);
        saveLocButton = menu.findItem(R.id.save_locations);
        addLocButton.setVisible(true);
        saveLocButton.setVisible(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //=======================helper functions==========================
    private void showLandmark(Landmark landmark) {
        MarkerOptions opt = new MarkerOptions();
        opt.position(new LatLng(landmark.getLatitude(), landmark.getLongitude()));
        opt.title(landmark.getLandmarkName());
        gMap.addMarker(opt);
    }

    private Location getLocation() {
        LocationRequest lr = new LocationRequest();
        lr.setInterval(0);//0 means do asap
        lr.setFastestInterval(0);
        lr.setNumUpdates(1);//stop after one recieved
        lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return null;
    }

    private void moveToLocation(Location l) {
        LatLng coords = new LatLng(l.getLatitude(), l.getLongitude());
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords, 14));
    }

    private static float roundFloat(float d, int decimal)//code copied from: https://stackoverflow.com/questions/8911356/whats-the-best-practice-to-round-a-float-to-2-decimals
    {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimal, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }//*/
}
