package Events;

public class Event implements Comparable<Event>{
    private long eventTimeMillis;
    private TopicEnum topic;

    private String text = "";

    public Event(TopicEnum topic, long eventTimeMillis){
        this.eventTimeMillis = eventTimeMillis;
        this.topic = topic;
    }

    public Event(TopicEnum topic){
        this.eventTimeMillis = System.currentTimeMillis();
        this.topic = topic;
    }

    public Event(TopicEnum topic, long eventTimeMillis, String text){
        this.eventTimeMillis = eventTimeMillis;
        this.topic = topic;
        this.text = text;
    }

    public Event(TopicEnum topic, String text){
        this.eventTimeMillis = System.currentTimeMillis();
        this.topic = topic;
        this.text = text;
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

    public String getText(){
        return this.text;
    }
}
