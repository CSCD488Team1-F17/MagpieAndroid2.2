package com.magpiehunt.magpie.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.magpiehunt.magpie.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LandmarkContainerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LandmarkContainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandmarkContainerFragment extends Fragment implements GoogleMapFragment.OnFragmentInteractionListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private BottomNavigationView navBar;
    private FragmentManager fragmentManager;

    private OnFragmentInteractionListener mListener;

    public LandmarkContainerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LandmarkContainerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LandmarkContainerFragment newInstance() {
        LandmarkContainerFragment fragment = new LandmarkContainerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentManager = getChildFragmentManager();
        View view = inflater.inflate(R.layout.fragment_landmark_container, container, false);
        navBar = view.findViewById(R.id.landmark_nav);
        navBar.setSelectedItemId(R.id.nav_menu_list);
        navBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentTransaction replace;
                        Fragment fragment = null;
                        switch (item.getItemId()) {
                            case R.id.nav_menu_list:
                                fragment = null;
                                //fragment = LandmarkContainerFragment.newInstance();//GoogleMapFragment.newInstance();
                                break;
                            case R.id.nav_menu_grid:
                                fragment = null;//QRFragment.newInstance();
                                break;
                            case R.id.nav_menu_map:
                                fragment = GoogleMapFragment.newInstance();
                                break;
                        }
                        changeFragments(fragment);
                        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                            fragmentManager.popBackStack();
                        }
                        //hideBackButton();
                        return true;
                    }
                });
        changeFragments(GoogleMapFragment.newInstance());
        return view;
    }

    public int changeFragments(Fragment frag)
    {
        fragmentManager = getChildFragmentManager();
        return fragmentManager.beginTransaction().replace(R.id.location_fragment_container, frag).commit();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
    }
}
