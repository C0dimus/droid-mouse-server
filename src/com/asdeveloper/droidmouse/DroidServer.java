package com.asdeveloper.droidmouse;

import com.asdeveloper.droidmouse.Utils.Const;
import com.asdeveloper.droidmouse.Utils.HideToTray;
import com.asdeveloper.droidmouse.Utils.Options;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;


public class DroidServer extends JFrame {
    private Image appIcon;
    private JLabel IPLabel;
    private JTextField IPTextField;
    private SpringLayout layout;
    private JButton exitButton;
    private JButton startButton;
    private JButton refreshButton;
    private Preferences prefs;
    private MySocket mySocket;
    private boolean serverStatus; 

    private void initData() {
        try {
            Const.REG_VALUE = new File(".").getCanonicalPath();
            System.out.println(Const.REG_VALUE);
        } catch (IOException ex) {
            Logger.getLogger(DroidServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        prefs = Preferences.systemNodeForPackage(com.asdeveloper.droidmouse.DroidServer.class);
        appIcon = new ImageIcon(getClass().getResource(Const.PATH_APP_ICON)).getImage();
        mySocket = new MySocket();
        serverStatus = true;
        layout = new SpringLayout();
        IPLabel= new JLabel("Twoj adres IP to:");
        IPTextField = new JTextField(20);
        IPTextField.setText(MySocket.getIP() + MySocket.getPort());
        IPTextField.setEditable(false);
        exitButton= new JButton("Wyjście");
        exitButton.setContentAreaFilled(false);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        startButton = new JButton("Stop serwer");
        startButton.setContentAreaFilled(false);
        startButton.addActionListener(new  ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Start Button " + serverStatus);
                if(serverStatus) {
                    serverStatus = false;
                    mySocket.startServer(serverStatus);
                    startButton.setText("Start serwer");
                } else {
                    serverStatus = true;
                    mySocket.startServer(serverStatus);
                    startButton.setText("Stop serwer");
                }
            }
        });

        ImageIcon refreshIcon = new ImageIcon(getClass().getResource(Const.PATH_BUTTON_REFRESH));
        refreshButton = new JButton(refreshIcon);
        refreshButton.setOpaque(true);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setBorderPainted(true);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IPTextField.setText(MySocket.getIP() + MySocket.getPort());
                System.out.println(MySocket.getIP() + MySocket.getPort());
            }
        });

    }

    private void fillLayout() {
        layout.putConstraint(SpringLayout.NORTH, IPLabel,20,SpringLayout.NORTH,this);
        layout.putConstraint(SpringLayout.WEST, IPLabel,30,SpringLayout.WEST,this);

        layout.putConstraint(SpringLayout.NORTH, IPTextField,10,SpringLayout.SOUTH,IPLabel);
        layout.putConstraint(SpringLayout.WEST, IPTextField,30,SpringLayout.WEST,this);

        layout.putConstraint(SpringLayout.NORTH, refreshButton, 10, SpringLayout.SOUTH, IPTextField);
        layout.putConstraint(SpringLayout.EAST, refreshButton, 0, SpringLayout.EAST, IPTextField);

        layout.putConstraint(SpringLayout.NORTH, startButton, 10, SpringLayout.SOUTH, IPTextField);
        layout.putConstraint(SpringLayout.WEST, startButton, 30, SpringLayout.WEST, this);

        layout.putConstraint(SpringLayout.NORTH, exitButton, 30, SpringLayout.SOUTH, startButton);
        layout.putConstraint(SpringLayout.WEST, exitButton, 30, SpringLayout.WEST, this);

        add(IPLabel);
        add(IPTextField);
        add(startButton);
        add(refreshButton);
        add(exitButton);
    }

    public DroidServer()  {      
        initData();
        setIconImage(appIcon);
        setSize(300,300);
        setResizable(false);
        setTitle("Droid Mouse Server");
        setJMenuBar(Options.createMenu());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(layout);
        fillLayout();
        setVisible(true);
        System.out.println("Server status " + serverStatus);
        mySocket.startServer(serverStatus);
        new HideToTray(this, appIcon);
        if(prefs.getBoolean(Const.RUN_MINIMALIZED, false))
            this.setState(ICONIFIED);
    }

    public static void main(String[] args) {
        new DroidServer();
    }      
}
