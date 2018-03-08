package com.magpiehunt.magpie.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magpiehunt.magpie.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CollectionLandmarksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CollectionLandmarksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionLandmarksFragment extends Fragment {
    private static final java.lang.String NAME = "Name";
    private static final java.lang.String CID = "CID";
    private int cid;
    private String cName;
    private OnFragmentInteractionListener mListener;
    private FragmentManager fragmentManager;
    private TabLayout tabBar;

    public CollectionLandmarksFragment() {}

    public static CollectionLandmarksFragment newInstance(){
        return new CollectionLandmarksFragment();
    }

    public static CollectionLandmarksFragment newInstance(Bundle args) {
        CollectionLandmarksFragment fragment = new CollectionLandmarksFragment();
       // args.putString();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cid = getArguments().getInt(CID);
            cName = getArguments().getString(NAME);
        }

        //FragmentManager fragmentManager = getChildFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putInt("CID", cid);
        args.putString("Name", cName);
        //LandmarkListFragment cl = LandmarkListFragment.newInstance(args);
        //fragmentTransaction.replace(R.id.collectionLandmarksContainer, cl).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentManager = getChildFragmentManager();
        View view = inflater.inflate(R.layout.fragment_collection_landmarks, container, false);
        tabBar = view.findViewById(R.id.tabLayout);
        TabLayout.Tab tab = tabBar.getTabAt(0);
        tab.select();
        tabBar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = LandmarkListFragment.newInstance(new Bundle());
                        //fragment = LandmarkContainerFragment.newInstance();//GoogleMapFragment.newInstance();
                        break;
                    case 1:
                        fragment = GoogleMapFragment.newInstance();
                        break;
                }
                changeFragments(fragment);
                for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                    fragmentManager.popBackStack();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        changeFragments(LandmarkListFragment.newInstance(new Bundle()));
        return view;
    }

    public int changeFragments(Fragment frag)
    {
        fragmentManager = getChildFragmentManager();
        return fragmentManager.beginTransaction().replace(R.id.collectionLandmarksContainer, frag).commit();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
        void onFragmentInteraction(Uri uri);
    }
}
