package Events.Scheduling;

import Events.EventQueue;

import java.util.HashMap;

public class EventScheduler implements Runnable{

    private EventQueue targetQueue = null;

    private EventSchedule schedule;

    boolean enabled = false;
    private Thread activeThread = null;
    private HashMap<EventSchedule.ScheduleElement, Integer> millisSinceLastUpdates = new HashMap<>();

    private int bufferQuantity = 2;

    public EventScheduler(int bufferQuantity){
        this.bufferQuantity = bufferQuantity;
    }

    public void start(){
        for(EventSchedule.ScheduleElement scheduleElement : schedule.getScheduleElements()){
            millisSinceLastUpdates.put(scheduleElement, scheduleElement.occursEvery);
        }
        enabled = true;
        activeThread = new Thread(this);
        activeThread.start();
    }

    public void stop(){
        if(activeThread != null) {
            enabled = false;
            activeThread.interrupt();
        }
    }

    public void setSchedule(EventSchedule schedule){
        this.schedule = schedule;
    }

    public void setTargetQueue(EventQueue targetQueue){
        this.targetQueue = targetQueue;
    }

    @Override
    public void run() {
        long lastUpdateTime = System.currentTimeMillis();

        while(enabled) {
            int shortestMillisToNextUpdate = Integer.MAX_VALUE;
            long currentLoopTime = System.currentTimeMillis();
            for (EventSchedule.ScheduleElement scheduleElement : schedule.getScheduleElements()) {
                if (!millisSinceLastUpdates.containsKey(scheduleElement)) {
                    millisSinceLastUpdates.put(scheduleElement, scheduleElement.occursEvery);
                    continue;
                } else {
                    millisSinceLastUpdates.put(scheduleElement, (int) (millisSinceLastUpdates.get(scheduleElement) + (currentLoopTime - lastUpdateTime)));
                }

                int millisToNextUpdate = 0;
                int millisSinceLastUpdate = millisSinceLastUpdates.get(scheduleElement);

                // Buffer as many events as asked for with appropriate timings
                while(millisSinceLastUpdate >= (1-this.bufferQuantity) * scheduleElement.occursEvery) {
                    // Add the event to the target queue with intended timings
                    scheduleElement.eventAdder.accept(targetQueue, currentLoopTime - millisSinceLastUpdate + scheduleElement.occursEvery);
                    // Adjust the milliseconds since update. With buffering, this can be negative!
                    millisSinceLastUpdate = (int) (millisSinceLastUpdates.get(scheduleElement) - scheduleElement.occursEvery);
                    millisSinceLastUpdates.put(scheduleElement, millisSinceLastUpdate);
                }

                // Calculate the milliseconds until we next have to add event
                millisToNextUpdate = (1-this.bufferQuantity) * scheduleElement.occursEvery - millisSinceLastUpdate;
//                System.out.println(millisToNextUpdate);

                // Find the shortest waiting time, as we will miss it otherwise
                if(millisToNextUpdate < shortestMillisToNextUpdate){
                    shortestMillisToNextUpdate = millisToNextUpdate;
                }
            }
            lastUpdateTime = currentLoopTime;

            int millisToWait = shortestMillisToNextUpdate;
            if (millisToWait != -1) {
                // If we are buffering events, then we don't need to be as specific when waiting
                if(bufferQuantity > 0){
                    try {
//                        System.out.println(this.getClass().getName() + " sleeping for " + (millisToWait));
                        Thread.sleep(millisToWait);
                    } catch (InterruptedException e) {
                        continue;
                    }
                // If we aren't buffering events, then we need to be precise
                } else {
                    if (millisToWait > 50) {
                        try {
//                            System.out.println(this.getClass().getName() + " sleeping for " + (millisToWait - 50));
                            Thread.sleep(millisToWait - 50);
                        } catch (InterruptedException e) {
                            continue;
                        }
                    } else if (millisToWait > 5) {
                        try {
//                            System.out.println(this.getClass().getName() + " sleeping for " + (millisToWait - 5));
                            Thread.sleep(millisToWait - 5);
                        } catch (InterruptedException e) {
                            continue;
                        }
                    } else if (millisToWait > 0) {
                        try {
//                            System.out.println(this.getClass().getName() + " sleeping for " + (millisToWait));
                            Thread.sleep(millisToWait);
                        } catch (InterruptedException e) {
                            continue;
                        }
                    }
                }
                continue;
            }
        }
    }
}
