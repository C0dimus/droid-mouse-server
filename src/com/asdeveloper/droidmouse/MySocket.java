/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asdeveloper.droidmouse;

import com.asdeveloper.droidmouse.AWT.KeyboardControl;
import com.asdeveloper.droidmouse.AWT.MouseControl;
import com.asdeveloper.droidmouse.Utils.Const;
import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adam
 */
public class MySocket {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static InputStreamReader inputStreamReader;
    private static BufferedReader bufferedReader;
    private static String message;
    private MouseControl myszka;
    private KeyboardControl klawiatura;
    private boolean serverStatus;
    private static int port;

    public MySocket() {
        serverStatus = true;
        port = Const.DEFAULT_PORT;
        do {
            try {
                serverSocket = new ServerSocket(port);    
            } catch (IOException e) {
                port++;
            }
        }while(serverSocket == null);
        try {
            myszka = new MouseControl();
            klawiatura = new KeyboardControl();
        } catch (AWTException ex) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
        }       
        System.out.println("port: " + port);
    }
    
    public void startServer(boolean status) {
        serverStatus = status;
        new Thread(new Runnable() {
            @Override
            public void run() {
               serverListening();
            }
        }).start();
    }

    public static String getIP() {
        try {
            String cutIP = InetAddress.getLocalHost().toString();
            return cutIP.substring(cutIP.indexOf("/") + 1);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
            return "Nieznany host!";
        }
    }
    
    public static String getPort() {
        if(port == Const.DEFAULT_PORT)
            return "";
        else
            return String.format(":%d", port);
    }

    private void serverListening() {
        while (serverStatus) {
            try {
                clientSocket = serverSocket.accept();
                inputStreamReader = new InputStreamReader(clientSocket.getInputStream(), "UTF-8");
                bufferedReader = new BufferedReader(inputStreamReader);
                message = bufferedReader.readLine();
                System.out.println(message);
                if (message == null) {
                    message = "error";
                }
                if (message.indexOf("!") == 0) {
                    myszka.move(message);
                }
                if (message.indexOf("@") == 0) {
                    message = message.substring(message.indexOf("@") + 1);
                    klawiatura.type(message);
                } else if (message.indexOf("#") == 0) {
                    message = message.substring(message.indexOf("#") + 1);
                    klawiatura.typeSpecial(message);
                    if(message.equals("close"))
                        System.exit(0);
                }
                inputStreamReader.close();
                clientSocket.close();

            } catch (IOException ex) {
                System.out.println("Problem in message reading");
            }
        }
    }

}
