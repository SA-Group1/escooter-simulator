package com.escooter.dto;

import com.escooter.model.GPS;

public class GetEscooterGpsResponse extends DefaultResponse {

    private GPS data;

    public GPS getData() {
        return data;
    }

    public void setData(GPS data) {
        this.data = data;
    }
}
