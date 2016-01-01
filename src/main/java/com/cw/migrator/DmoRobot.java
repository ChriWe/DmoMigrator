package com.cw.migrator;

import com.cw.utils.SbConfig;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Christoph on 17.11.2015.
 */
public class DmoRobot {

    private final SbConfig sbConfig;
    private final Robot robot;

    private DmoRobot(SbConfig sbConfig) throws AWTException {
        this.sbConfig = sbConfig;
        this.robot = new Robot();
    }

    public boolean execute(String dmo) {
        try {
            robot.delay(sbConfig.getRobotStartDelay());
            robot.setAutoDelay(10);
            robot.setAutoWaitForIdle(true);

            String initial = "t/demo dmo/";

            type(initial);
            type(dmo);
            type(KeyEvent.VK_ENTER);
            System.out.println("-- Initialize " + dmo + ".dmo");

            robot.delay(sbConfig.getRobotGamespeedDelay());
            type("t/gamespeed 1000");
            type(KeyEvent.VK_ENTER);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
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

    public static class Maker {

        public DmoRobot make(SbConfig sbConfig) {
            try {
                return new DmoRobot(sbConfig);
            } catch (AWTException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
}
