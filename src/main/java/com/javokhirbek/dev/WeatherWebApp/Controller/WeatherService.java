package com.javokhirbek.dev.WeatherWebApp.Controller;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class WeatherService {
    private OkHttpClient client;
    private Response response;
    private String cityName;
    private String unit;

    public JSONObject getWeather() {

        client = new OkHttpClient();
        Request request = new Request.Builder().url("http://api.openweathermap.org/data/2.5/weather?q=" + getCityName() + "&units=" + getUnit() +"&APPID=e7abf6ffc4ec6182f510bfc3ac340ad1")
                .build();
        try {
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray returnWeatherArray() throws JSONException {

        return getWeather().getJSONArray("weather");
    }

    public JSONObject returnMainObject() throws JSONException {

        return getWeather().getJSONObject("main");
    }

    public JSONObject returnWind() throws JSONException {

        return getWeather().getJSONObject("wind");
    }

    public JSONObject returnSunSet() throws JSONException{

        return getWeather().getJSONObject("sys");
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
