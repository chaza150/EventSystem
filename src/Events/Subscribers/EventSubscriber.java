package Events.Subscribers;

import Events.Consumers.EventConsumer;
import Events.Event;
import Events.EventQueue;

public abstract class EventSubscriber extends EventConsumer {

    public EventSubscriber(){
        super();
        this.setSourceQueue(new EventQueue("Subscriber Queue"));
        this.isPureSubscriber = true;
    }

    public EventSubscriber(boolean enforceTiming){
        super(enforceTiming);
        this.setSourceQueue(new EventQueue("Subscriber Queue"));
        this.isPureSubscriber = true;
    }

    @Override
    public void notifyEvent(Event event){
        this.getSourceQueue().addEvent(event);
        super.notifyEvent(event);
    }

    @Override
    protected abstract void processEvent(Event event);
}
