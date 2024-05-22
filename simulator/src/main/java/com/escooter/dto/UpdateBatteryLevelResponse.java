package com.escooter.dto;

import java.util.List;

public class UpdateBatteryLevelResponse extends DefaultResponse {
    
    private List<EscooterId> escooterId;

    public List<EscooterId> getEscooterId() {
        return escooterId;
    }
}
