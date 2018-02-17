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
import android.util.Log;
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
import com.magpiehunt.magpie.Helper.GPSTracker;
import com.magpiehunt.magpie.Helper.MapLocationInfoWindow;
import com.magpiehunt.magpie.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GoogleMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoogleMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//TODO handle denied permissions requests
//Fix map not reloading on reload of map fragment
public class GoogleMapFragment extends Fragment //implements OnViewCollectionListener
    //if you want to send data to this fragment use the following code
    /*
    private OnFragmentInteractionsListener mListener;

    //then inside a function call
    mListener.onFragmentInteraction(List<LatLon> locations);

     */
        implements OnMapReadyCallback/*, LocationSource.OnLocationChangedListener*/ {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    //private bool hasPosition, mapActive;
    public List<Landmark> landmarks;
    private MenuItem addLocButton, saveLocButton;
    private float zoom;
    private MapLocationInfoWindow infoWindow;
    private Marker selectedMarker = null;//could be used in the future to allow for updating distance to location
    private ArrayList<Marker> markerList;
    private final int PERMISSION_REQUEST = 1;
    private GoogleMap gMap = null;
    private Location currLoc;
    private OnFragmentInteractionListener mListener;
    private LatLng start = null;//curr loc
    GPSTracker gpsTracker;

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    public GoogleMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * param param1 Parameter 1.
     * param param2 Parameter 2.
     * @return A new instance of fragment GoogleMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoogleMapFragment newInstance() {
        GoogleMapFragment fragment = new GoogleMapFragment();
        Bundle args = new Bundle();
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

        //check permissions
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currLoc = location;
                start = new LatLng(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        LocationManager mLocationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
        infoWindow = new MapLocationInfoWindow(getContext());
        currLoc = new Location((LocationManager.GPS_PROVIDER));
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, mLocationListener);
        }

        markerList = new ArrayList<Marker>();
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if(savedInstanceState != null){
            //do stuff if saved
        }
        else{
            zoom = -1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {// Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        return v;
    }

    @Override
    public void onResume(){

        super.onResume();
        SupportMapFragment mapFrag = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);

        if(mapFrag == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFrag = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFrag).commit();
        }
        mapFrag.getMapAsync(this);
    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = getActivity().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        gMap = googleMap;
        initMap();
    }

    private void initMap() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        }
        else
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMISSION_REQUEST);
            if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                gMap.setMyLocationEnabled(true);
            }
            else{
                return;
            }
        }
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Location pressedLocation = new Location("");
                pressedLocation.setLatitude(marker.getPosition().latitude);
                pressedLocation.setLongitude(marker.getPosition().longitude);
                double distanceInMeters = currLoc.distanceTo(pressedLocation);
                marker.setSnippet(roundFloat((float)distanceInMeters, 2) + " meters to Location.");
                marker.showInfoWindow();
                selectedMarker = marker;
                return false;//this allows default behavior to take place
            }
        });
        UiSettings mapSettings = gMap.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
        mapSettings.setCompassEnabled(true);
        mapSettings.setRotateGesturesEnabled(true);
        if(zoom != -1){
            gMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }
        if(checkLocationPermission()) {
            Log.d("onResume OK", "");
            gpsTracker = new GPSTracker(getActivity());
            if (gpsTracker.canGetLocation()) {
                start = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                currLoc.setLatitude(start.latitude);
                currLoc.setLongitude(start.longitude);
                moveToLocation(currLoc);
                double meters = 150;
                double coef = meters * 0.0000089;
                placeMarker(new LatLng(currLoc.getLatitude() + coef, currLoc.getLongitude()), "Test Marker");
            } else {
                Toast.makeText(getActivity(), "Permissions required to proceed.", Toast.LENGTH_SHORT).show();
                //finish();//do something
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.add_location:
                addCurrentLocation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        switch(requestCode){
            case 1:{//request accepted
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    gpsTracker = new GPSTracker(getActivity());
                    if(gpsTracker.canGetLocation()){
                        start = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                    }
                    else{
                        //permission denied
                    }
                    return;
                }
            }
            //other cases can check for other permissions i might need
        }
    }

    private void placeMarker(LatLng loc, String title){
        gMap.addMarker(new MarkerOptions().position(loc).title(title).icon(BitmapDescriptorFactory.defaultMarker(190)));
    }


    private void addCurrentLocation(){
        placeMarker(new LatLng(currLoc.getLatitude(), currLoc.getLongitude()), "New Loc");
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        gpsTracker.stopUsingGPS();
        addLocButton.setVisible(false);
        saveLocButton.setVisible(false);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu){
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }//*/
    /*public void OnViewCollectionListener(){
        //public void on
    }//*/

    //try using this to get the fragment
    //getFragmentManager().findFragmentById(R.id.google_map_fragment);
    //and then call this function with required landmarks to display data
    public void displayCollection(List<Landmark> newCollection){
        landmarks = newCollection;
        if(gMap != null){
            for (Landmark l: landmarks) {
                showLandmark(l);
            }
        }
    }

    //helper functions==========================
    private void showLandmark(Landmark landmark){
        MarkerOptions opt = new MarkerOptions();
        opt.position(new LatLng(landmark.getLatitude(), landmark.getLongitude()));
        opt.title(landmark.getLandmarkName());
        gMap.addMarker(opt);
    }

    private boolean hasPermission(){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    public  void typePressed(View v){
        int type = gMap.getMapType();
        if(type == GoogleMap.MAP_TYPE_NORMAL){
            gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else if(type == GoogleMap.MAP_TYPE_TERRAIN){
            gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        else if(type == GoogleMap.MAP_TYPE_TERRAIN){
            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    /*public void markPressed(View v){
        Location l = getLocation();
        LatLng coords = new LatLng(l.getLatitude(), l.getLongitude());
        marks.add(new MarkerOptions());
        marks.get(marks.size() - 1).position(coords);
        marks.get(marks.size() - 1).title("Mark " + marks.size());
        gMap.addMarker(marks.get(marks.size() - 1));
    }//*/

    private Location getLocation(){
        LocationRequest lr = new LocationRequest();
        lr.setInterval(0);//0 means do asap
        lr.setFastestInterval(0);
        lr.setNumUpdates(1);//stop after one recieved
        lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return null;
    }

    private void moveToLocation(Location l){
        LatLng coords = new LatLng(l.getLatitude(), l.getLongitude());
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords, 14));
    }

    //code copied from:
    //https://stackoverflow.com/questions/8911356/whats-the-best-practice-to-round-a-float-to-2-decimals
    private static float roundFloat(float d, int decimal){
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimal, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
