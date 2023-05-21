package cz.cvut.fel.pjv.codenames.server;
/**
 * @author Prokop Jansa, Jakub Pelc
 * @version 1.0
 */
public class ServerStart {
    public static void main(String[] args) {
        System.out.println("Starting server:");
        Server server = new Server(1515);
        server.run();
    }
}