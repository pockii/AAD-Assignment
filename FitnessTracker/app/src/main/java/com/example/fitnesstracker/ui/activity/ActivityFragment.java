package com.example.fitnesstracker.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnesstracker.MainActivity;
import com.example.fitnesstracker.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class ActivityFragment extends Fragment implements OnMapReadyCallback {
    final int LOCATION_REQUEST_CODE = 1;
    private static final String TAG = "ActivityFragment";

    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    private Context context;


    private ActivityViewModel activityViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activityViewModel =
                new ViewModelProvider(this).get(ActivityViewModel.class);
        mView = inflater.inflate(R.layout.fragment_activity, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        context = getContext();


        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG,"onViewCreated 1" );
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG,"onViewCreated 2" );
            getLastLocation();
            Log.d(TAG,"onViewCreated 3" );
        } else {
            Log.d(TAG,"onViewCreated 4" );

            askLocationPermission();
        }
        mMapView = mView.findViewById(R.id.google_map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        LatLng singapore = new LatLng(-34, 151);
        mGoogleMap.addMarker(new MarkerOptions().position(singapore).title("Marker is Singapore"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(singapore));
    }


    private void getLastLocation() {
        Log.d(TAG,"GetLastLocation 1" );

        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

                Log.d(TAG,"GetLastLocation 2" );

            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Log.d(TAG,"GetLastLocation 3" );
                    if(location != null) {
                        //we have a location
                        Log.d(TAG,"OnSuccess" + location.toString());
                        Log.d(TAG,"OnSuccess" + location.getLatitude());
                        Log.d(TAG,"OnSuccess" + location.getLongitude());
                    } else {
                        Log.d(TAG,"OnSuccess: Location was null...");
                    }
                }
            });
            locationTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "OnFailure" + e.getLocalizedMessage());
                }
            });
        }



    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "askLocationPermission: you should show an alert dialog...");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission granted
                getLastLocation();
            } else {
                //Permission not granted

            }
        }
    }

}