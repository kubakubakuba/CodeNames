package cz.cvut.fel.pjv.codenames.server;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread    {

    private Socket socket;

    public ServerThread(Socket socket)  {
        this.socket = socket;
    }

    public void run(){
        try{
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String text;
            do{
                text = reader.readLine();
                String text2 = new StringBuilder(text).reverse().toString();
                writer.println("server: " + text2);
                System.out.println("client: " + text);
            } while(!text.equals("exit"));
        socket.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
