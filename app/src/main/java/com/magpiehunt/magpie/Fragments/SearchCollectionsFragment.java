package com.magpiehunt.magpie.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magpiehunt.magpie.Adapters.SearchCollectionAdapter;
import com.magpiehunt.magpie.Entities.Collection;
import com.magpiehunt.magpie.R;
import com.magpiehunt.magpie.WebClient.ApiService;
import com.magpiehunt.magpie.WebClient.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchCollectionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchCollectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchCollectionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "SearchCollectionsFrag";


    protected RecyclerView mRecyclerView;
    protected SearchCollectionAdapter mModelAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<Collection> collections;
    protected Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchCollectionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchCollectionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchCollectionsFragment newInstance() {
        SearchCollectionsFragment fragment = new SearchCollectionsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.context = this.getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_search_collections, container, false);

        //Toolbar myToolbar = rootView.findViewById(R.id.my_toolbar);
        //myToolbar.setTitle("Add a Collection");

        Toolbar toolbar = getActivity().findViewById(R.id.my_toolbar);
        toolbar.setTitle("Add a Collection");

        //init landmark_list_green
        mRecyclerView = rootView.findViewById(R.id.searchView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        setRecyclerViewLayoutManager();

        //get data from api
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<List<Collection>> call = apiService.getCollections();
        call.enqueue(new Callback<List<Collection>>() {
            @Override
            public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                collections = response.body();
                // collections = callCollections;
                mModelAdapter = new SearchCollectionAdapter(collections, context);
                // Set the adapter for RecyclerView.
                mRecyclerView.setAdapter(mModelAdapter);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            }

            @Override
            public void onFailure(Call<List<Collection>> call, Throwable t) {
                Log.e(TAG, "Call failed");

            }
        });

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
