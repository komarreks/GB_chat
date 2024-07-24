package server.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerGUI extends JFrame implements ServerView{
    private JTextPane log = new JTextPane();

    private JButton buttonStart = new JButton("Start");
    private JButton buttonStop  = new JButton("Stop");

    private Server server;

    public ServerGUI(Server server){
        add(createChatPanel(), BorderLayout.CENTER);
        add(createStartPanel(), BorderLayout.SOUTH);

        setSize(400,400);
        setVisible(true);
        setTitle("Server");
        setLocationRelativeTo(null);

        this.server = server;

        setEnabledActionButtons(server.isWorking());

        inicializeActions();
    }

    private void inicializeActions(){
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.startStopServer(true);
                log.setText(server.getLog());
                setEnabledActionButtons(server.isWorking());
            }
        });

        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.startStopServer(false);
                setEnabledActionButtons(server.isWorking());
                log.setText("Сервер остановлен");
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (server.working){
                    server.startStopServer(false);
                    server.saveLog();
                }
                super.windowClosing(e);
            }
        });
    }

    private void setEnabledActionButtons(Boolean flag){
        buttonStart.setEnabled(!server.isWorking());
        buttonStop.setEnabled(server.isWorking());
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

    @Override
    public void showMessage(String message) {
        log.setText(log.getText() + System.lineSeparator() + message);
    }

    @Override
    public String getViewLog() {
        return log.getText();
    }
}
