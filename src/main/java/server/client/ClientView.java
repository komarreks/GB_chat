package server.client;

public interface ClientView {
    void sendMessage();

    void updateChat(String text);

    void connectToServer();

    void disconnect();
}
