package com.sdr.jarjar;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import net.sourceforge.zmanim.ZmanimCalendar;
import net.sourceforge.zmanim.hebrewcalendar.JewishCalendar;
import net.sourceforge.zmanim.util.GeoLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = MainActivity.class.getName();
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildGoogleApiClient();
        mGoogleApiClient.connect();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }




    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Log.d(TAG, "Client Built");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Play services Connected!");

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d(TAG, "Last location - ");
            Log.d(TAG, "Accuracy = "+mLastLocation.getAccuracy());
            Log.d(TAG, "Provider = "+mLastLocation.getProvider());
            Log.d(TAG, "Latitude = " + mLastLocation.getLatitude());
            Log.d(TAG, "Longitude = "+mLastLocation.getLongitude());
        }

        //logLocation(mLastLocation, "from Last Location");


        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Play services Suspend!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Play services Failed!");
    }


    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, createLocationRequest(), this);
    }



    public void logLocation(Location location, final String message){
        new AsyncTask<Location, Void, List<Address>>(){
            @Override
            protected List<Address> doInBackground(Location... params) {
                Geocoder geocoder = new Geocoder(MainActivity.this);
                List<Address> addressList = new ArrayList<Address>();

                try {
                    addressList = geocoder.getFromLocation(params[0].getLatitude(), params[0].getLongitude(), 3);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return addressList;
            }

            @Override
            protected void onPostExecute(List<Address> addressList) {
                super.onPostExecute(addressList);

                Log.d(TAG, message);

                for(Address address: addressList){
                    Log.d(TAG, "Address info line 0- "+address.getAddressLine(0));
                }

            }
        }.execute(location);
    }


    @Override
    public void onLocationChanged(Location location) {
        // Heres where we get the new location
        Log.d(TAG, "New location - ");
        Log.d(TAG, "Accuracy = "+location.getAccuracy());
        Log.d(TAG, "Provider = "+location.getProvider());
        Log.d(TAG, "Latitude = " + location.getLatitude());
        Log.d(TAG, "Longitude = "+location.getLongitude());


        // You can do this if you want
        if (location.getAccuracy() < 30){
            stopLocationUpdates();
        }
    }
}
