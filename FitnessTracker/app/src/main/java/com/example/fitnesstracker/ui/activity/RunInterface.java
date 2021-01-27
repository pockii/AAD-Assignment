package com.example.fitnesstracker.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesstracker.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;

public class RunInterface extends AppCompatActivity implements LocationListener, com.google.android.gms.location.LocationListener {

    FusedLocationProviderClient fusionProvider;
    LocationManager locationManager;
    LocationRequest locationRequest;
    Location start_location, end_location, curr_location;

    TextView distance_counter, SpdInmph, SpdInkmh, CountDownTimerView;
    Button play_button, pause_button, stop_btn;
    ImageView overlayScreen;

    Chronometer timer;
    CountDownTimer countDownTimer;

    static final long StartTime = 11000;
    long TimeLeft = StartTime;

    boolean active;
    long update;
    double distance = 0;
    double current_speed;

    private static final int AccessCode = 48;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_interface);
        distance_counter = findViewById(R.id.distance_counter);
        play_button = findViewById(R.id.play_button);
        pause_button = findViewById(R.id.pause_button);
        stop_btn = findViewById(R.id.stop_btn);
        SpdInmph = findViewById(R.id.spdInmph);
        SpdInkmh = findViewById(R.id.speedInkmh);
        CountDownTimerView = findViewById(R.id.CountDownTimerView);
        timer = findViewById(R.id.timer);
        overlayScreen = findViewById(R.id.overlayScreen);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        fusionProvider = LocationServices.getFusedLocationProviderClient(RunInterface.this);

        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - timer.getBase()) >= 86400000) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                }
            }
        });
        play_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ResumeRunnable resumeRunnable = new ResumeRunnable();
                new Thread(resumeRunnable).start();
            }


            class ResumeRunnable implements Runnable {
                @Override
                public void run() {
                    play_button.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!active) {
                                if (ContextCompat.checkSelfPermission(RunInterface.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                        && ContextCompat.checkSelfPermission(RunInterface.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                        && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, RunInterface.this);
                                    createLocationRequest();
                                    timer.setBase(SystemClock.elapsedRealtime() - update);
                                    timer.start();
                                    play_button.setVisibility(View.GONE);
                                    pause_button.setVisibility(View.VISIBLE);
                                    active = true;
                                } else {
                                    RequestPermissions();
                                    {
                                    }
                                    play_button.setVisibility(View.VISIBLE);
                                    active = false;
                                }
                            }
                        }
                    });
                    pause_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PauseRunnable pauseRunnable = new PauseRunnable();
                            new Thread(pauseRunnable).start();
                        }

                        class PauseRunnable implements Runnable {
                            @Override
                            public void run() {
                                pause_button.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (active) {
                                            timer.stop();
                                            active = false;
                                            play_button.setVisibility(View.VISIBLE);
                                            pause_button.setVisibility(View.GONE);
                                            update = SystemClock.elapsedRealtime() - timer.getBase();
                                            locationManager.removeUpdates(RunInterface.this);

                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }


    private void RequestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(RunInterface.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(this)
                    .setTitle("Location Permission Required")
                    .setMessage("Please allow permission access to proceed.")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                ActivityCompat.requestPermissions(RunInterface.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                                }, AccessCode);
                            }
                            ActivityCompat.requestPermissions(RunInterface.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION,
                                            }, AccessCode);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                }, AccessCode);
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            }, AccessCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AccessCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationCall();

            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void LocationCall() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
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