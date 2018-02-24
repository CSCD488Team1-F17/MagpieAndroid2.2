package com.magpiehunt.magpie.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magpiehunt.magpie.Database.MagpieDatabase;
import com.magpiehunt.magpie.Entities.Landmark;
import com.magpiehunt.magpie.Fragments.CollectionLandmarksFragment;
import com.magpiehunt.magpie.Fragments.LandmarkFragment;
import com.magpiehunt.magpie.R;

import java.util.List;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by Blake Impecoven on 1/26/18.
 */

public class LandmarkAdapter extends RecyclerView.Adapter<LandmarkAdapter.LandmarkHolder> {

    private static final String TAG = "LandmarkAdapter";
    private final String fragmentTag;
    private final Context context;
    private final CollectionLandmarksFragment fragment;

    public List<Landmark> landmarkList;
    protected MagpieDatabase magpieDatabase;

    public LandmarkAdapter(List<Landmark> landmarkList, String tag, Context context, CollectionLandmarksFragment fragment) {
        this.landmarkList = landmarkList;
        this.fragmentTag = tag;
        this.context = context;
        this.fragment = fragment;

    }//end DVC

    @Override
    public LandmarkAdapter.LandmarkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.landmark_card, parent, false);

        return new LandmarkHolder(view);
    }//end onCreateViewHolder

    @Override
    public void onBindViewHolder(LandmarkHolder holder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        holder.setData(landmarkList.get(position), position);
    }//end onBindViewHolder

    @Override
    public int getItemCount() {
        return landmarkList.size();
    }//end getItemCount

    public class LandmarkHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "LandmarkHolder";

        private int position;

        // fields for CardView (While Condensed)
        private TextView landmarkSponsor, landmarkName;
        private TextView landmarkMiles, landmarkTime;
        private ImageView landmarkImage;
        private Landmark currentObject;

        // fields for CardView (While Expanded)
        private TextView description;
        private LinearLayout card;

        public LandmarkHolder(View itemView) {
            super(itemView);
            // attach to landmark_card.xml items
            this.landmarkSponsor = itemView.findViewById(R.id.landmarkSponsor);
            this.landmarkName = itemView.findViewById(R.id.landmarkName);
            this.landmarkMiles = itemView.findViewById(R.id.landmarkMiles);
            this.landmarkTime = itemView.findViewById(R.id.landmarkTime);
            this.landmarkImage = itemView.findViewById(R.id.landmarkImage);
            this.card = itemView.findViewById(R.id.landmarkCard);
            setListeners();
        }//end EVC

        public void setData(Landmark currentObject, int position) {
            // attach class fields to their respective items on landmark_card.xml

            landmarkSponsor.setText("Temporary Sponsor"); // may need to move sponsor to landmark to display on card
            landmarkName.setText(currentObject.getLandmarkName());
            landmarkMiles.setText(".84"); // need to get distance calculated
            landmarkTime.setText("14"); // need to get time using lat and long from google services and their estimated time
            landmarkImage.setImageResource(R.drawable.magpie_test_cardview_collectionimage); // replace once we find out how to deal w/ images

            this.currentObject = currentObject;
            this.position = position;
        }//end setData

        public void setListeners() {
            // set listeners for items to be implemented with onClick functionality
            this.card.setOnClickListener(LandmarkHolder.this);
            // TODO: set listeners

        }//end setListeners

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                // TODO: implement click functionality
                case R.id.landmarkCard:
                    log.d(TAG, "LandmarkClick: " + currentObject.getLandmarkName());
                    startLandmark();

                    break;
                default:
                    break;
            }//end switch
        }// end onClick

        private void startLandmark() {
            FragmentManager fragmentManager = fragment.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            LandmarkFragment fragment = LandmarkFragment.newInstance(currentObject.getCID(), currentObject.getLID(), currentObject.getLandmarkName(), currentObject.getBadgeID(), currentObject.getLandmarkDescription(), currentObject.getLatitude(),
                    currentObject.getLongitude(), currentObject.getPicID(), currentObject.getQRCode());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        }

        // will be used at some point.
        //TODO: decide on gesture or button removal.
        public void removeItem(int position) {
            landmarkList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, landmarkList.size());
        }// end removeItem

        public void addItem(int position, Landmark currentObject) {
            landmarkList.add(position, currentObject);
            notifyItemInserted(position);
            notifyItemRangeChanged(position, landmarkList.size());
        }// end addItem
    }//end inner class: LandmarkHolder
}//end LandmarkAdapter
