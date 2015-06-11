package com.mycompany.foo;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity {
    private BusinessActivity bs;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final float DEFAULTZOOM = 15;
    JSONArray businesses = null;
    String message;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> busList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        try {
            setUpMapIfNeeded();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            setUpMapIfNeeded();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() throws IOException {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /* This is the code that will be ran when we are marking a business
        location on the map. the function takes a string as an input,
        which is the address. We can also expand to have the function
        take more inputs so we can add a brief description at the top
        of the marker and display even more when the user clicks on
        the marker.
     */


    public void gotoLocation(double lat,double lng,float zoom){
        LatLng ll = new LatLng(lat,lng);

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title("This is a test"));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,zoom);
        mMap.moveCamera(update);

    }
    public void getLocate(String location) throws IOException {

        Geocoder gc = new Geocoder(this);

        List<Address> list = gc.getFromLocationName(location, 5);
        Address add = list.get(0);
        String locality = add.getLocality();

        double lat = add.getLatitude();
        double lng = add.getLongitude();

        gotoLocation(lat, lng, DEFAULTZOOM);
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

    public ArrayList<HashMap<String, String>> getAddresses( Intent intent){


        return busList;
    }


    private void setUpMap() throws IOException {
        ArrayList<String> address = new ArrayList<String>();
        address.add("97734");
        address.add("Corvallis, OR 97331");
        address.add("Eugene, OR");
        mMap.setMyLocationEnabled(true);
        for(String s : address){
            getLocate(s);

        }
        //mMap.addMarker(new MarkerOptions().position(new LatLng(44.5646, 123.2757)).title("Marker"));
    }

}
