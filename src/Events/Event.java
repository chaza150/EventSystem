package Events;

public class Event implements Comparable<Event>{
    private long eventTimeMillis;
    private TopicEnum topic;

    public Event(TopicEnum topic, long eventTimeMillis){
        this.eventTimeMillis = eventTimeMillis;
        this.topic = topic;
    }

    public Event(TopicEnum topic){
        this.eventTimeMillis = System.currentTimeMillis();
        this.topic = topic;
    }

    public TopicEnum getTopic(){
        return this.topic;
    }

    public long getEventTimeMillis(){
        return this.eventTimeMillis;
    }

    @Override
    public int compareTo(Event e2) {
        return (int) (this.getEventTimeMillis() - e2.getEventTimeMillis());
    }
}
