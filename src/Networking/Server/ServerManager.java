package Networking.Server;

import Networking.SocketReader;
import Networking.SocketWriter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerManager implements Runnable{

    ArrayList<ClientConnection> clientConnections = new ArrayList<>();
    ServerSocket serverSocket = null;
    private int port;

    private Thread activeThread;
    private boolean enabled = false;

    public ServerManager(int port){
        this.port = port;
    }

    public void start() {
        enabled = true;
        if(serverSocket == null || serverSocket.isClosed()) {
            try {
                this.serverSocket = new ServerSocket(port);
                activeThread = new Thread(this);
                activeThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void run(){
        while(enabled){
            try {
                Socket clientSocket = serverSocket.accept();
                createConnection(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createConnection(Socket socket) throws IOException {
        ClientConnection clientConnection = new ClientConnection(socket);
        clientConnections.add(clientConnection);
        clientConnection.start();
        clientConnection.writeMessage("Connected to Server");
    }

    private void broadcastMessage(String message){
        for (ClientConnection clientConnection: clientConnections){
            clientConnection.writeMessage(message);
        }
    }

    private class ClientConnection{
        public Socket socket;
        public SocketReader reader;
        public SocketWriter writer;

        public ClientConnection(Socket socket){
            this.socket = socket;
            this.reader = new SocketReader(socket);
            this.writer = new SocketWriter(socket);
        }

        public void start(){
            this.reader.start();
            this.writer.start();
        }

        public boolean writeMessage(String message){
            return writer.addMessage(message + "\n---END MESSAGE---\n");
        }
    }
}
