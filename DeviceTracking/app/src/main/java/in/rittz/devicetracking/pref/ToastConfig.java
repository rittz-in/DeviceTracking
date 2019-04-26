package in.rittz.devicetracking.pref;

import android.content.Context;
import android.widget.Toast;

public class ToastConfig {

    private Context context;
    public ToastConfig(Context context)
    {
        this.context = context;
    }

    public void DisplayToast(String msg)
    {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
