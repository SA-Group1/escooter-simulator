package com.escooter;

import com.escooter.service.EscooterSimulateService;

public class Main {

    private static final EscooterSimulateService escooterSimulateService = new EscooterSimulateService();
    
    public static void main(String[] args) {
        escooterSimulateService.start();
    }
}
