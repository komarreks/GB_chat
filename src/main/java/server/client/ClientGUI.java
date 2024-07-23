package server.client;

import server.server.ServerGUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class ClientGUI extends JFrame {

    private JFormattedTextField ipAdress = new JFormattedTextField(new MaskFormatter("###.###.###.###"));
    private JFormattedTextField port     = new JFormattedTextField(new MaskFormatter("#####"));
    private JTextField login             = new JTextField();
    private JPasswordField password      = new JPasswordField();
    private JButton buttonLogin          = new JButton("Login");

    private JTextPane chat               = new JTextPane();

    private JTextField message            = new JTextField();
    private JButton buttonSend           = new JButton("Send");

    private Boolean connected            = false;

    private ServerGUI server;

    public ClientGUI(ServerGUI server) throws ParseException {
        add(createNorthPanel(), BorderLayout.NORTH);
        add(createChatPanel(), BorderLayout.CENTER);
        add(createSendPanel(), BorderLayout.SOUTH);

        setSize(300,300);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Client");
        setLocationRelativeTo(null);

        ipAdress.setToolTipText("ip адрес");
        port.setToolTipText("порт");
        login.setToolTipText("логин");
        password.setToolTipText("пароль");

        buttonSend.setEnabled(connected);

        this.server = server;

        inicializationButtonsAction();

        ipAdress.setText("127.000.000.001");
        port.setText("34000");
    }

    private void connectToServer(){
        server.connect(this);
    }

    private void inicializationButtonsAction(){
        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkFields()){
                    if (server.working()){
                        connectToServer();
                        server.sendMessage(login.getText(),"подключился");
                        chat.setText(server.getLog());
                        buttonSend.setEnabled(true);
                    }else {
                        informDisableServer();
                    }
                }
            }
        });

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (server.working()){
                    server.sendMessage(login.getText(), message.getText());
                    message.setText("");
                }else {
                    informDisableServer();
                }
            }
        };

        buttonSend.addActionListener(actionListener);

        message.addActionListener(actionListener);
    }

    public void informDisableServer(){
        chat.setText("Сервер не активен");
        buttonSend.setEnabled(false);
    }

    public void updateChat(String message){
        chat.setText(chat.getText() + System.lineSeparator() + message);
    }

    private Boolean checkFields(){
        if (ipAdress.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Не заполнен ip адрес");
            return false;
        }

        if (port.getText().isEmpty()){
            JOptionPane.showMessageDialog(this,"Не указан порт");
            return false;
        }

        if (login.getText().isEmpty()){
            JOptionPane.showMessageDialog(this,"Не указан логин");
            return false;
        }

        if (password.getPassword().toString().isEmpty()){
            JOptionPane.showMessageDialog(this, "Не заполнен пароль");
            return false;
        }

        return true;
    }

    private JPanel createNorthPanel(){
        JPanel panelWithConnectionProperties = new JPanel(new GridLayout(1,1));

        panelWithConnectionProperties.add(createConnectionPanel());

        return panelWithConnectionProperties;
    }

    private JPanel createConnectionPanel(){

        JPanel panelConnecionProperties = new JPanel(new GridLayout(2, 3));

        panelConnecionProperties.add(ipAdress);
        panelConnecionProperties.add(port);
        panelConnecionProperties.add(new JLabel());

        panelConnecionProperties.add(login);
        panelConnecionProperties.add(password);
        panelConnecionProperties.add(buttonLogin);

        return panelConnecionProperties;
    }

    private JPanel createChatPanel(){
        JPanel chatPanel = new JPanel(new GridLayout(1,1));

        chat.setEditable(false);
        chatPanel.add(chat);

        JScrollPane jsp = new JScrollPane(chat);
        chatPanel.add(jsp);

        return chatPanel;
    }

    private JPanel createSendPanel(){
        JPanel sendPanel = new JPanel(new GridBagLayout());

        sendPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(0,3,0,3);
        constraints.weightx = 1;
        sendPanel.add(message, constraints);

        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(0,0,0,3);
        constraints.weightx = 0;
        sendPanel.add(buttonSend, constraints);

        return sendPanel;
    }
}
