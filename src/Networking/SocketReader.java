package Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketReader implements Runnable{

    private BufferedReader in;
    Socket socket;
    private boolean enabled;

    private Thread activeThread;

    public SocketReader(Socket socket){
        this.socket = socket;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
        String fullMessage = null;
        try {
            while (enabled) {
                String line = this.in.readLine();
                if (isTermination(line)) {
                    if(fullMessage != null) {
                        processMessage(fullMessage);
                    }
                } else {
                    if(fullMessage != null) {
                        fullMessage += "\n" + line;
                    } else {
                        fullMessage = line;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processMessage(String message){
        System.out.println(message);
    }

    private boolean isTermination(String message){
        return message.equals("---END MESSAGE---");
    }
}
