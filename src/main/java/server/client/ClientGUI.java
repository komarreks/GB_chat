package server.client;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;

public class ClientGUI extends JFrame implements ClientView{

    private JFormattedTextField ipAdress = new JFormattedTextField(new MaskFormatter("###.###.###.###"));
    private JFormattedTextField port     = new JFormattedTextField(new MaskFormatter("#####"));
    private JTextField login             = new JTextField();
    private JPasswordField password      = new JPasswordField();
    private JButton buttonLogin          = new JButton("Login");

    private JTextPane chat               = new JTextPane();

    private JTextField message            = new JTextField();
    private JButton buttonSend           = new JButton("Send");

    private JPanel panelWithConnectionProperties;

    private Client client;

    public ClientGUI(Client client) throws ParseException {
        add(createNorthPanel(), BorderLayout.NORTH);
        add(createChatPanel(), BorderLayout.CENTER);
        add(createSendPanel(), BorderLayout.SOUTH);

        setSize(300,300);
        setVisible(true);
        setTitle("Client");
        setLocationRelativeTo(null);

        ipAdress.setToolTipText("ip адрес");
        port.setToolTipText("порт");
        login.setToolTipText("логин");
        password.setToolTipText("пароль");

        this.client = client;

        setVisibleConnectionProperties();

        inicializationButtonsAction();

        ipAdress.setText("127.000.000.001");
        port.setText("34000");
    }

    private void setVisibleConnectionProperties(){
        buttonSend.setEnabled(client.connected());
        panelWithConnectionProperties.setVisible(!client.connected());
    }

    private void inicializationButtonsAction(){
        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkFields()){
                    connectToServer();
                }
            }
        });

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        };

        buttonSend.addActionListener(actionListener);

        message.addActionListener(actionListener);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (client.connected()){
                    client.disconnect(true);
                }
                super.windowClosing(e);
            }
        });
    }

    public void informDisableServer(){
        chat.setText("Сервер не активен");
        buttonSend.setEnabled(false);
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
        panelWithConnectionProperties = new JPanel(new GridLayout(1,1));

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

    @Override
    public void sendMessage() {
        client.sendMessage(message.getText());
        message.setText("");
    }

    @Override
    public void updateChat(String text) {
        chat.setText(chat.getText() + System.lineSeparator() + text);
    }

    @Override
    public void connectToServer() {
        Boolean clientConnected = client.connectToServer(login.getText());

        if (clientConnected){
            setVisibleConnectionProperties();
        }else {
            chat.setText("Сервер недоступен");
        }
    }

    @Override
    public void disconnect() {
        setVisibleConnectionProperties();
    }
}
