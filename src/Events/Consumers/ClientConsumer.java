package Events.Consumers;

import Events.Event;
import Events.TopicEnum;
import Networking.Client.ClientManager;

public class ClientConsumer extends EventConsumer{

    ClientManager clientManager = null;

    @Override
    protected void processEvent(Event e) {
        if(e.getTopic() == TopicEnum.SERVER_CONNECT){
            clientManager = new ClientManager("localhost",30300);
            clientManager.start();
        }
    }
}
