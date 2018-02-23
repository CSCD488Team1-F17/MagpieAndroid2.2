package com.magpiehunt.magpie.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CollectionLandmarksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CollectionLandmarksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionLandmarksFragment extends Fragment {

    private static final String TAG = "CollectionLandmarksFragment";

    private static final String CID = "CID";
    private static final String NAME = "Name";

    private int cid;
    private String cName;

    private OnFragmentInteractionListener mListener;
    protected RecyclerView mRecyclerView;
    protected LandmarkAdapter mModelAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    public CollectionLandmarksFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CollectionLandmarksFragment newInstance(int cid, String cName) {
        CollectionLandmarksFragment fragment = new CollectionLandmarksFragment();
        Bundle args = new Bundle();
        args.putString(CID, cid +"");
        args.putString(NAME, cName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cid = Integer.parseInt(getArguments().getString(CID));
            cName = getArguments().getString(NAME);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_collection_landmarks, container, false);

        MagpieDatabase magpieDatabase = MagpieDatabase.getMagpieDatabase(this.getActivity());
        //replace param with cid of collection from bundle
        List<Landmark> landmarks = magpieDatabase.landmarkDao().getLandmarks(this.cid);
        log.d(TAG, String.valueOf(cid));

        Toolbar toolbar = getActivity().findViewById(R.id.my_toolbar);
        toolbar.setTitle(cName);

        mRecyclerView = rootView.findViewById(R.id.landmarksRecyclerView);

        mLayoutManager = new LinearLayoutManager(getActivity());

        setRecyclerViewLayoutManager();

        MagpieDatabase db = MagpieDatabase.getMagpieDatabase(getActivity());
        //List<Collection> collections = db.collectionDao().getCollections();
        mModelAdapter = new LandmarkAdapter(landmarks, CollectionLandmarksFragment.TAG, this.getActivity(), CollectionLandmarksFragment.this);
        // Set the adapter for RecyclerView.
        mRecyclerView.setAdapter(mModelAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }
    public void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
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
        /*if (context instanceof OnFragmentInteractionListener) {
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
    //TODO implement this before release, just for testing

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
