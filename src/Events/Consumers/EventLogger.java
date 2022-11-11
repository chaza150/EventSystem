package Events.Consumers;

import Events.Consumers.EventConsumer;
import Events.Event;

public class EventLogger extends EventConsumer {

    @Override
    protected void processEvent(Event e) {
        System.out.println(e.getEventTimeMillis());
    }
}
