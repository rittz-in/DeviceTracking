package in.rittz.devicetracking.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import in.rittz.devicetracking.model.Device;
import in.rittz.devicetracking.adapter.ListViewAdapter;
import in.rittz.devicetracking.R;
import in.rittz.devicetracking.pref.FirebaseToken;
import in.rittz.devicetracking.pref.ToastConfig;
import in.rittz.devicetracking.api.ApiClient;
import in.rittz.devicetracking.api.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDeviceActivity extends AppCompatActivity {

    private RecyclerView listDevices;
    private ArrayList<Device> AllDevices;
    private ApiInterface apiInterface;
    private ListViewAdapter adapter;
    private ToastConfig toastConfig;
    private String stoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_device);

        init();
    }

    public void init()
    {
        listDevices = findViewById(R.id.listView);
        listDevices.setHasFixedSize(true);

        listDevices.setLayoutManager(new LinearLayoutManager(this));

         AllDevices= new ArrayList<Device>();

        adapter = new ListViewAdapter(this,AllDevices);
        listDevices.setAdapter(adapter);

        apiInterface =  ApiClient.getApiClient().create(ApiInterface.class);

        stoken = FirebaseInstanceId.getInstance().getToken();
        toastConfig = new ToastConfig(this);

        if(!stoken.isEmpty())
        {
            GetDeviceList();
        }
    }

    public void GetDeviceList()
    {
        Call<List<Device>> call = apiInterface.GetProductList(stoken);

        call.enqueue(new Callback<List<Device>>() {
            @Override
            public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {

                if(response.isSuccessful()) {
                    AllDevices.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    toastConfig.DisplayToast("No Device Available");
                }
            }

            @Override
            public void onFailure(Call<List<Device>> call, Throwable t) {
                toastConfig.DisplayToast("Server Connection Error");
            }
        });
    }
}
