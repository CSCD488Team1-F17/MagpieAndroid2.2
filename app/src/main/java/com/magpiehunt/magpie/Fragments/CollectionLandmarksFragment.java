package com.magpiehunt.magpie.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magpiehunt.magpie.Database.MagpieDatabase;
import com.magpiehunt.magpie.R;

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

    private int cid;

    private OnFragmentInteractionListener mListener;

    public CollectionLandmarksFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CollectionLandmarksFragment newInstance(int cid) {
        CollectionLandmarksFragment fragment = new CollectionLandmarksFragment();
        Bundle args = new Bundle();
        args.putInt(CID, cid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cid = Integer.parseInt(getArguments().getString(CID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_collection_landmarks, container, false);

        MagpieDatabase magpieDatabase = MagpieDatabase.getMagpieDatabase(this.getActivity());
        //replace param with cid of collection from bundle
        magpieDatabase.landmarkDao().getLandmarks(this.cid);
        log.d(TAG, String.valueOf(cid));


        return view;
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
