package in.rittz.devicetracking.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;
import in.rittz.devicetracking.api.ApiClient;
import in.rittz.devicetracking.api.ApiInterface;
import in.rittz.devicetracking.model.Device;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DeviceTrackService extends Service {

   private LocationManager locationManager;
    private String slatitude;
    private String slongitude;
    private String tid;
    private ApiInterface apiInterface;

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public MyLocationListener listener;

    @Override
    public void onCreate() {
        super.onCreate();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "Child Device Tracking Starts", Toast.LENGTH_SHORT).show();
        tid = intent.getStringExtra("tid");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, (LocationListener) listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent start = new Intent(this,DeviceTrackService.class);
        stopService(start);
        locationManager.removeUpdates(listener);
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }
    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc)
        {
            slatitude = String.valueOf(loc.getLatitude());
            slongitude = String.valueOf(loc.getLongitude());
                if(!slatitude.isEmpty()) {
                    SendToParent();
                }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }
    }

    public void SendToParent()
    {
        Call<Device> call = apiInterface.SendDeviceLatLang(slatitude,slongitude,tid);
        call.enqueue(new Callback<Device>() {
                @Override
                public void onResponse(Call<Device> call, Response<Device> response) {

                    if(response.isSuccessful())
                    {
                        onDestroy();
                    }
                    else
                    {
                    }
                }

                @Override
                public void onFailure(Call<Device> call, Throwable t)
                {

                }
            });
    }
}