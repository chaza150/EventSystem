package Events.Consumers;

import Events.Event;
import Events.TopicEnum;
import Networking.Client.ClientManager;
import Networking.Server.ServerManager;

public class ServerConsumer extends EventConsumer{
    ServerManager serverManager = null;

    public ServerConsumer(){
        super(true);
    }

    @Override
    protected void processEvent(Event e) {
        if(e.getTopic() == TopicEnum.SERVER_CREATE){
            serverManager = new ServerManager(30300);
            serverManager.start();
        }
    }
}
