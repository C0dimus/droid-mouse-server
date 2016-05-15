/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.asdeveloper.droidmouse.Utils;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import oracle.jrockit.jfr.tools.ConCatRepository;

/**
 *
 * @author Adam
 */
public class Options {
    public static JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Opcje");
        menuBar.add(optionsMenu);
        
        JCheckBoxMenuItem startMinimalized = new JCheckBoxMenuItem("Uruchom zminimalizowany");
        startMinimalized.addItemListener(minimalizedListener);
        if(getPreference(Const.RUN_MINIMALIZED)) 
            startMinimalized.setSelected(true);
        optionsMenu.add(startMinimalized);
        optionsMenu.addSeparator();
        
        JCheckBoxMenuItem startWithSystem = new JCheckBoxMenuItem("Uruchom przy starcie systemu");
        startWithSystem.addItemListener(startListener);
        if(getPreference(Const.RUN_WITH_SYSTEM))
            startWithSystem.setSelected(true);
        optionsMenu.add(startWithSystem);
        optionsMenu.addSeparator();        
        return menuBar;
    }
    
    private static final ItemListener minimalizedListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                setPreference(Const.RUN_MINIMALIZED, true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                setPreference(Const.RUN_MINIMALIZED, false);
            }
        }
    };
    
    private static final ItemListener startListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                setPreference(Const.RUN_WITH_SYSTEM, true);
                try {
                    WindowsRegistry.writeStringValue(WindowsRegistry.HKEY_LOCAL_MACHINE,
                            Const.REG_PATH , Const.APP_NAME, Const.REG_VALUE);
                    System.out.println("Key added: " + Const.REG_PATH + " with value: " + Const.REG_VALUE);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                setPreference(Const.RUN_WITH_SYSTEM, false);
                try {
                    WindowsRegistry.writeStringValue(WindowsRegistry.HKEY_LOCAL_MACHINE,
                            Const.REG_PATH , Const.APP_NAME, "");
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };
    
    public static void setPreference(String key, boolean value) {
        Preferences prefs = Preferences.systemNodeForPackage(com.asdeveloper.droidmouse.DroidServer.class);
        prefs.putBoolean(key, value);
    }
    
    public static boolean getPreference(String key) {
        Preferences prefs = Preferences.systemNodeForPackage(com.asdeveloper.droidmouse.DroidServer.class);
        return prefs.getBoolean(key, false);
    }
    
}
