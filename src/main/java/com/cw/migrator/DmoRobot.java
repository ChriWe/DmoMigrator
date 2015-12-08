package com.cw.migrator;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Christoph on 17.11.2015.
 */
public class DmoRobot {

    private Robot robot;

    public DmoRobot() throws AWTException {
        robot = new Robot();
    }

    public void executeRobot(String dmo) {

        robot.delay(5000);
        robot.setAutoDelay(10);
        robot.setAutoWaitForIdle(true);

        String initial = "t/demo dmo/";

        type(initial);
        type(dmo);
        type(KeyEvent.VK_ENTER);
        System.out.println("-- Initialize " + dmo + ".dmo");

        robot.delay(10000);
        type("t/gamespeed 1000");
        type(KeyEvent.VK_ENTER);

    }

    private void type(int i) {
        robot.delay(40);
        robot.keyPress(i);
        robot.keyRelease(i);
    }

    private void type(String s) {
        byte[] bytes = s.getBytes();
        for (byte b : bytes) {
            // Handle /
            if (b == 47) {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_7);
                robot.keyRelease(KeyEvent.VK_7);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            // Handle _
            } else if (b == 95) {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_MINUS);
                robot.keyRelease(KeyEvent.VK_MINUS);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }  else {
                int code = b;
                // keycode only handles [A-Z] (which is ASCII decimal [65-90])
                if (code > 96 && code < 123) {
                    code = code - 32;
                }
                robot.keyPress(code);
                robot.keyRelease(code);
            }
        }
    }
}
