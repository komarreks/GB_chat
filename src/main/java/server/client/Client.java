package server.client;

import server.server.Server;

public class Client{
    private Boolean connected = false;
    private Server server;
    private ClientView clientView;
    private String name;

    public void setServer(Server server){
        this.server = server;
    }

    public void setClientView(ClientView clientView){
        this.clientView = clientView;
    }

    public Boolean connected() {
        return connected;
    }

    public Boolean connectToServer(String name) {
        this.name = name;
        connected = server.connect(this);
        return connected();
    }

    public void disconnect(){
        connected = false;
        clientView.disconnect();
    }

    public void disconnect(Boolean clientIniciator){
        server.disconnectClient(this);
        disconnect();
    }

    public void updateChat(String text) {
        clientView.updateChat(text);
    }

    public String getName(){
        return name;
    }

    public void sendMessage(String message){
        server.sendMessage(name, message);
    }

}
