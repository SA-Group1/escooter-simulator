package com.escooter.dto;

import java.util.List;

public class GetEscoooterIdListResponse {
    private boolean status;
    private String message;
    private List<EscooterId> escooterId;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<EscooterId> getEscooterId() {
        return escooterId;
    }
}
