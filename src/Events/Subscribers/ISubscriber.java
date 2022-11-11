package Events.Subscribers;

import Events.Event;

public interface ISubscriber {
    public void notifyEvent(Event event);

    public boolean isPureSubscriber();
}
