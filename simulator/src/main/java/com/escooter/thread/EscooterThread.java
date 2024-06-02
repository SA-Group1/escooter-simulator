package com.escooter.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.escooter.model.Escooter;
import com.escooter.repository.EscooterRepository;

public class EscooterThread implements Runnable {

    private final EscooterRepository escooterRepository;
    private final List<Escooter> escooters;
    private volatile boolean running = true;
    private final double[][] rectangle = {
        {120.533207, 23.695134},
        {120.531437, 23.694810},
        {120.531716, 23.693326},
        {120.533513, 23.693606}
    };

    private final Map<Escooter, Integer> escooterCorners = new HashMap<>();
    private final Map<Escooter, double[]> startPositions = new HashMap<>();
    private final Map<Escooter, Boolean> wasRentedMap = new HashMap<>();

    public EscooterThread(List<Escooter> escooters) {
        this.escooters = escooters;
        this.escooterRepository = new EscooterRepository();
        for (Escooter escooter : escooters) {
            escooterCorners.put(escooter, 0);
            startPositions.put(escooter, new double[]{escooter.getLat(), escooter.getLon()});
            wasRentedMap.put(escooter, false);
        }
    }

    @Override
    public void run() {
        while (running) {
            updateEscooterStatus();
            updateBatteryLevels();
            updateGps();
            sleepForInterval(1000);
        }
    }

    public void stopRunning() {
        running = false;
        resetAllGps();
    }

    public void setEscooterMovable(String escooterId, boolean isMovable) {
        synchronized (escooters) {
            for (Escooter escooter : escooters) {
                if (escooter.getId().equals(escooterId)) {
                    escooter.setIsMovable(isMovable);
                    if (!isMovable) {
                        resetGps(escooter);
                    }
                    escooterRepository.updateGps(escooter.getId(), escooter.getLat(), escooter.getLon());
                    break;
                }
            }
        }
    }

    private void updateGps() {
        synchronized (escooters) {
            for (Escooter escooter : escooters) {
                boolean wasRented = wasRentedMap.get(escooter);
                if (!"Rented".equals(escooter.getStatus())) {
                    continue;
                }

                if (!escooter.getIsMovable()) {
                    if (wasRented) {
                        resetGps(escooter);
                        wasRentedMap.put(escooter, false);
                    }
                    continue;
                }

                if (!wasRented) {
                    escooter.setLat(rectangle[0][0]);
                    escooter.setLon(rectangle[0][1]);
                    escooterCorners.put(escooter, 0);
                }

                double[] currentGps = {escooter.getLat(), escooter.getLon()};
                double[] nextGps = calculateNextGps(currentGps, escooterCorners.get(escooter));
                escooter.setLat(nextGps[0]);
                escooter.setLon(nextGps[1]);
                escooterRepository.updateGps(escooter.getId(), nextGps[0], nextGps[1]);

                if (hasReachedCorner(nextGps[0], nextGps[1], rectangle[escooterCorners.get(escooter)])) {
                    escooterCorners.put(escooter, (escooterCorners.get(escooter) + 1) % rectangle.length);
                }
                wasRentedMap.put(escooter, true);
            }
        }
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
                if (!"Rented".equals(escooter.getStatus())) {
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

    private double[] calculateNextGps(double[] currentGps, int currentCorner) {
        double[] nextCorner = rectangle[currentCorner];
        double stepSize = 0.0006;

        double nextLatitude = moveTowards(currentGps[0], nextCorner[0], stepSize);
        double nextLongitude = moveTowards(currentGps[1], nextCorner[1], stepSize);

        return new double[]{nextLatitude, nextLongitude};
    }

    private double moveTowards(double current, double target, double stepSize) {
        if (Math.abs(target - current) <= stepSize) {
            return target;
        }
        return current + Math.signum(target - current) * stepSize;
    }

    private boolean hasReachedCorner(double latitude, double longitude, double[] corner) {
        return Math.abs(latitude - corner[0]) < 0.00001 && Math.abs(longitude - corner[1]) < 0.00001;
    }

    private void resetGps(Escooter escooter) {
        double[] startPosition = startPositions.get(escooter);
        escooter.setLat(startPosition[0]);
        escooter.setLon(startPosition[1]);
        escooterRepository.updateGps(escooter.getId(), startPosition[0], startPosition[1]);
    }

    private void resetAllGps() {
        synchronized (escooters) {
            for (Escooter escooter : escooters) {
                resetGps(escooter);
            }
        }
    }
}
