package in.rittz.devicetracking.api;

import java.util.List;

import in.rittz.devicetracking.model.Device;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("add_device.php")
    @FormUrlEncoded
    Call<Device> PostProduct(@Field("name") String name, @Field("token") String token);

    @POST("get_devices.php")
    @FormUrlEncoded
    Call<List<Device>> GetProductList(@Field("token") String token);

    @POST("add_device_track.php")
    @FormUrlEncoded
    Call<Device> AddDeviceTrack(@Field("device_id") String device_id,@Field("device_token") String device_token);

    @POST("send_track_data.php")
    @FormUrlEncoded
    Call<Device> SendDeviceLatLang(@Field("latitude") String latitude,@Field("longitude") String longitude,@Field("rid") String rid);

}
