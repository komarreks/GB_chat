package server;

import server.client.ClientGUI;
import server.server.ServerGUI;

import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws ParseException {
        ServerGUI serverGUI = new ServerGUI();

        new ClientGUI(serverGUI);
        new ClientGUI(serverGUI);
    }
}
