/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asdeveloper.droidmouse.Utils;

import com.asdeveloper.droidmouse.MySocket;
import java.awt.AWTException;
import static java.awt.Frame.ICONIFIED;
import static java.awt.Frame.MAXIMIZED_BOTH;
import static java.awt.Frame.NORMAL;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author Adam
 */
public class HideToTray {

    private static final String LOOP_BACK = "127.0.0.1";
    private TrayIcon trayIcon;
    private SystemTray tray;
    private JFrame frame;
    private String ip;

    public HideToTray(JFrame myFrame, final Image image) {
        System.out.println("creating instance");
        ip = "";
        setTimerForIPChange();
        frame = myFrame;
       
        
        try {
            System.out.println("setting look and feel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Unable to set LookAndFeel");
        }
        if (SystemTray.isSupported()) {
            System.out.println("system tray supported");
            tray = SystemTray.getSystemTray();
            PopupMenu popup = new PopupMenu();
            MenuItem openItem = new MenuItem("Otwórz");
            MenuItem exitItem = new MenuItem("Zamknij");
            openItem.addActionListener(openListener);
            exitItem.addActionListener(exitListener);
            popup.add(openItem);
            popup.add(exitItem);

            trayIcon = new TrayIcon(image, "Droid Mouse Server", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(trayMouseListener);
        } else {
            System.out.println("system tray not supported");
        }
        frame.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == ICONIFIED) {
                    try {
                        tray.add(trayIcon);
                        frame.setVisible(false);
                        System.out.println("added to SystemTray");
                    } catch (AWTException ex) {
                        System.out.println("unable to add to tray");
                    }
                }
                if (e.getNewState() == 7) {
                    try {
                        tray.add(trayIcon);
                        frame.setVisible(false);
                        System.out.println("added to SystemTray");
                    } catch (AWTException ex) {
                        System.out.println("unable to add to system tray");
                    }
                }
                if (e.getNewState() == MAXIMIZED_BOTH) {
                    tray.remove(trayIcon);
                    frame.setVisible(true);
                    System.out.println("Tray icon removed");
                }
                if (e.getNewState() == NORMAL) {
                    tray.remove(trayIcon);
                    frame.setVisible(true);
                    System.out.println("Tray icon removed");
                }
            }
        });

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    ActionListener exitListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Exiting....");
            System.exit(0);
        }
    };

    ActionListener openListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.setVisible(true);
            frame.setExtendedState(JFrame.NORMAL);
        }
    };

    MouseListener trayMouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
                frame.setVisible(true);
                frame.setExtendedState(JFrame.NORMAL);
            }
        }        
    };
    
    private void setTimerForIPChange() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!MySocket.getIP().equals(ip)) {
                   ip = MySocket.getIP();
                   if(!ip.equals(LOOP_BACK))
                        trayIcon.displayMessage("Obecny adres IP:", ip + MySocket.getPort(), TrayIcon.MessageType.INFO);
                   else 
                       trayIcon.displayMessage("Brak połączenia!", "Sprawdź ustawienia sieci", TrayIcon.MessageType.ERROR);
                }
            }
        }, 500, 10000);
    }
}
