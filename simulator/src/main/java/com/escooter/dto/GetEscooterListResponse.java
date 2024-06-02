package com.escooter.dto;

import java.util.List;

public class GetEscooterListResponse extends DefaultResponse {

    private List<EscooterId> data;

    public List<EscooterId> getData() {
        return data;
    }
}
