package in.rittz.devicetracking.model;

import com.google.gson.annotations.SerializedName;

public class Device {

    @SerializedName("response")
    private String Response;

    @SerializedName("msg")
    private String Msg;

    @SerializedName("name")
    private String Name;

    @SerializedName("device_id")
    private String Device_ID;

    @SerializedName("token")
    private String Token;

    @SerializedName("latitude")
    private String Latitude;

    @SerializedName("longitude")
    private String Longitude;

    public String getResponse() {
        return Response;
    }

    public Device(String response, String msg) {
        Response = response;
        Msg = msg;
    }
    public Device() {
      }


    public void setResponse(String response) {
        Response = response;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDevice_ID() {
        return Device_ID;
    }

    public void setDevice_ID(String device_ID) {
        Device_ID = device_ID;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
