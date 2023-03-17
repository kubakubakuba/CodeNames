package cz.cvut.fel.pjv.catan.server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    private final int PORT = 1313;

    public void Server()    {

        //host, socket,
    }

    @Override
    public void run(){
        try(ServerSocket ss = new ServerSocket(PORT)){
            while(true){
                Socket socket = ss.accept();
                System.out.println("new client");

                new ServerThread(socket).start();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void shutDown()    {

    }

}
