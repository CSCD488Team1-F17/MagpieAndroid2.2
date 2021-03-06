package com.magpiehunt.magpie.Adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import com.magpiehunt.magpie.Database.MagpieDatabase;
import com.magpiehunt.magpie.Entities.Collection;
import com.magpiehunt.magpie.Entities.Landmark;
import com.magpiehunt.magpie.Helper.ImageDownloader;
import com.magpiehunt.magpie.R;
import com.magpiehunt.magpie.WebClient.ApiService;
import com.magpiehunt.magpie.WebClient.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by Blake Impecoven on 1/22/18.
 * TODO:
 * - setup listeners for deletion and expansion of the cards.
 * - tap or swipe to delete?
 * - setup expansion functionality of the cards.
 * - make any corresponding changes to collection_cardn_card.xml.
 * - make any corresponding changes to CollectionModel.java.
 * - setup data set for testing of the cards.
 * - setup dummy data set for testing sooner (waiting on room data).
 * - tweak collection_card.xmlrd.xml font/expansion arrow.
 */

public class SearchCollectionAdapter extends RecyclerView.Adapter<SearchCollectionAdapter.CollectionHolder> {

    private static final String TAG = "SearchCollectionAdapter";
    private final Context context;

    private List<Collection> collectionList;
    private SparseBooleanArray expandState;

    public SearchCollectionAdapter(List<Collection> collectionList, Context context) {
        this.collectionList = collectionList;
        this.expandState = new SparseBooleanArray();
        for (int x = 0; x < collectionList.size(); x++) {
            expandState.append(x, false);
        }//end for
        this.context = context;
    }//end DVC

    // Create new views (invoked by the layout manager)
    @Override
    public CollectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create new view
        // this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_collection_card, parent, false);

        return new CollectionHolder(view);
    }//end onCreateViewHolder

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final CollectionHolder holder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        holder.setIsRecyclable(false);
        holder.setCondensedData(position);

        holder.expandableLinearLayout.setInRecyclerView(true);
        holder.expandableLinearLayout.setExpanded(expandState.get(position));
        holder.expandableLinearLayout.setListener(new ExpandableLayoutListenerAdapter() {

            @Override
            public void onPreOpen() {
                changeRotate(holder.expandArrow, 0f, 180f).start();
                expandState.put(position, true);
            }

            @Override
            public void onPreClose() {
                changeRotate(holder.expandArrow, 180f, 0f).start();
                expandState.put(position, false);
            }
        });

        holder.expandArrow.setRotation(expandState.get(position) ? 180f : 0f);
        holder.setListeners();

        holder.setExpandedData(position);
    }//end onBindViewHolder

    private ObjectAnimator changeRotate(ImageView button, float to, float from) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "rotation", to, from);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    public class CollectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "CollectionHolder";
        // fields for CardView (Expanded)
        ExpandableLinearLayout expandableLinearLayout;
        private int position;
        // We may need to add more fields here for expanding of the cards.
        // fields for CardView (Condensed)
        private TextView collectionTitle;
        private TextView collectionAbbreviation;
        private ImageView imgThumb, expandArrow;
        private Collection currentObject;
        private TextView description;
        private TextView rating;
        private Button addCollectionBtn;

        public CollectionHolder(View itemView) {
            super(itemView);

            this.collectionTitle = itemView.findViewById(R.id.tvTitle_search);
            this.collectionAbbreviation = itemView.findViewById(R.id.tvAbbreviation_search);
            this.imgThumb = itemView.findViewById(R.id.img_thumb_search);
            this.expandArrow = itemView.findViewById(R.id.expandArrow_search);

            // expanded views
            this.expandableLinearLayout = itemView.findViewById(R.id.expandableLayout_search);
            this.description = itemView.findViewById(R.id.dropdown_description_search);
            this.addCollectionBtn = itemView.findViewById(R.id.button_addCollection_search);
        }//end DVC

        void setCondensedData(int position) {
            currentObject = collectionList.get(position);

            this.collectionTitle.setText(currentObject.getName());
            this.collectionAbbreviation.setText(currentObject.getAbbreviation());
            this.imgThumb.setImageResource(R.drawable.magpie_test_cardview_collectionimage);
            // use the following line once images are in the DB. for now, we will use a dummy.
//            this.imgThumb.setImageResource(currentObject.getImage());
//            setListeners(); // uncomment when click functionality implemented.
        }//end setCondensedData

        void setExpandedData(int position) {
            Collection currentObject = collectionList.get(position);

            this.description.setText(currentObject.getDescription());
//            this.rating.setText(currentObject.getRating());
        }//end setExpandedData


        public void setListeners() {
            expandArrow.setOnClickListener(CollectionHolder.this);
            addCollectionBtn.setOnClickListener(CollectionHolder.this);
            //TODO: change this listener to respond to a click of the whole card?
            // imgThumb.setOnClickListener(CollectionHolder.this);
            //addBtn.setOnClickListener(CollectionHolder.this);
        }//end setListeners

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.expandArrow_search:
                    this.expandableLinearLayout.toggle();

                    break;

                case R.id.img_thumb_search:
                    //TODO: implement opening the collection (view landmarks)
                    break;

                //TODO: implement deletion below.
//                case delete item:
//                    removeItem(position);
//                    break;
                case R.id.button_addCollection_search:
                    addCollectionToDB(this.currentObject);
                    //     break;
                default:
                    break;
            }//end switch
        }//end onClick

        // will be used at some point.
        //TODO: decide on gesture or button removal.
        public void removeItem(int position) {
            collectionList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, collectionList.size());
        }//end removeItem

        public void addItem(int position, Collection currentObject) {
            collectionList.add(position, currentObject);
            notifyItemInserted(position);
            notifyItemRangeChanged(position, collectionList.size());
        }//end addItem

        private void addCollectionToDB(Collection c) {
            final MagpieDatabase db = MagpieDatabase.getMagpieDatabase(context);
            db.collectionDao().addCollection(c);
            log.d(TAG, c.getName() + " added to MagpieDatabase");
            ApiService apiService = ServiceGenerator.createService(ApiService.class);

            Call<List<Landmark>> call = apiService.getLandmarks(c.getCID());

            call.enqueue(new Callback<List<Landmark>>() {
                @Override
                public void onResponse(Call<List<Landmark>> call, Response<List<Landmark>> response) {
                    List<Landmark> landmarks = response.body();
                    if (landmarks != null) {
                        for (Landmark l : landmarks) {
                            ImageDownloader imageDownloader = new ImageDownloader();
                            Landmark li = imageDownloader.downloadImage(l);
                            db.landmarkDao().addLandmark(li);
                            log.d(TAG, l.getLandmarkName() + " added to MagpieDatabase");

                        }

                    }

                }

                @Override
                public void onFailure(Call<List<Landmark>> call, Throwable t) {
                    log.e(TAG, "Failed call to add landmarks to DB");
                    log.e(TAG, t.getMessage());

                }
            });

        }
    }//end inner class: CollectionHolder


}//end CollectionAdapter
