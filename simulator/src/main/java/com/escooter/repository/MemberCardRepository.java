package com.escooter.repository;

import com.escooter.dto.DefaultResponse;
import com.escooter.network.HttpRequestHandler;
import com.google.gson.Gson;

public class MemberCardRepository {

    private static final String BASE_API_URL = "http://127.0.0.1:8080/api/";
    private final Gson gson = new Gson();
    private final HttpRequestHandler httpRequest = new HttpRequestHandler();

    public boolean addMemberCard(String account, String cardNumber, String expirationDate) {
        String jsonInputString = String.format("{\"account\": \"%s\", \"cardNumber\": \"%s\", \"expirationDate\": \"%s\"}", account, cardNumber, expirationDate);
        try {
            String response = httpRequest.sendHttpRequest(BASE_API_URL + "bindMemberCard", "POST", jsonInputString);
            if (response != null) {
                DefaultResponse updateResponse = gson.fromJson(response, DefaultResponse.class);
                System.err.println(updateResponse.getMessage());
                return updateResponse.isStatus();
            }
        } catch (Exception e) {
        }

        return false;
    }

}
