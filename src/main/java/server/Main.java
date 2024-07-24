package server;

import server.client.Client;
import server.client.ClientGUI;
import server.client.ClientView;
import server.server.Server;
import server.server.ServerGUI;

import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws ParseException {
        Server server = new Server();
        ServerGUI serverGUI = new ServerGUI(server);
        server.setView(serverGUI);

        Client client1 = new Client();
        Client client2 = new Client();

        ClientView clientView1 = new ClientGUI(client1);
        ClientView clientView2 = new ClientGUI(client2);

        client1.setServer(server);
        client1.setClientView(clientView1);

        client2.setServer(server);
        client2.setClientView(clientView2);
    }
}
