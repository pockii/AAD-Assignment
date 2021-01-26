package com.example.fitnesstracker.ui.activity;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnesstracker.R;

import static androidx.core.content.ContextCompat.getSystemService;


public class ActivityFragment extends Fragment implements LocationListener, com.google.android.gms.location.LocationListener {

    private static final String TAG = "ActivityFragment";
    private static final int PermissionCode = 58;
    private Activity mActivity;

    Button startRun;
    LocationManager locationManager;

    private ActivityViewModel activityViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        activityViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_activity, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startRun = getActivity().findViewById(R.id.startRun);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        startRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onSuccess" );

                StartRunnable startRunnable = new StartRunnable();
                new Thread(startRunnable).start();
            }

            class StartRunnable implements Runnable {
                @Override
                public void run() {
                    startRun.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG,"onSuccess 2" );

                            if (ContextCompat.checkSelfPermission(getActivity(),
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                            {
                                Log.d(TAG,"ACCESS_FINE_LOCATION granted" );
                            }
                            if (ContextCompat.checkSelfPermission(getActivity(),
                                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                            {
                                Log.d(TAG,"ACCESS_COARSE_LOCATION granted" );
                            }
                            if ( locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                            {
                                Log.d(TAG,"GPS_PROVIDER enabled" );
                            }

                            if (ContextCompat.checkSelfPermission(getActivity(),
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                    && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                Log.d(TAG,"if checkSelfPermission" );

                                startRun_interface();
                            } else {
                                Log.d(TAG,"else LocationRequest" );
                                LocationPermissionRequest();
                            }
                        }
                    });
                }
            }
        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void LocationPermissionRequest() {
        Log.d(TAG,"LocationPermissionRequest function" );

        String[] permissionsA = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION};

        String[] permissionsB = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
        || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
        || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            Log.d(TAG,"shouldShowReq Permission = true" );

            new AlertDialog.Builder(getActivity())
                    .setTitle("Location Permission Required")
                    .setMessage("Please allow permission access to proceed.")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                Log.d(TAG,"IF permissions A" );
                                if (!hasPermissions(getContext(), permissionsA)) {
                                    Log.d(TAG,"IF has permissions A" );
                                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PermissionCode);
                                }
                            }
                            Log.d(TAG,"IF permissions B" );
                            ActivityCompat.requestPermissions(getActivity(), permissionsB, 1);
                        }

                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            Log.d(TAG,"shouldShowReq Permission = false" );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Log.d(TAG,"permissions A" );

                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(getActivity(), permissionsA, PermissionCode);
            }
            Log.d(TAG,"permissions B" );
            ActivityCompat.requestPermissions(getActivity(), permissionsB, PermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"onRequestPermissionsResult function" );
        if (requestCode == PermissionCode) {
            Log.d(TAG,"permission code = 58 " );

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationAlert();
            } else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public void LocationAlert() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getContext());
            dialogbuilder.setMessage(" Enable GPS To Continue")
                    .setPositiveButton("Turn location on", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent call_gps_settings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(call_gps_settings);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog alertDialog = dialogbuilder.create();
            alertDialog.show();

        }
    }

    private void startRun_interface() {
        Intent intent = new Intent(getActivity(), RunInterface.class);
        startActivity(intent);

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}