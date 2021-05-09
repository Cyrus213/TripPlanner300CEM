package com.example.tripplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class AddActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    public static int REQUEST_LOCATION = 1;

    EditText location_input, date_input, time_input;
    Button locate_button,add_button;

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected LocationRequest mLocationRequest;
    protected Geocoder mGeocoder;

    SimpleDateFormat dmy=new SimpleDateFormat("dd-MM-yyyy");

    String date=dmy.format(new java.util.Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        location_input = findViewById(R.id.location_input);
        date_input = findViewById(R.id.date_input);
        time_input = findViewById(R.id.time_input);
        add_button = findViewById(R.id.add_button);
        locate_button = findViewById(R.id.locate_button);

        // GoogleApiClient allows to connect to remote services, the two listeners are the first
        // two interfaces the current class implements
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locate_button.setEnabled(mGoogleApiClient.isConnected());

        // LocationReques sets how often etc the app receives location updates
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call MyDatabaseHelperUse class and insert into database
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addtrip(location_input.getText().toString().trim(),
                        date_input.getText().toString().trim(),
                        time_input.getText().toString().trim());
            }
        });
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {

        // check if the current app has permission to access location of the device
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            // This ACCESS_COARSE_LOCATION corresponds to permission defined in manifest
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_LOCATION);
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            // the last parameter specify the onLocationChanged listener
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback(){
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    mLastLocation = locationResult.getLastLocation();
                }
            }, Looper.myLooper());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    /*
     * This overriding method overrides ActivityCompat.OnRequestPermissionsResultCallback,
     * basically that is a method inherited.
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onConnected(null);
            }
        }
    }

    /*
     * Update UI on location change detected.
     * */
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    /*
     * Manually start/stop GoogleApiClient connection. This is for demo purposes only. In real
     * world case you'll want to start/stop in Activity life cyle callbacks. Take a look in here
     * https://developer.android.com/training/location/retrieve-current.html
     * */
    public void onStartClicked(View v) {
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
            locate_button.setEnabled(true);
        } else {
            mGoogleApiClient.disconnect();
            locate_button.setEnabled(false);
        }
    }

    /*
     * Get the address from the current location, and display back in the app.
     * This is for demo purposes only. In real world case you'll want to implement this in a
     * separate thread so that it won't block your main UI thread.
     * */
    public void onLocateClicked(View v) {
        mGeocoder = new Geocoder(this);
        try {
            // Only 1 address is needed here.
            List<Address> addresses = mGeocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
            if (addresses.size() == 1) {
                String address = addresses.get(0).getAddressLine(0);
                location_input.setText(address);
                time_input.setText(DateFormat.getTimeInstance().format(new Date()));
                date_input.setText(date);
            }
        } catch (Exception e) {
        }
    }
}