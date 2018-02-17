package com.magpiehunt.magpie.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.magpiehunt.magpie.R;

/**
 * Created by evan g on 2/14/2018.
 */

public class MapLocationInfoWindow implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public MapLocationInfoWindow(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.map_location_info_window, null);
        TextView title = view.findViewById(R.id.info_window_name);
        TextView distance = view.findViewById(R.id.info_window_distance);
        title.setText(marker.getTitle());
        distance.setText(marker.getSnippet());
        return view;
    }
}
