package br.borbi.ots;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import br.borbi.ots.pojo.CityResultSearch;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String INDEX_LIST_CITIES = "listCities";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Intent mMIntent = getIntent();

        ArrayList<CityResultSearch> mCities = (ArrayList<CityResultSearch>) mMIntent.getSerializableExtra(INDEX_LIST_CITIES);

        //Parameters that will be used to determine the bounds for zooming in;
        Double smallestLatitude = null;
        Double smallestLongitude = null;
        Double biggestLatitude = null;
        Double biggestLongitude = null;

        for (CityResultSearch cityResultSearch : mCities) {
            // Add a marker in Sydney and move the camera
            LatLng city = new LatLng(cityResultSearch.getCity().getLatitude(), cityResultSearch.getCity().getLongitude());

            if (cityResultSearch.isFirstCity()) {
                googleMap.addMarker(new MarkerOptions()
                        .position(city)
                        .title(cityResultSearch.getCity().getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_36dp)));
            } else {
                googleMap.addMarker(new MarkerOptions()
                        .position(city)
                        .title(cityResultSearch.getCity().getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_blue_36dp)));
            }


            if (smallestLatitude == null) {
                smallestLatitude = cityResultSearch.getCity().getLatitude();
                biggestLatitude = cityResultSearch.getCity().getLatitude();
                smallestLongitude = cityResultSearch.getCity().getLongitude();
                biggestLongitude = cityResultSearch.getCity().getLongitude();
            } else {
                if (smallestLatitude > cityResultSearch.getCity().getLatitude()) {
                    smallestLatitude = cityResultSearch.getCity().getLatitude();
                } else if (biggestLatitude < cityResultSearch.getCity().getLatitude()) {
                    biggestLatitude = cityResultSearch.getCity().getLatitude();
                }

                if (smallestLongitude > cityResultSearch.getCity().getLongitude()) {
                    smallestLongitude = cityResultSearch.getCity().getLongitude();
                } else if (biggestLongitude < cityResultSearch.getCity().getLongitude()) {
                    biggestLongitude = cityResultSearch.getCity().getLongitude();
                }
            }
        }

        // Gets screen size
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(smallestLatitude, smallestLongitude), new LatLng(biggestLatitude, biggestLongitude));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, width, height, getResources().getInteger(R.integer.map_padding)));
    }
}
