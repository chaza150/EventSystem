package Events.Consumers;

import Events.Event;
import Events.EventQueue;
import Events.Routing.EventRouting;

public class EventRouter extends EventConsumer {

    EventRouting routing;

    public EventRouter(EventRouting routing){
        super();
        this.routing = routing;
    }

    public EventRouter(EventRouting routing, boolean enforceTiming){
        super(enforceTiming);
        this.routing = routing;
    }

    @Override
    protected void processEvent(Event event) {
        EventQueue destination = routing.getRoute(event.getTopic());
        destination.addEvent(event);
        System.out.println("Routed");
    }
}
