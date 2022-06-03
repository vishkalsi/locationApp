package com.example.mapapplicationdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapapplicationdemo.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        TextView address =  findViewById(R.id.address);
        TextView latitude =  findViewById(R.id.latitude);
        TextView longitude =  findViewById(R.id.longitude);
        TextView accuracy =  findViewById(R.id.acuracy);
        TextView altitude =  findViewById(R.id.altitude);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        LocationRequest locationRequest = LocationRequest.create().setFastestInterval(0).setInterval(0).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    fusedLocationClient.removeLocationUpdates(this);
                    Location location = locationResult.getLocations().get(0);
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(mContext, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String userAddress = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        address.setText(userAddress);
                        latitude.setText(location.getLatitude()+"");
                        longitude.setText(location.getLongitude()+"");
                        altitude.setText(location.getAltitude()+"");
                        accuracy.setText(location.getAccuracy()+"");
                        Log.d("LOGGER", userAddress+" /n "+location.getLatitude()+""+location.getLongitude()+" /n"
                        +location.getAltitude()+"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


//                    listener.onLocationChanged(locationResult.getLocations().get(0));
                } else {
//                    listener.onLocationChanged(new Location(""));
                }

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(mContext, "Please provide location permission to this app from device app settings", Toast.LENGTH_SHORT).show();
            return;
        } else {
            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
            );
        }


    }

}