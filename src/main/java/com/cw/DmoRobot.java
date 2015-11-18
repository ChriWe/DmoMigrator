package com.cw;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Christoph on 17.11.2015.
 */
public class DmoRobot {

    private Robot robot;

    public DmoRobot() throws AWTException {
        robot = new Robot();
    }

    public void executeRobot(ArrayList<String> dmoList) {
        robot.delay(10000);
        robot.setAutoDelay(10);
        robot.setAutoWaitForIdle(true);

        String initial = "demo ";

        for (int i = 0; i < dmoList.size(); i++) {
            type(KeyEvent.VK_MINUS);
            type(initial);
            type(dmoList.get(i));
            type(KeyEvent.VK_ENTER);
        }

        System.exit(0);
    }

    public static void alt(Robot bot, int event1) {
        bot.keyPress(KeyEvent.VK_ALT);

        bot.keyPress(event1);
        bot.keyRelease(event1);

        bot.keyRelease(KeyEvent.VK_ALT);

    }

    private void type(int i) {
        robot.delay(40);
        robot.keyPress(i);
        robot.keyRelease(i);
    }

    private void type(String s) {
        byte[] bytes = s.getBytes();
        for (byte b : bytes) {
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
