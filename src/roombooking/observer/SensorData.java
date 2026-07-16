package roombooking.observer;

import java.util.ArrayList;
import java.util.List;

public class SensorData implements Subject {
    private static SensorData instance;
    private List<Observer> observers;
    private int roomId;
    private boolean occupied;
    private String badgeId;
    private String timestamp;
    
    private SensorData() {
        this.observers = new ArrayList<>();
    }
    
    public static SensorData getInstance() {
        if (instance == null) {
            instance = new SensorData();
        }
        return instance;
    }
    
    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }
    
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
    
    public void updateSensorData(int roomId, boolean occupied, String badgeId) {
        this.roomId = roomId;
        this.occupied = occupied;
        this.badgeId = badgeId;
        this.timestamp = java.time.LocalDateTime.now().toString();
        notifyObservers();
    }
    
    public int getRoomId() { return roomId; }
    public boolean isOccupied() { return occupied; }
    public String getBadgeId() { return badgeId; }
    public String getTimestamp() { return timestamp; }
}
