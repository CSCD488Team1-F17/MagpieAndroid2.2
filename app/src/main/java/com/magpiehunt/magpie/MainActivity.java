package com.magpiehunt.magpie;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.magpiehunt.magpie.Database.MagpieDatabase;
import com.magpiehunt.magpie.Entities.Collection;
import com.magpiehunt.magpie.Entities.Landmark;
import com.magpiehunt.magpie.Fragments.CollectionFragment;
import com.magpiehunt.magpie.Fragments.CollectionLandmarksFragment;
import com.magpiehunt.magpie.Fragments.GoogleMapFragment;
import com.magpiehunt.magpie.Fragments.LandmarkFragment;
import com.magpiehunt.magpie.Fragments.LandmarkListFragment;
import com.magpiehunt.magpie.Fragments.PrizesFragment;
import com.magpiehunt.magpie.Fragments.QRFragment;
import com.magpiehunt.magpie.Fragments.SearchCollectionsFragment;

import org.parceler.Parcels;

import java.util.List;

/**
 * Author:  Blake Impecoven
 * Date:    11/14/17.
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, CollectionFragment.OnCollectionSelectedListener,
        GoogleMapFragment.OnFragmentInteractionListener, QRFragment.OnFragmentInteractionListener, SearchCollectionsFragment.OnFragmentInteractionListener, PrizesFragment.OnFragmentInteractionListener,
        LandmarkListFragment.OnLandmarkSelectedListener, LandmarkFragment.OnLandmarkMapButtonListener {

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 123;
    public Menu optionsMenu;
    protected BottomNavigationView bottomNavigationView;
    /*
     * Firebase/Google instance variables
    **/
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    GoogleApiClient mGoogleApiClient;
    private int requestCode = 0;
    private FragmentManager fragmentManager;
    private Button addCollectionBtn;
    private String mUsername;
    private String mPhotoUrl; // Optional - if we want their photo
    //private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        // Initialize Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final int requestCode = 0;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivityForResult(new Intent(this, SignInActivity.class), requestCode);
        } else {
            // Just thought I'd throw this is in we need it in the future,
            // if not, it isnt hurting anything.
            mUsername = mFirebaseUser.getDisplayName();

            // Optional, will grab their photo url if it exists
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }//end if

            MagpieDatabase db = MagpieDatabase.getMagpieDatabase(this);
            //create collection here
            Collection c = new Collection();
            c.setCID(0);
            c.setAbbreviation("abbr");
            c.setAvailable(1);
            c.setName("test");


            db.collectionDao().addCollection(c);
            //create landmarks for that collection
            Landmark l = new Landmark();
            l.setBadgeID(R.drawable.magpie_test_cardview_collectionimage);
            l.setLandmarkName("test landmark");
            l.setBadgeID(0);
            l.setCID(0);
            db.landmarkDao().addLandmark(l);
        }//end if/else

        // Firebase Database Initialization - Soon to come...
//        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //Create bottom navigation bar to switch between app pages
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        setupFragments();
        this.addCollectionBtn = findViewById(R.id.button_addCollection_collection);
    }//end onCreate
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        fragmentManager = getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount() > 0){
            hideBackButton();
            fragmentManager.popBackStack();
        }
        else
            super.onBackPressed();



       // this.finish();
    }
    private boolean returnBackStackImmediate(FragmentManager fm) {
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
                    if (fragment.getChildFragmentManager().popBackStackImmediate()) {
                        return true;
                    } else {
                        return returnBackStackImmediate(fragment.getChildFragmentManager());
                    }
                }
            }
        }
        return false;
    }
    public void hideBackButton()
    {
        if(fragmentManager.getBackStackEntryCount() == 1 || fragmentManager.getBackStackEntryCount() == 0)
        {
            if (getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowHomeEnabled(false);
            }
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (requestCode == this.requestCode) {
                if (resultCode == RESULT_OK) {
                    // Google Sign-In was successful, authenticate with Firebase
                    mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                } else {
                    // Google Sign-In failed.
                    Log.e(TAG, "Google Sign-In failed.");
                }//if: successful. else: failure.
            }
        }//end if
    }//end

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        optionsMenu = menu;
        menu.findItem(R.id.add_location).setVisible(false);
        menu.findItem(R.id.save_locations).setVisible(false);
        return true;
    }

    /*public void toggleOptionsMenu(int id, boolean enabled){
        if(optionsMenu != null){
            MenuItem item = optionsMenu.findItem(id);
            item.setVisible(enabled);
        }
    }*/

    //TODO use new instance instead of new object
    //this method creates the fragments for each page accessible from the bottom navigation
    // bar and sets up the listener for the navigation bar.
    private void setupFragments() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentTransaction replace;
                        Fragment fragment = null;
                        switch (item.getItemId()) {
                            case R.id.menu_map:
                                fragment = GoogleMapFragment.newInstance();
                                break;
                            case R.id.menu_qr:
                                fragment = QRFragment.newInstance();
                                break;
                            case R.id.menu_home:
                                fragment = CollectionFragment.newInstance();
                                break;
                            case R.id.menu_search:
                                fragment = SearchCollectionsFragment.newInstance();
                                break;
                            case R.id.menu_prizes:
                                fragment = PrizesFragment.newInstance();
                                break;
                        }
                        changeFragments(fragment);
                        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                            fragmentManager.popBackStack();
                        }
                        hideBackButton();
                        return true;
                    }
                });
        changeFragments(CollectionFragment.newInstance());

    }

    public int changeFragments(Fragment frag)
    {
        fragmentManager = getSupportFragmentManager();
        return fragmentManager.beginTransaction().replace(R.id.fragment_container, frag).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                mFirebaseAuth.signOut();
                //Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mFirebaseUser = null;
                mUsername = null;
                mPhotoUrl = null;
                startActivity(new Intent(this, SignInActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }//end switch
    }//end


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        /*
         * An unresolvable error has occurred and Google APIs (including Sign-In) will not
         * be available.
        **/
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    @Override
    public void onCollectionSelected(int cid, String name) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putInt("CID", cid);
        args.putString("Name", name);

        CollectionLandmarksFragment cl = CollectionLandmarksFragment.newInstance(args);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container, cl);
        fragmentTransaction.commit();
    }

    @Override
    public void onLandmarkSelected(Landmark l) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        /*args.putInt("CID", cid);
        args.putInt("LID", lid);
        args.putString("LandmarkName", landmarkName);
        args.putInt("BadgeID", badgeID);
        args.putString("LandmarkDescription", landmarkDescription);
        args.putDouble("Latitude", latitude);
        args.putDouble("Longitude", longitude);
        args.putInt("PicID", picID);
        args.putString("QRCode", qrCode);
        args.putString("Subtitle", subtitle);*/
        args.putParcelable("landmark", Parcels.wrap(l));

        //TODO bundle image and send to frag
        LandmarkFragment fragment = LandmarkFragment.newInstance(args);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void OnLandmarkMapSelected(Landmark landmark) {

    }
   /* @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            mFirebaseAuth.removeAuthStateListener(authListener);
        }
    }*/
}
