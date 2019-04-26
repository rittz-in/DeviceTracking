package in.rittz.devicetracking.service;

import android.content.Intent;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.rittz.devicetracking.pref.Constants;
import in.rittz.devicetracking.pref.ToastConfig;

import static in.rittz.devicetracking.pref.Constants.GEOFENCE_ID_RITTZ;

public class FcmMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
   String title = remoteMessage.getNotification().getTitle();
        final String message = remoteMessage.getNotification().getBody();

        if(String.valueOf(title).equals("TRACK_DEVICE"))
        {
            Intent start = new Intent(this,DeviceTrackService.class);
            start.putExtra("tid",message.trim());
            startService(start);
        }
        else if(title.equals("TRACK_DEVICE_STOP"))
        {
            Intent start = new Intent(this,DeviceTrackService.class);
            stopService(start);
        }
        else
            {
                Constants.AREA_LANDMARKS.clear();
                Constants.AREA_LANDMARKS.put(GEOFENCE_ID_RITTZ,new LatLng(Float.parseFloat(title),Float.parseFloat(message)));
        }

    }
}