package com.magpiehunt.magpie.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magpiehunt.magpie.Adapters.LandmarkAdapter;
import com.magpiehunt.magpie.Database.MagpieDatabase;
import com.magpiehunt.magpie.Entities.Landmark;
import com.magpiehunt.magpie.R;

import java.util.List;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * This fragment displays a list (RecyclerView) of landmarks in the selected collection. Landmarks are retrieved
 * from the Room database using Collection CID
 * Nested fragment inside of CollectionLandmarksFragment
 * RecyclerView adapter for this fragment is LandmarkAdapter
 */
public class LandmarkListFragment extends Fragment {

    private static final String TAG = "LandmarkListFragment";

    private static final String CID = "CID";
    private static final String NAME = "Name";
    protected RecyclerView mRecyclerView;
    protected LandmarkAdapter mModelAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private int cid;
    private String cName;
    private OnLandmarkSelectedListener mListener;

    public LandmarkListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LandmarkListFragment newInstance(Bundle args) {
        LandmarkListFragment fragment = new LandmarkListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cid = getArguments().getInt(CID);
            cName = getArguments().getString(NAME);
            log.d(TAG, "Landmarks created");

        }
        log.d(TAG, "Landmarks created without args");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_landmark_list, container, false);

        MagpieDatabase magpieDatabase = MagpieDatabase.getMagpieDatabase(this.getActivity());
        //replace param with cid of collection from bundle
        List<Landmark> landmarks = magpieDatabase.landmarkDao().getLandmarks(this.cid);
        log.d(TAG, String.valueOf(cid));

        Toolbar toolbar = getActivity().findViewById(R.id.my_toolbar);
        toolbar.setTitle(cName);

        //TODO fix this back button nonsense in all fragments
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mRecyclerView = rootView.findViewById(R.id.landmarksRecyclerView);

        mLayoutManager = new LinearLayoutManager(getActivity());

        setRecyclerViewLayoutManager();

        MagpieDatabase db = MagpieDatabase.getMagpieDatabase(getActivity());
        //List<Collection> collections = db.collectionDao().getCollections();
        mModelAdapter = new LandmarkAdapter(landmarks, LandmarkListFragment.TAG, this.getActivity(), LandmarkListFragment.this, mListener);
        // Set the adapter for RecyclerView.
        mRecyclerView.setAdapter(mModelAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }

    public void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        RecyclerView.LayoutManager mana = mRecyclerView.getLayoutManager();
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }//end if

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }//end

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
       /* if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //TODO implement this before release, just for testing
        if (context instanceof OnLandmarkSelectedListener) {
            mListener = (OnLandmarkSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLandmarkSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    //TODO implement this before release, just for testing
    public interface OnLandmarkSelectedListener {
        // TODO: Update argument type and name
        void onLandmarkSelected(Landmark l);
    }
}
