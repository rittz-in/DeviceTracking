package in.rittz.devicetracking.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.rittz.devicetracking.R;
import in.rittz.devicetracking.activity.MainActivity;
import in.rittz.devicetracking.pref.ToastConfig;
import in.rittz.devicetracking.model.Device;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.MyViewHolder> {

    private Context context;
    private List<Device> deviceList;
    public ListViewAdapter(Context context, List<Device> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_item_list,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final Device device = deviceList.get(i);

        myViewHolder.textName.setText(device.getName());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastConfig toastConfig = new ToastConfig(context);

                Intent track = new Intent(context, MainActivity.class);
                track.putExtra("device_name",device.getName());
                track.putExtra("device_id",device.getDevice_ID());
                context.startActivity(track);
                ((Activity)context).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView textName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textDName);
        }
    }
}
