package in.rittz.devicetracking.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;

import in.rittz.devicetracking.R;
import in.rittz.devicetracking.pref.FirebaseToken;
import in.rittz.devicetracking.pref.ToastConfig;
import in.rittz.devicetracking.api.ApiClient;
import in.rittz.devicetracking.api.ApiInterface;
import in.rittz.devicetracking.model.Device;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDeviceActivity extends AppCompatActivity {

    private EditText editName;
    private String sname,stoken;
    private ApiInterface apiInterface;
    private ToastConfig toastConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        init();

}

    public void init() {
        editName = findViewById(R.id.editName);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        toastConfig = new ToastConfig(this);
    }

    public void AddDevice(View view) {
        sname = editName.getText().toString().trim();

        stoken = FirebaseInstanceId.getInstance().getToken();

        toastConfig.DisplayToast(stoken);

        if (sname.isEmpty()) {
            editName.setError("Enter Device Name");
            return;
        }
        else
        {

            Call<Device> call = apiInterface.PostProduct(sname, stoken);
            call.enqueue(new Callback<Device>() {
                @Override
                public void onResponse(Call<Device> call, Response<Device> response) {

                    if(response.isSuccessful())
                    {
                        toastConfig.DisplayToast(response.body().getMsg());
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

}
