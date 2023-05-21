package cz.cvut.fel.pjv.codenames.server;

import cz.cvut.fel.pjv.codenames.GUI.StartView;
import javafx.application.Application;
import javafx.stage.Stage;
/**
 * @author Prokop Jansa, Jakub Pelc
 * @version 1.0
 */
public class Start {
    public static void main(String[] args) {
        boolean isServer = false;
        int port = 1515;

        for (String arg : args) {
            if (arg.equals("-type=server")) {
                isServer = true;
            } else if (arg.startsWith("-port=")) {
                try {
                    port = Integer.parseInt(arg.substring(6));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid port number. Using default port 1515.");
                }
            }
        }

        if (isServer) {
            Server server = new Server(port);
            server.run();
        } else {
            StartView startView = new StartView();
            startView.startApp();
        }
    }
}
