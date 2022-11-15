package Networking.Client;

import Networking.SocketReader;
import Networking.SocketWriter;

import java.io.*;
import java.net.Socket;

public class ClientManager{

    Socket socket = null;
    private String hostName;
    private int port;

    private Thread activeThread;

    SocketReader reader;
    SocketWriter writer;

    public ClientManager(String hostName, int port){
        this.hostName = hostName;
        this.port = port;
    }

    public void start() {
        if(socket == null || socket.isClosed()){
            try {
                connect();
                reader.start();
                writer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connect() throws IOException {
        if (socket == null) {
            socket = new Socket(hostName, port);
            reader = new SocketReader(socket);
            writer = new SocketWriter(socket);
            writeMessage("Client joined Server");
        }
    }

    private boolean writeMessage(String message){
        return writer.addMessage(message + "\n---END MESSAGE---\n");
    }
}
