package com.escooter.thread;

import java.util.List;

import com.escooter.model.Escooter;
import com.escooter.repository.EscooterRepository;

public class EscooterThread implements Runnable{

    private final EscooterRepository escooterRepository;
    private final List<Escooter> escooters;
    private volatile boolean running = true;

    public EscooterThread(List<Escooter> escooters) {
        this.escooters = escooters;
        this.escooterRepository = new EscooterRepository();
    }

    @Override
    public void run() {
        while (running) {
            updateEscooterStatus();
            updateBatteryLevels();
            sleepForInterval(1000);
        }
    }

    public void stopRunning() {
        running = false;
    }

    private void updateEscooterStatus() {
        synchronized (escooters) {
            for (Escooter escooter : escooters) {
                String newStatus = escooterRepository.getStatus(escooter.getId());
                escooter.setStatus(newStatus);
            }
        }
    }

    private void updateBatteryLevels() {
        synchronized (escooters) {
            for (Escooter escooter : escooters) {
                if(!"Rented".equals(escooter.getStatus())){
                    escooterRepository.updateBatteryLevel(escooter.getId(), escooter.getBatteryLevel());
                    continue;
                }

                int newBatteryLevel = simulateBatteryLevel(escooter.getBatteryLevel());
                escooter.setBatteryLevel(newBatteryLevel);
                escooterRepository.updateBatteryLevel(escooter.getId(), newBatteryLevel);
            }
        }
    }

    private void sleepForInterval(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private int simulateBatteryLevel(int batteryLevel) {
        return Math.max(0, batteryLevel - 1); // Ensure battery level doesn't go below 0
    }
}