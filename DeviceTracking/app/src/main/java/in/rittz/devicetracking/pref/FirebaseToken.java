package in.rittz.devicetracking.pref;

import com.google.firebase.iid.FirebaseInstanceId;

public class FirebaseToken {

    public String GetToken()
    {
        String stoken = FirebaseInstanceId.getInstance().getToken();
        return stoken;
    }
}
