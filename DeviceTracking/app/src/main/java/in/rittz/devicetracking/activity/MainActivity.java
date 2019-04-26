package in.rittz.devicetracking.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import in.rittz.devicetracking.api.ApiClient;
import in.rittz.devicetracking.api.ApiInterface;
import in.rittz.devicetracking.model.Device;
import in.rittz.devicetracking.pref.Constants;
import in.rittz.devicetracking.R;
import in.rittz.devicetracking.pref.ToastConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_LOCATION_PERMISSION_CODE = 101;

    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;

    private MarkerOptions markerOptions;
    private Marker currentLocationMarker;
    private Circle circle;
    private String sname,sid;

    private TextView textStatus;
    private ApiInterface apiInterface;
    private ToastConfig toastConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    public void init()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION_CODE);
        }

        textStatus = findViewById(R.id.textStatus);



        sname  = getIntent().getExtras().getString("device_name","Child Device");
        sid  = getIntent().getExtras().getString("device_id","");

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        toastConfig = new ToastConfig(this);

        SendFirebase();
    }

    public void SendFirebase()
    {
        if(!sid.isEmpty())
        {
            Call<Device> call = apiInterface.AddDeviceTrack(sid, FirebaseInstanceId.getInstance().getToken());
            call.enqueue(new Callback<Device>() {
                @Override
                public void onResponse(Call<Device> call, Response<Device> response) {

                    if(response.isSuccessful())
                    {
                        //    toastConfig.DisplayToast(response.body().getMsg());
                    }
                    else
                    {
                        toastConfig.DisplayToast("Server Connection Error");
                    }
                }

                @Override
                public void onFailure(Call<Device> call, Throwable t) {

                    toastConfig.DisplayToast("Server Connection Error"+t.getMessage());
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        this.googleMap = googleMap;
        if(Constants.AREA_LANDMARKS.get(Constants.GEOFENCE_ID_RITTZ) != null)
        {
            LatLng latLng = Constants.AREA_LANDMARKS.get(Constants.GEOFENCE_ID_RITTZ);
            googleMap.addMarker(new MarkerOptions().position(latLng).title(sname+" Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f));
        }
        googleMap.setMyLocationEnabled(true);

        }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationMonitor();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void startLocationMonitor() {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(15000)
                .setFastestInterval(10000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    if (currentLocationMarker != null) {
                        currentLocationMarker.remove();
                    }
                    googleMap.clear();
                    markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
                    markerOptions.title("Your Location");
                    markerOptions.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    currentLocationMarker = googleMap.addMarker(markerOptions);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17f));

                    if(circle != null)
                    {
                        circle.remove();
                    }
                    circle = googleMap.addCircle(new CircleOptions()
                            .center(new LatLng(location.getLatitude(), location.getLongitude()))
                            .radius(Constants.GEOFENCE_RADIUS_IN_METERS)
                            .strokeColor(Color.RED)
                            .strokeWidth(4f));

                    float[] distance = new float[2];

                    if(Constants.AREA_LANDMARKS.get(Constants.GEOFENCE_ID_RITTZ) != null)
                    {
                        LatLng latLng = Constants.AREA_LANDMARKS.get(Constants.GEOFENCE_ID_RITTZ);
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(sname+" Location"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f));

                        Location.distanceBetween( latLng.latitude, latLng.longitude ,location.getLatitude(),location.getLongitude(),distance );

                        if( distance[0] > Constants.GEOFENCE_RADIUS_IN_METERS  ){
                            textStatus.setText( sname+ " is Outside the radius");
                            textStatus.setTextColor(Color.parseColor("#f91d33"));
                        } else {
                            textStatus.setText( sname+ " is Inside the radius");
                            textStatus.setTextColor(Color.parseColor("#46993e"));

                        }
                            SendFirebase();
                    }
                }
            });
        } catch (SecurityException e) {
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.reconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        int response = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (response != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, response, 1).show();
        } else {
        }
    }

}
