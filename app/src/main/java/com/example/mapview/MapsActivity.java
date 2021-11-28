package com.example.mapview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapview.databinding.ActivityMapsBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int flg=1;
    private ActivityMapsBinding binding;
    private static final float INITIAL_ZOOM = 15f;
    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroscopeEventListener;
    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
//    FirebaseAuth auth;
    BottomSheetDialog sheetDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
//        FirebaseApp.initializeApp(this);
//        auth = FirebaseAuth.getInstance();

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sheetDialog = new BottomSheetDialog(MapsActivity.this,R.style.BottomSheetStyle);

        if(gyroscopeSensor == null) {
            Toast.makeText(this, "device has no gyroscope",Toast.LENGTH_SHORT).show();
            finish();
        }

        gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                if(sensorEvent.values[1]>5f) {
//                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);

                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,200);
//                    Toast.makeText(getApplicationContext() , "Pothole Detected", Toast.LENGTH_SHORT).show();
                    if(flg++==1)
                    {

                        View view = LayoutInflater.from(MapsActivity.this).inflate(R.layout.bottomsheet_dialog, (RelativeLayout)findViewById(R.id.sheet));

                        sheetDialog.setContentView(view);
                        sheetDialog.show();
//                        try
//                        {
//                            Thread.sleep(3000);
//                        }
//                        catch(InterruptedException ex)
//                        {
//                            Thread.currentThread().interrupt();
//                        }
//                        sheetDialog.hide();
                    }
                } else if(sensorEvent.values[1]<-5f){
//                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);

                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,200);
//                    Toast.makeText(getApplicationContext() , "Pothole Detected", Toast.LENGTH_SHORT).show();
                    if(flg++==1)
                    {

                        View view = LayoutInflater.from(MapsActivity.this).inflate(R.layout.bottomsheet_dialog, (RelativeLayout)findViewById(R.id.sheet));

                        sheetDialog.setContentView(view);
                        sheetDialog.show();
//                        try
//                        {
//                            Thread.sleep(3000);
//                        }
//                        catch(InterruptedException ex)
//                        {
//                            Thread.currentThread().interrupt();
//                        }
//                        sheetDialog.hide();
                    }


                }
                else{
                    flg=1;
                }
//                try
//                {
//                    Thread.sleep(1000);
//                }
//                catch(InterruptedException ex)
//                {
//                    Thread.currentThread().interrupt();
//                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
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


        // ...

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            // Add a marker in Sydney and move the camera
            LatLng wce = new LatLng(16.845919177815695, 74.6010519249222);
            mMap.addMarker(new MarkerOptions().position(wce).title("Marker in WCE, Sangli"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wce, INITIAL_ZOOM));
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(gyroscopeEventListener);
    }


}