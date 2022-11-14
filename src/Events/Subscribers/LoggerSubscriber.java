package Events.Subscribers;

import Events.Event;
import Events.Subscribers.EventSubscriber;

public class LoggerSubscriber extends EventSubscriber {

    public LoggerSubscriber(){
        super();
    }

    public LoggerSubscriber(boolean enforceTiming){
        super(enforceTiming);
    }

    @Override
    protected void processEvent(Event event) {
        System.out.println(this.toString() + " Logged: (Topic: " + event.getTopic() + ", Event Time: " + event.getEventTimeMillis() + ", Processed Time: " + System.currentTimeMillis() + ")");
    }
}
