package com.escooter.service;

import java.util.List;

import com.escooter.model.Escooter;
import com.escooter.model.GPS;
import com.escooter.repository.EscooterRepository;
import com.escooter.thread.EscooterThread;

public class EscooterSimulateService {

    private final EscooterRepository escooterRepository;
    private final List<Escooter> escooters;
    private final EscooterThread escooterThread;
    private Thread thread;

    public EscooterSimulateService() {
        escooterRepository = new EscooterRepository();
        escooters = fetchEscooters();

        for (Escooter escooter : escooters) {
            GPS gps = escooterRepository.getGps(escooter.getId());
            escooter.setLat(gps.getLatitude());
            escooter.setLon(gps.getLongitude());
        }

        escooterThread = new EscooterThread(escooters);
        thread = new Thread(escooterThread);
    }

    private List<Escooter> fetchEscooters() {
        return escooterRepository.getEscooterList();
    }

    public void start() {
        if (!thread.isAlive()) {
            thread = new Thread(escooterThread);
            thread.start();
            System.out.println("Thread started.");
        } else {
            System.out.println("Thread is already running.");
        }
    }

    public void stop() {
        if (thread.isAlive()) {
            escooterThread.stopRunning();
            try {
                thread.join();
                System.out.println("Thread stopped and all escooters' GPS reset.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            System.out.println("Thread is not running.");
        }
    }

    public void stopEscooter(String escooterId) {
        escooterThread.setEscooterMovable(escooterId, false);
        System.out.println("rest " + escooterId + " GPS.");
    }

}
