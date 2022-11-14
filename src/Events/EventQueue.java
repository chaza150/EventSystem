package Events;

import Events.Subscribers.ISubscriber;

import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;

public class EventQueue {

    private PriorityBlockingQueue<Event> internalQueue = new PriorityBlockingQueue<>();

    private ArrayList<ISubscriber> notificationSubscribers = new ArrayList<>();

    private String name;

    public EventQueue(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public boolean addSubscriber(ISubscriber subscriber){
        return notificationSubscribers.add(subscriber);
    }

    public void notifyAllSubscribers(Event event, boolean notifyConsumers){
        for(ISubscriber subscriber:notificationSubscribers){
            if(notifyConsumers || subscriber.isPureSubscriber()) {
                subscriber.notifyEvent(event);
//                System.out.println(this.name + " notified");
            }
        }
//        System.out.println(this.name + " notified all");
    }

    public boolean addEvent(Event event){
            boolean wasEmpty = internalQueue.size() == 0;
            boolean added = internalQueue.add(event);
            //System.out.println(wasEmpty || internalQueue.peek() == event);
            notifyAllSubscribers(event, wasEmpty || internalQueue.peek() == event);
            return added;
    }

    public Event getNextEvent() throws InterruptedException{
            return internalQueue.take();
    }

    public Long getNextEventTime(){
            Event nextEvent = internalQueue.peek();
            if (nextEvent != null) {
                return nextEvent.getEventTimeMillis();
            } else {
                return null;
            }
    }

    public boolean isReady(){
        return internalQueue.peek() != null;
    }
}
