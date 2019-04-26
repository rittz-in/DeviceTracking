package in.rittz.devicetracking.pref;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class Constants {

    public static final String FIREBASE_TOKEN = "";
    public static final String GEOFENCE_ID_RITTZ = "Rittz1";
    public static final float GEOFENCE_RADIUS_IN_METERS = 10;

    public static final HashMap<String, LatLng> AREA_LANDMARKS = new HashMap<String, LatLng>();

    static
    {
        // child device  location
     //   AREA_LANDMARKS.put(GEOFENCE_ID_RITTZ, new LatLng(12.9134799, 77.6118585));
    }
}
