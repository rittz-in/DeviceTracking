package in.rittz.devicetracking.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import in.rittz.devicetracking.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public  void TrackDevice(View view)
    {
        startActivity(new Intent(HomeActivity.this, ListDeviceActivity.class));
    }

    public  void AddDevice(View view)
    {
        startActivity(new Intent(HomeActivity.this, AddDeviceActivity.class));
    }
}
