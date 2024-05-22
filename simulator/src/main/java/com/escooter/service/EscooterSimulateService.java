package com.escooter.service;

import java.util.List;

import com.escooter.model.Escooter;
import com.escooter.repository.EscooterRepository;
import com.escooter.thread.EscooterThread;

public class EscooterSimulateService {

    private final EscooterRepository escooterRepository;
    private final List<Escooter> escooters;
    private final EscooterThread escooterThread;

    public EscooterSimulateService(){
        escooterRepository = new EscooterRepository();
        escooters = fetchEscooters();
        escooterThread = new EscooterThread(escooters);
    }

    private List<Escooter> fetchEscooters(){
        return escooterRepository.getEscooterList();
    }

    public void start(){
        new Thread(escooterThread).start();
    }

    public void stop(){
        escooterThread.stopRunning();
    }

}