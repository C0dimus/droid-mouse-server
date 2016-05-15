/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asdeveloper.droidmouse.AWT;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

/**
 *
 * @author Adam
 */
public class MouseControl {

    private final Robot robot;

    public MouseControl() throws AWTException {
        robot = new Robot();
    }

    private Point position() {
        return MouseInfo.getPointerInfo().getLocation();
    }

    public void move(String msg) {
        int x = position().x,y = position().y;
        int offx=0;
        int offy=0;
        if (msg.indexOf(";") == 1) {
            String msgX = msg.substring(msg.indexOf(";")+1, msg.lastIndexOf(" "));
            String msgY = msg.substring(msg.lastIndexOf(" ") + 1);
            offx = Integer.parseInt(msgX);
            offy = Integer.parseInt(msgY);
            robot.mouseMove(x+offx, y+offy);
        }
        if(msg.indexOf(":") == 1) {
            String msgY= msg.substring(2);
            offy = Integer.parseInt(msgY);
            robot.mouseWheel(offy);
        }
        if (msg.equals("!<")) {
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        }
        if (msg.equals("!<p")) {
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        }
        if(msg.equals("!<r")) {
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        }
        
        if(msg.equals("!^")) {
            robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
        }
        
        if (msg.equals("!>")) {
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        }

    }

}
