package server.server;

import server.client.Client;
import server.repository.FileRepository;
import server.repository.RepositoryInt;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server{

    Boolean working = false;
    List<Client> clients = new ArrayList<>();
    RepositoryInt repository = new FileRepository();
    ServerView serverView;

    public void setView(ServerView serverView){
        this.serverView = serverView;
    }

    public void startStopServer(Boolean flag) {
        working = flag;

        if (!flag){
            saveLog();
            sendMessage("", "сервер остановлен");

            clients.clear();
        }
    }

    public void saveLog(){
        try {
            repository.safeLog(serverView.getViewLog());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLog(){
        try {
            return repository.uploadLog();
        } catch (IOException e) {
            return "Возникла ошибка чтения лога: " + e.getMessage();
        }
    }

    public Boolean connect(Client client) {
        if (working){
            clients.add(client);

            client.updateChat(serverView.getViewLog());

            sendMessage(client.getName(), "подключился");

            return true;
        }

        return false;
    }

    public Boolean isWorking(){
        return working;
    }

    public void sendMessage(String user, String message) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy (HH:mm)");

        Date date = new Date();

        String datestr = dateFormat.format(date);

        String complexMeassage = datestr + ": " + user + ": " + message;

        serverView.showMessage(complexMeassage);

        clients.forEach(client -> {
            client.updateChat(complexMeassage);
            if (!working){
                client.disconnect();
            }
        });
    }

    public void disconnectClient(Client client){
        sendMessage(client.getName(), "отключился");
        clients.remove(client);
    }
}
