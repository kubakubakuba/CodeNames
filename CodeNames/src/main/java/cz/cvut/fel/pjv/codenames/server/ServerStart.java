package cz.cvut.fel.pjv.codenames.server;

public class ServerStart {
    public static void main(String[] args) {
        System.out.println("Starting server:");
        Server server = new Server();
        server.run();
    }
}