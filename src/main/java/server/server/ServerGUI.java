package server.server;

import server.client.ClientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ServerGUI extends JFrame {
    private JTextPane log = new JTextPane();

    private JButton buttonStart = new JButton("Start");
    private JButton buttonStop  = new JButton("Stop");
    private Boolean isStarted = false;

    private final String LOG_FILE_NAME = "chat_log.txt";
    private java.util.List<ClientGUI> clients = new ArrayList<>();

    public ServerGUI(){
        add(createChatPanel(), BorderLayout.CENTER);
        add(createStartPanel(), BorderLayout.SOUTH);

        setSize(400,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setTitle("Server");
        setLocationRelativeTo(null);

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isStarted = true;
                try {
                    downLoadLog();
                } catch (IOException ex) {
                    log.setText("Ошибка загрузки лога чата");
                }
            }
        });

        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isStarted = false;
                try {
                    safeLog();
                } catch (IOException ex) {
                    log.setText(log.getText() + System.lineSeparator() + "ошибка записи лога");
                }
            }
        });
    }

    private JPanel createChatPanel(){
        JPanel panel = new JPanel(new GridLayout(1,1));

        log.setEditable(false);
        panel.add(log);

        JScrollPane jsp = new JScrollPane(log);
        panel.add(jsp);

        return panel;
    }

    private JPanel createStartPanel(){
        JPanel panel = new JPanel(new GridLayout(1,2));
        panel.add(buttonStart);
        panel.add(buttonStop);

        return panel;
    }

    public Boolean working(){
        return isStarted;
    }

    private void downLoadLog() throws IOException {
        File file = new File(LOG_FILE_NAME);

        if (!file.exists()){
            file.createNewFile();
        }

        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuilder sb = new StringBuilder();

        String line;
        while (true){
            line = br.readLine();

            if (line != null){
                sb.append(line + System.lineSeparator());
            }else {
                log.setText(sb.toString());
                break;
            }
        }
    }

    private void safeLog() throws IOException {
        File file = new File(LOG_FILE_NAME);

        if (!file.exists()){
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file);

        fw.write(log.getText());

        fw.flush();
    }

    public String getLog(){
        if (isStarted){
            return log.getText();
        }
        return "Сервер не запущен";
    }

    public Boolean connect(ClientGUI clientGUI){
        if (isStarted){
            clients.add(clientGUI);
            return true;
        }
        return false;
    }

    public void sendMessage(String user, String message){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy (HH:mm)");

        Date date = new Date();

        String datestr = dateFormat.format(date);

        String complexMeassage = datestr + ": " + user + ": " + message;

        log.setText(log.getText() + System.lineSeparator() + complexMeassage);

        clients.forEach(client -> {
            client.updateChat(complexMeassage);
        });
    }
}
