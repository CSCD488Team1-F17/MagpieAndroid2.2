package com.magpiehunt.magpie.Adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import com.magpiehunt.magpie.Entities.Collection;
import com.magpiehunt.magpie.R;

import java.util.List;

/**
 * Created by Blake Impecoven on 1/22/18.
*/
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionHolder> {

    private static final String TAG = "CollectionAdapter";
    private final Context context;

    private List<Collection> collectionList;
    private String fragmentTag;
    private SparseBooleanArray expandState;

    public CollectionAdapter(List<Collection> collectionList, String fragmentTag, Context context) {
        this.collectionList = collectionList;
        this.fragmentTag = fragmentTag;
        this.context = context;
        this.expandState = new SparseBooleanArray();
        for (int x = 0; x < collectionList.size(); x++) {
            expandState.append(x, false);
        }//end for
    }//end DVC
    // Create new views (invoked by the layout manager)
    @Override
    public CollectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collection_card, parent, false);

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

        holder.expandArrow.setRotation(expandState.get(position)?180f:0f);
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

        private int position;

        // We may need to add more fields here for expanding of the cards.
        // fields for CardView (Condensed)
        private TextView collectionTitle;
        private TextView collectionAbbreviation;
        private ImageView imgThumb, expandArrow;
        private Collection currentObject;

        // fields for CardView (Expanded)
        ExpandableLinearLayout expandableLinearLayout;
        private TextView description;
        private TextView rating;

        CollectionHolder(View itemView) {
            super(itemView);
            this.collectionTitle = itemView.findViewById(R.id.tvTitle_collection);
            this.collectionAbbreviation = itemView.findViewById(R.id.tvAbbreviation_collection);
            this.imgThumb = itemView.findViewById(R.id.img_thumb_collection);
            this.expandArrow = itemView.findViewById(R.id.expandArrow_collection);

            // expanded views
            expandableLinearLayout = itemView.findViewById(R.id.expandableLayout_collection);
            this.description = itemView.findViewById(R.id.dropdown_description_collection);

        }//end DVC

        void setCondensedData(int position) {
            Collection currentObject = collectionList.get(position);

            this.collectionTitle.setText(currentObject.getName());
            this.collectionAbbreviation.setText(currentObject.getAbbreviation());
            this.imgThumb.setImageResource(R.drawable.magpie_test_cardview_collectionimage);
            // use the following line once images are in the DB. for now, we will use a dummy.
//            this.imgThumb.setImageResource(currentObject.getImage());
//            setListeners(); // uncomment when click functionality implemented.
        }//end setCondensedData

        public void setListeners() {
            expandArrow.setOnClickListener(CollectionHolder.this);
            //TODO: change this listener to respond to a click of the whole card?
            //imgThumb.setOnClickListener(CollectionHolder.this);
        }//end setListeners
        void setExpandedData(int position) {
            Collection currentObject = collectionList.get(position);

            this.description.setText(currentObject.getDescription());
//            this.rating.setText(currentObject.getRating());
        }//end setExpandedData
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.expandArrow_collection:
                    this.expandableLinearLayout.toggle();

                    break;

                case R.id.img_thumb_collection:
                    //TODO: implement opening the collection (view landmarks)
                    break;

                //TODO: implement deletion below.
                //if deletion is to be added
//                case delete item:
//                    removeItem(position);
//                    break;

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

    }//end inner class: CollectionHolder










}//end CollectionAdapter
