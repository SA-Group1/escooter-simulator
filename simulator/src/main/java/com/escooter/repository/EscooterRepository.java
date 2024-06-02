package com.escooter.repository;

import java.util.ArrayList;
import java.util.List;

import com.escooter.dto.DefaultResponse;
import com.escooter.dto.EscooterId;
import com.escooter.dto.GetEscooterGpsResponse;
import com.escooter.dto.GetEscooterListResponse;
import com.escooter.dto.GetStringResponse;
import com.escooter.model.Escooter;
import com.escooter.model.GPS;
import com.escooter.network.HttpRequest;
import com.google.gson.Gson;

public class EscooterRepository {

    private static final String BASE_API_URL = "http://36.232.86.13:8080/api/";
    private final Gson gson = new Gson();
    private final HttpRequest httpRequest = new HttpRequest();

    public List<Escooter> getEscooterList() {
        try {
            String response = httpRequest.sendHttpRequest(BASE_API_URL + "getEscooterIdList", "GET", null);
            return parseGetEscooterListResponse(response);
        } catch (Exception e) {
        }
        return null;
    }

    public String getStatus(String escooterId) {
        String jsonInputString = String.format("{\"escooterId\": \"%s\"}", escooterId);
        try {
            String response = httpRequest.sendHttpRequest(BASE_API_URL + "getEscooterStatus", "POST", jsonInputString);
            if (response != null) {
                GetStringResponse defaultResponse = gson.fromJson(response, GetStringResponse.class);
                return defaultResponse.getData();
            }
        } catch (Exception e) {
        }

        return "";
    }

    public GPS getGps(String escooterId) {
        String jsonInputString = String.format("{\"escooterId\": \"%s\"}", escooterId);
        try {
            String response = httpRequest.sendHttpRequest(BASE_API_URL + "getEscooterGps", "POST", jsonInputString);
            if (response != null) {
                GetEscooterGpsResponse gpsResponse = gson.fromJson(response, GetEscooterGpsResponse.class);
                return gpsResponse.getData();
            }
        } catch (Exception e) {
        }

        return new GPS(120.532843, 23.693643);
    }

    public boolean updateBatteryLevel(String escooterId, int batteryLevel) {
        String jsonInputString = String.format("{\"escooterId\": \"%s\", \"batteryLevel\": %d}", escooterId, batteryLevel);
        try {
            String response = httpRequest.sendHttpRequest(BASE_API_URL + "updateBatteryLevel", "PUT", jsonInputString);
            if (response != null) {
                DefaultResponse updateResponse = gson.fromJson(response, DefaultResponse.class);
                return updateResponse.isStatus();
            }
        } catch (Exception e) {
        }

        return false;
    }

    public boolean updateGps(String escooterId, double latitude, double longitude) {
        String jsonInputString = String.format("{\"escooterId\": \"%s\", \"latitude\": %f, \"longitude\": %f}", escooterId, latitude, longitude);
        try {
            String response = httpRequest.sendHttpRequest(BASE_API_URL + "updateGps", "PUT", jsonInputString);
            if (response != null) {
                DefaultResponse updateResponse = gson.fromJson(response, DefaultResponse.class);
                return updateResponse.isStatus();
            }
        } catch (Exception e) {
        }

        return false;
    }

    private List<Escooter> parseGetEscooterListResponse(String response) {
        GetEscooterListResponse apiResponse = gson.fromJson(response, GetEscooterListResponse.class);

        if (!apiResponse.isStatus()) {
            System.out.println("Failed to get escooter ID list: " + apiResponse.getMessage());
            return null;
        }

        List<Escooter> escooterList = new ArrayList<>();
        for (EscooterId escooterId : apiResponse.getData()) {
            escooterList.add(new Escooter(escooterId.getEscooterId()));
        }

        return escooterList;
    }
}
