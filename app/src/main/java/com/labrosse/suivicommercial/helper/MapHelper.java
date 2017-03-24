package com.labrosse.suivicommercial.helper;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.labrosse.suivicommercial.service.response.map.direction.DirectionResults;

import java.util.ArrayList;

/**
 * Created by ahmedhammami on 03/01/2017.
 */

public class MapHelper {
    public void test(DirectionResults directionResults){
        Log.i("zacharia", "inside on success" +directionResults.getRoutes().size());
        ArrayList<LatLng> routelist = new ArrayList<LatLng>();
        if(directionResults.getRoutes().size()>0){
           /* ArrayList<LatLng> decodelist;
            Route routeA = directionResults.getRoutes().get(0);
            Log.i("zacharia", "Legs length : "+routeA.getLegses().size());
            if(routeA.getLegses().size()>0){
                Legs leg = routeA.getLegses().get(0);
                Log.i("zacharia","Steps size :"+steps.size());
                Steps step;
                Location location;
                String polyline;
                for(int i=0 ; i< leg.size();i++){



                    step = steps.get(i);
                    location =step.getStart_location();
                    routelist.add(new LatLng(location.getLat(), location.getLng()));
                    Log.i("zacharia", "Start Location :" + location.getLat() + ", " + location.getLng());
                    polyline = step.getPolyline().getPoints();
                    decodelist = MapHelper.decodePoly(polyline);
                    routelist.addAll(decodelist);
                    location =step.getEnd_location();
                    routelist.add(new LatLng(location.getLat() ,location.getLng()));
                    Log.i("zacharia","End Location :"+location.getLat() +", "+location.getLng());
                }
            }*/
        }
        Log.i("zacharia","routelist size : "+routelist.size());
        if(routelist.size()>0){
            /*PolylineOptions rectLine = new PolylineOptions().width(10).color(
                    Color.RED);

            for (int i = 0; i < routelist.size(); i++) {
                rectLine.add(routelist.get(i));
            }
            // Adding route on the map
            mMap.addPolyline(rectLine);
            markerOptions.position(toPosition);
            markerOptions.draggable(true);
            mMap.addMarker(markerOptions);*/
        }
    }

    public static ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }
}
