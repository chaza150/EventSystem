package Events.Consumers;

import Events.Event;
import Events.EventQueue;
import Events.Subscribers.ISubscriber;

public abstract class EventConsumer implements Runnable, ISubscriber {

    EventQueue sourceQueue = null;
    boolean enabled = false;
    private boolean processing = false;

    private Thread activeThread = null;

    protected boolean enforceTiming = false;

    protected boolean isPureSubscriber = false;

    public EventConsumer(){}

    public EventConsumer(boolean enforceTiming){
        this.enforceTiming = enforceTiming;
    }

    public void start(){
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

    public void setSourceQueue(EventQueue targetQueue){
        this.sourceQueue = targetQueue;
    }

    public EventQueue getSourceQueue(){
        return this.sourceQueue;
    }

    public boolean isPureSubscriber(){
        return this.isPureSubscriber;
    }

    protected abstract void processEvent(Event e);

    @Override
    public void notifyEvent(Event event){
        if(!this.processing) {
            activeThread.interrupt();
//            System.out.println(this.getClass().getName() + " nudged");
        }
    }

    @Override
    public void run(){
        boolean waiting = false;
        while(enabled){
            try {
                if(enforceTiming) {
                    Long nextEventTime = sourceQueue.getNextEventTime();
                    int millisToWait = -1;
                    if (nextEventTime == null) {
                        millisToWait = 60000;
                    } else {
                        if (nextEventTime > System.currentTimeMillis()) {
                            millisToWait = (int) (nextEventTime - System.currentTimeMillis());
                        }
                    }

                    if (millisToWait != -1) {
                        if(!waiting) {
//                            System.out.println(this.getClass().getName() + " sleeping for " + (millisToWait));
                        }
                        waiting=true;
                        if (millisToWait > 100) {
                            try {
                                Thread.sleep(millisToWait - 100);
                            } catch (InterruptedException e) {
                                continue;
                            }
                        } else if (millisToWait > 20) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                continue;
                            }
                        } else if (millisToWait > 1) {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                continue;
                            }
                        }
                        continue;
                    }
                    waiting = false;
                }

                this.processing = true;

                Event event = sourceQueue.getNextEvent();
                processEvent(event);
                this.processing = false;

            } catch (InterruptedException e) {}
        }
    }
}
