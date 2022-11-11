package Events.Routing;

import Events.EventQueue;
import Events.TopicEnum;

import java.util.HashMap;

public class EventRouting {

    HashMap<TopicEnum, EventQueue> destinationRoutes = new HashMap<>();

    public void addDestinationRoute(TopicEnum topic, EventQueue eventQueue){
        destinationRoutes.put(topic, eventQueue);
    }

    public EventQueue getRoute(TopicEnum topic){
        EventQueue destination = null;
        if (destinationRoutes.containsKey(topic)){
            destination = destinationRoutes.get(topic);
        }

        return destination;
    }

}
