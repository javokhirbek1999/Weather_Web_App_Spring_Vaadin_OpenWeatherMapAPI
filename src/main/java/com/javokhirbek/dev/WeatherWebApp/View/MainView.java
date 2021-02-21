package com.javokhirbek.dev.WeatherWebApp.View;

import com.javokhirbek.dev.WeatherWebApp.Controller.WeatherService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@SpringUI(path = "")
public class MainView extends UI {

    private VerticalLayout mainLayout;
    private NativeSelect<String> unitSelect;
    private TextField cityTextField;
    private Button showWeatherButton;
    private Label currentLocationTitle;
    private Label currentTemp;
    private Label weatherDescription;
    private Label weatherMin;
    private Label weatherMax;
    private Label pressureLabel;
    private Label humidityLabel;
    private Label windSpeedLabel;
    private Label windDegreeLabel;
    private Label sunRiseLabel;
    private Label sunSetLabel;
    private ExternalResource img;
    private Image iconImage;
    private HorizontalLayout dashBoard;
    private VerticalLayout descriptionLayout;
    private HorizontalLayout mainDescriptionLayout;
    private VerticalLayout pressureLayout;

    @Autowired
    private WeatherService weatherService = new WeatherService();


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setLayout();
        setHeader();
        setLogo();
        setUpForm();
        dashBoardTitle();

        showWeatherButton.addClickListener(event -> {
            if (!cityTextField.getValue().equals("")) {
                try {
                    dashBoardDescription();
                    updateUI();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Notification.show("Please enter a city");
            }
        });
    }

    public void setLayout() {

        iconImage = new Image();
        weatherDescription = new Label("Description: Clear Skies");
        weatherMin = new Label("Min: 56F");
        weatherMax = new Label("Max: 89F");
        pressureLabel = new Label("Pressure: 123pa");
        humidityLabel = new Label("Humidity: 98");
        windSpeedLabel = new Label("Wind Speed: 123/hr");
        windDegreeLabel = new Label("");
        sunRiseLabel = new Label("Sunrise: ");
        sunSetLabel = new Label("Sunset: ");


        mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        setContent(mainLayout);
    }

    private void setHeader() {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Label title = new Label("Weather");
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_BOLD);
        title.addStyleName(ValoTheme.LABEL_COLORED);

        headerLayout.addComponents(title);

        mainLayout.addComponents(headerLayout);
    }

    private void setLogo() {
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Image logo = new Image(null, new ClassResource("/weather_logo.jpg"));
        logo.setWidth("150px");
        logo.setHeight("125px");

        logoLayout.addComponents(logo);

        mainLayout.addComponents(logoLayout);
    }

    private void setUpForm() {
        HorizontalLayout formLayout = new HorizontalLayout();
//        formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        formLayout.setSpacing(true);
        formLayout.setMargin(true);

        // Create the selection component
        unitSelect = new NativeSelect<>();
        unitSelect.setWidth("40px");
        ArrayList<String> items = new ArrayList<>();
        items.add("C");
        items.add("F");

        unitSelect.setItems(items);
        unitSelect.setValue(items.get(1));

        formLayout.addComponents(unitSelect);


        //Select City
        cityTextField = new TextField();
        cityTextField.setWidth("80%");
        formLayout.addComponents(cityTextField);


        //Button
        showWeatherButton = new Button();
        showWeatherButton.setIcon(VaadinIcons.SEARCH);

        formLayout.addComponents(showWeatherButton);

        mainLayout.addComponents(formLayout);
    }

    private void dashBoardTitle() {
        dashBoard = new HorizontalLayout();
        dashBoard.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        currentLocationTitle = new Label("San Francisco");
        currentLocationTitle.addStyleName(ValoTheme.LABEL_H2);
        currentLocationTitle.addStyleName(ValoTheme.LABEL_LIGHT);

        // Current Temperature
        currentTemp = new Label("19F");
        currentTemp.addStyleName(ValoTheme.LABEL_BOLD);
        currentTemp.addStyleName(ValoTheme.LABEL_H1);
        currentTemp.addStyleName(ValoTheme.LABEL_LIGHT);

    }

    private void dashBoardDescription() {
        mainDescriptionLayout = new HorizontalLayout();
        mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        // Description Vertical Layout
        descriptionLayout = new VerticalLayout();
        descriptionLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

//        weatherDescription = new Label("Description: Clear Skies");
        descriptionLayout.addComponents(weatherDescription);

        descriptionLayout.addComponents(weatherMin);

        descriptionLayout.addComponents(weatherMax);


        // Pressure , humidity and etc...
        pressureLayout = new VerticalLayout();
        pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        pressureLayout.addComponents(pressureLabel);


        pressureLayout.addComponents(humidityLabel);

        pressureLayout.addComponents(windSpeedLabel);

        pressureLayout.addComponents(windDegreeLabel);

        pressureLayout.addComponents(sunRiseLabel);

        pressureLayout.addComponents(sunSetLabel);

    }

    private void updateUI() throws JSONException {
        String city = cityTextField.getValue();
        String defaultUnit;
        String unit;

        if (unitSelect.getValue().equals("F")) {
            defaultUnit = "imperial";
            unitSelect.setValue("F");
            unit = "\u00b0" + "F";
        } else {
            defaultUnit = "metric";
            unitSelect.setValue("C");
            unit = "\u00b0" + "C";
        }

        weatherService.setCityName(city);
        weatherService.setUnit(defaultUnit);

        currentLocationTitle.setValue(city);
        JSONObject myObject = weatherService.returnMainObject();
        double temp = myObject.getDouble("temp");
        currentTemp.setValue(temp + unit);


        //Get min, max, pressure , humidity
        JSONObject mainObject = weatherService.returnMainObject();
        double minTemp = mainObject.getDouble("temp_min");
        double maxTemp = mainObject.getDouble("temp_max");
        int pressure = mainObject.getInt("pressure");
        int humidity = mainObject.getInt("humidity");

        // Get Weather Object
        JSONObject windObject = weatherService.returnWind();
        double windSpeed = windObject.getDouble("speed");
        int windDegree = windObject.getInt("deg");

        // Get Sunset, Sunrise
        JSONObject sunObject = weatherService.returnSunSet();
        long sunRise = sunObject.getLong("sunrise") * 1000;
        long sunSet = sunObject.getLong("sunset") * 1000;


        //Setup icon image
        String iconCode = "";
        String description = "";
        JSONArray jsonArray = weatherService.returnWeatherArray();


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            description = jsonObject.getString("description");
            iconCode = jsonObject.getString("icon");
        }
        iconImage.setSource(new ExternalResource("http://openweathermap.org/img/w/" + iconCode + ".png"));

        dashBoard.addComponents(currentLocationTitle, iconImage, currentTemp);
        mainLayout.addComponents(dashBoard);

        // Update Description UI
        weatherDescription.setValue("Condition: " + description);
        weatherDescription.addStyleName(ValoTheme.LABEL_SUCCESS);
        weatherMin.setValue("Min: " + String.valueOf(minTemp) + unit);
        weatherMax.setValue("Max: " + String.valueOf(maxTemp) + unit);
        pressureLabel.setValue("Pressure: " + String.valueOf(pressure) + " hpa");
        humidityLabel.setValue("Humidity: " + String.valueOf(humidity));
        windSpeedLabel.setValue("Wind Speed: " + String.valueOf(windSpeed) + " %");
        windDegreeLabel.setValue("Wind Degree: " + String.valueOf(windDegree) + " m/s");

        sunRiseLabel.setValue("Sunrise: " + convertTime(sunRise));
        sunSetLabel.setValue("Sunset: " + convertTime(sunSet));

        mainDescriptionLayout.addComponents(descriptionLayout, pressureLayout);
        
        mainLayout.addComponents(mainDescriptionLayout);

    }

    public String convertTime(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy hh.mm aa");

        return dateFormat.format(time);
    }
}
