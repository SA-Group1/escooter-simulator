package com.escooter.repository;

import java.util.ArrayList;
import java.util.List;

import com.escooter.dto.DefaultResponse;
import com.escooter.dto.EscooterId;
import com.escooter.dto.UpdateBatteryLevelResponse;
import com.escooter.model.Escooter;
import com.escooter.network.HttpRequest;
import com.google.gson.Gson;

public class EscooterRepository {

    private static final String BASE_API_URL = "http://36.232.88.50:8080/api/";
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

    public String getStatus(String escooterId){
        String jsonInputString = String.format("{\"escooterId\": \"%s\"}", escooterId);
        try {
            String response = httpRequest.sendHttpRequest(BASE_API_URL + "getStatus", "POST", jsonInputString);
            if (response != null) {
                DefaultResponse defaultResponse = gson.fromJson(response, DefaultResponse.class);
                return defaultResponse.getMessage();
            }
        } catch (Exception e) {
        }

        return "";
    }

    public boolean updateBatteryLevel(String escooterId, int batteryLevel) {
        String jsonInputString = String.format("{\"escooterId\": \"%s\", \"batteryLevel\": %d}", escooterId, batteryLevel);
        try {
            String response = httpRequest.sendHttpRequest(BASE_API_URL + "updateBetteryLevel", "PUT", jsonInputString);
            if (response != null) {
                UpdateBatteryLevelResponse updateResponse = gson.fromJson(response, UpdateBatteryLevelResponse.class);
                return updateResponse.isStatus();
            }
        } catch (Exception e) {
        }

        return false;
    }

    private List<Escooter> parseGetEscooterListResponse(String response) {
        UpdateBatteryLevelResponse apiResponse = gson.fromJson(response, UpdateBatteryLevelResponse.class);

        if (!apiResponse.isStatus()) {
            System.out.println("Failed to get escooter ID list: " + apiResponse.getMessage());
            return null;
        }

        List<Escooter> escooterList = new ArrayList<>();
        for (EscooterId escooterId : apiResponse.getEscooterId()) {
            escooterList.add(new Escooter(escooterId.getEscooterId()));
        }

        return escooterList;
    }
}
