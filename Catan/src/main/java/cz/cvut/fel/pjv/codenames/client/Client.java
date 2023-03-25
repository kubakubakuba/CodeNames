package cz.cvut.fel.pjv.codenames.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String hostname = "localhost";
        int port = 1313;

        try (Socket socket = new Socket(hostname, port)){
             OutputStream output = socket.getOutputStream();
             PrintWriter writer = new PrintWriter(output, true);

             String text;

             do{
                 text = sc.nextLine().toString();
                 writer.println(text);

                 InputStream input = socket.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                 String msg = reader.readLine();
                 System.out.println(msg);
             } while(!text.equals("exit"));
             socket.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
