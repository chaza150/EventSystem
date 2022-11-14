package Networking;

import Events.Event;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class SocketWriter implements Runnable{

    private BufferedWriter out;
    Socket socket;
    private boolean enabled;

    private Thread activeThread;

    private Object notifyLock = new Object();

    private BlockingQueue<String> outboundMessageQueue = new LinkedBlockingQueue<>();

    public SocketWriter(Socket socket){
        this.socket = socket;
        try {
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        if(!enabled){
            this.activeThread = new Thread(this);
            enabled = true;
            activeThread.start();
        }
    }

    @Override
    public void run() {
        try {
            while (enabled) {
                String nextMessage = null;
                try {
                    nextMessage = outboundMessageQueue.take();
                } catch (InterruptedException e) {
                    continue;
                }
                this.out.write(nextMessage);
                this.out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addMessage(String message){
        return outboundMessageQueue.add(message);
    }
}
