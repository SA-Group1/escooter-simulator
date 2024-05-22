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
            System.out.println("thread");
            updateBatteryLevels();
            sleepForInterval(1000);
        }
    }

    public void stopRunning() {
        running = false;
    }

    private void updateBatteryLevels() {
        synchronized (escooters) {
            for (Escooter escooter : escooters) {
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