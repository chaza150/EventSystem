package Events.Scheduling;

import Events.EventQueue;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class EventSchedule {

    ArrayList<ScheduleElement> scheduleElements = new ArrayList<>();

    public boolean addScheduleElement(int occursEvery, BiConsumer<EventQueue, Long> eventAdder){
        return scheduleElements.add(new ScheduleElement(occursEvery, eventAdder));
    }

    public ArrayList<ScheduleElement> getScheduleElements(){
        return this.scheduleElements;
    }

    public class ScheduleElement{
        int occursEvery;
        BiConsumer<EventQueue, Long> eventAdder;

        public ScheduleElement(int occursEvery, BiConsumer<EventQueue, Long> eventAdder){
            this.occursEvery = occursEvery;
            this.eventAdder = eventAdder;
        }
    }
}
