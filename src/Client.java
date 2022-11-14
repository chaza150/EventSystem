import Events.*;
import Events.Consumers.ClientConsumer;
import Events.Consumers.EventRouter;
import Events.Consumers.ServerConsumer;
import Events.Routing.EventRouting;
import Events.Scheduling.EventSchedule;
import Events.Scheduling.EventScheduler;
import Events.Subscribers.LoggerSubscriber;

public class Client {

    public static void main(String args[]) {
        EventQueue queue = new EventQueue("Original");
        EventQueue testDestinationQueue = new EventQueue("Queue 1");
        EventQueue testDestinationQueue2 = new EventQueue("Queue 2");
        EventQueue networkingQueue = new EventQueue("Networking Queue");

        LoggerSubscriber loggerSubscriber = new LoggerSubscriber(true);
        testDestinationQueue.addSubscriber(loggerSubscriber);
        loggerSubscriber.start();

        LoggerSubscriber loggerSubscriber2 = new LoggerSubscriber(true);
        testDestinationQueue2.addSubscriber(loggerSubscriber2);
        loggerSubscriber2.start();

        LoggerSubscriber networkLoggerSubscriber = new LoggerSubscriber(true);
        networkingQueue.addSubscriber(networkLoggerSubscriber);
        networkLoggerSubscriber.start();

        EventRouting routing = new EventRouting();
        routing.addDestinationRoute(TopicEnum.DEFAULT, testDestinationQueue);
        routing.addDestinationRoute(TopicEnum.EXAMPLE_TOPIC1, testDestinationQueue2);
        routing.addDestinationRoute(TopicEnum.SERVER_CONNECT, networkingQueue);
        EventRouter eventRouter = new EventRouter(routing, false);
        eventRouter.setSourceQueue(queue);
        eventRouter.start();
        queue.addSubscriber(eventRouter);

        ClientConsumer clientConsumer = new ClientConsumer();
        clientConsumer.setSourceQueue(networkingQueue);
        networkingQueue.addSubscriber(clientConsumer);
        clientConsumer.start();

//        EventRouting routing2 = new EventRouting();
//        routing2.addDestinationRoute(TopicEnum.DEFAULT, queue);
//        EventRouter eventRouter2 = new EventRouter(routing2, true);
//        eventRouter2.setTargetQueue(testDestinationQueue);

        //EventBroker eb = new EventBroker();
        //eb.setTargetQueue(testDestinationQueue);

        //eb.start();
//        eventRouter2.start();

        EventSchedule schedule = new EventSchedule();
        //schedule.addScheduleElement(1000, (q, targetTime) -> q.addEvent(new Event(TopicEnum.DEFAULT, targetTime)));
        //schedule.addScheduleElement(1200, (q, targetTime) -> q.addEvent(new Event(TopicEnum.EXAMPLE_TOPIC1, targetTime)));
        EventScheduler scheduler = new EventScheduler(3);
        scheduler.setTargetQueue(queue);
        scheduler.setSchedule(schedule);
        scheduler.start();

        queue.addEvent(new Event(TopicEnum.SERVER_CONNECT));
    }
}
