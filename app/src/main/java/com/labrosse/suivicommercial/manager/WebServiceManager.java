package com.labrosse.suivicommercial.manager;

import android.content.Context;
import android.location.Location;

import com.labrosse.suivicommercial.model.database.SubBPartner;

import java.util.ArrayList;

/**
 * Created by ahmedhammami on 26/03/16.
 */
public class WebServiceManager {

    /**
            * buildDirectionUrl
    * @param context
    * @param mTrafficLocations
    * @param location
    * @return
            */

    public static String buildDirectionUrl(Context context, ArrayList<SubBPartner> subBPartners, Location startLocation) {

        StringBuilder locations = new StringBuilder();
        for (SubBPartner subBPartner : subBPartners) {

            if (!locations.toString().isEmpty()) {
                locations.append("|");
            }

            locations.append(subBPartner);
        }
        String currentLocation = startLocation.getLatitude() + "," + startLocation.getLongitude();

        return ""; //context.getString(R.string.webservice_map_direction_url, currentLocation, locations.toString());
    }


/*
    *//**
     * buildCreateNewLocationUrl
     * @param context
     * @param location
     * @param deviceId
     * @return
     *//*

    public static String buildCreateNewLocationUrl(Context context, Location location, String deviceId) {
        String currentLocation = new StringBuilder().append(location.getLatitude()).append(",").append(location.getLongitude()).toString();
        return context.getString(R.string.webservice_create_new_location_url, currentLocation, deviceId);
    }

    *//**
     * buildDirectionUrl
     * @param context
     * @param mTrafficLocations
     * @param location
     * @return
     *//*

    public static String buildDirectionUrl(Context context, ArrayList<Location> mTrafficLocations, Location location) {

        StringBuilder locations = new StringBuilder();
        for (Location trafficLocation : mTrafficLocations) {

            if (!locations.toString().isEmpty()) {
                locations.append("|");
            }

            locations.append(trafficLocation.getLatitude()).append(",").append(trafficLocation.getLongitude());
        }
        String currentLocation = location.getLatitude() + "," + location.getLongitude();

        return context.getString(R.string.webservice_map_direction_url, currentLocation, locations.toString());
    }

    *//**
     * buildLocationsUrl
     * @param context
     * @param location
     * @param deviceId
     * @return
     *//*

    public static String buildLocationsUrl(Context context, Location location, String deviceId) {
        String currentLocation = new StringBuilder().append(location.getLatitude()).append(",").append(location.getLongitude()).toString();
        return context.getString(R.string.webservice_traffic_locations_url, currentLocation, deviceId);
    }

    *//**
     * buildUpdateLocationUrl
     * @param context
     * @param location
     * @param deviceId
     * @param confirmation
     * @return
     *//*

    public static String buildUpdateLocationUrl(Context context, Location location, String deviceId, int confirmation) {
        return context.getString(R.string.webservice_update_location_url, location.getId(), confirmation, deviceId);
    }

    *//**
     * buildMainConfigurationUrl
     *
     * @param context
     * @return
     *//*

    public static String buildMainConfigurationUrl(Context context) {
        return context.getString(R.string.webservice_config_url, SecurityHelper.generateToken());
    }

    *//**
     * buildContactsUsUrl
     *
     * @param context
     * @return
     *//*

    public static String buildContactsUsUrl(Context context) {
        return context.getString(R.string.webservice_send_message_url, SecurityHelper.generateToken());
    }*/

}
