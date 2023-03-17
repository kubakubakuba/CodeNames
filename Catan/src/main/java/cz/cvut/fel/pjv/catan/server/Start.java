package cz.cvut.fel.pjv.catan.server;

public class Start {
    public static void main(String[] args) {
        System.out.println("Starting server:");
        Server server = new Server();
        server.run();
    }

}