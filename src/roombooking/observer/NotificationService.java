package roombooking.observer;

import java.util.ArrayList;
import java.util.List;

public class NotificationService implements Observer {
    private static NotificationService instance;
    private List<Observer> subscribers;
    
    private NotificationService() {
        this.subscribers = new ArrayList<>();
    }
    
    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }
    
    public void sendNotification(String message) {
        System.out.println("[NOTIFICATION] " + message);
        for (Observer observer : subscribers) {
            observer.update(message);
        }
    }
    
    @Override
    public void update(Object data) {
        if (data instanceof String) {
            sendNotification((String) data);
        }
    }
    
    public void subscribe(Observer observer) {
        if (!subscribers.contains(observer)) {
            subscribers.add(observer);
        }
    }
    
    public void unsubscribe(Observer observer) {
        subscribers.remove(observer);
    }
}
